package com.example.vliascrm.entity.order;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细表实体类
 */
@Data
@Entity
@Table(name = "ord_order_item")
@EntityListeners(AuditingEntityListener.class)
public class OrdOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 订单编号
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
     * 商品图片
     */
    @Column(name = "goods_image", length = 255)
    private String goodsImage;

    /**
     * 商品规格
     */
    @Column(name = "goods_spec", length = 200)
    private String goodsSpec;

    /**
     * 商品单价
     */
    @Column(name = "goods_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal goodsPrice;

    /**
     * 成本价
     */
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    /**
     * 购买数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 总金额
     */
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    @Column(name = "discount_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * 实际金额
     */
    @Column(name = "real_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal realAmount;

    /**
     * 退款状态 0-未退款 1-退款中 2-已退款
     */
    @Column(name = "refund_status", columnDefinition = "tinyint default 0")
    private Integer refundStatus = 0;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal refundAmount = BigDecimal.ZERO;

    /**
     * 评价状态 0-未评价 1-已评价
     */
    @Column(name = "comment_status", columnDefinition = "tinyint default 0")
    private Integer commentStatus = 0;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 