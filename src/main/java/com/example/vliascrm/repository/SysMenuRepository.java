package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 菜单数据访问接口
 */
@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {

    /**
     * 根据菜单编码查询
     * @param menuCode 菜单编码
     * @return 菜单
     */
    Optional<SysMenu> findByMenuCode(String menuCode);

    /**
     * 检查菜单编码是否存在
     * @param menuCode 菜单编码
     * @return 是否存在
     */
    boolean existsByMenuCode(String menuCode);

    /**
     * 根据父ID查询菜单列表
     * @param parentId 父ID
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 菜单列表
     */
    List<SysMenu> findByParentIdAndStatusAndIsDeletedOrderBySortAsc(Long parentId, Integer status, Boolean isDeleted);

    /**
     * 根据角色ID查询菜单列表
     * @param roleId 角色ID
     * @return 菜单列表
     */
    @Query("SELECT DISTINCT m FROM SysMenu m " +
           "JOIN SysPermission p ON m.permissionCode = p.permissionCode " +
           "JOIN SysRolePermission rp ON p.id = rp.permissionId " +
           "WHERE rp.roleId = ?1 AND m.status = 1 AND m.isDeleted = false " +
           "ORDER BY m.sort ASC")
    List<SysMenu> findMenusByRoleId(Long roleId);

    /**
     * 根据用户ID查询菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    @Query("SELECT DISTINCT m FROM SysMenu m " +
           "WHERE m.id IN (" +
           "    SELECT DISTINCT menu.id FROM SysMenu menu " +
           "    JOIN SysPermission p ON (" +
           "        p.permissionCode = menu.permissionCode OR " +
           "        p.permissionCode = CONCAT(menu.permissionCode, '-management') OR " +
           "        (menu.permissionCode = 'user' AND p.permissionCode = 'user-management') OR " +
           "        (menu.permissionCode = 'role' AND p.permissionCode = 'role-management') OR " +
           "        (menu.permissionCode = 'permission' AND p.permissionCode = 'permission-management') OR " +
           "        (menu.permissionCode = 'menu' AND p.permissionCode = 'menu-management') OR " +
           "        (menu.permissionCode = 'dict' AND p.permissionCode = 'dict-management') OR " +
           "        (menu.permissionCode = 'product-info-management:list' AND p.permissionCode = 'product-info-management') OR " +
           "        (menu.permissionCode = 'product-category-management:list' AND p.permissionCode = 'product-category-management') OR " +
           "        (menu.permissionCode = 'product-brand-management:list' AND p.permissionCode = 'product-brand-management') OR " +
           "        (menu.permissionCode = 'product-specification-management:list' AND p.permissionCode = 'product-specification-management') OR " +
           "        (menu.permissionCode = 'product-attribute-management:list' AND p.permissionCode = 'product-attribute-management') OR " +
           "        (menu.permissionCode = 'product-sku-management:list' AND p.permissionCode = 'product-sku-management') OR " +
           "        (menu.permissionCode = 'profile' AND p.permissionCode = 'profile-info-management')" +
           "    ) " +
           "    JOIN SysRolePermission rp ON p.id = rp.permissionId " +
           "    JOIN SysUserRole ur ON rp.roleId = ur.roleId " +
           "    WHERE ur.userId = ?1 AND menu.status = 1 AND menu.isDeleted = false" +
           ") AND m.status = 1 AND m.isDeleted = false " +
           "ORDER BY m.sort ASC")
    List<SysMenu> findMenusByUserId(Long userId);
} 