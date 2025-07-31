import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const request = axios.create({
  baseURL: '', // 使用相对路径，配合vite代理
  timeout: 15000 // 请求超时时间
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 从localStorage中获取token，并添加到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    
    // 添加调试日志
    console.log('发送请求:', config.method.toUpperCase(), config.url, config.params || config.data)
    
    return config
  },
  error => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 添加调试日志
    console.log('接收响应:', response.config.url, response.data)
    
    // 直接返回响应数据
    return response.data
  },
  error => {
    // 处理HTTP错误
    let message = '发生未知错误'
    if (error.response) {
      console.error('响应错误:', error.response.status, error.response.data)
      
      switch (error.response.status) {
        case 400:
          message = '请求错误'
          break
        case 401:
          message = '未授权，请重新登录'
          // 清除token并跳转到登录页
          localStorage.removeItem('token')
          setTimeout(() => {
            window.location.href = '/login'
          }, 1500)
          break
        case 403:
          message = '拒绝访问'
          break
        case 404:
          message = '请求的资源不存在'
          break
        case 500:
          message = '服务器错误'
          break
        default:
          message = `连接错误 ${error.response.status}`
      }
    } else if (error.request) {
      message = '服务器未响应'
      console.error('服务器未响应:', error.request)
    } else {
      message = error.message
      console.error('请求配置错误:', error.message)
    }

    ElMessage.error(message)
    return Promise.reject(error)
  }
)

export default request 