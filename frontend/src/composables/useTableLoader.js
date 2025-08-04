/**
 * 表格数据加载组合式函数
 * 提供智能缓存、防抖、错误处理和统一的加载状态管理
 */

import { ref, reactive, watch, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { debounce } from 'lodash-es'
import { withRetry, ErrorHandler, ERROR_TYPES } from '@/utils/errorHandler'

export function useTableLoader(config = {}) {
  const {
    // API函数
    apiFunction,
    // 缓存配置
    cacheEnabled = true,
    cacheTTL = 5 * 60 * 1000, // 5分钟
    cacheKey = 'default',
    // 防抖配置
    debounceDelay = 300,
    // 分页配置
    defaultPageSize = 20,
    // 错误处理
    errorMessage = '获取数据失败',
    // 自动加载
    autoLoad = true,
    // 预加载
    preloadEnabled = false,
    preloadDelay = 2000,
    // 重试配置
    enableRetry = true,
    retryConfig = {},
    // 错误处理配置
    errorHandlerConfig = {}
  } = config

  // 创建错误处理器
  const errorHandler = new ErrorHandler({
    showMessage: true,
    logErrors: true,
    customMessages: {
      [ERROR_TYPES.NETWORK]: '网络连接失败，数据加载中断',
      [ERROR_TYPES.TIMEOUT]: '请求超时，请检查网络状况',
      [ERROR_TYPES.SERVER]: '服务器错误，请稍后重试'
    },
    ...errorHandlerConfig
  })

  // 响应式状态
  const loading = ref(false)
  const refreshing = ref(false)
  const initialLoading = ref(true) // 新增：初始加载状态，用于骨架屏
  const tableData = ref([])
  const total = ref(0)
  const error = ref(null)
  
  // 分页状态
  const pagination = reactive({
    current: 1,
    size: defaultPageSize,
    total: 0
  })
  
  // 搜索状态
  const searchForm = ref({})
  const lastSearchParams = ref({})
  
  // 缓存管理
  const cache = reactive({
    data: new Map(),
    timestamps: new Map(),
    maxSize: 50 // 最大缓存50个查询
  })
  
  // 请求队列管理
  const requestQueue = new Map()
  const abortController = ref(null)
  
  // 生成缓存键
  function generateCacheKey(params) {
    const baseKey = typeof cacheKey === 'function' ? cacheKey(params) : cacheKey
    const paramKey = JSON.stringify(params)
    return `${baseKey}:${paramKey}`
  }
  
  // 检查缓存是否有效
  function isCacheValid(key) {
    if (!cacheEnabled || !cache.data.has(key)) return false
    
    const timestamp = cache.timestamps.get(key)
    const age = Date.now() - timestamp
    return age < cacheTTL
  }
  
  // 获取缓存数据
  function getFromCache(key) {
    if (isCacheValid(key)) {
      console.log(`使用表格缓存: ${key}`)
      return cache.data.get(key)
    }
    return null
  }
  
  // 设置缓存数据
  function setCache(key, data) {
    if (!cacheEnabled) return
    
    // 清理过期缓存
    if (cache.data.size >= cache.maxSize) {
      const oldestKey = Array.from(cache.timestamps.entries())
        .sort(([,a], [,b]) => a - b)[0][0]
      cache.data.delete(oldestKey)
      cache.timestamps.delete(oldestKey)
    }
    
    cache.data.set(key, {
      data: JSON.parse(JSON.stringify(data.data)),
      total: data.total,
      timestamp: Date.now()
    })
    cache.timestamps.set(key, Date.now())
    
    console.log(`表格数据已缓存: ${key}`)
  }
  
  // 清理缓存
  function clearCache(pattern = null) {
    if (pattern) {
      // 清理匹配模式的缓存
      const keysToDelete = Array.from(cache.data.keys()).filter(key => 
        key.includes(pattern)
      )
      keysToDelete.forEach(key => {
        cache.data.delete(key)
        cache.timestamps.delete(key)
      })
      console.log(`清理匹配缓存: ${pattern}, 清理数量: ${keysToDelete.length}`)
    } else {
      // 清理所有缓存
      cache.data.clear()
      cache.timestamps.clear()
      console.log('清理所有表格缓存')
    }
  }
  
  // 构建请求参数
  function buildRequestParams() {
    return {
      page: pagination.current - 1, // 后端分页从0开始
      size: pagination.size,
      ...searchForm.value
    }
  }
  
  // 核心数据加载函数
  async function loadData(params = null, options = {}) {
    const {
      useCache = true,
      showLoading = true,
      isRefresh = false
    } = options
    
    // 取消之前的请求
    if (abortController.value) {
      abortController.value.abort()
    }
    abortController.value = new AbortController()
    
    const requestParams = params || buildRequestParams()
    const requestKey = generateCacheKey(requestParams)
    
    // 防止重复请求
    if (requestQueue.has(requestKey)) {
      console.log('请求已在队列中，等待结果')
      return await requestQueue.get(requestKey)
    }
    
    // 检查缓存
    if (useCache && !isRefresh) {
      const cachedData = getFromCache(requestKey)
      if (cachedData) {
        tableData.value = cachedData.data
        total.value = cachedData.total
        pagination.total = cachedData.total
        return { success: true, data: cachedData.data, total: cachedData.total }
      }
    }
    
    // 设置加载状态
    if (showLoading) {
      if (isRefresh) {
        refreshing.value = true
      } else {
        loading.value = true
      }
    }
    
    // 定义请求函数
    const requestFn = async (attemptCount = 0) => {
      if (attemptCount > 0) {
        console.log(`第${attemptCount}次重试加载表格数据`)
      }
      
      if (!apiFunction) {
        throw new Error('未配置API函数')
      }
      
      const response = await apiFunction(requestParams, {
        signal: abortController.value.signal
      })
      
      if (!response.success) {
        throw new Error(response.message || errorMessage)
      }
      
      return response
    }
    
    const loadPromise = (async () => {
      try {
        error.value = null
        console.log('加载表格数据:', requestParams)
        
        // 使用重试机制
        const response = enableRetry 
          ? await withRetry(requestFn, {
            maxRetries: 2,
            retryDelay: 1000,
            retryDelayMultiplier: 1.5,
            retryableErrors: [ERROR_TYPES.NETWORK, ERROR_TYPES.TIMEOUT, ERROR_TYPES.SERVER],
            ...retryConfig
          })
          : await requestFn()
        
        const responseData = response.data || {}
        let resultData, resultTotal
        
        // 兼容不同的响应格式
        if (responseData.content !== undefined) {
          // Spring Boot分页格式
          resultData = responseData.content
          resultTotal = responseData.totalElements
        } else if (responseData.data !== undefined) {
          // 自定义分页格式
          resultData = responseData.data
          resultTotal = responseData.total
        } else if (Array.isArray(responseData)) {
          // 简单数组格式
          resultData = responseData
          resultTotal = responseData.length
        } else {
          resultData = []
          resultTotal = 0
        }
        
        // 更新状态
        tableData.value = resultData
        total.value = resultTotal
        pagination.total = resultTotal
        
        // 缓存数据
        if (useCache) {
          setCache(requestKey, {
            data: resultData,
            total: resultTotal
          })
        }
        
        // 保存搜索参数
        lastSearchParams.value = { ...requestParams }
        
        // 首次加载完成
        if (initialLoading.value) {
          initialLoading.value = false
        }
        
        console.log(`表格数据加载成功，共 ${resultTotal} 条记录`)
        
        return {
          success: true,
          data: resultData,
          total: resultTotal
        }
        
      } catch (err) {
        // 请求被取消时不显示错误
        if (err.name === 'AbortError') {
          console.log('请求已取消')
          return { success: false, cancelled: true }
        }
        
        console.error('表格数据加载失败:', err)
        
        // 使用错误处理器处理错误
        const errorInfo = errorHandler.handle(err, '表格数据加载')
        error.value = errorInfo.message
        
        // 尝试使用过期缓存
        const cachedData = cache.data.get(requestKey)
        if (cachedData && !isRefresh && errorInfo.type === ERROR_TYPES.NETWORK) {
          console.warn('网络错误，使用过期缓存数据')
          tableData.value = cachedData.data
          total.value = cachedData.total
          pagination.total = cachedData.total
          
          ElMessage.warning('网络异常，显示缓存数据')
          return {
            success: true,
            data: cachedData.data,
            total: cachedData.total,
            fromCache: true
          }
        }
        
        // 清空数据
        tableData.value = []
        total.value = 0
        pagination.total = 0
        
        return { success: false, error: errorInfo.message, errorType: errorInfo.type }
        
      } finally {
        loading.value = false
        refreshing.value = false
        requestQueue.delete(requestKey)
        
        if (abortController.value) {
          abortController.value = null
        }
      }
    })()
    
    // 添加到请求队列
    requestQueue.set(requestKey, loadPromise)
    
    return await loadPromise
  }
  
  // 防抖加载
  const debouncedLoad = debounce(loadData, debounceDelay)
  
  // 搜索处理
  function handleSearch(formData = null, resetPage = true) {
    if (formData) {
      searchForm.value = { ...formData }
    }
    
    if (resetPage) {
      pagination.current = 1
    }
    
    return debouncedLoad()
  }
  
  // 重置搜索
  function resetSearch(defaultForm = {}) {
    searchForm.value = { ...defaultForm }
    pagination.current = 1
    return loadData()
  }
  
  // 刷新数据
  function refresh() {
    return loadData(null, { isRefresh: true, useCache: false })
  }
  
  // 分页处理
  function handleSizeChange(size) {
    pagination.size = size
    pagination.current = 1 // 重置到第一页
    return loadData()
  }
  
  function handleCurrentChange(current) {
    pagination.current = current
    return loadData()
  }
  
  // 预加载下一页
  function preloadNextPage() {
    if (!preloadEnabled || loading.value || refreshing.value) return
    
    const nextPage = pagination.current + 1
    const maxPage = Math.ceil(pagination.total / pagination.size)
    
    if (nextPage <= maxPage) {
      const nextParams = {
        ...buildRequestParams(),
        page: nextPage - 1
      }
      
      setTimeout(() => {
        loadData(nextParams, { showLoading: false, useCache: true })
          .then(() => console.log(`预加载第${nextPage}页数据完成`))
          .catch(() => console.log(`预加载第${nextPage}页数据失败`))
      }, preloadDelay)
    }
  }
  
  // 监听分页变化进行预加载
  watch(() => pagination.current, () => {
    nextTick(() => {
      preloadNextPage()
    })
  })
  
  // 清理函数
  function cleanup() {
    if (abortController.value) {
      abortController.value.abort()
    }
    requestQueue.clear()
  }
  
  // 组件卸载时清理
  onUnmounted(() => {
    cleanup()
  })
  
  // 自动加载
  if (autoLoad) {
    onMounted(() => {
      loadData()
    })
  }
  
  return {
    // 状态
    loading,
    refreshing,
    initialLoading, // 新增：返回初始加载状态
    tableData,
    total,
    error,
    pagination,
    searchForm,
    
    // 方法
    loadData,
    handleSearch,
    resetSearch,
    refresh,
    handleSizeChange,
    handleCurrentChange,
    clearCache,
    cleanup,
    
    // 缓存状态
    getCacheStatus: () => ({
      size: cache.data.size,
      maxSize: cache.maxSize,
      keys: Array.from(cache.data.keys())
    })
  }
} 