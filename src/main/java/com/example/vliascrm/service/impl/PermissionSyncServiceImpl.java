package com.example.vliascrm.service.impl;

import com.example.vliascrm.config.PermissionSyncConfig;
import com.example.vliascrm.entity.SysPermission;
import com.example.vliascrm.repository.SysPermissionRepository;
import com.example.vliascrm.service.PermissionSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 权限同步服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionSyncServiceImpl implements PermissionSyncService {

    private final PermissionSyncConfig permissionSyncConfig;
    private final SysPermissionRepository permissionRepository;

    @Override
    @Transactional
    public String syncAllPermissions() {
        log.info("开始同步所有权限...");
        
        if (!permissionSyncConfig.isEnabled()) {
            return "权限同步功能未启用，请在配置文件中设置 vlias.permission-sync.enabled=true";
        }

        if (permissionSyncConfig.getModules() == null || permissionSyncConfig.getModules().isEmpty()) {
            return "未找到权限配置，请检查配置文件";
        }

        StringBuilder result = new StringBuilder();
        int addedCount = 0;
        int updatedCount = 0;
        int skippedCount = 0;

        for (PermissionSyncConfig.ModuleConfig module : permissionSyncConfig.getModules()) {
            // 同步一级权限（模块权限）
            SyncResult moduleResult = syncModulePermission(module);
            SysPermission modulePermission = moduleResult.permission;
            addedCount += moduleResult.added ? 1 : 0;
            updatedCount += moduleResult.updated ? 1 : 0;
            skippedCount += moduleResult.skipped ? 1 : 0;
            
            if (moduleResult.added) {
                result.append("新增模块权限: ").append(module.getName()).append("\n");
            } else if (moduleResult.updated) {
                result.append("更新模块权限: ").append(module.getName()).append("\n");
            } else if (moduleResult.skipped) {
                result.append("跳过模块权限: ").append(module.getName()).append(" (已存在且无需更新)\n");
            }

            // 同步二级权限（子模块权限）
            if (module.getSubmodules() != null && !module.getSubmodules().isEmpty()) {
                for (PermissionSyncConfig.SubmoduleConfig submodule : module.getSubmodules()) {
                    SyncResult submoduleResult = syncSubmodulePermission(submodule, modulePermission);
                    SysPermission submodulePermission = submoduleResult.permission;
                    addedCount += submoduleResult.added ? 1 : 0;
                    updatedCount += submoduleResult.updated ? 1 : 0;
                    skippedCount += submoduleResult.skipped ? 1 : 0;
                    
                    if (submoduleResult.added) {
                        result.append("  新增子模块权限: ").append(submodule.getName()).append("\n");
                    } else if (submoduleResult.updated) {
                        result.append("  更新子模块权限: ").append(submodule.getName()).append("\n");
                    } else if (submoduleResult.skipped) {
                        result.append("  跳过子模块权限: ").append(submodule.getName()).append(" (已存在且无需更新)\n");
                    }

                    // 同步三级权限（操作权限）
                    if (submodule.getOperations() != null && !submodule.getOperations().isEmpty()) {
                        for (PermissionSyncConfig.OperationConfig operation : submodule.getOperations()) {
                            SyncResult operationResult = syncOperationPermission(operation, submodulePermission);
                            addedCount += operationResult.added ? 1 : 0;
                            updatedCount += operationResult.updated ? 1 : 0;
                            skippedCount += operationResult.skipped ? 1 : 0;
                            
                            if (operationResult.added) {
                                result.append("    新增操作权限: ").append(operation.getName()).append("\n");
                            } else if (operationResult.updated) {
                                result.append("    更新操作权限: ").append(operation.getName()).append("\n");
                            } else if (operationResult.skipped) {
                                result.append("    跳过操作权限: ").append(operation.getName()).append(" (已存在且无需更新)\n");
                            }
                        }
                    }
                }
            }
        }

        String summary = String.format("权限同步完成！新增: %d, 更新: %d, 跳过: %d\n", addedCount, updatedCount, skippedCount);
        log.info(summary);
        
        return summary + "\n" + result.toString();
    }

    @Override
    @Transactional
    public String syncModulePermissions(String moduleCode) {
        log.info("开始同步模块权限: {}", moduleCode);

        if (!permissionSyncConfig.isEnabled()) {
            return "权限同步功能未启用";
        }

        Optional<PermissionSyncConfig.ModuleConfig> moduleOpt = permissionSyncConfig.getModules().stream()
                .filter(m -> m.getCode().equals(moduleCode))
                .findFirst();

        if (!moduleOpt.isPresent()) {
            return "未找到模块配置: " + moduleCode;
        }

        PermissionSyncConfig.ModuleConfig module = moduleOpt.get();
        StringBuilder result = new StringBuilder();

        // 同步模块权限
        SyncResult moduleResult = syncModulePermission(module);
        result.append("同步模块权限: ").append(module.getName()).append("\n");

        // 同步子模块权限
        if (module.getSubmodules() != null) {
            for (PermissionSyncConfig.SubmoduleConfig submodule : module.getSubmodules()) {
                SyncResult submoduleResult = syncSubmodulePermission(submodule, moduleResult.permission);
                result.append("  同步子模块权限: ").append(submodule.getName()).append("\n");
                
                // 同步操作权限
                if (submodule.getOperations() != null) {
                    for (PermissionSyncConfig.OperationConfig operation : submodule.getOperations()) {
                        syncOperationPermission(operation, submoduleResult.permission);
                        result.append("    同步操作权限: ").append(operation.getName()).append("\n");
                    }
                }
            }
        }

        return result.toString();
    }

    @Override
    public String validatePermissionConfig() {
        StringBuilder result = new StringBuilder();
        result.append("权限配置验证结果:\n");
        
        if (!permissionSyncConfig.isEnabled()) {
            result.append("❌ 权限同步功能未启用\n");
            return result.toString();
        }
        
        result.append("✅ 权限同步功能已启用\n");
        
        if (permissionSyncConfig.getModules() == null || permissionSyncConfig.getModules().isEmpty()) {
            result.append("❌ 未配置任何模块权限\n");
            return result.toString();
        }
        
        result.append("✅ 配置的模块数量: ").append(permissionSyncConfig.getModules().size()).append("\n\n");
        
        for (PermissionSyncConfig.ModuleConfig module : permissionSyncConfig.getModules()) {
            result.append("模块: ").append(module.getName()).append(" (").append(module.getCode()).append(")\n");
            if (module.getSubmodules() != null && !module.getSubmodules().isEmpty()) {
                result.append("  子模块数量: ").append(module.getSubmodules().size()).append("\n");
                for (PermissionSyncConfig.SubmoduleConfig submodule : module.getSubmodules()) {
                    result.append("    - ").append(submodule.getName()).append(" (").append(submodule.getCode()).append(")\n");
                    if (submodule.getOperations() != null) {
                        result.append("      操作数量: ").append(submodule.getOperations().size()).append("\n");
                        for (PermissionSyncConfig.OperationConfig operation : submodule.getOperations()) {
                            result.append("        - ").append(operation.getName()).append(" (").append(operation.getCode()).append(")\n");
                        }
                    }
                }
            }
            result.append("\n");
        }
        
        return result.toString();
    }

    /**
     * 同步结果类
     */
    private static class SyncResult {
        public SysPermission permission;
        public boolean added = false;
        public boolean updated = false;
        public boolean skipped = false;
        
        public SyncResult(SysPermission permission, boolean added, boolean updated, boolean skipped) {
            this.permission = permission;
            this.added = added;
            this.updated = updated;
            this.skipped = skipped;
        }
    }

    /**
     * 同步模块权限（一级权限）
     */
    private SyncResult syncModulePermission(PermissionSyncConfig.ModuleConfig module) {
        Optional<SysPermission> existingPermission = permissionRepository.findByPermissionCodeAndIsDeleted(module.getCode(), false);
        
        SysPermission permission;
        boolean added = false;
        boolean updated = false;
        boolean skipped = false;
        
        if (existingPermission.isPresent()) {
            permission = existingPermission.get();
            // 检查是否需要更新
            boolean needUpdate = !permission.getPermissionName().equals(module.getName()) ||
                                !permission.getDescription().equals(module.getDescription()) ||
                                !permission.getSortOrder().equals(module.getSort() != null ? module.getSort() : 0);
            
            if (needUpdate) {
                // 更新现有权限
                permission.setPermissionName(module.getName());
                permission.setDescription(module.getDescription());
                permission.setSortOrder(module.getSort() != null ? module.getSort() : 0);
                permission.setStatus(1);
                permission.setIsDeleted(false);
                permission.setUpdateTime(LocalDateTime.now());
                updated = true;
            } else {
                skipped = true;
            }
        } else {
            // 创建新权限
            permission = new SysPermission();
            permission.setPermissionName(module.getName());
            permission.setPermissionCode(module.getCode());
            permission.setPermissionType(1); // 一级权限（模块）
            permission.setLevelDepth(1);
            permission.setParentId(0L);
            permission.setDescription(module.getDescription());
            permission.setSortOrder(module.getSort() != null ? module.getSort() : 0);
            permission.setStatus(1);
            permission.setIsCore(0);
            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            permission.setIsDeleted(false);
            added = true;
        }

        permission = permissionRepository.save(permission);
        return new SyncResult(permission, added, updated, skipped);
    }

    /**
     * 同步子模块权限（二级权限）
     */
    private SyncResult syncSubmodulePermission(PermissionSyncConfig.SubmoduleConfig submodule, SysPermission parentPermission) {
        Optional<SysPermission> existingPermission = permissionRepository.findByPermissionCodeAndIsDeleted(submodule.getCode(), false);
        
        SysPermission permission;
        boolean added = false;
        boolean updated = false;
        boolean skipped = false;
        
        if (existingPermission.isPresent()) {
            permission = existingPermission.get();
            // 检查是否需要更新
            boolean needUpdate = !permission.getPermissionName().equals(submodule.getName()) ||
                                !permission.getDescription().equals(submodule.getDescription()) ||
                                !permission.getParentId().equals(parentPermission.getId()) ||
                                !permission.getSortOrder().equals(submodule.getSort() != null ? submodule.getSort() : 0);
            
            if (needUpdate) {
                // 更新现有权限
                permission.setPermissionName(submodule.getName());
                permission.setDescription(submodule.getDescription());
                permission.setParentId(parentPermission.getId());
                permission.setSortOrder(submodule.getSort() != null ? submodule.getSort() : 0);
                permission.setStatus(1);
                permission.setIsDeleted(false);
                permission.setUpdateTime(LocalDateTime.now());
                updated = true;
            } else {
                skipped = true;
            }
        } else {
            // 创建新权限
            permission = new SysPermission();
            permission.setPermissionName(submodule.getName());
            permission.setPermissionCode(submodule.getCode());
            permission.setPermissionType(2); // 二级权限（子模块）
            permission.setLevelDepth(2);
            permission.setParentId(parentPermission.getId());
            permission.setDescription(submodule.getDescription());
            permission.setSortOrder(submodule.getSort() != null ? submodule.getSort() : 0);
            permission.setStatus(1);
            permission.setIsCore(0);
            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            permission.setIsDeleted(false);
            added = true;
        }

        permission = permissionRepository.save(permission);
        return new SyncResult(permission, added, updated, skipped);
    }

    /**
     * 同步操作权限（三级权限）
     */
    private SyncResult syncOperationPermission(PermissionSyncConfig.OperationConfig operation, SysPermission parentPermission) {
        String operationCode = parentPermission.getPermissionCode() + ":" + operation.getCode();
        Optional<SysPermission> existingPermission = permissionRepository.findByPermissionCodeAndIsDeleted(operationCode, false);
        
        SysPermission permission;
        boolean added = false;
        boolean updated = false;
        boolean skipped = false;
        
        if (existingPermission.isPresent()) {
            permission = existingPermission.get();
            // 检查是否需要更新
            boolean needUpdate = !permission.getPermissionName().equals(operation.getName()) ||
                                !permission.getDescription().equals(operation.getDescription()) ||
                                !permission.getParentId().equals(parentPermission.getId()) ||
                                !permission.getSortOrder().equals(operation.getSort() != null ? operation.getSort() : 0);
            
            if (needUpdate) {
                // 更新现有权限
                permission.setPermissionName(operation.getName());
                permission.setDescription(operation.getDescription());
                permission.setParentId(parentPermission.getId());
                permission.setSortOrder(operation.getSort() != null ? operation.getSort() : 0);
                permission.setStatus(1);
                permission.setIsDeleted(false);
                permission.setUpdateTime(LocalDateTime.now());
                updated = true;
            } else {
                skipped = true;
            }
        } else {
            // 创建新权限
            permission = new SysPermission();
            permission.setPermissionName(operation.getName());
            permission.setPermissionCode(operationCode);
            permission.setPermissionType(3); // 三级权限（操作）
            permission.setLevelDepth(3);
            permission.setParentId(parentPermission.getId());
            permission.setDescription(operation.getDescription());
            permission.setSortOrder(operation.getSort() != null ? operation.getSort() : 0);
            permission.setStatus(1);
            permission.setIsCore(0);
            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            permission.setIsDeleted(false);
            added = true;
        }

        permission = permissionRepository.save(permission);
        return new SyncResult(permission, added, updated, skipped);
    }
} 