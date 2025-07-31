import request from '@/utils/request'

/**
 * 获取部门列表
 * @returns {Promise}
 */
export function getDepartmentList() {
  return request({
    url: '/api/department/list',
    method: 'get'
  })
}

/**
 * 获取部门树
 * @param {number} orgId - 组织ID
 * @returns {Promise}
 */
export function getDepartmentTree(orgId) {
  return request({
    url: '/api/department/tree',
    method: 'get',
    params: { orgId }
  })
}

/**
 * 根据ID获取部门
 * @param {number} id - 部门ID
 * @returns {Promise}
 */
export function getDepartment(id) {
  return request({
    url: `/api/department/${id}`,
    method: 'get'
  })
}

/**
 * 新增部门
 * @param {Object} data - 部门信息
 * @returns {Promise}
 */
export function addDepartment(data) {
  return request({
    url: '/api/department',
    method: 'post',
    data
  })
}

/**
 * 更新部门
 * @param {Object} data - 部门信息
 * @returns {Promise}
 */
export function updateDepartment(data) {
  return request({
    url: '/api/department',
    method: 'put',
    data
  })
}

/**
 * 删除部门
 * @param {number} id - 部门ID
 * @returns {Promise}
 */
export function deleteDepartment(id) {
  return request({
    url: `/api/department/${id}`,
    method: 'delete'
  })
}

/**
 * 根据组织ID获取部门列表
 * @param {number} orgId - 组织ID
 * @returns {Promise}
 */
export function getDepartmentsByOrgId(orgId) {
  return request({
    url: `/api/department/org/${orgId}`,
    method: 'get'
  })
}

/**
 * 根据父ID获取子部门
 * @param {number} parentId - 父ID
 * @returns {Promise}
 */
export function getChildrenDepartments(parentId) {
  return request({
    url: `/api/department/children/${parentId}`,
    method: 'get'
  })
}

/**
 * 检查部门编码是否存在
 * @param {number} orgId - 组织ID
 * @param {string} deptCode - 部门编码
 * @param {number} id - 部门ID（更新时使用）
 * @returns {Promise}
 */
export function checkDeptCodeExists(orgId, deptCode, id) {
  return request({
    url: '/api/department/check-code',
    method: 'get',
    params: { orgId, deptCode, id }
  })
} 