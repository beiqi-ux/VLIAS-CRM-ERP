package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 组织机构Repository接口
 */
public interface SysOrganizationRepository extends JpaRepository<SysOrganization, Long> {

    /**
     * 根据组织编码查询组织
     * @param orgCode 组织编码
     * @return 组织信息
     */
    Optional<SysOrganization> findByOrgCode(String orgCode);

    /**
     * 根据组织名称查询组织
     * @param orgName 组织名称
     * @return 组织信息
     */
    Optional<SysOrganization> findByOrgName(String orgName);

    /**
     * 根据父ID查询子组织列表
     * @param parentId 父ID
     * @return 子组织列表
     */
    List<SysOrganization> findByParentIdOrderBySortAsc(Long parentId);

    /**
     * 根据状态查询组织列表
     * @param status 状态
     * @return 组织列表
     */
    List<SysOrganization> findByStatusOrderBySortAsc(Integer status);

    /**
     * 检查是否存在子组织
     * @param parentId 父ID
     * @return 存在返回true，否则返回false
     */
    boolean existsByParentId(Long parentId);

    /**
     * 根据组织名称模糊查询
     * @param orgName 组织名称
     * @return 组织列表
     */
    @Query("SELECT o FROM SysOrganization o WHERE o.orgName LIKE %:orgName% ORDER BY o.sort ASC")
    List<SysOrganization> findByOrgNameLike(@Param("orgName") String orgName);
} 