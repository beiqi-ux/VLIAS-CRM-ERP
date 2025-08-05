<template>
  <div class="permission-display">
    <div class="permission-item">
      <span class="permission-name">{{ permission.permissionName }}</span>
      <el-tag 
        :type="getPermissionTypeTag(permission.permissionType)" 
        size="small" 
        class="permission-type-tag"
      >
        {{ getPermissionTypeLabel(permission.permissionType) }}
      </el-tag>
      <span class="permission-code">{{ permission.permissionCode }}</span>
      <span v-if="permission.permissionPath" class="permission-path">
        {{ permission.permissionPath }}
      </span>
      <span v-if="showInheritance && hasInheritedPermission" class="inherited-indicator">
        <el-icon><Promotion /></el-icon> 继承
      </span>
    </div>
    <div v-if="permission.description" class="permission-description">
      {{ permission.description }}
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Promotion } from '@element-plus/icons-vue'

const props = defineProps({
  permission: {
    type: Object,
    required: true
  },
  userPermissions: {
    type: Array,
    default: () => []
  },
  showInheritance: {
    type: Boolean,
    default: false
  }
})

// 获取权限类型标签颜色
const getPermissionTypeTag = (type) => {
  switch (type) {
    case 1: return 'primary'
    case 2: return 'success'
    case 3: return 'warning'
    default: return 'info'
  }
}

// 获取权限类型标签文本
const getPermissionTypeLabel = (type) => {
  switch (type) {
    case 1: return '一级(模块)'
    case 2: return '二级(子模块)'
    case 3: return '三级(操作)'
    default: return '未知'
  }
}

// 检查是否为继承权限
const hasInheritedPermission = computed(() => {
  if (!props.showInheritance || !props.userPermissions.length) {
    return false
  }
  
  // 直接拥有权限
  if (props.userPermissions.includes(props.permission.permissionCode)) {
    return false
  }
  
  // 检查继承权限
  const permissionType = getPermissionType(props.permission.permissionCode)
  
  if (permissionType === 3) {
    // 三级权限检查二级和一级
    const submoduleCode = extractSubmoduleCode(props.permission.permissionCode)
    if (submoduleCode && props.userPermissions.includes(submoduleCode)) {
      return true
    }
    const moduleCode = extractModuleCode(props.permission.permissionCode)
    if (moduleCode && props.userPermissions.includes(moduleCode)) {
      return true
    }
  } else if (permissionType === 2) {
    // 二级权限检查一级
    const moduleCode = extractModuleCode(props.permission.permissionCode)
    if (moduleCode && props.userPermissions.includes(moduleCode)) {
      return true
    }
  }
  
  return false
})

// 辅助函数
const getPermissionType = (permissionCode) => {
  if (!permissionCode) return null
  if (permissionCode.includes(':')) return 3
  if (permissionCode.includes('-')) return 2
  return 1
}

const extractSubmoduleCode = (permissionCode) => {
  if (permissionCode && permissionCode.includes(':')) {
    return permissionCode.split(':')[0]
  }
  return null
}

const extractModuleCode = (permissionCode) => {
  if (!permissionCode) return null
  let code = permissionCode
  if (code.includes(':')) {
    code = code.split(':')[0]
  }
  if (code.includes('-')) {
    return code.split('-')[0]
  }
  return code
}
</script>

<style scoped>
.permission-display {
  padding: 8px 0;
}

.permission-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 4px;
}

.permission-name {
  font-weight: 500;
  color: #303133;
  min-width: 120px;
}

.permission-type-tag {
  flex-shrink: 0;
}

.permission-code {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 12px;
  color: #909399;
  background-color: #f5f7fa;
  padding: 2px 6px;
  border-radius: 3px;
  flex-shrink: 0;
}

.permission-path {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 11px;
  color: #b3b3b3;
  margin-left: auto;
  flex-shrink: 0;
}

.inherited-indicator {
  color: #e6a23c;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 2px;
  flex-shrink: 0;
}

.permission-description {
  font-size: 12px;
  color: #666;
  margin-left: 8px;
  margin-top: 2px;
}
</style> 