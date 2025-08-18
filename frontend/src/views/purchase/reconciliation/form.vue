<template>
  <div class="reconciliation-form">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>{{ pageTitle }}</h2>
      <div class="header-actions">
        <el-button @click="handleBack">
          <el-icon><ArrowLeft /></el-icon>
          返回
        </el-button>
      </div>
    </div>

    <!-- 表单区域 -->
    <div class="form-container">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        :disabled="isView"
      >
        <div class="form-section">
          <div class="section-title">基本信息</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="对账单号" prop="reconciliationNo">
                <el-input
                  v-model="formData.reconciliationNo"
                  placeholder="系统自动生成"
                  readonly
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="供应商" prop="supplierId">
                <el-select
                  v-model="formData.supplierId"
                  placeholder="请选择供应商"
                  filterable
                  style="width: 100%"
                  :disabled="isEdit"
                  @change="handleSupplierChange"
                >
                  <el-option
                    v-for="supplier in supplierList"
                    :key="supplier.id"
                    :label="supplier.supplierName"
                    :value="supplier.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="开始日期" prop="startDate">
                <el-date-picker
                  v-model="formData.startDate"
                  type="date"
                  placeholder="请选择开始日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束日期" prop="endDate">
                <el-date-picker
                  v-model="formData.endDate"
                  type="date"
                  placeholder="请选择结束日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="总金额">
                <el-input
                  :value="formatCurrency(summaryInfo.totalAmount)"
                  readonly
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="已付金额">
                <el-input-number
                  v-model="formData.paidAmount"
                  :min="0"
                  :max="formData.totalAmount"
                  :precision="2"
                  controls-position="right"
                  style="width: 100%"
                  @change="handlePaidAmountChange"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="未付金额">
                <el-input
                  :value="formatCurrency(formData.unpaidAmount)"
                  readonly
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="状态">
                <el-tag :type="getStatusTagType(formData.status)">
                  {{ getStatusText(formData.status) }}
                </el-tag>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="创建时间" v-if="isEdit || isView">
                <el-input
                  :value="formData.createTime"
                  readonly
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="备注">
                <el-input
                  v-model="formData.remark"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入备注"
                  maxlength="500"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 对账明细 -->
        <div class="form-section">
          <div class="section-title">
            对账明细
            <div class="section-actions" v-if="!isView">
              <el-button
                size="small"
                type="primary"
                @click="handleLoadOrderItems"
                :loading="loadingItems"
                :disabled="!formData.supplierId || !formData.startDate || !formData.endDate"
              >
                <el-icon><Refresh /></el-icon>
                加载采购数据
              </el-button>
              <el-button
                size="small"
                type="success"
                @click="handleAddItem"
              >
                <el-icon><Plus /></el-icon>
                添加明细
              </el-button>
            </div>
          </div>
          
          <div class="table-container">
            <el-table
              :data="formData.items"
              border
              @selection-change="handleItemSelectionChange"
            >
              <el-table-column
                v-if="!isView"
                type="selection"
                width="50"
              />
              <el-table-column prop="orderNo" label="采购单号" width="150" />
              <el-table-column prop="goodsCode" label="商品编码" width="120" />
              <el-table-column prop="goodsName" label="商品名称" min-width="150" />
              <el-table-column prop="specification" label="规格" width="100" />
              <el-table-column prop="unit" label="单位" width="80" />
              <el-table-column prop="quantity" label="数量" width="100" align="right">
                <template #default="{ row }">
                  <span>{{ formatAmount(row.quantity) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="unitPrice" label="单价" width="100" align="right">
                <template #default="{ row }">
                  <span>{{ formatCurrency(row.unitPrice) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="totalAmount" label="金额" width="120" align="right">
                <template #default="{ row }">
                  <span class="amount-text">{{ formatCurrency(row.totalAmount) }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="orderDate" label="采购日期" width="110" />
              <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
              <el-table-column
                v-if="!isView"
                label="操作"
                width="80"
                align="center"
              >
                <template #default="{ $index }">
                  <el-button
                    size="small"
                    type="danger"
                    @click="handleRemoveItem($index)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>

          <!-- 汇总信息 -->
          <div class="summary-info">
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="summary-item">
                  <span class="summary-label">明细数量：</span>
                  <span class="summary-value">{{ summaryInfo.itemCount }} 项</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="summary-item">
                  <span class="summary-label">总数量：</span>
                  <span class="summary-value">{{ formatAmount(summaryInfo.totalQuantity) }}</span>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="summary-item">
                  <span class="summary-label">总金额：</span>
                  <span class="summary-value amount-text">{{ formatCurrency(summaryInfo.totalAmount) }}</span>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 操作按钮 -->
        <div class="form-actions" v-if="!isView">
          <el-button @click="handleBack">取消</el-button>
          <el-button
            type="primary"
            :loading="submitLoading"
            @click="handleSubmit"
          >
            {{ isEdit ? '更新' : '保存' }}
          </el-button>
          <el-button
            v-if="!isEdit && hasActionPermission('reconciliation-management:auto-generate')"
            type="success"
            :loading="submitLoading"
            @click="handleSaveAndGenerate"
          >
            保存并生成
          </el-button>
        </div>
      </el-form>
    </div>

    <!-- 添加明细对话框 -->
    <el-dialog
      v-model="addItemDialogVisible"
      title="添加对账明细"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="itemFormRef"
        :model="itemForm"
        :rules="itemFormRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="采购单号" prop="orderNo">
              <el-input
                v-model="itemForm.orderNo"
                placeholder="请输入采购单号"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品编码" prop="goodsCode">
              <el-input
                v-model="itemForm.goodsCode"
                placeholder="请输入商品编码"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品名称" prop="goodsName">
              <el-input
                v-model="itemForm.goodsName"
                placeholder="请输入商品名称"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="规格">
              <el-input
                v-model="itemForm.specification"
                placeholder="请输入规格"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="数量" prop="quantity">
              <el-input-number
                v-model="itemForm.quantity"
                :min="0"
                :precision="2"
                controls-position="right"
                style="width: 100%"
                @change="calculateItemTotal"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="单价" prop="unitPrice">
              <el-input-number
                v-model="itemForm.unitPrice"
                :min="0"
                :precision="2"
                controls-position="right"
                style="width: 100%"
                @change="calculateItemTotal"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="金额">
              <el-input
                :value="formatCurrency(itemForm.totalAmount)"
                readonly
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="itemForm.remark"
                type="textarea"
                :rows="2"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="addItemDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAddItemConfirm">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowLeft, Refresh, Plus
} from '@element-plus/icons-vue'
import { hasActionPermission } from '@/utils/permission'
import { reconciliationApi } from '@/api/reconciliation'
import { supplierApi } from '@/api/supplier'
import {
  RECONCILIATION_STATUS,
  getDefaultFormData,
  formatAmount,
  formatCurrency,
  getStatusText,
  getStatusTagType,
  calculateSummary,
  validateReconciliationData
} from '@/utils/reconciliation'

// 路由
const router = useRouter()
const route = useRoute()

// 页面模式
const pageMode = computed(() => {
  const name = route.name
  if (name?.includes('create')) return 'create'
  if (name?.includes('edit')) return 'edit'
  if (name?.includes('view')) return 'view'
  return 'create'
})

const isCreate = computed(() => pageMode.value === 'create')
const isEdit = computed(() => pageMode.value === 'edit')
const isView = computed(() => pageMode.value === 'view')

// 页面标题
const pageTitle = computed(() => {
  const titleMap = {
    create: '新增对账单',
    edit: '编辑对账单',
    view: '对账单详情'
  }
  return titleMap[pageMode.value] || '对账单'
})

// 响应式数据
const formRef = ref()
const itemFormRef = ref()
const submitLoading = ref(false)
const loadingItems = ref(false)
const supplierList = ref([])
const selectedItems = ref([])

// 表单数据
const formData = reactive(getDefaultFormData())

// 表单验证规则
const formRules = {
  supplierId: [
    { required: true, message: '请选择供应商', trigger: 'change' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: '请选择结束日期', trigger: 'change' }
  ]
}

// 添加明细对话框
const addItemDialogVisible = ref(false)
const itemForm = reactive({
  orderNo: '',
  goodsCode: '',
  goodsName: '',
  specification: '',
  unit: '',
  quantity: null,
  unitPrice: null,
  totalAmount: 0,
  orderDate: '',
  remark: ''
})

// 明细表单验证规则
const itemFormRules = {
  orderNo: [
    { required: true, message: '请输入采购单号', trigger: 'blur' }
  ],
  goodsCode: [
    { required: true, message: '请输入商品编码', trigger: 'blur' }
  ],
  goodsName: [
    { required: true, message: '请输入商品名称', trigger: 'blur' }
  ],
  quantity: [
    { required: true, message: '请输入数量', trigger: 'blur' }
  ],
  unitPrice: [
    { required: true, message: '请输入单价', trigger: 'blur' }
  ]
}

// 计算属性
const summaryInfo = computed(() => {
  const summary = calculateSummary(formData.items)
  // 更新表单总金额
  formData.totalAmount = summary.totalAmount
  // 计算未付金额
  formData.unpaidAmount = formData.totalAmount - (formData.paidAmount || 0)
  return summary
})

// 页面加载
onMounted(() => {
  loadSuppliers()
  initPage()
})

// 监听路由参数变化
watch(() => route.params.id, (newId) => {
  if (newId) {
    loadFormData(newId)
  }
}, { immediate: true })

// 初始化页面
const initPage = async () => {
  if (isCreate.value) {
    // 新增模式，生成对账单号
    await generateReconciliationNo()
  } else if (route.params.id) {
    // 编辑/查看模式，加载数据
    await loadFormData(route.params.id)
  }
}

// 加载供应商列表
const loadSuppliers = async () => {
  try {
    const response = await supplierApi.getAllActiveSuppliers()
    if (response.success) {
      supplierList.value = response.data || []
    }
  } catch (error) {
    console.error('加载供应商列表失败:', error)
  }
}

// 生成对账单号
const generateReconciliationNo = async () => {
  try {
    const response = await reconciliationApi.generateReconciliationNo()
    if (response.code === 200) {
      formData.reconciliationNo = response.data
    }
  } catch (error) {
    console.error('生成对账单号失败:', error)
  }
}

// 加载表单数据
const loadFormData = async (id) => {
  try {
    const response = await reconciliationApi.getReconciliationById(id)
    if (response.code === 200) {
      Object.assign(formData, response.data)
    } else {
      ElMessage.error(response.message || '加载数据失败')
      handleBack()
    }
  } catch (error) {
    console.error('加载对账单数据失败:', error)
    ElMessage.error('加载数据失败')
    handleBack()
  }
}

// 供应商变化
const handleSupplierChange = (supplierId) => {
  const supplier = supplierList.value.find(s => s.id === supplierId)
  if (supplier) {
    formData.supplierName = supplier.supplierName
  }
  // 清空明细
  formData.items = []
}

// 已付金额变化
const handlePaidAmountChange = (value) => {
  formData.unpaidAmount = formData.totalAmount - (value || 0)
}

// 加载采购数据
const handleLoadOrderItems = async () => {
  if (!formData.supplierId || !formData.startDate || !formData.endDate) {
    ElMessage.warning('请先选择供应商和日期范围')
    return
  }

  try {
    loadingItems.value = true
    // 这里应该调用获取采购订单明细的API
    // 暂时模拟数据
    ElMessage.info('加载采购数据功能待实现')
  } catch (error) {
    console.error('加载采购数据失败:', error)
    ElMessage.error('加载采购数据失败')
  } finally {
    loadingItems.value = false
  }
}

// 添加明细
const handleAddItem = () => {
  // 重置表单
  Object.assign(itemForm, {
    orderNo: '',
    goodsCode: '',
    goodsName: '',
    specification: '',
    unit: '',
    quantity: null,
    unitPrice: null,
    totalAmount: 0,
    orderDate: '',
    remark: ''
  })
  addItemDialogVisible.value = true
  
  nextTick(() => {
    itemFormRef.value?.clearValidate()
  })
}

// 计算明细金额
const calculateItemTotal = () => {
  const quantity = itemForm.quantity || 0
  const unitPrice = itemForm.unitPrice || 0
  itemForm.totalAmount = quantity * unitPrice
}

// 确认添加明细
const handleAddItemConfirm = async () => {
  try {
    const valid = await itemFormRef.value.validate()
    if (!valid) return

    // 添加到明细列表
    formData.items.push({ ...itemForm })
    addItemDialogVisible.value = false
    ElMessage.success('添加成功')
  } catch (error) {
    console.error('添加明细失败:', error)
  }
}

// 删除明细
const handleRemoveItem = (index) => {
  formData.items.splice(index, 1)
  ElMessage.success('删除成功')
}

// 明细选择变化
const handleItemSelectionChange = (selection) => {
  selectedItems.value = selection
}

// 返回
const handleBack = () => {
  router.push({ name: 'purchase-reconciliation' })
}

// 提交表单
const handleSubmit = async () => {
  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    // 数据验证
    const errors = validateReconciliationData(formData)
    if (errors.length > 0) {
      ElMessage.error(errors[0])
      return
    }

    submitLoading.value = true

    // 准备提交数据
    const submitData = { ...formData }
    
    let response
    if (isEdit.value) {
      response = await reconciliationApi.updateReconciliation(formData.id, submitData)
    } else {
      response = await reconciliationApi.createReconciliation(submitData)
    }

    if (response.code === 200) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      handleBack()
    } else {
      ElMessage.error(response.message || (isEdit.value ? '更新失败' : '创建失败'))
    }
  } catch (error) {
    console.error('提交对账单失败:', error)
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  } finally {
    submitLoading.value = false
  }
}

// 保存并生成
const handleSaveAndGenerate = async () => {
  // 先保存
  await handleSubmit()
  // 如果保存成功，可以进行其他操作
}
</script>

<style scoped>
.reconciliation-form {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #ebeef5;
}

.page-header h2 {
  margin: 0;
  color: #303133;
  font-size: 20px;
  font-weight: 500;
}

.form-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.form-section {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

.form-section:last-child {
  border-bottom: none;
}

.section-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.section-actions {
  display: flex;
  gap: 8px;
}

.table-container {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  overflow: hidden;
}

.summary-info {
  margin-top: 16px;
  padding: 16px;
  background-color: #fafafa;
  border-radius: 4px;
}

.summary-item {
  text-align: center;
}

.summary-label {
  color: #606266;
  font-size: 14px;
}

.summary-value {
  color: #303133;
  font-weight: 500;
  font-size: 16px;
}

.amount-text {
  font-weight: 500;
  color: #409eff;
}

.form-actions {
  padding: 20px;
  text-align: right;
  background-color: #fafafa;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-table) {
  border-radius: 0;
}

:deep(.el-table__header) {
  background-color: #fafafa;
}

:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-textarea__inner) {
  resize: vertical;
}
</style> 