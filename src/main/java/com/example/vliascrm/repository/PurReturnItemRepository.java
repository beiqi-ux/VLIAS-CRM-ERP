package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 采购退货明细数据访问层
 */
@Repository
public interface PurReturnItemRepository extends JpaRepository<PurReturnItem, Long> {

    /**
     * 根据退货单ID查询明细列表
     */
    List<PurReturnItem> findByReturnIdOrderByCreateTimeAsc(Long returnId);

    /**
     * 根据退货单号查询明细列表
     */
    List<PurReturnItem> findByReturnNoOrderByCreateTimeAsc(String returnNo);

    /**
     * 根据入库单ID查询退货明细
     */
    List<PurReturnItem> findByReceiptIdOrderByCreateTimeAsc(Long receiptId);

    /**
     * 根据入库单项ID查询退货明细
     */
    List<PurReturnItem> findByReceiptItemIdOrderByCreateTimeAsc(Long receiptItemId);

    /**
     * 根据商品ID查询退货明细
     */
    List<PurReturnItem> findByGoodsIdOrderByCreateTimeDesc(Long goodsId);

    /**
     * 根据SKU ID查询退货明细
     */
    List<PurReturnItem> findBySkuIdOrderByCreateTimeDesc(Long skuId);

    /**
     * 计算退货单总金额
     */
    @Query("SELECT SUM(pri.totalAmount) FROM PurReturnItem pri WHERE pri.returnId = :returnId")
    BigDecimal sumTotalAmountByReturnId(@Param("returnId") Long returnId);

    /**
     * 统计退货单明细数量
     */
    @Query("SELECT COUNT(pri) FROM PurReturnItem pri WHERE pri.returnId = :returnId")
    Long countByReturnId(@Param("returnId") Long returnId);

    /**
     * 根据批次号查询退货明细
     */
    List<PurReturnItem> findByBatchNumberOrderByCreateTimeDesc(String batchNumber);

    /**
     * 删除退货单的所有明细
     */
    void deleteByReturnId(Long returnId);

    /**
     * 检查某个入库单项是否有退货记录
     */
    boolean existsByReceiptItemId(Long receiptItemId);

    /**
     * 根据退货单ID和商品ID查询明细
     */
    List<PurReturnItem> findByReturnIdAndGoodsId(Long returnId, Long goodsId);
} 
 