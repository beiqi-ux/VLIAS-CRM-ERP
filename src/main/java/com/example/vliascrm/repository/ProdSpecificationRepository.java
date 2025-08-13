package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品规格Repository
 */
@Repository
public interface ProdSpecificationRepository extends JpaRepository<ProdSpecification, Long> {
    
    /**
     * 根据规格名称查找
     */
    Optional<ProdSpecification> findBySpecificationNameAndIsDeleted(String specificationName, Boolean isDeleted);
    
    /**
     * 根据规格编码查找
     */
    Optional<ProdSpecification> findBySpecificationCodeAndIsDeleted(String specificationCode, Boolean isDeleted);
    
    /**
     * 根据状态查找所有启用的规格
     */
    List<ProdSpecification> findByStatusAndIsDeletedOrderByCreateTimeDesc(Integer status, Boolean isDeleted);
    
    /**
     * 分页查询规格列表
     */
    @Query("SELECT s FROM ProdSpecification s WHERE s.isDeleted = false " +
           "AND (:specificationName IS NULL OR s.specificationName LIKE %:specificationName%) " +
           "AND (:status IS NULL OR s.status = :status) " +
           "ORDER BY s.createTime DESC")
    Page<ProdSpecification> findSpecificationsWithConditions(
            @Param("specificationName") String specificationName,
            @Param("status") Integer status,
            Pageable pageable);
    
    /**
     * 检查规格编码是否存在（排除指定ID）
     */
    @Query("SELECT COUNT(s) > 0 FROM ProdSpecification s WHERE s.specificationCode = :specificationCode " +
           "AND s.isDeleted = false AND (:excludeId IS NULL OR s.id != :excludeId)")
    boolean existsBySpecificationCodeExcludingId(@Param("specificationCode") String specificationCode, 
                                               @Param("excludeId") Long excludeId);
    
    /**
     * 根据分类ID查找规格列表
     */
    List<ProdSpecification> findByCategoryIdAndIsDeletedFalseOrderBySortAsc(Long categoryId);
    
    /**
     * 根据商品ID查找规格列表
     */
    List<ProdSpecification> findByGoodsIdAndIsDeletedFalseOrderBySortAsc(Long goodsId);
    
    /**
     * 根据商品ID和分类ID查找规格列表（优先级查询）
     */
    @Query("SELECT s FROM ProdSpecification s WHERE s.isDeleted = false " +
           "AND (s.goodsId = :goodsId OR (s.goodsId IS NULL AND s.categoryId = :categoryId)) " +
           "AND s.status = 1 " +
           "ORDER BY CASE WHEN s.goodsId IS NOT NULL THEN 1 ELSE 2 END, s.sort ASC")
    List<ProdSpecification> findByGoodsIdOrCategoryIdPrioritized(@Param("goodsId") Long goodsId, 
                                                               @Param("categoryId") Long categoryId);
} 