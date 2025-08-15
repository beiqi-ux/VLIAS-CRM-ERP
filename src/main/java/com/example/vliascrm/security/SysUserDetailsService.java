package com.example.vliascrm.security;

import com.example.vliascrm.entity.SysPermission;
import com.example.vliascrm.entity.SysRole;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.repository.SysUserRepository;
import com.example.vliascrm.service.SysPermissionService;
import com.example.vliascrm.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统用户认证详情服务
 */
@Service
@RequiredArgsConstructor
public class SysUserDetailsService implements UserDetailsService {

    private final SysUserRepository sysUserRepository;
    private final SysRoleService roleService;
    private final SysPermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据用户名查询用户
        SysUser user = sysUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户 " + username + " 不存在"));
        
        // 加载用户的角色和权限
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        
        // 加载角色
        List<SysRole> roles = roleService.getUserRoles(user.getId());
        for (SysRole role : roles) {
            // 添加角色，注意角色需要加上 ROLE_ 前缀
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleCode()));
        }
        
        // 加载权限
        List<SysPermission> permissions = permissionService.getPermissionsByUserId(user.getId());
        authorities.addAll(permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionCode()))
                .collect(Collectors.toList()));
        
        // 设置用户权限
        user.setAuthorities(authorities);
        
        return user;
    }
} 