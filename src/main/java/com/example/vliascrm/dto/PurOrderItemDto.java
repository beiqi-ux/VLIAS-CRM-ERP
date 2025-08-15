package com.example.vliascrm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购订单明细DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PurOrderItemDto {

    /**
     * 明细ID
     */
    private Long id;

    /**
     * 采购单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 单位
     */
    private String unit;

    /**
     * 采购数量
     */
    private BigDecimal quantity;

    /**
     * 采购单价
     */
    private BigDecimal unitPrice;

    /**
     * 小计金额
     */
    private BigDecimal totalPrice;

    /**
     * 已入库数量
     */
    private BigDecimal receivedQuantity;

    /**
     * 未入库数量
     */
    private BigDecimal pendingQuantity;

    /**
     * 备注
     */
    private String remark;

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
     * 计算未入库数量
     */
    public BigDecimal getPendingQuantity() {
        if (quantity == null) return BigDecimal.ZERO;
        if (receivedQuantity == null) return quantity;
        return quantity.subtract(receivedQuantity);
    }

    /**
     * 是否已完全入库
     */
    public boolean isFullyReceived() {
        return getPendingQuantity().compareTo(BigDecimal.ZERO) <= 0;
    }

    /**
     * 入库进度百分比
     */
    public BigDecimal getReceiptProgress() {
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (receivedQuantity == null) {
            return BigDecimal.ZERO;
        }
        return receivedQuantity.divide(quantity, 2, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100"));
    }
} 