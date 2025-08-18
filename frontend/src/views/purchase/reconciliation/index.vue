<template>
  <div class="reconciliation-list">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>供应商对账管理</h2>
    </div>

    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="供应商">
          <el-select
            v-model="searchForm.supplierId"
            placeholder="请选择供应商"
            clearable
            filterable
            style="width: 200px"
          >
            <el-option
              v-for="supplier in supplierList"
              :key="supplier.id"
              :label="supplier.supplierName"
              :value="supplier.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="status in statusOptions"
              :key="status.value"
              :label="status.label"
              :value="status.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="对账单号">
          <el-input
            v-model="searchForm.reconciliationNo"
            placeholder="请输入对账单号"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 240px"
            @change="handleDateRangeChange"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="action-container">
      <el-button
        v-if="hasActionPermission('reconciliation-management:create')"
        type="primary"
        @click="handleCreate"
      >
        <el-icon><Plus /></el-icon>
        新增对账单
      </el-button>
      <el-button
        v-if="hasActionPermission('reconciliation-management:auto-generate')"
        type="success"
        @click="handleAutoGenerate"
      >
                        <el-icon><Tools /></el-icon>
        自动生成
      </el-button>
      <el-button
        v-if="hasActionPermission('reconciliation-management:confirm')"
        type="warning"
        :disabled="!hasSelection"
        @click="handleBatchConfirm"
      >
        <el-icon><Check /></el-icon>
        批量确认
      </el-button>
      <el-button
        v-if="hasActionPermission('reconciliation-management:delete')"
        type="danger"
        :disabled="!hasSelection"
        @click="handleBatchDelete"
      >
        <el-icon><Delete /></el-icon>
        批量删除
      </el-button>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        :data="reconciliationList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="50" />
        <el-table-column prop="reconciliationNo" label="对账单号" width="150" />
        <el-table-column prop="supplierName" label="供应商" width="150" />
        <el-table-column prop="startDate" label="开始日期" width="110" />
        <el-table-column prop="endDate" label="结束日期" width="110" />
        <el-table-column prop="totalAmount" label="总金额" width="120" align="right">
          <template #default="{ row }">
            <span class="amount-text">{{ formatCurrency(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="paidAmount" label="已付金额" width="120" align="right">
          <template #default="{ row }">
            <span class="amount-text">{{ formatCurrency(row.paidAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="unpaidAmount" label="未付金额" width="120" align="right">
          <template #default="{ row }">
            <span class="amount-text text-danger">{{ formatCurrency(row.unpaidAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
        <el-table-column label="操作" width="220" align="center" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="hasActionPermission('reconciliation-management:view')"
              size="small"
              @click="handleView(row)"
            >
              查看
            </el-button>
            <el-button
              v-if="hasActionPermission('reconciliation-management:edit') && canEdit(row.status)"
              size="small"
              type="primary"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="hasActionPermission('reconciliation-management:confirm') && canConfirm(row.status)"
              size="small"
              type="success"
              @click="handleConfirm(row)"
            >
              确认
            </el-button>
            <el-button
              v-if="hasActionPermission('reconciliation-management:settle') && canSettle(row.status)"
              size="small"
              type="warning"
              @click="handleSettle(row)"
            >
              结算
            </el-button>
            <el-button
              v-if="hasActionPermission('reconciliation-management:delete') && canDelete(row.status)"
              size="small"
              type="danger"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页组件 -->
    <div class="pagination-container">
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

    <!-- 自动生成对话框 -->
    <el-dialog
      v-model="autoGenerateDialogVisible"
      title="自动生成对账单"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="autoGenerateFormRef"
        :model="autoGenerateForm"
        :rules="autoGenerateRules"
        label-width="120px"
      >
        <el-form-item label="供应商" prop="supplierId">
          <el-select
            v-model="autoGenerateForm.supplierId"
            placeholder="请选择供应商"
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="supplier in supplierList"
              :key="supplier.id"
              :label="supplier.supplierName"
              :value="supplier.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
            v-model="autoGenerateForm.startDate"
            type="date"
            placeholder="请选择开始日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束日期" prop="endDate">
          <el-date-picker
            v-model="autoGenerateForm.endDate"
            type="date"
            placeholder="请选择结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="autoGenerateDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="autoGenerateLoading" @click="handleAutoGenerateConfirm">
            生成
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search, Refresh, Plus, Tools, Check, Delete
} from '@element-plus/icons-vue'
import { hasActionPermission } from '@/utils/permission'
import { reconciliationApi } from '@/api/reconciliation'
import { supplierApi } from '@/api/supplier'
import {
  RECONCILIATION_STATUS,
  STATUS_OPTIONS,
  getDefaultSearchForm,
  formatCurrency,
  getStatusText,
  getStatusTagType,
  canEdit,
  canConfirm,
  canSettle,
  canDelete,
  hasSelection as checkHasSelection,
  getSelectedIds
} from '@/utils/reconciliation'

// 路由
const router = useRouter()

// 响应式数据
const loading = ref(false)
const reconciliationList = ref([])
const supplierList = ref([])
const selectedRows = ref([])
const dateRange = ref([])

// 搜索表单
const searchForm = reactive(getDefaultSearchForm())

// 分页信息
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 状态选项
const statusOptions = STATUS_OPTIONS

// 自动生成对话框
const autoGenerateDialogVisible = ref(false)
const autoGenerateLoading = ref(false)
const autoGenerateFormRef = ref()
const autoGenerateForm = reactive({
  supplierId: null,
  startDate: null,
  endDate: null
})

// 自动生成表单验证规则
const autoGenerateRules = {
  supplierId: [
    { required: true, message: '请选择供应商', trigger: 'change' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: '请选择结束日期', trigger: 'change' }
  ]
}

// 计算属性
const hasSelection = computed(() => checkHasSelection(selectedRows.value))

// 页面加载时执行
onMounted(() => {
  loadData()
  loadSuppliers()
})

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    const params = {
      ...searchForm,
      page: pagination.page - 1, // 后端页码从0开始
      size: pagination.size
    }

    const response = await reconciliationApi.getReconciliationPage(params)
    if (response.code === 200) {
      reconciliationList.value = response.data || []
      pagination.total = response.total || 0
    } else {
      ElMessage.error(response.message || '查询失败')
    }
  } catch (error) {
    console.error('加载对账单列表失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 加载供应商列表
const loadSuppliers = async () => {
  try {
    const response = await supplierApi.getAllActiveSuppliers()
    if (response.success) {
      supplierList.value = response.data || []
    }
  } catch (error) {
    console.error('加载供应商列表失败:', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, getDefaultSearchForm())
  dateRange.value = []
  pagination.page = 1
  loadData()
}

// 日期范围变化
const handleDateRangeChange = (value) => {
  if (value && value.length === 2) {
    searchForm.startDate = value[0]
    searchForm.endDate = value[1]
  } else {
    searchForm.startDate = null
    searchForm.endDate = null
  }
}

// 分页大小变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadData()
}

// 当前页变化
const handleCurrentChange = (page) => {
  pagination.page = page
  loadData()
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 新增对账单
const handleCreate = () => {
  router.push({ name: 'purchase-reconciliation-create' })
}

// 查看对账单
const handleView = (row) => {
  router.push({ 
    name: 'purchase-reconciliation-view', 
    params: { id: row.id } 
  })
}

// 编辑对账单
const handleEdit = (row) => {
  router.push({ 
    name: 'purchase-reconciliation-edit', 
    params: { id: row.id } 
  })
}

// 删除对账单
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除对账单"${row.reconciliationNo}"吗？`,
      '删除确认',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
    )

    const response = await reconciliationApi.deleteReconciliation(row.id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      loadData()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除对账单失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 确认对账单
const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要确认对账单"${row.reconciliationNo}"吗？`,
      '确认提示',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
    )

    const response = await reconciliationApi.confirmReconciliation(row.id)
    if (response.code === 200) {
      ElMessage.success('确认成功')
      loadData()
    } else {
      ElMessage.error(response.message || '确认失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('确认对账单失败:', error)
      ElMessage.error('确认失败')
    }
  }
}

// 结算对账单
const handleSettle = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要结算对账单"${row.reconciliationNo}"吗？`,
      '结算确认',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
    )

    const response = await reconciliationApi.settleReconciliation(row.id)
    if (response.code === 200) {
      ElMessage.success('结算成功')
      loadData()
    } else {
      ElMessage.error(response.message || '结算失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('结算对账单失败:', error)
      ElMessage.error('结算失败')
    }
  }
}

// 自动生成对账单
const handleAutoGenerate = () => {
  // 重置表单
  Object.assign(autoGenerateForm, {
    supplierId: null,
    startDate: null,
    endDate: null
  })
  autoGenerateDialogVisible.value = true
  
  nextTick(() => {
    autoGenerateFormRef.value?.clearValidate()
  })
}

// 确认自动生成
const handleAutoGenerateConfirm = async () => {
  try {
    const valid = await autoGenerateFormRef.value.validate()
    if (!valid) return

    autoGenerateLoading.value = true
    const response = await reconciliationApi.autoGenerateReconciliation(autoGenerateForm)
    
    if (response.code === 200) {
      ElMessage.success('自动生成成功')
      autoGenerateDialogVisible.value = false
      loadData()
    } else {
      ElMessage.error(response.message || '自动生成失败')
    }
  } catch (error) {
    console.error('自动生成对账单失败:', error)
    ElMessage.error('自动生成失败')
  } finally {
    autoGenerateLoading.value = false
  }
}

// 批量确认
const handleBatchConfirm = async () => {
  try {
    const ids = getSelectedIds(selectedRows.value)
    if (ids.length === 0) {
      ElMessage.warning('请选择要确认的对账单')
      return
    }

    await ElMessageBox.confirm(
      `确定要批量确认选中的 ${ids.length} 个对账单吗？`,
      '批量确认',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
    )

    const response = await reconciliationApi.batchConfirmReconciliations(ids)
    if (response.code === 200) {
      ElMessage.success('批量确认成功')
      loadData()
    } else {
      ElMessage.error(response.message || '批量确认失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量确认失败:', error)
      ElMessage.error('批量确认失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    const ids = getSelectedIds(selectedRows.value)
    if (ids.length === 0) {
      ElMessage.warning('请选择要删除的对账单')
      return
    }

    await ElMessageBox.confirm(
      `确定要批量删除选中的 ${ids.length} 个对账单吗？`,
      '批量删除',
      {
        type: 'warning',
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }
    )

    const response = await reconciliationApi.batchDeleteReconciliations(ids)
    if (response.code === 200) {
      ElMessage.success('批量删除成功')
      loadData()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}
</script>

<style scoped>
.reconciliation-list {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #303133;
  font-size: 20px;
  font-weight: 500;
}

.search-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.action-container {
  background: #fff;
  padding: 16px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.amount-text {
  font-weight: 500;
}

.text-danger {
  color: #f56c6c;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-table) {
  border-radius: 0;
}

:deep(.el-table__header) {
  background-color: #fafafa;
}

:deep(.el-form-item) {
  margin-bottom: 18px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style> 