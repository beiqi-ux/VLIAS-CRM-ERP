import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import formatPlugin from './plugins/format'

import App from './App.vue'
import router from './router'
import DictSelect from './components/DictSelect.vue'
import DictRadio from './components/DictRadio.vue'

import './assets/main.css'

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

app.mount('#app') 