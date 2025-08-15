<template>
  <div class="position-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>岗位管理</span>
          <el-button 
            v-if="hasActionPermission(PERMISSIONS.ORGANIZATION.POSITION.CREATE)"
            type="primary" 
            @click="handleAdd"
          >
            新增岗位
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
          <el-form-item label="所属组织">
            <el-select
              v-model="searchForm.orgId"
              placeholder="请选择组织"
              clearable
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
          <el-form-item label="所属部门">
            <el-select
              v-model="searchForm.deptId"
              placeholder="请选择部门"
              clearable
            >
              <el-option
                v-for="dept in deptOptions"
                :key="dept.id"
                :label="dept.deptName"
                :value="dept.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="岗位名称">
            <el-input
              v-model="searchForm.positionName"
              placeholder="请输入岗位名称"
              clearable
            />
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="searchForm.status"
              placeholder="请选择状态"
              clearable
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
              搜索
            </el-button>
            <el-button @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 岗位列表 -->
      <el-table
        v-loading="loading"
        :data="positionList"
        stripe
        border
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
          prop="positionName"
          label="岗位名称"
          width="150"
        />
        <el-table-column
          prop="positionCode"
          label="岗位编码"
          width="120"
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
          prop="sort"
          label="排序"
          width="80"
          align="center"
        />
        <el-table-column
          prop="status"
          label="状态"
          width="80"
        >
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
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
          v-if="hasActionPermission(PERMISSIONS.ORGANIZATION.POSITION.EDIT) || hasActionPermission(PERMISSIONS.ORGANIZATION.POSITION.DELETE)"
          label="操作" 
          width="200" 
          fixed="right"
        >
          <template #default="{ row }">
            <el-button 
              v-if="hasActionPermission(PERMISSIONS.ORGANIZATION.POSITION.EDIT)"
              type="primary" 
              size="small" 
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button 
              v-if="hasActionPermission(PERMISSIONS.ORGANIZATION.POSITION.DELETE)"
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

    <!-- 岗位表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="positionFormRef"
        :model="positionForm"
        :rules="positionRules"
        label-width="100px"
      >
        <el-form-item
          label="所属组织"
          prop="orgId"
        >
          <el-select
            v-model="positionForm.orgId"
            placeholder="请选择组织"
            @change="handleFormOrgChange"
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
          label="所属部门"
          prop="deptId"
        >
          <el-select
            v-model="positionForm.deptId"
            placeholder="请选择部门"
          >
            <el-option
              v-for="dept in formDeptOptions"
              :key="dept.id"
              :label="dept.deptName"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="岗位名称"
          prop="positionName"
        >
          <el-input
            v-model="positionForm.positionName"
            placeholder="请输入岗位名称"
          />
        </el-form-item>
        <el-form-item
          label="岗位编码"
          prop="positionCode"
        >
          <el-input
            v-model="positionForm.positionCode"
            placeholder="请输入岗位编码"
          />
        </el-form-item>
        <el-form-item
          label="排序"
          prop="sort"
        >
          <el-input-number
            v-model="positionForm.sort"
            :min="1"
            :max="999"
          />
        </el-form-item>
        <el-form-item
          label="状态"
          prop="status"
        >
          <el-radio-group v-model="positionForm.status">
            <el-radio :label="1">
              启用
            </el-radio>
            <el-radio :label="0">
              禁用
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="备注"
          prop="remark"
        >
          <el-input
            v-model="positionForm.remark"
            type="textarea"
            placeholder="请输入备注"
            :rows="3"
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
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDateTime } from '@/utils/format'
import { 
  getPositionList,
  addPosition,
  updatePosition,
  deletePosition,
  getPositionsByOrgId,
  checkPositionCodeExists,
  getPositionListWithInfo,
  getPositionManagementList
} from '@/api/position'
import { getOrganizationList } from '@/api/organization'
import { getDepartmentsByOrgId } from '@/api/department'
import { hasActionPermission, PERMISSIONS } from '@/utils/permission'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const positionList = ref([])
const orgOptions = ref([])
const deptOptions = ref([])
const formDeptOptions = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const positionFormRef = ref()

// 搜索表单
const searchForm = reactive({
  orgId: '',
  deptId: '',
  positionName: '',
  status: ''
})

// 岗位表单
const positionForm = reactive({
  id: null,
  orgId: null,
  deptId: null,
  positionName: '',
  positionCode: '',
  sort: 1,
  status: 1,
  remark: ''
})

// 表单验证规则
const positionRules = {
  orgId: [
    { required: true, message: '请选择所属组织', trigger: 'change' }
  ],
  deptId: [
    { required: true, message: '请选择所属部门', trigger: 'change' }
  ],
  positionName: [
    { required: true, message: '请输入岗位名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  positionCode: [
    { required: true, message: '请输入岗位编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  sort: [
    { required: true, message: '请输入排序', trigger: 'blur' }
  ]
}

// 获取岗位列表
const fetchPositionList = async () => {
  loading.value = true
  try {
    const response = await getPositionManagementList(
      searchForm.orgId || null, 
      searchForm.deptId || null,
      searchForm.status !== '' ? searchForm.status : null,
      searchForm.positionName || null
    )
    positionList.value = response.data || []
  } catch (error) {
    ElMessage.error('获取岗位列表失败')
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

// 获取部门列表
const fetchDeptOptions = async (orgId) => {
  if (!orgId) {
    deptOptions.value = []
    return
  }
  
  try {
    const response = await getDepartmentsByOrgId(orgId)
    deptOptions.value = response.data || []
  } catch (error) {
    ElMessage.error('获取部门列表失败')
  }
}

// 获取表单部门列表
const fetchFormDeptOptions = async (orgId) => {
  if (!orgId) {
    formDeptOptions.value = []
    return
  }
  
  try {
    const response = await getDepartmentsByOrgId(orgId)
    formDeptOptions.value = response.data || []
  } catch (error) {
    ElMessage.error('获取部门列表失败')
  }
}

// 组织变更
const handleOrgChange = async (orgId) => {
  searchForm.orgId = orgId
  searchForm.deptId = ''
  await fetchDeptOptions(orgId)
}

// 表单中组织变更
const handleFormOrgChange = async (orgId) => {
  positionForm.orgId = orgId
  positionForm.deptId = null
  await fetchFormDeptOptions(orgId)
}

// 搜索
const handleSearch = () => {
  fetchPositionList()
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    orgId: '',
    deptId: '',
    positionName: '',
    status: ''
  })
  fetchPositionList()
}

// 新增岗位
const handleAdd = () => {
  dialogTitle.value = '新增岗位'
  Object.assign(positionForm, {
    id: null,
    orgId: null,
    deptId: null,
    positionName: '',
    positionCode: '',
    sort: 1,
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑岗位
const handleEdit = async (row) => {
  dialogTitle.value = '编辑岗位'
  Object.assign(positionForm, { ...row })
  await fetchFormDeptOptions(row.orgId)
  dialogVisible.value = true
}

// 删除岗位
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该岗位吗？删除后将无法恢复！', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deletePosition(row.id)
    ElMessage.success('删除成功')
    fetchPositionList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 验证岗位编码是否存在
const validatePositionCode = async (rule, value, callback) => {
  if (!value || !positionForm.orgId) {
    callback()
    return
  }
  
  try {
    const response = await checkPositionCodeExists(positionForm.orgId, value, positionForm.id || '')
    if (response.data) {
      callback(new Error('该岗位编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    callback()
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!positionFormRef.value) return
  
  try {
    await positionFormRef.value.validate()
    submitting.value = true
    
    if (positionForm.id) {
      await updatePosition(positionForm)
      ElMessage.success('更新成功')
    } else {
      await addPosition(positionForm)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchPositionList()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  } finally {
    submitting.value = false
  }
}

// 关闭对话框
const handleDialogClose = () => {
  positionFormRef.value?.resetFields()
}

// 页面加载时获取数据
onMounted(() => {
  fetchPositionList()
  fetchOrgOptions()
})
</script>

<style scoped>
.position-list {
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