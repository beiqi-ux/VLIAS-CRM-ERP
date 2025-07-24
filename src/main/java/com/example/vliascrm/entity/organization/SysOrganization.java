package com.example.vliascrm.entity.organization;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 组织机构实体类
 */
@Data
@Entity
@Table(name = "sys_organization")
@EqualsAndHashCode(callSuper = true)
public class SysOrganization extends BaseEntity {

    /**
     * 父组织ID
     */
    @Column(name = "parent_id", columnDefinition = "bigint default 0")
    private Long parentId = 0L;

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
    @Column(name = "leader", length = 50)
    private String leader;

    /**
     * 联系电话
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 地址
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;
} 