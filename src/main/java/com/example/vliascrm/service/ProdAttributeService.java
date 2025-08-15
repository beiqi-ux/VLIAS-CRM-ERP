package com.example.vliascrm.service;

import com.example.vliascrm.dto.AttributeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品属性服务接口
 */
public interface ProdAttributeService {
    
    /**
     * 分页查询属性列表
     */
    Page<AttributeDTO> findAttributesWithConditions(String attributeName, Integer status, Pageable pageable);
    
    /**
     * 根据ID获取属性详情
     */
    AttributeDTO getAttributeById(Long id);
    
    /**
     * 创建属性
     */
    AttributeDTO createAttribute(AttributeDTO attributeDTO);
    
    /**
     * 更新属性
     */
    AttributeDTO updateAttribute(Long id, AttributeDTO attributeDTO);
    
    /**
     * 删除属性
     */
    void deleteAttribute(Long id);
    
    /**
     * 批量删除属性
     */
    void batchDeleteAttributes(List<Long> ids);
    
    /**
     * 获取所有启用的属性
     */
    List<AttributeDTO> getEnabledAttributes();
    
    /**
     * 检查属性编码是否存在
     */
    boolean existsByAttributeCode(String attributeCode, Long excludeId);
} 