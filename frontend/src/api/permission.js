import request from '@/utils/request'

/**
 * 获取权限树
 * @returns {Promise}
 */
export function getPermissionTree() {
  return request({
    url: '/api/sys/permissions/tree',
    method: 'get'
  })
}

/**
 * 获取权限管理专用的权限树（包括禁用的权限）
 * @returns {Promise}
 */
export function getPermissionTreeForAdmin() {
  return request({
    url: '/api/sys/permissions/tree/admin',
    method: 'get'
  })
}

/**
 * 根据ID获取权限详情
 * @param {Number} id - 权限ID
 * @returns {Promise}
 */
export function getPermissionById(id) {
  return request({
    url: `/api/sys/permissions/${id}`,
    method: 'get'
  })
}

/**
 * 创建权限
 * @param {Object} data - 权限数据
 * @returns {Promise}
 */
export function createPermission(data) {
  return request({
    url: '/api/sys/permissions',
    method: 'post',
    data
  })
}

/**
 * 更新权限
 * @param {Number} id - 权限ID
 * @param {Object} data - 权限数据
 * @returns {Promise}
 */
export function updatePermission(id, data) {
  return request({
    url: `/api/sys/permissions/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除权限
 * @param {Number} id - 权限ID
 * @returns {Promise}
 */
export function deletePermission(id) {
  return request({
    url: `/api/sys/permissions/${id}`,
    method: 'delete'
  })
}

/**
 * 获取所有权限列表
 * @returns {Promise}
 */
export function getAllPermissions() {
  return request({
    url: '/api/sys/permissions',
    method: 'get'
  })
}

/**
 * 分页查询权限列表
 * @param {Object} params - 查询参数
 * @param {Number} params.page - 页码，从0开始
 * @param {Number} params.size - 每页大小
 * @param {String} params.permissionName - 权限名称（模糊查询）
 * @param {String} params.permissionCode - 权限编码（模糊查询）
 * @param {Number} params.permissionType - 权限类型
 * @param {Number} params.status - 状态
 * @param {Number} params.parentId - 父权限ID
 * @returns {Promise}
 */
export function getPermissionPage(params) {
  return request({
    url: '/api/sys/permissions/page',
    method: 'get',
    params
  })
}

  // ==================== 权限同步功能 ====================
  
/**
 * 同步所有权限
 * @returns {Promise}
 */
export function syncAllPermissions() {
  return request({
    url: '/api/sys/permission-sync/sync-all',
    method: 'post'
  })
}

/**
 * 同步指定模块权限
 * @param {String} moduleCode - 模块编码
 * @returns {Promise}
 */
export function syncModulePermissions(moduleCode) {
  return request({
    url: `/api/sys/permission-sync/sync-module/${moduleCode}`,
    method: 'post'
  })
}

/**
 * 验证权限配置
 * @returns {Promise}
 */
export function validatePermissionConfig() {
  return request({
    url: '/api/sys/permission-sync/validate-config',
    method: 'get'
  })
}

/**
 * 重置所有权限
 * @returns {Promise}
 */
export function resetAllPermissions() {
  return request({
    url: '/api/sys/permission-sync/reset-all',
    method: 'post'
  })
}

/**
 * 获取权限同步功能状态
 * @returns {Promise}
 */
export function getPermissionSyncStatus() {
  return request({
    url: '/api/sys/permission-sync/status',
    method: 'get'
  })
}

  /**
 * 根据用户ID获取权限列表
 * @param {Number} userId - 用户ID
 * @returns {Promise}
 */
export function getUserPermissions(userId) {
  return request({
    url: `/api/sys/permissions/users/${userId}`,
    method: 'get'
  })
}

/**
 * 根据权限类型获取权限列表
 * @param {Number} permissionType - 权限类型 1-一级 2-二级 3-三级
 * @returns {Promise}
 */
export function getPermissionsByType(permissionType) {
  return request({
    url: `/api/sys/permissions/type/${permissionType}`,
    method: 'get'
  })
}

/**
 * 获取权限的所有子孙权限
 * @param {Number} permissionId - 权限ID
 * @returns {Promise}
 */
export function getDescendantPermissions(permissionId) {
  return request({
    url: `/api/sys/permissions/${permissionId}/descendants`,
    method: 'get'
  })
}

/**
 * 检查权限继承关系
 * @param {Array} userPermissions - 用户权限编码列表
 * @param {String} requiredPermission - 需要检查的权限编码
 * @returns {Promise}
 */
export function checkPermissionWithInheritance(userPermissions, requiredPermission) {
  return request({
    url: '/api/sys/permissions/check-inheritance',
    method: 'post',
    data: {
      userPermissions,
      requiredPermission
    }
  })
}

/**
 * 更新权限状态
 * @param {Number} id - 权限ID
 * @param {Number} status - 状态 0-禁用 1-启用
 * @returns {Promise}
 */
export function updatePermissionStatus(id, status) {
  return request({
    url: `/api/sys/permissions/${id}/status`,
    method: 'patch',
    params: { status }
  })
} 