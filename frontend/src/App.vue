<template>
  <RouterView />
</template>

<script setup>
import { RouterView } from 'vue-router'
import { onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { securityLog } from '@/utils/security'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

// 应用启动时验证token有效性
onMounted(async () => {
  // 如果当前在登录页，不需要验证
  if (route.path === '/login') {
    return
  }
  
  // 如果有token，验证其有效性并获取用户信息
  if (userStore.token) {
    try {
      // 先尝试获取用户信息（这会验证token有效性）
      const result = await userStore.fetchUserInfo()
      
      if (!result) {
        // 如果获取用户信息失败，说明token无效
        securityLog('App', 'Token已失效，跳转到登录页')
        router.replace('/login')
      }
    } catch (error) {
      securityLog('App', 'Token验证失败，跳转到登录页', { error: error.message })
      router.replace('/login')
    }
  } else {
    // 没有token，跳转到登录页
    router.replace('/login')
  }
})
</script>

<style>
body {
  margin: 0;
  padding: 0;
  font-family: Arial, Helvetica, sans-serif;
}

#app {
  width: 100%;
  height: 100vh;
}
</style> 