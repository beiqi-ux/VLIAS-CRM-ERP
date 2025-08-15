package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采购订单明细Repository
 */
@Repository
public interface PurOrderItemRepository extends JpaRepository<PurOrderItem, Long> {

    /**
     * 根据采购单ID查询明细
     */
    List<PurOrderItem> findByOrderIdAndIsDeletedOrderByIdAsc(Long orderId, Integer isDeleted);

    /**
     * 根据商品ID查询明细
     */
    List<PurOrderItem> findByGoodsIdAndIsDeleted(Long goodsId, Integer isDeleted);

    /**
     * 统计采购单的明细数量
     */
    Long countByOrderIdAndIsDeleted(Long orderId, Integer isDeleted);

    /**
     * 统计采购单的总数量
     */
    @Query("SELECT SUM(p.quantity) FROM PurOrderItem p WHERE p.orderId = :orderId AND p.isDeleted = 0")
    BigDecimal sumQuantityByOrderId(@Param("orderId") Long orderId);

    /**
     * 统计采购单的总金额
     */
    @Query("SELECT SUM(p.totalPrice) FROM PurOrderItem p WHERE p.orderId = :orderId AND p.isDeleted = 0")
    BigDecimal sumTotalPriceByOrderId(@Param("orderId") Long orderId);

    /**
     * 统计采购单的已入库数量
     */
    @Query("SELECT SUM(p.receivedQuantity) FROM PurOrderItem p WHERE p.orderId = :orderId AND p.isDeleted = 0")
    BigDecimal sumReceivedQuantityByOrderId(@Param("orderId") Long orderId);

    /**
     * 根据采购单ID删除明细
     */
    void deleteByOrderIdAndIsDeleted(Long orderId, Integer isDeleted);

    /**
     * 检查采购单是否还有未入库的明细
     */
    @Query("SELECT COUNT(p) FROM PurOrderItem p WHERE p.orderId = :orderId AND p.isDeleted = 0 " +
           "AND (p.receivedQuantity IS NULL OR p.receivedQuantity < p.quantity)")
    Long countPendingItemsByOrderId(@Param("orderId") Long orderId);

    /**
     * 检查采购单是否有已入库的明细
     */
    @Query("SELECT COUNT(p) FROM PurOrderItem p WHERE p.orderId = :orderId AND p.isDeleted = 0 " +
           "AND p.receivedQuantity IS NOT NULL AND p.receivedQuantity > 0")
    Long countReceivedItemsByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询商品的采购历史
     */
    @Query("SELECT p FROM PurOrderItem p WHERE p.goodsId = :goodsId AND p.isDeleted = 0 " +
           "ORDER BY p.createTime DESC")
    List<PurOrderItem> findGoodsPurchaseHistory(@Param("goodsId") Long goodsId);

    /**
     * 统计商品的采购总量
     */
    @Query("SELECT SUM(p.quantity) FROM PurOrderItem p WHERE p.goodsId = :goodsId AND p.isDeleted = 0")
    BigDecimal sumGoodsPurchaseQuantity(@Param("goodsId") Long goodsId);

    /**
     * 统计商品的平均采购价格
     */
    @Query("SELECT AVG(p.unitPrice) FROM PurOrderItem p WHERE p.goodsId = :goodsId AND p.isDeleted = 0")
    BigDecimal avgGoodsPurchasePrice(@Param("goodsId") Long goodsId);

    /**
     * 获取商品的最新采购价格
     */
    @Query("SELECT p.unitPrice FROM PurOrderItem p WHERE p.goodsId = :goodsId AND p.isDeleted = 0 " +
           "ORDER BY p.createTime DESC LIMIT 1")
    BigDecimal getLatestGoodsPurchasePrice(@Param("goodsId") Long goodsId);
} 