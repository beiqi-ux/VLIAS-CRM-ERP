// 权限相关的组合式 API
import { 
  hasPermission as _hasPermission,
  hasActionPermission as _hasActionPermission,
  hasAnyPermission as _hasAnyPermission,
  hasAllPermissions as _hasAllPermissions,
  hasAnyActionPermission as _hasAnyActionPermission,
  hasAllActionPermissions as _hasAllActionPermissions,
  PERMISSIONS,
  PERMISSION_TYPES,
  ACTION_TYPES
} from '@/utils/permission'

// 组合式 API 函数，返回权限相关的方法
export function usePermission() {
  return {
    hasPermission: _hasPermission,
    hasActionPermission: _hasActionPermission,
    hasAnyPermission: _hasAnyPermission,
    hasAllPermissions: _hasAllPermissions,
    hasAnyActionPermission: _hasAnyActionPermission,
    hasAllActionPermissions: _hasAllActionPermissions,
    PERMISSIONS,
    PERMISSION_TYPES,
    ACTION_TYPES
  }
}

// 也可以用于 composition API 中的 action 权限检查
export function useActionPermission() {
  return {
    hasActionPermission: _hasActionPermission,
    hasAnyActionPermission: _hasAnyActionPermission,
    hasAllActionPermissions: _hasAllActionPermissions,
    ACTION_TYPES
  }
}

// 直接导出函数，兼容直接导入的使用方式
export {
  _hasPermission as hasPermission,
  _hasActionPermission as hasActionPermission,
  _hasAnyPermission as hasAnyPermission,
  _hasAllPermissions as hasAllPermissions,
  _hasAnyActionPermission as hasAnyActionPermission,
  _hasAllActionPermissions as hasAllActionPermissions,
  PERMISSIONS,
  PERMISSION_TYPES,
  ACTION_TYPES
} 