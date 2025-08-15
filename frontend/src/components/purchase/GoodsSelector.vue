<template>
  <div class="goods-selector">
    <!-- 搜索表单 -->
    <el-form 
      ref="searchForm" 
      :model="searchForm" 
      :inline="true"
      label-width="80px"
      class="search-form"
    >
      <el-form-item label="商品编码">
        <el-input
          v-model="searchForm.goodsCode"
          placeholder="请输入商品编码"
          clearable
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item label="商品名称">
        <el-input
          v-model="searchForm.goodsName"
          placeholder="请输入商品名称"
          clearable
          style="width: 200px"
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
      ref="table"
      v-loading="loading"
      :data="tableData"
      border
      style="width: 100%; margin-top: 15px;"
      max-height="400px"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="50"
      />
      <el-table-column
        prop="goodsCode"
        label="商品编码"
        width="120"
      />
      <el-table-column
        prop="goodsName"
        label="商品名称"
        min-width="150"
      />
      <el-table-column
        prop="specification"
        label="规格"
        width="100"
      />
      <el-table-column
        prop="unit"
        label="单位"
        width="80"
      />
      <el-table-column
        prop="receiptQuantity"
        label="入库数量"
        width="100"
        align="right"
      />
      <el-table-column
        prop="returnedQuantity"
        label="已退数量"
        width="100"
        align="right"
      />
      <el-table-column
        prop="remainQuantity"
        label="可退数量"
        width="100"
        align="right"
      >
        <template #default="scope">
          <span :class="{ 'text-danger': scope.row.remainQuantity <= 0 }">
            {{ scope.row.remainQuantity }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="unitPrice"
        label="单价"
        width="100"
        align="right"
      >
        <template #default="scope">
          ¥{{ (scope.row.unitPrice || 0).toFixed(2) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="batchNo"
        label="批次号"
        width="120"
      />
      <el-table-column
        prop="expiryDate"
        label="有效期"
        width="100"
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

    <!-- 已选商品 -->
    <div
      v-if="selectedGoods.length > 0"
      class="selected-goods"
    >
      <h4>已选择商品 ({{ selectedGoods.length }})</h4>
      <el-table
        :data="selectedGoods"
        border
        size="small"
        style="width: 100%"
        max-height="200px"
      >
        <el-table-column
          prop="goodsCode"
          label="商品编码"
          width="120"
        />
        <el-table-column
          prop="goodsName"
          label="商品名称"
          min-width="150"
        />
        <el-table-column
          prop="specification"
          label="规格"
          width="100"
        />
        <el-table-column
          prop="quantity"
          label="退货数量"
          width="120"
        >
          <template #default="scope">
            <el-input-number
              v-model="scope.row.quantity"
              :min="1"
              :max="scope.row.maxQuantity"
              size="mini"
              style="width: 100%"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="unitPrice"
          label="单价"
          width="100"
          align="right"
        >
          <template #default="scope">
            ¥{{ (scope.row.unitPrice || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="80"
        >
          <template #default="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleRemoveSelected(scope.$index)"
            >
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 操作按钮 -->
    <div class="dialog-footer">
      <el-button @click="handleCancel">
        取消
      </el-button>
      <el-button 
        type="primary" 
        :disabled="selectedGoods.length === 0"
        @click="handleConfirm"
      >
        确定 ({{ selectedGoods.length }})
      </el-button>
    </div>
  </div>
</template>

<script>
import { purReturnApi } from '@/api/purchase/return'

export default {
  name: 'GoodsSelector',
  
  props: {
    receiptId: {
      type: [String, Number],
      required: true
    },
    selectedGoods: {
      type: Array,
      default: () => []
    }
  },
  
  emits: ['select', 'cancel'],
  
  data() {
    return {
      // 搜索表单
      searchForm: {
        goodsCode: '',
        goodsName: ''
      },
      
      // 表格数据
      tableData: [],
      loading: false,
      selectedRows: [],
      
      // 分页
      pagination: {
        page: 1,
        size: 10,
        total: 0
      },
      
      // 内部已选商品列表
      internalSelectedGoods: []
    }
  },
  
  created() {
    this.internalSelectedGoods = [...this.selectedGoods]
    this.loadData()
  },
  
  methods: {
    // 加载可退货商品数据
    async loadData() {
      if (!this.receiptId) return
      
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.pagination.page - 1,
          size: this.pagination.size
        }
        
        // 这里应该调用获取可退货明细的API，暂时模拟数据
        const response = await this.mockReturnableItems(params)
        this.tableData = response.data.content.map(item => ({
          ...item,
          remainQuantity: item.receiptQuantity - item.returnedQuantity
        })).filter(item => item.remainQuantity > 0) // 只显示可退数量大于0的商品
        
        this.pagination.total = response.data.totalElements
        
        // 恢复之前的选择状态
        this.restoreSelection()
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 模拟可退货商品数据 - 实际项目中替换为真实API
    async mockReturnableItems(params) {
      // 模拟网络延迟
      await new Promise(resolve => setTimeout(resolve, 500))
      
      const mockData = [
        {
          id: 1,
          receiptItemId: 1,
          goodsId: 1,
          goodsCode: 'HW001',
          goodsName: '华为智能眼镜 Pro',
          specification: '黑色/大号',
          unit: '副',
          receiptQuantity: 20,
          returnedQuantity: 5,
          unitPrice: 1299.00,
          batchNo: 'HW20240815001',
          expiryDate: '2026-08-15'
        },
        {
          id: 2,
          receiptItemId: 2,
          goodsId: 2,
          goodsCode: 'RB001',
          goodsName: '雷朋经典款太阳镜',
          specification: '金色/中号',
          unit: '副',
          receiptQuantity: 30,
          returnedQuantity: 0,
          unitPrice: 899.00,
          batchNo: 'RB20240815001',
          expiryDate: '2026-08-15'
        },
        {
          id: 3,
          receiptItemId: 3,
          goodsId: 3,
          goodsCode: 'OK001',
          goodsName: 'Oakley运动眼镜',
          specification: '蓝色/均码',
          unit: '副',
          receiptQuantity: 15,
          returnedQuantity: 0,
          unitPrice: 1599.00,
          batchNo: 'OK20240815001',
          expiryDate: '2026-08-15'
        }
      ]
      
      // 根据搜索条件过滤
      let filteredData = mockData
      if (params.goodsCode) {
        filteredData = filteredData.filter(item => 
          item.goodsCode.toLowerCase().includes(params.goodsCode.toLowerCase())
        )
      }
      if (params.goodsName) {
        filteredData = filteredData.filter(item => 
          item.goodsName.toLowerCase().includes(params.goodsName.toLowerCase())
        )
      }
      
      return {
        data: {
          content: filteredData,
          totalElements: filteredData.length
        }
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
        goodsCode: '',
        goodsName: ''
      }
      this.handleSearch()
    },
    
    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
      
      // 更新内部已选商品列表
      selection.forEach(row => {
        const existingIndex = this.internalSelectedGoods.findIndex(item => item.id === row.id)
        if (existingIndex === -1) {
          // 新增选择
          this.internalSelectedGoods.push({
            ...row,
            quantity: 1,
            maxQuantity: row.remainQuantity,
            totalAmount: row.unitPrice
          })
        }
      })
      
      // 移除取消选择的商品
      this.internalSelectedGoods = this.internalSelectedGoods.filter(item => 
        selection.some(row => row.id === item.id)
      )
    },
    
    // 恢复选择状态
    restoreSelection() {
      this.$nextTick(() => {
        this.tableData.forEach(row => {
          const isSelected = this.internalSelectedGoods.some(item => item.id === row.id)
          if (isSelected) {
            this.$refs.table.toggleRowSelection(row, true)
          }
        })
      })
    },
    
    // 移除已选商品
    handleRemoveSelected(index) {
      const removedItem = this.internalSelectedGoods[index]
      this.internalSelectedGoods.splice(index, 1)
      
      // 取消表格中的选择
      const tableRow = this.tableData.find(row => row.id === removedItem.id)
      if (tableRow) {
        this.$refs.table.toggleRowSelection(tableRow, false)
      }
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
      if (this.internalSelectedGoods.length === 0) {
        this.$message.warning('请选择要退货的商品')
        return
      }
      
      // 验证退货数量
      const invalidItems = this.internalSelectedGoods.filter(item => 
        !item.quantity || item.quantity <= 0 || item.quantity > item.maxQuantity
      )
      
      if (invalidItems.length > 0) {
        this.$message.warning('请检查退货数量，确保在有效范围内')
        return
      }
      
      // 计算每个商品的金额
      const goods = this.internalSelectedGoods.map(item => ({
        ...item,
        totalAmount: (item.quantity || 0) * (item.unitPrice || 0)
      }))
      
      this.$emit('select', goods)
    },
    
    // 取消
    handleCancel() {
      this.$emit('cancel')
    }
  }
}
</script>

<style lang="scss" scoped>
.goods-selector {
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
  
  .selected-goods {
    margin-top: 20px;
    padding: 15px;
    background-color: #f8f9fa;
    border-radius: 4px;
    
    h4 {
      margin: 0 0 15px 0;
      color: #303133;
    }
  }
  
  .dialog-footer {
    margin-top: 20px;
    text-align: right;
  }
  
  .text-danger {
    color: #f56c6c;
  }
}
</style> 