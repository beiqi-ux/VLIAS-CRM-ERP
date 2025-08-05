import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * 检查用户是否具有指定权限（支持权限继承）
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
  
  // 使用权限继承检查
  return hasPermissionWithInheritance(userPermissions, permission)
}

/**
 * 检查权限继承关系
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
  
  // 2. 继承权限检查
  const permissionType = getPermissionType(required)
  
  if (!permissionType) {
    return false
  }
  
  switch (permissionType) {
    case 3: // 三级权限，检查是否有二级或一级权限
      const submoduleCode = extractSubmoduleCode(required)
      if (submoduleCode && userPermissions.includes(submoduleCode)) {
        return true
      }
      // 继续检查一级权限
      const moduleCode = extractModuleCode(required)
      if (moduleCode && userPermissions.includes(moduleCode)) {
        return true
      }
      break
      
    case 2: // 二级权限，检查是否有一级权限
      const parentModuleCode = extractModuleCode(required)
      if (parentModuleCode && userPermissions.includes(parentModuleCode)) {
        return true
      }
      break
      
    case 1: // 一级权限，无继承关系
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
  
  // 三级权限包含冒号
  if (code.includes(':')) {
    return 3 // 三级权限(操作)
  }
  
  // 二级权限包含连字符但不包含冒号
  if (code.includes('-')) {
    return 2 // 二级权限(子模块)
  }
  
  // 一级权限只包含字母
  if (/^[a-z]+$/.test(code)) {
    return 1 // 一级权限(模块)
  }
  
  return null
}

/**
 * 解析权限编码，获取模块编码
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
 * 解析权限编码，获取子模块编码
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
 * 检查用户是否具有任一指定权限
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
 * 检查用户是否具有所有指定权限
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
 * Vue组合式API权限检查
 * @param {string} permission - 权限编码
 * @returns {import('vue').ComputedRef<boolean>} 权限状态的响应式引用
 */
export function usePermission(permission) {
  const userStore = useUserStore()
  
  return computed(() => {
    const userPermissions = userStore.userInfo.permissions || []
    return userPermissions.includes(permission)
  })
}

// 权限编码常量 - 3级权限体系
export const PERMISSIONS = {
  // 一级权限：系统管理模块
  SYSTEM: 'system',
  
  // 系统管理模块下的权限
  SYS: {
    // 二级权限：用户管理
    USER_MANAGEMENT: 'user-management',
    USER: {
      VIEW: 'user-management:view',
      CREATE: 'user-management:create', 
      EDIT: 'user-management:edit',
      DELETE: 'user-management:delete',
      RESET_PASSWORD: 'user-management:reset-password'
    },
    
    // 二级权限：角色管理
    ROLE_MANAGEMENT: 'role-management',
    ROLE: {
      VIEW: 'role-management:view',
      CREATE: 'role-management:create',
      EDIT: 'role-management:edit', 
      DELETE: 'role-management:delete',
      ASSIGN_PERMISSION: 'role-management:assign-permission'
    },
    
    // 二级权限：权限管理
    PERMISSION_MANAGEMENT: 'permission-management',
    PERMISSION: {
      VIEW: 'permission-management:view',
      CREATE: 'permission-management:create',
      EDIT: 'permission-management:edit',
      DELETE: 'permission-management:delete',
      SYNC: 'permission-management:sync',
      RESET: 'permission-management:reset',
      VALIDATE: 'permission-management:validate'
    },
    
    // 二级权限：菜单管理
    MENU_MANAGEMENT: 'menu-management',
    MENU: {
      VIEW: 'menu-management:view',
      CREATE: 'menu-management:create',
      EDIT: 'menu-management:edit',
      DELETE: 'menu-management:delete'
    },
    
    // 二级权限：字典管理
    DICT_MANAGEMENT: 'dict-management',
    DICT: {
      VIEW: 'dict-management:view',
      CREATE: 'dict-management:create',
      EDIT: 'dict-management:edit',
      DELETE: 'dict-management:delete'
    }
  },

  // 一级权限：组织架构模块
  ORG: 'org',
  
  // 组织架构模块下的权限
  ORGANIZATION: {
    // 二级权限：组织机构管理
    ORG_MANAGEMENT: 'org-management',
    ORG: {
      VIEW: 'org-management:view',
      CREATE: 'org-management:create',
      EDIT: 'org-management:edit',
      DELETE: 'org-management:delete'
    },
    
    // 二级权限：部门管理
    DEPT_MANAGEMENT: 'dept-management',
    DEPARTMENT: {
      VIEW: 'dept-management:view',
      CREATE: 'dept-management:create',
      EDIT: 'dept-management:edit',
      DELETE: 'dept-management:delete'
    },
    
    // 二级权限：岗位管理
    POSITION_MANAGEMENT: 'position-management',
    POSITION: {
      VIEW: 'position-management:view',
      CREATE: 'position-management:create',
      EDIT: 'position-management:edit',
      DELETE: 'position-management:delete'
    }
  },

  // 一级权限：商品管理模块
  PRODUCT: 'product',
  
  // 商品管理模块下的权限
  GOODS: {
    // 二级权限：商品信息管理
    PRODUCT_INFO_MANAGEMENT: 'product-info-management',
    INFO: {
      VIEW: 'product-info-management:view',
      CREATE: 'product-info-management:create',
      EDIT: 'product-info-management:edit',
      DELETE: 'product-info-management:delete'
    },
    
    // 二级权限：商品分类管理
    PRODUCT_CATEGORY_MANAGEMENT: 'product-category-management',
    CATEGORY: {
      VIEW: 'product-category-management:view',
      CREATE: 'product-category-management:create',
      EDIT: 'product-category-management:edit',
      DELETE: 'product-category-management:delete'
    },
    
    // 二级权限：商品品牌管理
    PRODUCT_BRAND_MANAGEMENT: 'product-brand-management',
    BRAND: {
      VIEW: 'product-brand-management:view',
      CREATE: 'product-brand-management:create',
      EDIT: 'product-brand-management:edit',
      DELETE: 'product-brand-management:delete'
    },
    
    // 二级权限：SKU管理
    PRODUCT_SKU_MANAGEMENT: 'product-sku-management',
    SKU: {
      VIEW: 'product-sku-management:view',
      CREATE: 'product-sku-management:create',
      EDIT: 'product-sku-management:edit',
      DELETE: 'product-sku-management:delete'
    },
    
    // 二级权限：商品信息管理 (GoodsList使用)
    PRODUCT_GOODS_MANAGEMENT: 'product-goods-management',
    GOODS: {
      VIEW: 'product-goods-management:view',
      CREATE: 'product-goods-management:create',
      EDIT: 'product-goods-management:edit',
      DELETE: 'product-goods-management:delete',
      AUDIT: 'product-goods-management:audit'
    }
  },

  // 一级权限：个人中心模块
  PROFILE: 'profile',
  
  // 个人中心模块下的权限
  PERSONAL: {
    // 二级权限：个人信息管理
    PROFILE_INFO_MANAGEMENT: 'profile-info-management',
    INFO: {
      VIEW: 'profile-info-management:view',
      EDIT: 'profile-info-management:edit',
      CHANGE_PASSWORD: 'profile-info-management:change-password'
    }
  }
} 