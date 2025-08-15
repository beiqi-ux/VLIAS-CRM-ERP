package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.PurReturnDto;
import com.example.vliascrm.dto.PurReturnItemDto;
import com.example.vliascrm.entity.PurReturn;
import com.example.vliascrm.entity.PurReturnItem;
import com.example.vliascrm.repository.PurReturnRepository;
import com.example.vliascrm.repository.PurReturnItemRepository;
import com.example.vliascrm.service.PurReturnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * 采购退货业务服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurReturnServiceImpl implements PurReturnService {

    private final PurReturnRepository purReturnRepository;
    private final PurReturnItemRepository purReturnItemRepository;

    @Override
    public Page<PurReturnDto> getPurReturnPage(String returnNo, String receiptNo, Long supplierId, 
            Long warehouseId, Integer returnStatus, Integer reasonType, LocalDate startDate, 
            LocalDate endDate, LocalDateTime createStartTime, LocalDateTime createEndTime, 
            int page, int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<PurReturn> purReturnPage = purReturnRepository.findPurReturnPage(
                returnNo, receiptNo, supplierId, warehouseId, returnStatus, reasonType,
                startDate, endDate, createStartTime, createEndTime, pageable);
        
        return purReturnPage.map(this::convertToDto);
    }

    @Override
    public PurReturnDto getPurReturnById(Long id) {
        PurReturn purReturn = purReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("采购退货单不存在"));
        
        PurReturnDto dto = convertToDto(purReturn);
        
        // 获取明细列表
        List<PurReturnItem> items = purReturnItemRepository.findByReturnIdOrderByCreateTimeAsc(id);
        dto.setItems(items.stream().map(this::convertItemToDto).collect(Collectors.toList()));
        
        return dto;
    }

    @Override
    public PurReturnDto getPurReturnByNo(String returnNo) {
        PurReturn purReturn = purReturnRepository.findByReturnNoAndIsDeleted(returnNo, 0)
                .orElseThrow(() -> new RuntimeException("采购退货单不存在"));
        return convertToDto(purReturn);
    }

    @Override
    @Transactional
    public PurReturnDto createPurReturn(PurReturnDto purReturnDto) {
        // 创建主表记录
        PurReturn purReturn = new PurReturn();
        BeanUtils.copyProperties(purReturnDto, purReturn, "id", "createTime", "updateTime");
        
        // 生成退货单号
        if (purReturn.getReturnNo() == null || purReturn.getReturnNo().isEmpty()) {
            purReturn.setReturnNo(generateReturnNo());
        }
        
        // 设置创建信息
        purReturn.setCreateBy(getCurrentUserId());
        purReturn.setUpdateBy(getCurrentUserId());
        
        purReturn = purReturnRepository.save(purReturn);
        
        // 保存明细
        if (purReturnDto.getItems() != null && !purReturnDto.getItems().isEmpty()) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            int totalQuantity = 0;
            
            for (PurReturnItemDto itemDto : purReturnDto.getItems()) {
                PurReturnItem item = new PurReturnItem();
                BeanUtils.copyProperties(itemDto, item, "id", "createTime", "updateTime");
                item.setReturnId(purReturn.getId());
                item.setReturnNo(purReturn.getReturnNo());
                
                purReturnItemRepository.save(item);
                
                totalAmount = totalAmount.add(item.getTotalAmount());
                totalQuantity += item.getQuantity();
            }
            
            // 更新主表金额
            purReturn.setTotalAmount(totalAmount);
            purReturnRepository.save(purReturn);
        }
        
        return convertToDto(purReturn);
    }

    @Override
    @Transactional
    public PurReturnDto updatePurReturn(Long id, PurReturnDto purReturnDto) {
        PurReturn existingReturn = purReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("采购退货单不存在"));
        
        // 检查是否可以编辑
        if (!canEdit(id)) {
            throw new RuntimeException("当前状态不允许编辑");
        }
        
        // 更新主表
        BeanUtils.copyProperties(purReturnDto, existingReturn, "id", "returnNo", "createTime", "updateTime", "createBy");
        existingReturn.setUpdateBy(getCurrentUserId());
        
        // 删除原有明细
        purReturnItemRepository.deleteByReturnId(id);
        
        // 重新保存明细
        if (purReturnDto.getItems() != null && !purReturnDto.getItems().isEmpty()) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            
            for (PurReturnItemDto itemDto : purReturnDto.getItems()) {
                PurReturnItem item = new PurReturnItem();
                BeanUtils.copyProperties(itemDto, item, "id", "createTime", "updateTime");
                item.setReturnId(existingReturn.getId());
                item.setReturnNo(existingReturn.getReturnNo());
                
                purReturnItemRepository.save(item);
                totalAmount = totalAmount.add(item.getTotalAmount());
            }
            
            existingReturn.setTotalAmount(totalAmount);
        }
        
        purReturnRepository.save(existingReturn);
        return convertToDto(existingReturn);
    }

    @Override
    @Transactional
    public void deletePurReturn(Long id) {
        if (!canDelete(id)) {
            throw new RuntimeException("当前状态不允许删除");
        }
        
        PurReturn purReturn = purReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("采购退货单不存在"));
        
        purReturn.setIsDeleted(1);
        purReturn.setUpdateBy(getCurrentUserId());
        purReturnRepository.save(purReturn);
    }

    @Override
    @Transactional
    public void batchDeletePurReturns(List<Long> ids) {
        for (Long id : ids) {
            deletePurReturn(id);
        }
    }

    @Override
    @Transactional
    public void submitPurReturn(Long id) {
        if (!canSubmit(id)) {
            throw new RuntimeException("当前状态不允许提交");
        }
        
        PurReturn purReturn = purReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("采购退货单不存在"));
        
        purReturn.setReturnStatus(PurReturn.STATUS_PENDING_AUDIT);
        purReturn.setUpdateBy(getCurrentUserId());
        purReturnRepository.save(purReturn);
    }

    @Override
    @Transactional
    public void auditPurReturn(Long id, boolean approved, String auditRemark) {
        if (!canAudit(id)) {
            throw new RuntimeException("当前状态不允许审核");
        }
        
        PurReturn purReturn = purReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("采购退货单不存在"));
        
        purReturn.setReturnStatus(approved ? PurReturn.STATUS_AUDITED : PurReturn.STATUS_CANCELLED);
        purReturn.setAuditId(getCurrentUserId());
        purReturn.setAuditTime(LocalDateTime.now());
        purReturn.setAuditRemark(auditRemark);
        purReturn.setUpdateBy(getCurrentUserId());
        
        purReturnRepository.save(purReturn);
    }

    @Override
    @Transactional
    public void confirmReturn(Long id) {
        if (!canConfirm(id)) {
            throw new RuntimeException("当前状态不允许确认退货");
        }
        
        PurReturn purReturn = purReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("采购退货单不存在"));
        
        purReturn.setReturnStatus(PurReturn.STATUS_RETURNED);
        purReturn.setUpdateBy(getCurrentUserId());
        purReturnRepository.save(purReturn);
        
        // TODO: 处理库存回退逻辑
    }

    @Override
    @Transactional
    public void cancelPurReturn(Long id, String reason) {
        if (!canCancel(id)) {
            throw new RuntimeException("当前状态不允许取消");
        }
        
        PurReturn purReturn = purReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("采购退货单不存在"));
        
        purReturn.setReturnStatus(PurReturn.STATUS_CANCELLED);
        purReturn.setRemark(reason);
        purReturn.setUpdateBy(getCurrentUserId());
        purReturnRepository.save(purReturn);
    }

    @Override
    public PurReturnDto copyPurReturn(Long id) {
        PurReturnDto original = getPurReturnById(id);
        
        // 清除ID和单号等信息
        original.setId(null);
        original.setReturnNo(null);
        original.setReturnStatus(PurReturn.STATUS_DRAFT);
        original.setCreateTime(null);
        original.setUpdateTime(null);
        
        if (original.getItems() != null) {
            original.getItems().forEach(item -> {
                item.setId(null);
                item.setReturnId(null);
                item.setReturnNo(null);
                item.setCreateTime(null);
                item.setUpdateTime(null);
            });
        }
        
        return original;
    }

    @Override
    public List<PurReturnItemDto> getReturnableItemsByReceiptId(Long receiptId) {
        // TODO: 实现根据入库单获取可退货明细的逻辑
        return List.of();
    }

    @Override
    public List<PurReturnItemDto> getPurReturnItems(Long returnId) {
        List<PurReturnItem> items = purReturnItemRepository.findByReturnIdOrderByCreateTimeAsc(returnId);
        return items.stream().map(this::convertItemToDto).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getReturnStatistics(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> stats = new HashMap<>();
        
        // TODO: 实现统计逻辑
        stats.put("totalReturns", 0);
        stats.put("totalAmount", BigDecimal.ZERO);
        stats.put("pendingAudit", 0);
        
        return stats;
    }

    @Override
    public List<PurReturnDto> getPendingAuditReturns() {
        List<PurReturn> returns = purReturnRepository.findPendingAuditReturns(PurReturn.STATUS_PENDING_AUDIT);
        return returns.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public String generateReturnNo() {
        String prefix = "TH";
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        return prefix + date + timestamp;
    }

    @Override
    public boolean canEdit(Long id) {
        PurReturn purReturn = purReturnRepository.findById(id).orElse(null);
        return purReturn != null && purReturn.getReturnStatus() == PurReturn.STATUS_DRAFT;
    }

    @Override
    public boolean canDelete(Long id) {
        return canEdit(id);
    }

    @Override
    public boolean canSubmit(Long id) {
        return canEdit(id);
    }

    @Override
    public boolean canAudit(Long id) {
        PurReturn purReturn = purReturnRepository.findById(id).orElse(null);
        return purReturn != null && purReturn.getReturnStatus() == PurReturn.STATUS_PENDING_AUDIT;
    }

    @Override
    public boolean canConfirm(Long id) {
        PurReturn purReturn = purReturnRepository.findById(id).orElse(null);
        return purReturn != null && purReturn.getReturnStatus() == PurReturn.STATUS_AUDITED;
    }

    @Override
    public boolean canCancel(Long id) {
        PurReturn purReturn = purReturnRepository.findById(id).orElse(null);
        return purReturn != null && (purReturn.getReturnStatus() == PurReturn.STATUS_DRAFT || 
                                   purReturn.getReturnStatus() == PurReturn.STATUS_PENDING_AUDIT);
    }

    @Override
    public byte[] exportPurReturns(String returnNo, String receiptNo, Long supplierId, 
                                  Long warehouseId, Integer returnStatus, Integer reasonType,
                                  LocalDate startDate, LocalDate endDate) {
        // TODO: 实现导出功能
        return new byte[0];
    }

    @Override
    public byte[] printPurReturn(Long id) {
        // TODO: 实现打印功能
        return new byte[0];
    }

    // 转换方法
    private PurReturnDto convertToDto(PurReturn purReturn) {
        PurReturnDto dto = new PurReturnDto();
        BeanUtils.copyProperties(purReturn, dto);
        
        // 设置状态名称
        dto.setReturnStatusName(getStatusName(purReturn.getReturnStatus()));
        dto.setReasonTypeName(getReasonTypeName(purReturn.getReasonType()));
        
        // TODO: 设置关联名称（供应商、仓库、用户等）
        
        return dto;
    }

    private PurReturnItemDto convertItemToDto(PurReturnItem item) {
        PurReturnItemDto dto = new PurReturnItemDto();
        BeanUtils.copyProperties(item, dto);
        
        // TODO: 设置商品相关信息
        
        return dto;
    }

    private String getStatusName(Integer status) {
        if (status == null) return "";
        switch (status) {
            case 1: return "草稿";
            case 2: return "待审核";
            case 3: return "已审核";
            case 4: return "已退货";
            case 5: return "已取消";
            default: return "未知";
        }
    }

    private String getReasonTypeName(Integer reasonType) {
        if (reasonType == null) return "";
        switch (reasonType) {
            case 1: return "质量问题";
            case 2: return "运输损坏";
            case 3: return "发货错误";
            case 4: return "过期商品";
            case 9: return "其他";
            default: return "未知";
        }
    }

    private Long getCurrentUserId() {
        // TODO: 从安全上下文获取当前用户ID
        return 1L;
    }
} 
 