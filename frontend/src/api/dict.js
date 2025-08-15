import request from '@/utils/request'

/**
 * 获取字典列表（分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getDictList(params) {
  return request({
    url: '/api/sys/dicts',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用的字典列表
 * @returns {Promise}
 */
export function getEnabledDictList() {
  return request({
    url: '/api/sys/dicts/enabled',
    method: 'get'
  })
}

/**
 * 获取单个字典
 * @param {number} id - 字典ID
 * @returns {Promise}
 */
export function getDictById(id) {
  return request({
    url: `/api/sys/dicts/${id}`,
    method: 'get'
  })
}

/**
 * 创建字典
 * @param {Object} data - 字典信息
 * @returns {Promise}
 */
export function createDict(data) {
  return request({
    url: '/api/sys/dicts',
    method: 'post',
    data
  })
}

/**
 * 更新字典
 * @param {number} id - 字典ID
 * @param {Object} data - 字典信息
 * @returns {Promise}
 */
export function updateDict(id, data) {
  return request({
    url: `/api/sys/dicts/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除字典
 * @param {number} id - 字典ID
 * @returns {Promise}
 */
export function deleteDict(id) {
  return request({
    url: `/api/sys/dicts/${id}`,
    method: 'delete'
  })
}

/**
 * 更新字典状态
 * @param {number} id - 字典ID
 * @param {number} status - 状态
 * @returns {Promise}
 */
export function updateDictStatus(id, status) {
  return request({
    url: `/api/sys/dicts/${id}/status`,
    method: 'patch',
    params: { status }
  })
}

/**
 * 获取字典项列表（分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getDictItemList(params) {
  return request({
    url: '/api/sys/dict-items',
    method: 'get',
    params
  })
}

/**
 * 根据字典ID获取字典项列表
 * @param {number} dictId - 字典ID
 * @param {number} status - 状态（可选）
 * @returns {Promise}
 */
export function getDictItemsByDictId(dictId, status) {
  const params = status !== undefined ? { status } : {}
  return request({
    url: `/api/sys/dict-items/dict/${dictId}`,
    method: 'get',
    params
  })
}

/**
 * 根据字典编码获取字典项列表
 * @param {string} dictCode - 字典编码
 * @param {number} status - 状态（可选）
 * @returns {Promise}
 */
export function getDictItemsByDictCode(dictCode, status) {
  const params = status !== undefined ? { status } : {}
  return request({
    url: `/api/sys/dict-items/dict-code/${dictCode}`,
    method: 'get',
    params
  })
}

/**
 * 获取单个字典项
 * @param {number} id - 字典项ID
 * @returns {Promise}
 */
export function getDictItemById(id) {
  return request({
    url: `/api/sys/dict-items/${id}`,
    method: 'get'
  })
}

/**
 * 创建字典项
 * @param {Object} data - 字典项信息
 * @returns {Promise}
 */
export function createDictItem(data) {
  return request({
    url: '/api/sys/dict-items',
    method: 'post',
    data
  })
}

/**
 * 更新字典项
 * @param {number} id - 字典项ID
 * @param {Object} data - 字典项信息
 * @returns {Promise}
 */
export function updateDictItem(id, data) {
  return request({
    url: `/api/sys/dict-items/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除字典项
 * @param {number} id - 字典项ID
 * @returns {Promise}
 */
export function deleteDictItem(id) {
  return request({
    url: `/api/sys/dict-items/${id}`,
    method: 'delete'
  })
}

/**
 * 更新字典项状态
 * @param {number} id - 字典项ID
 * @param {number} status - 状态
 * @returns {Promise}
 */
export function updateDictItemStatus(id, status) {
  return request({
    url: `/api/sys/dict-items/${id}/status`,
    method: 'patch',
    params: { status }
  })
} 