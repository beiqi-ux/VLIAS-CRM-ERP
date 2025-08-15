<template>
  <div class="table-container">
    <!-- 操作栏 -->
    <div
      v-if="$slots.actions"
      class="table-actions"
    >
      <slot name="actions" />
    </div>
    
    <!-- 表格内容 -->
    <div class="table-content">
      <slot />
    </div>
    
    <!-- 分页 -->
    <div
      v-if="$slots.pagination"
      class="table-pagination"
    >
      <slot name="pagination" />
    </div>
  </div>
</template>

<script setup>
// 表格容器组件，专门用于表格页面的布局管理
</script>

<style scoped>
.table-container {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.table-actions {
  flex-shrink: 0;
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
  background: #fafafa;
}

.table-content {
  flex: 1;
  overflow: auto;
  position: relative;
}

.table-pagination {
  flex-shrink: 0;
  padding: 16px 20px;
  border-top: 1px solid #e4e7ed;
  background: #fafafa;
  display: flex;
  justify-content: flex-end;
}

/* 确保内部表格能够正确滚动 */
:deep(.el-table) {
  height: 100%;
}

:deep(.el-table .el-table__body-wrapper) {
  max-height: calc(100% - 55px); /* 减去表头高度 */
  overflow-y: auto;
}

/* 优化表格滚动条 */
:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

:deep(.el-table .el-table__body-wrapper)::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style> 