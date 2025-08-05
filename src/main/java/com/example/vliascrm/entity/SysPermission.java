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
    @Column(name = "permission_name", nullable = false, length = 100)
    private String permissionName;

    /**
     * 权限编码
     */
    @Column(name = "permission_code", nullable = false, length = 200, unique = true)
    private String permissionCode;

    /**
     * 权限路径，格式：/parent1/parent2/current
     */
    @Column(name = "permission_path", length = 500)
    private String permissionPath;

    /**
     * 权限类型 1-一级权限(模块) 2-二级权限(子模块) 3-三级权限(操作)
     */
    @Column(name = "permission_type", nullable = false)
    private Integer permissionType;

    /**
     * 权限层级深度 1-一级 2-二级 3-三级
     */
    @Column(name = "level_depth")
    private Integer levelDepth = 1;

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
     * 排序字段
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

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