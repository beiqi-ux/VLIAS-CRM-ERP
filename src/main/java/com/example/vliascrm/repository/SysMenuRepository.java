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
           "JOIN SysPermission p ON m.id = p.menuId " +
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
           "JOIN SysPermission p ON m.id = p.menuId " +
           "JOIN SysRolePermission rp ON p.id = rp.permissionId " +
           "JOIN SysUserRole ur ON rp.roleId = ur.roleId " +
           "WHERE ur.userId = ?1 AND m.status = 1 AND m.isDeleted = false " +
           "ORDER BY m.sort ASC")
    List<SysMenu> findMenusByUserId(Long userId);
} 