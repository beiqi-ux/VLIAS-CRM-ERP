package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限数据访问接口
 */
@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long> {

    /**
     * 根据权限编码查询
     * @param permissionCode 权限编码
     * @return 权限
     */
    Optional<SysPermission> findByPermissionCode(String permissionCode);

    /**
     * 检查权限编码是否存在
     * @param permissionCode 权限编码
     * @return 是否存在
     */
    boolean existsByPermissionCode(String permissionCode);

    /**
     * 根据角色ID查询权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Query("SELECT p FROM SysPermission p JOIN SysRolePermission rp ON p.id = rp.permissionId WHERE rp.roleId = ?1 AND p.status = 1 AND p.isDeleted = false")
    List<SysPermission> findPermissionsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    @Query("SELECT DISTINCT p FROM SysPermission p " +
           "JOIN SysRolePermission rp ON p.id = rp.permissionId " +
           "JOIN SysUserRole ur ON rp.roleId = ur.roleId " +
           "WHERE ur.userId = ?1 AND p.status = 1 AND p.isDeleted = false")
    List<SysPermission> findPermissionsByUserId(Long userId);

    /**
     * 根据父ID查询权限列表
     * @param parentId 父ID
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 权限列表
     */
    List<SysPermission> findByParentIdAndStatusAndIsDeletedOrderByIdAsc(Long parentId, Integer status, Boolean isDeleted);
} 