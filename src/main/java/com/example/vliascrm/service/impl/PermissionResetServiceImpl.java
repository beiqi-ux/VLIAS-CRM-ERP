package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.SysPermission;
import com.example.vliascrm.repository.SysPermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限重置服务实现类
 * 提供基础权限数据的重置功能
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionResetServiceImpl {

    private final SysPermissionRepository permissionRepository;

    /**
     * 重置所有基础权限
     * 会删除现有权限并重新创建基础权限数据
     */
    public String resetAllPermissions() {
        log.info("开始重置权限数据...");
        
        try {
            // 分步骤执行，确保每步都能完成
            return resetPermissionsStepByStep();
            
        } catch (Exception e) {
            log.error("权限重置失败", e);
            throw new RuntimeException("权限重置失败: " + e.getMessage());
        }
    }
    
    @Transactional
    private String resetPermissionsStepByStep() {
        // 第一步：修改所有现有权限的编码为临时编码
        List<SysPermission> allPermissions = permissionRepository.findAll();
        String timestamp = String.valueOf(System.currentTimeMillis());
        
        log.info("第一步：修改现有权限编码，共 {} 个权限", allPermissions.size());
        for (SysPermission permission : allPermissions) {
            // 先修改编码避免冲突，再设置删除标记
            permission.setPermissionCode("temp_" + timestamp + "_" + permission.getId());
            permission.setIsDeleted(true);
            permission.setUpdateTime(LocalDateTime.now());
        }
        permissionRepository.saveAll(allPermissions);
        permissionRepository.flush(); // 强制同步到数据库
        
        log.info("第二步：创建基础权限数据");
        // 第二步：创建基础权限数据（简化版，直接创建不分步）
        List<SysPermission> newPermissions = createAllPermissionsAtOnce();
        List<SysPermission> savedPermissions = permissionRepository.saveAll(newPermissions);
        
        log.info("权限重置完成，逻辑删除 {} 个旧权限，新创建 {} 个基础权限", 
                allPermissions.size(), savedPermissions.size());
        
        return String.format("权限重置成功！逻辑删除 %d 个旧权限，新创建 %d 个基础权限", 
                allPermissions.size(), savedPermissions.size());
    }

    /**
     * 创建基础权限数据
     */
    private List<SysPermission> createBasePermissions() {
        List<SysPermission> allPermissions = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        
        // 第一步：创建并保存一级权限（模块权限）
        List<SysPermission> modulePermissions = createModulePermissions(now);
        List<SysPermission> savedModules = permissionRepository.saveAll(modulePermissions);
        allPermissions.addAll(savedModules);
        
        // 建立模块编码到ID的映射
        Map<String, Long> moduleCodeToIdMap = new HashMap<>();
        for (SysPermission module : savedModules) {
            moduleCodeToIdMap.put(module.getPermissionCode(), module.getId());
        }
        
        // 第二步：创建二级权限（操作权限）
        List<SysPermission> operationPermissions = createOperationPermissions(moduleCodeToIdMap, now);
        List<SysPermission> savedOperations = permissionRepository.saveAll(operationPermissions);
        allPermissions.addAll(savedOperations);
        
        return allPermissions;
    }
    
    /**
     * 创建一级权限（模块权限）
     */
    private List<SysPermission> createModulePermissions(LocalDateTime now) {
        List<SysPermission> modules = new ArrayList<>();
        
        // 定义基础权限模块
        Object[][] moduleData = {
            {"system", "系统管理", "系统基础功能管理"},
            {"user", "用户管理", "用户账户管理"},
            {"role", "角色管理", "角色权限管理"},
            {"permission", "权限管理", "权限配置管理"},
            {"menu", "菜单管理", "系统菜单管理"},
            {"org", "组织管理", "组织架构管理"},
            {"dept", "部门管理", "部门信息管理"},
            {"position", "岗位管理", "岗位信息管理"},
            {"dict", "字典管理", "系统字典管理"},
            {"product", "商品管理", "商品信息管理"},
            {"brand", "品牌管理", "商品品牌管理"},
            {"category", "分类管理", "商品分类管理"}
        };
        
        for (Object[] data : moduleData) {
            SysPermission module = new SysPermission();
            // 不设置ID，让JPA自动生成
            module.setPermissionName((String) data[1]);
            module.setPermissionCode((String) data[0]);
            module.setPermissionType(1); // 一级权限
            module.setParentId(0L);
            module.setDescription((String) data[2]);
            module.setStatus(1);
            module.setCreateTime(now);
            module.setUpdateTime(now);
            module.setIsDeleted(false);
            
            modules.add(module);
        }
        
        return modules;
    }
    
    /**
     * 创建二级权限（操作权限）
     */
    private List<SysPermission> createOperationPermissions(Map<String, Long> moduleCodeToIdMap, LocalDateTime now) {
        List<SysPermission> operations = new ArrayList<>();
        
        // 定义二级权限（操作权限）
        Object[][] operationData = {
            // 系统管理操作
            {"system", "view", "查看", "查看系统信息"},
            {"system", "config", "配置", "系统配置管理"},
            
            // 用户管理操作
            {"user", "view", "查看", "查看用户列表"},
            {"user", "add", "新增", "新增用户"},
            {"user", "edit", "编辑", "编辑用户信息"},
            {"user", "delete", "删除", "删除用户"},
            {"user", "reset-password", "重置密码", "重置用户密码"},
            {"user", "assign-role", "分配角色", "为用户分配角色"},
            
            // 角色管理操作
            {"role", "view", "查看", "查看角色列表"},
            {"role", "add", "新增", "新增角色"},
            {"role", "edit", "编辑", "编辑角色信息"},
            {"role", "delete", "删除", "删除角色"},
            {"role", "assign", "分配权限", "为角色分配权限"},
            
            // 权限管理操作
            {"permission", "view", "查看", "查看权限列表"},
            {"permission", "add", "新增", "新增权限"},
            {"permission", "edit", "编辑", "编辑权限信息"},
            {"permission", "delete", "删除", "删除权限"},
            {"permission", "sync", "同步", "同步权限数据"},
            
            // 菜单管理操作
            {"menu", "view", "查看", "查看菜单列表"},
            {"menu", "add", "新增", "新增菜单"},
            {"menu", "edit", "编辑", "编辑菜单信息"},
            {"menu", "delete", "删除", "删除菜单"},
            
            // 组织管理操作
            {"org", "view", "查看", "查看组织列表"},
            {"org", "add", "新增", "新增组织"},
            {"org", "edit", "编辑", "编辑组织信息"},
            {"org", "delete", "删除", "删除组织"},
            
            // 部门管理操作
            {"dept", "view", "查看", "查看部门列表"},
            {"dept", "add", "新增", "新增部门"},
            {"dept", "edit", "编辑", "编辑部门信息"},
            {"dept", "delete", "删除", "删除部门"},
            
            // 岗位管理操作
            {"position", "view", "查看", "查看岗位列表"},
            {"position", "add", "新增", "新增岗位"},
            {"position", "edit", "编辑", "编辑岗位信息"},
            {"position", "delete", "删除", "删除岗位"},
            
            // 字典管理操作
            {"dict", "view", "查看", "查看字典列表"},
            {"dict", "add", "新增", "新增字典"},
            {"dict", "edit", "编辑", "编辑字典信息"},
            {"dict", "delete", "删除", "删除字典"},
            
            // 商品管理操作
            {"product", "view", "查看", "查看商品列表"},
            {"product", "add", "新增", "新增商品"},
            {"product", "edit", "编辑", "编辑商品信息"},
            {"product", "delete", "删除", "删除商品"},
            
            // 品牌管理操作
            {"brand", "view", "查看", "查看品牌列表"},
            {"brand", "add", "新增", "新增品牌"},
            {"brand", "edit", "编辑", "编辑品牌信息"},
            {"brand", "delete", "删除", "删除品牌"},
            
            // 分类管理操作
            {"category", "view", "查看", "查看分类列表"},
            {"category", "add", "新增", "新增分类"},
            {"category", "edit", "编辑", "编辑分类信息"},
            {"category", "delete", "删除", "删除分类"}
        };
        
        for (Object[] data : operationData) {
            String moduleCode = (String) data[0];
            Long parentId = moduleCodeToIdMap.get(moduleCode);
            
            if (parentId != null) {
                SysPermission operation = new SysPermission();
                // 不设置ID，让JPA自动生成
                operation.setPermissionName((String) data[2]);
                operation.setPermissionCode(moduleCode + ":" + data[1]);
                operation.setPermissionType(2); // 二级权限
                operation.setParentId(parentId);
                operation.setDescription((String) data[3]);
                operation.setStatus(1);
                operation.setCreateTime(now);
                operation.setUpdateTime(now);
                operation.setIsDeleted(false);
                
                operations.add(operation);
            }
        }
        
        return operations;
    }
    
    /**
     * 创建所有权限数据（一次性创建）
     */
    private List<SysPermission> createAllPermissionsAtOnce() {
        LocalDateTime now = LocalDateTime.now();
        List<SysPermission> allPermissions = new ArrayList<>();
        Map<String, Long> moduleCodeToIdMap = new HashMap<>();
        
        // 定义所有权限数据：{权限编码, 权限名称, 权限类型, 父权限编码, 描述, 排序}
        Object[][] allPermissionData = {
            // 一级权限（模块权限）
            {"system", "系统管理", 1, null, "系统基础功能管理", 1},
            {"user", "用户管理", 1, null, "用户账户管理", 2},
            {"role", "角色管理", 1, null, "角色权限管理", 3},
            {"permission", "权限管理", 1, null, "权限配置管理", 4},
            {"menu", "菜单管理", 1, null, "系统菜单管理", 5},
            {"org", "组织管理", 1, null, "组织架构管理", 6},
            {"dept", "部门管理", 1, null, "部门信息管理", 7},
            {"position", "岗位管理", 1, null, "岗位信息管理", 8},
            {"dict", "字典管理", 1, null, "系统字典管理", 9},
            {"product", "商品管理", 1, null, "商品信息管理", 10},
            {"brand", "品牌管理", 1, null, "商品品牌管理", 11},
            {"category", "分类管理", 1, null, "商品分类管理", 12},
            {"profile", "个人中心", 1, null, "个人信息管理", 999}, // 个人中心设置最大sort值
            
            // 二级权限（操作权限）
            // 系统管理操作
            {"system:view", "查看", 2, "system", "查看系统信息", 1},
            {"system:config", "配置", 2, "system", "系统配置管理", 2},
            
            // 用户管理操作
            {"user:view", "查看", 2, "user", "查看用户列表", 1},
            {"user:add", "新增", 2, "user", "新增用户", 2},
            {"user:edit", "编辑", 2, "user", "编辑用户信息", 3},
            {"user:delete", "删除", 2, "user", "删除用户", 4},
            {"user:reset-password", "重置密码", 2, "user", "重置用户密码", 5},
            {"user:assign-role", "分配角色", 2, "user", "为用户分配角色", 6},
            
            // 角色管理操作
            {"role:view", "查看", 2, "role", "查看角色列表", 1},
            {"role:add", "新增", 2, "role", "新增角色", 2},
            {"role:edit", "编辑", 2, "role", "编辑角色信息", 3},
            {"role:delete", "删除", 2, "role", "删除角色", 4},
            {"role:assign", "分配权限", 2, "role", "为角色分配权限", 5},
            
            // 权限管理操作
            {"permission:view", "查看", 2, "permission", "查看权限列表", 1},
            {"permission:add", "新增", 2, "permission", "新增权限", 2},
            {"permission:edit", "编辑", 2, "permission", "编辑权限信息", 3},
            {"permission:delete", "删除", 2, "permission", "删除权限", 4},
            {"permission:sync", "同步", 2, "permission", "同步权限数据", 5},
            {"permission:reset", "重置", 2, "permission", "重置权限数据", 6},
            {"permission:validate", "验证", 2, "permission", "验证权限配置", 7},
            
            // 菜单管理操作
            {"menu:view", "查看", 2, "menu", "查看菜单列表", 1},
            {"menu:add", "新增", 2, "menu", "新增菜单", 2},
            {"menu:edit", "编辑", 2, "menu", "编辑菜单信息", 3},
            {"menu:delete", "删除", 2, "menu", "删除菜单", 4},
            
            // 组织管理操作
            {"org:view", "查看", 2, "org", "查看组织列表", 1},
            {"org:add", "新增", 2, "org", "新增组织", 2},
            {"org:edit", "编辑", 2, "org", "编辑组织信息", 3},
            {"org:delete", "删除", 2, "org", "删除组织", 4},
            
            // 部门管理操作
            {"dept:view", "查看", 2, "dept", "查看部门列表", 1},
            {"dept:add", "新增", 2, "dept", "新增部门", 2},
            {"dept:edit", "编辑", 2, "dept", "编辑部门信息", 3},
            {"dept:delete", "删除", 2, "dept", "删除部门", 4},
            
            // 岗位管理操作
            {"position:view", "查看", 2, "position", "查看岗位列表", 1},
            {"position:add", "新增", 2, "position", "新增岗位", 2},
            {"position:edit", "编辑", 2, "position", "编辑岗位信息", 3},
            {"position:delete", "删除", 2, "position", "删除岗位", 4},
            
            // 字典管理操作
            {"dict:view", "查看", 2, "dict", "查看字典列表", 1},
            {"dict:add", "新增", 2, "dict", "新增字典", 2},
            {"dict:edit", "编辑", 2, "dict", "编辑字典信息", 3},
            {"dict:delete", "删除", 2, "dict", "删除字典", 4},
            
            // 商品管理操作
            {"product:view", "查看", 2, "product", "查看商品列表", 1},
            {"product:add", "新增", 2, "product", "新增商品", 2},
            {"product:edit", "编辑", 2, "product", "编辑商品信息", 3},
            {"product:delete", "删除", 2, "product", "删除商品", 4},
            
            // 品牌管理操作
            {"brand:view", "查看", 2, "brand", "查看品牌列表", 1},
            {"brand:add", "新增", 2, "brand", "新增品牌", 2},
            {"brand:edit", "编辑", 2, "brand", "编辑品牌信息", 3},
            {"brand:delete", "删除", 2, "brand", "删除品牌", 4},
            
            // 分类管理操作
            {"category:view", "查看", 2, "category", "查看分类列表", 1},
            {"category:add", "新增", 2, "category", "新增分类", 2},
            {"category:edit", "编辑", 2, "category", "编辑分类信息", 3},
            {"category:delete", "删除", 2, "category", "删除分类", 4},
            
            // 个人中心操作
            {"profile:view", "查看", 2, "profile", "查看个人信息", 1},
            {"profile:edit", "编辑", 2, "profile", "编辑个人信息", 2},
            {"profile:password", "修改密码", 2, "profile", "修改个人密码", 3}
        };
        
        // 第一次遍历：创建模块权限
        for (Object[] data : allPermissionData) {
            Integer permissionType = (Integer) data[2];
            if (permissionType == 1) { // 只处理模块权限
                SysPermission permission = new SysPermission();
                permission.setPermissionName((String) data[1]);
                permission.setPermissionCode((String) data[0]);
                permission.setPermissionType(permissionType);
                permission.setParentId(0L);
                permission.setDescription((String) data[4]);
                permission.setSort((Integer) data[5]); // 设置排序值
                permission.setStatus(1);
                permission.setCreateTime(now);
                permission.setUpdateTime(now);
                permission.setIsDeleted(false);
                
                allPermissions.add(permission);
            }
        }
        
        // 保存模块权限并获取ID
        List<SysPermission> savedModules = permissionRepository.saveAll(allPermissions);
        for (SysPermission module : savedModules) {
            moduleCodeToIdMap.put(module.getPermissionCode(), module.getId());
        }
        
        // 第二次遍历：创建操作权限
        List<SysPermission> operationPermissions = new ArrayList<>();
        for (Object[] data : allPermissionData) {
            Integer permissionType = (Integer) data[2];
            if (permissionType == 2) { // 只处理操作权限
                String parentCode = (String) data[3];
                Long parentId = moduleCodeToIdMap.get(parentCode);
                
                if (parentId != null) {
                    SysPermission permission = new SysPermission();
                    permission.setPermissionName((String) data[1]);
                    permission.setPermissionCode((String) data[0]);
                    permission.setPermissionType(permissionType);
                    permission.setParentId(parentId);
                    permission.setDescription((String) data[4]);
                    permission.setSort((Integer) data[5]); // 设置排序值
                    permission.setStatus(1);
                    permission.setCreateTime(now);
                    permission.setUpdateTime(now);
                    permission.setIsDeleted(false);
                    
                    operationPermissions.add(permission);
                }
            }
        }
        
        // 保存操作权限
        List<SysPermission> savedOperations = permissionRepository.saveAll(operationPermissions);
        allPermissions.addAll(savedOperations);
        
        return allPermissions;
    }
} 