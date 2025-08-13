<template>
  <div
    v-loading="loading"
    class="user-profile"
  >
    <!-- 加载状态 -->
    <div
      v-if="loading"
      class="loading-container"
    >
      <el-skeleton
        :rows="8"
        animated
      />
      <p style="text-align: center; margin-top: 20px; color: #909399;">
        正在加载个人信息...
      </p>
    </div>
    
    <!-- 错误状态 -->
    <div
      v-else-if="error"
      class="error-container"
    >
      <el-result
        icon="warning"
        title="加载失败"
        :sub-title="error"
      >
        <template #extra>
          <el-button
            type="primary"
            @click="fetchUserInfo"
          >
            重新加载
          </el-button>
        </template>
      </el-result>
    </div>
    
    <!-- 正常内容 -->
    <el-row
      v-else
      :gutter="20"
    >
      <!-- 个人信息卡片 -->
      <el-col :span="8">
        <el-card class="profile-card">
          <div class="avatar-container">
            <div class="avatar-wrapper">
              <el-avatar 
                :size="100" 
                :src="avatarUrl"
                :icon="!avatarUrl ? 'User' : undefined"
                @error="handleAvatarLoadError"
              />
              <div class="avatar-upload">
                <el-upload
                  class="avatar-uploader"
                  :action="'/api/files/avatar'"
                  :headers="uploadHeaders"
                  :show-file-list="false"
                  :on-success="handleAvatarSuccess"
                  :on-error="handleAvatarError"
                  :before-upload="beforeAvatarUpload"
                >
                  <el-icon class="avatar-edit-icon">
                    <Edit />
                  </el-icon>
                </el-upload>
              </div>
            </div>
            <h3>{{ userInfo.realName || userInfo.username }}</h3>
            <p>{{ userInfo.email || '暂无邮箱' }}</p>
          </div>
          
          <el-divider />
          
          <div class="info-list">
            <div class="info-item">
              <span class="label">用户名：</span>
              <span class="value">{{ userInfo.username }}</span>
            </div>
            <div class="info-item">
              <span class="label">真实姓名：</span>
              <span class="value">{{ userInfo.realName || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="label">手机号：</span>
              <span class="value">{{ userInfo.mobile || '未设置' }}</span>
            </div>
            <div class="info-item">
              <span class="label">性别：</span>
              <span class="value">{{ getGenderText(userInfo.gender) }}</span>
            </div>
            <div class="info-item">
              <span class="label">状态：</span>
              <el-tag :type="userInfo.status === 1 ? 'success' : 'danger'">
                {{ userInfo.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="label">最后登录：</span>
              <span class="value">{{ formatDateTime(userInfo.lastLoginTime) }}</span>
            </div>
            <div class="info-item">
              <span class="label">创建时间：</span>
              <span class="value">{{ formatDateTime(userInfo.createTime) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      
      <!-- 详细信息卡片 -->
      <el-col :span="16">
        <el-card>
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>详细信息</span>
              <div>
                <el-button 
                  size="small" 
                  type="primary" 
                  :loading="loading"
                  @click="() => fetchUserInfo(true)"
                >
                  重新加载
                </el-button>
                <el-button 
                  size="small" 
                  type="info" 
                  @click="showDebugInfo"
                >
                  调试信息
                </el-button>
              </div>
            </div>
          </template>
          
          <el-descriptions
            :column="2"
            border
          >
            <el-descriptions-item label="用户ID">
              {{ userInfo.id }}
            </el-descriptions-item>
            <el-descriptions-item label="用户名">
              {{ userInfo.username }}
            </el-descriptions-item>
            <el-descriptions-item label="真实姓名">
              {{ userInfo.realName || '未设置' }}
            </el-descriptions-item>
            <el-descriptions-item label="邮箱">
              {{ userInfo.email || '未设置' }}
            </el-descriptions-item>
            <el-descriptions-item label="手机号">
              {{ userInfo.mobile || '未设置' }}
            </el-descriptions-item>
            <el-descriptions-item label="性别">
              {{ getGenderText(userInfo.gender) }}
            </el-descriptions-item>
            <el-descriptions-item label="组织">
              {{ userInfo.orgName || '未设置' }}
            </el-descriptions-item>
            <el-descriptions-item label="部门">
              {{ userInfo.deptName || '未设置' }}
            </el-descriptions-item>
            <el-descriptions-item label="岗位">
              {{ userInfo.positionName || '未设置' }}
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="userInfo.status === 1 ? 'success' : 'danger'">
                {{ userInfo.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item
              label="最后登录时间"
              :span="2"
            >
              {{ formatDateTime(userInfo.lastLoginTime) }}
            </el-descriptions-item>
            <el-descriptions-item
              label="创建时间"
              :span="2"
            >
              {{ formatDateTime(userInfo.createTime) }}
            </el-descriptions-item>
            <el-descriptions-item
              label="更新时间"
              :span="2"
            >
              {{ formatDateTime(userInfo.updateTime) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { Edit } from '@element-plus/icons-vue'
import { getUserInfo } from '@/api/auth'
import { securityLog } from '@/utils/security'

// 响应式数据
const userStore = useUserStore()
const userInfo = ref({})
const loading = ref(true)
const error = ref('')
const isInitialized = ref(false) // 防止重复初始化

// 计算属性：处理头像URL
const avatarUrl = computed(() => {
  if (!userInfo.value.avatar) {
    return '' // 返回空字符串，让el-avatar显示默认图标
  }
  
  // 使用格式化函数处理头像URL
  const formatted = window.app?.config?.globalProperties?.$formatImageUrl?.(userInfo.value.avatar) || 
                   formatImageUrl(userInfo.value.avatar)
  
  console.log('头像URL处理:', {
    original: userInfo.value.avatar,
    formatted: formatted
  })
  
  return formatted
})

// 本地导入格式化函数作为备用
const formatImageUrl = (url, defaultUrl = '') => {
  if (!url) return defaultUrl
  if (url.startsWith('http')) return url
  
  // 直接使用相对路径，让代理配置处理
  const timestamp = new Date().getTime()
  return `${url}?t=${timestamp}`
}

// 获取性别文本
const getGenderText = (gender) => {
  switch (gender) {
  case 1: return '男'
  case 2: return '女'
  default: return '未知'
  }
}

// 格式化日期时间
const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

// 直接从API获取用户信息，避免通过store的循环问题
const fetchUserInfo = async (forceRefresh = false) => {
  // 如果不是强制刷新且已初始化，跳过
  if (!forceRefresh && isInitialized.value) {
    console.log('用户信息已初始化，跳过重复获取')
    return
  }
  
  loading.value = true
  error.value = ''
  
  try {
    console.log('个人中心页面：开始获取用户信息...', { forceRefresh })
    
    // 首先检查用户是否已登录
    if (!userStore.isLoggedIn) {
      error.value = '用户未登录，请先登录'
      ElMessage.error('用户未登录，请先登录')
      return
    }
    
    // 直接调用API，避免store的循环问题
    const response = await getUserInfo()
    console.log('个人中心页面：API响应:', response)
    
    if (response && response.success) {
      userInfo.value = { ...response.data } // 使用解构赋值确保响应式更新
      isInitialized.value = true
      securityLog('个人中心页面', '用户信息设置成功', { userId: userInfo.value?.userId })
      
      // 使用nextTick确保DOM更新
      await nextTick()
      console.log('个人中心页面：DOM已更新')
    } else {
      console.error('个人中心页面：获取用户信息失败:', response)
      error.value = response?.message || '获取用户信息失败'
      ElMessage.error(response?.message || '获取用户信息失败')
    }
  } catch (err) {
    console.error('个人中心页面：获取用户信息异常:', err)
    
    // 检查是否是取消的请求 - 优化判断逻辑
    if (err.name === 'CanceledError' || (err.message && err.message.includes('取消'))) {
      console.log('请求被取消，尝试重新请求...')
      // 延迟重试，避免立即重复请求
      setTimeout(() => {
        if (!isInitialized.value) {
          fetchUserInfo(true)
        }
      }, 1000)
      return
    }
    
    error.value = '获取用户信息失败: ' + (err.message || '未知错误')
    ElMessage.error('获取用户信息失败: ' + (err.message || '未知错误'))
  } finally {
    loading.value = false
  }
}

// 上传头像相关
const uploadHeaders = computed(() => {
  return {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  }
})

const handleAvatarSuccess = async (response, uploadFile) => {
  if (response.success) {
    // 获取头像URL
    const avatarUrl = response.data.url
    
    // 调试信息
    console.log('服务器返回的头像URL:', avatarUrl)
    console.log('格式化后的头像URL:', window.location.origin + avatarUrl)
    
    // 更新用户信息
    userInfo.value.avatar = avatarUrl
    ElMessage.success('头像更新成功')
    
    // 更新用户存储中的头像
    userStore.updateUserAvatar(avatarUrl)
  } else {
    ElMessage.error(response.message || '头像上传失败')
  }
}

const handleAvatarError = (error) => {
  console.error('头像上传失败:', error)
  ElMessage.error('头像上传失败，请重试')
}

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('头像只能是图片格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!')
    return false
  }
  return true
}

// 处理头像加载失败
const handleAvatarLoadError = () => {
  console.warn('头像加载失败，使用默认头像')
  // 可以在这里设置一个默认头像的URL
  // userInfo.value.avatar = 'https://cube.elemecdn.com/0/88/03b0d633a00067c8a7005739700jpeg.jpeg'; // 示例默认头像
}

// 显示调试信息
const showDebugInfo = () => {
  const debugInfo = {
    页面状态: {
      loading: loading.value,
      error: error.value,
      isInitialized: isInitialized.value,
      userInfoKeys: Object.keys(userInfo.value)
    },
    Store状态: {
      isLoggedIn: userStore.isLoggedIn,
      permissionsLoaded: userStore.permissionsLoaded,
      isFetchingUserInfo: userStore.isFetchingUserInfo,
      hasToken: !!userStore.token,
      storeUserInfoKeys: Object.keys(userStore.userInfo)
    },
    头像信息: {
      原始头像URL: userInfo.value.avatar,
      计算后头像URL: avatarUrl.value,
      格式化函数可用: !!window.app?.config?.globalProperties?.$formatImageUrl
    },
    用户信息: userInfo.value
  }
  
  console.log('=== 个人中心调试信息 ===')
  console.log(debugInfo)
  
  ElMessage.info(`调试信息已输出到控制台 - 初始化状态: ${isInitialized.value}`)
}

// 页面加载时获取数据 - 简化逻辑
onMounted(async () => {
  console.log('个人中心页面：组件挂载')
  
  // 等待一个微任务，确保所有同步代码执行完成
  await nextTick()
  
  // 只在页面首次加载时获取数据
  if (!isInitialized.value) {
    await fetchUserInfo()
  }
})
</script>

<style scoped>
.user-profile {
  padding: 20px;
}

.profile-card {
  text-align: center;
}

.avatar-container {
  margin-bottom: 20px;
}

.avatar-container h3 {
  margin: 10px 0 5px 0;
  color: #303133;
}

.avatar-container p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.info-list {
  text-align: left;
  margin-bottom: 20px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-item:last-child {
  border-bottom: none;
}

.info-item .label {
  color: #606266;
  font-weight: 500;
}

.info-item .value {
  color: #303133;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.avatar-wrapper {
  position: relative;
  display: inline-block;
}

.avatar-upload {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
}

.avatar-upload:hover {
  background-color: rgba(0, 0, 0, 0.7);
}

.avatar-edit-icon {
  color: white;
  font-size: 14px;
}

.avatar-uploader {
  display: inline-block;
  width: 100%;
  height: 100%;
}

.avatar-uploader :deep(.el-upload) {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}
</style> 