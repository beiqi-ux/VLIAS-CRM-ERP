<template>
  <div class="category-list">
    <!-- 搜索栏 -->
    <el-card
      class="search-card"
      shadow="never"
    >
      <el-form
        :model="searchForm"
        inline
      >
        <el-form-item label="分类名称">
          <el-input 
            v-model="searchForm.categoryName" 
            placeholder="请输入分类名称" 
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="searchForm.status"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option
              label="启用"
              :value="1"
            />
            <el-option
              label="禁用"
              :value="0"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="显示状态">
          <el-select
            v-model="searchForm.isShow"
            placeholder="请选择显示状态"
            clearable
            style="width: 120px"
          >
            <el-option
              label="显示"
              :value="1"
            />
            <el-option
              label="隐藏"
              :value="0"
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
            v-if="hasPermission(PERMISSIONS.GOODS.CATEGORY.CREATE)"
            type="primary" 
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增分类
          </el-button>
          <el-button
            type="success"
            @click="handleExpandAll"
          >
            <el-icon><Sort /></el-icon>
            {{ isExpanded ? '收起全部' : '展开全部' }}
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
        :data="categoryList"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="isExpanded"
        stripe
        style="width: 100%"
      >
        <el-table-column
          prop="categoryName"
          label="分类名称"
          min-width="200"
        />
        <el-table-column
          prop="level"
          label="层级"
          width="80"
        />
        <el-table-column
          prop="sort"
          label="排序"
          width="80"
        />
        <el-table-column
          label="状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="显示状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag :type="row.isShow === 1 ? 'success' : 'info'">
              {{ row.isShow === 1 ? '显示' : '隐藏' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="description"
          label="描述"
          min-width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="180"
        />
        <el-table-column 
          label="操作" 
          width="280" 
          fixed="right"
        >
          <template #default="{ row }">
            <div class="action-buttons">
            <el-button 
              v-if="hasPermission(PERMISSIONS.GOODS.CATEGORY.CREATE)"
              type="primary" 
                size="small"
                class="action-btn add-btn"
              @click="handleAddChild(row)"
            >
                <el-icon><Plus /></el-icon>
              添加子级
            </el-button>
            <el-button 
              v-if="hasPermission(PERMISSIONS.GOODS.CATEGORY.EDIT)"
              type="primary" 
                size="small"
                class="action-btn edit-btn"
              @click="handleEdit(row)"
            >
                <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button 
              v-if="hasPermission(PERMISSIONS.GOODS.CATEGORY.DELETE)"
              type="danger" 
                size="small"
                class="action-btn delete-btn"
              @click="handleDelete(row)"
            >
                <el-icon><Delete /></el-icon>
              删除
            </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="100px"
      >
        <el-form-item
          label="上级分类"
          prop="parentId"
        >
          <el-tree-select
            v-model="formData.parentId"
            :data="categoryTreeData"
            :props="{ value: 'id', label: 'categoryName', children: 'children' }"
            placeholder="请选择上级分类（不选择为顶级分类）"
            clearable
            check-strictly
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="分类名称"
          prop="categoryName"
        >
          <el-input
            v-model="formData.categoryName"
            placeholder="请输入分类名称"
          />
        </el-form-item>

        <el-form-item
          label="排序"
          prop="sort"
        >
          <el-input-number
            v-model="formData.sort"
            :min="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="状态"
          prop="status"
        >
          <el-radio-group v-model="formData.status">
            <el-radio :label="1">
              启用
            </el-radio>
            <el-radio :label="0">
              禁用
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="是否显示"
          prop="isShow"
        >
          <el-radio-group v-model="formData.isShow">
            <el-radio :label="1">
              显示
            </el-radio>
            <el-radio :label="0">
              隐藏
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="分类描述"
          prop="description"
        >
          <el-input 
            v-model="formData.description" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入分类描述" 
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Sort, Edit, Delete } from '@element-plus/icons-vue'
import {
  getCategoryTree,
  getAdminCategoryTree,
  createCategory,
  updateCategory,
  deleteCategory
} from '@/api/category'
import { formatDateTime } from '@/utils/format'
import { hasPermission, PERMISSIONS } from '@/utils/permission'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const categoryList = ref([])
const categoryTreeData = ref([])
const isExpanded = ref(false)

// 搜索表单
const searchForm = reactive({
  categoryName: '',
  status: null,
  isShow: null
})

// 对话框相关
const dialogVisible = ref(false)
const dialogTitle = computed(() => formData.id ? '编辑分类' : '新增分类')
const formRef = ref()

// 表单数据
const formData = reactive({
  id: null,
  parentId: null,
  categoryName: '',
  sort: 0,
  status: 1,
  isShow: 1,
  description: ''
})

// 表单验证规则
const formRules = {
  categoryName: [
    { required: true, message: '请输入分类名称', trigger: 'blur' }
  ],
  sort: [
    { required: true, message: '请输入排序值', trigger: 'blur' }
  ]
}

// 方法
const loadCategoryList = async () => {
  loading.value = true
  try {
    const response = await getAdminCategoryTree()
    if (response.success) {
      categoryList.value = response.data
      // 构建树选择器数据（去除当前编辑项及其子项）
      categoryTreeData.value = buildTreeSelectData(response.data)
    }
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  } finally {
    loading.value = false
  }
}

const buildTreeSelectData = (data, excludeId = null) => {
  return data.filter(item => item.id !== excludeId).map(item => ({
    id: item.id,
    categoryName: item.categoryName,
    children: item.children ? buildTreeSelectData(item.children, excludeId) : []
  }))
}

const handleSearch = () => {
  // 实现搜索逻辑
  const filteredData = filterTreeData(categoryList.value, searchForm)
  categoryList.value = filteredData
}

const filterTreeData = (data, filters) => {
  return data.filter(item => {
    let match = true
    
    if (filters.categoryName && !item.categoryName.includes(filters.categoryName)) {
      match = false
    }
    if (filters.status !== null && item.status !== filters.status) {
      match = false
    }
    if (filters.isShow !== null && item.isShow !== filters.isShow) {
      match = false
    }
    
    // 如果有子项匹配，也应该显示父项
    if (!match && item.children && item.children.length > 0) {
      const filteredChildren = filterTreeData(item.children, filters)
      if (filteredChildren.length > 0) {
        item.children = filteredChildren
        match = true
      }
    }
    
    return match
  })
}

const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = key.includes('status') || key.includes('Show') ? null : ''
  })
  loadCategoryList()
}

const handleRefresh = () => {
  loadCategoryList()
}

const handleExpandAll = () => {
  isExpanded.value = !isExpanded.value
}

const handleAdd = () => {
  resetFormData()
  // 更新树选择器数据，确保显示正确的分类名称
  categoryTreeData.value = buildTreeSelectData(categoryList.value)
  dialogVisible.value = true
}

const handleAddChild = (row) => {
  resetFormData()
  formData.parentId = row.id
  // 更新树选择器数据，确保显示正确的分类名称
  categoryTreeData.value = buildTreeSelectData(categoryList.value)
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.keys(formData).forEach(key => {
    formData[key] = row[key] || (key === 'sort' ? 0 : 
      key === 'status' || key === 'isShow' ? (row[key] ?? 1) : 
        key === 'parentId' ? (row[key] || null) : '')
  })
  // 更新树选择器数据，排除当前编辑项
  categoryTreeData.value = buildTreeSelectData(categoryList.value, row.id)
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确定要删除分类"${row.categoryName}"吗？删除后子分类也将被删除！`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const response = await deleteCategory(row.id)
      if (response.success) {
        ElMessage.success('删除成功')
        loadCategoryList()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  })
}



const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const response = formData.id 
      ? await updateCategory(formData.id, formData)
      : await createCategory(formData)
      
    if (response.success) {
      ElMessage.success(formData.id ? '更新成功' : '创建成功')
      dialogVisible.value = false
      loadCategoryList()
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
    formData[key] = key === 'status' || key === 'isShow' ? 1 :
      key === 'sort' ? 0 :
        key === 'id' || key === 'parentId' ? null : ''
  })
}

// 生命周期
onMounted(() => {
  loadCategoryList()
})
</script>

<style scoped>
.category-list {
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

.category-name {
  display: flex;
  align-items: center;
}

.dialog-footer {
  text-align: right;
}

/* 操作按钮样式 */
.action-buttons {
  display: flex;
  gap: 4px;
  align-items: center;
  justify-content: flex-start;
  flex-wrap: wrap;
}

.action-btn {
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.3s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  border: 1px solid transparent;
}

.action-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.action-btn .el-icon {
  margin-right: 4px;
}

.add-btn {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
  border-color: #67c23a;
  color: white;
}

.add-btn:hover {
  background: linear-gradient(135deg, #529b2e 0%, #67c23a 100%);
  border-color: #529b2e;
}

.edit-btn {
  background: linear-gradient(135deg, #409eff 0%, #66b1ff 100%);
  border-color: #409eff;
  color: white;
}

.edit-btn:hover {
  background: linear-gradient(135deg, #337ecc 0%, #409eff 100%);
  border-color: #337ecc;
}

.delete-btn {
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
  border-color: #f56c6c;
  color: white;
}

.delete-btn:hover {
  background: linear-gradient(135deg, #dd6161 0%, #f56c6c 100%);
  border-color: #dd6161;
}
</style> 