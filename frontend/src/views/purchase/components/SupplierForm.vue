<template>
  <el-dialog
    v-model="dialogVisible"
    :title="isEdit ? '编辑供应商' : '新增供应商'"
    :width="800"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      size="default"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="供应商名称"
            prop="supplierName"
          >
            <el-input
              v-model="formData.supplierName"
              placeholder="请输入供应商名称"
              maxlength="100"
              show-word-limit
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="供应商编码"
            prop="supplierCode"
          >
            <el-input
              v-model="formData.supplierCode"
              placeholder="请输入供应商编码"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="联系人"
            prop="contact"
          >
            <el-input
              v-model="formData.contact"
              placeholder="请输入联系人"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="联系电话"
            prop="mobile"
          >
            <el-input
              v-model="formData.mobile"
              placeholder="请输入联系电话"
              maxlength="20"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="邮箱"
            prop="email"
          >
            <el-input
              v-model="formData.email"
              placeholder="请输入邮箱"
              maxlength="100"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="状态"
            prop="status"
          >
            <el-radio-group v-model="formData.status">
              <el-radio :label="1">
                正常
              </el-radio>
              <el-radio :label="0">
                禁用
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="省市区"
            prop="region"
          >
            <el-cascader
              v-model="formData.region"
              :options="regionOptions"
              :props="cascaderProps"
              placeholder="请选择省市区"
              clearable
              filterable
              style="width: 100%"
              @change="handleRegionChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="详细地址"
            prop="address"
          >
            <el-input
              v-model="formData.address"
              placeholder="请输入详细地址"
              maxlength="200"
              show-word-limit
              @input="handleAddressInput"
            />
          </el-form-item>
        </el-col>
      </el-row>



      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="开户行"
            prop="bankName"
          >
            <el-input
              v-model="formData.bankName"
              placeholder="请输入开户行"
              maxlength="100"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="银行账号"
            prop="bankAccount"
          >
            <el-input
              v-model="formData.bankAccount"
              placeholder="请输入银行账号"
              maxlength="50"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item
        label="税号"
        prop="taxNo"
      >
        <el-input
          v-model="formData.taxNo"
          placeholder="请输入税号"
          maxlength="50"
        />
      </el-form-item>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="供应商类型"
            prop="supplierType"
          >
            <el-select
              v-model="formData.supplierType"
              placeholder="请选择供应商类型"
              clearable
              style="width: 100%"
            >
              <el-option
                label="镜框供应商"
                :value="1"
              />
              <el-option
                label="镜片供应商"
                :value="2"
              />
              <el-option
                label="配件供应商"
                :value="3"
              />
              <el-option
                label="设备供应商"
                :value="4"
              />
              <el-option
                label="其他"
                :value="5"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="供应商等级"
            prop="level"
          >
            <el-select
              v-model="formData.level"
              placeholder="请选择供应商等级"
              clearable
              style="width: 100%"
            >
              <el-option
                label="A级"
                :value="1"
              />
              <el-option
                label="B级"
                :value="2"
              />
              <el-option
                label="C级"
                :value="3"
              />
              <el-option
                label="D级"
                :value="4"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item
            label="信用等级"
            prop="creditLevel"
          >
            <el-select
              v-model="formData.creditLevel"
              placeholder="请选择信用等级"
              clearable
              style="width: 100%"
            >
              <el-option
                label="优秀"
                :value="1"
              />
              <el-option
                label="良好"
                :value="2"
              />
              <el-option
                label="一般"
                :value="3"
              />
              <el-option
                label="较差"
                :value="4"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item
            label="结算方式"
            prop="settlementType"
          >
            <el-select
              v-model="formData.settlementType"
              placeholder="请选择结算方式"
              clearable
              style="width: 100%"
            >
              <el-option
                label="现结"
                :value="1"
              />
              <el-option
                label="月结"
                :value="2"
              />
              <el-option
                label="季结"
                :value="3"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-form-item 
        v-if="formData.settlementType && formData.settlementType !== 1" 
        label="结算天数" 
        prop="settlementDay"
      >
        <el-input-number
          v-model="formData.settlementDay"
          placeholder="请输入结算天数"
          :min="1"
          :max="365"
          controls-position="right"
          style="width: 200px"
        />
        <span class="form-tip">天</span>
      </el-form-item>

      <el-form-item
        label="备注"
        prop="remark"
      >
        <el-input
          v-model="formData.remark"
          type="textarea"
          placeholder="请输入备注"
          :rows="3"
          maxlength="500"
          show-word-limit
        />
      </el-form-item>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">
          取消
        </el-button>
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
import { ref, reactive, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { supplierApi } from '@/api/supplier'
import { regionData, extractRegionFromAddress } from '@/utils/regionData'

// Props
const props = defineProps({
  modelValue: {
    type: Boolean,
    default: false
  },
  supplierData: {
    type: Object,
    default: null
  },
  isEdit: {
    type: Boolean,
    default: false
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'success'])

// 响应式数据
const formRef = ref()
const submitLoading = ref(false)
const dialogVisible = ref(false)

// 省市区数据
const regionOptions = ref(regionData)

// 级联选择器配置
const cascaderProps = {
  expandTrigger: 'hover',
  value: 'value',
  label: 'label',
  children: 'children'
}

// 表单数据
const formData = reactive({
  supplierName: '',
  supplierCode: '',
  contact: '',
  mobile: '',
  email: '',
  region: [], // 省市区级联选择
  province: '',
  city: '',
  district: '',
  address: '',
  bankName: '',
  bankAccount: '',
  taxNo: '',
  status: 1,
  supplierType: null,
  level: null,
  creditLevel: null,
  settlementType: null,
  settlementDay: null,
  remark: ''
})

// 省市区变化处理
const handleRegionChange = (value) => {
  if (value && value.length >= 3) {
    // 根据选择的省市区代码获取名称
    const provinceCode = value[0]
    const cityCode = value[1]
    const districtCode = value[2]
    
    // 从省市区数据中查找对应的名称
    for (const province of regionData) {
      if (province.value === provinceCode) {
        formData.province = province.label
        for (const city of province.children || []) {
          if (city.value === cityCode) {
            formData.city = city.label
            for (const district of city.children || []) {
              if (district.value === districtCode) {
                formData.district = district.label
                break
              }
            }
            break
          }
        }
        break
      }
    }
  } else {
    // 清空省市区名称
    formData.province = ''
    formData.city = ''
    formData.district = ''
  }
}

// 地址输入处理 - 自动识别省市区
const handleAddressInput = (value) => {
  if (value) {
    const extractedRegion = extractRegionFromAddress(value)
    if (extractedRegion.province || extractedRegion.city || extractedRegion.district) {
      // 更新省市区名称
      if (extractedRegion.province) formData.province = extractedRegion.province
      if (extractedRegion.city) formData.city = extractedRegion.city
      if (extractedRegion.district) formData.district = extractedRegion.district
      
      // 尝试更新级联选择器的值
      updateRegionCascader(extractedRegion)
    }
  }
}

// 更新级联选择器值
const updateRegionCascader = (regionInfo) => {
  const cascaderValue = []
  
  if (regionInfo.province) {
    // 查找省份
    for (const province of regionData) {
      if (province.label === regionInfo.province) {
        cascaderValue.push(province.value)
        
        if (regionInfo.city) {
          // 查找城市
          for (const city of province.children || []) {
            if (city.label === regionInfo.city) {
              cascaderValue.push(city.value)
              
              if (regionInfo.district) {
                // 查找区县
                for (const district of city.children || []) {
                  if (district.label === regionInfo.district) {
                    cascaderValue.push(district.value)
                    break
                  }
                }
              }
              break
            }
          }
        }
        break
      }
    }
  }
  
  if (cascaderValue.length > 0) {
    formData.region = cascaderValue
  }
}

// 表单验证规则
const formRules = {
  supplierName: [
    { required: true, message: '请输入供应商名称', trigger: 'blur' },
    { min: 2, max: 100, message: '供应商名称长度为2-100个字符', trigger: 'blur' }
  ],
  supplierCode: [
    { max: 50, message: '供应商编码长度不能超过50个字符', trigger: 'blur' }
  ],
  contact: [
    { max: 50, message: '联系人长度不能超过50个字符', trigger: 'blur' }
  ],
  mobile: [
    { 
      pattern: /^1[3-9]\d{9}$/, 
      message: '请输入正确的手机号码', 
      trigger: 'blur' 
    }
  ],
  email: [
    { 
      type: 'email', 
      message: '请输入正确的邮箱地址', 
      trigger: 'blur' 
    }
  ],
  region: [
    { type: 'array', message: '请选择省市区', trigger: 'change' }
  ],
  address: [
    { max: 200, message: '详细地址长度不能超过200个字符', trigger: 'blur' }
  ],
  bankName: [
    { max: 100, message: '开户行长度不能超过100个字符', trigger: 'blur' }
  ],
  bankAccount: [
    { max: 50, message: '银行账号长度不能超过50个字符', trigger: 'blur' }
  ],
  taxNo: [
    { max: 50, message: '税号长度不能超过50个字符', trigger: 'blur' }
  ],
  settlementDay: [
    { type: 'number', min: 1, max: 365, message: '结算天数必须在1-365之间', trigger: 'blur' }
  ],
  remark: [
    { max: 500, message: '备注长度不能超过500个字符', trigger: 'blur' }
  ]
}

// 自定义验证器
const validateSupplierCode = async (rule, value, callback) => {
  if (!value) {
    callback()
    return
  }
  
  try {
    const excludeId = props.isEdit ? props.supplierData?.id : null
    const response = await supplierApi.checkSupplierCode(value, excludeId)
    if (response.success && response.data) {
      callback(new Error('供应商编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    console.error('验证供应商编码失败:', error)
    callback()
  }
}

const validateSupplierName = async (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入供应商名称'))
    return
  }
  
  try {
    const excludeId = props.isEdit ? props.supplierData?.id : null
    const response = await supplierApi.checkSupplierName(value, excludeId)
    if (response.success && response.data) {
      callback(new Error('供应商名称已存在'))
    } else {
      callback()
    }
  } catch (error) {
    console.error('验证供应商名称失败:', error)
    callback()
  }
}

// 添加自定义验证规则
formRules.supplierCode.push({ validator: validateSupplierCode, trigger: 'blur' })
formRules.supplierName.push({ validator: validateSupplierName, trigger: 'blur' })

// 监听对话框显示状态
watch(() => props.modelValue, (val) => {
  dialogVisible.value = val
  if (val) {
    nextTick(() => {
      resetForm()
      if (props.supplierData && props.isEdit) {
        Object.assign(formData, props.supplierData)
        // 根据省市区名称设置级联选择器的值
        if (formData.province || formData.city || formData.district) {
          updateRegionCascader({
            province: formData.province,
            city: formData.city,
            district: formData.district
          })
        }
      }
    })
  }
})

watch(dialogVisible, (val) => {
  emit('update:modelValue', val)
})

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  Object.assign(formData, {
    supplierName: '',
    supplierCode: '',
    contact: '',
    mobile: '',
    email: '',
    region: [], // 省市区级联选择
    province: '',
    city: '',
    district: '',
    address: '',
    bankName: '',
    bankAccount: '',
    taxNo: '',
    status: 1,
    supplierType: null,
    level: null,
    creditLevel: null,
    settlementType: null,
    settlementDay: null,
    remark: ''
  })
}

// 关闭对话框
const handleClose = () => {
  dialogVisible.value = false
}

// 提交表单
const handleSubmit = async () => {
  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    submitLoading.value = true

    // 准备提交数据
    const submitData = { ...formData }
    
    // 清除空值
    Object.keys(submitData).forEach(key => {
      if (submitData[key] === '' || submitData[key] === null || submitData[key] === undefined) {
        delete submitData[key]
      }
    })

    let response
    if (props.isEdit) {
      response = await supplierApi.updateSupplier(props.supplierData.id, submitData)
    } else {
      response = await supplierApi.createSupplier(submitData)
    }

    if (response.success) {
      ElMessage.success(response.message || (props.isEdit ? '更新成功' : '创建成功'))
      emit('success')
      handleClose()
    } else {
      ElMessage.error(response.message || (props.isEdit ? '更新失败' : '创建失败'))
    }
  } catch (error) {
    console.error('提交供应商数据失败:', error)
    ElMessage.error(props.isEdit ? '更新失败' : '创建失败')
  } finally {
    submitLoading.value = false
  }
}
</script>

<style scoped>
.form-tip {
  margin-left: 8px;
  color: #909399;
  font-size: 14px;
}

.dialog-footer {
  text-align: right;
}

:deep(.el-form-item__label) {
  font-weight: 500;
}

:deep(.el-input-number) {
  width: 100%;
}
</style> 