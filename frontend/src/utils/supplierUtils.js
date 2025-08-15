/**
 * 供应商管理工具函数
 * 提供无感刷新、状态管理等功能
 */

import { ref } from 'vue'
import { ElMessage } from 'element-plus'

// 全局单例状态管理器
let globalStateManager = null

/**
 * 供应商状态管理类
 */
export class SupplierStateManager {
  constructor() {
    this.autoRefreshTimer = ref(null)
    this.lastUpdateTime = ref(null)
    this.isRefreshing = ref(false)
    this.pendingRefresh = false // 防止重复刷新的标志
  }

  /**
   * 启动自动刷新
   * @param {Function} refreshCallback 刷新回调函数
   * @param {number} delay 延迟时间（毫秒）
   */
  startAutoRefresh(refreshCallback, delay = 3000) {
    // 如果已经有定时器在运行，先清除
    this.stopAutoRefresh()
    
    // 如果正在刷新，标记为待刷新
    if (this.isRefreshing.value) {
      this.pendingRefresh = true
      return
    }
    
    this.autoRefreshTimer.value = setTimeout(() => {
      if (refreshCallback && typeof refreshCallback === 'function') {
        refreshCallback()
      }
      this.autoRefreshTimer.value = null
    }, delay)
  }

  /**
   * 停止自动刷新
   */
  stopAutoRefresh() {
    if (this.autoRefreshTimer.value) {
      clearTimeout(this.autoRefreshTimer.value)
      this.autoRefreshTimer.value = null
    }
  }

  /**
   * 静默刷新数据
   * @param {Function} refreshCallback 刷新回调函数
   * @param {Object} options 选项
   */
  async silentRefresh(refreshCallback, options = {}) {
    if (this.isRefreshing.value) {
      // 如果正在刷新，标记为待刷新
      this.pendingRefresh = true
      return
    }
    
    try {
      this.isRefreshing.value = true
      this.pendingRefresh = false
      
      if (refreshCallback && typeof refreshCallback === 'function') {
        await refreshCallback()
      }
      
      this.lastUpdateTime.value = new Date()
      
      // 显示静默刷新提示（可选）
      if (options.showNotification) {
        ElMessage({
          message: '数据已自动刷新',
          type: 'success',
          duration: 1500,
          showClose: false
        })
      }
    } catch (error) {
      console.error('静默刷新失败:', error)
      // 静默刷新失败时不显示错误提示
    } finally {
      this.isRefreshing.value = false
      
      // 如果有待刷新的请求，执行它
      if (this.pendingRefresh) {
        this.pendingRefresh = false
        setTimeout(() => {
          this.silentRefresh(refreshCallback, options)
        }, 100)
      }
    }
  }

  /**
   * 智能刷新策略（防重复刷新）
   * @param {Function} immediateCallback 立即刷新回调
   * @param {Function} delayedCallback 延迟刷新回调
   * @param {Object} options 选项
   */
  smartRefresh(immediateCallback, delayedCallback, options = {}) {
    const {
      immediateDelay = 0,
      delayedInterval = 3000,
      maxRetries = 2,
      showNotification = true
    } = options

    // 立即刷新
    if (immediateDelay > 0) {
      setTimeout(async () => {
        if (immediateCallback && typeof immediateCallback === 'function') {
          await this.silentRefresh(immediateCallback, { showNotification: false })
        }
      }, immediateDelay)
    } else if (immediateCallback && typeof immediateCallback === 'function') {
      this.silentRefresh(immediateCallback, { showNotification: false })
    }

    // 延迟刷新
    if (delayedCallback && typeof delayedCallback === 'function') {
      this.startAutoRefresh(delayedCallback, delayedInterval)
    }
  }

  /**
   * 清理资源
   */
  destroy() {
    this.stopAutoRefresh()
    this.isRefreshing.value = false
    this.lastUpdateTime.value = null
    this.pendingRefresh = false
  }
}

/**
 * 供应商操作结果处理器
 */
export class SupplierOperationHandler {
  /**
   * 处理操作成功
   * @param {Object} response API响应
   * @param {Function} refreshCallback 刷新回调
   * @param {Object} options 选项
   */
  static async handleSuccess(response, refreshCallback, options = {}) {
    const {
      showMessage = true,
      message = '操作成功',
      autoRefresh = true,
      refreshDelay = 0,
      refreshInterval = 3000
    } = options

    // 显示成功消息
    if (showMessage) {
      ElMessage.success(response.message || message)
    }

    // 自动刷新
    if (autoRefresh && refreshCallback) {
      // 使用全局单例状态管理器，防止重复创建
      const stateManager = getGlobalStateManager()
      
      if (refreshDelay > 0) {
        stateManager.smartRefresh(
          refreshCallback,
          refreshCallback,
          { immediateDelay: refreshDelay, delayedInterval: refreshInterval }
        )
      } else {
        stateManager.smartRefresh(
          refreshCallback,
          refreshCallback,
          { delayedInterval: refreshInterval }
        )
      }
    }
  }

  /**
   * 处理操作失败
   * @param {Object} response API响应
   * @param {Function} fallbackCallback 失败后的回调
   * @param {Object} options 选项
   */
  static async handleFailure(response, fallbackCallback, options = {}) {
    const {
      showMessage = true,
      message = '操作失败',
      autoRetry = false,
      retryCallback = null
    } = options

    // 显示失败消息
    if (showMessage) {
      ElMessage.error(response.message || message)
    }

    // 自动重试
    if (autoRetry && retryCallback) {
      setTimeout(() => {
        retryCallback()
      }, 2000)
    }

    // 执行失败后的回调
    if (fallbackCallback && typeof fallbackCallback === 'function') {
      await fallbackCallback()
    }
  }
}

/**
 * 供应商数据同步器
 */
export class SupplierDataSynchronizer {
  constructor() {
    this.syncQueue = []
    this.isSyncing = false
  }

  /**
   * 添加同步任务
   * @param {Function} syncTask 同步任务
   * @param {number} priority 优先级（数字越小优先级越高）
   */
  addSyncTask(syncTask, priority = 5) {
    this.syncQueue.push({ task: syncTask, priority })
    this.syncQueue.sort((a, b) => a.priority - b.priority)
    
    if (!this.isSyncing) {
      this.processSyncQueue()
    }
  }

  /**
   * 处理同步队列
   */
  async processSyncQueue() {
    if (this.isSyncing || this.syncQueue.length === 0) return
    
    this.isSyncing = true
    
    try {
      while (this.syncQueue.length > 0) {
        const { task } = this.syncQueue.shift()
        
        if (task && typeof task === 'function') {
          await task()
        }
        
        // 添加小延迟，避免过于频繁的API调用
        await new Promise(resolve => setTimeout(resolve, 100))
      }
    } catch (error) {
      console.error('处理同步队列失败:', error)
    } finally {
      this.isSyncing = false
    }
  }

  /**
   * 清空同步队列
   */
  clearSyncQueue() {
    this.syncQueue = []
  }
}

/**
 * 获取全局状态管理器实例（单例模式）
 */
export function getGlobalStateManager() {
  if (!globalStateManager) {
    globalStateManager = new SupplierStateManager()
  }
  return globalStateManager
}

/**
 * 创建供应商状态管理器实例
 * @deprecated 建议使用 getGlobalStateManager() 获取全局实例
 */
export function createSupplierStateManager() {
  console.warn('createSupplierStateManager 已废弃，请使用 getGlobalStateManager() 获取全局实例')
  return getGlobalStateManager()
}

/**
 * 创建供应商数据同步器实例
 */
export function createSupplierDataSynchronizer() {
  return new SupplierDataSynchronizer()
}

/**
 * 清理全局状态管理器
 */
export function destroyGlobalStateManager() {
  if (globalStateManager) {
    globalStateManager.destroy()
    globalStateManager = null
  }
}

export default {
  SupplierStateManager,
  SupplierOperationHandler,
  SupplierDataSynchronizer,
  getGlobalStateManager,
  createSupplierStateManager,
  createSupplierDataSynchronizer,
  destroyGlobalStateManager
} 