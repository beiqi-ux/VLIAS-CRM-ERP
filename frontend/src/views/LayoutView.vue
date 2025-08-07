<template>
  <div class="layout-container">
    <!-- 侧边栏 -->
    <div
      :style="{ width: isCollapse ? '64px' : '200px' }"
      class="layout-sidebar sidebar-container"
    >
      <div class="sidebar-content">
      <!-- 顶部logo -->
      <div class="logo-container">
        <div
          class="vlias-logo"
          :class="{ 'collapsed': isCollapse }"
        >
          {{ isCollapse ? 'V' : 'VLIAS' }}
        </div>
        <span v-if="!isCollapse">企业管理系统</span>
      </div>
      
        <!-- 主要菜单区域 -->
        <div class="main-menu-area">
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
          <template #title>
            首页
          </template>
        </el-menu-item>
        
            <!-- 动态渲染菜单（排除个人中心） -->
        <template
              v-for="menu in filteredUserMenus"
          :key="menu.id"
        >
          <!-- 目录类型菜单 -->
          <el-sub-menu
            v-if="menu.menuType === 1"
            :index="menu.menuCode"
          >
            <template #title>
              <el-icon v-if="menu.icon && iconMap[menu.icon]">
                <component :is="iconMap[menu.icon]" />
              </el-icon>
              <el-icon v-else>
                <Setting v-if="menu.menuCode === 'system'" />
                <OfficeBuilding v-else-if="menu.menuCode === 'org'" />
                <Goods v-else-if="menu.menuCode === 'product'" />
                <Document v-else-if="menu.menuCode === 'crm'" />
                <Calendar v-else-if="menu.menuCode === 'order'" />
                <Box v-else-if="menu.menuCode === 'inventory'" />
                <Document v-else-if="menu.menuCode === 'purchase'" />
                <Star v-else-if="menu.menuCode === 'promotion'" />
                <Money v-else-if="menu.menuCode === 'finance'" />
                <User v-else-if="menu.menuCode === 'member'" />
                <Bell v-else-if="menu.menuCode === 'message'" />
                <Menu v-else />
              </el-icon>
              <span>{{ menu.menuName }}</span>
            </template>
            <!-- 子菜单 -->
            <template
              v-for="child in menu.children"
              :key="child.id"
            >
              <el-menu-item :index="child.path">
                <template #title>
                  {{ child.menuName }}
                </template>
              </el-menu-item>
            </template>
          </el-sub-menu>
          
          <!-- 菜单类型 -->
          <el-menu-item
            v-else-if="menu.menuType === 2"
            :index="menu.path"
          >
            <el-icon v-if="menu.icon && iconMap[menu.icon]">
              <component :is="iconMap[menu.icon]" />
            </el-icon>
            <el-icon v-else>
              <User v-if="menu.menuCode === 'profile'" />
              <View v-else />
            </el-icon>
            <template #title>
              {{ menu.menuName }}
            </template>
          </el-menu-item>
        </template>
      </el-menu>
        </div>
        
        <!-- 底部固定个人中心菜单 -->
        <div class="bottom-menu-area">
          <el-menu
            :collapse="isCollapse"
            :default-active="activeMenu"
            background-color="#304156"
            text-color="#bfcbd9"
            active-text-color="#409eff"
            :collapse-transition="false"
            router
          >
            <template v-if="profileMenu">
              <el-menu-item :index="profileMenu.path">
                <el-icon v-if="profileMenu.icon && iconMap[profileMenu.icon]">
                  <component :is="iconMap[profileMenu.icon]" />
                </el-icon>
                <el-icon v-else>
                  <User />
                </el-icon>
                <template #title>
                  {{ profileMenu.menuName }}
                </template>
              </el-menu-item>
            </template>
          </el-menu>
        </div>
      </div>
    </div>
    
    <!-- 主容器 -->
    <div class="layout-main">
      <!-- 头部 -->
      <div class="layout-header header">
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
          <el-dropdown
            trigger="click"
            @command="handleCommand"
          >
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
                <el-dropdown-item @click="showUserProfile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item @click="showDebugInfo">
                  <el-icon><Tools /></el-icon>
                  调试权限
                </el-dropdown-item>
                <el-dropdown-item
                  divided
                  @click="handleLogout"
                >
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
      
      <!-- 主内容区域 -->
      <main class="layout-content main-content">
        <!-- 权限数据加载中 -->
        <div
          v-if="!userStore.permissionsLoaded"
          class="loading-container"
        >
          <el-skeleton
            :rows="8"
            animated
          />
        </div>
        <!-- 权限数据已加载，显示正常内容 -->
        <RouterView v-else />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox, ElMessage } from 'element-plus'
import { 
  House, User, UserFilled, Setting, Lock, Menu, Expand, Fold,
  OfficeBuilding, SetUp, Files, List, Collection, View, Goods, Box, Grid, Star,
  Document, Edit, Delete, Search, Plus, Refresh, Download, Upload,
  Bell, Message, Calendar, Location, Phone, Link, StarFilled,
  CircleCheck, CircleClose, Warning, InfoFilled, SuccessFilled, WarningFilled, CircleCheckFilled,
  Money, ChatDotRound, Tools, SwitchButton, DataBoard
} from '@element-plus/icons-vue'

// 创建图标组件映射
const iconMap = {
  House, User, UserFilled, Setting, Lock, Menu, Expand, Fold,
  OfficeBuilding, SetUp, Files, List, Collection, View, Goods, Box, Grid, Star,
  Document, Edit, Delete, Search, Plus, Refresh, Download, Upload,
  Bell, Message, Calendar, Location, Phone, Link, StarFilled,
  CircleCheck, CircleClose, Warning, InfoFilled, SuccessFilled, WarningFilled, CircleCheckFilled,
  Money, ChatDotRound, Tools, SwitchButton, DataBoard
}
import { getUserMenuTree, getMenuTree } from '@/api/menu'
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

// 过滤掉个人中心的菜单（用于主要菜单区域）
const filteredUserMenus = computed(() => {
  return userMenus.value.filter(menu => menu.menuCode !== 'profile')
})

// 个人中心菜单（用于底部固定显示）
const profileMenu = computed(() => {
  return userMenus.value.find(menu => menu.menuCode === 'profile')
})

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
      console.log('用户信息:', userInfo.value)
      
      const { data } = await getUserMenuTree(userId)
      console.log('获取到的菜单数据:', data)
      userMenus.value = data || []
      console.log('设置后的菜单数据:', userMenus.value)
      
      // 如果没有菜单数据，显示提示
      if (!data || data.length === 0) {
        console.warn('用户没有菜单权限，可能需要分配角色权限')
      }
    } else {
      console.log('用户信息不完整，无法获取菜单')
      console.log('用户信息:', userInfo.value)
    }
  } catch (error) {
    console.error('获取用户菜单失败:', error)
    // 获取失败时不显示任何菜单，确保权限安全
    console.log('菜单获取失败，为安全起见不显示任何菜单')
    userMenus.value = []
    ElMessage.warning('获取菜单权限失败，请检查用户角色配置')
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
  console.log('LayoutView组件挂载')
  
  // 如果权限数据还未加载且有token，尝试获取用户信息
  if (!userStore.permissionsLoaded && userStore.token) {
    console.log('权限数据未加载，获取用户信息...')
    const result = await userStore.fetchUserInfo()
    // 如果获取用户信息失败，说明token可能已失效
    if (!result) {
      console.log('获取用户信息失败，可能需要重新登录')
      return
    }
  }
  
  // 获取用户菜单
  await fetchUserMenus()
})

// 移除可能导致循环的监听器，只在需要时手动刷新
// 监听路由变化
watch(route, (to, from) => {
  console.log('路由变化:', { to: to.path, from: from.path })
  
  // 只在从个人中心页面返回时更新用户信息
  if (from.path === '/profile' && to.path !== '/profile') {
    console.log('从个人中心返回，延迟更新用户信息')
    // 添加较长的防抖，避免与个人中心页面的请求冲突
    setTimeout(async () => {
      if (!userStore.isFetchingUserInfo) {
        await userStore.fetchUserInfo()
      }
    }, 2000)
  }
}, { immediate: false })

// 显示个人中心
function showUserProfile() {
  router.push('/profile')
}

// 显示调试权限信息
function showDebugInfo() {
  const debugInfo = {
    用户ID: userInfo.value.userId || userInfo.value.id,
    用户名: userInfo.value.username,
    真实姓名: userInfo.value.realName,
    角色: userInfo.value.roles,
    权限列表: userInfo.value.permissions,
    权限数量: userInfo.value.permissions ? userInfo.value.permissions.length : 0,
    菜单数量: userMenus.value ? userMenus.value.length : 0
  }
  
  console.log('=== 权限调试信息 ===')
  console.log(debugInfo)
  console.log('完整用户信息:', userInfo.value)
  console.log('菜单信息:', userMenus.value)
  
  ElMessageBox.alert(`
    <div style="text-align: left;">
      <p><strong>用户:</strong> ${debugInfo.用户名} (${debugInfo.真实姓名})</p>
      <p><strong>角色:</strong> ${JSON.stringify(debugInfo.角色)}</p>
      <p><strong>权限数量:</strong> ${debugInfo.权限数量}</p>
      <p><strong>权限列表:</strong> ${debugInfo.权限列表 ? debugInfo.权限列表.slice(0, 5).join(', ') + (debugInfo.权限列表.length > 5 ? '...' : '') : '无'}</p>
      <p><strong>菜单数量:</strong> ${debugInfo.菜单数量}</p>
      <p style="color: #909399; font-size: 12px;">详细信息请查看浏览器控制台</p>
    </div>
  `, '权限调试信息', {
    dangerouslyUseHTMLString: true,
    confirmButtonText: '确定'
  })
}

// 退出登录
async function handleLogout() {
  ElMessageBox.confirm('确定退出登录?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    await userStore.logout()
    router.push('/login')
  }).catch(() => {})
}
</script>

<style scoped>
.layout-container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.layout-sidebar {
  flex-shrink: 0;
}

.layout-main {
  flex: 1;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.layout-header {
  flex-shrink: 0;
  height: 60px;
}

.layout-content {
  flex: 1;
  overflow: hidden;
}

.sidebar-container {
  transition: width 0.3s;
  background-color: #304156;
  overflow-x: hidden;
}

.sidebar-content {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-menu-area {
  flex: 1;
  overflow-y: auto;
}

.bottom-menu-area {
  margin-top: auto;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
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
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 60px);
}

.main-content {
  padding: 0;
  background-color: #f5f5f5;
  height: 100%;
  overflow: auto;
}

/* 主内容区域滚动条样式 */
.main-content::-webkit-scrollbar {
  width: 6px;
}

.main-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.main-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.main-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

.loading-container {
  padding: 20px;
  background: white;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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