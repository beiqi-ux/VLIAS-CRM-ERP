package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 商品SKU实体类
 */
@Data
@Entity
@Table(name = "prod_sku")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class ProdSku extends BaseEntity {

    /**
     * 关联的商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * SKU编码
     */
    @Column(name = "sku_code", nullable = false, length = 100, unique = true)
    private String skuCode;

    /**
     * SKU名称
     */
    @Column(name = "sku_name", nullable = false, length = 200)
    private String skuName;

    /**
     * 销售价格
     */
    @Column(name = "sale_price", precision = 10, scale = 2)
    private BigDecimal salePrice;

    /**
     * 原价
     */
    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    /**
     * 最低价
     */
    @Column(name = "min_price", precision = 10, scale = 2)
    private BigDecimal minPrice;

    /**
     * 成本价格
     */
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    /**
     * 库存数量
     */
    @Column(name = "stock_quantity")
    private Integer stockQty = 0;

    /**
     * 预警库存
     */
    @Column(name = "warning_stock")
    private Integer warnStock = 0;

    /**
     * 重量(克)
     */
    @Column(name = "weight")
    private Integer weight;

    /**
     * 体积(立方厘米)
     */
    @Column(name = "volume")
    private Integer volume;

    /**
     * 规格属性JSON (如: {"颜色": "红色", "尺码": "L"})
     */
    @Column(name = "spec_attrs", columnDefinition = "TEXT")
    private String specAttrs;

    /**
     * 规格值JSON
     */
    @Column(name = "spec_values", columnDefinition = "TEXT")
    private String specValues;

    /**
     * SKU图片
     */
    @Column(name = "sku_image")
    private String skuImage;

    /**
     * 条码
     */
    @Column(name = "barcode")
    private String barcode;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 销售数量
     */
    @Column(name = "sale_qty")
    private Integer saleQty = 0;

    /**
     * 状态 0-下架 1-上架
     */
    @Column(name = "status")
    private Integer status = 1;

    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 关联的商品对象（非持久化字段）
     */
    @Transient
    private ProdGoods goods;

    // 为了兼容Service中的方法调用，添加一些别名方法

    public BigDecimal getSellingPrice() {
        return this.salePrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.salePrice = sellingPrice;
    }

    public Integer getStockQuantity() {
        return this.stockQty;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQty = stockQuantity;
    }

    public Integer getWarningStock() {
        return this.warnStock;
    }

    public void setWarningStock(Integer warningStock) {
        this.warnStock = warningStock;
    }

    public Integer getSort() {
        return this.sortOrder;
    }

    public void setSort(Integer sort) {
        this.sortOrder = sort;
    }
} 