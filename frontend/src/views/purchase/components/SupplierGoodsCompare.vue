<template>
  <el-dialog
    v-model="dialogVisible"
    title="供应商价格比较"
    width="900px"
    :before-close="handleClose"
  >
    <div v-loading="loading">
      <!-- 商品信息 -->
      <el-card v-if="compareData" class="goods-info-card" shadow="never">
        <template #header>
          <div class="goods-header">
            <el-icon class="goods-icon"><Goods /></el-icon>
            <span class="goods-title">商品信息</span>
          </div>
        </template>
        <div class="goods-details">
          <div class="goods-item">
            <span class="label">商品名称：</span>
            <span class="value">{{ compareData.goodsName }}</span>
          </div>
          <div class="goods-item">
            <span class="label">商品编码：</span>
            <span class="value">{{ compareData.goodsCode }}</span>
          </div>
          <div class="goods-item">
            <span class="label">供应商数量：</span>
            <span class="value">{{ compareData.suppliers?.length || 0 }}家</span>
          </div>
        </div>
      </el-card>

      <!-- 价格比较表格 -->
      <el-card v-if="compareData?.suppliers?.length > 0" class="compare-table-card">
        <template #header>
          <div class="compare-header">
            <el-icon class="compare-icon"><TrendCharts /></el-icon>
            <span class="compare-title">价格比较</span>
            <div class="compare-actions">
              <el-button type="success" size="small" @click="handleCreateOrder">
                <el-icon><Plus /></el-icon>
                创建采购订单
              </el-button>
              <el-button type="primary" size="small" @click="handleExport">
                <el-icon><Download /></el-icon>
                导出比较结果
              </el-button>
            </div>
          </div>
        </template>

        <el-table
          :data="compareData.suppliers"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="55" />
          
          <el-table-column prop="priceRank" label="价格排名" width="80" align="center">
            <template #default="{ row }">
              <el-tag 
                :type="getRankTagType(row.priceRank)" 
                size="small"
              >
                第{{ row.priceRank }}名
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column prop="supplierName" label="供应商" width="140">
            <template #default="{ row }">
              <div class="supplier-cell">
                <div class="supplier-name">{{ row.supplierName }}</div>
                <div class="supplier-code">{{ row.supplierCode }}</div>
              </div>
            </template>
          </el-table-column>

          <el-table-column prop="purchasePrice" label="采购价格" width="120" align="right">
            <template #default="{ row }">
              <span class="price-value" :class="getPriceClass(row.priceRank)">
                ¥{{ row.purchasePrice }}
              </span>
            </template>
          </el-table-column>

          <el-table-column prop="minPurchaseQty" label="最小采购量" width="100" align="center">
            <template #default="{ row }">
              <span v-if="row.minPurchaseQty">{{ row.minPurchaseQty }}</span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>

          <el-table-column prop="deliveryDay" label="交货天数" width="100" align="center">
            <template #default="{ row }">
              <span v-if="row.deliveryDay">
                <el-tag 
                  :type="getDeliveryTagType(row.deliveryDay)"
                  size="small"
                >
                  {{ row.deliveryDay }}天
                </el-tag>
              </span>
              <span v-else class="text-muted">-</span>
            </template>
          </el-table-column>

          <el-table-column prop="isRecommended" label="推荐状态" width="120" align="center">
            <template #default="{ row }">
              <div class="recommend-cell">
                <el-tag 
                  v-if="row.isRecommended" 
                  type="success" 
                  size="small"
                >
                  推荐
                </el-tag>
                <span v-else class="text-muted">-</span>
                <div v-if="row.recommendReason" class="recommend-reason">
                  {{ row.recommendReason }}
                </div>
              </div>
            </template>
          </el-table-column>

          <el-table-column label="综合评分" width="120" align="center">
            <template #default="{ row }">
              <el-rate
                :model-value="getComprehensiveScore(row)"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}"
              />
            </template>
          </el-table-column>

          <el-table-column label="操作" width="100" fixed="right">
            <template #default="{ row }">
              <el-button 
                type="primary" 
                size="small"
                @click="handleSelectSupplier(row)"
              >
                选择
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 无数据提示 -->
      <el-empty v-else-if="!loading" description="该商品暂无供应商信息" />
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">关闭</el-button>
        <el-button 
          type="primary" 
          @click="handleBatchCreateOrder"
          :disabled="selectedSuppliers.length === 0"
        >
          批量创建订单 ({{ selectedSuppliers.length }})
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Goods, TrendCharts, Plus, Download 
} from '@element-plus/icons-vue'
import { supplierGoodsApi } from '@/api/supplierGoods'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  goodsId: {
    type: [Number, String],
    default: null
  }
})

const emit = defineEmits(['update:visible', 'create-order'])

// 状态
const dialogVisible = ref(false)
const loading = ref(false)
const compareData = ref(null)
const selectedSuppliers = ref([])

// 监听显示状态
watch(() => props.visible, (val) => {
  dialogVisible.value = val
  if (val && props.goodsId) {
    loadCompareData()
  }
})

watch(dialogVisible, (val) => {
  emit('update:visible', val)
})

// 加载比较数据
const loadCompareData = async () => {
  if (!props.goodsId) {
    ElMessage.warning('请先选择商品')
    return
  }

  try {
    loading.value = true
    const response = await supplierGoodsApi.comparePrice(props.goodsId)
    if (response.code === 200) {
      compareData.value = response.data
    }
  } catch (error) {
    console.error('加载价格比较数据失败:', error)
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

// 计算综合评分
const getComprehensiveScore = (supplier) => {
  let score = 5 // 基础分

  // 价格排名评分
  if (supplier.priceRank === 1) score += 2
  else if (supplier.priceRank === 2) score += 1
  else if (supplier.priceRank === 3) score += 0.5

  // 交货期评分
  if (supplier.deliveryDay <= 3) score += 1
  else if (supplier.deliveryDay <= 7) score += 0.5

  // 推荐状态评分
  if (supplier.isRecommended) score += 1

  return Math.min(10, Math.max(1, score))
}

// 获取排名标签类型
const getRankTagType = (rank) => {
  if (rank === 1) return 'success'
  if (rank === 2) return 'warning'
  if (rank === 3) return 'info'
  return 'danger'
}

// 获取价格样式类
const getPriceClass = (rank) => {
  return {
    'price-best': rank === 1,
    'price-good': rank === 2,
    'price-normal': rank >= 3
  }
}

// 获取交货期标签类型
const getDeliveryTagType = (days) => {
  if (days <= 3) return 'success'
  if (days <= 7) return 'warning'
  return 'danger'
}

// 事件处理
const handleSelectionChange = (selection) => {
  selectedSuppliers.value = selection
}

const handleSelectSupplier = (supplier) => {
  emit('create-order', {
    goodsId: props.goodsId,
    supplierId: supplier.supplierId,
    goodsName: compareData.value.goodsName,
    supplierName: supplier.supplierName,
    purchasePrice: supplier.purchasePrice,
    minPurchaseQty: supplier.minPurchaseQty,
    deliveryDay: supplier.deliveryDay
  })
  handleClose()
}

const handleCreateOrder = () => {
  if (selectedSuppliers.value.length === 0) {
    ElMessage.warning('请先选择供应商')
    return
  }
  
  if (selectedSuppliers.value.length === 1) {
    handleSelectSupplier(selectedSuppliers.value[0])
  } else {
    handleBatchCreateOrder()
  }
}

const handleBatchCreateOrder = async () => {
  if (selectedSuppliers.value.length === 0) {
    ElMessage.warning('请先选择供应商')
    return
  }

  try {
    await ElMessageBox.confirm(
      `确定要为选中的 ${selectedSuppliers.value.length} 个供应商创建采购订单吗？`,
      '确认创建',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }
    )

    // 发射批量创建事件
    emit('create-order', {
      goodsId: props.goodsId,
      goodsName: compareData.value.goodsName,
      suppliers: selectedSuppliers.value.map(s => ({
        supplierId: s.supplierId,
        supplierName: s.supplierName,
        purchasePrice: s.purchasePrice,
        minPurchaseQty: s.minPurchaseQty,
        deliveryDay: s.deliveryDay
      }))
    })
    
    ElMessage.success('批量创建采购订单成功')
    handleClose()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量创建订单失败:', error)
      ElMessage.error('批量创建失败')
    }
  }
}

const handleExport = () => {
  if (!compareData.value) {
    ElMessage.warning('没有可导出的数据')
    return
  }

  // 构建CSV数据
  const headers = ['排名', '供应商名称', '供应商编码', '采购价格', '最小采购量', '交货天数', '推荐状态', '推荐理由']
  const csvContent = [
    headers.join(','),
    ...compareData.value.suppliers.map(s => [
      s.priceRank,
      s.supplierName,
      s.supplierCode,
      s.purchasePrice,
      s.minPurchaseQty || '',
      s.deliveryDay || '',
      s.isRecommended ? '推荐' : '',
      s.recommendReason || ''
    ].join(','))
  ].join('\n')

  // 下载文件
  const blob = new Blob(['\ufeff' + csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = `${compareData.value.goodsName}_价格比较_${new Date().toISOString().slice(0, 10)}.csv`
  link.click()
  
  ElMessage.success('导出成功')
}

const handleClose = () => {
  dialogVisible.value = false
  compareData.value = null
  selectedSuppliers.value = []
}
</script>

<style scoped>
.goods-info-card {
  margin-bottom: 20px;
}

.goods-header {
  display: flex;
  align-items: center;
  gap: 8px;
}

.goods-icon {
  font-size: 16px;
  color: #409eff;
}

.goods-title {
  font-weight: 500;
}

.goods-details {
  display: flex;
  gap: 30px;
}

.goods-item {
  display: flex;
  align-items: center;
}

.goods-item .label {
  color: #909399;
  margin-right: 8px;
}

.goods-item .value {
  font-weight: 500;
  color: #303133;
}

.compare-table-card {
  margin-bottom: 20px;
}

.compare-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.compare-header .compare-icon {
  font-size: 16px;
  color: #67c23a;
  margin-right: 8px;
}

.compare-title {
  font-weight: 500;
  flex: 1;
}

.compare-actions {
  display: flex;
  gap: 10px;
}

.supplier-cell {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.supplier-name {
  font-weight: 500;
  color: #303133;
}

.supplier-code {
  font-size: 12px;
  color: #909399;
}

.price-value {
  font-weight: 600;
  font-size: 14px;
}

.price-best {
  color: #67c23a;
}

.price-good {
  color: #e6a23c;
}

.price-normal {
  color: #f56c6c;
}

.recommend-cell {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
}

.recommend-reason {
  font-size: 12px;
  color: #909399;
}

.text-muted {
  color: #c0c4cc;
}

.dialog-footer {
  text-align: right;
}

.dialog-footer .el-button {
  margin-left: 10px;
}
</style> 