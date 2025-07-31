<template>
  <div class="role-container">
    <div class="search-bar">
      <el-input
        v-model="searchForm.roleName"
        placeholder="角色名称"
        clearable
        style="width: 200px; margin-right: 10px"
        @keyup.enter="handleSearch"
      />
      <el-button type="primary" @click="handleSearch">搜索</el-button>
      <el-button @click="resetSearch">重置</el-button>
      <el-button type="success" @click="handleAdd" style="margin-left: 10px">新增角色</el-button>
    </div>

    <el-table
      v-loading="tableLoading"
      :data="tableData"
      border
      style="width: 100%; margin-top: 15px"
    >
      <el-table-column prop="id" label="ID" width="80">
        <template #default="scope">
          {{ $formatId(scope.row.id) }}
        </template>
      </el-table-column>
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" width="180">
        <template #default="scope">
          {{ formatDateTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="small" type="primary" @click="handleAssignPermission(scope.row)">分配权限</el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-if="total > 0"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-size="pageSize"
      :current-page="currentPage"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      style="margin-top: 15px; justify-content: flex-end"
    />

    <!-- 角色表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formTitle"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="roleFormRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="roleForm.roleCode" placeholder="请输入角色编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="roleForm.description"
            type="textarea"
            placeholder="请输入描述"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="roleForm.status"
            :active-value="1"
            :inactive-value="0"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 权限分配对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="分配权限"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-tree
        ref="permissionTreeRef"
        :data="permissionTree"
        show-checkbox
        node-key="id"
        :props="{ label: 'permissionName' }"
        :default-checked-keys="checkedPermissionIds"
      />
      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitPermission">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRolePage, getRoleById, createRole, updateRole, deleteRole, getRolePermissionIds, assignPermissions } from '@/api/role'
import { getPermissionTree } from '@/api/permission'

// 表格数据和加载状态
const tableData = ref([])
const tableLoading = ref(false)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 搜索表单
const searchForm = reactive({
  roleName: ''
})

// 角色表单相关
const dialogVisible = ref(false)
const formTitle = ref('')
const roleFormRef = ref(null)
const roleForm = reactive({
  id: null,
  roleName: '',
  roleCode: '',
  description: '',
  status: 1
})
const roleRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

// 权限分配相关
const permissionDialogVisible = ref(false)
const permissionTree = ref([])
const checkedPermissionIds = ref([])
const permissionTreeRef = ref(null)
const currentRoleId = ref(null)

// 初始化
onMounted(() => {
  fetchRoleList()
})

// 格式化时间
const formatDateTime = (dateTimeStr) => {
  if (!dateTimeStr) return ''
  const date = new Date(dateTimeStr)
  return date.toLocaleString()
}

// 获取角色列表
const fetchRoleList = async () => {
  try {
    tableLoading.value = true
    const params = {
      page: currentPage.value - 1, // 后端分页从0开始
      size: pageSize.value,
      roleName: searchForm.roleName
    }
    console.log('获取角色列表参数:', params)
    const response = await getRolePage(params)
    console.log('角色列表响应:', response)
    
    if (response.success) {
      tableData.value = response.data.content || []
      total.value = response.data.totalElements || 0
    } else {
      ElMessage.error(response.message || '获取角色列表失败')
      console.error('获取角色列表失败:', response)
      tableData.value = []
      total.value = 0
    }
  } catch (error) {
    ElMessage.error('获取角色列表失败')
    console.error('获取角色列表异常:', error)
    tableData.value = []
    total.value = 0
  } finally {
    tableLoading.value = false
  }
}

// 搜索
const handleSearch = () => {
  currentPage.value = 1
  fetchRoleList()
}

// 重置搜索
const resetSearch = () => {
  Object.keys(searchForm).forEach(key => {
    searchForm[key] = ''
  })
  handleSearch()
}

// 分页相关
const handleSizeChange = (val) => {
  pageSize.value = val
  fetchRoleList()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  fetchRoleList()
}

// 新增角色
const handleAdd = () => {
  formTitle.value = '新增角色'
  Object.keys(roleForm).forEach(key => {
    roleForm[key] = key === 'status' ? 1 : null
  })
  dialogVisible.value = true
}

// 编辑角色
const handleEdit = async (row) => {
  formTitle.value = '编辑角色'
  try {
    const { data } = await getRoleById(row.id)
    Object.keys(roleForm).forEach(key => {
      roleForm[key] = data[key]
    })
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取角色信息失败')
    console.error(error)
  }
}

// 提交表单
const submitForm = async () => {
  if (!roleFormRef.value) return
  
  try {
    await roleFormRef.value.validate()
    
    if (roleForm.id) {
      await updateRole(roleForm.id, roleForm)
      ElMessage.success('更新成功')
    } else {
      await createRole(roleForm)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchRoleList()
  } catch (error) {
    console.error(error)
    ElMessage.error('提交失败')
  }
}

// 删除角色
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该角色吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteRole(row.id)
      ElMessage.success('删除成功')
      fetchRoleList()
    } catch (error) {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }).catch(() => {})
}

// 分配权限
const handleAssignPermission = async (row) => {
  try {
    currentRoleId.value = row.id
    
    // 加载权限树
    const { data: treeData } = await getPermissionTree()
    permissionTree.value = treeData || []
    
    // 获取已分配的权限ID
    const { data: permissionIds } = await getRolePermissionIds(row.id)
    checkedPermissionIds.value = permissionIds || []
    
    permissionDialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取权限信息失败')
    console.error(error)
  }
}

// 提交权限分配
const submitPermission = async () => {
  if (!permissionTreeRef.value || !currentRoleId.value) return
  
  try {
    const checkedKeys = permissionTreeRef.value.getCheckedKeys()
    const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
    const permissionIds = [...checkedKeys, ...halfCheckedKeys]
    
    await assignPermissions(currentRoleId.value, permissionIds)
    ElMessage.success('权限分配成功')
    permissionDialogVisible.value = false
  } catch (error) {
    ElMessage.error('权限分配失败')
    console.error(error)
  }
}
</script>

<style scoped>
.role-container {
  padding: 20px;
}
.search-bar {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}
</style> 