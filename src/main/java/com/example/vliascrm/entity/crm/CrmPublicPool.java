package com.example.vliascrm.entity.crm;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 客户公海池实体类
 */
@Data
@Entity
@Table(name = "crm_public_pool")
@EntityListeners(AuditingEntityListener.class)
public class CrmPublicPool implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户ID
     */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /**
     * 进入公海原因 1-超时未跟进 2-手动释放 3-离职释放
     */
    @Column(name = "reason_type")
    private Integer reasonType;

    /**
     * 进入原因
     */
    @Column(name = "reason", length = 200)
    private String reason;

    /**
     * 前负责人ID
     */
    @Column(name = "previous_owner_id")
    private Long previousOwnerId;

    /**
     * 进入公海时间
     */
    @Column(name = "enter_time", nullable = false)
    private LocalDateTime enterTime;

    /**
     * 状态 1-待领取 2-已领取
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 领取人ID
     */
    @Column(name = "receive_user_id")
    private Long receiveUserId;

    /**
     * 领取时间
     */
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "create_by")
    private Long createBy;
} 