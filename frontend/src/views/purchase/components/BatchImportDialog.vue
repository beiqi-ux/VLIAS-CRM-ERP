<template>
  <el-dialog
    v-model="dialogVisible"
    title="批量导入商品"
    width="70%"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="import-content">
      <!-- 导入说明 -->
      <el-alert
        title="导入说明"
        type="info"
        :closable="false"
        style="margin-bottom: 20px;"
      >
        <template #default>
          <ol style="margin: 0; padding-left: 20px;">
            <li>支持Excel文件格式（.xls, .xlsx）</li>
            <li>Excel表格应包含：商品名称、规格、单位、数量、单价等列</li>
            <li>商品名称为必填项，系统会自动匹配商品信息</li>
            <li>导入前请确保商品在商品库中已存在</li>
          </ol>
        </template>
      </el-alert>

      <!-- 模板下载 -->
      <div style="margin-bottom: 20px;">
        <el-button
          type="primary"
          size="small"
          @click="downloadTemplate"
        >
          <el-icon><Download /></el-icon>
          下载导入模板
        </el-button>
      </div>

      <!-- 文件上传 -->
      <el-upload
        ref="uploadRef"
        class="upload-demo"
        drag
        :action="uploadUrl"
        :headers="uploadHeaders"
        :before-upload="beforeUpload"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
        :file-list="fileList"
        :limit="1"
        accept=".xls,.xlsx"
        style="margin-bottom: 20px;"
      >
        <el-icon class="el-icon--upload">
          <upload-filled />
        </el-icon>
        <div class="el-upload__text">
          将Excel文件拖到此处，或<em>点击上传</em>
        </div>
        <template #tip>
          <div class="el-upload__tip">
            只能上传 .xls/.xlsx 格式文件，且不超过 10MB
          </div>
        </template>
      </el-upload>

      <!-- 预览数据 -->
      <div v-if="previewData.length > 0">
        <el-divider content-position="left">
          预览数据
        </el-divider>
        
        <el-table
          :data="previewData"
          border
          style="width: 100%"
          max-height="300"
          :row-class-name="tableRowClassName"
        >
          <el-table-column
            type="index"
            label="行号"
            width="60"
          />
          <el-table-column
            prop="goodsName"
            label="商品名称"
            min-width="150"
          >
            <template #default="scope">
              <span :class="{ 'error-text': scope.row.hasError && scope.row.errors.includes('商品不存在') }">
                {{ scope.row.goodsName }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="specification"
            label="规格"
            width="120"
          />
          <el-table-column
            prop="unit"
            label="单位"
            width="80"
          />
          <el-table-column
            prop="quantity"
            label="数量"
            width="100"
            align="right"
          >
            <template #default="scope">
              <span :class="{ 'error-text': scope.row.hasError && scope.row.errors.includes('数量格式错误') }">
                {{ scope.row.quantity }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="price"
            label="单价"
            width="120"
            align="right"
          >
            <template #default="scope">
              <span :class="{ 'error-text': scope.row.hasError && scope.row.errors.includes('单价格式错误') }">
                {{ scope.row.price }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            label="金额"
            width="120"
            align="right"
          >
            <template #default="scope">
              <span v-if="!scope.row.hasError">
                ￥{{ formatAmount(scope.row.quantity * scope.row.price) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="remark"
            label="备注"
            min-width="120"
          />
          <el-table-column
            label="状态"
            width="100"
          >
            <template #default="scope">
              <el-tag
                v-if="!scope.row.hasError"
                type="success"
                size="small"
              >
                正常
              </el-tag>
              <el-tooltip
                v-else
                :content="scope.row.errors.join('；')"
                placement="top"
              >
                <el-tag
                  type="danger"
                  size="small"
                >
                  错误
                </el-tag>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>

        <!-- 统计信息 -->
        <div class="import-summary">
          <el-row>
            <el-col :span="8">
              <div class="summary-item">
                <span class="label">总行数:</span>
                <span class="value">{{ previewData.length }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="summary-item">
                <span class="label">正常数据:</span>
                <span class="value success">{{ validDataCount }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="summary-item">
                <span class="label">错误数据:</span>
                <span class="value error">{{ errorDataCount }}</span>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>

    <template #footer>
      <el-button @click="handleClose">
        取消
      </el-button>
      <el-button 
        type="primary" 
        :disabled="validDataCount === 0" 
        :loading="importing"
        @click="handleConfirm"
      >
        确认导入（{{ validDataCount }}条）
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { UploadFilled, Download } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { goodsApi } from '@/api/goods'

const props = defineProps({
  visible: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:visible', 'confirm'])

const userStore = useUserStore()
const uploadRef = ref()
const importing = ref(false)
const fileList = ref([])
const previewData = ref([])

// 上传配置
const uploadUrl = `${import.meta.env.VITE_API_BASE_URL}/api/upload/excel`
const uploadHeaders = {
  'Authorization': `Bearer ${userStore.token}`
}

// 计算属性
const dialogVisible = computed({
  get: () => props.visible,
  set: (value) => emit('update:visible', value)
})

const validDataCount = computed(() => {
  return previewData.value.filter(item => !item.hasError).length
})

const errorDataCount = computed(() => {
  return previewData.value.filter(item => item.hasError).length
})

// 格式化金额
const formatAmount = (amount) => {
  return amount ? Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '0.00'
}

// 表格行样式
const tableRowClassName = ({ row }) => {
  return row.hasError ? 'error-row' : ''
}

// 下载模板
const downloadTemplate = () => {
  // 创建模板数据
  const templateData = [
    {
      '商品名称': '示例商品1',
      '规格': '500g',
      '单位': '盒',
      '数量': 10,
      '单价': 25.50,
      '备注': '备注信息'
    },
    {
      '商品名称': '示例商品2',
      '规格': '1kg',
      '单位': '袋',
      '数量': 5,
      '单价': 45.00,
      '备注': ''
    }
  ]

  // 这里应该调用后端API生成Excel文件并下载
  // 临时方案：提示用户
  ElMessage.info('模板下载功能开发中，请按照说明手动创建Excel文件')
}

// 上传前检查
const beforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                  file.type === 'application/vnd.ms-excel'
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isExcel) {
    ElMessage.error('只能上传Excel格式文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过 10MB!')
    return false
  }
  return true
}

// 上传成功
const handleUploadSuccess = (response) => {
  if (response.success) {
    ElMessage.success('文件上传成功')
    parseExcelData(response.data)
  } else {
    ElMessage.error(response.message || '文件上传失败')
  }
}

// 上传失败
const handleUploadError = (error) => {
  console.error('上传失败:', error)
  ElMessage.error('文件上传失败')
}

// 解析Excel数据
const parseExcelData = async (excelData) => {
  try {
    // 这里假设后端返回了解析后的数据
    // 实际项目中，可能需要调用专门的解析接口
    const parsedData = excelData.map((row, index) => ({
      rowIndex: index + 1,
      goodsName: row['商品名称'] || '',
      specification: row['规格'] || '',
      unit: row['单位'] || '个',
      quantity: parseFloat(row['数量']) || 0,
      price: parseFloat(row['单价']) || 0,
      remark: row['备注'] || '',
      hasError: false,
      errors: []
    }))

    // 验证数据
    await validateData(parsedData)
    previewData.value = parsedData
  } catch (error) {
    console.error('解析Excel数据失败:', error)
    ElMessage.error('解析Excel数据失败')
  }
}

// 验证数据
const validateData = async (data) => {
  // 获取所有商品信息用于验证
  const goodsResponse = await goodsApi.getAll()
  const allGoods = goodsResponse.success ? goodsResponse.data : []

  data.forEach(item => {
    const errors = []

    // 验证商品名称
    if (!item.goodsName.trim()) {
      errors.push('商品名称不能为空')
    } else {
      // 检查商品是否存在
      const goods = allGoods.find(g => g.goodsName === item.goodsName.trim())
      if (!goods) {
        errors.push('商品不存在')
      } else {
        // 补充商品信息
        item.goodsId = goods.id
        item.goodsCode = goods.goodsCode
        if (!item.specification) item.specification = goods.specification || ''
        if (!item.unit) item.unit = goods.unit || '个'
        if (!item.price) item.price = goods.purchasePrice || 0
      }
    }

    // 验证数量
    if (!item.quantity || item.quantity <= 0) {
      errors.push('数量格式错误')
    }

    // 验证单价
    if (item.price < 0) {
      errors.push('单价格式错误')
    }

    item.errors = errors
    item.hasError = errors.length > 0
  })
}

// 确认导入
const handleConfirm = () => {
  const validData = previewData.value.filter(item => !item.hasError)
  if (validData.length === 0) {
    ElMessage.warning('没有有效的数据可以导入')
    return
  }

  importing.value = true
  setTimeout(() => {
    emit('confirm', validData)
    importing.value = false
  }, 500)
}

// 关闭对话框
const handleClose = () => {
  emit('update:visible', false)
  resetData()
}

// 重置数据
const resetData = () => {
  fileList.value = []
  previewData.value = []
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}
</script>

<style scoped>
.import-content {
  max-height: 70vh;
  overflow-y: auto;
}

.import-summary {
  margin-top: 15px;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.summary-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.summary-item .label {
  color: #606266;
}

.summary-item .value {
  font-weight: bold;
}

.summary-item .value.success {
  color: #67c23a;
}

.summary-item .value.error {
  color: #f56c6c;
}

.error-text {
  color: #f56c6c;
}

:deep(.error-row) {
  background-color: #fef0f0;
}

:deep(.el-upload-dragger) {
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
  width: 100%;
  height: 120px;
  text-align: center;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
}

:deep(.el-upload-dragger:hover) {
  border-color: #409eff;
}
</style> 