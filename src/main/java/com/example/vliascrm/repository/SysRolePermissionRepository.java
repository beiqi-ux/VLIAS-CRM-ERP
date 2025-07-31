package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限关联数据访问接口
 */
@Repository
public interface SysRolePermissionRepository extends JpaRepository<SysRolePermission, Long> {

    /**
     * 根据角色ID查询
     * @param roleId 角色ID
     * @return 角色权限关联列表
     */
    List<SysRolePermission> findByRoleId(Long roleId);

    /**
     * 根据权限ID查询
     * @param permissionId 权限ID
     * @return 角色权限关联列表
     */
    List<SysRolePermission> findByPermissionId(Long permissionId);

    /**
     * 根据角色ID删除
     * @param roleId 角色ID
     */
    @Modifying
    @Query("DELETE FROM SysRolePermission rp WHERE rp.roleId = ?1")
    void deleteByRoleId(Long roleId);

    /**
     * 根据权限ID删除
     * @param permissionId 权限ID
     */
    @Modifying
    @Query("DELETE FROM SysRolePermission rp WHERE rp.permissionId = ?1")
    void deleteByPermissionId(Long permissionId);

    /**
     * 检查角色是否有指定权限
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 是否存在
     */
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
} 