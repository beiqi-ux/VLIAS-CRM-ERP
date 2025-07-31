package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 系统用户管理控制器
 */
@RestController
@RequestMapping("/api/sys/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 获取所有用户
     */
    @GetMapping
    public ApiResponse<List<SysUser>> getAllUsers() {
        List<SysUser> users = sysUserRepository.findAll();
        
        // 安全考虑，清空返回结果中的密码
        users.forEach(user -> user.setPassword(null));
        
        return ApiResponse.success(users);
    }

    /**
     * 获取单个用户
     */
    @GetMapping("/{id}")
    public ApiResponse<SysUser> getUserById(@PathVariable Long id) {
        return sysUserRepository.findById(id)
                .map(user -> {
                    // 安全考虑，清空返回结果中的密码
                    user.setPassword(null);
                    return ApiResponse.success(user);
                })
                .orElse(ApiResponse.failure("用户不存在"));
    }

    /**
     * 创建用户
     */
    @PostMapping
    public ApiResponse<SysUser> createUser(@RequestBody SysUser user) {
        // 检查用户名是否已存在
        if (sysUserRepository.existsByUsername(user.getUsername())) {
            return ApiResponse.failure("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        
        // 保存用户
        SysUser savedUser = sysUserRepository.save(user);
        
        // 安全考虑，清空返回结果中的密码
        savedUser.setPassword(null);
        
        return ApiResponse.success(savedUser);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public ApiResponse<SysUser> updateUser(@PathVariable Long id, @RequestBody SysUser user) {
        return sysUserRepository.findById(id)
                .map(existingUser -> {
                    // 更新用户信息
                    if (user.getUsername() != null) {
                        existingUser.setUsername(user.getUsername());
                    }
                    
                    if (user.getRealName() != null) {
                        existingUser.setRealName(user.getRealName());
                    }
                    
                    if (user.getEmail() != null) {
                        existingUser.setEmail(user.getEmail());
                    }
                    
                    if (user.getMobile() != null) {
                        existingUser.setMobile(user.getMobile());
                    }
                    
                    if (user.getAvatar() != null) {
                        existingUser.setAvatar(user.getAvatar());
                    }
                    
                    if (user.getGender() != null) {
                        existingUser.setGender(user.getGender());
                    }
                    
                    if (user.getOrgId() != null) {
                        existingUser.setOrgId(user.getOrgId());
                    }
                    
                    if (user.getDeptId() != null) {
                        existingUser.setDeptId(user.getDeptId());
                    }
                    
                    if (user.getPositionId() != null) {
                        existingUser.setPositionId(user.getPositionId());
                    }
                    
                    if (user.getStatus() != null) {
                        existingUser.setStatus(user.getStatus());
                    }
                    
                    // 如果提供了新密码则更新密码
                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
                    }
                    
                    SysUser updatedUser = sysUserRepository.save(existingUser);
                    
                    // 安全考虑，清空返回结果中的密码
                    updatedUser.setPassword(null);
                    
                    return ApiResponse.success(updatedUser);
                })
                .orElse(ApiResponse.failure("用户不存在"));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        if (!sysUserRepository.existsById(id)) {
            return ApiResponse.failure("用户不存在");
        }
        
        sysUserRepository.deleteById(id);
        return ApiResponse.success(null);
    }

    /**
     * 修改用户密码
     */
    @PostMapping("/{id}/change-password")
    public ApiResponse<Void> changePassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> passwordInfo) {
        
        String oldPassword = passwordInfo.get("oldPassword");
        String newPassword = passwordInfo.get("newPassword");
        
        if (oldPassword == null || newPassword == null) {
            return ApiResponse.failure("请提供旧密码和新密码");
        }
        
        Optional<SysUser> userOptional = sysUserRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ApiResponse.failure("用户不存在");
        }
        
        SysUser user = userOptional.get();
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ApiResponse.failure("旧密码不正确");
        }
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        sysUserRepository.save(user);
        
        return ApiResponse.success(null);
    }
    
    /**
     * 重置用户密码
     */
    @PostMapping("/{id}/reset-password")
    public ApiResponse<Map<String, String>> resetPassword(@PathVariable Long id) {
        Optional<SysUser> userOptional = sysUserRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ApiResponse.failure("用户不存在");
        }
        
        SysUser user = userOptional.get();
        
        // 生成随机密码
        String newPassword = generateRandomPassword();
        
        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        sysUserRepository.save(user);
        
        // 返回新密码
        return ApiResponse.success(Map.of("newPassword", newPassword));
    }
    
    /**
     * 生成随机密码
     */
    private String generateRandomPassword() {
        // 简单的随机密码生成，实际应用中可以使用更复杂的算法
        return "P" + System.currentTimeMillis() % 1000000;
    }
} 