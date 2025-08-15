import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin, getUserInfo, logout as apiLogout } from '@/api/auth'
import { ElMessage } from 'element-plus'
import { securityLog } from '@/utils/security'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref({})
  const permissionsLoaded = ref(false) // 权限数据是否已加载
  const isLoggedIn = computed(() => !!token.value)
  
  // 请求状态管理，防止重复请求
  const isFetchingUserInfo = ref(false)
  const isValidatingToken = ref(false)
  
  // 新增：用户信息缓存管理
  const userInfoCache = ref({
    timestamp: 0,
    ttl: 15 * 60 * 1000, // 默认15分钟过期
    version: '1.0.0',
    isAutoRefreshing: false // 是否正在后台自动刷新
  })
  
  // 检查用户信息缓存是否有效
  function isUserInfoCacheValid() {
    if (!permissionsLoaded.value || !userInfo.value.id) {
      return false
    }
    
    const now = Date.now()
    const cacheAge = now - userInfoCache.value.timestamp
    return cacheAge < userInfoCache.value.ttl
  }
  
  // 获取缓存剩余时间
  function getCacheRemainingTime() {
    if (!isUserInfoCacheValid()) return 0
    const elapsed = Date.now() - userInfoCache.value.timestamp
    return Math.max(0, userInfoCache.value.ttl - elapsed)
  }
  
  // 更新用户信息缓存时间戳
  function updateCacheTimestamp() {
    userInfoCache.value.timestamp = Date.now()
    securityLog('用户store', '缓存更新', { ttl: userInfoCache.value.ttl / 1000 })
  }
  
  // 设置token
  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }
  
  // 清除token
  function clearToken() {
    token.value = ''
    localStorage.removeItem('token')
    userInfo.value = {}
    permissionsLoaded.value = false // 重置权限加载状态
    isFetchingUserInfo.value = false // 重置请求状态
    isValidatingToken.value = false
    
    // 清除缓存时间戳
    userInfoCache.value.timestamp = 0
    userInfoCache.value.isAutoRefreshing = false
    securityLog('用户store', '已清除用户信息缓存')
  }
  
  // 设置用户信息
  function setUserInfo(info) {
    userInfo.value = info
    permissionsLoaded.value = true // 标记权限已加载
    updateCacheTimestamp() // 更新缓存时间戳
  }
  
  // 更新用户头像
  function updateUserAvatar(avatarUrl) {
    if (userInfo.value) {
      userInfo.value.avatar = avatarUrl
      // 头像更新不影响缓存过期时间，但需要标记缓存仍然有效
      if (permissionsLoaded.value) {
        updateCacheTimestamp()
      }
    }
  }
  
  // 验证token有效性
  async function validateToken() {
    // 如果没有token，直接返回false
    if (!token.value) {
      return false
    }
    
    // 防止重复验证
    if (isValidatingToken.value) {
      securityLog('用户store', '跳过重复token验证请求')
      return true
    }
    
    // 如果用户信息缓存仍然有效，直接返回true
    if (isUserInfoCacheValid()) {
      securityLog('用户store', '用户信息缓存有效', { remainingTime: Math.round(getCacheRemainingTime() / 1000) })
      return true
    }
    
    isValidatingToken.value = true
    
    try {
      securityLog('用户store', '开始验证token')
      
      // 尝试获取用户信息来验证token
      const response = await getUserInfo()
      if (response && response.success) {
        setUserInfo(response.data)
        securityLog('用户store', 'Token验证成功')
        return true
      } else {
        // token无效，清除本地存储
        securityLog('用户store', 'Token验证失败，清除认证信息')
        clearToken()
        return false
      }
    } catch (error) {
      // 请求失败，可能是token过期或网络问题
      securityLog('用户store', 'Token验证请求失败', { error: error.message })
      clearToken()
      return false
    } finally {
      isValidatingToken.value = false
    }
  }
  
  // 登录
  async function login(credentials) {
    try {
      const response = await apiLogin(credentials)
      if (response.success) {
        setToken(response.data.token)
        setUserInfo(response.data)
        return true
      } else {
        ElMessage.error(response.message || '登录失败')
        return false
      }
    } catch (error) {
      ElMessage.error(error.message || '登录失败')
      return false
    }
  }
  
  // 获取用户信息
  async function fetchUserInfo(forceRefresh = false) {
    // 如果不强制刷新且缓存有效，直接返回缓存
    if (!forceRefresh && isUserInfoCacheValid()) {
      const remainingTime = getCacheRemainingTime()
      securityLog('用户store', '使用用户信息缓存', { remainingTime: Math.round(remainingTime / 1000) })
      
      // 如果缓存即将过期（少于5分钟），启动后台刷新
      const shouldAutoRefresh = remainingTime < 5 * 60 * 1000 && !userInfoCache.value.isAutoRefreshing
      if (shouldAutoRefresh) {
        userInfoCache.value.isAutoRefreshing = true
        
        // 后台异步刷新，不阻塞当前调用
        setTimeout(async () => {
          try {
            await fetchUserInfo(true) // 强制刷新
            securityLog('用户store', '后台自动刷新用户信息完成')
          } catch (error) {
            console.warn('后台自动刷新失败:', error)
          } finally {
            userInfoCache.value.isAutoRefreshing = false
          }
        }, 0)
      }
      
      return { success: true, data: userInfo.value }
    }
    
    // 防止重复请求
    if (isFetchingUserInfo.value) {
      securityLog('用户store', '正在获取用户信息，等待当前请求完成')
      // 等待当前请求完成，而不是直接返回缓存
      while (isFetchingUserInfo.value) {
        await new Promise(resolve => setTimeout(resolve, 100))
      }
      return { success: true, data: userInfo.value }
    }
    
    isFetchingUserInfo.value = true
    
    try {
      if (forceRefresh) {
        securityLog('用户store', '强制刷新用户信息')
      } else {
        securityLog('用户store', '获取用户信息')
      }
      
      // 仅在非后台刷新时重置权限加载状态
      if (!userInfoCache.value.isAutoRefreshing) {
        permissionsLoaded.value = false
      }
      
      const response = await getUserInfo()
      securityLog('用户store', 'getUserInfo API调用', { success: !!response?.success })
      
      if (response && response.success) {
        setUserInfo(response.data)
        securityLog('用户store', '用户信息设置成功', { userId: response.data?.userId })
        return response
      } else {
        console.error('用户store：API返回失败:', response)
        clearToken()
        return false
      }
    } catch (error) {
      console.error('用户store：获取用户信息失败:', error)
      
      // 如果是401错误，说明token已过期
      if (error.response && error.response.status === 401) {
        securityLog('用户store', 'Token已过期，清除认证信息')
        clearToken()
      }
      
      return false
    } finally {
      isFetchingUserInfo.value = false
    }
  }
  
  // 登出
  async function logout() {
    try {
      await apiLogout()
    } catch (error) {
      console.error('登出请求失败:', error)
    } finally {
      // 无论请求是否成功，都清除本地信息
      clearToken()
    }
  }
  
  // 新增：设置缓存过期时间
  function setCacheTTL(ttl) {
    userInfoCache.value.ttl = ttl
    securityLog('用户store', '缓存过期时间设置', { ttl: ttl / 1000 })
  }
  
  // 新增：获取缓存状态
  function getCacheStatus() {
    return {
      isValid: isUserInfoCacheValid(),
      remainingTime: getCacheRemainingTime(),
      isAutoRefreshing: userInfoCache.value.isAutoRefreshing,
      ttl: userInfoCache.value.ttl,
      timestamp: userInfoCache.value.timestamp,
      version: userInfoCache.value.version
    }
  }
  
  // 新增：强制刷新用户信息（便捷方法）
  async function refreshUserInfo() {
    securityLog('用户store', '手动刷新用户信息')
    return await fetchUserInfo(true)
  }

  return {
    // 状态
    token,
    userInfo,
    permissionsLoaded,
    isLoggedIn,
    isFetchingUserInfo,
    isValidatingToken,
    
    // 新增：缓存相关状态
    userInfoCache: computed(() => userInfoCache.value),
    
    // 方法
    setToken,
    clearToken,
    setUserInfo,
    updateUserAvatar,
    validateToken,
    login,
    fetchUserInfo,
    logout,
    
    // 新增：缓存管理方法
    isUserInfoCacheValid,
    getCacheRemainingTime,
    setCacheTTL,
    getCacheStatus,
    refreshUserInfo
  }
}) 