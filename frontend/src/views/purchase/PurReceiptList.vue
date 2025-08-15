<template>
  <div class="pur-receipt-list">
    <!-- 搜索区域 -->
    <el-card
      class="search-card"
      shadow="never"
    >
      <div class="search-header">
        <h3 class="search-title">
          <el-icon><Search /></el-icon>
          搜索条件
        </h3>
        <div class="search-actions">
          <el-button
            type="primary"
            @click="handleSearch"
          >
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </div>
      </div>
      
      <el-form
        :model="searchForm"
        class="search-form"
      >
        <!-- 第一行 -->
        <el-row
          :gutter="20"
          class="search-row"
        >
          <el-col
            :xl="6"
            :lg="8"
            :md="12"
            :sm="24"
            :xs="24"
          >
            <el-form-item
              label="入库单号"
              class="form-item"
            >
              <el-input
                v-model="searchForm.receiptNo"
                placeholder="请输入入库单号"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col
            :xl="6"
            :lg="8"
            :md="12"
            :sm="24"
            :xs="24"
          >
            <el-form-item
              label="采购单号"
              class="form-item"
            >
              <el-input
                v-model="searchForm.orderNo"
                placeholder="请输入采购单号"
                clearable
                @keyup.enter="handleSearch"
              />
            </el-form-item>
          </el-col>
          <el-col
            :xl="6"
            :lg="8"
            :md="12"
            :sm="24"
            :xs="24"
          >
            <el-form-item
              label="供应商"
              class="form-item"
            >
              <el-select
                v-model="searchForm.supplierId"
                placeholder="请选择供应商"
                clearable
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="supplier in supplierOptions"
                  :key="supplier.id"
                  :label="supplier.supplierName"
                  :value="supplier.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col
            :xl="6"
            :lg="8"
            :md="12"
            :sm="24"
            :xs="24"
          >
            <el-form-item
              label="仓库"
              class="form-item"
            >
              <el-select
                v-model="searchForm.warehouseId"
                placeholder="请选择仓库"
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="warehouse in warehouseOptions"
                  :key="warehouse.id"
                  :label="warehouse.warehouseName"
                  :value="warehouse.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <!-- 第二行 -->
        <el-row
          :gutter="20"
          class="search-row"
        >
          <el-col
            :xl="6"
            :lg="8"
            :md="12"
            :sm="24"
            :xs="24"
          >
            <el-form-item
              label="入库状态"
              class="form-item"
            >
              <el-select
                v-model="searchForm.receiptStatus"
                placeholder="请选择状态"
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="status in receiptStatusOptions"
                  :key="status.value"
                  :label="status.label"
                  :value="status.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col
            :xl="6"
            :lg="8"
            :md="12"
            :sm="24"
            :xs="24"
          >
            <el-form-item
              label="入库类型"
              class="form-item"
            >
              <el-select
                v-model="searchForm.receiptType"
                placeholder="请选择类型"
                clearable
                style="width: 100%"
              >
                <el-option
                  v-for="type in receiptTypeOptions"
                  :key="type.value"
                  :label="type.label"
                  :value="type.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col
            :xl="6"
            :lg="8"
            :md="12"
            :sm="24"
            :xs="24"
          >
            <el-form-item
              label="入库时间"
              class="form-item"
            >
              <el-date-picker
                v-model="searchForm.receiptTimeRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col
            :xl="6"
            :lg="8"
            :md="12"
            :sm="24"
            :xs="24"
          >
            <el-form-item
              label="创建时间"
              class="form-item"
            >
              <el-date-picker
                v-model="searchForm.createTimeRange"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 操作区域 -->
    <el-card
      class="toolbar-card"
      shadow="never"
    >
      <div class="toolbar">
        <div class="toolbar-left">
          <el-button 
            v-if="hasActionPermission('pur-receipt-management:create')" 
            type="primary"
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增入库单
          </el-button>
          <el-button 
            v-if="hasActionPermission('pur-receipt-management:delete')" 
            type="danger"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
          <el-button 
            v-if="hasActionPermission('pur-receipt-management:confirm')" 
            type="success"
            :disabled="selectedRows.length === 0"
            @click="handleBatchConfirm"
          >
            <el-icon><Check /></el-icon>
            批量确认入库
          </el-button>
          <el-button 
            v-if="hasActionPermission('pur-receipt-management:audit')" 
            type="warning"
            :disabled="selectedRows.length === 0"
            @click="handleBatchAudit"
          >
            <el-icon><EditPen /></el-icon>
            批量审核
          </el-button>
          <el-button 
            v-if="hasActionPermission('pur-receipt-management:export')" 
            type="info"
            @click="handleExport"
          >
            <el-icon><Download /></el-icon>
            导出
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button
            circle
            @click="loadData"
          >
            <el-icon><Refresh /></el-icon>
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
        :data="tableData"
        row-key="id"
        style="width: 100%"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column
          type="selection"
          width="55"
        />
        <el-table-column
          prop="receiptNo"
          label="入库单号"
          width="160"
          fixed="left"
        >
          <template #default="{ row }">
            <el-link
              type="primary"
              @click="handleView(row)"
            >
              {{ row.receiptNo }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column
          prop="orderNo"
          label="采购单号"
          width="160"
        >
          <template #default="{ row }">
            <el-link
              v-if="row.orderNo"
              type="primary"
              @click="viewPurchaseOrder(row)"
            >
              {{ row.orderNo }}
            </el-link>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="supplierName"
          label="供应商"
          width="180"
          show-overflow-tooltip
        />
        <el-table-column
          prop="warehouseName"
          label="仓库"
          width="120"
        />
        <el-table-column
          prop="receiptStatusName"
          label="入库状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag
              :type="getStatusTagType(row.receiptStatus)"
              size="small"
            >
              {{ row.receiptStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="receiptTypeName"
          label="入库类型"
          width="100"
        >
          <template #default="{ row }">
            <el-tag
              type="info"
              size="small"
            >
              {{ row.receiptTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="receiptTime"
          label="入库日期"
          width="120"
          sortable="custom"
        />
        <el-table-column
          prop="totalAmount"
          label="入库总金额"
          width="120"
          align="right"
        >
          <template #default="{ row }">
            ¥{{ (row.totalAmount || 0).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column
          prop="itemCount"
          label="明细数量"
          width="100"
          align="center"
        />
        <el-table-column
          prop="receiptUserName"
          label="入库人"
          width="100"
        />
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
        />
        <el-table-column
          prop="createByName"
          label="创建人"
          width="100"
        />
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="160"
          sortable="custom"
        />
        <el-table-column
          prop="auditName"
          label="审核人"
          width="100"
        />
        <el-table-column
          prop="auditTime"
          label="审核时间"
          width="160"
        />
        <el-table-column
          label="操作"
          width="280"
          fixed="right"
        >
          <template #default="{ row }">
            <el-button
              v-if="hasActionPermission('pur-receipt-management:view')"
              type="primary"
              link
              size="small"
              @click="handleView(row)"
            >
              查看
            </el-button>
            <el-button
              v-if="row.receiptStatus === 1 && hasActionPermission('pur-receipt-management:edit')"
              type="primary"
              link
              size="small"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="row.receiptStatus === 1 && hasActionPermission('pur-receipt-management:submit')"
              type="warning"
              link
              size="small"
              @click="handleSubmit(row)"
            >
              提交
            </el-button>
            <el-button
              v-if="row.receiptStatus === 2 && hasActionPermission('pur-receipt-management:audit')"
              type="success"
              link
              size="small"
              @click="handleAudit(row, true)"
            >
              审核通过
            </el-button>
            <el-button
              v-if="row.receiptStatus === 2 && hasActionPermission('pur-receipt-management:audit')"
              type="danger"
              link
              size="small"
              @click="handleAudit(row, false)"
            >
              审核拒绝
            </el-button>
            <el-button
              v-if="row.receiptStatus === 3 && hasActionPermission('pur-receipt-management:confirm')"
              type="success"
              link
              size="small"
              @click="handleConfirm(row)"
            >
              确认入库
            </el-button>
            <el-button
              v-if="hasActionPermission('pur-receipt-management:copy')"
              type="info"
              link
              size="small"
              @click="handleCopy(row)"
            >
              复制
            </el-button>
            <el-button
              v-if="hasActionPermission('pur-receipt-management:print')"
              type="info"
              link
              size="small"
              @click="handlePrint(row)"
            >
              打印
            </el-button>
            <el-dropdown
              v-if="hasMoreActions(row)"
              @command="handleCommand"
            >
              <el-button
                type="primary"
                link
                size="small"
              >
                更多<el-icon class="el-icon--right">
                  <arrow-down />
                </el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item 
                    v-if="hasActionPermission('pur-receipt-management:cancel')"
                    command="cancel"
                    :disabled="!canCancel(row)"
                  >
                    取消
                  </el-dropdown-item>
                  <el-dropdown-item 
                    v-if="hasActionPermission('pur-receipt-management:delete')"
                    command="delete"
                    :disabled="!canDelete(row)"
                  >
                    删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="auditDialogVisible"
      :title="auditForm.approved ? '审核通过' : '审核拒绝'"
      width="500px"
    >
      <el-form
        ref="auditFormRef"
        :model="auditForm"
        :rules="auditRules"
        label-width="80px"
      >
        <el-form-item
          label="审核备注"
          prop="auditRemark"
        >
          <el-input
            v-model="auditForm.auditRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入审核备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="auditDialogVisible = false">
            取消
          </el-button>
          <el-button
            type="primary"
            @click="submitAudit"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 取消对话框 -->
    <el-dialog
      v-model="cancelDialogVisible"
      title="取消入库单"
      width="500px"
    >
      <el-form
        ref="cancelFormRef"
        :model="cancelForm"
        :rules="cancelRules"
        label-width="80px"
      >
        <el-form-item
          label="取消原因"
          prop="reason"
        >
          <el-input
            v-model="cancelForm.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入取消原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelDialogVisible = false">
            取消
          </el-button>
          <el-button
            type="primary"
            @click="submitCancel"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search,
  Refresh,
  Plus,
  Delete,
  Check,
  EditPen,
  Download,
  ArrowDown
} from '@element-plus/icons-vue'
import { purReceiptApi } from '@/api/purchase/purReceipt'
import { supplierApi } from '@/api/supplier'
import { warehouseApi } from '@/api/warehouse'
import { hasActionPermission } from '@/utils/permission'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const selectedRows = ref([])
const supplierOptions = ref([])
const warehouseOptions = ref([])
const receiptStatusOptions = ref([])
const receiptTypeOptions = ref([])

// 搜索表单
const searchForm = reactive({
  receiptNo: '',
  orderNo: '',
  supplierId: null,
  warehouseId: null,
  receiptStatus: null,
  receiptType: null,
  receiptTimeRange: null,
  createTimeRange: null
})

// 分页数据
const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

// 排序数据
const sortData = reactive({
  prop: '',
  order: ''
})

// 审核对话框
const auditDialogVisible = ref(false)
const auditForm = reactive({
  id: null,
  approved: true,
  auditRemark: ''
})
const auditRules = {
  auditRemark: [
    { required: true, message: '请输入审核备注', trigger: 'blur' }
  ]
}
const auditFormRef = ref()

// 取消对话框
const cancelDialogVisible = ref(false)
const cancelForm = reactive({
  id: null,
  reason: ''
})
const cancelRules = {
  reason: [
    { required: true, message: '请输入取消原因', trigger: 'blur' }
  ]
}
const cancelFormRef = ref()

// 生命周期
onMounted(() => {
  loadBaseData()
  loadData()
})

// 方法
const loadBaseData = async () => {
  try {
    // 加载供应商选项
    const supplierRes = await supplierApi.getList()
    supplierOptions.value = supplierRes.data || []
    
    // 加载仓库选项
    const warehouseRes = await warehouseApi.getList()
    warehouseOptions.value = warehouseRes.data || []
    
    // 加载状态选项
    receiptStatusOptions.value = purReceiptApi.getReceiptStatusOptions()
    receiptTypeOptions.value = purReceiptApi.getReceiptTypeOptions()
  } catch (error) {
    console.error('加载基础数据失败:', error)
  }
}

const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.page - 1,
      size: pagination.size,
      ...searchForm
    }
    
    // 处理时间范围
    if (searchForm.receiptTimeRange) {
      params.startTime = searchForm.receiptTimeRange[0]
      params.endTime = searchForm.receiptTimeRange[1]
    }
    if (searchForm.createTimeRange) {
      params.createStartTime = searchForm.createTimeRange[0]
      params.createEndTime = searchForm.createTimeRange[1]
    }
    
    // 处理排序
    if (sortData.prop) {
      params.sortBy = sortData.prop
      params.sortOrder = sortData.order === 'ascending' ? 'asc' : 'desc'
    }
    
    const response = await purReceiptApi.getPurReceiptPage(params)
    const data = response.data
    
    tableData.value = data.content || []
    pagination.total = data.totalElements || 0
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 搜索相关
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

const handleReset = () => {
  Object.assign(searchForm, {
    receiptNo: '',
    orderNo: '',
    supplierId: null,
    warehouseId: null,
    receiptStatus: null,
    receiptType: null,
    receiptTimeRange: null,
    createTimeRange: null
  })
  handleSearch()
}

// 分页相关
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadData()
}

const handleCurrentChange = (page) => {
  pagination.page = page
  loadData()
}

// 排序相关
const handleSortChange = ({ prop, order }) => {
  sortData.prop = prop
  sortData.order = order
  loadData()
}

// 选择相关
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 操作相关
const handleAdd = () => {
  router.push('/purchase/receipt/create')
}

const handleView = (row) => {
  router.push(`/purchase/receipt/view/${row.id}`)
}

const handleEdit = (row) => {
  router.push(`/purchase/receipt/edit/${row.id}`)
}

const handleSubmit = async (row) => {
  try {
    await ElMessageBox.confirm('确认提交该入库单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await purReceiptApi.submitPurReceipt(row.id)
    ElMessage.success('提交成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('提交失败')
    }
  }
}

const handleAudit = (row, approved) => {
  auditForm.id = row.id
  auditForm.approved = approved
  auditForm.auditRemark = ''
  auditDialogVisible.value = true
  
  nextTick(() => {
    auditFormRef.value?.clearValidate()
  })
}

const submitAudit = async () => {
  try {
    await auditFormRef.value?.validate()
    
    await purReceiptApi.auditPurReceipt(auditForm.id, {
      approved: auditForm.approved,
      auditRemark: auditForm.auditRemark
    })
    
    ElMessage.success('审核成功')
    auditDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('审核失败:', error)
    if (error !== false) {
      ElMessage.error('审核失败')
    }
  }
}

const handleConfirm = async (row) => {
  try {
    await ElMessageBox.confirm('确认该入库单入库吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await purReceiptApi.confirmReceipt(row.id)
    ElMessage.success('确认入库成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('确认入库失败')
    }
  }
}

const handleCopy = async (row) => {
  try {
    const response = await purReceiptApi.copyPurReceipt(row.id)
    const newReceipt = response.data
    router.push(`/purchase/receipt/edit/${newReceipt.id}`)
  } catch (error) {
    ElMessage.error('复制失败')
  }
}

const handlePrint = async (row) => {
  try {
    const response = await purReceiptApi.generateReceiptPrintData(row.id)
    // TODO: 实现打印功能
    ElMessage.info('打印功能待实现')
  } catch (error) {
    ElMessage.error('获取打印数据失败')
  }
}

const handleCommand = (command) => {
  const row = selectedRows.value[0]
  switch (command) {
  case 'cancel':
    handleCancel(row)
    break
  case 'delete':
    handleDelete(row)
    break
  }
}

const handleCancel = (row) => {
  cancelForm.id = row.id
  cancelForm.reason = ''
  cancelDialogVisible.value = true
  
  nextTick(() => {
    cancelFormRef.value?.clearValidate()
  })
}

const submitCancel = async () => {
  try {
    await cancelFormRef.value?.validate()
    
    await purReceiptApi.cancelPurReceipt(cancelForm.id, cancelForm.reason)
    ElMessage.success('取消成功')
    cancelDialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('取消失败:', error)
    if (error !== false) {
      ElMessage.error('取消失败')
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除该入库单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await purReceiptApi.deletePurReceipt(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确认删除选中的 ${selectedRows.value.length} 条记录吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    await Promise.all(ids.map(id => purReceiptApi.deletePurReceipt(id)))
    
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

const handleBatchConfirm = async () => {
  try {
    await ElMessageBox.confirm(`确认将选中的 ${selectedRows.value.length} 条入库单确认入库吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    await purReceiptApi.batchConfirmReceipt(ids)
    
    ElMessage.success('批量确认入库成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量确认入库失败')
    }
  }
}

const handleBatchAudit = async () => {
  try {
    const { value } = await ElMessageBox.prompt('请输入审核备注', '批量审核', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入审核备注'
    })
    
    const ids = selectedRows.value.map(row => row.id)
    await purReceiptApi.batchAuditPurReceipt(ids, true, value)
    
    ElMessage.success('批量审核成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量审核失败')
    }
  }
}

const handleExport = async () => {
  try {
    const params = { ...searchForm }
    if (searchForm.receiptTimeRange) {
      params.startTime = searchForm.receiptTimeRange[0]
      params.endTime = searchForm.receiptTimeRange[1]
    }
    if (searchForm.createTimeRange) {
      params.createStartTime = searchForm.createTimeRange[0]
      params.createEndTime = searchForm.createTimeRange[1]
    }
    
    const response = await purReceiptApi.exportReceiptData(params)
    
    // 下载文件
    const blob = new Blob([response.data], { 
      type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' 
    })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `采购入库单_${new Date().toLocaleDateString()}.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

const viewPurchaseOrder = (row) => {
  if (row.orderId) {
    router.push(`/purchase/order/view/${row.orderId}`)
  }
}

// 辅助方法
const getStatusTagType = (status) => {
  const statusMap = {
    1: '', // 草稿
    2: 'warning', // 待审核
    3: 'primary', // 已审核
    4: 'success', // 已入库
    5: 'danger' // 已取消
  }
  return statusMap[status] || ''
}

const hasMoreActions = (row) => {
  return (canCancel(row) && hasActionPermission('pur-receipt-management:cancel')) || 
         (canDelete(row) && hasActionPermission('pur-receipt-management:delete'))
}

const canCancel = (row) => {
  return [1, 2, 3].includes(row.receiptStatus)
}

const canDelete = (row) => {
  return [1, 5].includes(row.receiptStatus)
}
</script>

<style scoped>
.pur-receipt-list {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.search-card,
.toolbar-card,
.table-card {
  margin-bottom: 20px;
  border-radius: 8px;
  border: none;
}

.search-card {
  background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

/* 搜索区域头部样式 */
.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.search-title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 8px;
}

.search-title .el-icon {
  color: #409eff;
  font-size: 18px;
}

.search-actions {
  display: flex;
  gap: 12px;
}

.search-actions .el-button {
  border-radius: 6px;
  font-weight: 500;
  padding: 10px 20px;
  border: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.search-actions .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

/* 搜索表单样式 */
.search-form {
  background: transparent;
}

.search-row {
  margin-bottom: 16px;
}

.search-row:last-child {
  margin-bottom: 0;
}

.form-item {
  margin-bottom: 0 !important;
}

.form-item :deep(.el-form-item__label) {
  font-weight: 500;
  color: #606266;
  padding: 0 8px 0 0;
  min-width: 80px;
  text-align: right;
}

.form-item :deep(.el-input__wrapper) {
  border-radius: 6px;
  box-shadow: 0 0 0 1px #dcdfe6 inset;
  transition: all 0.3s ease;
}

.form-item :deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc inset;
}

.form-item :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #409eff inset;
}

.form-item :deep(.el-select) {
  width: 100%;
}

.form-item :deep(.el-select .el-select__wrapper) {
  border-radius: 6px;
  box-shadow: 0 0 0 1px #dcdfe6 inset;
  transition: all 0.3s ease;
}

.form-item :deep(.el-select .el-select__wrapper:hover) {
  box-shadow: 0 0 0 1px #c0c4cc inset;
}

.form-item :deep(.el-select .el-select__wrapper.is-focused) {
  box-shadow: 0 0 0 1px #409eff inset;
}

.form-item :deep(.el-date-editor) {
  width: 100%;
}

.form-item :deep(.el-date-editor .el-input__wrapper) {
  border-radius: 6px;
}

/* 工具栏样式 */
.toolbar-card {
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.toolbar-left {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

.toolbar-left .el-button {
  border-radius: 6px;
  font-weight: 500;
  padding: 10px 16px;
  border: none;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.toolbar-left .el-button:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.toolbar-left .el-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 表格卡片样式 */
.table-card {
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.table-card :deep(.el-table) {
  border-radius: 6px;
  overflow: hidden;
}

.table-card :deep(.el-table__header-wrapper) {
  background: #f8fafc;
}

.table-card :deep(.el-table th) {
  background: #f8fafc;
  color: #606266;
  font-weight: 600;
  border-bottom: 1px solid #e4e7ed;
}

.table-card :deep(.el-table td) {
  border-bottom: 1px solid #f0f2f5;
}

.table-card :deep(.el-table__body tr:hover) {
  background-color: #f8fafc;
}

/* 分页样式 */
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  padding: 20px;
  background: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.pagination-container :deep(.el-pagination) {
  font-weight: 500;
}

.pagination-container :deep(.el-pagination .btn-next),
.pagination-container :deep(.el-pagination .btn-prev) {
  border-radius: 6px;
}

.pagination-container :deep(.el-pagination .el-pager li) {
  border-radius: 6px;
  margin: 0 4px;
}

/* 对话框样式 */
.dialog-footer {
  text-align: right;
  padding-top: 20px;
}

.dialog-footer .el-button {
  border-radius: 6px;
  font-weight: 500;
  padding: 10px 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .pur-receipt-list {
    padding: 16px;
  }
  
  .search-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }
  
  .search-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .toolbar {
    flex-direction: column;
    gap: 16px;
    align-items: stretch;
  }
  
  .toolbar-left {
    justify-content: center;
  }
  
  .form-item :deep(.el-form-item__label) {
    text-align: left;
    min-width: auto;
    margin-bottom: 4px;
  }
}

/* 状态标签样式增强 */
:deep(.el-tag) {
  border-radius: 4px;
  font-weight: 500;
  border: none;
}

/* 按钮组样式 */
:deep(.el-button-group) {
  border-radius: 6px;
  overflow: hidden;
}

/* 加载状态 */
:deep(.el-loading-mask) {
  border-radius: 8px;
}
</style> 
 