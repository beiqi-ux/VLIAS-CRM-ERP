package com.example.vliascrm.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购退货明细DTO
 */
@Data
public class PurReturnItemDto {

    /**
     * ID
     */
    private Long id;

    /**
     * 退货单ID
     */
    private Long returnId;

    /**
     * 退货单号
     */
    private String returnNo;

    /**
     * 关联入库单ID
     */
    private Long receiptId;

    /**
     * 关联入库单项ID
     */
    private Long receiptItemId;

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
     * 批次号
     */
    private String batchNumber;

    /**
     * 采购价
     */
    private BigDecimal purchasePrice;

    /**
     * 退货数量
     */
    private Integer quantity;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * SKU编码
     */
    private String skuCode;

    /**
     * 品牌名称
     */
    private String brandName;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 可退数量（基于入库数量）
     */
    private Integer availableQuantity;

    /**
     * 已退数量
     */
    private Integer returnedQuantity;
} 
 