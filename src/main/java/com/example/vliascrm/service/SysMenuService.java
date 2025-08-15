package com.example.vliascrm.service;

import com.example.vliascrm.dto.MenuDTO;
import com.example.vliascrm.entity.SysMenu;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface SysMenuService {

    /**
     * 创建菜单
     * @param menuDTO 菜单信息
     * @return 创建后的菜单
     */
    SysMenu createMenu(MenuDTO menuDTO);

    /**
     * 更新菜单
     * @param id 菜单ID
     * @param menuDTO 菜单信息
     * @return 更新后的菜单
     */
    SysMenu updateMenu(Long id, MenuDTO menuDTO);

    /**
     * 删除菜单
     * @param id 菜单ID
     */
    void deleteMenu(Long id);

    /**
     * 批量为所有菜单生成对应权限
     * @return 生成的权限数量
     */
    int batchGenerateMenuPermissions();

    /**
     * 为指定菜单重新生成权限
     * @param menuId 菜单ID
     * @return 生成的权限数量
     */
    int regenerateMenuPermissions(Long menuId);

    /**
     * 根据ID获取菜单
     * @param id 菜单ID
     * @return 菜单信息
     */
    SysMenu getMenuById(Long id);

    /**
     * 获取所有菜单
     * @return 菜单列表
     */
    List<SysMenu> getAllMenus();

    /**
     * 获取菜单树（用户侧，只显示启用且未删除的菜单）
     * @return 菜单树
     */
    List<MenuDTO> getMenuTree();

    /**
     * 获取管理员菜单树（管理侧，显示所有未删除的菜单，包括禁用的）
     * @return 菜单树
     */
    List<MenuDTO> getAdminMenuTree();

    /**
     * 根据角色ID获取菜单列表
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<SysMenu> getMenusByRoleId(Long roleId);

    /**
     * 根据用户ID获取菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> getMenusByUserId(Long userId);

    /**
     * 根据用户ID获取菜单树
     * @param userId 用户ID
     * @return 菜单树
     */
    List<MenuDTO> getUserMenuTree(Long userId);

    /**
     * 切换菜单状态（启用/禁用）
     * @param id 菜单ID
     */
    void toggleMenuStatus(Long id);

} 