import request from '@/utils/request'

/**
 * 用户登录
 * @param {Object} data - 登录信息
 * @returns {Promise}
 */
export function login(data) {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

/**
 * 用户登出
 * @returns {Promise}
 */
export function logout() {
  return request({
    url: '/api/auth/logout',
    method: 'post'
  })
}

/**
 * 刷新令牌
 * @param {string} token - 旧令牌
 * @returns {Promise}
 */
export function refreshToken(token) {
  return request({
    url: '/api/auth/refresh',
    method: 'post',
    params: { token }
  })
}

/**
 * 获取当前用户信息
 * @returns {Promise}
 */
export function getUserInfo() {
  return request({
    url: '/api/auth/info',
    method: 'get'
  })
} 