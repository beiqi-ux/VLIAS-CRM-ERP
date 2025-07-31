import request from '@/utils/request'

/**
 * 获取用户列表（支持分页和条件查询）
 * @param {Object} params - 查询参数
 * @returns {Promise}
 */
export function getUserList(params) {
  return request({
    url: '/api/sys/users',
    method: 'get',
    params
  })
}

/**
 * 获取单个用户
 * @param {number} id - 用户ID
 * @returns {Promise}
 */
export function getUserById(id) {
  return request({
    url: `/api/sys/users/${id}`,
    method: 'get'
  })
}

/**
 * 创建用户
 * @param {Object} data - 用户信息
 * @returns {Promise}
 */
export function createUser(data) {
  return request({
    url: '/api/sys/users',
    method: 'post',
    data
  })
}

/**
 * 更新用户
 * @param {number} id - 用户ID
 * @param {Object} data - 用户信息
 * @returns {Promise}
 */
export function updateUser(id, data) {
  return request({
    url: `/api/sys/users/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除用户
 * @param {number} id - 用户ID
 * @returns {Promise}
 */
export function deleteUser(id) {
  return request({
    url: `/api/sys/users/${id}`,
    method: 'delete'
  })
}

/**
 * 修改用户密码
 * @param {number} id - 用户ID
 * @param {Object} data - 包含oldPassword和newPassword
 * @returns {Promise}
 */
export function changePassword(id, data) {
  return request({
    url: `/api/sys/users/${id}/change-password`,
    method: 'post',
    data
  })
}

/**
 * 重置用户密码
 * @param {number} id - 用户ID
 * @returns {Promise}
 */
export function resetPassword(id) {
  return request({
    url: `/api/sys/users/${id}/reset-password`,
    method: 'post'
  })
} 