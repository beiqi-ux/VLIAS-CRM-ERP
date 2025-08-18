package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurReconciliation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 供应商对账Repository
 */
@Repository
public interface PurReconciliationRepository extends JpaRepository<PurReconciliation, Long> {
    
    /**
     * 根据对账单号查找
     */
    Optional<PurReconciliation> findByReconciliationNoAndIsDeleted(String reconciliationNo, Integer isDeleted);
    
    /**
     * 根据供应商ID查找对账单列表
     */
    List<PurReconciliation> findBySupplierId(Long supplierId);
    
    /**
     * 根据状态查找对账单列表
     */
    List<PurReconciliation> findByStatusAndIsDeleted(Integer status, Integer isDeleted);
    
    /**
     * 分页查询对账单列表
     */
    @Query("SELECT r FROM PurReconciliation r WHERE r.isDeleted = 0 " +
           "AND (:supplierId IS NULL OR r.supplierId = :supplierId) " +
           "AND (:status IS NULL OR r.status = :status) " +
           "AND (:reconciliationNo IS NULL OR r.reconciliationNo LIKE %:reconciliationNo%) " +
           "AND (:startDate IS NULL OR r.startDate >= :startDate) " +
           "AND (:endDate IS NULL OR r.endDate <= :endDate) " +
           "ORDER BY r.createTime DESC")
    Page<PurReconciliation> findReconciliationPage(@Param("supplierId") Long supplierId,
                                                   @Param("status") Integer status,
                                                   @Param("reconciliationNo") String reconciliationNo,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate,
                                                   Pageable pageable);
    
    /**
     * 统计供应商未结算对账单数量
     */
    @Query("SELECT COUNT(r) FROM PurReconciliation r WHERE r.supplierId = :supplierId " +
           "AND r.status IN (1, 2, 3) AND r.isDeleted = 0")
    Long countUnsettledBySupplierId(@Param("supplierId") Long supplierId);
    
    /**
     * 检查对账单号是否存在
     */
    boolean existsByReconciliationNoAndIsDeleted(String reconciliationNo, Integer isDeleted);
} 