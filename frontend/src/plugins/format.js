import { formatDateTime, formatDate, formatId, formatImageUrl } from '@/utils/format'

/**
 * 格式化插件
 * 提供全局可用的格式化函数
 */
export default {
  install(app) {
    // 添加全局属性
    app.config.globalProperties.$formatters = {
      datetime: formatDateTime,
      date: formatDate,
      id: formatId,
      imageUrl: formatImageUrl
    }

    // 添加全局方法
    app.config.globalProperties.$formatDateTime = formatDateTime
    app.config.globalProperties.$formatDate = formatDate
    app.config.globalProperties.$formatId = formatId
    app.config.globalProperties.$formatImageUrl = formatImageUrl
  }
} 