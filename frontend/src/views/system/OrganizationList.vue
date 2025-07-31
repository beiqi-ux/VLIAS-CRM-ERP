<template>
  <div class="organization-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>组织机构管理</span>
          <el-button type="primary" @click="handleAdd">新增组织机构</el-button>
        </div>
      </template>

      <!-- 搜索区域 -->
      <div class="search-area">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="机构名称">
            <el-input v-model="searchForm.orgName" placeholder="请输入机构名称" clearable />
          </el-form-item>
          <el-form-item label="机构编码">
            <el-input v-model="searchForm.orgCode" placeholder="请输入机构编码" clearable />
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

      <!-- 组织机构列表 -->
      <el-table :data="filteredOrgList" v-loading="loading" row-key="id" default-expand-all>
        <el-table-column prop="id" label="ID" width="80">
          <template #default="{ row }">
            {{ $formatId(row.id) }}
          </template>
        </el-table-column>
        <el-table-column prop="orgName" label="机构名称" width="180" />
        <el-table-column prop="orgCode" label="机构编码" width="120" />
        <el-table-column prop="orgType" label="机构类型" width="120">
          <template #default="{ row }">
            <el-tag>{{ getOrgTypeLabel(row.orgType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="leader" label="负责人" width="120" />
        <el-table-column prop="phone" label="联系电话" width="130" />
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
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 组织机构表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form
        ref="orgFormRef"
        :model="orgForm"
        :rules="orgRules"
        label-width="100px"
      >
        <el-form-item label="上级机构" prop="parentId">
          <el-tree-select
            v-model="orgForm.parentId"
            :data="orgTreeOptions"
            node-key="id"
            :props="{ label: 'orgName', children: 'children' }"
            placeholder="请选择上级机构"
            check-strictly
            clearable
          />
        </el-form-item>
        <el-form-item label="机构名称" prop="orgName">
          <el-input v-model="orgForm.orgName" placeholder="请输入机构名称" />
        </el-form-item>
        <el-form-item label="机构编码" prop="orgCode">
          <el-input v-model="orgForm.orgCode" placeholder="请输入机构编码" />
        </el-form-item>
        <el-form-item label="机构类型" prop="orgType">
          <el-select v-model="orgForm.orgType" placeholder="请选择机构类型">
            <el-option label="集团" :value="1" />
            <el-option label="公司" :value="2" />
            <el-option label="分公司" :value="3" />
            <el-option label="部门" :value="4" />
            <el-option label="其他" :value="5" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="orgForm.sort" :min="1" :max="999" />
        </el-form-item>
        <el-form-item label="负责人" prop="leader">
          <el-input v-model="orgForm.leader" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="orgForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="orgForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="orgForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="orgForm.remark"
            type="textarea"
            placeholder="请输入备注"
            :rows="3"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">
            确定
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { formatDateTime } from '@/utils/format'
import { 
  getOrganizationList,
  getOrganizationTree,
  addOrganization,
  updateOrganization,
  deleteOrganization,
  checkOrgCodeExists,
  checkOrgNameExists
} from '@/api/organization'

// 响应式数据
const loading = ref(false)
const submitting = ref(false)
const orgList = ref([])
const orgTreeOptions = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const orgFormRef = ref()

// 搜索表单
const searchForm = reactive({
  orgName: '',
  orgCode: '',
  status: ''
})

// 组织机构表单
const orgForm = reactive({
  id: null,
  parentId: null,
  orgName: '',
  orgCode: '',
  orgType: 2,
  sort: 1,
  leader: '',
  phone: '',
  email: '',
  status: 1,
  remark: ''
})

// 表单验证规则
const orgRules = {
  orgName: [
    { required: true, message: '请输入机构名称', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  orgCode: [
    { required: true, message: '请输入机构编码', trigger: 'blur' },
    { pattern: /^[A-Za-z0-9_]+$/, message: '只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  orgType: [
    { required: true, message: '请选择机构类型', trigger: 'change' }
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

// 机构类型标签
const getOrgTypeLabel = (type) => {
  const typeMap = {
    1: '集团',
    2: '公司',
    3: '分公司',
    4: '部门',
    5: '其他'
  }
  return typeMap[type] || '未知'
}

// 过滤后的组织机构列表
const filteredOrgList = computed(() => {
  if (!orgList.value) return []
  
  return orgList.value.filter(org => {
    return (
      (!searchForm.orgName || org.orgName.includes(searchForm.orgName)) &&
      (!searchForm.orgCode || org.orgCode.includes(searchForm.orgCode)) &&
      (searchForm.status === '' || org.status === searchForm.status)
    )
  })
})

// 获取组织机构列表
const fetchOrgList = async () => {
  loading.value = true
  try {
    const response = await getOrganizationList()
    orgList.value = response.data || []
  } catch (error) {
    ElMessage.error('获取组织机构列表失败')
  } finally {
    loading.value = false
  }
}

// 获取组织机构树
const fetchOrgTree = async () => {
  try {
    const response = await getOrganizationTree()
    orgTreeOptions.value = response.data || []
  } catch (error) {
    ElMessage.error('获取组织机构树失败')
  }
}

// 搜索
const handleSearch = () => {
  // 前端本地过滤，已通过计算属性实现
}

// 重置搜索
const handleReset = () => {
  Object.assign(searchForm, {
    orgName: '',
    orgCode: '',
    status: ''
  })
}

// 新增组织机构
const handleAdd = () => {
  dialogTitle.value = '新增组织机构'
  Object.assign(orgForm, {
    id: null,
    parentId: null,
    orgName: '',
    orgCode: '',
    orgType: 2,
    sort: 1,
    leader: '',
    phone: '',
    email: '',
    status: 1,
    remark: ''
  })
  dialogVisible.value = true
}

// 编辑组织机构
const handleEdit = (row) => {
  dialogTitle.value = '编辑组织机构'
  Object.assign(orgForm, { ...row })
  dialogVisible.value = true
}

// 删除组织机构
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该组织机构吗？删除后将无法恢复！', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteOrganization(row.id)
    ElMessage.success('删除成功')
    fetchOrgList()
    fetchOrgTree()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 验证组织机构名称是否存在
const validateOrgName = async (rule, value, callback) => {
  if (!value) {
    callback()
    return
  }
  
  try {
    const response = await checkOrgNameExists(value, orgForm.id || '')
    if (response.data) {
      callback(new Error('该组织机构名称已存在'))
    } else {
      callback()
    }
  } catch (error) {
    callback()
  }
}

// 验证组织机构编码是否存在
const validateOrgCode = async (rule, value, callback) => {
  if (!value) {
    callback()
    return
  }
  
  try {
    const response = await checkOrgCodeExists(value, orgForm.id || '')
    if (response.data) {
      callback(new Error('该组织机构编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    callback()
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!orgFormRef.value) return
  
  try {
    await orgFormRef.value.validate()
    submitting.value = true
    
    if (orgForm.id) {
      await updateOrganization(orgForm)
      ElMessage.success('更新成功')
    } else {
      await addOrganization(orgForm)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchOrgList()
    fetchOrgTree()
  } catch (error) {
    ElMessage.error('操作失败')
    console.error(error)
  } finally {
    submitting.value = false
  }
}

// 关闭对话框
const handleDialogClose = () => {
  orgFormRef.value?.resetFields()
}

// 页面加载时获取数据
onMounted(() => {
  fetchOrgList()
  fetchOrgTree()
})
</script>

<style scoped>
.organization-list {
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