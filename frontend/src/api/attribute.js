import request from '@/utils/request'

/**
 * 获取属性列表（支持分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getAttributeList(params) {
  return request({
    url: '/api/prod/attributes',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取属性详情
 * @param {Number} id - 属性ID
 * @returns {Promise}
 */
export function getAttributeById(id) {
  return request({
    url: `/api/prod/attributes/${id}`,
    method: 'get'
  })
}

/**
 * 创建属性
 * @param {Object} data - 属性数据
 * @returns {Promise}
 */
export function createAttribute(data) {
  return request({
    url: '/api/prod/attributes',
    method: 'post',
    data
  })
}

/**
 * 更新属性
 * @param {Number} id - 属性ID
 * @param {Object} data - 属性数据
 * @returns {Promise}
 */
export function updateAttribute(id, data) {
  return request({
    url: `/api/prod/attributes/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除属性
 * @param {Number} id - 属性ID
 * @returns {Promise}
 */
export function deleteAttribute(id) {
  return request({
    url: `/api/prod/attributes/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除属性
 * @param {Array} ids - 属性ID数组
 * @returns {Promise}
 */
export function batchDeleteAttributes(ids) {
  return request({
    url: '/api/prod/attributes/batch',
    method: 'delete',
    data: { ids }
  })
}

/**
 * 获取所有启用的属性（不分页）
 * @returns {Promise}
 */
export function getAllEnabledAttributes() {
  return request({
    url: '/api/prod/attributes/enabled',
    method: 'get'
  })
} 