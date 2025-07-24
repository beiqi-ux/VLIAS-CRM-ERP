package com.example.vliascrm.entity.system;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体类
 */
@Data
@Entity
@Table(name = "sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    /**
     * 角色编码
     */
    @Column(name = "role_code", nullable = false, length = 50, unique = true)
    private String roleCode;

    /**
     * 角色描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 所属组织ID
     */
    @Column(name = "org_id")
    private Long orgId;
} 