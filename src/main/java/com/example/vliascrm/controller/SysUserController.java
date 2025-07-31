package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.UserDTO;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.repository.SysUserRepository;
import com.example.vliascrm.service.OrgDepartmentService;
import com.example.vliascrm.service.OrgPositionService;
import com.example.vliascrm.service.SysOrganizationService;
import com.example.vliascrm.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.example.vliascrm.entity.SysOrganization;

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
    public ApiResponse<Map<String, Object>> getUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status) {
        
        // 创建分页对象
        Pageable pageable = PageRequest.of(page - 1, size);
        
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

    /**
     * 获取用户详情列表（包含组织、部门和岗位名称）
     */
    @GetMapping("/detailed")
    public ApiResponse<Map<String, Object>> getDetailedUserList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String realName,
            @RequestParam(required = false) Integer status) {
        
        // 创建分页对象
        Pageable pageable = PageRequest.of(page - 1, size);
        
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