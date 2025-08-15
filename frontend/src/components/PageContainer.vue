<template>
  <div class="page-container">
    <div
      v-if="$slots.header"
      class="page-header"
    >
      <slot name="header" />
    </div>
    <div class="page-content">
      <slot />
    </div>
  </div>
</template>

<script setup>
// 页面容器组件，用于统一管理页面布局和滚动
</script>

<style scoped>
.page-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.page-header {
  flex-shrink: 0;
  padding: 20px 20px 0 20px;
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
}

.page-content {
  flex: 1;
  overflow: auto;
  padding: 20px;
  background: #f5f5f5;
}

/* 自定义滚动条样式 */
.page-content::-webkit-scrollbar {
  width: 6px;
}

.page-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.page-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.page-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style> 