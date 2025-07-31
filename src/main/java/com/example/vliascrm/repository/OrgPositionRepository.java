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
} 