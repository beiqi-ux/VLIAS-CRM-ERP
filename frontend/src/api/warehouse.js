import request from '@/utils/request'

/**
 * 仓库管理API
 */
export const warehouseApi = {
  /**
   * 创建仓库
   */
  create(data) {
    return request({
      url: '/api/inv/warehouse',
      method: 'post',
      data
    })
  },

  /**
   * 更新仓库
   */
  update(id, data) {
    return request({
      url: `/api/inv/warehouse/${id}`,
      method: 'put',
      data
    })
  },

  /**
   * 删除仓库
   */
  delete(id) {
    return request({
      url: `/api/inv/warehouse/${id}`,
      method: 'delete'
    })
  },

  /**
   * 批量删除仓库
   */
  batchDelete(ids) {
    return request({
      url: '/api/inv/warehouse/batch',
      method: 'delete',
      data: ids
    })
  },

  /**
   * 查询仓库详情
   */
  getById(id) {
    return request({
      url: `/api/inv/warehouse/${id}`,
      method: 'get'
    })
  },

  /**
   * 分页查询仓库
   */
  getPage(params) {
    return request({
      url: '/api/inv/warehouse/page',
      method: 'get',
      params
    })
  },

  /**
   * 查询所有仓库
   */
  getAll() {
    return request({
      url: '/api/inv/warehouse/all',
      method: 'get'
    })
  },

  /**
   * 查询启用仓库
   */
  getActive() {
    return request({
      url: '/api/inv/warehouse/active',
      method: 'get'
    })
  },

  /**
   * 查询仓库列表（getAll的别名，保持向后兼容）
   */
  getList() {
    return this.getAll()
  },

  /**
   * 启用仓库
   */
  enable(id) {
    return request({
      url: `/api/inv/warehouse/${id}/enable`,
      method: 'put'
    })
  },

  /**
   * 禁用仓库
   */
  disable(id) {
    return request({
      url: `/api/inv/warehouse/${id}/disable`,
      method: 'put'
    })
  },

  /**
   * 批量启用仓库
   */
  batchEnable(ids) {
    return request({
      url: '/api/inv/warehouse/batch/enable',
      method: 'put',
      data: ids
    })
  },

  /**
   * 批量禁用仓库
   */
  batchDisable(ids) {
    return request({
      url: '/api/inv/warehouse/batch/disable',
      method: 'put',
      data: ids
    })
  },

  /**
   * 设置默认仓库
   */
  setDefault(id) {
    return request({
      url: `/api/inv/warehouse/${id}/default`,
      method: 'put'
    })
  },

  /**
   * 查询默认仓库
   */
  getDefault() {
    return request({
      url: '/api/inv/warehouse/default',
      method: 'get'
    })
  },

  /**
   * 检查仓库编码
   */
  checkCode(warehouseCode, excludeId = null) {
    return request({
      url: '/api/inv/warehouse/check-code',
      method: 'get',
      params: {
        warehouseCode,
        excludeId
      }
    })
  },

  /**
   * 导出仓库数据
   */
  export(params) {
    return request({
      url: '/api/inv/warehouse/export',
      method: 'get',
      params
    })
  }
} 