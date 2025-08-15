import request from '@/utils/request'

// 采购订单管理 API
export const purchaseOrderApi = {
  // 获取采购订单列表
  getList(params) {
    return request({
      url: '/api/purchase-orders',
      method: 'get',
      params
    })
  },

  // 分页获取采购订单列表
  getPurchaseOrderPage(params) {
    return request({
      url: '/api/purchase-orders',
      method: 'get',
      params
    })
  },

  // 获取采购订单详情
  getById(id) {
    return request({
      url: `/api/purchase-orders/${id}`,
      method: 'get'
    })
  },

  // 创建采购订单
  create(data) {
    return request({
      url: '/api/purchase-orders',
      method: 'post',
      data
    })
  },

  // 更新采购订单
  update(id, data) {
    return request({
      url: `/api/purchase-orders/${id}`,
      method: 'put',
      data
    })
  },

  // 删除采购订单
  delete(id) {
    return request({
      url: `/api/purchase-orders/${id}`,
      method: 'delete'
    })
  },

  // 批量删除采购订单
  batchDelete(ids) {
    return request({
      url: '/api/purchase-orders/batch',
      method: 'delete',
      data: ids
    })
  },

  // 提交采购订单
  submit(id) {
    return request({
      url: `/api/purchase-orders/${id}/submit`,
      method: 'put'
    })
  },

  // 审核采购订单
  audit(id, data) {
    return request({
      url: `/api/purchase-orders/${id}/audit`,
      method: 'put',
      data
    })
  },

  // 取消采购订单
  cancel(id, reason) {
    return request({
      url: `/api/purchase-orders/${id}/cancel`,
      method: 'put',
      data: { reason }
    })
  },

  // 复制采购订单
  copy(id) {
    return request({
      url: `/api/purchase-orders/${id}/copy`,
      method: 'post'
    })
  },

  // 获取采购订单统计
  getStatistics(params) {
    return request({
      url: '/api/purchase-orders/statistics',
      method: 'get',
      params
    })
  },

  // 导出采购订单
  export(params) {
    return request({
      url: '/api/purchase-orders/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  },

  // 获取订单状态选项
  getStatusOptions() {
    return [
      { value: 'DRAFT', label: '草稿' },
      { value: 'PENDING_AUDIT', label: '待审核' },
      { value: 'AUDITED', label: '已审核' },
      { value: 'ORDERED', label: '已下单' },
      { value: 'PARTIAL_RECEIVED', label: '部分入库' },
      { value: 'COMPLETED', label: '已完成' },
      { value: 'CANCELLED', label: '已取消' }
    ]
  },

  // 通过ID获取采购订单
  getPurchaseOrderById(id) {
    return this.getById(id)
  },

  // 创建采购订单
  createPurchaseOrder(data) {
    return this.create(data)
  },

  // 更新采购订单
  updatePurchaseOrder(id, data) {
    return this.update(id, data)
  },

  // 提交采购订单
  submitPurchaseOrder(id) {
    return this.submit(id)
  },

  // 审核采购订单
  auditPurchaseOrder(id, data) {
    return this.audit(id, data)
  },

  // 复制采购订单
  copyPurchaseOrder(id) {
    return this.copy(id)
  },

  // 取消采购订单
  cancelPurchaseOrder(id, reason) {
    return this.cancel(id, reason)
  },

  // 删除采购订单
  deletePurchaseOrder(id) {
    return this.delete(id)
  },

  // 批量删除采购订单
  batchDeletePurchaseOrders(ids) {
    return this.batchDelete(ids)
  },

  // 获取采购订单统计
  getPurchaseOrderStatistics(params) {
    return this.getStatistics(params)
  },

  // 获取需要跟进的订单
  getOrdersNeedFollow(params) {
    return request({
      url: '/api/purchase-orders/need-follow',
      method: 'get',
      params
    })
  }
}

export default purchaseOrderApi 