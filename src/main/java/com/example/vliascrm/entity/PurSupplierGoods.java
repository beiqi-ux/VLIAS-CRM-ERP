package com.example.vliascrm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 供应商商品关联实体类
 * 对应数据库表：pur_supplier_goods
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pur_supplier_goods")
public class PurSupplierGoods {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    /**
     * 商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * SKU ID
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 供应商商品编码
     */
    @Column(name = "supplier_goods_code", length = 50)
    private String supplierGoodsCode;

    /**
     * 供应商商品名称
     */
    @Column(name = "supplier_goods_name", length = 100)
    private String supplierGoodsName;

    /**
     * 采购价格
     */
    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    /**
     * 最小采购量
     */
    @Column(name = "min_purchase_qty")
    private Integer minPurchaseQty;

    /**
     * 交货天数
     */
    @Column(name = "delivery_day")
    private Integer deliveryDay;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人ID
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 更新人ID
     */
    @Column(name = "update_by")
    private Long updateBy;

    /**
     * 是否删除 0-否 1-是
     */
    @Column(name = "is_deleted")
    private Integer isDeleted = 0;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
} 