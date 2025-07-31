package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.PermissionDTO;
import com.example.vliascrm.entity.SysPermission;
import com.example.vliascrm.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/sys/permissions")
@RequiredArgsConstructor
public class SysPermissionController {

    private final SysPermissionService permissionService;

    /**
     * 创建权限
     * @param permissionDTO 权限信息
     * @return 创建后的权限
     */
    @PostMapping
    public ApiResponse<SysPermission> createPermission(@RequestBody PermissionDTO permissionDTO) {
        return ApiResponse.success(permissionService.createPermission(permissionDTO));
    }

    /**
     * 更新权限
     * @param id 权限ID
     * @param permissionDTO 权限信息
     * @return 更新后的权限
     */
    @PutMapping("/{id}")
    public ApiResponse<SysPermission> updatePermission(@PathVariable Long id, @RequestBody PermissionDTO permissionDTO) {
        return ApiResponse.success(permissionService.updatePermission(id, permissionDTO));
    }

    /**
     * 删除权限
     * @param id 权限ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ApiResponse.success(null);
    }

    /**
     * 根据ID获取权限
     * @param id 权限ID
     * @return 权限信息
     */
    @GetMapping("/{id}")
    public ApiResponse<SysPermission> getPermissionById(@PathVariable Long id) {
        return ApiResponse.success(permissionService.getPermissionById(id));
    }

    /**
     * 获取所有权限
     * @return 权限列表
     */
    @GetMapping
    public ApiResponse<List<SysPermission>> getAllPermissions() {
        return ApiResponse.success(permissionService.getAllPermissions());
    }

    /**
     * 获取权限树
     * @return 权限树
     */
    @GetMapping("/tree")
    public ApiResponse<List<PermissionDTO>> getPermissionTree() {
        return ApiResponse.success(permissionService.getPermissionTree());
    }

    /**
     * 根据角色ID获取权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    @GetMapping("/roles/{roleId}")
    public ApiResponse<List<SysPermission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        return ApiResponse.success(permissionService.getPermissionsByRoleId(roleId));
    }

    /**
     * 根据用户ID获取权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    @GetMapping("/users/{userId}")
    public ApiResponse<List<SysPermission>> getPermissionsByUserId(@PathVariable Long userId) {
        return ApiResponse.success(permissionService.getPermissionsByUserId(userId));
    }
} 