package com.example.vliascrm.service;

import com.example.vliascrm.dto.RoleDTO;
import com.example.vliascrm.entity.SysRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 角色服务接口
 */
public interface SysRoleService {

    /**
     * 创建角色
     * @param roleDTO 角色信息
     * @return 创建后的角色
     */
    SysRole createRole(RoleDTO roleDTO);

    /**
     * 更新角色
     * @param id 角色ID
     * @param roleDTO 角色信息
     * @return 更新后的角色
     */
    SysRole updateRole(Long id, RoleDTO roleDTO);

    /**
     * 删除角色
     * @param id 角色ID
     */
    void deleteRole(Long id);

    /**
     * 根据ID获取角色
     * @param id 角色ID
     * @return 角色信息
     */
    SysRole getRoleById(Long id);

    /**
     * 获取所有角色
     * @return 角色列表
     */
    List<SysRole> getAllRoles();

    /**
     * 分页获取角色
     * @param keyword 关键字
     * @param pageable 分页参数
     * @return 角色分页数据
     */
    Page<SysRole> getRolePage(String keyword, Pageable pageable);

    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);

    /**
     * 获取角色的权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> getRolePermissionIds(Long roleId);

    /**
     * 获取用户的角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> getUserRoles(Long userId);

    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void assignUserRoles(Long userId, List<Long> roleIds);

    /**
     * 获取用户的角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    List<Long> getUserRoleIds(Long userId);
} 