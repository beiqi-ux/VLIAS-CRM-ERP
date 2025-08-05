package com.example.vliascrm.service;

import com.example.vliascrm.dto.SpecificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品规格服务接口
 */
public interface ProdSpecificationService {
    
    /**
     * 分页查询规格列表
     */
    Page<SpecificationDTO> findSpecificationsWithConditions(String specificationName, Integer status, Pageable pageable);
    
    /**
     * 根据ID获取规格详情
     */
    SpecificationDTO getSpecificationById(Long id);
    
    /**
     * 创建规格
     */
    SpecificationDTO createSpecification(SpecificationDTO specificationDTO);
    
    /**
     * 更新规格
     */
    SpecificationDTO updateSpecification(Long id, SpecificationDTO specificationDTO);
    
    /**
     * 删除规格
     */
    void deleteSpecification(Long id);
    
    /**
     * 批量删除规格
     */
    void batchDeleteSpecifications(List<Long> ids);
    
    /**
     * 获取所有启用的规格
     */
    List<SpecificationDTO> getEnabledSpecifications();
    
    /**
     * 检查规格编码是否存在
     */
    boolean existsBySpecificationCode(String specificationCode, Long excludeId);
} 