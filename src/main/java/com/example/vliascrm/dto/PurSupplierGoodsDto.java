package com.example.vliascrm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 供应商商品关联数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurSupplierGoodsDto {

    /**
     * 主键ID
     */
    private Long id;

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
     * 供应商商品编码
     */
    private String supplierGoodsCode;

    /**
     * 供应商商品名称
     */
    private String supplierGoodsName;

    /**
     * 采购价格
     */
    private BigDecimal purchasePrice;

    /**
     * 最小采购量
     */
    private Integer minPurchaseQty;

    /**
     * 交货天数
     */
    private Integer deliveryDay;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 更新人ID
     */
    private Long updateBy;

    // 保留这些字段用于兼容性，但实际不使用
    private Long goodsId;
    private String goodsName;
    private String goodsCode;
    private Long skuId;
    private String skuName;

    /**
     * 用于JPQL查询的构造函数（简化版本）
     * @param id 主键ID
     * @param supplierId 供应商ID
     * @param supplierName 供应商名称
     * @param supplierCode 供应商编码
     * @param supplierGoodsCode 供应商商品编码
     * @param supplierGoodsName 供应商商品名称
     * @param purchasePrice 采购价格
     * @param minPurchaseQty 最小采购量
     * @param deliveryDay 交货天数
     * @param createTime 创建时间
     * @param updateTime 更新时间
     */
    public PurSupplierGoodsDto(Long id, Long supplierId, String supplierName, String supplierCode,
                              String supplierGoodsCode, String supplierGoodsName,
                              BigDecimal purchasePrice, Integer minPurchaseQty, Integer deliveryDay,
                              LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierCode = supplierCode;
        this.supplierGoodsCode = supplierGoodsCode;
        this.supplierGoodsName = supplierGoodsName;
        this.purchasePrice = purchasePrice;
        this.minPurchaseQty = minPurchaseQty;
        this.deliveryDay = deliveryDay;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
} 