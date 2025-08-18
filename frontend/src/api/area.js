import request from '@/utils/request'

/**
 * 库区管理API
 */
export const areaApi = {
  /**
   * 创建库区
   */
  create(data) {
    return request({
      url: '/api/inv/area',
      method: 'post',
      data
    })
  },

  /**
   * 更新库区
   */
  update(id, data) {
    return request({
      url: `/api/inv/area/${id}`,
      method: 'put',
      data
    })
  },

  /**
   * 删除库区
   */
  delete(id) {
    return request({
      url: `/api/inv/area/${id}`,
      method: 'delete'
    })
  },

  /**
   * 批量删除库区
   */
  batchDelete(ids) {
    return request({
      url: '/api/inv/area/batch',
      method: 'delete',
      data: ids
    })
  },

  /**
   * 查询库区详情
   */
  getById(id) {
    return request({
      url: `/api/inv/area/${id}`,
      method: 'get'
    })
  },

  /**
   * 分页查询库区
   */
  getPage(params) {
    return request({
      url: '/api/inv/area/page',
      method: 'get',
      params
    })
  },

  /**
   * 根据仓库查询库区
   */
  getByWarehouse(warehouseId) {
    return request({
      url: `/api/inv/area/warehouse/${warehouseId}`,
      method: 'get'
    })
  },

  /**
   * 查询所有库区
   */
  getAll() {
    return request({
      url: '/api/inv/area/all',
      method: 'get'
    })
  },

  /**
   * 查询启用库区
   */
  getActive() {
    return request({
      url: '/api/inv/area/active',
      method: 'get'
    })
  },

  /**
   * 启用库区
   */
  enable(id) {
    return request({
      url: `/api/inv/area/${id}/enable`,
      method: 'put'
    })
  },

  /**
   * 禁用库区
   */
  disable(id) {
    return request({
      url: `/api/inv/area/${id}/disable`,
      method: 'put'
    })
  },

  /**
   * 批量启用库区
   */
  batchEnable(ids) {
    return request({
      url: '/api/inv/area/batch/enable',
      method: 'put',
      data: ids
    })
  },

  /**
   * 批量禁用库区
   */
  batchDisable(ids) {
    return request({
      url: '/api/inv/area/batch/disable',
      method: 'put',
      data: ids
    })
  },

  /**
   * 检查库区编码
   */
  checkCode(warehouseId, areaCode, excludeId = null) {
    return request({
      url: '/api/inv/area/check-code',
      method: 'get',
      params: {
        warehouseId,
        areaCode,
        excludeId
      }
    })
  },

  /**
   * 统计库区库位数量
   */
  countLocations(areaId) {
    return request({
      url: `/api/inv/area/${areaId}/locations/count`,
      method: 'get'
    })
  },

  /**
   * 导出库区数据
   */
  export(params) {
    return request({
      url: '/api/inv/area/export',
      method: 'get',
      params
    })
  }
} 