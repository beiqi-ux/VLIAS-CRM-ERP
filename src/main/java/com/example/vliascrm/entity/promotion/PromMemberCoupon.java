package com.example.vliascrm.entity.promotion;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员优惠券实体类
 */
@Data
@Entity
@Table(name = "prom_member_coupon")
@EntityListeners(AuditingEntityListener.class)
public class PromMemberCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 优惠券ID
     */
    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    /**
     * 优惠券码
     */
    @Column(name = "coupon_code", nullable = false, length = 50)
    private String couponCode;

    /**
     * 状态 1-未使用 2-已使用 3-已过期
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 获取方式 1-主动领取 2-系统发放 3-活动获得
     */
    @Column(name = "get_type", columnDefinition = "tinyint default 1")
    private Integer getType = 1;

    /**
     * 获取时间
     */
    @Column(name = "get_time", nullable = false)
    private LocalDateTime getTime;

    /**
     * 使用时间
     */
    @Column(name = "use_time")
    private LocalDateTime useTime;

    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    /**
     * 使用订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 使用订单号
     */
    @Column(name = "order_no", length = 50)
    private String orderNo;

    /**
     * 实际优惠金额
     */
    @Column(name = "use_amount", precision = 10, scale = 2)
    private BigDecimal useAmount;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 