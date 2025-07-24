package com.example.vliascrm.entity.distribution;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 分销订单实体类
 */
@Data
@Entity
@Table(name = "dist_order")
@EntityListeners(AuditingEntityListener.class)
public class DistOrder {

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
     * 订单金额
     */
    @Column(name = "order_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderAmount;

    /**
     * 分销员ID
     */
    @Column(name = "distributor_id", nullable = false)
    private Long distributorId;

    /**
     * 下单会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 分销层级 1-一级 2-二级 3-三级
     */
    @Column(name = "level", columnDefinition = "int default 1")
    private Integer level = 1;

    /**
     * 佣金比例
     */
    @Column(name = "commission_rate", nullable = false, precision = 5, scale = 4)
    private BigDecimal commissionRate;

    /**
     * 佣金金额
     */
    @Column(name = "commission_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal commissionAmount;

    /**
     * 状态 1-未结算 2-已结算 3-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 结算ID
     */
    @Column(name = "settlement_id")
    private Long settlementId;

    /**
     * 结算时间
     */
    @Column(name = "settlement_time")
    private LocalDateTime settlementTime;

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