package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurReturn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 采购退货单Repository
 */
@Repository
public interface PurReturnRepository extends JpaRepository<PurReturn, Long> {

    /**
     * 根据退货单号查询
     */
    Optional<PurReturn> findByReturnNoAndIsDeleted(String returnNo, Integer isDeleted);

    /**
     * 根据供应商ID查询
     */
    List<PurReturn> findBySupplierIdAndIsDeleted(Long supplierId, Integer isDeleted);

    /**
     * 根据仓库ID查询
     */
    List<PurReturn> findByWarehouseIdAndIsDeleted(Long warehouseId, Integer isDeleted);

    /**
     * 根据退货状态查询
     */
    List<PurReturn> findByReturnStatusAndIsDeleted(Integer returnStatus, Integer isDeleted);

    /**
     * 根据入库单ID查询
     */
    List<PurReturn> findByReceiptIdAndIsDeleted(Long receiptId, Integer isDeleted);

    /**
     * 分页查询采购退货单
     */
    @Query("SELECT r FROM PurReturn r WHERE r.isDeleted = 0 " +
           "AND (:returnNo IS NULL OR r.returnNo LIKE %:returnNo%) " +
           "AND (:receiptNo IS NULL OR r.receiptNo LIKE %:receiptNo%) " +
           "AND (:supplierId IS NULL OR r.supplierId = :supplierId) " +
           "AND (:warehouseId IS NULL OR r.warehouseId = :warehouseId) " +
           "AND (:returnStatus IS NULL OR r.returnStatus = :returnStatus) " +
           "AND (:reasonType IS NULL OR r.reasonType = :reasonType) " +
           "AND (:startDate IS NULL OR r.returnTime >= :startDate) " +
           "AND (:endDate IS NULL OR r.returnTime <= :endDate) " +
           "AND (:createStartTime IS NULL OR r.createTime >= :createStartTime) " +
           "AND (:createEndTime IS NULL OR r.createTime <= :createEndTime) " +
           "ORDER BY r.createTime DESC")
    Page<PurReturn> findPurReturnPage(@Param("returnNo") String returnNo,
                                     @Param("receiptNo") String receiptNo,
                                     @Param("supplierId") Long supplierId,
                                     @Param("warehouseId") Long warehouseId,
                                     @Param("returnStatus") Integer returnStatus,
                                     @Param("reasonType") Integer reasonType,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate,
                                     @Param("createStartTime") LocalDateTime createStartTime,
                                     @Param("createEndTime") LocalDateTime createEndTime,
                                     Pageable pageable);

    /**
     * 查询待审核的退货单
     */
    @Query("SELECT r FROM PurReturn r WHERE r.returnStatus = :status AND r.isDeleted = 0 ORDER BY r.createTime ASC")
    List<PurReturn> findPendingAuditReturns(@Param("status") Integer status);

    /**
     * 统计各状态退货单数量
     */
    @Query("SELECT r.returnStatus, COUNT(r) FROM PurReturn r WHERE r.isDeleted = 0 GROUP BY r.returnStatus")
    List<Object[]> countByReturnStatus();

    /**
     * 统计各原因类型退货单数量
     */
    @Query("SELECT r.reasonType, COUNT(r) FROM PurReturn r WHERE r.isDeleted = 0 GROUP BY r.reasonType")
    List<Object[]> countByReasonType();

    /**
     * 统计指定时间范围内的退货总金额
     */
    @Query("SELECT SUM(r.totalAmount) FROM PurReturn r WHERE r.isDeleted = 0 " +
           "AND r.returnTime >= :startDate AND r.returnTime <= :endDate " +
           "AND r.returnStatus = 4") // 只统计已退货的
    Double sumTotalAmountByReturnTimeBetween(@Param("startDate") LocalDate startDate,
                                           @Param("endDate") LocalDate endDate);

    /**
     * 根据创建人查询
     */
    List<PurReturn> findByCreateByAndIsDeleted(Long createBy, Integer isDeleted);

    /**
     * 根据审核人查询
     */
    List<PurReturn> findByAuditIdAndIsDeleted(Long auditId, Integer isDeleted);

    /**
     * 查询指定时间范围内创建的退货单
     */
    List<PurReturn> findByCreateTimeBetweenAndIsDeleted(LocalDateTime startTime, LocalDateTime endTime, Integer isDeleted);

    /**
     * 查询指定时间范围内退货的退货单
     */
    List<PurReturn> findByReturnTimeBetweenAndIsDeleted(LocalDate startDate, LocalDate endDate, Integer isDeleted);
} 
 