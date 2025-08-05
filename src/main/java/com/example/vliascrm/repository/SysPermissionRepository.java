package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限数据访问接口
 */
@Repository
public interface SysPermissionRepository extends JpaRepository<SysPermission, Long>, JpaSpecificationExecutor<SysPermission> {

    /**
     * 根据权限编码查询
     * @param permissionCode 权限编码
     * @return 权限
     */
    Optional<SysPermission> findByPermissionCode(String permissionCode);

    /**
     * 根据权限编码查询未删除的权限
     * @param permissionCode 权限编码
     * @param isDeleted 是否删除
     * @return 权限
     */
    Optional<SysPermission> findByPermissionCodeAndIsDeleted(String permissionCode, Boolean isDeleted);

    /**
     * 检查权限编码是否存在
     * @param permissionCode 权限编码
     * @return 是否存在
     */
    boolean existsByPermissionCode(String permissionCode);

    /**
     * 检查权限编码是否存在（未删除）
     * @param permissionCode 权限编码
     * @param isDeleted 是否删除
     * @return 是否存在
     */
    boolean existsByPermissionCodeAndIsDeleted(String permissionCode, Boolean isDeleted);

    /**
     * 根据角色ID查询权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Query("SELECT p FROM SysPermission p JOIN SysRolePermission rp ON p.id = rp.permissionId WHERE rp.roleId = ?1 AND p.status = 1 AND p.isDeleted = false ORDER BY p.sortOrder ASC, p.id ASC")
    List<SysPermission> findPermissionsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    @Query("SELECT DISTINCT p FROM SysPermission p " +
           "JOIN SysRolePermission rp ON p.id = rp.permissionId " +
           "JOIN SysUserRole ur ON rp.roleId = ur.roleId " +
           "JOIN SysRole r ON ur.roleId = r.id " +
           "WHERE ur.userId = ?1 AND p.status = 1 AND p.isDeleted = false " +
           "AND r.status = 1 AND r.isDeleted = false " +
           "ORDER BY p.sortOrder ASC, p.id ASC")
    List<SysPermission> findPermissionsByUserId(Long userId);

    /**
     * 根据父ID查询权限列表
     * @param parentId 父ID
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 权限列表
     */
    List<SysPermission> findByParentIdAndStatusAndIsDeletedOrderBySortOrderAscIdAsc(Long parentId, Integer status, Boolean isDeleted);

    /**
     * 查询所有未删除的权限
     * @return 权限列表
     */
    List<SysPermission> findByIsDeletedOrderBySortOrderAscIdAsc(Boolean isDeleted);

    /**
     * 根据权限类型查询权限列表
     * @param permissionType 权限类型
     * @param isDeleted 是否删除
     * @return 权限列表
     */
    List<SysPermission> findByPermissionTypeAndIsDeletedOrderBySortOrderAscIdAsc(Integer permissionType, Boolean isDeleted);

    /**
     * 根据层级深度查询权限列表
     * @param levelDepth 层级深度
     * @param isDeleted 是否删除
     * @return 权限列表
     */
    List<SysPermission> findByLevelDepthAndIsDeletedOrderBySortOrderAscIdAsc(Integer levelDepth, Boolean isDeleted);

    /**
     * 根据权限路径前缀查询权限列表
     * @param pathPrefix 路径前缀
     * @param isDeleted 是否删除
     * @return 权限列表
     */
    @Query("SELECT p FROM SysPermission p WHERE p.permissionPath LIKE CONCAT(?1, '%') AND p.isDeleted = ?2 ORDER BY p.sortOrder ASC, p.id ASC")
    List<SysPermission> findByPermissionPathStartingWithAndIsDeleted(String pathPrefix, Boolean isDeleted);

    /**
     * 查询所有权限并按3级结构排序
     * @param isDeleted 是否删除
     * @return 权限列表
     */
    @Query("SELECT p FROM SysPermission p WHERE p.isDeleted = ?1 ORDER BY p.permissionType ASC, p.sortOrder ASC, p.id ASC")
    List<SysPermission> findAllPermissionsOrderByTypeAndSort(Boolean isDeleted);
} 