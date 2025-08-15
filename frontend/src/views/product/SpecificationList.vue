<template>
  <div class="specification-management">
    <!-- 标签页切换 -->
    <el-tabs
      v-model="activeTab"
      type="border-card"
    >
      <!-- 规格分类管理 -->
      <el-tab-pane
        label="规格分类管理"
        name="categories"
      >
        <div class="specification-categories">
          <!-- 搜索栏 -->
          <el-card
            class="search-card"
            shadow="never"
          >
            <el-form
              :model="categorySearchForm"
              inline
            >
              <el-form-item label="分类名称">
                <el-input 
                  v-model="categorySearchForm.categoryName" 
                  placeholder="请输入分类名称" 
                  clearable
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item label="状态">
                <el-select
                  v-model="categorySearchForm.status"
                  placeholder="请选择状态"
                  clearable
                  style="width: 120px"
                >
                  <el-option
                    label="启用"
                    :value="1"
                  />
                  <el-option
                    label="禁用"
                    :value="0"
                  />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  :loading="categoryLoading"
                  @click="handleCategorySearch"
                >
                  <el-icon><Search /></el-icon>
                  搜索
                </el-button>
                <el-button @click="resetCategorySearch">
                  <el-icon><Refresh /></el-icon>
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 操作栏 -->
          <el-card
            class="operation-card"
            shadow="never"
          >
            <div class="operation-row">
              <div class="left-operations">
                <el-button 
                  v-action-permission="'product-specification-management:create'" 
                  type="primary"
                  @click="handleAddCategory"
                >
                  <el-icon><Plus /></el-icon>
                  新增分类
                </el-button>
                <el-button 
                  v-action-permission="'product-specification-management:delete'" 
                  type="danger"
                  :disabled="selectedCategories.length === 0"
                  @click="handleBatchDeleteCategories"
                >
                  <el-icon><Delete /></el-icon>
                  批量删除
                </el-button>
              </div>
              <div class="right-operations">
                <el-button
                  type="success"
                  @click="handleRefreshCategories"
                >
                  <el-icon><Refresh /></el-icon>
                  刷新
                </el-button>
              </div>
            </div>
          </el-card>

          <!-- 数据表格 -->
          <el-card
            class="table-card"
            shadow="never"
          >
            <el-table
              v-loading="categoryLoading"
              :data="categoryList"
              stripe
              style="width: 100%"
              @selection-change="handleCategorySelectionChange"
            >
              <el-table-column
                type="selection"
                width="55"
              />
              <el-table-column
                prop="id"
                label="ID"
                width="80"
              />
              <el-table-column
                prop="categoryCode"
                label="分类代码"
                min-width="120"
              />
              <el-table-column
                prop="categoryName"
                label="分类名称"
                min-width="150"
              />
              <el-table-column
                prop="description"
                label="描述"
                min-width="200"
                show-overflow-tooltip
              />
              <el-table-column
                prop="sortOrder"
                label="排序"
                width="80"
              />
              <el-table-column
                prop="status"
                label="状态"
                width="100"
              >
                <template #default="{ row }">
                  <el-tag
                    :type="row.status === 1 ? 'success' : 'danger'"
                    size="small"
                  >
                    {{ row.status === 1 ? '启用' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="createTime"
                label="创建时间"
                width="180"
              />
              <el-table-column
                label="操作"
                width="250"
                fixed="right"
              >
                <template #default="{ row }">
                  <el-button
                    v-action-permission="'product-specification-management:edit'"
                    type="primary"
                    size="small"
                    @click="handleEditCategory(row)"
                  >
                    <el-icon><Edit /></el-icon>
                    编辑
                  </el-button>
                  <el-button
                    type="info"
                    size="small"
                    @click="handleManageValues(row)"
                  >
                    <el-icon><Setting /></el-icon>
                    管理规格值
                  </el-button>
                  <el-button
                    v-action-permission="'product-specification-management:delete'"
                    type="danger"
                    size="small"
                    @click="handleDeleteCategory(row)"
                  >
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination-container">
              <el-pagination
                v-model:current-page="categoryPagination.currentPage"
                v-model:page-size="categoryPagination.pageSize"
                :total="categoryPagination.total"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleCategorySizeChange"
                @current-change="handleCategoryCurrentChange"
              />
            </div>
          </el-card>
        </div>
      </el-tab-pane>

      <!-- 规格项管理 -->
      <el-tab-pane
        label="规格项管理"
        name="items"
      >
        <div class="specification-items">
          <!-- 搜索栏 -->
          <el-card
            class="search-card"
            shadow="never"
          >
            <el-form
              :model="itemSearchForm"
              inline
            >
              <el-form-item label="所属分类">
                <el-select
                  v-model="itemSearchForm.categoryId"
                  placeholder="选择分类"
                  clearable
                  style="width: 200px"
                >
                  <el-option
                    v-for="category in categoryList"
                    :key="category.id"
                    :label="category.categoryName"
                    :value="category.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="规格项名称">
                <el-input
                  v-model="itemSearchForm.itemName"
                  placeholder="请输入规格项名称"
                  style="width: 200px"
                  clearable
                />
              </el-form-item>
              <el-form-item label="状态">
                <el-select
                  v-model="itemSearchForm.status"
                  placeholder="选择状态"
                  clearable
                  style="width: 120px"
                >
                  <el-option
                    label="启用"
                    :value="1"
                  />
                  <el-option
                    label="禁用"
                    :value="0"
                  />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  :loading="itemLoading"
                  @click="handleItemSearch"
                >
                  <el-icon><Search /></el-icon>
                  搜索
                </el-button>
                <el-button @click="resetItemSearch">
                  <el-icon><Refresh /></el-icon>
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 操作栏 -->
          <el-card
            class="operation-card"
            shadow="never"
          >
            <div class="operation-row">
              <div class="left-operations">
                <el-button
                  v-action-permission="'product-specification-management:create'"
                  type="primary"
                  @click="handleAddItem"
                >
                  <el-icon><Plus /></el-icon>
                  新增规格项
                </el-button>
                <el-button 
                  v-action-permission="'product-specification-management:delete'" 
                  type="danger"
                  :disabled="selectedItems.length === 0"
                  @click="handleBatchDeleteItems"
                >
                  <el-icon><Delete /></el-icon>
                  批量删除
                </el-button>
              </div>
            </div>
          </el-card>

          <!-- 表格 -->
          <el-card
            class="table-card"
            shadow="never"
          >
            <el-table
              ref="itemTableRef"
              v-loading="itemLoading"
              :data="itemList"
              stripe
              border
              height="500"
              @selection-change="handleItemSelectionChange"
            >
              <el-table-column
                type="selection"
                width="55"
              />
              <el-table-column
                prop="itemCode"
                label="规格项代码"
                width="120"
              />
              <el-table-column
                prop="itemName"
                label="规格项名称"
                width="150"
              />
              <el-table-column
                prop="categoryId"
                label="所属分类"
                width="120"
              >
                <template #default="{ row }">
                  {{ getCategoryName(row.categoryId) }}
                </template>
              </el-table-column>
              <el-table-column
                prop="unit"
                label="单位"
                width="80"
              />
              <el-table-column
                prop="isRequired"
                label="必填"
                width="80"
              >
                <template #default="{ row }">
                  <el-tag :type="row.isRequired === 1 ? 'danger' : 'info'">
                    {{ row.isRequired === 1 ? '是' : '否' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="isSku"
                label="影响SKU"
                width="100"
              >
                <template #default="{ row }">
                  <el-tag :type="row.isSku === 1 ? 'warning' : 'info'">
                    {{ row.isSku === 1 ? '是' : '否' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="status"
                label="状态"
                width="80"
              >
                <template #default="{ row }">
                  <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                    {{ row.status === 1 ? '启用' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="sortOrder"
                label="排序"
                width="80"
              />
              <el-table-column
                prop="description"
                label="描述"
                min-width="150"
                show-overflow-tooltip
              />
              <el-table-column
                prop="createTime"
                label="创建时间"
                width="160"
              >
                <template #default="{ row }">
                  {{ formatDateTime(row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                width="180"
                fixed="right"
              >
                <template #default="{ row }">
                  <el-button 
                    v-action-permission="'product-specification-management:update'" 
                    type="primary"
                    size="small"
                    @click="handleEditItem(row)"
                  >
                    <el-icon><Edit /></el-icon>
                    编辑
                  </el-button>
                  <el-button
                    v-action-permission="'product-specification-management:delete'"
                    type="danger"
                    size="small"
                    @click="handleDeleteItem(row)"
                  >
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <!-- 分页 -->
            <div class="pagination-container">
              <el-pagination
                v-model:current-page="itemPagination.currentPage"
                v-model:page-size="itemPagination.pageSize"
                :total="itemPagination.total"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleItemSizeChange"
                @current-change="handleItemCurrentChange"
              />
            </div>
          </el-card>
        </div>
      </el-tab-pane>

      <!-- 规格值管理 -->
      <el-tab-pane
        label="规格值管理"
        name="values"
      >
        <div class="specification-values">
          <!-- 搜索栏 -->
          <el-card
            class="search-card"
            shadow="never"
          >
            <el-form
              :model="valueSearchForm"
              inline
            >
              <el-form-item label="规格项">
                <el-select
                  v-model="valueSearchForm.itemId"
                  placeholder="请选择规格项"
                  clearable
                  style="width: 200px"
                >
                  <el-option
                    v-for="item in itemList"
                    :key="item.id"
                    :label="item.itemName"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item label="规格值名称">
                <el-input
                  v-model="valueSearchForm.valueName" 
                  placeholder="请输入规格值名称" 
                  clearable
                  style="width: 200px"
                />
              </el-form-item>
              <el-form-item label="状态">
                <el-select
                  v-model="valueSearchForm.status"
                  placeholder="请选择状态"
                  clearable
                  style="width: 120px"
                >
                  <el-option
                    label="启用"
                    :value="1"
                  />
                  <el-option
                    label="禁用"
                    :value="0"
                  />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  :loading="valueLoading"
                  @click="handleValueSearch"
                >
                  <el-icon><Search /></el-icon>
                  搜索
                </el-button>
                <el-button @click="resetValueSearch">
                  <el-icon><Refresh /></el-icon>
                  重置
                </el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 操作栏 -->
          <el-card
            class="operation-card"
            shadow="never"
          >
            <div class="operation-row">
              <div class="left-operations">
                <el-button
                  type="primary"
                  @click="handleAddValue"
                >
                  <el-icon><Plus /></el-icon>
                  新增规格值
                </el-button>
                <el-button
                  type="danger"
                  :disabled="selectedValues.length === 0"
                  @click="handleBatchDeleteValues"
                >
                  <el-icon><Delete /></el-icon>
                  批量删除
                </el-button>
              </div>
              <div class="right-operations">
                <el-button @click="handleRefreshValues">
                  <el-icon><Refresh /></el-icon>
                  刷新
                </el-button>
              </div>
            </div>
          </el-card>

          <!-- 表格 -->
          <el-card
            class="table-card"
            shadow="never"
          >
            <el-table
              v-loading="valueLoading"
              :data="valueList"
              stripe
              border
              height="500"
              @selection-change="handleValueSelectionChange"
            >
              <el-table-column
                type="selection"
                width="55"
              />
              <el-table-column
                prop="valueCode"
                label="规格值代码"
                width="120"
              />
              <el-table-column
                prop="valueName"
                label="规格值名称"
                width="150"
              />
              <el-table-column
                prop="categoryId"
                label="所属分类"
                width="120"
              >
                <template #default="{ row }">
                  {{ getCategoryNameByItemId(row.itemId) }}
                </template>
              </el-table-column>
              <el-table-column
                prop="itemId"
                label="所属规格项"
                width="150"
              >
                <template #default="{ row }">
                  {{ getItemName(row.itemId) }}
                </template>
              </el-table-column>
              <el-table-column
                prop="textValue"
                label="文本值"
                width="120"
              />
              <el-table-column
                prop="numericValue"
                label="数值"
                width="100"
              />
              <el-table-column
                prop="status"
                label="状态"
                width="80"
              >
                <template #default="{ row }">
                  <el-tag :type="row.status === 1 ? 'success' : 'danger'">
                    {{ row.status === 1 ? '启用' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column
                prop="sortOrder"
                label="排序"
                width="80"
              />
              <el-table-column
                prop="valueDesc"
                label="描述"
                min-width="150"
                show-overflow-tooltip
              />
              <el-table-column
                prop="createTime"
                label="创建时间"
                width="160"
              >
                <template #default="{ row }">
                  {{ formatDateTime(row.createTime) }}
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                width="180"
                fixed="right"
              >
                <template #default="{ row }">
                  <el-button
                    type="primary"
                    size="small"
                    @click="handleEditValue(row)"
                  >
                    <el-icon><Edit /></el-icon>
                    编辑
                  </el-button>
                  <el-button
                    type="danger"
                    size="small"
                    @click="handleDeleteValue(row)"
                  >
                    <el-icon><Delete /></el-icon>
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination-container">
              <el-pagination
                v-model:current-page="valuePagination.currentPage"
                v-model:page-size="valuePagination.pageSize"
                :total="valuePagination.total"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleValueSizeChange"
                @current-change="handleValueCurrentChange"
              />
            </div>
          </el-card>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 规格分类新增/编辑对话框 -->
    <el-dialog
      v-model="categoryDialogVisible"
      :title="isCategoryEdit ? '编辑规格分类' : '新增规格分类'"
      width="600px"
      @close="handleCategoryDialogClose"
    >
      <el-form
        ref="categoryFormRef"
        :model="categoryForm"
        :rules="categoryFormRules"
        label-width="100px"
      >
        <el-form-item
          label="分类代码"
          prop="categoryCode"
        >
          <el-input
            v-model="categoryForm.categoryCode"
            placeholder="请输入分类代码，如：color、size等"
          />
        </el-form-item>
        <el-form-item
          label="分类名称"
          prop="categoryName"
        >
          <el-input
            v-model="categoryForm.categoryName"
            placeholder="请输入分类名称，如：颜色、尺寸等"
          />
        </el-form-item>
        <el-form-item
          label="描述"
          prop="description"
        >
          <el-input
            v-model="categoryForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
          />
        </el-form-item>
        <el-form-item
          label="排序"
          prop="sortOrder"
        >
          <el-input-number
            v-model="categoryForm.sortOrder"
            :min="0"
            :max="999"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="状态"
          prop="status"
        >
          <el-radio-group v-model="categoryForm.status">
            <el-radio :label="1">
              启用
            </el-radio>
            <el-radio :label="0">
              禁用
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="categoryDialogVisible = false">
            取消
          </el-button>
          <el-button
            type="primary"
            :loading="categorySubmitLoading"
            @click="handleCategorySubmit"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 规格项新增/编辑对话框 -->
    <el-dialog
      v-model="itemDialogVisible"
      :title="isItemEdit ? '编辑规格项' : '新增规格项'"
      width="600px"
      @close="handleItemDialogClose"
    >
      <el-form
        ref="itemFormRef"
        :model="itemForm"
        :rules="itemFormRules"
        label-width="100px"
      >
        <el-form-item
          label="规格分类"
          prop="categoryId"
        >
          <el-select
            v-model="itemForm.categoryId"
            placeholder="请选择规格分类"
            style="width: 100%"
          >
            <el-option
              v-for="category in activeCategories"
              :key="category.id"
              :label="category.categoryName"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="规格项代码"
          prop="itemCode"
        >
          <el-input
            v-model="itemForm.itemCode"
            placeholder="请输入规格项代码"
          />
        </el-form-item>
        <el-form-item
          label="规格项名称"
          prop="itemName"
        >
          <el-input
            v-model="itemForm.itemName"
            placeholder="请输入规格项名称"
          />
        </el-form-item>
        <el-form-item label="单位">
          <el-input
            v-model="itemForm.unit"
            placeholder="请输入单位（如：mm、个、种）"
          />
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="itemForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
          />
        </el-form-item>
        <el-form-item label="是否必填">
          <el-radio-group v-model="itemForm.isRequired">
            <el-radio :label="1">
              是
            </el-radio>
            <el-radio :label="0">
              否
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="影响SKU">
          <el-radio-group v-model="itemForm.isSku">
            <el-radio :label="1">
              是
            </el-radio>
            <el-radio :label="0">
              否
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="排序"
          prop="sortOrder"
        >
          <el-input-number
            v-model="itemForm.sortOrder"
            :min="0"
            :max="999"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="状态"
          prop="status"
        >
          <el-radio-group v-model="itemForm.status">
            <el-radio :label="1">
              启用
            </el-radio>
            <el-radio :label="0">
              禁用
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="itemDialogVisible = false">
            取消
          </el-button>
          <el-button
            type="primary"
            :loading="itemSubmitLoading"
            @click="handleItemSubmit"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 规格值新增/编辑对话框 -->
    <el-dialog
      v-model="valueDialogVisible"
      :title="isValueEdit ? '编辑规格值' : '新增规格值'"
      width="600px"
      @close="handleValueDialogClose"
    >
      <el-form
        ref="valueFormRef"
        :model="valueForm"
        :rules="valueFormRules"
        label-width="100px"
      >
        <el-form-item
          label="规格项"
          prop="itemId"
        >
          <el-select
            v-model="valueForm.itemId"
            placeholder="请选择规格项"
            style="width: 100%"
          >
            <el-option
              v-for="item in itemList"
              :key="item.id"
              :label="item.itemName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          label="规格值代码"
          prop="valueCode"
        >
          <el-input
            v-model="valueForm.valueCode"
            placeholder="请输入规格值代码"
          />
        </el-form-item>
        <el-form-item
          label="规格值名称"
          prop="valueName"
        >
          <el-input
            v-model="valueForm.valueName"
            placeholder="请输入规格值名称"
          />
        </el-form-item>
        <el-form-item label="文本值">
          <el-input
            v-model="valueForm.textValue"
            placeholder="请输入文本值（如：黑色、圆形等）"
          />
        </el-form-item>
        <el-form-item label="数值">
          <el-input-number
            v-model="valueForm.numericValue"
            placeholder="请输入数值（如：140、2.5等）"
            :precision="2"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="描述"
          prop="valueDesc"
        >
          <el-input
            v-model="valueForm.valueDesc"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
          />
        </el-form-item>
        <el-form-item
          label="排序"
          prop="sortOrder"
        >
          <el-input-number
            v-model="valueForm.sortOrder"
            :min="0"
            :max="999"
            controls-position="right"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          label="状态"
          prop="status"
        >
          <el-radio-group v-model="valueForm.status">
            <el-radio :label="1">
              启用
            </el-radio>
            <el-radio :label="0">
              禁用
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="valueDialogVisible = false">
            取消
          </el-button>
          <el-button
            type="primary"
            :loading="valueSubmitLoading"
            @click="handleValueSubmit"
          >
            确定
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, Setting } from '@element-plus/icons-vue'
import { 
  getSpecificationCategories, 
  getActiveSpecificationCategories,
  createSpecificationCategory,
  updateSpecificationCategory,
  deleteSpecificationCategory,
  batchDeleteSpecificationCategories,
  getSpecificationItems,
  createSpecificationItem,
  updateSpecificationItem,
  deleteSpecificationItem,
  batchDeleteSpecificationItems,
  getSpecificationValues,
  createSpecificationValue,
  updateSpecificationValue,
  deleteSpecificationValue,
  batchDeleteSpecificationValues
} from '@/api/specification'



// 响应式数据
const activeTab = ref('categories')
const categoryLoading = ref(false)
const itemLoading = ref(false)
const valueLoading = ref(false)
const categorySubmitLoading = ref(false)
const itemSubmitLoading = ref(false)
const valueSubmitLoading = ref(false)
const categoryDialogVisible = ref(false)
const itemDialogVisible = ref(false)
const valueDialogVisible = ref(false)
const isCategoryEdit = ref(false)
const isItemEdit = ref(false)
const isValueEdit = ref(false)
const selectedCategories = ref([])
const selectedItems = ref([])
const selectedValues = ref([])
const categoryList = ref([])
const itemList = ref([])
const valueList = ref([])
const allCategories = ref([])
const categoryFormRef = ref()
const itemFormRef = ref()
const valueFormRef = ref()

// 搜索表单
const categorySearchForm = reactive({
  categoryName: '',
  status: null
})

const itemSearchForm = reactive({
  categoryId: null,
  itemName: '',
  status: null
})

const valueSearchForm = reactive({
  itemId: null,
  valueName: '',
  status: null
})

// 分页数据
const categoryPagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const itemPagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

const valuePagination = reactive({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 表单数据
const categoryForm = reactive({
  id: null,
  categoryCode: '',
  categoryName: '',
  description: '',
  sortOrder: 0,
  status: 1
})

const itemForm = reactive({
  id: null,
  categoryId: null,
  itemCode: '',
  itemName: '',
  unit: '',
  description: '',
  isRequired: 0,
  isSku: 0,
  sortOrder: 0,
  status: 1
})

const valueForm = reactive({
  id: null,
  itemId: null,
  valueCode: '',
  valueName: '',
  textValue: '',
  numericValue: null,
  valueDesc: '',
  sortOrder: 0,
  status: 1
})

// 表单验证规则
const categoryFormRules = {
  categoryCode: [
    { required: true, message: '请输入分类代码', trigger: 'blur' }
  ],
  categoryName: [
    { required: true, message: '请输入分类名称', trigger: 'blur' }
  ]
}

const itemFormRules = {
  categoryId: [
    { required: true, message: '请选择规格分类', trigger: 'change' }
  ],
  itemCode: [
    { required: true, message: '请输入规格项代码', trigger: 'blur' }
  ],
  itemName: [
    { required: true, message: '请输入规格项名称', trigger: 'blur' }
  ]
}

const valueFormRules = {
  itemId: [
    { required: true, message: '请选择规格项', trigger: 'change' }
  ],
  valueCode: [
    { required: true, message: '请输入规格值代码', trigger: 'blur' }
  ],
  valueName: [
    { required: true, message: '请输入规格值名称', trigger: 'blur' }
  ]
}

// 计算属性
const activeCategories = computed(() => {
  return allCategories.value.filter(category => category.status === 1)
})

// 获取分类名称
function getCategoryName(categoryId) {
  const category = allCategories.value.find(c => c.id === categoryId)
  return category ? category.categoryName : '未知分类'
}

// 获取规格项名称
function getItemName(itemId) {
  const item = itemList.value.find(i => i.id === itemId)
  return item ? item.itemName : '未知规格项'
}

// 通过规格项ID获取分类名称
function getCategoryNameByItemId(itemId) {
  const item = itemList.value.find(i => i.id === itemId)
  if (!item) return '未知分类'
  const category = allCategories.value.find(c => c.id === item.categoryId)
  return category ? category.categoryName : '未知分类'
}

// 格式化时间
function formatDateTime(dateTime) {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 获取规格分类列表
async function fetchCategoryList() {
  try {
    categoryLoading.value = true
    const params = {
      pageNum: categoryPagination.currentPage,
      pageSize: categoryPagination.pageSize,
      ...categorySearchForm
    }
    const data = await getSpecificationCategories(params)
    
    if (data.success) {
      categoryList.value = data.data.content || []
      categoryPagination.total = data.data.totalElements || 0
    } else {
      ElMessage.error(data.message || '获取规格分类列表失败')
    }
  } catch (error) {
    console.error('获取规格分类列表失败:', error)
    ElMessage.error('获取规格分类列表失败')
  } finally {
    categoryLoading.value = false
  }
}

// 获取所有启用的规格分类
async function fetchAllActiveCategories() {
  try {
    const data = await getActiveSpecificationCategories()
    
    if (data.success) {
      allCategories.value = data.data || []
    }
  } catch (error) {
    console.error('获取规格分类失败:', error)
  }
}

// 获取规格值列表
async function fetchValueList() {
  try {
    valueLoading.value = true
    const params = {
      pageNum: valuePagination.currentPage,
      pageSize: valuePagination.pageSize,
      ...valueSearchForm
    }
    const data = await getSpecificationValues(params)
    
    if (data.success) {
      valueList.value = data.data.content || []
      valuePagination.total = data.data.totalElements || 0
    } else {
      ElMessage.error(data.message || '获取规格值列表失败')
    }
  } catch (error) {
    console.error('获取规格值列表失败:', error)
    ElMessage.error('获取规格值列表失败')
  } finally {
    valueLoading.value = false
  }
}

// 规格分类相关方法
function handleCategorySearch() {
  categoryPagination.currentPage = 1
  fetchCategoryList()
}

function resetCategorySearch() {
  Object.assign(categorySearchForm, {
    categoryName: '',
    status: null
  })
  categoryPagination.currentPage = 1
  fetchCategoryList()
}

function handleRefreshCategories() {
  fetchCategoryList()
}

function handleAddCategory() {
  isCategoryEdit.value = false
  resetCategoryForm()
  categoryDialogVisible.value = true
}

function handleEditCategory(row) {
  isCategoryEdit.value = true
  Object.assign(categoryForm, row)
  categoryDialogVisible.value = true
}

function handleDeleteCategory(row) {
  ElMessageBox.confirm(
    `确定要删除规格分类 "${row.categoryName}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const data = await deleteSpecificationCategory(row.id)
      
      if (data.success) {
        ElMessage.success('删除成功')
        fetchCategoryList()
        fetchAllActiveCategories()
      } else {
        ElMessage.error(data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

function handleBatchDeleteCategories() {
  if (selectedCategories.value.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedCategories.value.length} 条数据吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedCategories.value.map(row => row.id)
      const data = await batchDeleteSpecificationCategories(ids)
      
      if (data.success) {
        ElMessage.success('批量删除成功')
        fetchCategoryList()
        fetchAllActiveCategories()
      } else {
        ElMessage.error(data.message || '批量删除失败')
      }
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}

function handleManageValues(row) {
  // 切换到规格值管理标签页，并筛选该分类的规格值
  activeTab.value = 'values'
  valueSearchForm.categoryId = row.id
  valuePagination.currentPage = 1
  fetchValueList()
}

function handleCategorySelectionChange(selection) {
  selectedCategories.value = selection
}

function handleCategorySizeChange(size) {
  categoryPagination.pageSize = size
  categoryPagination.currentPage = 1
  fetchCategoryList()
}

function handleCategoryCurrentChange(page) {
  categoryPagination.currentPage = page
  fetchCategoryList()
}

async function handleCategorySubmit() {
  try {
    await categoryFormRef.value.validate()
    categorySubmitLoading.value = true
    
    const data = isCategoryEdit.value 
      ? await updateSpecificationCategory(categoryForm.id, categoryForm)
      : await createSpecificationCategory(categoryForm)
    
    if (data.success) {
      ElMessage.success(isCategoryEdit.value ? '更新成功' : '创建成功')
      categoryDialogVisible.value = false
      fetchCategoryList()
      fetchAllActiveCategories()
    } else {
      ElMessage.error(data.message || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    if (error !== false) {
      ElMessage.error('提交失败')
    }
  } finally {
    categorySubmitLoading.value = false
  }
}

function resetCategoryForm() {
  Object.assign(categoryForm, {
    id: null,
    categoryCode: '',
    categoryName: '',
    description: '',
    sortOrder: 0,
    status: 1
  })
  categoryFormRef.value?.clearValidate()
}

function handleCategoryDialogClose() {
  resetCategoryForm()
}

// 规格值相关方法
function handleValueSearch() {
  valuePagination.currentPage = 1
  fetchValueList()
}

function resetValueSearch() {
  Object.assign(valueSearchForm, {
    itemId: null,
    valueName: '',
    status: null
  })
  valuePagination.currentPage = 1
  fetchValueList()
}

function handleRefreshValues() {
  fetchValueList()
}

function handleAddValue() {
  isValueEdit.value = false
  resetValueForm()
  valueDialogVisible.value = true
}

function handleEditValue(row) {
  isValueEdit.value = true
  Object.assign(valueForm, row)
  valueDialogVisible.value = true
}

function handleDeleteValue(row) {
  ElMessageBox.confirm(
    `确定要删除规格值 "${row.valueName}" 吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const data = await deleteSpecificationValue(row.id)
      
      if (data.success) {
        ElMessage.success('删除成功')
        fetchValueList()
      } else {
        ElMessage.error(data.message || '删除失败')
      }
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

function handleBatchDeleteValues() {
  if (selectedValues.value.length === 0) {
    ElMessage.warning('请选择要删除的数据')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedValues.value.length} 条数据吗？`,
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      const ids = selectedValues.value.map(row => row.id)
      const data = await batchDeleteSpecificationValues(ids)
      
      if (data.success) {
        ElMessage.success('批量删除成功')
        fetchValueList()
      } else {
        ElMessage.error(data.message || '批量删除失败')
      }
    } catch (error) {
      console.error('批量删除失败:', error)
      ElMessage.error('批量删除失败')
    }
  })
}

function handleValueSelectionChange(selection) {
  selectedValues.value = selection
}

function handleValueSizeChange(size) {
  valuePagination.pageSize = size
  valuePagination.currentPage = 1
  fetchValueList()
}

function handleValueCurrentChange(page) {
  valuePagination.currentPage = page
  fetchValueList()
}

async function handleValueSubmit() {
  try {
    await valueFormRef.value.validate()
    valueSubmitLoading.value = true
    
    const data = isValueEdit.value 
      ? await updateSpecificationValue(valueForm.id, valueForm)
      : await createSpecificationValue(valueForm)
    
    if (data.success) {
      ElMessage.success(isValueEdit.value ? '更新成功' : '创建成功')
      valueDialogVisible.value = false
      fetchValueList()
    } else {
      ElMessage.error(data.message || '操作失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    if (error !== false) {
      ElMessage.error('提交失败')
    }
  } finally {
    valueSubmitLoading.value = false
  }
}

function resetValueForm() {
  Object.assign(valueForm, {
    id: null,
    itemId: null,
    valueCode: '',
    valueName: '',
    textValue: '',
    numericValue: null,
    valueDesc: '',
    sortOrder: 0,
    status: 1
  })
  valueFormRef.value?.clearValidate()
}

function handleValueDialogClose() {
  resetValueForm()
}

// ====== 规格项管理相关方法 ======

// 规格项搜索
function handleItemSearch() {
  itemPagination.currentPage = 1
  fetchItemList()
}

// 重置规格项搜索
function resetItemSearch() {
  Object.assign(itemSearchForm, {
    categoryId: null,
    itemName: '',
    status: null
  })
  itemPagination.currentPage = 1
  fetchItemList()
}

// 获取规格项列表
async function fetchItemList() {
  try {
    itemLoading.value = true
    const params = {
      pageNum: itemPagination.currentPage,
      pageSize: itemPagination.pageSize,
      categoryId: itemSearchForm.categoryId,
      itemName: itemSearchForm.itemName,
      status: itemSearchForm.status
    }
    
    const response = await getSpecificationItems(params)
    if (response.success) {
      itemList.value = response.data.content || []
      itemPagination.total = response.data.totalElements || 0
    } else {
      ElMessage.error(response.message || '获取规格项列表失败')
    }
  } catch (error) {
    console.error('获取规格项列表失败:', error)
    ElMessage.error('获取规格项列表失败')
  } finally {
    itemLoading.value = false
  }
}

// 规格项选择变化
function handleItemSelectionChange(selection) {
  selectedItems.value = selection
}

// 新增规格项
function handleAddItem() {
  isItemEdit.value = false
  Object.assign(itemForm, {
    id: null,
    categoryId: null,
    itemCode: '',
    itemName: '',
    unit: '',
    description: '',
    isRequired: 0,
    isSku: 0,
    sortOrder: 0,
    status: 1
  })
  itemDialogVisible.value = true
}

// 编辑规格项
function handleEditItem(row) {
  isItemEdit.value = true
  Object.assign(itemForm, { ...row })
  itemDialogVisible.value = true
}

// 删除规格项
async function handleDeleteItem(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除规格项"${row.itemName}"吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const response = await deleteSpecificationItem(row.id)
    if (response.success) {
      ElMessage.success('删除成功')
      fetchItemList()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除规格项失败:', error)
      ElMessage.error('删除规格项失败')
    }
  }
}

// 批量删除规格项
async function handleBatchDeleteItems() {
  try {
    await ElMessageBox.confirm(
      `确定要删除选中的${selectedItems.value.length}个规格项吗？`,
      '确认批量删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    const ids = selectedItems.value.map(item => item.id)
    const response = await batchDeleteSpecificationItems(ids)
    
    if (response.success) {
      ElMessage.success('批量删除成功')
      selectedItems.value = []
      fetchItemList()
    } else {
      ElMessage.error(response.message || '批量删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('批量删除规格项失败:', error)
      ElMessage.error('批量删除规格项失败')
    }
  }
}

// 规格项分页大小变化
function handleItemSizeChange(size) {
  itemPagination.pageSize = size
  itemPagination.currentPage = 1
  fetchItemList()
}

// 规格项当前页变化
function handleItemCurrentChange(page) {
  itemPagination.currentPage = page
  fetchItemList()
}

// 规格项对话框关闭处理
function handleItemDialogClose() {
  resetItemForm()
}

// 规格项提交
async function handleItemSubmit() {
  try {
    await itemFormRef.value.validate()
    itemSubmitLoading.value = true
    
    let response
    if (isItemEdit.value) {
      response = await updateSpecificationItem(itemForm.id, itemForm)
    } else {
      response = await createSpecificationItem(itemForm)
    }
    
    if (response.success) {
      ElMessage.success(isItemEdit.value ? '编辑成功' : '新增成功')
      itemDialogVisible.value = false
      fetchItemList()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    console.error('规格项提交失败:', error)
    ElMessage.error('提交失败')
  } finally {
    itemSubmitLoading.value = false
  }
}

// 重置规格项表单
function resetItemForm() {
  Object.assign(itemForm, {
    id: null,
    categoryId: null,
    itemCode: '',
    itemName: '',
    unit: '',
    description: '',
    isRequired: 0,
    isSku: 0,
    sortOrder: 0,
    status: 1
  })
  itemFormRef.value?.clearValidate()
}

// 监听标签页切换
watch(activeTab, (newTab) => {
  if (newTab === 'categories') {
    fetchCategoryList()
    fetchAllActiveCategories()
  } else if (newTab === 'items') {
    fetchItemList()
  } else if (newTab === 'values') {
    // 规格值页面需要同时加载规格项数据和规格值数据
    fetchItemList() // 确保有规格项数据用于显示分类信息
    fetchValueList()
  }
})

// 组件挂载时获取数据
onMounted(() => {
  fetchCategoryList()
  fetchAllActiveCategories()
  // 如果默认显示其他标签页，也需要加载数据
  if (activeTab.value === 'items') {
    fetchItemList()
  } else if (activeTab.value === 'values') {
    // 规格值页面需要同时加载规格项数据和规格值数据
    fetchItemList() // 确保有规格项数据用于显示分类信息
    fetchValueList()
  }
})
</script>

<style scoped>
.specification-management {
  padding: 20px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.operation-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.left-operations {
  display: flex;
  gap: 10px;
}

.right-operations {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}

.dialog-footer {
  text-align: right;
}
</style> 