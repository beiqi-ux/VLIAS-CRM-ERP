import request from '@/utils/request'

/**
 * 供应商对账管理API接口
 */
export const reconciliationApi = {
  // 获取分页列表
  getReconciliationPage(params) {
    return request({
      url: '/api/reconciliation/list',
      method: 'get',
      params
    })
  },

  // 根据ID获取详情
  getReconciliationById(id) {
    return request({
      url: `/api/pur-reconciliation/${id}`,
      method: 'get'
    })
  },

  // 根据对账单号获取详情
  getReconciliationByNo(reconciliationNo) {
    return request({
      url: `/api/pur-reconciliation/no/${reconciliationNo}`,
      method: 'get'
    })
  },

  // 创建对账单
  createReconciliation(data) {
    return request({
      url: '/api/pur-reconciliation',
      method: 'post',
      data
    })
  },

  // 更新对账单
  updateReconciliation(id, data) {
    return request({
      url: `/api/pur-reconciliation/${id}`,
      method: 'put',
      data
    })
  },

  // 删除对账单
  deleteReconciliation(id) {
    return request({
      url: `/api/pur-reconciliation/${id}`,
      method: 'delete'
    })
  },

  // 确认对账单
  confirmReconciliation(id) {
    return request({
      url: `/api/pur-reconciliation/${id}/confirm`,
      method: 'put'
    })
  },

  // 结算对账单
  settleReconciliation(id) {
    return request({
      url: `/api/pur-reconciliation/${id}/settle`,
      method: 'put'
    })
  },

  // 自动生成对账单
  autoGenerateReconciliation(params) {
    return request({
      url: '/api/pur-reconciliation/auto-generate',
      method: 'post',
      params
    })
  },

  // 生成对账单号
  generateReconciliationNo() {
    return request({
      url: '/api/pur-reconciliation/generate-no',
      method: 'get'
    })
  },

  // 获取供应商未结算对账单列表
  getUnsettledBySupplierId(supplierId) {
    return request({
      url: `/api/pur-reconciliation/unsettled/${supplierId}`,
      method: 'get'
    })
  },

  // 批量删除
  batchDeleteReconciliations(ids) {
    return request({
      url: '/api/pur-reconciliation/batch',
      method: 'delete',
      data: ids
    })
  },

  // 批量确认
  batchConfirmReconciliations(ids) {
    return request({
      url: '/api/pur-reconciliation/batch-confirm',
      method: 'put',
      data: ids
    })
  }
}

export default reconciliationApi 