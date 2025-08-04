/**
 * 统一错误处理工具
 * 提供错误分类、重试机制、用户友好提示
 */

import { ref } from 'vue'
import { ElMessage, ElNotification, ElMessageBox } from 'element-plus'

// 错误类型定义
export const ERROR_TYPES = {
  NETWORK: 'network',        // 网络错误
  AUTH: 'auth',             // 认证错误
  PERMISSION: 'permission',  // 权限错误
  VALIDATION: 'validation',  // 验证错误
  SERVER: 'server',         // 服务器错误
  TIMEOUT: 'timeout',       // 超时错误
  UNKNOWN: 'unknown'        // 未知错误
}

// 错误消息映射
const ERROR_MESSAGES = {
  [ERROR_TYPES.NETWORK]: '网络连接失败，请检查网络设置',
  [ERROR_TYPES.AUTH]: '登录已过期，请重新登录',
  [ERROR_TYPES.PERMISSION]: '您没有执行此操作的权限',
  [ERROR_TYPES.VALIDATION]: '数据验证失败，请检查输入',
  [ERROR_TYPES.SERVER]: '服务器内部错误，请稍后重试',
  [ERROR_TYPES.TIMEOUT]: '请求超时，请稍后重试',
  [ERROR_TYPES.UNKNOWN]: '未知错误，请联系管理员'
}

// 错误分类函数
export function classifyError(error) {
  if (!error) return ERROR_TYPES.UNKNOWN
  
  // 网络错误
  if (error.code === 'NETWORK_ERROR' || error.message?.includes('Network Error')) {
    return ERROR_TYPES.NETWORK
  }
  
  // 超时错误
  if (error.code === 'ECONNABORTED' || error.message?.includes('timeout')) {
    return ERROR_TYPES.TIMEOUT
  }
  
  // HTTP状态码错误
  if (error.response) {
    const status = error.response.status
    
    switch (status) {
    case 401:
      return ERROR_TYPES.AUTH
    case 403:
      return ERROR_TYPES.PERMISSION
    case 400:
    case 422:
      return ERROR_TYPES.VALIDATION
    case 500:
    case 502:
    case 503:
    case 504:
      return ERROR_TYPES.SERVER
    default:
      return ERROR_TYPES.UNKNOWN
    }
  }
  
  return ERROR_TYPES.UNKNOWN
}

// 获取用户友好的错误消息
export function getUserFriendlyMessage(error, customMessages = {}) {
  const errorType = classifyError(error)
  
  // 优先使用自定义消息
  if (customMessages[errorType]) {
    return customMessages[errorType]
  }
  
  // 使用后端返回的消息（如果存在且友好）
  if (error.response?.data?.message && !error.response.data.message.includes('Exception')) {
    return error.response.data.message
  }
  
  // 使用默认消息
  return ERROR_MESSAGES[errorType] || ERROR_MESSAGES[ERROR_TYPES.UNKNOWN]
}

// 重试配置
const DEFAULT_RETRY_CONFIG = {
  maxRetries: 3,
  retryDelay: 1000,
  retryDelayMultiplier: 2,
  retryableErrors: [ERROR_TYPES.NETWORK, ERROR_TYPES.TIMEOUT, ERROR_TYPES.SERVER]
}

// 重试函数
export async function withRetry(fn, options = {}) {
  const config = { ...DEFAULT_RETRY_CONFIG, ...options }
  let lastError = null
  
  for (let attempt = 0; attempt <= config.maxRetries; attempt++) {
    try {
      const result = await fn(attempt)
      
      // 如果是重试成功，显示提示
      if (attempt > 0) {
        console.log(`重试成功，尝试次数: ${attempt}`)
        ElMessage.success('网络已恢复，操作成功')
      }
      
      return result
    } catch (error) {
      lastError = error
      const errorType = classifyError(error)
      
      // 检查是否应该重试
      const shouldRetry = attempt < config.maxRetries && 
                         config.retryableErrors.includes(errorType)
      
      if (!shouldRetry) {
        break
      }
      
      // 计算延迟时间
      const delay = config.retryDelay * Math.pow(config.retryDelayMultiplier, attempt)
      
      console.warn(`请求失败，${delay}ms后进行第${attempt + 1}次重试:`, error.message)
      
      // 等待后重试
      await new Promise(resolve => setTimeout(resolve, delay))
    }
  }
  
  throw lastError
}

// 错误处理器类
export class ErrorHandler {
  constructor(options = {}) {
    this.options = {
      showMessage: true,
      showNotification: false,
      logErrors: true,
      customMessages: {},
      ...options
    }
  }
  
  // 处理错误
  handle(error, context = '') {
    const errorType = classifyError(error)
    const message = getUserFriendlyMessage(error, this.options.customMessages)
    
    // 记录错误日志
    if (this.options.logErrors) {
      console.error(`[ErrorHandler] ${context}:`, {
        type: errorType,
        message: error.message,
        stack: error.stack,
        response: error.response?.data
      })
    }
    
    // 显示用户提示
    this.showUserFeedback(errorType, message)
    
    return {
      type: errorType,
      message,
      originalError: error
    }
  }
  
  // 显示用户反馈
  showUserFeedback(errorType, message) {
    if (errorType === ERROR_TYPES.AUTH) {
      // 认证错误 - 显示确认对话框
      ElMessageBox.confirm(
        '登录状态已过期，是否重新登录？',
        '提示',
        {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        // 清除token并跳转到登录页
        localStorage.removeItem('token')
        window.location.href = '/login'
      }).catch(() => {
        // 用户取消
      })
      return
    }
    
    if (this.options.showNotification) {
      // 显示通知
      ElNotification({
        title: '错误提示',
        message,
        type: 'error',
        duration: 5000
      })
    } else if (this.options.showMessage) {
      // 显示消息
      ElMessage.error(message)
    }
  }
  
  // 批量处理错误
  handleBatch(errors, context = '') {
    const results = errors.map((error, index) => 
      this.handle(error, `${context}[${index}]`)
    )
    
    // 统计错误类型
    const errorStats = results.reduce((stats, result) => {
      stats[result.type] = (stats[result.type] || 0) + 1
      return stats
    }, {})
    
    console.log('批量错误处理完成，错误统计:', errorStats)
    
    return results
  }
}

// 创建默认错误处理器实例
export const defaultErrorHandler = new ErrorHandler()

// 便捷函数
export function handleError(error, context = '', options = {}) {
  const handler = new ErrorHandler(options)
  return handler.handle(error, context)
}

// 带重试的请求包装器
export async function requestWithRetry(requestFn, options = {}) {
  const { errorHandler = defaultErrorHandler, retryConfig = {}, context = '' } = options
  
  try {
    return await withRetry(requestFn, retryConfig)
  } catch (error) {
    return errorHandler.handle(error, context)
  }
}

// 错误边界组合式函数
export function useErrorBoundary(options = {}) {
  const errorHandler = new ErrorHandler(options)
  
  // 错误状态
  const hasError = ref(false)
  const errorInfo = ref(null)
  
  // 错误处理函数
  const handleError = (error, context = '') => {
    hasError.value = true
    errorInfo.value = errorHandler.handle(error, context)
    return errorInfo.value
  }
  
  // 清除错误
  const clearError = () => {
    hasError.value = false
    errorInfo.value = null
  }
  
  // 重试函数
  const retry = async (fn) => {
    clearError()
    try {
      const result = await fn()
      return result
    } catch (error) {
      handleError(error, 'retry')
      throw error
    }
  }
  
  return {
    hasError,
    errorInfo,
    handleError,
    clearError,
    retry
  }
} 