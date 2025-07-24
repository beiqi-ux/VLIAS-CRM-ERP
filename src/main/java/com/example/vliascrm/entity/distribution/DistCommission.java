package com.example.vliascrm.entity.distribution;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 佣金记录实体类
 */
@Data
@Entity
@Table(name = "dist_commission")
@EntityListeners(AuditingEntityListener.class)
public class DistCommission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分销员ID
     */
    @Column(name = "distributor_id", nullable = false)
    private Long distributorId;

    /**
     * 佣金金额
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * 类型 1-获得 2-提现 3-退回 4-冻结 5-解冻
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 关联订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 关联订单编号
     */
    @Column(name = "order_no", length = 50)
    private String orderNo;

    /**
     * 提现记录ID
     */
    @Column(name = "withdraw_id")
    private Long withdrawId;

    /**
     * 描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 变动前金额
     */
    @Column(name = "before_amount", precision = 10, scale = 2)
    private BigDecimal beforeAmount;

    /**
     * 变动后金额
     */
    @Column(name = "after_amount", precision = 10, scale = 2)
    private BigDecimal afterAmount;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 