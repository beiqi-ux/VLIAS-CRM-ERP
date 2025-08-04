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
        // 检查权限编码是否已存在（只检查未删除的权限）
        if (permissionRepository.existsByPermissionCodeAndIsDeleted(permissionDTO.getPermissionCode(), false)) {
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

        // 如果修改了权限编码，需要检查是否存在（只检查未删除的权限）
        if (!permission.getPermissionCode().equals(permissionDTO.getPermissionCode()) &&
                permissionRepository.existsByPermissionCodeAndIsDeleted(permissionDTO.getPermissionCode(), false)) {
            throw new BusinessException("权限编码已存在");
        }

        // 检查是否为核心权限，不允许禁用
        if ((permission.getIsCore() != null && permission.getIsCore() == 1) || 
            isCorePermissionByCode(permission.getPermissionCode())) {
            if (permissionDTO.getStatus() != null && permissionDTO.getStatus() == 0) {
                throw new BusinessException("核心权限（系统管理、个人中心等）不能禁用");
            }
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

        // 检查是否为核心权限
        if ((permission.getIsCore() != null && permission.getIsCore() == 1) || 
            isCorePermissionByCode(permission.getPermissionCode())) {
            throw new BusinessException("核心权限（系统管理、个人中心等）不允许删除，以防止系统功能不可用");
        }

        // 检查是否有子权限
        List<SysPermission> children = permissionRepository.findByParentIdAndStatusAndIsDeletedOrderBySortAscIdAsc(id, 1, false);
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
        // 获取所有未删除且启用的权限
        List<SysPermission> allPermissions = permissionRepository.findByIsDeletedOrderBySortAscIdAsc(false).stream()
                .filter(p -> p.getStatus() == 1)
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

    /**
     * 获取权限管理页面专用的权限树（包括禁用的权限）
     * 这样管理员就能在权限管理界面看到并重新启用被禁用的权限
     */
    public List<PermissionDTO> getPermissionTreeForAdmin() {
        // 获取所有未删除的权限（包括禁用的）
        List<SysPermission> allPermissions = permissionRepository.findByIsDeletedOrderBySortAscIdAsc(false);

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

    /**
     * 根据权限编码判断是否为核心权限
     * @param permissionCode 权限编码
     * @return 是否为核心权限
     */
    private boolean isCorePermissionByCode(String permissionCode) {
        if (permissionCode == null) {
            return false;
        }
        
        // 定义核心权限编码列表
        String[] corePermissions = {
            "system",       // 系统管理
            "profile",      // 个人中心
            "permission",   // 权限管理
            "menu",         // 菜单管理
            "user",         // 用户管理
            "role"          // 角色管理
        };
        
        for (String corePermission : corePermissions) {
            if (permissionCode.equals(corePermission)) {
                return true;
            }
        }
        
        return false;
    }
} 