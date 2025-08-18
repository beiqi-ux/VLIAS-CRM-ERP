<template>
  <el-dialog
    :model-value="visible"
    :title="isEdit ? '编辑库位' : '新增库位'"
    width="800px"
    :before-close="handleClose"
    :close-on-click-modal="false"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      size="default"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="库位名称" prop="locationName">
            <el-input
              v-model="formData.locationName"
              placeholder="请输入库位名称"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="库位编码" prop="locationCode">
            <el-input
              v-model="formData.locationCode"
              placeholder="请输入库位编码"
              clearable
              @blur="checkLocationCode"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="所属仓库" prop="warehouseId">
            <el-select
              v-model="formData.warehouseId"
              placeholder="请选择仓库"
              filterable
              style="width: 100%"
              @change="handleWarehouseChange"
            >
              <el-option
                v-for="warehouse in warehouseList"
                :key="warehouse.id"
                :label="warehouse.warehouseName"
                :value="warehouse.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="所属库区" prop="areaId">
            <el-select
              v-model="formData.areaId"
              placeholder="请选择库区"
              filterable
              clearable
              style="width: 100%"
            >
              <el-option
                v-for="area in filteredAreaList"
                :key="area.id"
                :label="area.areaName"
                :value="area.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio :label="1">启用</el-radio>
              <el-radio :label="0">禁用</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="排序" prop="sort">
            <el-input-number
              v-model="formData.sort"
              :min="0"
              :max="999999"
              controls-position="right"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item label="备注" prop="remark">
        <el-input
          v-model="formData.remark"
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
          :loading="submitLoading"
          @click="handleSubmit"
        >
          确定
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, watch, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { locationApi } from '@/api/location'
import { warehouseApi } from '@/api/warehouse'
import { areaApi } from '@/api/area'

// Props
const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  locationData: {
    type: Object,
    default: null
  },
  isEdit: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['close', 'success'])

// 响应式数据
const formRef = ref()
const submitLoading = ref(false)
const warehouseList = ref([])
const areaList = ref([])

// 表单数据
const formData = reactive({
  locationName: '',
  locationCode: '',
  warehouseId: null,
  areaId: null,
  status: 1,
  sort: 0,
  remark: ''
})

// 计算属性
const filteredAreaList = computed(() => {
  if (!formData.warehouseId) {
    return []
  }
  return areaList.value.filter(area => area.warehouseId === formData.warehouseId)
})

// 表单验证规则
const formRules = {
  locationName: [
    { required: true, message: '请输入库位名称', trigger: 'blur' },
    { min: 1, max: 50, message: '库位名称长度在 1 到 50 个字符', trigger: 'blur' }
  ],
  locationCode: [
    { max: 50, message: '库位编码长度不能超过 50 个字符', trigger: 'blur' },
    {
      validator: async (rule, value, callback) => {
        if (value && formData.warehouseId) {
          try {
            const response = await locationApi.checkCode(
              formData.warehouseId,
              value,
              props.isEdit ? props.locationData?.id : null
            )
            if (response.data?.exists) {
              callback(new Error('该仓库下的库位编码已存在'))
            } else {
              callback()
            }
          } catch (error) {
            callback()
          }
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  warehouseId: [
    { required: true, message: '请选择仓库', trigger: 'change' }
  ],
  sort: [
    { type: 'number', min: 0, max: 999999, message: '排序值范围为 0-999999', trigger: 'blur' }
  ]
}

// 监听器
watch(() => props.visible, (newVal) => {
  if (newVal) {
    resetForm()
    if (props.isEdit && props.locationData) {
      Object.assign(formData, {
        locationName: props.locationData.locationName,
        locationCode: props.locationData.locationCode,
        warehouseId: props.locationData.warehouseId,
        areaId: props.locationData.areaId,
        status: props.locationData.status,
        sort: props.locationData.sort || 0,
        remark: props.locationData.remark || ''
      })
    }
  }
})

watch(() => formData.warehouseId, (newVal) => {
  if (!newVal) {
    formData.areaId = null
  } else {
    // 检查当前选择的库区是否属于新选择的仓库
    if (formData.areaId) {
      const selectedArea = areaList.value.find(area => area.id === formData.areaId)
      if (!selectedArea || selectedArea.warehouseId !== newVal) {
        formData.areaId = null
      }
    }
  }
})

// 方法
const loadWarehouses = async () => {
  try {
    const response = await warehouseApi.getActive()
    if (response.code === 200) {
      warehouseList.value = response.data
    }
  } catch (error) {
    console.error('加载仓库数据失败:', error)
  }
}

const loadAreas = async () => {
  try {
    const response = await areaApi.getActive()
    if (response.code === 200) {
      areaList.value = response.data
    }
  } catch (error) {
    console.error('加载库区数据失败:', error)
  }
}

const handleWarehouseChange = () => {
  // 仓库变化时重置库区选择
  formData.areaId = null
}

const checkLocationCode = async () => {
  if (formData.locationCode && formData.warehouseId) {
    try {
      const response = await locationApi.checkCode(
        formData.warehouseId,
        formData.locationCode,
        props.isEdit ? props.locationData?.id : null
      )
      if (response.data?.exists) {
        ElMessage.warning('该仓库下的库位编码已存在')
      }
    } catch (error) {
      console.error('检查库位编码失败:', error)
    }
  }
}

const resetForm = () => {
  Object.assign(formData, {
    locationName: '',
    locationCode: '',
    warehouseId: null,
    areaId: null,
    status: 1,
    sort: 0,
    remark: ''
  })
  formRef.value?.resetFields()
}

const handleClose = () => {
  emit('close')
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    submitLoading.value = true
    
    const submitData = { ...formData }
    
    let response
    if (props.isEdit) {
      response = await locationApi.update(props.locationData.id, submitData)
    } else {
      response = await locationApi.create(submitData)
    }
    
    if (response.code === 200) {
      ElMessage.success(props.isEdit ? '库位更新成功' : '库位创建成功')
      emit('success')
    } else {
      ElMessage.error(response.message || (props.isEdit ? '更新失败' : '创建失败'))
    }
  } catch (error) {
    if (error !== false) { // 不是表单验证失败
      console.error('提交库位表单失败:', error)
      ElMessage.error(props.isEdit ? '更新失败' : '创建失败')
    }
  } finally {
    submitLoading.value = false
  }
}

// 生命周期
onMounted(async () => {
  await Promise.all([
    loadWarehouses(),
    loadAreas()
  ])
})
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}
</style> 