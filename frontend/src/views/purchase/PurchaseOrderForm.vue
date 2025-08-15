<template>
  <div class="purchase-order-form">
    <el-card
      class="form-card"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <h3>{{ isEdit ? '编辑采购订单' : '新增采购订单' }}</h3>
          <div class="header-actions">
            <el-button @click="handleBack">
              返回列表
            </el-button>
          </div>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        class="purchase-order-form-content"
      >
        <!-- 基本信息 -->
        <el-card
          class="section-card"
          shadow="never"
        >
          <template #header>
            <span class="section-title">基本信息</span>
          </template>
          
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item
                label="订单号"
                prop="orderNo"
              >
                <el-input
                  v-model="form.orderNo"
                  placeholder="系统自动生成"
                  disabled
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="供应商"
                prop="supplierId"
              >
                <el-select
                  v-model="form.supplierId"
                  placeholder="请选择供应商"
                  filterable
                  style="width: 100%"
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
                label="入库仓库"
                prop="warehouseId"
              >
                <el-select
                  v-model="form.warehouseId"
                  placeholder="请选择仓库"
                  style="width: 100%"
                >
                  <el-option
                    label="主仓库"
                    :value="1"
                  />
                  <el-option
                    label="次仓库"
                    :value="2"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item
                label="预计到货日期"
                prop="expectedTime"
              >
                <el-date-picker
                  v-model="form.expectedTime"
                  type="date"
                  placeholder="请选择日期"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
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
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item
                label="备注"
                prop="remark"
              >
                <el-input
                  v-model="form.remark"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入备注信息"
                />
              </el-form-item>
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
              <el-button
                type="primary"
                @click="handleAddItem"
              >
                <el-icon><Plus /></el-icon>
                添加商品
              </el-button>
            </div>
          </template>

          <el-table
            :data="form.items"
            border
            style="width: 100%"
            class="items-table"
          >
            <el-table-column
              type="index"
              label="序号"
              width="60"
              align="center"
            />
            <el-table-column
              label="商品信息"
              min-width="200"
            >
              <template #default="{ row, $index }">
                <div class="goods-info">
                  <div class="goods-main">
                    <span class="goods-name">{{ row.goodsName || '请选择商品' }}</span>
                    <span class="goods-code">{{ row.goodsCode }}</span>
                  </div>
                  <el-button
                    type="primary"
                    link
                    size="small"
                    @click="handleSelectGoods($index)"
                  >
                    选择商品
                  </el-button>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              label="规格"
              width="120"
            >
              <template #default="{ row }">
                {{ row.specification || '-' }}
              </template>
            </el-table-column>
            <el-table-column
              label="单位"
              width="80"
            >
              <template #default="{ row }">
                {{ row.unit || '-' }}
              </template>
            </el-table-column>
            <el-table-column
              label="采购数量"
              width="120"
            >
              <template #default="{ row, $index }">
                <el-input-number
                  v-model="row.quantity"
                  :min="0"
                  :precision="2"
                  controls-position="right"
                  style="width: 100%"
                  @change="handleQuantityChange($index)"
                />
              </template>
            </el-table-column>
            <el-table-column
              label="采购单价"
              width="120"
            >
              <template #default="{ row, $index }">
                <el-input-number
                  v-model="row.unitPrice"
                  :min="0"
                  :precision="2"
                  controls-position="right"
                  style="width: 100%"
                  @change="handlePriceChange($index)"
                />
              </template>
            </el-table-column>
            <el-table-column
              label="小计金额"
              width="120"
              align="right"
            >
              <template #default="{ row }">
                <span class="amount-text">¥{{ formatAmount(row.totalPrice) }}</span>
              </template>
            </el-table-column>
            <el-table-column
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
              align="center"
            >
              <template #default="{ $index }">
                <el-button
                  type="danger"
                  link
                  @click="handleRemoveItem($index)"
                >
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <!-- 合计信息 -->
          <div class="total-info">
            <el-row :gutter="20">
              <el-col :span="18">
                <div class="total-left">
                  <span>共 {{ form.items.length }} 种商品</span>
                  <span>总数量：{{ totalQuantity }}</span>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="total-amount">
                  <span class="total-label">订单总金额：</span>
                  <span class="total-value">¥{{ formatAmount(form.totalAmount) }}</span>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <!-- 操作按钮 -->
        <div class="form-actions">
          <el-button @click="handleBack">
            取消
          </el-button>
          <el-button
            type="info"
            @click="handleSaveDraft"
          >
            保存草稿
          </el-button>
          <el-button
            type="primary"
            @click="handleSaveAndSubmit"
          >
            保存并提交
          </el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 供应商商品选择器 -->
    <SupplierGoodsSelector
      v-model="goodsSelectorVisible"
      :multiple="false"
      :supplier-id="form.supplierId"
      :selected="[]"
      @confirm="handleGoodsSelected"
    />
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'
import { purchaseOrderApi } from '@/api/purchaseOrder'
import { supplierApi } from '@/api/supplier'
import SupplierGoodsSelector from '@/components/GoodsSelector/SupplierGoodsSelector.vue'

const router = useRouter()
const route = useRoute()

// 响应式数据
const formRef = ref(null)
const goodsSelectorVisible = ref(false)
const currentItemIndex = ref(-1)
const supplierOptions = ref([])
const isEdit = computed(() => !!route.params.id)

// 表单数据
const form = reactive({
  id: null,
  orderNo: '',
  supplierId: null,
  warehouseId: null,
  expectedTime: '',
  contact: '',
  mobile: '',
  remark: '',
  totalAmount: 0,
  items: []
})

// 表单验证规则
const rules = {
  supplierId: [
    { required: true, message: '请选择供应商', trigger: 'change' }
  ],
  warehouseId: [
    { required: true, message: '请选择入库仓库', trigger: 'change' }
  ],
  expectedTime: [
    { required: true, message: '请选择预计到货日期', trigger: 'change' }
  ]
}

// 计算属性
const totalQuantity = computed(() => {
  return form.items.reduce((total, item) => {
    return total + (item.quantity || 0)
  }, 0)
})

// 加载供应商选项
const loadSuppliers = async () => {
  try {
    const response = await supplierApi.getAllActiveSuppliers()
    if (response.success) {
      supplierOptions.value = response.data
    }
  } catch (error) {
    console.error('加载供应商失败:', error)
  }
}

// 加载订单数据（编辑模式）
const loadOrderData = async () => {
  if (!route.params.id) return

  try {
    const response = await purchaseOrderApi.getPurchaseOrderById(route.params.id)
    if (response.data.success) {
      const data = response.data.data
      Object.assign(form, {
        id: data.id,
        orderNo: data.orderNo,
        supplierId: data.supplierId,
        warehouseId: data.warehouseId,
        expectedTime: data.expectedTime,
        contact: data.contact,
        mobile: data.mobile,
        remark: data.remark,
        totalAmount: data.totalAmount || 0,
        items: data.items || []
      })
    } else {
      ElMessage.error(response.data.message || '加载订单数据失败')
      handleBack()
    }
  } catch (error) {
    console.error('加载订单数据失败:', error)
    ElMessage.error('加载订单数据失败')
    handleBack()
  }
}

// 供应商变化处理
const handleSupplierChange = (supplierId) => {
  const supplier = supplierOptions.value.find(s => s.id === supplierId)
  if (supplier) {
    form.contact = supplier.contactName || ''
    form.mobile = supplier.contactPhone || ''
  }
}

// 添加明细行
const handleAddItem = () => {
  form.items.push({
    goodsId: null,
    goodsCode: '',
    goodsName: '',
    specification: '',
    unit: '',
    quantity: 1,
    unitPrice: 0,
    totalPrice: 0,
    remark: ''
  })
}

// 删除明细行
const handleRemoveItem = (index) => {
  form.items.splice(index, 1)
  calculateTotalAmount()
}

// 选择商品
const handleSelectGoods = (index) => {
  currentItemIndex.value = index
  goodsSelectorVisible.value = true
}

// 商品选择确认
const handleGoodsSelected = (selectedGoods) => {
  if (selectedGoods.length > 0 && currentItemIndex.value >= 0) {
    const supplierGoods = selectedGoods[0]
    const item = form.items[currentItemIndex.value]
    
    Object.assign(item, {
      goodsId: supplierGoods.goodsId, // 使用商品ID，不是供应商商品ID
      goodsCode: supplierGoods.goodsCode,
      goodsName: supplierGoods.goodsName,
      specification: supplierGoods.specification || '',
      unit: supplierGoods.unit || '',
      unitPrice: supplierGoods.purchasePrice || 0,
      // 保存供应商商品相关信息
      supplierGoodsId: supplierGoods.id,
      supplierGoodsCode: supplierGoods.supplierGoodsCode,
      supplierGoodsName: supplierGoods.supplierGoodsName,
      minOrderQty: supplierGoods.minOrderQty,
      leadTime: supplierGoods.leadTime
    })

    // 如果有最小起订量，设置默认数量
    if (supplierGoods.minOrderQty && supplierGoods.minOrderQty > 0) {
      item.quantity = Math.max(item.quantity || 1, supplierGoods.minOrderQty)
    }

    // 重新计算小计
    handlePriceChange(currentItemIndex.value)
  }
  
  currentItemIndex.value = -1
}

// 数量变化处理
const handleQuantityChange = (index) => {
  const item = form.items[index]
  item.totalPrice = (item.quantity || 0) * (item.unitPrice || 0)
  calculateTotalAmount()
}

// 单价变化处理
const handlePriceChange = (index) => {
  const item = form.items[index]
  item.totalPrice = (item.quantity || 0) * (item.unitPrice || 0)
  calculateTotalAmount()
}

// 计算总金额
const calculateTotalAmount = () => {
  form.totalAmount = form.items.reduce((total, item) => {
    return total + (item.totalPrice || 0)
  }, 0)
}

// 表单验证
const validateForm = async () => {
  try {
    await formRef.value.validate()
    
    if (form.items.length === 0) {
      ElMessage.error('请至少添加一种商品')
      return false
    }

    for (let i = 0; i < form.items.length; i++) {
      const item = form.items[i]
      if (!item.goodsId) {
        ElMessage.error(`第 ${i + 1} 行商品信息不完整`)
        return false
      }
      if (!item.quantity || item.quantity <= 0) {
        ElMessage.error(`第 ${i + 1} 行采购数量必须大于0`)
        return false
      }
      if (!item.unitPrice || item.unitPrice <= 0) {
        ElMessage.error(`第 ${i + 1} 行采购单价必须大于0`)
        return false
      }
    }

    return true
  } catch (error) {
    return false
  }
}

// 保存草稿
const handleSaveDraft = async () => {
  if (!await validateForm()) return

  try {
    if (isEdit.value) {
      await purchaseOrderApi.updatePurchaseOrder(form.id, form)
      ElMessage.success('保存成功')
    } else {
      await purchaseOrderApi.createPurchaseOrder(form)
      ElMessage.success('保存成功')
      handleBack()
    }
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('保存失败')
  }
}

// 保存并提交
const handleSaveAndSubmit = async () => {
  if (!await validateForm()) return

  try {
    let orderId = form.id

    // 先保存
    if (isEdit.value) {
      await purchaseOrderApi.updatePurchaseOrder(form.id, form)
    } else {
      const response = await purchaseOrderApi.createPurchaseOrder(form)
      if (response.data.success) {
        orderId = response.data.data.id
      }
    }

    // 再提交
    if (orderId) {
      await purchaseOrderApi.submitPurchaseOrder(orderId)
      ElMessage.success('保存并提交成功')
      handleBack()
    }
  } catch (error) {
    console.error('保存并提交失败:', error)
    ElMessage.error('保存并提交失败')
  }
}

// 返回列表
const handleBack = () => {
  router.push('/purchase/order')
}

// 格式化金额
const formatAmount = (amount) => {
  return amount ? amount.toFixed(2) : '0.00'
}

// 组件挂载
onMounted(() => {
  loadSuppliers()
  if (isEdit.value) {
    loadOrderData()
  } else {
    // 新增模式下添加一行空明细
    handleAddItem()
  }
})
</script>

<style scoped>
.purchase-order-form {
  padding: 20px;
}

.form-card {
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

.goods-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.goods-main {
  flex: 1;
}

.goods-name {
  display: block;
  font-weight: 500;
  color: #303133;
  margin-bottom: 4px;
}

.goods-code {
  font-size: 12px;
  color: #909399;
}

.items-table {
  margin-bottom: 20px;
}

.total-info {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 6px;
  border: 1px solid #ebeef5;
}

.total-left {
  display: flex;
  gap: 20px;
  align-items: center;
  color: #606266;
}

.total-amount {
  text-align: right;
}

.total-label {
  font-size: 16px;
  color: #303133;
  margin-right: 8px;
}

.total-value {
  font-size: 20px;
  font-weight: 600;
  color: #f56c6c;
}

.amount-text {
  font-weight: 600;
  color: #f56c6c;
}

.form-actions {
  text-align: center;
  padding: 20px 0;
  border-top: 1px solid #ebeef5;
  margin-top: 20px;
}

.form-actions .el-button {
  margin: 0 10px;
  min-width: 100px;
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

:deep(.el-input-number) {
  width: 100%;
}

:deep(.el-input-number .el-input__inner) {
  text-align: left;
}
</style> 