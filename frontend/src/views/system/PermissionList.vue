<template>
  <div class="permission-container">
    <div class="action-bar">
      <el-button 
        v-if="hasPermission(PERMISSIONS.SYS.PERMISSION.CREATE)"
        type="primary" 
        @click="handleAddTopLevel"
      >
        新增一级权限
      </el-button>
      <el-button
        type="success"
        @click="expandAll"
      >
        展开全部
      </el-button>
      <el-button
        type="info"
        @click="collapseAll"
      >
        折叠全部
      </el-button>
      
      <!-- 权限同步功能 -->
      <el-divider direction="vertical" />
      <el-button 
        v-if="hasPermission(PERMISSIONS.SYS.PERMISSION.SYNC)"
        type="warning" 
        :loading="syncLoading" 
        @click="handleSyncPermissions"
      >
        <el-icon><Refresh /></el-icon>
        同步权限
      </el-button>
      <el-button 
        v-if="hasPermission(PERMISSIONS.SYS.PERMISSION.RESET)"
        type="danger" 
        :loading="resetLoading" 
        @click="handleResetPermissions"
      >
        <el-icon><Delete /></el-icon>
        重置权限
      </el-button>
      <el-button 
        v-if="hasPermission(PERMISSIONS.SYS.PERMISSION.VALIDATE)"
        type="info" 
        @click="handleValidateConfig"
      >
        <el-icon><Tools /></el-icon>
        验证配置
      </el-button>
    </div>

    <el-table
      ref="permissionTableRef"
      v-loading="tableLoading"
      :data="tableData"
      row-key="id"
      border
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      style="width: 100%; margin-top: 15px"
    >
      <el-table-column
        prop="id"
        label="ID"
        width="80"
      >
        <template #default="scope">
          {{ $formatId(scope.row.id) }}
        </template>
      </el-table-column>
      <el-table-column
        prop="permissionName"
        label="权限名称"
      />
      <el-table-column
        prop="permissionCode"
        label="权限编码"
      />
      <el-table-column
        prop="permissionType"
        label="权限类型"
        width="140"
      >
        <template #default="scope">
          <el-tag :type="getPermissionTypeTag(scope.row.permissionType)">
            {{ getPermissionTypeName(scope.row.permissionType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="permissionPath"
        label="权限路径"
        width="200"
        show-overflow-tooltip
      >
        <template #default="scope">
          <span class="permission-path">{{ scope.row.permissionPath }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="levelDepth"
        label="层级深度"
        width="100"
        align="center"
      >
        <template #default="scope">
          <el-tag size="small" type="info">
            {{ scope.row.levelDepth }}级
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="description"
        label="描述"
      />
      <el-table-column
        prop="status"
        label="状态"
        width="100"
      >
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="180"
      >
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column 
        v-if="hasPermission(PERMISSIONS.SYS.PERMISSION.EDIT) || hasPermission(PERMISSIONS.SYS.PERMISSION.CREATE) || hasPermission(PERMISSIONS.SYS.PERMISSION.DELETE)"
        label="操作" 
        width="280" 
        fixed="right"
      >
        <template #default="scope">
          <el-button 
            v-if="hasPermission(PERMISSIONS.SYS.PERMISSION.EDIT)"
            size="small" 
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button 
            v-if="scope.row.permissionType === 1 && hasPermission(PERMISSIONS.SYS.PERMISSION.CREATE)" 
            size="small" 
            type="success" 
            @click="handleAddSubmodule(scope.row)"
          >
            添加子模块
          </el-button>
          <el-button 
            v-if="scope.row.permissionType === 2 && hasPermission(PERMISSIONS.SYS.PERMISSION.CREATE)" 
            size="small" 
            type="warning" 
            @click="handleAddAction(scope.row)"
          >
            添加操作
          </el-button>
          <el-button 
            v-if="hasPermission(PERMISSIONS.SYS.PERMISSION.DELETE) && !isCorePermission(scope.row)"
            size="small" 
            type="danger" 
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

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
          <el-radio-group v-model="permissionForm.permissionType" @change="onPermissionTypeChange">
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
          <div v-if="isCorePermission(permissionForm)" class="text-gray-500 text-sm mt-1">
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
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPermissionTree, getPermissionTreeForAdmin, getPermissionById, createPermission, updatePermission, deletePermission, syncAllPermissions, validatePermissionConfig, resetAllPermissions } from '@/api/permission'
import { getMenuList } from '@/api/menu'
import { Refresh, Tools, Delete } from '@element-plus/icons-vue'
import { hasPermission, PERMISSIONS } from '@/utils/permission'


// 表格数据和加载状态
const tableData = ref([])
const tableLoading = ref(false)
const permissionTableRef = ref(null)

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
const resetLoading = ref(false)


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

// 获取权限列表
const fetchPermissionList = async () => {
  try {
    tableLoading.value = true
    const { data } = await getPermissionTreeForAdmin()
    tableData.value = data || []
    
    // 获取不同级别的权限作为选项
    updatePermissionOptions(data)
  } catch (error) {
    ElMessage.error('获取权限列表失败')
    console.error(error)
  } finally {
    tableLoading.value = false
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
    console.error('获取菜单列表失败', error)
  }
}

// 展开全部
const expandAll = () => {
  if (permissionTableRef.value) {
    tableData.value.forEach(row => {
      permissionTableRef.value.toggleRowExpansion(row, true)
    })
  }
}

// 折叠全部
const collapseAll = () => {
  if (permissionTableRef.value) {
    tableData.value.forEach(row => {
      permissionTableRef.value.toggleRowExpansion(row, false)
    })
  }
}

// 新增顶级权限
const handleAddTopLevel = () => {
  formTitle.value = '新增一级权限'
  Object.keys(permissionForm).forEach(key => {
    permissionForm[key] = key === 'status' ? 1 : key === 'permissionType' ? 1 : key === 'parentId' ? 0 : null
  })
  dialogVisible.value = true
}

// 新增子模块（二级权限）
const handleAddSubmodule = (row) => {
  formTitle.value = '新增子模块'
  Object.keys(permissionForm).forEach(key => {
    permissionForm[key] = key === 'status' ? 1 : key === 'permissionType' ? 2 : key === 'parentId' ? row.id : null
  })
  dialogVisible.value = true
}

// 新增操作（三级权限）
const handleAddAction = (row) => {
  formTitle.value = '新增操作权限'
  Object.keys(permissionForm).forEach(key => {
    permissionForm[key] = key === 'status' ? 1 : key === 'permissionType' ? 3 : key === 'parentId' ? row.id : null
  })
  dialogVisible.value = true
}

// 编辑权限
const handleEdit = async (row) => {
  formTitle.value = '编辑权限'
  try {
    const { data } = await getPermissionById(row.id)
    Object.keys(permissionForm).forEach(key => {
      permissionForm[key] = data[key]
    })
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取权限信息失败')
    console.error(error)
  }
}

// 提交表单
const submitForm = async () => {
  if (!permissionFormRef.value) return
  
  try {
    await permissionFormRef.value.validate()
    
    // 根据权限类型设置parentId
    if (permissionForm.permissionType === 1) {
      permissionForm.parentId = 0
      permissionForm.menuId = null
    } else if (permissionForm.permissionType === 3) {
      permissionForm.menuId = null
    }
    
    if (permissionForm.id) {
      await updatePermission(permissionForm.id, permissionForm)
      ElMessage.success('更新成功')
    } else {
      await createPermission(permissionForm)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchPermissionList()
  } catch (error) {
    console.error(error)
    ElMessage.error('提交失败')
  }
}

// 删除权限
const handleDelete = (row) => {
  if (row.children && row.children.length > 0) {
    ElMessage.warning('该权限下有子权限，请先删除子权限')
    return
  }
  
  ElMessageBox.confirm('确定要删除该权限吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deletePermission(row.id)
      ElMessage.success('删除成功')
      fetchPermissionList()
    } catch (error) {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }).catch(() => {})
}

// 权限同步功能
const handleSyncPermissions = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要同步权限吗？此操作将根据配置文件自动创建或更新权限，请谨慎操作！',
      '权限同步确认',
      {
        confirmButtonText: '确定同步',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    syncLoading.value = true
    const response = await syncAllPermissions()
    
    ElMessage.success('权限同步成功')
    
    // 显示同步结果
    ElMessageBox.alert(response.data, '同步结果', {
      confirmButtonText: '确定',
      type: 'success'
    })
    
    // 刷新权限列表
    await fetchPermissionList()
    
  } catch (error) {
    if (error !== 'cancel') {
      console.error('权限同步失败:', error)
      ElMessage.error('权限同步失败: ' + (error.response?.data?.message || error.message))
    }
  } finally {
    syncLoading.value = false
  }
}

// 验证权限配置
const handleValidateConfig = async () => {
  try {
    const response = await validatePermissionConfig()
    
    ElMessageBox.alert(response.data, '权限配置验证结果', {
      confirmButtonText: '确定',
      type: 'info'
    })
    
  } catch (error) {
    console.error('验证权限配置失败:', error)
    ElMessage.error('验证权限配置失败: ' + (error.response?.data?.message || error.message))
  }
}

// 重置所有权限
const handleResetPermissions = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要重置所有权限吗？\n\n⚠️ 警告：此操作将逻辑删除所有现有权限并重新创建基础权限数据！\n\n' +
      '💡 说明：已删除权限的编码会被修改以避免冲突，新权限将使用标准编码。\n\n请谨慎操作！',
      '权限重置确认',
      {
        confirmButtonText: '确定重置',
        cancelButtonText: '取消',
        type: 'error',
        dangerouslyUseHTMLString: true
      }
    )
    
    resetLoading.value = true
    const response = await resetAllPermissions()
    
    ElMessage.success('权限重置成功！')
    
    // 显示重置结果详情
    const resultMessage = `
      <div style="text-align: left;">
        <h4>权限重置完成！</h4>
        <p>${response.data}</p>
        <br/>
        <p><strong>已重新创建的权限模块：</strong></p>
        <ul>
          <li>系统管理 - 系统基础功能管理</li>
          <li>用户管理 - 用户账户管理</li>
          <li>角色管理 - 角色权限管理</li>
          <li>权限管理 - 权限配置管理</li>
          <li>菜单管理 - 系统菜单管理</li>
          <li>组织管理 - 组织架构管理</li>
          <li>部门管理 - 部门信息管理</li>
          <li>岗位管理 - 岗位信息管理</li>
          <li>字典管理 - 系统字典管理</li>
          <li>商品管理 - 商品信息管理</li>
          <li>品牌管理 - 商品品牌管理</li>
          <li>分类管理 - 商品分类管理</li>
        </ul>
        <p>每个模块都包含查看、新增、编辑、删除等基础操作权限。</p>
      </div>
    `
    
    ElMessageBox.alert(resultMessage, '重置结果', {
      confirmButtonText: '确定',
      type: 'success',
      dangerouslyUseHTMLString: true
    })
    
    // 刷新权限列表
    await fetchPermissionList()
    
  } catch (error) {
    if (error !== 'cancel') {
      console.error('权限重置失败:', error)
      ElMessage.error('权限重置失败: ' + (error.response?.data?.message || error.message))
    }
  } finally {
    resetLoading.value = false
  }
}

// 判断是否为核心权限（不能被禁用或删除）
const isCorePermission = (permission) => {
  if (!permission || !permission.permissionCode) {
    return false
  }
  
  const corePermissions = ['system', 'profile']
  return corePermissions.includes(permission.permissionCode)
}

// 获取权限类型名称
const getPermissionTypeName = (type) => {
  switch (type) {
    case 1: return '一级权限(模块)'
    case 2: return '二级权限(子模块)'
    case 3: return '三级权限(操作)'
    default: return '未知类型'
  }
}

// 获取权限类型标签颜色
const getPermissionTypeTag = (type) => {
  switch (type) {
    case 1: return 'primary'
    case 2: return 'success'
    case 3: return 'warning'
    default: return 'info'
  }
}

// 权限类型变化处理
const onPermissionTypeChange = (newType) => {
  // 清空父级权限选择
  permissionForm.parentId = newType === 1 ? 0 : null
  permissionForm.menuId = null
}




</script>

<style scoped>
.permission-container {
  padding: 20px;
}
.action-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
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
</style> 