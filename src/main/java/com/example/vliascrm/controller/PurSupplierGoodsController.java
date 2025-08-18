package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.PurSupplierGoodsDto;
import com.example.vliascrm.dto.SupplierPriceCompareDto;
import com.example.vliascrm.service.PurSupplierGoodsService;
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
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 供应商商品管理控制器
 * 权限编码遵循3级结构：purchase > supplier-goods-management > supplier-goods-management:operation
 */
@Tag(name = "供应商商品管理", description = "采购管理-供应商商品关联管理相关接口")
@RestController
@RequestMapping("/api/purchase/supplier-goods")
public class PurSupplierGoodsController {

    @Autowired
    private PurSupplierGoodsService supplierGoodsService;

    /**
     * 分页查询供应商商品列表
     * 权限编码：supplier-goods-management:view
     */
    @Operation(summary = "分页查询供应商商品列表")
    @GetMapping
    @PreAuthorize("hasAuthority('supplier-goods-management:view')")
    public ApiResponse<Page<PurSupplierGoodsDto>> getSupplierGoodsPage(
            @Parameter(description = "供应商ID") @RequestParam(required = false) Long supplierId,
            @Parameter(description = "商品ID") @RequestParam(required = false) Long goodsId,
            @Parameter(description = "供应商商品名称") @RequestParam(required = false) String supplierGoodsName,
            @Parameter(description = "排序字段") @RequestParam(required = false) String sortBy,
            @Parameter(description = "排序方向") @RequestParam(required = false) String sortDirection,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "10") int size) {
        
        // 构建排序
        Sort sort = Sort.unsorted();
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortBy);
        }
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurSupplierGoodsDto> result = supplierGoodsService.getSupplierGoodsPage(
            supplierId, goodsId, supplierGoodsName, pageable);
        
        return ApiResponse.success(result);
    }

    /**
     * 根据ID查询供应商商品详情
     * 权限编码：supplier-goods-management:view
     */
    @Operation(summary = "根据ID查询供应商商品详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('supplier-goods-management:view')")
    public ApiResponse<PurSupplierGoodsDto> getSupplierGoodsById(
            @Parameter(description = "供应商商品ID") @PathVariable Long id) {
        
        PurSupplierGoodsDto result = supplierGoodsService.getSupplierGoodsById(id);
        return ApiResponse.success(result);
    }

    /**
     * 新增供应商商品关联
     * 权限编码：supplier-goods-management:create
     */
    @Operation(summary = "新增供应商商品关联")
    @PostMapping
    @PreAuthorize("hasAuthority('supplier-goods-management:create')")
    public ApiResponse<PurSupplierGoodsDto> createSupplierGoods(
            @Valid @RequestBody PurSupplierGoodsDto dto) {
        
        PurSupplierGoodsDto result = supplierGoodsService.createSupplierGoods(dto);
        return ApiResponse.success(result);
    }

    /**
     * 更新供应商商品关联
     * 权限编码：supplier-goods-management:update
     */
    @Operation(summary = "更新供应商商品关联")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('supplier-goods-management:update')")
    public ApiResponse<PurSupplierGoodsDto> updateSupplierGoods(
            @Parameter(description = "供应商商品ID") @PathVariable Long id,
            @Valid @RequestBody PurSupplierGoodsDto dto) {
        
        PurSupplierGoodsDto result = supplierGoodsService.updateSupplierGoods(id, dto);
        return ApiResponse.success(result);
    }

    /**
     * 删除供应商商品关联
     * 权限编码：supplier-goods-management:delete
     */
    @Operation(summary = "删除供应商商品关联")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('supplier-goods-management:delete')")
    public ApiResponse<String> deleteSupplierGoods(
            @Parameter(description = "供应商商品ID") @PathVariable Long id) {
        
        supplierGoodsService.deleteSupplierGoods(id);
        return ApiResponse.success("删除成功");
    }

    /**
     * 查询供应商的所有商品
     * 权限编码：supplier-goods-management:view
     */
    @Operation(summary = "查询供应商的所有商品")
    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAuthority('supplier-goods-management:view')")
    public ApiResponse<List<PurSupplierGoodsDto>> getGoodsBySupplierId(
            @Parameter(description = "供应商ID") @PathVariable Long supplierId) {
        
        List<PurSupplierGoodsDto> result = supplierGoodsService.getGoodsBySupplierId(supplierId);
        return ApiResponse.success(result);
    }

    /**
     * 查询商品的所有供应商
     * 权限编码：supplier-goods-management:view
     */
    @Operation(summary = "查询商品的所有供应商")
    @GetMapping("/goods/{goodsId}")
    @PreAuthorize("hasAuthority('supplier-goods-management:view')")
    public ApiResponse<List<PurSupplierGoodsDto>> getSuppliersByGoodsId(
            @Parameter(description = "商品ID") @PathVariable Long goodsId) {
        
        List<PurSupplierGoodsDto> result = supplierGoodsService.getSuppliersByGoodsId(goodsId);
        return ApiResponse.success(result);
    }

    /**
     * 价格比较
     * 权限编码：supplier-goods-management:compare
     */
    @Operation(summary = "商品供应商价格比较")
    @GetMapping("/compare/{goodsId}")
    @PreAuthorize("hasAuthority('supplier-goods-management:compare')")
    public ApiResponse<SupplierPriceCompareDto> comparePrice(
            @Parameter(description = "商品ID") @PathVariable Long goodsId) {
        
        SupplierPriceCompareDto result = supplierGoodsService.comparePrice(goodsId);
        return ApiResponse.success(result);
    }

    /**
     * 批量新增供应商商品关联
     * 权限编码：supplier-goods-management:create
     */
    @Operation(summary = "批量新增供应商商品关联")
    @PostMapping("/batch")
    @PreAuthorize("hasAuthority('supplier-goods-management:create')")
    public ApiResponse<Map<String, Object>> batchCreateSupplierGoods(
            @RequestBody List<PurSupplierGoodsDto> dtoList) {
        
        List<PurSupplierGoodsDto> results = supplierGoodsService.batchCreateSupplierGoods(dtoList);
        
        Map<String, Object> result = new HashMap<>();
        result.put("total", dtoList.size());
        result.put("success", results.size());
        result.put("failed", dtoList.size() - results.size());
        result.put("data", results);
        
        return ApiResponse.success(result);
    }

    /**
     * 获取推荐供应商
     * 权限编码：supplier-goods-management:view
     */
    @Operation(summary = "获取推荐供应商")
    @GetMapping("/recommend/{goodsId}")
    @PreAuthorize("hasAuthority('supplier-goods-management:view')")
    public ApiResponse<List<PurSupplierGoodsDto>> getRecommendedSuppliers(
            @Parameter(description = "商品ID") @PathVariable Long goodsId,
            @Parameter(description = "采购数量") @RequestParam(required = false, defaultValue = "1") Integer quantity) {
        
        List<PurSupplierGoodsDto> result = supplierGoodsService.getRecommendedSuppliers(goodsId, quantity);
        return ApiResponse.success(result);
    }

    /**
     * 检查供应商商品关联是否存在
     * 权限编码：supplier-goods-management:view
     */
    @Operation(summary = "检查供应商商品关联是否存在")
    @GetMapping("/exists")
    @PreAuthorize("hasAuthority('supplier-goods-management:view')")
    public ApiResponse<Boolean> checkSupplierGoodsExists(
            @Parameter(description = "供应商ID") @RequestParam Long supplierId,
            @Parameter(description = "商品ID") @RequestParam Long goodsId,
            @Parameter(description = "SKU ID") @RequestParam(required = false) Long skuId) {
        
        boolean exists = supplierGoodsService.checkSupplierGoodsExists(supplierId, goodsId, skuId);
        return ApiResponse.success(exists);
    }

    /**
     * 获取统计信息
     * 权限编码：supplier-goods-management:view
     */
    @Operation(summary = "获取统计信息")
    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('supplier-goods-management:view')")
    public ApiResponse<Map<String, Object>> getStatistics(
            @Parameter(description = "供应商ID") @RequestParam(required = false) Long supplierId,
            @Parameter(description = "商品ID") @RequestParam(required = false) Long goodsId) {
        
        Map<String, Object> statistics = new HashMap<>();
        
        if (supplierId != null) {
            long goodsCount = supplierGoodsService.getGoodsCountBySupplierId(supplierId);
            statistics.put("goodsCount", goodsCount);
        }
        
        if (goodsId != null) {
            long supplierCount = supplierGoodsService.getSupplierCountByGoodsId(goodsId);
            statistics.put("supplierCount", supplierCount);
        }
        
        return ApiResponse.success(statistics);
    }

    /**
     * 搜索供应商商品
     * 权限编码：supplier-goods-management:view
     */
    @Operation(summary = "搜索供应商商品")
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('supplier-goods-management:view')")
    public ApiResponse<List<PurSupplierGoodsDto>> searchSupplierGoods(
            @Parameter(description = "供应商商品名称") @RequestParam String supplierGoodsName,
            @Parameter(description = "供应商ID（可选）") @RequestParam(required = false) Long supplierId) {
        
        List<PurSupplierGoodsDto> result = supplierGoodsService.searchSupplierGoods(supplierGoodsName, supplierId);
        return ApiResponse.success(result);
    }

    /**
     * 导出供应商商品Excel
     * 权限编码：supplier-goods-management:export
     */
    @Operation(summary = "导出供应商商品Excel")
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('supplier-goods-management:export')")
    public void exportSupplierGoods(
            @Parameter(description = "供应商ID") @RequestParam(required = false) Long supplierId,
            @Parameter(description = "商品ID") @RequestParam(required = false) Long goodsId,
            @Parameter(description = "供应商商品名称") @RequestParam(required = false) String supplierGoodsName,
            HttpServletResponse response) throws IOException {
        
        supplierGoodsService.exportSupplierGoods(supplierId, goodsId, supplierGoodsName, response);
    }

    /**
     * 导入供应商商品Excel
     * 权限编码：supplier-goods-management:import
     */
    @Operation(summary = "导入供应商商品Excel")
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('supplier-goods-management:import')")
    public ApiResponse<Map<String, Object>> importSupplierGoods(
            @Parameter(description = "Excel文件") @RequestParam("file") MultipartFile file) throws IOException {
        
        Map<String, Object> result = supplierGoodsService.importSupplierGoods(file);
        return ApiResponse.success(result);
    }

    /**
     * 下载导入模板
     * 权限编码：supplier-goods-management:import
     */
    @Operation(summary = "下载导入模板")
    @GetMapping("/import/template")
    @PreAuthorize("hasAuthority('supplier-goods-management:import')")
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        
        supplierGoodsService.downloadImportTemplate(response);
    }
} 