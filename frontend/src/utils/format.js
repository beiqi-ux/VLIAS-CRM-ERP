/**
 * 格式化日期时间
 * @param {string|Date} dateTime - 日期时间
 * @returns {string} - 格式化后的日期时间字符串
 */
export function formatDateTime(dateTime) {
  if (!dateTime) return '-'
  
  try {
    // 处理不同格式的日期时间
    let date
    if (typeof dateTime === 'string') {
      // 如果是字符串，直接解析
      date = new Date(dateTime)
    } else if (dateTime instanceof Date) {
      date = dateTime
    } else {
      // 处理可能的对象格式（如果后端返回的是对象）
      return '-'
    }
    
    // 检查日期是否有效
    if (isNaN(date.getTime())) {
      console.warn('Invalid date:', dateTime)
      return '-'
    }
    
    return date.toLocaleString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit',
      hour12: false
    })
  } catch (error) {
    console.error('Date formatting error:', error, dateTime)
    return '-'
  }
}

/**
 * 格式化日期
 * @param {string|Date} date - 日期
 * @returns {string} - 格式化后的日期字符串
 */
export function formatDate(date) {
  if (!date) return '-'
  
  try {
    // 处理不同格式的日期
    let dateObj
    if (typeof date === 'string') {
      dateObj = new Date(date)
    } else if (date instanceof Date) {
      dateObj = date
    } else {
      return '-'
    }
    
    // 检查日期是否有效
    if (isNaN(dateObj.getTime())) {
      console.warn('Invalid date:', date)
      return '-'
    }
    
    return dateObj.toLocaleDateString('zh-CN', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit'
    })
  } catch (error) {
    console.error('Date formatting error:', error, date)
    return '-'
  }
}

/**
 * 格式化ID为四位数显示（如：0001）
 * @param {number|string} id - 原始ID
 * @returns {string} - 格式化后的ID字符串
 */
export function formatId(id) {
  if (id === undefined || id === null) return '-'
  return String(id).padStart(4, '0')
}

/**
 * 处理图片URL，确保可以正确访问
 * @param {string} url - 图片URL
 * @param {string} defaultUrl - 默认图片URL
 * @returns {string} - 处理后的图片URL
 */
export function formatImageUrl(url, defaultUrl = '/default-avatar.png') {
  if (!url) return defaultUrl
  if (url.startsWith('http')) return url
  
  // 直接使用相对路径，让代理配置处理
  // 添加随机参数防止缓存
  const timestamp = new Date().getTime()
  return `${url}?t=${timestamp}`
} 