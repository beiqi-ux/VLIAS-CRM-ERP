package com.example.vliascrm.entity.promotion;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 拼团成员实体类
 */
@Data
@Entity
@Table(name = "prom_group_buy_member")
@EntityListeners(AuditingEntityListener.class)
public class PromGroupBuyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 拼团记录ID
     */
    @Column(name = "group_record_id", nullable = false)
    private Long groupRecordId;

    /**
     * 团编号
     */
    @Column(name = "group_no", nullable = false, length = 50)
    private String groupNo;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

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
     * 是否团长 0-否 1-是
     */
    @Column(name = "is_leader", columnDefinition = "tinyint default 0")
    private Integer isLeader = 0;

    /**
     * 状态 1-待支付 2-拼团中 3-已成团 4-未成团
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

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