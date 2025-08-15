import request from '@/utils/request'

// 供应商管理 API
export const supplierApi = {
  // 分页获取供应商列表
  getSupplierPage(params) {
    return request({
      url: '/api/purchase/suppliers',
      method: 'get',
      params
    })
  },

  // 获取供应商详情
  getSupplierById(id) {
    return request({
      url: `/api/purchase/suppliers/${id}`,
      method: 'get'
    })
  },

  // 创建供应商
  createSupplier(data) {
    return request({
      url: '/api/purchase/suppliers',
      method: 'post',
      data
    })
  },

  // 更新供应商
  updateSupplier(id, data) {
    return request({
      url: `/api/purchase/suppliers/${id}`,
      method: 'put',
      data
    })
  },

  // 删除供应商
  deleteSupplier(id) {
    return request({
      url: `/api/purchase/suppliers/${id}`,
      method: 'delete'
    })
  },

  // 批量删除供应商
  deleteSuppliers(ids) {
    return request({
      url: '/api/purchase/suppliers/batch',
      method: 'delete',
      data: ids
    })
  },

  // 更新供应商状态
  updateSupplierStatus(id, status) {
    return request({
      url: `/api/purchase/suppliers/${id}/status`,
      method: 'patch',
      data: { status }
    })
  },

  // 获取所有正常状态的供应商
  getAllActiveSuppliers() {
    return request({
      url: '/api/purchase/suppliers/active',
      method: 'get'
    })
  },

  // 检查供应商编码
  checkSupplierCode(supplierCode, excludeId) {
    return request({
      url: '/api/purchase/suppliers/check-code',
      method: 'get',
      params: { supplierCode, excludeId }
    })
  },

  // 检查供应商名称
  checkSupplierName(supplierName, excludeId) {
    return request({
      url: '/api/purchase/suppliers/check-name',
      method: 'get',
      params: { supplierName, excludeId }
    })
  },

  // 获取供应商统计信息
  getSupplierStats() {
    return request({
      url: '/api/purchase/suppliers/stats',
      method: 'get'
    })
  }
}

export default supplierApi 