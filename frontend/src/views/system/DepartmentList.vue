<template>
  <div class="department-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>部门管理</span>
          <el-button 
            v-if="hasPermission(PERMISSIONS.ORG.DEPARTMENT.ADD)"
            type="primary" 
            @click="handleAdd"
          >
            新增部门
          </el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="所属组织">
            <el-select v-model="searchForm.orgId" placeholder="请选择组织" clearable @change="handleOrgChange">
              <el-option
                v-for="org in orgOptions"
                :key="org.id"
                :label="org.orgName"
                :value="org.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="部门名称">
            <el-input v-model="searchForm.deptName" placeholder="请输入部门名称" clearable />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 部门列表 -->
      <el-table :data="filteredDeptList" v-loading="loading" row-key="id" default-expand-all>
        <el-table-column prop="id" label="ID" width="80">
          <template #default="{ row }">
            {{ $formatId(row.id) }}
          </template>
        </el-table-column>
        <el-table-column prop="deptName" label="部门名称" width="180" />
        <el-table-column prop="deptCode" label="部门编码" width="120" />
        <el-table-column prop="orgName" label="所属组织" width="150" />
        <el-table-column prop="leader" label="负责人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template #default="{ row }">
            {{ formatDateTime(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column 
          v-if="hasPermission(PERMISSIONS.ORG.DEPARTMENT.EDIT) || hasPermission(PERMISSIONS.ORG.DEPARTMENT.DELETE)"
          label="操作" 
          width="200" 
          fixed="right"
        >
          <template #default="{ row }">
            <el-button 
              v-if="hasPermission(PERMISSIONS.ORG.DEPARTMENT.EDIT)"
              type="primary" 
              size="small" 
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="hasPermission(PERMISSIONS.ORG.DEPARTMENT.DELETE)"
              type="danger" 
              size="small" 
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 部门表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="deptFormRef"
        :model="deptForm"
        :rules="deptRules"
        label-width="100px"
      >
        <el-form-item label="所属组织" prop="orgId">
          <el-select v-model="deptForm.orgId" placeholder="请选择组织" @change="handleFormOrgChange">
            <el-option
              v-for="org in orgOptions"
              :key="org.id"
              :label="org.orgName"
              :value="org.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上级部门" prop="parentId">
          <el-cascader
            v-model="deptForm.parentId"
            :options="deptTreeOptions"
            :props="{
              checkStrictly: true,
              label: 'deptName',
              value: 'id',
              emitPath: false
            }"
            placeholder="请选择上级部门（可选）"
            clearable
          />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="deptForm.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门编码" prop="deptCode">
          <el-input v-model="deptForm.deptCode" placeholder="请输入部门编码" :rules="[{ validator: validateDeptCode, trigger: 'blur' }]" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="deptForm.leader" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系电话">
          <el-input v-model="deptForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="deptForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="deptForm.sort" :min="1" placeholder="请输入排序" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="deptForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
            v-model="deptForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDateTime } from '@/utils/format'
import { hasPermission, PERMISSIONS } from '@/utils/permission'
import {
  getDepartmentList,
  getDepartmentTree,
  addDepartment,
  updateDepartment,
  deleteDepartment,
  checkDeptCodeExists
} from '@/api/department'
import { getOrganizationList } from '@/api/organization'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const deptList = ref([])
const deptTreeOptions = ref([])
const orgOptions = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const deptFormRef = ref()

// 搜索表单
const searchForm = reactive({
  orgId: '',
  deptName: '',
  status: ''
})

// 部门表单
const deptForm = reactive({
  id: null,
  orgId: null,
  parentId: null,
  deptName: '',
  deptCode: '',
  leader: '',
  phone: '',
  email: '',
  sort: 1,
  status: 1,
  remark: ''
})

// 表单验证规则
const deptRules = {
  orgId: [
    { required: true, message: '请选择所属组织', trigger: 'change' }
  ],
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  deptCode: [
    { required: true, message: '请输入部门编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  sort: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

// 过滤后的部门列表
const filteredDeptList = computed(() => {
  if (!deptList.value) return []
  
  return deptList.value.filter(dept => {
    return (
      (!searchForm.orgId || dept.orgId === searchForm.orgId) &&
      (!searchForm.deptName || dept.deptName.includes(searchForm.deptName)) &&
      (searchForm.status === '' || dept.status === searchForm.status)
    )
  })
})

// 获取部门列表
const fetchDeptList = async () => {
  loading.value = true
  try {
    const response = await getDepartmentList()
    deptList.value = response.data || []
  } catch (error) {
    ElMessage.error('获取部门列表失败')
  } finally {
    loading.value = false
  }
}

// 获取组织机构列表
const fetchOrgOptions = async () => {
  try {
    const response = await getOrganizationList()
    orgOptions.value = response.data || []
  } catch (error) {
    ElMessage.error('获取组织机构列表失败')
  }
}

// 获取部门树
const fetchDeptTree = async (orgId) => {
  if (!orgId) {
    deptTreeOptions.value = []
    return
  }
  
  try {
    const response = await getDepartmentTree(orgId)
    deptTreeOptions.value = response.data || []
  } catch (error) {
    ElMessage.error('获取部门树失败')
  }
}

// 搜索
const handleSearch = () => {
  // 前端本地过滤，已通过计算属性实现
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    orgId: '',
    deptName: '',
    status: ''
  })
}

// 组织变更
const handleOrgChange = async (orgId) => {
  searchForm.orgId = orgId
}

// 表单中组织变更
const handleFormOrgChange = async (orgId) => {
  deptForm.orgId = orgId
  deptForm.parentId = null
  await fetchDeptTree(orgId)
}

// 新增部门
const handleAdd = () => {
  dialogTitle.value = '新增部门'
  Object.assign(deptForm, {
    id: null,
    orgId: null,
    parentId: null,
    deptName: '',
    deptCode: '',
    leader: '',
    phone: '',
    email: '',
    sort: 1,
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑部门
const handleEdit = async (row) => {
  dialogTitle.value = '编辑部门'
  Object.assign(deptForm, { ...row })
  await fetchDeptTree(row.orgId)
  dialogVisible.value = true
}

// 删除部门
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该部门吗？删除后将无法恢复！', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteDepartment(row.id)
    ElMessage.success('删除成功')
    fetchDeptList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 验证部门编码是否存在
const validateDeptCode = async (rule, value, callback) => {
  if (!value || !deptForm.orgId) {
    callback()
    return
  }
  
  try {
    const response = await checkDeptCodeExists(deptForm.orgId, value, deptForm.id || '')
    if (response.data) {
      callback(new Error('该部门编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    callback()
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!deptFormRef.value) return
  
  try {
    await deptFormRef.value.validate()
    submitting.value = true
    
    if (deptForm.id) {
      await updateDepartment(deptForm)
      ElMessage.success('更新成功')
    } else {
      await addDepartment(deptForm)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchDeptList()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  } finally {
    submitting.value = false
  }
}

// 关闭对话框
const handleDialogClose = () => {
  deptFormRef.value?.resetFields()
}

// 页面加载时获取数据
onMounted(() => {
  fetchDeptList()
  fetchOrgOptions()
})
</script>

<style scoped>
.department-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-area {
  margin-bottom: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style> 