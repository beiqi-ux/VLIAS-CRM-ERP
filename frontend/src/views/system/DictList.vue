<template>
  <div class="dict-container">
    <div class="search-bar">
      <el-input
        v-model="searchForm.dictName"
        placeholder="字典名称"
        clearable
        style="width: 200px; margin-right: 10px"
        @keyup.enter="handleSearch"
      />
      <el-input
        v-model="searchForm.dictCode"
        placeholder="字典编码"
        clearable
        style="width: 200px; margin-right: 10px"
        @keyup.enter="handleSearch"
      />
      <el-select
        v-model="searchForm.status"
        placeholder="状态"
        clearable
        style="width: 120px; margin-right: 10px"
      >
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
      <el-button type="success" @click="handleAdd" style="margin-left: 10px">新增字典</el-button>
    </div>

    <el-table
      v-loading="tableLoading"
      :data="tableData"
      border
      style="width: 100%; margin-top: 15px"
    >
      <el-table-column prop="id" label="ID" width="80">
        <template #default="scope">
          {{ $formatId(scope.row.id) }}
        </template>
      </el-table-column>
      <el-table-column prop="dictName" label="字典名称" />
      <el-table-column prop="dictCode" label="字典编码" />
      <el-table-column prop="description" label="描述" show-overflow-tooltip />
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="primary" @click="handleManageItems(scope.row)">管理字典项</el-button>
          <el-button
            size="small"
            :type="scope.row.status === 1 ? 'warning' : 'success'"
            @click="handleToggleStatus(scope.row)"
          >
            {{ scope.row.status === 1 ? '禁用' : '启用' }}
          </el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-if="total > 0"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-size="pageSize"
      :current-page="currentPage"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 15px; justify-content: flex-end"
    />

    <!-- 字典表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formTitle"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="dictFormRef"
        :model="dictForm"
        :rules="dictRules"
        label-width="100px"
      >
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="dictForm.dictName" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="字典编码" prop="dictCode">
          <el-input 
            v-model="dictForm.dictCode" 
            placeholder="请输入字典编码"
            :disabled="isEdit"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="dictForm.description"
            type="textarea"
            placeholder="请输入描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="dictForm.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 字典项管理对话框 -->
    <el-dialog
      v-model="itemDialogVisible"
      title="字典项管理"
      width="80%"
      :close-on-click-modal="false"
    >
      <div class="item-header">
        <span>字典：{{ currentDict.dictName }} ({{ currentDict.dictCode }})</span>
        <el-button type="primary" size="small" @click="handleAddItem">新增字典项</el-button>
      </div>
      
      <el-table
        v-loading="itemTableLoading"
        :data="itemTableData"
        border
        style="width: 100%; margin-top: 15px"
      >
        <el-table-column prop="id" label="ID" width="80">
          <template #default="scope">
            {{ $formatId(scope.row.id) }}
          </template>
        </el-table-column>
        <el-table-column prop="itemText" label="字典项文本" />
        <el-table-column prop="itemValue" label="字典项值" />
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="handleEditItem(scope.row)">编辑</el-button>
            <el-button
              size="small"
              :type="scope.row.status === 1 ? 'warning' : 'success'"
              @click="handleToggleItemStatus(scope.row)"
            >
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button
              size="small"
              type="danger"
              @click="handleDeleteItem(scope.row)"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 字典项表单对话框 -->
    <el-dialog
      v-model="itemFormDialogVisible"
      :title="itemFormTitle"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="dictItemFormRef"
        :model="dictItemForm"
        :rules="dictItemRules"
        label-width="100px"
      >
        <el-form-item label="字典项文本" prop="itemText">
          <el-input v-model="dictItemForm.itemText" placeholder="请输入字典项文本" />
        </el-form-item>
        <el-form-item label="字典项值" prop="itemValue">
          <el-input v-model="dictItemForm.itemValue" placeholder="请输入字典项值" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="dictItemForm.description"
            type="textarea"
            placeholder="请输入描述"
          />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number 
            v-model="dictItemForm.sort" 
            :min="0" 
            :max="9999"
            placeholder="请输入排序"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="dictItemForm.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="itemFormDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmitItem" :loading="itemSubmitLoading">
            {{ isEditItem ? '更新' : '创建' }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDateTime } from '@/utils/format'
import {
  getDictList,
  createDict,
  updateDict,
  deleteDict,
  updateDictStatus,
  getDictItemsByDictId,
  createDictItem,
  updateDictItem,
  deleteDictItem,
  updateDictItemStatus
} from '@/api/dict'

// 表格数据
const tableData = ref([])
const tableLoading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 搜索表单
const searchForm = reactive({
  dictName: '',
  dictCode: '',
  status: null
})

// 字典表单
const dialogVisible = ref(false)
const dictFormRef = ref()
const dictForm = reactive({
  id: null,
  dictName: '',
  dictCode: '',
  description: '',
  status: 1
})

const dictRules = {
  dictName: [
    { required: true, message: '请输入字典名称', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  dictCode: [
    { required: true, message: '请输入字典编码', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z_][a-zA-Z0-9_]*$/, message: '编码只能包含字母、数字和下划线，且以字母或下划线开头', trigger: 'blur' }
  ]
}

const isEdit = ref(false)
const submitLoading = ref(false)

// 字典项相关
const itemDialogVisible = ref(false)
const currentDict = ref({})
const itemTableData = ref([])
const itemTableLoading = ref(false)

// 字典项表单
const itemFormDialogVisible = ref(false)
const dictItemFormRef = ref()
const dictItemForm = reactive({
  id: null,
  dictId: null,
  itemText: '',
  itemValue: '',
  description: '',
  sort: 0,
  status: 1
})

const dictItemRules = {
  itemText: [
    { required: true, message: '请输入字典项文本', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  itemValue: [
    { required: true, message: '请输入字典项值', trigger: 'blur' },
    { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  sort: [
    { type: 'number', message: '排序必须是数字', trigger: 'blur' }
  ]
}

const isEditItem = ref(false)
const itemSubmitLoading = ref(false)

// 计算属性
const formTitle = computed(() => (isEdit.value ? '编辑字典' : '新增字典'))
const itemFormTitle = computed(() => (isEditItem.value ? '编辑字典项' : '新增字典项'))

// 获取数据
const fetchData = async () => {
  tableLoading.value = true
  try {
    const params = {
      page: currentPage.value - 1,
      size: pageSize.value,
      ...searchForm
    }
    
    const response = await getDictList(params)
    if (response.success) {
      tableData.value = response.data.content
      total.value = response.data.totalElements
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取数据失败:', error)
    ElMessage.error('获取数据失败')
  } finally {
    tableLoading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    dictName: '',
    dictCode: '',
    status: null
  })
  handleSearch()
}

// 分页处理
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  fetchData()
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
  fetchData()
}

// 新增
const handleAdd = () => {
  isEdit.value = false
  Object.assign(dictForm, {
    id: null,
    dictName: '',
    dictCode: '',
    description: '',
    status: 1
  })
  dialogVisible.value = true
  
  // 清除验证状态
  if (dictFormRef.value) {
    dictFormRef.value.clearValidate()
  }
}

// 编辑
const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(dictForm, { ...row })
  dialogVisible.value = true
  
  // 清除验证状态
  if (dictFormRef.value) {
    dictFormRef.value.clearValidate()
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    const valid = await dictFormRef.value.validate()
    if (!valid) return
    
    submitLoading.value = true
    
    let response
    if (isEdit.value) {
      response = await updateDict(dictForm.id, dictForm)
    } else {
      response = await createDict(dictForm)
    }
    
    if (response.success) {
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitLoading.value = false
  }
}

// 切换状态
const handleToggleStatus = async (row) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    const response = await updateDictStatus(row.id, newStatus)
    
    if (response.success) {
      ElMessage.success(`${newStatus === 1 ? '启用' : '禁用'}成功`)
      fetchData()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    console.error('状态更新失败:', error)
    ElMessage.error('状态更新失败')
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除字典 "${row.dictName}" 吗？删除后将同时删除所有相关字典项，此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await deleteDict(row.id)
    if (response.success) {
      ElMessage.success('删除成功')
      fetchData()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 管理字典项
const handleManageItems = async (row) => {
  currentDict.value = row
  itemDialogVisible.value = true
  await fetchItemData()
}

// 获取字典项数据
const fetchItemData = async () => {
  itemTableLoading.value = true
  try {
    const response = await getDictItemsByDictId(currentDict.value.id)
    if (response.success) {
      itemTableData.value = response.data
    } else {
      ElMessage.error(response.message || '获取字典项失败')
    }
  } catch (error) {
    console.error('获取字典项失败:', error)
    ElMessage.error('获取字典项失败')
  } finally {
    itemTableLoading.value = false
  }
}

// 新增字典项
const handleAddItem = () => {
  isEditItem.value = false
  Object.assign(dictItemForm, {
    id: null,
    dictId: currentDict.value.id,
    itemText: '',
    itemValue: '',
    description: '',
    sort: 0,
    status: 1
  })
  itemFormDialogVisible.value = true
  
  // 清除验证状态
  if (dictItemFormRef.value) {
    dictItemFormRef.value.clearValidate()
  }
}

// 编辑字典项
const handleEditItem = (row) => {
  isEditItem.value = true
  Object.assign(dictItemForm, { ...row })
  itemFormDialogVisible.value = true
  
  // 清除验证状态
  if (dictItemFormRef.value) {
    dictItemFormRef.value.clearValidate()
  }
}

// 提交字典项表单
const handleSubmitItem = async () => {
  try {
    const valid = await dictItemFormRef.value.validate()
    if (!valid) return
    
    itemSubmitLoading.value = true
    
    let response
    if (isEditItem.value) {
      response = await updateDictItem(dictItemForm.id, dictItemForm)
    } else {
      response = await createDictItem(dictItemForm)
    }
    
    if (response.success) {
      ElMessage.success(isEditItem.value ? '更新成功' : '创建成功')
      itemFormDialogVisible.value = false
      fetchItemData()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    itemSubmitLoading.value = false
  }
}

// 切换字典项状态
const handleToggleItemStatus = async (row) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    const response = await updateDictItemStatus(row.id, newStatus)
    
    if (response.success) {
      ElMessage.success(`${newStatus === 1 ? '启用' : '禁用'}成功`)
      fetchItemData()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    console.error('状态更新失败:', error)
    ElMessage.error('状态更新失败')
  }
}

// 删除字典项
const handleDeleteItem = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除字典项 "${row.itemText}" 吗？此操作不可恢复。`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await deleteDictItem(row.id)
    if (response.success) {
      ElMessage.success('删除成功')
      fetchItemData()
    } else {
      ElMessage.error(response.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 生命周期
onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.dict-container {
  padding: 20px;
}

.search-bar {
  margin-bottom: 20px;
}

.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  margin-bottom: 10px;
}

.dialog-footer {
  text-align: right;
}
</style> 