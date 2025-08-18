<template>
  <div class="receipt-selector">
    <!-- 搜索表单 -->
    <el-form 
      ref="searchForm" 
      :model="searchForm" 
      :inline="true"
      label-width="80px"
      class="search-form"
    >
      <el-form-item label="入库单号">
        <el-input
          v-model="searchForm.receiptNo"
          placeholder="请输入入库单号"
          clearable
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item label="供应商">
        <el-select
          v-model="searchForm.supplierId"
          placeholder="请选择供应商"
          clearable
          filterable
          style="width: 180px"
        >
          <el-option
            v-for="supplier in supplierList"
            :key="supplier.id"
            :label="supplier.supplierName"
            :value="supplier.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="入库日期">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd"
          style="width: 240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="handleSearch"
        >
          搜索
        </el-button>
        <el-button
          icon="el-icon-refresh"
          @click="handleReset"
        >
          重置
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="tableData"
      border
      highlight-current-row
      style="width: 100%; margin-top: 15px;"
      max-height="400px"
      @current-change="handleCurrentChange"
    >
      <el-table-column
        prop="receiptNo"
        label="入库单号"
        width="160"
      />
      <el-table-column
        prop="supplierName"
        label="供应商"
        width="140"
      />
      <el-table-column
        prop="warehouseName"
        label="仓库"
        width="120"
      />
      <el-table-column
        prop="receiptDate"
        label="入库日期"
        width="100"
      />
      <el-table-column
        prop="totalAmount"
        label="入库金额"
        width="120"
        align="right"
      >
        <template #default="scope">
          ¥{{ (scope.row.totalAmount || 0).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="returnedAmount"
        label="已退金额"
        width="120"
        align="right"
      >
        <template #default="scope">
          ¥{{ (scope.row.returnedAmount || 0).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="remainAmount"
        label="可退金额"
        width="120"
        align="right"
      >
        <template #default="scope">
          ¥{{ ((scope.row.totalAmount || 0) - (scope.row.returnedAmount || 0)).toFixed(2) }}
        </template>
      </el-table-column>
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
    </el-table>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        :current-page="pagination.page"
        :page-sizes="[10, 20, 50]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>

    <!-- 操作按钮 -->
    <div class="dialog-footer">
      <el-button @click="handleCancel">
        取消
      </el-button>
      <el-button 
        type="primary" 
        :disabled="!selectedReceipt"
        @click="handleConfirm"
      >
        确定
      </el-button>
    </div>
  </div>
</template>

<script>
import { purReceiptApi as purchaseReceiptApi } from '@/api/purchase/purReceipt'
import { supplierApi } from '@/api/supplier'

export default {
  name: 'ReceiptSelector',
  
  emits: ['select', 'cancel'],
  
  data() {
    return {
      // 搜索表单
      searchForm: {
        receiptNo: '',
        supplierId: null
      },
      dateRange: [],
      
      // 表格数据
      tableData: [],
      loading: false,
      selectedReceipt: null,
      
      // 分页
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      
      // 下拉选项
      supplierList: []
    }
  },
  
  created() {
    this.loadData()
    this.loadSuppliers()
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
          receiptStatus: 3, // 只显示已入库的单据
          page: this.pagination.page - 1,
          size: this.pagination.size
        }
        
        // 这里应该调用入库单API，暂时模拟数据
        const response = await this.mockReceiptData(params)
        this.tableData = response.data.content
        this.pagination.total = response.data.totalElements
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 模拟入库单数据 - 实际项目中替换为真实API
    async mockReceiptData(params) {
      // 模拟网络延迟
      await new Promise(resolve => setTimeout(resolve, 500))
      
      const mockData = [
        {
          id: 1,
          receiptNo: 'RK202408150001',
          supplierId: 1,
          supplierName: '华为眼镜供应商',
          warehouseId: 1,
          warehouseName: '主仓库',
          receiptDate: '2024-08-15',
          totalAmount: 15600.00,
          returnedAmount: 2000.00,
          createByName: '张三',
          createTime: '2024-08-15 10:30:00'
        },
        {
          id: 2,
          receiptNo: 'RK202408150002',
          supplierId: 2,
          supplierName: '雷朋眼镜供应商',
          warehouseId: 1,
          warehouseName: '主仓库',
          receiptDate: '2024-08-15',
          totalAmount: 23400.00,
          returnedAmount: 0,
          createByName: '李四',
          createTime: '2024-08-15 14:20:00'
        }
      ]
      
      return {
        data: {
          content: mockData,
          totalElements: mockData.length
        }
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
    
    // 搜索
    handleSearch() {
      this.pagination.page = 1
      this.loadData()
    },
    
    // 重置搜索
    handleReset() {
      this.searchForm = {
        receiptNo: '',
        supplierId: null
      }
      this.dateRange = []
      this.handleSearch()
    },
    
    // 行选择变化
    handleCurrentChange(row) {
      this.selectedReceipt = row
    },
    
    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.page = 1
      this.loadData()
    },
    
    // 当前页变化
    handlePageChange(page) {
      this.pagination.page = page
      this.loadData()
    },
    
    // 确认选择
    handleConfirm() {
      if (!this.selectedReceipt) {
        this.$message.warning('请选择一条入库单记录')
        return
      }
      
      this.$emit('select', this.selectedReceipt)
    },
    
    // 取消
    handleCancel() {
      this.$emit('cancel')
    }
  }
}
</script>

<style lang="scss" scoped>
.receipt-selector {
  .search-form {
    background-color: #f8f9fa;
    padding: 15px;
    border-radius: 4px;
    margin-bottom: 15px;
  }
  
  .pagination-container {
    margin-top: 15px;
    text-align: center;
  }
  
  .dialog-footer {
    margin-top: 20px;
    text-align: right;
  }
}
</style> 
 