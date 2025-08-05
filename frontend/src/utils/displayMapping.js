/**
 * 显示映射工具
 * 将技术编码转换为用户友好的中文显示
 */

// 权限编码到中文名称的映射
export const PERMISSION_DISPLAY_MAP = {
  // 一级权限（模块）
  'org': '组织架构',
  'system': '系统管理', 
  'product': '商品管理',
  'order': '订单管理',
  'customer': '客户管理',
  'inventory': '库存管理',
  'finance': '财务管理',
  'member': '会员管理',
  'promotion': '营销管理',
  'purchase': '采购管理',
  'message': '消息管理',
  
  // 二级权限（子模块）
  'org-management': '组织机构管理',
  'dept-management': '部门管理',
  'position-management': '岗位管理',
  'user-management': '用户管理',
  'role-management': '角色管理',
  'permission-management': '权限管理',
  'menu-management': '菜单管理',
  'dict-management': '数据字典管理',
  'goods-management': '商品管理',
  'category-management': '分类管理',
  'brand-management': '品牌管理',
  'attribute-management': '属性管理',
  'specification-management': '规格管理',
  'sku-management': 'SKU管理',
  
  // 三级权限（操作）
  'org-management:view': '查看组织',
  'org-management:create': '新增组织',
  'org-management:edit': '编辑组织',
  'org-management:delete': '删除组织',
  'dept-management:view': '查看部门',
  'dept-management:create': '新增部门',
  'dept-management:edit': '编辑部门',
  'dept-management:delete': '删除部门',
  'position-management:view': '查看岗位',
  'position-management:create': '新增岗位',
  'position-management:edit': '编辑岗位',
  'position-management:delete': '删除岗位',
  'user-management:view': '查看用户',
  'user-management:create': '新增用户',
  'user-management:edit': '编辑用户',
  'user-management:delete': '删除用户',
  'user-management:reset-password': '重置密码',
  'role-management:view': '查看角色',
  'role-management:create': '新增角色',
  'role-management:edit': '编辑角色',
  'role-management:delete': '删除角色',
  'role-management:assign-permission': '分配权限',
  'permission-management:view': '查看权限',
  'permission-management:create': '新增权限',
  'permission-management:edit': '编辑权限',
  'permission-management:delete': '删除权限',
  'menu-management:view': '查看菜单',
  'menu-management:create': '新增菜单',
  'menu-management:edit': '编辑菜单',
  'menu-management:delete': '删除菜单',
  'goods-management:view': '查看商品',
  'goods-management:create': '新增商品',
  'goods-management:edit': '编辑商品',
  'goods-management:delete': '删除商品',
  'goods-management:export': '导出商品',
  'goods-management:import': '导入商品',
  'category-management:view': '查看分类',
  'category-management:create': '新增分类',
  'category-management:edit': '编辑分类',
  'category-management:delete': '删除分类',
  'brand-management:view': '查看品牌',
  'brand-management:create': '新增品牌',
  'brand-management:edit': '编辑品牌',
  'brand-management:delete': '删除品牌',
  'attribute-management:view': '查看属性',
  'attribute-management:create': '新增属性',
  'attribute-management:edit': '编辑属性',
  'attribute-management:delete': '删除属性',
  'specification-management:view': '查看规格',
  'specification-management:create': '新增规格',
  'specification-management:edit': '编辑规格',
  'specification-management:delete': '删除规格',
  'sku-management:view': '查看SKU',
  'sku-management:create': '新增SKU',
  'sku-management:edit': '编辑SKU',
  'sku-management:delete': '删除SKU'
}

// 菜单编码到中文名称的映射
export const MENU_DISPLAY_MAP = {
  'system': '系统管理',
  'org': '组织架构',
  'product': '商品管理',
  'order': '订单管理',
  'customer': '客户管理',
  'inventory': '库存管理',
  'finance': '财务管理',
  'member': '会员管理',
  'promotion': '营销管理',
  'purchase': '采购管理',
  'message': '消息管理',
  'profile': '个人中心',
  'dashboard': '数据面板',
  'users': '用户管理',
  'roles': '角色管理',
  'permissions': '权限管理',
  'menus': '菜单管理',
  'organizations': '组织机构',
  'departments': '部门管理',
  'positions': '岗位管理',
  'dicts': '数据字典',
  'goods': '商品管理',
  'categories': '分类管理',
  'brands': '品牌管理',
  'attributes': '商品属性',
  'specifications': '商品规格',
  'skus': 'SKU管理'
}

// 路由路径到中文名称的映射
export const ROUTE_DISPLAY_MAP = {
  '/home': '首页',
  '/profile': '个人中心',
  '/users': '用户管理',
  '/roles': '角色管理', 
  '/permissions': '权限管理',
  '/menus': '菜单管理',
  '/organizations': '组织机构管理',
  '/departments': '部门管理',
  '/positions': '岗位管理',
  '/dicts': '数据字典管理',
  '/goods': '商品管理',
  '/categories': '分类管理',
  '/brands': '品牌管理',
  '/attributes': '商品属性管理',
  '/specifications': '商品规格管理',
  '/skus': 'SKU管理'
}

// 操作类型到中文名称的映射
export const ACTION_DISPLAY_MAP = {
  'view': '查看',
  'create': '新增',
  'edit': '编辑',
  'delete': '删除',
  'export': '导出',
  'import': '导入',
  'audit': '审核',
  'approve': '批准',
  'reject': '拒绝',
  'reset-password': '重置密码',
  'assign-permission': '分配权限',
  'change-status': '状态变更'
}

/**
 * 获取权限的友好显示名称
 * @param {string} permissionCode - 权限编码
 * @returns {string} 友好显示名称
 */
export function getPermissionDisplayName(permissionCode) {
  if (!permissionCode) return '未知权限'
  
  // 直接查找映射
  if (PERMISSION_DISPLAY_MAP[permissionCode]) {
    return PERMISSION_DISPLAY_MAP[permissionCode]
  }
  
  // 如果是三级权限，尝试分解
  if (permissionCode.includes(':')) {
    const [moduleCode, actionCode] = permissionCode.split(':')
    const moduleName = PERMISSION_DISPLAY_MAP[moduleCode] || moduleCode
    const actionName = ACTION_DISPLAY_MAP[actionCode] || actionCode
    return `${actionName}${moduleName}`
  }
  
  // 如果是二级权限，尝试去掉-management后缀
  if (permissionCode.includes('-management')) {
    const baseCode = permissionCode.replace('-management', '')
    const baseName = PERMISSION_DISPLAY_MAP[baseCode] || baseCode
    return `${baseName}管理`
  }
  
  return permissionCode
}

/**
 * 获取菜单的友好显示名称
 * @param {string} menuCode - 菜单编码
 * @returns {string} 友好显示名称
 */
export function getMenuDisplayName(menuCode) {
  if (!menuCode) return '未知菜单'
  return MENU_DISPLAY_MAP[menuCode] || menuCode
}

/**
 * 获取路由的友好显示名称
 * @param {string} routePath - 路由路径
 * @returns {string} 友好显示名称
 */
export function getRouteDisplayName(routePath) {
  if (!routePath) return '未知页面'
  return ROUTE_DISPLAY_MAP[routePath] || routePath
}

/**
 * 获取操作的友好显示名称
 * @param {string} actionCode - 操作编码
 * @returns {string} 友好显示名称
 */
export function getActionDisplayName(actionCode) {
  if (!actionCode) return '未知操作'
  return ACTION_DISPLAY_MAP[actionCode] || actionCode
}

/**
 * 批量转换权限编码为显示名称
 * @param {string[]} permissionCodes - 权限编码数组
 * @returns {Object[]} 包含编码和显示名称的对象数组
 */
export function batchConvertPermissions(permissionCodes) {
  if (!Array.isArray(permissionCodes)) return []
  
  return permissionCodes.map(code => ({
    code,
    displayName: getPermissionDisplayName(code),
    type: getPermissionType(code)
  }))
}

/**
 * 获取权限类型的中文描述
 * @param {string} permissionCode - 权限编码
 * @returns {string} 权限类型描述
 */
function getPermissionType(permissionCode) {
  if (!permissionCode) return '未知'
  
  if (permissionCode.includes(':')) {
    return '操作权限'
  } else if (permissionCode.includes('-')) {
    return '功能模块'
  } else {
    return '系统模块'
  }
}

/**
 * 生成面包屑导航显示名称
 * @param {string} routePath - 当前路由路径
 * @returns {string[]} 面包屑名称数组
 */
export function generateBreadcrumbNames(routePath) {
  const breadcrumbs = ['首页']
  
  if (!routePath || routePath === '/home') {
    return breadcrumbs
  }
  
  // 移除开头的斜杠并分割路径
  const pathSegments = routePath.replace(/^\//, '').split('/')
  
  pathSegments.forEach(segment => {
    const displayName = getRouteDisplayName(`/${segment}`)
    if (displayName !== `/${segment}`) {
      breadcrumbs.push(displayName)
    }
  })
  
  return breadcrumbs
} 