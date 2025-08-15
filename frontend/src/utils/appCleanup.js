/**
 * 应用级别清理工具
 * 用于在应用退出时清理全局资源
 */

import { destroyGlobalStateManager } from './supplierUtils'

/**
 * 应用清理管理器
 */
class AppCleanupManager {
  constructor() {
    this.cleanupTasks = []
    this.isCleaning = false
  }

  /**
   * 注册清理任务
   * @param {Function} task 清理任务函数
   * @param {number} priority 优先级（数字越小优先级越高）
   */
  registerCleanupTask(task, priority = 5) {
    if (typeof task === 'function') {
      this.cleanupTasks.push({ task, priority })
      this.cleanupTasks.sort((a, b) => a.priority - b.priority)
    }
  }

  /**
   * 执行所有清理任务
   */
  async executeCleanup() {
    if (this.isCleaning) return
    
    this.isCleaning = true
    console.log('[应用清理] 开始执行清理任务...')
    
    try {
      // 执行注册的清理任务
      for (const { task } of this.cleanupTasks) {
        try {
          await task()
        } catch (error) {
          console.error('[应用清理] 清理任务执行失败:', error)
        }
      }
      
      // 执行内置清理任务
      await this.executeBuiltinCleanup()
      
      console.log('[应用清理] 所有清理任务执行完成')
    } catch (error) {
      console.error('[应用清理] 清理过程出错:', error)
    } finally {
      this.isCleaning = false
    }
  }

  /**
   * 执行内置清理任务
   */
  async executeBuiltinCleanup() {
    console.log('[应用清理] 执行内置清理任务...')
    
    // 清理供应商状态管理器
    try {
      destroyGlobalStateManager()
      console.log('[应用清理] 供应商状态管理器已清理')
    } catch (error) {
      console.error('[应用清理] 清理供应商状态管理器失败:', error)
    }
    
    // 清理定时器
    try {
      this.clearAllTimers()
      console.log('[应用清理] 定时器已清理')
    } catch (error) {
      console.error('[应用清理] 清理定时器失败:', error)
    }
    
    // 清理事件监听器
    try {
      this.removeAllEventListeners()
      console.log('[应用清理] 事件监听器已清理')
    } catch (error) {
      console.error('[应用清理] 清理事件监听器失败:', error)
    }
  }

  /**
   * 清理所有定时器
   */
  clearAllTimers() {
    // 清理setTimeout
    const highestTimeoutId = setTimeout(() => {}, 0)
    for (let i = 0; i < highestTimeoutId; i++) {
      clearTimeout(i)
    }
    
    // 清理setInterval
    const highestIntervalId = setInterval(() => {}, 0)
    for (let i = 0; i < highestIntervalId; i++) {
      clearInterval(i)
    }
  }

  /**
   * 移除所有事件监听器
   */
  removeAllEventListeners() {
    // 移除window事件监听器
    const events = ['beforeunload', 'unload', 'pagehide', 'visibilitychange']
    events.forEach(event => {
      window.removeEventListener(event, this.handlePageUnload)
    })
  }

  /**
   * 页面卸载处理
   */
  handlePageUnload = async () => {
    console.log('[应用清理] 页面即将卸载，执行清理...')
    await this.executeCleanup()
  }

  /**
   * 初始化清理管理器
   */
  init() {
    // 注册页面卸载事件
    window.addEventListener('beforeunload', this.handlePageUnload)
    window.addEventListener('unload', this.handlePageUnload)
    window.addEventListener('pagehide', this.handlePageUnload)
    
    // 注册页面可见性变化事件
    document.addEventListener('visibilitychange', () => {
      if (document.visibilityState === 'hidden') {
        console.log('[应用清理] 页面隐藏，准备清理...')
        // 页面隐藏时延迟执行清理，避免影响用户体验
        setTimeout(() => {
          this.executeCleanup()
        }, 1000)
      }
    })
    
    console.log('[应用清理] 清理管理器已初始化')
  }

  /**
   * 销毁清理管理器
   */
  destroy() {
    this.removeAllEventListeners()
    this.cleanupTasks = []
    this.isCleaning = false
    console.log('[应用清理] 清理管理器已销毁')
  }
}

// 创建全局清理管理器实例
const appCleanupManager = new AppCleanupManager()

/**
 * 注册应用清理任务
 * @param {Function} task 清理任务函数
 * @param {number} priority 优先级
 */
export function registerAppCleanupTask(task, priority = 5) {
  appCleanupManager.registerCleanupTask(task, priority)
}

/**
 * 手动执行应用清理
 */
export async function executeAppCleanup() {
  await appCleanupManager.executeCleanup()
}

/**
 * 初始化应用清理管理器
 */
export function initAppCleanup() {
  appCleanupManager.init()
}

/**
 * 销毁应用清理管理器
 */
export function destroyAppCleanup() {
  appCleanupManager.destroy()
}

/**
 * 获取应用清理管理器实例
 */
export function getAppCleanupManager() {
  return appCleanupManager
}

export default {
  registerAppCleanupTask,
  executeAppCleanup,
  initAppCleanup,
  destroyAppCleanup,
  getAppCleanupManager
} 