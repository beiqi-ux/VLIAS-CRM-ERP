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
      redirect: '/home',
      children: [
        {
          path: 'home',
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
        // 数据字典管理
        {
          path: 'dicts',
          name: 'dicts',
          component: () => import('@/views/system/DictList.vue'),
          meta: { title: '数据字典管理' }
        },
        // 商品管理
        {
          path: 'goods',
          name: 'goods',
          component: () => import('@/views/product/GoodsList.vue'),
          meta: { title: '商品管理' }
        },
        // 商品编辑
        {
          path: 'goods/edit/:id?',
          name: 'goods-edit',
          component: () => import('@/views/product/GoodsEdit.vue'),
          meta: { title: '商品编辑' }
        },
        // 分类管理
        {
          path: 'categories',
          name: 'categories',
          component: () => import('@/views/product/CategoryList.vue'),
          meta: { title: '分类管理' }
        },
        // 品牌管理
        {
          path: 'brands',
          name: 'brands',
          component: () => import('@/views/product/BrandList.vue'),
          meta: { title: '品牌管理' }
        },
        // 商品属性管理
        {
          path: 'attributes',
          name: 'attributes',
          component: () => import('@/views/product/AttributeList.vue'),
          meta: { title: '商品属性管理' }
        },
        // 商品规格管理
        {
          path: 'specifications',
          name: 'specifications',
          component: () => import('@/views/product/SpecificationList.vue'),
          meta: { title: '商品规格管理' }
        },
        // SKU管理
        {
          path: 'skus',
          name: 'skus',
          component: () => import('@/views/product/SkuList.vue'),
          meta: { title: 'SKU管理' }
        },
        // 供应商管理
        {
          path: 'suppliers',
          name: 'suppliers',
          component: () => import('@/views/purchase/SupplierList.vue'),
          meta: { title: '供应商管理' }
        },
        // 采购订单管理
        {
          path: 'purchase/order',
          name: 'purchase-order',
          component: () => import('@/views/purchase/PurchaseOrderList.vue'),
          meta: { title: '采购订单管理' }
        },
        // 新增采购订单
        {
          path: 'purchase/order/create',
          name: 'purchase-order-create',
          component: () => import('@/views/purchase/PurchaseOrderForm.vue'),
          meta: { title: '新增采购订单' }
        },
        // 编辑采购订单
        {
          path: 'purchase/order/edit/:id',
          name: 'purchase-order-edit',
          component: () => import('@/views/purchase/PurchaseOrderForm.vue'),
          meta: { title: '编辑采购订单' }
        },
        // 查看采购订单
        {
          path: 'purchase/order/view/:id',
          name: 'purchase-order-view',
          component: () => import('@/views/purchase/PurchaseOrderDetail.vue'),
          meta: { title: '采购订单详情' }
        },
        // 采购入库管理
        {
          path: 'purchase/receipt',
          name: 'purchase-receipt',
          component: () => import('@/views/purchase/PurReceiptList.vue'),
          meta: { title: '采购入库管理' }
        },
        // 新增采购入库单
        {
          path: 'purchase/receipt/create',
          name: 'purchase-receipt-create',
          component: () => import('@/views/purchase/PurReceiptForm.vue'),
          meta: { title: '新增采购入库单' }
        },
        // 编辑采购入库单
        {
          path: 'purchase/receipt/edit/:id',
          name: 'purchase-receipt-edit',
          component: () => import('@/views/purchase/PurReceiptForm.vue'),
          meta: { title: '编辑采购入库单' }
        },
        // 查看采购入库单
        {
          path: 'purchase/receipt/view/:id',
          name: 'purchase-receipt-view',
          component: () => import('@/views/purchase/PurReceiptDetail.vue'),
          meta: { title: '采购入库单详情' }
        },
        // 采购退货管理
        {
          path: 'purchase/return',
          name: 'purchase-return',
          component: () => import('@/views/purchase/return/index.vue'),
          meta: { 
            title: '采购退货管理',
            requiresAuth: true,
            permission: 'PURCHASE.RETURN.LIST'
          }
        },
        // 新增采购退货单
        {
          path: 'purchase/return/create',
          name: 'purchase-return-create',
          component: () => import('@/views/purchase/return/form.vue'),
          meta: { 
            title: '新建退货单',
            requiresAuth: true,
            permission: 'PURCHASE.RETURN.CREATE'
          }
        },
        // 编辑采购退货单
        {
          path: 'purchase/return/edit/:id',
          name: 'purchase-return-edit',
          component: () => import('@/views/purchase/return/form.vue'),
          meta: { 
            title: '编辑退货单',
            requiresAuth: true,
            permission: 'PURCHASE.RETURN.EDIT'
          }
        },
        // 查看采购退货单
        {
          path: 'purchase/return/view/:id',
          name: 'purchase-return-view',
          component: () => import('@/views/purchase/return/form.vue'),
          meta: { 
            title: '查看退货单',
            requiresAuth: true,
            permission: 'PURCHASE.RETURN.VIEW'
          }
        },
        // 新增采购退货单
        {
          path: 'purchase/return/create',
          name: 'purchase-return-create',
          component: () => import('@/views/purchase/return/form.vue'),
          meta: { title: '新增采购退货单' }
        },
        // 编辑采购退货单
        {
          path: 'purchase/return/edit/:id',
          name: 'purchase-return-edit',
          component: () => import('@/views/purchase/return/form.vue'),
          meta: { title: '编辑采购退货单' }
        },
        // 查看采购退货单
        {
          path: 'purchase/return/view/:id',
          name: 'purchase-return-view',
          component: () => import('@/views/purchase/return/form.vue'),
          meta: { title: '采购退货单详情' }
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
router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth !== false)
  
  // 如果需要认证且用户未登录，重定向到登录页
  if (requiresAuth && !userStore.isLoggedIn) {
    next('/login')
    return
  }
  
  if (to.path === '/login' && userStore.isLoggedIn) {
    // 如果已登录且尝试访问登录页，重定向到首页
    next('/')
    return
  }
  
  // 简化权限验证逻辑，不在路由守卫中进行权限数据获取
  // 让各个页面自己处理权限数据的获取
  next()
})

export default router 