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
          width="180" 
          fixed="right"
        >
          <template #default="{ row }">
            <div class="action-buttons">
            <el-button 
              v-if="hasPermission(PERMISSIONS.GOODS.BRAND.EDIT)"
              type="primary" 
                size="small"
                class="action-btn edit-btn"
              @click="handleEdit(row)"
            >
                <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button 
              v-if="hasPermission(PERMISSIONS.GOODS.BRAND.DELETE)"
              type="danger" 
                size="small"
                class="action-btn delete-btn"
              @click="handleDelete(row)"
            >
                <el-icon><Delete /></el-icon>
              删除
            </el-button>
            </div>
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
          <el-upload
            class="logo-uploader"
            :action="uploadAction"
            :headers="uploadHeaders"
            :show-file-list="false"
            :on-success="handleLogoSuccess"
            :on-error="handleLogoError"
            :before-upload="beforeLogoUpload"
            accept="image/jpeg,image/jpg,image/png,image/gif"
          >
            <div v-if="formData.brandLogo" class="logo-preview">
            <el-image 
              :src="formData.brandLogo" 
                style="width: 120px; height: 80px; border-radius: 8px;"
              fit="cover"
            />
              <div class="logo-overlay">
                <el-icon><Picture /></el-icon>
                <span>更换图片</span>
              </div>
            </div>
            <div v-else class="logo-upload-placeholder">
              <el-icon class="upload-icon"><Upload /></el-icon>
              <div class="upload-text">点击上传Logo</div>
              <div class="upload-hint">支持 JPG、PNG、GIF 格式，大小不超过2MB</div>
          </div>
          </el-upload>
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
import { Search, Refresh, Plus, Delete, Edit, Upload, Picture } from '@element-plus/icons-vue'
import {
  getBrandList,
  createBrand,
  updateBrand,
  deleteBrand,
  batchDeleteBrands
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

// 上传配置
const uploadAction = ref('/api/files/upload') // 后端文件上传接口
const uploadHeaders = ref({
  'Authorization': localStorage.getItem('token') ? `Bearer ${localStorage.getItem('token')}` : ''
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

// 图片上传相关方法
const beforeLogoUpload = (file) => {
  const isJPGorPNG = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'].includes(file.type)
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPGorPNG) {
    ElMessage.error('Logo图片只能是 JPG、PNG、GIF 格式!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('Logo图片大小不能超过 2MB!')
    return false
  }
  return true
}

const handleLogoSuccess = (response) => {
  if (response.success) {
    formData.brandLogo = response.data.url
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

const handleLogoError = () => {
  ElMessage.error('图片上传失败，请稍后重试')
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
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 6px;
  font-size: 11px;
  color: #909399;
  border: 1px solid #e4e7ed;
  font-weight: 500;
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

/* 操作按钮样式 */
.action-buttons {
  display: flex;
  gap: 4px;
  align-items: center;
  justify-content: flex-start;
  flex-wrap: wrap;
}

.action-btn {
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid transparent;
}

.action-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-btn .el-icon {
  margin-right: 4px;
}

.edit-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border-color: #409eff;
  color: white;
}

.edit-btn:hover {
  background: linear-gradient(135deg, #337ecc 0%, #409eff 100%);
  border-color: #337ecc;
}

.status-btn.el-button--success {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border-color: #67c23a;
  color: white;
}

.status-btn.el-button--success:hover {
  background: linear-gradient(135deg, #529b2e 0%, #67c23a 100%);
  border-color: #529b2e;
}

.status-btn.el-button--warning {
  background: linear-gradient(135deg, #e6a23c 0%, #ebb563 100%);
  border-color: #e6a23c;
  color: white;
}

.status-btn.el-button--warning:hover {
  background: linear-gradient(135deg, #b88230 0%, #e6a23c 100%);
  border-color: #b88230;
}

.delete-btn {
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
  border-color: #f56c6c;
  color: white;
}

.delete-btn:hover {
  background: linear-gradient(135deg, #dd6161 0%, #f56c6c 100%);
  border-color: #dd6161;
}

/* Logo上传组件样式 */
.logo-uploader {
  width: 100%;
}

.logo-uploader .el-upload {
  position: relative;
  cursor: pointer;
  border: 2px dashed #d9d9d9;
  border-radius: 8px;
  overflow: hidden;
  transition: border-color 0.3s ease;
}

.logo-uploader .el-upload:hover {
  border-color: #409eff;
}

.logo-upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  min-height: 120px;
  background-color: #fafafa;
  transition: background-color 0.3s ease;
}

.logo-upload-placeholder:hover {
  background-color: #f0f9ff;
}

.upload-icon {
  font-size: 32px;
  color: #8c939d;
  margin-bottom: 8px;
}

.upload-text {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
  font-weight: 500;
}

.upload-hint {
  font-size: 12px;
  color: #909399;
}

.logo-preview {
  position: relative;
  display: inline-block;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.logo-preview:hover .logo-overlay {
  opacity: 1;
}

.logo-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
  color: white;
  font-size: 12px;
}

.logo-overlay .el-icon {
  font-size: 20px;
  margin-bottom: 4px;
}
</style> 