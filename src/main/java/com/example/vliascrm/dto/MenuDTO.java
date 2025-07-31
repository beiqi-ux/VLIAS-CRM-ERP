package com.example.vliascrm.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单数据传输对象
 */
@Data
public class MenuDTO {

    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单编码
     */
    private String menuCode;

    /**
     * 菜单类型 1-目录 2-菜单 3-按钮
     */
    private Integer menuType;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 是否显示 0-隐藏 1-显示
     */
    private Integer visible;

    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;

    /**
     * 权限标识
     */
    private String permissionCode;

    /**
     * 是否外链 0-否 1-是
     */
    private Integer isFrame;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子菜单列表
     */
    private List<MenuDTO> children = new ArrayList<>();
} 