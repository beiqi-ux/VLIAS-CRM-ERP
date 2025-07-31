package com.example.vliascrm.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色数据传输对象
 */
@Data
public class RoleDTO {

    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;

    /**
     * 所属组织ID
     */
    private Long orgId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 菜单ID列表
     */
    private List<Long> menuIds;

    /**
     * 权限ID列表
     */
    private List<Long> permissionIds;
} 