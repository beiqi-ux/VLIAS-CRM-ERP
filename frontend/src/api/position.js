import request from '@/utils/request'

/**
 * 获取岗位列表
 * @returns {Promise}
 */
export function getPositionList() {
  return request({
    url: '/api/position/list',
    method: 'get'
  })
}

/**
 * 获取岗位列表（包含组织和部门信息）
 * @param {number} orgId - 组织ID
 * @param {number} deptId - 部门ID
 * @returns {Promise}
 */
export function getPositionListWithInfo(orgId, deptId) {
  return request({
    url: '/api/position/list-with-info',
    method: 'get',
    params: { orgId, deptId }
  })
}

/**
 * 根据ID获取岗位
 * @param {number} id - 岗位ID
 * @returns {Promise}
 */
export function getPosition(id) {
  return request({
    url: `/api/position/${id}`,
    method: 'get'
  })
}

/**
 * 新增岗位
 * @param {Object} data - 岗位信息
 * @returns {Promise}
 */
export function addPosition(data) {
  return request({
    url: '/api/position',
    method: 'post',
    data
  })
}

/**
 * 更新岗位
 * @param {Object} data - 岗位信息
 * @returns {Promise}
 */
export function updatePosition(data) {
  return request({
    url: '/api/position',
    method: 'put',
    data
  })
}

/**
 * 删除岗位
 * @param {number} id - 岗位ID
 * @returns {Promise}
 */
export function deletePosition(id) {
  return request({
    url: `/api/position/${id}`,
    method: 'delete'
  })
}

/**
 * 根据组织ID获取岗位列表
 * @param {number} orgId - 组织ID
 * @returns {Promise}
 */
export function getPositionsByOrgId(orgId) {
  return request({
    url: `/api/position/org/${orgId}`,
    method: 'get'
  })
}

/**
 * 根据部门ID获取岗位列表
 * @param {number} deptId - 部门ID
 * @returns {Promise}
 */
export function getPositionsByDeptId(deptId) {
  return request({
    url: `/api/position/dept/${deptId}`,
    method: 'get'
  })
}

/**
 * 检查岗位编码是否存在
 * @param {number} orgId - 组织ID
 * @param {string} positionCode - 岗位编码
 * @param {number} id - 岗位ID（更新时使用）
 * @returns {Promise}
 */
export function checkPositionCodeExists(orgId, positionCode, id) {
  return request({
    url: '/api/position/check-code',
    method: 'get',
    params: { orgId, positionCode, id }
  })
} 