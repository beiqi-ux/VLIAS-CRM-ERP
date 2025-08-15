import request from '@/utils/request'

/**
 * 供应商商品管理API
 */

// 分页查询供应商商品列表
export function getSupplierGoodsList(params) {
  return request({
    url: '/api/purchase/supplier-goods',
    method: 'get',
    params
  })
}

// 根据ID查询供应商商品详情
export function getSupplierGoodsById(id) {
  return request({
    url: `/api/purchase/supplier-goods/${id}`,
    method: 'get'
  })
}

// 新增供应商商品关联
export function createSupplierGoods(data) {
  return request({
    url: '/api/purchase/supplier-goods',
    method: 'post',
    data
  })
}

// 更新供应商商品关联
export function updateSupplierGoods(id, data) {
  return request({
    url: `/api/purchase/supplier-goods/${id}`,
    method: 'put',
    data
  })
}

// 删除供应商商品关联
export function deleteSupplierGoods(id) {
  return request({
    url: `/api/purchase/supplier-goods/${id}`,
    method: 'delete'
  })
}

// 查询供应商的所有商品
export function getGoodsBySupplierId(supplierId) {
  return request({
    url: `/api/purchase/supplier-goods/supplier/${supplierId}`,
    method: 'get'
  })
}

// 查询商品的所有供应商
export function getSuppliersByGoodsId(goodsId) {
  return request({
    url: `/api/purchase/supplier-goods/goods/${goodsId}`,
    method: 'get'
  })
}

// 价格比较
export function comparePrice(goodsId) {
  return request({
    url: `/api/purchase/supplier-goods/compare/${goodsId}`,
    method: 'get'
  })
}

// 批量新增供应商商品关联
export function batchCreateSupplierGoods(data) {
  return request({
    url: '/api/purchase/supplier-goods/batch',
    method: 'post',
    data
  })
}

// 更新供应商商品状态
export function updateSupplierGoodsStatus(id, status) {
  return request({
    url: `/api/purchase/supplier-goods/${id}/status`,
    method: 'put',
    params: { status }
  })
}

// 获取推荐供应商
export function getRecommendedSuppliers(goodsId, quantity) {
  return request({
    url: `/api/purchase/supplier-goods/recommend/${goodsId}`,
    method: 'get',
    params: { quantity }
  })
}

// 检查供应商商品关联是否存在
export function checkSupplierGoodsExists(params) {
  return request({
    url: '/api/purchase/supplier-goods/exists',
    method: 'get',
    params
  })
}

// 获取统计信息
export function getSupplierGoodsStatistics(params) {
  return request({
    url: '/api/purchase/supplier-goods/statistics',
    method: 'get',
    params
  })
}

// 搜索供应商商品
export function searchSupplierGoods(params) {
  return request({
    url: '/api/purchase/supplier-goods/search',
    method: 'get',
    params
  })
}

// 导出供应商商品Excel
export function exportSupplierGoods(params) {
  return request({
    url: '/api/purchase/supplier-goods/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 导入供应商商品Excel
export function importSupplierGoods(file) {
  const formData = new FormData()
  formData.append('file', file)
  return request({
    url: '/api/purchase/supplier-goods/import',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 下载导入模板
export function downloadImportTemplate() {
  return request({
    url: '/api/purchase/supplier-goods/import/template',
    method: 'get',
    responseType: 'blob'
  })
}

// 默认导出对象，用于兼容现有组件
export const supplierGoodsApi = {
  list: getSupplierGoodsList,
  getById: getSupplierGoodsById,
  create: createSupplierGoods,
  createSupplierGoods: createSupplierGoods,
  update: updateSupplierGoods,
  updateSupplierGoods: updateSupplierGoods,
  delete: deleteSupplierGoods,
  deleteSupplierGoods: deleteSupplierGoods,
  getGoodsBySupplierId: getGoodsBySupplierId,
  getSuppliersByGoodsId: getSuppliersByGoodsId,
  comparePrice: comparePrice,
  batchCreate: batchCreateSupplierGoods,
  updateStatus: updateSupplierGoodsStatus,
  getRecommended: getRecommendedSuppliers,
  checkExists: checkSupplierGoodsExists,
  checkSupplierGoodsExists: checkSupplierGoodsExists,
  getStatistics: getSupplierGoodsStatistics,
  search: searchSupplierGoods,
  export: exportSupplierGoods,
  import: importSupplierGoods,
  downloadTemplate: downloadImportTemplate,
  getSupplierGoodsPage: getSupplierGoodsList // 为了兼容某些组件
} 