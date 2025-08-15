import request from '@/utils/request'

// 仓库API
export const warehouseApi = {
  // 获取所有活跃仓库
  getAll() {
    return request({
      url: '/api/warehouses',
      method: 'get'
    })
  },

  // 获取仓库分页列表
  getPage(params) {
    return request({
      url: '/api/warehouses/page',
      method: 'get',
      params
    })
  },

  // 获取仓库详情
  getById(id) {
    return request({
      url: `/api/warehouses/${id}`,
      method: 'get'
    })
  },

  // 创建仓库
  create(data) {
    return request({
      url: '/api/warehouses',
      method: 'post',
      data
    })
  },

  // 更新仓库
  update(id, data) {
    return request({
      url: `/api/warehouses/${id}`,
      method: 'put',
      data
    })
  },

  // 删除仓库
  delete(id) {
    return request({
      url: `/api/warehouses/${id}`,
      method: 'delete'
    })
  },

  // 检查仓库编码是否存在
  checkCode(code, excludeId = null) {
    return request({
      url: '/api/warehouses/check-code',
      method: 'get',
      params: { code, excludeId }
    })
  },

  // 获取默认仓库
  getDefault() {
    return request({
      url: '/api/warehouses/default',
      method: 'get'
    })
  },

  // 设置默认仓库
  setDefault(id) {
    return request({
      url: `/api/warehouses/${id}/set-default`,
      method: 'post'
    })
  },

  // 获取仓库库存统计
  getStockStatistics(id) {
    return request({
      url: `/api/warehouses/${id}/stock-statistics`,
      method: 'get'
    })
  },

  // 获取仓库区域列表
  getAreas(id) {
    return request({
      url: `/api/warehouses/${id}/areas`,
      method: 'get'
    })
  }
} 