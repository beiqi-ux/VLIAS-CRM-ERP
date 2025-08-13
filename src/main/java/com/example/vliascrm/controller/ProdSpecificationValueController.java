package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdSpecificationValue;
import com.example.vliascrm.service.ProdSpecificationValueService;
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
 * 商品规格值管理控制器
 */
@RestController
@RequestMapping("/api/prod/spec-values")
@RequiredArgsConstructor
public class ProdSpecificationValueController {

    private final ProdSpecificationValueService valueService;

    /**
     * 获取规格值列表（支持分页）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param itemId 规格项ID（可选）
     * @param valueName 规格值名称（可选）
     * @param status 状态（可选）
     * @return 规格值分页列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<Page<ProdSpecificationValue>> getValueList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) String valueName,
            @RequestParam(required = false) Integer status) {
        
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, 
            Sort.by("itemId").ascending().and(Sort.by("sortOrder").ascending()));
        Page<ProdSpecificationValue> result = valueService.findByConditions(
            pageable, itemId, valueName, status);
        
        return ApiResponse.success(result);
    }

    /**
     * 根据ID获取规格值详情
     * @param id 规格值ID
     * @return 规格值详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<ProdSpecificationValue> getValueById(@PathVariable Long id) {
        Optional<ProdSpecificationValue> value = valueService.findById(id);
        if (value.isPresent()) {
            return ApiResponse.success(value.get());
        } else {
            return ApiResponse.failure("规格值不存在");
        }
    }

    /**
     * 创建规格值
     * @param value 规格值信息
     * @return 创建后的规格值
     */
    @PostMapping
    @PreAuthorize("hasAuthority('product-specification-management:create')")
    public ApiResponse<ProdSpecificationValue> createValue(@RequestBody ProdSpecificationValue value) {
        try {
            // 验证规格值代码是否重复
            if (valueService.existsByValueCode(value.getItemId(), value.getValueCode(), null)) {
                return ApiResponse.failure("规格值代码已存在");
            }
            
            // 验证规格值名称是否重复
            if (valueService.existsByValueName(value.getItemId(), value.getValueName(), null)) {
                return ApiResponse.failure("规格值名称已存在");
            }
            
            ProdSpecificationValue savedValue = valueService.save(value);
            return ApiResponse.success(savedValue);
        } catch (Exception e) {
            return ApiResponse.failure("创建规格值失败: " + e.getMessage());
        }
    }

    /**
     * 更新规格值
     * @param id 规格值ID
     * @param value 规格值信息
     * @return 更新后的规格值
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product-specification-management:edit')")
    public ApiResponse<ProdSpecificationValue> updateValue(@PathVariable Long id, @RequestBody ProdSpecificationValue value) {
        Optional<ProdSpecificationValue> existingValue = valueService.findById(id);
        if (!existingValue.isPresent()) {
            return ApiResponse.failure("规格值不存在");
        }
        
        try {
            // 验证规格值代码是否重复（排除当前记录）
            if (valueService.existsByValueCode(value.getItemId(), value.getValueCode(), id)) {
                return ApiResponse.failure("规格值代码已存在");
            }
            
            // 验证规格值名称是否重复（排除当前记录）
            if (valueService.existsByValueName(value.getItemId(), value.getValueName(), id)) {
                return ApiResponse.failure("规格值名称已存在");
            }
            
            value.setId(id);
            ProdSpecificationValue updatedValue = valueService.update(value);
            return ApiResponse.success(updatedValue);
        } catch (Exception e) {
            return ApiResponse.failure("更新规格值失败: " + e.getMessage());
        }
    }

    /**
     * 删除规格值（软删除）
     * @param id 规格值ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product-specification-management:delete')")
    public ApiResponse<String> deleteValue(@PathVariable Long id) {
        Optional<ProdSpecificationValue> value = valueService.findById(id);
        if (!value.isPresent()) {
            return ApiResponse.failure("规格值不存在");
        }
        
        try {
            valueService.deleteById(id);
            return ApiResponse.success("删除成功");
        } catch (Exception e) {
            return ApiResponse.failure("删除规格值失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除规格值
     * @param ids 规格值ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('product-specification-management:delete')")
    public ApiResponse<String> batchDeleteValues(@RequestBody List<Long> ids) {
        try {
            valueService.batchDelete(ids);
            return ApiResponse.success("批量删除成功");
        } catch (Exception e) {
            return ApiResponse.failure("批量删除规格值失败: " + e.getMessage());
        }
    }

    /**
     * 根据规格项ID获取规格值列表
     * @param itemId 规格项ID
     * @return 规格值列表
     */
    @GetMapping("/item/{itemId}")
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<List<ProdSpecificationValue>> getValuesByItemId(@PathVariable Long itemId) {
        List<ProdSpecificationValue> values = valueService.findByItemId(itemId);
        return ApiResponse.success(values);
    }

    /**
     * 获取启用的规格值列表
     * @param itemId 规格项ID（可选）
     * @return 启用的规格值列表
     */
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<List<ProdSpecificationValue>> getActiveValues(@RequestParam(required = false) Long itemId) {
        List<ProdSpecificationValue> values;
        if (itemId != null) {
            values = valueService.findActiveByItemId(itemId);
        } else {
            values = valueService.findAllActive();
        }
        return ApiResponse.success(values);
    }
} 