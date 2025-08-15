package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurReceipt;
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
 * 采购入库单Repository
 */
@Repository
public interface PurReceiptRepository extends JpaRepository<PurReceipt, Long> {

    /**
     * 根据入库单号查询
     */
    Optional<PurReceipt> findByReceiptNoAndIsDeleted(String receiptNo, Integer isDeleted);

    /**
     * 根据采购单ID查询入库单列表
     */
    List<PurReceipt> findByOrderIdAndIsDeletedOrderByCreateTimeDesc(Long orderId, Integer isDeleted);

    /**
     * 根据供应商ID查询
     */
    List<PurReceipt> findBySupplierId(Long supplierId);

    /**
     * 根据入库状态查询
     */
    List<PurReceipt> findByReceiptStatusAndIsDeleted(Integer receiptStatus, Integer isDeleted);

    /**
     * 根据仓库ID查询
     */
    List<PurReceipt> findByWarehouseIdAndIsDeleted(Long warehouseId, Integer isDeleted);

    /**
     * 分页查询采购入库单
     */
    @Query("SELECT r FROM PurReceipt r WHERE r.isDeleted = 0 " +
           "AND (:receiptNo IS NULL OR r.receiptNo LIKE %:receiptNo%) " +
           "AND (:orderNo IS NULL OR r.orderNo LIKE %:orderNo%) " +
           "AND (:supplierId IS NULL OR r.supplierId = :supplierId) " +
           "AND (:warehouseId IS NULL OR r.warehouseId = :warehouseId) " +
           "AND (:receiptStatus IS NULL OR r.receiptStatus = :receiptStatus) " +
           "AND (:receiptType IS NULL OR r.receiptType = :receiptType) " +
           "AND (:startTime IS NULL OR r.receiptTime >= :startTime) " +
           "AND (:endTime IS NULL OR r.receiptTime <= :endTime) " +
           "AND (:createStartTime IS NULL OR r.createTime >= :createStartTime) " +
           "AND (:createEndTime IS NULL OR r.createTime <= :createEndTime) " +
           "ORDER BY r.createTime DESC")
    Page<PurReceipt> findPurReceiptsWithFilter(@Param("receiptNo") String receiptNo,
                                              @Param("orderNo") String orderNo,
                                              @Param("supplierId") Long supplierId,
                                              @Param("warehouseId") Long warehouseId,
                                              @Param("receiptStatus") Integer receiptStatus,
                                              @Param("receiptType") Integer receiptType,
                                              @Param("startTime") LocalDate startTime,
                                              @Param("endTime") LocalDate endTime,
                                              @Param("createStartTime") LocalDateTime createStartTime,
                                              @Param("createEndTime") LocalDateTime createEndTime,
                                              Pageable pageable);

    /**
     * 统计各状态入库单数量
     */
    @Query("SELECT r.receiptStatus, COUNT(r) FROM PurReceipt r WHERE r.isDeleted = 0 GROUP BY r.receiptStatus")
    List<Object[]> countByReceiptStatus();

    /**
     * 统计各类型入库单数量
     */
    @Query("SELECT r.receiptType, COUNT(r) FROM PurReceipt r WHERE r.isDeleted = 0 GROUP BY r.receiptType")
    List<Object[]> countByReceiptType();

    /**
     * 统计指定时间范围内的入库总金额
     */
    @Query("SELECT SUM(r.totalAmount) FROM PurReceipt r WHERE r.isDeleted = 0 " +
           "AND r.receiptTime >= :startTime AND r.receiptTime <= :endTime " +
           "AND r.receiptStatus = 4") // 只统计已入库的
    Double sumTotalAmountByReceiptTimeBetween(@Param("startTime") LocalDate startTime,
                                            @Param("endTime") LocalDate endTime);

    /**
     * 查询待审核的入库单
     */
    List<PurReceipt> findByReceiptStatusAndIsDeletedOrderByCreateTimeAsc(Integer receiptStatus, Integer isDeleted);

    /**
     * 根据入库人查询
     */
    List<PurReceipt> findByReceiptUserIdAndIsDeletedOrderByCreateTimeDesc(Long receiptUserId, Integer isDeleted);

    /**
     * 根据审核人查询
     */
    List<PurReceipt> findByAuditIdAndIsDeletedOrderByAuditTimeDesc(Long auditId, Integer isDeleted);

    /**
     * 检查入库单号是否存在
     */
    boolean existsByReceiptNoAndIsDeleted(String receiptNo, Integer isDeleted);

    /**
     * 统计供应商的入库单数量
     */
    @Query("SELECT COUNT(r) FROM PurReceipt r WHERE r.supplierId = :supplierId AND r.isDeleted = 0")
    Long countBySupplierIdAndIsDeleted(@Param("supplierId") Long supplierId);

    /**
     * 统计仓库的入库单数量
     */
    @Query("SELECT COUNT(r) FROM PurReceipt r WHERE r.warehouseId = :warehouseId AND r.isDeleted = 0")
    Long countByWarehouseIdAndIsDeleted(@Param("warehouseId") Long warehouseId);

    /**
     * 查询指定采购单的所有入库单
     */
    @Query("SELECT r FROM PurReceipt r WHERE r.orderId = :orderId AND r.isDeleted = :isDeleted ORDER BY r.createTime DESC")
    List<PurReceipt> findByOrderIdAndIsDeleted(@Param("orderId") Long orderId, @Param("isDeleted") Integer isDeleted);

    /**
     * 统计指定采购单的入库总数量
     */
    @Query("SELECT SUM(ri.quantity) FROM PurReceiptItem ri " +
           "JOIN PurReceipt r ON ri.receiptId = r.id " +
           "WHERE r.orderId = :orderId AND r.receiptStatus = 4 AND r.isDeleted = 0")
    Integer sumQuantityByOrderId(@Param("orderId") Long orderId);
} 