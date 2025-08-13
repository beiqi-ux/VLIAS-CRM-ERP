<template>
  <div class="sku-list">
    <!-- 搜索栏 -->
    <el-card
      class="search-card"
      shadow="never"
    >
      <el-form
        :model="searchForm"
        inline
      >
        <el-form-item label="SKU名称">
          <el-input 
            v-model="searchForm.skuName" 
            placeholder="请输入SKU名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="SKU编码">
          <el-input 
            v-model="searchForm.skuCode" 
            placeholder="请输入SKU编码" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option
              label="启用"
              :value="1"
            />
            <el-option
              label="禁用"
              :value="0"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            v-if="hasPermission(PERMISSIONS.GOODS.SKU.VIEW)"
            type="primary"
            :loading="loading"
            @click="handleSearch"
          >
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 -->
    <el-card
      class="operation-card"
      shadow="never"
    >
      <div class="operation-row">
        <div class="left-operations">
          <el-button 
            v-if="hasPermission(PERMISSIONS.GOODS.SKU.CREATE)"
            type="primary" 
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增SKU
          </el-button>
          <el-button 
            v-if="hasPermission(PERMISSIONS.GOODS.SKU.DELETE)"
            type="danger" 
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
        <div class="right-operations">
          <el-button
            type="success"
            @click="handleRefresh"
          >
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card
      class="table-card"
      shadow="never"
    >
      <el-table
        v-loading="loading"
        :data="skuList"
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
        height="600"
      >
        <el-table-column
          type="selection"
          width="55"
        />
        <el-table-column
          prop="id"
          label="ID"
          width="80"
        />
        <el-table-column
          prop="skuName"
          label="SKU名称"
          min-width="150"
        />
        <el-table-column
          prop="skuCode"
          label="SKU编码"
          min-width="120"
        />
        <el-table-column
          prop="sellingPrice"
          label="价格"
          width="100"
        >
          <template #default="{ row }">
            ¥{{ row.sellingPrice }}
          </template>
        </el-table-column>
        <el-table-column
          prop="stockQty"
          label="库存"
          width="100"
        />
        <el-table-column
          prop="status"
          label="状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag
              :type="row.status === 1 ? 'success' : 'danger'"
              size="small"
            >
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="180"
        />
        <el-table-column
          label="操作"
          width="260"
          fixed="right"
        >
          <template #default="{ row }">
            <el-button
              v-if="hasPermission(PERMISSIONS.GOODS.SKU.EDIT)"
              type="primary"
              size="small"
              @click="handleEdit(row)"
            >
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button
              v-if="hasPermission(PERMISSIONS.GOODS.SKU.DELETE)"
              type="danger"
              size="small"
              @click="handleDelete(row)"
            >
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.currentPage"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑SKU' : '新增SKU'"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item
          label="SKU名称"
          prop="skuName"
        >
          <el-input
            v-model="form.skuName"
            placeholder="请输入SKU名称"
          />
        </el-form-item>
        <el-form-item
          label="关联商品"
          prop="goodsId"
        >
          <el-select
            v-model="form.goodsId"
            placeholder="请选择商品"
            clearable
            style="width: 100%"
            filterable
            @change="handleGoodsChange"
          >
            <el-option
              v-for="goods in goodsList"
              :key="goods.id"
              :label="goods.goodsName"
              :value="goods.id"
            />
          </el-select>
        </el-form-item>
        
        <!-- 规格属性选择区域 -->
        <el-form-item 
          v-if="availableSpecs.length > 0"
          label="规格属性"
        >
          <div class="spec-attrs-container">
            <div 
              v-for="spec in availableSpecs" 
              :key="spec.id"
              class="spec-group"
            >
              <label class="spec-label">{{ spec.specificationName }}:</label>
              <el-select
                v-model="selectedSpecAttrs[spec.id]"
                placeholder="请选择"
                clearable
                style="width: 200px; margin-left: 10px;"
              >
                <el-option
                  v-for="value in spec.specificationValues"
                  :key="value.id"
                  :label="getSpecValueLabel(value)"
                  :value="value.id"
                />
              </el-select>
            </div>
          </div>
        </el-form-item>
        
        <el-form-item
          label="SKU编码"
          prop="skuCode"
        >
          <el-input
            v-model="form.skuCode"
            placeholder="请输入SKU编码"
          />
        </el-form-item>
        <el-form-item
          label="价格"
          prop="sellingPrice"
        >
          <el-input-number
            v-model="form.sellingPrice"
            :min="0"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="库存"
          prop="stockQty"
        >
          <el-input-number
            v-model="form.stockQty"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button
            type="primary"
            :loading="submitLoading"
            @click="handleSubmit"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getSkuList,
  createSku,
  updateSku,
  deleteSku,
  batchDeleteSkus
} from '@/api/sku'
import { getGoodsList } from '@/api/goods'
import { getOptimalSpecifications } from '@/api/specification'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { hasPermission, PERMISSIONS } from '@/utils/permission'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const selectedRows = ref([])
const skuList = ref([])
const formRef = ref()
const goodsList = ref([]) // 商品列表
const availableSpecs = ref([]) // 可用规格列表
const selectedSpecAttrs = ref({}) // 选中的规格属性值

// 搜索表单
const searchForm = reactive({
  skuName: '',
  skuCode: '',
  status: null
})

// 分页数据
const pagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 表单数据
const form = reactive({
  id: null,
  skuName: '',
  skuCode: '',
  sellingPrice: 0,
  stockQty: 0,
  status: 1,
  goodsId: null, // 新增：关联商品ID
  selectedSpecAttrs: {} // 新增：已选择的规格属性值ID
})

// 表单验证规则
const formRules = {
  skuName: [
    { required: true, message: '请输入SKU名称', trigger: 'blur' }
  ],
  skuCode: [
    { required: true, message: '请输入SKU编码', trigger: 'blur' }
  ],
  sellingPrice: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ],
  stockQty: [
    { required: true, message: '请输入库存', trigger: 'blur' }
  ]
}

// 获取SKU列表
async function fetchSkuList() {
  try {
    loading.value = true
    const data = await getSkuList({
      ...searchForm,
      pageNum: pagination.currentPage,
      pageSize: pagination.pageSize
    })
    
    if (data.success) {
      skuList.value = data.data.content || []
      pagination.total = data.data.totalElements || 0
    } else {
      ElMessage.error(data.message || '获取SKU列表失败')
    }
  } catch (error) {
    console.error('获取SKU列表失败:', error)
    ElMessage.error('获取SKU列表失败')
  } finally {
    loading.value = false
  }
}

// 获取商品列表
async function fetchGoodsList() {
  try {
    const { data } = await getGoodsList({ pageNum: 1, pageSize: 1000 })
    goodsList.value = data.content || []
  } catch (error) {
    console.error('获取商品列表失败:', error)
    ElMessage.error('获取商品列表失败')
  }
}

// 处理商品选择变化
async function handleGoodsChange(goodsId) {
  if (!goodsId) {
    availableSpecs.value = []
    selectedSpecAttrs.value = {}
    return
  }
  
  try {
    // 获取选中商品的分类ID
    const selectedGoods = goodsList.value.find(g => g.id === goodsId)
    if (!selectedGoods || !selectedGoods.categoryId) {
      ElMessage.warning('该商品没有设置分类，无法获取规格数据')
      return
    }
    
    // 获取最精准的规格数据：优先商品专属规格，其次分类通用规格
    const { data: specifications } = await getOptimalSpecifications(goodsId, selectedGoods.categoryId)
    
    availableSpecs.value = specifications || []
    selectedSpecAttrs.value = {} // 重置选中的规格属性
    
    // 显示获取到的规格信息
    const goodsSpecificSpecs = specifications.filter(spec => spec.goodsId === goodsId)
    const categorySpecs = specifications.filter(spec => !spec.goodsId)
    
    if (goodsSpecificSpecs.length > 0) {
      ElMessage.success(`找到 ${goodsSpecificSpecs.length} 个该商品专属规格，${categorySpecs.length} 个分类通用规格`)
    } else if (categorySpecs.length > 0) {
      ElMessage.info(`使用 ${categorySpecs.length} 个分类通用规格`)
    } else {
      ElMessage.warning('该商品和分类都没有可用规格')
    }
    
  } catch (error) {
    console.error('获取规格数据失败:', error)
    ElMessage.error('获取规格数据失败')
  }
}

// 获取规格值标签显示
function getSpecValueLabel(value) {
  if (value.colorName) return value.colorName
  if (value.frameWidth) return `${value.frameWidth}mm`
  if (value.shapeName) return value.shapeName
  if (value.materialName) return value.materialName
  return value.valueCode || '未知'
}

// 搜索
function handleSearch() {
  pagination.currentPage = 1
  fetchSkuList()
}

// 重置搜索
function resetSearch() {
  Object.assign(searchForm, {
    skuName: '',
    skuCode: '',
    status: null
  })
  pagination.currentPage = 1
  fetchSkuList()
}

// 刷新
function handleRefresh() {
  fetchSkuList()
}

// 新增
function handleAdd() {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

// 编辑
function handleEdit(row) {
  isEdit.value = true
  // 只复制需要编辑的业务字段，避免包含时间戳等系统字段
  Object.assign(form, {
    id: row.id,
    skuName: row.skuName,
    skuCode: row.skuCode,
    sellingPrice: row.sellingPrice,
    stockQty: row.stockQty,
    status: row.status,
    goodsId: row.goodsId, // 复制关联商品ID
    selectedSpecAttrs: row.selectedSpecAttrs || {} // 复制已选择的规格属性值
  })
  dialogVisible.value = true
}

// 删除
function handleDelete(row) {
  ElMessageBox.confirm(
    `确定要删除SKU "${row.skuName}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const data = await deleteSku(row.id)
      if (data.success) {
      ElMessage.success('删除成功')
      fetchSkuList()
      } else {
        ElMessage.error(data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 批量删除
function handleBatchDelete() {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 条数据吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      const data = await batchDeleteSkus(ids)
      if (data.success) {
      ElMessage.success('批量删除成功')
      fetchSkuList()
      } else {
        ElMessage.error(data.message || '批量删除失败')
      }
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}



// 表格选择变化
function handleSelectionChange(selection) {
  selectedRows.value = selection
}

// 分页大小变化
function handleSizeChange(size) {
  pagination.pageSize = size
  pagination.currentPage = 1
  fetchSkuList()
}

// 当前页变化
function handleCurrentChange(page) {
  pagination.currentPage = page
  fetchSkuList()
}

// 提交表单
async function handleSubmit() {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    let result
    if (isEdit.value) {
      result = await updateSku(form.id, form)
    } else {
      result = await createSku(form)
    }
    
    if (result.success) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    } else {
      ElMessage.error(result.message || '操作失败')
      return
    }
    
    dialogVisible.value = false
    fetchSkuList()
  } catch (error) {
    console.error('提交失败:', error)
    if (error !== false) { // 表单验证失败时不显示错误消息
      ElMessage.error('提交失败')
    }
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
function resetForm() {
  Object.assign(form, {
    id: null,
    skuName: '',
    skuCode: '',
    sellingPrice: 0,
    stockQty: 0,
    status: 1,
    goodsId: null, // 重置关联商品ID
    selectedSpecAttrs: {} // 重置已选择的规格属性值
  })
  formRef.value?.clearValidate()
}

// 对话框关闭
function handleDialogClose() {
  resetForm()
}

// 组件挂载时获取数据
onMounted(() => {
  fetchSkuList()
  fetchGoodsList()
})
</script>

<style scoped>
.sku-list {
  padding: 20px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.operation-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.left-operations {
  display: flex;
  gap: 10px;
}

.right-operations {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  text-align: right;
}

/* 表格滚动条样式 */
:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-corner {
  background: #f1f1f1;
}

.spec-attrs-container {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  background-color: #fafafa;
}

.spec-group {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.spec-group:last-child {
  margin-bottom: 0;
}

.spec-label {
  font-weight: bold;
  color: #606266;
  min-width: 100px;
  flex-shrink: 0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.mb-3 {
  margin-bottom: 15px;
}
</style> 