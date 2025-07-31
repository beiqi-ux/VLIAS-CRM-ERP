import request from '@/utils/request'

/**
 * 获取商品列表（支持分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getGoodsList(params) {
  return request({
    url: '/api/prod/goods',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取商品详情
 * @param {Number} id - 商品ID
 * @returns {Promise}
 */
export function getGoodsById(id) {
  return request({
    url: `/api/prod/goods/${id}`,
    method: 'get'
  })
}

/**
 * 根据商品编码获取商品
 * @param {String} goodsCode - 商品编码
 * @returns {Promise}
 */
export function getGoodsByCode(goodsCode) {
  return request({
    url: `/api/prod/goods/code/${goodsCode}`,
    method: 'get'
  })
}

/**
 * 根据分类ID获取商品列表
 * @param {Number} categoryId - 分类ID
 * @returns {Promise}
 */
export function getGoodsByCategory(categoryId) {
  return request({
    url: `/api/prod/goods/category/${categoryId}`,
    method: 'get'
  })
}

/**
 * 根据品牌ID获取商品列表
 * @param {Number} brandId - 品牌ID
 * @returns {Promise}
 */
export function getGoodsByBrand(brandId) {
  return request({
    url: `/api/prod/goods/brand/${brandId}`,
    method: 'get'
  })
}

/**
 * 获取推荐商品
 * @returns {Promise}
 */
export function getRecommendedGoods() {
  return request({
    url: '/api/prod/goods/recommended',
    method: 'get'
  })
}

/**
 * 获取热销商品
 * @returns {Promise}
 */
export function getHotGoods() {
  return request({
    url: '/api/prod/goods/hot',
    method: 'get'
  })
}

/**
 * 获取新品
 * @returns {Promise}
 */
export function getNewGoods() {
  return request({
    url: '/api/prod/goods/new',
    method: 'get'
  })
}

/**
 * 获取库存预警商品
 * @returns {Promise}
 */
export function getLowStockGoods() {
  return request({
    url: '/api/prod/goods/low-stock',
    method: 'get'
  })
}

/**
 * 根据审核状态获取商品
 * @param {Number} auditStatus - 审核状态
 * @returns {Promise}
 */
export function getGoodsByAuditStatus(auditStatus) {
  return request({
    url: `/api/prod/goods/audit/${auditStatus}`,
    method: 'get'
  })
}

/**
 * 搜索商品
 * @param {String} goodsName - 商品名称
 * @returns {Promise}
 */
export function searchGoods(goodsName) {
  return request({
    url: '/api/prod/goods/search',
    method: 'get',
    params: { goodsName }
  })
}

/**
 * 创建商品
 * @param {Object} data - 商品数据
 * @returns {Promise}
 */
export function createGoods(data) {
  return request({
    url: '/api/prod/goods',
    method: 'post',
    data
  })
}

/**
 * 更新商品
 * @param {Number} id - 商品ID
 * @param {Object} data - 商品数据
 * @returns {Promise}
 */
export function updateGoods(id, data) {
  return request({
    url: `/api/prod/goods/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除商品（软删除）
 * @param {Number} id - 商品ID
 * @returns {Promise}
 */
export function deleteGoods(id) {
  return request({
    url: `/api/prod/goods/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除商品（软删除）
 * @param {Array} ids - 商品ID列表
 * @returns {Promise}
 */
export function batchDeleteGoods(ids) {
  return request({
    url: '/api/prod/goods/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 上架商品
 * @param {Number} id - 商品ID
 * @returns {Promise}
 */
export function onSaleGoods(id) {
  return request({
    url: `/api/prod/goods/${id}/on-sale`,
    method: 'put'
  })
}

/**
 * 下架商品
 * @param {Number} id - 商品ID
 * @returns {Promise}
 */
export function offSaleGoods(id) {
  return request({
    url: `/api/prod/goods/${id}/off-sale`,
    method: 'put'
  })
}

/**
 * 审核商品
 * @param {Number} id - 商品ID
 * @param {Object} auditData - 审核数据
 * @returns {Promise}
 */
export function auditGoods(id, auditData) {
  return request({
    url: `/api/prod/goods/${id}/audit`,
    method: 'put',
    data: auditData
  })
}

/**
 * 更新库存
 * @param {Number} id - 商品ID
 * @param {Object} stockData - 库存数据
 * @returns {Promise}
 */
export function updateStock(id, stockData) {
  return request({
    url: `/api/prod/goods/${id}/stock`,
    method: 'put',
    data: stockData
  })
}

/**
 * 检查商品编码是否存在
 * @param {String} goodsCode - 商品编码
 * @returns {Promise}
 */
export function checkGoodsCode(goodsCode) {
  return request({
    url: '/api/prod/goods/check-code',
    method: 'get',
    params: { goodsCode }
  })
}

/**
 * 统计分类下的商品数量
 * @param {Number} categoryId - 分类ID
 * @returns {Promise}
 */
export function countByCategory(categoryId) {
  return request({
    url: `/api/prod/goods/count/category/${categoryId}`,
    method: 'get'
  })
}

/**
 * 统计品牌下的商品数量
 * @param {Number} brandId - 品牌ID
 * @returns {Promise}
 */
export function countByBrand(brandId) {
  return request({
    url: `/api/prod/goods/count/brand/${brandId}`,
    method: 'get'
  })
} 