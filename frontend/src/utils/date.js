/**
 * 日期工具函数
 */

/**
 * 格式化日期
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @param {string} format - 格式字符串，默认 'YYYY-MM-DD HH:mm:ss'
 * @returns {string} 格式化后的日期字符串
 */
export function formatDate(date, format = 'YYYY-MM-DD HH:mm:ss') {
  if (!date) return ''
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')
  
  return format
    .replace('YYYY', year)
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

/**
 * 格式化日期为字符串（只包含日期部分）
 * @param {Date|string|number} date - 日期
 * @returns {string} YYYY-MM-DD 格式的日期字符串
 */
export function formatDateOnly(date) {
  return formatDate(date, 'YYYY-MM-DD')
}

/**
 * 格式化时间为字符串（只包含时间部分）
 * @param {Date|string|number} date - 日期
 * @returns {string} HH:mm:ss 格式的时间字符串
 */
export function formatTimeOnly(date) {
  return formatDate(date, 'HH:mm:ss')
}

/**
 * 格式化日期时间为字符串
 * @param {Date|string|number} date - 日期
 * @returns {string} YYYY-MM-DD HH:mm:ss 格式的日期时间字符串
 */
export function formatDateTime(date) {
  return formatDate(date, 'YYYY-MM-DD HH:mm:ss')
}

/**
 * 解析日期字符串为Date对象
 * @param {string} dateStr - 日期字符串
 * @returns {Date|null} 解析后的日期对象，失败时返回null
 */
export function parseDate(dateStr) {
  if (!dateStr) return null
  
  const date = new Date(dateStr)
  return isNaN(date.getTime()) ? null : date
}

/**
 * 获取当前日期时间
 * @returns {Date} 当前日期时间
 */
export function now() {
  return new Date()
}

/**
 * 获取今天的开始时间（00:00:00）
 * @returns {Date} 今天开始时间
 */
export function startOfToday() {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return today
}

/**
 * 获取今天的结束时间（23:59:59）
 * @returns {Date} 今天结束时间
 */
export function endOfToday() {
  const today = new Date()
  today.setHours(23, 59, 59, 999)
  return today
}

/**
 * 计算两个日期之间的天数差
 * @param {Date|string|number} date1 - 第一个日期
 * @param {Date|string|number} date2 - 第二个日期
 * @returns {number} 天数差（绝对值）
 */
export function daysBetween(date1, date2) {
  const d1 = new Date(date1)
  const d2 = new Date(date2)
  
  if (isNaN(d1.getTime()) || isNaN(d2.getTime())) return 0
  
  const diffTime = Math.abs(d2.getTime() - d1.getTime())
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
}

/**
 * 检查是否为今天
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {boolean} 是否为今天
 */
export function isToday(date) {
  const d = new Date(date)
  if (isNaN(d.getTime())) return false
  
  const today = new Date()
  return d.getFullYear() === today.getFullYear() &&
         d.getMonth() === today.getMonth() &&
         d.getDate() === today.getDate()
}

/**
 * 检查是否为昨天
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {boolean} 是否为昨天
 */
export function isYesterday(date) {
  const d = new Date(date)
  if (isNaN(d.getTime())) return false
  
  const yesterday = new Date()
  yesterday.setDate(yesterday.getDate() - 1)
  
  return d.getFullYear() === yesterday.getFullYear() &&
         d.getMonth() === yesterday.getMonth() &&
         d.getDate() === yesterday.getDate()
}

/**
 * 获取相对时间描述
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {string} 相对时间描述
 */
export function getRelativeTime(date) {
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const now = new Date()
  const diffMs = now.getTime() - d.getTime()
  const diffSeconds = Math.floor(diffMs / 1000)
  const diffMinutes = Math.floor(diffSeconds / 60)
  const diffHours = Math.floor(diffMinutes / 60)
  const diffDays = Math.floor(diffHours / 24)
  
  if (diffSeconds < 60) {
    return '刚刚'
  } else if (diffMinutes < 60) {
    return `${diffMinutes}分钟前`
  } else if (diffHours < 24 && isToday(date)) {
    return `今天 ${formatDate(date, 'HH:mm')}`
  } else if (diffDays === 1 || isYesterday(date)) {
    return `昨天 ${formatDate(date, 'HH:mm')}`
  } else if (diffDays < 7) {
    return `${diffDays}天前`
  } else {
    return formatDate(date, 'YYYY-MM-DD')
  }
}

/**
 * 获取日期范围的预设选项
 * @returns {Object} 包含常用日期范围的对象
 */
export function getDateRangeShortcuts() {
  const today = new Date()
  const yesterday = new Date(today)
  yesterday.setDate(yesterday.getDate() - 1)
  
  const lastWeek = new Date(today)
  lastWeek.setDate(lastWeek.getDate() - 7)
  
  const lastMonth = new Date(today)
  lastMonth.setMonth(lastMonth.getMonth() - 1)
  
  const last3Months = new Date(today)
  last3Months.setMonth(last3Months.getMonth() - 3)
  
  return {
    今天: {
      start: startOfToday(),
      end: endOfToday()
    },
    昨天: {
      start: new Date(yesterday.getFullYear(), yesterday.getMonth(), yesterday.getDate()),
      end: new Date(yesterday.getFullYear(), yesterday.getMonth(), yesterday.getDate(), 23, 59, 59, 999)
    },
    最近一周: {
      start: new Date(lastWeek.getFullYear(), lastWeek.getMonth(), lastWeek.getDate()),
      end: endOfToday()
    },
    最近一个月: {
      start: new Date(lastMonth.getFullYear(), lastMonth.getMonth(), lastMonth.getDate()),
      end: endOfToday()
    },
    最近三个月: {
      start: new Date(last3Months.getFullYear(), last3Months.getMonth(), last3Months.getDate()),
      end: endOfToday()
    }
  }
} 