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
     * 获取权限树
     * @return 权限树
     */
    @GetMapping("/tree")
    public ApiResponse<List<PermissionDTO>> getPermissionTree() {
        return ApiResponse.success(permissionService.getPermissionTree());
    }

    /**
     * 获取权限管理专用的权限树（包括禁用的权限）
     * @return 权限树
     */
    @GetMapping("/tree/admin")
    public ApiResponse<List<PermissionDTO>> getPermissionTreeForAdmin() {
        if (permissionService instanceof com.example.vliascrm.service.impl.SysPermissionServiceImpl) {
            return ApiResponse.success(((com.example.vliascrm.service.impl.SysPermissionServiceImpl) permissionService).getPermissionTreeForAdmin());
        }
        // 如果不是预期的实现类，返回普通权限树
        return ApiResponse.success(permissionService.getPermissionTree());
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

    /**
     * 根据权限类型获取权限列表
     * @param permissionType 权限类型
     * @return 权限列表
     */
    @GetMapping("/type/{permissionType}")
    public ApiResponse<List<PermissionDTO>> getPermissionsByType(@PathVariable Integer permissionType) {
        if (permissionService instanceof com.example.vliascrm.service.impl.SysPermissionServiceImpl) {
            return ApiResponse.success(((com.example.vliascrm.service.impl.SysPermissionServiceImpl) permissionService).getPermissionsByType(permissionType));
        }
        return ApiResponse.success(new java.util.ArrayList<>());
    }

    /**
     * 获取权限的所有子孙权限
     * @param permissionId 权限ID
     * @return 子孙权限列表
     */
    @GetMapping("/{permissionId}/descendants")
    public ApiResponse<List<PermissionDTO>> getDescendantPermissions(@PathVariable Long permissionId) {
        if (permissionService instanceof com.example.vliascrm.service.impl.SysPermissionServiceImpl) {
            return ApiResponse.success(((com.example.vliascrm.service.impl.SysPermissionServiceImpl) permissionService).getDescendantPermissions(permissionId));
        }
        return ApiResponse.success(new java.util.ArrayList<>());
    }

    /**
     * 检查权限继承关系
     * @param userPermissions 用户权限编码列表
     * @param requiredPermission 需要检查的权限编码
     * @return 是否有权限
     */
    @PostMapping("/check-inheritance")
    public ApiResponse<Boolean> checkPermissionWithInheritance(
            @RequestBody java.util.Map<String, Object> requestBody) {
        @SuppressWarnings("unchecked")
        java.util.List<String> userPermissions = (java.util.List<String>) requestBody.get("userPermissions");
        String requiredPermission = (String) requestBody.get("requiredPermission");
        
        if (permissionService instanceof com.example.vliascrm.service.impl.SysPermissionServiceImpl) {
            boolean hasPermission = ((com.example.vliascrm.service.impl.SysPermissionServiceImpl) permissionService)
                    .hasPermissionWithInheritance(userPermissions, requiredPermission);
            return ApiResponse.success(hasPermission);
        }
        return ApiResponse.success(false);
    }
} 