package com.example.vliascrm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购订单DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PurOrderDto {

    /**
     * 采购单ID
     */
    private Long id;

    /**
     * 采购单号
     */
    private String orderNo;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 订单状态 1-草稿 2-待审核 3-已审核 4-已下单 5-部分入库 6-已完成 7-已取消
     */
    private Integer orderStatus;

    /**
     * 订单状态名称
     */
    private String orderStatusName;

    /**
     * 入库仓库ID
     */
    private Long warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 支付状态 0-未支付 1-部分支付 2-已支付
     */
    private Integer payStatus;

    /**
     * 支付状态名称
     */
    private String payStatusName;

    /**
     * 已付金额
     */
    private BigDecimal paidAmount;

    /**
     * 预计到货日期
     */
    private LocalDate expectedTime;

    /**
     * 发货状态 0-未发货 1-部分发货 2-已发货
     */
    private Integer deliveryStatus;

    /**
     * 发货状态名称
     */
    private String deliveryStatusName;

    /**
     * 入库状态 0-未入库 1-部分入库 2-已入库
     */
    private Integer receiptStatus;

    /**
     * 入库状态名称
     */
    private String receiptStatusName;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 备注
     */
    private String remark;

    /**
     * 制单人ID
     */
    private Long creatorId;

    /**
     * 制单人姓名
     */
    private String creatorName;

    /**
     * 审核人ID
     */
    private Long auditId;

    /**
     * 审核人姓名
     */
    private String auditName;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 采购订单明细列表
     */
    private List<PurOrderItemDto> items;

    /**
     * 明细数量
     */
    private Integer itemCount;

    /**
     * 总数量
     */
    private BigDecimal totalQuantity;

    /**
     * 已入库数量
     */
    private BigDecimal receivedQuantity;

    /**
     * 未入库数量
     */
    private BigDecimal pendingQuantity;

    // 状态映射方法
    public String getOrderStatusName() {
        if (orderStatus == null) return "";
        switch (orderStatus) {
            case 1: return "草稿";
            case 2: return "待审核";
            case 3: return "已审核";
            case 4: return "已下单";
            case 5: return "部分入库";
            case 6: return "已完成";
            case 7: return "已取消";
            default: return "未知";
        }
    }

    public String getPayStatusName() {
        if (payStatus == null) return "";
        switch (payStatus) {
            case 0: return "未支付";
            case 1: return "部分支付";
            case 2: return "已支付";
            default: return "未知";
        }
    }

    public String getDeliveryStatusName() {
        if (deliveryStatus == null) return "";
        switch (deliveryStatus) {
            case 0: return "未发货";
            case 1: return "部分发货";
            case 2: return "已发货";
            default: return "未知";
        }
    }

    public String getReceiptStatusName() {
        if (receiptStatus == null) return "";
        switch (receiptStatus) {
            case 0: return "未入库";
            case 1: return "部分入库";
            case 2: return "已入库";
            default: return "未知";
        }
    }
} 