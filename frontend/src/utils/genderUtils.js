import { getDictItems, getDictText, getDictTextSync } from './dictUtils'

/**
 * 性别字典工具类
 * 提供性别相关的常用方法
 */

// 性别字典编码
export const GENDER_DICT_CODE = 'gender'

/**
 * 获取性别选项列表
 * @returns {Promise<Array>} 性别选项数组
 */
export async function getGenderOptions() {
  return await getDictItems(GENDER_DICT_CODE)
}

/**
 * 根据性别值获取显示文本
 * @param {string|number} value 性别值 (0-未知, 1-男, 2-女)
 * @returns {Promise<string>} 性别显示文本
 */
export async function getGenderText(value) {
  return await getDictText(GENDER_DICT_CODE, value)
}

/**
 * 根据性别值获取显示文本（同步版本）
 * @param {string|number} value 性别值
 * @returns {string} 性别显示文本
 */
export function getGenderTextSync(value) {
  return getDictTextSync(GENDER_DICT_CODE, value)
}

/**
 * 性别值枚举
 */
export const GENDER_VALUES = {
  UNKNOWN: '0',  // 未知
  MALE: '1',     // 男
  FEMALE: '2'    // 女
}

/**
 * 性别文本枚举
 */
export const GENDER_LABELS = {
  [GENDER_VALUES.UNKNOWN]: '未知',
  [GENDER_VALUES.MALE]: '男',
  [GENDER_VALUES.FEMALE]: '女'
}

/**
 * 判断是否为有效的性别值
 * @param {string|number} value 性别值
 * @returns {boolean} 是否有效
 */
export function isValidGender(value) {
  return Object.values(GENDER_VALUES).includes(String(value))
}

/**
 * 获取性别对应的图标
 * @param {string|number} value 性别值
 * @returns {string} 图标名称
 */
export function getGenderIcon(value) {
  const iconMap = {
    [GENDER_VALUES.UNKNOWN]: 'user',
    [GENDER_VALUES.MALE]: 'male',
    [GENDER_VALUES.FEMALE]: 'female'
  }
  return iconMap[String(value)] || 'user'
}

/**
 * 获取性别对应的颜色
 * @param {string|number} value 性别值
 * @returns {string} 颜色值
 */
export function getGenderColor(value) {
  const colorMap = {
    [GENDER_VALUES.UNKNOWN]: '#909399',  // 灰色
    [GENDER_VALUES.MALE]: '#409EFF',     // 蓝色
    [GENDER_VALUES.FEMALE]: '#E6A23C'    // 橙色
  }
  return colorMap[String(value)] || '#909399'
} 