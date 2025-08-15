package com.example.vliascrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
     * 权限类型 1-一级权限(模块) 2-二级权限(子模块) 3-三级权限(操作)
     */
    private Integer permissionType;

    /**
     * 权限层级深度 1-一级 2-二级 3-三级
     */
    private Integer levelDepth;

    /**
     * 权限路径，格式：/parent1/parent2/current
     */
    private String permissionPath;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 关联菜单ID
     */
    private Long menuId;

    /**
     * 关联资源ID（通用资源关联）
     */
    private Long resourceId;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;

    /**
     * 排序字段
     */
    private Integer sortOrder;

    /**
     * 是否核心权限 0-否 1-是
     */
    private Integer isCore;

    /**
     * 创建时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String createBy;

    /**
     * 更新人
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String updateBy;

    /**
     * 是否删除
     */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean isDeleted;

    /**
     * 子权限列表
     */
    private List<PermissionDTO> children;
} 