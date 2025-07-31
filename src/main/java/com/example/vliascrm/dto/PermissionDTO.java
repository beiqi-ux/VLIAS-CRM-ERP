package com.example.vliascrm.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 权限数据传输对象
 */
@Data
public class PermissionDTO {

    /**
     * 权限ID
     */
    private Long id;

    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 权限编码
     */
    private String permissionCode;

    /**
     * 权限类型 1-一级权限(模块) 2-二级权限(操作)
     */
    private Integer permissionType;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 关联菜单ID
     */
    private Long menuId;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子权限列表
     */
    private List<PermissionDTO> children;
} 