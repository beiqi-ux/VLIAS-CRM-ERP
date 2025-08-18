<template>
  <el-dialog
    v-model="visible"
    title="选择供应商商品"
    width="1200px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    class="supplier-goods-dialog"
    @close="handleClose"
  >
    <div class="supplier-goods-selector">
      <!-- 搜索区域 -->
      <div class="search-form">
        <el-form
          :model="searchForm"
          :inline="true"
          class="search-form-fields"
        >
          <el-form-item label="供应商商品名称">
            <el-input
              v-model="searchForm.supplierGoodsName"
              placeholder="请输入供应商商品名称"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="供应商" v-if="!supplierId">
            <el-select
              v-model="searchForm.supplierId"
              placeholder="请选择供应商"
              clearable
              filterable
              style="width: 180px"
            >
              <el-option
                v-for="supplier in supplierOptions"
                :key="supplier.id"
                :label="supplier.supplierName"
                :value="supplier.id"
              />
            </el-select>
          </el-form-item>
        </el-form>
        <div class="search-buttons">
          <el-button
            type="primary"
            @click="handleSearch"
            icon="Search"
            size="default"
          >
            搜索
          </el-button>
          <el-button 
            @click="handleReset" 
            icon="Refresh"
            size="default"
          >
            重置
          </el-button>
        </div>
      </div>

      <!-- 商品列表 -->
      <el-table
        ref="tableRef"
        :data="goodsList"
        v-loading="loading"
        row-key="id"
        @selection-change="handleSelectionChange"
        height="450"
        class="goods-table"
        stripe
      >
        <el-table-column
          v-if="multiple"
          type="selection"
          width="50"
          :reserve-selection="true"
          align="center"
        />
        <el-table-column
          v-else
          width="50"
          align="center"
        >
          <template #default="scope">
            <el-radio
              v-model="singleSelected"
              :label="scope.row.id"
              @change="handleSingleSelect(scope.row)"
            >
              &nbsp;
            </el-radio>
          </template>
        </el-table-column>
        
        <el-table-column
          prop="supplierName"
          label="供应商"
          width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="supplierGoodsCode"
          label="商品编码"
          width="160"
          show-overflow-tooltip
        />
        <el-table-column
          prop="supplierGoodsName"
          label="商品名称"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          prop="purchasePrice"
          label="采购价格"
          width="120"
          align="right"
        >
          <template #default="scope">
            <span class="price-text">¥{{ (scope.row.purchasePrice || 0).toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="minPurchaseQty"
          label="最小起订量"
          width="120"
          align="right"
        >
          <template #default="scope">
            <span class="qty-text">{{ scope.row.minPurchaseQty || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="deliveryDay"
          label="交货周期(天)"
          width="130"
          align="right"
        >
          <template #default="scope">
            <span class="delivery-text">{{ scope.row.deliveryDay || '-' }}</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :small="false"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose" size="default">取消</el-button>
        <el-button
          type="primary"
          @click="handleConfirm"
          :disabled="selectedGoods.length === 0"
          size="default"
        >
          确定 ({{ selectedGoods.length }})
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { supplierGoodsApi } from '@/api/supplierGoods'
import { supplierApi } from '@/api/supplier'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  multiple: {
    type: Boolean,
    default: true
  },
  supplierId: {
    type: Number,
    default: null
  },
  selected: {
    type: Array,
    default: () => []
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'confirm'])

// 响应式数据
const tableRef = ref(null)
const goodsList = ref([])
const supplierOptions = ref([])
const loading = ref(false)
const selectedGoods = ref([])
const singleSelected = ref(null)

// 搜索表单
const searchForm = reactive({
  supplierGoodsName: '',
  supplierId: props.supplierId || null
})

// 分页
const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

// 计算属性
const visible = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

// 监听选中数据变化
watch(() => props.selected, (newVal) => {
  selectedGoods.value = [...newVal]
  if (!props.multiple && newVal.length > 0) {
    singleSelected.value = newVal[0].id
  }
}, { immediate: true })

// 监听supplierId变化
watch(() => props.supplierId, (newVal) => {
  searchForm.supplierId = newVal
  if (visible.value) {
    handleSearch()
  }
}, { immediate: true })

// 监听对话框显示状态
watch(visible, (newVal) => {
  if (newVal) {
    loadSuppliers()
    handleSearch()
  }
})

// 加载供应商列表
const loadSuppliers = async () => {
  if (props.supplierId) return // 如果已指定供应商，不需要加载列表
  
  try {
    const response = await supplierApi.getAllActiveSuppliers()
    if (response.success) {
      supplierOptions.value = response.data
    }
  } catch (error) {
    console.error('加载供应商列表失败:', error)
  }
}

// 搜索商品
const handleSearch = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page - 1,
      size: pagination.size,
      supplierGoodsName: searchForm.supplierGoodsName || undefined,
      supplierId: searchForm.supplierId || undefined
    }
    
    const response = await supplierGoodsApi.getSupplierGoodsPage(params)
    if (response.success) {
      goodsList.value = response.data.content || []
      pagination.total = response.data.totalElements || 0
      
      // 恢复选中状态
      if (props.multiple && tableRef.value) {
        setTimeout(() => {
          selectedGoods.value.forEach(item => {
            const row = goodsList.value.find(goods => goods.id === item.id)
            if (row) {
              tableRef.value.toggleRowSelection(row, true)
            }
          })
        }, 50)
      }
    } else {
      ElMessage.error(response.message || '查询失败')
    }
  } catch (error) {
    console.error('搜索商品失败:', error)
    ElMessage.error('搜索商品失败')
  } finally {
    loading.value = false
  }
}

// 重置搜索
const handleReset = () => {
  searchForm.supplierGoodsName = ''
  if (!props.supplierId) {
    searchForm.supplierId = null
  }
  pagination.page = 1
  handleSearch()
}

// 多选变化
const handleSelectionChange = (selection) => {
  selectedGoods.value = selection
}

// 单选变化
const handleSingleSelect = (row) => {
  selectedGoods.value = [row]
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  handleSearch()
}

// 页码变化
const handleCurrentChange = (page) => {
  pagination.page = page
  handleSearch()
}

// 确认选择
const handleConfirm = () => {
  if (selectedGoods.value.length === 0) {
    ElMessage.warning('请选择商品')
    return
  }
  
  emit('confirm', selectedGoods.value)
  handleClose()
}

// 关闭对话框
const handleClose = () => {
  visible.value = false
  // 重置数据
  selectedGoods.value = []
  singleSelected.value = null
  searchForm.supplierGoodsName = ''
  if (!props.supplierId) {
    searchForm.supplierId = null
  }
  pagination.page = 1
}

// 初始化
onMounted(() => {
  if (props.supplierId) {
    searchForm.supplierId = props.supplierId
  }
})
</script>

<style scoped>
/* 弹窗整体样式 */
:deep(.supplier-goods-dialog) {
  .el-dialog {
    margin-top: 5vh;
  }
  
  .el-dialog__body {
    padding: 20px;
    max-height: 70vh;
    overflow-y: auto;
  }
}

.supplier-goods-selector {
  padding: 0;
}

/* 搜索表单样式 */
.search-form {
  display: flex;
  align-items: flex-end;
  margin-bottom: 20px;
  padding: 16px 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
  gap: 20px;
}

.search-form-fields {
  flex: 1;
  min-width: 0; /* 防止flex子项收缩问题 */
}

.search-form-fields .el-form-item {
  margin-bottom: 0;
  margin-right: 20px;
}

.search-form-fields .el-form-item:last-child {
  margin-right: 0;
}

/* 按钮区域样式 */
.search-buttons {
  display: flex;
  gap: 10px;
  align-items: center;
  flex-shrink: 0;
  margin-left: auto; /* 自动推到右边 */
  max-width: 300px; /* 限制最大宽度 */
  width: auto; /* 自适应内容宽度 */
}

.search-buttons .el-button {
  min-width: 80px;
  max-width: 120px; /* 限制按钮最大宽度 */
  white-space: nowrap; /* 防止文字换行 */
}

/* 表格样式优化 */
.goods-table {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

:deep(.goods-table) {
  .el-table__header {
    background-color: #f8f9fa;
  }
  
  .el-table__header th {
    background-color: #f8f9fa;
    color: #495057;
    font-weight: 600;
    padding: 12px 0;
  }
  
  .el-table__body td {
    padding: 10px 0;
  }
  
  .el-table__row:hover > td {
    background-color: #f0f8ff;
  }
}

/* 价格和数量文本样式 */
.price-text {
  color: #e74c3c;
  font-weight: 600;
}

.qty-text {
  color: #2c3e50;
  font-weight: 500;
}

.delivery-text {
  color: #27ae60;
  font-weight: 500;
}

/* 分页容器 */
.pagination-container {
  margin-top: 20px;
  padding: 15px 0;
  text-align: right;
  border-top: 1px solid #e9ecef;
  background-color: #fafbfc;
}

/* 底部按钮区域 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 12px 0;
}

.dialog-footer .el-button {
  min-width: 80px;
}

/* 响应式设计 */
@media (max-width: 1400px) {
  :deep(.supplier-goods-dialog) {
    .el-dialog {
      width: 95% !important;
      margin-top: 3vh;
    }
  }
}

@media (max-width: 768px) {
  .search-form {
    flex-direction: column;
    align-items: stretch;
    padding: 12px 15px;
    gap: 15px;
  }
  
  .search-form-fields .el-form-item {
    margin-right: 10px;
    margin-bottom: 10px;
  }
  
  .search-buttons {
    justify-content: center;
    margin-left: 0; /* 移动端不需要自动推到右边 */
    max-width: none; /* 移动端移除宽度限制 */
  }
  
  .goods-table {
    font-size: 14px;
  }
  
  .pagination-container {
    text-align: center;
  }
}
</style> 