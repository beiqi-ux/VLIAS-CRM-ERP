import request from '@/utils/request'

/**
 * 上传通用文件
 * @param {FormData} formData - 包含文件的FormData对象
 * @param {string} subDir - 子目录名称
 * @returns {Promise}
 */
export function uploadFile(formData, subDir = 'common') {
  return request({
    url: '/api/files/upload',
    method: 'post',
    params: { subDir },
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传商品图片
 * @param {FormData} formData - 包含文件和商品信息的FormData对象
 * @returns {Promise}
 */
export function uploadProductImage(formData) {
  return request({
    url: '/api/files/product-image',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 批量上传商品图片
 * @param {FormData} formData - 包含文件数组和商品信息的FormData对象
 * @returns {Promise}
 */
export function uploadProductImages(formData) {
  return request({
    url: '/api/files/product-images',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 上传用户头像
 * @param {FormData} formData - 包含头像文件的FormData对象
 * @returns {Promise}
 */
export function uploadAvatar(formData) {
  return request({
    url: '/api/files/avatar',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除文件
 * @param {string} filePath - 文件路径
 * @returns {Promise}
 */
export function deleteFile(filePath) {
  return request({
    url: '/api/files/delete',
    method: 'delete',
    params: { filePath }
  })
} 