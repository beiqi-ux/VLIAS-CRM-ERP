package com.example.vliascrm.entity.system;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单实体类
 */
@Data
@Entity
@Table(name = "sys_menu")
@EqualsAndHashCode(callSuper = true)
public class SysMenu extends BaseEntity {

    /**
     * 父菜单ID
     */
    @Column(name = "parent_id", columnDefinition = "bigint default 0")
    private Long parentId = 0L;

    /**
     * 菜单名称
     */
    @Column(name = "menu_name", nullable = false, length = 50)
    private String menuName;

    /**
     * 菜单编码
     */
    @Column(name = "menu_code", nullable = false, length = 50, unique = true)
    private String menuCode;

    /**
     * 菜单类型 1-目录 2-菜单 3-按钮
     */
    @Column(name = "menu_type", nullable = false)
    private Integer menuType;

    /**
     * 路由地址
     */
    @Column(name = "path", length = 200)
    private String path;

    /**
     * 组件路径
     */
    @Column(name = "component", length = 200)
    private String component;

    /**
     * 图标
     */
    @Column(name = "icon", length = 100)
    private String icon;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 是否显示 0-隐藏 1-显示
     */
    @Column(name = "visible", columnDefinition = "tinyint default 1")
    private Integer visible = 1;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 权限标识
     */
    @Column(name = "permission_code", length = 50)
    private String permissionCode;

    /**
     * 是否外链 0-否 1-是
     */
    @Column(name = "is_frame", columnDefinition = "tinyint default 0")
    private Integer isFrame = 0;
} 