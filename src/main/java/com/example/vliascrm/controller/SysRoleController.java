package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.RoleDTO;
import com.example.vliascrm.entity.SysRole;
import com.example.vliascrm.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Set;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/sys/roles")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    // 检查是否为内置角色
    private boolean isBuiltinRole(String roleCode) {
        Set<String> builtinRoles = Set.of("SUPER_ADMIN", "ADMIN", "MANAGER", "EMPLOYEE", "WAREHOUSE");
        return builtinRoles.contains(roleCode);
    }

    /**
     * 创建角色
     * @param roleDTO 角色信息
     * @return 创建后的角色
     */
    @PostMapping
    @PreAuthorize("hasAuthority('role-management:create')")
    public ApiResponse<SysRole> createRole(@RequestBody RoleDTO roleDTO) {
        return ApiResponse.success(roleService.createRole(roleDTO));
    }

    /**
     * 更新角色
     * @param id 角色ID
     * @param roleDTO 角色信息
     * @return 更新后的角色
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('role-management:edit')")
    public ApiResponse<SysRole> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        // 检查是否为内置角色，如果是则不允许修改状态
        SysRole existingRole = roleService.getRoleById(id);
        if (isBuiltinRole(existingRole.getRoleCode())) {
            // 保持原有状态，不允许修改
            roleDTO.setStatus(existingRole.getStatus());
        }
        return ApiResponse.success(roleService.updateRole(id, roleDTO));
    }

    /**
     * 删除角色
     * @param id 角色ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role-management:delete')")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        // 检查是否为内置角色，如果是则不允许删除
        SysRole existingRole = roleService.getRoleById(id);
        if (isBuiltinRole(existingRole.getRoleCode())) {
            return ApiResponse.failure("内置角色不允许删除");
        }
        roleService.deleteRole(id);
        return ApiResponse.success(null);
    }

    /**
     * 根据ID获取角色
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role-management:view')")
    public ApiResponse<SysRole> getRoleById(@PathVariable Long id) {
        return ApiResponse.success(roleService.getRoleById(id));
    }

    /**
     * 获取所有角色列表（用于下拉选择）
     * @return 角色列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('role-management:view')")
    public ApiResponse<List<SysRole>> getAllRolesList() {
        return ApiResponse.success(roleService.getAllRoles());
    }

    /**
     * 分页获取角色
     * @return 角色分页列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('role-management:view')")
    public ApiResponse<Page<SysRole>> getRoleList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by(Sort.Direction.DESC, "createTime"));
        return ApiResponse.success(roleService.getRolePage(null, pageable));
    }

    /**
     * 分页获取角色
     * @param keyword 关键字
     * @param page 页码
     * @param size 每页大小
     * @return 角色分页数据
     */
    @GetMapping("/page")
    public ApiResponse<Page<SysRole>> getRolePage(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        return ApiResponse.success(roleService.getRolePage(keyword, pageable));
    }

    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     * @return 操作结果
     */
    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('role-management:assign-permission')")
    public ApiResponse<Void> assignPermissions(
            @PathVariable Long roleId,
            @RequestBody List<Long> permissionIds) {
        roleService.assignPermissions(roleId, permissionIds);
        return ApiResponse.success(null);
    }

    /**
     * 获取角色的权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('role-management:view')")
    public ApiResponse<List<Long>> getRolePermissionIds(@PathVariable Long roleId) {
        return ApiResponse.success(roleService.getRolePermissionIds(roleId));
    }

    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    @PostMapping("/users/{userId}")
    public ApiResponse<Void> assignUserRoles(
            @PathVariable Long userId,
            @RequestBody List<Long> roleIds) {
        roleService.assignUserRoles(userId, roleIds);
        return ApiResponse.success(null);
    }

    /**
     * 获取用户的角色ID列表
     * @param userId 用户ID
     * @return 角色ID列表
     */
    @GetMapping("/users/{userId}")
    public ApiResponse<List<Long>> getUserRoleIds(@PathVariable Long userId) {
        return ApiResponse.success(roleService.getUserRoleIds(userId));
    }

    /**
     * 获取用户的角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    @GetMapping("/users/{userId}/list")
    public ApiResponse<List<SysRole>> getUserRoles(@PathVariable Long userId) {
        return ApiResponse.success(roleService.getUserRoles(userId));
    }
} 