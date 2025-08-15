package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统菜单实体类
 */
@Data
@Entity
@Table(name = "sys_menu")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SysMenu extends BaseEntity {

    /**
     * 父菜单ID
     */
    @Column(name = "parent_id")
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
    @Column(length = 200)
    private String path;

    /**
     * 组件路径
     */
    @Column(length = 200)
    private String component;

    /**
     * 图标
     */
    @Column(length = 100)
    private String icon;

    /**
     * 排序
     */
    @Column
    private Integer sort = 0;

    /**
     * 是否显示 0-隐藏 1-显示
     */
    @Column
    private Integer visible = 1;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column
    private Integer status = 1;

    /**
     * 权限标识
     */
    @Column(name = "permission_code", length = 50)
    private String permissionCode;

    /**
     * 是否外链 0-否 1-是
     */
    @Column(name = "is_frame")
    private Integer isFrame = 0;

    /**
     * 子菜单列表
     */
    @Transient
    private List<SysMenu> children = new ArrayList<>();
} 