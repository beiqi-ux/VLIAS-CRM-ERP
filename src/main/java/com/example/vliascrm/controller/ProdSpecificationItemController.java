package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdSpecificationItem;
import com.example.vliascrm.service.ProdSpecificationItemService;
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
 * 商品规格项管理控制器
 */
@RestController
@RequestMapping("/api/prod/spec-items")
@RequiredArgsConstructor
public class ProdSpecificationItemController {

    private final ProdSpecificationItemService itemService;

    /**
     * 获取规格项列表（支持分页）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param categoryId 分类ID（可选）
     * @param itemName 规格项名称（可选）
     * @param status 状态（可选）
     * @return 规格项分页列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<Page<ProdSpecificationItem>> getItemList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String itemName,
            @RequestParam(required = false) Integer status) {
        
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("sort").ascending());
        Page<ProdSpecificationItem> result = itemService.findByConditions(
            pageable, categoryId, itemName, status);
        
        return ApiResponse.success(result);
    }

    /**
     * 根据分类ID获取规格项列表
     */
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ProdSpecificationItem>> getItemsByCategoryId(@PathVariable Long categoryId) {
        List<ProdSpecificationItem> items = itemService.findByCategoryId(categoryId);
        return ApiResponse.success(items);
    }

    /**
     * 获取启用的规格项列表
     */
    @GetMapping("/active")
    public ApiResponse<List<ProdSpecificationItem>> getActiveItems(@RequestParam(required = false) Long categoryId) {
        List<ProdSpecificationItem> items;
        if (categoryId != null) {
            items = itemService.findActiveByCategoryId(categoryId);
        } else {
            items = itemService.findAllActive();
        }
        return ApiResponse.success(items);
    }

    /**
     * 根据ID获取规格项详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ProdSpecificationItem> getItemById(@PathVariable Long id) {
        Optional<ProdSpecificationItem> item = itemService.findById(id);
        if (item.isPresent()) {
            return ApiResponse.success(item.get());
        } else {
            return ApiResponse.failure("规格项不存在");
        }
    }

    /**
     * 创建规格项
     */
    @PostMapping
    public ApiResponse<ProdSpecificationItem> createItem(@RequestBody ProdSpecificationItem item) {
        try {
            // 验证规格项代码是否重复
            if (itemService.existsByItemCode(item.getCategoryId(), item.getItemCode(), null)) {
                return ApiResponse.failure("规格项代码已存在");
            }
            
            // 验证规格项名称是否重复
            if (itemService.existsByItemName(item.getCategoryId(), item.getItemName(), null)) {
                return ApiResponse.failure("规格项名称已存在");
            }
            
            ProdSpecificationItem savedItem = itemService.save(item);
            return ApiResponse.success(savedItem);
        } catch (Exception e) {
            return ApiResponse.failure("创建失败: " + e.getMessage());
        }
    }

    /**
     * 更新规格项
     */
    @PutMapping("/{id}")
    public ApiResponse<ProdSpecificationItem> updateItem(@PathVariable Long id, @RequestBody ProdSpecificationItem item) {
        try {
            Optional<ProdSpecificationItem> existingItem = itemService.findById(id);
            if (existingItem.isEmpty()) {
                return ApiResponse.failure("规格项不存在");
            }

            // 验证规格项代码是否重复（排除当前记录）
            if (itemService.existsByItemCode(item.getCategoryId(), item.getItemCode(), id)) {
                return ApiResponse.failure("规格项代码已存在");
            }
            
            // 验证规格项名称是否重复（排除当前记录）
            if (itemService.existsByItemName(item.getCategoryId(), item.getItemName(), id)) {
                return ApiResponse.failure("规格项名称已存在");
            }

            item.setId(id);
            ProdSpecificationItem updatedItem = itemService.update(item);
            return ApiResponse.success(updatedItem);
        } catch (Exception e) {
            return ApiResponse.failure("更新失败: " + e.getMessage());
        }
    }

    /**
     * 删除规格项
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteItem(@PathVariable Long id) {
        try {
            Optional<ProdSpecificationItem> item = itemService.findById(id);
            if (item.isEmpty()) {
                return ApiResponse.failure("规格项不存在");
            }

            itemService.deleteById(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.failure("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除规格项
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> batchDeleteItems(@RequestBody List<Long> ids) {
        try {
            if (ids == null || ids.isEmpty()) {
                return ApiResponse.failure("请选择要删除的规格项");
            }

            itemService.batchDelete(ids);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.failure("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 启用规格项
     */
    @PutMapping("/{id}/enable")
    public ApiResponse<Void> enableItem(@PathVariable Long id) {
        try {
            Optional<ProdSpecificationItem> item = itemService.findById(id);
            if (item.isEmpty()) {
                return ApiResponse.failure("规格项不存在");
            }

            itemService.enableById(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.failure("启用失败: " + e.getMessage());
        }
    }

    /**
     * 禁用规格项
     */
    @PutMapping("/{id}/disable")
    public ApiResponse<Void> disableItem(@PathVariable Long id) {
        try {
            Optional<ProdSpecificationItem> item = itemService.findById(id);
            if (item.isEmpty()) {
                return ApiResponse.failure("规格项不存在");
            }

            itemService.disableById(id);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.failure("禁用失败: " + e.getMessage());
        }
    }

    /**
     * 根据分类ID统计规格项数量
     */
    @GetMapping("/count/{categoryId}")
    public ApiResponse<Long> countByCategoryId(@PathVariable Long categoryId) {
        long count = itemService.countByCategoryId(categoryId);
        return ApiResponse.success(count);
    }
} 