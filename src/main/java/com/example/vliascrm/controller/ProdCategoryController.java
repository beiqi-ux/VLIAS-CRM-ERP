package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdCategory;
import com.example.vliascrm.service.ProdCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商品分类管理控制器
 */
@Slf4j
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
    @PreAuthorize("hasAuthority('product-category-management:view')")
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
    @PreAuthorize("hasAuthority('product-category-management:view')")
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
    @PreAuthorize("hasAuthority('product-category-management:view')")
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
    @PreAuthorize("hasAuthority('product-category-management:view')")
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
    @PreAuthorize("hasAuthority('product-category-management:view')")
    public ApiResponse<List<ProdCategory>> getCategoriesByLevel(@PathVariable Integer level) {
        List<ProdCategory> categories = prodCategoryService.findByLevel(level);
        return ApiResponse.success(categories);
    }

    /**
     * 获取显示的分類
     * @return 显示的分类列表
     */
    @GetMapping("/visible")
    @PreAuthorize("hasAuthority('product-category-management:view')")
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
    @PreAuthorize("hasAuthority('product-category-management:view')")
    public ApiResponse<List<ProdCategory>> searchCategories(@RequestParam String categoryName) {
        List<ProdCategory> categories = prodCategoryService.findByCategoryNameContaining(categoryName);
        return ApiResponse.success(categories);
    }

    /**
     * 构建分类树
     * @return 分类树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('product-category-management:view')")
    public ApiResponse<List<ProdCategory>> getCategoryTree() {
        List<ProdCategory> categoryTree = prodCategoryService.buildCategoryTree();
        return ApiResponse.success(categoryTree);
    }

    /**
     * 构建管理后台分类树（包括禁用状态）
     * @return 分类树
     */
    @GetMapping("/admin-tree")
    @PreAuthorize("hasAuthority('product-category-management:view')")
    public ApiResponse<List<ProdCategory>> getAdminCategoryTree() {
        List<ProdCategory> categoryTree = prodCategoryService.buildAdminCategoryTree();
        
        // 调试日志：打印树结构
        log.info("管理后台分类树根节点数量: {}", categoryTree.size());
        for (ProdCategory root : categoryTree) {
            logCategoryTree(root, 0);
        }
        
        // 确保每个节点都有children字段
        ensureChildrenField(categoryTree);
        
        // 调试：检查第一个根节点的children字段
        if (!categoryTree.isEmpty()) {
            ProdCategory firstRoot = categoryTree.get(0);
            log.info("第一个根节点 {} 的children字段: {}, 大小: {}", 
                    firstRoot.getCategoryName(), 
                    firstRoot.getChildren() != null ? "存在" : "null",
                    firstRoot.getChildren() != null ? firstRoot.getChildren().size() : "N/A");
        }
        
        return ApiResponse.success(categoryTree);
    }
    
    /**
     * 确保每个分类节点都有children字段，即使为空
     */
    private void ensureChildrenField(List<ProdCategory> categories) {
        if (categories == null) return;
        
        for (ProdCategory category : categories) {
            if (category.getChildren() == null) {
                category.setChildren(new ArrayList<>());
            }
            ensureChildrenField(category.getChildren());
        }
    }
    
    /**
     * 递归打印分类树结构（用于调试）
     */
    private void logCategoryTree(ProdCategory category, int level) {
        String indent = "  ".repeat(level);
        log.info("{}分类: {} (ID: {}, children数量: {})", 
                indent, category.getCategoryName(), category.getId(), 
                category.getChildren() != null ? category.getChildren().size() : "null");
        
        if (category.getChildren() != null) {
            for (ProdCategory child : category.getChildren()) {
                logCategoryTree(child, level + 1);
            }
        }
    }

    /**
     * 调试分类树构建
     * @return 分类树
     */
    @GetMapping("/debug-tree")
    @PreAuthorize("hasAuthority('product-category-management:view')")
    public ApiResponse<List<ProdCategory>> debugCategoryTree() {
        // 获取根分类
        List<ProdCategory> rootCategories = prodCategoryService.findRootCategories();
        System.out.println("Root categories count: " + rootCategories.size());
        
        // 为每个根分类手动构建子树
        for (ProdCategory root : rootCategories) {
            System.out.println("Processing root category: " + root.getCategoryName() + " (ID: " + root.getId() + ")");
            
            // 查找子分类
            List<ProdCategory> children = prodCategoryService.findByParentId(root.getId());
            System.out.println("Found " + children.size() + " children for category " + root.getId());
            
            root.setChildren(children);
            
            // 为子分类查找孙分类
            for (ProdCategory child : children) {
                System.out.println("Processing child category: " + child.getCategoryName() + " (ID: " + child.getId() + ")");
                List<ProdCategory> grandChildren = prodCategoryService.findByParentId(child.getId());
                System.out.println("Found " + grandChildren.size() + " grandchildren for category " + child.getId());
                child.setChildren(grandChildren);
            }
        }
        
        return ApiResponse.success(rootCategories);
    }

    /**
     * 创建分类
     * @param category 分类信息
     * @return 创建后的分类
     */
    @PostMapping
    @PreAuthorize("hasAuthority('product-category-management:create')")
    public ApiResponse<ProdCategory> createCategory(@RequestBody ProdCategory category) {
        // 检查分类名称是否存在（同级下）
        Long parentId = category.getParentId();
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
    @PreAuthorize("hasAuthority('product-category-management:edit')")
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
    @PreAuthorize("hasAuthority('product-category-management:delete')")
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
    @PreAuthorize("hasAuthority('product-category-management:delete')")
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
    @PreAuthorize("hasAuthority('product-category-management:edit')")
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
    @PreAuthorize("hasAuthority('product-category-management:edit')")
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
    @PreAuthorize("hasAuthority('product-category-management:edit')")
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
    @PreAuthorize("hasAuthority('product-category-management:edit')")
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
    @PreAuthorize("hasAuthority('product-category-management:view')")
    public ApiResponse<Boolean> checkCategoryName(@RequestParam String categoryName, 
                                                 @RequestParam(required = false) Long parentId) {
        boolean exists = prodCategoryService.existsByCategoryNameAndParentId(categoryName, parentId);
        return ApiResponse.success(exists);
    }

    /**
     * 统计父级下的子分类数量
     * @param parentId 父级ID
     * @return 子分类数量
     */
    @GetMapping("/count/parent/{parentId}")
    @PreAuthorize("hasAuthority('product-category-management:view')")
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
    @PreAuthorize("hasAuthority('product-category-management:view')")
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
    @PreAuthorize("hasAuthority('product-category-management:view')")
    public ApiResponse<List<Long>> getAllChildCategoryIds(@PathVariable Long parentId) {
        List<Long> allChildIds = prodCategoryService.findAllChildCategoryIds(parentId);
        return ApiResponse.success(allChildIds);
    }
} 