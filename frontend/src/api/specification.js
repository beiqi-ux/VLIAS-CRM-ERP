import request from '@/utils/request'

/**
 * 获取规格列表（支持分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getSpecificationList(params) {
  return request({
    url: '/api/prod/specifications',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取规格详情
 * @param {Number} id - 规格ID
 * @returns {Promise}
 */
export function getSpecificationById(id) {
  return request({
    url: `/api/prod/specifications/${id}`,
    method: 'get'
  })
}

/**
 * 创建规格
 * @param {Object} data - 规格数据
 * @returns {Promise}
 */
export function createSpecification(data) {
  return request({
    url: '/api/prod/specifications',
    method: 'post',
    data
  })
}

/**
 * 更新规格
 * @param {Number} id - 规格ID
 * @param {Object} data - 规格数据
 * @returns {Promise}
 */
export function updateSpecification(id, data) {
  return request({
    url: `/api/prod/specifications/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除规格
 * @param {Number} id - 规格ID
 * @returns {Promise}
 */
export function deleteSpecification(id) {
  return request({
    url: `/api/prod/specifications/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除规格
 * @param {Array} ids - 规格ID数组
 * @returns {Promise}
 */
export function batchDeleteSpecifications(ids) {
  return request({
    url: '/api/prod/specifications/batch',
    method: 'delete',
    data: { ids }
  })
}

/**
 * 获取所有启用的规格（不分页）
 * @returns {Promise}
 */
export function getAllEnabledSpecifications() {
  return request({
    url: '/api/prod/specifications/enabled',
    method: 'get'
  })
} 