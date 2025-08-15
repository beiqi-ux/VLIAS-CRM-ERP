<template>
  <div class="permission-display">
    <!-- 权限标签显示 -->
    <template v-if="displayMode === 'tag'">
      <el-tag
        v-for="permission in formattedPermissions"
        :key="permission.code"
        :type="getTagType(permission.type)"
        :size="size"
        class="permission-tag"
        :closable="closable"
        @close="handleClose(permission.code)"
      >
        <el-icon
          v-if="showIcon"
          class="permission-icon"
        >
          <Lock v-if="permission.type === '操作权限'" />
          <Grid v-else-if="permission.type === '功能模块'" />
          <Menu v-else />
        </el-icon>
        {{ permission.displayName }}
      </el-tag>
    </template>

    <!-- 列表显示 -->
    <template v-else-if="displayMode === 'list'">
      <div class="permission-list">
        <div
          v-for="permission in formattedPermissions"
          :key="permission.code"
          class="permission-item"
        >
          <el-icon class="permission-icon">
            <Lock v-if="permission.type === '操作权限'" />
            <Grid v-else-if="permission.type === '功能模块'" />
            <Menu v-else />
          </el-icon>
          <span class="permission-name">{{ permission.displayName }}</span>
          <el-tag
            :type="getTagType(permission.type)"
            size="small"
            class="permission-type-tag"
          >
            {{ permission.type }}
          </el-tag>
          <span
            v-if="showCode"
            class="permission-code"
          >{{ permission.code }}</span>
        </div>
      </div>
    </template>

    <!-- 树形显示 -->
    <template v-else-if="displayMode === 'tree'">
      <el-tree
        :data="treeData"
        :props="treeProps"
        :default-expand-all="expandAll"
        :show-checkbox="showCheckbox"
        :check-strictly="checkStrictly"
        node-key="code"
        class="permission-tree"
        @check="handleTreeCheck"
      >
        <template #default="{ data }">
          <div class="tree-node">
            <el-icon class="tree-icon">
              <Lock v-if="data.type === '操作权限'" />
              <Grid v-else-if="data.type === '功能模块'" />
              <Menu v-else />
            </el-icon>
            <span class="tree-label">{{ data.displayName }}</span>
            <el-tag
              :type="getTagType(data.type)"
              size="small"
              class="tree-type-tag"
            >
              {{ data.type }}
            </el-tag>
          </div>
        </template>
      </el-tree>
    </template>

    <!-- 简单文本显示 -->
    <template v-else>
      <span class="permission-text">
        {{ formattedPermissions.map(p => p.displayName).join(', ') }}
      </span>
    </template>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Lock, Grid, Menu } from '@element-plus/icons-vue'
import { batchConvertPermissions } from '@/utils/displayMapping'

// Props定义
const props = defineProps({
  // 权限编码，可以是字符串或数组
  permissions: {
    type: [String, Array],
    default: () => []
  },
  // 显示模式：tag, list, tree, text
  displayMode: {
    type: String,
    default: 'tag',
    validator: (value) => ['tag', 'list', 'tree', 'text'].includes(value)
  },
  // 标签大小
  size: {
    type: String,
    default: 'default',
    validator: (value) => ['large', 'default', 'small'].includes(value)
  },
  // 是否可关闭（仅tag模式）
  closable: {
    type: Boolean,
    default: false
  },
  // 是否显示图标
  showIcon: {
    type: Boolean,
    default: true
  },
  // 是否显示编码（仅list模式）
  showCode: {
    type: Boolean,
    default: false
  },
  // 是否展开全部（仅tree模式）
  expandAll: {
    type: Boolean,
    default: false
  },
  // 是否显示复选框（仅tree模式）
  showCheckbox: {
    type: Boolean,
    default: false
  },
  // 是否严格模式（仅tree模式）
  checkStrictly: {
    type: Boolean,
    default: false
  }
})

// 事件定义
const emit = defineEmits(['close', 'tree-check'])

// 格式化权限数据
const formattedPermissions = computed(() => {
  const permissionArray = Array.isArray(props.permissions) 
    ? props.permissions 
    : [props.permissions].filter(Boolean)
  
  return batchConvertPermissions(permissionArray)
})

// 树形数据（仅tree模式使用）
const treeData = computed(() => {
  if (props.displayMode !== 'tree') return []
  
  const permissions = formattedPermissions.value
  const tree = {}
  
  // 构建树形结构
  permissions.forEach(permission => {
    const parts = permission.code.split(/[-:]/)
    let current = tree
    
    parts.forEach((part, index) => {
      if (!current[part]) {
        current[part] = {
          code: parts.slice(0, index + 1).join(index === parts.length - 1 && permission.code.includes(':') ? ':' : '-'),
          displayName: permission.displayName,
          type: permission.type,
          children: {}
        }
      }
      current = current[part].children
    })
  })
  
  // 转换为数组格式
  function convertToArray(obj) {
    return Object.values(obj).map(item => ({
      ...item,
      children: Object.keys(item.children).length > 0 ? convertToArray(item.children) : undefined
    }))
  }
  
  return convertToArray(tree)
})

// 树形组件属性
const treeProps = {
  children: 'children',
  label: 'displayName'
}

// 获取标签类型
function getTagType(permissionType) {
  switch (permissionType) {
  case '系统模块':
    return 'danger'
  case '功能模块':
    return 'warning'
  case '操作权限':
    return 'success'
  default:
    return 'info'
  }
}

// 处理标签关闭
function handleClose(permissionCode) {
  emit('close', permissionCode)
}

// 处理树形选择
function handleTreeCheck(data, checkedInfo) {
  emit('tree-check', data, checkedInfo)
}
</script>

<style scoped>
.permission-display {
  width: 100%;
}

/* 标签模式样式 */
.permission-tag {
  margin: 2px 4px 2px 0;
  display: inline-flex;
  align-items: center;
}

.permission-tag .permission-icon {
  margin-right: 4px;
  font-size: 12px;
}

/* 列表模式样式 */
.permission-list {
  max-height: 300px;
  overflow-y: auto;
}

.permission-item {
  display: flex;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
}

.permission-item:last-child {
  border-bottom: none;
}

.permission-item .permission-icon {
  margin-right: 8px;
  color: #666;
  font-size: 14px;
}

.permission-name {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.permission-type-tag {
  margin: 0 8px;
}

.permission-code {
  font-size: 12px;
  color: #999;
  font-family: 'Courier New', monospace;
}

/* 树形模式样式 */
.permission-tree {
  max-height: 400px;
  overflow-y: auto;
}

.tree-node {
  display: flex;
  align-items: center;
  flex: 1;
  padding-right: 8px;
}

.tree-icon {
  margin-right: 6px;
  color: #666;
  font-size: 14px;
}

.tree-label {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.tree-type-tag {
  margin-left: 8px;
}

/* 文本模式样式 */
.permission-text {
  font-size: 14px;
  color: #333;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .permission-item {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .permission-item .permission-icon {
    margin-bottom: 4px;
  }
  
  .permission-type-tag {
    margin: 4px 0;
  }
}
</style> 