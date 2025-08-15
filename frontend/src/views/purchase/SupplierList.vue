<template>
  <div class="supplier-list">
    <!-- 骨架屏 -->
    <TableSkeleton 
      v-if="initialLoading"
      :columns="[
        { width: '80px', titleWidth: '20px', cellWidth: '40px' },
        { width: '120px', titleWidth: '60px', cellWidth: '80px' },
        { width: '120px', titleWidth: '60px', cellWidth: '80px' },
        { width: '100px', titleWidth: '40px', cellWidth: '70px' },
        { width: '120px', titleWidth: '40px', cellWidth: '90px' },
        { width: '150px', titleWidth: '50px', cellWidth: '120px' },
        { width: '200px', titleWidth: '40px', cellWidth: '160px' },
        { width: '100px', titleWidth: '60px', cellWidth: '70px' },
        { width: '80px', titleWidth: '30px', cellWidth: '50px', variant: 'button' },
        { width: '150px', titleWidth: '60px', cellWidth: '120px' },
        { width: '150px', titleWidth: '30px', cellWidth: '120px', variant: 'button' }
      ]"
      :rows="10"
      :search-items="4"
    />
    
    <!-- 实际内容 -->
    <el-card v-else>
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon class="header-icon">
              <Shop />
            </el-icon>
            <span class="header-title">供应商管理</span>
          </div>
          <el-button 
            v-if="hasActionPermission('supplier-management:create')"
            type="primary" 
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增供应商
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form
          :inline="true"
          :model="searchForm"
          class="search-form"
        >
          <el-form-item label="供应商名称">
            <el-input 
              v-model="searchForm.supplierName" 
              placeholder="请输入供应商名称" 
              clearable 
              style="width: 200px;"
              @keyup.enter="handleSearchClick"
            >
              <template #prefix>
                <el-icon><Shop /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="联系人">
            <el-input 
              v-model="searchForm.contact" 
              placeholder="请输入联系人" 
              clearable 
              style="width: 200px;"
              @keyup.enter="handleSearchClick"
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="状态">
            <el-select 
              v-model="searchForm.status" 
              placeholder="请选择状态" 
              clearable 
              style="width: 180px;"
            >
              <el-option
                label="正常"
                :value="1"
              />
              <el-option
                label="禁用"
                :value="0"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="供应商类型">
            <el-select 
              v-model="searchForm.supplierType" 
              placeholder="请选择供应商类型" 
              clearable 
              style="width: 180px;"
            >
              <el-option
                label="镜框供应商"
                :value="1"
              />
              <el-option
                label="镜片供应商"
                :value="2"
              />
              <el-option
                label="配件供应商"
                :value="3"
              />
              <el-option
                label="设备供应商"
                :value="4"
              />
              <el-option
                label="其他"
                :value="5"
              />
            </el-select>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              @click="handleSearchClick"
            >
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
            <el-button
              :loading="refreshing"
              @click="handleRefresh"
            >
              <el-icon><RefreshRight /></el-icon>
              刷新
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 批量操作 -->
      <div
        v-if="selectedRows.length > 0"
        class="batch-actions"
      >
        <el-alert
          :title="`已选择 ${selectedRows.length} 项`"
          type="info"
          show-icon
          :closable="false"
        >
          <template #default>
            <div class="batch-buttons">
              <el-button
                v-if="hasActionPermission('supplier-management:delete')"
                type="danger"
                size="small"
                @click="handleBatchDelete"
              >
                <el-icon><Delete /></el-icon>
                批量删除
              </el-button>
            </div>
          </template>
        </el-alert>
      </div>

      <!-- 表格 -->
      <el-table
        :key="tableKey"
        v-loading="loading"
        :data="tableData"
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column
          type="selection"
          width="55"
        />
        <el-table-column 
          prop="supplierCode" 
          label="供应商编码" 
          width="120"
          show-overflow-tooltip
          sortable="custom"
        />
        <el-table-column 
          prop="supplierName" 
          label="供应商名称" 
          width="150"
          show-overflow-tooltip
          sortable="custom"
        />
        <el-table-column 
          prop="supplierTypeText" 
          label="供应商类型" 
          width="120"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span v-if="scope.row.supplierTypeText">{{ scope.row.supplierTypeText }}</span>
            <span
              v-else
              class="text-muted"
            >-</span>
          </template>
        </el-table-column>

        <el-table-column 
          prop="contact" 
          label="联系人" 
          width="100"
          show-overflow-tooltip
        />
        <el-table-column 
          prop="mobile" 
          label="联系电话" 
          width="120"
          show-overflow-tooltip
        />
        <el-table-column 
          prop="email" 
          label="邮箱" 
          width="150"
          show-overflow-tooltip
        />
        <el-table-column 
          prop="address" 
          label="地址" 
          width="200"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span v-if="scope.row.province || scope.row.city || scope.row.district || scope.row.address">
              {{ formatAddress(scope.row) }}
            </span>
            <span
              v-else
              class="text-muted"
            >-</span>
          </template>
        </el-table-column>
        <el-table-column 
          prop="levelText" 
          label="供应商等级" 
          width="100"
          show-overflow-tooltip
        >
          <template #default="scope">
            <span v-if="scope.row.levelText">{{ scope.row.levelText }}</span>
            <span
              v-else
              class="text-muted"
            >-</span>
          </template>
        </el-table-column>
        <el-table-column 
          prop="status" 
          label="状态" 
          width="80"
          align="center"
        >
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              :disabled="!hasActionPermission('supplier-management:edit')"
              @change="(value) => handleStatusUpdate(scope.row, value)"
            />
          </template>
        </el-table-column>
        <el-table-column 
          prop="createTime" 
          label="创建时间" 
          width="150"
          sortable="custom"
        >
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="200"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleView(scope.row)"
            >
              <el-icon><View /></el-icon>
              查看
            </el-button>
            <el-button
              v-if="hasActionPermission('supplier-management:edit')"
              type="primary"
              size="small"
              link
              @click="handleEdit(scope.row)"
            >
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button
              v-if="hasActionPermission('supplier-management:delete')"
              type="danger"
              size="small"
              link
              @click="handleDelete(scope.row)"
            >
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
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
    </el-card>

    <!-- 供应商表单弹窗 -->
    <SupplierForm
      v-model="formDialogVisible"
      :supplier-data="currentSupplier"
      :is-edit="isEdit"
      @success="handleFormSuccess"
    />

    <!-- 供应商详情弹窗 -->
    <SupplierDetail
      v-model="detailDialogVisible"
      :supplier-data="currentSupplier"
      @edit="handleEditFromDetail"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Search, Refresh, RefreshRight, Edit, Delete, View, User, Shop
} from '@element-plus/icons-vue'
import { supplierApi } from '@/api/supplier'
import { formatDateTime } from '@/utils/format'
import { hasActionPermission } from '@/utils/permission'
import { getGlobalStateManager, SupplierOperationHandler, destroyGlobalStateManager } from '@/utils/supplierUtils'

import TableSkeleton from '@/components/Skeleton/TableSkeleton.vue'
import SupplierForm from './components/SupplierForm.vue'
import SupplierDetail from './components/SupplierDetail.vue'

// 响应式数据
const initialLoading = ref(true)
const loading = ref(false)
const refreshing = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const formDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentSupplier = ref(null)
const isEdit = ref(false)
const tableKey = ref(0) // 用于强制重新渲染表格

// 环境变量 - 使用Vue 3的import.meta.env
const isDev = import.meta.env.DEV

// 获取全局状态管理器
const stateManager = getGlobalStateManager()

// 调试信息
const debugInfo = ref({
  lastRefreshTime: null,
  refreshCount: 0,
  isRefreshing: false
})

// 监听刷新状态变化
watch(() => stateManager.isRefreshing.value, (newVal) => {
  debugInfo.value.isRefreshing = newVal
  if (newVal) {
    debugInfo.value.refreshCount++
    debugInfo.value.lastRefreshTime = new Date().toLocaleTimeString()
  }
})

// 搜索表单
const searchForm = reactive({
  supplierName: '',
  contact: '',
  status: null,
  supplierType: null
})

// 排序参数
const sortParams = reactive({
  prop: '',
  order: ''
})

// 分页信息
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 方法
const formatAddress = (row) => {
  const parts = [row.province, row.city, row.district, row.address].filter(Boolean)
  return parts.join(' ')
}

const loadData = async () => {
  try {
    loading.value = true
    const params = {
      ...searchForm,
      page: pagination.page - 1,
      size: pagination.size
    }
    
    // 添加排序参数
    if (sortParams.prop && sortParams.order) {
      params.sortBy = sortParams.prop
      params.sortDirection = sortParams.order === 'ascending' ? 'ASC' : 'DESC'
    }
    
    // 清除空值
    Object.keys(params).forEach(key => {
      if (params[key] === '' || params[key] === null || params[key] === undefined) {
        delete params[key]
      }
    })

    const response = await supplierApi.getSupplierPage(params)
    
    if (response) {
      if (response.success && response.data) {
        if (response.data.content) {
          tableData.value = response.data.content
          pagination.total = response.data.totalElements || 0
        } else {
          tableData.value = []
          pagination.total = 0
        }
      } else {
        ElMessage.error(response.message || '加载数据失败')
        tableData.value = []
        pagination.total = 0
      }
    } else {
      ElMessage.error('加载数据失败')
      tableData.value = []
      pagination.total = 0
    }
  } catch (error) {
    console.error('加载供应商数据失败:', error)
    ElMessage.error('加载数据失败')
    tableData.value = []
    pagination.total = 0
  } finally {
    loading.value = false
    initialLoading.value = false
  }
}

const handleSearchClick = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    supplierName: '',
    contact: '',
    status: null,
    supplierType: null
  })
  pagination.page = 1
  loadData()
}

const handleRefresh = async () => {
  try {
    // 更新调试信息
    debugInfo.value.isRefreshing = true
    debugInfo.value.refreshCount++
    debugInfo.value.lastRefreshTime = new Date().toLocaleString()
    
    // 设置刷新状态
    refreshing.value = true
    
    // 强制刷新数据
    await loadData()
    
    // 更新表格key强制重新渲染
    tableKey.value++
    
    // 显示成功消息
    ElMessage.success('强制刷新成功')
    
    console.log('强制刷新完成，刷新次数:', debugInfo.value.refreshCount)
  } catch (error) {
    console.error('强制刷新失败:', error)
    ElMessage.error('刷新失败')
  } finally {
    // 重置状态
    refreshing.value = false
    debugInfo.value.isRefreshing = false
  }
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSortChange = ({ prop, order }) => {
  console.log('排序变化:', { prop, order })
  sortParams.prop = prop
  sortParams.order = order
  pagination.page = 1 // 重置到第一页
  loadData()
}

const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadData()
}

const handleCurrentChange = (page) => {
  pagination.page = page
  loadData()
}

const handleAdd = () => {
  currentSupplier.value = null
  isEdit.value = false
  formDialogVisible.value = true
}

const handleView = (row) => {
  currentSupplier.value = row
  detailDialogVisible.value = true
}

const handleEdit = (row) => {
  currentSupplier.value = { ...row }
  isEdit.value = true
  formDialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除供应商"${row.supplierName}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await supplierApi.deleteSupplier(row.id)
      if (response.success) {
        // 直接刷新数据，不使用复杂的刷新策略避免重复
        await loadData()
        ElMessage.success(response.message || '删除成功')
      } else {
        ElMessage.error(response.message || '删除失败')
      }
    } catch (error) {
      console.error('删除供应商失败:', error)
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const handleBatchDelete = () => {
  const supplierNames = selectedRows.value.map(row => row.supplierName).join('、')
  ElMessageBox.confirm(
    `确定要删除以下供应商吗？\n${supplierNames}`,
    '确认批量删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      const response = await supplierApi.deleteSuppliers(ids)
      if (response.success) {
        selectedRows.value = []
        
        // 直接刷新数据，不使用复杂的刷新策略避免重复
        await loadData()
        ElMessage.success(response.message || '批量删除成功')
      } else {
        ElMessage.error(response.message || '批量删除失败')
      }
    } catch (error) {
      console.error('批量删除供应商失败:', error)
      ElMessage.error('批量删除失败')
    }
  }).catch(() => {})
}

const handleStatusUpdate = async (row, newStatus) => {
  try {
    const response = await supplierApi.updateSupplierStatus(row.id, newStatus)
    
    if (response.success) {
      // 直接更新tableData中的数据，避免重复刷新
      const index = tableData.value.findIndex(item => item.id === row.id)
      if (index !== -1) {
        tableData.value[index].status = newStatus
        // 强制重新渲染表格
        tableKey.value++
      }
      
      // 显示成功消息，但不自动刷新（因为已经直接更新了数据）
      ElMessage.success(response.message || '状态更新成功')
      
      // 可选：延迟一段时间后刷新以确保数据同步（仅在需要时启用）
      // setTimeout(() => {
      //   loadData()
      // }, 2000)
    } else {
      // 失败时重新加载数据
      await loadData()
      ElMessage.error(response.message || '状态更新失败')
    }
  } catch (error) {
    console.error('更新供应商状态失败:', error)
    // 出错时重新加载数据
    await loadData()
    ElMessage.error('状态更新失败')
  }
}

const handleFormSuccess = () => {
  formDialogVisible.value = false
  
  // 直接刷新数据，不使用复杂的刷新策略避免重复
  loadData()
  ElMessage.success('操作成功')
  
  // 如果需要延迟刷新确保数据同步，可以启用下面的代码
  // setTimeout(() => {
  //   loadData()
  // }, 1000)
}

const handleEditFromDetail = (supplierData) => {
  detailDialogVisible.value = false
  currentSupplier.value = { ...supplierData }
  isEdit.value = true
  formDialogVisible.value = true
}

// 生命周期
onMounted(() => {
  loadData()
})

// 组件卸载时清理资源
onUnmounted(() => {
  // 注意：这里不销毁全局状态管理器，因为可能还有其他组件在使用
  // 只有在应用完全退出时才销毁
})
</script>

<style scoped>
.supplier-list {
  height: 100%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-icon {
  margin-right: 8px;
  font-size: 18px;
  color: #409eff;
}

.header-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.search-area {
  margin-bottom: 16px;
}

.search-form {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
}

.batch-actions {
  margin-bottom: 16px;
}

.batch-buttons {
  margin-top: 8px;
}

.text-muted {
  color: #909399;
}

.pagination-wrapper {
  margin-top: 16px;
  display: flex;
  justify-content: center;
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

:deep(.el-pagination) {
  justify-content: center;
}

.debug-info {
  margin-top: 16px;
  padding: 12px;
  background-color: #f0f9eb; /* 浅绿色背景 */
  border-radius: 8px;
  border: 1px solid #e1f3d8; /* 浅绿色边框 */
}

.debug-details p {
  margin-bottom: 4px;
  font-size: 14px;
  color: #67c23a; /* 绿色文字 */
}

.debug-details strong {
  color: #333;
}
</style> 