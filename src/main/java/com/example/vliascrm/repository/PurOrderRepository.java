package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 采购订单Repository
 */
@Repository
public interface PurOrderRepository extends JpaRepository<PurOrder, Long> {

    /**
     * 根据订单号查询
     */
    Optional<PurOrder> findByOrderNoAndIsDeleted(String orderNo, Integer isDeleted);

    /**
     * 根据供应商ID查询
     */
    List<PurOrder> findBySupplierId(Long supplierId);

    /**
     * 根据订单状态查询
     */
    List<PurOrder> findByOrderStatusAndIsDeleted(Integer orderStatus, Integer isDeleted);

    /**
     * 分页查询采购订单
     */
    @Query("SELECT p FROM PurOrder p WHERE p.isDeleted = 0 " +
           "AND (:orderNo IS NULL OR p.orderNo LIKE %:orderNo%) " +
           "AND (:supplierId IS NULL OR p.supplierId = :supplierId) " +
           "AND (:orderStatus IS NULL OR p.orderStatus = :orderStatus) " +
           "AND (:warehouseId IS NULL OR p.warehouseId = :warehouseId) " +
           "AND (:payStatus IS NULL OR p.payStatus = :payStatus) " +
           "AND (:deliveryStatus IS NULL OR p.deliveryStatus = :deliveryStatus) " +
           "AND (:receiptStatus IS NULL OR p.receiptStatus = :receiptStatus) " +
           "AND (:startTime IS NULL OR p.createTime >= :startTime) " +
           "AND (:endTime IS NULL OR p.createTime <= :endTime) " +
           "ORDER BY p.createTime DESC")
    Page<PurOrder> findPurOrdersWithFilter(@Param("orderNo") String orderNo,
                                          @Param("supplierId") Long supplierId,
                                          @Param("orderStatus") Integer orderStatus,
                                          @Param("warehouseId") Long warehouseId,
                                          @Param("payStatus") Integer payStatus,
                                          @Param("deliveryStatus") Integer deliveryStatus,
                                          @Param("receiptStatus") Integer receiptStatus,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime,
                                          Pageable pageable);

    /**
     * 统计各状态订单数量
     */
    @Query("SELECT p.orderStatus, COUNT(p) FROM PurOrder p WHERE p.isDeleted = 0 GROUP BY p.orderStatus")
    List<Object[]> countByOrderStatus();

    /**
     * 统计各支付状态订单数量
     */
    @Query("SELECT p.payStatus, COUNT(p) FROM PurOrder p WHERE p.isDeleted = 0 GROUP BY p.payStatus")
    List<Object[]> countByPayStatus();

    /**
     * 统计指定时间范围内的订单总金额
     */
    @Query("SELECT SUM(p.totalAmount) FROM PurOrder p WHERE p.isDeleted = 0 " +
           "AND p.createTime >= :startTime AND p.createTime <= :endTime " +
           "AND p.orderStatus NOT IN (1, 7)") // 排除草稿和已取消状态
    Double sumTotalAmountByCreateTimeBetween(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);

    /**
     * 查询需要跟进的订单（已下单但未完成的）
     */
    @Query("SELECT p FROM PurOrder p WHERE p.isDeleted = 0 " +
           "AND p.orderStatus IN (4, 5) " + // 已下单、部分入库
           "ORDER BY p.expectedTime ASC, p.createTime DESC")
    List<PurOrder> findOrdersNeedFollow();

    /**
     * 查询逾期未到货的订单
     */
    @Query("SELECT p FROM PurOrder p WHERE p.isDeleted = 0 " +
           "AND p.orderStatus IN (4, 5) " +
           "AND p.expectedTime < CURRENT_DATE " +
           "ORDER BY p.expectedTime ASC")
    List<PurOrder> findOverdueOrders();

    /**
     * 根据制单人查询订单
     */
    List<PurOrder> findByCreatorIdAndIsDeletedOrderByCreateTimeDesc(Long creatorId, Integer isDeleted);

    /**
     * 根据审核人查询订单
     */
    List<PurOrder> findByAuditIdAndIsDeletedOrderByAuditTimeDesc(Long auditId, Integer isDeleted);

    /**
     * 查询待审核的订单
     */
    List<PurOrder> findByOrderStatusAndIsDeletedOrderByCreateTimeAsc(Integer orderStatus, Integer isDeleted);

    /**
     * 检查订单号是否存在
     */
    boolean existsByOrderNoAndIsDeleted(String orderNo, Integer isDeleted);

    /**
     * 统计供应商的订单数量
     */
    @Query("SELECT COUNT(p) FROM PurOrder p WHERE p.supplierId = :supplierId AND p.isDeleted = 0")
    Long countBySupplierIdAndIsDeleted(@Param("supplierId") Long supplierId);
} 