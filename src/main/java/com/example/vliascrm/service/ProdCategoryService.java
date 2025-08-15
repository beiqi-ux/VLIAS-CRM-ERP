package com.example.vliascrm.service;

import com.example.vliascrm.entity.ProdCategory;

import java.util.List;
import java.util.Optional;

/**
 * 商品分类服务接口
 */
public interface ProdCategoryService {

    /**
     * 根据ID查询分类
     * @param id 分类ID
     * @return 分类对象
     */
    Optional<ProdCategory> findById(Long id);

    /**
     * 查询所有分类
     * @return 分类列表
     */
    List<ProdCategory> findAll();

    /**
     * 根据父级ID查询子分类
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<ProdCategory> findByParentId(Long parentId);

    /**
     * 查询根分类（一级分类）
     * @return 根分类列表
     */
    List<ProdCategory> findRootCategories();

    /**
     * 根据层级查询分类
     * @param level 层级
     * @return 分类列表
     */
    List<ProdCategory> findByLevel(Integer level);

    /**
     * 根据分类名称模糊查询
     * @param categoryName 分类名称
     * @return 分类列表
     */
    List<ProdCategory> findByCategoryNameContaining(String categoryName);

    /**
     * 查询显示的分类
     * @return 显示的分类列表
     */
    List<ProdCategory> findVisibleCategories();

    /**
     * 保存分类
     * @param category 分类对象
     * @return 保存后的分类对象
     */
    ProdCategory save(ProdCategory category);

    /**
     * 更新分类
     * @param category 分类对象
     * @return 更新后的分类对象
     */
    ProdCategory update(ProdCategory category);

    /**
     * 删除分类（软删除）
     * @param id 分类ID
     */
    void deleteById(Long id);

    /**
     * 批量删除分类（软删除）
     * @param ids 分类ID列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 启用分类
     * @param id 分类ID
     */
    void enable(Long id);

    /**
     * 禁用分类
     * @param id 分类ID
     */
    void disable(Long id);

    /**
     * 显示分类
     * @param id 分类ID
     */
    void show(Long id);

    /**
     * 隐藏分类
     * @param id 分类ID
     */
    void hide(Long id);

    /**
     * 检查分类名称是否存在（同级下）
     * @param categoryName 分类名称
     * @param parentId 父级ID
     * @return 是否存在
     */
    boolean existsByCategoryNameAndParentId(String categoryName, Long parentId);

    /**
     * 统计父级下的子分类数量
     * @param parentId 父级ID
     * @return 子分类数量
     */
    long countByParentId(Long parentId);

    /**
     * 查询所有子级分类ID（用于级联删除）
     * @param parentId 父级ID
     * @return 子级分类ID列表
     */
    List<Long> findChildCategoryIds(Long parentId);

    /**
     * 递归查询所有下级分类ID
     * @param parentId 父级ID
     * @return 所有下级分类ID列表
     */
    List<Long> findAllChildCategoryIds(Long parentId);

    /**
     * 构建分类树
     * @return 分类树列表
     */
    List<ProdCategory> buildCategoryTree();

    /**
     * 构建管理后台分类树（包括禁用状态）
     * @return 分类树列表
     */
    List<ProdCategory> buildAdminCategoryTree();

    /**
     * 根据父级ID查询所有子分类（包括禁用状态）
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<ProdCategory> findAllByParentId(Long parentId);
} 