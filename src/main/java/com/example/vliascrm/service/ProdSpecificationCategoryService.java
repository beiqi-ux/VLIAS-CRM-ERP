package com.example.vliascrm.service;

import com.example.vliascrm.entity.ProdSpecificationCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 商品规格分类服务接口
 */
public interface ProdSpecificationCategoryService {

    /**
     * 根据ID查询规格分类
     * @param id 分类ID
     * @return 规格分类对象
     */
    Optional<ProdSpecificationCategory> findById(Long id);



    /**
     * 查询所有规格分类（分页）
     * @param pageable 分页参数
     * @return 规格分类分页列表
     */
    Page<ProdSpecificationCategory> findAll(Pageable pageable);

    /**
     * 根据条件查询规格分类（分页）
     * @param pageable 分页参数
     * @param categoryName 分类名称（可选）
     * @param status 状态（可选）
     * @return 规格分类分页列表
     */
    Page<ProdSpecificationCategory> findByConditions(Pageable pageable, String categoryName, Integer status);

    /**
     * 查询所有启用的规格分类
     * @return 启用的规格分类列表
     */
    List<ProdSpecificationCategory> findActiveCategories();

    /**
     * 根据分类名称模糊查询
     * @param categoryName 分类名称
     * @return 规格分类列表
     */
    List<ProdSpecificationCategory> findByCategoryNameContaining(String categoryName);

    /**
     * 保存规格分类
     * @param category 规格分类对象
     * @return 保存后的规格分类对象
     */
    ProdSpecificationCategory save(ProdSpecificationCategory category);

    /**
     * 更新规格分类
     * @param category 规格分类对象
     * @return 更新后的规格分类对象
     */
    ProdSpecificationCategory update(ProdSpecificationCategory category);

    /**
     * 删除规格分类（软删除）
     * @param id 分类ID
     */
    void deleteById(Long id);

    /**
     * 批量删除规格分类（软删除）
     * @param ids 分类ID列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 启用规格分类
     * @param id 分类ID
     */
    void enableCategory(Long id);

    /**
     * 禁用规格分类
     * @param id 分类ID
     */
    void disableCategory(Long id);



    /**
     * 检查分类名称是否存在
     * @param categoryName 分类名称
     * @return 是否存在
     */
    boolean existsByCategoryName(String categoryName);

    /**
     * 检查分类名称是否存在（排除指定ID）
     * @param categoryName 分类名称
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByCategoryNameAndIdNot(String categoryName, Long excludeId);

    /**
     * 检查分类是否被规格值使用
     * @param categoryId 分类ID
     * @return 是否被使用
     */
    boolean isUsedBySpecificationValues(Long categoryId);
} 