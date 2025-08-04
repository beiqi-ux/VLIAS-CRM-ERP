package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 系统权限实体类
 */
@Data
@Entity
@Table(name = "sys_permission")
@EqualsAndHashCode(callSuper = true)
public class SysPermission extends BaseEntity {

    /**
     * 权限名称
     */
    @Column(name = "permission_name", nullable = false, length = 50)
    private String permissionName;

    /**
     * 权限编码
     */
    @Column(name = "permission_code", nullable = false, length = 50, unique = true)
    private String permissionCode;

    /**
     * 权限类型 1-一级权限(模块) 2-二级权限(操作)
     */
    @Column(name = "permission_type", nullable = false)
    private Integer permissionType;

    /**
     * 父权限ID
     */
    @Column(name = "parent_id")
    private Long parentId = 0L;

    /**
     * 关联菜单ID
     */
    @Column(name = "menu_id")
    private Long menuId;

    /**
     * 权限描述
     */
    @Column(length = 200)
    private String description;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column
    private Integer status = 1;

    /**
     * 是否核心权限 0-否 1-是
     */
    @Column(name = "is_core")
    private Integer isCore = 0;
} 