package com.example.vliascrm.entity.crm;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 客户联系人实体类
 */
@Data
@Entity
@Table(name = "crm_contact")
@EqualsAndHashCode(callSuper = true)
public class CrmContact extends BaseEntity {

    /**
     * 客户ID
     */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /**
     * 联系人姓名
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /**
     * 职位
     */
    @Column(name = "position", length = 50)
    private String position;

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
     * 是否主联系人 0-否 1-是
     */
    @Column(name = "is_primary", columnDefinition = "tinyint default 0")
    private Integer isPrimary = 0;

    /**
     * 性别 0-未知 1-男 2-女
     */
    @Column(name = "gender", columnDefinition = "tinyint default 0")
    private Integer gender = 0;

    /**
     * 生日
     */
    @Column(name = "birthday")
    private LocalDate birthday;

    /**
     * 微信号
     */
    @Column(name = "wechat", length = 50)
    private String wechat;

    /**
     * QQ号
     */
    @Column(name = "qq", length = 20)
    private String qq;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
    private String remark;
} 