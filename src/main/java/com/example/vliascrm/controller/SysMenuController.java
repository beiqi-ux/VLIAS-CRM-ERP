package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.MenuDTO;
import com.example.vliascrm.entity.SysMenu;
import com.example.vliascrm.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<SysMenu> updateMenu(@PathVariable Long id, @RequestBody MenuDTO menuDTO) {
        return ApiResponse.success(menuService.updateMenu(id, menuDTO));
    }

    /**
     * 删除菜单
     * @param id 菜单ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ApiResponse.success(null);
    }

    /**
     * 根据ID获取菜单
     * @param id 菜单ID
     * @return 菜单信息
     */
    @GetMapping("/{id}")
    public ApiResponse<SysMenu> getMenuById(@PathVariable Long id) {
        return ApiResponse.success(menuService.getMenuById(id));
    }

    /**
     * 获取所有菜单
     * @return 菜单列表
     */
    @GetMapping
    public ApiResponse<List<SysMenu>> getAllMenus() {
        return ApiResponse.success(menuService.getAllMenus());
    }

    /**
     * 获取菜单树
     * @return 菜单树
     */
    @GetMapping("/tree")
    public ApiResponse<List<MenuDTO>> getMenuTree() {
        return ApiResponse.success(menuService.getMenuTree());
    }

    /**
     * 根据角色ID获取菜单列表
     * @param roleId 角色ID
     * @return 菜单列表
     */
    @GetMapping("/roles/{roleId}")
    public ApiResponse<List<SysMenu>> getMenusByRoleId(@PathVariable Long roleId) {
        return ApiResponse.success(menuService.getMenusByRoleId(roleId));
    }

    /**
     * 根据用户ID获取菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    @GetMapping("/users/{userId}")
    public ApiResponse<List<SysMenu>> getMenusByUserId(@PathVariable Long userId) {
        return ApiResponse.success(menuService.getMenusByUserId(userId));
    }

    /**
     * 根据用户ID获取菜单树
     * @param userId 用户ID
     * @return 菜单树
     */
    @GetMapping("/users/{userId}/tree")
    public ApiResponse<List<MenuDTO>> getUserMenuTree(@PathVariable Long userId) {
        return ApiResponse.success(menuService.getUserMenuTree(userId));
    }

    /**
     * 切换菜单状态
     * @param id 菜单ID
     * @return 操作结果
     */
    @PutMapping("/{id}/toggle-status")
    public ApiResponse<Void> toggleMenuStatus(@PathVariable Long id) {
        menuService.toggleMenuStatus(id);
        return ApiResponse.success(null);
    }
} 