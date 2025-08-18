<template>
  <el-dialog
    v-model="dialogVisible"
    title="仓库详情"
    width="800px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-if="warehouse" class="warehouse-detail">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="仓库ID">
          {{ warehouse.id }}
        </el-descriptions-item>
        <el-descriptions-item label="仓库名称">
          {{ warehouse.warehouseName }}
        </el-descriptions-item>
        <el-descriptions-item label="仓库编码">
          {{ warehouse.warehouseCode }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="warehouse.status === 1 ? 'success' : 'danger'">
            {{ warehouse.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="联系人">
          {{ warehouse.contact || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="联系电话">
          {{ warehouse.mobile || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="省份">
          {{ warehouse.province || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="城市">
          {{ warehouse.city || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="区县">
          {{ warehouse.district || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="详细地址">
          {{ warehouse.address || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="完整地址" :span="2">
          {{ formatAddress(warehouse) }}
        </el-descriptions-item>
        <el-descriptions-item label="是否默认仓库">
          <el-tag v-if="warehouse.isDefault === 1" type="warning">默认</el-tag>
          <span v-else>否</span>
        </el-descriptions-item>
        <el-descriptions-item label="排序">
          {{ warehouse.sort }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDateTime(warehouse.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
          {{ formatDateTime(warehouse.updateTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          {{ warehouse.remark || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, watch } from 'vue'
import { formatDateTime } from '@/utils/format'

// Props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  warehouse: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['update:visible'])

// 响应式数据
const dialogVisible = ref(false)

// 监听对话框显示状态
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal
})

// 监听对话框内部状态变化
watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal)
})

// 方法
const formatAddress = (row) => {
  const parts = [row.province, row.city, row.district, row.address].filter(Boolean)
  return parts.join(' ') || '-'
}

const handleClose = () => {
  dialogVisible.value = false
}
</script>

<style scoped>
.warehouse-detail {
  padding: 10px 0;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-descriptions__body) {
  background-color: #fafafa;
}

:deep(.el-descriptions__label) {
  font-weight: bold;
  background-color: #f5f7fa;
}
</style> 