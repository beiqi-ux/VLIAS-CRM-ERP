package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品分类数据访问接口
 */
@Repository
public interface ProdCategoryRepository extends JpaRepository<ProdCategory, Long>, JpaSpecificationExecutor<ProdCategory> {

    /**
     * 根据父级ID查询子分类
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<ProdCategory> findByParentIdAndStatusAndIsDeletedOrderBySortAsc(Long parentId, Integer status, Boolean isDeleted);

    /**
     * 根据层级查询分类
     * @param level 层级
     * @return 分类列表
     */
    List<ProdCategory> findByLevelAndStatusAndIsDeletedOrderBySortAsc(Integer level, Integer status, Boolean isDeleted);

    /**
     * 根据分类名称查询
     * @param categoryName 分类名称
     * @return 分类列表
     */
    List<ProdCategory> findByCategoryNameContainingAndStatusAndIsDeleted(String categoryName, Integer status, Boolean isDeleted);

    /**
     * 查询显示的分类
     * @return 显示的分类列表
     */
    List<ProdCategory> findByIsShowAndStatusAndIsDeletedOrderBySortAsc(Integer isShow, Integer status, Boolean isDeleted);

    /**
     * 查询根分类（一级分类）
     * @return 根分类列表
     */
    @Query("SELECT c FROM ProdCategory c WHERE c.parentId = 0 AND c.status = 1 AND c.isDeleted = false ORDER BY c.sort ASC")
    List<ProdCategory> findRootCategories();

    /**
     * 统计父级下的子分类数量
     * @param parentId 父级ID
     * @return 子分类数量
     */
    long countByParentIdAndIsDeleted(Long parentId, Boolean isDeleted);

    /**
     * 检查分类名称是否存在（同级下）
     * @param categoryName 分类名称
     * @param parentId 父级ID
     * @return 是否存在
     */
    boolean existsByCategoryNameAndParentIdAndIsDeleted(String categoryName, Long parentId, Boolean isDeleted);

    /**
     * 查询所有子级分类ID（用于级联删除）
     * @param parentId 父级ID
     * @return 子级分类ID列表
     */
    @Query("SELECT c.id FROM ProdCategory c WHERE c.parentId = ?1 AND c.isDeleted = false")
    List<Long> findChildCategoryIds(Long parentId);

    /**
     * 递归查询所有下级分类ID
     * @param parentId 父级ID
     * @return 所有下级分类ID列表
     */
    @Query(value = "WITH RECURSIVE category_tree AS (" +
            "    SELECT id FROM prod_category WHERE parent_id = ?1 AND is_deleted = 0" +
            "    UNION ALL" +
            "    SELECT c.id FROM prod_category c" +
            "    INNER JOIN category_tree ct ON c.parent_id = ct.id" +
            "    WHERE c.is_deleted = 0" +
            ") SELECT id FROM category_tree", nativeQuery = true)
    List<Long> findAllChildCategoryIds(Long parentId);
} 