<template>
  <el-dialog 
    v-model="dialogVisible" 
    :title="isEdit ? '编辑供应商商品' : '新增供应商商品'"
    width="800px"
    :before-close="handleClose"
  >
    <el-form 
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="120px"
      v-loading="submitting"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="供应商" prop="supplierId">
            <el-select
              v-model="formData.supplierId"
              placeholder="请选择供应商"
              filterable
              style="width: 100%"
              :disabled="isEdit"
            >
              <el-option
                v-for="supplier in supplierList"
                :key="supplier.id"
                :label="supplier.supplierName"
                :value="supplier.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        
        <el-col :span="12">
          <el-form-item label="商品" prop="goodsId">
            <el-select
              v-model="formData.goodsId"
              placeholder="请选择商品"
              filterable
              remote
              :remote-method="searchGoods"
              :loading="goodsSearchLoading"
              style="width: 100%"
              :disabled="isEdit"
            >
              <el-option
                v-for="goods in goodsList"
                :key="goods.id"
                :label="`${goods.goodsName} (${goods.goodsCode})`"
                :value="goods.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="供应商商品编码">
            <el-input
              v-model="formData.supplierGoodsCode"
              placeholder="请输入供应商商品编码"
            />
          </el-form-item>
        </el-col>
        
        <el-col :span="12">
          <el-form-item label="供应商商品名称">
            <el-input
              v-model="formData.supplierGoodsName"
              placeholder="请输入供应商商品名称"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="采购价格" prop="purchasePrice">
            <el-input-number
              v-model="formData.purchasePrice"
              :precision="2"
              :min="0"
              style="width: 100%"
              placeholder="请输入采购价格"
            />
          </el-form-item>
        </el-col>
        
        <el-col :span="12">
          <el-form-item label="最小采购量">
            <el-input-number
              v-model="formData.minPurchaseQty"
              :min="1"
              style="width: 100%"
              placeholder="请输入最小采购量"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="交货天数">
            <el-input-number
              v-model="formData.deliveryDay"
              :min="1"
              style="width: 100%"
              placeholder="请输入交货天数"
            />
          </el-form-item>
        </el-col>
        

      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item label="备注">
            <el-input
              v-model="formData.remark"
              type="textarea"
              :rows="3"
              placeholder="请输入备注信息"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">
          {{ isEdit ? '更新' : '创建' }}
        </el-button>
        <el-button 
          v-if="!isEdit" 
          type="success" 
          @click="handleSubmitAndContinue" 
          :loading="submitting"
        >
          保存并继续
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { supplierGoodsApi } from '@/api/supplierGoods'
import { supplierApi } from '@/api/supplier'
import { goodsApi } from '@/api/goods'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  },
  formData: {
    type: Object,
    default: () => ({})
  },
  isEdit: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:visible', 'success'])

// 表单状态
const formRef = ref()
const dialogVisible = ref(false)
const submitting = ref(false)
const goodsSearchLoading = ref(false)
const supplierList = ref([])
const goodsList = ref([])

// 表单数据
const formData = reactive({
  supplierId: null,
  goodsId: null,
  skuId: null,
  supplierGoodsCode: '',
  supplierGoodsName: '',
  purchasePrice: null,
  minPurchaseQty: null,
  deliveryDay: null,
  status: 1,
  remark: ''
})

// 表单验证规则
const formRules = {
  supplierId: [
    { required: true, message: '请选择供应商', trigger: 'change' }
  ],
  goodsId: [
    { required: true, message: '请选择商品', trigger: 'change' }
  ],
  purchasePrice: [
    { required: true, message: '请输入采购价格', trigger: 'blur' },
    { type: 'number', min: 0, message: '采购价格不能小于0', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 监听显示状态
watch(() => props.visible, (val) => {
  dialogVisible.value = val
  if (val) {
    initForm()
    loadSuppliers()
  }
})

watch(dialogVisible, (val) => {
  emit('update:visible', val)
})

// 初始化表单
const initForm = () => {
  if (props.isEdit && props.formData) {
    Object.assign(formData, props.formData)
    // 如果是编辑模式，需要加载当前商品信息
    if (formData.goodsId) {
      loadGoodsById(formData.goodsId)
    }
  } else {
    // 重置表单
    Object.assign(formData, {
      supplierId: null,
      goodsId: null,
      skuId: null,
      supplierGoodsCode: '',
      supplierGoodsName: '',
      purchasePrice: null,
      minPurchaseQty: null,
      deliveryDay: null,
      remark: ''
    })
    goodsList.value = []
  }
  
  // 清空验证状态
  nextTick(() => {
    formRef.value?.clearValidate()
  })
}

// 加载供应商列表
const loadSuppliers = async () => {
  try {
    const response = await supplierApi.getAllActiveSuppliers()
    if (response.success) {
      supplierList.value = response.data
    }
  } catch (error) {
    console.error('加载供应商列表失败:', error)
  }
}

// 根据ID加载商品信息
const loadGoodsById = async (goodsId) => {
  try {
    const response = await goodsApi.getGoodsById(goodsId)
    if (response.code === 200) {
      goodsList.value = [response.data]
    }
  } catch (error) {
    console.error('加载商品信息失败:', error)
  }
}

// 搜索商品（从产品库中搜索，不是供应商商品）
const searchGoods = async (query) => {
  if (!query) {
    goodsList.value = []
    return
  }
  
  try {
    goodsSearchLoading.value = true
    const response = await goodsApi.searchGoods({ goodsName: query })
    if (response.code === 200) {
      goodsList.value = response.data
    }
  } catch (error) {
    console.error('搜索商品失败:', error)
  } finally {
    goodsSearchLoading.value = false
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    
    submitting.value = true
    
    // 检查是否已存在
    const existsResponse = await supplierGoodsApi.checkSupplierGoodsExists({
      supplierId: formData.supplierId,
      goodsId: formData.goodsId,
      skuId: formData.skuId || null
    })
    
    if (existsResponse.data && !props.isEdit) {
      ElMessage.warning('该供应商商品关联已存在')
      return
    }
    
    if (props.isEdit) {
      await supplierGoodsApi.updateSupplierGoods(props.formData.id, formData)
      ElMessage.success('更新成功')
    } else {
      await supplierGoodsApi.createSupplierGoods(formData)
      ElMessage.success('创建成功')
    }
    
    emit('success')
    handleClose()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitting.value = false
  }
}

// 保存并继续
const handleSubmitAndContinue = async () => {
  try {
    await formRef.value.validate()
    
    submitting.value = true
    
    await supplierGoodsApi.createSupplierGoods(formData)
    ElMessage.success('创建成功')
    
    emit('success')
    
    // 重置表单继续添加
    Object.assign(formData, {
      supplierId: formData.supplierId, // 保持供应商选择
      goodsId: null,
      skuId: null,
      supplierGoodsCode: '',
      supplierGoodsName: '',
      purchasePrice: null,
      minPurchaseQty: null,
      deliveryDay: null,
      status: 1,
      remark: ''
    })
    goodsList.value = []
    formRef.value?.clearValidate()
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    submitting.value = false
  }
}

// 关闭弹窗
const handleClose = () => {
  dialogVisible.value = false
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
}

.dialog-footer .el-button {
  margin-left: 10px;
}
</style> 