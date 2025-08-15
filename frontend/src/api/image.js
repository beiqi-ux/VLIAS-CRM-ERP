import request from '@/utils/request'

/**
 * 根据ID获取图片详情
 * @param {Number} id - 图片ID
 * @returns {Promise}
 */
export function getImageById(id) {
  return request({
    url: `/api/prod/images/${id}`,
    method: 'get'
  })
}

/**
 * 根据商品ID获取图片列表
 * @param {Number} goodsId - 商品ID
 * @returns {Promise}
 */
export function getImagesByGoodsId(goodsId) {
  return request({
    url: `/api/prod/images/goods/${goodsId}`,
    method: 'get'
  })
}

/**
 * 根据商品ID获取主图
 * @param {Number} goodsId - 商品ID
 * @returns {Promise}
 */
export function getMainImageByGoodsId(goodsId) {
  return request({
    url: `/api/prod/images/goods/${goodsId}/main`,
    method: 'get'
  })
}

/**
 * 创建图片
 * @param {Object} data - 图片数据
 * @returns {Promise}
 */
export function createImage(data) {
  return request({
    url: '/api/prod/images',
    method: 'post',
    data
  })
}

/**
 * 批量创建图片
 * @param {Array} images - 图片列表
 * @returns {Promise}
 */
export function createImages(images) {
  return request({
    url: '/api/prod/images/batch',
    method: 'post',
    data: images
  })
}

/**
 * 更新图片
 * @param {Number} id - 图片ID
 * @param {Object} data - 图片数据
 * @returns {Promise}
 */
export function updateImage(id, data) {
  return request({
    url: `/api/prod/images/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除图片（软删除）
 * @param {Number} id - 图片ID
 * @returns {Promise}
 */
export function deleteImage(id) {
  return request({
    url: `/api/prod/images/${id}`,
    method: 'delete'
  })
}

/**
 * 批量删除图片（软删除）
 * @param {Array} ids - 图片ID列表
 * @returns {Promise}
 */
export function batchDeleteImages(ids) {
  return request({
    url: '/api/prod/images/batch',
    method: 'delete',
    data: ids
  })
}

/**
 * 删除商品的所有图片
 * @param {Number} goodsId - 商品ID
 * @returns {Promise}
 */
export function deleteImagesByGoodsId(goodsId) {
  return request({
    url: `/api/prod/images/goods/${goodsId}`,
    method: 'delete'
  })
}

/**
 * 设置主图
 * @param {Number} id - 图片ID
 * @returns {Promise}
 */
export function setAsMainImage(id) {
  return request({
    url: `/api/prod/images/${id}/set-main`,
    method: 'put'
  })
}

/**
 * 取消商品的所有主图状态
 * @param {Number} goodsId - 商品ID
 * @returns {Promise}
 */
export function clearMainImageByGoodsId(goodsId) {
  return request({
    url: `/api/prod/images/goods/${goodsId}/clear-main`,
    method: 'put'
  })
}

/**
 * 统计商品图片数量
 * @param {Number} goodsId - 商品ID
 * @returns {Promise}
 */
export function countByGoodsId(goodsId) {
  return request({
    url: `/api/prod/images/count/goods/${goodsId}`,
    method: 'get'
  })
} 