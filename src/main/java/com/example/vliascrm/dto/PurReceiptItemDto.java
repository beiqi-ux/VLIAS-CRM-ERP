package com.example.vliascrm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购入库单明细DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PurReceiptItemDto {

    /**
     * ID
     */
    private Long id;

    /**
     * 入库单ID
     */
    private Long receiptId;

    /**
     * 入库单号
     */
    private String receiptNo;

    /**
     * 采购单ID
     */
    private Long orderId;

    /**
     * 采购单项ID
     */
    private Long orderItemId;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * SKU ID
     */
    private Long skuId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * SKU名称
     */
    private String skuName;

    /**
     * 商品规格
     */
    private String goodsSpec;

    /**
     * 单位
     */
    private String goodsUnit;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 批次号
     */
    private String batchNumber;

    /**
     * 生产日期
     */
    private LocalDate productionDate;

    /**
     * 到期日期
     */
    private LocalDate expiryDate;

    /**
     * 采购价
     */
    private BigDecimal purchasePrice;

    /**
     * 入库数量
     */
    private BigDecimal quantity;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 库位ID
     */
    private Long locationId;

    /**
     * 库位名称
     */
    private String locationName;

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
     * 订单中采购数量
     */
    private BigDecimal orderQuantity;

    /**
     * 订单中已入库数量
     */
    private BigDecimal receivedQuantity;

    /**
     * 订单中待入库数量
     */
    private BigDecimal pendingQuantity;
} 