<template>
  <div class="purchase-order-list">
    <!-- 搜索区域 -->
    <el-card
      class="search-card"
      shadow="never"
    >
      <el-form
        :model="searchForm"
        :inline="true"
        class="search-form"
      >
        <el-form-item label="订单号">
          <el-input
            v-model="searchForm.orderNo"
            placeholder="请输入订单号"
            clearable
            style="width: 200px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="供应商">
          <el-select
            v-model="searchForm.supplierId"
            placeholder="请选择供应商"
            clearable
            filterable
            style="width: 200px"
          >
            <el-option
              v-for="supplier in supplierOptions"
              :key="supplier.id"
              :label="supplier.supplierName"
              :value="supplier.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select
            v-model="searchForm.orderStatus"
            placeholder="请选择状态"
            clearable
            style="width: 150px"
          >
            <el-option
              label="草稿"
              :value="1"
            />
            <el-option
              label="待审核"
              :value="2"
            />
            <el-option
              label="已审核"
              :value="3"
            />
            <el-option
              label="已下单"
              :value="4"
            />
            <el-option
              label="部分入库"
              :value="5"
            />
            <el-option
              label="已完成"
              :value="6"
            />
            <el-option
              label="已取消"
              :value="7"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="支付状态">
          <el-select
            v-model="searchForm.payStatus"
            placeholder="请选择支付状态"
            clearable
            style="width: 150px"
          >
            <el-option
              label="未支付"
              :value="0"
            />
            <el-option
              label="部分支付"
              :value="1"
            />
            <el-option
              label="已支付"
              :value="2"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm:ss"
            style="width: 350px"
          />
        </el-form-item>
        <el-form-item>
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
        </el-form-item>
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
            v-hasPermission="'purchase-order-management:create'" 
            type="primary"
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增采购订单
          </el-button>
          <el-button 
            v-hasPermission="'purchase-order-management:delete'" 
            type="danger"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
          <el-button 
            v-hasPermission="'purchase-order-management:list'" 
            type="info"
            @click="handleExport"
          >
            <el-icon><Download /></el-icon>
            导出数据
          </el-button>
        </div>
        <div class="toolbar-right">
          <el-button-group>
            <el-button 
              v-hasPermission="'purchase-order-management:statistics'"
              @click="loadStatistics"
            >
              <el-icon><DataAnalysis /></el-icon>
              统计分析
            </el-button>
            <el-button 
              v-hasPermission="'purchase-order-management:follow'"
              @click="loadFollowOrders"
            >
              <el-icon><Bell /></el-icon>
              订单跟进
            </el-button>
          </el-button-group>
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
          prop="orderNo"
          label="订单号"
          width="160"
          fixed="left"
        />
        <el-table-column
          prop="supplierName"
          label="供应商"
          width="150"
        />
        <el-table-column
          prop="totalAmount"
          label="订单金额"
          width="120"
          align="right"
        >
          <template #default="{ row }">
            <span class="amount-text">¥{{ formatAmount(row.totalAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="orderStatus"
          label="订单状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.orderStatus)">
              {{ getStatusText(row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="payStatus"
          label="支付状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag :type="getPayStatusTagType(row.payStatus)">
              {{ getPayStatusText(row.payStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="receiptStatus"
          label="入库状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag :type="getReceiptStatusTagType(row.receiptStatus)">
              {{ getReceiptStatusText(row.receiptStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="itemCount"
          label="明细数量"
          width="100"
          align="center"
        />
        <el-table-column
          prop="expectedTime"
          label="预计到货"
          width="120"
        >
          <template #default="{ row }">
            <span :class="{ 'overdue-text': isOverdue(row.expectedTime, row.orderStatus) }">
              {{ formatDate(row.expectedTime) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="creatorName"
          label="制单人"
          width="100"
        />
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="160"
          sortable="custom"
        >
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="240"
          fixed="right"
        >
          <template #default="{ row }">
            <div class="action-buttons">
              <el-button 
                v-hasPermission="'purchase-order-management:view'" 
                type="primary" 
                size="small"
                link
                @click="handleView(row)"
              >
                查看
              </el-button>
              <el-button 
                v-if="canEdit(row.orderStatus)" 
                v-hasPermission="'purchase-order-management:update'" 
                type="primary"
                size="small"
                link
                @click="handleEdit(row)"
              >
                编辑
              </el-button>
              <el-button 
                v-if="row.orderStatus === 1" 
                v-hasPermission="'purchase-order-management:submit'" 
                type="warning"
                size="small"
                link
                @click="handleSubmit(row)"
              >
                提交
              </el-button>
              <el-button 
                v-if="row.orderStatus === 2" 
                v-hasPermission="'purchase-order-management:audit'" 
                type="success"
                size="small"
                link
                @click="handleAudit(row, true)"
              >
                通过
              </el-button>
              <el-button 
                v-if="row.orderStatus === 2" 
                v-hasPermission="'purchase-order-management:audit'" 
                type="danger"
                size="small"
                link
                @click="handleAudit(row, false)"
              >
                驳回
              </el-button>
              <el-dropdown
                trigger="click"
                @command="(command) => handleDropdownCommand(command, row)"
              >
                <el-button
                  type="info"
                  size="small"
                  link
                >
                  更多<el-icon class="el-icon--right">
                    <ArrowDown />
                  </el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item 
                      v-hasPermission="'purchase-order-management:create'"
                      command="copy"
                    >
                      复制订单
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="row.orderStatus >= 3"
                      v-hasPermission="'purchase-order-management:update'"
                      command="editPayStatus"
                    >
                      编辑支付状态
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="row.orderStatus >= 3"
                      v-hasPermission="'purchase-order-management:update'"
                      command="editReceiptStatus"
                    >
                      编辑入库状态
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="canCancel(row.orderStatus)"
                      v-hasPermission="'purchase-order-management:update'"
                      command="cancel"
                      divided
                    >
                      取消订单
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="row.orderStatus === 1"
                      v-hasPermission="'purchase-order-management:delete'"
                      command="delete"
                    >
                      删除订单
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
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

    <!-- 统计对话框 -->
    <el-dialog
      v-model="statisticsVisible"
      title="采购订单统计"
      width="800px"
    >
      <div class="statistics-content">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>订单状态分布</span>
              </template>
              <div
                v-for="(count, status) in statistics.statusCount"
                :key="status"
                class="statistic-item"
              >
                <span class="label">{{ getStatusText(parseInt(status)) }}：</span>
                <span class="value">{{ count }}</span>
              </div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <template #header>
                <span>支付状态分布</span>
              </template>
              <div
                v-for="(count, status) in statistics.payCount"
                :key="status"
                class="statistic-item"
              >
                <span class="label">{{ getPayStatusText(parseInt(status)) }}：</span>
                <span class="value">{{ count }}</span>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-row style="margin-top: 20px">
          <el-col :span="24">
            <el-card>
              <template #header>
                <span>本月采购金额</span>
              </template>
              <div class="month-amount">
                ¥{{ formatAmount(statistics.monthAmount) }}
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-dialog>

    <!-- 跟进订单对话框 -->
    <el-dialog
      v-model="followVisible"
      title="需要跟进的订单"
      width="1000px"
    >
      <el-table
        :data="followOrders"
        style="width: 100%"
      >
        <el-table-column
          prop="orderNo"
          label="订单号"
          width="160"
        />
        <el-table-column
          prop="supplierName"
          label="供应商"
          width="150"
        />
        <el-table-column
          prop="totalAmount"
          label="订单金额"
          width="120"
          align="right"
        >
          <template #default="{ row }">
            ¥{{ formatAmount(row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="expectedTime"
          label="预计到货"
          width="120"
        >
          <template #default="{ row }">
            <span :class="{ 'overdue-text': isOverdue(row.expectedTime, row.orderStatus) }">
              {{ formatDate(row.expectedTime) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          prop="orderStatus"
          label="状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag :type="getStatusTagType(row.orderStatus)">
              {{ getStatusText(row.orderStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="100"
        >
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              link
              @click="handleView(row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="auditVisible"
      :title="auditForm.approved ? '审核通过' : '审核驳回'"
      width="500px"
    >
      <el-form
        :model="auditForm"
        label-width="80px"
      >
        <el-form-item label="审核意见">
          <el-input
            v-model="auditForm.auditRemark"
            type="textarea"
            :rows="4"
            placeholder="请输入审核意见"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="auditVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="confirmAudit"
          >确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑支付状态对话框 -->
    <el-dialog
      v-model="payStatusVisible"
      title="编辑支付状态"
      width="500px"
    >
      <el-form
        :model="payStatusForm"
        label-width="100px"
      >
        <el-form-item label="订单号">
          <el-input
            v-model="payStatusForm.orderNo"
            disabled
          />
        </el-form-item>
        <el-form-item label="当前支付状态">
          <el-tag :type="getPayStatusTagType(payStatusForm.currentStatus)">
            {{ getPayStatusText(payStatusForm.currentStatus) }}
          </el-tag>
        </el-form-item>
        <el-form-item label="新支付状态" required>
          <el-select
            v-model="payStatusForm.newStatus"
            placeholder="请选择支付状态"
            style="width: 100%"
          >
            <el-option
              :value="0"
              label="未支付"
            />
            <el-option
              :value="1"
              label="部分支付"
            />
            <el-option
              :value="2"
              label="已支付"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="payStatusForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入修改原因或备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="payStatusVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="confirmUpdatePayStatus"
          >确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑入库状态对话框 -->
    <el-dialog
      v-model="receiptStatusVisible"
      title="编辑入库状态"
      width="500px"
    >
      <el-form
        :model="receiptStatusForm"
        label-width="100px"
      >
        <el-form-item label="订单号">
          <el-input
            v-model="receiptStatusForm.orderNo"
            disabled
          />
        </el-form-item>
        <el-form-item label="当前入库状态">
          <el-tag :type="getReceiptStatusTagType(receiptStatusForm.currentStatus)">
            {{ getReceiptStatusText(receiptStatusForm.currentStatus) }}
          </el-tag>
        </el-form-item>
        <el-form-item label="新入库状态" required>
          <el-select
            v-model="receiptStatusForm.newStatus"
            placeholder="请选择入库状态"
            style="width: 100%"
          >
            <el-option
              :value="0"
              label="未入库"
            />
            <el-option
              :value="1"
              label="部分入库"
            />
            <el-option
              :value="2"
              label="已入库"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="receiptStatusForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入修改原因或备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="receiptStatusVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="confirmUpdateReceiptStatus"
          >确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  Search, Refresh, Plus, Delete, Download, DataAnalysis, Bell, ArrowDown
} from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { purchaseOrderApi } from '@/api/purchaseOrder'
import { supplierApi } from '@/api/supplier'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const supplierOptions = ref([])
const selectedRows = ref([])
const statisticsVisible = ref(false)
const followVisible = ref(false)
const auditVisible = ref(false)
const statistics = ref({})
const followOrders = ref([])

// 搜索表单
const searchForm = reactive({
  orderNo: '',
  supplierId: null,
  orderStatus: null,
  payStatus: null,
  dateRange: null
})

// 分页信息
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 审核表单
const auditForm = reactive({
  id: null,
  approved: true,
  auditRemark: ''
})

// 编辑支付状态相关
const payStatusVisible = ref(false)
const payStatusForm = reactive({
  id: null,
  orderNo: '',
  currentStatus: 0,
  newStatus: 0,
  remark: ''
})

// 编辑入库状态相关
const receiptStatusVisible = ref(false)
const receiptStatusForm = reactive({
  id: null,
  orderNo: '',
  currentStatus: 0,
  newStatus: 0,
  remark: ''
})

// 加载数据
const loadData = async () => {
  try {
    loading.value = true
    
    const params = {
      ...searchForm,
      page: pagination.page - 1,
      size: pagination.size
    }

    // 处理日期范围
    if (searchForm.dateRange && searchForm.dateRange.length === 2) {
      params.startTime = searchForm.dateRange[0]
      params.endTime = searchForm.dateRange[1]
    }
    delete params.dateRange

    // 清除空值
    Object.keys(params).forEach(key => {
      if (params[key] === '' || params[key] === null || params[key] === undefined) {
        delete params[key]
      }
    })

    const response = await purchaseOrderApi.getPurchaseOrderPage(params)
    if (response.code === 200) {
      tableData.value = response.data.content
      pagination.total = response.data.totalElements
    } else {
      ElMessage.error(response.message || '查询失败')
    }
  } catch (error) {
    console.error('加载数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 加载供应商选项
const loadSuppliers = async () => {
  try {
    const response = await supplierApi.getAllActiveSuppliers()
    if (response.success) {
      supplierOptions.value = response.data
    }
  } catch (error) {
    console.error('加载供应商失败:', error)
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    orderNo: '',
    supplierId: null,
    orderStatus: null,
    payStatus: null,
    dateRange: null
  })
  pagination.page = 1
  loadData()
}

// 新增
const handleAdd = () => {
  router.push('/purchase/order/create')
}

// 查看
const handleView = (row) => {
  router.push(`/purchase/order/view/${row.id}`)
}

// 编辑
const handleEdit = (row) => {
  router.push(`/purchase/order/edit/${row.id}`)
}

// 提交
const handleSubmit = async (row) => {
  try {
    await ElMessageBox.confirm('确定要提交该采购订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await purchaseOrderApi.submitPurchaseOrder(row.id)
    ElMessage.success('提交成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
      ElMessage.error('提交失败')
    }
  }
}

// 审核
const handleAudit = (row, approved) => {
  auditForm.id = row.id
  auditForm.approved = approved
  auditForm.auditRemark = ''
  auditVisible.value = true
}

// 确认审核
const confirmAudit = async () => {
  try {
    await purchaseOrderApi.auditPurchaseOrder(
      auditForm.id,
      auditForm.approved,
      auditForm.auditRemark
    )
    ElMessage.success('审核成功')
    auditVisible.value = false
    loadData()
  } catch (error) {
    console.error('审核失败:', error)
    ElMessage.error('审核失败')
  }
}

// 编辑支付状态
const handleEditPayStatus = (row) => {
  payStatusForm.id = row.id
  payStatusForm.orderNo = row.orderNo
  payStatusForm.currentStatus = row.payStatus
  payStatusForm.newStatus = row.payStatus
  payStatusForm.remark = ''
  payStatusVisible.value = true
}

// 确认更新支付状态
const confirmUpdatePayStatus = async () => {
  if (payStatusForm.newStatus === payStatusForm.currentStatus) {
    ElMessage.warning('请选择不同的支付状态')
    return
  }

  try {
    await purchaseOrderApi.updatePaymentStatus(payStatusForm.id, {
      payStatus: payStatusForm.newStatus,
      paidAmount: null, // 手动修改时不指定具体金额
      remark: payStatusForm.remark
    })
    ElMessage.success('支付状态更新成功')
    payStatusVisible.value = false
    loadData()
  } catch (error) {
    console.error('更新支付状态失败:', error)
    ElMessage.error('更新支付状态失败')
  }
}

// 编辑入库状态
const handleEditReceiptStatus = (row) => {
  receiptStatusForm.id = row.id
  receiptStatusForm.orderNo = row.orderNo
  receiptStatusForm.currentStatus = row.receiptStatus
  receiptStatusForm.newStatus = row.receiptStatus
  receiptStatusForm.remark = ''
  receiptStatusVisible.value = true
}

// 确认更新入库状态
const confirmUpdateReceiptStatus = async () => {
  if (receiptStatusForm.newStatus === receiptStatusForm.currentStatus) {
    ElMessage.warning('请选择不同的入库状态')
    return
  }

  try {
    await purchaseOrderApi.updateReceiptStatus(receiptStatusForm.id, {
      receiptStatus: receiptStatusForm.newStatus,
      remark: receiptStatusForm.remark
    })
    ElMessage.success('入库状态更新成功')
    receiptStatusVisible.value = false
    loadData()
  } catch (error) {
    console.error('更新入库状态失败:', error)
    ElMessage.error('更新入库状态失败')
  }
}

// 下拉菜单命令处理
const handleDropdownCommand = async (command, row) => {
  switch (command) {
  case 'copy':
    await handleCopy(row)
    break
  case 'editPayStatus':
    handleEditPayStatus(row)
    break
  case 'editReceiptStatus':
    handleEditReceiptStatus(row)
    break
  case 'cancel':
    await handleCancel(row)
    break
  case 'delete':
    await handleDelete(row)
    break
  }
}

// 复制订单
const handleCopy = async (row) => {
  try {
    await purchaseOrderApi.copyPurchaseOrder(row.id)
    ElMessage.success('复制成功')
    loadData()
  } catch (error) {
    console.error('复制失败:', error)
    ElMessage.error('复制失败')
  }
}

// 取消订单
const handleCancel = async (row) => {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入取消原因：', '取消订单', {
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })

    await purchaseOrderApi.cancelPurchaseOrder(row.id, reason)
    ElMessage.success('取消成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败:', error)
      ElMessage.error('取消失败')
    }
  }
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该采购订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    await purchaseOrderApi.deletePurchaseOrder(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 批量删除
const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedRows.value.length} 条记录吗？`, '批量删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    const ids = selectedRows.value.map(row => row.id)
    await purchaseOrderApi.batchDeletePurchaseOrders(ids)
    ElMessage.success('批量删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  }
}

// 导出
const handleExport = () => {
  ElMessage.info('导出功能开发中...')
}

// 加载统计信息
const loadStatistics = async () => {
  try {
    const response = await purchaseOrderApi.getPurchaseOrderStatistics()
    if (response.code === 200) {
      statistics.value = response.data
      statisticsVisible.value = true
    }
  } catch (error) {
    console.error('加载统计信息失败:', error)
    ElMessage.error('加载统计信息失败')
  }
}

// 加载跟进订单
const loadFollowOrders = async () => {
  try {
    const response = await purchaseOrderApi.getOrdersNeedFollow()
    if (response.code === 200) {
      followOrders.value = response.data
      followVisible.value = true
    }
  } catch (error) {
    console.error('加载跟进订单失败:', error)
    ElMessage.error('加载跟进订单失败')
  }
}

// 选择变化
const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

// 排序变化
const handleSortChange = ({ prop, order }) => {
  // 实现排序逻辑
  loadData()
}

// 分页变化
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadData()
}

const handleCurrentChange = (page) => {
  pagination.page = page
  loadData()
}

// 工具函数
const formatAmount = (amount) => {
  return amount ? amount.toFixed(2) : '0.00'
}

const formatDate = (dateString) => {
  if (!dateString) return ''
  return dateString.split('T')[0]
}

const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return ''
  return dateTimeString.replace('T', ' ').slice(0, 19)
}

const getStatusText = (status) => {
  const statusMap = {
    1: '草稿',
    2: '待审核',
    3: '已审核',
    4: '已下单',
    5: '部分入库',
    6: '已完成',
    7: '已取消'
  }
  return statusMap[status] || '未知'
}

const getStatusTagType = (status) => {
  const typeMap = {
    1: 'info',
    2: 'warning',
    3: 'success',
    4: 'primary',
    5: 'warning',
    6: 'success',
    7: 'danger'
  }
  return typeMap[status] || 'info'
}

const getPayStatusText = (status) => {
  const statusMap = {
    0: '未支付',
    1: '部分支付',
    2: '已支付'
  }
  return statusMap[status] || '未知'
}

const getPayStatusTagType = (status) => {
  const typeMap = {
    0: 'danger',
    1: 'warning',
    2: 'success'
  }
  return typeMap[status] || 'info'
}

const getReceiptStatusText = (status) => {
  const statusMap = {
    0: '未入库',
    1: '部分入库',
    2: '已入库'
  }
  return statusMap[status] || '未知'
}

const getReceiptStatusTagType = (status) => {
  const typeMap = {
    0: 'danger',
    1: 'warning',
    2: 'success'
  }
  return typeMap[status] || 'info'
}

const canEdit = (status) => {
  return status === 1 || status === 2 // 草稿、待审核
}

const canCancel = (status) => {
  return status !== 6 && status !== 7 // 非已完成、已取消
}

const isOverdue = (expectedTime, orderStatus) => {
  if (!expectedTime || orderStatus === 6 || orderStatus === 7) return false
  return new Date(expectedTime) < new Date()
}

// 组件挂载
onMounted(() => {
  loadData()
  loadSuppliers()
})
</script>

<style scoped>
.purchase-order-list {
  padding: 20px;
}

.search-card,
.toolbar-card,
.table-card {
  margin-bottom: 20px;
}

.search-form {
  padding: 10px 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.toolbar-left,
.toolbar-right {
  display: flex;
  gap: 10px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.action-buttons {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.amount-text {
  font-weight: 600;
  color: #f56c6c;
}

.overdue-text {
  color: #f56c6c;
  font-weight: 600;
}

.statistics-content .statistic-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  padding: 5px 0;
  border-bottom: 1px solid #f0f0f0;
}

.statistics-content .label {
  font-weight: 500;
  color: #606266;
}

.statistics-content .value {
  font-weight: 600;
  color: #409eff;
}

.month-amount {
  font-size: 32px;
  font-weight: 600;
  color: #67c23a;
  text-align: center;
  padding: 20px 0;
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

:deep(.el-card__header) {
  background: #f8f9fa;
  border-bottom: 1px solid #ebeef5;
}

.dialog-footer {
  text-align: right;
}
</style> 