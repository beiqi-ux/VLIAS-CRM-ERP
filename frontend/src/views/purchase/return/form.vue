<template>
  <div class="pur-receipt-form">
    <el-card
      class="form-card"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-button
              :icon="ArrowLeft"
              @click="goBack"
            >
              返回
            </el-button>
            <span class="title">{{ isView ? '查看' : (isEdit ? '编辑' : '新建') }}退货单</span>
          </div>
          <div class="actions">
            <el-button
              v-if="!isView && form.returnStatus === 1"
              @click="handleSave"
            >
              保存
            </el-button>
            <el-button 
              v-if="!isView && form.returnStatus === 1" 
              type="primary" 
              @click="handleSubmit"
            >
              提交
            </el-button>
            <el-button
              v-if="isView && hasPermission('pur-return-management:edit')"
              type="primary"
              @click="handleEdit"
            >
              编辑
            </el-button>
            <el-button
              v-if="isView && hasPermission('pur-return-management:print')"
              @click="handlePrint"
            >
              打印
            </el-button>
          </div>
        </div>
      </template>

      <!-- 基本信息 -->
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        label-width="120px"
        :disabled="isView"
        class="receipt-form"
      >
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item
              label="退货单号"
              prop="returnNo"
            >
              <el-input
                v-model="form.returnNo"
                placeholder="系统自动生成"
                :disabled="true"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="关联入库单"
              prop="receiptNo"
            >
              <el-input
                v-model="form.receiptNo"
                placeholder="请选择入库单"
                readonly
                style="cursor: pointer;"
                @click="handleSelectReceipt"
              >
                <template #append>
                  <el-button
                    :icon="Search"
                    @click="handleSelectReceipt"
                  />
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="供应商"
              prop="supplierId"
            >
              <el-select
                v-model="form.supplierId"
                placeholder="请选择供应商"
                style="width: 100%"
                :disabled="true"
                :loading="suppliersLoading"
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
        </el-row>

        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item
              label="退货仓库"
              prop="warehouseId"
            >
              <el-select
                v-model="form.warehouseId"
                placeholder="请选择仓库"
                style="width: 100%"
                :loading="warehousesLoading"
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
          <el-col :span="8">
            <el-form-item
              label="退货日期"
              prop="returnDate"
            >
              <el-date-picker
                v-model="form.returnDate"
                type="date"
                placeholder="选择退货日期"
                style="width: 100%"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="退货原因"
              prop="returnReason"
            >
              <el-select
                v-model="form.returnReason"
                placeholder="请选择退货原因"
                style="width: 100%"
              >
                <el-option
                  v-for="reason in returnReasonOptions"
                  :key="reason.value"
                  :label="reason.label"
                  :value="reason.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="24">
            <el-form-item
              label="备注"
              prop="remark"
            >
              <el-input
                v-model="form.remark"
                type="textarea"
                :rows="3"
                placeholder="请输入备注信息"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 退货明细 -->
    <el-card
      class="items-card"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <span class="title">退货明细</span>
          <div
            v-if="!isView"
            class="actions"
          >
            <el-button 
              size="small" 
              type="primary" 
              :icon="Plus"
              @click="handleAddItem"
            >
              添加商品
            </el-button>
            <el-button 
              size="small" 
              :icon="Delete"
              :disabled="selectedItems.length === 0"
              @click="handleBatchDeleteItems"
            >
              批量删除
            </el-button>
          </div>
        </div>
      </template>

      <el-table
        ref="itemTable"
        :data="form.items"
        border
        style="width: 100%"
        @selection-change="handleItemSelectionChange"
      >
        <el-table-column
          v-if="!isView"
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
          prop="quantity"
          label="退货数量"
          width="120"
        >
          <template #default="scope">
            <el-input-number
              v-if="!isView"
              v-model="scope.row.quantity"
              :min="1"
              :max="scope.row.maxQuantity"
              size="small"
              style="width: 100%"
              @change="handleQuantityChange(scope.row)"
            />
            <span v-else>{{ scope.row.quantity }}</span>
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
          prop="totalAmount"
          label="金额"
          width="120"
          align="right"
        >
          <template #default="scope">
            ¥{{ (scope.row.totalAmount || 0).toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注"
          min-width="120"
        >
          <template #default="scope">
            <el-input
              v-if="!isView"
              v-model="scope.row.remark"
              size="small"
              placeholder="请输入备注"
            />
            <span v-else>{{ scope.row.remark }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="!isView"
          label="操作"
          width="80"
          fixed="right"
        >
          <template #default="scope">
            <el-button
              size="small"
              type="danger"
              :icon="Delete"
              @click="handleDeleteItem(scope.$index)"
            />
          </template>
        </el-table-column>
      </el-table>

      <div class="total-row">
        <span>合计数量: {{ totalQuantity }}</span>
        <span>合计金额: <span class="total-amount">¥{{ totalAmount.toFixed(2) }}</span></span>
      </div>
    </el-card>

    <!-- 审核信息 -->
    <el-card
      v-if="form.auditTime"
      class="form-card"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <span class="title">审核信息</span>
        </div>
      </template>
      
      <el-row :gutter="24">
        <el-col :span="8">
          <el-form-item label="审核人">
            <el-input
              v-model="form.auditByName"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="审核时间">
            <el-input
              v-model="form.auditTime"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="审核结果">
            <el-tag :type="form.returnStatus === 3 ? 'success' : 'danger'">
              {{ form.returnStatus === 3 ? '通过' : '驳回' }}
            </el-tag>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="24">
        <el-col :span="24">
          <el-form-item label="审核备注">
            <el-input
              v-model="form.auditRemark"
              type="textarea"
              :rows="2"
              :disabled="true"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- 选择入库单对话框 -->
    <el-dialog
      v-model="receiptDialogVisible"
      title="选择入库单"
      width="800px"
      :close-on-click-modal="false"
    >
      <ReceiptSelector
        v-if="receiptDialogVisible"
        @select="handleReceiptSelect"
        @cancel="receiptDialogVisible = false"
      />
    </el-dialog>

    <!-- 添加商品对话框 -->
    <el-dialog
      v-model="goodsDialogVisible"
      title="添加退货商品"
      width="1000px"
      :close-on-click-modal="false"
    >
      <GoodsSelector
        v-if="goodsDialogVisible"
        :receipt-id="form.receiptId"
        :selected-goods="form.items"
        @select="handleGoodsSelect"
        @cancel="goodsDialogVisible = false"
      />
    </el-dialog>
  </div>
</template>

<script>
import { purReturnApi } from '@/api/purchase/return'
import { supplierApi } from '@/api/supplier'
import { warehouseApi } from '@/api/warehouse'
import ReceiptSelector from '@/components/purchase/ReceiptSelector.vue'
import GoodsSelector from '@/components/purchase/GoodsSelector.vue'
import { ArrowLeft } from '@element-plus/icons-vue'

export default {
  name: 'PurchaseReturnForm',
  components: {
    ReceiptSelector,
    GoodsSelector
  },
  
  data() {
    return {
      // 表单数据
      form: {
        id: null,
        returnNo: '',
        receiptId: null,
        receiptNo: '',
        supplierId: null,
        warehouseId: null,
        returnDate: new Date().toISOString().slice(0, 10),
        returnReason: null,
        remark: '',
        returnStatus: 1,
        totalAmount: 0,
        items: [],
        auditTime: null,
        auditByName: '',
        auditRemark: ''
      },
      
      // 表单验证规则
      rules: {
        receiptNo: [{ required: true, message: '请选择入库单', trigger: 'blur' }],
        supplierId: [{ required: true, message: '请选择供应商', trigger: 'blur' }],
        warehouseId: [{ required: true, message: '请选择仓库', trigger: 'blur' }],
        returnDate: [{ required: true, message: '请选择退货日期', trigger: 'blur' }],
        returnReason: [{ required: true, message: '请选择退货原因', trigger: 'blur' }]
      },
      
      // 页面状态
      loading: false,
      selectedItems: [],
      suppliersLoading: false,
      warehousesLoading: false,
      
      // 下拉选项
      supplierOptions: [],
      warehouseOptions: [],
      returnReasonOptions: [
        { label: '质量问题', value: 1 },
        { label: '运输损坏', value: 2 },
        { label: '发货错误', value: 3 },
        { label: '过期商品', value: 4 },
        { label: '其他', value: 9 }
      ],
      receiptDialogVisible: false,
      goodsDialogVisible: false
    }
  },
    
  // 表单验证规则
  rules: {
    receiptId: [
      { required: true, message: '请选择入库单', trigger: 'change' }
    ],
    supplierId: [
      { required: true, message: '请选择供应商', trigger: 'change' }
    ],
    warehouseId: [
      { required: true, message: '请选择仓库', trigger: 'change' }
    ],
    returnDate: [
      { required: true, message: '请选择退货日期', trigger: 'change' }
    ],
    returnReason: [
      { required: true, message: '请选择退货原因', trigger: 'change' }
    ]
  },
  
  computed: {
    // 判断页面模式
    isView() {
      return this.$route.path.includes('/view/')
    },
    isEdit() {
      return this.$route.path.includes('/edit/')
    },
    isCreate() {
      return this.$route.path.includes('/create')
    },
    
    // 合计数量
    totalQuantity() {
      return this.form.items.reduce((total, item) => total + (item.quantity || 0), 0)
    },
    
    // 合计金额
    totalAmount() {
      return this.form.items.reduce((total, item) => total + (item.totalAmount || 0), 0)
    }
  },
  
  created() {
    this.loadSuppliers()
    this.loadWarehouses()
    
    if (this.isEdit || this.isView) {
      this.loadData()
    }
  },
  
  methods: {
    // 加载数据
    async loadData() {
      const id = this.$route.params.id
      if (!id) return
      
      this.loading = true
      try {
        const response = await purReturnApi.getById(id)
        this.form = { ...this.form, ...response.data }
      } catch (error) {
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    // 加载供应商
    async loadSuppliers() {
      this.suppliersLoading = true
      try {
        const response = await supplierApi.getAllActiveSuppliers()
        this.supplierOptions = response.success ? response.data : []
      } catch (error) {
        console.error('加载供应商失败', error)
        this.$message.error('加载供应商列表失败')
      } finally {
        this.suppliersLoading = false
      }
    },
    
    // 加载仓库
    async loadWarehouses() {
      this.warehousesLoading = true
      try {
        const response = await warehouseApi.getList()
        this.warehouseOptions = response.data || []
      } catch (error) {
        console.error('加载仓库失败', error)
        this.$message.error('加载仓库列表失败')
      } finally {
        this.warehousesLoading = false
      }
    },
    
    // 选择入库单
    handleSelectReceipt() {
      this.receiptDialogVisible = true
    },
    
    // 入库单选择回调
    handleReceiptSelect(receipt) {
      this.form.receiptId = receipt.id
      this.form.receiptNo = receipt.receiptNo
      this.form.supplierId = receipt.supplierId
      this.form.warehouseId = receipt.warehouseId
      this.receiptDialogVisible = false
    },
    
    // 添加商品
    handleAddItem() {
      if (!this.form.receiptId) {
        this.$message.warning('请先选择入库单')
        return
      }
      this.goodsDialogVisible = true
    },
    
    // 商品选择回调
    handleGoodsSelect(goods) {
      this.form.items = [...this.form.items, ...goods]
      this.goodsDialogVisible = false
      this.updateTotalAmount()
    },
    
    // 删除明细
    async handleDeleteItem(index) {
      try {
        await this.$confirm('确认删除该商品吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        this.form.items.splice(index, 1)
        this.updateTotalAmount()
        this.$message.success('删除成功')
      } catch (error) {
        // 用户取消操作
      }
    },
    
    // 批量删除明细
    async handleBatchDeleteItems() {
      if (this.selectedItems.length === 0) {
        this.$message.warning('请选择要删除的商品')
        return
      }
      
      try {
        await this.$confirm(`确认删除选中的 ${this.selectedItems.length} 个商品吗？`, '批量删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        this.selectedItems.forEach(item => {
          const index = this.form.items.findIndex(i => i === item)
          if (index !== -1) {
            this.form.items.splice(index, 1)
          }
        })
        
        this.selectedItems = []
        this.updateTotalAmount()
        this.$message.success('批量删除成功')
      } catch (error) {
        // 用户取消操作
      }
    },
    
    // 数量变化
    handleQuantityChange(item) {
      item.totalAmount = (item.quantity || 0) * (item.unitPrice || 0)
      this.updateTotalAmount()
    },
    
    // 更新总金额
    updateTotalAmount() {
      this.form.totalAmount = this.totalAmount
    },
    
    // 明细选择变化
    handleItemSelectionChange(selection) {
      this.selectedItems = selection
    },
    
    // 保存
    async handleSave() {
      try {
        await this.$refs.form.validate()
        
        if (this.form.items.length === 0) {
          this.$message.warning('请添加退货商品')
          return
        }
        
        this.loading = true
        
        if (this.isEdit) {
          await purReturnApi.update(this.form.id, this.form)
          this.$message.success('保存成功')
        } else {
          const response = await purReturnApi.create(this.form)
          this.$message.success('退货单创建成功')
          this.$router.replace(`/purchase/return/edit/${response.data.id}`)
        }
      } catch (error) {
        if (error !== 'validation failed') {
          this.$message.error('保存失败')
        }
      } finally {
        this.loading = false
      }
    },
    
    // 提交
    async handleSubmit() {
      try {
        await this.handleSave()
        
        if (this.form.id) {
          await purReturnApi.submit(this.form.id)
          this.$message.success('提交成功')
          this.goBack()
        }
      } catch (error) {
        this.$message.error('提交失败')
      }
    },
    
    // 编辑
    handleEdit() {
      this.$router.push(`/purchase/return/edit/${this.form.id}`)
    },
    
    // 打印
    handlePrint() {
      // TODO: 实现打印功能
      this.$message.info('打印功能开发中')
    },
    
    // 返回
    goBack() {
      this.$router.back()
    },
    
    // 权限检查
    hasPermission(permission) {
      return this.$store.getters.permissions.includes(permission)
    }
  }
}
</script>

<style scoped>
.pur-receipt-form {
  padding: 20px;
}

.form-card,
.items-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title {
  font-size: 16px;
  font-weight: bold;
}

.actions {
  display: flex;
  gap: 8px;
}

.receipt-form .el-form-item {
  margin-bottom: 20px;
}

.total-row {
  margin-top: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
  text-align: right;
  font-size: 16px;
  font-weight: bold;
}

.total-amount {
  color: #f56c6c;
  font-size: 18px;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.dialog-footer {
  text-align: right;
}
</style> 
 