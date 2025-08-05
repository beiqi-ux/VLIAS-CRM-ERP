<template>
  <div class="user-list">
    <!-- 骨架屏 -->
    <TableSkeleton 
      v-if="initialLoading"
      :columns="[
        { width: '80px', titleWidth: '20px', cellWidth: '40px' },
        { width: '120px', titleWidth: '40px', cellWidth: '80px' },
        { width: '120px', titleWidth: '50px', cellWidth: '80px' },
        { width: '130px', titleWidth: '40px', cellWidth: '100px' },
        { width: '180px', titleWidth: '30px', cellWidth: '140px' },
        { width: '150px', titleWidth: '50px', cellWidth: '100px' },
        { width: '150px', titleWidth: '50px', cellWidth: '100px' },
        { width: '150px', titleWidth: '50px', cellWidth: '100px' },
        { width: '80px', titleWidth: '30px', cellWidth: '50px', variant: 'button' },
        { width: '180px', titleWidth: '60px', cellWidth: '140px' },
        { width: '150px', titleWidth: '30px', cellWidth: '120px', variant: 'button' }
      ]"
      :rows="10"
      :search-items="3"
    />
    
    <!-- 实际内容 -->
    <el-card v-else>
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-icon class="header-icon">
              <User />
            </el-icon>
            <span class="header-title">用户管理</span>
          </div>
          <el-button 
            v-if="hasPermission(PERMISSIONS.SYS.USER.CREATE)"
            type="primary" 
            @click="handleAdd"
          >
            <el-icon><Plus /></el-icon>
            新增用户
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form
          :inline="true"
          :model="searchForm"
          class="search-form"
        >
          <el-form-item label="用户名">
            <el-input 
              v-model="searchForm.username" 
              placeholder="请输入用户名" 
              clearable 
              style="width: 200px;"
              @keyup.enter="handleSearchClick"
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="真实姓名">
            <el-input 
              v-model="searchForm.realName" 
              placeholder="请输入真实姓名" 
              clearable 
              style="width: 200px;"
              @keyup.enter="handleSearchClick"
            >
              <template #prefix>
                <el-icon><UserFilled /></el-icon>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item label="状态">
            <DictSelect 
              v-model="searchForm.status" 
              dict-code="USER_STATUS" 
              placeholder="请选择状态" 
              clearable 
              value-type="number"
              style="width: 150px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              @click="handleSearchClick"
            >
              <el-icon><Search /></el-icon>
              搜索
            </el-button>
            <el-button @click="handleReset">
              <el-icon><Refresh /></el-icon>
              重置
            </el-button>
            <el-button
              :loading="refreshing"
              @click="handleRefresh"
            >
              <el-icon><Refresh /></el-icon>
              {{ refreshing ? '刷新中' : '刷新' }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 用户列表 -->
      <el-table 
        v-loading="loading || refreshing" 
        :data="userList" 
        :element-loading-text="refreshing ? '刷新中...' : '加载中...'"
        stripe 
        border
        style="width: 100%;"
        :header-cell-style="{ background: '#f5f7fa', color: '#606266' }"
        height="600"
      >
        <el-table-column
          prop="id"
          label="ID"
          width="80"
        >
          <template #default="{ row }">
            {{ $formatId(row.id) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="username"
          label="用户名"
          width="120"
        />
        <el-table-column
          prop="realName"
          label="真实姓名"
          width="120"
        />
        <el-table-column
          prop="mobile"
          label="手机号"
          width="130"
        />
        <el-table-column
          prop="email"
          label="邮箱"
          width="180"
        />
        <el-table-column
          prop="orgName"
          label="所属组织"
          width="150"
        >
          <template #default="{ row }">
            {{ row.orgName || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="deptName"
          label="所属部门"
          width="150"
        >
          <template #default="{ row }">
            {{ row.deptName || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="positionName"
          label="所属岗位"
          width="150"
        >
          <template #default="{ row }">
            {{ row.positionName || '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          width="100"
        >
          <template #default="{ row }">
            <el-tag 
              v-if="!hasPermission(PERMISSIONS.SYS.USER.EDIT)"
              :type="getStatusType(row.status)"
            >
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
            <el-tag 
              v-else
              :type="getStatusType(row.status)"
              style="cursor: pointer;"
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="lastLoginTime"
          label="最后登录时间"
          width="180"
        >
          <template #default="{ row }">
            {{ row.lastLoginTime ? formatDateTime(row.lastLoginTime) : '-' }}
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="创建时间"
          width="180"
        >
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column 
          v-if="hasPermission(PERMISSIONS.SYS.USER.EDIT) || hasPermission(PERMISSIONS.SYS.USER.DELETE) || hasPermission(PERMISSIONS.SYS.USER.RESET_PASSWORD)"
          label="操作" 
          width="220" 
          fixed="right"
        >
          <template #default="{ row }">
            <el-button-group>
              <el-button 
                v-if="hasPermission(PERMISSIONS.SYS.USER.EDIT)"
                type="primary" 
                size="small" 
                @click="handleEdit(row)"
              >
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button 
                v-if="hasPermission(PERMISSIONS.SYS.USER.RESET_PASSWORD)"
                type="success" 
                size="small" 
                @click="handleAssignRole(row)"
              >
                <el-icon><UserFilled /></el-icon>
                角色
              </el-button>
              <el-dropdown 
                v-if="hasPermission(PERMISSIONS.SYS.USER.DELETE) || hasPermission(PERMISSIONS.SYS.USER.EDIT)"
                trigger="click" 
                @command="(command) => handleDropdownCommand(command, row)"
              >
                <el-button
                  type="info"
                  size="small"
                >
                  更多<el-icon class="el-icon--right">
                    <ArrowDown />
                  </el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item 
                      v-if="hasPermission(PERMISSIONS.SYS.USER.EDIT)"
                      command="changePassword"
                    >
                      <el-icon><Key /></el-icon>
                      修改密码
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="hasPermission(PERMISSIONS.SYS.USER.EDIT)"
                      command="resetPassword"
                    >
                      <el-icon><Refresh /></el-icon>
                      重置密码
                    </el-dropdown-item>
                    <el-dropdown-item 
                      v-if="hasPermission(PERMISSIONS.SYS.USER.DELETE)"
                      divided 
                      command="delete"
                    >
                      <el-icon><Delete /></el-icon>
                      删除
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
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

    <!-- 用户表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="userFormRef"
        :model="userForm"
        :rules="userRules"
        label-width="100px"
      >
        <el-form-item
          label="用户名"
          prop="username"
        >
          <el-input
            v-model="userForm.username"
            placeholder="请输入用户名"
          />
        </el-form-item>
        <el-form-item
          label="真实姓名"
          prop="realName"
        >
          <el-input
            v-model="userForm.realName"
            placeholder="请输入真实姓名"
          />
        </el-form-item>
        <el-form-item
          label="手机号"
          prop="mobile"
        >
          <el-input
            v-model="userForm.mobile"
            placeholder="请输入手机号"
          />
        </el-form-item>
        <el-form-item
          label="邮箱"
          prop="email"
        >
          <el-input
            v-model="userForm.email"
            placeholder="请输入邮箱"
          />
        </el-form-item>
        <el-form-item
          label="性别"
          prop="gender"
        >
          <DictSelect 
            v-model="userForm.gender" 
            dict-code="GENDER" 
            placeholder="请选择性别"
            value-type="number"
          />
        </el-form-item>
        <el-form-item
          label="组织"
          prop="orgId"
        >
          <el-select
            v-model="userForm.orgId"
            placeholder="请选择组织"
            @change="handleOrgChange"
          >
            <el-option
              v-for="org in orgOptions"
              :key="org.id"
              :label="org.orgName"
              :value="org.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="部门"
          prop="deptId"
        >
          <el-select
            v-model="userForm.deptId"
            placeholder="请选择部门"
            @change="handleDeptChange"
          >
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.deptName"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="岗位"
          prop="positionId"
        >
          <el-select
            v-model="userForm.positionId"
            placeholder="请选择岗位"
          >
            <el-option
              v-for="pos in positionOptions"
              :key="pos.id"
              :label="pos.positionName"
              :value="pos.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="!userForm.id"
          label="密码"
          prop="password"
        >
          <el-input
            v-model="userForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button
            type="primary"
            :loading="submitting"
            @click="handleSubmit"
          >
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 修改密码对话框 -->
    <el-dialog
      v-model="passwordDialogVisible"
      title="修改密码"
      width="400px"
    >
      <el-form
        ref="passwordFormRef"
        :model="passwordForm"
        :rules="passwordRules"
        label-width="100px"
      >
        <el-form-item
          label="旧密码"
          prop="oldPassword"
        >
          <el-input
            v-model="passwordForm.oldPassword"
            type="password"
            placeholder="请输入旧密码"
            show-password
          />
        </el-form-item>
        <el-form-item
          label="新密码"
          prop="newPassword"
        >
          <el-input
            v-model="passwordForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            show-password
          />
        </el-form-item>
        <el-form-item
          label="确认密码"
          prop="confirmPassword"
        >
          <el-input
            v-model="passwordForm.confirmPassword"
            type="password"
            placeholder="请再次输入新密码"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="passwordDialogVisible = false">取消</el-button>
          <el-button
            type="primary"
            :loading="submitting"
            @click="handlePasswordSubmit"
          >
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 角色分配对话框 -->
    <el-dialog
      v-model="roleDialogVisible"
      title="分配角色"
      width="500px"
    >
      <div v-loading="roleLoading">
        <p class="mb-10">
          当前用户：{{ currentUser.username }} ({{ currentUser.realName || '未设置姓名' }})
        </p>
        <el-checkbox-group v-model="selectedRoles">
          <el-checkbox
            v-for="role in roleList"
            :key="role.id"
            :label="role.id"
          >
            {{ role.roleName }} ({{ role.roleCode }})
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <template #footer>
        <el-button @click="roleDialogVisible = false">
          取消
        </el-button>
        <el-button
          type="primary"
          :loading="roleSaving"
          @click="handleSaveUserRoles"
        >
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDateTime } from '@/utils/format'
import { getDictTextSync, preloadDicts } from '@/utils/dictUtils'
import { 
  getUserList, 
  getUserDetailList,
  createUser, 
  updateUser, 
  deleteUser, 
  changePassword, 
  resetPassword,
  updateUserStatus
} from '@/api/user'
import { getRoleList, getUserRoleIds, assignUserRoles } from '@/api/role'
import { getOrganizationList } from '@/api/organization'
import { getDepartmentsByOrgId } from '@/api/department'
import { getPositionsByDeptId } from '@/api/position'
import { Edit, UserFilled, ArrowDown, Key, Refresh, Delete, User, Search, Plus } from '@element-plus/icons-vue'
import { hasPermission, PERMISSIONS } from '@/utils/permission'
import { useTableLoader } from '@/composables/useTableLoader'
import TableSkeleton from '@/components/Skeleton/TableSkeleton.vue'

// 使用新的表格加载器
const {
  loading,
  refreshing,
  initialLoading,
  tableData: userList,
  total,
  pagination,
  searchForm: searchFormRef,
  handleSearch,
  resetSearch,
  refresh,
  handleSizeChange,
  handleCurrentChange,
  clearCache
} = useTableLoader({
  apiFunction: getUserDetailList,
  cacheKey: 'userList',
  cacheTTL: 3 * 60 * 1000, // 3分钟缓存
  defaultPageSize: 20,
  errorMessage: '获取用户列表失败',
  preloadEnabled: true // 启用预加载
})

// 原有的搜索表单（保持向后兼容）
const searchForm = reactive({
  username: '',
  realName: '',
  status: ''
})

// 同步搜索表单到新的loader
function syncSearchForm() {
  searchFormRef.value = {
    username: searchForm.username || null,
    realName: searchForm.realName || null,
    status: searchForm.status || null
  }
}

// 响应式数据
const submitting = ref(false)
const dialogVisible = ref(false)
const passwordDialogVisible = ref(false)
const dialogTitle = ref('')
const userFormRef = ref()
const passwordFormRef = ref()

// 组织、部门、岗位数据
const orgOptions = ref([])
const deptOptions = ref([])
const positionOptions = ref([])
const loadingOrg = ref(false)
const loadingDept = ref(false)
const loadingPosition = ref(false)

// 用户表单
const userForm = reactive({
  id: null,
  username: '',
  realName: '',
  mobile: '',
  email: '',
  gender: 0,
  status: 1,
  password: '',
  orgId: null,
  deptId: null,
  positionId: null
})

// 密码表单
const passwordForm = reactive({
  userId: null,
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const userRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  mobile: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

const passwordRules = {
  oldPassword: [
    { required: true, message: '请输入旧密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 新增用户
const handleAdd = () => {
  dialogTitle.value = '新增用户'
  Object.assign(userForm, {
    id: null,
    username: '',
    realName: '',
    mobile: '',
    email: '',
    gender: 0,
    status: 1,
    password: '',
    orgId: null,
    deptId: null,
    positionId: null
  })
  dialogVisible.value = true
}

// 重新定义搜索和重置函数（兼容新loader）
const handleSearchClick = () => {
  syncSearchForm()
  handleSearch(searchFormRef.value)
}

const handleReset = () => {
  Object.assign(searchForm, {
    username: '',
    realName: '',
    status: ''
  })
  syncSearchForm()
  resetSearch()
}

// 手动刷新（带缓存清理）
const handleRefresh = () => {
  clearCache('userList') // 清理用户列表相关缓存
  refresh()
}

// 编辑用户
const handleEdit = async (row) => {
  dialogTitle.value = '编辑用户'
  Object.assign(userForm, {
    id: row.id,
    username: row.username,
    realName: row.realName,
    mobile: row.mobile,
    email: row.email,
    gender: row.gender,
    status: row.status,
    orgId: row.orgId,
    deptId: row.deptId,
    positionId: row.positionId
  })
  
  // 加载关联数据
  if (row.orgId) {
    await fetchDepartments(row.orgId)
    if (row.deptId) {
      await fetchPositions(row.deptId)
    }
  }
  
  dialogVisible.value = true
}

// 删除用户
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteUser(row.id)
    ElMessage.success('删除成功')
    refresh() // 刷新列表
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 修改密码
const handleChangePassword = (row) => {
  passwordForm.userId = row.id
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  passwordDialogVisible.value = true
}

// 重置密码
const handleResetPassword = async (row) => {
  try {
    await ElMessageBox.confirm('确定要重置该用户的密码吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await resetPassword(row.id)
    ElMessage.success(`密码重置成功，新密码：${response.data.newPassword}`)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重置密码失败')
    }
  }
}

// 切换用户状态
const handleToggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  const statusText = newStatus === 1 ? '启用' : '禁用'
  
  try {
    await ElMessageBox.confirm(`确定要${statusText}该用户吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await updateUserStatus(row.id, newStatus)
    if (response.success) {
      ElMessage.success(`用户${statusText}成功`)
      // 更新本地数据
      row.status = newStatus
    } else {
      ElMessage.error(response.message || `用户${statusText}失败`)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`用户${statusText}失败`)
    }
  }
}

// 提交用户表单
const handleSubmit = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    submitting.value = true
    
    if (userForm.id) {
      await updateUser(userForm.id, userForm)
      ElMessage.success('更新成功')
    } else {
      await createUser(userForm)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    refresh() // 刷新列表
  } catch (error) {
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 提交密码表单
const handlePasswordSubmit = async () => {
  if (!passwordFormRef.value) return
  
  try {
    await passwordFormRef.value.validate()
    submitting.value = true
    
    await changePassword(passwordForm.userId, {
      oldPassword: passwordForm.oldPassword,
      newPassword: passwordForm.newPassword
    })
    
    ElMessage.success('密码修改成功')
    passwordDialogVisible.value = false
  } catch (error) {
    ElMessage.error('密码修改失败')
  } finally {
    submitting.value = false
  }
}

// 关闭对话框
const handleDialogClose = () => {
  userFormRef.value?.resetFields()
}

// 角色分配相关
const roleDialogVisible = ref(false)
const roleLoading = ref(false)
const roleSaving = ref(false)
const roleList = ref([])
const selectedRoles = ref([])
const currentUser = ref({})

// 获取所有角色
const fetchAllRoles = async () => {
  try {
    roleLoading.value = true
    const response = await getRoleList()
    if (response.success) {
      roleList.value = response.data || []
    } else {
      ElMessage.error(response.message || '获取角色列表失败')
    }
  } catch (error) {
    ElMessage.error('获取角色列表失败')
    console.error(error)
  } finally {
    roleLoading.value = false
  }
}

// 获取用户的角色
const fetchUserRoles = async (userId) => {
  try {
    roleLoading.value = true
    const response = await getUserRoleIds(userId)
    if (response.success) {
      selectedRoles.value = response.data || []
    } else {
      ElMessage.error(response.message || '获取用户角色失败')
      selectedRoles.value = []
    }
  } catch (error) {
    ElMessage.error('获取用户角色失败')
    console.error(error)
    selectedRoles.value = []
  } finally {
    roleLoading.value = false
  }
}

// 分配角色
const handleAssignRole = async (row) => {
  currentUser.value = row
  roleDialogVisible.value = true
  
  // 获取所有角色
  await fetchAllRoles()
  
  // 获取用户当前角色
  await fetchUserRoles(row.id)
}

// 保存用户角色
const handleSaveUserRoles = async () => {
  try {
    roleSaving.value = true
    const response = await assignUserRoles(currentUser.value.id, selectedRoles.value)
    if (response.success) {
      ElMessage.success('角色分配成功')
      roleDialogVisible.value = false
    } else {
      ElMessage.error(response.message || '角色分配失败')
    }
  } catch (error) {
    ElMessage.error('角色分配失败')
    console.error(error)
  } finally {
    roleSaving.value = false
  }
}

// 处理下拉菜单命令
const handleDropdownCommand = async (command, row) => {
  if (command === 'changePassword') {
    handleChangePassword(row)
  } else if (command === 'resetPassword') {
    handleResetPassword(row)
  } else if (command === 'delete') {
    handleDelete(row)
  }
}

// 初始化
onMounted(async () => {
  try {
    // 预加载字典数据
    await preloadDicts(['USER_STATUS', 'GENDER'])
    
    // 初始化搜索表单
    syncSearchForm()
    
    // 加载组织数据
    await fetchOrganizations()
  } catch (error) {
    console.error('初始化失败:', error)
  }
})

// 加载组织数据
const fetchOrganizations = async () => {
  try {
    loadingOrg.value = true
    const response = await getOrganizationList({})
    if (response.success) {
      orgOptions.value = response.data || []
    }
  } catch (error) {
    console.error('获取组织列表失败:', error)
  } finally {
    loadingOrg.value = false
  }
}

// 获取部门列表
const fetchDepartments = async (orgId) => {
  if (!orgId) {
    deptOptions.value = []
    return
  }
  
  loadingDept.value = true
  try {
    const res = await getDepartmentsByOrgId(orgId)
    deptOptions.value = res.data || []
  } catch (error) {
    console.error('Failed to fetch departments:', error)
    ElMessage.error('获取部门列表失败')
  } finally {
    loadingDept.value = false
  }
}

// 获取岗位列表
const fetchPositions = async (deptId) => {
  if (!deptId) {
    positionOptions.value = []
    return
  }
  
  loadingPosition.value = true
  try {
    const res = await getPositionsByDeptId(deptId)
    positionOptions.value = res.data || []
  } catch (error) {
    console.error('Failed to fetch positions:', error)
    ElMessage.error('获取岗位列表失败')
  } finally {
    loadingPosition.value = false
  }
}

// 处理组织变更
const handleOrgChange = async () => {
  userForm.deptId = null
  userForm.positionId = null
  positionOptions.value = []
  await fetchDepartments(userForm.orgId)
}

// 处理部门变更
const handleDeptChange = async () => {
  userForm.positionId = null
  await fetchPositions(userForm.deptId)
}

// 字典相关方法
const getStatusText = (status) => {
  return getDictTextSync('USER_STATUS', status)
}

const getStatusType = (status) => {
  // 根据状态值返回对应的标签类型
  if (status == 1) return 'success'  // 启用
  if (status == 0) return 'danger'   // 禁用
  if (status == 2) return 'warning'  // 短暂暂停使用
  return 'info'
}
</script>

<style scoped>
.user-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-icon {
  margin-right: 8px;
  font-size: 20px;
  color: #409EFF;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.search-area {
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: center;
}

.search-form .el-form-item {
  margin-bottom: 0;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  padding: 16px 0;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 表格操作按钮样式 */
.el-button-group {
  display: flex;
}

.el-button-group .el-button {
  margin-right: 0;
}

/* 下拉菜单样式 */
.el-dropdown {
  margin-left: 0;
}

.el-dropdown .el-button {
  border-left: none;
  border-top-left-radius: 0;
  border-bottom-left-radius: 0;
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

/* 响应式布局 */
@media (max-width: 768px) {
  .search-form {
    flex-direction: column;
    align-items: stretch;
  }
  
  .search-form .el-form-item {
    margin-bottom: 12px;
  }
  
  .card-header {
    flex-direction: column;
    gap: 12px;
    align-items: stretch;
  }
  
  .header-left {
    justify-content: center;
  }
}
</style> 