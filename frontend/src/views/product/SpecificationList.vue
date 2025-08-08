<template>
  <div class="specification-list">
    <!-- 搜索栏 -->
    <el-card
      class="search-card"
      shadow="never"
    >
      <el-form
        :model="searchForm"
        inline
      >
        <el-form-item label="规格名称">
          <el-input 
            v-model="searchForm.specificationName" 
            placeholder="请输入规格名称" 
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
            新增规格
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
        :data="specificationList"
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
          prop="specificationName"
          label="规格名称"
          min-width="150"
        />
        <el-table-column
          prop="specificationCode"
          label="规格编码"
          min-width="120"
        />
        <el-table-column
          prop="description"
          label="描述"
          min-width="200"
          show-overflow-tooltip
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
      :title="isEdit ? '编辑规格' : '新增规格'"
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
          label="规格名称"
          prop="specificationName"
        >
          <el-input
            v-model="form.specificationName"
            placeholder="请输入规格名称"
          />
        </el-form-item>
        <el-form-item
          label="规格编码"
          prop="specificationCode"
        >
          <el-input
            v-model="form.specificationCode"
            placeholder="请输入规格编码"
          />
        </el-form-item>
        <el-form-item
          label="描述"
          prop="description"
        >
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
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
import { 
  getSpecificationList, 
  createSpecification, 
  updateSpecification, 
  deleteSpecification, 
  batchDeleteSpecifications 
} from '@/api/specification'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const selectedRows = ref([])
const specificationList = ref([])
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  specificationName: '',
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
  specificationName: '',
  specificationCode: '',
  description: '',
  status: 1
})

// 表单验证规则
const formRules = {
  specificationName: [
    { required: true, message: '请输入规格名称', trigger: 'blur' }
  ],
  specificationCode: [
    { required: true, message: '请输入规格编码', trigger: 'blur' }
  ]
}

// 获取规格列表
async function fetchSpecificationList() {
  try {
    loading.value = true
    const { data } = await getSpecificationList({
      ...searchForm,
      page: pagination.currentPage - 1, // 后端分页从0开始
      size: pagination.pageSize
    })
    
    specificationList.value = data.content
    pagination.total = data.totalElements
  } catch (error) {
    console.error('获取规格列表失败:', error)
    ElMessage.error('获取规格列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  pagination.currentPage = 1
  fetchSpecificationList()
}

// 重置搜索
function resetSearch() {
  Object.assign(searchForm, {
    specificationName: '',
    status: null
  })
  pagination.currentPage = 1
  fetchSpecificationList()
}

// 刷新
function handleRefresh() {
  fetchSpecificationList()
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
    `确定要删除规格 "${row.specificationName}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteSpecification(row.id)
      ElMessage.success('删除成功')
      fetchSpecificationList()
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
      await batchDeleteSpecifications(ids)
      ElMessage.success('批量删除成功')
      fetchSpecificationList()
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
  fetchSpecificationList()
}

// 当前页变化
function handleCurrentChange(page) {
  pagination.currentPage = page
  fetchSpecificationList()
}

// 提交表单
async function handleSubmit() {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      await updateSpecification(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createSpecification(form)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchSpecificationList()
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
    specificationName: '',
    specificationCode: '',
    description: '',
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
  fetchSpecificationList()
})
</script>

<style scoped>
.specification-list {
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