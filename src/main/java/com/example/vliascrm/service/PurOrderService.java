package com.example.vliascrm.service;

import com.example.vliascrm.dto.PurOrderDto;
import com.example.vliascrm.dto.PurOrderItemDto;
import com.example.vliascrm.entity.PurOrder;
import com.example.vliascrm.entity.PurOrderItem;
import com.example.vliascrm.entity.PurSupplier;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.repository.PurOrderRepository;
import com.example.vliascrm.repository.PurOrderItemRepository;
import com.example.vliascrm.repository.PurSupplierRepository;
import com.example.vliascrm.repository.PurSupplierGoodsRepository;
import com.example.vliascrm.repository.SysUserRepository;
import com.example.vliascrm.entity.PurSupplierGoods;
import com.example.vliascrm.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 采购订单服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PurOrderService {

    private final PurOrderRepository purOrderRepository;
    private final PurOrderItemRepository purOrderItemRepository;
    private final PurSupplierRepository supplierRepository;
    private final PurSupplierGoodsRepository purSupplierGoodsRepository;
    private final SysUserRepository userRepository;

    /**
     * 分页查询采购订单
     */
    public Page<PurOrderDto> getPurOrderPage(String orderNo, Long supplierId, Integer orderStatus,
                                           Long warehouseId, Integer payStatus, Integer deliveryStatus,
                                           Integer receiptStatus, LocalDateTime startTime, LocalDateTime endTime,
                                           int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PurOrder> purOrderPage = purOrderRepository.findPurOrdersWithFilter(
                orderNo, supplierId, orderStatus, warehouseId, payStatus,
                deliveryStatus, receiptStatus, startTime, endTime, pageable);

        return purOrderPage.map(this::convertToDto);
    }

    /**
     * 根据ID获取采购订单详情
     */
    public PurOrderDto getPurOrderById(Long id) {
        Optional<PurOrder> optional = purOrderRepository.findById(id);
        if (optional.isPresent()) {
            PurOrder purOrder = optional.get();
            if (purOrder.getIsDeleted() == 0) {
                return convertToDto(purOrder);
            }
        }
        return null;
    }

    /**
     * 创建采购订单
     */
    @Transactional
    public PurOrderDto createPurOrder(PurOrderDto purOrderDto) {
        // 生成订单号
        String orderNo = generateOrderNo();
        
        // 创建采购订单
        PurOrder purOrder = new PurOrder();
        purOrder.setOrderNo(orderNo);
        purOrder.setSupplierId(purOrderDto.getSupplierId());
        purOrder.setWarehouseId(purOrderDto.getWarehouseId());
        purOrder.setExpectedTime(purOrderDto.getExpectedTime());
        purOrder.setContact(purOrderDto.getContact());
        purOrder.setMobile(purOrderDto.getMobile());
        purOrder.setRemark(purOrderDto.getRemark());
        purOrder.setCreatorId(SecurityUtils.getCurrentUserId());
        
        PurOrder savedOrder = purOrderRepository.save(purOrder);

        // 保存订单明细
        if (purOrderDto.getItems() != null && !purOrderDto.getItems().isEmpty()) {
            saveOrderItems(savedOrder.getId(), purOrderDto.getItems());
            // 计算并更新订单总金额
            updateOrderTotalAmount(savedOrder.getId());
        }

        return convertToDto(savedOrder);
    }

    /**
     * 更新采购订单
     */
    @Transactional
    public PurOrderDto updatePurOrder(Long id, PurOrderDto purOrderDto) {
        Optional<PurOrder> optional = purOrderRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("采购订单不存在");
        }

        PurOrder purOrder = optional.get();

        // 检查订单状态是否允许修改
        if (purOrder.getOrderStatus() != PurOrder.STATUS_DRAFT && 
            purOrder.getOrderStatus() != PurOrder.STATUS_PENDING_AUDIT) {
            throw new RuntimeException("当前状态不允许修改");
        }

        // 更新订单信息
        purOrder.setSupplierId(purOrderDto.getSupplierId());
        purOrder.setWarehouseId(purOrderDto.getWarehouseId());
        purOrder.setExpectedTime(purOrderDto.getExpectedTime());
        purOrder.setContact(purOrderDto.getContact());
        purOrder.setMobile(purOrderDto.getMobile());
        purOrder.setRemark(purOrderDto.getRemark());

        PurOrder savedOrder = purOrderRepository.save(purOrder);

        // 删除原有明细
        List<PurOrderItem> existingItems = purOrderItemRepository.findByOrderIdAndIsDeletedOrderByIdAsc(id, 0);
        for (PurOrderItem item : existingItems) {
            item.setIsDeleted(1);
            purOrderItemRepository.save(item);
        }

        // 保存新明细
        if (purOrderDto.getItems() != null && !purOrderDto.getItems().isEmpty()) {
            saveOrderItems(savedOrder.getId(), purOrderDto.getItems());
            // 重新计算订单总金额
            updateOrderTotalAmount(savedOrder.getId());
        }

        return convertToDto(savedOrder);
    }

    /**
     * 删除采购订单
     */
    @Transactional
    public void deletePurOrder(Long id) {
        Optional<PurOrder> optional = purOrderRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("采购订单不存在");
        }

        PurOrder purOrder = optional.get();

        // 检查订单状态是否允许删除
        if (purOrder.getOrderStatus() != PurOrder.STATUS_DRAFT) {
            throw new RuntimeException("只有草稿状态的订单才能删除");
        }

        // 逻辑删除订单
        purOrder.setIsDeleted(1);
        purOrderRepository.save(purOrder);

        // 逻辑删除明细
        List<PurOrderItem> items = purOrderItemRepository.findByOrderIdAndIsDeletedOrderByIdAsc(id, 0);
        for (PurOrderItem item : items) {
            item.setIsDeleted(1);
            purOrderItemRepository.save(item);
        }
    }

    /**
     * 提交采购订单
     */
    @Transactional
    public void submitPurOrder(Long id) {
        Optional<PurOrder> optional = purOrderRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("采购订单不存在");
        }

        PurOrder purOrder = optional.get();

        // 检查订单状态
        if (purOrder.getOrderStatus() != PurOrder.STATUS_DRAFT) {
            throw new RuntimeException("只有草稿状态的订单才能提交");
        }

        // 检查订单明细
        List<PurOrderItem> items = purOrderItemRepository.findByOrderIdAndIsDeletedOrderByIdAsc(id, 0);
        if (items.isEmpty()) {
            throw new RuntimeException("采购订单明细不能为空");
        }

        // 更新订单状态为待审核
        purOrder.setOrderStatus(PurOrder.STATUS_PENDING_AUDIT);
        purOrderRepository.save(purOrder);
    }

    /**
     * 审核采购订单
     */
    @Transactional
    public void auditPurOrder(Long id, boolean approved, String auditRemark) {
        Optional<PurOrder> optional = purOrderRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("采购订单不存在");
        }

        PurOrder purOrder = optional.get();

        // 检查订单状态
        if (purOrder.getOrderStatus() != PurOrder.STATUS_PENDING_AUDIT) {
            throw new RuntimeException("只有待审核状态的订单才能审核");
        }

        // 更新审核信息
        purOrder.setAuditId(SecurityUtils.getCurrentUserId());
        purOrder.setAuditTime(LocalDateTime.now());
        purOrder.setAuditRemark(auditRemark);

        if (approved) {
            purOrder.setOrderStatus(PurOrder.STATUS_AUDITED);  // 审核通过变为已审核状态
        } else {
            purOrder.setOrderStatus(PurOrder.STATUS_DRAFT);
        }

        purOrderRepository.save(purOrder);
    }

    /**
     * 取消采购订单
     */
    @Transactional
    public void cancelPurOrder(Long id, String reason) {
        Optional<PurOrder> optional = purOrderRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("采购订单不存在");
        }

        PurOrder purOrder = optional.get();

        // 检查订单状态
        if (purOrder.getOrderStatus() == PurOrder.STATUS_COMPLETED || 
            purOrder.getOrderStatus() == PurOrder.STATUS_CANCELLED) {
            throw new RuntimeException("当前状态不允许取消");
        }

        // 更新订单状态
        purOrder.setOrderStatus(PurOrder.STATUS_CANCELLED);
        purOrder.setAuditRemark(reason);
        purOrderRepository.save(purOrder);
    }

    /**
     * 获取采购订单统计信息
     */
    public Map<String, Object> getPurOrderStatistics() {
        // 订单状态统计
        List<Object[]> statusStats = purOrderRepository.countByOrderStatus();
        Map<Integer, Long> statusCount = statusStats.stream()
                .collect(Collectors.toMap(
                        arr -> (Integer) arr[0],
                        arr -> (Long) arr[1]
                ));

        // 支付状态统计
        List<Object[]> payStats = purOrderRepository.countByPayStatus();
        Map<Integer, Long> payCount = payStats.stream()
                .collect(Collectors.toMap(
                        arr -> (Integer) arr[0],
                        arr -> (Long) arr[1]
                ));

        // 本月采购金额
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime monthEnd = LocalDateTime.now();
        Double monthAmount = purOrderRepository.sumTotalAmountByCreateTimeBetween(monthStart, monthEnd);

        return Map.of(
                "statusCount", statusCount,
                "payCount", payCount,
                "monthAmount", monthAmount != null ? monthAmount : 0.0
        );
    }

    /**
     * 获取需要跟进的订单
     */
    public List<PurOrderDto> getOrdersNeedFollow() {
        List<PurOrder> orders = purOrderRepository.findOrdersNeedFollow();
        return orders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * 获取逾期订单
     */
    public List<PurOrderDto> getOverdueOrders() {
        List<PurOrder> orders = purOrderRepository.findOverdueOrders();
        return orders.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * 保存订单明细
     */
    private void saveOrderItems(Long orderId, List<PurOrderItemDto> itemDtos) {
        // 获取订单信息，特别是订单号
        Optional<PurOrder> orderOptional = purOrderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new RuntimeException("订单不存在");
        }
        String orderNo = orderOptional.get().getOrderNo();
        
        for (PurOrderItemDto itemDto : itemDtos) {
            PurOrderItem item = new PurOrderItem();
            item.setOrderId(orderId);
            item.setOrderNo(orderNo); // 设置订单号
            
            // 如果goodsId为空，但有supplierGoodsId，则通过供应商商品获取goodsId
            Long goodsId = itemDto.getGoodsId();
            if (goodsId == null && itemDto.getSupplierGoodsId() != null) {
                goodsId = getGoodsIdBySupplierGoods(itemDto.getSupplierGoodsId());
            }
            
            item.setGoodsId(goodsId);
            item.setGoodsCode(itemDto.getGoodsCode());
            item.setGoodsName(itemDto.getGoodsName());
            item.setSpecification(itemDto.getSpecification());
            item.setUnit(itemDto.getUnit());
            item.setQuantity(itemDto.getQuantity() != null ? itemDto.getQuantity().intValue() : 0);
            item.setUnitPrice(itemDto.getUnitPrice());
            item.setTotalPrice(itemDto.getTotalPrice());
            item.setRemark(itemDto.getRemark());
            item.setCreateBy(SecurityUtils.getCurrentUserId());
            item.setUpdateBy(SecurityUtils.getCurrentUserId());

            purOrderItemRepository.save(item);
        }
    }

    /**
     * 更新订单总金额
     */
    private void updateOrderTotalAmount(Long orderId) {
        BigDecimal totalAmount = purOrderItemRepository.sumTotalPriceByOrderId(orderId);
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }

        Optional<PurOrder> optional = purOrderRepository.findById(orderId);
        if (optional.isPresent()) {
            PurOrder purOrder = optional.get();
            purOrder.setTotalAmount(totalAmount);
            purOrderRepository.save(purOrder);
        }
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        String prefix = "PO";
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String suffix = String.valueOf(System.currentTimeMillis() % 1000);
        return prefix + timestamp + suffix;
    }

    /**
     * 通过供应商商品ID获取商品ID
     */
    private Long getGoodsIdBySupplierGoods(Long supplierGoodsId) {
        Optional<PurSupplierGoods> supplierGoods = purSupplierGoodsRepository.findById(supplierGoodsId);
        return supplierGoods.map(PurSupplierGoods::getGoodsId).orElse(null);
    }

    /**
     * 转换为DTO
     */
    private PurOrderDto convertToDto(PurOrder purOrder) {
        PurOrderDto dto = new PurOrderDto();
        dto.setId(purOrder.getId());
        dto.setOrderNo(purOrder.getOrderNo());
        dto.setSupplierId(purOrder.getSupplierId());
        dto.setOrderStatus(purOrder.getOrderStatus());
        dto.setWarehouseId(purOrder.getWarehouseId());
        dto.setTotalAmount(purOrder.getTotalAmount());
        dto.setPayStatus(purOrder.getPayStatus());
        dto.setPaidAmount(purOrder.getPaidAmount());
        dto.setExpectedTime(purOrder.getExpectedTime());
        dto.setDeliveryStatus(purOrder.getDeliveryStatus());
        dto.setReceiptStatus(purOrder.getReceiptStatus());
        dto.setContact(purOrder.getContact());
        dto.setMobile(purOrder.getMobile());
        dto.setRemark(purOrder.getRemark());
        dto.setCreatorId(purOrder.getCreatorId());
        dto.setAuditId(purOrder.getAuditId());
        dto.setAuditTime(purOrder.getAuditTime());
        dto.setAuditRemark(purOrder.getAuditRemark());
        dto.setCreateTime(purOrder.getCreateTime());
        dto.setUpdateTime(purOrder.getUpdateTime());
        dto.setCreateBy(purOrder.getCreateBy());
        dto.setUpdateBy(purOrder.getUpdateBy());

        // 设置供应商信息
        if (purOrder.getSupplierId() != null) {
            Optional<PurSupplier> supplierOpt = supplierRepository.findById(purOrder.getSupplierId());
            if (supplierOpt.isPresent()) {
                PurSupplier supplier = supplierOpt.get();
                dto.setSupplierName(supplier.getSupplierName());
                dto.setSupplierCode(supplier.getSupplierCode());
            }
        }

        // 设置制单人信息
        if (purOrder.getCreatorId() != null) {
            Optional<SysUser> userOpt = userRepository.findById(purOrder.getCreatorId());
            if (userOpt.isPresent()) {
                dto.setCreatorName(userOpt.get().getUsername());
            }
        }

        // 设置审核人信息
        if (purOrder.getAuditId() != null) {
            Optional<SysUser> userOpt = userRepository.findById(purOrder.getAuditId());
            if (userOpt.isPresent()) {
                dto.setAuditName(userOpt.get().getUsername());
            }
        }

        // 设置订单明细
        List<PurOrderItem> items = purOrderItemRepository.findByOrderIdAndIsDeletedOrderByIdAsc(purOrder.getId(), 0);
        List<PurOrderItemDto> itemDtos = items.stream().map(this::convertItemToDto).collect(Collectors.toList());
        dto.setItems(itemDtos);
        dto.setItemCount(itemDtos.size());

        // 计算统计信息
        BigDecimal totalQty = items.stream()
                .map(item -> item.getQuantity() != null ? BigDecimal.valueOf(item.getQuantity()) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal receivedQty = items.stream()
                .map(item -> item.getReceivedQuantity() != null ? BigDecimal.valueOf(item.getReceivedQuantity()) : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        dto.setTotalQuantity(totalQty);
        dto.setReceivedQuantity(receivedQty);
        dto.setPendingQuantity(dto.getTotalQuantity().subtract(dto.getReceivedQuantity()));

        return dto;
    }

    /**
     * 转换明细为DTO
     */
    private PurOrderItemDto convertItemToDto(PurOrderItem item) {
        PurOrderItemDto dto = new PurOrderItemDto();
        dto.setId(item.getId());
        dto.setOrderId(item.getOrderId());
        dto.setGoodsId(item.getGoodsId());
        dto.setGoodsCode(item.getGoodsCode());
        dto.setGoodsName(item.getGoodsName());
        dto.setSpecification(item.getSpecification());
        dto.setUnit(item.getUnit());
        dto.setQuantity(item.getQuantity() != null ? BigDecimal.valueOf(item.getQuantity()) : BigDecimal.ZERO);
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getTotalPrice());
        dto.setReceivedQuantity(item.getReceivedQuantity() != null ? BigDecimal.valueOf(item.getReceivedQuantity()) : BigDecimal.ZERO);
        dto.setRemark(item.getRemark());
        dto.setCreateTime(item.getCreateTime());
        dto.setUpdateTime(item.getUpdateTime());
        dto.setCreateBy(item.getCreateBy());
        dto.setUpdateBy(item.getUpdateBy());

        return dto;
    }

    /**
     * 更新采购订单支付状态
     */
    @Transactional
    public void updatePaymentStatus(Long id, Integer payStatus, Double paidAmount, String remark) {
        Optional<PurOrder> optional = purOrderRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("采购订单不存在");
        }

        PurOrder purOrder = optional.get();

        // 检查订单状态是否允许修改支付状态
        if (purOrder.getOrderStatus() == PurOrder.STATUS_CANCELLED) {
            throw new RuntimeException("已取消的订单不能修改支付状态");
        }

        // 验证支付状态
        if (payStatus != null && (payStatus < 0 || payStatus > 2)) {
            throw new RuntimeException("无效的支付状态");
        }

        // 验证已付金额
        if (paidAmount != null) {
            BigDecimal paidAmountBd = BigDecimal.valueOf(paidAmount);
            BigDecimal totalAmount = purOrder.getTotalAmount() != null ? purOrder.getTotalAmount() : BigDecimal.ZERO;
            
            if (paidAmountBd.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("已付金额不能为负数");
            }
            
            if (paidAmountBd.compareTo(totalAmount) > 0) {
                throw new RuntimeException("已付金额不能超过订单总金额");
            }
            
            purOrder.setPaidAmount(paidAmountBd);
            
            // 根据已付金额自动计算支付状态
            if (paidAmountBd.compareTo(BigDecimal.ZERO) == 0) {
                purOrder.setPayStatus(PurOrder.PAY_STATUS_UNPAID);
            } else if (paidAmountBd.compareTo(totalAmount) == 0) {
                purOrder.setPayStatus(PurOrder.PAY_STATUS_PAID);
            } else {
                purOrder.setPayStatus(PurOrder.PAY_STATUS_PARTIAL);
            }
        } else if (payStatus != null) {
            purOrder.setPayStatus(payStatus);
        }

        // 根据支付状态更新订单状态
        if (purOrder.getPayStatus() == PurOrder.PAY_STATUS_PAID) {
            // 已支付 -> 订单状态更新为已下单（如果当前状态小于已下单）
            if (purOrder.getOrderStatus() < PurOrder.STATUS_ORDERED) {
                purOrder.setOrderStatus(PurOrder.STATUS_ORDERED);
            }
        }

        purOrder.setUpdateBy(SecurityUtils.getCurrentUserId());
        purOrderRepository.save(purOrder);
    }

    /**
     * 更新采购订单入库状态
     */
    @Transactional
    public void updateReceiptStatus(Long id, Integer receiptStatus, String remark) {
        Optional<PurOrder> optional = purOrderRepository.findById(id);
        if (!optional.isPresent() || optional.get().getIsDeleted() == 1) {
            throw new RuntimeException("采购订单不存在");
        }

        PurOrder purOrder = optional.get();

        // 检查订单状态是否允许修改入库状态
        if (purOrder.getOrderStatus() == PurOrder.STATUS_CANCELLED) {
            throw new RuntimeException("已取消的订单不能修改入库状态");
        }

        // 只有已下单或之后的状态才能修改入库状态
        if (purOrder.getOrderStatus() < PurOrder.STATUS_ORDERED) {
            throw new RuntimeException("订单未下单，不能修改入库状态");
        }

        // 验证入库状态
        if (receiptStatus != null && (receiptStatus < 0 || receiptStatus > 2)) {
            throw new RuntimeException("无效的入库状态");
        }

        if (receiptStatus != null) {
            purOrder.setReceiptStatus(receiptStatus);
            
            // 根据入库状态更新订单状态
            if (receiptStatus == PurOrder.RECEIPT_STATUS_RECEIVED) {
                // 已入库 -> 订单完成
                purOrder.setOrderStatus(PurOrder.STATUS_COMPLETED);
            } else if (receiptStatus == PurOrder.RECEIPT_STATUS_PARTIAL) {
                // 部分入库 -> 部分入库状态
                purOrder.setOrderStatus(PurOrder.STATUS_PARTIAL_RECEIPT);
            }
        }

        purOrder.setUpdateBy(SecurityUtils.getCurrentUserId());
        purOrderRepository.save(purOrder);
    }
} 