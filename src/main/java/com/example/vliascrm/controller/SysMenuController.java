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

/**
 * 菜单管理控制器
 */
@RestController
@RequestMapping("/api/sys/menus")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

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
     * 获取菜单树
     * @return 菜单树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('menu-management:view')")
    public ApiResponse<List<MenuDTO>> getMenuTree() {
        return ApiResponse.success(menuService.getMenuTree());
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
     * 获取用户菜单树
     * @param userId 用户ID
     * @return 用户菜单树
     */
    @GetMapping("/users/{userId}/tree")
    @PreAuthorize("hasAuthority('menu-management:view')")
    public ApiResponse<List<MenuDTO>> getUserMenuTree(@PathVariable Long userId) {
        return ApiResponse.success(menuService.getUserMenuTree(userId));
    }

    /**
     * 切换菜单状态
     * @param id 菜单ID
     * @return 操作结果
     */
    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasAuthority('menu-management:edit')")
    public ApiResponse<Void> toggleMenuStatus(@PathVariable Long id) {
        menuService.toggleMenuStatus(id);
        return ApiResponse.success(null);
    }
} 