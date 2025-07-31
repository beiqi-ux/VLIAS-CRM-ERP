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

/**
 * 格式化ID为四位数显示（如：0001）
 * @param {number|string} id - 原始ID
 * @returns {string} - 格式化后的ID字符串
 */
export function formatId(id) {
  if (id === undefined || id === null) return '-';
  return String(id).padStart(4, '0');
}

/**
 * 处理图片URL，确保可以正确访问
 * @param {string} url - 图片URL
 * @param {string} defaultUrl - 默认图片URL
 * @returns {string} - 处理后的图片URL
 */
export function formatImageUrl(url, defaultUrl = '/default-avatar.png') {
  if (!url) return defaultUrl;
  if (url.startsWith('http')) return url;
  
  // 直接使用相对路径，让代理配置处理
  // 添加随机参数防止缓存
  const timestamp = new Date().getTime();
  return `${url}?t=${timestamp}`;
} 