package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.List;

/**
 * 系统角色实体类
 */
@Data
@Entity
@Table(name = "sys_role")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends BaseEntity {

    /**
     * 角色名称
     */
    @Column(nullable = false, length = 50)
    private String roleName;

    /**
     * 角色编码
     */
    @Column(nullable = false, length = 50, unique = true)
    private String roleCode;

    /**
     * 角色描述
     */
    @Column(length = 200)
    private String description;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column
    private Integer status = 1;

    /**
     * 所属组织ID
     */
    @Column(name = "org_id")
    private Long orgId;

    /**
     * 角色关联的权限列表（非持久化字段）
     */
    @Transient
    private List<SysPermission> permissions;

    /**
     * 菜单ID列表（非持久化字段，用于前端传参）
     */
    @Transient
    private List<Long> menuIds;

    /**
     * 权限ID列表（非持久化字段，用于前端传参）
     */
    @Transient
    private List<Long> permissionIds;
} 