import request from '@/utils/request'

/**
 * 获取SKU列表（支持分页）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getSkuList(params) {
  return request({
    url: '/api/prod/skus',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取SKU详情
 * @param {Number} id - SKU ID
 * @returns {Promise}
 */
export function getSkuById(id) {
  return request({
    url: `/api/prod/skus/${id}`,
    method: 'get'
  })
}

/**
 * 根据SKU编码获取SKU详情
 * @param {String} skuCode - SKU编码
 * @returns {Promise}
 */
export function getSkuByCode(skuCode) {
  return request({
    url: `/api/prod/skus/code/${skuCode}`,
    method: 'get'
  })
}

/**
 * 根据商品ID获取SKU列表
 * @param {Number} goodsId - 商品ID
 * @param {Boolean} activeOnly - 是否只查询启用的SKU
 * @returns {Promise}
 */
export function getSkusByGoodsId(goodsId, activeOnly = false) {
  return request({
    url: `/api/prod/skus/goods/${goodsId}`,
    method: 'get',
    params: { activeOnly }
  })
}

/**
 * 根据条形码获取SKU
 * @param {String} barcode - 条形码
 * @returns {Promise}
 */
export function getSkuByBarcode(barcode) {
  return request({
    url: `/api/prod/skus/barcode/${barcode}`,
    method: 'get'
  })
}

/**
 * 获取库存预警SKU列表
 * @returns {Promise}
 */
export function getLowStockSkus() {
  return request({
    url: '/api/prod/skus/low-stock',
    method: 'get'
  })
}

/**
 * 获取零库存SKU列表
 * @returns {Promise}
 */
export function getZeroStockSkus() {
  return request({
    url: '/api/prod/skus/zero-stock',
    method: 'get'
  })
}

/**
 * 获取热销SKU列表
 * @returns {Promise}
 */
export function getTopSellingSkus() {
  return request({
    url: '/api/prod/skus/top-selling',
    method: 'get'
  })
}

/**
 * 创建SKU
 * @param {Object} data - SKU数据
 * @returns {Promise}
 */
export function createSku(data) {
  return request({
    url: '/api/prod/skus',
    method: 'post',
    data
  })
}

/**
 * 批量创建SKU
 * @param {Array} skus - SKU列表
 * @returns {Promise}
 */
export function batchCreateSkus(skus) {
  return request({
    url: '/api/prod/skus/batch',
    method: 'post',
    data: skus
  })
}

/**
 * 更新SKU
 * @param {Number} id - SKU ID
 * @param {Object} data - SKU数据
 * @returns {Promise}
 */
export function updateSku(id, data) {
  return request({
    url: `/api/prod/skus/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除SKU（软删除）
 * @param {Number} id - SKU ID
 * @returns {Promise}
 */
export function deleteSku(id) {
  return request({
    url: `/api/prod/skus/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除SKU（软删除）
 * @param {Array} ids - SKU ID列表
 * @returns {Promise}
 */
export function batchDeleteSkus(ids) {
  return request({
    url: '/api/prod/skus/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 启用SKU
 * @param {Number} id - SKU ID
 * @returns {Promise}
 */
export function enableSku(id) {
  return request({
    url: `/api/prod/skus/${id}/enable`,
    method: 'put'
  })
}

/**
 * 禁用SKU
 * @param {Number} id - SKU ID
 * @returns {Promise}
 */
export function disableSku(id) {
  return request({
    url: `/api/prod/skus/${id}/disable`,
    method: 'put'
  })
}

/**
 * 更新库存
 * @param {Number} id - SKU ID
 * @param {Object} stockData - 库存数据
 * @returns {Promise}
 */
export function updateSkuStock(id, stockData) {
  return request({
    url: `/api/prod/skus/${id}/stock`,
    method: 'put',
    data: stockData
  })
}

/**
 * 增加库存
 * @param {Number} id - SKU ID
 * @param {Object} stockData - 库存数据
 * @returns {Promise}
 */
export function addSkuStock(id, stockData) {
  return request({
    url: `/api/prod/skus/${id}/stock/add`,
    method: 'put',
    data: stockData
  })
}

/**
 * 减少库存
 * @param {Number} id - SKU ID
 * @param {Object} stockData - 库存数据
 * @returns {Promise}
 */
export function reduceSkuStock(id, stockData) {
  return request({
    url: `/api/prod/skus/${id}/stock/reduce`,
    method: 'put',
    data: stockData
  })
}

/**
 * 检查SKU编码是否存在
 * @param {String} skuCode - SKU编码
 * @returns {Promise}
 */
export function checkSkuCode(skuCode) {
  return request({
    url: `/api/prod/skus/check-code/${skuCode}`,
    method: 'get'
  })
}

/**
 * 统计商品下的SKU数量
 * @param {Number} goodsId - 商品ID
 * @param {Boolean} activeOnly - 是否只统计启用的SKU
 * @returns {Promise}
 */
export function countSkusByGoodsId(goodsId, activeOnly = false) {
  return request({
    url: `/api/prod/skus/count/goods/${goodsId}`,
    method: 'get',
    params: { activeOnly }
  })
} 