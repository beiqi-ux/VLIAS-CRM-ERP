/**
 * 格式化日期时间
 * @param {string|Date} dateTime - 日期时间
 * @returns {string} - 格式化后的日期时间字符串
 */
export function formatDateTime(dateTime) {
  if (!dateTime) return '-';
  return new Date(dateTime).toLocaleString('zh-CN');
}

/**
 * 格式化日期
 * @param {string|Date} date - 日期
 * @returns {string} - 格式化后的日期字符串
 */
export function formatDate(date) {
  if (!date) return '-';
  return new Date(date).toLocaleDateString('zh-CN');
} 