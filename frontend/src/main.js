import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import '@/styles/layout.css'

// 导入权限检查函数
import { hasPermission, hasActionPermission } from '@/utils/permission'

// 导入格式化插件
import formatPlugin from '@/plugins/format'

// 导入自定义组件
import DictSelect from '@/components/DictSelect.vue'
import DictRadio from '@/components/DictRadio.vue'

// 导入安全保护
import { initSecurity } from '@/utils/security'

// 导入应用清理管理器
import { initAppCleanup } from '@/utils/appCleanup'

const app = createApp(App)
const pinia = createPinia()

app.use(ElementPlus, {
  locale: zhCn,
})
app.use(pinia)
app.use(router)
app.use(formatPlugin)

// 注册全局组件
app.component('DictSelect', DictSelect)
app.component('DictRadio', DictRadio)

// 全局权限指令（用于UI显示，支持权限继承）
app.directive('permission', {
  mounted(el, binding) {
    const permission = binding.value
    if (permission && !hasPermission(permission)) {
      el.style.display = 'none'
    }
  },
  updated(el, binding) {
    const permission = binding.value
    if (permission && !hasPermission(permission)) {
      el.style.display = 'none'
    } else {
      el.style.display = ''
    }
  }
})

// 安全移除元素的辅助函数
function safeRemoveElement(el) {
  try {
    if (el && el.parentNode && el.parentNode.contains && el.parentNode.contains(el)) {
      // 标记元素已被权限控制，防止重复处理
      if (!el.__permission_removed) {
        el.__permission_removed = true
        el.parentNode.removeChild(el)
      }
    }
  } catch (error) {
    console.warn('权限指令移除元素时发生错误:', error)
  }
}

// 操作权限指令（用于按钮功能控制，严格检查）
app.directive('action-permission', {
  mounted(el, binding) {
    const permission = binding.value
    if (permission && !hasActionPermission(permission)) {
      safeRemoveElement(el)
    }
  },
  updated(el, binding) {
    const permission = binding.value
    if (permission && !hasActionPermission(permission)) {
      safeRemoveElement(el)
    }
  }
})

// hasPermission指令（支持v-hasPermission语法，使用严格检查）
app.directive('hasPermission', {
  mounted(el, binding) {
    const permission = binding.value
    if (permission && !hasActionPermission(permission)) {
      safeRemoveElement(el)
    }
  },
  updated(el, binding) {
    const permission = binding.value
    if (permission && !hasActionPermission(permission)) {
      safeRemoveElement(el)
    }
  }
})

// 初始化安全保护（生产环境生效）
initSecurity()

// 初始化应用清理管理器
initAppCleanup()

app.mount('#app') 