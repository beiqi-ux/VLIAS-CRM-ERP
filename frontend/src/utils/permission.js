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
      VIEW: 'user',
      ADD: 'user',
      EDIT: 'user',
      DELETE: 'user',
      ROLE: 'user'
    },
    ROLE: {
      VIEW: 'role', 
      ADD: 'role',
      EDIT: 'role',
      DELETE: 'role',
      PERMISSION: 'role'
    },
    PERMISSION: {
      VIEW: 'permission',
      ADD: 'permission',
      EDIT: 'permission',
      DELETE: 'permission',
      SYNC: 'permission',
      RESET: 'permission',
      VALIDATE: 'permission'
    },
    MENU: {
      VIEW: 'menu',
      ADD: 'menu',
      EDIT: 'menu',
      DELETE: 'menu'
    },
    DICT: {
      VIEW: 'dict',
      ADD: 'dict',
      EDIT: 'dict',
      DELETE: 'dict'
    }
  },

  // 组织架构权限
  ORG: {
    ORGANIZATION: {
      VIEW: 'organization',
      ADD: 'organization',
      EDIT: 'organization',
      DELETE: 'organization'
    },
    DEPARTMENT: {
      VIEW: 'department',
      ADD: 'department',
      EDIT: 'department',
      DELETE: 'department'
    },
    POSITION: {
      VIEW: 'position',
      ADD: 'position',
      EDIT: 'position',
      DELETE: 'position'
    }
  },

  // 商品管理权限
  PRODUCT: {
    GOODS: {
      VIEW: 'goods',
      ADD: 'goods',
      EDIT: 'goods',
      DELETE: 'goods',
      AUDIT: 'goods'
    },
    CATEGORY: {
      VIEW: 'category',
      ADD: 'category',
      EDIT: 'category',
      DELETE: 'category'
    },
    BRAND: {
      VIEW: 'brand',
      ADD: 'brand',
      EDIT: 'brand',
      DELETE: 'brand'
    }
  }
} 