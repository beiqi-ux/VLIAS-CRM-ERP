package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.PurReceiptDto;
import com.example.vliascrm.dto.PurReceiptItemDto;
import com.example.vliascrm.entity.*;
import com.example.vliascrm.repository.*;
import com.example.vliascrm.service.PurReceiptService;
import com.example.vliascrm.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购入库服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurReceiptServiceImpl implements PurReceiptService {

    private final PurReceiptRepository purReceiptRepository;
    private final PurReceiptItemRepository purReceiptItemRepository;
    private final PurOrderRepository purOrderRepository;
    private final PurOrderItemRepository purOrderItemRepository;
    private final PurSupplierRepository purSupplierRepository;
    private final SysUserRepository sysUserRepository;

    @Override
    public Page<PurReceiptDto> getPurReceiptPage(String receiptNo, String orderNo, Long supplierId,
                                               Long warehouseId, Integer receiptStatus, Integer receiptType,
                                               LocalDate startTime, LocalDate endTime,
                                               LocalDateTime createStartTime, LocalDateTime createEndTime,
                                               int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PurReceipt> receiptPage = purReceiptRepository.findPurReceiptsWithFilter(
                receiptNo, orderNo, supplierId, warehouseId, receiptStatus, receiptType,
                startTime, endTime, createStartTime, createEndTime, pageable);

        return receiptPage.map(this::convertToDto);
    }

    @Override
    public PurReceiptDto getPurReceiptById(Long id) {
        Optional<PurReceipt> optional = purReceiptRepository.findById(id);
        if (optional.isPresent()) {
            PurReceipt receipt = optional.get();
            if (receipt.getIsDeleted() == 0) {
                return convertToDto(receipt);
            }
        }
        throw new RuntimeException("入库单不存在");
    }

    @Override
    public PurReceiptDto getPurReceiptByNo(String receiptNo) {
        Optional<PurReceipt> optional = purReceiptRepository.findByReceiptNoAndIsDeleted(receiptNo, 0);
        if (optional.isPresent()) {
            return convertToDto(optional.get());
        }
        throw new RuntimeException("入库单不存在");
    }

    @Override
    @Transactional
    public PurReceiptDto createPurReceipt(PurReceiptDto purReceiptDto) {
        log.info("开始创建采购入库单，供应商ID: {}", purReceiptDto.getSupplierId());

        // 验证基础数据
        validateReceiptData(purReceiptDto);

        // 生成入库单号
        String receiptNo = generateReceiptNo();

        // 创建入库单
        PurReceipt receipt = new PurReceipt();
        receipt.setReceiptNo(receiptNo);
        receipt.setOrderId(purReceiptDto.getOrderId());
        receipt.setOrderNo(purReceiptDto.getOrderNo());
        receipt.setSupplierId(purReceiptDto.getSupplierId());
        receipt.setWarehouseId(purReceiptDto.getWarehouseId());
        receipt.setReceiptStatus(PurReceipt.STATUS_DRAFT);
        receipt.setReceiptType(purReceiptDto.getReceiptType());
        receipt.setReceiptTime(purReceiptDto.getReceiptTime());
        receipt.setReceiptUserId(SecurityUtils.getCurrentUserId());
        receipt.setContact(purReceiptDto.getContact());
        receipt.setMobile(purReceiptDto.getMobile());
        receipt.setRemark(purReceiptDto.getRemark());
        receipt.setCreateBy(SecurityUtils.getCurrentUserId());
        receipt.setUpdateBy(SecurityUtils.getCurrentUserId());

        PurReceipt savedReceipt = purReceiptRepository.save(receipt);

        // 保存入库明细
        if (purReceiptDto.getItems() != null && !purReceiptDto.getItems().isEmpty()) {
            saveReceiptItems(savedReceipt.getId(), purReceiptDto.getItems());
            updateReceiptTotalAmount(savedReceipt.getId());
        }

        log.info("入库单创建成功，入库单号: {}", receiptNo);
        return convertToDto(savedReceipt);
    }

    @Override
    @Transactional
    public PurReceiptDto createReceiptFromOrder(Long orderId, PurReceiptDto receiptDto) {
        log.info("从采购订单创建入库单，订单ID: {}", orderId);

        // 获取采购订单
        Optional<PurOrder> orderOpt = purOrderRepository.findById(orderId);
        if (!orderOpt.isPresent() || orderOpt.get().getIsDeleted() == 1) {
            throw new RuntimeException("采购订单不存在");
        }

        PurOrder order = orderOpt.get();

        // 检查订单状态
        if (order.getOrderStatus() < PurOrder.STATUS_AUDITED) {
            throw new RuntimeException("只有已审核的订单才能创建入库单");
        }

        // 生成入库单号
        String receiptNo = generateReceiptNo();

        // 创建入库单
        PurReceipt receipt = new PurReceipt();
        receipt.setReceiptNo(receiptNo);
        receipt.setOrderId(orderId);
        receipt.setOrderNo(order.getOrderNo());
        receipt.setSupplierId(order.getSupplierId());
        receipt.setWarehouseId(receiptDto.getWarehouseId() != null ? receiptDto.getWarehouseId() : order.getWarehouseId());
        receipt.setReceiptStatus(PurReceipt.STATUS_DRAFT);
        receipt.setReceiptType(PurReceipt.TYPE_PURCHASE);
        receipt.setReceiptTime(receiptDto.getReceiptTime() != null ? receiptDto.getReceiptTime() : LocalDate.now());
        receipt.setReceiptUserId(SecurityUtils.getCurrentUserId());
        receipt.setContact(receiptDto.getContact() != null ? receiptDto.getContact() : order.getContact());
        receipt.setMobile(receiptDto.getMobile() != null ? receiptDto.getMobile() : order.getMobile());
        receipt.setRemark(receiptDto.getRemark());
        receipt.setCreateBy(SecurityUtils.getCurrentUserId());
        receipt.setUpdateBy(SecurityUtils.getCurrentUserId());

        PurReceipt savedReceipt = purReceiptRepository.save(receipt);

        // 从订单明细创建入库明细
        List<PurOrderItem> orderItems = purOrderItemRepository.findByOrderIdAndIsDeletedOrderByIdAsc(orderId, 0);
        List<PurReceiptItemDto> receiptItems = new ArrayList<>();

        for (PurOrderItem orderItem : orderItems) {
            PurReceiptItemDto itemDto = new PurReceiptItemDto();
            itemDto.setOrderId(orderId);
            itemDto.setOrderItemId(orderItem.getId());
            itemDto.setGoodsId(orderItem.getGoodsId());
            itemDto.setGoodsName(orderItem.getGoodsName());
            itemDto.setGoodsSpec(orderItem.getSpecification());
            itemDto.setGoodsUnit(orderItem.getUnit());
            itemDto.setPurchasePrice(orderItem.getUnitPrice());
            itemDto.setQuantity(orderItem.getQuantity().intValue());
            itemDto.setTotalAmount(orderItem.getUnitPrice().multiply(orderItem.getQuantity()));

            receiptItems.add(itemDto);
        }

        if (!receiptItems.isEmpty()) {
            saveReceiptItems(savedReceipt.getId(), receiptItems);
            updateReceiptTotalAmount(savedReceipt.getId());
        }

        log.info("从采购订单创建入库单成功，入库单号: {}", receiptNo);
        return convertToDto(savedReceipt);
    }

    @Override
    @Transactional
    public PurReceiptDto updatePurReceipt(Long id, PurReceiptDto purReceiptDto) {
        log.info("更新采购入库单，ID: {}", id);

        Optional<PurReceipt> optional = purReceiptRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("入库单不存在");
        }

        PurReceipt receipt = optional.get();

        // 检查入库单状态是否允许修改
        if (receipt.getReceiptStatus() != PurReceipt.STATUS_DRAFT && 
            receipt.getReceiptStatus() != PurReceipt.STATUS_PENDING_AUDIT) {
            throw new RuntimeException("当前状态不允许修改");
        }

        // 更新入库单信息
        receipt.setSupplierId(purReceiptDto.getSupplierId());
        receipt.setWarehouseId(purReceiptDto.getWarehouseId());
        receipt.setReceiptType(purReceiptDto.getReceiptType());
        receipt.setReceiptTime(purReceiptDto.getReceiptTime());
        receipt.setContact(purReceiptDto.getContact());
        receipt.setMobile(purReceiptDto.getMobile());
        receipt.setRemark(purReceiptDto.getRemark());
        receipt.setUpdateBy(SecurityUtils.getCurrentUserId());

        PurReceipt savedReceipt = purReceiptRepository.save(receipt);

        // 删除原有明细
        purReceiptItemRepository.deleteByReceiptId(id);

        // 保存新明细
        if (purReceiptDto.getItems() != null && !purReceiptDto.getItems().isEmpty()) {
            saveReceiptItems(savedReceipt.getId(), purReceiptDto.getItems());
            updateReceiptTotalAmount(savedReceipt.getId());
        }

        log.info("入库单更新成功，入库单号: {}", receipt.getReceiptNo());
        return convertToDto(savedReceipt);
    }

    @Override
    @Transactional
    public void deletePurReceipt(Long id) {
        log.info("删除采购入库单，ID: {}", id);

        Optional<PurReceipt> optional = purReceiptRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("入库单不存在");
        }

        PurReceipt receipt = optional.get();

        // 检查入库单状态是否允许删除
        if (receipt.getReceiptStatus() != PurReceipt.STATUS_DRAFT) {
            throw new RuntimeException("只有草稿状态的入库单才能删除");
        }

        // 逻辑删除入库单
        receipt.setIsDeleted(1);
        receipt.setUpdateBy(SecurityUtils.getCurrentUserId());
        purReceiptRepository.save(receipt);

        // 删除明细
        purReceiptItemRepository.deleteByReceiptId(id);

        log.info("入库单删除成功，入库单号: {}", receipt.getReceiptNo());
    }

    @Override
    @Transactional
    public void submitPurReceipt(Long id) {
        log.info("提交采购入库单，ID: {}", id);

        Optional<PurReceipt> optional = purReceiptRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("入库单不存在");
        }

        PurReceipt receipt = optional.get();

        // 检查入库单状态
        if (receipt.getReceiptStatus() != PurReceipt.STATUS_DRAFT) {
            throw new RuntimeException("只有草稿状态的入库单才能提交");
        }

        // 检查入库单明细
        List<PurReceiptItem> items = purReceiptItemRepository.findByReceiptIdOrderByCreateTimeAsc(id);
        if (items.isEmpty()) {
            throw new RuntimeException("入库单明细不能为空");
        }

        // 更新入库单状态为待审核
        receipt.setReceiptStatus(PurReceipt.STATUS_PENDING_AUDIT);
        receipt.setUpdateBy(SecurityUtils.getCurrentUserId());
        purReceiptRepository.save(receipt);

        log.info("入库单提交成功，入库单号: {}", receipt.getReceiptNo());
    }

    @Override
    @Transactional
    public void auditPurReceipt(Long id, boolean approved, String auditRemark) {
        log.info("审核采购入库单，ID: {}, 审核结果: {}", id, approved ? "通过" : "拒绝");

        Optional<PurReceipt> optional = purReceiptRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("入库单不存在");
        }

        PurReceipt receipt = optional.get();

        // 检查入库单状态
        if (receipt.getReceiptStatus() != PurReceipt.STATUS_PENDING_AUDIT) {
            throw new RuntimeException("只有待审核状态的入库单才能审核");
        }

        // 更新审核信息
        receipt.setReceiptStatus(approved ? PurReceipt.STATUS_AUDITED : PurReceipt.STATUS_DRAFT);
        receipt.setAuditId(SecurityUtils.getCurrentUserId());
        receipt.setAuditTime(LocalDateTime.now());
        receipt.setAuditRemark(auditRemark);
        receipt.setUpdateBy(SecurityUtils.getCurrentUserId());

        purReceiptRepository.save(receipt);

        log.info("入库单审核完成，入库单号: {}", receipt.getReceiptNo());
    }

    @Override
    @Transactional
    public void confirmReceipt(Long id) {
        log.info("确认采购入库，ID: {}", id);

        Optional<PurReceipt> optional = purReceiptRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("入库单不存在");
        }

        PurReceipt receipt = optional.get();

        // 检查入库单状态
        if (receipt.getReceiptStatus() != PurReceipt.STATUS_AUDITED) {
            throw new RuntimeException("只有已审核的入库单才能确认入库");
        }

        // 获取入库明细
        List<PurReceiptItem> items = purReceiptItemRepository.findByReceiptIdOrderByCreateTimeAsc(id);
        if (items.isEmpty()) {
            throw new RuntimeException("入库单明细不能为空");
        }

        // 更新入库单状态
        receipt.setReceiptStatus(PurReceipt.STATUS_RECEIVED);
        receipt.setUpdateBy(SecurityUtils.getCurrentUserId());
        purReceiptRepository.save(receipt);

        // 同步库存
        syncStockQuantity(id);

        log.info("采购入库确认完成，入库单号: {}", receipt.getReceiptNo());
    }

    @Override
    @Transactional
    public void cancelPurReceipt(Long id, String reason) {
        log.info("取消采购入库单，ID: {}", id);

        Optional<PurReceipt> optional = purReceiptRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("入库单不存在");
        }

        PurReceipt receipt = optional.get();

        // 检查入库单状态
        if (receipt.getReceiptStatus() == PurReceipt.STATUS_RECEIVED) {
            throw new RuntimeException("已入库的单据无法取消");
        }

        // 更新入库单状态
        receipt.setReceiptStatus(PurReceipt.STATUS_CANCELLED);
        receipt.setAuditRemark(reason);
        receipt.setUpdateBy(SecurityUtils.getCurrentUserId());
        purReceiptRepository.save(receipt);

        log.info("入库单取消成功，入库单号: {}", receipt.getReceiptNo());
    }

    @Override
    @Transactional
    public void batchConfirmReceipt(List<Long> ids) {
        log.info("批量确认入库，数量: {}", ids.size());
        for (Long id : ids) {
            try {
                confirmReceipt(id);
            } catch (Exception e) {
                log.error("确认入库失败，ID: {}, 错误: {}", id, e.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public void batchAuditPurReceipt(List<Long> ids, boolean approved, String auditRemark) {
        log.info("批量审核入库单，数量: {}", ids.size());
        for (Long id : ids) {
            try {
                auditPurReceipt(id, approved, auditRemark);
            } catch (Exception e) {
                log.error("审核入库单失败，ID: {}, 错误: {}", id, e.getMessage());
            }
        }
    }

    @Override
    public List<PurReceiptDto> getReceiptsByOrderId(Long orderId) {
        List<PurReceipt> receipts = purReceiptRepository.findByOrderIdAndIsDeletedOrderByCreateTimeDesc(orderId, 0);
        return receipts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PurReceiptDto> getReceiptsBySupplierId(Long supplierId) {
        List<PurReceipt> receipts = purReceiptRepository.findBySupplierId(supplierId);
        return receipts.stream()
                .filter(r -> r.getIsDeleted() == 0)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurReceiptDto> getReceiptsByWarehouseId(Long warehouseId) {
        List<PurReceipt> receipts = purReceiptRepository.findByWarehouseIdAndIsDeleted(warehouseId, 0);
        return receipts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<PurReceiptDto> getPendingAuditReceipts() {
        List<PurReceipt> receipts = purReceiptRepository.findByReceiptStatusAndIsDeletedOrderByCreateTimeAsc(
                PurReceipt.STATUS_PENDING_AUDIT, 0);
        return receipts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getPurReceiptStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // 待审核数量
        List<PurReceipt> pendingReceipts = purReceiptRepository.findByReceiptStatusAndIsDeleted(PurReceipt.STATUS_PENDING_AUDIT, 0);
        stats.put("pendingCount", (long) pendingReceipts.size());
        
        // 草稿数量
        List<PurReceipt> draftReceipts = purReceiptRepository.findByReceiptStatusAndIsDeleted(PurReceipt.STATUS_DRAFT, 0);
        stats.put("draftCount", (long) draftReceipts.size());
        
        // 今日入库数量和本月入库数量使用简单统计
        stats.put("todayCount", 0L);
        stats.put("monthCount", 0L);
        
        return stats;
    }

    @Override
    public List<Map<String, Object>> getReceiptTrendData(LocalDate startDate, LocalDate endDate) {
        // 简单实现，实际应该按日期分组统计
        List<Map<String, Object>> trendData = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("date", startDate);
        data.put("count", 0);
        data.put("amount", BigDecimal.ZERO);
        trendData.add(data);
        return trendData;
    }

    @Override
    public List<Map<String, Object>> getGoodsReceiptSummary(Long goodsId, LocalDate startDate, LocalDate endDate) {
        // 简单实现，实际应该根据商品统计
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getSupplierReceiptSummary(Long supplierId, LocalDate startDate, LocalDate endDate) {
        // 简单实现，实际应该根据供应商统计
        return new ArrayList<>();
    }

    @Override
    public boolean existsByReceiptNo(String receiptNo) {
        return purReceiptRepository.existsByReceiptNoAndIsDeleted(receiptNo, 0);
    }

    @Override
    public boolean isOrderFullyReceived(Long orderId) {
        // 简单实现，实际应该比较订单数量和已入库数量
        return false;
    }

    @Override
    public Map<String, Object> getOrderReceiptProgress(Long orderId) {
        Map<String, Object> progress = new HashMap<>();
        progress.put("orderId", orderId);
        progress.put("totalQuantity", 0);
        progress.put("receivedQuantity", 0);
        progress.put("percentage", 0);
        return progress;
    }

    @Override
    public BigDecimal getLatestReceiptPrice(Long goodsId, Long skuId) {
        // 查询最近的入库价格
        List<BigDecimal> prices = purReceiptItemRepository.findLatestPurchasePriceByGoodsId(goodsId, skuId);
        return prices.isEmpty() ? BigDecimal.ZERO : prices.get(0);
    }

    @Override
    public List<Map<String, Object>> getStockWarningInfo() {
        // 库存预警信息，简单实现
        return new ArrayList<>();
    }

    @Override
    public String generateReceiptNo() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String prefix = "RK" + today;
        
        // 简单实现：查询所有入库单，计算同前缀的数量
        List<PurReceipt> allReceipts = purReceiptRepository.findAll();
        long count = allReceipts.stream()
                .filter(r -> r.getReceiptNo() != null && r.getReceiptNo().startsWith(prefix))
                .count();
        int nextSeq = (int) count + 1;
        
        return prefix + String.format("%04d", nextSeq);
    }

    @Override
    public List<PurReceiptItemDto> getReceiptItemsByBatch(String batchNumber) {
        List<PurReceiptItem> items = purReceiptItemRepository.findByBatchNumberOrderByCreateTimeDesc(batchNumber);
        return items.stream().map(this::convertItemToDto).collect(Collectors.toList());
    }

    @Override
    public List<PurReceiptItemDto> getExpiringItems(int days) {
        // 简单实现：返回空列表，实际项目中需要添加对应的Repository方法
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public void updateOrderReceiptStatus(Long orderId) {
        // 更新采购订单的入库状态
        log.info("更新采购订单入库状态，订单ID: {}", orderId);
    }

    @Override
    @Transactional
    public void syncStockQuantity(Long receiptId) {
        log.info("同步库存数量，入库单ID: {}", receiptId);
        // TODO: 实际实现库存同步逻辑
    }

    @Override
    @Transactional
    public void rollbackStockQuantity(Long receiptId) {
        log.info("回滚库存数量，入库单ID: {}", receiptId);
        // TODO: 实际实现库存回滚逻辑
    }

    @Override
    public byte[] exportReceiptData(String receiptNo, String orderNo, Long supplierId,
                                  Long warehouseId, Integer receiptStatus, Integer receiptType,
                                  LocalDate startTime, LocalDate endTime) {
        // TODO: 实现导出功能
        return new byte[0];
    }

    @Override
    public List<String> importReceiptData(byte[] fileData) {
        // TODO: 实现导入功能
        return new ArrayList<>();
    }

    @Override
    public List<PurReceiptItemDto> getReceiptItems(Long receiptId) {
        List<PurReceiptItem> items = purReceiptItemRepository.findByReceiptIdOrderByCreateTimeAsc(receiptId);
        return items.stream().map(this::convertItemToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void saveReceiptItems(Long receiptId, List<PurReceiptItemDto> items) {
        for (PurReceiptItemDto itemDto : items) {
            PurReceiptItem item = new PurReceiptItem();
            item.setReceiptId(receiptId);
            
            // 设置入库单号
            Optional<PurReceipt> receiptOpt = purReceiptRepository.findById(receiptId);
            if (receiptOpt.isPresent()) {
                item.setReceiptNo(receiptOpt.get().getReceiptNo());
            }
            
            item.setOrderId(itemDto.getOrderId());
            item.setOrderItemId(itemDto.getOrderItemId());
            item.setGoodsId(itemDto.getGoodsId());
            item.setGoodsName(itemDto.getGoodsName());
            item.setGoodsSpec(itemDto.getGoodsSpec());
            item.setGoodsUnit(itemDto.getGoodsUnit());
            item.setPurchasePrice(itemDto.getPurchasePrice());
            item.setQuantity(itemDto.getQuantity());
            item.setTotalAmount(itemDto.getTotalAmount());
            item.setRemark(itemDto.getRemark());

            purReceiptItemRepository.save(item);
        }
    }

    @Override
    @Transactional
    public void deleteReceiptItems(Long receiptId) {
        purReceiptItemRepository.deleteByReceiptId(receiptId);
    }

    @Override
    public PurReceiptDto copyPurReceipt(Long id) {
        PurReceiptDto original = getPurReceiptById(id);
        original.setId(null);
        original.setReceiptNo(null);
        original.setReceiptStatus(PurReceipt.STATUS_DRAFT);
        return original;
    }

    @Override
    public Map<String, Object> generateReceiptPrintData(Long id) {
        PurReceiptDto receipt = getPurReceiptById(id);
        Map<String, Object> printData = new HashMap<>();
        printData.put("receipt", receipt);
        printData.put("items", receipt.getItems());
        return printData;
    }

    @Override
    public List<Map<String, Object>> getReceiptAuditHistory(Long id) {
        // TODO: 实现审核历史查询
        return new ArrayList<>();
    }

    // 私有辅助方法

    private PurReceiptDto convertToDto(PurReceipt receipt) {
        PurReceiptDto dto = new PurReceiptDto();
        BeanUtils.copyProperties(receipt, dto);

        // 设置供应商信息
        if (receipt.getSupplierId() != null) {
            Optional<PurSupplier> supplierOpt = purSupplierRepository.findById(receipt.getSupplierId());
            if (supplierOpt.isPresent()) {
                PurSupplier supplier = supplierOpt.get();
                dto.setSupplierName(supplier.getSupplierName());
                dto.setSupplierCode(supplier.getSupplierCode());
            }
        }

        // 设置用户信息
        setUserInfo(dto, receipt);

        // 设置入库明细
        List<PurReceiptItem> items = purReceiptItemRepository.findByReceiptIdOrderByCreateTimeAsc(receipt.getId());
        List<PurReceiptItemDto> itemDtos = items.stream().map(this::convertItemToDto).collect(Collectors.toList());
        dto.setItems(itemDtos);

        return dto;
    }

    private PurReceiptItemDto convertItemToDto(PurReceiptItem item) {
        PurReceiptItemDto dto = new PurReceiptItemDto();
        BeanUtils.copyProperties(item, dto);
        return dto;
    }

    private void setUserInfo(PurReceiptDto dto, PurReceipt receipt) {
        // 设置创建人信息
        if (receipt.getCreateBy() != null) {
            Optional<SysUser> createUserOpt = sysUserRepository.findById(receipt.getCreateBy());
            if (createUserOpt.isPresent()) {
                dto.setCreateByName(createUserOpt.get().getUsername());
            }
        }

        // 设置入库人信息
        if (receipt.getReceiptUserId() != null) {
            Optional<SysUser> receiptUserOpt = sysUserRepository.findById(receipt.getReceiptUserId());
            if (receiptUserOpt.isPresent()) {
                dto.setReceiptUserName(receiptUserOpt.get().getUsername());
            }
        }

        // 设置审核人信息
        if (receipt.getAuditId() != null) {
            Optional<SysUser> auditUserOpt = sysUserRepository.findById(receipt.getAuditId());
            if (auditUserOpt.isPresent()) {
                dto.setAuditName(auditUserOpt.get().getUsername());
            }
        }
    }

    private void validateReceiptData(PurReceiptDto purReceiptDto) {
        if (purReceiptDto.getSupplierId() == null) {
            throw new RuntimeException("供应商不能为空");
        }
        
        if (purReceiptDto.getWarehouseId() == null) {
            throw new RuntimeException("仓库不能为空");
        }
        
        if (purReceiptDto.getReceiptTime() == null) {
            throw new RuntimeException("入库日期不能为空");
        }
        
        if (purReceiptDto.getReceiptType() == null) {
            throw new RuntimeException("入库类型不能为空");
        }
    }

    private void updateReceiptTotalAmount(Long receiptId) {
        List<PurReceiptItem> items = purReceiptItemRepository.findByReceiptIdOrderByCreateTimeAsc(receiptId);
        BigDecimal totalAmount = items.stream()
                .map(PurReceiptItem::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Optional<PurReceipt> receiptOpt = purReceiptRepository.findById(receiptId);
        if (receiptOpt.isPresent()) {
            PurReceipt receipt = receiptOpt.get();
            receipt.setTotalAmount(totalAmount);
            receipt.setUpdateBy(SecurityUtils.getCurrentUserId());
            purReceiptRepository.save(receipt);
        }
    }
} 