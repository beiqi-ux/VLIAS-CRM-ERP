package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdAttribute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品属性Repository
 */
@Repository
public interface ProdAttributeRepository extends JpaRepository<ProdAttribute, Long> {
    
    /**
     * 根据属性名称查找
     */
    Optional<ProdAttribute> findByAttributeNameAndIsDeleted(String attributeName, Boolean isDeleted);
    
    /**
     * 根据属性编码查找
     */
    Optional<ProdAttribute> findByAttributeCodeAndIsDeleted(String attributeCode, Boolean isDeleted);
    
    /**
     * 根据状态查找所有启用的属性
     */
    List<ProdAttribute> findByStatusAndIsDeletedOrderByCreateTimeDesc(Integer status, Boolean isDeleted);
    
    /**
     * 分页查询属性列表
     */
    @Query("SELECT a FROM ProdAttribute a WHERE a.isDeleted = false " +
           "AND (:attributeName IS NULL OR a.attributeName LIKE %:attributeName%) " +
           "AND (:status IS NULL OR a.status = :status) " +
           "ORDER BY a.createTime DESC")
    Page<ProdAttribute> findAttributesWithConditions(
            @Param("attributeName") String attributeName,
            @Param("status") Integer status,
            Pageable pageable);
    
    /**
     * 检查属性编码是否存在（排除指定ID）
     */
    @Query("SELECT COUNT(a) > 0 FROM ProdAttribute a WHERE a.attributeCode = :attributeCode " +
           "AND a.isDeleted = false AND (:excludeId IS NULL OR a.id != :excludeId)")
    boolean existsByAttributeCodeExcludingId(@Param("attributeCode") String attributeCode, 
                                           @Param("excludeId") Long excludeId);
} 