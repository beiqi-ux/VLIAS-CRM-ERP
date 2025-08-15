<template>
  <div class="login-container">
    <div class="login-left">
      <div class="login-overlay" />
      <div class="login-content">
        <div class="logo-container">
          <div class="logo">
            VLIAS
          </div>
          <div class="tagline">
            企业级全栈管理系统
          </div>
        </div>
        <div class="features">
          <div class="feature-item">
            <el-icon><DataAnalysis /></el-icon>
            <span>全业务流程管理</span>
          </div>
          <div class="feature-item">
            <el-icon><TrendCharts /></el-icon>
            <span>智能数据分析</span>
          </div>
          <div class="feature-item">
            <el-icon><Connection /></el-icon>
            <span>一体化协作平台</span>
          </div>
        </div>
      </div>
    </div>
    <div class="login-right">
      <div class="login-form-container">
        <div class="welcome-text">
          <h2>欢迎回来</h2>
          <p>请登录您的账号以继续</p>
        </div>
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
        >
          <el-form-item prop="username">
            <el-input 
              v-model="loginForm.username" 
              placeholder="用户名"
              size="large"
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="密码"
              size="large"
              show-password
              @keyup.enter="handleLogin"
            >
              <template #prefix>
                <el-icon><Lock /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item class="remember-me">
            <el-checkbox v-model="loginForm.rememberMe">
              记住我
            </el-checkbox>
            <a
              href="#"
              class="forgot-password"
            >忘记密码?</a>
          </el-form-item>
          <el-form-item>
            <el-button
              :loading="loading"
              type="primary"
              size="large"
              class="login-button"
              @click="handleLogin"
            >
              {{ loading ? '登录中...' : '登录' }}
            </el-button>
          </el-form-item>
        </el-form>
        <div class="footer">
          <p>© {{ new Date().getFullYear() }} VLIAS 企业级全栈管理系统. 保留所有权利</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { User, Lock, DataAnalysis, TrendCharts, Connection } from '@element-plus/icons-vue'

// 路由
const router = useRouter()

// 用户store
const userStore = useUserStore()

// 登录表单引用
const loginFormRef = ref(null)

// 登录表单数据
const loginForm = reactive({
  username: '',
  password: '',
  rememberMe: false
})

// 登录验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

// 加载状态
const loading = ref(false)

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    // 表单验证
    await loginFormRef.value.validate()
    
    // 显示加载状态
    loading.value = true
    
    // 调用登录
    const result = await userStore.login(loginForm)
    
    if (result) {
      ElMessage.success('登录成功')
      router.push('/')
    }
  } catch (error) {
    console.error('登录失败', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* 左侧展示区 */
.login-left {
  flex: 1;
  position: relative;
  background-color: #003087; /* VLIAS深蓝色背景 */
  color: white;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(circle at 30% 50%, rgba(0, 77, 195, 0.4) 0%, rgba(0, 48, 135, 0.8) 80%);
  z-index: 1;
}

.login-content {
  position: relative;
  z-index: 2;
  padding: 20px;
  width: 100%;
  max-width: 500px;
}

.logo-container {
  margin-bottom: 60px;
  text-align: center;
}

.logo {
  font-size: 6rem;
  font-weight: 700;
  letter-spacing: 8px;
  text-shadow: 0px 4px 8px rgba(0, 0, 0, 0.3);
  margin-bottom: 10px;
  color: white;
}

.tagline {
  font-size: 1.2rem;
  opacity: 0.8;
}

.features {
  margin-top: 40px;
}

.feature-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  background-color: rgba(255, 255, 255, 0.1);
  padding: 15px 20px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.feature-item:hover {
  background-color: rgba(255, 255, 255, 0.2);
  transform: translateY(-3px);
}

.feature-item .el-icon {
  font-size: 24px;
  margin-right: 15px;
}

.feature-item span {
  font-size: 16px;
}

/* 右侧登录区 */
.login-right {
  width: 500px;
  background-color: white;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 50px;
  box-shadow: -10px 0 20px rgba(0, 0, 0, 0.1);
}

.login-form-container {
  width: 100%;
}

.welcome-text {
  text-align: center;
  margin-bottom: 40px;
}

.welcome-text h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 10px;
}

.welcome-text p {
  color: #888;
  font-size: 16px;
}

.login-form {
  margin-bottom: 30px;
}

.login-form :deep(.el-input__wrapper) {
  padding: 12px 15px;
  background-color: #f8fafc;
  border: none;
  box-shadow: none;
}

.login-form :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #003087 inset !important;
}

.remember-me {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.forgot-password {
  color: #003087;
  font-size: 14px;
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}

.login-button {
  width: 100%;
  padding: 12px 0;
  font-size: 16px;
  background-color: #003087; /* 使用VLIAS蓝色 */
  border-color: #003087;
  transition: all 0.3s ease;
}

.login-button:hover {
  background-color: #00245e;
  border-color: #00245e;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 48, 135, 0.3);
}

.footer {
  text-align: center;
  margin-top: 30px;
  color: #999;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .login-left {
    display: none;
  }
  
  .login-right {
    width: 100%;
  }
}
</style> 