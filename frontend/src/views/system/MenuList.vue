<template>
  <div class="menu-container">
    <div class="action-bar">
      <el-button 
        v-if="hasPermission(PERMISSIONS.SYS.MENU.ADD)"
        type="primary" 
        @click="handleAddTopLevel"
      >
        新增顶级菜单
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
    </div>

    <el-table
      ref="menuTableRef"
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
      <el-table-column label="菜单名称">
        <template #default="scope">
          <div class="menu-name">
            <el-icon
              v-if="scope.row.icon"
              class="menu-icon"
            >
              <component :is="scope.row.icon" />
            </el-icon>
            <span>{{ scope.row.menuName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="menuCode"
        label="菜单编码"
      />
      <el-table-column
        prop="menuType"
        label="菜单类型"
        width="120"
      >
        <template #default="scope">
          <el-tag :type="getMenuTypeTagType(scope.row.menuType)">
            {{ getMenuTypeName(scope.row.menuType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="path"
        label="路由地址"
      />
      <el-table-column
        prop="component"
        label="组件路径"
      />
      <el-table-column
        prop="permissionCode"
        label="权限标识"
      />
      <el-table-column
        prop="sort"
        label="排序"
        width="80"
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
        v-if="hasPermission(PERMISSIONS.SYS.MENU.EDIT) || hasPermission(PERMISSIONS.SYS.MENU.ADD) || hasPermission(PERMISSIONS.SYS.MENU.DELETE)"
        label="操作" 
        width="300" 
        fixed="right"
      >
        <template #default="scope">
          <el-button 
            v-if="hasPermission(PERMISSIONS.SYS.MENU.EDIT)"
            size="small" 
            @click="handleEdit(scope.row)"
          >
            编辑
          </el-button>
          <el-button 
            v-if="scope.row.menuType !== 3 && hasPermission(PERMISSIONS.SYS.MENU.ADD)" 
            size="small" 
            type="success" 
            @click="handleAddChild(scope.row)"
          >
            添加子菜单
          </el-button>
          <el-button 
            v-if="hasPermission(PERMISSIONS.SYS.MENU.DELETE)"
            size="small" 
            type="danger" 
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 菜单表单对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="formTitle"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="menuFormRef"
        :model="menuForm"
        :rules="menuRules"
        label-width="120px"
      >
        <el-form-item
          label="菜单名称"
          prop="menuName"
        >
          <el-input
            v-model="menuForm.menuName"
            placeholder="请输入菜单名称"
          />
        </el-form-item>
        <el-form-item
          label="菜单编码"
          prop="menuCode"
        >
          <el-input
            v-model="menuForm.menuCode"
            placeholder="请输入菜单编码"
          />
        </el-form-item>
        <el-form-item
          label="菜单类型"
          prop="menuType"
        >
          <el-radio-group v-model="menuForm.menuType">
            <el-radio :label="1">
              目录
            </el-radio>
            <el-radio :label="2">
              菜单
            </el-radio>
            <el-radio :label="3">
              按钮
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          v-if="menuForm.parentId !== 0"
          label="上级菜单"
        >
          <el-cascader
            v-model="menuForm.parentId"
            :options="parentMenuOptions"
            :props="{
              checkStrictly: true,
              label: 'menuName',
              value: 'id',
              emitPath: false
            }"
            clearable
            placeholder="请选择上级菜单"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          v-if="menuForm.menuType !== 3"
          label="图标"
        >
          <el-input
            v-model="menuForm.icon"
            placeholder="请输入图标"
          />
        </el-form-item>
        <el-form-item
          v-if="menuForm.menuType !== 3"
          label="路由地址"
          prop="path"
        >
          <el-input
            v-model="menuForm.path"
            placeholder="请输入路由地址"
          />
        </el-form-item>
        <el-form-item
          v-if="menuForm.menuType === 2"
          label="组件路径"
        >
          <el-input
            v-model="menuForm.component"
            placeholder="请输入组件路径"
          />
        </el-form-item>
        <el-form-item
          v-if="menuForm.menuType === 3"
          label="权限标识"
        >
          <el-input
            v-model="menuForm.permissionCode"
            placeholder="请输入权限标识"
          />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number
            v-model="menuForm.sort"
            :min="0"
            :max="999"
          />
        </el-form-item>
        <el-form-item
          v-if="menuForm.menuType !== 3"
          label="显示状态"
        >
          <el-radio-group v-model="menuForm.visible">
            <el-radio :label="1">
              显示
            </el-radio>
            <el-radio :label="0">
              隐藏
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          v-if="menuForm.menuType !== 3"
          label="是否外链"
        >
          <el-radio-group v-model="menuForm.isFrame">
            <el-radio :label="1">
              是
            </el-radio>
            <el-radio :label="0">
              否
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="菜单状态"
          prop="status"
        >
          <el-switch
            v-model="menuForm.status"
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMenuTree, getMenuById, createMenu, updateMenu, deleteMenu } from '@/api/menu'
import { hasPermission, PERMISSIONS } from '@/utils/permission'

// 表格数据和加载状态
const tableData = ref([])
const tableLoading = ref(false)
const menuTableRef = ref(null)

// 菜单表单相关
const dialogVisible = ref(false)
const formTitle = ref('')
const menuFormRef = ref(null)
const menuForm = reactive({
  id: null,
  parentId: 0,
  menuName: '',
  menuCode: '',
  menuType: 1,
  path: '',
  component: '',
  icon: '',
  sort: 0,
  visible: 1,
  status: 1,
  permissionCode: '',
  isFrame: 0
})
const menuRules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  menuCode: [{ required: true, message: '请输入菜单编码', trigger: 'blur' }],
  menuType: [{ required: true, message: '请选择菜单类型', trigger: 'change' }],
  path: [{ required: true, message: '请输入路由地址', trigger: 'blur' }]
}

// 菜单类型选项
const parentMenuOptions = ref([])

// 初始化
onMounted(() => {
  fetchMenuList()
})

// 获取菜单列表
const fetchMenuList = async () => {
  try {
    tableLoading.value = true
    const { data } = await getMenuTree()
    tableData.value = data || []
    // 处理父级菜单选项
    parentMenuOptions.value = data.filter(item => item.menuType !== 3) || []
  } catch (error) {
    ElMessage.error('获取菜单列表失败')
    console.error(error)
  } finally {
    tableLoading.value = false
  }
}

// 菜单类型转换
const getMenuTypeName = (type) => {
  switch (type) {
  case 1: return '目录'
  case 2: return '菜单'
  case 3: return '按钮'
  default: return '未知'
  }
}

// 菜单类型标签
const getMenuTypeTagType = (type) => {
  switch (type) {
  case 1: return 'primary'
  case 2: return 'success'
  case 3: return 'warning'
  default: return 'info'
  }
}

// 展开全部
const expandAll = () => {
  if (menuTableRef.value) {
    tableData.value.forEach(row => {
      menuTableRef.value.toggleRowExpansion(row, true)
    })
  }
}

// 折叠全部
const collapseAll = () => {
  if (menuTableRef.value) {
    tableData.value.forEach(row => {
      menuTableRef.value.toggleRowExpansion(row, false)
    })
  }
}

// 新增顶级菜单
const handleAddTopLevel = () => {
  formTitle.value = '新增顶级菜单'
  Object.keys(menuForm).forEach(key => {
    menuForm[key] = ['status', 'visible', 'menuType'].includes(key) ? 1 : key === 'sort' ? 0 : key === 'isFrame' ? 0 : key === 'parentId' ? 0 : ''
  })
  dialogVisible.value = true
}

// 新增子菜单
const handleAddChild = (row) => {
  formTitle.value = '新增子菜单'
  Object.keys(menuForm).forEach(key => {
    menuForm[key] = ['status', 'visible', 'menuType'].includes(key) ? 1 : key === 'sort' ? 0 : key === 'isFrame' ? 0 : key === 'parentId' ? row.id : ''
  })
  
  // 子菜单类型根据父菜单类型自动设置
  if (row.menuType === 1) {
    menuForm.menuType = 2
  } else if (row.menuType === 2) {
    menuForm.menuType = 3
  }
  
  dialogVisible.value = true
}

// 编辑菜单
const handleEdit = async (row) => {
  formTitle.value = '编辑菜单'
  try {
    const { data } = await getMenuById(row.id)
    Object.keys(menuForm).forEach(key => {
      menuForm[key] = data[key]
    })
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取菜单信息失败')
    console.error(error)
  }
}

// 提交表单
const submitForm = async () => {
  if (!menuFormRef.value) return
  
  try {
    await menuFormRef.value.validate()
    
    // 根据菜单类型设置相关字段
    if (menuForm.menuType === 3) {
      menuForm.path = ''
      menuForm.component = ''
      menuForm.icon = ''
      menuForm.visible = 1
      menuForm.isFrame = 0
    } else if (menuForm.menuType === 1) {
      menuForm.component = ''
    }
    
    if (menuForm.id) {
      await updateMenu(menuForm.id, menuForm)
      ElMessage.success('更新成功')
    } else {
      await createMenu(menuForm)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    fetchMenuList()
  } catch (error) {
    console.error(error)
    ElMessage.error('提交失败')
  }
}

// 删除菜单
const handleDelete = (row) => {
  if (row.children && row.children.length > 0) {
    ElMessage.warning('该菜单下有子菜单，请先删除子菜单')
    return
  }
  
  ElMessageBox.confirm('确定要删除该菜单吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteMenu(row.id)
      ElMessage.success('删除成功')
      fetchMenuList()
    } catch (error) {
      ElMessage.error('删除失败')
      console.error(error)
    }
  }).catch(() => {})
}
</script>

<style scoped>
.menu-container {
  padding: 20px;
}
.action-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 15px;
}
.menu-name {
  display: flex;
  align-items: center;
}
.menu-icon {
  margin-right: 5px;
}
</style> 