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