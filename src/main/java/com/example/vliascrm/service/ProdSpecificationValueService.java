package com.example.vliascrm.service;

import com.example.vliascrm.entity.ProdSpecificationValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 商品规格值服务接口
 */
public interface ProdSpecificationValueService {

    /**
     * 根据ID获取规格值
     */
    Optional<ProdSpecificationValue> findById(Long id);

    /**
     * 分页查询规格值
     */
    Page<ProdSpecificationValue> findAll(Pageable pageable);

    /**
     * 根据条件分页查询规格值
     */
    Page<ProdSpecificationValue> findByConditions(Pageable pageable, Long itemId, String valueName, Integer status);

    /**
     * 根据规格项ID查询规格值列表
     */
    List<ProdSpecificationValue> findByItemId(Long itemId);

    /**
     * 根据规格项ID查询启用的规格值列表
     */
    List<ProdSpecificationValue> findActiveByItemId(Long itemId);

    /**
     * 根据规格值名称模糊查询
     */
    List<ProdSpecificationValue> findByValueNameContaining(String valueName);

    /**
     * 根据ID列表查询规格值
     */
    List<ProdSpecificationValue> findByIds(List<Long> ids);

    /**
     * 保存规格值
     */
    ProdSpecificationValue save(ProdSpecificationValue specValue);

    /**
     * 更新规格值
     */
    ProdSpecificationValue update(ProdSpecificationValue specValue);

    /**
     * 删除规格值
     */
    void deleteById(Long id);

    /**
     * 批量删除规格值
     */
    void batchDelete(List<Long> ids);

    /**
     * 启用规格值
     */
    void enableById(Long id);

    /**
     * 禁用规格值
     */
    void disableById(Long id);

    /**
     * 检查规格值代码是否存在
     */
    boolean existsByValueCode(Long itemId, String valueCode, Long excludeId);

    /**
     * 检查规格值名称是否存在
     */
    boolean existsByValueName(Long itemId, String valueName, Long excludeId);

    /**
     * 获取所有启用的规格值
     */
    List<ProdSpecificationValue> findAllActive();

    /**
     * 根据规格项ID统计规格值数量
     */
    long countByItemId(Long itemId);
} 