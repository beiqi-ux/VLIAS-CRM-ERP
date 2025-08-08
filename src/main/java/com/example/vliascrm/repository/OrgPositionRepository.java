package com.example.vliascrm.repository;

import com.example.vliascrm.entity.OrgPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 岗位Repository接口
 */
public interface OrgPositionRepository extends JpaRepository<OrgPosition, Long> {

    /**
     * 根据岗位编码查询岗位
     * @param positionCode 岗位编码
     * @return 岗位信息
     */
    Optional<OrgPosition> findByPositionCode(String positionCode);

    /**
     * 根据组织ID和岗位编码查询岗位
     * @param orgId 组织ID
     * @param positionCode 岗位编码
     * @return 岗位信息
     */
    Optional<OrgPosition> findByOrgIdAndPositionCode(Long orgId, String positionCode);

    /**
     * 根据组织ID查询岗位列表
     * @param orgId 组织ID
     * @return 岗位列表
     */
    List<OrgPosition> findByOrgIdOrderBySortAsc(Long orgId);

    /**
     * 根据部门ID查询岗位列表
     * @param deptId 部门ID
     * @return 岗位列表
     */
    List<OrgPosition> findByDeptIdOrderBySortAsc(Long deptId);

    /**
     * 根据组织ID和状态查询岗位列表
     * @param orgId 组织ID
     * @param status 状态
     * @return 岗位列表
     */
    List<OrgPosition> findByOrgIdAndStatusOrderBySortAsc(Long orgId, Integer status);

    /**
     * 根据部门ID和状态查询岗位列表
     * @param deptId 部门ID
     * @param status 状态
     * @return 岗位列表
     */
    List<OrgPosition> findByDeptIdAndStatusOrderBySortAsc(Long deptId, Integer status);

    /**
     * 根据岗位名称模糊查询
     * @param positionName 岗位名称
     * @return 岗位列表
     */
    @Query("SELECT p FROM OrgPosition p WHERE p.positionName LIKE %:positionName% ORDER BY p.sort ASC")
    List<OrgPosition> findByPositionNameLike(@Param("positionName") String positionName);

    /**
     * 查询所有岗位（只返回启用状态的岗位，且所属组织和部门状态都为启用）
     * @return 岗位列表
     */
    @Query("SELECT p FROM OrgPosition p " +
           "JOIN SysOrganization o ON p.orgId = o.id " +
           "LEFT JOIN OrgDepartment d ON p.deptId = d.id " +
           "WHERE p.status = 1 AND o.status = 1 AND (p.deptId IS NULL OR d.status = 1) " +
           "ORDER BY p.sort ASC")
    List<OrgPosition> findAllWithActiveOrganizationAndDepartment();

    /**
     * 根据组织ID查询岗位列表（只返回启用状态的岗位，且所属组织和部门状态都为启用）
     * @param orgId 组织ID
     * @return 岗位列表
     */
    @Query("SELECT p FROM OrgPosition p " +
           "JOIN SysOrganization o ON p.orgId = o.id " +
           "LEFT JOIN OrgDepartment d ON p.deptId = d.id " +
           "WHERE p.orgId = :orgId AND p.status = 1 AND o.status = 1 AND (p.deptId IS NULL OR d.status = 1) " +
           "ORDER BY p.sort ASC")
    List<OrgPosition> findByOrgIdWithActiveOrganizationAndDepartment(@Param("orgId") Long orgId);

    /**
     * 根据部门ID查询岗位列表（只返回启用状态的岗位，且所属组织和部门状态都为启用）
     * @param deptId 部门ID
     * @return 岗位列表
     */
    @Query("SELECT p FROM OrgPosition p " +
           "JOIN SysOrganization o ON p.orgId = o.id " +
           "JOIN OrgDepartment d ON p.deptId = d.id " +
           "WHERE p.deptId = :deptId AND p.status = 1 AND o.status = 1 AND d.status = 1 " +
           "ORDER BY p.sort ASC")
    List<OrgPosition> findByDeptIdWithActiveOrganizationAndDepartment(@Param("deptId") Long deptId);

    /**
     * 查询所有岗位（管理列表用，显示所有岗位包括禁用的，但只显示启用组织和部门下的岗位）
     * @return 岗位列表
     */
    @Query("SELECT p FROM OrgPosition p " +
           "JOIN SysOrganization o ON p.orgId = o.id " +
           "LEFT JOIN OrgDepartment d ON p.deptId = d.id " +
           "WHERE o.status = 1 AND (p.deptId IS NULL OR d.status = 1) " +
           "ORDER BY p.sort ASC")
    List<OrgPosition> findAllForManagement();

    /**
     * 根据组织ID查询岗位列表（管理列表用，显示所有岗位包括禁用的，但只显示启用组织和部门下的岗位）
     * @param orgId 组织ID
     * @return 岗位列表
     */
    @Query("SELECT p FROM OrgPosition p " +
           "JOIN SysOrganization o ON p.orgId = o.id " +
           "LEFT JOIN OrgDepartment d ON p.deptId = d.id " +
           "WHERE p.orgId = :orgId AND o.status = 1 AND (p.deptId IS NULL OR d.status = 1) " +
           "ORDER BY p.sort ASC")
    List<OrgPosition> findByOrgIdForManagement(@Param("orgId") Long orgId);

    /**
     * 根据部门ID查询岗位列表（管理列表用，显示所有岗位包括禁用的，但只显示启用组织和部门下的岗位）
     * @param deptId 部门ID
     * @return 岗位列表
     */
    @Query("SELECT p FROM OrgPosition p " +
           "JOIN SysOrganization o ON p.orgId = o.id " +
           "JOIN OrgDepartment d ON p.deptId = d.id " +
           "WHERE p.deptId = :deptId AND o.status = 1 AND d.status = 1 " +
           "ORDER BY p.sort ASC")
    List<OrgPosition> findByDeptIdForManagement(@Param("deptId") Long deptId);

    /**
     * 根据条件查询岗位列表（管理列表用，支持多种筛选条件）
     * @param orgId 组织ID（可选）
     * @param deptId 部门ID（可选）
     * @param status 状态（可选）
     * @param positionName 岗位名称（可选）
     * @return 岗位列表
     */
    @Query("SELECT p FROM OrgPosition p " +
           "JOIN SysOrganization o ON p.orgId = o.id " +
           "LEFT JOIN OrgDepartment d ON p.deptId = d.id " +
           "WHERE o.status = 1 AND (p.deptId IS NULL OR d.status = 1) " +
           "AND (:orgId IS NULL OR p.orgId = :orgId) " +
           "AND (:deptId IS NULL OR p.deptId = :deptId) " +
           "AND (:status IS NULL OR p.status = :status) " +
           "AND (:positionName IS NULL OR :positionName = '' OR p.positionName LIKE %:positionName%) " +
           "ORDER BY p.sort ASC")
    List<OrgPosition> findByConditionsForManagement(
            @Param("orgId") Long orgId,
            @Param("deptId") Long deptId,
            @Param("status") Integer status,
            @Param("positionName") String positionName);
} 