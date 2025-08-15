<template>
  <el-select
    v-model="selectedValue"
    :placeholder="placeholder"
    :clearable="clearable"
    :disabled="disabled"
    :multiple="multiple"
    :filterable="filterable"
    :loading="loading"
    @change="handleChange"
  >
    <el-option
      v-for="item in options"
      :key="item.itemValue"
      :label="item.itemText"
      :value="convertValue(item.itemValue)"
      :disabled="item.status === 0"
    />
  </el-select>
</template>

<script setup>
import { ref, watch, onMounted, computed } from 'vue'
import { getDictItemsByDictCode } from '@/api/dict'

// Props
const props = defineProps({
  modelValue: {
    type: [String, Number, Array],
    default: null
  },
  dictCode: {
    type: String,
    required: true
  },
  placeholder: {
    type: String,
    default: '请选择'
  },
  clearable: {
    type: Boolean,
    default: true
  },
  disabled: {
    type: Boolean,
    default: false
  },
  multiple: {
    type: Boolean,
    default: false
  },
  filterable: {
    type: Boolean,
    default: false
  },
  includeDisabled: {
    type: Boolean,
    default: false
  },
  // 新增：值类型转换
  valueType: {
    type: String,
    default: 'auto', // 'string', 'number', 'auto'
    validator: (value) => ['string', 'number', 'auto'].includes(value)
  }
})

// Emits
const emit = defineEmits(['update:modelValue', 'change'])

// Data
const options = ref([])
const loading = ref(false)

// Methods
const convertValue = (value) => {
  if (props.valueType === 'number') {
    const num = Number(value)
    return isNaN(num) ? value : num
  } else if (props.valueType === 'string') {
    return String(value)
  } else {
    // auto: 尝试转换为数字，如果原始值是数字类型
    if (typeof props.modelValue === 'number') {
      const num = Number(value)
      return isNaN(num) ? value : num
    }
    return value
  }
}

// Computed
const selectedValue = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const fetchOptions = async () => {
  if (!props.dictCode) return
  
  loading.value = true
  try {
    // 根据是否包含禁用项决定是否传递状态参数
    const status = props.includeDisabled ? undefined : 1
    const response = await getDictItemsByDictCode(props.dictCode, status)
    
    if (response.success) {
      options.value = response.data || []
    } else {
      console.error('获取字典项失败:', response.message)
      options.value = []
    }
  } catch (error) {
    console.error('获取字典项失败:', error)
    options.value = []
  } finally {
    loading.value = false
  }
}

const handleChange = (value) => {
  emit('change', value)
}

// Watchers
watch(() => props.dictCode, () => {
  fetchOptions()
}, { immediate: true })

// Lifecycle
onMounted(() => {
  if (props.dictCode) {
    fetchOptions()
  }
})
</script>

<style scoped>
/* 组件样式，如果需要的话 */
</style> 