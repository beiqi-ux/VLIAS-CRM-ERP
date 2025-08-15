<template>
  <el-dialog
    v-model="dialogVisible"
    title="供应商详情"
    :width="800"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div
      v-if="supplierData"
      class="supplier-detail"
    >
      <!-- 基本信息 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><Shop /></el-icon>
          基本信息
        </h3>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="detail-item">
              <label>供应商名称：</label>
              <span>{{ supplierData.supplierName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <label>供应商编码：</label>
              <span>{{ supplierData.supplierCode || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="detail-item">
              <label>联系人：</label>
              <span>{{ supplierData.contact || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <label>联系电话：</label>
              <span>{{ supplierData.mobile || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="detail-item">
              <label>邮箱：</label>
              <span>{{ supplierData.email || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <label>状态：</label>
              <el-tag :type="supplierData.status === 1 ? 'success' : 'danger'">
                {{ supplierData.status === 1 ? '正常' : '禁用' }}
              </el-tag>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 地址信息 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><LocationInformation /></el-icon>
          地址信息
        </h3>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="detail-item">
              <label>省份：</label>
              <span>{{ supplierData.province || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>城市：</label>
              <span>{{ supplierData.city || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="detail-item">
              <label>区县：</label>
              <span>{{ supplierData.district || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <div class="detail-item">
              <label>详细地址：</label>
              <span>{{ supplierData.address || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <div class="detail-item">
              <label>完整地址：</label>
              <span>{{ formatFullAddress(supplierData) }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 财务信息 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><Money /></el-icon>
          财务信息
        </h3>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="detail-item">
              <label>开户行：</label>
              <span>{{ supplierData.bankName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <label>银行账号：</label>
              <span>{{ supplierData.bankAccount || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="24">
            <div class="detail-item">
              <label>税号：</label>
              <span>{{ supplierData.taxNo || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 等级信息 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><Star /></el-icon>
          等级信息
        </h3>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="detail-item">
              <label>供应商类型：</label>
              <span>{{ supplierData.supplierTypeText || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <label>供应商等级：</label>
              <span>{{ supplierData.levelText || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="detail-item">
              <label>信用等级：</label>
              <span>{{ supplierData.creditLevelText || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <label>结算方式：</label>
              <span>{{ supplierData.settlementTypeText || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row
          v-if="supplierData.settlementDay"
          :gutter="24"
        >
          <el-col :span="12">
            <div class="detail-item">
              <label>结算天数：</label>
              <span>{{ supplierData.settlementDay }}天</span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 备注信息 -->
      <div
        v-if="supplierData.remark"
        class="detail-section"
      >
        <h3 class="section-title">
          <el-icon><Document /></el-icon>
          备注信息
        </h3>
        <div class="detail-item">
          <div class="remark-content">
            {{ supplierData.remark }}
          </div>
        </div>
      </div>

      <!-- 系统信息 -->
      <div class="detail-section">
        <h3 class="section-title">
          <el-icon><InfoFilled /></el-icon>
          系统信息
        </h3>
        <el-row :gutter="24">
          <el-col :span="12">
            <div class="detail-item">
              <label>创建时间：</label>
              <span>{{ formatDateTime(supplierData.createTime) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <label>创建人：</label>
              <span>{{ supplierData.createByName || '-' }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row
          v-if="supplierData.updateTime"
          :gutter="24"
        >
          <el-col :span="12">
            <div class="detail-item">
              <label>更新时间：</label>
              <span>{{ formatDateTime(supplierData.updateTime) }}</span>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-item">
              <label>更新人：</label>
              <span>{{ supplierData.updateByName || '-' }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">
          关闭
        </el-button>
        <el-button 
          v-if="hasActionPermission('supplier-management:edit')"
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
import { ref, watch } from 'vue'
import { 
  Shop, LocationInformation, Money, Star, Document, InfoFilled 
} from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils/format'
import { hasActionPermission } from '@/utils/permission'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  supplierData: {
    type: Object,
    default: null
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'edit'])

// 响应式数据
const dialogVisible = ref(false)

// 监听对话框显示状态
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
})

watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

// 格式化完整地址
const formatFullAddress = (data) => {
  if (!data) return '-'
  
  const parts = [data.province, data.city, data.district, data.address].filter(Boolean)
  return parts.length > 0 ? parts.join(' ') : '-'
}

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
}

// 编辑供应商
const handleEdit = () => {
  emit('edit', props.supplierData)
  handleClose()
}
</script>

<style scoped>
.supplier-detail {
  max-height: 600px;
  overflow-y: auto;
}

.detail-section {
  margin-bottom: 24px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.detail-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
}

.section-title {
  display: flex;
  align-items: center;
  margin: 0 0 16px 0;
  padding: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.section-title .el-icon {
  margin-right: 8px;
  color: #409eff;
}

.detail-item {
  margin-bottom: 12px;
  display: flex;
  align-items: flex-start;
}

.detail-item label {
  display: inline-block;
  width: 120px;
  color: #606266;
  font-weight: 500;
  flex-shrink: 0;
}

.detail-item span {
  color: #303133;
  word-break: break-all;
}

.remark-content {
  background: #f8f9fa;
  padding: 12px;
  border-radius: 6px;
  border-left: 4px solid #409eff;
  margin-top: 8px;
  white-space: pre-wrap;
  word-break: break-word;
  color: #606266;
  line-height: 1.6;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-tag) {
  font-weight: 500;
}

/* 自定义滚动条 */
.supplier-detail::-webkit-scrollbar {
  width: 6px;
}

.supplier-detail::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.supplier-detail::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.supplier-detail::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style> 