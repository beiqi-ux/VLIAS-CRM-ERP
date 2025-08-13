/**
 * 生产环境安全保护工具
 */

// 检测是否为生产环境
const isProduction = import.meta.env.PROD || process.env.NODE_ENV === 'production'

/**
 * 禁用开发者工具相关功能
 */
export function disableDevTools() {
  if (!isProduction) return

  // 禁用右键菜单
  document.addEventListener('contextmenu', (e) => {
    e.preventDefault()
    showSecurityWarning('检测到右键操作，已被阻止')
    return false
  })

  // 禁用F12和常见快捷键
  document.addEventListener('keydown', (e) => {
    // F12
    if (e.key === 'F12') {
      e.preventDefault()
      showSecurityWarning('开发者工具访问已被禁用')
      return false
    }
    
    // Ctrl+Shift+I (Windows/Linux)
    if (e.ctrlKey && e.shiftKey && e.key === 'I') {
      e.preventDefault()
      showSecurityWarning('开发者工具访问已被禁用')
      return false
    }
    
    // Ctrl+Shift+J (Windows/Linux)
    if (e.ctrlKey && e.shiftKey && e.key === 'J') {
      e.preventDefault()
      showSecurityWarning('控制台访问已被禁用')
      return false
    }
    
    // Ctrl+U (查看源代码)
    if (e.ctrlKey && e.key === 'u') {
      e.preventDefault()
      showSecurityWarning('查看源代码已被禁用')
      return false
    }
    
    // Ctrl+S (保存页面)
    if (e.ctrlKey && e.key === 's') {
      e.preventDefault()
      showSecurityWarning('页面保存已被禁用')
      return false
    }

    // Cmd+Option+I (Mac)
    if (e.metaKey && e.altKey && e.key === 'i') {
      e.preventDefault()
      showSecurityWarning('开发者工具访问已被禁用')
      return false
    }
    
    // Cmd+Option+J (Mac)
    if (e.metaKey && e.altKey && e.key === 'j') {
      e.preventDefault()
      showSecurityWarning('控制台访问已被禁用')
      return false
    }
  })

  // 检测开发者工具是否打开
  detectDevTools()
}

/**
 * 检测开发者工具是否打开
 */
function detectDevTools() {
  if (!isProduction) return

  let devtools = { open: false }
  
  setInterval(() => {
    const start = performance.now()
    
    // 使用console.clear()检测开发者工具
    const original = console.clear
    let isDevToolsOpen = false
    
    console.clear = function() {
      isDevToolsOpen = true
      original.apply(console, arguments)
    }
    
    try {
      console.clear()
    } catch (e) {
      // ignore
    }
    
    console.clear = original
    
    // 检测窗口尺寸变化（开发者工具会改变可视区域）
    const threshold = 160
    if (window.outerHeight - window.innerHeight > threshold || 
        window.outerWidth - window.innerWidth > threshold) {
      isDevToolsOpen = true
    }
    
    if (isDevToolsOpen && !devtools.open) {
      devtools.open = true
      handleDevToolsDetected()
    } else if (!isDevToolsOpen && devtools.open) {
      devtools.open = false
    }
  }, 1000)
}

/**
 * 处理检测到开发者工具打开
 */
function handleDevToolsDetected() {
  // 清除敏感数据
  clearSensitiveData()
  
  // 显示警告
  showSecurityWarning('检测到开发者工具，为保护数据安全，部分功能已被限制')
  
  // 可选：重定向到安全页面或登出
  // window.location.href = '/security-warning'
}

/**
 * 清除敏感数据
 */
function clearSensitiveData() {
  // 清除localStorage中的敏感信息
  const sensitiveKeys = ['token', 'userInfo', 'authData']
  sensitiveKeys.forEach(key => {
    localStorage.removeItem(key)
  })
  
  // 清除sessionStorage
  sessionStorage.clear()
  
  // 清除可能的全局变量
  if (window.userStore) {
    window.userStore = null
  }
}

/**
 * 显示安全警告
 */
function showSecurityWarning(message) {
  // 创建警告提示
  const warning = document.createElement('div')
  warning.style.cssText = `
    position: fixed;
    top: 20px;
    right: 20px;
    background: #f56565;
    color: white;
    padding: 12px 20px;
    border-radius: 6px;
    z-index: 10000;
    font-size: 14px;
    box-shadow: 0 4px 12px rgba(0,0,0,0.15);
  `
  warning.textContent = message
  document.body.appendChild(warning)
  
  // 3秒后自动移除
  setTimeout(() => {
    if (warning.parentNode) {
      warning.parentNode.removeChild(warning)
    }
  }, 3000)
}

/**
 * 重写console方法（生产环境）
 */
export function disableConsole() {
  if (!isProduction) return

  const noop = () => {}
  
  // 重写所有console方法
  Object.keys(console).forEach(key => {
    if (typeof console[key] === 'function') {
      console[key] = noop
    }
  })
}

/**
 * 安全的console（只在开发环境生效）
 */
export const secureConsole = {
  log: (...args) => {
    if (!isProduction) {
      console.log(...args)
    }
  },
  warn: (...args) => {
    if (!isProduction) {
      console.warn(...args)
    }
  },
  error: (...args) => {
    if (!isProduction) {
      console.error(...args)
    }
  },
  info: (...args) => {
    if (!isProduction) {
      console.info(...args)
    }
  }
}

/**
 * 安全日志记录（只在开发环境输出）
 */
export function securityLog(module, message, data = null) {
  if (!isProduction) {
    const timestamp = new Date().toISOString()
    const logMessage = `[${timestamp}] [${module}] ${message}`
    
    if (data) {
      // 对敏感数据进行脱敏处理
      const maskedData = maskSensitiveData(data)
      console.log(logMessage, maskedData)
    } else {
      console.log(logMessage)
    }
  }
}

/**
 * 数据脱敏处理
 */
export function maskSensitiveData(data, fields = ['password', 'token', 'mobile', 'email', 'idCard']) {
  if (!data || typeof data !== 'object') return data
  
  const masked = { ...data }
  
  fields.forEach(field => {
    if (masked[field]) {
      const value = String(masked[field])
      if (field === 'mobile') {
        // 手机号脱敏：138****1234
        masked[field] = value.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2')
      } else if (field === 'email') {
        // 邮箱脱敏：abc***@example.com
        masked[field] = value.replace(/(.{1,3}).+@/, '$1***@')
      } else if (field === 'idCard') {
        // 身份证脱敏：123456****1234
        masked[field] = value.replace(/(\d{6})\d{8}(\d{4})/, '$1****$2')
      } else {
        // 其他字段用星号替换
        masked[field] = '*'.repeat(Math.min(value.length, 8))
      }
    }
  })
  
  return masked
}

/**
 * 初始化安全保护
 */
export function initSecurity() {
  if (isProduction) {
    disableDevTools()
    disableConsole()
    
    // 添加安全头部检测
    if (!document.querySelector('meta[http-equiv="X-Frame-Options"]')) {
      const meta = document.createElement('meta')
      meta.setAttribute('http-equiv', 'X-Frame-Options')
      meta.setAttribute('content', 'DENY')
      document.head.appendChild(meta)
    }
  }
}

export default {
  disableDevTools,
  disableConsole,
  secureConsole,
  securityLog,
  maskSensitiveData,
  initSecurity
} 