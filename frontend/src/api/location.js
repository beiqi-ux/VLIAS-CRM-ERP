import request from '@/utils/request'

/**
 * 库位管理API
 */
export const locationApi = {
  /**
   * 创建库位
   */
  create(data) {
    return request({
      url: '/api/inv/location',
      method: 'post',
      data
    })
  },

  /**
   * 更新库位
   */
  update(id, data) {
    return request({
      url: `/api/inv/location/${id}`,
      method: 'put',
      data
    })
  },

  /**
   * 删除库位
   */
  delete(id) {
    return request({
      url: `/api/inv/location/${id}`,
      method: 'delete'
    })
  },

  /**
   * 批量删除库位
   */
  batchDelete(ids) {
    return request({
      url: '/api/inv/location/batch',
      method: 'delete',
      data: ids
    })
  },

  /**
   * 查询库位详情
   */
  getById(id) {
    return request({
      url: `/api/inv/location/${id}`,
      method: 'get'
    })
  },

  /**
   * 分页查询库位
   */
  getPage(params) {
    return request({
      url: '/api/inv/location/page',
      method: 'get',
      params
    })
  },

  /**
   * 根据仓库ID查询库位列表
   */
  getByWarehouseId(warehouseId) {
    return request({
      url: `/api/inv/location/warehouse/${warehouseId}`,
      method: 'get'
    })
  },

  /**
   * 根据库区ID查询库位列表
   */
  getByAreaId(areaId) {
    return request({
      url: `/api/inv/location/area/${areaId}`,
      method: 'get'
    })
  },

  /**
   * 查询所有启用的库位
   */
  getActive() {
    return request({
      url: '/api/inv/location/active',
      method: 'get'
    })
  },

  /**
   * 启用库位
   */
  enable(id) {
    return request({
      url: `/api/inv/location/${id}/enable`,
      method: 'put'
    })
  },

  /**
   * 禁用库位
   */
  disable(id) {
    return request({
      url: `/api/inv/location/${id}/disable`,
      method: 'put'
    })
  },

  /**
   * 批量启用库位
   */
  batchEnable(ids) {
    return request({
      url: '/api/inv/location/batch/enable',
      method: 'put',
      data: ids
    })
  },

  /**
   * 批量禁用库位
   */
  batchDisable(ids) {
    return request({
      url: '/api/inv/location/batch/disable',
      method: 'put',
      data: ids
    })
  },

  /**
   * 检查库位编码
   */
  checkCode(warehouseId, locationCode, excludeId = null) {
    return request({
      url: '/api/inv/location/check-code',
      method: 'get',
      params: {
        warehouseId,
        locationCode,
        excludeId
      }
    })
  },

  /**
   * 统计仓库库位数量
   */
  countByWarehouse(warehouseId) {
    return request({
      url: `/api/inv/location/count/warehouse/${warehouseId}`,
      method: 'get'
    })
  },

  /**
   * 统计库区库位数量
   */
  countByArea(areaId) {
    return request({
      url: `/api/inv/location/count/area/${areaId}`,
      method: 'get'
    })
  },

  /**
   * 导出库位数据
   */
  export(params) {
    return request({
      url: '/api/inv/location/export',
      method: 'get',
      params
    })
  }
} 