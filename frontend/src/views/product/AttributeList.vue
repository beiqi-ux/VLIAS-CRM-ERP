<template>
  <div class="attribute-list">
    <!-- 搜索栏 -->
    <el-card
      class="search-card"
      shadow="never"
    >
      <el-form
        :model="searchForm"
        inline
      >
        <el-form-item label="属性名称">
          <el-input 
            v-model="searchForm.attributeName" 
            placeholder="请输入属性名称" 
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
            新增属性
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
        :data="attributeList"
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
          prop="attributeName"
          label="属性名称"
          min-width="150"
        />
        <el-table-column
          prop="attributeCode"
          label="属性编码"
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
      :title="isEdit ? '编辑属性' : '新增属性'"
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
          label="属性名称"
          prop="attributeName"
        >
          <el-input
            v-model="form.attributeName"
            placeholder="请输入属性名称"
          />
        </el-form-item>
        <el-form-item
          label="属性编码"
          prop="attributeCode"
        >
          <el-input
            v-model="form.attributeCode"
            placeholder="请输入属性编码"
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
  getAttributeList, 
  createAttribute, 
  updateAttribute, 
  deleteAttribute, 
  batchDeleteAttributes 
} from '@/api/attribute'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const selectedRows = ref([])
const attributeList = ref([])
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  attributeName: '',
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
  attributeName: '',
  attributeCode: '',
  description: '',
  status: 1
})

// 表单验证规则
const formRules = {
  attributeName: [
    { required: true, message: '请输入属性名称', trigger: 'blur' }
  ],
  attributeCode: [
    { required: true, message: '请输入属性编码', trigger: 'blur' }
  ]
}

// 获取属性列表
async function fetchAttributeList() {
  try {
    loading.value = true
    const { data } = await getAttributeList({
      ...searchForm,
      page: pagination.currentPage - 1, // 后端分页从0开始
      size: pagination.pageSize
    })
    
    attributeList.value = data.content
    pagination.total = data.totalElements
  } catch (error) {
    console.error('获取属性列表失败:', error)
    ElMessage.error('获取属性列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
function handleSearch() {
  pagination.currentPage = 1
  fetchAttributeList()
}

// 重置搜索
function resetSearch() {
  Object.assign(searchForm, {
    attributeName: '',
    status: null
  })
  pagination.currentPage = 1
  fetchAttributeList()
}

// 刷新
function handleRefresh() {
  fetchAttributeList()
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
    `确定要删除属性 "${row.attributeName}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await deleteAttribute(row.id)
      ElMessage.success('删除成功')
      fetchAttributeList()
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
      await batchDeleteAttributes(ids)
      ElMessage.success('批量删除成功')
      fetchAttributeList()
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
  fetchAttributeList()
}

// 当前页变化
function handleCurrentChange(page) {
  pagination.currentPage = page
  fetchAttributeList()
}

// 提交表单
async function handleSubmit() {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      await updateAttribute(form.id, form)
      ElMessage.success('更新成功')
    } else {
      await createAttribute(form)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchAttributeList()
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
    attributeName: '',
    attributeCode: '',
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
  fetchAttributeList()
})
</script>

<style scoped>
.attribute-list {
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