<template>
  <div class="supplier-goods-list">
    <!-- 骨架屏 -->
    <TableSkeleton 
      v-if="initialLoading"
      :columns="[
        { width: '55px', titleWidth: '30px', cellWidth: '40px', variant: 'checkbox' },
        { width: '140px', titleWidth: '80px', cellWidth: '120px' },
        { width: '160px', titleWidth: '80px', cellWidth: '140px' },
        { width: '140px', titleWidth: '100px', cellWidth: '120px' },
        { width: '140px', titleWidth: '100px', cellWidth: '120px' },
        { width: '100px', titleWidth: '60px', cellWidth: '80px' },
        { width: '100px', titleWidth: '70px', cellWidth: '80px' },
        { width: '100px', titleWidth: '60px', cellWidth: '80px' },
        { width: '160px', titleWidth: '60px', cellWidth: '140px' },
        { width: '180px', titleWidth: '30px', cellWidth: '150px', variant: 'button' }
      ]"
      :rows="10"
      :search-items="3"
    />
    
    <!-- 实际内容 -->
    <el-card v-else>
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon class="header-icon">
              <Connection />
            </el-icon>
            <span class="header-title">供应商商品管理</span>
          </div>
          <el-button 
            v-if="hasActionPermission('supplier-goods-management:create')"
            type="primary" 
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增供应商商品
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form
          :inline="true"
          :model="searchParams"
          class="search-form"
        >
          <el-form-item label="供应商">
            <el-select
              v-model="searchParams.supplierId"
              placeholder="请选择供应商"
              clearable
              filterable
              style="width: 200px;"
              @change="handleSupplierChange"
            >
              <template #prefix>
                <el-icon><Shop /></el-icon>
              </template>
              <el-option
                v-for="supplier in supplierList"
                :key="supplier.id"
                :label="supplier.supplierName"
                :value="supplier.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="商品">
            <el-select
              v-model="selectedGoodsId"
              placeholder="请输入搜索商品"
              filterable
              remote
              :remote-method="searchGoods"
              :loading="goodsSearchLoading"
              clearable
              style="width: 200px;"
              @change="handleGoodsChange"
            >
              <template #prefix>
                <el-icon><Grid /></el-icon>
              </template>
              <el-option
                v-for="goods in goodsList"
                :key="goods.id"
                :label="`${goods.name} (${goods.sku})`"
                :value="goods.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="fetchData" :loading="loading">
              <el-icon><Search /></el-icon>
              查询
            </el-button>
            <el-button @click="resetSearch">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
            <el-button 
              v-if="hasActionPermission('supplier-goods-management:compare')"
              type="warning" 
              @click="handlePriceCompare"
              :disabled="!selectedGoodsId"
            >
              <el-icon><TrendCharts /></el-icon>
              价格比较
            </el-button>
            <el-button 
              v-if="hasActionPermission('supplier-goods-management:export')"
              type="success" 
              @click="handleExport"
              :loading="exportLoading"
            >
              <el-icon><Download /></el-icon>
              导出
            </el-button>
            <el-button 
              v-if="hasActionPermission('supplier-goods-management:import')"
              type="info" 
              @click="handleImport"
            >
              <el-icon><Upload /></el-icon>
              导入
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column label="供应商信息" width="140">
          <template #default="{ row }">
            <div>
              <div class="text-primary">{{ row.supplierName || '-' }}</div>
              <div class="text-muted text-sm">{{ row.supplierCode || '-' }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="商品信息" width="160">
          <template #default="{ row }">
            <div>
              <div class="text-primary">{{ row.goodsName || '-' }}</div>
              <div class="text-muted text-sm">SKU: {{ row.goodsCode || '-' }}</div>
            </div>
          </template>
        </el-table-column>

        <el-table-column label="供应商商品编码" prop="supplierGoodsCode" width="140" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.supplierGoodsCode || '-' }}
          </template>
        </el-table-column>

        <el-table-column label="供应商商品名称" prop="supplierGoodsName" width="140" show-overflow-tooltip>
          <template #default="{ row }">
            {{ row.supplierGoodsName || '-' }}
          </template>
        </el-table-column>

        <el-table-column label="采购价格" width="100" align="right">
          <template #default="{ row }">
            <span class="price">{{ row.purchasePrice ? `¥${row.purchasePrice}` : '-' }}</span>
          </template>
        </el-table-column>

                <el-table-column label="最小起订量" width="100" align="right">
          <template #default="{ row }">
            <span class="quantity">{{ row.minPurchaseQty || '-' }}</span>
          </template>
        </el-table-column>

        <el-table-column label="供货周期" width="100" align="center">
          <template #default="{ row }">
            <el-tag 
              v-if="row.deliveryDay"
              :type="getDeliveryDayType(row.deliveryDay)"
              size="small"
            >
              {{ row.deliveryDay }}天
            </el-tag>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>

        <el-table-column label="更新时间" width="160">
          <template #default="{ row }">
            <div class="time-cell">
              <el-icon><Clock /></el-icon>
              <span>{{ formatDateTime(row.updatedAt) }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button 
              v-if="hasActionPermission('supplier-goods-management:update')"
              type="primary" 
              link 
              size="small"
              @click="handleEdit(row)"
            >
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button 
              v-if="hasActionPermission('supplier-goods-management:delete')"
              type="danger" 
              link 
              size="small"
              @click="handleDelete(row)"
            >
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
            <el-button 
              v-if="hasActionPermission('supplier-goods-management:view-history')"
              type="info" 
              link 
              size="small"
              @click="handleHistory(row)"
            >
              <el-icon><Clock /></el-icon>
              历史
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </el-card>

    <!-- 表单对话框 -->
    <SupplierGoodsForm
      v-model:visible="formVisible"
      :is-edit="isEdit"
      :supplier-list="supplierList"
      @success="handleFormSuccess"
    />

    <!-- 价格比较对话框 -->
    <SupplierGoodsCompare
      v-model:visible="compareVisible"
      :goods-id="selectedGoodsId"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Connection, Plus, Search, Refresh, TrendCharts, Grid, Money, Clock, Edit, Delete, Document, Shop, Download, Upload
} from '@element-plus/icons-vue'
import { supplierGoodsApi } from '@/api/supplierGoods'
import { supplierApi } from '@/api/supplier'
import { goodsApi } from '@/api/goods'
import { hasActionPermission } from '@/utils/permission'
import { formatDateTime } from '@/utils/date'
import TableSkeleton from '@/components/Skeleton/TableSkeleton.vue'
import SupplierGoodsForm from './components/SupplierGoodsForm.vue'
import SupplierGoodsCompare from './components/SupplierGoodsCompare.vue'

// 数据状态
const initialLoading = ref(true)
const loading = ref(false)
const goodsSearchLoading = ref(false)
const exportLoading = ref(false)
const tableData = ref([])
const supplierList = ref([])
const goodsList = ref([])
const selectedGoodsId = ref(null)
const selectedRows = ref([])

// 搜索参数
const searchParams = reactive({
  supplierId: null,
  goodsId: null
})

// 分页
const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

// 表单状态
const formVisible = ref(false)
const formData = ref({})
const isEdit = ref(false)

// 价格比较状态
const compareVisible = ref(false)

// 获取供应商列表
const fetchSuppliers = async () => {
  try {
    const response = await supplierApi.getAllActiveSuppliers()
    if (response.success && response.data) {
      supplierList.value = response.data
    } else {
      supplierList.value = []
      ElMessage.error(response.message || '获取供应商列表失败')
    }
  } catch (error) {
    console.error('获取供应商列表失败:', error)
    ElMessage.error('获取供应商列表失败')
  }
}

// 搜索商品
const searchGoods = async (query) => {
  if (!query) {
    goodsList.value = []
    return
  }
  
  goodsSearchLoading.value = true
  try {
    const response = await supplierGoodsApi.search({
      goodsName: query,  // 修改：使用goodsName而不是keyword
      supplierId: null   // 不限制供应商
    })
    // 提取商品信息，避免重复
    const uniqueGoods = new Map()
    if (response.data) {
      response.data.forEach(item => {
        if (!uniqueGoods.has(item.goodsId)) {
          uniqueGoods.set(item.goodsId, {
            id: item.goodsId,
            name: item.goodsName,
            sku: item.skuCode || item.goodsCode
          })
        }
      })
    }
    goodsList.value = Array.from(uniqueGoods.values())
  } catch (error) {
    console.error('搜索商品失败:', error)
    ElMessage.error('搜索商品失败')
  } finally {
    goodsSearchLoading.value = false
  }
}

// 获取供应商商品列表
const fetchData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page - 1, // 前端页码从1开始，后端从0开始
      size: pagination.size,
      ...searchParams
    }
    
    // 清理空值参数
    Object.keys(params).forEach(key => {
      if (params[key] === null || params[key] === '') {
        delete params[key]
      }
    })
    
    const response = await supplierGoodsApi.list(params)
    tableData.value = response.data.content || []
    pagination.total = response.data.totalElements || 0
  } catch (error) {
    console.error('获取供应商商品列表失败:', error)
    ElMessage.error('获取供应商商品列表失败')
  } finally {
    loading.value = false
    initialLoading.value = false
  }
}

// 供应商变化处理
const handleSupplierChange = (supplierId) => {
  searchParams.supplierId = supplierId
  fetchData()
}

// 商品变化处理
const handleGoodsChange = (goodsId) => {
  searchParams.goodsId = goodsId
  selectedGoodsId.value = goodsId
  fetchData()
}

// 重置搜索
const resetSearch = () => {
  Object.keys(searchParams).forEach(key => {
    searchParams[key] = null
  })
  selectedGoodsId.value = null
  pagination.page = 1
  fetchData()
}

// 新增
const handleAdd = () => {
  formData.value = {}
  isEdit.value = false
  formVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  formData.value = { ...row }
  isEdit.value = true
  formVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除供应商"${row.supplierName}"的商品"${row.goodsName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await supplierGoodsApi.delete(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error(error.message || '删除失败')
    }
  }
}

// 价格比较
const handlePriceCompare = () => {
  if (!selectedGoodsId.value) {
    ElMessage.warning('请先选择要比较的商品')
    return
  }
  compareVisible.value = true
}

// 查看历史
const handleHistory = (row) => {
  // TODO: 实现历史记录查看功能
  ElMessage.info('历史记录功能开发中...')
}

// 多选处理
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 导出
const handleExport = async () => {
  try {
    exportLoading.value = true
    
    // 构建导出参数
    const params = {
      supplierId: searchParams.supplierId || null,
      goodsId: selectedGoodsId.value || null
    }
    
    const response = await supplierGoodsApi.export(params)
    
    // 创建下载链接
    const blob = new Blob([response], { 
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `供应商商品关联信息_${new Date().toISOString().slice(0, 10)}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('导出成功')
  } catch (error) {
    console.error('导出失败:', error)
    ElMessage.error(error.message || '导出失败')
  } finally {
    exportLoading.value = false
  }
}

// 导入
const handleImport = () => {
  // 创建文件选择器
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = '.xlsx,.xls'
  input.onchange = async (event) => {
    const file = event.target.files[0]
    if (!file) return
    
    try {
      const formData = new FormData()
      formData.append('file', file)
      
      const result = await supplierGoodsApi.import(formData)
      
      ElMessage.success(`导入完成！成功：${result.success}条，失败：${result.failed}条`)
      
      if (result.errors && result.errors.length > 0) {
        console.warn('导入错误信息:', result.errors)
      }
      
      fetchData()
    } catch (error) {
      console.error('导入失败:', error)
      ElMessage.error(error.message || '导入失败')
    }
  }
  input.click()
}

// 表单提交成功
const handleFormSuccess = () => {
  formVisible.value = false
  fetchData()
}

// 获取供货周期标签类型
const getDeliveryDayType = (days) => {
  if (!days) return 'info'
  const dayNum = parseInt(days)
  if (dayNum <= 3) return 'success'    // 绿色：3天内
  if (dayNum <= 7) return 'warning'    // 橙色：7天内
  if (dayNum <= 15) return 'danger'    // 红色：15天内
  return 'info'                        // 灰色：15天以上
}

// 初始化
onMounted(async () => {
  await Promise.all([
    fetchSuppliers(),
    fetchData()
  ])
})
</script>

<style scoped>
.supplier-goods-list {
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

.text-muted {
  color: #909399;
}

.text-primary {
  color: #303133;
  font-weight: 500;
}

.text-sm {
  font-size: 12px;
}

.price {
  font-weight: bold;
  color: #e6a23c;
}

.quantity {
  font-weight: 500;
  color: #606266;
}

.time-cell {
  display: flex;
  align-items: center;
  font-size: 14px;
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
</style>
