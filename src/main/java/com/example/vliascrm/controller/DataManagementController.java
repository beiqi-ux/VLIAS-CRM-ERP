package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.SysRole;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.repository.SysRoleRepository;
import com.example.vliascrm.repository.SysUserRepository;
import com.example.vliascrm.repository.SysUserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 数据管理控制器
 * 用于处理系统数据管理操作
 */
@Slf4j
@RestController
@RequestMapping("/api/data-management")
@RequiredArgsConstructor
public class DataManagementController {

    private final SysUserRepository userRepository;
    private final SysUserRoleRepository userRoleRepository;
    private final SysRoleRepository roleRepository;

    /**
     * 删除指定用户
     * @param username 用户名
     * @return 操作结果
     */
    @DeleteMapping("/users/{username}")
    @Transactional
    public ApiResponse<String> deleteUser(@PathVariable String username) {
        try {
            log.info("尝试删除用户: {}", username);
            
            Optional<SysUser> userOpt = userRepository.findByUsername(username);
            if (!userOpt.isPresent()) {
                return ApiResponse.failure("用户不存在: " + username);
            }
            
            SysUser user = userOpt.get();
            Long userId = user.getId();
            
            // 1. 删除用户角色关联
            userRoleRepository.deleteByUserId(userId);
            log.info("已删除用户 {} 的角色关联", username);
            
            // 2. 删除用户
            userRepository.delete(user);
            log.info("已删除用户: {}", username);
            
            return ApiResponse.success("用户 " + username + " 删除成功");
            
        } catch (Exception e) {
            log.error("删除用户失败: {}", username, e);
            return ApiResponse.failure("删除用户失败: " + e.getMessage());
        }
    }

    /**
     * 初始化基础角色数据
     * @return 操作结果
     */
    @PostMapping("/init-roles")
    @Transactional
    public ApiResponse<String> initRoles() {
        try {
            log.info("开始初始化基础角色数据...");
            
            // 创建基础角色
            createRoleIfNotExists("超级管理员", "ADMIN", "系统超级管理员，拥有所有权限");
            createRoleIfNotExists("经理", "MANAGER", "部门经理，拥有大部分管理权限");
            createRoleIfNotExists("员工", "EMPLOYEE", "普通员工，拥有基础权限");
            createRoleIfNotExists("仓库管理员", "WAREHOUSE", "仓库管理员，拥有库存相关权限");
            
            log.info("基础角色数据初始化完成");
            return ApiResponse.success("基础角色数据初始化成功");
            
        } catch (Exception e) {
            log.error("角色数据初始化失败", e);
            return ApiResponse.failure("角色数据初始化失败: " + e.getMessage());
        }
    }

    /**
     * 获取系统数据统计
     * @return 统计信息
     */
    @GetMapping("/stats")
    public ApiResponse<String> getSystemStats() {
        try {
            long userCount = userRepository.count();
            long roleCount = roleRepository.count();
            
            String stats = String.format(
                "系统数据统计:\n" +
                "- 用户总数: %d\n" +
                "- 角色总数: %d\n",
                userCount, roleCount
            );
            
            return ApiResponse.success(stats);
        } catch (Exception e) {
            log.error("获取系统统计失败", e);
            return ApiResponse.failure("获取系统统计失败: " + e.getMessage());
        }
    }

    /**
     * 创建角色（如果不存在）
     */
    private void createRoleIfNotExists(String name, String code, String description) {
        Optional<SysRole> existing = roleRepository.findByRoleCode(code);
        if (existing.isPresent()) {
            log.info("角色已存在: {} ({})", name, code);
            return;
        }
        
        SysRole role = new SysRole();
        role.setRoleName(name);
        role.setRoleCode(code);
        role.setDescription(description);
        role.setStatus(1);
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        role.setIsDeleted(false);
        
        roleRepository.save(role);
        log.info("创建角色: {} ({})", name, code);
    }
} 