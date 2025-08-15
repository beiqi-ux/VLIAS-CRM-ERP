import { getDictItemsByDictCode } from '@/api/dict'

// 字典缓存 - 增强版本，保持向后兼容
const dictCache = new Map()

// 缓存版本管理
const CACHE_VERSION = '1.0.0'
const CACHE_VERSION_KEY = 'dict_cache_version'

// 缓存配置
const CACHE_CONFIG = {
  defaultTTL: 5 * 60 * 1000, // 默认5分钟过期
  maxCacheSize: 100, // 最大缓存条目数
  checkInterval: 60 * 1000 // 每分钟检查一次过期缓存
}

// 检查缓存版本兼容性
function checkCacheVersion() {
  const storedVersion = localStorage.getItem(CACHE_VERSION_KEY)
  if (storedVersion !== CACHE_VERSION) {
    dictCache.clear()
    localStorage.setItem(CACHE_VERSION_KEY, CACHE_VERSION)
  }
}

// 创建缓存条目
function createCacheEntry(data, ttl = CACHE_CONFIG.defaultTTL) {
  return {
    data,
    timestamp: Date.now(),
    ttl,
    version: CACHE_VERSION
  }
}

// 检查缓存条目是否有效
function isCacheValid(entry) {
  if (!entry || entry.version !== CACHE_VERSION) {
    return false
  }
  return Date.now() - entry.timestamp < entry.ttl
}

// 清理过期缓存
function cleanExpiredCache() {
  for (const [key, entry] of dictCache.entries()) {
    if (!isCacheValid(entry)) {
      dictCache.delete(key)
    }
  }
  
  // 如果缓存条目过多，清理最老的
  if (dictCache.size > CACHE_CONFIG.maxCacheSize) {
    const entries = Array.from(dictCache.entries())
    entries.sort((a, b) => a[1].timestamp - b[1].timestamp)
    
    const toDelete = entries.slice(0, dictCache.size - CACHE_CONFIG.maxCacheSize)
    toDelete.forEach(([key]) => {
      dictCache.delete(key)
    })
  }
}

// 初始化缓存系统
checkCacheVersion()

// 定期清理过期缓存
const cleanupInterval = setInterval(cleanExpiredCache, CACHE_CONFIG.checkInterval)

// 页面卸载时清理定时器
if (typeof window !== 'undefined') {
  window.addEventListener('beforeunload', () => {
    if (cleanupInterval) {
      clearInterval(cleanupInterval)
    }
  })
}

/**
 * 获取字典项列表
 * @param {string} dictCode 字典编码
 * @param {boolean} useCache 是否使用缓存（保持向后兼容）
 * @param {Object} options 扩展选项 { ttl: 缓存时间, forceRefresh: 强制刷新 }
 * @returns {Promise<Array>} 字典项列表
 */
export async function getDictItems(dictCode, useCache = true, options = {}) {
  // 向后兼容：如果第二个参数是对象，则认为是新的options参数
  if (typeof useCache === 'object') {
    options = useCache
    useCache = options.useCache !== false
  }
  
  const { ttl = CACHE_CONFIG.defaultTTL, forceRefresh = false } = options
  
  // 如果不使用缓存或强制刷新，直接请求
  if (!useCache || forceRefresh) {
    try {
      const response = await getDictItemsByDictCode(dictCode, 1)
      const items = response.success ? response.data : []
      
      // 即使不使用缓存，也存储结果供后续使用
      if (useCache) {
        dictCache.set(dictCode, createCacheEntry(items, ttl))
      }
      
      return items
    } catch (error) {
      console.error(`获取字典项失败: ${dictCode}`, error)
      return []
    }
  }
  
  // 检查缓存
  const cacheEntry = dictCache.get(dictCode)
  if (cacheEntry && isCacheValid(cacheEntry)) {
    return cacheEntry.data
  }
  
  // 缓存未命中或已过期，重新获取
  try {
    const response = await getDictItemsByDictCode(dictCode, 1)
    const items = response.success ? response.data : []
    
    // 存储到缓存
    dictCache.set(dictCode, createCacheEntry(items, ttl))
    
    return items
  } catch (error) {
    console.error(`获取字典项失败: ${dictCode}`, error)
    
    // 如果有过期缓存，在网络错误时仍然返回
    if (cacheEntry && cacheEntry.data) {
      console.warn(`网络错误，使用过期缓存: ${dictCode}`)
      return cacheEntry.data
    }
    
    return []
  }
}

/**
 * 根据字典编码和值获取显示文本
 * @param {string} dictCode 字典编码
 * @param {string|number} value 字典项值
 * @returns {Promise<string>} 显示文本
 */
export async function getDictText(dictCode, value) {
  if (value === null || value === undefined || value === '') {
    return '-'
  }
  
  const items = await getDictItems(dictCode)
  const item = items.find(item => item.itemValue == value)
  return item ? item.itemText : value.toString()
}

/**
 * 根据字典编码和值获取显示文本（同步版本，需要先预加载字典）
 * @param {string} dictCode 字典编码
 * @param {string|number} value 字典项值
 * @returns {string} 显示文本
 */
export function getDictTextSync(dictCode, value) {
  if (value === null || value === undefined || value === '') {
    return '-'
  }
  
  // 适配新的缓存结构
  const cacheEntry = dictCache.get(dictCode)
  const items = (cacheEntry && isCacheValid(cacheEntry)) ? cacheEntry.data : []
  
  // 如果缓存已过期但仍有数据，给出警告
  if (cacheEntry && !isCacheValid(cacheEntry) && cacheEntry.data.length > 0) {
    console.warn(`字典缓存已过期但仍在使用: ${dictCode}，建议重新加载`)
  }
  
  // 确保类型匹配，同时支持字符串和数字比较
  const item = items.find(item => 
    item.itemValue == value || 
    item.itemValue === value.toString() || 
    item.itemValue === parseInt(value)
  )
  return item ? item.itemText : value.toString()
}

/**
 * 预加载字典数据
 * @param {Array<string>} dictCodes 字典编码数组
 */
export async function preloadDicts(dictCodes) {
  const promises = dictCodes.map(dictCode => getDictItems(dictCode, true))
  await Promise.all(promises)
}

/**
 * 清除字典缓存
 * @param {string} dictCode 字典编码，不传则清除所有
 * @param {Object} options 清理选项 { force: 强制清理所有版本 }
 */
export function clearDictCache(dictCode, options = {}) {
  const { force = false } = options
  
  if (dictCode) {
    // 清除指定字典缓存
    const deleted = dictCache.delete(dictCode)
    if (deleted) {
      // 字典缓存已删除
    }
  } else {
    // 清除所有缓存
    dictCache.clear()
    
    if (force) {
      // 强制清理时，重置版本标识
      localStorage.removeItem(CACHE_VERSION_KEY)
    } else {
      // 非强制清理时的逻辑
    }
  }
}

/**
 * 获取缓存统计信息（新增调试功能）
 * @returns {Object} 缓存统计
 */
export function getCacheStats() {
  const stats = {
    size: dictCache.size,
    version: CACHE_VERSION,
    items: []
  }
  
  for (const [key, entry] of dictCache.entries()) {
    stats.items.push({
      dictCode: key,
      isValid: isCacheValid(entry),
      age: Date.now() - entry.timestamp,
      ttl: entry.ttl,
      dataSize: entry.data.length
    })
  }
  
  return stats
}

/**
 * 获取字典选项（用于el-select等组件）
 * @param {string} dictCode 字典编码
 * @returns {Promise<Array>} 选项数组 [{label, value}]
 */
export async function getDictOptions(dictCode) {
  const items = await getDictItems(dictCode)
  return items.map(item => ({
    label: item.itemText,
    value: item.itemValue
  }))
} 