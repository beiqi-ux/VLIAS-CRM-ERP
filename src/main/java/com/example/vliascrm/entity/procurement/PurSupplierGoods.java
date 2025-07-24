package com.example.vliascrm.entity.procurement;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 供应商商品实体类
 */
@Data
@Entity
@Table(name = "pur_supplier_goods")
@EqualsAndHashCode(callSuper = true)
public class PurSupplierGoods extends BaseEntity {

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
     * 采购价
     */
    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    /**
     * 最小采购量
     */
    @Column(name = "min_purchase_qty", columnDefinition = "int default 1")
    private Integer minPurchaseQty = 1;

    /**
     * 交货天数
     */
    @Column(name = "delivery_day")
    private Integer deliveryDay;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;
} 