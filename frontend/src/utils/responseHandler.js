/**
 * 响应处理工具函数
 * 处理后端的两种响应格式：
 * 1. ApiResponse: { success: boolean, message: string, data: any }
 * 2. Result: { code: number, message: string, data: any }
 */

/**
 * 检查响应是否成功
 * @param {Object} response - axios响应对象
 * @returns {boolean} 是否成功
 */
export function isResponseSuccess(response) {
  if (!response || !response.data) {
    return false
  }

  const data = response.data
  
  // ApiResponse格式：检查success字段
  if (typeof data.success === 'boolean') {
    return data.success
  }
  
  // Result格式：检查code字段
  if (typeof data.code === 'number') {
    return data.code === 200
  }
  
  return false
}

/**
 * 获取响应数据
 * @param {Object} response - axios响应对象
 * @returns {any} 响应数据
 */
export function getResponseData(response) {
  if (!response || !response.data) {
    return null
  }
  
  return response.data.data
}

/**
 * 获取响应消息
 * @param {Object} response - axios响应对象
 * @returns {string} 响应消息
 */
export function getResponseMessage(response) {
  if (!response || !response.data) {
    return '请求失败'
  }
  
  return response.data.message || '操作失败'
}

/**
 * 处理分页数据
 * @param {Object} response - axios响应对象
 * @returns {Object} 包含列表数据和总数的对象
 */
export function handlePageResponse(response) {
  if (!isResponseSuccess(response)) {
    return {
      content: [],
      totalElements: 0
    }
  }
  
  const data = getResponseData(response)
  
  // 如果是分页对象
  if (data && typeof data === 'object' && Array.isArray(data.content)) {
    return {
      content: data.content,
      totalElements: data.totalElements || 0
    }
  }
  
  // 如果是数组
  if (Array.isArray(data)) {
    return {
      content: data,
      totalElements: data.length
    }
  }
  
  return {
    content: [],
    totalElements: 0
  }
} 