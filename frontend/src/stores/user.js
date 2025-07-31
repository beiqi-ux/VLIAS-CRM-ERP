import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as apiLogin, getUserInfo, logout as apiLogout } from '@/api/auth'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref({})
  const isLoggedIn = computed(() => !!token.value)
  
  // 设置token
  function setToken(newToken) {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }
  
  // 清除token
  function clearToken() {
    token.value = ''
    localStorage.removeItem('token')
  }
  
  // 设置用户信息
  function setUserInfo(info) {
    userInfo.value = info
  }
  
  // 更新用户头像
  function updateUserAvatar(avatarUrl) {
    if (userInfo.value) {
      userInfo.value.avatar = avatarUrl
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
    try {
      const response = await getUserInfo()
      if (response.success) {
        setUserInfo(response.data)
        return response
      } else {
        clearToken()
        return false
      }
    } catch (error) {
      clearToken()
      return false
    }
  }
  
  // 登出
  async function logout() {
    try {
      await apiLogout()
    } catch (error) {
      console.error('登出错误', error)
    } finally {
      clearToken()
      userInfo.value = {}
    }
  }
  
  return {
    token,
    userInfo,
    isLoggedIn,
    login,
    logout,
    fetchUserInfo,
    updateUserAvatar
  }
}) 