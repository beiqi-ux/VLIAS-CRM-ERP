package com.example.vliascrm.entity.aftersales;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退货明细实体类
 */
@Data
@Entity
@Table(name = "serv_return_item")
@EntityListeners(AuditingEntityListener.class)
public class ServReturnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 退货ID
     */
    @Column(name = "return_id", nullable = false)
    private Long returnId;

    /**
     * 退货单号
     */
    @Column(name = "return_no", nullable = false, length = 50)
    private String returnNo;

    /**
     * 订单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 订单明细ID
     */
    @Column(name = "order_item_id", nullable = false)
    private Long orderItemId;

    /**
     * 商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name", nullable = false, length = 100)
    private String goodsName;

    /**
     * 商品图片
     */
    @Column(name = "goods_image", length = 255)
    private String goodsImage;

    /**
     * SKU ID
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * SKU名称
     */
    @Column(name = "sku_name", length = 100)
    private String skuName;

    /**
     * 商品规格，JSON格式
     */
    @Column(name = "goods_spec", length = 200)
    private String goodsSpec;

    /**
     * 单价
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 小计金额
     */
    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;

    /**
     * 是否收到货物 0-否 1-是
     */
    @Column(name = "is_received", columnDefinition = "tinyint default 0")
    private Integer isReceived = 0;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 