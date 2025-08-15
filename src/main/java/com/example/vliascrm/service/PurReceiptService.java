package com.example.vliascrm.service;

import com.example.vliascrm.dto.PurReceiptDto;
import com.example.vliascrm.dto.PurReceiptItemDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 采购入库服务接口
 */
public interface PurReceiptService {

    /**
     * 分页查询采购入库单
     */
    Page<PurReceiptDto> getPurReceiptPage(String receiptNo, String orderNo, Long supplierId, 
                                        Long warehouseId, Integer receiptStatus, Integer receiptType,
                                        LocalDate startTime, LocalDate endTime, 
                                        LocalDateTime createStartTime, LocalDateTime createEndTime,
                                        int page, int size);

    /**
     * 根据ID获取采购入库单详情
     */
    PurReceiptDto getPurReceiptById(Long id);

    /**
     * 根据入库单号获取详情
     */
    PurReceiptDto getPurReceiptByNo(String receiptNo);

    /**
     * 创建采购入库单
     */
    PurReceiptDto createPurReceipt(PurReceiptDto purReceiptDto);

    /**
     * 从采购订单创建入库单
     */
    PurReceiptDto createReceiptFromOrder(Long orderId, PurReceiptDto receiptDto);

    /**
     * 更新采购入库单
     */
    PurReceiptDto updatePurReceipt(Long id, PurReceiptDto purReceiptDto);

    /**
     * 删除采购入库单
     */
    void deletePurReceipt(Long id);

    /**
     * 提交采购入库单
     */
    void submitPurReceipt(Long id);

    /**
     * 审核采购入库单
     */
    void auditPurReceipt(Long id, boolean approved, String auditRemark);

    /**
     * 确认入库
     */
    void confirmReceipt(Long id);

    /**
     * 取消入库单
     */
    void cancelPurReceipt(Long id, String reason);

    /**
     * 批量确认入库
     */
    void batchConfirmReceipt(List<Long> ids);

    /**
     * 批量审核入库单
     */
    void batchAuditPurReceipt(List<Long> ids, boolean approved, String auditRemark);

    /**
     * 获取采购订单的入库记录
     */
    List<PurReceiptDto> getReceiptsByOrderId(Long orderId);

    /**
     * 获取供应商的入库记录
     */
    List<PurReceiptDto> getReceiptsBySupplierId(Long supplierId);

    /**
     * 获取仓库的入库记录
     */
    List<PurReceiptDto> getReceiptsByWarehouseId(Long warehouseId);

    /**
     * 获取待审核的入库单
     */
    List<PurReceiptDto> getPendingAuditReceipts();

    /**
     * 获取入库单统计信息
     */
    Map<String, Object> getPurReceiptStatistics();

    /**
     * 获取入库趋势数据
     */
    List<Map<String, Object>> getReceiptTrendData(LocalDate startDate, LocalDate endDate);

    /**
     * 获取商品入库汇总
     */
    List<Map<String, Object>> getGoodsReceiptSummary(Long goodsId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取供应商入库汇总
     */
    List<Map<String, Object>> getSupplierReceiptSummary(Long supplierId, LocalDate startDate, LocalDate endDate);

    /**
     * 检查入库单号是否存在
     */
    boolean existsByReceiptNo(String receiptNo);

    /**
     * 检查采购订单是否已完全入库
     */
    boolean isOrderFullyReceived(Long orderId);

    /**
     * 获取采购订单的入库进度
     */
    Map<String, Object> getOrderReceiptProgress(Long orderId);

    /**
     * 获取商品的最近入库价格
     */
    java.math.BigDecimal getLatestReceiptPrice(Long goodsId, Long skuId);

    /**
     * 获取库存预警信息
     */
    List<Map<String, Object>> getStockWarningInfo();

    /**
     * 生成入库单号
     */
    String generateReceiptNo();

    /**
     * 根据批次号查询入库记录
     */
    List<PurReceiptItemDto> getReceiptItemsByBatch(String batchNumber);

    /**
     * 获取即将到期的商品
     */
    List<PurReceiptItemDto> getExpiringItems(int days);

    /**
     * 更新采购订单的入库状态
     */
    void updateOrderReceiptStatus(Long orderId);

    /**
     * 同步库存数量
     */
    void syncStockQuantity(Long receiptId);

    /**
     * 回滚库存数量
     */
    void rollbackStockQuantity(Long receiptId);

    /**
     * 导出入库单数据
     */
    byte[] exportReceiptData(String receiptNo, String orderNo, Long supplierId, 
                           Long warehouseId, Integer receiptStatus, Integer receiptType,
                           LocalDate startTime, LocalDate endTime);

    /**
     * 导入入库单数据
     */
    List<String> importReceiptData(byte[] fileData);

    /**
     * 获取入库单明细
     */
    List<PurReceiptItemDto> getReceiptItems(Long receiptId);

    /**
     * 保存入库单明细
     */
    void saveReceiptItems(Long receiptId, List<PurReceiptItemDto> items);

    /**
     * 删除入库单明细
     */
    void deleteReceiptItems(Long receiptId);

    /**
     * 复制入库单
     */
    PurReceiptDto copyPurReceipt(Long id);

    /**
     * 生成入库单打印数据
     */
    Map<String, Object> generateReceiptPrintData(Long id);

    /**
     * 获取入库单审核历史
     */
    List<Map<String, Object>> getReceiptAuditHistory(Long id);
} 