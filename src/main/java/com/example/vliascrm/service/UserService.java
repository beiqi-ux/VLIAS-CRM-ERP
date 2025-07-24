package com.example.vliascrm.service;

import com.example.vliascrm.entity.system.SysUser;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID
     * @return 用户对象
     */
    Optional<SysUser> findById(Long id);
    
    /**
     * 根据用户名查询用户
     * 
     * @param username 用户名
     * @return 用户对象
     */
    Optional<SysUser> findByUsername(String username);
    
    /**
     * 获取所有用户
     * 
     * @return 用户列表
     */
    List<SysUser> findAll();
    
    /**
     * 保存用户
     * 
     * @param user 用户对象
     * @return 保存后的用户
     */
    SysUser save(SysUser user);
    
    /**
     * 更新用户
     * 
     * @param user 用户对象
     * @return 更新后的用户
     */
    SysUser update(SysUser user);
    
    /**
     * 删除用户
     * 
     * @param id 用户ID
     */
    void deleteById(Long id);
} 