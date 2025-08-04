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
            type="primary" 
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增SKU
          </el-button>
          <el-button 
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
          prop="price"
          label="价格"
          width="100"
        >
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column
          prop="stock"
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
          width="200"
          fixed="right"
        >
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
            >
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button
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
          prop="price"
        >
          <el-input-number
            v-model="form.price"
            :min="0"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="库存"
          prop="stock"
        >
          <el-input-number
            v-model="form.stock"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="状态"
          prop="status"
        >
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
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const selectedRows = ref([])
const skuList = ref([])
const formRef = ref()

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
  price: 0,
  stock: 0,
  status: 1
})

// 表单验证规则
const formRules = {
  skuName: [
    { required: true, message: '请输入SKU名称', trigger: 'blur' }
  ],
  skuCode: [
    { required: true, message: '请输入SKU编码', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' }
  ],
  stock: [
    { required: true, message: '请输入库存', trigger: 'blur' }
  ]
}

// 获取SKU列表
async function fetchSkuList() {
  try {
    loading.value = true
    // TODO: 调用API获取SKU列表
    // const { data } = await getSkuList({
    //   ...searchForm,
    //   page: pagination.currentPage,
    //   size: pagination.pageSize
    // })
    
    // 模拟数据
    const mockData = {
      content: [
        {
          id: 1,
          skuName: 'iPhone 15 红色 128GB',
          skuCode: 'IP15-RED-128',
          price: 5999.00,
          stock: 100,
          status: 1,
          createTime: '2024-01-01 10:00:00'
        },
        {
          id: 2,
          skuName: 'iPhone 15 蓝色 256GB',
          skuCode: 'IP15-BLUE-256',
          price: 6999.00,
          stock: 50,
          status: 1,
          createTime: '2024-01-01 11:00:00'
        }
      ],
      totalElements: 2
    }
    
    skuList.value = mockData.content
    pagination.total = mockData.totalElements
  } catch (error) {
    console.error('获取SKU列表失败:', error)
    ElMessage.error('获取SKU列表失败')
  } finally {
    loading.value = false
  }
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
  Object.assign(form, row)
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
      // TODO: 调用删除API
      // await deleteSku(row.id)
      ElMessage.success('删除成功')
      fetchSkuList()
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
      // TODO: 调用批量删除API
      // const ids = selectedRows.value.map(row => row.id)
      // await batchDeleteSkus(ids)
      ElMessage.success('批量删除成功')
      fetchSkuList()
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
    
    // TODO: 调用保存API
    if (isEdit.value) {
      // await updateSku(form.id, form)
      ElMessage.success('更新成功')
    } else {
      // await createSku(form)
      ElMessage.success('创建成功')
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
    price: 0,
    stock: 0,
    status: 1
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
</style> 