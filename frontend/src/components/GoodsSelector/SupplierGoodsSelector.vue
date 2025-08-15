<template>
  <el-dialog
    v-model="visible"
    title="选择供应商商品"
    width="80%"
    @close="handleClose"
  >
    <div class="supplier-goods-selector">
      <!-- 搜索区域 -->
      <el-form
        :model="searchForm"
        :inline="true"
        class="search-form"
      >
        <el-form-item label="商品名称">
          <el-input
            v-model="searchForm.goodsName"
            placeholder="请输入商品名称"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="供应商" v-if="!supplierId">
          <el-select
            v-model="searchForm.supplierId"
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
            @click="handleSearch"
          >
            搜索
          </el-button>
          <el-button @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 商品列表 -->
      <el-table
        ref="tableRef"
        :data="goodsList"
        v-loading="loading"
        row-key="id"
        @selection-change="handleSelectionChange"
        height="400"
      >
        <el-table-column
          v-if="multiple"
          type="selection"
          width="55"
          :reserve-selection="true"
        />
        <el-table-column
          v-else
          width="55"
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
          prop="goodsName"
          label="商品名称"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="goodsCode"
          label="商品编码"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="supplierName"
          label="供应商"
          width="120"
          show-overflow-tooltip
        />
        <el-table-column
          prop="supplierGoodsCode"
          label="供应商商品编码"
          width="140"
          show-overflow-tooltip
        />
        <el-table-column
          prop="supplierGoodsName"
          label="供应商商品名称"
          width="140"
          show-overflow-tooltip
        />
        <el-table-column
          prop="purchasePrice"
          label="采购价格"
          width="100"
          align="right"
        >
          <template #default="scope">
            ¥{{ (scope.row.purchasePrice || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="minOrderQty"
          label="最小起订量"
          width="100"
          align="right"
        >
          <template #default="scope">
            {{ scope.row.minOrderQty || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="unit"
          label="单位"
          width="80"
        />
        <el-table-column
          prop="leadTime"
          label="交货周期(天)"
          width="120"
          align="right"
        >
          <template #default="scope">
            {{ scope.row.leadTime || '-' }}
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
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button
          type="primary"
          @click="handleConfirm"
          :disabled="selectedGoods.length === 0"
        >
          确定 ({{ selectedGoods.length }})
        </el-button>
      </span>
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
  goodsName: '',
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
    if (response.code === 200) {
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
      goodsName: searchForm.goodsName || undefined,
      supplierId: searchForm.supplierId || undefined
    }
    
    const response = await supplierGoodsApi.getSupplierGoodsPage(params)
    if (response.code === 200) {
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
  searchForm.goodsName = ''
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
  searchForm.goodsName = ''
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
.supplier-goods-selector {
  padding: 0;
}

.search-form {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 