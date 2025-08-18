<template>
  <el-dialog
    :model-value="visible"
    title="库位详情"
    width="800px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <div v-loading="loading" class="location-detail">
      <el-descriptions
        :column="2"
        border
        size="default"
        class="detail-descriptions"
      >
        <el-descriptions-item label="库位ID">
          {{ locationData?.id || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="库位名称">
          {{ locationData?.locationName || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="库位编码">
          {{ locationData?.locationCode || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="所属仓库">
          {{ warehouseName }}
        </el-descriptions-item>
        <el-descriptions-item label="所属库区">
          {{ areaName }}
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag 
            :type="locationData?.status === 1 ? 'success' : 'danger'"
            effect="light"
          >
            {{ locationData?.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="排序">
          {{ locationData?.sort || 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ locationData?.createTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="更新时间">
          {{ locationData?.updateTime || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="创建人">
          {{ locationData?.createBy || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="更新人">
          {{ locationData?.updateBy || '-' }}
        </el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">
          <div class="remark-content">
            {{ locationData?.remark || '-' }}
          </div>
        </el-descriptions-item>
      </el-descriptions>

      <!-- 统计信息 -->
      <el-card class="stats-card" shadow="never">
        <template #header>
          <div class="stats-header">
            <span>统计信息</span>
            <el-button
              size="small"
              type="primary"
              @click="loadStats"
            >
              <el-icon><RefreshRight /></el-icon>
              刷新
            </el-button>
          </div>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="stat-item">
              <div class="stat-value">{{ stats.stockCount || 0 }}</div>
              <div class="stat-label">库存记录</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-item">
              <div class="stat-value">{{ stats.totalQty || 0 }}</div>
              <div class="stat-label">总库存量</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="stat-item">
              <div class="stat-value">{{ stats.availableQty || 0 }}</div>
              <div class="stat-label">可用库存</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button
          v-if="hasActionPermission('warehouse-location-management:edit')"
          type="primary"
          @click="handleEdit"
        >
          编辑
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { RefreshRight } from '@element-plus/icons-vue'
import { locationApi } from '@/api/location'
import { warehouseApi } from '@/api/warehouse'
import { areaApi } from '@/api/area'
import { hasActionPermission } from '@/utils/permission'

// Props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  locationData: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['close', 'edit'])

// 响应式数据
const loading = ref(false)
const warehouseList = ref([])
const areaList = ref([])
const stats = reactive({
  stockCount: 0,
  totalQty: 0,
  availableQty: 0
})

// 计算属性
const warehouseName = computed(() => {
  if (!props.locationData?.warehouseId) return '-'
  const warehouse = warehouseList.value.find(w => w.id === props.locationData.warehouseId)
  return warehouse ? warehouse.warehouseName : '-'
})

const areaName = computed(() => {
  if (!props.locationData?.areaId) return '-'
  const area = areaList.value.find(a => a.id === props.locationData.areaId)
  return area ? area.areaName : '-'
})

// 监听器
watch(() => props.visible, (newVal) => {
  if (newVal && props.locationData) {
    loadStats()
  }
})

// 方法
const loadWarehouses = async () => {
  try {
    const response = await warehouseApi.getActive()
    if (response.code === 200) {
      warehouseList.value = response.data
    }
  } catch (error) {
    console.error('加载仓库数据失败:', error)
  }
}

const loadAreas = async () => {
  try {
    const response = await areaApi.getActive()
    if (response.code === 200) {
      areaList.value = response.data
    }
  } catch (error) {
    console.error('加载库区数据失败:', error)
  }
}

const loadStats = async () => {
  if (!props.locationData?.id) return
  
  loading.value = true
  try {
    // 这里可以调用库存统计相关的API
    // 暂时使用模拟数据
    Object.assign(stats, {
      stockCount: Math.floor(Math.random() * 100),
      totalQty: Math.floor(Math.random() * 1000),
      availableQty: Math.floor(Math.random() * 800)
    })
  } catch (error) {
    console.error('加载统计信息失败:', error)
    ElMessage.error('加载统计信息失败')
  } finally {
    loading.value = false
  }
}

const handleClose = () => {
  emit('close')
}

const handleEdit = () => {
  emit('edit')
}

// 生命周期
onMounted(async () => {
  await Promise.all([
    loadWarehouses(),
    loadAreas()
  ])
})
</script>

<style scoped>
.location-detail {
  padding: 10px 0;
}

.detail-descriptions {
  margin-bottom: 20px;
}

.remark-content {
  min-height: 20px;
  white-space: pre-wrap;
  word-break: break-all;
}

.stats-card {
  margin-top: 20px;
}

.stats-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-item {
  text-align: center;
  padding: 20px 0;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-descriptions__label) {
  font-weight: 500;
  width: 120px;
}

:deep(.el-descriptions__content) {
  word-break: break-all;
}
</style> 