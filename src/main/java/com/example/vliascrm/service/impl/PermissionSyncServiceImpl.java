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

        for (PermissionSyncConfig.ModuleConfig module : permissionSyncConfig.getModules()) {
            // 同步一级权限（模块权限）
            SysPermission modulePermission = syncModulePermission(module);
            if (modulePermission.getId() == null) {
                addedCount++;
                result.append("新增模块权限: ").append(module.getName()).append("\n");
            } else {
                updatedCount++;
                result.append("更新模块权限: ").append(module.getName()).append("\n");
            }

            // 同步二级权限（操作权限）
            if (module.getOperations() != null && !module.getOperations().isEmpty()) {
                for (PermissionSyncConfig.OperationConfig operation : module.getOperations()) {
                    SysPermission operationPermission = syncOperationPermission(operation, modulePermission);
                    if (operationPermission.getId() == null) {
                        addedCount++;
                        result.append("  新增操作权限: ").append(operation.getName()).append("\n");
                    } else {
                        updatedCount++;
                        result.append("  更新操作权限: ").append(operation.getName()).append("\n");
                    }
                }
            }
        }

        String summary = String.format("权限同步完成！新增: %d, 更新: %d\n", addedCount, updatedCount);
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
        SysPermission modulePermission = syncModulePermission(module);
        result.append("同步模块权限: ").append(module.getName()).append("\n");

        // 同步操作权限
        if (module.getOperations() != null) {
            for (PermissionSyncConfig.OperationConfig operation : module.getOperations()) {
                syncOperationPermission(operation, modulePermission);
                result.append("  同步操作权限: ").append(operation.getName()).append("\n");
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
            if (module.getOperations() != null && !module.getOperations().isEmpty()) {
                result.append("  操作数量: ").append(module.getOperations().size()).append("\n");
                for (PermissionSyncConfig.OperationConfig operation : module.getOperations()) {
                    result.append("    - ").append(operation.getName()).append(" (").append(operation.getCode()).append(")\n");
                }
            }
            result.append("\n");
        }
        
        return result.toString();
    }

    /**
     * 同步模块权限（一级权限）
     */
    private SysPermission syncModulePermission(PermissionSyncConfig.ModuleConfig module) {
        Optional<SysPermission> existingPermission = permissionRepository.findByPermissionCodeAndIsDeleted(module.getCode(), false);
        
        SysPermission permission;
        if (existingPermission.isPresent()) {
            permission = existingPermission.get();
            // 更新现有权限
            permission.setPermissionName(module.getName());
            permission.setDescription(module.getDescription());
            permission.setStatus(1);
            permission.setIsDeleted(false);
            permission.setUpdateTime(LocalDateTime.now());
        } else {
            // 创建新权限
            permission = new SysPermission();
            permission.setPermissionName(module.getName());
            permission.setPermissionCode(module.getCode());
            permission.setPermissionType(1); // 一级权限（模块）
            permission.setParentId(0L);
            permission.setDescription(module.getDescription());
            permission.setStatus(1);
            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            permission.setIsDeleted(false);
        }

        return permissionRepository.save(permission);
    }

    /**
     * 同步操作权限（二级权限）
     */
    private SysPermission syncOperationPermission(PermissionSyncConfig.OperationConfig operation, SysPermission parentPermission) {
        String operationCode = parentPermission.getPermissionCode() + ":" + operation.getCode();
        Optional<SysPermission> existingPermission = permissionRepository.findByPermissionCodeAndIsDeleted(operationCode, false);
        
        SysPermission permission;
        if (existingPermission.isPresent()) {
            permission = existingPermission.get();
            // 更新现有权限
            permission.setPermissionName(operation.getName());
            permission.setDescription(operation.getDescription());
            permission.setParentId(parentPermission.getId());
            permission.setStatus(1);
            permission.setIsDeleted(false);
            permission.setUpdateTime(LocalDateTime.now());
        } else {
            // 创建新权限
            permission = new SysPermission();
            permission.setPermissionName(operation.getName());
            permission.setPermissionCode(operationCode);
            permission.setPermissionType(2); // 二级权限（操作）
            permission.setParentId(parentPermission.getId());
            permission.setDescription(operation.getDescription());
            permission.setStatus(1);
            permission.setCreateTime(LocalDateTime.now());
            permission.setUpdateTime(LocalDateTime.now());
            permission.setIsDeleted(false);
        }

        return permissionRepository.save(permission);
    }
} 