<template>
  <div v-if="loading" class="table-skeleton">
    <!-- 搜索区域骨架 -->
    <div
      v-if="showSearch"
      class="search-skeleton"
    >
      <div class="search-row">
        <el-skeleton
          :rows="1"
          animated
        >
          <template #template>
            <div class="search-items">
              <div
                v-for="i in searchItems"
                :key="i"
                class="search-item"
              >
                <el-skeleton-item
                  variant="text"
                  style="width: 60px; height: 14px;"
                />
                <el-skeleton-item
                  variant="rect"
                  style="width: 160px; height: 32px; margin-left: 8px;"
                />
              </div>
              <div class="search-buttons">
                <el-skeleton-item
                  variant="button"
                  style="width: 80px; height: 32px; margin-left: 8px;"
                />
                <el-skeleton-item
                  variant="button"
                  style="width: 80px; height: 32px; margin-left: 8px;"
                />
              </div>
            </div>
          </template>
        </el-skeleton>
      </div>
    </div>
    
    <!-- 表格头部骨架 -->
    <div
      v-if="showHeader"
      class="table-header-skeleton"
    >
      <div class="header-content">
        <div class="header-left">
          <el-skeleton-item
            variant="circle"
            style="width: 16px; height: 16px;"
          />
          <el-skeleton-item
            variant="text"
            style="width: 80px; height: 16px; margin-left: 8px;"
          />
        </div>
        <div class="header-right">
          <el-skeleton-item
            variant="button"
            style="width: 100px; height: 32px;"
          />
        </div>
      </div>
    </div>
    
    <!-- 表格骨架 -->
    <div class="table-content-skeleton">
      <el-skeleton
        :rows="rows"
        animated
      >
        <template #template>
          <!-- 表格头 -->
          <div class="table-header">
            <div
              v-for="(col, index) in columns"
              :key="index"
              class="table-header-cell"
              :style="{ width: col.width || 'auto' }"
            >
              <el-skeleton-item
                variant="text"
                :style="{ width: col.titleWidth || '60px', height: '14px' }"
              />
            </div>
          </div>
          
          <!-- 表格行 -->
          <div
            v-for="i in rows"
            :key="i"
            class="table-row"
          >
            <div
              v-for="(col, index) in columns"
              :key="index"
              class="table-cell"
              :style="{ width: col.width || 'auto' }"
            >
              <el-skeleton-item 
                :variant="col.variant || 'text'" 
                :style="{ 
                  width: col.cellWidth || '80%', 
                  height: col.variant === 'circle' ? '32px' : '14px',
                  ...(col.variant === 'button' && { borderRadius: '4px' })
                }" 
              />
            </div>
          </div>
        </template>
      </el-skeleton>
    </div>
    
    <!-- 分页骨架 -->
    <div
      v-if="showPagination"
      class="pagination-skeleton"
    >
      <div class="pagination-content">
        <el-skeleton-item
          variant="text"
          style="width: 80px; height: 14px;"
        />
        <div class="pagination-controls">
          <el-skeleton-item
            variant="rect"
            style="width: 100px; height: 32px;"
          />
          <el-skeleton-item
            variant="button"
            style="width: 32px; height: 32px; margin-left: 8px;"
          />
          <el-skeleton-item
            variant="text"
            style="width: 60px; height: 14px; margin-left: 8px;"
          />
          <el-skeleton-item
            variant="button"
            style="width: 32px; height: 32px; margin-left: 8px;"
          />
          <el-skeleton-item
            variant="rect"
            style="width: 80px; height: 32px; margin-left: 8px;"
          />
        </div>
      </div>
    </div>
  </div>
  
  <!-- 实际内容 -->
  <div v-else>
    <slot></slot>
  </div>
</template>

<script setup>
const props = defineProps({
  // 加载状态
  loading: {
    type: Boolean,
    default: true
  },
  // 表格行数
  rows: {
    type: Number,
    default: 8
  },
  // 列配置
  columns: {
    type: Array,
    default: () => [
      { width: '80px', titleWidth: '30px', cellWidth: '50px' },
      { width: '120px', titleWidth: '50px', cellWidth: '80px' },
      { width: '120px', titleWidth: '60px', cellWidth: '90px' },
      { width: '130px', titleWidth: '40px', cellWidth: '100px' },
      { width: '180px', titleWidth: '30px', cellWidth: '140px' },
      { width: '150px', titleWidth: '60px', cellWidth: '100px' },
      { width: '100px', titleWidth: '30px', cellWidth: '60px', variant: 'button' },
      { width: '150px', titleWidth: '30px', cellWidth: '120px' }
    ]
  },
  // 是否显示搜索区域
  showSearch: {
    type: Boolean,
    default: true
  },
  // 搜索项数量
  searchItems: {
    type: Number,
    default: 3
  },
  // 是否显示表格头部
  showHeader: {
    type: Boolean,
    default: true
  },
  // 是否显示分页
  showPagination: {
    type: Boolean,
    default: true
  }
})
</script>

<style scoped>
.table-skeleton {
  padding: 20px;
}

.search-skeleton {
  margin-bottom: 20px;
  padding: 16px;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}

.search-items {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 16px;
}

.search-item {
  display: flex;
  align-items: center;
}

.search-buttons {
  display: flex;
  align-items: center;
  margin-left: auto;
}

.table-header-skeleton {
  margin-bottom: 16px;
  padding: 16px 20px;
  background: #fff;
  border-radius: 4px 4px 0 0;
  border: 1px solid #ebeef5;
  border-bottom: none;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
}

.table-content-skeleton {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 0 0 4px 4px;
}

.table-header {
  display: flex;
  background: #f5f7fa;
  border-bottom: 1px solid #ebeef5;
  padding: 12px 0;
}

.table-header-cell {
  padding: 0 12px;
  display: flex;
  align-items: center;
  border-right: 1px solid #ebeef5;
}

.table-header-cell:last-child {
  border-right: none;
}

.table-row {
  display: flex;
  border-bottom: 1px solid #ebeef5;
  padding: 12px 0;
}

.table-row:last-child {
  border-bottom: none;
}

.table-cell {
  padding: 0 12px;
  display: flex;
  align-items: center;
  border-right: 1px solid #ebeef5;
}

.table-cell:last-child {
  border-right: none;
}

.pagination-skeleton {
  margin-top: 16px;
  padding: 16px 20px;
  background: #fff;
  border-radius: 4px;
  border: 1px solid #ebeef5;
}

.pagination-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.pagination-controls {
  display: flex;
  align-items: center;
}

/* 动画效果 */
.table-skeleton .el-skeleton__item {
  background: linear-gradient(90deg, #f2f2f2 25%, #e6e6e6 37%, #f2f2f2 63%);
  background-size: 400% 100%;
  animation: skeleton-loading 1.4s ease infinite;
}

@keyframes skeleton-loading {
  0% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}
</style> 