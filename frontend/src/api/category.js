import request from '@/utils/request'

/**
 * 获取分类列表
 * @returns {Promise}
 */
export function getCategoryList() {
  return request({
    url: '/api/prod/categories',
    method: 'get'
  })
}

/**
 * 根据ID获取分类详情
 * @param {Number} id - 分类ID
 * @returns {Promise}
 */
export function getCategoryById(id) {
  return request({
    url: `/api/prod/categories/${id}`,
    method: 'get'
  })
}

/**
 * 获取根分类（一级分类）
 * @returns {Promise}
 */
export function getRootCategories() {
  return request({
    url: '/api/prod/categories/root',
    method: 'get'
  })
}

/**
 * 根据父级ID获取子分类
 * @param {Number} parentId - 父级ID
 * @returns {Promise}
 */
export function getCategoriesByParent(parentId) {
  return request({
    url: `/api/prod/categories/parent/${parentId}`,
    method: 'get'
  })
}

/**
 * 根据层级获取分类
 * @param {Number} level - 层级
 * @returns {Promise}
 */
export function getCategoriesByLevel(level) {
  return request({
    url: `/api/prod/categories/level/${level}`,
    method: 'get'
  })
}

/**
 * 获取显示的分类
 * @returns {Promise}
 */
export function getVisibleCategories() {
  return request({
    url: '/api/prod/categories/visible',
    method: 'get'
  })
}

/**
 * 搜索分类
 * @param {String} categoryName - 分类名称
 * @returns {Promise}
 */
export function searchCategories(categoryName) {
  return request({
    url: '/api/prod/categories/search',
    method: 'get',
    params: { categoryName }
  })
}

/**
 * 构建分类树
 * @returns {Promise}
 */
export function getCategoryTree() {
  return request({
    url: '/api/prod/categories/tree',
    method: 'get'
  })
}

/**
 * 构建管理后台分类树（包括禁用状态）
 * @returns {Promise}
 */
export function getAdminCategoryTree() {
  return request({
    url: '/api/prod/categories/admin-tree',
    method: 'get'
  })
}

/**
 * 创建分类
 * @param {Object} data - 分类数据
 * @returns {Promise}
 */
export function createCategory(data) {
  return request({
    url: '/api/prod/categories',
    method: 'post',
    data
  })
}

/**
 * 更新分类
 * @param {Number} id - 分类ID
 * @param {Object} data - 分类数据
 * @returns {Promise}
 */
export function updateCategory(id, data) {
  return request({
    url: `/api/prod/categories/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除分类（软删除）
 * @param {Number} id - 分类ID
 * @returns {Promise}
 */
export function deleteCategory(id) {
  return request({
    url: `/api/prod/categories/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除分类（软删除）
 * @param {Array} ids - 分类ID列表
 * @returns {Promise}
 */
export function batchDeleteCategories(ids) {
  return request({
    url: '/api/prod/categories/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 启用分类
 * @param {Number} id - 分类ID
 * @returns {Promise}
 */
export function enableCategory(id) {
  return request({
    url: `/api/prod/categories/${id}/enable`,
    method: 'put'
  })
}

/**
 * 禁用分类
 * @param {Number} id - 分类ID
 * @returns {Promise}
 */
export function disableCategory(id) {
  return request({
    url: `/api/prod/categories/${id}/disable`,
    method: 'put'
  })
}

/**
 * 显示分类
 * @param {Number} id - 分类ID
 * @returns {Promise}
 */
export function showCategory(id) {
  return request({
    url: `/api/prod/categories/${id}/show`,
    method: 'put'
  })
}

/**
 * 隐藏分类
 * @param {Number} id - 分类ID
 * @returns {Promise}
 */
export function hideCategory(id) {
  return request({
    url: `/api/prod/categories/${id}/hide`,
    method: 'put'
  })
}

/**
 * 检查分类名称是否存在（同级下）
 * @param {String} categoryName - 分类名称
 * @param {Number} parentId - 父级ID
 * @returns {Promise}
 */
export function checkCategoryName(categoryName, parentId = 0) {
  return request({
    url: '/api/prod/categories/check-name',
    method: 'get',
    params: { categoryName, parentId }
  })
}

/**
 * 统计父级下的子分类数量
 * @param {Number} parentId - 父级ID
 * @returns {Promise}
 */
export function countByParent(parentId) {
  return request({
    url: `/api/prod/categories/count/parent/${parentId}`,
    method: 'get'
  })
}

/**
 * 查询所有子级分类ID（用于级联删除）
 * @param {Number} parentId - 父级ID
 * @returns {Promise}
 */
export function getChildCategoryIds(parentId) {
  return request({
    url: `/api/prod/categories/child-ids/${parentId}`,
    method: 'get'
  })
}

/**
 * 递归查询所有下级分类ID
 * @param {Number} parentId - 父级ID
 * @returns {Promise}
 */
export function getAllChildCategoryIds(parentId) {
  return request({
    url: `/api/prod/categories/all-child-ids/${parentId}`,
    method: 'get'
  })
} 