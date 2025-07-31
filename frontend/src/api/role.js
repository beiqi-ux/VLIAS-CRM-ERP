import request from '@/utils/request'

/**
 * 获取角色列表
 * @returns {Promise}
 */
export function getRoleList() {
  return request({
    url: '/api/sys/roles',
    method: 'get'
  })
}

/**
 * 分页获取角色
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getRolePage(params) {
  return request({
    url: '/api/sys/roles/page',
    method: 'get',
    params
  })
}

/**
 * 获取单个角色
 * @param {number} id - 角色ID
 * @returns {Promise}
 */
export function getRoleById(id) {
  return request({
    url: `/api/sys/roles/${id}`,
    method: 'get'
  })
}

/**
 * 创建角色
 * @param {Object} data - 角色信息
 * @returns {Promise}
 */
export function createRole(data) {
  return request({
    url: '/api/sys/roles',
    method: 'post',
    data
  })
}

/**
 * 更新角色
 * @param {number} id - 角色ID
 * @param {Object} data - 角色信息
 * @returns {Promise}
 */
export function updateRole(id, data) {
  return request({
    url: `/api/sys/roles/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除角色
 * @param {number} id - 角色ID
 * @returns {Promise}
 */
export function deleteRole(id) {
  return request({
    url: `/api/sys/roles/${id}`,
    method: 'delete'
  })
}

/**
 * 为角色分配权限
 * @param {number} roleId - 角色ID
 * @param {Array} permissionIds - 权限ID列表
 * @returns {Promise}
 */
export function assignPermissions(roleId, permissionIds) {
  return request({
    url: `/api/sys/roles/${roleId}/permissions`,
    method: 'post',
    data: permissionIds
  })
}

/**
 * 获取角色的权限ID列表
 * @param {number} roleId - 角色ID
 * @returns {Promise}
 */
export function getRolePermissionIds(roleId) {
  return request({
    url: `/api/sys/roles/${roleId}/permissions`,
    method: 'get'
  })
}

/**
 * 获取用户的角色列表
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export function getUserRoles(userId) {
  return request({
    url: `/api/sys/roles/users/${userId}/list`,
    method: 'get'
  })
}

/**
 * 获取用户的角色ID列表
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export function getUserRoleIds(userId) {
  return request({
    url: `/api/sys/roles/users/${userId}`,
    method: 'get'
  })
}

/**
 * 为用户分配角色
 * @param {number} userId - 用户ID
 * @param {Array} roleIds - 角色ID列表
 * @returns {Promise}
 */
export function assignUserRoles(userId, roleIds) {
  return request({
    url: `/api/sys/roles/users/${userId}`,
    method: 'post',
    data: roleIds
  })
} 