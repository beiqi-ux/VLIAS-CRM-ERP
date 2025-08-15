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

// ==================== 规格分类管理 ====================

/**
 * 获取规格分类列表（支持分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getSpecificationCategories(params) {
  return request({
    url: '/api/prod/spec-categories',
    method: 'get',
    params
  })
}

/**
 * 获取所有启用的规格分类（不分页）
 * @returns {Promise}
 */
export function getActiveSpecificationCategories() {
  return request({
    url: '/api/prod/spec-categories/active',
    method: 'get'
  })
}

/**
 * 根据ID获取规格分类详情
 * @param {Number} id - 分类ID
 * @returns {Promise}
 */
export function getSpecificationCategoryById(id) {
  return request({
    url: `/api/prod/spec-categories/${id}`,
    method: 'get'
  })
}

/**
 * 创建规格分类
 * @param {Object} data - 分类数据
 * @returns {Promise}
 */
export function createSpecificationCategory(data) {
  return request({
    url: '/api/prod/spec-categories',
    method: 'post',
    data
  })
}

/**
 * 更新规格分类
 * @param {Number} id - 分类ID
 * @param {Object} data - 分类数据
 * @returns {Promise}
 */
export function updateSpecificationCategory(id, data) {
  return request({
    url: `/api/prod/spec-categories/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除规格分类
 * @param {Number} id - 分类ID
 * @returns {Promise}
 */
export function deleteSpecificationCategory(id) {
  return request({
    url: `/api/prod/spec-categories/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除规格分类
 * @param {Array} ids - 分类ID数组
 * @returns {Promise}
 */
export function batchDeleteSpecificationCategories(ids) {
  return request({
    url: '/api/prod/spec-categories/batch',
    method: 'delete',
    data: ids
  })
}

// ==================== 规格项管理 ====================

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

// ==================== 规格值管理 ====================

/**
 * 获取规格值列表（支持分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getSpecificationValues(params) {
  return request({
    url: '/api/prod/spec-values',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取规格值详情
 * @param {Number} id - 规格值ID
 * @returns {Promise}
 */
export function getSpecificationValueById(id) {
  return request({
    url: `/api/prod/spec-values/${id}`,
    method: 'get'
  })
}

/**
 * 创建规格值
 * @param {Object} data - 规格值数据
 * @returns {Promise}
 */
export function createSpecificationValue(data) {
  return request({
    url: '/api/prod/spec-values',
    method: 'post',
    data
  })
}

/**
 * 更新规格值
 * @param {Number} id - 规格值ID
 * @param {Object} data - 规格值数据
 * @returns {Promise}
 */
export function updateSpecificationValue(id, data) {
  return request({
    url: `/api/prod/spec-values/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除规格值
 * @param {Number} id - 规格值ID
 * @returns {Promise}
 */
export function deleteSpecificationValue(id) {
  return request({
    url: `/api/prod/spec-values/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除规格值
 * @param {Array} ids - 规格值ID数组
 * @returns {Promise}
 */
export function batchDeleteSpecificationValues(ids) {
  return request({
    url: '/api/prod/spec-values/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 根据商品ID获取规格列表
 * @param {Number} goodsId - 商品ID
 * @returns {Promise}
 */
export function getSpecificationsByGoodsId(goodsId) {
  return request({
    url: `/api/prod/specifications/goods/${goodsId}`,
    method: 'get'
  })
}

/**
 * 获取最精准的规格数据
 * 优先返回商品专属规格，其次返回分类通用规格
 * @param {Number} goodsId - 商品ID
 * @param {Number} categoryId - 分类ID
 * @returns {Promise}
 */
export function getOptimalSpecifications(goodsId, categoryId) {
  return request({
    url: '/api/prod/specifications/optimal',
    method: 'get',
    params: { goodsId, categoryId }
  })
} 