package com.example.vliascrm.entity.member;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 会员成长值实体类
 */
@Data
@Entity
@Table(name = "mem_growth")
@EntityListeners(AuditingEntityListener.class)
public class MemGrowth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 成长值变动
     */
    @Column(name = "growth", nullable = false)
    private Integer growth;

    /**
     * 类型 1-获得 2-扣减
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 操作类型 1-注册 2-购物 3-评价 4-签到 5-管理员操作
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
     * 变动前成长值
     */
    @Column(name = "before_growth")
    private Integer beforeGrowth;

    /**
     * 变动后成长值
     */
    @Column(name = "after_growth")
    private Integer afterGrowth;

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