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
      <el-button
        type="primary"
        @click="handleSearch"
      >
        搜索
      </el-button>
      <el-button @click="resetSearch">
        重置
      </el-button>
      <el-button 
        v-if="hasPermission(PERMISSIONS.SYS.ROLE.CREATE)"
        type="success" 
        style="margin-left: 10px" 
        @click="handleAdd"
      >
        新增角色
      </el-button>
    </div>

    <el-table
      v-loading="tableLoading"
      :data="tableData"
      border
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
        prop="roleName"
        label="角色名称"
      />
      <el-table-column
        prop="roleCode"
        label="角色编码"
      />
      <el-table-column
        prop="description"
        label="描述"
      />
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
        v-if="hasPermission(PERMISSIONS.SYS.ROLE.EDIT) || hasPermission(PERMISSIONS.SYS.ROLE.DELETE) || hasPermission(PERMISSIONS.SYS.ROLE.ASSIGN_PERMISSION)"
        label="操作" 
        width="280" 
        fixed="right"
      >
        <template #default="scope">
          <el-button 
            v-if="hasPermission(PERMISSIONS.SYS.ROLE.EDIT)"
            size="small" 
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button 
            v-if="hasPermission(PERMISSIONS.SYS.ROLE.ASSIGN_PERMISSION)"
            size="small" 
            type="primary" 
            @click="handleAssignPermission(scope.row)"
          >
            分配权限
          </el-button>
          <el-button
            v-if="hasPermission(PERMISSIONS.SYS.ROLE.DELETE)"
            size="small"
            type="danger"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-if="total > 0"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-size="pageSize"
      :current-page="currentPage"
      style="margin-top: 15px; justify-content: flex-end"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
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
        <el-form-item
          label="角色名称"
          prop="roleName"
        >
          <el-input
            v-model="roleForm.roleName"
            placeholder="请输入角色名称"
          />
        </el-form-item>
        <el-form-item
          label="角色编码"
          prop="roleCode"
        >
          <el-input
            v-model="roleForm.roleCode"
            placeholder="请输入角色编码"
          />
        </el-form-item>
        <el-form-item
          label="描述"
          prop="description"
        >
          <el-input
            v-model="roleForm.description"
            type="textarea"
            placeholder="请输入描述"
          />
        </el-form-item>
        <el-form-item
          label="状态"
          prop="status"
        >
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

    <!-- 权限分配对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="分配权限"
      width="600px"
      :close-on-click-modal="false"
    >
      <div v-loading="permissionLoading">
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTree"
          show-checkbox
          node-key="id"
          :props="{ label: 'permissionName', children: 'children' }"
          :default-expanded-keys="expandedPermissionIds"
          @check="handlePermissionCheck"
        >
          <template #default="{ node, data }">
            <div class="permission-tree-node">
              <span class="permission-name">{{ data.permissionName }}</span>
              <el-tag 
                :type="getPermissionTypeTag(data.permissionType)" 
                size="small" 
                class="permission-type-tag"
              >
                {{ getPermissionTypeLabel(data.permissionType) }}
              </el-tag>
              <span class="permission-code">{{ data.permissionCode }}</span>
              <span v-if="data.permissionPath" class="permission-path">{{ data.permissionPath }}</span>
            </div>
          </template>
        </el-tree>
      </div>
      <template #footer>
        <el-button @click="permissionDialogVisible = false">
          取消
        </el-button>
        <el-button
          type="primary"
          :loading="permissionLoading"
          @click="submitPermission"
        >
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRolePage, getRoleById, createRole, updateRole, deleteRole, getRolePermissionIds, assignPermissions } from '@/api/role'
import { getPermissionTree } from '@/api/permission'
import { hasPermission, PERMISSIONS } from '@/utils/permission'

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
const expandedPermissionIds = ref([])
const permissionTreeRef = ref(null)
const currentRoleId = ref(null)
const permissionLoading = ref(false)

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
    permissionLoading.value = true
    currentRoleId.value = row.id
    
    // 重置权限树状态
    resetPermissionTreeState()
    
    // 加载权限树
    const { data: treeData } = await getPermissionTree()
    permissionTree.value = treeData || []
    console.log('权限树数据:', permissionTree.value)
    
    // 检查权限树数据结构
    if (permissionTree.value.length > 0) {
      console.log('权限树第一个节点ID类型:', typeof permissionTree.value[0].id)
      console.log('权限树第一个节点ID值:', permissionTree.value[0].id)
    }
    
    // 从数据库重新获取该角色的权限ID
    const { data: permissionIds } = await getRolePermissionIds(row.id)
    checkedPermissionIds.value = permissionIds || []
    console.log('从数据库获取的权限ID:', checkedPermissionIds.value)
    
    // 确保ID类型一致（都转换为数字）
    checkedPermissionIds.value = checkedPermissionIds.value.map(id => Number(id))
    console.log('转换后的权限ID:', checkedPermissionIds.value)
    
    // 计算需要展开的节点ID（包含已选权限的所有父级节点）
    expandedPermissionIds.value = calculateExpandedKeys(permissionTree.value, checkedPermissionIds.value)
    console.log('展开节点ID:', expandedPermissionIds.value)
    
    permissionDialogVisible.value = true
    
    // 使用nextTick确保权限树渲染完成后再设置选中状态
    await nextTick()
    console.log('权限树引用:', permissionTreeRef.value)
    console.log('要设置的权限ID:', checkedPermissionIds.value)
    
    if (permissionTreeRef.value && checkedPermissionIds.value.length > 0) {
      console.log('开始设置权限树选中状态...')
      
      // 先清除所有选中状态
      permissionTreeRef.value.setCheckedKeys([])
      
      // 等待一下确保清除完成
      await nextTick()
      
      // 逐个设置权限，确保精确控制
      for (const permissionId of checkedPermissionIds.value) {
        console.log('设置权限ID:', permissionId)
        permissionTreeRef.value.setChecked(permissionId, true, false)
      }
      
      console.log('权限树选中状态设置完成')
      
      // 验证设置结果
      const currentCheckedKeys = permissionTreeRef.value.getCheckedKeys()
      const currentHalfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
      console.log('设置后的选中权限:', currentCheckedKeys)
      console.log('设置后的半选权限:', currentHalfCheckedKeys)
      
      // 验证设置是否正确
      const expectedIds = checkedPermissionIds.value.sort()
      const actualIds = currentCheckedKeys.sort()
      console.log('期望的权限ID:', expectedIds)
      console.log('实际的权限ID:', actualIds)
      console.log('设置是否正确:', JSON.stringify(expectedIds) === JSON.stringify(actualIds))
      
    } else {
      console.log('权限树引用不存在或没有权限ID')
    }
  } catch (error) {
    ElMessage.error('获取权限信息失败')
    console.error(error)
  } finally {
    permissionLoading.value = false
  }
}

// 重置权限树状态
const resetPermissionTreeState = () => {
  permissionTree.value = []
  checkedPermissionIds.value = []
  expandedPermissionIds.value = []
}

// 计算需要展开的节点ID
const calculateExpandedKeys = (tree, checkedKeys) => {
  const expandedKeys = new Set()
  
  const findParents = (nodes, targetIds) => {
    for (const node of nodes) {
      if (targetIds.includes(node.id)) {
        // 如果当前节点被选中，需要展开其所有父级节点
        let parent = findParentNode(tree, node.id)
        while (parent) {
          expandedKeys.add(parent.id)
          parent = findParentNode(tree, parent.id)
        }
      }
      if (node.children && node.children.length > 0) {
        findParents(node.children, targetIds)
      }
    }
  }
  
  findParents(tree, checkedKeys)
  
  // 如果没有选中的权限，至少展开第一级
  if (expandedKeys.size === 0 && tree.length > 0) {
    tree.forEach(node => expandedKeys.add(node.id))
  }
  
  // 如果树不为空，总是展开第一级
  if (tree.length > 0) {
    tree.forEach(node => expandedKeys.add(node.id))
  }
  
  return Array.from(expandedKeys)
}

// 查找父节点
const findParentNode = (tree, nodeId) => {
  for (const node of tree) {
    if (node.children) {
      for (const child of node.children) {
        if (child.id === nodeId) {
          return node
        }
        const found = findParentNode([child], nodeId)
        if (found) return found
      }
    }
  }
  return null
}

// 权限树节点选中/取消选中时触发
const handlePermissionCheck = (data, checked, indeterminate) => {
  const permissionId = data.id;
  if (checked) {
    // 选中
    if (!checkedPermissionIds.value.includes(permissionId)) {
      checkedPermissionIds.value.push(permissionId);
    }
  } else {
    // 取消选中
    checkedPermissionIds.value = checkedPermissionIds.value.filter(id => id !== permissionId);
  }
  console.log('当前选中权限ID:', checkedPermissionIds.value);
};

// 提交权限分配
const submitPermission = async () => {
  try {
    // 获取完全选中的权限节点和半选状态的权限节点
    const checkedKeys = permissionTreeRef.value.getCheckedKeys()
    const halfCheckedKeys = permissionTreeRef.value.getHalfCheckedKeys()
    
    console.log('=== 权限提交分析 ===')
    console.log('用户选择的权限数量:', '5个') // 根据用户描述
    console.log('完全选中的权限ID (checkedKeys):', checkedKeys)
    console.log('半选状态的权限ID (halfCheckedKeys):', halfCheckedKeys)
    console.log('完全选中权限数量:', checkedKeys.length)
    console.log('半选状态权限数量:', halfCheckedKeys.length)
    
    // 分析权限树结构，找出哪些是叶子节点权限
    const leafPermissions = []
    const parentPermissions = []
    
    const analyzePermissions = (nodes) => {
      for (const node of nodes) {
        if (node.children && node.children.length > 0) {
          parentPermissions.push(node.id)
          analyzePermissions(node.children)
        } else {
          leafPermissions.push(node.id)
        }
      }
    }
    
    analyzePermissions(permissionTree.value)
    
    console.log('叶子节点权限ID:', leafPermissions)
    console.log('父级节点权限ID:', parentPermissions)
    
    // 合并完全选中的权限和半选状态的权限
    // 这样可以确保用户实际选择的所有权限节点都被保存
    const allSelectedPermissions = [...new Set([...checkedKeys, ...halfCheckedKeys])]
    
    console.log('用户实际选中的权限ID:', allSelectedPermissions)
    console.log('实际选中权限数量:', allSelectedPermissions.length)
    
    // 提交所有被选中的权限（包括完全选中和半选状态的）
    const finalPermissionIds = allSelectedPermissions
    
    console.log('最终提交的权限ID:', finalPermissionIds)
    console.log('最终提交权限数量:', finalPermissionIds.length)
    console.log('=== 权限提交分析结束 ===')
    
    // 提交所有选中的权限
    await assignPermissions(currentRoleId.value, finalPermissionIds)
    ElMessage.success('权限分配成功')
    
    // 提交成功后，重新获取权限状态以确保数据一致性
    const { data: updatedPermissionIds } = await getRolePermissionIds(currentRoleId.value)
    console.log('提交后重新获取的权限ID:', updatedPermissionIds)
    console.log('提交后权限数量:', updatedPermissionIds ? updatedPermissionIds.length : 0)
    
    permissionDialogVisible.value = false
  } catch (error) {
    ElMessage.error('权限分配失败')
    console.error(error)
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

// 获取权限类型标签文本
const getPermissionTypeLabel = (type) => {
  switch (type) {
    case 1: return '模块'
    case 2: return '子模块'
    case 3: return '操作'
    default: return '未知'
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
.permission-tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}
.permission-name {
  font-weight: 500;
  color: #303133;
  min-width: 120px;
}
.permission-type-tag {
  margin-left: 8px;
}
.permission-code {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 12px;
  color: #909399;
  background-color: #f5f7fa;
  padding: 2px 6px;
  border-radius: 3px;
}
.permission-path {
  font-family: 'Monaco', 'Menlo', 'Ubuntu Mono', monospace;
  font-size: 11px;
  color: #b3b3b3;
  margin-left: auto;
}
</style> 