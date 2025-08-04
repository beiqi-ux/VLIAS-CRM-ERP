<template>
  <div class="container">
    <h1>VLIAS CRM 测试登录</h1>
    <div class="login-form">
      <div>
        <label>用户名:</label>
        <input
          v-model="username"
          type="text"
          placeholder="请输入用户名"
        >
      </div>
      <div>
        <label>密码:</label>
        <input
          v-model="password"
          type="password"
          placeholder="请输入密码"
        >
      </div>
      <div>
        <button
          :disabled="loading"
          @click="login"
        >
          {{ loading ? "登录中..." : "登录" }}
        </button>
      </div>
      <div
        v-if="error"
        class="error"
      >
        {{ error }}
      </div>
      <div
        v-if="user"
        class="success"
      >
        <h3>登录成功!</h3>
        <p>欢迎, {{ user.realName || user.username }}</p>
        <p>用户ID: {{ user.userId }}</p>
        <button @click="logout">
          退出登录
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue'
import axios from 'axios'

export default {
  setup() {
    const username = ref('')
    const password = ref('')
    const loading = ref(false)
    const error = ref('')
    const user = ref(null)

    async function login() {
      if (!username.value || !password.value) {
        error.value = '请输入用户名和密码'
        return
      }

      loading.value = true
      error.value = ''

      try {
        const response = await axios.post('/api/auth/login', {
          username: username.value,
          password: password.value,
          rememberMe: true
        })

        if (response.data.success) {
          user.value = response.data.data
          // 存储token
          localStorage.setItem('token', response.data.data.token)
          axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.data.token}`
        } else {
          error.value = response.data.message || '登录失败'
        }
      } catch (e) {
        error.value = e.response?.data?.message || '登录请求失败'
        console.error('登录错误:', e)
      } finally {
        loading.value = false
      }
    }

    function logout() {
      localStorage.removeItem('token')
      delete axios.defaults.headers.common['Authorization']
      user.value = null
    }

    return {
      username,
      password,
      loading,
      error,
      user,
      login,
      logout
    }
  }
}
</script>

<style>
body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: #f5f7fa;
}

.container {
  max-width: 500px;
  margin: 50px auto;
  padding: 20px;
  background-color: white;
  border-radius: 5px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h1 {
  text-align: center;
  color: #409eff;
}

.login-form {
  margin-top: 20px;
}

.login-form > div {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #337ecc;
}

button:disabled {
  background-color: #a0cfff;
  cursor: not-allowed;
}

.error {
  color: #f56c6c;
  margin-top: 10px;
}

.success {
  margin-top: 20px;
  padding: 15px;
  background-color: #f0f9eb;
  border-radius: 4px;
  color: #67c23a;
}
</style>
