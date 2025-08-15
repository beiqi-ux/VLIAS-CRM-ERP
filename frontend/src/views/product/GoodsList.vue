<template>
  <div class="goods-list">
    <!-- 搜索栏 -->
    <el-card
      class="search-card"
      shadow="never"
    >
      <el-form
        :model="searchForm"
        inline
      >
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
          <el-tree-select
            v-model="searchForm.categoryId"
            :data="categoryList"
            :props="{ value: 'id', label: 'categoryName', children: 'children' }"
            placeholder="请选择分类"
            clearable
            check-strictly
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="商品品牌">
          <el-select
            v-model="searchForm.brandId"
            placeholder="请选择品牌"
            clearable
            style="width: 200px"
          >
            <el-option
              v-for="brand in brandList"
              :key="brand.id"
              :label="brand.brandName"
              :value="brand.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="商品状态">
          <el-select
            v-model="searchForm.saleStatus"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option
              label="在售"
              :value="1"
            />
            <el-option
              label="下架"
              :value="0"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select
            v-model="searchForm.auditStatus"
            placeholder="请选择审核状态"
            clearable
            style="width: 120px"
          >
            <el-option
              label="待审核"
              :value="0"
            />
            <el-option
              label="已通过"
              :value="1"
            />
            <el-option
              label="已拒绝"
              :value="2"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            @click="handleSearch"
          >
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
    <el-card
      class="operation-card"
      shadow="never"
    >
      <div class="operation-row">
        <div class="left-operations">
          <el-button 
            v-if="hasActionPermission(PERMISSIONS.GOODS.GOODS.CREATE)"
            type="primary" 
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增商品
          </el-button>
          <el-button 
            v-if="hasActionPermission(PERMISSIONS.GOODS.GOODS.DELETE)"
            type="danger" 
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            <el-icon><Delete /></el-icon>
            批量删除
          </el-button>
        </div>
        <div class="right-operations">
          <el-button
            type="success"
            @click="handleRefresh"
          >
            <el-icon><Refresh /></el-icon>
            刷新
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
        :data="goodsList"
        stripe
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
        />
        <el-table-column
          prop="id"
          label="ID"
          width="80"
        />
        <el-table-column
          label="商品图片"
          width="80"
        >
          <template #default="{ row }">
            <el-image
              :src="getMainImageUrl(row.mainImage)"
              fit="cover"
              style="width: 50px; height: 50px; border-radius: 4px;"
              :preview-src-list="row.mainImage ? [getMainImageUrl(row.mainImage)] : []"
              preview-teleported
              lazy
            >
              <template #error>
                <div class="image-slot">
                  <el-icon><Picture /></el-icon>
                </div>
              </template>
              <template #placeholder>
                <div class="image-slot">
                  <el-icon><Loading /></el-icon>
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
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
          prop="categoryName"
          label="分类"
          width="120"
        />
        <el-table-column
          prop="brandName"
          label="品牌"
          width="120"
        />
        <el-table-column
          label="价格信息"
          width="180"
        >
          <template #default="{ row }">
            <div class="price-info">
              <div class="cost-price">
                成本: ¥{{ row.costPrice || 0 }}
              </div>
              <div class="selling-price">
                售价: ¥{{ row.sellingPrice || 0 }}
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="stockQty"
          label="库存"
          width="80"
        />
        <el-table-column
          label="状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag :type="row.saleStatus === 1 ? 'success' : 'danger'">
              {{ row.saleStatus === 1 ? '在售' : '下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="审核状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag 
              :type="row.auditStatus === 1 ? 'success' : row.auditStatus === 2 ? 'danger' : 'warning'"
            >
              {{ getAuditStatusText(row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="180"
        />
        <el-table-column 
          v-if="hasActionPermission(PERMISSIONS.GOODS.GOODS.VIEW) || hasActionPermission(PERMISSIONS.GOODS.GOODS.EDIT) || hasActionPermission(PERMISSIONS.GOODS.GOODS.DELETE) || hasActionPermission(PERMISSIONS.GOODS.GOODS.AUDIT)"
          label="操作" 
          width="280" 
          fixed="right"
        >
          <template #default="{ row }">
            <el-button 
              v-if="hasActionPermission(PERMISSIONS.GOODS.GOODS.VIEW)"
              type="primary" 
              text 
              @click="handleView(row)"
            >
              查看
            </el-button>
            <el-button 
              v-if="hasActionPermission(PERMISSIONS.GOODS.GOODS.EDIT)"
              type="primary" 
              text 
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="hasActionPermission(PERMISSIONS.GOODS.GOODS.EDIT)"
              :type="row.saleStatus === 1 ? 'warning' : 'success'" 
              text 
              @click="handleToggleSaleStatus(row)"
            >
              {{ row.saleStatus === 1 ? '下架' : '上架' }}
            </el-button>
            <el-button 
              v-if="row.auditStatus === 0 && hasActionPermission(PERMISSIONS.GOODS.GOODS.AUDIT)"
              type="success" 
              text 
              @click="handleAudit(row)"
            >
              审核
            </el-button>
            <el-button 
              v-if="hasActionPermission(PERMISSIONS.GOODS.GOODS.DELETE)"
              type="danger" 
              text 
              @click="handleDelete(row)"
            >
              删除
            </el-button>
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
      v-model="dialogVisible"
      :title="dialogTitle"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              label="商品编码"
              prop="goodsCode"
            >
              <el-input
                v-model="formData.goodsCode"
                placeholder="请输入商品编码"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="商品名称"
              prop="goodsName"
            >
              <el-input
                v-model="formData.goodsName"
                placeholder="请输入商品名称"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item
              label="商品分类"
              prop="categoryId"
            >
              <el-tree-select
                v-model="formData.categoryId"
                :data="categoryList"
                :props="{ value: 'id', label: 'categoryName', children: 'children' }"
                placeholder="请选择分类"
                clearable
                check-strictly
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="商品品牌"
              prop="brandId"
            >
              <el-select
                v-model="formData.brandId"
                placeholder="请选择品牌"
                style="width: 100%"
              >
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
            <el-form-item
              label="成本价"
              prop="costPrice"
            >
              <el-input-number 
                v-model="formData.costPrice" 
                :precision="2" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="售价"
              prop="sellingPrice"
            >
              <el-input-number 
                v-model="formData.sellingPrice" 
                :precision="2" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="原价"
              prop="originalPrice"
            >
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
            <el-form-item
              label="库存数量"
              prop="stockQty"
            >
              <el-input-number 
                v-model="formData.stockQty" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="预警库存"
              prop="warnStock"
            >
              <el-input-number 
                v-model="formData.warnStock" 
                :min="0" 
                style="width: 100%" 
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item
              label="重量(g)"
              prop="weight"
            >
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
            <el-form-item
              label="销售状态"
              prop="saleStatus"
            >
              <el-radio-group v-model="formData.saleStatus">
                <el-radio :label="1">
                  上架
                </el-radio>
                <el-radio :label="0">
                  下架
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="是否推荐"
              prop="isRecommended"
            >
              <el-radio-group v-model="formData.isRecommended">
                <el-radio :label="1">
                  是
                </el-radio>
                <el-radio :label="0">
                  否
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item
          label="商品简介"
          prop="brief"
        >
          <el-input 
            v-model="formData.brief" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入商品简介" 
          />
        </el-form-item>
        <el-form-item
          label="商品图片"
        >
          <div class="upload-container">
            <el-upload
              ref="uploadRef"
              v-model:file-list="imageFileList"
              :action="uploadAction"
              :headers="uploadHeaders"
              :data="{ goodsId: formData.id || 0, isMain: imageList.length === 0 ? 1 : 0 }"
              :on-success="handleImageUploadSuccess"
              :on-error="handleImageUploadError"
              :on-remove="handleImageRemove"
              :before-upload="beforeImageUpload"
              list-type="picture-card"
              :limit="5"
              multiple
              accept="image/*"
            >
              <el-icon><Plus /></el-icon>
              <template #tip>
                <div class="el-upload__tip">
                  支持jpg/png/gif格式，单张图片不超过2MB，最多上传5张
                </div>
              </template>
            </el-upload>
            <div
              v-if="imageList.length > 0"
              class="image-list"
            >
              <div
                v-for="(image, index) in imageList"
                :key="image.id"
                class="image-item"
                :class="{ 'is-main': image.isMain === 1 }"
              >
                <el-image
                  :src="getImageUrl(image.imageUrl)"
                  :preview-src-list="[getImageUrl(image.imageUrl)]"
                  fit="cover"
                  style="width: 100px; height: 100px"
                  lazy
                >
                  <template #error>
                    <div
                      class="image-slot"
                      style="width: 100px; height: 100px; display: flex; align-items: center; justify-content: center; background: #f5f5f5;"
                    >
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                  <template #placeholder>
                    <div
                      class="image-slot"
                      style="width: 100px; height: 100px; display: flex; align-items: center; justify-content: center; background: #f5f5f5;"
                    >
                      <el-icon><Loading /></el-icon>
                    </div>
                  </template>
                </el-image>
                <div class="image-actions">
                  <el-button
                    v-if="image.isMain !== 1"
                    size="small"
                    type="primary"
                    @click="setMainImage(image)"
                  >
                    设为主图
                  </el-button>
                  <el-button
                    v-else
                    size="small"
                    type="success"
                    disabled
                  >
                    主图
                  </el-button>
                  <el-button
                    size="small"
                    type="danger"
                    @click="removeImage(image, index)"
                  >
                    删除
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-form-item>
        <el-form-item
          label="商品详情"
          prop="content"
        >
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
          <el-button @click="dialogVisible = false">
            取消
          </el-button>
          <el-button
            type="primary"
            :loading="submitLoading"
            @click="handleSubmit"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog
      v-model="auditDialogVisible"
      title="商品审核"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="auditFormRef"
        :model="auditForm"
        :rules="auditRules"
        label-width="100px"
      >
        <el-form-item
          label="审核结果"
          prop="auditStatus"
        >
          <el-radio-group v-model="auditForm.auditStatus">
            <el-radio :label="1">
              通过
            </el-radio>
            <el-radio :label="2">
              拒绝
            </el-radio>
          </el-radio-group>
        </el-form-item>
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
            :loading="auditLoading"
            @click="handleAuditSubmit"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Delete, Picture, Loading } from '@element-plus/icons-vue'
import { formatDateTime } from '@/utils/format'
import { hasActionPermission, PERMISSIONS } from '@/utils/permission'
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
import { getImagesByGoodsId, deleteImage, updateImage, setAsMainImage } from '@/api/image'
import { uploadProductImage } from '@/api/file'

// 响应式数据
const loading = ref(false)
const router = useRouter()
const submitLoading = ref(false)
const auditLoading = ref(false)
const goodsList = ref([])
const categoryList = ref([])
const brandList = ref([])
const selectedRows = ref([])

// 图片相关
const imageList = ref([])
const imageFileList = ref([])
const uploadRef = ref()
const uploadAction = ref('/api/files/product-image')
const uploadHeaders = ref({
  'Authorization': localStorage.getItem('token') ? `Bearer ${localStorage.getItem('token')}` : ''
})

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
      
      // 为每个商品获取主图信息
      const goodsWithImages = await Promise.all(
        content.map(async (goods) => {
          try {
            const imageResponse = await getImagesByGoodsId(goods.id)
            if (imageResponse.success && imageResponse.data.length > 0) {
              // 设置主图
              goods.mainImage = imageResponse.data[0].url
            }
          } catch (error) {
            console.error(`获取商品 ${goods.id} 图片失败:`, error)
          }
          return goods
        })
      )
      
      goodsList.value = goodsWithImages
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
      categoryList.value = response.data
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
  // 跳转到新的编辑页面
  router.push('/goods/edit')
}

const handleEdit = async (row) => {
  // 跳转到新的编辑页面
  router.push(`/goods/edit/${row.id}`)
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
      // 如果是新创建的商品且有待关联的图片，需要更新图片的商品ID
      if (!formData.id && imageList.value.length > 0) {
        const newGoodsId = response.data.id
        // 更新临时上传的图片，设置正确的商品ID
        for (const image of imageList.value) {
          if (image.goodsId === 0) {
            await updateImage(image.id, { ...image, goodsId: newGoodsId })
          }
        }
      }
      
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
  // 重置图片相关数据
  imageList.value = []
  imageFileList.value = []
}

// 监听对话框状态变化，关闭时清理图片数据
watch(dialogVisible, (newVal) => {
  if (!newVal) {
    // 对话框关闭时重置图片列表
    imageList.value = []
    imageFileList.value = []
  }
})

// 图片处理方法
const loadProductImages = async (goodsId) => {
  if (!goodsId) {
    imageList.value = []
    return
  }
  try {
    const response = await getImagesByGoodsId(goodsId)
    
    if (response.success && response.data.length > 0) {
      imageList.value = response.data
    }
  } catch (error) {
    console.error('加载商品图片失败:', error)
    imageList.value = [] // 异常时重置图片列表
  }
}

const getImageUrl = (url) => {
  if (!url) return ''
  if (url.startsWith('http')) {
    return url
  }
  return `${import.meta.env.VITE_API_BASE_URL}${url}`
}

const getMainImageUrl = (url) => {
  if (!url) {
    // 返回一个默认的商品图片占位符
    const defaultImage = 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNTAiIGhlaWdodD0iNTAiIHZpZXdCb3g9IjAgMCA1MCA1MCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHJlY3Qgd2lkdGg9IjUwIiBoZWlnaHQ9IjUwIiBmaWxsPSIjRjVGNUY1Ii8+CjxwYXRoIGQ9Ik0yNSAyMEMyNi4zODA3IDIwIDI3LjUgMTguODgwNyAyNy41IDE3LjVDMjcuNSAxNi4xMTkzIDI2LjM4MDcgMTUgMjUgMTVDMjMuNjE5MyAxNSAyMi41IDE2LjExOTMgMjIuNSAxNy41QzIyLjUgMTguODgwNyAyMy42MTkzIDIwIDI1IDIwWiIgZmlsbD0iI0M0QzRDNCIvPgo8cGF0aCBkPSJNMzUgMzVIMTVMMjAgMjVMMjUgMzBMMzAgMjBMMzUgMzVaIiBmaWxsPSIjQzRDNEM0Ii8+Cjwvc3ZnPgo='
    return defaultImage
  }
  if (url.startsWith('http')) {
    return url
  }
  // 确保URL以/开头
  const cleanUrl = url.startsWith('/') ? url : `/${url}`
  const finalUrl = `${import.meta.env.VITE_API_BASE_URL}${cleanUrl}`
  return finalUrl
}

const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过 2MB!')
    return false
  }
  
  // 检查图片数量限制
  if (imageList.value.length >= 5) {
    ElMessage.error('每个商品最多只能上传5张图片!')
    return false
  }
  
  return true
}

const handleImageUploadSuccess = (response, file, fileList) => {
  if (response.success) {
    ElMessage.success('图片上传成功')
    // 重新加载商品图片
    if (formData.id) {
      loadProductImages(formData.id)
    } else {
      // 新商品时，将上传结果添加到图片列表
      imageList.value.push(response.data)
    }
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

const handleImageUploadError = (error, file, fileList) => {
  console.error('图片上传错误:', error)
  // 尝试解析后端返回的错误信息
  try {
    const response = JSON.parse(error.message)
    ElMessage.error(response.message || '图片上传失败')
  } catch (e) {
    ElMessage.error('图片上传失败')
  }
}

const handleImageRemove = (file, fileList) => {
  // 如果是从服务器删除，需要调用删除接口
  // 这里暂时不实现，因为el-upload的remove主要用于删除未上传的文件
}

const setMainImage = async (image) => {
  try {
    const response = await setAsMainImage(image.id)
    if (response.success) {
      ElMessage.success('设置主图成功')
      // 重新加载商品图片列表
      loadProductImages(formData.id)
      // 重新加载商品列表以更新主图显示
      loadGoodsList()
    }
  } catch (error) {
    ElMessage.error('设置主图失败')
  }
}

const removeImage = async (image, index) => {
  try {
    await ElMessageBox.confirm('确定要删除这张图片吗？', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await deleteImage(image.id)
    if (response.success) {
      ElMessage.success('删除成功')
      loadProductImages(formData.id)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
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

.search-card {
  margin-bottom: 20px;
}

.operation-card {
  margin-bottom: 20px;
}

.operation-row {
  display: flex;
  justify-content: space-between;
}

.upload-container {
  width: 100%;
}

.image-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-top: 10px;
}

.image-item {
  position: relative;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  padding: 5px;
  background: #fff;
}

.image-item.is-main {
  border-color: #67c23a;
  background: #f0f9ff;
}

.image-actions {
  display: flex;
  gap: 5px;
  margin-top: 5px;
  justify-content: center;
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 14px;
}

.image-actions .el-button {
  padding: 2px 6px;
  font-size: 12px;
  min-height: auto;
  align-items: center;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

/* TreeSelect层级样式优化 */
:deep(.el-tree-select .el-tree-node__content) {
  padding-left: calc(20px * var(--level, 0)) !important;
}

:deep(.el-tree-select .el-tree-node[data-level="1"] .el-tree-node__content) {
  font-weight: 600;
  color: #409EFF;
}

:deep(.el-tree-select .el-tree-node[data-level="2"] .el-tree-node__content) {
  font-weight: 500;
  color: #606266;
  padding-left: 20px !important;
}

:deep(.el-tree-select .el-tree-node[data-level="3"] .el-tree-node__content) {
  color: #909399;
  padding-left: 40px !important;
}

:deep(.el-tree-select .el-tree-node .el-tree-node__expand-icon) {
  color: #409EFF;
}
</style> 