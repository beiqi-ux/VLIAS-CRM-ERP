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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

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
        
        // 设置层级深度
        permission.setLevelDepth(permissionDTO.getPermissionType());
        
        // 生成权限路径
        permission.setPermissionPath(generatePermissionPath(permission));

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

        // 记录原有的权限编码和父ID，用于判断是否需要更新路径
        String oldPermissionCode = permission.getPermissionCode();
        Long oldParentId = permission.getParentId();
        
        BeanUtils.copyProperties(permissionDTO, permission);
        permission.setUpdateTime(LocalDateTime.now());
        
        // 更新层级深度
        permission.setLevelDepth(permissionDTO.getPermissionType());
        
        // 如果权限编码或父ID发生变化，需要重新生成路径
        if (!oldPermissionCode.equals(permission.getPermissionCode()) || 
            !oldParentId.equals(permission.getParentId())) {
            updatePermissionPath(permission);
        }

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
        List<SysPermission> children = permissionRepository.findByParentIdAndStatusAndIsDeletedOrderBySortOrderAscIdAsc(id, 1, false);
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
        // 按照3级权限结构排序：权限类型 → 排序字段 → ID
        return permissionRepository.findAllPermissionsOrderByTypeAndSort(false);
    }

    @Override
    public Page<SysPermission> getPermissionPage(Specification<SysPermission> specification, Pageable pageable) {
        return permissionRepository.findAll(specification, pageable);
    }

    @Override
    public List<PermissionDTO> getPermissionTree() {
        // 获取所有未删除且启用的权限
        List<SysPermission> allPermissions = permissionRepository.findByIsDeletedOrderBySortOrderAscIdAsc(false).stream()
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
        List<SysPermission> allPermissions = permissionRepository.findByIsDeletedOrderBySortOrderAscIdAsc(false);

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
        // 获取用户直接分配的权限
        List<SysPermission> directPermissions = permissionRepository.findPermissionsByUserId(userId);
        
        // 如果没有直接权限，直接返回空列表
        if (directPermissions.isEmpty()) {
            return directPermissions;
        }
        
        // 关键修复：获取所有相关的父级权限
        // 当用户有子权限时，应该自动获得访问父级权限的能力
        Set<SysPermission> allAccessiblePermissions = new HashSet<>(directPermissions);
        
        // 为每个直接权限添加其所有父级权限
        for (SysPermission permission : directPermissions) {
            addParentPermissions(permission, allAccessiblePermissions);
        }
        
        // 转换为列表并按排序规则排序
        return allAccessiblePermissions.stream()
                .sorted((p1, p2) -> {
                    // 先按权限类型排序（1级 -> 2级 -> 3级）
                    int typeCompare = Integer.compare(p1.getPermissionType(), p2.getPermissionType());
                    if (typeCompare != 0) return typeCompare;
                    
                    // 再按排序字段排序
                    int sortCompare = Integer.compare(p1.getSortOrder(), p2.getSortOrder());
                    if (sortCompare != 0) return sortCompare;
                    
                    // 最后按ID排序
                    return Long.compare(p1.getId(), p2.getId());
                })
                .collect(Collectors.toList());
    }

    /**
     * 递归添加父级权限
     * 当用户有子权限时，应该自动获得访问所有父级权限的能力
     * 
     * @param permission 当前权限
     * @param allPermissions 所有权限集合
     */
    private void addParentPermissions(SysPermission permission, Set<SysPermission> allPermissions) {
        if (permission.getParentId() != null && permission.getParentId() > 0) {
            // 查找父权限
            Optional<SysPermission> parentPermission = permissionRepository.findById(permission.getParentId());
            if (parentPermission.isPresent() && 
                parentPermission.get().getStatus() == 1 && 
                !parentPermission.get().getIsDeleted()) {
                
                SysPermission parent = parentPermission.get();
                
                // 如果父权限还未添加，则添加并继续查找其父权限
                if (!allPermissions.contains(parent)) {
                    allPermissions.add(parent);
                    // 递归添加父权限的父权限
                    addParentPermissions(parent, allPermissions);
                }
            }
        }
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

    /**
     * 生成权限路径
     * @param permission 权限对象
     * @return 权限路径
     */
    private String generatePermissionPath(SysPermission permission) {
        if (permission.getParentId() == null || permission.getParentId() == 0) {
            // 一级权限，直接使用权限编码
            return "/" + permission.getPermissionCode();
        }
        
        // 查找父权限
        SysPermission parent = permissionRepository.findById(permission.getParentId()).orElse(null);
        if (parent == null) {
            return "/" + permission.getPermissionCode();
        }
        
        // 递归构建路径
        String parentPath = parent.getPermissionPath();
        if (parentPath == null || parentPath.isEmpty()) {
            parentPath = generatePermissionPath(parent);
        }
        
        return parentPath + "/" + permission.getPermissionCode();
    }

    /**
     * 更新权限路径
     * @param permission 权限对象
     */
    private void updatePermissionPath(SysPermission permission) {
        permission.setPermissionPath(generatePermissionPath(permission));
        permissionRepository.save(permission);
        
        // 更新所有子权限的路径
        updateChildrenPaths(permission.getId());
    }

    /**
     * 递归更新子权限路径
     * @param parentId 父权限ID
     */
    private void updateChildrenPaths(Long parentId) {
        List<SysPermission> children = permissionRepository.findByParentIdAndStatusAndIsDeletedOrderBySortOrderAscIdAsc(parentId, 1, false);
        for (SysPermission child : children) {
            child.setPermissionPath(generatePermissionPath(child));
            permissionRepository.save(child);
            updateChildrenPaths(child.getId());
        }
    }

    /**
     * 检查权限继承关系
     * @param userPermissions 用户权限编码列表
     * @param requiredPermission 需要检查的权限编码
     * @return 是否有权限
     */
    public boolean hasPermissionWithInheritance(List<String> userPermissions, String requiredPermission) {
        // 1. 直接权限检查
        if (userPermissions.contains(requiredPermission)) {
            return true;
        }
        
        // 2. 继承权限检查
        // 如果是三级权限（操作权限），检查是否有二级权限
        if (requiredPermission.contains(":")) {
            String[] parts = requiredPermission.split(":");
            if (parts.length == 2) {
                String submoduleCode = parts[0];
                if (userPermissions.contains(submoduleCode)) {
                    return true;
                }
                
                // 检查是否有一级权限
                String[] subParts = submoduleCode.split("-");
                if (subParts.length > 0) {
                    String moduleCode = subParts[0];
                    if (userPermissions.contains(moduleCode)) {
                        return true;
                    }
                }
            }
        }
        // 如果是二级权限，检查是否有一级权限
        else if (requiredPermission.contains("-")) {
            String[] parts = requiredPermission.split("-");
            if (parts.length > 0) {
                String moduleCode = parts[0];
                if (userPermissions.contains(moduleCode)) {
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * 获取指定类型的权限列表
     * @param permissionType 权限类型
     * @return 权限列表
     */
    public List<PermissionDTO> getPermissionsByType(Integer permissionType) {
        List<SysPermission> permissions = permissionRepository.findByPermissionTypeAndIsDeletedOrderBySortOrderAscIdAsc(permissionType, false);
        return permissions.stream().map(p -> {
            PermissionDTO dto = new PermissionDTO();
            BeanUtils.copyProperties(p, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 获取权限的所有子孙权限
     * @param permissionId 权限ID
     * @return 子孙权限列表
     */
    public List<PermissionDTO> getDescendantPermissions(Long permissionId) {
        SysPermission permission = permissionRepository.findById(permissionId).orElse(null);
        if (permission == null) {
            return new ArrayList<>();
        }
        
        String pathPrefix = permission.getPermissionPath();
        if (pathPrefix == null || pathPrefix.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<SysPermission> descendants = permissionRepository.findByPermissionPathStartingWithAndIsDeleted(pathPrefix + "/", false);
        return descendants.stream().map(p -> {
            PermissionDTO dto = new PermissionDTO();
            BeanUtils.copyProperties(p, dto);
            return dto;
        }).collect(Collectors.toList());
    }
} 