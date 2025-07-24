package com.example.vliascrm.entity.procurement;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购订单明细实体类
 */
@Data
@Entity
@Table(name = "pur_order_item")
@EntityListeners(AuditingEntityListener.class)
public class PurOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 采购单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 采购单号
     */
    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

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
     * 商品名称
     */
    @Column(name = "goods_name", nullable = false, length = 100)
    private String goodsName;

    /**
     * SKU名称
     */
    @Column(name = "sku_name", length = 100)
    private String skuName;

    /**
     * 商品规格
     */
    @Column(name = "goods_spec", length = 200)
    private String goodsSpec;

    /**
     * 单位
     */
    @Column(name = "goods_unit", length = 20)
    private String goodsUnit;

    /**
     * 采购价
     */
    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    /**
     * 采购数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 总金额
     */
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 已入库数量
     */
    @Column(name = "received_qty", columnDefinition = "int default 0")
    private Integer receivedQty = 0;

    /**
     * 待入库数量
     */
    @Column(name = "remain_qty", columnDefinition = "int default 0")
    private Integer remainQty = 0;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;
} 