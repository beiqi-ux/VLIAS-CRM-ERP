package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.SpecificationDTO;
import com.example.vliascrm.service.ProdSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品规格管理Controller
 */
@RestController
@RequestMapping("/api/prod/specifications")
@RequiredArgsConstructor
public class ProdSpecificationController {
    
    private final ProdSpecificationService specificationService;
    
    /**
     * 分页查询规格列表
     * @param page 页码
     * @param size 页大小
     * @param goodsId 商品ID（可选）
     * @param specificationName 规格名称（可选，模糊查询）
     * @return 分页规格列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<Page<SpecificationDTO>> getSpecifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String specificationName,
            @RequestParam(required = false) Integer status) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<SpecificationDTO> specifications = specificationService.findSpecificationsWithConditions(
                specificationName, status, pageable);
        return ApiResponse.success(specifications);
    }

    /**
     * 根据ID获取规格详情
     * @param id 规格ID
     * @return 规格详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<SpecificationDTO> getSpecificationById(@PathVariable Long id) {
        SpecificationDTO specification = specificationService.getSpecificationById(id);
        return ApiResponse.success(specification);
    }

    /**
     * 创建规格
     * @param specificationDTO 规格信息
     * @return 创建后的规格
     */
    @PostMapping
    @PreAuthorize("hasAuthority('product-specification-management:create')")
    public ApiResponse<SpecificationDTO> createSpecification(@RequestBody SpecificationDTO specificationDTO) {
        SpecificationDTO result = specificationService.createSpecification(specificationDTO);
        return ApiResponse.success(result);
    }

    /**
     * 更新规格
     * @param id 规格ID
     * @param specificationDTO 规格信息
     * @return 更新后的规格
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product-specification-management:edit')")
    public ApiResponse<SpecificationDTO> updateSpecification(@PathVariable Long id, @RequestBody SpecificationDTO specificationDTO) {
        SpecificationDTO result = specificationService.updateSpecification(id, specificationDTO);
        return ApiResponse.success(result);
    }

    /**
     * 删除规格
     * @param id 规格ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product-specification-management:delete')")
    public ApiResponse<Void> deleteSpecification(@PathVariable Long id) {
        specificationService.deleteSpecification(id);
        return ApiResponse.success(null);
    }

    /**
     * 批量删除规格
     * @param ids 规格ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('product-specification-management:delete')")
    public ApiResponse<Void> deleteSpecifications(@RequestBody List<Long> ids) {
        specificationService.batchDeleteSpecifications(ids);
        return ApiResponse.success(null);
    }

    /**
     * 获取所有启用的规格
     * @return 启用的规格列表
     */
    @GetMapping("/enabled")
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<List<SpecificationDTO>> getEnabledSpecifications() {
        List<SpecificationDTO> specifications = specificationService.getEnabledSpecifications();
        return ApiResponse.success(specifications);
    }

    /**
     * 根据商品ID获取规格列表
     * @param goodsId 商品ID
     * @return 规格列表
     */
    @GetMapping("/goods/{goodsId}")
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<List<SpecificationDTO>> getSpecificationsByGoodsId(@PathVariable Long goodsId) {
        List<SpecificationDTO> specifications = specificationService.findByGoodsId(goodsId);
        return ApiResponse.success(specifications);
    }

    /**
     * 根据分类ID获取规格列表
     * @param categoryId 分类ID
     * @param subCategory 子分类（可选）
     * @return 规格列表
     */
    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAuthority('product-specification-management:view')")
    public ApiResponse<List<SpecificationDTO>> getSpecificationsByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(required = false) String subCategory) {
        List<SpecificationDTO> specifications = specificationService.findByCategoryId(categoryId, subCategory);
        return ApiResponse.success(specifications);
    }

    /**
     * 检查规格编码是否存在
     * @param specificationCode 规格编码
     * @param excludeId 排除的ID（编辑时使用）
     * @return 是否存在
     */
    @GetMapping("/check-code")
    public ApiResponse<Boolean> checkSpecificationCode(@RequestParam String specificationCode, 
                                                       @RequestParam(required = false) Long excludeId) {
        boolean exists = specificationService.existsBySpecificationCode(specificationCode, excludeId);
        return ApiResponse.success(exists);
    }
} 