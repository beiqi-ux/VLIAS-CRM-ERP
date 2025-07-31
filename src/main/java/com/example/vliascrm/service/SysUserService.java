package com.example.vliascrm.service;

import com.example.vliascrm.entity.SysUser;

import java.util.List;
import java.util.Optional;

/**
 * 系统用户服务接口
 */
public interface SysUserService {

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
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<SysUser> findAll();

    /**
     * 保存用户
     *
     * @param user 用户对象
     * @return 保存后的用户对象
     */
    SysUser save(SysUser user);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void deleteById(Long id);

    /**
     * 更新用户头像
     *
     * @param username 用户名
     * @param avatarUrl 头像URL
     * @return 更新后的用户对象
     */
    SysUser updateAvatar(String username, String avatarUrl);
} 