<template>
  <div class="pur-receipt-form">
    <el-card
      class="form-card"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <span class="title">{{ isEdit ? '编辑入库单' : '新增入库单' }}</span>
          <div class="actions">
            <el-button @click="handleCancel">
              取消
            </el-button>
            <el-button 
              type="primary" 
              :loading="saving"
              @click="handleSave"
            >
              保存
            </el-button>
            <el-button 
              v-if="!isEdit || form.receiptStatus === 1" 
              type="success"
              :loading="saving"
              @click="handleSaveAndSubmit"
            >
              保存并提交
            </el-button>
          </div>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="receipt-form"
      >
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item
              label="入库单号"
              prop="receiptNo"
            >
              <el-input
                v-model="form.receiptNo"
                placeholder="系统自动生成"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="采购单号"
              prop="orderNo"
            >
              <el-input
                v-model="form.orderNo"
                placeholder="请输入或选择采购单号"
                :disabled="!!form.orderId"
              >
                <template #append>
                  <el-button 
                    icon="Search" 
                    :disabled="!!form.orderId"
                    @click="selectPurchaseOrder"
                  />
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="入库类型"
              prop="receiptType"
            >
              <el-select
                v-model="form.receiptType"
                placeholder="请选择入库类型"
                style="width: 100%"
              >
                <el-option
                  v-for="type in receiptTypeOptions"
                  :key="type.value"
                  :label="type.label"
                  :value="type.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item
              label="供应商"
              prop="supplierId"
            >
              <el-select
                v-model="form.supplierId"
                placeholder="请选择供应商"
                style="width: 100%"
                filterable
                :disabled="!!form.orderId"
                @change="handleSupplierChange"
              >
                <el-option
                  v-for="supplier in supplierOptions"
                  :key="supplier.id"
                  :label="supplier.supplierName"
                  :value="supplier.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="仓库"
              prop="warehouseId"
            >
              <el-select
                v-model="form.warehouseId"
                placeholder="请选择仓库"
                style="width: 100%"
              >
                <el-option
                  v-for="warehouse in warehouseOptions"
                  :key="warehouse.id"
                  :label="warehouse.warehouseName"
                  :value="warehouse.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="入库日期"
              prop="receiptTime"
            >
              <el-date-picker
                v-model="form.receiptTime"
                type="date"
                placeholder="请选择入库日期"
                style="width: 100%"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item
              label="联系人"
              prop="contact"
            >
              <el-input
                v-model="form.contact"
                placeholder="请输入联系人"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="联系电话"
              prop="mobile"
            >
              <el-input
                v-model="form.mobile"
                placeholder="请输入联系电话"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="入库状态">
              <el-tag
                :type="getStatusTagType(form.receiptStatus)"
                size="large"
              >
                {{ getStatusName(form.receiptStatus) }}
              </el-tag>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="24">
            <el-form-item
              label="备注"
              prop="remark"
            >
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 入库明细 -->
    <el-card
      class="items-card"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <span class="title">入库明细</span>
          <div class="actions">
            <el-button 
              v-if="!form.orderId" 
              type="primary"
              size="small"
              @click="handleAddItem"
            >
              <el-icon><Plus /></el-icon>
              添加商品
            </el-button>
            <el-button 
              type="danger" 
              size="small"
              :disabled="selectedItems.length === 0"
              @click="handleBatchDeleteItems"
            >
              <el-icon><Delete /></el-icon>
              删除选中
            </el-button>
          </div>
        </div>
      </template>

      <el-table
        :data="form.items"
        row-key="id"
        style="width: 100%"
        @selection-change="handleItemSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
        />
        <el-table-column
          prop="goodsName"
          label="商品名称"
          width="180"
        />
        <el-table-column
          prop="skuName"
          label="规格型号"
          width="120"
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
        >
          <template #default="{ row }">
            <el-input
              v-model="row.batchNumber"
              placeholder="请输入批次号"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="productionDate"
          label="生产日期"
          width="130"
        >
          <template #default="{ row }">
            <el-date-picker
              v-model="row.productionDate"
              type="date"
              placeholder="生产日期"
              size="small"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="expiryDate"
          label="到期日期"
          width="130"
        >
          <template #default="{ row }">
            <el-date-picker
              v-model="row.expiryDate"
              type="date"
              placeholder="到期日期"
              size="small"
              value-format="YYYY-MM-DD"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="purchasePrice"
          label="采购价"
          width="120"
          align="right"
        >
          <template #default="{ row }">
            <el-input-number
              v-model="row.purchasePrice"
              :precision="2"
              :min="0"
              size="small"
              controls-position="right"
              style="width: 100%"
              @change="calculateItemTotal(row)"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="quantity"
          label="入库数量"
          width="120"
          align="center"
        >
          <template #default="{ row }">
            <el-input-number
              v-model="row.quantity"
              :min="0"
              size="small"
              controls-position="right"
              style="width: 100%"
              @change="calculateItemTotal(row)"
            />
          </template>
        </el-table-column>
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
          prop="locationId"
          label="库位"
          width="120"
        >
          <template #default="{ row }">
            <el-select
              v-model="row.locationId"
              placeholder="请选择库位"
              size="small"
              style="width: 100%"
            >
              <el-option
                v-for="location in locationOptions"
                :key="location.id"
                :label="location.locationName"
                :value="location.id"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注"
          width="150"
        >
          <template #default="{ row }">
            <el-input
              v-model="row.remark"
              placeholder="备注"
              size="small"
            />
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="80"
          fixed="right"
        >
          <template #default="{ $index }">
            <el-button
              type="danger"
              link
              size="small"
              @click="handleDeleteItem($index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 合计 -->
      <div class="total-row">
        <span>合计：</span>
        <span class="total-amount">¥{{ totalAmount.toFixed(2) }}</span>
      </div>
    </el-card>

    <!-- 选择采购订单对话框 -->
    <el-dialog
      v-model="orderDialogVisible"
      title="选择采购订单"
      width="80%"
      :close-on-click-modal="false"
    >
      <el-form
        :model="orderSearchForm"
        :inline="true"
        class="search-form"
      >
        <el-form-item label="订单号">
          <el-input
            v-model="orderSearchForm.orderNo"
            placeholder="请输入订单号"
            clearable
            @keyup.enter="searchOrders"
          />
        </el-form-item>
        <el-form-item label="供应商">
          <el-select
            v-model="orderSearchForm.supplierId"
            placeholder="请选择供应商"
            clearable
            filterable
          >
            <el-option
              v-for="supplier in supplierOptions"
              :key="supplier.id"
              :label="supplier.supplierName"
              :value="supplier.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="searchOrders"
          >
            搜索
          </el-button>
          <el-button @click="resetOrderSearch">
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <el-table
        v-loading="orderLoading"
        :data="orderList"
        row-key="id"
        highlight-current-row
        style="width: 100%"
        @current-change="handleOrderSelectionChange"
      >
        <el-table-column
          prop="orderNo"
          label="订单号"
          width="160"
        />
        <el-table-column
          prop="supplierName"
          label="供应商"
          width="180"
        />
        <el-table-column
          prop="orderStatus"
          label="订单状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag>{{ getOrderStatusName(row.orderStatus) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="totalAmount"
          label="订单金额"
          width="120"
          align="right"
        >
          <template #default="{ row }">
            ¥{{ (row.totalAmount || 0).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column
          prop="orderTime"
          label="订单日期"
          width="120"
        />
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="160"
        />
      </el-table>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="orderDialogVisible = false">
            取消
          </el-button>
          <el-button 
            type="primary" 
            :disabled="!selectedOrder"
            @click="confirmSelectOrder"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 选择商品对话框 -->
    <GoodsSelector
      v-model:visible="goodsSelectorVisible"
      :multiple="true"
      @confirm="handleGoodsSelected"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete } from '@element-plus/icons-vue'
import { purReceiptApi } from '@/api/purchase/purReceipt'
import { purchaseOrderApi } from '@/api/purchaseOrder'
import { supplierApi } from '@/api/supplier'
import { warehouseApi } from '@/api/warehouse'
import GoodsSelector from '@/components/GoodsSelector/index.vue'

const route = useRoute()
const router = useRouter()

// 响应式数据
const formRef = ref()
const saving = ref(false)
const isEdit = ref(false)
const receiptId = ref(null)

// 基础数据
const supplierOptions = ref([])
const warehouseOptions = ref([])
const locationOptions = ref([])
const receiptTypeOptions = ref([])

// 表单数据
const form = reactive({
  id: null,
  receiptNo: '',
  orderId: null,
  orderNo: '',
  supplierId: null,
  warehouseId: null,
  receiptStatus: 1,
  receiptType: 1,
  receiptTime: null,
  contact: '',
  mobile: '',
  remark: '',
  items: []
})

// 表单验证规则
const rules = {
  supplierId: [
    { required: true, message: '请选择供应商', trigger: 'change' }
  ],
  warehouseId: [
    { required: true, message: '请选择仓库', trigger: 'change' }
  ],
  receiptType: [
    { required: true, message: '请选择入库类型', trigger: 'change' }
  ],
  receiptTime: [
    { required: true, message: '请选择入库日期', trigger: 'change' }
  ]
}

// 明细选择
const selectedItems = ref([])

// 采购订单选择
const orderDialogVisible = ref(false)
const orderLoading = ref(false)
const orderList = ref([])
const selectedOrder = ref(null)
const orderSearchForm = reactive({
  orderNo: '',
  supplierId: null
})

// 商品选择
const goodsSelectorVisible = ref(false)

// 计算属性
const totalAmount = computed(() => {
  return form.items.reduce((total, item) => {
    return total + (item.totalAmount || 0)
  }, 0)
})

// 生命周期
onMounted(() => {
  receiptId.value = route.params.id
  isEdit.value = !!receiptId.value

  loadBaseData()
  
  if (isEdit.value) {
    loadReceiptData()
  } else {
    initNewReceipt()
  }
})

// 方法
const loadBaseData = async () => {
  try {
    // 加载供应商选项
    const supplierRes = await supplierApi.getList()
    supplierOptions.value = supplierRes.data || []
    
    // 加载仓库选项
    const warehouseRes = await warehouseApi.getList()
    warehouseOptions.value = warehouseRes.data || []
    
    // 加载入库类型选项
    receiptTypeOptions.value = purReceiptApi.getReceiptTypeOptions()
    
    // TODO: 加载库位选项
    locationOptions.value = []
  } catch (error) {
    console.error('加载基础数据失败:', error)
  }
}

const initNewReceipt = () => {
  form.receiptTime = new Date().toISOString().split('T')[0]
}

const loadReceiptData = async () => {
  try {
    const response = await purReceiptApi.getPurReceiptById(receiptId.value)
    const data = response.data
    
    Object.assign(form, data)
    
    // 处理明细数据
    form.items = data.items || []
  } catch (error) {
    console.error('加载入库单数据失败:', error)
    ElMessage.error('加载数据失败')
  }
}

const handleSupplierChange = (supplierId) => {
  // 清空采购订单相关信息
  form.orderId = null
  form.orderNo = ''
  form.items = []
}

const selectPurchaseOrder = () => {
  orderDialogVisible.value = true
  searchOrders()
}

const searchOrders = async () => {
  orderLoading.value = true
  try {
    const params = {
      ...orderSearchForm,
      orderStatus: 3, // 只显示已审核的订单
      page: 0,
      size: 50
    }
    
    if (form.supplierId) {
      params.supplierId = form.supplierId
    }
    
    const response = await purchaseOrderApi.getPurchaseOrderPage(params)
    orderList.value = response.data.content || []
  } catch (error) {
    console.error('搜索采购订单失败:', error)
    ElMessage.error('搜索采购订单失败')
  } finally {
    orderLoading.value = false
  }
}

const resetOrderSearch = () => {
  Object.assign(orderSearchForm, {
    orderNo: '',
    supplierId: null
  })
  searchOrders()
}

const handleOrderSelectionChange = (currentRow) => {
  selectedOrder.value = currentRow
}

const confirmSelectOrder = async () => {
  if (!selectedOrder.value) return
  
  try {
    // 从采购订单创建入库单
    const response = await purReceiptApi.createReceiptFromOrder(
      selectedOrder.value.id,
      {
        warehouseId: form.warehouseId,
        receiptTime: form.receiptTime,
        receiptType: form.receiptType,
        contact: form.contact,
        mobile: form.mobile,
        remark: form.remark
      }
    )
    
    const receiptData = response.data
    Object.assign(form, receiptData)
    
    orderDialogVisible.value = false
    ElMessage.success('导入采购订单成功')
  } catch (error) {
    console.error('导入采购订单失败:', error)
    ElMessage.error('导入采购订单失败')
  }
}

const handleAddItem = () => {
  goodsSelectorVisible.value = true
}

const handleGoodsSelected = (selectedGoods) => {
  selectedGoods.forEach(goods => {
    form.items.push({
      id: null,
      goodsId: goods.id,
      skuId: goods.skuId,
      goodsName: goods.goodsName,
      skuName: goods.skuName,
      goodsSpec: goods.goodsSpec,
      goodsUnit: goods.goodsUnit,
      goodsCode: goods.goodsCode,
      skuCode: goods.skuCode,
      batchNumber: '',
      productionDate: null,
      expiryDate: null,
      purchasePrice: 0,
      quantity: 1,
      totalAmount: 0,
      locationId: null,
      remark: ''
    })
  })
}

const handleDeleteItem = (index) => {
  form.items.splice(index, 1)
}

const handleItemSelectionChange = (selection) => {
  selectedItems.value = selection
}

const handleBatchDeleteItems = () => {
  const selectedIds = selectedItems.value.map(item => item.id)
  form.items = form.items.filter(item => !selectedIds.includes(item.id))
}

const calculateItemTotal = (item) => {
  item.totalAmount = (item.purchasePrice || 0) * (item.quantity || 0)
}

const handleSave = async () => {
  try {
    await formRef.value?.validate()
    
    if (form.items.length === 0) {
      ElMessage.warning('请添加入库明细')
      return
    }
    
    saving.value = true
    
    // 设置总金额
    form.totalAmount = totalAmount.value
    
    let response
    if (isEdit.value) {
      response = await purReceiptApi.updatePurReceipt(receiptId.value, form)
    } else {
      response = await purReceiptApi.createPurReceipt(form)
    }
    
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    
    if (!isEdit.value) {
      // 新增后跳转到编辑页面
      router.replace(`/purchase/receipt/edit/${response.data.id}`)
    }
  } catch (error) {
    console.error('保存失败:', error)
    if (error !== false) {
      ElMessage.error('保存失败')
    }
  } finally {
    saving.value = false
  }
}

const handleSaveAndSubmit = async () => {
  try {
    await handleSave()
    
    if (form.id) {
      await purReceiptApi.submitPurReceipt(form.id)
      ElMessage.success('提交成功')
      form.receiptStatus = 2
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  }
}

const handleCancel = () => {
  router.back()
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

const getStatusName = (status) => {
  const statusMap = {
    1: '草稿',
    2: '待审核',
    3: '已审核',
    4: '已入库',
    5: '已取消'
  }
  return statusMap[status] || '未知'
}

const getOrderStatusName = (status) => {
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
</script>

<style scoped>
.pur-receipt-form {
  padding: 20px;
}

.form-card,
.items-card {
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

.receipt-form .el-form-item {
  margin-bottom: 20px;
}

.total-row {
  margin-top: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  text-align: right;
  font-size: 16px;
  font-weight: bold;
}

.total-amount {
  color: #f56c6c;
  font-size: 18px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.dialog-footer {
  text-align: right;
}
</style> 
 