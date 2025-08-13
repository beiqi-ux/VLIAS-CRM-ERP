package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdSpecificationCategory;
import com.example.vliascrm.service.ProdSpecificationCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 商品规格分类管理控制器
 */
@RestController
@RequestMapping("/api/prod/spec-categories")
@RequiredArgsConstructor
public class ProdSpecificationCategoryController {

    private final ProdSpecificationCategoryService categoryService;

    /**
     * 获取规格分类列表（支持分页）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param categoryName 分类名称（可选）
     * @param status 状态（可选）
     * @return 规格分类分页列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<Page<ProdSpecificationCategory>> getCategoryList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Integer status) {
        
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("sort").ascending());
        Page<ProdSpecificationCategory> page = categoryService.findByConditions(
            pageable, categoryName, status);
        
        return ApiResponse.success(page);
    }

    /**
     * 获取所有启用的规格分类（不分页）
     * @return 启用的规格分类列表
     */
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<List<ProdSpecificationCategory>> getActiveCategories() {
        List<ProdSpecificationCategory> categories = categoryService.findActiveCategories();
        return ApiResponse.success(categories);
    }

    /**
     * 根据ID获取规格分类详情
     * @param id 分类ID
     * @return 规格分类详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<ProdSpecificationCategory> getCategoryById(@PathVariable Long id) {
        Optional<ProdSpecificationCategory> category = categoryService.findById(id);
        return category.map(ApiResponse::success)
                      .orElse(ApiResponse.failure("规格分类不存在"));
    }



    /**
     * 创建规格分类
     * @param category 规格分类信息
     * @return 创建后的规格分类
     */
    @PostMapping
    @PreAuthorize("hasAuthority('product-specification-management:create')")
    public ApiResponse<ProdSpecificationCategory> createCategory(@RequestBody ProdSpecificationCategory category) {
        // 检查分类名称是否存在
        if (categoryService.existsByCategoryName(category.getCategoryName())) {
            return ApiResponse.failure("分类名称已存在");
        }
        
        ProdSpecificationCategory savedCategory = categoryService.save(category);
        return ApiResponse.success(savedCategory);
    }

    /**
     * 更新规格分类
     * @param id 分类ID
     * @param category 规格分类信息
     * @return 更新后的规格分类
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product-specification-management:edit')")
    public ApiResponse<ProdSpecificationCategory> updateCategory(@PathVariable Long id, @RequestBody ProdSpecificationCategory category) {
        Optional<ProdSpecificationCategory> existingCategory = categoryService.findById(id);
        if (!existingCategory.isPresent()) {
            return ApiResponse.failure("规格分类不存在");
        }
        
        // 检查分类名称是否存在（排除当前记录）
        if (categoryService.existsByCategoryNameAndIdNot(category.getCategoryName(), id)) {
            return ApiResponse.failure("分类名称已存在");
        }
        
        category.setId(id);
        ProdSpecificationCategory updatedCategory = categoryService.update(category);
        return ApiResponse.success(updatedCategory);
    }

    /**
     * 删除规格分类（软删除）
     * @param id 分类ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product-specification-management:delete')")
    public ApiResponse<String> deleteCategory(@PathVariable Long id) {
        Optional<ProdSpecificationCategory> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return ApiResponse.failure("规格分类不存在");
        }
        
        // 检查是否被规格值使用
        if (categoryService.isUsedBySpecificationValues(id)) {
            return ApiResponse.failure("该分类下存在规格值，无法删除");
        }
        
        categoryService.deleteById(id);
        return ApiResponse.success("删除成功");
    }

    /**
     * 批量删除规格分类（软删除）
     * @param ids 分类ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('product-specification-management:delete')")
    public ApiResponse<String> batchDeleteCategories(@RequestBody List<Long> ids) {
        // 检查是否存在被使用的分类
        for (Long id : ids) {
            if (categoryService.isUsedBySpecificationValues(id)) {
                return ApiResponse.failure("存在被规格值使用的分类，无法删除");
            }
        }
        
        categoryService.deleteByIds(ids);
        return ApiResponse.success("批量删除成功");
    }

    /**
     * 启用规格分类
     * @param id 分类ID
     * @return 操作结果
     */
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasAuthority('product-specification-management:edit')")
    public ApiResponse<String> enableCategory(@PathVariable Long id) {
        Optional<ProdSpecificationCategory> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return ApiResponse.failure("规格分类不存在");
        }
        
        categoryService.enableCategory(id);
        return ApiResponse.success("启用成功");
    }

    /**
     * 禁用规格分类
     * @param id 分类ID
     * @return 操作结果
     */
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasAuthority('product-specification-management:edit')")
    public ApiResponse<String> disableCategory(@PathVariable Long id) {
        Optional<ProdSpecificationCategory> category = categoryService.findById(id);
        if (!category.isPresent()) {
            return ApiResponse.failure("规格分类不存在");
        }
        
        categoryService.disableCategory(id);
        return ApiResponse.success("禁用成功");
    }



    /**
     * 检查分类名称是否存在
     * @param categoryName 分类名称
     * @return 检查结果
     */
    @GetMapping("/check-name")
    public ApiResponse<Boolean> checkCategoryName(@RequestParam String categoryName) {
        boolean exists = categoryService.existsByCategoryName(categoryName);
        return ApiResponse.success(exists);
    }
} 