<template>
  <div class="goods-edit">
    <!-- 页面头部 -->
    <div class="page-header">
      <el-page-header @back="handleBack">
        <template #content>
          <span class="page-title">{{ isEdit ? '编辑商品' : '新增商品' }}</span>
        </template>
      </el-page-header>
    </div>

    <!-- 主要内容区域 -->
    <div class="page-content">
      <el-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-width="120px"
        class="goods-form"
      >
        <!-- 基本信息 -->
        <el-card
          class="form-section"
          shadow="never"
        >
          <template #header>
            <div class="card-header">
              <span class="section-title">基本信息</span>
            </div>
          </template>
          
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item
                label="商品名称"
                prop="goodsName"
              >
                <el-input
                  v-model="formData.goodsName"
                  placeholder="请输入商品名称"
                  maxlength="100"
                  show-word-limit
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item
                label="商品编码"
                prop="goodsCode"
              >
                <el-input
                  v-model="formData.goodsCode"
                  placeholder="请输入商品编码"
                  maxlength="50"
                  :disabled="isEdit"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item
                label="商品分类"
                prop="categoryId"
              >
                <el-tree-select
                  v-model="formData.categoryId"
                  :data="categoryList"
                  :props="{ value: 'id', label: 'categoryName', children: 'children' }"
                  placeholder="请选择商品分类"
                  clearable
                  check-strictly
                  @change="handleCategoryChange"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="商品品牌"
                prop="brandId"
              >
                <el-select
                  v-model="formData.brandId"
                  placeholder="请选择商品品牌"
                  clearable
                  filterable
                >
                  <el-option
                    v-for="brand in brandList"
                    :key="brand.id"
                    :label="brand.brandName"
                    :value="brand.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="商品类型"
                prop="goodsType"
              >
                <el-select
                  v-model="formData.goodsType"
                  placeholder="请选择商品类型"
                >
                  <el-option
                    label="实物商品"
                    :value="1"
                  />
                  <el-option
                    label="虚拟商品"
                    :value="2"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item
                label="商品单位"
                prop="unit"
              >
                <el-select
                  v-model="formData.unit"
                  placeholder="请选择商品单位"
                  clearable
                  filterable
                  allow-create
                  default-first-option
                  style="width: 100%"
                >
                  <el-option
                    label="副"
                    value="副"
                  />
                  <el-option
                    label="个"
                    value="个"
                  />
                  <el-option
                    label="支"
                    value="支"
                  />
                  <el-option
                    label="件"
                    value="件"
                  />
                  <el-option
                    label="套"
                    value="套"
                  />
                  <el-option
                    label="盒"
                    value="盒"
                  />
                  <el-option
                    label="对"
                    value="对"
                  />
                  <el-option
                    label="双"
                    value="双"
                  />
                  <el-option
                    label="片"
                    value="片"
                  />
                  <el-option
                    label="包"
                    value="包"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="商品标签"
                prop="tags"
              >
                <el-input
                  v-model="formData.tags"
                  placeholder="请输入商品标签，多个标签用逗号分隔"
                  maxlength="200"
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item
                label="排序"
                prop="sort"
              >
                <el-input-number
                  v-model="formData.sort"
                  :min="0"
                  placeholder="数字越小排序越靠前"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <!-- 商品规格选择 -->
          <el-row>
            <el-col :span="24">
              <el-form-item label="商品规格">
                <div class="specification-container">
                  <!-- 空状态 -->
                  <div
                    v-if="specificationGroups.length === 0"
                    class="spec-empty-state"
                  >
                    <div class="spec-empty-content">
                      <div class="spec-empty-icon">
                        <el-icon
                          size="40"
                          color="#c8c9cc"
                        >
                          <Setting />
                        </el-icon>
                      </div>
                      <div class="spec-empty-text">
                        <div class="spec-empty-title">
                          暂未设置商品规格
                        </div>
                        <div class="spec-empty-desc">
                          为商品添加规格属性，如颜色、尺寸、材质等，支持多种规格组合
                        </div>
                      </div>
                    </div>
                    <el-button 
                      type="primary" 
                      :icon="Plus"
                      @click="openSpecificationDialog"
                    >
                      添加商品规格
                    </el-button>
                  </div>
              
                  <!-- 规格选择器 -->
                  <div
                    v-else
                    class="spec-selector"
                  >
                    <div class="spec-header">
                      <div class="spec-header-left">
                        <el-icon
                          class="spec-icon"
                          color="#409eff"
                        >
                          <Setting />
                        </el-icon>
                        <span class="spec-title">商品规格</span>
                        <el-tag
                          size="small"
                          type="info"
                          class="spec-count"
                        >
                          {{ specificationGroups.length }} 个规格类型
                        </el-tag>
                      </div>
                      <div class="spec-actions">
                        <el-button 
                          type="primary" 
                          size="small"
                          :icon="Plus"
                          @click="openSpecificationDialog"
                        >
                          添加规格
                        </el-button>
                        <el-button 
                          type="danger" 
                          size="small"
                          plain
                          :icon="Delete"
                          @click="clearAllSpecifications"
                        >
                          清空全部
                        </el-button>
                      </div>
                    </div>

                    <!-- 规格分类列表 -->
                    <div class="spec-categories">
                      <div 
                        v-for="category in groupedSpecificationsByCategory" 
                        :key="category.categoryName"
                        class="spec-category"
                      >
                        <div class="spec-category-header">
                          <div class="spec-category-title">
                            <span class="spec-category-name">{{ category.categoryName }}</span>
                            <el-tag
                              size="small"
                              type="success"
                            >
                              {{ category.totalValues }} 个选项
                            </el-tag>
                          </div>
                          <el-button
                            type="danger"
                            size="small"
                            text
                            :icon="Close"
                            @click="removeSpecificationCategory(category.categoryName)"
                          >
                            删除
                          </el-button>
                        </div>
                    
                        <div class="spec-items-horizontal">
                          <div 
                            v-for="group in category.groups"
                            :key="group.id"
                            class="spec-item-group"
                          >
                            <span class="spec-item-name">{{ group.name }}:</span>
                            <div class="spec-item-values">
                              <el-tag
                                v-for="value in group.values"
                                :key="value.id"
                                size="small"
                                closable
                                class="spec-value-tag"
                                @close="removeSpecificationValue(group.id, value.id)"
                              >
                                {{ value.name }}
                              </el-tag>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item
            label="商品简介"
            prop="brief"
          >
            <el-input
              v-model="formData.brief"
              type="textarea"
              :rows="2"
              placeholder="请输入商品简介"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>

          <el-form-item
            label="商品描述"
            prop="description"
          >
            <el-input
              v-model="formData.description"
              type="textarea"
              :rows="4"
              placeholder="请输入商品描述"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>

          <el-form-item
            label="视频链接"
            prop="videoUrl"
          >
            <el-input
              v-model="formData.videoUrl"
              placeholder="请输入商品视频链接"
              maxlength="500"
            />
          </el-form-item>

          <el-form-item
            label="备注"
            prop="remark"
          >
            <el-input
              v-model="formData.remark"
              type="textarea"
              :rows="2"
              placeholder="请输入备注信息"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-card>

        <!-- 商品图片 -->
        <el-card
          class="form-section"
          shadow="never"
        >
          <template #header>
            <div class="card-header">
              <span class="section-title">商品图片</span>
              <span class="section-tip">建议上传尺寸800x800像素的图片，支持JPG、PNG格式</span>
            </div>
          </template>

          <el-form-item label="商品图片">
            <div class="image-upload-container">
              <el-upload
                ref="uploadRef"
                v-model:file-list="imageFileList"
                :action="uploadAction"
                :headers="uploadHeaders"
                :data="uploadData"
                :before-upload="beforeImageUpload"
                :on-success="handleImageSuccess"
                :on-error="handleImageError"
                :on-remove="handleImageRemove"
                list-type="picture-card"
                :limit="10"
                multiple
                accept="image/*"
              >
                <el-icon class="avatar-uploader-icon">
                  <Plus />
                </el-icon>
                <template #tip>
                  <div class="el-upload__tip">
                    最多可上传10张图片，单张图片不超过5MB
                  </div>
                </template>
              </el-upload>
              
              <!-- 主图设置 -->
              <div
                v-if="imageList.length > 0"
                class="main-image-section"
              >
                <el-divider content-position="left">
                  主图设置
                </el-divider>
                <div class="main-image-grid">
                  <div
                    v-for="image in imageList"
                    :key="image.id || image.uid"
                    :class="['image-item', { 'is-main': image.isMain }]"
                    @click="setMainImage(image)"
                  >
                    <el-image
                      :src="getImageDisplayUrl(image)"
                      fit="cover"
                      class="preview-image"
                    />
                    <div class="image-overlay">
                      <el-icon
                        v-if="image.isMain"
                        class="main-icon"
                      >
                        <Star />
                      </el-icon>
                      <span class="image-text">{{ image.isMain ? '主图' : '设为主图' }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </el-form-item>
        </el-card>

        <!-- 价格库存 -->
        <el-card
          class="form-section"
          shadow="never"
        >
          <template #header>
            <div class="card-header">
              <span class="section-title">价格库存</span>
            </div>
          </template>

          <el-row :gutter="24">
            <el-col :span="6">
              <el-form-item
                label="售价"
                prop="sellingPrice"
              >
                <el-input-number
                  v-model="formData.sellingPrice"
                  :min="0"
                  :precision="2"
                  placeholder="请输入售价"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="成本价"
                prop="costPrice"
              >
                <el-input-number
                  v-model="formData.costPrice"
                  :min="0"
                  :precision="2"
                  placeholder="请输入成本价"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="市场价"
                prop="originalPrice"
              >
                <el-input-number
                  v-model="formData.originalPrice"
                  :min="0"
                  :precision="2"
                  placeholder="请输入市场价"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="最低价"
                prop="minPrice"
              >
                <el-input-number
                  v-model="formData.minPrice"
                  :min="0"
                  :precision="2"
                  placeholder="请输入最低价"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="6">
              <el-form-item
                label="库存数量"
                prop="stockQty"
              >
                <el-input-number
                  v-model="formData.stockQty"
                  :min="0"
                  placeholder="请输入库存数量"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="预警库存"
                prop="warnStock"
              >
                <el-input-number
                  v-model="formData.warnStock"
                  :min="0"
                  placeholder="请输入预警库存"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="商品重量(kg)"
                prop="weight"
              >
                <el-input-number
                  v-model="formData.weight"
                  :min="0"
                  :precision="3"
                  placeholder="请输入商品重量"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="商品体积(m³)"
                prop="volume"
              >
                <el-input-number
                  v-model="formData.volume"
                  :min="0"
                  :precision="3"
                  placeholder="请输入商品体积"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </el-card>

        <!-- 销售设置 -->
        <el-card
          class="form-section"
          shadow="never"
        >
          <template #header>
            <div class="card-header">
              <span class="section-title">销售设置</span>
            </div>
          </template>

          <el-row :gutter="24">
            <el-col :span="6">
              <el-form-item
                label="销售状态"
                prop="status"
              >
                <el-radio-group v-model="formData.status">
                  <el-radio :label="1">
                    上架销售
                  </el-radio>
                  <el-radio :label="0">
                    暂停销售
                  </el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="推荐商品"
                prop="isRecommended"
              >
                <el-switch
                  v-model="formData.isRecommended"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="是"
                  inactive-text="否"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="热销商品"
                prop="isHot"
              >
                <el-switch
                  v-model="formData.isHot"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="是"
                  inactive-text="否"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item
                label="新品"
                prop="isNew"
              >
                <el-switch
                  v-model="formData.isNew"
                  :active-value="1"
                  :inactive-value="0"
                  active-text="是"
                  inactive-text="否"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item
            label="商品关键词"
            prop="keywords"
          >
            <el-input
              v-model="formData.keywords"
              placeholder="请输入商品关键词，多个关键词用逗号分隔"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-card>

        <!-- 操作按钮 -->
        <div class="form-actions">
          <el-button
            size="large"
            @click="handleBack"
          >
            取消
          </el-button>
          <el-button 
            type="primary" 
            size="large" 
            :loading="submitLoading"
            @click="handleSubmit"
          >
            {{ isEdit ? '更新商品' : '创建商品' }}
          </el-button>
        </div>
      </el-form>
    </div>
  </div>

  <!-- 规格选择对话框 -->
  <el-dialog
    v-model="specificationDialogVisible"
    title="选择商品规格"
    width="900px"
    :before-close="handleSpecificationDialogClose"
    class="spec-dialog"
  >
    <template #header>
      <div class="dialog-header">
        <el-icon
          class="dialog-icon"
          color="#409eff"
        >
          <Setting />
        </el-icon>
        <span>选择商品规格</span>
      </div>
    </template>

    <div class="specification-dialog">
      <!-- 规格选择表单 -->
      <div class="spec-form-container">
        <el-form
          :model="specFormData"
          label-width="100px"
          class="spec-form"
        >
          <!-- 规格分类选择 -->
          <el-form-item
            label="规格分类"
            required
          >
            <el-select
              v-model="specFormData.selectedCategoryId"
              placeholder="请选择规格分类"
              clearable
              filterable
              style="width: 100%"
              @change="onSpecCategoryChange"
            >
              <el-option
                v-for="category in specificationCategories"
                :key="category.id"
                :label="category.categoryName"
                :value="category.id"
              >
                <div class="spec-option-content">
                  <span class="spec-option-name">{{ category.categoryName }}</span>
                  <span
                    v-if="category.description"
                    class="spec-option-desc"
                  >
                    ({{ category.description }})
                  </span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>

          <!-- 规格项和规格值展示 -->
          <el-form-item 
            v-if="specFormData.selectedCategoryId && currentCategoryItems.length > 0"
            label="规格项与值"
            required
          >
            <div class="spec-category-content">
              <div class="spec-category-header">
                <span>{{ getCurrentCategoryName() }} 包含的规格项</span>
                <el-tag
                  size="small"
                  type="info"
                >
                  {{ currentCategoryItems.length }} 个规格项
                </el-tag>
              </div>
              
              <div class="spec-items-list">
                <div 
                  v-for="item in currentCategoryItems" 
                  :key="item.id"
                  class="spec-item-container"
                >
                  <div class="spec-item-header">
                    <div class="spec-item-info">
                      <span class="spec-item-name">{{ item.itemName }}</span>
                      <el-tag
                        v-if="item.unit"
                        size="small"
                        type="info"
                      >
                        {{ item.unit }}
                      </el-tag>
                    </div>
                    <el-tag
                      size="small"
                      type="success"
                    >
                      {{ item.values?.length || 0 }} 个可选值
                    </el-tag>
                  </div>
                  
                  <div
                    v-if="item.values && item.values.length > 0"
                    class="spec-values-selection"
                  >
                    <el-checkbox-group 
                      v-model="specFormData.selectedValuesByItem[item.id]" 
                      class="spec-values-grid"
                      @change="onValueSelectionChange"
                    >
                      <el-checkbox
                        v-for="value in item.values"
                        :key="value.id"
                        :label="value.id"
                        class="spec-value-checkbox"
                      >
                        <div class="spec-value-content">
                          <span class="value-name">{{ value.valueName || value.valueCode }}</span>
                          <el-tag 
                            v-if="value.numericValue" 
                            size="small" 
                            type="info" 
                            class="value-numeric-tag"
                          >
                            {{ value.numericValue }}{{ item.unit || '' }}
                          </el-tag>
                        </div>
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>
                  
                  <div
                    v-else
                    class="spec-no-values"
                  >
                    <el-alert
                      title="该规格项暂无可选值"
                      type="info"
                      :closable="false"
                      show-icon
                    />
                  </div>
                </div>
              </div>
            </div>
          </el-form-item>

          <!-- 选择提示 -->
          <div
            v-if="!specFormData.selectedCategoryId && !loadingSpecs"
            class="spec-form-hint"
          >
            <el-alert
              title="请先选择规格分类"
              description="选择规格分类后，系统会显示该分类下的所有规格项和对应的规格值"
              type="info"
              :closable="false"
              show-icon
            />
          </div>

          <!-- 无规格项提示 -->
          <div
            v-if="specFormData.selectedCategoryId && currentCategoryItems.length === 0 && !loadingSpecItems"
            class="spec-form-hint"
          >
            <el-alert
              title="该分类暂无规格项"
              description="该规格分类下暂时没有配置规格项，请先到规格管理中添加规格项"
              type="warning"
              :closable="false"
              show-icon
            />
          </div>

          <!-- 已选择的规格总览 -->
          <div
            v-if="getTotalSelectedValues() > 0"
            class="selected-values-summary"
          >
            <div class="summary-header">
              <el-icon><Check /></el-icon>
              <span>已选择规格总览</span>
              <el-tag
                size="small"
                type="success"
              >
                {{ getTotalSelectedValues() }} 个规格值
              </el-tag>
            </div>
            <div class="summary-content">
              <div 
                v-for="item in currentCategoryItems.filter(item => specFormData.selectedValuesByItem[item.id]?.length > 0)"
                :key="item.id"
                class="summary-item-group"
              >
                <span class="summary-item-name">{{ item.itemName }}:</span>
                <div class="summary-item-values">
                  <el-tag
                    v-for="valueId in specFormData.selectedValuesByItem[item.id]"
                    :key="valueId"
                    size="small"
                    type="success"
                    class="summary-value-tag"
                  >
                    {{ getValueNameById(valueId, item.values) }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </el-form>
      </div>

      <!-- 加载状态 -->
      <div
        v-if="loadingSpecs"
        class="spec-loading"
      >
        <el-icon class="is-loading">
          <Loading />
        </el-icon>
        <span>正在加载规格数据...</span>
      </div>

      <!-- 空状态 -->
      <div
        v-if="!loadingSpecs && specificationCategories.length === 0"
        class="spec-dialog-empty"
      >
        <el-empty 
          description="暂无可用的规格类型"
          :image-size="120"
        >
          <template #description>
            <div class="empty-desc">
              <div>暂无可用的规格类型</div>
              <div>请先在规格管理中创建规格类型</div>
            </div>
          </template>
        </el-empty>
      </div>
    </div>

    <template #footer>
      <div class="dialog-footer">
        <el-button
          size="large"
          @click="specificationDialogVisible = false"
        >
          取消
        </el-button>
        <el-button 
          type="primary" 
          size="large"
          :disabled="!hasSelectedSpecs"
          @click="confirmSpecificationSelection"
        >
          确定选择
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Star, Setting, Collection, Close, Delete, Check, Loading } from '@element-plus/icons-vue'

// 引入API和工具函数
import { 
  getGoodsById, 
  createGoods, 
  updateGoods,
  checkGoodsCode,
  getGoodsSpecifications,
  setGoodsSpecifications
} from '@/api/goods'
import { getCategoryTree } from '@/api/category'
import { getActiveBrands } from '@/api/brand'
import {
  getSpecificationValues,
  getActiveSpecificationCategories
} from '@/api/specification'
import {
  getSpecificationItems,
  getSpecificationItemsByCategory
} from '@/api/specification-item'
import { 
  getImagesByGoodsId, 
  updateImage,
  deleteImage,
  setAsMainImage as setMainImageApi 
} from '@/api/image'
import { uploadProductImage } from '@/api/file'
import { hasActionPermission, PERMISSIONS } from '@/utils/permission'

const router = useRouter()
const route = useRoute()

// 判断是否为编辑模式
const isEdit = computed(() => !!route.params.id)

// 表单引用
const formRef = ref()
const uploadRef = ref()

// 加载状态
const submitLoading = ref(false)
const imageUploading = ref(false)

// 表单数据
const formData = reactive({
  id: null,
  goodsName: '',
  goodsCode: '',
  description: '',
  categoryId: null,
  brandId: null,
  goodsType: 1, // 商品类型：1-实物商品，2-虚拟商品
  unit: '',
  weight: 0,
  volume: 0,
  sellingPrice: 0, // 售价
  costPrice: 0, // 成本价
  originalPrice: 0, // 原价/市场价
  minPrice: 0, // 最低价
  stockQty: 0,
  warnStock: 0, // 预警库存
  saleQty: 0, // 销量
  status: 1, // 销售状态：1-上架，0-下架
  isRecommended: 0, // 推荐商品
  isHot: 0, // 热销商品
  isNew: 0, // 新品
  keywords: '',
  tags: '',
  mainImage: '',
  videoUrl: '',
  brief: '',
  remark: '',
  sort: 0
})

// 保存原始商品编码，用于编辑时的验证
const originalGoodsCode = ref('')

// 分类和品牌数据
const categoryList = ref([])
const brandList = ref([])

// 规格相关数据
const specificationDialogVisible = ref(false)
const specificationCategories = ref([])
const specificationItems = ref([])
const selectedSpecCategory = ref(null)
const selectedSpecValues = ref({})
const selectedSpecifications = ref([])

// 新的规格数据结构
const specificationGroups = ref([])
const loadingSpecs = ref(false)
const loadingSpecItems = ref(false)
const categoryItemCounts = ref({})
const selectedCategoryName = ref('')

// 新的规格表单数据
const specFormData = ref({
  selectedCategoryId: null,
  selectedValuesByItem: {} // 按规格项ID组织的选中值：{ itemId: [valueId1, valueId2] }
})

// 当前分类下的规格项
const currentCategoryItems = ref([])

// 图片相关数据
const imageList = ref([])
const imageFileList = ref([])

// 上传配置
const uploadAction = ref('/api/prod/images/upload')
const uploadHeaders = computed(() => ({
  'Authorization': `Bearer ${localStorage.getItem('token')}`
}))
const uploadData = computed(() => ({
  subDir: 'goods',
  goodsId: formData.id || 0 // 新建时为0，编辑时为实际ID
}))

// 规格分组计算属性
const groupedSpecifications = computed(() => {
  const groups = {}
  selectedSpecifications.value.forEach(spec => {
    const categoryName = spec.categoryName || '其他'
    if (!groups[categoryName]) {
      groups[categoryName] = {
        categoryName,
        specifications: []
      }
    }
    groups[categoryName].specifications.push(spec)
  })
  return Object.values(groups)
})

// 按分类分组的规格计算属性（用于横向显示）
const groupedSpecificationsByCategory = computed(() => {
  const categories = {}
  
  specificationGroups.value.forEach(group => {
    // 从规格组中获取分类信息，如果没有则使用默认分类
    const categoryName = group.categoryName || '默认分类'
    
    if (!categories[categoryName]) {
      categories[categoryName] = {
        categoryName,
        groups: [],
        totalValues: 0
      }
    }
    
    categories[categoryName].groups.push(group)
    categories[categoryName].totalValues += group.values.length
  })
  
  return Object.values(categories)
})

// 检查是否有选中的规格
const hasSelectedSpecs = computed(() => {
  return specFormData.value.selectedCategoryId && getTotalSelectedValues() > 0
})

// 获取当前分类名称
const getCurrentCategoryName = () => {
  if (!specFormData.value.selectedCategoryId) return ''
  const category = specificationCategories.value.find(c => c.id === specFormData.value.selectedCategoryId)
  return category ? category.categoryName : ''
}

// 计算总选中的规格值数量
const getTotalSelectedValues = () => {
  let total = 0
  Object.values(specFormData.value.selectedValuesByItem).forEach(values => {
    total += values?.length || 0
  })
  return total
}



// 表单验证规则
const formRules = {
  goodsName: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
    { min: 1, max: 100, message: '商品名称长度在1到100个字符', trigger: 'blur' }
  ],
  goodsCode: [
    { required: true, message: '请输入商品编码', trigger: 'blur' },
    { min: 1, max: 50, message: '商品编码长度在1到50个字符', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        // 编辑模式下，如果商品编码没有改变，跳过验证
        if (isEdit.value && originalGoodsCode.value === value) {
          callback()
          return true
        }
        // 否则调用原验证函数
        validateGoodsCode(rule, value, callback)
      }, 
      trigger: 'blur' 
    }
  ],
  categoryId: [
    { required: true, message: '请选择商品分类', trigger: 'change' }
  ],
  sellingPrice: [
    { required: true, message: '请输入售价', trigger: 'blur' },
    { type: 'number', min: 0, message: '售价不能小于0', trigger: 'blur' }
  ],
  stockQty: [
    { required: true, message: '请输入库存数量', trigger: 'blur' },
    { type: 'number', min: 0, message: '库存数量不能小于0', trigger: 'blur' }
  ]
}

// 商品编码验证器
async function validateGoodsCode(rule, value, callback) {
  if (!value) {
    callback()
    return
  }
  
  // 编辑模式下，如果商品编码没有改变，跳过验证
  if (isEdit.value && originalGoodsCode.value === value) {
    callback()
    return true
  }
  
  try {
    const response = await checkGoodsCode(value)
    if (response.success && response.data) {
      callback(new Error('商品编码已存在'))
    } else {
      callback()
    }
  } catch (error) {
    console.error('商品编码验证出错:', error)
    callback()
  }
}

// 页面初始化
onMounted(async () => {
  // 检查权限
  if (isEdit.value && !hasActionPermission(PERMISSIONS.GOODS.GOODS.EDIT)) {
    ElMessage.error('您没有编辑商品的权限')
    router.back()
    return
  }
  
  if (!isEdit.value && !hasActionPermission(PERMISSIONS.GOODS.GOODS.CREATE)) {
    ElMessage.error('您没有创建商品的权限')
    router.back()
    return
  }

  // 加载基础数据
  await Promise.all([
    loadCategoryList(),
    loadBrandList()
  ])

  // 如果是编辑模式，加载商品数据
  if (isEdit.value) {
    await loadGoodsData()
  }
})

// 加载分类列表
const loadCategoryList = async () => {
  try {
    const response = await getCategoryTree()
    if (response.success) {
      categoryList.value = response.data || []
    }
  } catch (error) {
    console.error('加载分类列表失败:', error)
  }
}

// 加载品牌列表
const loadBrandList = async () => {
  try {
    const response = await getActiveBrands()
    if (response.success) {
      brandList.value = response.data || []
    } else {
      console.error('获取品牌列表失败:', response.message)
    }
  } catch (error) {
    console.error('加载品牌列表失败:', error)
  }
}

// ==================== 规格相关方法 ====================

// 加载规格分类
const loadSpecificationCategories = async () => {
  try {
    loadingSpecs.value = true
    const response = await getActiveSpecificationCategories()
    if (response.success) {
      specificationCategories.value = response.data || []
      
      // 统计每个分类下的规格项数量
      for (const category of specificationCategories.value) {
        try {
          const itemsResponse = await getSpecificationItemsByCategory(category.id)
          if (itemsResponse.success) {
            categoryItemCounts.value[category.id] = itemsResponse.data?.length || 0
          }
        } catch (error) {
          categoryItemCounts.value[category.id] = 0
        }
      }
    }
  } catch (error) {
    console.error('加载规格分类失败:', error)
    ElMessage.error('加载规格分类失败')
  } finally {
    loadingSpecs.value = false
  }
}

// 选择规格分类
const selectSpecCategory = async (categoryId) => {
  selectedSpecCategory.value = categoryId
  const category = specificationCategories.value.find(c => c.id === categoryId)
  selectedCategoryName.value = category ? category.categoryName : ''
  await loadSpecificationItems()
}

// 加载规格项
const loadSpecificationItems = async () => {
  if (!selectedSpecCategory.value) {
    specificationItems.value = []
    return
  }
  
  try {
    loadingSpecItems.value = true
    const response = await getSpecificationItemsByCategory(selectedSpecCategory.value)
    if (response.success) {
      // 为每个规格项加载规格值
      const items = response.data || []
      for (const item of items) {
        const valuesResponse = await getSpecificationValues({
          itemId: item.id,
          pageSize: 100
        })
        if (valuesResponse.success) {
          item.values = valuesResponse.data.content || []
        } else {
          item.values = []
        }
      }
      specificationItems.value = items
    }
  } catch (error) {
    console.error('加载规格项失败:', error)
    ElMessage.error('加载规格项失败')
  } finally {
    loadingSpecItems.value = false
  }
}

// 打开规格选择对话框 - 修改为添加规格组
const openSpecificationDialog = async () => {
  specificationDialogVisible.value = true
  // 重置表单数据
  specFormData.value = {
    selectedCategoryId: null,
    selectedValuesByItem: {}
  }
  currentCategoryItems.value = []
  await loadSpecificationCategories()
}

// 加载所有规格项数据
const loadAllSpecificationItems = async () => {
  try {
    // 为每个分类加载规格项
    for (const category of specificationCategories.value) {
      const response = await getSpecificationItems({
        categoryId: category.id,
        pageSize: 100
      })
      if (response.success) {
        const items = response.data.content || []
        // 为每个规格项加载规格值
        for (const item of items) {
          const valuesResponse = await getSpecificationValues({
            itemId: item.id,
            pageSize: 100
          })
          if (valuesResponse.success) {
            item.values = valuesResponse.data.content || []
          } else {
            item.values = []
          }
          // 添加分类信息
          item.categoryId = category.id
          item.categoryName = category.categoryName
        }
        specificationItems.value.push(...items)
      }
    }
  } catch (error) {
    console.error('加载所有规格项失败:', error)
  }
}

// 根据分类ID获取规格项
const getItemsByCategory = (categoryId) => {
  return specificationItems.value.filter(item => item.categoryId === categoryId)
}

// 规格分类选择变化处理
const onSpecCategoryChange = async (categoryId) => {
  if (categoryId) {
    await loadCategoryItems(categoryId)
  } else {
    currentCategoryItems.value = []
  }
  // 清空已选择的规格值
  specFormData.value.selectedValuesByItem = {}
}

// 加载分类下的规格项
const loadCategoryItems = async (categoryId) => {
  try {
    loadingSpecItems.value = true
    const response = await getSpecificationItems({
      categoryId: categoryId,
      pageSize: 100
    })
    if (response.success) {
      const items = response.data.content || []
      // 为每个规格项加载规格值
      for (const item of items) {
        const valuesResponse = await getSpecificationValues({
          itemId: item.id,
          pageSize: 100
        })
        if (valuesResponse.success) {
          item.values = valuesResponse.data.content || []
        } else {
          item.values = []
        }
      }
      currentCategoryItems.value = items
      
      // 初始化选中值对象
      const selectedValuesByItem = {}
      items.forEach(item => {
        selectedValuesByItem[item.id] = []
      })
      specFormData.value.selectedValuesByItem = selectedValuesByItem
    }
  } catch (error) {
    console.error('加载分类规格项失败:', error)
    ElMessage.error('加载分类规格项失败')
  } finally {
    loadingSpecItems.value = false
  }
}

// 规格值选择变化处理
const onValueSelectionChange = () => {
  // 可以在这里添加一些逻辑，比如实时更新预览等
}

// 根据值ID获取值名称
const getValueNameById = (valueId, values) => {
  if (!values) return ''
  const value = values.find(v => v.id === valueId)
  return value ? (value.valueName || value.valueCode) : ''
}



// 关闭规格选择对话框
const handleSpecificationDialogClose = (done) => {
  // 重置选择状态
  selectedSpecCategory.value = null
  selectedCategoryName.value = ''
  selectedSpecValues.value = {}
  specificationItems.value = []
  specificationCategories.value = []
  categoryItemCounts.value = {}
  
  // 重置新表单数据
  specFormData.value = {
    selectedCategoryId: null,
    selectedValuesByItem: {}
  }
  currentCategoryItems.value = []
  
  if (done) {
    done()
  } else {
    specificationDialogVisible.value = false
  }
}

// 确认规格选择
const confirmSpecificationSelection = () => {
  if (!specFormData.value.selectedCategoryId || getTotalSelectedValues() === 0) {
    ElMessage.warning('请选择规格分类和规格值')
    return
  }

  const categoryName = getCurrentCategoryName()
  if (!categoryName) {
    ElMessage.error('规格分类信息不完整')
    return
  }

  // 移除该分类下已存在的规格组
  const existingCategoryGroups = specificationGroups.value.filter(group => 
    group.categoryName === categoryName
  )
  
  if (existingCategoryGroups.length > 0) {
    // 移除现有分类的所有规格
    const existingGroupIds = existingCategoryGroups.map(g => g.id)
    specificationGroups.value = specificationGroups.value.filter(group => 
      !existingGroupIds.includes(group.id)
    )
    
    // 从 selectedSpecifications 中移除该分类的所有规格值
    selectedSpecifications.value = selectedSpecifications.value.filter(spec => 
      spec.categoryName !== categoryName
    )
  }

  // 为每个有选中值的规格项创建规格组
  currentCategoryItems.value.forEach(item => {
    const selectedValues = specFormData.value.selectedValuesByItem[item.id]
    if (selectedValues && selectedValues.length > 0) {
      // 创建新的规格组
      const specGroup = {
        id: `group_${Date.now()}_${item.id}`,
        name: item.itemName,
        specItemId: item.id,
        categoryName: categoryName,
        values: selectedValues.map(valueId => {
          const value = item.values.find(v => v.id == valueId)
          return {
            id: value.id,
            name: value.valueName || value.valueCode,
            valueId: value.id
          }
        })
      }
      
      // 添加新的规格组
      specificationGroups.value.push(specGroup)
      
      // 同时添加到 selectedSpecifications
      selectedValues.forEach(valueId => {
        const value = item.values.find(v => v.id == valueId)
        if (value) {
          selectedSpecifications.value.push({
            id: value.id,
            itemId: item.id,
            itemName: item.itemName,
            valueName: value.valueName || value.valueCode,
            categoryName: categoryName
          })
        }
      })
    }
  })
  
  // 如果是编辑模式，立即保存规格绑定
  if (isEdit.value && formData.id) {
    saveGoodsSpecifications()
  }
  
  ElMessage.success(`规格分类"${categoryName}"添加成功`)
  
  // 关闭对话框
  specificationDialogVisible.value = false
}

// 移除单个规格
const removeSpecification = (specId) => {
  selectedSpecifications.value = selectedSpecifications.value.filter(spec => spec.id !== specId)
  
  // 如果是编辑模式，保存规格绑定
  if (isEdit.value && formData.id) {
    saveGoodsSpecifications()
  }
}

// 清空所有规格
const clearAllSpecifications = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空所有商品规格吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    selectedSpecifications.value = []
    specificationGroups.value = []
    
    // 如果是编辑模式，保存规格绑定
    if (isEdit.value && formData.id) {
      await saveGoodsSpecifications()
    }
    
    ElMessage.success('已清空所有规格')
  } catch {
    // 用户取消操作
  }
}

// 删除规格组
const removeSpecificationGroup = (groupId) => {
  const group = specificationGroups.value.find(g => g.id === groupId)
  if (group) {
    // 同时从 selectedSpecifications 中删除该规格项的所有值
    selectedSpecifications.value = selectedSpecifications.value.filter(spec => spec.itemId != group.specItemId)
    
    // 从 specificationGroups 中删除
    const index = specificationGroups.value.findIndex(g => g.id === groupId)
    if (index !== -1) {
      specificationGroups.value.splice(index, 1)
    }
    
    // 如果是编辑模式，保存规格绑定
    if (isEdit.value && formData.id) {
      saveGoodsSpecifications()
    }
    
    ElMessage.success('规格类型删除成功')
  }
}

// 删除规格值
const removeSpecificationValue = (groupId, valueId) => {
  const group = specificationGroups.value.find(g => g.id === groupId)
  if (group) {
    const valueIndex = group.values.findIndex(v => v.id === valueId)
    if (valueIndex !== -1) {
      // 从 specificationGroups 中删除规格值
      group.values.splice(valueIndex, 1)
      
      // 同时从 selectedSpecifications 中删除对应的规格值
      selectedSpecifications.value = selectedSpecifications.value.filter(spec => spec.id !== valueId)
      
      // 如果该组没有规格值了，删除整个组
      if (group.values.length === 0) {
        removeSpecificationGroup(groupId)
      } else {
        // 如果是编辑模式，保存规格绑定
        if (isEdit.value && formData.id) {
          saveGoodsSpecifications()
        }
        ElMessage.success('规格值删除成功')
      }
    }
  }
}

// 删除整个规格分类
const removeSpecificationCategory = (categoryName) => {
  // 找到该分类下的所有规格组
  const groupsToRemove = specificationGroups.value.filter(group => {
    const groupCategoryName = group.categoryName || '默认分类'
    return groupCategoryName === categoryName
  })
  
  if (groupsToRemove.length > 0) {
    // 收集要删除的规格值ID
    const specValueIdsToRemove = []
    groupsToRemove.forEach(group => {
      group.values.forEach(value => {
        specValueIdsToRemove.push(value.id)
      })
    })
    
    // 从 selectedSpecifications 中删除这些规格值
    selectedSpecifications.value = selectedSpecifications.value.filter(spec => 
      !specValueIdsToRemove.includes(spec.id)
    )
    
    // 从 specificationGroups 中删除该分类的所有组
    specificationGroups.value = specificationGroups.value.filter(group => {
      const groupCategoryName = group.categoryName || '默认分类'
      return groupCategoryName !== categoryName
    })
    
    // 如果是编辑模式，保存规格绑定
    if (isEdit.value && formData.id) {
      saveGoodsSpecifications()
    }
    
    ElMessage.success(`已删除分类"${categoryName}"下的所有规格`)
  }
}



// 保存商品规格绑定
const saveGoodsSpecifications = async () => {
  if (!formData.id) return
  
  try {
    const specValueIds = selectedSpecifications.value.map(spec => spec.id)
    await setGoodsSpecifications(formData.id, specValueIds)
    ElMessage.success('规格设置成功')
  } catch (error) {
    console.error('保存规格绑定失败:', error)
    ElMessage.error('保存规格绑定失败')
  }
}

// 加载商品已绑定的规格
const loadGoodsSpecifications = async (goodsId) => {
  try {
    const response = await getGoodsSpecifications(goodsId)
    if (response.success) {
      const specifications = response.data.map(item => ({
        id: item.specificationValue.id,
        itemId: item.specificationValue.itemId,
        itemName: item.specificationValue.item.itemName,
        valueName: item.specificationValue.valueName,
        categoryName: item.specificationValue.item.category.categoryName
      }))
      selectedSpecifications.value = specifications
      
      // 转换为 specificationGroups 格式以便页面正确显示
      convertSpecificationsToGroups(specifications)
    }
  } catch (error) {
    console.error('加载商品规格失败:', error)
  }
}

// 将规格数据转换为 specificationGroups 格式
const convertSpecificationsToGroups = (specifications) => {
  const groupsMap = {}
  
  specifications.forEach(spec => {
    const key = `${spec.itemId}_${spec.categoryName}`
    if (!groupsMap[key]) {
      groupsMap[key] = {
        id: spec.itemId,
        name: spec.itemName,
        categoryName: spec.categoryName,
        values: []
      }
    }
    
    groupsMap[key].values.push({
      id: spec.id,
      name: spec.valueName
    })
  })
  
  specificationGroups.value = Object.values(groupsMap)
}

// 加载商品数据
const loadGoodsData = async () => {
  try {
    const response = await getGoodsById(route.params.id)
    if (response.success) {
      Object.assign(formData, response.data)
      // 保存原始商品编码
      originalGoodsCode.value = response.data.goodsCode
      console.log('加载商品数据完成，原始编码:', originalGoodsCode.value)
      // 加载商品图片
      await loadProductImages(formData.id)
      // 加载商品规格
      await loadGoodsSpecifications(formData.id)
    } else {
      ElMessage.error('商品不存在')
      router.back()
    }
  } catch (error) {
    console.error('加载商品数据失败:', error)
    ElMessage.error('加载商品数据失败')
    router.back()
  }
}

// 加载商品图片
const loadProductImages = async (goodsId) => {
  if (!goodsId) return
  
  try {
    const response = await getImagesByGoodsId(goodsId)
    if (response.success) {
      imageList.value = response.data || []
      // 转换为上传组件的格式
      imageFileList.value = imageList.value.map(img => ({
        uid: img.id,
        name: img.imageName,
        url: getImageDisplayUrl(img),
        status: 'success'
      }))
    }
  } catch (error) {
    console.error('加载商品图片失败:', error)
  }
}

// 获取图片显示URL
const getImageDisplayUrl = (image) => {
  if (!image) return ''
  if (typeof image === 'string') return image
  if (image.imageUrl) return image.imageUrl
  if (image.url) return image.url
  return ''
}

// 分类变化处理
const handleCategoryChange = (categoryId) => {
  // 这里可以根据分类加载对应的规格等信息
  console.log('分类变化:', categoryId)
}

// 图片上传相关方法
const beforeImageUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5

  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过5MB!')
    return false
  }
  
  imageUploading.value = true
  return true
}

const handleImageSuccess = (response, file) => {
  imageUploading.value = false
  if (response.success) {
    const imageData = response.data
    imageList.value.push(imageData)
    
    // 如果是第一张图片，设置为主图
    if (imageList.value.length === 1) {
      setMainImage(imageData)
    }
    
    ElMessage.success('图片上传成功')
  } else {
    ElMessage.error(response.message || '图片上传失败')
  }
}

const handleImageError = (error, file) => {
  imageUploading.value = false
  console.error('图片上传失败:', error)
  ElMessage.error('图片上传失败')
}

const handleImageRemove = async (file) => {
  try {
    // 查找对应的图片数据
    const imageData = imageList.value.find(img => 
      img.id === file.uid || 
      img.imageName === file.name ||
      img.imageUrl === file.url
    )
    
    if (imageData && imageData.id) {
      const response = await deleteImage(imageData.id)
      if (response.success) {
        // 从列表中移除
        const index = imageList.value.findIndex(img => img.id === imageData.id)
        if (index > -1) {
          imageList.value.splice(index, 1)
        }
        ElMessage.success('图片删除成功')
      }
    }
  } catch (error) {
    console.error('删除图片失败:', error)
    ElMessage.error('删除图片失败')
  }
}

// 设置主图
const setMainImage = async (image) => {
  try {
    if (image.id) {
      const response = await setMainImageApi(image.id)
      if (response.success) {
        // 更新本地数据
        imageList.value.forEach(img => {
          img.isMain = img.id === image.id
        })
        formData.mainImage = image.imageUrl
        ElMessage.success('主图设置成功')
      }
    } else {
      // 临时设置（新上传的图片）
      imageList.value.forEach(img => {
        img.isMain = img === image
      })
      formData.mainImage = getImageDisplayUrl(image)
    }
  } catch (error) {
    console.error('设置主图失败:', error)
    ElMessage.error('设置主图失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    const response = isEdit.value 
      ? await updateGoods(formData.id, formData)
      : await createGoods(formData)
      
    if (response.success) {
      // 保存规格绑定（新建商品时需要使用返回的商品ID）
      const goodsId = isEdit.value ? formData.id : response.data.id
      if (selectedSpecifications.value.length > 0) {
        try {
          const specValueIds = selectedSpecifications.value.map(spec => spec.id)
          await setGoodsSpecifications(goodsId, specValueIds)
        } catch (specError) {
          console.error('保存规格绑定失败:', specError)
          ElMessage.warning('商品保存成功，但规格绑定失败，请手动设置')
        }
      }
      
      ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
      router.push('/goods')
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('提交失败:', error)
      ElMessage.error('操作失败')
    }
  } finally {
    submitLoading.value = false
  }
}

// 返回处理
const handleBack = () => {
  router.back()
}

// 加载分类数据
const loadCategories = async () => {
  try {
    const response = await getCategoryTree()
    if (response.success) {
      categoryList.value = response.data || []
    }
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

// 加载品牌数据
const loadBrands = async () => {
  try {
    const response = await getActiveBrands()
    if (response.success) {
      brandList.value = response.data || []
    }
  } catch (error) {
    console.error('加载品牌失败:', error)
  }
}

// 加载商品详情
const loadGoodsDetail = async (id) => {
  try {
    const response = await getGoodsById(id)
    if (response.success && response.data) {
      const goods = response.data
      // 填充表单数据
      Object.assign(formData, {
        id: goods.id,
        goodsName: goods.goodsName || '',
        goodsCode: goods.goodsCode || '',
        description: goods.description || '',
        brief: goods.brief || '',
        categoryId: goods.categoryId,
        brandId: goods.brandId,
        goodsType: goods.goodsType || 1,
        unit: goods.unit || '',
        weight: goods.weight || 0,
        volume: goods.volume || 0,
        sellingPrice: goods.sellingPrice || 0,
        costPrice: goods.costPrice || 0,
        originalPrice: goods.originalPrice || 0,
        minPrice: goods.minPrice || 0,
        stockQty: goods.stockQty || 0,
        warnStock: goods.warnStock || 0,
        saleQty: goods.saleQty || 0,
        status: goods.status !== undefined ? goods.status : 1,
        isRecommended: goods.isRecommended || 0,
        isHot: goods.isHot || 0,
        isNew: goods.isNew || 0,
        keywords: goods.keywords || '',
        tags: goods.tags || '',
        mainImage: goods.mainImage || '',
        videoUrl: goods.videoUrl || '',
        remark: goods.remark || '',
        sort: goods.sort || 0
      })
      
      // 保存原始商品编码
      originalGoodsCode.value = goods.goodsCode || ''
      console.log('加载商品数据完成，原始编码:', originalGoodsCode.value)
      
      // 加载商品图片
      await loadGoodsImages(id)
      
      // 加载商品规格
      await loadGoodsSpecifications(id)
    } else {
      ElMessage.error('获取商品详情失败')
      router.push('/goods')
    }
  } catch (error) {
    console.error('加载商品详情失败:', error)
    ElMessage.error('获取商品详情失败')
    router.push('/goods')
  }
}

// 加载商品图片
const loadGoodsImages = async (goodsId) => {
  try {
    const response = await getImagesByGoodsId(goodsId)
    if (response.success && response.data) {
      imageList.value = response.data.map(img => ({
        ...img,
        url: img.imageUrl,
        name: img.imageName || `image_${img.id}`
      }))
      
      // 设置上传组件的文件列表
      imageFileList.value = imageList.value.map(img => ({
        name: img.name,
        url: img.url,
        uid: img.id,
        status: 'success'
      }))
    }
  } catch (error) {
    console.error('加载商品图片失败:', error)
  }
}

// 初始化数据
const initData = async () => {
  // 加载分类和品牌数据
  await Promise.all([
    loadCategories(),
    loadBrands()
  ])
  
  // 如果是编辑模式，加载商品详情
  if (isEdit.value && route.params.id) {
    await loadGoodsDetail(route.params.id)
  }
}

// 组件挂载时初始化
onMounted(() => {
  initData()
})
</script>

<style scoped>
.goods-edit {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 60px);
}

.page-header {
  background: white;
  padding: 16px 24px;
  margin-bottom: 20px;
  border-radius: 6px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
}

.page-title {
  font-size: 18px;
  font-weight: 500;
  color: #303133;
}

.page-content {
  max-width: 1200px;
  margin: 0 auto;
}

.form-section {
  margin-bottom: 20px;
}

.form-section :deep(.el-card__header) {
  padding: 16px 24px;
  border-bottom: 1px solid #ebeef5;
}

.form-section :deep(.el-card__body) {
  padding: 24px;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.section-title {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.section-tip {
  font-size: 12px;
  color: #909399;
}

.goods-form :deep(.el-form-item) {
  margin-bottom: 24px;
}

.goods-form :deep(.el-form-item__content) {
  width: 100%;
  max-width: 100%;
}

.goods-form :deep(.el-input),
.goods-form :deep(.el-textarea),
.goods-form :deep(.el-select) {
  width: 100%;
}



.image-upload-container {
  width: 100%;
}

.main-image-section {
  margin-top: 20px;
}

.main-image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.image-item {
  position: relative;
  width: 120px;
  height: 120px;
  border: 2px solid #dcdfe6;
  border-radius: 6px;
  overflow: hidden;
  cursor: pointer;
  transition: all 0.3s;
}

.image-item:hover {
  border-color: #409eff;
}

.image-item.is-main {
  border-color: #f56c6c;
  box-shadow: 0 2px 8px rgba(245, 108, 108, 0.3);
}

.preview-image {
  width: 100%;
  height: 100%;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  color: white;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s;
}

.image-item:hover .image-overlay,
.image-item.is-main .image-overlay {
  opacity: 1;
}

.main-icon {
  font-size: 20px;
  color: #f56c6c;
  margin-bottom: 4px;
}

.image-text {
  font-size: 12px;
}

.form-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
  padding: 40px 0;
  background: white;
  border-radius: 6px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.1);
}

:deep(.el-upload--picture-card) {
  width: 120px;
  height: 120px;
}

:deep(.el-upload-list--picture-card .el-upload-list__item) {
  width: 120px;
  height: 120px;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
}

/* 规格选择样式 */
.specification-container {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: #fafafa;
  overflow: hidden;
  width: 100%;
  box-sizing: border-box;
}

/* 空状态样式 */
.spec-empty-state {
  text-align: center;
  padding: 40px 20px;
  background: #ffffff;
}

.spec-empty-content {
  margin-bottom: 24px;
}

.spec-empty-icon {
  margin-bottom: 16px;
}

.spec-empty-text {
  margin-bottom: 20px;
}

.spec-empty-title {
  font-size: 16px;
  color: #303133;
  margin-bottom: 8px;
  font-weight: 500;
}

.spec-empty-desc {
  font-size: 14px;
  color: #909399;
  line-height: 1.5;
}

/* 已选规格样式 */
.spec-content {
  background: #ffffff;
}

.spec-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f2f5;
  background: #f8f9fa;
}

.spec-header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.spec-icon {
  font-size: 18px;
}

.spec-title {
  font-weight: 600;
  color: #303133;
  font-size: 16px;
}

.spec-count {
  font-weight: normal;
}

.spec-actions {
  display: flex;
  gap: 8px;
}

.spec-list {
  padding: 20px;
}

.spec-category {
  margin-bottom: 24px;
}

.spec-category:last-child {
  margin-bottom: 0;
}

.spec-category-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f0f2f5;
}

.category-icon {
  font-size: 16px;
}

.spec-category-name {
  font-weight: 500;
  color: #303133;
  font-size: 15px;
}

.spec-category-count {
  font-weight: normal;
}

.spec-items {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
}

.spec-item-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  background: #ffffff;
  transition: all 0.2s;
}

.spec-item-card:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.spec-item-content {
  flex: 1;
  min-width: 0;
}

.spec-item-label {
  font-size: 13px;
  color: #909399;
  margin-bottom: 4px;
}

.spec-item-value {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
  word-break: break-all;
}

.spec-remove-btn {
  margin-left: 8px;
  flex-shrink: 0;
}

/* 规格分类横向布局样式 */
.spec-categories {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 20px;
}

.spec-category {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  background: #ffffff;
}

.spec-category-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f0f0f0;
}

.spec-category-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.spec-category-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.spec-items-horizontal {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
  align-items: flex-start;
}

.spec-item-group {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.spec-item-name {
  font-size: 14px;
  font-weight: 500;
  color: #606266;
  white-space: nowrap;
  flex-shrink: 0;
}

.spec-item-values {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.spec-value-tag {
  margin: 0;
}

/* 规格对话框样式 */
.spec-dialog {
  --el-dialog-border-radius: 12px;
}

.dialog-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.dialog-icon {
  font-size: 20px;
}

.specification-dialog {
  max-height: 600px;
  overflow-y: auto;
}

/* 新的规格表单样式 */
.spec-form-container {
  padding: 20px 0;
}

.spec-form {
  max-width: 100%;
}

/* 规格分类内容样式 */
.spec-category-content {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: #fafafa;
  overflow: hidden;
}

.spec-category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #e4e7ed;
  font-weight: 500;
  color: #303133;
}

.spec-items-list {
  padding: 16px;
  max-height: 400px;
  overflow-y: auto;
}

.spec-item-container {
  margin-bottom: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: #ffffff;
  overflow: hidden;
}

.spec-item-container:last-child {
  margin-bottom: 0;
}

.spec-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.spec-item-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.spec-item-name {
  font-weight: 500;
  color: #303133;
  font-size: 14px;
}

.spec-values-selection {
  padding: 16px;
}

.value-numeric-tag {
  margin-left: 4px;
}

/* 已选择规格总览样式 */
.selected-values-summary {
  margin-top: 20px;
  border: 1px solid #67c23a;
  border-radius: 8px;
  background: #f0f9ff;
  overflow: hidden;
}

.summary-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: #e1f3d8;
  border-bottom: 1px solid #67c23a;
  font-weight: 500;
  color: #67c23a;
}

.summary-content {
  padding: 16px;
}

.summary-item-group {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.summary-item-group:last-child {
  margin-bottom: 0;
}

.summary-item-name {
  font-weight: 500;
  color: #606266;
  min-width: 100px;
  flex-shrink: 0;
}

.summary-item-values {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.summary-value-tag {
  margin: 0;
}

.spec-option-content {
  display: flex;
  align-items: center;
  gap: 8px;
  width: 100%;
}

.spec-option-name {
  font-weight: 500;
  color: #303133;
}

.spec-option-desc {
  font-size: 12px;
  color: #909399;
  font-style: italic;
}

.spec-values-container {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  background: #fafafa;
}

.spec-values-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
  font-weight: 500;
  color: #303133;
}

.spec-values-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
}

.spec-value-checkbox {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  padding: 12px;
  transition: all 0.2s;
  margin: 0;
}

.spec-value-checkbox:hover {
  border-color: #409eff;
  background: #f0f9ff;
}

.spec-value-checkbox.is-checked {
  border-color: #409eff;
  background: #e1f3d8;
}

.spec-value-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
  margin-left: 8px;
}

.value-name {
  font-weight: 500;
  color: #303133;
}

.value-desc-tag {
  align-self: flex-start;
}

.spec-form-hint {
  margin: 20px 0;
}

.selected-values-preview {
  margin-top: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 16px;
  background: #f8f9fa;
}

.preview-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
  font-weight: 500;
  color: #67c23a;
}

.preview-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.preview-tag {
  margin: 0;
}

.spec-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 40px;
  color: #909399;
}

.spec-dialog-empty {
  padding: 40px 20px;
}

/* 分类筛选样式 */
.spec-filter-section {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;
  padding: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e4e7ed;
}

.filter-label {
  display: flex;
  align-items: center;
  gap: 6px;
  font-weight: 500;
  color: #303133;
  white-space: nowrap;
}

/* 规格项容器样式 */
.spec-items-container {
  background: #ffffff;
}

.spec-items-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f2f5;
  font-weight: 600;
  color: #303133;
  font-size: 16px;
}

.spec-items-list {
  max-height: 450px;
  overflow-y: auto;
  padding-right: 8px;
}

.spec-item-card {
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 16px;
  background: #ffffff;
  transition: all 0.2s;
}

.spec-item-card:hover {
  border-color: #409eff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.1);
}

.spec-item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #f5f7fa;
}

.spec-item-name {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: #303133;
  font-size: 15px;
}

.spec-item-icon {
  font-size: 16px;
}

.spec-values-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.spec-value-item {
  margin: 0;
  padding: 8px 12px;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  background: #ffffff;
  transition: all 0.2s;
  flex: 0 0 auto;
  white-space: nowrap;
}

.spec-value-item:hover {
  border-color: #409eff;
  background: #f0f8ff;
}

.spec-value-item.is-checked {
  border-color: #409eff;
  background: #e6f4ff;
  color: #409eff;
}

/* 空状态样式 */
.spec-dialog-empty {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
  background: #fafafa;
  border-radius: 8px;
  border: 1px dashed #d9d9d9;
}

.empty-desc {
  color: #909399;
  line-height: 1.6;
  text-align: center;
}

.empty-desc div {
  margin-bottom: 4px;
}

/* 对话框底部样式 */
.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #f0f2f5;
}

/* 新规格选择器样式 */
.spec-selector {
  background: #ffffff;
  border-radius: 8px;
}

.spec-groups {
  padding: 20px;
}

.spec-group {
  margin-bottom: 24px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background: #ffffff;
  overflow: hidden;
}

.spec-group:last-child {
  margin-bottom: 0;
}

.spec-group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #e4e7ed;
}

.spec-group-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.spec-group-name {
  font-weight: 600;
  color: #303133;
  font-size: 16px;
}

.spec-values {
  padding: 16px 20px;
}

.spec-value-item {
  display: inline-flex;
  align-items: center;
  justify-content: space-between;
  margin: 0 8px 8px 0;
  padding: 8px 12px;
  border: 1px solid #e4e7ed;
  border-radius: 20px;
  background: #f8f9fa;
  font-size: 14px;
  color: #303133;
  transition: all 0.2s;
}

.spec-value-item:hover {
  border-color: #409eff;
  background: #ecf5ff;
  color: #409eff;
}

.spec-value-text {
  margin-right: 8px;
}

.spec-value-remove {
  margin-left: 4px;
  padding: 0;
  min-width: auto;
  width: 16px;
  height: 16px;
}

.add-spec-value-btn {
  margin: 8px 0 0 0;
  border: 1px dashed #d9d9d9;
  background: #fafafa;
  color: #666;
  border-radius: 20px;
}

.add-spec-value-btn:hover {
  border-color: #409eff;
  color: #409eff;
  background: #ecf5ff;
}



/* 规格绑定相关样式 */
.spec-desc-tag {
  margin-left: 8px;
}

.spec-no-values {
  padding: 16px;
  text-align: center;
  color: #909399;
  background: #f8f9fa;
  border-radius: 4px;
}

.spec-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 60px;
  color: #909399;
  font-size: 14px;
}

/* 规格分类选择样式 */
.spec-category-section {
  margin-bottom: 24px;
}

.spec-category-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  font-weight: 600;
  color: #303133;
}

.spec-categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 12px;
}

.spec-category-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px;
  border: 2px solid #e4e7ed;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #ffffff;
}

.spec-category-card:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.spec-category-card.active {
  border-color: #409eff;
  background: #ecf5ff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.2);
}

.category-icon {
  margin-bottom: 8px;
  color: #409eff;
  font-size: 24px;
}

.category-info {
  text-align: center;
  margin-bottom: 8px;
}

.category-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}

.category-desc {
  font-size: 12px;
  color: #909399;
}

.category-count {
  font-size: 12px;
  color: #67c23a;
  background: #f0f9ff;
  padding: 2px 8px;
  border-radius: 12px;
}

.spec-select-hint {
  padding: 40px;
  text-align: center;
}

.empty-desc {
  color: #909399;
  line-height: 1.5;
}

.empty-desc div:first-child {
  font-size: 16px;
  margin-bottom: 8px;
}

.empty-desc div:last-child {
  font-size: 14px;
}
</style> 