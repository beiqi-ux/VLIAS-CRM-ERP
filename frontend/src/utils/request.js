import axios from 'axios'
import { ElMessage } from 'element-plus'
import { secureConsole, maskSensitiveData } from '@/utils/security'

// 防止重复跳转登录页的标志
let isRedirecting = false

// 请求缓存Map，用于防止重复请求 - 简化版本
const requestCache = new Map()

// 生成请求的唯一标识
function generateRequestKey(config) {
  const { method, url, params, data } = config
  return [method, url, JSON.stringify(params), JSON.stringify(data)].join('&')
}

// 检查是否为重复请求
function isDuplicateRequest(config) {
  const requestKey = generateRequestKey(config)
  const now = Date.now()
  
  // 重要接口不进行去重（避免影响正常功能）
  const skipDedupeUrls = [
    '/api/auth/info',
    '/api/auth/login',
    '/api/files/avatar'
  ]
  
  const shouldSkip = skipDedupeUrls.some(url => config.url && config.url.includes(url))
  
  if (shouldSkip) {
    secureConsole.log('跳过重要接口的去重检查:', config.url)
    return false // 重要接口不去重
  }
  
  // 只对特定的高频接口进行去重
  const needDedupeUrls = [
    '/api/menu/user-tree',
    '/api/auth/validate'
  ]
  
  const shouldDedupe = needDedupeUrls.some(url => config.url && config.url.includes(url))
  
  if (!shouldDedupe) {
    return false // 不需要去重的接口直接通过
  }
  
  // 检查缓存中是否有相同请求在500毫秒内发起过
  if (requestCache.has(requestKey)) {
    const lastRequestTime = requestCache.get(requestKey)
    if (now - lastRequestTime < 500) {
      secureConsole.log('检测到重复请求，已跳过:', config.url)
      return true
    }
  }
  
  // 记录当前请求时间
  requestCache.set(requestKey, now)
  
  // 清理过期的缓存（超过3秒的）
  for (const [key, timestamp] of requestCache.entries()) {
    if (now - timestamp > 3000) {
      requestCache.delete(key)
    }
  }
  
  return false
}

// 创建axios实例
const request = axios.create({
  baseURL: '', // 使用相对路径，配合vite代理
  timeout: 15000 // 请求超时时间
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 添加请求去重
    if (isDuplicateRequest(config)) {
      return Promise.reject(new axios.Cancel('重复请求已取消'))
    }
    
    // 从localStorage中获取token，并添加到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    // 添加调试日志
    // 脱敏处理请求数据
  const maskedData = maskSensitiveData(config.params || config.data)
  secureConsole.log('发送请求:', config.method.toUpperCase(), config.url, maskedData)
    
    return config
  },
  error => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

// 清除用户认证信息
function clearAuthInfo() {
  localStorage.removeItem('token')
  
  // 尝试清除Pinia中的用户信息（如果可以访问到）
  try {
    // 动态导入并清除用户store中的信息
    import('@/stores/user').then(({ useUserStore }) => {
      const userStore = useUserStore()
      userStore.clearToken()
      userStore.userInfo = {}
    }).catch(() => {
      // 如果导入失败，忽略错误（在某些环境下可能无法访问）
      secureConsole.log('无法访问用户store，仅清除localStorage')
    })
  } catch (error) {
          secureConsole.log('清除用户store信息时出错，仅清除localStorage')
  }
}

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 移除已完成的请求
    // 由于去重机制的简化，这里不再需要移除pendingRequests
    
    // 添加调试日志
    // 脱敏处理响应数据
  const maskedResponse = maskSensitiveData(response.data)
  secureConsole.log('接收响应:', response.config.url, maskedResponse)
    
    // 直接返回响应数据
    return response.data
  },
  error => {
    // 移除已完成的请求
    // 由于去重机制的简化，这里不再需要移除pendingRequests
    
    // 如果是取消的请求，直接返回
    if (axios.isCancel(error)) {
      secureConsole.log('请求被取消:', error.message)
      return Promise.reject(error)
    }
    
    // 处理HTTP错误
    let message = '发生未知错误'
    if (error.response) {
      console.error('响应错误:', error.response.status, error.response.data)
      
      switch (error.response.status) {
      case 400:
        message = '请求错误'
        break
      case 401:
        message = '未授权，请重新登录'
          
        // 清除所有认证信息
        clearAuthInfo()
          
        // 防止重复跳转
        if (!isRedirecting) {
          isRedirecting = true
            
          // 延迟跳转，避免与其他导航冲突
          setTimeout(() => {
            // 优先使用router跳转，如果不可用则使用window.location
            if (window.__VUE_ROUTER__) {
              window.__VUE_ROUTER__.replace('/login')
            } else {
              window.location.href = '/login'
            }
              
            // 重置跳转标志
            setTimeout(() => {
              isRedirecting = false
            }, 1000)
          }, 1500)
        }
        break
      case 403:
        message = '拒绝访问'
        break
      case 404:
        message = '请求的资源不存在'
        break
      case 500:
        message = '服务器内部错误'
        break
      default:
        message = `错误 ${error.response.status}: ${error.response.statusText}`
      }
    } else if (error.request) {
      console.error('网络错误:', error.request)
      message = '网络连接失败，请检查网络'
    } else {
      console.error('请求配置错误:', error.message)
      message = '请求配置错误'
    }
    
    // 显示错误消息（避免重复显示相同错误）
    if (!axios.isCancel(error)) {
      ElMessage.error(message)
    }
    
    return Promise.reject(error)
  }
)

export default request 