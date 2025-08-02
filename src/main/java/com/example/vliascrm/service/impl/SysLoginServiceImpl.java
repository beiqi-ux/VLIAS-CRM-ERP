package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.LoginDTO;
import com.example.vliascrm.dto.LoginResponseDTO;
import com.example.vliascrm.entity.OrgDepartment;
import com.example.vliascrm.entity.OrgPosition;
import com.example.vliascrm.entity.SysOrganization;
import com.example.vliascrm.entity.SysRole;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.exception.BusinessException;
import com.example.vliascrm.repository.SysUserRepository;
import com.example.vliascrm.service.OrgDepartmentService;
import com.example.vliascrm.service.OrgPositionService;
import com.example.vliascrm.service.SysLoginService;
import com.example.vliascrm.service.SysOrganizationService;
import com.example.vliascrm.service.SysPermissionService;
import com.example.vliascrm.service.SysRoleService;
import com.example.vliascrm.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 登录服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysLoginServiceImpl implements SysLoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final SysUserRepository sysUserRepository;
    private final SysRoleService roleService;
    private final SysOrganizationService organizationService;
    private final OrgDepartmentService departmentService;
    private final OrgPositionService positionService;
    private final SysPermissionService permissionService;

    @Value("${app.jwt.expiration}")
    private Long expiration;

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            // 验证用户名密码
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
            );
            
            // 保存认证信息到上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 获取用户信息
            SysUser user = (SysUser) authentication.getPrincipal();
            
            // 生成JWT令牌
            String token = jwtUtil.generateToken(user);
            
            // 更新最后登录时间
            sysUserRepository.updateLastLoginTime(user.getId(), LocalDateTime.now());
            
            // 构建返回结果
            return buildLoginResponse(user, token);
            
        } catch (AuthenticationException e) {
            throw new BusinessException("用户名或密码错误");
        }
    }

    @Override
    public void logout() {
        // 清除上下文认证信息
        SecurityContextHolder.clearContext();
    }

    @Override
    public LoginResponseDTO refreshToken(String oldToken) {
        // 验证token是否有效
        if (!jwtUtil.validateToken(oldToken)) {
            throw new BusinessException("无效的令牌");
        }
        
        // 获取用户名
        String username = jwtUtil.extractUsername(oldToken);
        
        // 查询用户
        SysUser user = sysUserRepository.findByUsernameAndStatus(username, 1)
                .orElseThrow(() -> new BusinessException("用户不存在或已禁用"));
        
        // 生成新令牌
        String newToken = jwtUtil.generateToken(user);
        
        // 构建返回结果
        return buildLoginResponse(user, newToken);
    }

    @Override
    public LoginResponseDTO getCurrentUserInfo() {
        // 获取当前认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("用户未登录");
        }
        
        // 获取用户信息
        SysUser user = (SysUser) authentication.getPrincipal();
        
        // 重新查询最新数据
        SysUser latestUser = sysUserRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException("用户不存在"));
                
        // 查询组织名称
        String orgName = null;
        if (latestUser.getOrgId() != null) {
            SysOrganization org = organizationService.findById(latestUser.getOrgId());
            if (org != null) {
                orgName = org.getOrgName();
            }
        }
        
        // 查询部门名称
        String deptName = null;
        if (latestUser.getDeptId() != null) {
            OrgDepartment dept = departmentService.findById(latestUser.getDeptId());
            if (dept != null) {
                deptName = dept.getDeptName();
            }
        }
        
        // 查询岗位名称
        String positionName = null;
        if (latestUser.getPositionId() != null) {
            OrgPosition position = positionService.findById(latestUser.getPositionId());
            if (position != null) {
                positionName = position.getPositionName();
            }
        }
        
        // 返回用户信息，不包含token
        return LoginResponseDTO.builder()
                .userId(latestUser.getId())
                .username(latestUser.getUsername())
                .realName(latestUser.getRealName())
                .avatar(latestUser.getAvatar())
                .orgId(latestUser.getOrgId())
                .orgName(orgName)
                .deptId(latestUser.getDeptId())
                .deptName(deptName)
                .positionId(latestUser.getPositionId())
                .positionName(positionName)
                .mobile(latestUser.getMobile())
                .email(latestUser.getEmail())
                .gender(latestUser.getGender())
                .status(latestUser.getStatus())
                .lastLoginTime(latestUser.getLastLoginTime())
                .createTime(latestUser.getCreateTime())
                .roles(getRoleNames(latestUser.getId()))
                .permissions(getUserPermissions(latestUser.getId()))  // 返回真实的权限列表
                .build();
    }
    
    /**
     * 构建登录响应结果
     *
     * @param user 用户信息
     * @param token JWT令牌
     * @return 登录响应DTO
     */
    private LoginResponseDTO buildLoginResponse(SysUser user, String token) {
        // 查询组织名称
        String orgName = null;
        if (user.getOrgId() != null) {
            SysOrganization org = organizationService.findById(user.getOrgId());
            if (org != null) {
                orgName = org.getOrgName();
            }
        }
        
        // 查询部门名称
        String deptName = null;
        if (user.getDeptId() != null) {
            OrgDepartment dept = departmentService.findById(user.getDeptId());
            if (dept != null) {
                deptName = dept.getDeptName();
            }
        }
        
        // 查询岗位名称
        String positionName = null;
        if (user.getPositionId() != null) {
            OrgPosition position = positionService.findById(user.getPositionId());
            if (position != null) {
                positionName = position.getPositionName();
            }
        }
        
        return LoginResponseDTO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .avatar(user.getAvatar())
                .token(token)
                .tokenType("Bearer")
                .expiresIn(expiration * 1000)
                .orgId(user.getOrgId())
                .orgName(orgName)
                .deptId(user.getDeptId())
                .deptName(deptName)
                .positionId(user.getPositionId())
                .positionName(positionName)
                .mobile(user.getMobile())
                .email(user.getEmail())
                .gender(user.getGender())
                .status(user.getStatus())
                .lastLoginTime(user.getLastLoginTime())
                .createTime(user.getCreateTime())
                .roles(getRoleNames(user.getId()))
                .permissions(getUserPermissions(user.getId()))  // 返回真实的权限列表
                .build();
    }
    
    /**
     * 获取用户的角色名称列表
     *
     * @param userId 用户ID
     * @return 角色名称列表
     */
    private List<String> getRoleNames(Long userId) {
        List<SysRole> roles = roleService.getUserRoles(userId);
        return roles.stream()
                .map(SysRole::getRoleCode)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户的权限编码列表
     *
     * @param userId 用户ID
     * @return 权限编码列表
     */
    private List<String> getUserPermissions(Long userId) {
        return permissionService.getPermissionsByUserId(userId).stream()
                .map(permission -> permission.getPermissionCode())
                .collect(Collectors.toList());
    }
} 