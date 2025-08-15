package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 采购入库单明细Repository
 */
@Repository
public interface PurReceiptItemRepository extends JpaRepository<PurReceiptItem, Long> {

    /**
     * 根据入库单ID查询明细列表
     */
    List<PurReceiptItem> findByReceiptIdOrderByCreateTimeAsc(Long receiptId);

    /**
     * 根据入库单号查询明细列表
     */
    List<PurReceiptItem> findByReceiptNoOrderByCreateTimeAsc(String receiptNo);

    /**
     * 根据采购单ID查询入库明细
     */
    List<PurReceiptItem> findByOrderIdOrderByCreateTimeAsc(Long orderId);

    /**
     * 根据采购单项ID查询入库明细
     */
    List<PurReceiptItem> findByOrderItemIdOrderByCreateTimeAsc(Long orderItemId);

    /**
     * 根据商品ID查询入库明细
     */
    List<PurReceiptItem> findByGoodsIdOrderByCreateTimeDesc(Long goodsId);

    /**
     * 根据SKU ID查询入库明细
     */
    List<PurReceiptItem> findBySkuIdOrderByCreateTimeDesc(Long skuId);

    /**
     * 根据批次号查询入库明细
     */
    List<PurReceiptItem> findByBatchNumberOrderByCreateTimeDesc(String batchNumber);

    /**
     * 根据库位ID查询入库明细
     */
    List<PurReceiptItem> findByLocationIdOrderByCreateTimeDesc(Long locationId);

    /**
     * 删除指定入库单的所有明细
     */
    void deleteByReceiptId(Long receiptId);

    /**
     * 统计指定入库单的明细数量
     */
    @Query("SELECT COUNT(ri) FROM PurReceiptItem ri WHERE ri.receiptId = :receiptId")
    Long countByReceiptId(@Param("receiptId") Long receiptId);

    /**
     * 统计指定采购单项的已入库数量
     */
    @Query("SELECT SUM(ri.quantity) FROM PurReceiptItem ri " +
           "JOIN PurReceipt r ON ri.receiptId = r.id " +
           "WHERE ri.orderItemId = :orderItemId AND r.receiptStatus = 4")
    Integer sumQuantityByOrderItemId(@Param("orderItemId") Long orderItemId);

    /**
     * 统计指定商品的入库数量
     */
    @Query("SELECT SUM(ri.quantity) FROM PurReceiptItem ri " +
           "JOIN PurReceipt r ON ri.receiptId = r.id " +
           "WHERE ri.goodsId = :goodsId AND r.receiptStatus = 4")
    Integer sumQuantityByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 统计指定SKU的入库数量
     */
    @Query("SELECT SUM(ri.quantity) FROM PurReceiptItem ri " +
           "JOIN PurReceipt r ON ri.receiptId = r.id " +
           "WHERE ri.skuId = :skuId AND r.receiptStatus = 4")
    Integer sumQuantityBySkuId(@Param("skuId") Long skuId);

    /**
     * 查询指定采购单的入库明细汇总
     */
    @Query("SELECT ri.goodsId, ri.skuId, SUM(ri.quantity) as totalQuantity, " +
           "AVG(ri.purchasePrice) as avgPrice, SUM(ri.totalAmount) as totalAmount " +
           "FROM PurReceiptItem ri " +
           "JOIN PurReceipt r ON ri.receiptId = r.id " +
           "WHERE ri.orderId = :orderId AND r.receiptStatus = 4 " +
           "GROUP BY ri.goodsId, ri.skuId")
    List<Object[]> findReceiptSummaryByOrderId(@Param("orderId") Long orderId);

    /**
     * 查询商品的最近入库价格
     */
    @Query("SELECT ri.purchasePrice FROM PurReceiptItem ri " +
           "JOIN PurReceipt r ON ri.receiptId = r.id " +
           "WHERE ri.goodsId = :goodsId " +
           "AND (:skuId IS NULL OR ri.skuId = :skuId) " +
           "AND r.receiptStatus = 4 " +
           "ORDER BY r.receiptTime DESC, ri.createTime DESC")
    List<java.math.BigDecimal> findLatestPurchasePriceByGoodsId(@Param("goodsId") Long goodsId, 
                                                               @Param("skuId") Long skuId);
} 