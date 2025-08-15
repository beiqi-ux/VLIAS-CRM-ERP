import request from '@/utils/request'

/**
 * 获取品牌列表（支持分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getBrandList(params) {
  return request({
    url: '/api/prod/brands',
    method: 'get',
    params
  })
}

/**
 * 获取所有品牌（不分页）
 * @returns {Promise}
 */
export function getAllBrands() {
  return request({
    url: '/api/prod/brands/all',
    method: 'get'
  })
}

/**
 * 根据ID获取品牌详情
 * @param {Number} id - 品牌ID
 * @returns {Promise}
 */
export function getBrandById(id) {
  return request({
    url: `/api/prod/brands/${id}`,
    method: 'get'
  })
}

/**
 * 根据品牌名称获取品牌
 * @param {String} brandName - 品牌名称
 * @returns {Promise}
 */
export function getBrandByName(brandName) {
  return request({
    url: `/api/prod/brands/name/${brandName}`,
    method: 'get'
  })
}

/**
 * 获取正常状态的品牌
 * @returns {Promise}
 */
export function getActiveBrands() {
  return request({
    url: '/api/prod/brands/active',
    method: 'get'
  })
}

/**
 * 搜索品牌
 * @param {String} brandName - 品牌名称
 * @returns {Promise}
 */
export function searchBrands(brandName) {
  return request({
    url: '/api/prod/brands/search',
    method: 'get',
    params: { brandName }
  })
}

/**
 * 创建品牌
 * @param {Object} data - 品牌数据
 * @returns {Promise}
 */
export function createBrand(data) {
  return request({
    url: '/api/prod/brands',
    method: 'post',
    data
  })
}

/**
 * 更新品牌
 * @param {Number} id - 品牌ID
 * @param {Object} data - 品牌数据
 * @returns {Promise}
 */
export function updateBrand(id, data) {
  return request({
    url: `/api/prod/brands/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除品牌（软删除）
 * @param {Number} id - 品牌ID
 * @returns {Promise}
 */
export function deleteBrand(id) {
  return request({
    url: `/api/prod/brands/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除品牌（软删除）
 * @param {Array} ids - 品牌ID列表
 * @returns {Promise}
 */
export function batchDeleteBrands(ids) {
  return request({
    url: '/api/prod/brands/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 启用品牌
 * @param {Number} id - 品牌ID
 * @returns {Promise}
 */
export function enableBrand(id) {
  return request({
    url: `/api/prod/brands/${id}/enable`,
    method: 'put'
  })
}

/**
 * 禁用品牌
 * @param {Number} id - 品牌ID
 * @returns {Promise}
 */
export function disableBrand(id) {
  return request({
    url: `/api/prod/brands/${id}/disable`,
    method: 'put'
  })
}

/**
 * 检查品牌名称是否存在
 * @param {String} brandName - 品牌名称
 * @returns {Promise}
 */
export function checkBrandName(brandName) {
  return request({
    url: '/api/prod/brands/check-name',
    method: 'get',
    params: { brandName }
  })
}

/**
 * 统计品牌数量
 * @param {Number} status - 状态
 * @returns {Promise}
 */
export function countBrands(status = 1) {
  return request({
    url: '/api/prod/brands/count',
    method: 'get',
    params: { status }
  })
} 