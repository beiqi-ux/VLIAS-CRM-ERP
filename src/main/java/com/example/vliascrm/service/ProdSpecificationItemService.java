package com.example.vliascrm.service;

import com.example.vliascrm.entity.ProdSpecificationItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 商品规格项服务接口
 */
public interface ProdSpecificationItemService {

    /**
     * 根据ID获取规格项
     */
    Optional<ProdSpecificationItem> findById(Long id);

    /**
     * 分页查询规格项
     */
    Page<ProdSpecificationItem> findAll(Pageable pageable);

    /**
     * 根据条件分页查询规格项
     */
    Page<ProdSpecificationItem> findByConditions(Pageable pageable, Long categoryId, String itemName, Integer status);

    /**
     * 根据分类ID查询规格项列表
     */
    List<ProdSpecificationItem> findByCategoryId(Long categoryId);

    /**
     * 根据分类ID查询启用的规格项列表
     */
    List<ProdSpecificationItem> findActiveByCategoryId(Long categoryId);

    /**
     * 根据规格项名称模糊查询
     */
    List<ProdSpecificationItem> findByItemNameContaining(String itemName);

    /**
     * 根据ID列表查询规格项
     */
    List<ProdSpecificationItem> findByIds(List<Long> ids);

    /**
     * 保存规格项
     */
    ProdSpecificationItem save(ProdSpecificationItem specItem);

    /**
     * 更新规格项
     */
    ProdSpecificationItem update(ProdSpecificationItem specItem);

    /**
     * 删除规格项
     */
    void deleteById(Long id);

    /**
     * 批量删除规格项
     */
    void batchDelete(List<Long> ids);

    /**
     * 启用规格项
     */
    void enableById(Long id);

    /**
     * 禁用规格项
     */
    void disableById(Long id);

    /**
     * 检查规格项代码是否存在
     */
    boolean existsByItemCode(Long categoryId, String itemCode, Long excludeId);

    /**
     * 检查规格项名称是否存在
     */
    boolean existsByItemName(Long categoryId, String itemName, Long excludeId);

    /**
     * 获取所有启用的规格项
     */
    List<ProdSpecificationItem> findAllActive();

    /**
     * 根据分类ID统计规格项数量
     */
    long countByCategoryId(Long categoryId);
} 