package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.MenuDTO;
import com.example.vliascrm.entity.SysMenu;
import com.example.vliascrm.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.service.SysUserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("/api/sys/menus")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;
    private final SysUserService userService;

    /**
     * 创建菜单
     * @param menuDTO 菜单信息
     * @return 创建后的菜单
     */
    @PostMapping
    @PreAuthorize("hasAuthority('menu-management:create')")
    public ApiResponse<SysMenu> createMenu(@RequestBody MenuDTO menuDTO) {
        return ApiResponse.success(menuService.createMenu(menuDTO));
    }

    /**
     * 更新菜单
     * @param id 菜单ID
     * @param menuDTO 菜单信息
     * @return 更新后的菜单
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('menu-management:edit')")
    public ApiResponse<SysMenu> updateMenu(@PathVariable Long id, @RequestBody MenuDTO menuDTO) {
        return ApiResponse.success(menuService.updateMenu(id, menuDTO));
    }

    /**
     * 删除菜单
     * @param id 菜单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('menu-management:delete')")
    public ApiResponse<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ApiResponse.success(null);
    }

    /**
     * 批量为所有菜单生成权限
     */
    @PostMapping("/batch-generate-permissions")
    @PreAuthorize("hasAuthority('menu-management:edit')")
    public ApiResponse<Map<String, Object>> batchGeneratePermissions() {
        int generated = menuService.batchGenerateMenuPermissions();
        Map<String, Object> result = new HashMap<>();
        result.put("generated", generated);
        result.put("message", "批量生成权限完成，共生成 " + generated + " 个权限");
        return ApiResponse.success(result);
    }

    /**
     * 为指定菜单重新生成权限
     */
    @PostMapping("/{id}/regenerate-permissions")
    @PreAuthorize("hasAuthority('menu-management:edit')")
    public ApiResponse<Map<String, Object>> regeneratePermissions(@PathVariable Long id) {
        int generated = menuService.regenerateMenuPermissions(id);
        Map<String, Object> result = new HashMap<>();
        result.put("generated", generated);
        result.put("message", "为菜单重新生成权限完成，新增 " + generated + " 个权限");
        return ApiResponse.success(result);
    }

    /**
     * 根据ID获取菜单
     * @param id 菜单ID
     * @return 菜单信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('menu-management:view')")
    public ApiResponse<SysMenu> getMenuById(@PathVariable Long id) {
        return ApiResponse.success(menuService.getMenuById(id));
    }

    /**
     * 获取所有菜单
     * @return 菜单列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('menu-management:view')")
    public ApiResponse<List<SysMenu>> getAllMenus() {
        return ApiResponse.success(menuService.getAllMenus());
    }

    /**
     * 获取菜单树（管理员视图，包含禁用的菜单）
     * @return 菜单树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('menu-management:view')")
    public ApiResponse<List<MenuDTO>> getMenuTree() {
        return ApiResponse.success(menuService.getAdminMenuTree());
    }

    /**
     * 根据角色ID获取菜单列表
     * @param roleId 角色ID
     * @return 菜单列表
     */
    @GetMapping("/roles/{roleId}")
    @PreAuthorize("hasAuthority('menu-management:view')")
    public ApiResponse<List<SysMenu>> getMenusByRoleId(@PathVariable Long roleId) {
        return ApiResponse.success(menuService.getMenusByRoleId(roleId));
    }

    /**
     * 根据用户ID获取菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('menu-management:view')")
    public ApiResponse<List<SysMenu>> getMenusByUserId(@PathVariable Long userId) {
        return ApiResponse.success(menuService.getMenusByUserId(userId));
    }

    /**
     * 获取当前用户的菜单树
     * @return 当前用户菜单树
     */
    @GetMapping("/current-user/tree")
    public ApiResponse<List<MenuDTO>> getCurrentUserMenuTree(HttpServletRequest request) {
        // 获取当前用户信息
        String currentUsername = request.getRemoteUser();
        if (currentUsername == null) {
            throw new IllegalStateException("用户未认证");
        }
        
        // 获取当前用户ID
        Optional<SysUser> currentUserOpt = userService.findByUsername(currentUsername);
        if (currentUserOpt.isEmpty()) {
            throw new IllegalStateException("当前用户不存在");
        }
        
        return ApiResponse.success(menuService.getUserMenuTree(currentUserOpt.get().getId()));
    }

    /**
     * 获取用户菜单树（管理员功能）
     * @param userId 用户ID
     * @return 用户菜单树
     */
    @GetMapping("/users/{userId}/tree")
    @PreAuthorize("hasAuthority('menu-management:view')")
    public ApiResponse<List<MenuDTO>> getUserMenuTree(@PathVariable Long userId) {
        return ApiResponse.success(menuService.getUserMenuTree(userId));
    }


} 