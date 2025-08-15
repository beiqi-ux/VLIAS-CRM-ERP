package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.RoleDTO;
import com.example.vliascrm.entity.SysRole;
import com.example.vliascrm.entity.SysRolePermission;
import com.example.vliascrm.entity.SysUserRole;
import com.example.vliascrm.exception.BusinessException;
import com.example.vliascrm.exception.ResourceNotFoundException;
import com.example.vliascrm.repository.SysRolePermissionRepository;
import com.example.vliascrm.repository.SysRoleRepository;
import com.example.vliascrm.repository.SysUserRoleRepository;
import com.example.vliascrm.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl implements SysRoleService {

    private final SysRoleRepository roleRepository;
    private final SysRolePermissionRepository rolePermissionRepository;
    private final SysUserRoleRepository userRoleRepository;

    @Override
    @Transactional
    public SysRole createRole(RoleDTO roleDTO) {
        // 检查角色编码是否存在
        if (roleRepository.existsByRoleCode(roleDTO.getRoleCode())) {
            throw new BusinessException("角色编码已存在");
        }

        SysRole role = new SysRole();
        BeanUtils.copyProperties(roleDTO, role);
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        role.setIsDeleted(false);

        SysRole savedRole = roleRepository.save(role);

        // 分配权限
        if (roleDTO.getPermissionIds() != null && !roleDTO.getPermissionIds().isEmpty()) {
            assignPermissions(savedRole.getId(), roleDTO.getPermissionIds());
        }

        return savedRole;
    }

    @Override
    @Transactional
    public SysRole updateRole(Long id, RoleDTO roleDTO) {
        SysRole role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("角色不存在"));

        // 检查是否为内置角色，如果是则不允许修改状态
        boolean isBuiltin = isBuiltinRole(role.getRoleCode());
        if (isBuiltin && roleDTO.getStatus() != null && !roleDTO.getStatus().equals(role.getStatus())) {
            throw new BusinessException("内置角色状态不允许修改");
        }

        // 如果修改了角色编码，需要检查是否存在
        if (!role.getRoleCode().equals(roleDTO.getRoleCode()) &&
                roleRepository.existsByRoleCode(roleDTO.getRoleCode())) {
            throw new BusinessException("角色编码已存在");
        }

        // 保存原始状态
        Integer originalStatus = role.getStatus();
        
        BeanUtils.copyProperties(roleDTO, role);
        role.setUpdateTime(LocalDateTime.now());

        // 如果是内置角色，恢复原始状态
        if (isBuiltin) {
            role.setStatus(originalStatus);
        }

        SysRole updatedRole = roleRepository.save(role);

        // 更新权限
        if (roleDTO.getPermissionIds() != null) {
            assignPermissions(id, roleDTO.getPermissionIds());
        }

        return updatedRole;
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        SysRole role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("角色不存在"));

        // 检查是否为内置角色
        if (isBuiltinRole(role.getRoleCode())) {
            throw new BusinessException("内置角色不允许删除");
        }

        // 检查是否有用户正在使用该角色
        List<SysUserRole> userRoles = userRoleRepository.findByRoleId(id);
        if (!userRoles.isEmpty()) {
            throw new BusinessException("该角色正在被用户使用，请先解除用户关联后再删除");
        }

        // 删除角色权限关联
        rolePermissionRepository.deleteByRoleId(id);

        // 删除用户角色关联
        userRoleRepository.deleteByRoleId(id);

        // 逻辑删除角色
        role.setIsDeleted(true);
        role.setUpdateTime(LocalDateTime.now());
        roleRepository.save(role);
    }

    @Override
    public SysRole getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("角色不存在"));
    }

    @Override
    public List<SysRole> getAllRoles() {
        Specification<SysRole> specification = (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("isDeleted"), false);
        };
        return roleRepository.findAll(specification);
    }

    @Override
    public Page<SysRole> getRolePage(String keyword, Pageable pageable) {
        Specification<SysRole> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 未删除条件
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            // 关键字搜索
            if (StringUtils.hasText(keyword)) {
                Predicate nameLike = criteriaBuilder.like(root.get("roleName"), "%" + keyword + "%");
                Predicate codeLike = criteriaBuilder.like(root.get("roleCode"), "%" + keyword + "%");
                Predicate descLike = criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
                predicates.add(criteriaBuilder.or(nameLike, codeLike, descLike));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return roleRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 检查角色是否存在
        if (!roleRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("角色不存在");
        }

        // 删除旧的权限关联
        rolePermissionRepository.deleteByRoleId(roleId);

        // 创建新的权限关联
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<SysRolePermission> rolePermissions = permissionIds.stream()
                    .map(permissionId -> {
                        SysRolePermission rolePermission = new SysRolePermission();
                        rolePermission.setRoleId(roleId);
                        rolePermission.setPermissionId(permissionId);
                        rolePermission.setCreateTime(LocalDateTime.now());
                        return rolePermission;
                    })
                    .collect(Collectors.toList());

            rolePermissionRepository.saveAll(rolePermissions);
        }
    }

    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        List<SysRolePermission> rolePermissions = rolePermissionRepository.findByRoleId(roleId);
        return rolePermissions.stream()
                .map(SysRolePermission::getPermissionId)
                .collect(Collectors.toList());
    }

    @Override
    public List<SysRole> getUserRoles(Long userId) {
        return roleRepository.findRolesByUserId(userId);
    }

    @Override
    @Transactional
    public void assignUserRoles(Long userId, List<Long> roleIds) {
        // 删除旧的用户角色关联
        userRoleRepository.deleteByUserId(userId);

        // 创建新的用户角色关联
        if (roleIds != null && !roleIds.isEmpty()) {
            List<SysUserRole> userRoles = roleIds.stream()
                    .map(roleId -> {
                        SysUserRole userRole = new SysUserRole();
                        userRole.setUserId(userId);
                        userRole.setRoleId(roleId);
                        userRole.setCreateTime(LocalDateTime.now());
                        return userRole;
                    })
                    .collect(Collectors.toList());

            userRoleRepository.saveAll(userRoles);
        }
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        List<SysUserRole> userRoles = userRoleRepository.findByUserId(userId);
        return userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());
    }

    /**
     * 检查是否为内置角色
     * @param roleCode 角色编码
     * @return 是否为内置角色
     */
    private boolean isBuiltinRole(String roleCode) {
        // 内置角色编码列表
        String[] builtinRoles = {"ADMIN", "MANAGER", "EMPLOYEE", "WAREHOUSE"};
        for (String builtin : builtinRoles) {
            if (builtin.equals(roleCode)) {
                return true;
            }
        }
        return false;
    }
} 