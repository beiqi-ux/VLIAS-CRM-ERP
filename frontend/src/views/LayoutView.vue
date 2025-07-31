<template>
  <el-container class="app-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar-container">
      <!-- 顶部logo -->
      <div class="logo-container">
        <div v-if="!isCollapse" style="width: 32px; height: 32px; background-color: #409EFF; border-radius: 4px; margin-right: 10px;"></div>
        <span v-if="!isCollapse">VLIAS CRM</span>
      </div>
      
      <!-- 菜单 -->
      <el-menu
        :collapse="isCollapse"
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        :collapse-transition="false"
        router
      >
        <el-menu-item index="/">
          <el-icon><House /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        
        <!-- 系统管理 -->
        <el-sub-menu index="/system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/users">
            <el-icon><User /></el-icon>
            <template #title>用户管理</template>
          </el-menu-item>
          <el-menu-item index="/roles">
            <el-icon><UserFilled /></el-icon>
            <template #title>角色管理</template>
          </el-menu-item>
          <el-menu-item index="/permissions">
            <el-icon><Lock /></el-icon>
            <template #title>权限管理</template>
          </el-menu-item>
          <el-menu-item index="/menus">
            <el-icon><Menu /></el-icon>
            <template #title>菜单管理</template>
          </el-menu-item>
        </el-sub-menu>
        
        <!-- 组织架构 -->
        <el-sub-menu index="/org-structure">
          <template #title>
            <el-icon><OfficeBuilding /></el-icon>
            <span>组织架构</span>
          </template>
          <el-menu-item index="/organizations">
            <el-icon><SetUp /></el-icon>
            <template #title>组织机构管理</template>
          </el-menu-item>
          <el-menu-item index="/departments">
            <el-icon><Files /></el-icon>
            <template #title>部门管理</template>
          </el-menu-item>
          <el-menu-item index="/positions">
            <el-icon><List /></el-icon>
            <template #title>岗位管理</template>
          </el-menu-item>
        </el-sub-menu>
        
        <el-menu-item index="/profile">
          <el-icon><User /></el-icon>
          <template #title>个人中心</template>
        </el-menu-item>
      </el-menu>
    </el-aside>
    
    <!-- 主容器 -->
    <el-container>
      <!-- 头部 -->
      <el-header class="header">
        <div class="header-left">
          <el-icon
            class="collapse-btn"
            @click="toggleSidebar"
          >
            <component :is="isCollapse ? 'Expand' : 'Fold'" />
          </el-icon>
          <Breadcrumb />
        </div>
        
        <div class="header-right">
          <el-dropdown trigger="click" @command="handleCommand">
            <span class="user-dropdown-link">
              <el-avatar 
                :key="userInfo.avatar" 
                :size="32" 
                :src="$formatImageUrl(userInfo.avatar)"
              />
              <span class="username">{{ userInfo.realName || userInfo.username }}</span>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      
      <!-- 内容区域 -->
      <el-main class="main-container">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'
import { 
  House, User, UserFilled, Setting, Lock, Menu, Expand, Fold,
  OfficeBuilding, SetUp, Files, List
} from '@element-plus/icons-vue'
import Breadcrumb from '@/components/Breadcrumb.vue'

// 路由
const router = useRouter()
const route = useRoute()

// 用户store
const userStore = useUserStore()

// 用户信息
const userInfo = computed(() => userStore.userInfo)

// 侧边栏折叠状态
const isCollapse = ref(false)

// 激活菜单
const activeMenu = computed(() => route.path)

// 切换侧边栏
function toggleSidebar() {
  isCollapse.value = !isCollapse.value
}

// 下拉菜单命令处理
function handleCommand(command) {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'logout':
      ElMessageBox.confirm('确定退出登录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        await userStore.logout()
        router.push('/login')
      }).catch(() => {})
      break
  }
}

// 组件挂载时获取用户信息
onMounted(() => {
  if (!Object.keys(userInfo.value).length) {
    userStore.fetchUserInfo()
  }
})

// 监听路由变化
watch(route, (to, from) => {
  // 如果从个人中心页面返回，重新获取用户信息以更新头像
  if (from.path === '/profile' && to.path !== '/profile') {
    userStore.fetchUserInfo()
  }
}, { immediate: false })
</script>

<style scoped>
.app-container {
  height: 100%;
}

.sidebar-container {
  transition: width 0.3s;
  background-color: #304156;
  overflow-x: hidden;
}

.logo-container {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  font-weight: bold;
}

.logo-container img {
  height: 32px;
  margin-right: 10px;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.collapse-btn {
  font-size: 20px;
  cursor: pointer;
  margin-right: 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin-left: 8px;
  font-size: 14px;
}

.main-container {
  padding: 15px;
  background-color: #f0f2f5;
}
</style> 