<template>
  <div class="permission-container">
    <!-- 固定顶部区域 -->
    <div class="fixed-header">
      <!-- 操作栏 -->
      <div class="action-bar">
        <el-button 
          v-if="hasActionPermission(PERMISSIONS.SYS.PERMISSION.CREATE)"
          type="primary" 
          @click="handleAddTopLevel"
        >
          新增一级权限
        </el-button>
      
        <!-- 视图切换 -->
        <el-radio-group
          v-model="viewMode"
          class="ml-4"
        >
          <el-radio-button label="tree">
            树形视图
          </el-radio-button>
          <el-radio-button label="list">
            列表视图
          </el-radio-button>
        </el-radio-group>
      
        <el-button
          v-if="viewMode === 'tree'"
          type="success"
          @click="expandAll"
        >
          展开全部
        </el-button>
        <el-button
          v-if="viewMode === 'tree'"
          type="info"
          @click="collapseAll"
        >
          折叠全部
        </el-button>
      
        <!-- 权限同步功能 -->
        <el-divider direction="vertical" />
        <el-button 
          v-if="hasActionPermission(PERMISSIONS.SYS.PERMISSION.SYNC)"
          type="warning" 
          :loading="syncLoading" 
          @click="handleSyncPermissions"
        >
          <el-icon><Refresh /></el-icon>
          同步权限
        </el-button>
      </div>

      <!-- 搜索栏 - 仅在列表视图显示 -->
      <div
        v-if="viewMode === 'list'"
        class="search-bar"
      >
        <el-form
          :model="searchForm"
          inline
          @submit.prevent="handleSearch"
        >
          <el-form-item label="权限名称">
            <el-input
              v-model="searchForm.permissionName"
              placeholder="请输入权限名称"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="权限编码">
            <el-input
              v-model="searchForm.permissionCode"
              placeholder="请输入权限编码"
              clearable
              style="width: 200px"
              @keyup.enter="handleSearch"
            />
          </el-form-item>
          <el-form-item label="权限类型">
            <el-select
              v-model="searchForm.permissionType"
              placeholder="请选择权限类型"
              clearable
              style="width: 150px"
              @change="handleSearch"
            >
              <el-option
                label="一级权限"
                :value="1"
              />
              <el-option
                label="二级权限"
                :value="2"
              />
              <el-option
                label="三级权限"
                :value="3"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
              style="width: 120px"
              @change="handleSearch"
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
          <el-form-item>
            <el-button
              type="primary"
              @click="handleSearch"
            >
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleResetSearch">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 权限类型说明 -->
      <div class="permission-type-info">
        <div class="info-item">
          <el-tag
            type="primary"
            size="small"
          >
            一级权限
          </el-tag>
          <span class="info-text">模块级权限，如：系统管理、组织架构、商品管理等</span>
        </div>
        <div class="info-item">
          <el-tag
            type="success"
            size="small"
          >
            二级权限
          </el-tag>
          <span class="info-text">功能级权限，如：用户管理、角色管理、权限管理等</span>
        </div>
        <div class="info-item">
          <el-tag
            type="warning"
            size="small"
          >
            三级权限
          </el-tag>
          <span class="info-text">操作级权限，如：查看、新增、编辑、删除等</span>
        </div>
      </div>
    </div>

    <!-- 内容区域 -->
    <div class="content-area">
      <!-- 树形视图 -->
      <div
        v-if="viewMode === 'tree'"
        class="tree-view"
      >
        <el-table
          ref="permissionTableRef"
          v-loading="tableLoading"
          :data="tableData"
          row-key="id"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          :default-expand-all="false"
          :row-class-name="getRowClassName"
          border
          stripe
          class="permission-table"
          height="600"
          style="width: 100%"
          @expand-change="handleExpandChange"
        >
          <el-table-column
            prop="permissionName"
            label="权限名称"
            min-width="200"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <div
                class="permission-name-cell"
                :class="{ 'disabled-permission': row.status === 0 }"
              >
                <el-tag 
                  :type="getPermissionTypeTag(row.permissionType)" 
                  size="small" 
                  class="permission-type-tag"
                >
                  {{ getPermissionTypeLabel(row.permissionType) }}
                </el-tag>
                <span class="permission-name">{{ row.permissionName }}</span>
                <el-tooltip 
                  v-if="getPermissionDisplayName(row.permissionCode) !== row.permissionName"
                  :content="`友好名称: ${getPermissionDisplayName(row.permissionCode)}`"
                  placement="top"
                >
                  <el-icon class="friendly-name-icon">
                    <InfoFilled />
                  </el-icon>
                </el-tooltip>
                <!-- 禁用状态提示 -->
                <el-tooltip 
                  v-if="row.status === 0"
                  content="权限已禁用，无法展开查看子权限"
                  placement="top"
                >
                  <el-icon class="disabled-icon">
                    <Lock />
                  </el-icon>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>
        
          <el-table-column
            label="权限编码"
            min-width="250"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <div class="permission-code-cell">
                <code class="permission-code">{{ row.permissionCode }}</code>
                <el-tag 
                  size="small" 
                  type="info" 
                  class="friendly-display"
                >
                  {{ getPermissionDisplayName(row.permissionCode) }}
                </el-tag>
              </div>
            </template>
          </el-table-column>
        
          <el-table-column
            prop="description"
            label="描述"
            min-width="200"
            show-overflow-tooltip
          />
        
          <el-table-column
            prop="status"
            label="状态"
            width="80"
            align="center"
          >
            <template #default="{ row }">
              <el-tag
                :type="row.status === 1 ? 'success' : 'danger'"
                size="small"
              >
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
        
          <el-table-column
            prop="sortOrder"
            label="排序"
            width="80"
            align="center"
          />
        
          <el-table-column
            prop="createTime"
            label="创建时间"
            width="180"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDateTime(row.createTime) }}
            </template>
          </el-table-column>
        
          <el-table-column
            label="操作"
            width="240"
            align="center"
            fixed="right"
          >
            <template #default="{ row }">
              <div class="action-buttons-container">
                <el-button
                  v-if="hasActionPermission(PERMISSIONS.SYS.PERMISSION.CREATE) && row.permissionType < 3"
                  type="primary"
                  size="small"
                  @click="handleAddChild(row)"
                >
                  新增子权限
                </el-button>
                <el-button
                  v-if="hasActionPermission(PERMISSIONS.SYS.PERMISSION.EDIT)"
                  type="warning"
                  size="small"
                  @click="handleEdit(row)"
                >
                  编辑
                </el-button>
                <el-button
                  v-if="hasActionPermission(PERMISSIONS.SYS.PERMISSION.DELETE) && !isCorePermission(row)"
                  type="danger"
                  size="small"
                  @click="handleDelete(row)"
                >
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 列表视图 -->
      <div
        v-if="viewMode === 'list'"
        class="list-view"
      >
        <el-table
          v-loading="listLoading"
          :data="listData"
          border
          stripe
          class="permission-list-table"
          height="600"
          style="width: 100%"
        >
          <el-table-column
            prop="permissionName"
            label="权限名称"
            min-width="200"
            show-overflow-tooltip
          >
            <template #default="{ row }">
              <div class="permission-name-cell">
                <el-tag 
                  :type="getPermissionTypeTag(row.permissionType)" 
                  size="small" 
                  class="permission-type-tag"
                >
                  {{ getPermissionTypeLabel(row.permissionType) }}
                </el-tag>
                <span class="permission-name">{{ row.permissionName }}</span>
              </div>
            </template>
          </el-table-column>
        
          <el-table-column
            prop="permissionCode"
            label="权限编码"
            min-width="250"
            show-overflow-tooltip
          />
        
          <el-table-column
            prop="parentId"
            label="父权限"
            width="120"
            align="center"
          >
            <template #default="{ row }">
              <span v-if="row.parentId === 0">顶级权限</span>
              <span v-else>{{ getParentPermissionName(row.parentId) }}</span>
            </template>
          </el-table-column>
        
          <el-table-column
            prop="description"
            label="描述"
            min-width="200"
            show-overflow-tooltip
          />
        
          <el-table-column
            prop="status"
            label="状态"
            width="80"
            align="center"
          >
            <template #default="{ row }">
              <el-tag
                :type="row.status === 1 ? 'success' : 'danger'"
                size="small"
              >
                {{ row.status === 1 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
        
          <el-table-column
            prop="sortOrder"
            label="排序"
            width="80"
            align="center"
          />
        
          <el-table-column
            prop="createTime"
            label="创建时间"
            width="180"
            align="center"
          >
            <template #default="{ row }">
              {{ formatDateTime(row.createTime) }}
            </template>
          </el-table-column>
        
          <el-table-column
            label="操作"
            width="240"
            align="center"
            fixed="right"
          >
            <template #default="{ row }">
              <div class="action-buttons-container">
                <el-button
                  v-if="hasActionPermission(PERMISSIONS.SYS.PERMISSION.CREATE) && row.permissionType < 3"
                  type="primary"
                  size="small"
                  @click="handleAddChild(row)"
                >
                  新增子权限
                </el-button>
                <el-button
                  v-if="hasActionPermission(PERMISSIONS.SYS.PERMISSION.EDIT)"
                  type="warning"
                  size="small"
                  @click="handleEdit(row)"
                >
                  编辑
                </el-button>
                <el-button
                  v-if="hasActionPermission(PERMISSIONS.SYS.PERMISSION.EDIT) && !isCorePermission(row)"
                  :type="row.status === 1 ? 'info' : 'success'"
                  size="small"
                  @click="handleToggleStatus(row)"
                >
                  {{ row.status === 1 ? '禁用' : '启用' }}
                </el-button>
                <el-button
                  v-if="hasActionPermission(PERMISSIONS.SYS.PERMISSION.DELETE) && !isCorePermission(row)"
                  type="danger"
                  size="small"
                  @click="handleDelete(row)"
                >
                  删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <div class="pagination-container">
          <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :total="pagination.total"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>

      <!-- 权限表单对话框 -->
      <el-dialog
        v-model="dialogVisible"
        :title="formTitle"
        width="500px"
        :close-on-click-modal="false"
      >
        <el-form
          ref="permissionFormRef"
          :model="permissionForm"
          :rules="permissionRules"
          label-width="120px"
        >
          <el-form-item
            label="权限名称"
            prop="permissionName"
          >
            <el-input
              v-model="permissionForm.permissionName"
              placeholder="请输入权限名称"
            />
          </el-form-item>
          <el-form-item
            label="权限编码"
            prop="permissionCode"
          >
            <el-input
              v-model="permissionForm.permissionCode"
              placeholder="请输入权限编码"
            />
          </el-form-item>
          <el-form-item
            label="权限类型"
            prop="permissionType"
          >
            <el-radio-group
              v-model="permissionForm.permissionType"
              @change="onPermissionTypeChange"
            >
              <el-radio :label="1">
                一级权限(模块)
              </el-radio>
              <el-radio :label="2">
                二级权限(子模块)
              </el-radio>
              <el-radio :label="3">
                三级权限(操作)
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item
            v-if="permissionForm.permissionType === 2"
            label="上级模块"
          >
            <el-select
              v-model="permissionForm.parentId"
              placeholder="请选择上级模块"
              style="width: 100%"
            >
              <el-option
                v-for="item in moduleOptions"
                :key="item.id"
                :label="item.permissionName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="permissionForm.permissionType === 3"
            label="上级子模块"
          >
            <el-select
              v-model="permissionForm.parentId"
              placeholder="请选择上级子模块"
              style="width: 100%"
            >
              <el-option
                v-for="item in submoduleOptions"
                :key="item.id"
                :label="item.permissionName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="permissionForm.permissionType === 2"
            label="关联菜单"
          >
            <el-select
              v-model="permissionForm.menuId"
              placeholder="请选择关联菜单"
              style="width: 100%"
              clearable
            >
              <el-option
                v-for="item in menuOptions"
                :key="item.id"
                :label="item.menuName"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="permissionForm.permissionType === 3"
            label="权限编码提示"
          >
            <div class="text-sm text-gray-500">
              <p>三级权限编码格式：子模块编码:操作名称</p>
              <p>例如：user-management:view, org-management:create</p>
            </div>
          </el-form-item>
          <el-form-item
            label="描述"
            prop="description"
          >
            <el-input
              v-model="permissionForm.description"
              type="textarea"
              placeholder="请输入描述"
            />
          </el-form-item>
          <el-form-item
            label="状态"
            prop="status"
          >
            <el-switch
              v-model="permissionForm.status"
              :active-value="1"
              :inactive-value="0"
              active-text="启用"
              inactive-text="禁用"
              :disabled="isCorePermission(permissionForm)"
            />
            <div
              v-if="isCorePermission(permissionForm)"
              class="text-gray-500 text-sm mt-1"
            >
              核心模块不允许禁用
            </div>
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">
            取消
          </el-button>
          <el-button
            type="primary"
            @click="submitForm"
          >
            确定
          </el-button>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPermissionTree, getPermissionTreeForAdmin, getPermissionById, createPermission, updatePermission, deletePermission, getPermissionPage, updatePermissionStatus } from '@/api/permission'
import { getMenuList } from '@/api/menu'
import { Refresh, Tools, Delete, Search, InfoFilled, Lock } from '@element-plus/icons-vue'
import { hasActionPermission, PERMISSIONS } from '@/utils/permission'
import { getPermissionDisplayName } from '@/utils/displayMapping'


// 表格数据和加载状态
const tableData = ref([])
const tableLoading = ref(false)
const permissionTableRef = ref(null)

// 列表数据和加载状态
const listData = ref([])
const listLoading = ref(false)
const permissionListRef = ref(null)

// 搜索表单
const searchForm = reactive({
  permissionName: '',
  permissionCode: '',
  permissionType: null,
  status: null
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 视图模式
const viewMode = ref('tree')

// 权限表单相关
const dialogVisible = ref(false)
const formTitle = ref('')
const permissionFormRef = ref(null)
const permissionForm = reactive({
  id: null,
  permissionName: '',
  permissionCode: '',
  permissionType: 1,
  parentId: 0,
  menuId: null,
  description: '',
  status: 1
})
const permissionRules = {
  permissionName: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  permissionCode: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
  permissionType: [{ required: true, message: '请选择权限类型', trigger: 'change' }]
}

// 权限选项和菜单选项
const moduleOptions = ref([]) // 一级权限（模块）选项
const submoduleOptions = ref([]) // 二级权限（子模块）选项
const menuOptions = ref([])

// 权限同步加载状态
const syncLoading = ref(false)



// 监听视图模式变化
watch(viewMode, (newMode) => {
  if (newMode === 'tree') {
    fetchPermissionList()
  } else {
    fetchPermissionListData()
  }
})

// 初始化
onMounted(() => {
  fetchPermissionList()
  fetchMenuList()
})

// 格式化时间
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  const date = new Date(dateTimeStr)
  return date.toLocaleString()
}

// 获取权限列表（树形视图）
const fetchPermissionList = async () => {
  try {
    tableLoading.value = true
    const { data } = await getPermissionTreeForAdmin()
    console.log('权限数据结构:', JSON.stringify(data, null, 2))
    tableData.value = data || []
    
    // 获取不同级别的权限作为选项
    updatePermissionOptions(data || [])
    
    // 确保表格默认折叠状态
    nextTick(() => {
      if (permissionTableRef.value) {
        // 强制折叠所有行
        tableData.value.forEach(row => {
          permissionTableRef.value.toggleRowExpansion(row, false)
        })
      }
    })
  } catch (error) {
    ElMessage.error('获取权限列表失败')
    console.error(error)
  } finally {
    tableLoading.value = false
  }
}

// 获取权限列表数据（列表视图）
const fetchPermissionListData = async () => {
  try {
    listLoading.value = true
    const params = {
      page: pagination.current - 1, // 后端从0开始
      size: pagination.size,
      permissionName: searchForm.permissionName,
      permissionCode: searchForm.permissionCode,
      permissionType: searchForm.permissionType,
      status: searchForm.status
    }
    const { data } = await getPermissionPage(params)
    console.log('分页权限数据:', data)
    listData.value = data.data || []
    pagination.total = data.total || 0
    
    // 更新权限选项
    updatePermissionOptions(data.data || [])
  } catch (error) {
    ElMessage.error('获取权限列表失败')
    console.error(error)
  } finally {
    listLoading.value = false
  }
}

// 更新权限选项
const updatePermissionOptions = (data) => {
  // 一级权限（模块）
  moduleOptions.value = data.filter(item => item.permissionType === 1) || []
  
  // 二级权限（子模块）- 递归收集所有二级权限
  submoduleOptions.value = []
  const collectSubmodules = (items) => {
    items.forEach(item => {
      if (item.permissionType === 2) {
        submoduleOptions.value.push(item)
      }
      if (item.children && item.children.length > 0) {
        collectSubmodules(item.children)
      }
    })
  }
  collectSubmodules(data)
}

// 获取菜单列表
const fetchMenuList = async () => {
  try {
    const { data } = await getMenuList()
    menuOptions.value = data || []
  } catch (error) {
    console.error('获取菜单列表失败:', error)
  }
}

// 获取权限类型标签样式
const getPermissionTypeTag = (type) => {
  switch (type) {
  case 1: return 'primary'
  case 2: return 'success'
  case 3: return 'warning'
  default: return 'info'
  }
}

// 获取权限类型标签文本
const getPermissionTypeLabel = (type) => {
  switch (type) {
  case 1: return '一级权限'
  case 2: return '二级权限'
  case 3: return '三级权限'
  default: return '未知类型'
  }
}

// 获取父权限名称
const getParentPermissionName = (parentId) => {
  if (parentId === 0) return '顶级权限'
  const parent = moduleOptions.value.find(item => item.id === parentId) || 
                submoduleOptions.value.find(item => item.id === parentId)
  return parent ? parent.permissionName : '未知父权限'
}

// 展开全部
const expandAll = () => {
  if (permissionTableRef.value) {
    tableData.value.forEach(row => {
      permissionTableRef.value.toggleRowExpansion(row, true)
      if (row.children) {
        expandChildren(row.children)
      }
    })
  }
}

// 递归展开子节点
const expandChildren = (children) => {
  children.forEach(child => {
    if (permissionTableRef.value) {
      permissionTableRef.value.toggleRowExpansion(child, true)
      if (child.children) {
        expandChildren(child.children)
      }
    }
  })
}

// 折叠全部
const collapseAll = () => {
  if (permissionTableRef.value) {
    tableData.value.forEach(row => {
      permissionTableRef.value.toggleRowExpansion(row, false)
    })
  }
}

// 获取行样式类名
const getRowClassName = ({ row }) => {
  if (row.status === 0) {
    return 'disabled-permission-row'
  }
  return ''
}

// 处理展开变化事件
const handleExpandChange = (row, expandedRows) => {
  // 如果权限被禁用，阻止展开
  if (row.status === 0) {
    ElMessage.warning(`权限"${row.permissionName}"已禁用，无法展开查看子权限`)
    // 强制折叠
    if (permissionTableRef.value) {
      permissionTableRef.value.toggleRowExpansion(row, false)
    }
    return false
  }
}

// 新增一级权限
const handleAddTopLevel = () => {
  resetForm()
  permissionForm.permissionType = 1
  permissionForm.parentId = 0
  formTitle.value = '新增一级权限'
  dialogVisible.value = true
}

// 新增子权限（用于树形视图）
const handleAddChild = (row) => {
  if (row.permissionType === 1) {
    handleAddSubmodule(row)
  } else if (row.permissionType === 2) {
    handleAddAction(row)
  }
}

// 新增子模块（二级权限）
const handleAddSubmodule = (parentRow) => {
  resetForm()
  permissionForm.permissionType = 2
  permissionForm.parentId = parentRow.id
  formTitle.value = `为"${parentRow.permissionName}"新增子模块`
  dialogVisible.value = true
}

// 新增操作（三级权限）
const handleAddAction = (parentRow) => {
  resetForm()
  permissionForm.permissionType = 3
  permissionForm.parentId = parentRow.id
  formTitle.value = `为"${parentRow.permissionName}"新增操作权限`
  dialogVisible.value = true
}

// 编辑权限
const handleEdit = async (row) => {
  try {
    const { data } = await getPermissionById(row.id)
    Object.assign(permissionForm, data)
    formTitle.value = '编辑权限'
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取权限详情失败')
  }
}

// 删除权限
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除权限"${row.permissionName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deletePermission(row.id)
    ElMessage.success('删除成功')
    
    // 刷新列表
    if (viewMode.value === 'tree') {
      fetchPermissionList()
    } else {
      fetchPermissionListData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 搜索权限
const handleSearch = () => {
  pagination.current = 1
  fetchPermissionListData()
}

// 重置搜索
const handleResetSearch = () => {
  Object.assign(searchForm, {
    permissionName: '',
    permissionCode: '',
    permissionType: null,
    status: null
  })
  pagination.current = 1
  fetchPermissionListData()
}

// 分页大小变化
const handleSizeChange = (val) => {
  pagination.size = val
  fetchPermissionListData()
}

// 当前页变化
const handleCurrentChange = (val) => {
  pagination.current = val
  fetchPermissionListData()
}

// 提交表单
const submitForm = async () => {
  try {
    await permissionFormRef.value.validate()
    
    if (permissionForm.id) {
      await updatePermission(permissionForm.id, permissionForm)
      ElMessage.success('更新成功')
    } else {
      await createPermission(permissionForm)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    
    // 刷新列表
    if (viewMode.value === 'tree') {
      fetchPermissionList()
    } else {
      fetchPermissionListData()
    }
  } catch (error) {
    console.error('权限更新失败:', error)
    console.error('错误详情:', {
      message: error.message,
      response: error.response,
      status: error.response?.status,
      data: error.response?.data
    })
    
    let errorMessage = '操作失败'
    if (error.response?.data?.message) {
      errorMessage = error.response.data.message
    } else if (error.response?.status) {
      errorMessage = `操作失败 (状态码: ${error.response.status})`
    }
    
    ElMessage.error(errorMessage)
  }
}

// 重置表单
const resetForm = () => {
  Object.assign(permissionForm, {
    id: null,
    permissionName: '',
    permissionCode: '',
    permissionType: 1,
    parentId: 0,
    menuId: null,
    description: '',
    status: 1
  })
}

// 判断是否为核心权限
const isCorePermission = (permission) => {
  const corePermissions = [
    'system', 'profile', 'permission-management', 'menu-management', 'user-management', 'role-management'
  ]
  
  // 直接匹配权限编码
  if (corePermissions.includes(permission.permissionCode)) {
    return true
  }
  
  // 对于三级权限，检查其父级权限编码（权限编码的前缀部分）
  if (permission.permissionCode && permission.permissionCode.includes(':')) {
    const parentCode = permission.permissionCode.split(':')[0]
    return corePermissions.includes(parentCode)
  }
  
  return false
}

// 权限同步（实际上是刷新权限列表）
const handleSyncPermissions = async () => {
  try {
    syncLoading.value = true
    
    // 直接刷新列表（从数据库获取最新权限数据）
    if (viewMode.value === 'tree') {
      await fetchPermissionList()
    } else {
      await fetchPermissionListData()
    }
    
    ElMessage.success('权限列表已刷新')
  } catch (error) {
    ElMessage.error('刷新失败')
  } finally {
    syncLoading.value = false
  }
}

// 切换权限状态
const handleToggleStatus = async (row) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    const statusText = newStatus === 1 ? '启用' : '禁用'
    
    // 构建确认消息，提示级联影响
    let confirmMessage = `确定要${statusText}权限"${row.permissionName}"吗？`
    
    if (newStatus === 0) {
      // 禁用操作：提示会影响子权限
      if (row.permissionType < 3) {
        confirmMessage += '\n\n注意：禁用此权限将同时禁用其下所有子权限。'
      }
    } else {
      // 启用操作：提示会影响父权限
      if (row.parentId) {
        confirmMessage += '\n\n注意：启用此权限将同时启用其父级权限。'
      }
    }
    
    await ElMessageBox.confirm(
      confirmMessage,
      '确认操作',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: false
      }
    )
    
    await updatePermissionStatus(row.id, newStatus)
    ElMessage.success(`${statusText}成功`)
    
    // 刷新列表
    if (viewMode.value === 'tree') {
      fetchPermissionList()
    } else {
      fetchPermissionListData()
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}



</script>

<style scoped>
.permission-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #fff;
}

/* 固定头部区域 */
.fixed-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  padding: 20px 20px 0 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 内容区域 */
.content-area {
  flex: 1;
  overflow: auto;
  padding: 20px;
}
.action-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  align-items: center;
  flex-wrap: wrap;
}
.permission-path {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 12px;
  color: #606266;
  background-color: #f5f7fa;
  padding: 2px 6px;
  border-radius: 3px;
}
.text-gray-500 {
  color: #6b7280;
}
.text-sm {
  font-size: 0.875rem;
}
.text-sm p {
  margin: 2px 0;
}

/* 表格样式优化 */
:deep(.el-table) {
  font-size: 14px;
}

:deep(.el-table .el-table__header-wrapper th) {
  background-color: #fafafa;
  color: #606266;
  font-weight: 600;
  border-bottom: 2px solid #e4e7ed;
}

:deep(.el-table .el-table__row) {
  transition: all 0.2s ease;
}

:deep(.el-table .el-table__row:hover) {
  background-color: #f8f9fa;
}

/* 表格滚动条样式 */
:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-corner {
  background: #f1f1f1;
}

/* 禁用行样式 */
:deep(.el-table .el-table__row.disabled-row) {
  opacity: 0.6;
  background-color: #fef0f0 !important;
}

:deep(.el-table .el-table__row.disabled-row:hover) {
  background-color: #fde2e2 !important;
}

:deep(.el-table .el-table__row.disabled-row .permission-name-text) {
  text-decoration: line-through;
  color: #c0c4cc !important;
}

/* 权限编码样式 */
.permission-code-wrapper {
  display: flex;
  align-items: center;
}

.permission-code {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 12px;
  background-color: #f5f7fa;
  padding: 2px 6px;
  border-radius: 3px;
  border: 1px solid #e4e7ed;
  color: #606266;
}

.level-1 .permission-code {
  background-color: #ecf5ff;
  border-color: #b3d8ff;
  color: #409eff;
  font-weight: 600;
}

.level-2 .permission-code {
  background-color: #f0f9ff;
  border-color: #b3e19d;
  color: #67c23a;
  font-weight: 500;
}

.level-3 .permission-code {
  background-color: #fdf6ec;
  border-color: #f5dab1;
  color: #e6a23c;
  font-weight: 400;
}



/* 按钮间距优化 */
:deep(.el-button + .el-button) {
  margin-left: 8px;
}

/* 操作按钮容器样式 */
.action-buttons-container {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  justify-content: center;
  align-items: center;
  min-height: 32px;
  padding: 4px 0;
}

.action-buttons-container .el-button {
  margin: 0;
  font-size: 12px;
  padding: 5px 10px;
  min-width: 60px;
  height: 28px;
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.action-buttons-container .el-button + .el-button {
  margin-left: 0;
}

/* 操作按钮样式优化 */
.action-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  justify-content: center;
  align-items: center;
}

.action-buttons .el-button {
  margin: 0;
  font-size: 12px;
  padding: 5px 8px;
  min-width: auto;
}

.action-buttons .el-button + .el-button {
  margin-left: 0;
}

/* 标签样式优化 */
:deep(.el-tag) {
  border-radius: 4px;
  font-weight: 500;
}

/* 权限名称层级样式 */
.permission-name-wrapper {
  display: flex;
  align-items: center;
  width: 100%;
}

.level-indent {
  display: inline-block;
  width: 20px;
  height: 1px;
}

.level-icon {
  margin-right: 6px;
  color: #909399;
  font-size: 12px;
  width: 20px;
  display: inline-flex;
  align-items: center;
  justify-content: flex-start;
  font-family: monospace;
}

.level-2-icon {
  color: #67c23a;
  font-weight: bold;
}

.level-3-icon {
  color: #e6a23c;
  font-weight: bold;
}

.permission-name-text {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 不同层级的权限名称样式 */
.level-1 .permission-name-text {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

.level-2 .permission-name-text {
  font-weight: 500;
  color: #606266;
  font-size: 13px;
}

.level-3 .permission-name-text {
  font-weight: 400;
  color: #909399;
  font-size: 12px;
}

/* 层级标签样式 */
.level-tag-1 {
  background-color: #409eff !important;
  border-color: #409eff !important;
  font-weight: 600 !important;
}

.level-tag-2 {
  background-color: #67c23a !important;
  border-color: #67c23a !important;
  font-weight: 500 !important;
}

.level-tag-3 {
  background-color: #e6a23c !important;
  border-color: #e6a23c !important;
  font-weight: 400 !important;
}

/* 表格行的层级背景色 */
:deep(.el-table .el-table__row.level-1) {
  background-color: #f8fbff;
}

:deep(.el-table .el-table__row.level-2) {
  background-color: #f8fff8;
}

:deep(.el-table .el-table__row.level-3) {
  background-color: #fffbf0;
}

:deep(.el-table .el-table__row.level-1:hover) {
  background-color: #ecf5ff;
}

:deep(.el-table .el-table__row.level-2:hover) {
  background-color: #f0f9ff;
}

:deep(.el-table .el-table__row.level-3:hover) {
  background-color: #fdf6ec;
}

/* 搜索栏样式 */
.search-bar {
  margin-bottom: 10px;
  padding: 10px 0;
  background-color: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 10px;
}

/* 树形视图样式 */
.tree-view {
  margin-top: 10px;
}

/* 禁用权限行样式 */
.permission-table :deep(.disabled-permission-row) {
  background-color: #f5f7fa !important;
  opacity: 0.7;
  color: #c0c4cc;
}

.permission-table :deep(.disabled-permission-row:hover) {
  background-color: #e9ecef !important;
}

.permission-table :deep(.disabled-permission-row .el-table__expand-icon) {
  color: #c0c4cc;
  cursor: not-allowed;
}

.permission-table :deep(.disabled-permission-row .permission-name) {
  color: #c0c4cc;
  text-decoration: line-through;
}

.permission-table :deep(.disabled-permission-row .permission-code) {
  color: #c0c4cc;
  background-color: #f0f0f0;
  border-color: #d3d3d3;
}

.permission-table :deep(.disabled-permission-row .el-tag) {
  opacity: 0.6;
}

/* 禁用权限名称单元格样式 */
.disabled-permission {
  opacity: 0.7;
}

.disabled-permission .permission-name {
  color: #c0c4cc !important;
  text-decoration: line-through;
}

.disabled-permission .disabled-icon {
  color: #f56c6c;
  margin-left: 8px;
  font-size: 14px;
}

/* 列表视图样式 */
.list-view {
  margin-top: 10px;
}

.permission-list-table {
  font-size: 14px;
}

.permission-list-table :deep(.el-table__header-wrapper th) {
  background-color: #fafafa;
  color: #606266;
  font-weight: 600;
  border-bottom: 2px solid #e4e7ed;
}

.permission-list-table :deep(.el-table__row) {
  transition: all 0.2s ease;
}

.permission-list-table :deep(.el-table__row:hover) {
  background-color: #f8f9fa;
}

.permission-list-table :deep(.el-table__row.disabled-row) {
  opacity: 0.6;
  background-color: #fef0f0 !important;
}

.permission-list-table :deep(.el-table__row.disabled-row:hover) {
  background-color: #fde2e2 !important;
}

.permission-list-table :deep(.el-table__row.disabled-row .permission-name-text) {
  text-decoration: line-through;
  color: #c0c4cc !important;
}

.permission-list-table .permission-name-cell {
  display: flex;
  align-items: center;
  gap: 8px;
}

.permission-list-table .permission-type-tag {
  flex-shrink: 0;
}

.permission-list-table .permission-name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.permission-list-table .friendly-name-icon {
  color: #409eff;
  font-size: 14px;
  margin-left: 4px;
  cursor: help;
}

.permission-code-cell {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.permission-code {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 12px;
  color: #666;
  background-color: #f5f7fa;
  padding: 2px 6px;
  border-radius: 3px;
  border: 1px solid #dcdfe6;
}

.friendly-display {
  font-size: 11px;
  background-color: #e1f3d8;
  color: #67c23a;
  border-color: #b3d8a4;
}

.pagination-container {
  margin-top: 15px;
  display: flex;
  justify-content: flex-end;
}

/* 权限类型说明样式 */
.permission-type-info {
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 6px;
  padding: 12px 16px;
  margin-bottom: 10px;
  display: flex;
  gap: 24px;
  flex-wrap: wrap;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-text {
  font-size: 13px;
  color: #666;
  line-height: 1.4;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .action-buttons-container {
    flex-direction: column;
    gap: 4px;
    min-height: auto;
  }
  
  .action-buttons-container .el-button {
    width: 100%;
    min-width: 80px;
    margin: 2px 0;
  }
}

@media (max-width: 480px) {
  .action-buttons-container .el-button {
    font-size: 11px;
    padding: 4px 8px;
    height: 26px;
  }
}
</style> 