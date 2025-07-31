import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

// 路由配置
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: () => import('@/views/LayoutView.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'home',
          component: () => import('@/views/HomeView.vue'),
          meta: { title: '首页' }
        },
        // 用户管理
        {
          path: 'users',
          name: 'users',
          component: () => import('@/views/system/UserList.vue'),
          meta: { title: '用户管理' }
        },
        // 角色管理
        {
          path: 'roles',
          name: 'roles',
          component: () => import('@/views/system/RoleList.vue'),
          meta: { title: '角色管理' }
        },
        // 权限管理
        {
          path: 'permissions',
          name: 'permissions',
          component: () => import('@/views/system/PermissionList.vue'),
          meta: { title: '权限管理' }
        },
        // 菜单管理
        {
          path: 'menus',
          name: 'menus',
          component: () => import('@/views/system/MenuList.vue'),
          meta: { title: '菜单管理' }
        },
        // 组织机构管理
        {
          path: 'organizations',
          name: 'organizations',
          component: () => import('@/views/system/OrganizationList.vue'),
          meta: { title: '组织机构管理' }
        },
        // 部门管理
        {
          path: 'departments',
          name: 'departments',
          component: () => import('@/views/system/DepartmentList.vue'),
          meta: { title: '部门管理' }
        },
        // 岗位管理
        {
          path: 'positions',
          name: 'positions',
          component: () => import('@/views/system/PositionList.vue'),
          meta: { title: '岗位管理' }
        },
        // 个人中心
        {
          path: 'profile',
          name: 'profile',
          component: () => import('@/views/system/UserProfile.vue'),
          meta: { title: '个人中心' }
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'notFound',
      component: () => import('@/views/NotFoundView.vue')
    }
  ]
})

// 全局导航守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false)
  
  // 如果需要认证且用户未登录，重定向到登录页
  if (requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else if (to.path === '/login' && userStore.isLoggedIn) {
    // 如果已登录且尝试访问登录页，重定向到首页
    next('/')
  } else {
    next()
  }
})

export default router 