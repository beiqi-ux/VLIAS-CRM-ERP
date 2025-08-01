<template>
  <el-container class="app-container">
    <!-- 侧边栏 -->
    <el-aside :width="isCollapse ? '64px' : '200px'" class="sidebar-container">
      <!-- 顶部logo -->
      <div class="logo-container">
        <div class="vlias-logo" :class="{ 'collapsed': isCollapse }">
          {{ isCollapse ? 'V' : 'VLIAS' }}
        </div>
        <span v-if="!isCollapse">企业管理系统</span>
      </div>
      
      <!-- 动态菜单 -->
      <el-menu
        :collapse="isCollapse"
        :default-active="activeMenu"
        :default-openeds="[]"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409eff"
        :collapse-transition="false"
        router
      >
        <!-- 首页菜单 -->
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
        
        <!-- 动态渲染菜单 -->
        <template v-for="menu in userMenus" :key="menu.id">
          <!-- 目录类型菜单 -->
          <el-sub-menu v-if="menu.menuType === 1" :index="menu.menuCode">
            <template #title>
              <el-icon v-if="menu.icon">
                <component :is="menu.icon" />
              </el-icon>
              <el-icon v-else>
                <Setting v-if="menu.menuCode === 'system'" />
                <OfficeBuilding v-else-if="menu.menuCode === 'org'" />
                <Goods v-else-if="menu.menuCode === 'product'" />
                <Menu v-else />
              </el-icon>
              <span>{{ menu.menuName }}</span>
            </template>
            <!-- 子菜单 -->
            <template v-for="child in menu.children" :key="child.id">
              <el-menu-item :index="child.path">
                <el-icon v-if="child.icon">
                  <component :is="child.icon" />
                </el-icon>
                <el-icon v-else>
                  <User v-if="child.menuCode === 'system:user'" />
                  <UserFilled v-else-if="child.menuCode === 'system:role'" />
                  <Lock v-else-if="child.menuCode === 'system:permission'" />
                  <Menu v-else-if="child.menuCode === 'system:menu'" />
                  <Collection v-else-if="child.menuCode === 'system:dict'" />
                  <SetUp v-else-if="child.menuCode === 'org:organization'" />
                  <Files v-else-if="child.menuCode === 'org:department'" />
                  <List v-else-if="child.menuCode === 'org:position'" />
                  <Box v-else-if="child.menuCode === 'product:goods'" />
                  <Grid v-else-if="child.menuCode === 'product:category'" />
                  <Star v-else-if="child.menuCode === 'product:brand'" />
                  <View v-else />
                </el-icon>
                <template #title>{{ child.menuName }}</template>
              </el-menu-item>
            </template>
          </el-sub-menu>
          
          <!-- 菜单类型 -->
          <el-menu-item v-else-if="menu.menuType === 2" :index="menu.path">
            <el-icon v-if="menu.icon">
              <component :is="menu.icon" />
            </el-icon>
            <el-icon v-else>
              <User v-if="menu.menuCode === 'profile'" />
              <View v-else />
            </el-icon>
            <template #title>{{ menu.menuName }}</template>
          </el-menu-item>
        </template>
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
  OfficeBuilding, SetUp, Files, List, Collection, View, Goods, Box, Grid, Star
} from '@element-plus/icons-vue'
import { getUserMenuTree } from '@/api/menu'
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

// 用户菜单数据
const userMenus = ref([])

// 切换侧边栏
function toggleSidebar() {
  isCollapse.value = !isCollapse.value
}

// 获取用户菜单
async function fetchUserMenus() {
  try {
    if (userInfo.value.userId || userInfo.value.id) {
      const userId = userInfo.value.userId || userInfo.value.id
      console.log('获取用户菜单，用户ID:', userId)
      const { data } = await getUserMenuTree(userId)
      console.log('获取到的菜单数据:', data)
      userMenus.value = data || []
      console.log('设置后的菜单数据:', userMenus.value)
    } else {
      console.log('用户信息不完整，无法获取菜单')
      console.log('用户信息:', userInfo.value)
    }
  } catch (error) {
    console.error('获取用户菜单失败:', error)
  }
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

// 组件挂载时获取用户信息和菜单
onMounted(async () => {
  if (!Object.keys(userInfo.value).length) {
    await userStore.fetchUserInfo()
  }
  await fetchUserMenus()
})

// 监听用户信息变化，重新获取菜单
watch(() => userInfo.value.userId || userInfo.value.id, async (newId) => {
  if (newId) {
    await fetchUserMenus()
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

.vlias-logo {
  width: 32px;
  height: 32px;
  background-color: #002FA7;
  border-radius: 4px;
  margin-right: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 10px;
  font-weight: bold;
  font-family: Arial, sans-serif;
}

.vlias-logo.collapsed {
  margin-right: 0;
  font-size: 14px;
}
</style> 