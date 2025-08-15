<template>
  <el-breadcrumb separator="/">
    <el-breadcrumb-item
      v-for="(item, index) in breadcrumbs"
      :key="index"
      :to="item.path"
    >
      {{ item.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const breadcrumbs = ref([])

// 获取面包屑数据
const getBreadcrumbs = () => {
  const matched = route.matched.filter(item => item.meta && item.meta.title)
  
  breadcrumbs.value = [
    { path: '/', title: '首页' },
    ...matched.map(item => ({
      path: item.path,
      title: item.meta.title
    }))
  ]
  
  // 避免重复显示首页
  if (breadcrumbs.value.length > 0 && breadcrumbs.value[0].path === route.path) {
    breadcrumbs.value.splice(1)
  }
}

// 监听路由变化
watch(
  () => route.path,
  () => getBreadcrumbs(),
  { immediate: true }
)
</script>

<style scoped>
.el-breadcrumb {
  line-height: 24px;
}
</style> 