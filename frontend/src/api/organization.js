import request from '@/utils/request'

/**
 * 获取组织机构列表
 * @returns {Promise}
 */
export function getOrganizationList() {
  return request({
    url: '/api/organization/list',
    method: 'get'
  })
}

/**
 * 获取组织机构树
 * @returns {Promise}
 */
export function getOrganizationTree() {
  return request({
    url: '/api/organization/tree',
    method: 'get'
  })
}

/**
 * 获取组织机构选项列表（用于下拉框）
 * @returns {Promise}
 */
export function getOrganizationOptions() {
  return request({
    url: '/api/organization/options',
    method: 'get'
  })
}

/**
 * 根据ID获取组织机构
 * @param {number} id - 组织机构ID
 * @returns {Promise}
 */
export function getOrganization(id) {
  return request({
    url: `/api/organization/${id}`,
    method: 'get'
  })
}

/**
 * 新增组织机构
 * @param {Object} data - 组织机构信息
 * @returns {Promise}
 */
export function addOrganization(data) {
  return request({
    url: '/api/organization',
    method: 'post',
    data
  })
}

/**
 * 更新组织机构
 * @param {Object} data - 组织机构信息
 * @returns {Promise}
 */
export function updateOrganization(data) {
  return request({
    url: '/api/organization',
    method: 'put',
    data
  })
}

/**
 * 删除组织机构
 * @param {number} id - 组织机构ID
 * @returns {Promise}
 */
export function deleteOrganization(id) {
  return request({
    url: `/api/organization/${id}`,
    method: 'delete'
  })
}

/**
 * 根据父ID获取子组织机构
 * @param {number} parentId - 父ID
 * @returns {Promise}
 */
export function getChildrenOrganizations(parentId) {
  return request({
    url: `/api/organization/children/${parentId}`,
    method: 'get'
  })
}

/**
 * 检查组织编码是否存在
 * @param {string} orgCode - 组织编码
 * @param {number} id - 组织ID（更新时使用）
 * @returns {Promise}
 */
export function checkOrgCodeExists(orgCode, id) {
  return request({
    url: '/api/organization/check-code',
    method: 'get',
    params: { orgCode, id }
  })
}

/**
 * 检查组织名称是否存在
 * @param {string} orgName - 组织名称
 * @param {number} id - 组织ID（更新时使用）
 * @returns {Promise}
 */
export function checkOrgNameExists(orgName, id) {
  return request({
    url: '/api/organization/check-name',
    method: 'get',
    params: { orgName, id }
  })
} 