package com.example.vliascrm.repository;

import com.example.vliascrm.entity.OrgDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 部门Repository接口
 */
public interface OrgDepartmentRepository extends JpaRepository<OrgDepartment, Long> {

    /**
     * 根据部门编码查询部门
     * @param deptCode 部门编码
     * @return 部门信息
     */
    Optional<OrgDepartment> findByDeptCode(String deptCode);

    /**
     * 根据组织ID和部门编码查询部门
     * @param orgId 组织ID
     * @param deptCode 部门编码
     * @return 部门信息
     */
    Optional<OrgDepartment> findByOrgIdAndDeptCode(Long orgId, String deptCode);

    /**
     * 根据组织ID查询部门列表
     * @param orgId 组织ID
     * @return 部门列表
     */
    List<OrgDepartment> findByOrgIdOrderBySortAsc(Long orgId);

    /**
     * 根据父ID查询子部门列表
     * @param parentId 父ID
     * @return 子部门列表
     */
    List<OrgDepartment> findByParentIdOrderBySortAsc(Long parentId);

    /**
     * 根据组织ID和状态查询部门列表
     * @param orgId 组织ID
     * @param status 状态
     * @return 部门列表
     */
    List<OrgDepartment> findByOrgIdAndStatusOrderBySortAsc(Long orgId, Integer status);

    /**
     * 检查是否存在子部门
     * @param parentId 父ID
     * @return 存在返回true，否则返回false
     */
    boolean existsByParentId(Long parentId);

    /**
     * 根据部门名称模糊查询
     * @param deptName 部门名称
     * @return 部门列表
     */
    @Query("SELECT d FROM OrgDepartment d WHERE d.deptName LIKE %:deptName% ORDER BY d.sort ASC")
    List<OrgDepartment> findByDeptNameLike(@Param("deptName") String deptName);
} 