package com.example.vliascrm.controller;

import com.example.vliascrm.dto.PurOrderDto;
import com.example.vliascrm.service.PurOrderService;
import com.example.vliascrm.common.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 采购订单控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurOrderController {

    private final PurOrderService purOrderService;

    /**
     * 分页查询采购订单
     */
    @GetMapping
    @PreAuthorize("hasAuthority('purchase-order-management:list')")
    public Result<Page<PurOrderDto>> getPurOrderPage(
            @RequestParam(required = false) String orderNo,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Integer orderStatus,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Integer payStatus,
            @RequestParam(required = false) Integer deliveryStatus,
            @RequestParam(required = false) Integer receiptStatus,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<PurOrderDto> result = purOrderService.getPurOrderPage(
                    orderNo, supplierId, orderStatus, warehouseId, payStatus,
                    deliveryStatus, receiptStatus, startTime, endTime, page, size);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询采购订单列表失败", e);
            return Result.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取采购订单详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('purchase-order-management:view')")
    public Result<PurOrderDto> getPurOrderById(@PathVariable Long id) {
        try {
            PurOrderDto purOrder = purOrderService.getPurOrderById(id);
            if (purOrder != null) {
                return Result.success(purOrder);
            } else {
                return Result.error("采购订单不存在");
            }
        } catch (Exception e) {
            log.error("获取采购订单详情失败", e);
            return Result.error("获取详情失败：" + e.getMessage());
        }
    }

    /**
     * 创建采购订单
     */
    @PostMapping
    @PreAuthorize("hasAuthority('purchase-order-management:create')")
    public Result<PurOrderDto> createPurOrder(@RequestBody PurOrderDto purOrderDto) {
        try {
            PurOrderDto result = purOrderService.createPurOrder(purOrderDto);
            return Result.success(result);
        } catch (Exception e) {
            log.error("创建采购订单失败", e);
            return Result.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新采购订单
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('purchase-order-management:update')")
    public Result<PurOrderDto> updatePurOrder(@PathVariable Long id, @RequestBody PurOrderDto purOrderDto) {
        try {
            PurOrderDto result = purOrderService.updatePurOrder(id, purOrderDto);
            return Result.success(result);
        } catch (Exception e) {
            log.error("更新采购订单失败", e);
            return Result.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除采购订单
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('purchase-order-management:delete')")
    public Result<Void> deletePurOrder(@PathVariable Long id) {
        try {
            purOrderService.deletePurOrder(id);
            return Result.success(null);
        } catch (Exception e) {
            log.error("删除采购订单失败", e);
            return Result.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 提交采购订单
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('purchase-order-management:submit')")
    public Result<Void> submitPurOrder(@PathVariable Long id) {
        try {
            purOrderService.submitPurOrder(id);
            return Result.success(null);
        } catch (Exception e) {
            log.error("提交采购订单失败", e);
            return Result.error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 审核采购订单
     */
    @PostMapping("/{id}/audit")
    @PreAuthorize("hasAuthority('purchase-order-management:audit')")
    public Result<Void> auditPurOrder(@PathVariable Long id, 
                                     @RequestParam boolean approved,
                                     @RequestParam(required = false) String auditRemark) {
        try {
            purOrderService.auditPurOrder(id, approved, auditRemark);
            return Result.success(null);
        } catch (Exception e) {
            log.error("审核采购订单失败", e);
            return Result.error("审核失败：" + e.getMessage());
        }
    }

    /**
     * 取消采购订单
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('purchase-order-management:update')")
    public Result<Void> cancelPurOrder(@PathVariable Long id, 
                                      @RequestParam(required = false) String reason) {
        try {
            purOrderService.cancelPurOrder(id, reason);
            return Result.success(null);
        } catch (Exception e) {
            log.error("取消采购订单失败", e);
            return Result.error("取消失败：" + e.getMessage());
        }
    }

    /**
     * 获取采购订单统计信息
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('purchase-order-management:statistics')")
    public Result<Map<String, Object>> getPurOrderStatistics() {
        try {
            Map<String, Object> statistics = purOrderService.getPurOrderStatistics();
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取采购订单统计信息失败", e);
            return Result.error("获取统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取需要跟进的订单
     */
    @GetMapping("/follow")
    @PreAuthorize("hasAuthority('purchase-order-management:follow')")
    public Result<List<PurOrderDto>> getOrdersNeedFollow() {
        try {
            List<PurOrderDto> orders = purOrderService.getOrdersNeedFollow();
            return Result.success(orders);
        } catch (Exception e) {
            log.error("获取需要跟进的订单失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取逾期订单
     */
    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('purchase-order-management:follow')")
    public Result<List<PurOrderDto>> getOverdueOrders() {
        try {
            List<PurOrderDto> orders = purOrderService.getOverdueOrders();
            return Result.success(orders);
        } catch (Exception e) {
            log.error("获取逾期订单失败", e);
            return Result.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除采购订单
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('purchase-order-management:delete')")
    public Result<Void> batchDeletePurOrders(@RequestBody List<Long> ids) {
        try {
            for (Long id : ids) {
                purOrderService.deletePurOrder(id);
            }
            return Result.success(null);
        } catch (Exception e) {
            log.error("批量删除采购订单失败", e);
            return Result.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 复制采购订单
     */
    @PostMapping("/{id}/copy")
    @PreAuthorize("hasAuthority('purchase-order-management:create')")
    public Result<PurOrderDto> copyPurOrder(@PathVariable Long id) {
        try {
            PurOrderDto original = purOrderService.getPurOrderById(id);
            if (original == null) {
                return Result.error("原订单不存在");
            }

            // 清空ID和订单号，重新创建
            original.setId(null);
            original.setOrderNo(null);
            original.setOrderStatus(1); // 设置为草稿状态
            original.setAuditId(null);
            original.setAuditTime(null);
            original.setAuditRemark(null);

            // 清空明细ID
            if (original.getItems() != null) {
                original.getItems().forEach(item -> {
                    item.setId(null);
                    item.setOrderId(null);
                    item.setReceivedQuantity(null);
                });
            }

            PurOrderDto result = purOrderService.createPurOrder(original);
            return Result.success(result);
        } catch (Exception e) {
            log.error("复制采购订单失败", e);
            return Result.error("复制失败：" + e.getMessage());
        }
    }
} 