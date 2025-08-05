<template>
  <div class="brand-list">
    <!-- 搜索栏 -->
    <el-card
      class="search-card"
      shadow="never"
    >
      <el-form
        :model="searchForm"
        inline
      >
        <el-form-item label="品牌名称">
          <el-input 
            v-model="searchForm.brandName" 
            placeholder="请输入品牌名称" 
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
            v-if="hasPermission(PERMISSIONS.GOODS.BRAND.CREATE)"
            type="primary" 
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增品牌
          </el-button>
          <el-button 
            v-if="hasPermission(PERMISSIONS.GOODS.BRAND.DELETE)"
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
        :data="brandList"
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
          label="品牌Logo"
          width="100"
        >
          <template #default="{ row }">
            <el-image
              v-if="row.brandLogo"
              :src="row.brandLogo"
              fit="cover"
              style="width: 60px; height: 40px; border-radius: 4px;"
              :preview-src-list="[row.brandLogo]"
              preview-teleported
            />
            <div
              v-else
              class="no-logo"
            >
              暂无Logo
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="brandName"
          label="品牌名称"
          min-width="150"
        />
        <el-table-column
          prop="description"
          label="品牌描述"
          min-width="200"
          show-overflow-tooltip
        />
        <el-table-column
          label="官网"
          width="120"
        >
          <template #default="{ row }">
            <el-link 
              v-if="row.website" 
              :href="row.website" 
              target="_blank" 
              type="primary"
            >
              访问官网
            </el-link>
            <span
              v-else
              class="text-gray"
            >-</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="sort"
          label="排序"
          width="80"
        />
        <el-table-column
          label="状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
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
          v-if="hasPermission(PERMISSIONS.GOODS.BRAND.EDIT) || hasPermission(PERMISSIONS.GOODS.BRAND.DELETE)"
          label="操作" 
          width="220" 
          fixed="right"
        >
          <template #default="{ row }">
            <el-button 
              v-if="hasPermission(PERMISSIONS.GOODS.BRAND.EDIT)"
              type="primary" 
              text 
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="hasPermission(PERMISSIONS.GOODS.BRAND.EDIT)"
              :type="row.status === 1 ? 'warning' : 'success'" 
              text 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button 
              v-if="hasPermission(PERMISSIONS.GOODS.BRAND.DELETE)"
              type="danger" 
              text 
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item
          label="品牌名称"
          prop="brandName"
        >
          <el-input
            v-model="formData.brandName"
            placeholder="请输入品牌名称"
          />
        </el-form-item>
        <el-form-item
          label="品牌Logo"
          prop="brandLogo"
        >
          <el-input
            v-model="formData.brandLogo"
            placeholder="请输入Logo图片URL"
          />
          <div
            v-if="formData.brandLogo"
            class="logo-preview"
          >
            <el-image 
              :src="formData.brandLogo" 
              style="width: 100px; height: 60px; border-radius: 4px; margin-top: 8px;"
              fit="cover"
            />
          </div>
        </el-form-item>
        <el-form-item
          label="官网地址"
          prop="website"
        >
          <el-input
            v-model="formData.website"
            placeholder="请输入官网地址（可选）"
          />
        </el-form-item>
        <el-form-item
          label="排序"
          prop="sort"
        >
          <el-input-number
            v-model="formData.sort"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="状态"
          prop="status"
        >
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">
              启用
            </el-radio>
            <el-radio :label="0">
              禁用
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="品牌描述"
          prop="description"
        >
          <el-input 
            v-model="formData.description" 
            type="textarea" 
            :rows="4" 
            placeholder="请输入品牌描述" 
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">
            取消
          </el-button>
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
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import {
  getBrandList,
  createBrand,
  updateBrand,
  deleteBrand,
  batchDeleteBrands,
  enableBrand,
  disableBrand
} from '@/api/brand'
import { formatDateTime } from '@/utils/format'
import { hasPermission, PERMISSIONS } from '@/utils/permission'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const brandList = ref([])
const selectedRows = ref([])

// 搜索表单
const searchForm = reactive({
  brandName: '',
  status: null
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = computed(() => formData.id ? '编辑品牌' : '新增品牌')
const formRef = ref()

// 表单数据
const formData = reactive({
  id: null,
  brandName: '',
  brandLogo: '',
  website: '',
  sort: 0,
  status: 1,
  description: ''
})

// 表单验证规则
const formRules = {
  brandName: [
    { required: true, message: '请输入品牌名称', trigger: 'blur' }
  ],
  sort: [
    { required: true, message: '请输入排序值', trigger: 'blur' }
  ]
}

// 方法
const loadBrandList = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.current - 1,
      size: pagination.size,
      ...searchForm
    }
    const response = await getBrandList(params)
    if (response.success) {
      const { content, totalElements } = response.data
      brandList.value = content
      pagination.total = totalElements
    }
  } catch (error) {
    ElMessage.error('获取品牌列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadBrandList()
}

const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = key === 'status' ? null : ''
  })
  pagination.current = 1
  loadBrandList()
}

const handleRefresh = () => {
  loadBrandList()
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  loadBrandList()
}

const handleCurrentChange = (page) => {
  pagination.current = page
  loadBrandList()
}

const handleAdd = () => {
  resetFormData()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.keys(formData).forEach(key => {
    formData[key] = row[key] || (key === 'sort' ? 0 : 
      key === 'status' ? (row[key] ?? 1) : 
        key === 'id' ? row[key] : '')
  })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除品牌"${row.brandName}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await deleteBrand(row.id)
      if (response.success) {
        ElMessage.success('删除成功')
        loadBrandList()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的品牌')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个品牌吗？`,
    '确认批量删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      const response = await batchDeleteBrands(ids)
      if (response.success) {
        ElMessage.success('批量删除成功')
        loadBrandList()
      }
    } catch (error) {
      ElMessage.error('批量删除失败')
    }
  })
}

const handleToggleStatus = async (row) => {
  try {
    const action = row.status === 1 ? '禁用' : '启用'
    await ElMessageBox.confirm(`确定要${action}品牌"${row.brandName}"吗？`, `确认${action}`)
    
    const response = row.status === 1 
      ? await disableBrand(row.id)
      : await enableBrand(row.id)
      
    if (response.success) {
      ElMessage.success(`${action}成功`)
      loadBrandList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const response = formData.id 
      ? await updateBrand(formData.id, formData)
      : await createBrand(formData)
      
    if (response.success) {
      ElMessage.success(formData.id ? '更新成功' : '创建成功')
      dialogVisible.value = false
      loadBrandList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  } finally {
    submitLoading.value = false
  }
}

const resetFormData = () => {
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'status' ? 1 :
      key === 'sort' ? 0 :
        key === 'id' ? null : ''
  })
}

// 生命周期
onMounted(() => {
  loadBrandList()
})
</script>

<style scoped>
.brand-list {
  padding: 20px;
}

.search-card, .operation-card, .table-card {
  margin-bottom: 16px;
}

.operation-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.no-logo {
  width: 60px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  border-radius: 4px;
  font-size: 12px;
  color: #999;
}

.text-gray {
  color: #999;
}

.logo-preview {
  margin-top: 8px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}
</style> 