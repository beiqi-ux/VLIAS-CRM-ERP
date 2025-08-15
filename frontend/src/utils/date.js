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
 * 格式化日期时间为简短格式
 * @param {Date|string|number} date - 日期
 * @returns {string} YYYY-MM-DD HH:mm 格式的日期时间字符串
 */
export function formatDateTime(date) {
  return formatDate(date, 'YYYY-MM-DD HH:mm')
}

/**
 * 解析日期字符串为Date对象
 * @param {string} dateStr - 日期字符串
 * @returns {Date|null} 解析后的Date对象，解析失败返回null
 */
export function parseDate(dateStr) {
  if (!dateStr) return null
  
  const date = new Date(dateStr)
  return isNaN(date.getTime()) ? null : date
}

/**
 * 获取当前时间戳
 * @returns {number} 当前时间戳（毫秒）
 */
export function now() {
  return Date.now()
}

/**
 * 获取今天的开始时间（00:00:00）
 * @returns {Date} 今天的开始时间
 */
export function startOfToday() {
  const today = new Date()
  today.setHours(0, 0, 0, 0)
  return today
}

/**
 * 获取今天的结束时间（23:59:59.999）
 * @returns {Date} 今天的结束时间
 */
export function endOfToday() {
  const today = new Date()
  today.setHours(23, 59, 59, 999)
  return today
}

/**
 * 计算两个日期之间的天数差
 * @param {Date|string} date1 - 第一个日期
 * @param {Date|string} date2 - 第二个日期
 * @returns {number} 天数差（date1 - date2）
 */
export function daysBetween(date1, date2) {
  const d1 = new Date(date1)
  const d2 = new Date(date2)
  
  if (isNaN(d1.getTime()) || isNaN(d2.getTime())) {
    return 0
  }
  
  const diffTime = d1.getTime() - d2.getTime()
  return Math.ceil(diffTime / (1000 * 60 * 60 * 24))
}

/**
 * 判断是否为今天
 * @param {Date|string} date - 要判断的日期
 * @returns {boolean} 是否为今天
 */
export function isToday(date) {
  const d = new Date(date)
  if (isNaN(d.getTime())) return false
  
  const today = new Date()
  return d.toDateString() === today.toDateString()
}

/**
 * 判断是否为昨天
 * @param {Date|string} date - 要判断的日期
 * @returns {boolean} 是否为昨天
 */
export function isYesterday(date) {
  const d = new Date(date)
  if (isNaN(d.getTime())) return false
  
  const yesterday = new Date()
  yesterday.setDate(yesterday.getDate() - 1)
  return d.toDateString() === yesterday.toDateString()
}

/**
 * 获取相对时间描述
 * @param {Date|string} date - 日期
 * @returns {string} 相对时间描述，如"刚刚"、"5分钟前"、"今天 15:30"等
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
  switch (range) {
    case 'today':
      return {
        start: new Date(today),
        end: new Date(today.getTime() + 24 * 60 * 60 * 1000 - 1)
      }
    case 'yesterday':
      const yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000)
      return {
        start: yesterday,
        end: new Date(yesterday.getTime() + 24 * 60 * 60 * 1000 - 1)
      }
    case 'week':
      const weekStart = new Date(today.getTime() - (today.getDay() || 7 - 1) * 24 * 60 * 60 * 1000)
      return {
        start: weekStart,
        end: new Date(weekStart.getTime() + 7 * 24 * 60 * 60 * 1000 - 1)
      }
    case 'month':
      const monthStart = new Date(now.getFullYear(), now.getMonth(), 1)
      const monthEnd = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59, 999)
      return {
        start: monthStart,
        end: monthEnd
      }
    case 'year':
      const yearStart = new Date(now.getFullYear(), 0, 1)
      const yearEnd = new Date(now.getFullYear(), 11, 31, 23, 59, 59, 999)
      return {
        start: yearStart,
        end: yearEnd
      }
    default:
      return {
        start: today,
        end: new Date(today.getTime() + 24 * 60 * 60 * 1000 - 1)
      }
  }
} 
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
 * 格式化日期为日期格式（不包含时间）
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {string} 格式化后的日期字符串
 */
export function formatDateOnly(date) {
  return formatDate(date, 'YYYY-MM-DD')
}

/**
 * 格式化日期为时间格式（不包含日期）
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {string} 格式化后的时间字符串
 */
export function formatTimeOnly(date) {
  return formatDate(date, 'HH:mm:ss')
}

/**
 * 获取相对时间描述
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {string} 相对时间描述
 */
export function getRelativeTime(date) {
  if (!date) return ''
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const month = 30 * day
  const year = 365 * day
  
  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return `${Math.floor(diff / minute)}分钟前`
  } else if (diff < day) {
    return `${Math.floor(diff / hour)}小时前`
  } else if (diff < month) {
    return `${Math.floor(diff / day)}天前`
  } else if (diff < year) {
    return `${Math.floor(diff / month)}个月前`
  } else {
    return `${Math.floor(diff / year)}年前`
  }
}

/**
 * 判断日期是否为今天
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {boolean} 是否为今天
 */
export function isToday(date) {
  if (!date) return false
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return false
  
  const today = new Date()
  return d.toDateString() === today.toDateString()
}

/**
 * 获取日期范围的开始和结束时间
 * @param {string} range - 范围类型：'today', 'yesterday', 'week', 'month', 'year'
 * @returns {object} 包含 start 和 end 的对象
 */
export function getDateRange(range) {
  const now = new Date()
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  
  switch (range) {
    case 'today':
      return {
        start: new Date(today),
        end: new Date(today.getTime() + 24 * 60 * 60 * 1000 - 1)
      }
    case 'yesterday':
      const yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000)
      return {
        start: yesterday,
        end: new Date(yesterday.getTime() + 24 * 60 * 60 * 1000 - 1)
      }
    case 'week':
      const weekStart = new Date(today.getTime() - (today.getDay() || 7 - 1) * 24 * 60 * 60 * 1000)
      return {
        start: weekStart,
        end: new Date(weekStart.getTime() + 7 * 24 * 60 * 60 * 1000 - 1)
      }
    case 'month':
      const monthStart = new Date(now.getFullYear(), now.getMonth(), 1)
      const monthEnd = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59, 999)
      return {
        start: monthStart,
        end: monthEnd
      }
    case 'year':
      const yearStart = new Date(now.getFullYear(), 0, 1)
      const yearEnd = new Date(now.getFullYear(), 11, 31, 23, 59, 59, 999)
      return {
        start: yearStart,
        end: yearEnd
      }
    default:
      return {
        start: today,
        end: new Date(today.getTime() + 24 * 60 * 60 * 1000 - 1)
      }
  }
} 
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
 * 格式化日期为日期格式（不包含时间）
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {string} 格式化后的日期字符串
 */
export function formatDateOnly(date) {
  return formatDate(date, 'YYYY-MM-DD')
}

/**
 * 格式化日期为时间格式（不包含日期）
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {string} 格式化后的时间字符串
 */
export function formatTimeOnly(date) {
  return formatDate(date, 'HH:mm:ss')
}

/**
 * 获取相对时间描述
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {string} 相对时间描述
 */
export function getRelativeTime(date) {
  if (!date) return ''
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return ''
  
  const now = new Date()
  const diff = now.getTime() - d.getTime()
  
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const month = 30 * day
  const year = 365 * day
  
  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return `${Math.floor(diff / minute)}分钟前`
  } else if (diff < day) {
    return `${Math.floor(diff / hour)}小时前`
  } else if (diff < month) {
    return `${Math.floor(diff / day)}天前`
  } else if (diff < year) {
    return `${Math.floor(diff / month)}个月前`
  } else {
    return `${Math.floor(diff / year)}年前`
  }
}

/**
 * 判断日期是否为今天
 * @param {Date|string|number} date - 日期对象、日期字符串或时间戳
 * @returns {boolean} 是否为今天
 */
export function isToday(date) {
  if (!date) return false
  
  const d = new Date(date)
  if (isNaN(d.getTime())) return false
  
  const today = new Date()
  return d.toDateString() === today.toDateString()
}

/**
 * 获取日期范围的开始和结束时间
 * @param {string} range - 范围类型：'today', 'yesterday', 'week', 'month', 'year'
 * @returns {object} 包含 start 和 end 的对象
 */
export function getDateRange(range) {
  const now = new Date()
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate())
  
  switch (range) {
    case 'today':
      return {
        start: new Date(today),
        end: new Date(today.getTime() + 24 * 60 * 60 * 1000 - 1)
      }
    case 'yesterday':
      const yesterday = new Date(today.getTime() - 24 * 60 * 60 * 1000)
      return {
        start: yesterday,
        end: new Date(yesterday.getTime() + 24 * 60 * 60 * 1000 - 1)
      }
    case 'week':
      const weekStart = new Date(today.getTime() - (today.getDay() || 7 - 1) * 24 * 60 * 60 * 1000)
      return {
        start: weekStart,
        end: new Date(weekStart.getTime() + 7 * 24 * 60 * 60 * 1000 - 1)
      }
    case 'month':
      const monthStart = new Date(now.getFullYear(), now.getMonth(), 1)
      const monthEnd = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59, 999)
      return {
        start: monthStart,
        end: monthEnd
      }
    case 'year':
      const yearStart = new Date(now.getFullYear(), 0, 1)
      const yearEnd = new Date(now.getFullYear(), 11, 31, 23, 59, 59, 999)
      return {
        start: yearStart,
        end: yearEnd
      }
    default:
      return {
        start: today,
        end: new Date(today.getTime() + 24 * 60 * 60 * 1000 - 1)
      }
  }
} 