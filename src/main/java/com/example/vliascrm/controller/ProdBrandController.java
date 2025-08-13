package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdBrand;
import com.example.vliascrm.service.ProdBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 商品品牌管理控制器
 */
@RestController
@RequestMapping("/api/prod/brands")
@RequiredArgsConstructor
public class ProdBrandController {

    private final ProdBrandService prodBrandService;

    /**
     * 获取品牌列表（支持分页）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param brandName 品牌名称（可选）
     * @return 品牌分页列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('product-brand-management:view')")
    public ApiResponse<Page<ProdBrand>> getBrandList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String brandName) {
        
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("sort").ascending().and(Sort.by("createTime").descending()));
        Page<ProdBrand> page = prodBrandService.findAll(pageable);
        
        return ApiResponse.success(page);
    }

    /**
     * 获取所有品牌（不分页）
     * @return 品牌列表
     */
    @GetMapping("/all")
    public ApiResponse<List<ProdBrand>> getAllBrands() {
        List<ProdBrand> brands = prodBrandService.findAll();
        return ApiResponse.success(brands);
    }

    /**
     * 根据ID获取品牌详情
     * @param id 品牌ID
     * @return 品牌详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product-brand-management:view')")
    public ApiResponse<ProdBrand> getBrandById(@PathVariable Long id) {
        Optional<ProdBrand> brand = prodBrandService.findById(id);
        if (brand.isPresent()) {
            return ApiResponse.success(brand.get());
        }
        return ApiResponse.failure("品牌不存在");
    }

    /**
     * 根据品牌名称获取品牌
     * @param brandName 品牌名称
     * @return 品牌信息
     */
    @GetMapping("/name/{brandName}")
    public ApiResponse<ProdBrand> getBrandByName(@PathVariable String brandName) {
        Optional<ProdBrand> brand = prodBrandService.findByBrandName(brandName);
        if (brand.isPresent()) {
            return ApiResponse.success(brand.get());
        }
        return ApiResponse.failure("品牌不存在");
    }

    /**
     * 获取正常状态的品牌
     * @return 正常品牌列表
     */
    @GetMapping("/active")
    public ApiResponse<List<ProdBrand>> getActiveBrands() {
        List<ProdBrand> brands = prodBrandService.findActivesBrands();
        return ApiResponse.success(brands);
    }

    /**
     * 搜索品牌
     * @param brandName 品牌名称
     * @return 品牌列表
     */
    @GetMapping("/search")
    public ApiResponse<List<ProdBrand>> searchBrands(@RequestParam String brandName) {
        List<ProdBrand> brands = prodBrandService.findByBrandNameContaining(brandName);
        return ApiResponse.success(brands);
    }

    /**
     * 创建品牌
     * @param brand 品牌信息
     * @return 创建后的品牌
     */
    @PostMapping
    @PreAuthorize("hasAuthority('product-brand-management:create')")
    public ApiResponse<ProdBrand> createBrand(@RequestBody ProdBrand brand) {
        // 检查品牌名称是否存在
        if (prodBrandService.existsByBrandName(brand.getBrandName())) {
            return ApiResponse.failure("品牌名称已存在");
        }
        
        // 验证status字段
        if (brand.getStatus() != null && brand.getStatus() != 0 && brand.getStatus() != 1) {
            return ApiResponse.failure("状态值无效，只能是0（禁用）或1（正常）");
        }
        
        ProdBrand savedBrand = prodBrandService.save(brand);
        return ApiResponse.success(savedBrand);
    }

    /**
     * 更新品牌
     * @param id 品牌ID
     * @param brand 品牌信息
     * @return 更新后的品牌
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product-brand-management:edit')")
    public ApiResponse<ProdBrand> updateBrand(@PathVariable Long id, @RequestBody ProdBrand brand) {
        Optional<ProdBrand> existingBrand = prodBrandService.findById(id);
        if (!existingBrand.isPresent()) {
            return ApiResponse.failure("品牌不存在或已被删除");
        }
        
        // 检查品牌名称是否与其他品牌重复（排除当前品牌）
        Optional<ProdBrand> brandWithSameName = prodBrandService.findByBrandName(brand.getBrandName());
        if (brandWithSameName.isPresent() && !brandWithSameName.get().getId().equals(id)) {
            return ApiResponse.failure("品牌名称已存在");
        }
        
        // 验证status字段
        if (brand.getStatus() != null && brand.getStatus() != 0 && brand.getStatus() != 1) {
            return ApiResponse.failure("状态值无效，只能是0（禁用）或1（正常）");
        }
        
        brand.setId(id);
        ProdBrand updatedBrand = prodBrandService.update(brand);
        return ApiResponse.success(updatedBrand);
    }

    /**
     * 删除品牌（软删除）
     * @param id 品牌ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product-brand-management:delete')")
    public ApiResponse<String> deleteBrand(@PathVariable Long id) {
        Optional<ProdBrand> brand = prodBrandService.findById(id);
        if (!brand.isPresent()) {
            return ApiResponse.failure("品牌不存在或已被删除");
        }
        
        prodBrandService.deleteById(id);
        return ApiResponse.success("删除成功");
    }

    /**
     * 批量删除品牌（软删除）
     * @param ids 品牌ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public ApiResponse<String> batchDeleteBrands(@RequestBody List<Long> ids) {
        prodBrandService.deleteByIds(ids);
        return ApiResponse.success("批量删除成功");
    }

    /**
     * 启用品牌
     * @param id 品牌ID
     * @return 操作结果
     */
    @PutMapping("/{id}/enable")
    public ApiResponse<String> enableBrand(@PathVariable Long id) {
        Optional<ProdBrand> brand = prodBrandService.findById(id);
        if (!brand.isPresent()) {
            return ApiResponse.failure("品牌不存在或已被删除");
        }
        
        prodBrandService.enable(id);
        return ApiResponse.success("启用成功");
    }

    /**
     * 禁用品牌
     * @param id 品牌ID
     * @return 操作结果
     */
    @PutMapping("/{id}/disable")
    public ApiResponse<String> disableBrand(@PathVariable Long id) {
        Optional<ProdBrand> brand = prodBrandService.findById(id);
        if (!brand.isPresent()) {
            return ApiResponse.failure("品牌不存在或已被删除");
        }
        
        prodBrandService.disable(id);
        return ApiResponse.success("禁用成功");
    }

    /**
     * 检查品牌名称是否存在
     * @param brandName 品牌名称
     * @return 检查结果
     */
    @GetMapping("/check-name")
    public ApiResponse<Boolean> checkBrandName(@RequestParam String brandName) {
        boolean exists = prodBrandService.existsByBrandName(brandName);
        return ApiResponse.success(exists);
    }

    /**
     * 统计品牌数量
     * @param status 状态
     * @return 品牌数量
     */
    @GetMapping("/count")
    public ApiResponse<Long> countBrands(@RequestParam(defaultValue = "1") Integer status) {
        long count = prodBrandService.countByStatus(status);
        return ApiResponse.success(count);
    }
} 