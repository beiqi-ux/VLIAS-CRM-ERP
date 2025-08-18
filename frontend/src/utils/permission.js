import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * 检查用户是否具有指定权限（用于UI显示，支持权限继承）
 * @param {string} permission - 权限编码
 * @returns {boolean} 是否具有权限
 */
export function hasPermission(permission) {
  const userStore = useUserStore()
  
  // 如果用户未登录，返回false
  if (!userStore.isLoggedIn) {
    return false
  }
  
  // 如果权限数据还未完全加载，返回false
  // 因为LayoutView会显示加载状态，不会渲染权限相关内容
  if (!userStore.permissionsLoaded) {
    return false
  }
  
  const userPermissions = userStore.userInfo.permissions
  
  // 如果权限数据不是数组，返回false
  if (!Array.isArray(userPermissions)) {
    return false
  }
  
  // 使用权限继承检查（用于UI显示）
  return hasPermissionWithInheritance(userPermissions, permission)
}

/**
 * 检查用户是否具有指定操作权限（用于按钮功能控制，严格检查）
 * @param {string} permission - 权限编码
 * @returns {boolean} 是否具有权限
 */
export function hasActionPermission(permission) {
  const userStore = useUserStore()
  
  // 如果用户未登录，返回false
  if (!userStore.isLoggedIn) {
    return false
  }
  
  // 如果权限数据还未完全加载，返回false
  if (!userStore.permissionsLoaded) {
    return false
  }
  
  const userPermissions = userStore.userInfo.permissions
  
  // 如果权限数据不是数组，返回false
  if (!Array.isArray(userPermissions)) {
    return false
  }
  
  // 严格权限检查（用于操作控制）
  return hasStrictPermission(userPermissions, permission)
}

/**
 * 严格权限检查（不允许继承，用于操作权限控制）
 * @param {string[]} userPermissions - 用户权限编码列表
 * @param {string} requiredPermission - 需要检查的权限编码
 * @returns {boolean} 是否有权限
 */
export function hasStrictPermission(userPermissions, requiredPermission) {
  if (!userPermissions || userPermissions.length === 0 || 
      !requiredPermission || requiredPermission.trim() === '') {
    return false
  }

  const required = requiredPermission.trim()
  
  // 只进行直接权限检查，不允许继承
  return userPermissions.includes(required)
}

/**
 * 检查权限继承关系（用于UI显示权限检查）
 * @param {string[]} userPermissions - 用户权限编码列表
 * @param {string} requiredPermission - 需要检查的权限编码
 * @returns {boolean} 是否有权限
 */
export function hasPermissionWithInheritance(userPermissions, requiredPermission) {
  if (!userPermissions || userPermissions.length === 0 || 
      !requiredPermission || requiredPermission.trim() === '') {
    return false
  }

  const required = requiredPermission.trim()
  
  // 1. 直接权限检查
  if (userPermissions.includes(required)) {
    return true
  }
  
  // 2. 继承权限检查（仅用于UI显示）
  const permissionType = getPermissionType(required)
  
  if (!permissionType) {
    return false
  }
  
  switch (permissionType) {
  case 3: { // 三级权限（操作），检查是否有二级或一级权限
    const submoduleCode = extractSubmoduleCode(required)
    // 检查是否有完整的二级权限
    if (submoduleCode && userPermissions.includes(submoduleCode)) {
      return true
    }
    // 继续检查一级权限
    const moduleCode = extractModuleCode(required)
    if (moduleCode && userPermissions.includes(moduleCode)) {
      return true
    }
    break
  }
      
  case 2: { // 二级权限（子模块），检查是否有一级权限
    const parentModuleCode = extractModuleCode(required)
    if (parentModuleCode && userPermissions.includes(parentModuleCode)) {
      return true
    }
    break
  }
      
  case 1: // 一级权限（模块），无继承关系
  default:
    break
  }
  
  return false
}

/**
 * 判断权限编码的类型
 * @param {string} permissionCode - 权限编码
 * @returns {number|null} 权限类型 1-一级权限(模块) 2-二级权限(子模块) 3-三级权限(操作)
 */
export function getPermissionType(permissionCode) {
  if (!permissionCode || permissionCode.trim() === '') {
    return null
  }

  const code = permissionCode.trim()
  
  // 三级权限包含冒号（操作权限）
  if (code.includes(':')) {
    return 3 // 三级权限(操作)
  }
  
  // 二级权限包含连字符但不包含冒号（子模块权限）
  if (code.includes('-')) {
    return 2 // 二级权限(子模块)
  }
  
  // 一级权限只包含字母（模块权限）
  if (/^[a-z]+$/.test(code)) {
    return 1 // 一级权限(模块)
  }
  
  return null
}

/**
 * 解析权限编码，获取模块编码（一级权限）
 * @param {string} permissionCode - 权限编码
 * @returns {string|null} 模块编码
 */
export function extractModuleCode(permissionCode) {
  if (!permissionCode || permissionCode.trim() === '') {
    return null
  }

  const code = permissionCode.trim()
  
  // 如果是三级权限（包含:），先去掉操作部分
  let moduleCode = code
  if (code.includes(':')) {
    moduleCode = code.split(':')[0]
  }
  
  // 如果是二级权限（包含-），取第一个部分作为模块编码
  if (moduleCode.includes('-')) {
    return moduleCode.split('-')[0]
  }
  
  // 如果是一级权限，直接返回
  return moduleCode
}

/**
 * 解析权限编码，获取子模块编码（二级权限）
 * @param {string} permissionCode - 权限编码
 * @returns {string|null} 子模块编码，如果不是三级权限则返回null
 */
export function extractSubmoduleCode(permissionCode) {
  if (!permissionCode || permissionCode.trim() === '') {
    return null
  }

  const code = permissionCode.trim()
  
  // 只有三级权限才有子模块编码
  if (code.includes(':')) {
    return code.split(':')[0]
  }
  
  return null
}

/**
 * 检查用户是否具有任一指定权限（UI显示权限检查）
 * @param {string[]} permissions - 权限编码数组
 * @returns {boolean} 是否具有任一权限
 */
export function hasAnyPermission(permissions) {
  if (!permissions || permissions.length === 0) {
    return false
  }
  
  return permissions.some(permission => hasPermission(permission))
}

/**
 * 检查用户是否具有所有指定权限（UI显示权限检查）
 * @param {string[]} permissions - 权限编码数组
 * @returns {boolean} 是否具有所有权限
 */
export function hasAllPermissions(permissions) {
  if (!permissions || permissions.length === 0) {
    return true
  }
  
  return permissions.every(permission => hasPermission(permission))
}

/**
 * 检查用户是否具有任一指定操作权限（严格检查）
 * @param {string[]} permissions - 权限编码数组
 * @returns {boolean} 是否具有任一权限
 */
export function hasAnyActionPermission(permissions) {
  if (!permissions || permissions.length === 0) {
    return false
  }
  
  return permissions.some(permission => hasActionPermission(permission))
}

/**
 * 检查用户是否具有所有指定操作权限（严格检查）
 * @param {string[]} permissions - 权限编码数组
 * @returns {boolean} 是否具有所有权限
 */
export function hasAllActionPermissions(permissions) {
  if (!permissions || permissions.length === 0) {
    return true
  }
  
  return permissions.every(permission => hasActionPermission(permission))
}

/**
 * Vue组合式API权限检查（UI显示）
 * @param {string} permission - 权限编码
 * @returns {import('vue').ComputedRef<boolean>} 权限状态的响应式引用
 */
export function usePermission(permission) {
  const userStore = useUserStore()
  
  return computed(() => {
    if (!userStore.isLoggedIn || !userStore.permissionsLoaded) {
      return false
    }
    
    const userPermissions = userStore.userInfo.permissions
    if (!Array.isArray(userPermissions)) {
      return false
    }
    
    return hasPermissionWithInheritance(userPermissions, permission)
  })
}

/**
 * Vue组合式API操作权限检查（严格检查）
 * @param {string} permission - 权限编码
 * @returns {import('vue').ComputedRef<boolean>} 权限状态的响应式引用
 */
export function useActionPermission(permission) {
  const userStore = useUserStore()
  
  return computed(() => {
    if (!userStore.isLoggedIn || !userStore.permissionsLoaded) {
      return false
    }
    
    const userPermissions = userStore.userInfo.permissions
    if (!Array.isArray(userPermissions)) {
      return false
    }
    
    return hasStrictPermission(userPermissions, permission)
  })
}

/**
 * 获取权限类型文本
 * @param {number} permissionType - 权限类型
 * @returns {string} 权限类型文本
 */
export function getPermissionTypeText(permissionType) {
  const typeMap = {
    1: '一级权限(模块)',
    2: '二级权限(子模块)', 
    3: '三级权限(操作)'
  }
  return typeMap[permissionType] || '未知类型'
}

/**
 * 获取权限层级深度
 * @param {string} permissionCode - 权限编码
 * @returns {number} 层级深度
 */
export function getPermissionDepth(permissionCode) {
  const type = getPermissionType(permissionCode)
  return type || 0
}

/**
 * 检查权限编码格式是否正确
 * @param {string} permissionCode - 权限编码
 * @param {number} expectedType - 期望的权限类型
 * @returns {boolean} 格式是否正确
 */
export function validatePermissionCode(permissionCode, expectedType) {
  const actualType = getPermissionType(permissionCode)
  return actualType === expectedType
}

// 导出权限类型常量
export const PERMISSION_TYPES = {
  MODULE: 1,        // 一级权限(模块)
  SUBMODULE: 2,     // 二级权限(子模块)
  ACTION: 3         // 三级权限(操作)
}

// 导出常用操作权限后缀
export const ACTION_TYPES = {
  VIEW: 'view',
  CREATE: 'create', 
  EDIT: 'edit',
  DELETE: 'delete',
  EXPORT: 'export',
  IMPORT: 'import'
}

// 导出权限常量对象（与后端权限编码保持一致）
export const PERMISSIONS = {
  // 系统管理模块
  SYS: {
    // 用户管理
    USER: {
      VIEW: 'user-management:view',
      CREATE: 'user-management:create',
      EDIT: 'user-management:edit',
      DELETE: 'user-management:delete',
      RESET_PASSWORD: 'user-management:reset-password',
      EXPORT: 'user-management:export',
      ASSIGN_ROLE: 'user-management:assign-role'
    },
    // 角色管理
    ROLE: {
      VIEW: 'role-management:view',
      CREATE: 'role-management:create',
      EDIT: 'role-management:edit',
      DELETE: 'role-management:delete',
      ASSIGN_PERMISSION: 'role-management:assign-permission',
      EXPORT: 'role-management:export'
    },
    // 菜单管理
    MENU: {
      VIEW: 'menu-management:view',
      CREATE: 'menu-management:create',
      EDIT: 'menu-management:edit',
      DELETE: 'menu-management:delete',
      EXPORT: 'menu-management:export'
    },
    // 权限管理
    PERMISSION: {
      VIEW: 'permission-management:view',
      CREATE: 'permission-management:create',
      EDIT: 'permission-management:edit',
      DELETE: 'permission-management:delete',
      SYNC: 'permission-management:validate',
      EXPORT: 'permission-management:export'
    },
    // 字典管理
    DICT: {
      VIEW: 'dict-management:view',
      CREATE: 'dict-management:create',
      EDIT: 'dict-management:edit',
      DELETE: 'dict-management:delete',
      EXPORT: 'dict-management:export'
    }
  },
  
  // 组织架构模块
  ORGANIZATION: {
    // 部门管理
    DEPARTMENT: {
      VIEW: 'dept-management:view',
      CREATE: 'dept-management:create',
      EDIT: 'dept-management:edit',
      DELETE: 'dept-management:delete',
      EXPORT: 'dept-management:export'
    },
    // 职位管理
    POSITION: {
      VIEW: 'position-management:view',
      CREATE: 'position-management:create',
      EDIT: 'position-management:edit',
      DELETE: 'position-management:delete',
      EXPORT: 'position-management:export'
    }
  },
  
  // 商品管理模块
  GOODS: {
    // 商品管理
    GOODS: {
      VIEW: 'product-info-management:view',
      CREATE: 'product-info-management:create',
      EDIT: 'product-info-management:edit',
      DELETE: 'product-info-management:delete',
      AUDIT: 'product-info-management:audit',
      EXPORT: 'product-info-management:export'
    },
    // SKU管理
    SKU: {
      VIEW: 'product-sku-management:view',
      CREATE: 'product-sku-management:create',
      EDIT: 'product-sku-management:edit',
      DELETE: 'product-sku-management:delete',
      EXPORT: 'product-sku-management:export'
    },
    // 品牌管理
    BRAND: {
      VIEW: 'product-brand-management:view',
      CREATE: 'product-brand-management:create',
      EDIT: 'product-brand-management:edit',
      DELETE: 'product-brand-management:delete',
      EXPORT: 'product-brand-management:export'
    },
    // 分类管理
    CATEGORY: {
      VIEW: 'product-category-management:view',
      CREATE: 'product-category-management:create',
      EDIT: 'product-category-management:edit',
      DELETE: 'product-category-management:delete',
      EXPORT: 'product-category-management:export'
    },
    // 规格分类管理
    SPECIFICATION_CATEGORY: {
      VIEW: 'product-specification-management:view',
      CREATE: 'product-specification-management:create',
      EDIT: 'product-specification-management:edit',
      DELETE: 'product-specification-management:delete',
      EXPORT: 'product-specification-management:export'
    },
    // 规格管理
    SPECIFICATION: {
      VIEW: 'product-specification-management:view',
      CREATE: 'product-specification-management:create',
      EDIT: 'product-specification-management:edit',
      DELETE: 'product-specification-management:delete',
      EXPORT: 'product-specification-management:export'
    }
  },
  
  // 采购管理模块
  PURCHASE: {
    // 供应商商品管理
    SUPPLIER_GOODS: {
      VIEW: 'supplier-goods-management:view',
      CREATE: 'supplier-goods-management:create',
      EDIT: 'supplier-goods-management:update',
      DELETE: 'supplier-goods-management:delete',
      EXPORT: 'supplier-goods-management:export',
      IMPORT: 'supplier-goods-management:import',
      COMPARE: 'supplier-goods-management:compare',
      VIEW_HISTORY: 'supplier-goods-management:view-history'
    },
    // 采购入库管理
    RECEIPT: {
      LIST: 'pur-receipt-management:list',
      VIEW: 'pur-receipt-management:view',
      CREATE: 'pur-receipt-management:create',
      EDIT: 'pur-receipt-management:edit',
      DELETE: 'pur-receipt-management:delete',
      SUBMIT: 'pur-receipt-management:submit',
      AUDIT: 'pur-receipt-management:audit',
      CONFIRM: 'pur-receipt-management:confirm',
      CANCEL: 'pur-receipt-management:cancel',
      EXPORT: 'pur-receipt-management:export',
      IMPORT: 'pur-receipt-management:import',
      PRINT: 'pur-receipt-management:print',
      COPY: 'pur-receipt-management:copy',
      STATISTICS: 'pur-receipt-management:statistics'
    },
    // 采购退货管理
    RETURN: {
      LIST: 'pur-return-management:list',
      VIEW: 'pur-return-management:view',
      CREATE: 'pur-return-management:create',
      EDIT: 'pur-return-management:edit',
      DELETE: 'pur-return-management:delete',
      SUBMIT: 'pur-return-management:submit',
      AUDIT: 'pur-return-management:audit',
      CONFIRM: 'pur-return-management:confirm',
      CANCEL: 'pur-return-management:cancel',
      EXPORT: 'pur-return-management:export',
      PRINT: 'pur-return-management:print',
      COPY: 'pur-return-management:copy',
      STATISTICS: 'pur-return-management:statistics'
    },
    // 供应商对账管理
    RECONCILIATION: {
      VIEW: 'reconciliation-management:view',
      CREATE: 'reconciliation-management:add',
      EDIT: 'reconciliation-management:edit',
      DELETE: 'reconciliation-management:delete',
      CONFIRM: 'reconciliation-management:confirm',
      SETTLE: 'reconciliation-management:settle',
      AUTO_GENERATE: 'reconciliation-management:auto-generate',
      EXPORT: 'reconciliation-management:export',
      PRINT: 'reconciliation-management:print'
    }
  },
  
  // 库存管理模块
  INVENTORY: {
    // 仓库管理
    WAREHOUSE: {
      VIEW: 'warehouse-management:view',
      CREATE: 'warehouse-management:create',
      EDIT: 'warehouse-management:edit',
      DELETE: 'warehouse-management:delete',
      CHANGE_STATUS: 'warehouse-management:change-status',
      EXPORT: 'warehouse-management:export'
    },
    // 库位管理
    LOCATION: {
      VIEW: 'warehouse-location-management:view',
      CREATE: 'warehouse-location-management:create',
      EDIT: 'warehouse-location-management:edit',
      DELETE: 'warehouse-location-management:delete',
      CHANGE_STATUS: 'warehouse-location-management:change-status',
      EXPORT: 'warehouse-location-management:export'
    },
    // 库存查询
    QUERY: {
      VIEW: 'inventory-query-management:view',
      EXPORT: 'inventory-query-management:export'
    },
    // 库存盘点
    STOCKTAKING: {
      VIEW: 'inventory-stocktaking-management:view',
      CREATE: 'inventory-stocktaking-management:create',
      EDIT: 'inventory-stocktaking-management:edit',
      DELETE: 'inventory-stocktaking-management:delete',
      EXECUTE: 'inventory-stocktaking-management:execute',
      CONFIRM: 'inventory-stocktaking-management:confirm',
      EXPORT: 'inventory-stocktaking-management:export'
    },
    // 库存调拨
    TRANSFER: {
      VIEW: 'inventory-transfer-management:view',
      CREATE: 'inventory-transfer-management:create',
      EDIT: 'inventory-transfer-management:edit',
      DELETE: 'inventory-transfer-management:delete',
      CONFIRM: 'inventory-transfer-management:confirm',
      EXPORT: 'inventory-transfer-management:export'
    }
  }
} 