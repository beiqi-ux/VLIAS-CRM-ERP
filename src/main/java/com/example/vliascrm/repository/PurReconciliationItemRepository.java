package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurReconciliationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 供应商对账明细Repository
 */
@Repository
public interface PurReconciliationItemRepository extends JpaRepository<PurReconciliationItem, Long> {
    
    /**
     * 根据对账单ID查找明细列表
     */
    List<PurReconciliationItem> findByReconciliationIdOrderByCreateTimeDesc(Long reconciliationId);
    
    /**
     * 根据对账单号查找明细列表
     */
    List<PurReconciliationItem> findByReconciliationNoOrderByCreateTimeDesc(String reconciliationNo);
    
    /**
     * 根据单据ID和单据类型查找明细
     */
    List<PurReconciliationItem> findByBillIdAndBillType(Long billId, Integer billType);
    
    /**
     * 根据单据编号查找明细
     */
    List<PurReconciliationItem> findByBillNo(String billNo);
    
    /**
     * 删除对账单的所有明细
     */
    void deleteByReconciliationId(Long reconciliationId);
    
    /**
     * 计算对账单总金额
     */
    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM PurReconciliationItem i WHERE i.reconciliationId = :reconciliationId")
    BigDecimal sumAmountByReconciliationId(@Param("reconciliationId") Long reconciliationId);
    
    /**
     * 统计对账单明细数量
     */
    Long countByReconciliationId(Long reconciliationId);
    
    /**
     * 检查单据是否已经在对账单中
     */
    boolean existsByBillIdAndBillType(Long billId, Integer billType);
} 