<template>
  <div class="purchase-order-detail">
    <el-card
      v-loading="loading"
      class="detail-card"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <h3>采购订单详情</h3>
          <div class="header-actions">
            <el-button @click="handleBack">
              返回列表
            </el-button>
            <el-button 
              v-if="canEdit" 
              v-hasPermission="'purchase-order-management:update'"
              type="primary"
              @click="handleEdit"
            >
              编辑订单
            </el-button>
          </div>
        </div>
      </template>

      <!-- 订单状态条 -->
      <div
        v-if="orderData.id"
        class="status-bar"
      >
        <el-steps
          :active="getStepActive(orderData.orderStatus)"
          finish-status="success"
        >
          <el-step title="草稿" />
          <el-step title="待审核" />
          <el-step title="已审核" />
          <el-step title="已下单" />
          <el-step title="入库中" />
          <el-step title="已完成" />
        </el-steps>
      </div>

      <!-- 基本信息 -->
      <el-card
        class="section-card"
        shadow="never"
      >
        <template #header>
          <span class="section-title">基本信息</span>
        </template>
        
        <div class="info-grid">
          <div class="info-item">
            <label>订单号：</label>
            <span>{{ orderData.orderNo }}</span>
          </div>
          <div class="info-item">
            <label>供应商：</label>
            <span>{{ orderData.supplierName }}</span>
          </div>
          <div class="info-item">
            <label>供应商编码：</label>
            <span>{{ orderData.supplierCode }}</span>
          </div>
          <div class="info-item">
            <label>入库仓库：</label>
            <span>{{ orderData.warehouseName || '主仓库' }}</span>
          </div>
          <div class="info-item">
            <label>预计到货日期：</label>
            <span :class="{ 'overdue-text': isOverdue }">
              {{ formatDate(orderData.expectedTime) }}
            </span>
          </div>
          <div class="info-item">
            <label>联系人：</label>
            <span>{{ orderData.contact }}</span>
          </div>
          <div class="info-item">
            <label>联系电话：</label>
            <span>{{ orderData.mobile }}</span>
          </div>
          <div class="info-item">
            <label>订单状态：</label>
            <el-tag :type="getStatusTagType(orderData.orderStatus)">
              {{ getStatusText(orderData.orderStatus) }}
            </el-tag>
          </div>
          <div class="info-item">
            <label>支付状态：</label>
            <el-tag :type="getPayStatusTagType(orderData.payStatus)">
              {{ getPayStatusText(orderData.payStatus) }}
            </el-tag>
          </div>
          <div class="info-item">
            <label>入库状态：</label>
            <el-tag :type="getReceiptStatusTagType(orderData.receiptStatus)">
              {{ getReceiptStatusText(orderData.receiptStatus) }}
            </el-tag>
          </div>
          <div class="info-item full-width">
            <label>备注：</label>
            <span>{{ orderData.remark || '无' }}</span>
          </div>
        </div>
      </el-card>

      <!-- 金额信息 -->
      <el-card
        class="section-card"
        shadow="never"
      >
        <template #header>
          <span class="section-title">金额信息</span>
        </template>
        
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="amount-item">
              <div class="amount-label">
                订单总金额
              </div>
              <div class="amount-value total">
                ¥{{ formatAmount(orderData.totalAmount) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="amount-item">
              <div class="amount-label">
                已付金额
              </div>
              <div class="amount-value paid">
                ¥{{ formatAmount(orderData.paidAmount) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="amount-item">
              <div class="amount-label">
                未付金额
              </div>
              <div class="amount-value unpaid">
                ¥{{ formatAmount((orderData.totalAmount || 0) - (orderData.paidAmount || 0)) }}
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="amount-item">
              <div class="amount-label">
                付款进度
              </div>
              <div class="amount-value">
                <el-progress 
                  :percentage="getPaymentProgress()" 
                  :color="getProgressColor()"
                  :stroke-width="8"
                />
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 采购明细 -->
      <el-card
        class="section-card"
        shadow="never"
      >
        <template #header>
          <div class="section-header">
            <span class="section-title">采购明细</span>
            <div class="summary-info">
              <span>共 {{ orderData.itemCount || 0 }} 种商品</span>
              <span>总数量：{{ formatQuantity(orderData.totalQuantity) }}</span>
            </div>
          </div>
        </template>

        <el-table
          :data="orderData.items"
          border
          style="width: 100%"
          class="detail-table"
        >
          <el-table-column
            type="index"
            label="序号"
            width="60"
            align="center"
          />
          <el-table-column
            prop="goodsCode"
            label="商品编码"
            width="120"
          />
          <el-table-column
            prop="goodsName"
            label="商品名称"
            min-width="200"
          />
          <el-table-column
            prop="specification"
            label="规格"
            width="120"
          >
            <template #default="{ row }">
              {{ row.specification || '-' }}
            </template>
          </el-table-column>
          <el-table-column
            prop="unit"
            label="单位"
            width="80"
          />
          <el-table-column
            prop="quantity"
            label="采购数量"
            width="100"
            align="right"
          >
            <template #default="{ row }">
              {{ formatQuantity(row.quantity) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="unitPrice"
            label="采购单价"
            width="100"
            align="right"
          >
            <template #default="{ row }">
              ¥{{ formatAmount(row.unitPrice) }}
            </template>
          </el-table-column>
          <el-table-column
            prop="totalPrice"
            label="小计金额"
            width="120"
            align="right"
          >
            <template #default="{ row }">
              <span class="amount-text">¥{{ formatAmount(row.totalPrice) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="receivedQuantity"
            label="已入库数量"
            width="120"
            align="right"
          >
            <template #default="{ row }">
              <span :class="{ 'success-text': row.isFullyReceived }">
                {{ formatQuantity(row.receivedQuantity) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="pendingQuantity"
            label="未入库数量"
            width="120"
            align="right"
          >
            <template #default="{ row }">
              <span :class="{ 'warning-text': row.pendingQuantity > 0 }">
                {{ formatQuantity(row.pendingQuantity) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            min-width="150"
          >
            <template #default="{ row }">
              {{ row.remark || '-' }}
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 审核信息 -->
      <el-card
        v-if="orderData.auditTime"
        class="section-card"
        shadow="never"
      >
        <template #header>
          <span class="section-title">审核信息</span>
        </template>
        
        <div class="info-grid">
          <div class="info-item">
            <label>审核人：</label>
            <span>{{ orderData.auditName }}</span>
          </div>
          <div class="info-item">
            <label>审核时间：</label>
            <span>{{ formatDateTime(orderData.auditTime) }}</span>
          </div>
          <div class="info-item full-width">
            <label>审核意见：</label>
            <span>{{ orderData.auditRemark || '无' }}</span>
          </div>
        </div>
      </el-card>

      <!-- 操作记录 -->
      <el-card
        class="section-card"
        shadow="never"
      >
        <template #header>
          <span class="section-title">操作记录</span>
        </template>
        
        <div class="info-grid">
          <div class="info-item">
            <label>制单人：</label>
            <span>{{ orderData.creatorName }}</span>
          </div>
          <div class="info-item">
            <label>创建时间：</label>
            <span>{{ formatDateTime(orderData.createTime) }}</span>
          </div>
          <div class="info-item">
            <label>最后修改时间：</label>
            <span>{{ formatDateTime(orderData.updateTime) }}</span>
          </div>
        </div>
      </el-card>

      <!-- 操作按钮 -->
      <div
        v-if="orderData.id"
        class="action-bar"
      >
        <el-button 
          v-if="orderData.orderStatus === 1" 
          v-hasPermission="'purchase-order-management:submit'"
          type="warning"
          @click="handleSubmit"
        >
          提交审核
        </el-button>
        <el-button 
          v-if="orderData.orderStatus === 2" 
          v-hasPermission="'purchase-order-management:audit'"
          type="success"
          @click="handleAudit(true)"
        >
          审核通过
        </el-button>
        <el-button 
          v-if="orderData.orderStatus === 2" 
          v-hasPermission="'purchase-order-management:audit'"
          type="danger"
          @click="handleAudit(false)"
        >
          审核驳回
        </el-button>

        <!-- 编辑支付状态按钮 -->
        <el-button 
          v-if="orderData.orderStatus >= 3"
          v-hasPermission="'purchase-order-management:update'"
          type="warning"
          @click="handleEditPaymentStatus"
        >
          编辑支付状态
        </el-button>

        <el-button 
          v-hasPermission="'purchase-order-management:create'" 
          type="info"
          @click="handleCopy"
        >
          复制订单
        </el-button>
        <el-button 
          v-if="canCancel" 
          v-hasPermission="'purchase-order-management:update'"
          type="warning"
          @click="handleCancel"
        >
          取消订单
        </el-button>
        <el-button 
          v-if="orderData.orderStatus === 1" 
          v-hasPermission="'purchase-order-management:delete'"
          type="danger"
          @click="handleDelete"
        >
          删除订单
        </el-button>
      </div>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="auditVisible"
      :title="auditForm.approved ? '审核通过' : '审核驳回'"
      width="500px"
    >
      <el-form
        :model="auditForm"
        label-width="80px"
      >
        <el-form-item label="审核意见">
          <el-input
            v-model="auditForm.auditRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="auditVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="confirmAudit"
          >确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑支付状态对话框 -->
    <el-dialog
      v-model="paymentStatusVisible"
      title="编辑支付状态"
      width="500px"
    >
      <el-form
        ref="paymentFormRef"
        :model="paymentForm"
        :rules="paymentRules"
        label-width="100px"
      >
        <el-form-item label="支付状态" prop="payStatus">
          <el-select
            v-model="paymentForm.payStatus"
            placeholder="请选择支付状态"
            style="width: 100%"
          >
            <el-option label="未支付" :value="0" />
            <el-option label="部分支付" :value="1" />
            <el-option label="已支付" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item 
          v-if="paymentForm.payStatus === 1 || paymentForm.payStatus === 2"
          label="已付金额" 
          prop="paidAmount"
        >
          <el-input-number
            v-model="paymentForm.paidAmount"
            :min="0"
            :max="orderData.totalAmount"
            :precision="2"
            style="width: 100%"
            placeholder="请输入已付金额"
          />
          <div class="form-hint">
            订单总金额：¥{{ formatAmount(orderData.totalAmount) }}
          </div>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="paymentForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="paymentStatusVisible = false">取消</el-button>
          <el-button
            type="primary"
            :loading="paymentLoading"
            @click="confirmUpdatePaymentStatus"
          >确定</el-button>
        </span>
      </template>
    </el-dialog>




  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import { purchaseOrderApi } from '@/api/purchaseOrder'

const router = useRouter()
const route = useRoute()

// 响应式数据
const loading = ref(false)
const auditVisible = ref(false)
const paymentStatusVisible = ref(false)
const paymentLoading = ref(false)

const orderData = ref({})

// 表单引用
const paymentFormRef = ref()

// 审核表单
const auditForm = reactive({
  approved: true,
  auditRemark: ''
})

// 支付状态表单
const paymentForm = reactive({
  payStatus: 0,
  paidAmount: 0,
  remark: ''
})

// 表单验证规则
const paymentRules = {
  payStatus: [
    { required: true, message: '请选择支付状态', trigger: 'change' }
  ],
  paidAmount: [
    { 
      required: true, 
      message: '请输入已付金额', 
      trigger: 'blur',
      validator: (rule, value, callback) => {
        if (paymentForm.payStatus === 0) {
          callback() // 未支付状态不需要验证金额
          return
        }
        if (value === undefined || value === null || value === '') {
          callback(new Error('请输入已付金额'))
          return
        }
        if (value < 0) {
          callback(new Error('已付金额不能为负数'))
          return
        }
        if (paymentForm.payStatus === 2 && value !== orderData.value.totalAmount) {
          callback(new Error('已支付状态下，已付金额应等于订单总金额'))
          return
        }
        if (paymentForm.payStatus === 1 && value >= orderData.value.totalAmount) {
          callback(new Error('部分支付状态下，已付金额应小于订单总金额'))
          return
        }
        callback()
      }
    }
  ]
}

// 计算属性
const canEdit = computed(() => {
  return orderData.value.orderStatus === 1 || orderData.value.orderStatus === 2
})

const canCancel = computed(() => {
  const status = orderData.value.orderStatus
  return status !== 6 && status !== 7 // 非已完成、已取消
})

const isOverdue = computed(() => {
  if (!orderData.value.expectedTime) return false
  const status = orderData.value.orderStatus
  if (status === 6 || status === 7) return false // 已完成或已取消不算逾期
  return new Date(orderData.value.expectedTime) < new Date()
})

// 加载订单数据
const loadOrderData = async () => {
  if (!route.params.id) {
    ElMessage.error('订单ID不能为空')
    handleBack()
    return
  }

  try {
    loading.value = true
    const response = await purchaseOrderApi.getPurchaseOrderById(route.params.id)
    if (response.code === 200) {
      orderData.value = response.data
    } else {
      ElMessage.error(response.message || '加载订单数据失败')
      handleBack()
    }
  } catch (error) {
    console.error('加载订单数据失败:', error)
    ElMessage.error('加载订单数据失败')
    handleBack()
  } finally {
    loading.value = false
  }
}

// 提交审核
const handleSubmit = async () => {
  try {
    await ElMessageBox.confirm('确定要提交该采购订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await purchaseOrderApi.submitPurchaseOrder(orderData.value.id)
    ElMessage.success('提交成功')
    loadOrderData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
      ElMessage.error('提交失败')
    }
  }
}

// 审核
const handleAudit = (approved) => {
  auditForm.approved = approved
  auditForm.auditRemark = ''
  auditVisible.value = true
}

// 确认审核
const confirmAudit = async () => {
  try {
    await purchaseOrderApi.auditPurchaseOrder(
      orderData.value.id,
      auditForm.approved,
      auditForm.auditRemark
    )
    ElMessage.success('审核成功')
    auditVisible.value = false
    loadOrderData()
  } catch (error) {
    console.error('审核失败:', error)
    ElMessage.error('审核失败')
  }
}

// 编辑支付状态
const handleEditPaymentStatus = () => {
  paymentForm.payStatus = orderData.value.payStatus || 0
  paymentForm.paidAmount = orderData.value.paidAmount || 0
  paymentForm.remark = ''
  paymentStatusVisible.value = true
}

// 确认更新支付状态
const confirmUpdatePaymentStatus = async () => {
  try {
    await paymentFormRef.value.validate()
    
    paymentLoading.value = true
    
    const response = await purchaseOrderApi.updatePaymentStatus(orderData.value.id, {
      payStatus: paymentForm.payStatus,
      paidAmount: paymentForm.paidAmount,
      remark: paymentForm.remark
    })
    
    if (response.code === 200) {
      ElMessage.success('支付状态更新成功')
      paymentStatusVisible.value = false
      loadOrderData()
    } else {
      ElMessage.error(response.message || '更新失败')
    }
  } catch (error) {
    if (error !== false) { // 不是表单验证失败
      console.error('更新支付状态失败:', error)
      ElMessage.error('更新失败')
    }
  } finally {
    paymentLoading.value = false
  }
}

// 复制订单
const handleCopy = async () => {
  try {
    await purchaseOrderApi.copyPurchaseOrder(orderData.value.id)
    ElMessage.success('复制成功')
    router.push('/purchase/order')
  } catch (error) {
    console.error('复制失败:', error)
    ElMessage.error('复制失败')
  }
}

// 取消订单
const handleCancel = async () => {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入取消原因：', '取消订单', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })

    await purchaseOrderApi.cancelPurchaseOrder(orderData.value.id, reason)
    ElMessage.success('取消成功')
    loadOrderData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败:', error)
      ElMessage.error('取消失败')
    }
  }
}

// 删除订单
const handleDelete = async () => {
  try {
    await ElMessageBox.confirm('确定要删除该采购订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await purchaseOrderApi.deletePurchaseOrder(orderData.value.id)
    ElMessage.success('删除成功')
    handleBack()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 编辑订单
const handleEdit = () => {
  router.push(`/purchase/order/edit/${orderData.value.id}`)
}

// 返回列表
const handleBack = () => {
  router.push('/purchase/order')
}





// 工具函数
const formatAmount = (amount) => {
  return amount ? amount.toFixed(2) : '0.00'
}

const formatQuantity = (quantity) => {
  if (!quantity) return '0'
  // 如果是整数，不显示小数点；如果是小数，保留3位小数
  return Number(quantity) % 1 === 0 ? quantity.toString() : Number(quantity).toFixed(3)
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return dateString.split('T')[0]
}

const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return ''
  return dateTimeString.replace('T', ' ').slice(0, 19)
}

const getStatusText = (status) => {
  const statusMap = {
    1: '草稿',
    2: '待审核',
    3: '已审核',
    4: '已下单',
    5: '部分入库',
    6: '已完成',
    7: '已取消'
  }
  return statusMap[status] || '未知'
}

const getStatusTagType = (status) => {
  const typeMap = {
    1: 'info',
    2: 'warning',
    3: 'success',
    4: 'primary',
    5: 'warning',
    6: 'success',
    7: 'danger'
  }
  return typeMap[status] || 'info'
}

const getPayStatusText = (status) => {
  const statusMap = {
    0: '未支付',
    1: '部分支付',
    2: '已支付'
  }
  return statusMap[status] || '未知'
}

const getPayStatusTagType = (status) => {
  const typeMap = {
    0: 'danger',
    1: 'warning',
    2: 'success'
  }
  return typeMap[status] || 'info'
}

const getReceiptStatusText = (status) => {
  const statusMap = {
    0: '未入库',
    1: '部分入库',
    2: '已入库'
  }
  return statusMap[status] || '未知'
}

const getReceiptStatusTagType = (status) => {
  const typeMap = {
    0: 'danger',
    1: 'warning',
    2: 'success'
  }
  return typeMap[status] || 'info'
}

const getStepActive = (status) => {
  const stepMap = {
    1: 0, // 草稿
    2: 1, // 待审核
    3: 2, // 已审核
    4: 3, // 已下单
    5: 4, // 部分入库
    6: 5, // 已完成
    7: -1 // 已取消
  }
  return stepMap[status] !== undefined ? stepMap[status] : -1
}

const getPaymentProgress = () => {
  const total = orderData.value.totalAmount || 0
  const paid = orderData.value.paidAmount || 0
  if (total === 0) return 0
  return Math.round((paid / total) * 100)
}

const getProgressColor = () => {
  const progress = getPaymentProgress()
  if (progress === 100) return '#67c23a'
  if (progress >= 50) return '#e6a23c'
  return '#f56c6c'
}

// 组件挂载
onMounted(() => {
  loadOrderData()
})
</script>

<style scoped>
.purchase-order-detail {
  padding: 20px;
}

.detail-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  margin: 0;
  color: #303133;
}

.status-bar {
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.section-card {
  margin-bottom: 20px;
  border: 1px solid #ebeef5;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-title {
  font-weight: 600;
  color: #303133;
}

.summary-info {
  display: flex;
  gap: 20px;
  color: #606266;
  font-size: 14px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  padding: 20px 0;
}

.info-item {
  display: flex;
  align-items: flex-start;
}

.info-item.full-width {
  grid-column: 1 / -1;
}

.info-item label {
  min-width: 100px;
  font-weight: 500;
  color: #606266;
  margin-right: 10px;
}

.info-item span {
  color: #303133;
  word-break: break-all;
}

.amount-item {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #ebeef5;
}

.amount-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.amount-value {
  font-size: 24px;
  font-weight: 600;
}

.amount-value.total {
  color: #409eff;
}

.amount-value.paid {
  color: #67c23a;
}

.amount-value.unpaid {
  color: #f56c6c;
}

.detail-table {
  margin-bottom: 20px;
}

.amount-text {
  font-weight: 600;
  color: #f56c6c;
}

.success-text {
  color: #67c23a;
  font-weight: 600;
}

.warning-text {
  color: #e6a23c;
  font-weight: 600;
}

.overdue-text {
  color: #f56c6c;
  font-weight: 600;
}

.action-bar {
  text-align: center;
  padding: 20px 0;
  border-top: 1px solid #ebeef5;
  margin-top: 20px;
}

.action-bar .el-button {
  margin: 0 5px;
}

.form-hint {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

:deep(.el-card__header) {
  background: #f8f9fa;
  border-bottom: 1px solid #ebeef5;
}

:deep(.section-card .el-card__header) {
  background: #fafafa;
  padding: 12px 20px;
}

:deep(.el-table) {
  border-radius: 6px;
  overflow: hidden;
}

:deep(.el-table th) {
  background: #f8f9fa !important;
  color: #606266;
  font-weight: 600;
}

:deep(.el-steps) {
  padding: 0;
}

:deep(.el-progress-bar) {
  padding-right: 0;
}

.dialog-footer {
  text-align: right;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style> 