import { computed } from 'vue'
import { useUserStore } from '@/stores/user'

/**
 * 检查用户是否具有指定权限
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
  
  // 检查是否包含指定权限
  return userPermissions.includes(permission)
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

// 权限编码常量
export const PERMISSIONS = {
  // 系统管理权限（基于菜单中的permissionCode）
  SYS: {
    USER: {
      VIEW: 'user:view',
      ADD: 'user:add',
      EDIT: 'user:edit',
      DELETE: 'user:delete',
      ROLE: 'user:assign-role'
    },
    ROLE: {
      VIEW: 'role:view', 
      ADD: 'role:add',
      EDIT: 'role:edit',
      DELETE: 'role:delete',
      PERMISSION: 'role:assign'
    },
    PERMISSION: {
      VIEW: 'permission:view',
      ADD: 'permission:add',
      EDIT: 'permission:edit',
      DELETE: 'permission:delete',
      SYNC: 'permission:sync',
      RESET: 'permission:reset',
      VALIDATE: 'permission:validate'
    },
    MENU: {
      VIEW: 'menu:view',
      ADD: 'menu:add',
      EDIT: 'menu:edit',
      DELETE: 'menu:delete'
    },
    DICT: {
      VIEW: 'dict:view',
      ADD: 'dict:add',
      EDIT: 'dict:edit',
      DELETE: 'dict:delete'
    }
  },

  // 组织架构权限
  ORG: {
    ORGANIZATION: {
      VIEW: 'organization:view',
      ADD: 'organization:add',
      EDIT: 'organization:edit',
      DELETE: 'organization:delete'
    },
    DEPARTMENT: {
      VIEW: 'department:view',
      ADD: 'department:add',
      EDIT: 'department:edit',
      DELETE: 'department:delete'
    },
    POSITION: {
      VIEW: 'position:view',
      ADD: 'position:add',
      EDIT: 'position:edit',
      DELETE: 'position:delete'
    }
  },

  // 商品管理权限
  PRODUCT: {
    GOODS: {
      VIEW: 'product:view',
      ADD: 'product:add',
      EDIT: 'product:edit',
      DELETE: 'product:delete',
      AUDIT: 'product:audit'
    },
    CATEGORY: {
      VIEW: 'category:view',
      ADD: 'category:add',
      EDIT: 'category:edit',
      DELETE: 'category:delete'
    },
    BRAND: {
      VIEW: 'brand:view',
      ADD: 'brand:add',
      EDIT: 'brand:edit',
      DELETE: 'brand:delete'
    }
  }
} 