<template>
  <div class="user-profile">
    <el-row :gutter="20">
      <!-- 个人信息卡片 -->
      <el-col :span="8">
        <el-card class="profile-card">
          <div class="avatar-container">
            <div class="avatar-wrapper">
              <el-avatar 
                :size="100" 
                :src="$formatImageUrl(userInfo.avatar)" 
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
                  <el-icon class="avatar-edit-icon"><Edit /></el-icon>
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
            <span>详细信息</span>
          </template>
          
          <el-descriptions :column="2" border>
            <el-descriptions-item label="用户ID">{{ userInfo.id }}</el-descriptions-item>
            <el-descriptions-item label="用户名">{{ userInfo.username }}</el-descriptions-item>
            <el-descriptions-item label="真实姓名">{{ userInfo.realName || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userInfo.email || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="手机号">{{ userInfo.mobile || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ getGenderText(userInfo.gender) }}</el-descriptions-item>
            <el-descriptions-item label="组织">{{ userInfo.orgName || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="部门">{{ userInfo.deptName || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="岗位">{{ userInfo.positionName || '未设置' }}</el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="userInfo.status === 1 ? 'success' : 'danger'">
                {{ userInfo.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="最后登录时间" :span="2">
              {{ formatDateTime(userInfo.lastLoginTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间" :span="2">
              {{ formatDateTime(userInfo.createTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="更新时间" :span="2">
              {{ formatDateTime(userInfo.updateTime) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { Edit } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

// 响应式数据
const userStore = useUserStore()
const userInfo = ref({})

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

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const response = await userStore.fetchUserInfo()
    if (response && response.success) {
      userInfo.value = response.data
    } else {
      ElMessage.error('获取用户信息失败')
    }
  } catch (error) {
    ElMessage.error('获取用户信息失败')
  }
}

// 上传头像相关
const uploadHeaders = computed(() => {
  return {
    Authorization: `Bearer ${localStorage.getItem('token')}`
  };
});

const handleAvatarSuccess = async (response, uploadFile) => {
  if (response.success) {
    // 获取头像URL
    const avatarUrl = response.data.url;
    
    // 更新用户信息
    userInfo.value.avatar = avatarUrl;
    ElMessage.success('头像更新成功');
    
    // 更新用户存储中的头像
    userStore.updateUserAvatar(avatarUrl);
    
    // 获取最新用户信息并完全刷新
    await userStore.fetchUserInfo();
  } else {
    ElMessage.error(response.message || '头像上传失败');
  }
};

const handleAvatarError = (error) => {
  console.error('头像上传失败:', error);
  ElMessage.error('头像上传失败，请重试');
};

const beforeAvatarUpload = (file) => {
  const isImage = file.type.startsWith('image/');
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isImage) {
    ElMessage.error('头像只能是图片格式!');
    return false;
  }
  if (!isLt2M) {
    ElMessage.error('头像大小不能超过 2MB!');
    return false;
  }
  return true;
};

// 页面加载时获取数据
onMounted(() => {
  fetchUserInfo()
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