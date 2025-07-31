<template>
  <div class="goods-list">
    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" inline>
        <el-form-item label="商品名称">
          <el-input 
            v-model="searchForm.goodsName" 
            placeholder="请输入商品名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="商品编码">
          <el-input 
            v-model="searchForm.goodsCode" 
            placeholder="请输入商品编码" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="商品分类">
          <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable style="width: 200px">
            <el-option
              v-for="category in categoryList"
              :key="category.id"
              :label="category.categoryName"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品品牌">
          <el-select v-model="searchForm.brandId" placeholder="请选择品牌" clearable style="width: 200px">
            <el-option
              v-for="brand in brandList"
              :key="brand.id"
              :label="brand.brandName"
              :value="brand.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品状态">
          <el-select v-model="searchForm.saleStatus" placeholder="请选择状态" clearable style="width: 120px">
            <el-option label="在售" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select v-model="searchForm.auditStatus" placeholder="请选择审核状态" clearable style="width: 120px">
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch" :loading="loading">
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
        <div class="left-operations">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增商品
          </el-button>
          <el-button 
            type="danger" 
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
        <div class="right-operations">
          <el-button type="success" @click="handleRefresh">
            <el-icon><Refresh /></el-icon>
            刷新
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="goodsList"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="商品图片" width="80">
          <template #default="{ row }">
            <el-image
              v-if="row.mainImage"
              :src="row.mainImage"
              fit="cover"
              style="width: 50px; height: 50px; border-radius: 4px;"
              :preview-src-list="[row.mainImage]"
              preview-teleported
            />
            <div v-else class="no-image">暂无图片</div>
          </template>
        </el-table-column>
        <el-table-column prop="goodsCode" label="商品编码" width="120" />
        <el-table-column prop="goodsName" label="商品名称" min-width="150" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="brandName" label="品牌" width="120" />
        <el-table-column label="价格信息" width="180">
          <template #default="{ row }">
            <div class="price-info">
              <div class="cost-price">成本: ¥{{ row.costPrice || 0 }}</div>
              <div class="selling-price">售价: ¥{{ row.sellingPrice || 0 }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="stockQty" label="库存" width="80" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.saleStatus === 1 ? 'success' : 'danger'">
              {{ row.saleStatus === 1 ? '在售' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审核状态" width="100">
          <template #default="{ row }">
            <el-tag 
              :type="row.auditStatus === 1 ? 'success' : row.auditStatus === 2 ? 'danger' : 'warning'"
            >
              {{ getAuditStatusText(row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" text @click="handleView(row)">查看</el-button>
            <el-button type="primary" text @click="handleEdit(row)">编辑</el-button>
            <el-button 
              :type="row.saleStatus === 1 ? 'warning' : 'success'" 
              text 
              @click="handleToggleSaleStatus(row)"
            >
              {{ row.saleStatus === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button 
              v-if="row.auditStatus === 0"
              type="success" 
              text 
              @click="handleAudit(row)"
            >
              审核
            </el-button>
            <el-button type="danger" text @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品编码" prop="goodsCode">
              <el-input v-model="formData.goodsCode" placeholder="请输入商品编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品名称" prop="goodsName">
              <el-input v-model="formData.goodsName" placeholder="请输入商品名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="商品分类" prop="categoryId">
              <el-select v-model="formData.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option
                  v-for="category in categoryList"
                  :key="category.id"
                  :label="category.categoryName"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="商品品牌" prop="brandId">
              <el-select v-model="formData.brandId" placeholder="请选择品牌" style="width: 100%">
                <el-option
                  v-for="brand in brandList"
                  :key="brand.id"
                  :label="brand.brandName"
                  :value="brand.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="成本价" prop="costPrice">
              <el-input-number 
                v-model="formData.costPrice" 
                :precision="2" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="售价" prop="sellingPrice">
              <el-input-number 
                v-model="formData.sellingPrice" 
                :precision="2" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="原价" prop="originalPrice">
              <el-input-number 
                v-model="formData.originalPrice" 
                :precision="2" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="库存数量" prop="stockQty">
              <el-input-number 
                v-model="formData.stockQty" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="预警库存" prop="warnStock">
              <el-input-number 
                v-model="formData.warnStock" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="重量(g)" prop="weight">
              <el-input-number 
                v-model="formData.weight" 
                :precision="2" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="销售状态" prop="saleStatus">
              <el-radio-group v-model="formData.saleStatus">
                <el-radio :label="1">上架</el-radio>
                <el-radio :label="0">下架</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否推荐" prop="isRecommended">
              <el-radio-group v-model="formData.isRecommended">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="商品简介" prop="brief">
          <el-input 
            v-model="formData.brief" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入商品简介" 
          />
        </el-form-item>
        <el-form-item label="商品详情" prop="content">
          <el-input 
            v-model="formData.content" 
            type="textarea" 
            :rows="5" 
            placeholder="请输入商品详情" 
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog
      title="商品审核"
      v-model="auditDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form :model="auditForm" :rules="auditRules" ref="auditFormRef" label-width="100px">
        <el-form-item label="审核结果" prop="auditStatus">
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio :label="1">通过</el-radio>
            <el-radio :label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" prop="auditRemark">
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
          <el-button @click="auditDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAuditSubmit" :loading="auditLoading">
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete } from '@element-plus/icons-vue'
import {
  getGoodsList,
  createGoods,
  updateGoods,
  deleteGoods,
  batchDeleteGoods,
  onSaleGoods,
  offSaleGoods,
  auditGoods
} from '@/api/goods'
import { getCategoryTree } from '@/api/category'
import { getAllBrands } from '@/api/brand'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const auditLoading = ref(false)
const goodsList = ref([])
const categoryList = ref([])
const brandList = ref([])
const selectedRows = ref([])

// 搜索表单
const searchForm = reactive({
  goodsName: '',
  goodsCode: '',
  categoryId: null,
  brandId: null,
  saleStatus: null,
  auditStatus: null
})

// 分页信息
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = computed(() => formData.id ? '编辑商品' : '新增商品')
const formRef = ref()

// 表单数据
const formData = reactive({
  id: null,
  goodsCode: '',
  goodsName: '',
  categoryId: null,
  brandId: null,
  costPrice: 0,
  sellingPrice: 0,
  originalPrice: 0,
  stockQty: 0,
  warnStock: 0,
  weight: 0,
  saleStatus: 1,
  isRecommended: 0,
  brief: '',
  content: ''
})

// 表单验证规则
const formRules = {
  goodsCode: [
    { required: true, message: '请输入商品编码', trigger: 'blur' }
  ],
  goodsName: [
    { required: true, message: '请输入商品名称', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  brandId: [
    { required: true, message: '请选择商品品牌', trigger: 'change' }
  ],
  sellingPrice: [
    { required: true, message: '请输入售价', trigger: 'blur' }
  ]
}

// 审核对话框相关
const auditDialogVisible = ref(false)
const auditFormRef = ref()
const currentAuditGoods = ref(null)

const auditForm = reactive({
  auditStatus: 1,
  auditRemark: ''
})

const auditRules = {
  auditStatus: [
    { required: true, message: '请选择审核结果', trigger: 'change' }
  ]
}

// 方法
const loadGoodsList = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.current - 1,
      size: pagination.size,
      ...searchForm
    }
    const response = await getGoodsList(params)
    if (response.success) {
      const { content, totalElements } = response.data
      goodsList.value = content
      pagination.total = totalElements
    }
  } catch (error) {
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}

const loadCategoryList = async () => {
  try {
    const response = await getCategoryTree()
    if (response.success) {
      categoryList.value = flattenTree(response.data)
    }
  } catch (error) {
    console.error('获取分类列表失败:', error)
  }
}

const loadBrandList = async () => {
  try {
    const response = await getAllBrands()
    if (response.success) {
      brandList.value = response.data
    }
  } catch (error) {
    console.error('获取品牌列表失败:', error)
  }
}

// 扁平化树结构
const flattenTree = (tree) => {
  const result = []
  const traverse = (nodes, level = 0) => {
    nodes.forEach(node => {
      result.push({
        ...node,
        categoryName: '　'.repeat(level) + node.categoryName
      })
      if (node.children && node.children.length > 0) {
        traverse(node.children, level + 1)
      }
    })
  }
  traverse(tree)
  return result
}

const handleSearch = () => {
  pagination.current = 1
  loadGoodsList()
}

const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = key.includes('Id') ? null : ''
  })
  pagination.current = 1
  loadGoodsList()
}

const handleRefresh = () => {
  loadGoodsList()
}

const handleSelectionChange = (selection) => {
  selectedRows.value = selection
}

const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  loadGoodsList()
}

const handleCurrentChange = (page) => {
  pagination.current = page
  loadGoodsList()
}

const handleAdd = () => {
  resetFormData()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.keys(formData).forEach(key => {
    formData[key] = row[key] || (key.includes('Price') || key.includes('Qty') || key.includes('Stock') || key === 'weight' ? 0 : 
                                key === 'saleStatus' || key === 'isRecommended' ? (row[key] ?? 1) : '')
  })
  dialogVisible.value = true
}

const handleView = (row) => {
  // 这里可以实现查看详情功能
  ElMessage.info('查看功能开发中...')
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除商品"${row.goodsName}"吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await deleteGoods(row.id)
      if (response.success) {
        ElMessage.success('删除成功')
        loadGoodsList()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}

const handleBatchDelete = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请选择要删除的商品')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedRows.value.length} 个商品吗？`,
    '确认批量删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedRows.value.map(row => row.id)
      const response = await batchDeleteGoods(ids)
      if (response.success) {
        ElMessage.success('批量删除成功')
        loadGoodsList()
      }
    } catch (error) {
      ElMessage.error('批量删除失败')
    }
  })
}

const handleToggleSaleStatus = async (row) => {
  try {
    const action = row.saleStatus === 1 ? '下架' : '上架'
    await ElMessageBox.confirm(`确定要${action}商品"${row.goodsName}"吗？`, `确认${action}`)
    
    const response = row.saleStatus === 1 
      ? await offSaleGoods(row.id)
      : await onSaleGoods(row.id)
      
    if (response.success) {
      ElMessage.success(`${action}成功`)
      loadGoodsList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

const handleAudit = (row) => {
  currentAuditGoods.value = row
  auditForm.auditStatus = 1
  auditForm.auditRemark = ''
  auditDialogVisible.value = true
}

const handleAuditSubmit = async () => {
  try {
    await auditFormRef.value.validate()
    auditLoading.value = true
    
    const response = await auditGoods(currentAuditGoods.value.id, auditForm)
    if (response.success) {
      ElMessage.success('审核成功')
      auditDialogVisible.value = false
      loadGoodsList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('审核失败')
    }
  } finally {
    auditLoading.value = false
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const response = formData.id 
      ? await updateGoods(formData.id, formData)
      : await createGoods(formData)
      
    if (response.success) {
      ElMessage.success(formData.id ? '更新成功' : '创建成功')
      dialogVisible.value = false
      loadGoodsList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  } finally {
    submitLoading.value = false
  }
}

const resetFormData = () => {
  Object.keys(formData).forEach(key => {
    formData[key] = key === 'saleStatus' || key === 'isRecommended' ? (key === 'saleStatus' ? 1 : 0) :
                    key.includes('Price') || key.includes('Qty') || key.includes('Stock') || key === 'weight' ? 0 :
                    key === 'id' ? null : ''
  })
}

const getAuditStatusText = (status) => {
  const statusMap = {
    0: '待审核',
    1: '已通过',
    2: '已拒绝'
  }
  return statusMap[status] || '未知'
}

// 生命周期
onMounted(() => {
  loadGoodsList()
  loadCategoryList()
  loadBrandList()
})
</script>

<style scoped>
.goods-list {
  padding: 20px;
}

.search-card, .operation-card, .table-card {
  margin-bottom: 16px;
}

.operation-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.no-image {
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f5f5;
  border-radius: 4px;
  font-size: 12px;
  color: #999;
}

.price-info {
  font-size: 12px;
}

.cost-price {
  color: #909399;
  margin-bottom: 2px;
}

.selling-price {
  color: #e6a23c;
  font-weight: bold;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.dialog-footer {
  text-align: right;
}
</style> 