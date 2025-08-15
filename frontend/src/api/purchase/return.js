import request from '@/utils/request'

const API_PREFIX = '/api/pur-returns'

export const purReturnApi = {
  // 分页查询
  getPage(params) {
    return request({
      url: API_PREFIX,
      method: 'get',
      params
    })
  },

  // 根据ID获取详情
  getById(id) {
    return request({
      url: `${API_PREFIX}/${id}`,
      method: 'get'
    })
  },

  // 根据单号获取详情
  getByNo(returnNo) {
    return request({
      url: `${API_PREFIX}/by-no/${returnNo}`,
      method: 'get'
    })
  },

  // 创建退货单
  create(data) {
    return request({
      url: API_PREFIX,
      method: 'post',
      data
    })
  },

  // 更新退货单
  update(id, data) {
    return request({
      url: `${API_PREFIX}/${id}`,
      method: 'put',
      data
    })
  },

  // 删除退货单
  delete(id) {
    return request({
      url: `${API_PREFIX}/${id}`,
      method: 'delete'
    })
  },

  // 批量删除
  batchDelete(ids) {
    return request({
      url: `${API_PREFIX}/batch`,
      method: 'delete',
      data: ids
    })
  },

  // 提交退货单
  submit(id) {
    return request({
      url: `${API_PREFIX}/${id}/submit`,
      method: 'post'
    })
  },

  // 审核退货单
  audit(id, data) {
    return request({
      url: `${API_PREFIX}/${id}/audit`,
      method: 'post',
      params: data
    })
  },

  // 确认退货
  confirm(id) {
    return request({
      url: `${API_PREFIX}/${id}/confirm`,
      method: 'post'
    })
  },

  // 取消退货单
  cancel(id, reason) {
    return request({
      url: `${API_PREFIX}/${id}/cancel`,
      method: 'post',
      params: { reason }
    })
  },

  // 复制退货单
  copy(id) {
    return request({
      url: `${API_PREFIX}/${id}/copy`,
      method: 'post'
    })
  },

  // 获取可退货明细
  getReturnableItems(receiptId) {
    return request({
      url: `${API_PREFIX}/returnable-items/${receiptId}`,
      method: 'get'
    })
  },

  // 获取退货明细
  getItems(returnId) {
    return request({
      url: `${API_PREFIX}/${returnId}/items`,
      method: 'get'
    })
  },

  // 获取待审核列表
  getPendingAudit() {
    return request({
      url: `${API_PREFIX}/pending-audit`,
      method: 'get'
    })
  },

  // 获取统计信息
  getStatistics(params) {
    return request({
      url: `${API_PREFIX}/statistics`,
      method: 'get',
      params
    })
  },

  // 导出
  export(params) {
    return request({
      url: `${API_PREFIX}/export`,
      method: 'get',
      params,
      responseType: 'blob'
    })
  },

  // 打印
  print(id) {
    return request({
      url: `${API_PREFIX}/${id}/print`,
      method: 'get',
      responseType: 'blob'
    })
  },

  // 生成退货单号
  generateNo() {
    return request({
      url: `${API_PREFIX}/generate-no`,
      method: 'post'
    })
  }
} 
 