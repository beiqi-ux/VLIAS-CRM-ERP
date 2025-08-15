package com.example.vliascrm.service;

import com.example.vliascrm.dto.PurReturnDto;
import com.example.vliascrm.dto.PurReturnItemDto;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 采购退货业务服务接口
 */
public interface PurReturnService {

    /**
     * 分页查询采购退货单
     */
    Page<PurReturnDto> getPurReturnPage(
            String returnNo,
            String receiptNo,
            Long supplierId,
            Long warehouseId,
            Integer returnStatus,
            Integer reasonType,
            LocalDate startDate,
            LocalDate endDate,
            LocalDateTime createStartTime,
            LocalDateTime createEndTime,
            int page,
            int size);

    /**
     * 根据ID获取退货单详情
     */
    PurReturnDto getPurReturnById(Long id);

    /**
     * 根据退货单号获取退货单详情
     */
    PurReturnDto getPurReturnByNo(String returnNo);

    /**
     * 创建采购退货单
     */
    PurReturnDto createPurReturn(PurReturnDto purReturnDto);

    /**
     * 更新采购退货单
     */
    PurReturnDto updatePurReturn(Long id, PurReturnDto purReturnDto);

    /**
     * 删除采购退货单
     */
    void deletePurReturn(Long id);

    /**
     * 批量删除采购退货单
     */
    void batchDeletePurReturns(List<Long> ids);

    /**
     * 提交采购退货单
     */
    void submitPurReturn(Long id);

    /**
     * 审核采购退货单
     */
    void auditPurReturn(Long id, boolean approved, String auditRemark);

    /**
     * 确认退货
     */
    void confirmReturn(Long id);

    /**
     * 取消采购退货单
     */
    void cancelPurReturn(Long id, String reason);

    /**
     * 复制采购退货单
     */
    PurReturnDto copyPurReturn(Long id);

    /**
     * 根据入库单ID获取可退货明细
     */
    List<PurReturnItemDto> getReturnableItemsByReceiptId(Long receiptId);

    /**
     * 获取退货单明细列表
     */
    List<PurReturnItemDto> getPurReturnItems(Long returnId);

    /**
     * 获取退货统计
     */
    Map<String, Object> getReturnStatistics(LocalDate startDate, LocalDate endDate);

    /**
     * 获取待审核的退货单
     */
    List<PurReturnDto> getPendingAuditReturns();

    /**
     * 生成退货单号
     */
    String generateReturnNo();

    /**
     * 验证退货单是否可以编辑
     */
    boolean canEdit(Long id);

    /**
     * 验证退货单是否可以删除
     */
    boolean canDelete(Long id);

    /**
     * 验证退货单是否可以提交
     */
    boolean canSubmit(Long id);

    /**
     * 验证退货单是否可以审核
     */
    boolean canAudit(Long id);

    /**
     * 验证退货单是否可以确认退货
     */
    boolean canConfirm(Long id);

    /**
     * 验证退货单是否可以取消
     */
    boolean canCancel(Long id);

    /**
     * 导出退货单
     */
    byte[] exportPurReturns(String returnNo, String receiptNo, Long supplierId, 
                           Long warehouseId, Integer returnStatus, Integer reasonType,
                           LocalDate startDate, LocalDate endDate);

    /**
     * 打印退货单
     */
    byte[] printPurReturn(Long id);
} 
 