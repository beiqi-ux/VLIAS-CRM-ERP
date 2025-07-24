package com.example.vliascrm.entity.member;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 积分记录实体类
 */
@Data
@Entity
@Table(name = "mem_points")
@EntityListeners(AuditingEntityListener.class)
public class MemPoints {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 积分变动值
     */
    @Column(name = "points", nullable = false)
    private Integer points;

    /**
     * 类型 1-获得 2-消费
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 操作类型 1-注册 2-签到 3-购物 4-评价 5-兑换 6-过期 7-管理员操作
     */
    @Column(name = "operation_type", nullable = false)
    private Integer operationType;

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
     * 描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 变动前积分
     */
    @Column(name = "before_points")
    private Integer beforePoints;

    /**
     * 变动后积分
     */
    @Column(name = "after_points")
    private Integer afterPoints;

    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;
} 