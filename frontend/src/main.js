import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import formatPlugin from './plugins/format'
import { hasPermission } from './utils/permission'

import App from './App.vue'
import router from './router'
import DictSelect from './components/DictSelect.vue'
import DictRadio from './components/DictRadio.vue'

import './assets/main.css'
import './styles/layout.css'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {
  locale: zhCn
})
app.use(formatPlugin)

// 全局注册组件
app.component('DictSelect', DictSelect)
app.component('DictRadio', DictRadio)

// 全局权限指令
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

app.mount('#app') 