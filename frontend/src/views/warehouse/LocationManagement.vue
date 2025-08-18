<template>
  <div class="location-management">
    <!-- 骨架屏 -->
    <TableSkeleton 
      v-if="initialLoading"
      :columns="[
        { width: '80px', titleWidth: '20px', cellWidth: '40px' },
        { width: '120px', titleWidth: '60px', cellWidth: '80px' },
        { width: '120px', titleWidth: '60px', cellWidth: '80px' },
        { width: '120px', titleWidth: '60px', cellWidth: '80px' },
        { width: '120px', titleWidth: '60px', cellWidth: '80px' },
        { width: '120px', titleWidth: '40px', cellWidth: '90px' },
        { width: '100px', titleWidth: '60px', cellWidth: '70px' },
        { width: '80px', titleWidth: '30px', cellWidth: '50px' },
        { width: '150px', titleWidth: '40px', cellWidth: '120px' },
        { width: '150px', titleWidth: '40px', cellWidth: '120px' },
        { width: '180px', titleWidth: '30px', cellWidth: '150px', variant: 'button' }
      ]"
      :rows="10"
      :search-items="4"
    />
    
    <!-- 实际内容 -->
    <el-card v-else>
      <!-- 搜索栏 -->
      <el-card class="search-card" shadow="never">
        <el-form :model="searchForm" inline>
          <el-form-item label="库位名称">
            <el-input 
              v-model="searchForm.locationName" 
              placeholder="请输入库位名称" 
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="库位编码">
            <el-input 
              v-model="searchForm.locationCode" 
              placeholder="请输入库位编码" 
              clearable
              style="width: 200px"
            />
          </el-form-item>
          <el-form-item label="仓库">
            <el-select
              v-model="searchForm.warehouseId"
              placeholder="请选择仓库"
              clearable
              filterable
              style="width: 200px"
            >
              <el-option
                v-for="warehouse in warehouseList"
                :key="warehouse.id"
                :label="warehouse.warehouseName"
                :value="warehouse.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="库区">
            <el-select
              v-model="searchForm.areaId"
              placeholder="请选择库区"
              clearable
              filterable
              style="width: 200px"
            >
              <el-option
                v-for="area in areaList"
                :key="area.id"
                :label="area.areaName"
                :value="area.id"
              />
            </el-select>
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
              v-if="hasActionPermission('warehouse-location-management:create')"
              type="primary"
              @click="handleAdd"
            >
              <el-icon><Plus /></el-icon>
              新增库位
            </el-button>
            <el-button
              v-if="hasActionPermission('warehouse-location-management:delete') && selectedRows.length > 0"
              type="danger"
              @click="handleBatchDelete"
            >
              <el-icon><Delete /></el-icon>
              批量删除
            </el-button>
            <el-button
              v-if="hasActionPermission('warehouse-location-management:change-status') && selectedRows.length > 0"
              type="success"
              @click="handleBatchEnable"
            >
              <el-icon><Check /></el-icon>
              批量启用
            </el-button>
            <el-button
              v-if="hasActionPermission('warehouse-location-management:change-status') && selectedRows.length > 0"
              type="warning"
              @click="handleBatchDisable"
            >
              <el-icon><Close /></el-icon>
              批量禁用
            </el-button>
          </div>
          <div class="operation-right">
            <el-button
              v-if="hasActionPermission('warehouse-location-management:export')"
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
          <el-table-column prop="locationName" label="库位名称" min-width="120" />
          <el-table-column prop="locationCode" label="库位编码" width="120" />
          <el-table-column label="所属仓库" min-width="120">
            <template #default="{ row }">
              {{ getWarehouseName(row.warehouseId) }}
            </template>
          </el-table-column>
          <el-table-column label="所属库区" min-width="120">
            <template #default="{ row }">
              {{ getAreaName(row.areaId) }}
            </template>
          </el-table-column>
          <el-table-column prop="sort" label="排序" width="80" />
          <el-table-column label="状态" width="100">
            <template #default="{ row }">
              <el-tag 
                :type="row.status === 1 ? 'success' : 'danger'"
                effect="light"
              >
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
          <el-table-column prop="createTime" label="创建时间" width="150" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="{ row }">
              <el-button
                v-if="hasActionPermission('warehouse-location-management:view')"
                size="small"
                type="primary"
                @click="handleView(row)"
              >
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button
                v-if="hasActionPermission('warehouse-location-management:edit')"
                size="small"
                type="success"
                @click="handleEdit(row)"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-dropdown 
                v-if="hasActionPermission('warehouse-location-management:delete') || hasActionPermission('warehouse-location-management:change-status')"
                trigger="click"
                @command="(command) => handleCommand(command, row)"
              >
                <el-button size="small" type="info">
                  <el-icon><MoreFilled /></el-icon>
                  更多
                  <el-icon class="el-icon--right"><ArrowDown /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item 
                      v-if="hasActionPermission('warehouse-location-management:change-status')"
                      :command="`${row.status === 1 ? 'disable' : 'enable'}-${row.id}`"
                    >
                      <el-icon><Switch /></el-icon>
                      {{ row.status === 1 ? '禁用' : '启用' }}
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="hasActionPermission('warehouse-location-management:delete')"
                      :command="`delete-${row.id}`"
                      style="color: #f56c6c"
                    >
                      <el-icon><Delete /></el-icon>
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </el-card>
    </el-card>

    <!-- 库位表单弹窗 -->
    <LocationForm
      v-if="formDialogVisible"
      :visible="formDialogVisible"
      :location-data="currentLocation"
      :is-edit="isEdit"
      @close="formDialogVisible = false"
      @success="handleFormSuccess"
    />

    <!-- 库位详情弹窗 -->
    <LocationDetail
      v-if="detailDialogVisible"
      :visible="detailDialogVisible"
      :location-data="currentLocation"
      @close="detailDialogVisible = false"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Plus, Search, Refresh, RefreshRight, Edit, Delete, View, 
  Check, Close, Download, Switch, MoreFilled, ArrowDown
} from '@element-plus/icons-vue'
import { locationApi } from '@/api/location'
import { warehouseApi } from '@/api/warehouse'
import { areaApi } from '@/api/area'
import { formatDateTime } from '@/utils/format'
import { hasActionPermission } from '@/utils/permission'

import TableSkeleton from '@/components/Skeleton/TableSkeleton.vue'
import LocationForm from './components/LocationForm.vue'
import LocationDetail from './components/LocationDetail.vue'

// 响应式数据
const initialLoading = ref(true)
const loading = ref(false)
const refreshing = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const formDialogVisible = ref(false)
const detailDialogVisible = ref(false)
const currentLocation = ref(null)
const isEdit = ref(false)

// 下拉选项数据
const warehouseList = ref([])
const areaList = ref([])
const allAreaList = ref([]) // 保存所有库区数据

// 搜索表单
const searchForm = reactive({
  locationName: '',
  locationCode: '',
  warehouseId: null,
  areaId: null,
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

// 计算属性
const filteredAreaList = computed(() => {
  if (!searchForm.warehouseId) {
    return allAreaList.value
  }
  return allAreaList.value.filter(area => area.warehouseId === searchForm.warehouseId)
})

// 监听仓库变化，重置库区选择
watch(() => searchForm.warehouseId, (newVal) => {
  if (!newVal) {
    searchForm.areaId = null
  } else {
    // 检查当前选择的库区是否属于新选择的仓库
    if (searchForm.areaId) {
      const selectedArea = allAreaList.value.find(area => area.id === searchForm.areaId)
      if (!selectedArea || selectedArea.warehouseId !== newVal) {
        searchForm.areaId = null
      }
    }
  }
  // 更新库区列表
  areaList.value = filteredAreaList.value
})

// 方法
const getWarehouseName = (warehouseId) => {
  const warehouse = warehouseList.value.find(w => w.id === warehouseId)
  return warehouse ? warehouse.warehouseName : '-'
}

const getAreaName = (areaId) => {
  if (!areaId) return '-'
  const area = allAreaList.value.find(a => a.id === areaId)
  return area ? area.areaName : '-'
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      ...searchForm,
      page: pagination.page - 1,
      size: pagination.size
    }
    
    // 添加排序参数
    if (sortParams.prop) {
      params.sort = `${sortParams.prop},${sortParams.order === 'ascending' ? 'asc' : 'desc'}`
    }
    
    const response = await locationApi.getPage(params)
    if (response.code === 200) {
      tableData.value = response.data
      pagination.total = response.total
    } else {
      ElMessage.error(response.message || '加载数据失败')
    }
  } catch (error) {
    console.error('加载库位数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
    initialLoading.value = false
  }
}

const loadWarehouses = async () => {
  try {
    const response = await warehouseApi.getActive()
    if (response.code === 200) {
      warehouseList.value = response.data
    }
  } catch (error) {
    console.error('加载仓库数据失败:', error)
  }
}

const loadAreas = async () => {
  try {
    const response = await areaApi.getActive()
    if (response.code === 200) {
      allAreaList.value = response.data
      areaList.value = response.data
    }
  } catch (error) {
    console.error('加载库区数据失败:', error)
  }
}

const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const resetSearch = () => {
  Object.assign(searchForm, {
    locationName: '',
    locationCode: '',
    warehouseId: null,
    areaId: null,
    status: null
  })
  handleSearch()
}

const handleRefresh = async () => {
  refreshing.value = true
  try {
    await loadData()
    ElMessage.success('刷新成功')
  } catch (error) {
    ElMessage.error('刷新失败')
  } finally {
    refreshing.value = false
  }
}

const handleAdd = () => {
  currentLocation.value = null
  isEdit.value = false
  formDialogVisible.value = true
}

const handleEdit = (row) => {
  currentLocation.value = { ...row }
  isEdit.value = true
  formDialogVisible.value = true
}

const handleView = (row) => {
  currentLocation.value = { ...row }
  detailDialogVisible.value = true
}

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个库位吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await locationApi.delete(id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      await loadData()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除库位失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的库位')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 个库位吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    const response = await locationApi.batchDelete(ids)
    if (response.code === 200) {
      ElMessage.success('批量删除成功')
      await loadData()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除库位失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

const handleEnable = async (id) => {
  try {
    const response = await locationApi.enable(id)
    if (response.code === 200) {
      ElMessage.success('启用成功')
      await loadData()
    } else {
      ElMessage.error(response.message || '启用失败')
    }
  } catch (error) {
    console.error('启用库位失败:', error)
    ElMessage.error('启用失败')
  }
}

const handleDisable = async (id) => {
  try {
    const response = await locationApi.disable(id)
    if (response.code === 200) {
      ElMessage.success('禁用成功')
      await loadData()
    } else {
      ElMessage.error(response.message || '禁用失败')
    }
  } catch (error) {
    console.error('禁用库位失败:', error)
    ElMessage.error('禁用失败')
  }
}

const handleBatchEnable = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要启用的库位')
    return
  }
  
  try {
    const ids = selectedRows.value.map(row => row.id)
    const response = await locationApi.batchEnable(ids)
    if (response.code === 200) {
      ElMessage.success('批量启用成功')
      await loadData()
    } else {
      ElMessage.error(response.message || '批量启用失败')
    }
  } catch (error) {
    console.error('批量启用库位失败:', error)
    ElMessage.error('批量启用失败')
  }
}

const handleBatchDisable = async () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要禁用的库位')
    return
  }
  
  try {
    const ids = selectedRows.value.map(row => row.id)
    const response = await locationApi.batchDisable(ids)
    if (response.code === 200) {
      ElMessage.success('批量禁用成功')
      await loadData()
    } else {
      ElMessage.error(response.message || '批量禁用失败')
    }
  } catch (error) {
    console.error('批量禁用库位失败:', error)
    ElMessage.error('批量禁用失败')
  }
}

const handleExport = async () => {
  try {
    const params = { ...searchForm }
    const response = await locationApi.export(params)
    if (response.code === 200) {
      ElMessage.success('导出成功')
      // 这里可以添加下载文件的逻辑
    } else {
      ElMessage.error(response.message || '导出失败')
    }
  } catch (error) {
    console.error('导出库位数据失败:', error)
    ElMessage.error('导出失败')
  }
}

const handleCommand = (command, row) => {
  const [action, id] = command.split('-')
  const locationId = parseInt(id)
  
  switch (action) {
    case 'enable':
      handleEnable(locationId)
      break
    case 'disable':
      handleDisable(locationId)
      break
    case 'delete':
      handleDelete(locationId)
      break
  }
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSortChange = ({ prop, order }) => {
  sortParams.prop = prop
  sortParams.order = order
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

const handleFormSuccess = () => {
  formDialogVisible.value = false
  loadData()
}

// 生命周期
onMounted(async () => {
  await Promise.all([
    loadWarehouses(),
    loadAreas(),
    loadData()
  ])
})
</script>

<style scoped>
.location-management {
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

.operation-left,
.operation-right {
  display: flex;
  gap: 10px;
}

:deep(.el-pagination) {
  margin-top: 20px;
  text-align: right;
}
</style> 