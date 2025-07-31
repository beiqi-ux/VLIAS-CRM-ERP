package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.PermissionDTO;
import com.example.vliascrm.entity.SysPermission;
import com.example.vliascrm.exception.BusinessException;
import com.example.vliascrm.exception.ResourceNotFoundException;
import com.example.vliascrm.repository.SysPermissionRepository;
import com.example.vliascrm.repository.SysRolePermissionRepository;
import com.example.vliascrm.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {

    private final SysPermissionRepository permissionRepository;
    private final SysRolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public SysPermission createPermission(PermissionDTO permissionDTO) {
        // 检查权限编码是否存在
        if (permissionRepository.existsByPermissionCode(permissionDTO.getPermissionCode())) {
            throw new BusinessException("权限编码已存在");
        }

        SysPermission permission = new SysPermission();
        BeanUtils.copyProperties(permissionDTO, permission);
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        permission.setIsDeleted(false);

        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public SysPermission updatePermission(Long id, PermissionDTO permissionDTO) {
        SysPermission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("权限不存在"));

        // 如果修改了权限编码，需要检查是否存在
        if (!permission.getPermissionCode().equals(permissionDTO.getPermissionCode()) &&
                permissionRepository.existsByPermissionCode(permissionDTO.getPermissionCode())) {
            throw new BusinessException("权限编码已存在");
        }

        BeanUtils.copyProperties(permissionDTO, permission);
        permission.setUpdateTime(LocalDateTime.now());

        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        SysPermission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("权限不存在"));

        // 检查是否有子权限
        List<SysPermission> children = permissionRepository.findByParentIdAndStatusAndIsDeletedOrderByIdAsc(id, 1, false);
        if (!children.isEmpty()) {
            throw new BusinessException("请先删除子权限");
        }

        // 删除角色权限关联
        rolePermissionRepository.deleteByPermissionId(id);

        // 逻辑删除权限
        permission.setIsDeleted(true);
        permission.setUpdateTime(LocalDateTime.now());
        permissionRepository.save(permission);
    }

    @Override
    public SysPermission getPermissionById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("权限不存在"));
    }

    @Override
    public List<SysPermission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public List<PermissionDTO> getPermissionTree() {
        // 获取所有未删除的权限
        List<SysPermission> allPermissions = permissionRepository.findAll().stream()
                .filter(p -> !p.getIsDeleted() && p.getStatus() == 1)
                .collect(Collectors.toList());

        // 转换为DTO
        List<PermissionDTO> dtoList = allPermissions.stream().map(p -> {
            PermissionDTO dto = new PermissionDTO();
            BeanUtils.copyProperties(p, dto);
            return dto;
        }).collect(Collectors.toList());

        // 构建树形结构
        Map<Long, List<PermissionDTO>> parentMap = dtoList.stream()
                .collect(Collectors.groupingBy(PermissionDTO::getParentId));

        // 获取顶级权限
        List<PermissionDTO> rootPermissions = parentMap.getOrDefault(0L, new ArrayList<>());

        // 递归设置子权限
        rootPermissions.forEach(root -> setChildren(root, parentMap));

        return rootPermissions;
    }

    private void setChildren(PermissionDTO parent, Map<Long, List<PermissionDTO>> parentMap) {
        List<PermissionDTO> children = parentMap.getOrDefault(parent.getId(), new ArrayList<>());
        parent.setChildren(children);
        children.forEach(child -> setChildren(child, parentMap));
    }

    @Override
    public List<SysPermission> getPermissionsByRoleId(Long roleId) {
        return permissionRepository.findPermissionsByRoleId(roleId);
    }

    @Override
    public List<SysPermission> getPermissionsByUserId(Long userId) {
        return permissionRepository.findPermissionsByUserId(userId);
    }
} 