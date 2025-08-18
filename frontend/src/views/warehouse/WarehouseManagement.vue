<template>
  <div class="warehouse-management">
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
        { width: '80px', titleWidth: '30px', cellWidth: '50px', variant: 'button' },
        { width: '150px', titleWidth: '60px', cellWidth: '120px' },
        { width: '180px', titleWidth: '30px', cellWidth: '150px', variant: 'button' }
      ]"
      :rows="10"
      :search-items="3"
    />
    
    <!-- 实际内容 -->
    <el-card v-else>
      <!-- 搜索栏 -->
      <el-card class="search-card" shadow="never">
        <el-form :model="searchForm" inline>
          <el-form-item label="仓库名称">
            <el-input 
              v-model="searchForm.warehouseName" 
              placeholder="请输入仓库名称" 
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="仓库编码">
            <el-input 
              v-model="searchForm.warehouseCode" 
              placeholder="请输入仓库编码" 
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
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
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
      <el-card class="operation-card" shadow="never">
        <div class="operation-row">
          <div class="operation-left">
            <el-button
              v-if="hasActionPermission('warehouse-management:create')"
              type="primary"
              @click="handleAdd"
            >
              <el-icon><Plus /></el-icon>
              新增仓库
            </el-button>
            <el-button
              v-if="hasActionPermission('warehouse-management:delete') && selectedRows.length > 0"
              type="danger"
              @click="handleBatchDelete"
            >
              <el-icon><Delete /></el-icon>
              批量删除
            </el-button>
            <el-button
              v-if="hasActionPermission('warehouse-management:change-status') && selectedRows.length > 0"
              type="success"
              @click="handleBatchEnable"
            >
              <el-icon><Check /></el-icon>
              批量启用
            </el-button>
            <el-button
              v-if="hasActionPermission('warehouse-management:change-status') && selectedRows.length > 0"
              type="warning"
              @click="handleBatchDisable"
            >
              <el-icon><Close /></el-icon>
              批量禁用
            </el-button>
          </div>
          <div class="operation-right">
            <el-button
              v-if="hasActionPermission('warehouse-management:export')"
              type="info"
              @click="handleExport"
            >
              <el-icon><Download /></el-icon>
              导出
            </el-button>
            <el-button
              type="primary"
              :loading="refreshing"
              @click="handleRefresh"
            >
              <el-icon><RefreshRight /></el-icon>
              刷新
            </el-button>
          </div>
        </div>
      </el-card>

      <!-- 数据表格 -->
      <el-card class="table-card" shadow="never">
        <el-table
          v-loading="loading"
          :data="tableData"
          stripe
          style="width: 100%"
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="warehouseName" label="仓库名称" min-width="120" />
          <el-table-column prop="warehouseCode" label="仓库编码" width="120" />
          <el-table-column prop="contact" label="联系人" width="100" />
          <el-table-column prop="mobile" label="联系电话" width="120" />
          <el-table-column label="地址" min-width="200">
            <template #default="{ row }">
              {{ formatAddress(row) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="80">
            <template #default="{ row }">
              <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="isDefault" label="默认仓库" width="100">
            <template #default="{ row }">
              <el-tag v-if="row.isDefault === 1" type="warning">默认</el-tag>
              <span v-else>-</span>
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="80" />
          <el-table-column prop="createTime" label="创建时间" width="160">
            <template #default="{ row }">
              {{ formatDateTime(row.createTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button
                v-if="hasActionPermission('warehouse-management:view')"
                link
                type="primary"
                size="small"
                @click="handleView(row)"
              >
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button
                v-if="hasActionPermission('warehouse-management:edit')"
                link
                type="primary"
                size="small"
                @click="handleEdit(row)"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button
                v-if="hasActionPermission('warehouse-management:change-status')"
                link
                :type="row.status === 1 ? 'warning' : 'success'"
                size="small"
                @click="handleToggleStatus(row)"
              >
                <el-icon><Switch /></el-icon>
                {{ row.status === 1 ? '禁用' : '启用' }}
              </el-button>
              <el-button
                v-if="hasActionPermission('warehouse-management:edit') && row.isDefault !== 1"
                link
                type="warning"
                size="small"
                @click="handleSetDefault(row)"
              >
                <el-icon><Star /></el-icon>
                设为默认
              </el-button>
              <el-button
                v-if="hasActionPermission('warehouse-management:delete')"
                link
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
            v-model:current-page="pagination.page"
            v-model:page-size="pagination.size"
            :page-sizes="[10, 20, 50, 100]"
            :total="pagination.total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-card>

      <!-- 表单对话框 -->
      <WarehouseForm
        v-model:visible="formDialogVisible"
        :warehouse="currentWarehouse"
        :is-edit="isEdit"
        @success="handleFormSuccess"
      />

      <!-- 详情对话框 -->
      <WarehouseDetail
        v-model:visible="detailDialogVisible"
        :warehouse="currentWarehouse"
      />
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Search, Refresh, RefreshRight, Edit, Delete, View, Switch, Star,
  Download, Check, Close
} from '@element-plus/icons-vue'
import { warehouseApi } from '@/api/warehouse'
import { formatDateTime } from '@/utils/format'
import { hasActionPermission } from '@/utils/permission'

import TableSkeleton from '@/components/Skeleton/TableSkeleton.vue'
import WarehouseForm from './components/WarehouseForm.vue'
import WarehouseDetail from './components/WarehouseDetail.vue'

// 响应式数据
const initialLoading = ref(true)
const loading = ref(false)
const refreshing = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const formDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentWarehouse = ref(null)
const isEdit = ref(false)

// 搜索表单
const searchForm = reactive({
  warehouseName: '',
  warehouseCode: '',
  status: null
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
  return parts.join(' ') || '-'
}

const loadData = async () => {
  try {
    loading.value = true
    
    const params = {
      page: pagination.page - 1,
      size: pagination.size,
      ...searchForm
    }
    
    // 添加排序参数
    if (sortParams.prop) {
      params.sortBy = sortParams.prop
      params.sortDir = sortParams.order === 'ascending' ? 'asc' : 'desc'
    }
    
    const response = await warehouseApi.getPage(params)
    
    if (response.code === 200) {
      tableData.value = response.data.content
      pagination.total = response.data.totalElements
    } else {
      ElMessage.error(response.message || '获取数据失败')
    }
  } catch (error) {
    console.error('获取仓库列表失败：', error)
    ElMessage.error('获取数据失败')
  } finally {
    loading.value = false
    initialLoading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const resetSearch = () => {
  Object.assign(searchForm, {
    warehouseName: '',
    warehouseCode: '',
    status: null
  })
  pagination.page = 1
  loadData()
}

const handleRefresh = async () => {
  refreshing.value = true
  try {
    await loadData()
    ElMessage.success('刷新成功')
  } finally {
    refreshing.value = false
  }
}

const handleAdd = () => {
  currentWarehouse.value = null
  isEdit.value = false
  formDialogVisible.value = true
}

const handleEdit = (row) => {
  currentWarehouse.value = { ...row }
  isEdit.value = true
  formDialogVisible.value = true
}

const handleView = (row) => {
  currentWarehouse.value = row
  detailDialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要删除仓库"${row.warehouseName}"吗？`, '确认删除', {
      type: 'warning'
    })
    
    const response = await warehouseApi.delete(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      await loadData()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除仓库失败：', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的${selectedRows.value.length}个仓库吗？`, '确认删除', {
      type: 'warning'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    const response = await warehouseApi.batchDelete(ids)
    
    if (response.code === 200) {
      ElMessage.success('批量删除成功')
      selectedRows.value = []
      await loadData()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除仓库失败：', error)
      ElMessage.error('批量删除失败')
    }
  }
}

const handleToggleStatus = async (row) => {
  try {
    const action = row.status === 1 ? '禁用' : '启用'
    await ElMessageBox.confirm(`确定要${action}仓库"${row.warehouseName}"吗？`, `确认${action}`, {
      type: 'warning'
    })
    
    const response = row.status === 1 
      ? await warehouseApi.disable(row.id)
      : await warehouseApi.enable(row.id)
    
    if (response.code === 200) {
      ElMessage.success(`${action}成功`)
      await loadData()
    } else {
      ElMessage.error(response.message || `${action}失败`)
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('修改仓库状态失败：', error)
      ElMessage.error('操作失败')
    }
  }
}

const handleBatchEnable = async () => {
  try {
    await ElMessageBox.confirm(`确定要启用选中的${selectedRows.value.length}个仓库吗？`, '确认启用', {
      type: 'warning'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    const response = await warehouseApi.batchEnable(ids)
    
    if (response.code === 200) {
      ElMessage.success('批量启用成功')
      selectedRows.value = []
      await loadData()
    } else {
      ElMessage.error(response.message || '批量启用失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量启用仓库失败：', error)
      ElMessage.error('批量启用失败')
    }
  }
}

const handleBatchDisable = async () => {
  try {
    await ElMessageBox.confirm(`确定要禁用选中的${selectedRows.value.length}个仓库吗？`, '确认禁用', {
      type: 'warning'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    const response = await warehouseApi.batchDisable(ids)
    
    if (response.code === 200) {
      ElMessage.success('批量禁用成功')
      selectedRows.value = []
      await loadData()
    } else {
      ElMessage.error(response.message || '批量禁用失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量禁用仓库失败：', error)
      ElMessage.error('批量禁用失败')
    }
  }
}

const handleSetDefault = async (row) => {
  try {
    await ElMessageBox.confirm(`确定要将"${row.warehouseName}"设为默认仓库吗？`, '确认设置', {
      type: 'warning'
    })
    
    const response = await warehouseApi.setDefault(row.id)
    if (response.code === 200) {
      ElMessage.success('设置默认仓库成功')
      await loadData()
    } else {
      ElMessage.error(response.message || '设置失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('设置默认仓库失败：', error)
      ElMessage.error('设置失败')
    }
  }
}

const handleExport = async () => {
  try {
    const response = await warehouseApi.export(searchForm)
    if (response.code === 200) {
      // 这里可以添加导出逻辑，比如下载Excel文件
      ElMessage.success('导出成功')
      console.log('导出数据：', response.data)
    } else {
      ElMessage.error(response.message || '导出失败')
    }
  } catch (error) {
    console.error('导出失败：', error)
    ElMessage.error('导出失败')
  }
}

const handleSelectionChange = (rows) => {
  selectedRows.value = rows
}

const handleSortChange = ({ prop, order }) => {
  sortParams.prop = prop
  sortParams.order = order
  loadData()
}

const handleSizeChange = (newSize) => {
  pagination.size = newSize
  pagination.page = 1
  loadData()
}

const handleCurrentChange = (newPage) => {
  pagination.page = newPage
  loadData()
}

const handleFormSuccess = () => {
  formDialogVisible.value = false
  loadData()
}

// 生命周期
onMounted(() => {
  loadData()
})
</script>

<style scoped>
.warehouse-management {
  padding: 20px;
}

.search-card, .operation-card, .table-card {
  margin-bottom: 20px;
}

.operation-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.operation-left .el-button {
  margin-right: 10px;
}

.operation-right .el-button {
  margin-left: 10px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.el-table .el-button + .el-button {
  margin-left: 0;
}
</style> 