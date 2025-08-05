package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.SpecificationDTO;
import com.example.vliascrm.service.ProdSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
     */
    @GetMapping
    public ApiResponse<Page<SpecificationDTO>> getSpecifications(
            @RequestParam(required = false) String specificationName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<SpecificationDTO> result = specificationService.findSpecificationsWithConditions(specificationName, status, pageable);
        return ApiResponse.success(result);
    }
    
    /**
     * 根据ID获取规格详情
     */
    @GetMapping("/{id}")
    public ApiResponse<SpecificationDTO> getSpecificationById(@PathVariable Long id) {
        SpecificationDTO specification = specificationService.getSpecificationById(id);
        return ApiResponse.success(specification);
    }
    
    /**
     * 创建规格
     */
    @PostMapping
    public ApiResponse<SpecificationDTO> createSpecification(@RequestBody SpecificationDTO specificationDTO) {
        SpecificationDTO result = specificationService.createSpecification(specificationDTO);
        return ApiResponse.success(result);
    }
    
    /**
     * 更新规格
     */
    @PutMapping("/{id}")
    public ApiResponse<SpecificationDTO> updateSpecification(@PathVariable Long id, @RequestBody SpecificationDTO specificationDTO) {
        SpecificationDTO result = specificationService.updateSpecification(id, specificationDTO);
        return ApiResponse.success(result);
    }
    
    /**
     * 删除规格
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSpecification(@PathVariable Long id) {
        specificationService.deleteSpecification(id);
        return ApiResponse.success(null);
    }
    
    /**
     * 批量删除规格
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> batchDeleteSpecifications(@RequestBody List<Long> ids) {
        specificationService.batchDeleteSpecifications(ids);
        return ApiResponse.success(null);
    }
    
    /**
     * 获取所有启用的规格
     */
    @GetMapping("/enabled")
    public ApiResponse<List<SpecificationDTO>> getEnabledSpecifications() {
        List<SpecificationDTO> specifications = specificationService.getEnabledSpecifications();
        return ApiResponse.success(specifications);
    }
    
    /**
     * 检查规格编码是否存在
     */
    @GetMapping("/check-code")
    public ApiResponse<Boolean> checkSpecificationCode(
            @RequestParam String specificationCode,
            @RequestParam(required = false) Long excludeId) {
        boolean exists = specificationService.existsBySpecificationCode(specificationCode, excludeId);
        return ApiResponse.success(exists);
    }
} 