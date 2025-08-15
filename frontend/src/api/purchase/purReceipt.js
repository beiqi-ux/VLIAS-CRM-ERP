import request from '@/utils/request'

// 采购入库管理 API
export const purReceiptApi = {
  // 分页获取采购入库单列表
  getPurReceiptPage(params) {
    return request({
      url: '/api/pur-receipts',
      method: 'get',
      params
    })
  },

  // 获取采购入库单详情
  getPurReceiptById(id) {
    return request({
      url: `/api/pur-receipts/${id}`,
      method: 'get'
    })
  },

  // 根据入库单号获取详情
  getPurReceiptByNo(receiptNo) {
    return request({
      url: `/api/pur-receipts/no/${receiptNo}`,
      method: 'get'
    })
  },

  // 创建采购入库单
  createPurReceipt(data) {
    return request({
      url: '/api/pur-receipts',
      method: 'post',
      data
    })
  },

  // 从采购订单创建入库单
  createReceiptFromOrder(orderId, data) {
    return request({
      url: `/api/pur-receipts/from-order/${orderId}`,
      method: 'post',
      data
    })
  },

  // 更新采购入库单
  updatePurReceipt(id, data) {
    return request({
      url: `/api/pur-receipts/${id}`,
      method: 'put',
      data
    })
  },

  // 删除采购入库单
  deletePurReceipt(id) {
    return request({
      url: `/api/pur-receipts/${id}`,
      method: 'delete'
    })
  },

  // 提交采购入库单
  submitPurReceipt(id) {
    return request({
      url: `/api/pur-receipts/${id}/submit`,
      method: 'put'
    })
  },

  // 审核采购入库单
  auditPurReceipt(id, data) {
    return request({
      url: `/api/pur-receipts/${id}/audit`,
      method: 'put',
      data
    })
  },

  // 确认入库
  confirmReceipt(id) {
    return request({
      url: `/api/pur-receipts/${id}/confirm`,
      method: 'put'
    })
  },

  // 取消入库单
  cancelPurReceipt(id, reason) {
    return request({
      url: `/api/pur-receipts/${id}/cancel`,
      method: 'put',
      data: { reason }
    })
  },

  // 批量确认入库
  batchConfirmReceipt(ids) {
    return request({
      url: '/api/pur-receipts/batch-confirm',
      method: 'put',
      data: ids
    })
  },

  // 批量审核入库单
  batchAuditPurReceipt(ids, approved, auditRemark) {
    return request({
      url: '/api/pur-receipts/batch-audit',
      method: 'put',
      data: { ids, approved, auditRemark }
    })
  },

  // 获取采购订单的入库记录
  getReceiptsByOrderId(orderId) {
    return request({
      url: `/api/pur-receipts/order/${orderId}`,
      method: 'get'
    })
  },

  // 获取供应商的入库记录
  getReceiptsBySupplierId(supplierId) {
    return request({
      url: `/api/pur-receipts/supplier/${supplierId}`,
      method: 'get'
    })
  },

  // 获取仓库的入库记录
  getReceiptsByWarehouseId(warehouseId) {
    return request({
      url: `/api/pur-receipts/warehouse/${warehouseId}`,
      method: 'get'
    })
  },

  // 获取待审核的入库单
  getPendingAuditReceipts() {
    return request({
      url: '/api/pur-receipts/pending-audit',
      method: 'get'
    })
  },

  // 获取入库单统计信息
  getPurReceiptStatistics() {
    return request({
      url: '/api/pur-receipts/statistics',
      method: 'get'
    })
  },

  // 获取入库趋势数据
  getReceiptTrendData(params) {
    return request({
      url: '/api/pur-receipts/trend',
      method: 'get',
      params
    })
  },

  // 获取商品入库汇总
  getGoodsReceiptSummary(params) {
    return request({
      url: '/api/pur-receipts/goods-summary',
      method: 'get',
      params
    })
  },

  // 获取供应商入库汇总
  getSupplierReceiptSummary(params) {
    return request({
      url: '/api/pur-receipts/supplier-summary',
      method: 'get',
      params
    })
  },

  // 检查入库单号是否存在
  existsByReceiptNo(receiptNo) {
    return request({
      url: `/api/pur-receipts/exists/${receiptNo}`,
      method: 'get'
    })
  },

  // 检查采购订单是否已完全入库
  isOrderFullyReceived(orderId) {
    return request({
      url: `/api/pur-receipts/order/${orderId}/fully-received`,
      method: 'get'
    })
  },

  // 获取采购订单的入库进度
  getOrderReceiptProgress(orderId) {
    return request({
      url: `/api/pur-receipts/order/${orderId}/progress`,
      method: 'get'
    })
  },

  // 获取商品的最近入库价格
  getLatestReceiptPrice(goodsId, skuId) {
    return request({
      url: '/api/pur-receipts/latest-price',
      method: 'get',
      params: { goodsId, skuId }
    })
  },

  // 获取库存预警信息
  getStockWarningInfo() {
    return request({
      url: '/api/pur-receipts/stock-warning',
      method: 'get'
    })
  },

  // 生成入库单号
  generateReceiptNo() {
    return request({
      url: '/api/pur-receipts/generate-no',
      method: 'get'
    })
  },

  // 根据批次号查询入库记录
  getReceiptItemsByBatch(batchNumber) {
    return request({
      url: `/api/pur-receipts/batch/${batchNumber}`,
      method: 'get'
    })
  },

  // 获取即将到期的商品
  getExpiringItems(days) {
    return request({
      url: '/api/pur-receipts/expiring-items',
      method: 'get',
      params: { days }
    })
  },

  // 导出入库单数据
  exportReceiptData(params) {
    return request({
      url: '/api/pur-receipts/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  },

  // 导入入库单数据
  importReceiptData(fileData) {
    const formData = new FormData()
    formData.append('file', fileData)
    return request({
      url: '/api/pur-receipts/import',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 获取入库单明细
  getReceiptItems(receiptId) {
    return request({
      url: `/api/pur-receipts/${receiptId}/items`,
      method: 'get'
    })
  },

  // 保存入库单明细
  saveReceiptItems(receiptId, items) {
    return request({
      url: `/api/pur-receipts/${receiptId}/items`,
      method: 'put',
      data: items
    })
  },

  // 删除入库单明细
  deleteReceiptItems(receiptId) {
    return request({
      url: `/api/pur-receipts/${receiptId}/items`,
      method: 'delete'
    })
  },

  // 复制入库单
  copyPurReceipt(id) {
    return request({
      url: `/api/pur-receipts/${id}/copy`,
      method: 'post'
    })
  },

  // 生成入库单打印数据
  generateReceiptPrintData(id) {
    return request({
      url: `/api/pur-receipts/${id}/print-data`,
      method: 'get'
    })
  },

  // 获取入库单审核历史
  getReceiptAuditHistory(id) {
    return request({
      url: `/api/pur-receipts/${id}/audit-history`,
      method: 'get'
    })
  },

  // 获取入库状态选项
  getReceiptStatusOptions() {
    return [
      { value: 1, label: '草稿' },
      { value: 2, label: '待审核' },
      { value: 3, label: '已审核' },
      { value: 4, label: '已入库' },
      { value: 5, label: '已取消' }
    ]
  },

  // 获取入库类型选项
  getReceiptTypeOptions() {
    return [
      { value: 1, label: '采购入库' },
      { value: 2, label: '退货入库' },
      { value: 3, label: '调拨入库' },
      { value: 4, label: '其他入库' }
    ]
  }
}

export default purReceiptApi 
 