package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 组织机构实体类
 */
@Data
@Entity
@Table(name = "sys_organization")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysOrganization extends BaseEntity {

    /**
     * 父组织ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 组织名称
     */
    @Column(name = "org_name", nullable = false, length = 50)
    private String orgName;

    /**
     * 组织编码
     */
    @Column(name = "org_code", nullable = false, length = 50, unique = true)
    private String orgCode;

    /**
     * 组织类型 1-集团 2-公司 3-分公司 4-部门
     */
    @Column(name = "org_type")
    private Integer orgType;

    /**
     * 负责人
     */
    @Column(length = 50)
    private String leader;

    /**
     * 联系电话
     */
    @Column(length = 20)
    private String phone;

    /**
     * 邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 地址
     */
    @Column(length = 200)
    private String address;

    /**
     * 排序
     */
    @Column
    private Integer sort;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column
    private Integer status;

    /**
     * 备注
     */
    @Column(columnDefinition = "TEXT")
    private String remark;
} 