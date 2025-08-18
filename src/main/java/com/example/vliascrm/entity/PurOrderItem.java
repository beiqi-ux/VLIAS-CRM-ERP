package com.example.vliascrm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购订单明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pur_order_item")
public class PurOrderItem {

    /**
     * 明细ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 采购单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 采购单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private Long goodsId;

    /**
     * 商品编码
     */
    @Column(name = "goods_code")
    private String goodsCode;

    /**
     * SKU ID
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;

    /**
     * SKU名称
     */
    @Column(name = "sku_name")
    private String skuName;

    /**
     * 规格型号
     */
    @Column(name = "goods_spec")
    private String specification;

    /**
     * 单位
     */
    @Column(name = "goods_unit")
    private String unit;

    /**
     * 采购数量
     */
    @Column(name = "quantity")
    private Integer quantity;

    /**
     * 采购单价
     */
    @Column(name = "purchase_price")
    private BigDecimal unitPrice;

    /**
     * 小计金额
     */
    @Column(name = "total_amount")
    private BigDecimal totalPrice;

    /**
     * 已入库数量
     */
    @Column(name = "received_qty")
    private Integer receivedQuantity;

    /**
     * 剩余数量
     */
    @Column(name = "remain_qty")
    private Integer remainQuantity;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @Column(name = "create_by", updatable = false)
    private Long createBy;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;

    /**
     * 是否删除 0-否 1-是
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;
}

