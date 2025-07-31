import request from '@/utils/request'

/**
 * 获取权限列表
 * @returns {Promise}
 */
export function getPermissionList() {
  return request({
    url: '/api/sys/permissions',
    method: 'get'
  })
}

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
 * 获取单个权限
 * @param {number} id - 权限ID
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
 * @param {Object} data - 权限信息
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
 * @param {number} id - 权限ID
 * @param {Object} data - 权限信息
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
 * @param {number} id - 权限ID
 * @returns {Promise}
 */
export function deletePermission(id) {
  return request({
    url: `/api/sys/permissions/${id}`,
    method: 'delete'
  })
}

/**
 * 根据角色ID获取权限列表
 * @param {number} roleId - 角色ID
 * @returns {Promise}
 */
export function getPermissionsByRoleId(roleId) {
  return request({
    url: `/api/sys/permissions/roles/${roleId}`,
    method: 'get'
  })
}

/**
 * 根据用户ID获取权限列表
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export function getPermissionsByUserId(userId) {
  return request({
    url: `/api/sys/permissions/users/${userId}`,
    method: 'get'
  })
} 