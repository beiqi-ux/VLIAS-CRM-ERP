<template>
  <div class="pur-receipt-detail">
    <el-card
      v-loading="loading"
      class="header-card"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <span class="title">入库单详情</span>
          <div class="actions">
            <el-button @click="handleBack">
              <el-icon><ArrowLeft /></el-icon>
              返回
            </el-button>
            <el-button 
              v-if="detail.receiptStatus === 1" 
              v-hasPermission="'pur-receipt-management:edit'"
              type="primary"
              @click="handleEdit"
            >
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button 
              v-if="detail.receiptStatus === 1" 
              v-hasPermission="'pur-receipt-management:submit'"
              type="warning"
              @click="handleSubmit"
            >
              <el-icon><Upload /></el-icon>
              提交
            </el-button>
            <el-button 
              v-if="detail.receiptStatus === 2" 
              v-hasPermission="'pur-receipt-management:audit'"
              type="success"
              @click="handleAudit(true)"
            >
              <el-icon><Check /></el-icon>
              审核通过
            </el-button>
            <el-button 
              v-if="detail.receiptStatus === 2" 
              v-hasPermission="'pur-receipt-management:audit'"
              type="danger"
              @click="handleAudit(false)"
            >
              <el-icon><Close /></el-icon>
              审核拒绝
            </el-button>
            <el-button 
              v-if="detail.receiptStatus === 3" 
              v-hasPermission="'pur-receipt-management:confirm'"
              type="success"
              @click="handleConfirm"
            >
              <el-icon><Check /></el-icon>
              确认入库
            </el-button>
            <el-button 
              v-hasPermission="'pur-receipt-management:print'" 
              type="info"
              @click="handlePrint"
            >
              <el-icon><Printer /></el-icon>
              打印
            </el-button>
            <el-dropdown @command="handleCommand">
              <el-button type="primary">
                更多<el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item 
                    v-hasPermission="'pur-receipt-management:copy'"
                    command="copy"
                  >
                    复制
                  </el-dropdown-item>
                  <el-dropdown-item 
                    v-hasPermission="'pur-receipt-management:cancel'"
                    command="cancel"
                    :disabled="!canCancel()"
                  >
                    取消
                  </el-dropdown-item>
                  <el-dropdown-item 
                    v-hasPermission="'pur-receipt-management:delete'"
                    command="delete"
                    :disabled="!canDelete()"
                  >
                    删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </template>

      <!-- 基本信息 -->
      <div class="basic-info">
        <el-row :gutter="24">
          <el-col :span="6">
            <div class="info-item">
              <label>入库单号：</label>
              <span>{{ detail.receiptNo }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>采购单号：</label>
              <el-link 
                v-if="detail.orderNo" 
                type="primary" 
                @click="viewPurchaseOrder"
              >
                {{ detail.orderNo }}
              </el-link>
              <span v-else>-</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>入库状态：</label>
              <el-tag :type="getStatusTagType(detail.receiptStatus)">
                {{ detail.receiptStatusName }}
              </el-tag>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>入库类型：</label>
              <el-tag type="info">
                {{ detail.receiptTypeName }}
              </el-tag>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="6">
            <div class="info-item">
              <label>供应商：</label>
              <span>{{ detail.supplierName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>仓库：</label>
              <span>{{ detail.warehouseName }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>入库日期：</label>
              <span>{{ detail.receiptTime }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>入库总金额：</label>
              <span class="amount">¥{{ (detail.totalAmount || 0).toLocaleString() }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="6">
            <div class="info-item">
              <label>联系人：</label>
              <span>{{ detail.contact || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>联系电话：</label>
              <span>{{ detail.mobile || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>入库人：</label>
              <span>{{ detail.receiptUserName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>创建人：</label>
              <span>{{ detail.createByName }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="6">
            <div class="info-item">
              <label>创建时间：</label>
              <span>{{ detail.createTime }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>审核人：</label>
              <span>{{ detail.auditName || '-' }}</span>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="info-item">
              <label>审核时间：</label>
              <span>{{ detail.auditTime || '-' }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row
          v-if="detail.remark"
          :gutter="24"
        >
          <el-col :span="24">
            <div class="info-item">
              <label>备注：</label>
              <span>{{ detail.remark }}</span>
            </div>
          </el-col>
        </el-row>

        <el-row
          v-if="detail.auditRemark"
          :gutter="24"
        >
          <el-col :span="24">
            <div class="info-item">
              <label>审核备注：</label>
              <span>{{ detail.auditRemark }}</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>

    <!-- 入库明细 -->
    <el-card
      class="items-card"
      shadow="never"
    >
      <template #header>
        <span class="title">入库明细</span>
      </template>

      <el-table
        :data="detail.items"
        row-key="id"
        style="width: 100%"
      >
        <el-table-column
          type="index"
          label="序号"
          width="60"
          align="center"
        />
        <el-table-column
          prop="goodsName"
          label="商品名称"
          width="180"
          show-overflow-tooltip
        />
        <el-table-column
          prop="skuName"
          label="规格型号"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="goodsUnit"
          label="单位"
          width="80"
        />
        <el-table-column
          prop="batchNumber"
          label="批次号"
          width="120"
        />
        <el-table-column
          prop="productionDate"
          label="生产日期"
          width="120"
        />
        <el-table-column
          prop="expiryDate"
          label="到期日期"
          width="120"
        />
        <el-table-column
          prop="purchasePrice"
          label="采购价"
          width="120"
          align="right"
        >
          <template #default="{ row }">
            ¥{{ (row.purchasePrice || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="quantity"
          label="入库数量"
          width="100"
          align="center"
        />
        <el-table-column
          prop="totalAmount"
          label="总金额"
          width="120"
          align="right"
        >
          <template #default="{ row }">
            ¥{{ (row.totalAmount || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="locationName"
          label="库位"
          width="120"
        />
        <el-table-column
          prop="remark"
          label="备注"
          min-width="150"
          show-overflow-tooltip
        />
      </el-table>

      <!-- 合计 -->
      <div class="total-row">
        <span>合计数量：{{ totalQuantity }}</span>
        <span>合计金额：<span class="total-amount">¥{{ (detail.totalAmount || 0).toFixed(2) }}</span></span>
      </div>
    </el-card>

    <!-- 操作日志 -->
    <el-card
      v-if="auditHistory.length > 0"
      class="history-card"
      shadow="never"
    >
      <template #header>
        <span class="title">操作历史</span>
      </template>

      <el-timeline>
        <el-timeline-item
          v-for="(item, index) in auditHistory"
          :key="index"
          :timestamp="item.createTime"
        >
          <el-card>
            <h4>{{ item.action }}</h4>
            <p>操作人：{{ item.operatorName }}</p>
            <p v-if="item.remark">
              备注：{{ item.remark }}
            </p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="auditDialogVisible"
      :title="auditForm.approved ? '审核通过' : '审核拒绝'"
      width="500px"
    >
      <el-form
        ref="auditFormRef"
        :model="auditForm"
        :rules="auditRules"
        label-width="80px"
      >
        <el-form-item
          label="审核备注"
          prop="auditRemark"
        >
          <el-input
            v-model="auditForm.auditRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入审核备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="auditDialogVisible = false">
            取消
          </el-button>
          <el-button
            type="primary"
            @click="submitAudit"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 取消对话框 -->
    <el-dialog
      v-model="cancelDialogVisible"
      title="取消入库单"
      width="500px"
    >
      <el-form
        ref="cancelFormRef"
        :model="cancelForm"
        :rules="cancelRules"
        label-width="80px"
      >
        <el-form-item
          label="取消原因"
          prop="reason"
        >
          <el-input
            v-model="cancelForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入取消原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelDialogVisible = false">
            取消
          </el-button>
          <el-button
            type="primary"
            @click="submitCancel"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft,
  Edit,
  Upload,
  Check,
  Close,
  Printer,
  ArrowDown
} from '@element-plus/icons-vue'
import { purReceiptApi } from '@/api/purchase/purReceipt'

const route = useRoute()
const router = useRouter()

// 响应式数据
const loading = ref(false)
const receiptId = ref(null)
const detail = ref({})
const auditHistory = ref([])

// 审核对话框
const auditDialogVisible = ref(false)
const auditForm = reactive({
  approved: true,
  auditRemark: ''
})
const auditRules = {
  auditRemark: [
    { required: true, message: '请输入审核备注', trigger: 'blur' }
  ]
}
const auditFormRef = ref()

// 取消对话框
const cancelDialogVisible = ref(false)
const cancelForm = reactive({
  reason: ''
})
const cancelRules = {
  reason: [
    { required: true, message: '请输入取消原因', trigger: 'blur' }
  ]
}
const cancelFormRef = ref()

// 计算属性
const totalQuantity = computed(() => {
  return (detail.value.items || []).reduce((total, item) => {
    return total + (item.quantity || 0)
  }, 0)
})

// 生命周期
onMounted(() => {
  receiptId.value = route.params.id
  loadData()
  loadAuditHistory()
})

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const response = await purReceiptApi.getPurReceiptById(receiptId.value)
    detail.value = response.data || {}
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadAuditHistory = async () => {
  try {
    const response = await purReceiptApi.getReceiptAuditHistory(receiptId.value)
    auditHistory.value = response.data || []
  } catch (error) {
    console.error('加载操作历史失败:', error)
  }
}

const handleBack = () => {
  router.back()
}

const handleEdit = () => {
  router.push(`/purchase/receipt/edit/${receiptId.value}`)
}

const handleSubmit = async () => {
  try {
    await ElMessageBox.confirm('确认提交该入库单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await purReceiptApi.submitPurReceipt(receiptId.value)
    ElMessage.success('提交成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('提交失败')
    }
  }
}

const handleAudit = (approved) => {
  auditForm.approved = approved
  auditForm.auditRemark = ''
  auditDialogVisible.value = true
  
  nextTick(() => {
    auditFormRef.value?.clearValidate()
  })
}

const submitAudit = async () => {
  try {
    await auditFormRef.value?.validate()
    
    await purReceiptApi.auditPurReceipt(receiptId.value, {
      approved: auditForm.approved,
      auditRemark: auditForm.auditRemark
    })
    
    ElMessage.success('审核成功')
    auditDialogVisible.value = false
    loadData()
    loadAuditHistory()
  } catch (error) {
    console.error('审核失败:', error)
    if (error !== false) {
      ElMessage.error('审核失败')
    }
  }
}

const handleConfirm = async () => {
  try {
    await ElMessageBox.confirm('确认该入库单入库吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await purReceiptApi.confirmReceipt(receiptId.value)
    ElMessage.success('确认入库成功')
    loadData()
    loadAuditHistory()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('确认入库失败')
    }
  }
}

const handlePrint = async () => {
  try {
    const response = await purReceiptApi.generateReceiptPrintData(receiptId.value)
    // TODO: 实现打印功能
    ElMessage.info('打印功能待实现')
  } catch (error) {
    ElMessage.error('获取打印数据失败')
  }
}

const handleCommand = (command) => {
  switch (command) {
  case 'copy':
    handleCopy()
    break
  case 'cancel':
    handleCancel()
    break
  case 'delete':
    handleDelete()
    break
  }
}

const handleCopy = async () => {
  try {
    const response = await purReceiptApi.copyPurReceipt(receiptId.value)
    const newReceipt = response.data
    router.push(`/purchase/receipt/edit/${newReceipt.id}`)
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

const handleCancel = () => {
  cancelForm.reason = ''
  cancelDialogVisible.value = true
  
  nextTick(() => {
    cancelFormRef.value?.clearValidate()
  })
}

const submitCancel = async () => {
  try {
    await cancelFormRef.value?.validate()
    
    await purReceiptApi.cancelPurReceipt(receiptId.value, cancelForm.reason)
    ElMessage.success('取消成功')
    cancelDialogVisible.value = false
    loadData()
    loadAuditHistory()
  } catch (error) {
    console.error('取消失败:', error)
    if (error !== false) {
      ElMessage.error('取消失败')
    }
  }
}

const handleDelete = async () => {
  try {
    await ElMessageBox.confirm('确认删除该入库单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await purReceiptApi.deletePurReceipt(receiptId.value)
    ElMessage.success('删除成功')
    router.push('/purchase/receipt')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const viewPurchaseOrder = () => {
  if (detail.value.orderId) {
    router.push(`/purchase/order/view/${detail.value.orderId}`)
  }
}

// 辅助方法
const getStatusTagType = (status) => {
  const statusMap = {
    1: '', // 草稿
    2: 'warning', // 待审核
    3: 'primary', // 已审核
    4: 'success', // 已入库
    5: 'danger' // 已取消
  }
  return statusMap[status] || ''
}

const canCancel = () => {
  return [1, 2, 3].includes(detail.value.receiptStatus)
}

const canDelete = () => {
  return [1, 5].includes(detail.value.receiptStatus)
}
</script>

<style scoped>
.pur-receipt-detail {
  padding: 20px;
}

.header-card,
.items-card,
.history-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 16px;
  font-weight: bold;
}

.actions {
  display: flex;
  gap: 8px;
}

.basic-info {
  padding: 20px 0;
}

.info-item {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.info-item label {
  font-weight: 500;
  color: #606266;
  width: 120px;
  flex-shrink: 0;
}

.info-item span {
  color: #303133;
}

.amount {
  font-weight: bold;
  color: #f56c6c;
}

.total-row {
  margin-top: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  text-align: right;
  font-size: 16px;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
}

.total-amount {
  color: #f56c6c;
  font-size: 18px;
}

.dialog-footer {
  text-align: right;
}
</style> 
 