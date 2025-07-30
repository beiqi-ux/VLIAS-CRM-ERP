package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 系统用户数据访问接口
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象
     */
    Optional<SysUser> findByUsername(String username);
    
    /**
     * 根据用户名和状态查找用户
     * @param username 用户名
     * @param status 状态
     * @return 用户对象
     */
    Optional<SysUser> findByUsernameAndStatus(String username, Integer status);
    
    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 检查手机号是否存在
     * @param mobile 手机号
     * @return 是否存在
     */
    boolean existsByMobile(String mobile);
    
    /**
     * 检查邮箱是否存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 更新最后登录时间
     * @param userId 用户ID
     * @param lastLoginTime 最后登录时间
     * @return 影响行数
     */
    @Modifying
    @Transactional
    @Query("UPDATE SysUser u SET u.lastLoginTime = ?2 WHERE u.id = ?1")
    int updateLastLoginTime(Long userId, LocalDateTime lastLoginTime);
} 