package com.example.vliascrm.security;

import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 系统用户认证详情服务
 */
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {

    private final SysUserRepository sysUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户
        SysUser user = sysUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户 " + username + " 不存在"));
        
        // 这里可以添加角色和权限加载逻辑
        // 例如: user.setAuthorities(loadUserAuthorities(user.getId()));
        
        return user;
    }
} 