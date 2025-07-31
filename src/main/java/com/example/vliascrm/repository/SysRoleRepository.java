package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色数据访问接口
 */
@Repository
public interface SysRoleRepository extends JpaRepository<SysRole, Long>, JpaSpecificationExecutor<SysRole> {

    /**
     * 根据角色编码查询
     * @param roleCode 角色编码
     * @return 角色
     */
    Optional<SysRole> findByRoleCode(String roleCode);

    /**
     * 检查角色编码是否存在
     * @param roleCode 角色编码
     * @return 是否存在
     */
    boolean existsByRoleCode(String roleCode);

    /**
     * 根据用户ID查询角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    @Query("SELECT r FROM SysRole r JOIN SysUserRole ur ON r.id = ur.roleId WHERE ur.userId = ?1 AND r.status = 1 AND r.isDeleted = false")
    List<SysRole> findRolesByUserId(Long userId);
} 