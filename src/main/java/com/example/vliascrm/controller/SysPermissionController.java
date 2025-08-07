package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.PermissionDTO;
import com.example.vliascrm.entity.SysPermission;
import com.example.vliascrm.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/sys/permissions")
@RequiredArgsConstructor
public class SysPermissionController {

    private static final Logger log = LoggerFactory.getLogger(SysPermissionController.class);
    private final SysPermissionService permissionService;

    /**
     * 分页查询权限列表
     * @param page 页码，从0开始
     * @param size 每页大小
     * @param permissionName 权限名称（模糊查询）
     * @param permissionCode 权限编码（模糊查询）
     * @param permissionType 权限类型
     * @param status 状态
     * @param parentId 父权限ID
     * @return 分页结果
     */
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('permission-management:view')")
    public ApiResponse<Map<String, Object>> getPermissionPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String permissionName,
            @RequestParam(required = false) String permissionCode,
            @RequestParam(required = false) Integer permissionType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Long parentId) {

        // 构建查询条件
        Specification<SysPermission> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 权限名称模糊查询
            if (permissionName != null && !permissionName.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("permissionName"), "%" + permissionName + "%"));
            }

            // 权限编码模糊查询
            if (permissionCode != null && !permissionCode.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("permissionCode"), "%" + permissionCode + "%"));
            }

            // 权限类型查询
            if (permissionType != null) {
                predicates.add(criteriaBuilder.equal(root.get("permissionType"), permissionType));
            }

            // 状态查询
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 父权限ID查询
            if (parentId != null) {
                predicates.add(criteriaBuilder.equal(root.get("parentId"), parentId));
            }

            // 只查询未删除的权限 (包括NULL值，因为NULL在业务上等同于未删除)
            predicates.add(criteriaBuilder.or(
                criteriaBuilder.equal(root.get("isDeleted"), false),
                criteriaBuilder.isNull(root.get("isDeleted"))
            ));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // 创建分页对象，按权限类型、排序字段、ID排序
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(Sort.Direction.ASC, "permissionType")
                .and(Sort.by(Sort.Direction.ASC, "sortOrder"))
                .and(Sort.by(Sort.Direction.ASC, "id")));

        // 执行查询
        Page<SysPermission> permissionPage = permissionService.getPermissionPage(spec, pageable);

        // 构造返回结果
        Map<String, Object> result = Map.of(
            "data", permissionPage.getContent(),
            "total", permissionPage.getTotalElements(),
            "current", page,
            "size", size,
            "pages", permissionPage.getTotalPages()
        );

        return ApiResponse.success(result);
    }

    /**
     * 创建权限
     * @param permissionDTO 权限信息
     * @return 创建后的权限
     */
    @PostMapping
    @PreAuthorize("hasAuthority('permission-management:create')")
    public ApiResponse<SysPermission> createPermission(@RequestBody PermissionDTO permissionDTO) {
        SysPermission result = permissionService.createPermission(permissionDTO);
        return ApiResponse.success(result);
    }

    /**
     * 更新权限
     * @param id 权限ID
     * @param permissionDTO 权限信息
     * @return 更新后的权限
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('permission-management:edit')")
    public ApiResponse<SysPermission> updatePermission(@PathVariable Long id, @RequestBody PermissionDTO permissionDTO) {
        return ApiResponse.success(permissionService.updatePermission(id, permissionDTO));
    }

    /**
     * 删除权限
     * @param id 权限ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('permission-management:delete')")
    public ApiResponse<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ApiResponse.success(null);
    }

    /**
     * 更新权限状态
     * @param id 权限ID
     * @param status 状态
     * @return 更新后的权限
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('permission-management:edit')")
    public ApiResponse<SysPermission> updatePermissionStatus(@PathVariable Long id, @RequestParam Integer status) {
        SysPermission updatedPermission = permissionService.updatePermissionStatus(id, status);
        return ApiResponse.success(updatedPermission);
    }
    


    /**
     * 根据ID获取权限
     * @param id 权限ID
     * @return 权限信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('permission-management:view')")
    public ApiResponse<SysPermission> getPermissionById(@PathVariable Long id) {
        return ApiResponse.success(permissionService.getPermissionById(id));
    }

    /**
     * 获取权限树
     * @return 权限树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('permission-management:view')")
    public ApiResponse<List<PermissionDTO>> getPermissionTree() {
        return ApiResponse.success(permissionService.getPermissionTree());
    }

    /**
     * 获取权限管理专用的权限树（包括禁用的权限）
     * @return 权限树
     */
    @GetMapping("/tree/admin")
    @PreAuthorize("hasAuthority('permission-management:view')")
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
    @PreAuthorize("hasAuthority('permission-management:view')")
    public ApiResponse<List<SysPermission>> getAllPermissions() {
        return ApiResponse.success(permissionService.getAllPermissions());
    }

    /**
     * 根据角色ID获取权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    @GetMapping("/roles/{roleId}")
    @PreAuthorize("hasAuthority('permission-management:view')")
    public ApiResponse<List<SysPermission>> getPermissionsByRoleId(@PathVariable Long roleId) {
        return ApiResponse.success(permissionService.getPermissionsByRoleId(roleId));
    }

    /**
     * 根据用户ID获取权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('permission-management:view')")
    public ApiResponse<List<SysPermission>> getPermissionsByUserId(@PathVariable Long userId) {
        return ApiResponse.success(permissionService.getPermissionsByUserId(userId));
    }

    /**
     * 根据权限类型获取权限列表
     * @param permissionType 权限类型
     * @return 权限列表
     */
    @GetMapping("/type/{permissionType}")
    @PreAuthorize("hasAuthority('permission-management:view')")
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
    @PreAuthorize("hasAuthority('permission-management:view')")
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
    @PreAuthorize("hasAuthority('permission-management:validate')")
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

    /**
     * 同步菜单权限 - 根据现有菜单生成对应的权限记录
     * @return 同步的权限数量
     */
    @PostMapping("/sync-menu-permissions")
    @PreAuthorize("hasAuthority('permission-management:create')")
    public ApiResponse<Integer> syncMenuPermissions() {
        int syncCount = permissionService.syncMenuPermissions();
        return ApiResponse.success(syncCount);
    }
} 