import request from '@/utils/request'

/**
 * 获取规格项列表（支持分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getSpecificationItems(params) {
  return request({
    url: '/api/prod/spec-items',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用的规格项（不分页）
 * @returns {Promise}
 */
export function getActiveSpecificationItems() {
  return request({
    url: '/api/prod/spec-items/active',
    method: 'get'
  })
}

/**
 * 根据ID获取规格项详情
 * @param {Number} id - 规格项ID
 * @returns {Promise}
 */
export function getSpecificationItemById(id) {
  return request({
    url: `/api/prod/spec-items/${id}`,
    method: 'get'
  })
}

/**
 * 创建规格项
 * @param {Object} data - 规格项数据
 * @returns {Promise}
 */
export function createSpecificationItem(data) {
  return request({
    url: '/api/prod/spec-items',
    method: 'post',
    data
  })
}

/**
 * 更新规格项
 * @param {Number} id - 规格项ID
 * @param {Object} data - 规格项数据
 * @returns {Promise}
 */
export function updateSpecificationItem(id, data) {
  return request({
    url: `/api/prod/spec-items/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除规格项
 * @param {Number} id - 规格项ID
 * @returns {Promise}
 */
export function deleteSpecificationItem(id) {
  return request({
    url: `/api/prod/spec-items/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除规格项
 * @param {Array} ids - 规格项ID数组
 * @returns {Promise}
 */
export function batchDeleteSpecificationItems(ids) {
  return request({
    url: '/api/prod/spec-items/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 根据分类ID获取规格项列表
 * @param {Number} categoryId - 分类ID
 * @returns {Promise}
 */
export function getSpecificationItemsByCategory(categoryId) {
  return request({
    url: `/api/prod/spec-items/category/${categoryId}`,
    method: 'get'
  })
} 