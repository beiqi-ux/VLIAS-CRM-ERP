<template>
  <div class="purchase-return-list">
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
              label="退货单号"
              class="form-item"
            >
              <el-input
                v-model="searchForm.returnNo"
                placeholder="请输入退货单号"
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
              label="关联入库单"
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
                  v-for="supplier in supplierList"
                  :key="supplier.id"
                  :label="supplier.name"
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
                  v-for="warehouse in warehouseList"
                  :key="warehouse.id"
                  :label="warehouse.name"
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
              label="退货状态"
              class="form-item"
            >
              <el-select
                v-model="searchForm.returnStatus"
                placeholder="请选择状态"
                clearable
                style="width: 100%"
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
                  label="已退货"
                  :value="4"
                />
                <el-option
                  label="已取消"
                  :value="5"
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
              label="退货原因"
              class="form-item"
            >
              <el-select
                v-model="searchForm.reasonType"
                placeholder="请选择原因"
                clearable
                style="width: 100%"
              >
                <el-option
                  label="质量问题"
                  :value="1"
                />
                <el-option
                  label="规格不符"
                  :value="2"
                />
                <el-option
                  label="数量错误"
                  :value="3"
                />
                <el-option
                  label="其他"
                  :value="4"
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
              label="退货日期"
              class="form-item"
            >
              <el-date-picker
                v-model="dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD"
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
            v-if="hasActionPermission('pur-return-management:create')" 
            type="primary"
            @click="handleCreate"
          >
            <el-icon><Plus /></el-icon>
            新建退货单
          </el-button>
          <el-button 
            v-if="hasActionPermission('pur-return-management:delete')" 
            type="danger"
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
          <el-button 
            v-if="hasActionPermission('pur-return-management:submit')" 
            type="success"
            :disabled="selectedRows.length === 0"
            @click="handleBatchSubmit"
          >
            <el-icon><Check /></el-icon>
            批量提交
          </el-button>
          <el-button 
            v-if="hasActionPermission('pur-return-management:audit')" 
            type="warning"
            :disabled="selectedRows.length === 0"
            @click="handleBatchAudit"
          >
            <el-icon><EditPen /></el-icon>
            批量审核
          </el-button>
          <el-button 
            v-if="hasActionPermission('pur-return-management:export')" 
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
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="50"
        />
        <el-table-column
          prop="returnNo"
          label="退货单号"
          width="160"
        />
        <el-table-column
          prop="receiptNo"
          label="关联入库单"
          width="160"
        />
        <el-table-column
          prop="supplierName"
          label="供应商"
          width="120"
        />
        <el-table-column
          prop="warehouseName"
          label="仓库"
          width="100"
        />
        <el-table-column
          prop="returnStatusName"
          label="状态"
          width="100"
        >
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.returnStatus)">
              {{ scope.row.returnStatusName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="reasonTypeName"
          label="退货原因"
          width="120"
        />
        <el-table-column
          prop="totalAmount"
          label="退货金额"
          width="120"
          align="right"
        >
          <template #default="scope">
            ¥{{ (scope.row.totalAmount || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="returnDate"
          label="退货日期"
          width="100"
        />
        <el-table-column
          prop="createByName"
          label="创建人"
          width="100"
        />
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="150"
        />
        <el-table-column
          label="操作"
          width="200"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              v-if="hasActionPermission('pur-return-management:view')"
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.row)"
            >
              查看
            </el-button>
            <el-button
              v-if="hasActionPermission('pur-return-management:edit') && scope.row.returnStatus === 1"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="hasActionPermission('pur-return-management:submit') && scope.row.returnStatus === 1"
              size="mini"
              type="text"
              icon="el-icon-check"
              @click="handleSubmit(scope.row)"
            >
              提交
            </el-button>
            <el-button
              v-if="hasActionPermission('pur-return-management:audit') && scope.row.returnStatus === 2"
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleAudit(scope.row)"
            >
              审核
            </el-button>
            <el-button
              v-if="hasActionPermission('pur-return-management:confirm') && scope.row.returnStatus === 3"
              size="mini"
              type="text"
              icon="el-icon-finished"
              @click="handleConfirm(scope.row)"
            >
              确认退货
            </el-button>
            <el-dropdown
              trigger="click"
              @command="(command) => handleDropdownCommand(command, scope.row)"
            >
              <el-button
                size="mini"
                type="text"
              >
                更多<i class="el-icon-arrow-down el-icon--right" />
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-if="hasActionPermission('pur-return-management:copy')"
                    command="copy"
                  >
                    复制
                  </el-dropdown-item>
                  <el-dropdown-item
                    v-if="hasActionPermission('pur-return-management:print')"
                    command="print"
                  >
                    打印
                  </el-dropdown-item>
                  <el-dropdown-item
                    v-if="hasActionPermission('pur-return-management:cancel') && [1, 2].includes(scope.row.returnStatus)"
                    command="cancel"
                  >
                    取消
                  </el-dropdown-item>
                  <el-dropdown-item
                    v-if="hasActionPermission('pur-return-management:delete') && scope.row.returnStatus === 1"
                    command="delete"
                    divided
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
          :current-page="pagination.page"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 审核对话框 -->
    <el-dialog
      v-model:visible="auditDialogVisible"
      title="审核退货单"
      width="500px"
    >
      <el-form
        :model="auditForm"
        label-width="80px"
      >
        <el-form-item label="审核结果">
          <el-radio-group v-model="auditForm.approved">
            <el-radio :label="true">
              通过
            </el-radio>
            <el-radio :label="false">
              驳回
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注">
          <el-input
            v-model="auditForm.auditRemark"
            type="textarea"
            :rows="3"
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
            @click="confirmAudit"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 取消对话框 -->
    <el-dialog
      v-model:visible="cancelDialogVisible"
      title="取消退货单"
      width="400px"
    >
      <el-form
        :model="cancelForm"
        label-width="80px"
      >
        <el-form-item
          label="取消原因"
          required
        >
          <el-input
            v-model="cancelForm.reason"
            type="textarea"
            :rows="3"
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
            @click="confirmCancel"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { 
  Search, 
  Refresh, 
  Plus, 
  Delete, 
  Check, 
  EditPen, 
  Download 
} from '@element-plus/icons-vue'
import { purReturnApi } from '@/api/purchase/return'
import { supplierApi } from '@/api/supplier'
import { warehouseApi } from '@/api/warehouse'
import { PERMISSIONS, hasActionPermission } from '@/utils/permission'

export default {
  name: 'PurchaseReturnList',
  components: {
    Search,
    Refresh,
    Plus,
    Delete,
    Check,
    EditPen,
    Download
  },
  data() {
    return {
      // 搜索表单
      searchForm: {
        returnNo: '',
        receiptNo: '',
        supplierId: null,
        warehouseId: null,
        returnStatus: null,
        reasonType: null
      },
      dateRange: [],
      
      // 表格数据
      tableData: [],
      loading: false,
      selectedRows: [],
      
      // 分页
      pagination: {
        page: 1,
        size: 20,
        total: 0
      },
      
      // 下拉选项
      supplierList: [],
      warehouseList: [],
      
      // 审核对话框
      auditDialogVisible: false,
      auditForm: {
        id: null,
        approved: true,
        auditRemark: ''
      },
      
      // 取消对话框
      cancelDialogVisible: false,
      cancelForm: {
        id: null,
        reason: ''
      }
    }
  },
  
  created() {
    this.loadData()
    this.loadSuppliers()
    this.loadWarehouses()
  },
  
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          startDate: this.dateRange?.[0] || null,
          endDate: this.dateRange?.[1] || null,
          page: this.pagination.page - 1,
          size: this.pagination.size
        }
        
        const response = await purReturnApi.getPage(params)
        this.tableData = response.data.content
        this.pagination.total = response.data.totalElements
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 加载供应商
    async loadSuppliers() {
      try {
        const response = await supplierApi.getAllActiveSuppliers()
        this.supplierList = response.success ? response.data : []
      } catch (error) {
        console.error('加载供应商失败', error)
      }
    },
    
    // 加载仓库
    async loadWarehouses() {
      try {
        const response = await warehouseApi.getAll()
        this.warehouseList = response.data
      } catch (error) {
        console.error('加载仓库失败', error)
      }
    },
    
    // 搜索
    handleSearch() {
      this.pagination.page = 1
      this.loadData()
    },
    
    // 重置搜索
    handleReset() {
      this.searchForm = {
        returnNo: '',
        receiptNo: '',
        supplierId: null,
        warehouseId: null,
        returnStatus: null,
        reasonType: null
      }
      this.dateRange = []
      this.handleSearch()
    },
    
    // 新建
    handleCreate() {
      this.$router.push('/purchase/return/create')
    },
    
    // 查看
    handleView(row) {
      this.$router.push(`/purchase/return/view/${row.id}`)
    },
    
    // 编辑
    handleEdit(row) {
      this.$router.push(`/purchase/return/edit/${row.id}`)
    },
    
    // 提交
    async handleSubmit(row) {
      try {
        await this.$confirm('确定要提交这张退货单吗？', '提示', {
          type: 'warning'
        })
        
        await purReturnApi.submit(row.id)
        this.$message.success('提交成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('提交失败')
        }
      }
    },
    
    // 审核
    handleAudit(row) {
      this.auditForm.id = row.id
      this.auditForm.approved = true
      this.auditForm.auditRemark = ''
      this.auditDialogVisible = true
    },
    
    // 确认审核
    async confirmAudit() {
      try {
        await purReturnApi.audit(this.auditForm.id, {
          approved: this.auditForm.approved,
          auditRemark: this.auditForm.auditRemark
        })
        
        this.auditDialogVisible = false
        this.$message.success('审核完成')
        this.loadData()
      } catch (error) {
        this.$message.error('审核失败')
      }
    },
    
    // 确认退货
    async handleConfirm(row) {
      try {
        await this.$confirm('确定要确认退货吗？', '提示', {
          type: 'warning'
        })
        
        await purReturnApi.confirm(row.id)
        this.$message.success('确认成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('确认失败')
        }
      }
    },
    
    // 下拉菜单操作
    handleDropdownCommand(command, row) {
      switch (command) {
      case 'copy':
        this.handleCopy(row)
        break
      case 'print':
        this.handlePrint(row)
        break
      case 'cancel':
        this.handleCancel(row)
        break
      case 'delete':
        this.handleDelete(row)
        break
      }
    },
    
    // 复制
    async handleCopy(row) {
      try {
        const response = await purReturnApi.copy(row.id)
        this.$router.push(`/purchase/return/edit/${response.data.id}`)
      } catch (error) {
        this.$message.error('复制失败')
      }
    },
    
    // 打印
    handlePrint(row) {
      // TODO: 实现打印功能
      this.$message.info('打印功能开发中')
    },
    
    // 取消
    handleCancel(row) {
      this.cancelForm.id = row.id
      this.cancelForm.reason = ''
      this.cancelDialogVisible = true
    },
    
    // 确认取消
    async confirmCancel() {
      if (!this.cancelForm.reason.trim()) {
        this.$message.warning('请输入取消原因')
        return
      }
      
      try {
        await purReturnApi.cancel(this.cancelForm.id, this.cancelForm.reason)
        this.cancelDialogVisible = false
        this.$message.success('取消成功')
        this.loadData()
      } catch (error) {
        this.$message.error('取消失败')
      }
    },
    
    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这张退货单吗？', '提示', {
          type: 'warning'
        })
        
        await purReturnApi.delete(row.id)
        this.$message.success('删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败')
        }
      }
    },
    
    // 批量删除
    async handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的记录')
        return
      }
      
      try {
        await this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 条记录吗？`, '提示', {
          type: 'warning'
        })
        
        const ids = this.selectedRows.map(row => row.id)
        await purReturnApi.batchDelete(ids)
        this.$message.success('批量删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败')
        }
      }
    },
    
    // 批量提交
    async handleBatchSubmit() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要提交的记录')
        return
      }
      
      const validRows = this.selectedRows.filter(row => row.returnStatus === 1)
      if (validRows.length === 0) {
        this.$message.warning('所选记录中没有可提交的退货单')
        return
      }
      
      try {
        await this.$confirm(`确定要提交选中的 ${validRows.length} 条退货单吗？`, '提示', {
          type: 'warning'
        })
        
        const ids = validRows.map(row => row.id)
        await purReturnApi.batchSubmit(ids)
        this.$message.success('批量提交成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量提交失败')
        }
      }
    },
    
    // 批量审核
    async handleBatchAudit() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要审核的记录')
        return
      }
      
      const validRows = this.selectedRows.filter(row => row.returnStatus === 2)
      if (validRows.length === 0) {
        this.$message.warning('所选记录中没有可审核的退货单')
        return
      }
      
      try {
        await this.$confirm(`确定要审核通过选中的 ${validRows.length} 条退货单吗？`, '提示', {
          type: 'warning'
        })
        
        const ids = validRows.map(row => row.id)
        await purReturnApi.batchAudit(ids, { approved: true, auditRemark: '批量审核通过' })
        this.$message.success('批量审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量审核失败')
        }
      }
    },
    
    // 导出
    async handleExport() {
      try {
        const params = {
          ...this.searchForm,
          startDate: this.dateRange?.[0] || null,
          endDate: this.dateRange?.[1] || null
        }
        
        const response = await purReturnApi.export(params)
        // TODO: 处理文件下载
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    
    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    
    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.page = 1
      this.loadData()
    },
    
    // 当前页变化
    handleCurrentChange(page) {
      this.pagination.page = page
      this.loadData()
    },
    
    // 获取状态标签类型
    getStatusType(status) {
      const statusMap = {
        1: 'info',
        2: 'warning',
        3: 'success',
        4: 'primary',
        5: 'danger'
      }
      return statusMap[status] || 'info'
    },
    
    // 操作权限检查
    hasActionPermission
  }
}
</script>

<style scoped>
.purchase-return-list {
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

.search-form :deep(.el-form-item) {
  margin-bottom: 16px;
}

.search-form :deep(.el-form-item__content) {
  position: relative;
}

.search-form :deep(.el-input),
.search-form :deep(.el-select) {
  width: 100%;
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
  color: #606266;
  font-weight: 500;
  min-width: 80px;
  text-align: right;
}

.form-item :deep(.el-input__wrapper),
.form-item :deep(.el-select .el-select__wrapper) {
  border-radius: 6px;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
}

.form-item :deep(.el-input__wrapper:hover),
.form-item :deep(.el-select .el-select__wrapper:hover) {
  border-color: #c0c4cc;
}

.form-item :deep(.el-input__wrapper.is-focus),
.form-item :deep(.el-select .el-select__wrapper.is-focused) {
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.1);
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

.toolbar-right {
  display: flex;
  gap: 8px;
}

.toolbar .el-button {
  border-radius: 6px;
  font-weight: 500;
  padding: 10px 16px;
  transition: all 0.3s ease;
}

.toolbar .el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.toolbar .el-button[circle] {
  width: 40px;
  height: 40px;
  padding: 10px;
}

/* 表格样式 */
.table-card {
  background: #ffffff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.table-card :deep(.el-table) {
  border-radius: 8px;
  overflow: hidden;
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
  .purchase-return-list {
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
 