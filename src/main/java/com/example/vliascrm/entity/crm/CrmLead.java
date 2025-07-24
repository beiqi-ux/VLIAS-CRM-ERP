package com.example.vliascrm.entity.crm;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 客户线索实体类
 */
@Data
@Entity
@Table(name = "crm_lead")
@EqualsAndHashCode(callSuper = true)
public class CrmLead extends BaseEntity {

    /**
     * 线索名称
     */
    @Column(name = "lead_name", nullable = false, length = 100)
    private String leadName;

    /**
     * 联系人姓名
     */
    @Column(name = "contact_name", length = 50)
    private String contactName;

    /**
     * 手机号
     */
    @Column(name = "mobile", length = 20)
    private String mobile;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 线索来源ID
     */
    @Column(name = "source_id")
    private Long sourceId;

    /**
     * 状态 1-未跟进 2-跟进中 3-已转化 4-已关闭
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 负责人ID
     */
    @Column(name = "owner_user_id")
    private Long ownerUserId;

    /**
     * 线索内容
     */
    @Column(name = "content", columnDefinition = "text")
    private String content;

    /**
     * 地址
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 是否已转化 0-否 1-是
     */
    @Column(name = "is_transformed", columnDefinition = "tinyint default 0")
    private Integer isTransformed = 0;

    /**
     * 转化后客户ID
     */
    @Column(name = "customer_id")
    private Long customerId;
} 