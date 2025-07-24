package com.example.vliascrm.entity.order;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单支付表实体类
 */
@Data
@Entity
@Table(name = "ord_payment")
@EntityListeners(AuditingEntityListener.class)
public class OrdPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 支付流水号
     */
    @Column(name = "pay_no", nullable = false, length = 50)
    private String payNo;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 订单编号
     */
    @Column(name = "order_no", length = 50)
    private String orderNo;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 支付方式 1-微信 2-支付宝 3-银联 4-余额
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 支付金额
     */
    @Column(name = "pay_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal payAmount;

    /**
     * 支付状态 0-未支付 1-已支付 2-已退款 3-部分退款
     */
    @Column(name = "pay_status", columnDefinition = "tinyint default 0")
    private Integer payStatus = 0;

    /**
     * 支付交易号
     */
    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    /**
     * 支付时间
     */
    @Column(name = "pay_time")
    private LocalDateTime payTime;

    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal refundAmount = BigDecimal.ZERO;

    /**
     * 客户端IP
     */
    @Column(name = "client_ip", length = 50)
    private String clientIp;

    /**
     * 设备信息
     */
    @Column(name = "device_info", length = 100)
    private String deviceInfo;

    /**
     * 支付渠道额外信息
     */
    @Column(name = "pay_channel_extra", columnDefinition = "text")
    private String payChannelExtra;

    /**
     * 回调内容
     */
    @Column(name = "callback_content", columnDefinition = "text")
    private String callbackContent;

    /**
     * 回调时间
     */
    @Column(name = "callback_time")
    private LocalDateTime callbackTime;

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