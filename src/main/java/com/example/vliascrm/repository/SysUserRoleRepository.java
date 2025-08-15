package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联数据访问接口
 */
@Repository
public interface SysUserRoleRepository extends JpaRepository<SysUserRole, Long> {

    /**
     * 根据用户ID查询
     * @param userId 用户ID
     * @return 用户角色关联列表
     */
    List<SysUserRole> findByUserId(Long userId);

    /**
     * 根据角色ID查询
     * @param roleId 角色ID
     * @return 用户角色关联列表
     */
    List<SysUserRole> findByRoleId(Long roleId);

    /**
     * 根据用户ID删除
     * @param userId 用户ID
     */
    @Modifying
    @Query("DELETE FROM SysUserRole ur WHERE ur.userId = ?1")
    void deleteByUserId(Long userId);

    /**
     * 根据角色ID删除
     * @param roleId 角色ID
     */
    @Modifying
    @Query("DELETE FROM SysUserRole ur WHERE ur.roleId = ?1")
    void deleteByRoleId(Long roleId);

    /**
     * 检查用户是否有指定角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 是否存在
     */
    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
} 