package com.example.vliascrm.entity.product;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品SKU实体类
 */
@Data
@Entity
@Table(name = "prod_sku")
@EqualsAndHashCode(callSuper = true)
public class ProdSku extends BaseEntity {

    /**
     * 商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * SKU编码
     */
    @Column(name = "sku_code", length = 50)
    private String skuCode;

    /**
     * 规格值JSON
     */
    @Column(name = "spec_values", columnDefinition = "text")
    private String specValues;

    /**
     * 原价
     */
    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    /**
     * 售价
     */
    @Column(name = "selling_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    /**
     * 成本价
     */
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    /**
     * 库存数量
     */
    @Column(name = "stock_qty", columnDefinition = "int default 0")
    private Integer stockQty = 0;

    /**
     * 库存预警值
     */
    @Column(name = "warn_stock", columnDefinition = "int default 0")
    private Integer warnStock = 0;

    /**
     * SKU图片
     */
    @Column(name = "sku_image", length = 255)
    private String skuImage;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;
} 