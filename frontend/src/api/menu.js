import request from '@/utils/request'

/**
 * 获取菜单列表
 * @returns {Promise}
 */
export function getMenuList() {
  return request({
    url: '/api/sys/menus',
    method: 'get'
  })
}

/**
 * 获取菜单树
 * @returns {Promise}
 */
export function getMenuTree() {
  return request({
    url: '/api/sys/menus/tree',
    method: 'get'
  })
}

/**
 * 获取单个菜单
 * @param {number} id - 菜单ID
 * @returns {Promise}
 */
export function getMenuById(id) {
  return request({
    url: `/api/sys/menus/${id}`,
    method: 'get'
  })
}

/**
 * 创建菜单
 * @param {Object} data - 菜单信息
 * @returns {Promise}
 */
export function createMenu(data) {
  return request({
    url: '/api/sys/menus',
    method: 'post',
    data
  })
}

/**
 * 更新菜单
 * @param {number} id - 菜单ID
 * @param {Object} data - 菜单信息
 * @returns {Promise}
 */
export function updateMenu(id, data) {
  return request({
    url: `/api/sys/menus/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除菜单
 * @param {number} id - 菜单ID
 * @returns {Promise}
 */
export function deleteMenu(id) {
  return request({
    url: `/api/sys/menus/${id}`,
    method: 'delete'
  })
}

/**
 * 根据角色ID获取菜单列表
 * @param {number} roleId - 角色ID
 * @returns {Promise}
 */
export function getMenusByRoleId(roleId) {
  return request({
    url: `/api/sys/menus/roles/${roleId}`,
    method: 'get'
  })
}

/**
 * 根据用户ID获取菜单列表
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export function getMenusByUserId(userId) {
  return request({
    url: `/api/sys/menus/users/${userId}`,
    method: 'get'
  })
}

/**
 * 根据用户ID获取菜单树
 * @param {number} userId - 用户ID
 * @returns {Promise}
 */
export function getUserMenuTree(userId) {
  return request({
    url: `/api/sys/menus/users/${userId}/tree`,
    method: 'get'
  })
}

/**
 * 切换菜单状态
 * @param {number} id - 菜单ID
 * @returns {Promise}
 */
export function toggleMenuStatus(id) {
  return request({
    url: `/api/sys/menus/${id}/toggle-status`,
    method: 'put'
  })
}

/**
 * 批量为所有菜单生成权限
 * @returns {Promise}
 */
export function batchGeneratePermissions() {
  return request({
    url: '/api/sys/menus/batch-generate-permissions',
    method: 'post'
  })
}

/**
 * 为指定菜单重新生成权限
 * @param {number} id - 菜单ID
 * @returns {Promise}
 */
export function regeneratePermissions(id) {
  return request({
    url: `/api/sys/menus/${id}/regenerate-permissions`,
    method: 'post'
  })
} 