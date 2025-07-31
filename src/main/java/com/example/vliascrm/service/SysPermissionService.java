package com.example.vliascrm.service;

import com.example.vliascrm.dto.PermissionDTO;
import com.example.vliascrm.entity.SysPermission;

import java.util.List;

/**
 * 权限服务接口
 */
public interface SysPermissionService {

    /**
     * 创建权限
     * @param permissionDTO 权限信息
     * @return 创建后的权限
     */
    SysPermission createPermission(PermissionDTO permissionDTO);

    /**
     * 更新权限
     * @param id 权限ID
     * @param permissionDTO 权限信息
     * @return 更新后的权限
     */
    SysPermission updatePermission(Long id, PermissionDTO permissionDTO);

    /**
     * 删除权限
     * @param id 权限ID
     */
    void deletePermission(Long id);

    /**
     * 根据ID获取权限
     * @param id 权限ID
     * @return 权限信息
     */
    SysPermission getPermissionById(Long id);

    /**
     * 获取所有权限
     * @return 权限列表
     */
    List<SysPermission> getAllPermissions();

    /**
     * 获取权限树
     * @return 权限树
     */
    List<PermissionDTO> getPermissionTree();

    /**
     * 根据角色ID获取权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermission> getPermissionsByRoleId(Long roleId);

    /**
     * 根据用户ID获取权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermission> getPermissionsByUserId(Long userId);
} 