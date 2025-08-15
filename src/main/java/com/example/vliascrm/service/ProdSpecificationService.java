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
    
    /**
     * 根据规格分类ID获取规格值列表
     */
    List<SpecificationDTO> findByCategoryId(Long categoryId, String subCategory);
    
    /**
     * 根据商品ID获取规格列表
     */
    List<SpecificationDTO> findByGoodsId(Long goodsId);
    
    /**
     * 根据商品ID和分类ID获取最精准的规格数据
     * 优先返回商品专属规格，其次返回分类通用规格
     */
    List<SpecificationDTO> findOptimalSpecifications(Long goodsId, Long categoryId);
} 