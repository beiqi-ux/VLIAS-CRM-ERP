import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin, getUserInfo, logout as apiLogout } from '@/api/auth'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref({})
  const permissionsLoaded = ref(false) // 权限数据是否已加载
  const isLoggedIn = computed(() => !!token.value)
  
  // 请求状态管理，防止重复请求
  const isFetchingUserInfo = ref(false)
  const isValidatingToken = ref(false)
  
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
  }
  
  // 设置用户信息
  function setUserInfo(info) {
    userInfo.value = info
    permissionsLoaded.value = true // 标记权限已加载
  }
  
  // 更新用户头像
  function updateUserAvatar(avatarUrl) {
    if (userInfo.value) {
      userInfo.value.avatar = avatarUrl
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
      console.log('正在验证token，跳过重复请求')
      return true
    }
    
    // 如果权限已经加载过，直接返回true
    if (permissionsLoaded.value) {
      console.log('权限已加载，跳过token验证')
      return true
    }
    
    isValidatingToken.value = true
    
    try {
      console.log('开始验证token...')
      
      // 尝试获取用户信息来验证token
      const response = await getUserInfo()
      if (response && response.success) {
        setUserInfo(response.data)
        console.log('Token验证成功')
        return true
      } else {
        // token无效，清除本地存储
        console.log('Token验证失败，清除认证信息')
        clearToken()
        return false
      }
    } catch (error) {
      // 请求失败，可能是token过期或网络问题
      console.log('Token验证请求失败:', error.message)
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
  async function fetchUserInfo() {
    // 防止重复请求
    if (isFetchingUserInfo.value) {
      console.log('正在获取用户信息，等待当前请求完成')
      // 等待当前请求完成，而不是直接返回缓存
      while (isFetchingUserInfo.value) {
        await new Promise(resolve => setTimeout(resolve, 100))
      }
      return { success: true, data: userInfo.value }
    }
    
    isFetchingUserInfo.value = true
    
    try {
      console.log('用户store获取用户信息...')
      
      // 重置权限加载状态
      permissionsLoaded.value = false
      
      const response = await getUserInfo()
      console.log('用户store getUserInfo API响应:', response)
      
      if (response && response.success) {
        setUserInfo(response.data)
        console.log('用户store：用户信息设置成功:', response.data)
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
        console.log('Token已过期，清除认证信息')
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
  
  return {
    // 状态
    token,
    userInfo,
    permissionsLoaded,
    isLoggedIn,
    isFetchingUserInfo,
    isValidatingToken,
    
    // 方法
    setToken,
    clearToken,
    setUserInfo,
    updateUserAvatar,
    validateToken,
    login,
    fetchUserInfo,
    logout
  }
}) 