package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.PurSupplierDto;
import com.example.vliascrm.service.PurSupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商管理控制器
 * 权限编码遵循3级结构：purchase > supplier-management > supplier-management:operation
 */
@Tag(name = "供应商管理", description = "采购管理-供应商管理相关接口")
@RestController
@RequestMapping("/api/purchase/suppliers")
public class PurSupplierController {

    @Autowired
    private PurSupplierService supplierService;

    /**
     * 分页查询供应商列表
     * 权限编码：supplier-management:view
     */
    @Operation(summary = "分页查询供应商列表")
    @GetMapping
    @PreAuthorize("hasAuthority('supplier-management:view')")
    public ApiResponse<Page<PurSupplierDto>> getSupplierPage(
            @Parameter(description = "供应商名称") @RequestParam(required = false) String supplierName,
            @Parameter(description = "联系人") @RequestParam(required = false) String contact,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "供应商类型") @RequestParam(required = false) Integer supplierType,
            @Parameter(description = "排序字段") @RequestParam(required = false) String sortBy,
            @Parameter(description = "排序方向") @RequestParam(required = false) String sortDirection,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        
        // 构建排序
        Sort sort = Sort.by(Sort.Direction.ASC, "supplierCode", "id"); // 默认排序
        if (sortBy != null && !sortBy.trim().isEmpty() && sortDirection != null) {
            Sort.Direction direction = "DESC".equalsIgnoreCase(sortDirection) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortBy);
        }
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurSupplierDto> supplierPage = supplierService.getSupplierPage(supplierName, contact, status, supplierType, pageable);
        return ApiResponse.success(supplierPage);
    }

    /**
     * 根据ID查询供应商详情
     * 权限编码：supplier-management:view
     */
    @Operation(summary = "根据ID查询供应商详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('supplier-management:view')")
    public ApiResponse<PurSupplierDto> getSupplierById(
            @Parameter(description = "供应商ID") @PathVariable Long id) {
        PurSupplierDto supplier = supplierService.getSupplierById(id);
        return ApiResponse.success(supplier);
    }

    /**
     * 创建供应商
     * 权限编码：supplier-management:create
     */
    @Operation(summary = "创建供应商")
    @PostMapping
    @PreAuthorize("hasAuthority('supplier-management:create')")
    public ApiResponse<PurSupplierDto> createSupplier(
            @Parameter(description = "供应商信息") @Valid @RequestBody PurSupplierDto supplierDto) {
        
        PurSupplierDto createdSupplier = supplierService.createSupplier(supplierDto);
        return ApiResponse.success("供应商创建成功", createdSupplier);
    }

    /**
     * 更新供应商
     * 权限编码：supplier-management:edit
     */
    @Operation(summary = "更新供应商")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('supplier-management:edit')")
    public ApiResponse<PurSupplierDto> updateSupplier(
            @Parameter(description = "供应商ID") @PathVariable Long id,
            @Parameter(description = "供应商信息") @Valid @RequestBody PurSupplierDto supplierDto) {
        
        PurSupplierDto updatedSupplier = supplierService.updateSupplier(id, supplierDto);
        return ApiResponse.success("供应商更新成功", updatedSupplier);
    }

    /**
     * 删除供应商
     * 权限编码：supplier-management:delete
     */
    @Operation(summary = "删除供应商")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('supplier-management:delete')")
    public ApiResponse<String> deleteSupplier(
            @Parameter(description = "供应商ID") @PathVariable Long id) {
        
        supplierService.deleteSupplier(id);
        return ApiResponse.success("供应商删除成功");
    }

    /**
     * 批量删除供应商
     * 权限编码：supplier-management:delete
     */
    @Operation(summary = "批量删除供应商")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('supplier-management:delete')")
    public ApiResponse<String> deleteSuppliers(
            @Parameter(description = "供应商ID列表") @RequestBody List<Long> ids) {
        
        supplierService.deleteSuppliers(ids);
        return ApiResponse.success("供应商批量删除成功");
    }

    /**
     * 更新供应商状态
     * 权限编码：supplier-management:edit
     */
    @Operation(summary = "更新供应商状态")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('supplier-management:edit')")
    public ApiResponse<Map<String, Object>> updateSupplierStatus(
            @Parameter(description = "供应商ID") @PathVariable Long id,
            @Parameter(description = "状态") @RequestBody Map<String, Integer> statusMap) {
        
        Integer status = statusMap.get("status");
        supplierService.updateSupplierStatus(id, status);
        
        // 返回更新后的状态信息
        Map<String, Object> result = new HashMap<>();
        result.put("status", status);
        return ApiResponse.success("供应商状态更新成功", result);
    }

    /**
     * 查询所有正常状态的供应商
     * 权限编码：supplier-management:view
     */
    @Operation(summary = "查询所有正常状态的供应商")
    @GetMapping("/active")
    @PreAuthorize("hasAuthority('supplier-management:view')")
    public ApiResponse<List<PurSupplierDto>> getAllActiveSuppliers() {
        List<PurSupplierDto> suppliers = supplierService.getAllActiveSuppliers();
        return ApiResponse.success(suppliers);
    }

    /**
     * 根据供应商编码查询供应商
     * 权限编码：supplier-management:view
     */
    @GetMapping("/code/{supplierCode}")
    @PreAuthorize("hasAuthority('supplier-management:view')")
    public ApiResponse<PurSupplierDto> getSupplierByCode(
            @Parameter(description = "供应商编码") @PathVariable String supplierCode) {
        
        PurSupplierDto supplier = supplierService.getSupplierByCode(supplierCode);
        return ApiResponse.success(supplier);
    }

    /**
     * 检查供应商编码是否存在
     * 权限编码：supplier-management:view
     */
    @Operation(summary = "检查供应商编码是否存在")
    @GetMapping("/check-code")
    @PreAuthorize("hasAuthority('supplier-management:view')")
    public ApiResponse<Boolean> checkSupplierCode(
            @Parameter(description = "供应商编码") @RequestParam String supplierCode,
            @Parameter(description = "排除的供应商ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = supplierService.isSupplierCodeExists(supplierCode, excludeId);
        return ApiResponse.success(exists);
    }

    /**
     * 检查供应商名称是否存在
     * 权限编码：supplier-management:view
     */
    @Operation(summary = "检查供应商名称是否存在")
    @GetMapping("/check-name")
    @PreAuthorize("hasAuthority('supplier-management:view')")
    public ApiResponse<Boolean> checkSupplierName(
            @Parameter(description = "供应商名称") @RequestParam String supplierName,
            @Parameter(description = "排除的供应商ID") @RequestParam(required = false) Long excludeId) {
        boolean exists = supplierService.isSupplierNameExists(supplierName, excludeId);
        return ApiResponse.success(exists);
    }

    /**
     * 获取供应商统计信息
     * 权限编码：supplier-management:view
     */
    @Operation(summary = "获取供应商统计信息")
    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('supplier-management:view')")
    public ApiResponse<PurSupplierService.SupplierStatistics> getSupplierStatistics() {
        PurSupplierService.SupplierStatistics statistics = supplierService.getSupplierStatistics();
        return ApiResponse.success(statistics);
    }
} 