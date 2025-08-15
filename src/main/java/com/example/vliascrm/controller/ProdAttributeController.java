package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.AttributeDTO;
import com.example.vliascrm.service.ProdAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品属性管理Controller
 */
@RestController
@RequestMapping("/api/prod/attributes")
@RequiredArgsConstructor
public class ProdAttributeController {
    
    private final ProdAttributeService attributeService;
    
    /**
     * 分页查询属性列表
     */
    @GetMapping
    public ApiResponse<Page<AttributeDTO>> getAttributes(
            @RequestParam(required = false) String attributeName,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<AttributeDTO> result = attributeService.findAttributesWithConditions(attributeName, status, pageable);
        return ApiResponse.success(result);
    }
    
    /**
     * 根据ID获取属性详情
     */
    @GetMapping("/{id}")
    public ApiResponse<AttributeDTO> getAttributeById(@PathVariable Long id) {
        AttributeDTO attribute = attributeService.getAttributeById(id);
        return ApiResponse.success(attribute);
    }
    
    /**
     * 创建属性
     */
    @PostMapping
    public ApiResponse<AttributeDTO> createAttribute(@RequestBody AttributeDTO attributeDTO) {
        AttributeDTO result = attributeService.createAttribute(attributeDTO);
        return ApiResponse.success(result);
    }
    
    /**
     * 更新属性
     */
    @PutMapping("/{id}")
    public ApiResponse<AttributeDTO> updateAttribute(@PathVariable Long id, @RequestBody AttributeDTO attributeDTO) {
        AttributeDTO result = attributeService.updateAttribute(id, attributeDTO);
        return ApiResponse.success(result);
    }
    
    /**
     * 删除属性
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteAttribute(@PathVariable Long id) {
        attributeService.deleteAttribute(id);
        return ApiResponse.success(null);
    }
    
    /**
     * 批量删除属性
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> batchDeleteAttributes(@RequestBody List<Long> ids) {
        attributeService.batchDeleteAttributes(ids);
        return ApiResponse.success(null);
    }
    
    /**
     * 获取所有启用的属性
     */
    @GetMapping("/enabled")
    public ApiResponse<List<AttributeDTO>> getEnabledAttributes() {
        List<AttributeDTO> attributes = attributeService.getEnabledAttributes();
        return ApiResponse.success(attributes);
    }
    
    /**
     * 检查属性编码是否存在
     */
    @GetMapping("/check-code")
    public ApiResponse<Boolean> checkAttributeCode(
            @RequestParam String attributeCode,
            @RequestParam(required = false) Long excludeId) {
        boolean exists = attributeService.existsByAttributeCode(attributeCode, excludeId);
        return ApiResponse.success(exists);
    }
} 