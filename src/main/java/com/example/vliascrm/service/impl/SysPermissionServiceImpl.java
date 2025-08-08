package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.PermissionDTO;
import com.example.vliascrm.entity.SysPermission;
import com.example.vliascrm.entity.SysMenu;
import com.example.vliascrm.exception.BusinessException;
import com.example.vliascrm.exception.ResourceNotFoundException;
import com.example.vliascrm.repository.SysPermissionRepository;
import com.example.vliascrm.repository.SysRolePermissionRepository;
import com.example.vliascrm.repository.SysMenuRepository;
import com.example.vliascrm.service.SysPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

/**
 * 权限服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {

    private final SysPermissionRepository permissionRepository;
    private final SysRolePermissionRepository rolePermissionRepository;
    private final SysMenuRepository menuRepository;

    @Override
    @Transactional
    public SysPermission createPermission(PermissionDTO permissionDTO) {
        try {
            // 检查权限编码是否已存在
            boolean exists = permissionRepository.existsByPermissionCodeAndIsDeleted(permissionDTO.getPermissionCode(), false);
            if (exists) {
                throw new BusinessException("权限编码已存在");
            }

            // 创建新的权限实体
            SysPermission permission = new SysPermission();
            BeanUtils.copyProperties(permissionDTO, permission);
            
            // 设置基础字段
            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            permission.setIsDeleted(false);
            permission.setLevelDepth(permissionDTO.getPermissionType());
            
            // 生成权限路径
            String permissionPath = generatePermissionPath(permission);
            permission.setPermissionPath(permissionPath);
            
            // 保存权限
            return permissionRepository.save(permission);
            
        } catch (Exception e) {
            log.error("创建权限失败: {}", e.getMessage(), e);
            throw e;
        }
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
        
        // 保存不应该被更新的字段
        Boolean originalIsDeleted = permission.getIsDeleted();
        LocalDateTime originalCreateTime = permission.getCreateTime();
        String originalCreateBy = permission.getCreateBy();
        
        BeanUtils.copyProperties(permissionDTO, permission);
        
        // 恢复不应该被更新的字段
        permission.setIsDeleted(originalIsDeleted);
        permission.setCreateTime(originalCreateTime);
        permission.setCreateBy(originalCreateBy);
        permission.setUpdateTime(LocalDateTime.now());
        
        // 更新层级深度
        permission.setLevelDepth(permissionDTO.getPermissionType());
        
        // 如果权限编码或父ID发生变化，需要重新生成路径
        if (!oldPermissionCode.equals(permission.getPermissionCode()) || 
            !Objects.equals(oldParentId, permission.getParentId())) {
            updatePermissionPath(permission);
        }

        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        SysPermission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("权限不存在"));

        // 检查是否为核心权限，不允许删除
        if ((permission.getIsCore() != null && permission.getIsCore() == 1) || 
            isCorePermissionByCode(permission.getPermissionCode())) {
            throw new BusinessException("核心权限（系统管理、个人中心等）不能删除");
        }

        // 软删除：设置 isDeleted 为 true
        permission.setIsDeleted(true);
        permission.setUpdateTime(LocalDateTime.now());
        permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public SysPermission updatePermissionStatus(Long id, Integer status) {
        SysPermission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("权限不存在"));

        // 检查是否为核心权限，不允许禁用
        if ((permission.getIsCore() != null && permission.getIsCore() == 1) || 
            isCorePermissionByCode(permission.getPermissionCode())) {
            if (status != null && status == 0) {
                throw new BusinessException("核心权限（系统管理、个人中心等）不能禁用");
            }
        }

        // 更新当前权限状态
        permission.setStatus(status);
        permission.setUpdateTime(LocalDateTime.now());
        SysPermission updatedPermission = permissionRepository.save(permission);

        // 级联更新逻辑
        if (status == 0) {
            // 禁用时：递归禁用所有子权限
            cascadeDisableChildren(id);
        } else if (status == 1) {
            // 启用时：确保父权限也被启用
            cascadeEnableParents(permission.getParentId());
        }

        return updatedPermission;
    }

    /**
     * 递归禁用所有子权限
     * @param parentId 父权限ID
     */
    private void cascadeDisableChildren(Long parentId) {
        List<SysPermission> children = permissionRepository.findByParentIdAndIsDeletedOrderBySortOrderAscIdAsc(parentId, false);
        
        for (SysPermission child : children) {
            // 跳过核心权限
            if ((child.getIsCore() != null && child.getIsCore() == 1) || 
                isCorePermissionByCode(child.getPermissionCode())) {
                continue;
            }
            
            // 只禁用当前启用的权限
            if (child.getStatus() == 1) {
                child.setStatus(0);
                child.setUpdateTime(LocalDateTime.now());
                permissionRepository.save(child);
                
                // 递归禁用子权限
                cascadeDisableChildren(child.getId());
            }
        }
    }

    /**
     * 递归启用父权限
     * @param parentId 父权限ID
     */
    private void cascadeEnableParents(Long parentId) {
        if (parentId == null) {
            return;
        }
        
        SysPermission parent = permissionRepository.findById(parentId).orElse(null);
        if (parent != null && parent.getStatus() == 0) {
            // 启用父权限
            parent.setStatus(1);
            parent.setUpdateTime(LocalDateTime.now());
            permissionRepository.save(parent);
            
            // 递归启用上级父权限
            cascadeEnableParents(parent.getParentId());
        }
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

    @Override
    @Transactional
    public int syncMenuPermissions() {
        int syncCount = 0;
        
        // 获取所有未删除的菜单
        List<SysMenu> menus = menuRepository.findAll().stream()
            .filter(menu -> !menu.getIsDeleted())
            .sorted((m1, m2) -> {
                int parentCompare = Long.compare(
                    m1.getParentId() != null ? m1.getParentId() : 0L,
                    m2.getParentId() != null ? m2.getParentId() : 0L
                );
                if (parentCompare != 0) return parentCompare;
                return Integer.compare(
                    m1.getSort() != null ? m1.getSort() : 0,
                    m2.getSort() != null ? m2.getSort() : 0
                );
            })
            .collect(Collectors.toList());
        
        for (SysMenu menu : menus) {
            // 检查是否有权限编码
            if (menu.getPermissionCode() == null || menu.getPermissionCode().trim().isEmpty()) {
                continue;
            }
            
            // 检查权限是否已存在
            Optional<SysPermission> existingPermission = permissionRepository
                .findByPermissionCodeAndIsDeleted(menu.getPermissionCode(), false);
            
            if (existingPermission.isPresent()) {
                continue; // 权限已存在，跳过
            }
            
            // 创建新权限
            SysPermission permission = new SysPermission();
            permission.setPermissionName(menu.getMenuName());
            permission.setPermissionCode(menu.getPermissionCode());
            permission.setDescription("菜单权限：" + menu.getMenuName());
            
            // 根据菜单层级设置权限层级
            if (menu.getParentId() == 0) {
                // 顶级菜单（目录）- 一级权限
                permission.setPermissionType(1);
                permission.setLevelDepth(1);
                permission.setParentId(0L);
            } else {
                // 子菜单 - 需要查找父菜单确定层级
                SysMenu parentMenu = menuRepository.findById(menu.getParentId()).orElse(null);
                if (parentMenu != null && parentMenu.getPermissionCode() != null) {
                    // 查找父菜单对应的权限
                    Optional<SysPermission> parentPermission = permissionRepository
                        .findByPermissionCodeAndIsDeleted(parentMenu.getPermissionCode(), false);
                    
                    if (parentPermission.isPresent()) {
                        permission.setParentId(parentPermission.get().getId());
                        permission.setLevelDepth(parentPermission.get().getLevelDepth() + 1);
                        permission.setPermissionType(parentPermission.get().getPermissionType() + 1);
                    } else {
                        // 如果父权限不存在，设为二级权限
                        permission.setPermissionType(2);
                        permission.setLevelDepth(2);
                        permission.setParentId(0L);
                    }
                } else {
                    // 如果找不到父菜单，设为二级权限
                    permission.setPermissionType(2);
                    permission.setLevelDepth(2);
                    permission.setParentId(0L);
                }
            }
            
            // 关联菜单ID和其他信息
            permission.setMenuId(menu.getId());
            permission.setSortOrder(menu.getSort() != null ? menu.getSort() : 0);
            permission.setStatus(1);
            permission.setIsCore(0);
            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            permission.setIsDeleted(false);
            
            // 保存权限
            SysPermission savedPermission = permissionRepository.save(permission);
            
            // 更新权限路径
            updatePermissionPath(savedPermission);
            
            syncCount++;
        }
        
        return syncCount;
    }
} 