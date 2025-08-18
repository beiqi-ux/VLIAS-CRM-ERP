<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑仓库' : '新增仓库'"
    width="800px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="仓库名称" prop="warehouseName">
            <el-input
              v-model="form.warehouseName"
              placeholder="请输入仓库名称"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="仓库编码" prop="warehouseCode">
            <el-input
              v-model="form.warehouseCode"
              placeholder="请输入仓库编码"
              maxlength="50"
              @blur="checkWarehouseCode"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="联系人" prop="contact">
            <el-input
              v-model="form.contact"
              placeholder="请输入联系人"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="联系电话" prop="mobile">
            <el-input
              v-model="form.mobile"
              placeholder="请输入联系电话"
              maxlength="20"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="省份" prop="province">
            <el-input
              v-model="form.province"
              placeholder="请输入省份"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="城市" prop="city">
            <el-input
              v-model="form.city"
              placeholder="请输入城市"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="区县" prop="district">
            <el-input
              v-model="form.district"
              placeholder="请输入区县"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="详细地址" prop="address">
        <el-input
          v-model="form.address"
          placeholder="请输入详细地址"
          maxlength="200"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="form.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序" prop="sort">
            <el-input-number
              v-model="form.sort"
              :min="0"
              :max="999"
              placeholder="请输入排序"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="form.remark"
          type="textarea"
          :rows="3"
          placeholder="请输入备注"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button
          type="primary"
          :loading="loading"
          @click="handleSubmit"
        >
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { warehouseApi } from '@/api/warehouse'

// Props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  warehouse: {
    type: Object,
    default: null
  },
  isEdit: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['update:visible', 'success'])

// 响应式数据
const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref()

// 表单数据
const form = reactive({
  warehouseName: '',
  warehouseCode: '',
  contact: '',
  mobile: '',
  province: '',
  city: '',
  district: '',
  address: '',
  status: 1,
  sort: 0,
  remark: ''
})

// 表单验证规则
const rules = {
  warehouseName: [
    { required: true, message: '请输入仓库名称', trigger: 'blur' },
    { min: 1, max: 50, message: '仓库名称长度为1-50个字符', trigger: 'blur' }
  ],
  warehouseCode: [
    { required: true, message: '请输入仓库编码', trigger: 'blur' },
    { min: 1, max: 50, message: '仓库编码长度为1-50个字符', trigger: 'blur' },
    { 
      pattern: /^[A-Za-z0-9_-]+$/, 
      message: '仓库编码只能包含字母、数字、下划线和横线', 
      trigger: 'blur' 
    }
  ],
  mobile: [
    { 
      pattern: /^1[3-9]\d{9}$/, 
      message: '请输入正确的手机号码', 
      trigger: 'blur' 
    }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ],
  sort: [
    { type: 'number', min: 0, max: 999, message: '排序值必须在0-999之间', trigger: 'blur' }
  ]
}

// 监听对话框显示状态
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal
  if (newVal) {
    resetForm()
    if (props.isEdit && props.warehouse) {
      Object.assign(form, {
        warehouseName: props.warehouse.warehouseName || '',
        warehouseCode: props.warehouse.warehouseCode || '',
        contact: props.warehouse.contact || '',
        mobile: props.warehouse.mobile || '',
        province: props.warehouse.province || '',
        city: props.warehouse.city || '',
        district: props.warehouse.district || '',
        address: props.warehouse.address || '',
        status: props.warehouse.status ?? 1,
        sort: props.warehouse.sort ?? 0,
        remark: props.warehouse.remark || ''
      })
    }
  }
})

// 监听对话框内部状态变化
watch(dialogVisible, (newVal) => {
  emit('update:visible', newVal)
})

// 方法
const resetForm = () => {
  Object.assign(form, {
    warehouseName: '',
    warehouseCode: '',
    contact: '',
    mobile: '',
    province: '',
    city: '',
    district: '',
    address: '',
    status: 1,
    sort: 0,
    remark: ''
  })
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

const checkWarehouseCode = async () => {
  if (!form.warehouseCode) return
  
  try {
    const excludeId = props.isEdit && props.warehouse ? props.warehouse.id : null
    const response = await warehouseApi.checkCode(form.warehouseCode, excludeId)
    
    if (response.code === 200 && response.data.exists) {
      ElMessage.warning('仓库编码已存在，请重新输入')
      return false
    }
    return true
  } catch (error) {
    console.error('检查仓库编码失败：', error)
    return true // 检查失败时允许继续
  }
}

const handleSubmit = async () => {
  try {
    // 表单验证
    const valid = await formRef.value.validate()
    if (!valid) return
    
    // 检查仓库编码
    const codeValid = await checkWarehouseCode()
    if (!codeValid) return
    
    loading.value = true
    
    let response
    if (props.isEdit && props.warehouse) {
      response = await warehouseApi.update(props.warehouse.id, form)
    } else {
      response = await warehouseApi.create(form)
    }
    
    if (response.code === 200) {
      ElMessage.success(props.isEdit ? '更新成功' : '创建成功')
      emit('success')
      handleClose()
    } else {
      ElMessage.error(response.message || (props.isEdit ? '更新失败' : '创建失败'))
    }
  } catch (error) {
    console.error('提交表单失败：', error)
    ElMessage.error(props.isEdit ? '更新失败' : '创建失败')
  } finally {
    loading.value = false
  }
}

const handleClose = () => {
  dialogVisible.value = false
  resetForm()
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

.el-form-item {
  margin-bottom: 18px;
}
</style> 