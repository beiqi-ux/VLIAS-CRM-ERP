package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdCategory;
import com.example.vliascrm.service.ProdCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 商品分类管理控制器
 */
@RestController
@RequestMapping("/api/prod/categories")
@RequiredArgsConstructor
public class ProdCategoryController {

    private final ProdCategoryService prodCategoryService;

    /**
     * 获取分类列表
     * @return 分类列表
     */
    @GetMapping
    public ApiResponse<List<ProdCategory>> getCategoryList() {
        List<ProdCategory> categories = prodCategoryService.findAll();
        return ApiResponse.success(categories);
    }

    /**
     * 根据ID获取分类详情
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ProdCategory> getCategoryById(@PathVariable Long id) {
        Optional<ProdCategory> category = prodCategoryService.findById(id);
        if (category.isPresent()) {
            return ApiResponse.success(category.get());
        }
        return ApiResponse.failure("分类不存在");
    }

    /**
     * 获取根分类（一级分类）
     * @return 根分类列表
     */
    @GetMapping("/root")
    public ApiResponse<List<ProdCategory>> getRootCategories() {
        List<ProdCategory> categories = prodCategoryService.findRootCategories();
        return ApiResponse.success(categories);
    }

    /**
     * 根据父级ID获取子分类
     * @param parentId 父级ID
     * @return 子分类列表
     */
    @GetMapping("/parent/{parentId}")
    public ApiResponse<List<ProdCategory>> getCategoriesByParent(@PathVariable Long parentId) {
        List<ProdCategory> categories = prodCategoryService.findByParentId(parentId);
        return ApiResponse.success(categories);
    }

    /**
     * 根据层级获取分类
     * @param level 层级
     * @return 分类列表
     */
    @GetMapping("/level/{level}")
    public ApiResponse<List<ProdCategory>> getCategoriesByLevel(@PathVariable Integer level) {
        List<ProdCategory> categories = prodCategoryService.findByLevel(level);
        return ApiResponse.success(categories);
    }

    /**
     * 获取显示的分類
     * @return 显示的分类列表
     */
    @GetMapping("/visible")
    public ApiResponse<List<ProdCategory>> getVisibleCategories() {
        List<ProdCategory> categories = prodCategoryService.findVisibleCategories();
        return ApiResponse.success(categories);
    }

    /**
     * 搜索分类
     * @param categoryName 分类名称
     * @return 分类列表
     */
    @GetMapping("/search")
    public ApiResponse<List<ProdCategory>> searchCategories(@RequestParam String categoryName) {
        List<ProdCategory> categories = prodCategoryService.findByCategoryNameContaining(categoryName);
        return ApiResponse.success(categories);
    }

    /**
     * 构建分类树
     * @return 分类树
     */
    @GetMapping("/tree")
    public ApiResponse<List<ProdCategory>> getCategoryTree() {
        List<ProdCategory> categoryTree = prodCategoryService.buildCategoryTree();
        return ApiResponse.success(categoryTree);
    }

    /**
     * 创建分类
     * @param category 分类信息
     * @return 创建后的分类
     */
    @PostMapping
    public ApiResponse<ProdCategory> createCategory(@RequestBody ProdCategory category) {
        // 检查分类名称是否存在（同级下）
        Long parentId = category.getParentId() != null ? category.getParentId() : 0L;
        if (prodCategoryService.existsByCategoryNameAndParentId(category.getCategoryName(), parentId)) {
            return ApiResponse.failure("同级下分类名称已存在");
        }
        
        ProdCategory savedCategory = prodCategoryService.save(category);
        return ApiResponse.success(savedCategory);
    }

    /**
     * 更新分类
     * @param id 分类ID
     * @param category 分类信息
     * @return 更新后的分类
     */
    @PutMapping("/{id}")
    public ApiResponse<ProdCategory> updateCategory(@PathVariable Long id, @RequestBody ProdCategory category) {
        Optional<ProdCategory> existingCategory = prodCategoryService.findById(id);
        if (!existingCategory.isPresent()) {
            return ApiResponse.failure("分类不存在");
        }
        
        category.setId(id);
        ProdCategory updatedCategory = prodCategoryService.update(category);
        return ApiResponse.success(updatedCategory);
    }

    /**
     * 删除分类（软删除）
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteCategory(@PathVariable Long id) {
        Optional<ProdCategory> category = prodCategoryService.findById(id);
        if (!category.isPresent()) {
            return ApiResponse.failure("分类不存在");
        }
        
        // 检查是否有子分类
        long childCount = prodCategoryService.countByParentId(id);
        if (childCount > 0) {
            return ApiResponse.failure("存在子分类，无法删除");
        }
        
        prodCategoryService.deleteById(id);
        return ApiResponse.success("删除成功");
    }

    /**
     * 批量删除分类（软删除）
     * @param ids 分类ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public ApiResponse<String> batchDeleteCategories(@RequestBody List<Long> ids) {
        prodCategoryService.deleteByIds(ids);
        return ApiResponse.success("批量删除成功");
    }

    /**
     * 启用分类
     * @param id 分类ID
     * @return 操作结果
     */
    @PutMapping("/{id}/enable")
    public ApiResponse<String> enableCategory(@PathVariable Long id) {
        Optional<ProdCategory> category = prodCategoryService.findById(id);
        if (!category.isPresent()) {
            return ApiResponse.failure("分类不存在");
        }
        
        prodCategoryService.enable(id);
        return ApiResponse.success("启用成功");
    }

    /**
     * 禁用分类
     * @param id 分类ID
     * @return 操作结果
     */
    @PutMapping("/{id}/disable")
    public ApiResponse<String> disableCategory(@PathVariable Long id) {
        Optional<ProdCategory> category = prodCategoryService.findById(id);
        if (!category.isPresent()) {
            return ApiResponse.failure("分类不存在");
        }
        
        prodCategoryService.disable(id);
        return ApiResponse.success("禁用成功");
    }

    /**
     * 显示分类
     * @param id 分类ID
     * @return 操作结果
     */
    @PutMapping("/{id}/show")
    public ApiResponse<String> showCategory(@PathVariable Long id) {
        Optional<ProdCategory> category = prodCategoryService.findById(id);
        if (!category.isPresent()) {
            return ApiResponse.failure("分类不存在");
        }
        
        prodCategoryService.show(id);
        return ApiResponse.success("显示设置成功");
    }

    /**
     * 隐藏分类
     * @param id 分类ID
     * @return 操作结果
     */
    @PutMapping("/{id}/hide")
    public ApiResponse<String> hideCategory(@PathVariable Long id) {
        Optional<ProdCategory> category = prodCategoryService.findById(id);
        if (!category.isPresent()) {
            return ApiResponse.failure("分类不存在");
        }
        
        prodCategoryService.hide(id);
        return ApiResponse.success("隐藏设置成功");
    }

    /**
     * 检查分类名称是否存在（同级下）
     * @param categoryName 分类名称
     * @param parentId 父级ID
     * @return 检查结果
     */
    @GetMapping("/check-name")
    public ApiResponse<Boolean> checkCategoryName(@RequestParam String categoryName, 
                                                 @RequestParam(defaultValue = "0") Long parentId) {
        boolean exists = prodCategoryService.existsByCategoryNameAndParentId(categoryName, parentId);
        return ApiResponse.success(exists);
    }

    /**
     * 统计父级下的子分类数量
     * @param parentId 父级ID
     * @return 子分类数量
     */
    @GetMapping("/count/parent/{parentId}")
    public ApiResponse<Long> countByParent(@PathVariable Long parentId) {
        long count = prodCategoryService.countByParentId(parentId);
        return ApiResponse.success(count);
    }

    /**
     * 查询所有子级分类ID（用于级联删除）
     * @param parentId 父级ID
     * @return 子级分类ID列表
     */
    @GetMapping("/child-ids/{parentId}")
    public ApiResponse<List<Long>> getChildCategoryIds(@PathVariable Long parentId) {
        List<Long> childIds = prodCategoryService.findChildCategoryIds(parentId);
        return ApiResponse.success(childIds);
    }

    /**
     * 递归查询所有下级分类ID
     * @param parentId 父级ID
     * @return 所有下级分类ID列表
     */
    @GetMapping("/all-child-ids/{parentId}")
    public ApiResponse<List<Long>> getAllChildCategoryIds(@PathVariable Long parentId) {
        List<Long> allChildIds = prodCategoryService.findAllChildCategoryIds(parentId);
        return ApiResponse.success(allChildIds);
    }
} 