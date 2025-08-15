<template>
  <el-dialog
    v-model="visible"
    title="选择商品"
    width="80%"
    @close="handleClose"
  >
    <div class="goods-selector">
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
        <el-form-item label="商品编码">
          <el-input
            v-model="searchForm.goodsCode"
            placeholder="请输入商品编码"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-select
            v-model="searchForm.categoryId"
            placeholder="请选择分类"
            clearable
            filterable
          >
            <el-option
              v-for="category in categoryOptions"
              :key="category.id"
              :label="category.categoryName"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="handleSearch"
          >
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 商品表格 -->
      <el-table
        v-loading="loading"
        :data="goodsList"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        @select="handleSelect"
        @select-all="handleSelectAll"
      >
        <el-table-column
          type="selection"
          width="55"
          :selectable="isSelectable"
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
          prop="categoryName"
          label="分类"
          width="120"
        />
        <el-table-column
          prop="brandName"
          label="品牌"
          width="120"
        />
        <el-table-column
          prop="unit"
          label="单位"
          width="80"
        />
        <el-table-column
          prop="salePrice"
          label="销售价"
          width="100"
        >
          <template #default="{ row }">
            ¥{{ row.salePrice?.toFixed(2) || '0.00' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="stock"
          label="库存"
          width="100"
        >
          <template #default="{ row }">
            <span :class="{ 'low-stock': row.stock < row.minStock }">
              {{ row.stock || 0 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="80"
        >
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>

      <!-- 已选择的商品 -->
      <div
        v-if="selectedGoods.length > 0"
        class="selected-goods"
      >
        <el-divider content-position="left">
          已选择商品 ({{ selectedGoods.length }})
        </el-divider>
        <div class="selected-list">
          <el-tag
            v-for="goods in selectedGoods"
            :key="goods.id"
            closable
            class="selected-tag"
            @close="removeSelectedGoods(goods)"
          >
            {{ goods.goodsName }} ({{ goods.goodsCode }})
          </el-tag>
        </div>
      </div>
    </div>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button
          type="primary"
          :disabled="selectedGoods.length === 0"
          @click="handleConfirm"
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
import { Search, Refresh } from '@element-plus/icons-vue'
import { getGoodsList } from '@/api/goods'
import { getCategoryList } from '@/api/category'

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
  selected: {
    type: Array,
    default: () => []
  },
  excludeIds: {
    type: Array,
    default: () => []
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'confirm'])

// 响应式数据
const loading = ref(false)
const goodsList = ref([])
const categoryOptions = ref([])
const selectedGoods = ref([])

// 计算属性
const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

// 搜索表单
const searchForm = reactive({
  goodsName: '',
  goodsCode: '',
  categoryId: null
})

// 分页信息
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 加载商品数据
const loadGoods = async () => {
  try {
    loading.value = true
    const params = {
      ...searchForm,
      page: pagination.page - 1,
      size: pagination.size,
      status: 1 // 只查询正常状态的商品
    }

    // 清除空值
    Object.keys(params).forEach(key => {
      if (params[key] === '' || params[key] === null || params[key] === undefined) {
        delete params[key]
      }
    })

    const response = await getGoodsList(params)
    if (response.data.success) {
      goodsList.value = response.data.data.content
      pagination.total = response.data.data.totalElements
    } else {
      ElMessage.error(response.data.message || '加载商品数据失败')
    }
  } catch (error) {
    console.error('加载商品数据失败:', error)
    ElMessage.error('加载商品数据失败')
  } finally {
    loading.value = false
  }
}

// 加载分类数据
const loadCategories = async () => {
  try {
    const response = await getCategoryList()
    if (response.data.success) {
      categoryOptions.value = response.data.data
    }
  } catch (error) {
    console.error('加载分类数据失败:', error)
  }
}

// 判断是否可选择
const isSelectable = (row) => {
  return !props.excludeIds.includes(row.id)
}

// 处理选择变化
const handleSelectionChange = (selection) => {
  selectedGoods.value = selection
}

// 处理单个选择
const handleSelect = (selection, row) => {
  if (!props.multiple && selection.length > 1) {
    // 单选模式下，只保留最新选择的
    selectedGoods.value = [row]
  }
}

// 处理全选
const handleSelectAll = (selection) => {
  if (!props.multiple && selection.length > 0) {
    // 单选模式下，全选时只选择第一个
    selectedGoods.value = [selection[0]]
  }
}

// 移除已选择的商品
const removeSelectedGoods = (goods) => {
  const index = selectedGoods.value.findIndex(item => item.id === goods.id)
  if (index > -1) {
    selectedGoods.value.splice(index, 1)
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadGoods()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    goodsName: '',
    goodsCode: '',
    categoryId: null
  })
  pagination.page = 1
  loadGoods()
}

// 分页变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadGoods()
}

const handleCurrentChange = (page) => {
  pagination.page = page
  loadGoods()
}

// 关闭对话框
const handleClose = () => {
  visible.value = false
  selectedGoods.value = []
}

// 确认选择
const handleConfirm = () => {
  if (selectedGoods.value.length === 0) {
    ElMessage.warning('请选择商品')
    return
  }

  emit('confirm', selectedGoods.value)
  visible.value = false
  selectedGoods.value = []
}

// 监听对话框显示状态
watch(visible, (val) => {
  if (val) {
    loadGoods()
    loadCategories()
    
    // 初始化已选择的商品
    if (props.selected && props.selected.length > 0) {
      selectedGoods.value = [...props.selected]
    }
  }
})

// 组件挂载时加载数据
onMounted(() => {
  if (visible.value) {
    loadGoods()
    loadCategories()
  }
})
</script>

<style scoped>
.goods-selector {
  padding: 10px 0;
}

.search-form {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
  margin-bottom: 16px;
}

.pagination-wrapper {
  margin-top: 16px;
  display: flex;
  justify-content: center;
}

.selected-goods {
  margin-top: 16px;
}

.selected-list {
  margin-top: 8px;
}

.selected-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}

.low-stock {
  color: #f56c6c;
  font-weight: bold;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
}

:deep(.el-table__header-wrapper) {
  background: #f8f9fa;
}

:deep(.el-table th) {
  background: #f8f9fa !important;
  color: #606266;
  font-weight: 600;
}

.el-divider {
  margin: 20px 0 10px 0;
}
</style> 