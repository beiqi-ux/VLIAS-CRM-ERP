package com.example.vliascrm.entity.order;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体类
 */
@Data
@Entity
@Table(name = "ord_order")
@EqualsAndHashCode(callSuper = true)
public class OrdOrder extends BaseEntity {

    /**
     * 订单编号
     */
    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 订单类型 1-普通 2-秒杀 3-拼团 4-积分兑换
     */
    @Column(name = "order_type", columnDefinition = "tinyint default 1")
    private Integer orderType = 1;

    /**
     * 订单来源 1-PC 2-APP 3-小程序 4-H5
     */
    @Column(name = "order_source", columnDefinition = "tinyint default 1")
    private Integer orderSource = 1;

    /**
     * 订单状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已取消 6-已关闭
     */
    @Column(name = "order_status", columnDefinition = "tinyint default 1")
    private Integer orderStatus = 1;

    /**
     * 支付状态 0-未支付 1-已支付 2-已退款 3-部分退款
     */
    @Column(name = "pay_status", columnDefinition = "tinyint default 0")
    private Integer payStatus = 0;

    /**
     * 发货状态 0-未发货 1-已发货 2-已收货
     */
    @Column(name = "delivery_status", columnDefinition = "tinyint default 0")
    private Integer deliveryStatus = 0;

    /**
     * 订单总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * 商品总金额
     */
    @Column(name = "goods_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal goodsAmount = BigDecimal.ZERO;

    /**
     * 运费金额
     */
    @Column(name = "freight_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal freightAmount = BigDecimal.ZERO;

    /**
     * 优惠金额
     */
    @Column(name = "discount_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * 优惠券金额
     */
    @Column(name = "coupon_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal couponAmount = BigDecimal.ZERO;

    /**
     * 积分抵扣金额
     */
    @Column(name = "points_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal pointsAmount = BigDecimal.ZERO;

    /**
     * 实付金额
     */
    @Column(name = "pay_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal payAmount = BigDecimal.ZERO;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private LocalDateTime payTime;

    /**
     * 支付方式 1-微信 2-支付宝 3-银联 4-余额
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 支付交易号
     */
    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    /**
     * 收货人
     */
    @Column(name = "consignee", length = 50)
    private String consignee;

    /**
     * 联系电话
     */
    @Column(name = "mobile", length = 20)
    private String mobile;

    /**
     * 省份
     */
    @Column(name = "province", length = 50)
    private String province;

    /**
     * 城市
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * 区县
     */
    @Column(name = "district", length = 50)
    private String district;

    /**
     * 详细地址
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 邮编
     */
    @Column(name = "zip_code", length = 20)
    private String zipCode;

    /**
     * 物流公司
     */
    @Column(name = "delivery_company", length = 50)
    private String deliveryCompany;

    /**
     * 物流单号
     */
    @Column(name = "delivery_no", length = 50)
    private String deliveryNo;

    /**
     * 发货时间
     */
    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    /**
     * 收货时间
     */
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

    /**
     * 完成时间
     */
    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    /**
     * 取消时间
     */
    @Column(name = "cancel_time")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    /**
     * 买家备注
     */
    @Column(name = "buyer_remark", length = 200)
    private String buyerRemark;

    /**
     * 卖家备注
     */
    @Column(name = "seller_remark", length = 200)
    private String sellerRemark;

    /**
     * 发票类型 0-不开发票 1-个人 2-企业
     */
    @Column(name = "invoice_type", columnDefinition = "tinyint default 0")
    private Integer invoiceType = 0;

    /**
     * 发票抬头
     */
    @Column(name = "invoice_title", length = 100)
    private String invoiceTitle;

    /**
     * 税号
     */
    @Column(name = "invoice_tax_no", length = 50)
    private String invoiceTaxNo;

    /**
     * 发票内容
     */
    @Column(name = "invoice_content", length = 100)
    private String invoiceContent;

    /**
     * 发票邮箱
     */
    @Column(name = "invoice_email", length = 100)
    private String invoiceEmail;
} 