import request from '@/utils/request'

// 采购退货管理 API
export const purReturnApi = {
  // 获取采购退货单列表
  getList(params) {
    return request({
      url: '/api/pur-returns',
      method: 'get',
      params
    })
  },

  // 分页获取采购退货单列表
  getPurReturnPage(params) {
    return request({
      url: '/api/pur-returns',
      method: 'get',
      params
    })
  },

  // 获取采购退货单详情
  getById(id) {
    return request({
      url: `/api/pur-returns/${id}`,
      method: 'get'
    })
  },

  // 根据退货单号获取详情
  getByNo(returnNo) {
    return request({
      url: `/api/pur-returns/by-no/${returnNo}`,
      method: 'get'
    })
  },

  // 创建采购退货单
  create(data) {
    return request({
      url: '/api/pur-returns',
      method: 'post',
      data
    })
  },

  // 更新采购退货单
  update(id, data) {
    return request({
      url: `/api/pur-returns/${id}`,
      method: 'put',
      data
    })
  },

  // 删除采购退货单
  delete(id) {
    return request({
      url: `/api/pur-returns/${id}`,
      method: 'delete'
    })
  },

  // 批量删除采购退货单
  batchDelete(ids) {
    return request({
      url: '/api/pur-returns/batch',
      method: 'delete',
      data: ids
    })
  },

  // 提交采购退货单
  submit(id) {
    return request({
      url: `/api/pur-returns/${id}/submit`,
      method: 'post'
    })
  },

  // 审核采购退货单
  audit(id, data) {
    return request({
      url: `/api/pur-returns/${id}/audit`,
      method: 'post',
      params: data
    })
  },

  // 确认退货
  confirm(id) {
    return request({
      url: `/api/pur-returns/${id}/confirm`,
      method: 'post'
    })
  },

  // 取消采购退货单
  cancel(id, reason) {
    return request({
      url: `/api/pur-returns/${id}/cancel`,
      method: 'post',
      params: { reason }
    })
  },

  // 复制采购退货单
  copy(id) {
    return request({
      url: `/api/pur-returns/${id}/copy`,
      method: 'post'
    })
  },

  // 根据入库单ID获取可退货明细
  getReturnableItems(receiptId) {
    return request({
      url: `/api/pur-returns/returnable-items/${receiptId}`,
      method: 'get'
    })
  },

  // 获取退货单明细列表
  getItems(id) {
    return request({
      url: `/api/pur-returns/${id}/items`,
      method: 'get'
    })
  },

  // 获取退货统计
  getStatistics(params) {
    return request({
      url: '/api/pur-returns/statistics',
      method: 'get',
      params
    })
  },

  // 获取待审核退货单
  getPendingAudit() {
    return request({
      url: '/api/pur-returns/pending-audit',
      method: 'get'
    })
  },

  // 生成退货单号
  generateReturnNo() {
    return request({
      url: '/api/pur-returns/generate-no',
      method: 'post'
    })
  },

  // 导出退货单
  export(params) {
    return request({
      url: '/api/pur-returns/export',
      method: 'get',
      params,
      responseType: 'blob'
    })
  },

  // 打印退货单
  print(id) {
    return request({
      url: `/api/pur-returns/${id}/print`,
      method: 'get',
      responseType: 'blob'
    })
  },

  // 获取退货状态选项
  getStatusOptions() {
    return [
      { value: 1, label: '草稿' },
      { value: 2, label: '待审核' },
      { value: 3, label: '已审核' },
      { value: 4, label: '已退货' },
      { value: 5, label: '已取消' }
    ]
  },

  // 获取退货原因类型选项
  getReasonTypeOptions() {
    return [
      { value: 1, label: '质量问题' },
      { value: 2, label: '运输损坏' },
      { value: 3, label: '发货错误' },
      { value: 4, label: '过期商品' },
      { value: 9, label: '其他' }
    ]
  },

  // 通过ID获取采购退货单
  getPurReturnById(id) {
    return this.getById(id)
  },

  // 通过退货单号获取采购退货单
  getPurReturnByNo(returnNo) {
    return this.getByNo(returnNo)
  },

  // 创建采购退货单
  createPurReturn(data) {
    return this.create(data)
  },

  // 更新采购退货单
  updatePurReturn(id, data) {
    return this.update(id, data)
  },

  // 提交采购退货单
  submitPurReturn(id) {
    return this.submit(id)
  },

  // 审核采购退货单
  auditPurReturn(id, data) {
    return this.audit(id, data)
  },

  // 确认退货
  confirmPurReturn(id) {
    return this.confirm(id)
  },

  // 复制采购退货单
  copyPurReturn(id) {
    return this.copy(id)
  },

  // 取消采购退货单
  cancelPurReturn(id, reason) {
    return this.cancel(id, reason)
  },

  // 删除采购退货单
  deletePurReturn(id) {
    return this.delete(id)
  },

  // 批量删除采购退货单
  batchDeletePurReturns(ids) {
    return this.batchDelete(ids)
  },

  // 获取采购退货统计
  getPurReturnStatistics(params) {
    return this.getStatistics(params)
  },

  // 根据入库单获取可退货明细
  getReturnableItemsByReceiptId(receiptId) {
    return this.getReturnableItems(receiptId)
  },

  // 获取退货单明细
  getPurReturnItems(id) {
    return this.getItems(id)
  }
}

export default purReturnApi 
 