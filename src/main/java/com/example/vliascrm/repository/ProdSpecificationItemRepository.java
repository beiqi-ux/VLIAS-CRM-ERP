package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdSpecificationItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品规格项Repository接口
 */
@Repository
public interface ProdSpecificationItemRepository extends JpaRepository<ProdSpecificationItem, Long>, JpaSpecificationExecutor<ProdSpecificationItem> {

    /**
     * 根据ID和删除状态查询规格项
     */
    Optional<ProdSpecificationItem> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    /**
     * 根据删除状态分页查询规格项
     */
    Page<ProdSpecificationItem> findByIsDeleted(Boolean isDeleted, Pageable pageable);

    /**
     * 根据分类ID查询规格项列表（按排序值升序）
     */
    List<ProdSpecificationItem> findByCategoryIdAndIsDeletedOrderBySortAsc(Long categoryId, Boolean isDeleted);

    /**
     * 根据分类ID和状态查询规格项列表（按排序值升序）
     */
    List<ProdSpecificationItem> findByCategoryIdAndStatusAndIsDeletedOrderBySortAsc(Long categoryId, Integer status, Boolean isDeleted);



    /**
     * 检查规格项名称是否存在（排除指定ID）
     */
    @Query("SELECT COUNT(i) > 0 FROM ProdSpecificationItem i WHERE i.categoryId = :categoryId AND i.itemName = :itemName AND i.isDeleted = false AND (:excludeId IS NULL OR i.id != :excludeId)")
    boolean existsByCategoryIdAndItemNameExcludingId(@Param("categoryId") Long categoryId, @Param("itemName") String itemName, @Param("excludeId") Long excludeId);

    /**
     * 检查规格项代码是否存在（排除指定ID）
     */
    @Query("SELECT COUNT(i) > 0 FROM ProdSpecificationItem i WHERE i.categoryId = :categoryId AND i.itemCode = :itemCode AND i.isDeleted = false AND (:excludeId IS NULL OR i.id != :excludeId)")
    boolean existsByCategoryIdAndItemCodeExcludingId(@Param("categoryId") Long categoryId, @Param("itemCode") String itemCode, @Param("excludeId") Long excludeId);

    /**
     * 根据规格项名称模糊查询
     */
    List<ProdSpecificationItem> findByItemNameContainingAndStatusAndIsDeleted(String itemName, Integer status, Boolean isDeleted);

    /**
     * 根据ID列表查询规格项
     */
    @Query("SELECT i FROM ProdSpecificationItem i WHERE i.id IN ?1 AND i.isDeleted = ?2 ORDER BY i.categoryId, i.sort")
    List<ProdSpecificationItem> findByIdInAndIsDeleted(List<Long> ids, Boolean isDeleted);

    /**
     * 根据分类ID查询规格项（不考虑删除状态）
     */
    List<ProdSpecificationItem> findByCategoryIdAndIsDeleted(Long categoryId, Boolean isDeleted);

    /**
     * 根据分类ID和状态查询规格项
     */
    List<ProdSpecificationItem> findByCategoryIdAndStatusAndIsDeleted(Long categoryId, Integer status, Boolean isDeleted);

    /**
     * 查询所有启用的规格项
     */
    List<ProdSpecificationItem> findByStatusAndIsDeletedOrderBySortAsc(Integer status, Boolean isDeleted);

    /**
     * 根据分类ID统计规格项数量
     */
    @Query("SELECT COUNT(i) FROM ProdSpecificationItem i WHERE i.categoryId = :categoryId AND i.isDeleted = false")
    long countByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * 根据分类ID和删除状态统计规格项数量
     */
    long countByCategoryIdAndIsDeleted(Long categoryId, Boolean isDeleted);
} 