package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.UserDTO;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.entity.SysOrganization;
import com.example.vliascrm.repository.SysUserRepository;
import com.example.vliascrm.service.OrgDepartmentService;
import com.example.vliascrm.service.OrgPositionService;
import com.example.vliascrm.service.SysOrganizationService;
import com.example.vliascrm.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import com.example.vliascrm.entity.OrgDepartment;
import com.example.vliascrm.entity.OrgPosition;
import com.example.vliascrm.entity.OrgPosition;
import io.swagger.v3.oas.annotations.Operation;
import com.example.vliascrm.entity.SysRole;

/**
 * 系统用户管理控制器
 */
@RestController
@RequestMapping("/api/sys/users")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final SysUserService sysUserService;
    private final SysOrganizationService organizationService;
    private final OrgDepartmentService departmentService;
    private final OrgPositionService positionService;

    /**
     * 获取用户列表（支持分页和条件查询）
     */
    @GetMapping
    @PreAuthorize("hasAuthority('user-management:view')")
    public ApiResponse<Map<String, Object>> getUserList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status) {
        
        // 创建分页对象 (前端已经传递了从0开始的页码)
        Pageable pageable = PageRequest.of(page, size);
        
        // 创建查询条件
        Specification<SysUser> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 用户名模糊查询
            if (username != null && !username.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }
            
            // 真实姓名模糊查询
            if (realName != null && !realName.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("realName"), "%" + realName + "%"));
            }
            
            // 状态查询
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            // 只查询未删除的用户
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        // 执行查询
        Page<SysUser> userPage = sysUserRepository.findAll(spec, pageable);
        
        // 清空密码
        userPage.getContent().forEach(user -> user.setPassword(null));
        
        // 构造返回结果
        Map<String, Object> result = Map.of(
            "data", userPage.getContent(),
            "total", userPage.getTotalElements(),
            "current", page,
            "size", size,
            "pages", userPage.getTotalPages()
        );
        
        return ApiResponse.success(result);
    }

    /**
     * 获取单个用户
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user-management:view')")
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
    @PreAuthorize("hasAuthority('user-management:create')")
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
    @PreAuthorize("hasAuthority('user-management:edit')")
    public ApiResponse<SysUser> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return sysUserRepository.findById(id)
                .map(existingUser -> {
                    // 更新用户信息
                    if (userDTO.getUsername() != null) {
                        existingUser.setUsername(userDTO.getUsername());
                    }
                    
                    if (userDTO.getRealName() != null) {
                        existingUser.setRealName(userDTO.getRealName());
                    }
                    
                    if (userDTO.getEmail() != null) {
                        existingUser.setEmail(userDTO.getEmail());
                    }
                    
                    if (userDTO.getMobile() != null) {
                        existingUser.setMobile(userDTO.getMobile());
                    }
                    
                    if (userDTO.getAvatar() != null) {
                        existingUser.setAvatar(userDTO.getAvatar());
                    }
                    
                    if (userDTO.getGender() != null) {
                        existingUser.setGender(userDTO.getGender());
                    }
                    
                    // 对于组织/部门/岗位ID，直接使用DTO中的值（包括null值）
                    // 这样可以支持清空关联关系
                    existingUser.setOrgId(userDTO.getOrgId());
                    existingUser.setDeptId(userDTO.getDeptId());
                    existingUser.setPositionId(userDTO.getPositionId());
                    
                    if (userDTO.getStatus() != null) {
                        existingUser.setStatus(userDTO.getStatus());
                    }
                    
                    // 如果提供了新密码则更新密码
                    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
    @PreAuthorize("hasAuthority('user-management:delete')")
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
    @PreAuthorize("hasAuthority('user-management:edit')")
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
    @PreAuthorize("hasAuthority('user-management:reset-password')")
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
     * 更新用户状态
     * @param id 用户ID
     * @param status 状态 (1: 启用, 0: 禁用)
     * @return 更新后的用户
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "更新用户状态", description = "更新用户的启用/禁用状态")
    @PreAuthorize("hasAuthority('user-management:edit')")
    public ApiResponse<SysUser> updateUserStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            Optional<SysUser> userOptional = sysUserRepository.findById(id);
            if (userOptional.isEmpty()) {
                return ApiResponse.failure("用户不存在");
            }
            
            SysUser user = userOptional.get();
            user.setStatus(status);
            SysUser updatedUser = sysUserRepository.save(user);
            
            // 安全考虑，清空返回结果中的密码
            updatedUser.setPassword(null);
            
            return ApiResponse.success(updatedUser);
        } catch (Exception e) {
            return ApiResponse.failure("更新用户状态失败: " + e.getMessage());
        }
    }

    /**
     * 生成随机密码
     */
    private String generateRandomPassword() {
        // 简单的随机密码生成，实际应用中可以使用更复杂的算法
        return "P" + System.currentTimeMillis() % 1000000;
    }

    /**
     * 获取用户详情列表（包含组织、部门和岗位名称）
     */
    @GetMapping("/detailed")
    @PreAuthorize("hasAuthority('user-management:view')")
    public ApiResponse<Map<String, Object>> getDetailedUserList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status) {
        
        // 创建分页对象 (前端已经传递了从0开始的页码)
        Pageable pageable = PageRequest.of(page, size);
        
        // 创建查询条件
        Specification<SysUser> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 用户名模糊查询
            if (username != null && !username.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }
            
            // 真实姓名模糊查询
            if (realName != null && !realName.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("realName"), "%" + realName + "%"));
            }
            
            // 状态查询
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            // 只查询未删除的用户
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        // 执行查询
        Page<SysUser> userPage = sysUserRepository.findAll(spec, pageable);
        
        // 转换为DTO并填充组织、部门和岗位名称
        List<UserDTO> userDTOList = userPage.getContent().stream().map(user -> {
            UserDTO dto = UserDTO.fromSysUser(user);
            
            // 填充组织名称
            if (user.getOrgId() != null) {
                SysOrganization org = organizationService.findById(user.getOrgId());
                if (org != null) {
                    dto.setOrgName(org.getOrgName());
                }
            }
            
            // 填充部门名称
            if (user.getDeptId() != null) {
                OrgDepartment dept = departmentService.findById(user.getDeptId());
                if (dept != null) {
                    dto.setDeptName(dept.getDeptName());
                }
            }
            
            // 填充岗位名称
            if (user.getPositionId() != null) {
                OrgPosition position = positionService.findById(user.getPositionId());
                if (position != null) {
                    dto.setPositionName(position.getPositionName());
                }
            }
            
            return dto;
        }).collect(Collectors.toList());
        
        // 构造返回结果
        Map<String, Object> result = Map.of(
            "data", userDTOList,
            "total", userPage.getTotalElements(),
            "current", page,
            "size", size,
            "pages", userPage.getTotalPages()
        );
        
        return ApiResponse.success(result);
    }


} 