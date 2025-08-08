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

// 操作权限指令（用于按钮功能控制，严格检查）
app.directive('action-permission', {
  mounted(el, binding) {
    const permission = binding.value
    if (permission && !hasActionPermission(permission)) {
      // 移除元素而不是隐藏，确保操作完全不可用
      el.parentNode && el.parentNode.removeChild(el)
    }
  },
  updated(el, binding) {
    const permission = binding.value
    if (permission && !hasActionPermission(permission)) {
      // 如果权限检查失败，移除元素
      el.parentNode && el.parentNode.removeChild(el)
    }
  }
})

app.mount('#app') 