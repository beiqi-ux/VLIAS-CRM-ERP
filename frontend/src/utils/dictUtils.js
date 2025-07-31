import { getDictItemsByDictCode } from '@/api/dict'

// 字典缓存
const dictCache = new Map()

/**
 * 获取字典项列表
 * @param {string} dictCode 字典编码
 * @param {boolean} useCache 是否使用缓存
 * @returns {Promise<Array>} 字典项列表
 */
export async function getDictItems(dictCode, useCache = true) {
  if (useCache && dictCache.has(dictCode)) {
    return dictCache.get(dictCode)
  }
  
  try {
    const response = await getDictItemsByDictCode(dictCode, 1) // 只获取启用的
    const items = response.success ? response.data : []
    
    if (useCache) {
      dictCache.set(dictCode, items)
    }
    
    return items
  } catch (error) {
    console.error(`获取字典项失败: ${dictCode}`, error)
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
  
  const items = dictCache.get(dictCode) || []
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
 */
export function clearDictCache(dictCode) {
  if (dictCode) {
    dictCache.delete(dictCode)
  } else {
    dictCache.clear()
  }
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