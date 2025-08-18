/**
 * 供应商对账相关工具函数
 */

// 对账单状态枚举
export const RECONCILIATION_STATUS = {
  DRAFT: 1,        // 草稿
  PENDING: 2,      // 待确认
  CONFIRMED: 3,    // 已确认
  SETTLED: 4       // 已结算
}

// 状态文本映射
export const STATUS_TEXT_MAP = {
  [RECONCILIATION_STATUS.DRAFT]: '草稿',
  [RECONCILIATION_STATUS.PENDING]: '待确认',
  [RECONCILIATION_STATUS.CONFIRMED]: '已确认',
  [RECONCILIATION_STATUS.SETTLED]: '已结算'
}

// 状态标签类型映射
export const STATUS_TAG_TYPE_MAP = {
  [RECONCILIATION_STATUS.DRAFT]: 'info',
  [RECONCILIATION_STATUS.PENDING]: 'warning',
  [RECONCILIATION_STATUS.CONFIRMED]: 'success',
  [RECONCILIATION_STATUS.SETTLED]: 'primary'
}

// 状态选项列表（用于下拉选择）
export const STATUS_OPTIONS = [
  { label: '草稿', value: RECONCILIATION_STATUS.DRAFT },
  { label: '待确认', value: RECONCILIATION_STATUS.PENDING },
  { label: '已确认', value: RECONCILIATION_STATUS.CONFIRMED },
  { label: '已结算', value: RECONCILIATION_STATUS.SETTLED }
]

/**
 * 格式化金额
 * @param {number|string} amount - 金额
 * @returns {string} 格式化后的金额字符串
 */
export function formatAmount(amount) {
  if (amount === null || amount === undefined || amount === '') {
    return '0.00'
  }
  return Number(amount).toFixed(2)
}

/**
 * 格式化金额（带货币符号）
 * @param {number|string} amount - 金额
 * @param {string} currency - 货币符号，默认为¥
 * @returns {string} 格式化后的金额字符串
 */
export function formatCurrency(amount, currency = '¥') {
  return `${currency}${formatAmount(amount)}`
}

/**
 * 获取状态文本
 * @param {number} status - 状态值
 * @returns {string} 状态文本
 */
export function getStatusText(status) {
  return STATUS_TEXT_MAP[status] || '未知状态'
}

/**
 * 获取状态标签类型
 * @param {number} status - 状态值
 * @returns {string} 标签类型
 */
export function getStatusTagType(status) {
  return STATUS_TAG_TYPE_MAP[status] || 'info'
}

/**
 * 检查状态是否可以编辑
 * @param {number} status - 状态值
 * @returns {boolean} 是否可以编辑
 */
export function canEdit(status) {
  return status === RECONCILIATION_STATUS.DRAFT
}

/**
 * 检查状态是否可以确认
 * @param {number} status - 状态值
 * @returns {boolean} 是否可以确认
 */
export function canConfirm(status) {
  return status === RECONCILIATION_STATUS.PENDING
}

/**
 * 检查状态是否可以结算
 * @param {number} status - 状态值
 * @returns {boolean} 是否可以结算
 */
export function canSettle(status) {
  return status === RECONCILIATION_STATUS.CONFIRMED
}

/**
 * 检查状态是否可以删除
 * @param {number} status - 状态值
 * @returns {boolean} 是否可以删除
 */
export function canDelete(status) {
  return status === RECONCILIATION_STATUS.DRAFT || status === RECONCILIATION_STATUS.PENDING
}

/**
 * 验证对账单数据
 * @param {Object} data - 对账单数据
 * @returns {string[]} 错误信息数组
 */
export function validateReconciliationData(data) {
  const errors = []
  
  if (!data.supplierId) {
    errors.push('请选择供应商')
  }
  
  if (!data.startDate) {
    errors.push('请选择开始日期')
  }
  
  if (!data.endDate) {
    errors.push('请选择结束日期')
  }
  
  if (data.startDate && data.endDate && new Date(data.startDate) > new Date(data.endDate)) {
    errors.push('开始日期不能大于结束日期')
  }
  
  return errors
}

/**
 * 生成搜索表单的默认值
 * @returns {Object} 默认搜索表单数据
 */
export function getDefaultSearchForm() {
  return {
    supplierId: null,
    status: null,
    reconciliationNo: '',
    startDate: null,
    endDate: null
  }
}

/**
 * 生成表单的默认值
 * @returns {Object} 默认表单数据
 */
export function getDefaultFormData() {
  return {
    id: null,
    reconciliationNo: '',
    supplierId: null,
    supplierName: '',
    startDate: null,
    endDate: null,
    totalAmount: 0,
    paidAmount: 0,
    unpaidAmount: 0,
    status: RECONCILIATION_STATUS.DRAFT,
    remark: '',
    items: []
  }
}

/**
 * 计算对账单汇总金额
 * @param {Array} items - 对账明细列表
 * @returns {Object} 汇总信息
 */
export function calculateSummary(items) {
  if (!Array.isArray(items) || items.length === 0) {
    return {
      totalAmount: 0,
      totalQuantity: 0,
      itemCount: 0
    }
  }
  
  const summary = items.reduce((acc, item) => {
    acc.totalAmount += Number(item.totalAmount || 0)
    acc.totalQuantity += Number(item.quantity || 0)
    return acc
  }, {
    totalAmount: 0,
    totalQuantity: 0,
    itemCount: items.length
  })
  
  return summary
}

/**
 * 格式化日期范围
 * @param {string} startDate - 开始日期
 * @param {string} endDate - 结束日期
 * @returns {string} 格式化的日期范围
 */
export function formatDateRange(startDate, endDate) {
  if (!startDate && !endDate) {
    return ''
  }
  
  if (startDate && endDate) {
    return `${startDate} ~ ${endDate}`
  }
  
  return startDate || endDate
}

/**
 * 检查是否有选中的记录
 * @param {Array} selectedRows - 选中的行数据
 * @returns {boolean} 是否有选中记录
 */
export function hasSelection(selectedRows) {
  return Array.isArray(selectedRows) && selectedRows.length > 0
}

/**
 * 获取选中记录的ID列表
 * @param {Array} selectedRows - 选中的行数据
 * @returns {Array} ID列表
 */
export function getSelectedIds(selectedRows) {
  if (!Array.isArray(selectedRows)) {
    return []
  }
  return selectedRows.map(row => row.id).filter(id => id !== null && id !== undefined)
} 