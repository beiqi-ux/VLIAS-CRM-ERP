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
 * 支持3级权限结构：模块权限(1级) -> 子模块权限(2级) -> 操作权限(3级)
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
        // 第二步：创建基础权限数据（3级权限结构）
        List<SysPermission> newPermissions = createAllPermissionsThreeLevel();
        List<SysPermission> savedPermissions = permissionRepository.saveAll(newPermissions);
        
        log.info("权限重置完成，逻辑删除 {} 个旧权限，新创建 {} 个基础权限", 
                allPermissions.size(), savedPermissions.size());
        
        return String.format("权限重置成功！逻辑删除 %d 个旧权限，新创建 %d 个基础权限", 
                allPermissions.size(), savedPermissions.size());
    }

    /**
     * 创建三级权限结构的所有权限数据
     */
    private List<SysPermission> createAllPermissionsThreeLevel() {
        LocalDateTime now = LocalDateTime.now();
        List<SysPermission> allPermissions = new ArrayList<>();
        Map<String, Long> moduleCodeToIdMap = new HashMap<>();
        Map<String, Long> subModuleCodeToIdMap = new HashMap<>();
        
        // 第一步：创建一级权限（模块权限）
        List<SysPermission> modulePermissions = createModulePermissionsThreeLevel(now);
        List<SysPermission> savedModules = permissionRepository.saveAll(modulePermissions);
        allPermissions.addAll(savedModules);
        
        // 建立模块编码到ID的映射
        for (SysPermission module : savedModules) {
            moduleCodeToIdMap.put(module.getPermissionCode(), module.getId());
        }
        
        // 第二步：创建二级权限（子模块权限）
        List<SysPermission> subModulePermissions = createSubModulePermissions(moduleCodeToIdMap, now);
        List<SysPermission> savedSubModules = permissionRepository.saveAll(subModulePermissions);
        allPermissions.addAll(savedSubModules);
        
        // 建立子模块编码到ID的映射
        for (SysPermission subModule : savedSubModules) {
            subModuleCodeToIdMap.put(subModule.getPermissionCode(), subModule.getId());
        }
        
        // 第三步：创建三级权限（操作权限）
        List<SysPermission> operationPermissions = createOperationPermissionsThreeLevel(subModuleCodeToIdMap, now);
        List<SysPermission> savedOperations = permissionRepository.saveAll(operationPermissions);
        allPermissions.addAll(savedOperations);
        
        return allPermissions;
    }
    
    /**
     * 创建一级权限（模块权限）
     */
    private List<SysPermission> createModulePermissionsThreeLevel(LocalDateTime now) {
        List<SysPermission> modules = new ArrayList<>();
        
        // 定义一级权限模块（与系统设计文档保持一致）
        Object[][] moduleData = {
            {"system", "系统管理", "系统基础功能管理", 1},
            {"org", "组织架构", "组织架构管理", 2},
            {"product", "商品管理", "商品信息管理", 3},
            {"customer", "客户管理", "客户信息管理", 4},
            {"sales", "销售管理", "销售业务管理", 5},
            {"purchase", "采购管理", "采购业务管理", 6},
            {"inventory", "库存管理", "库存业务管理", 7},
            {"finance", "财务管理", "财务业务管理", 8},
            {"report", "报表管理", "报表统计管理", 9},
            {"marketing", "营销管理", "营销活动管理", 10},
            {"workflow", "工作流管理", "工作流程管理", 11},
            {"notification", "消息通知", "通知消息管理", 12},
            {"log", "日志管理", "系统日志管理", 13},
            {"config", "配置管理", "系统配置管理", 14},
            {"profile", "个人中心", "个人信息管理", 15}
        };
        
        for (Object[] data : moduleData) {
            SysPermission module = new SysPermission();
            module.setPermissionName((String) data[1]);
            module.setPermissionCode((String) data[0]);
            module.setPermissionType(1); // 一级权限
            module.setLevelDepth(1);
            module.setParentId(0L);
            module.setDescription((String) data[2]);
            module.setSortOrder((Integer) data[3]);
            module.setStatus(1);
            module.setIsCore(1);
            module.setCreateTime(now);
            module.setUpdateTime(now);
            module.setIsDeleted(false);
            
            modules.add(module);
        }
        
        return modules;
    }
    
    /**
     * 创建二级权限（子模块权限）
     */
    private List<SysPermission> createSubModulePermissions(Map<String, Long> moduleCodeToIdMap, LocalDateTime now) {
        List<SysPermission> subModules = new ArrayList<>();
        
        // 定义二级权限（子模块权限）
        Object[][] subModuleData = {
            // 系统管理子模块
            {"user-management", "用户管理", "system", "用户账户管理", 1},
            {"role-management", "角色管理", "system", "角色权限管理", 2},
            {"permission-management", "权限管理", "system", "权限配置管理", 3},
            {"menu-management", "菜单管理", "system", "系统菜单管理", 4},
            {"dict-management", "数据字典管理", "system", "系统字典管理", 5},
            
            // 组织架构子模块
            {"org-management", "组织机构管理", "org", "组织机构管理", 1},
            {"dept-management", "部门管理", "org", "部门信息管理", 2},
            {"position-management", "岗位管理", "org", "岗位信息管理", 3},
            
            // 商品管理子模块
            {"product-info-management", "商品信息管理", "product", "商品基础信息管理", 1},
            {"product-category-management", "商品分类管理", "product", "商品分类管理", 2},
            {"product-brand-management", "商品品牌管理", "product", "商品品牌管理", 3},
            {"product-specification-management", "商品规格管理", "product", "商品规格管理", 4},
            {"product-attribute-management", "商品属性管理", "product", "商品属性管理", 5},
            {"product-sku-management", "商品SKU管理", "product", "商品SKU管理", 6},
            
            // 客户管理子模块
            {"customer-info-management", "客户信息管理", "customer", "客户基础信息管理", 1},
            {"customer-category-management", "客户分类管理", "customer", "客户分类管理", 2},
            {"customer-contact-management", "客户联系人管理", "customer", "客户联系人管理", 3},
            {"customer-follow-management", "客户跟进管理", "customer", "客户跟进管理", 4},
            
            // 销售管理子模块
            {"sales-order-management", "销售订单管理", "sales", "销售订单管理", 1},
            {"sales-quote-management", "销售报价管理", "sales", "销售报价管理", 2},
            {"sales-statistics-management", "销售统计管理", "sales", "销售统计管理", 3},
            {"sales-performance-management", "销售业绩管理", "sales", "销售业绩管理", 4},
            
            // 采购管理子模块
            {"purchase-order-management", "采购订单管理", "purchase", "采购订单管理", 1},
            {"supplier-management", "供应商管理", "purchase", "供应商管理", 2},
            {"purchase-request-management", "采购申请管理", "purchase", "采购申请管理", 3},
            {"purchase-statistics-management", "采购统计管理", "purchase", "采购统计管理", 4},
            
            // 库存管理子模块
            {"inventory-query-management", "库存查询管理", "inventory", "库存查询管理", 1},
            {"inbound-management", "入库管理", "inventory", "入库管理", 2},
            {"outbound-management", "出库管理", "inventory", "出库管理", 3},
            {"inventory-check-management", "库存盘点管理", "inventory", "库存盘点管理", 4},
            
            // 财务管理子模块
            {"finance-report-management", "财务报表管理", "finance", "财务报表管理", 1},
            {"receivable-management", "应收账款管理", "finance", "应收账款管理", 2},
            {"payable-management", "应付账款管理", "finance", "应付账款管理", 3},
            {"expense-management", "费用管理", "finance", "费用管理", 4},
            
            // 报表管理子模块
            {"sales-report-management", "销售报表管理", "report", "销售报表管理", 1},
            {"inventory-report-management", "库存报表管理", "report", "库存报表管理", 2},
            {"financial-report-management", "财务报表管理", "report", "财务报表管理", 3},
            {"custom-report-management", "自定义报表管理", "report", "自定义报表管理", 4},
            
            // 营销管理子模块
            {"promotion-management", "促销活动管理", "marketing", "促销活动管理", 1},
            {"marketing-strategy-management", "营销策略管理", "marketing", "营销策略管理", 2},
            {"customer-care-management", "客户关怀管理", "marketing", "客户关怀管理", 3},
            
            // 工作流管理子模块
            {"workflow-definition-management", "流程定义管理", "workflow", "流程定义管理", 1},
            {"workflow-instance-management", "流程实例管理", "workflow", "流程实例管理", 2},
            {"approval-task-management", "审批任务管理", "workflow", "审批任务管理", 3},
            
            // 消息通知子模块
            {"system-message-management", "系统消息管理", "notification", "系统消息管理", 1},
            {"email-notification-management", "邮件通知管理", "notification", "邮件通知管理", 2},
            {"sms-notification-management", "短信通知管理", "notification", "短信通知管理", 3},
            
            // 日志管理子模块
            {"operation-log-management", "操作日志管理", "log", "操作日志管理", 1},
            {"system-log-management", "系统日志管理", "log", "系统日志管理", 2},
            {"login-log-management", "登录日志管理", "log", "登录日志管理", 3},
            
            // 配置管理子模块
            {"system-config-management", "系统配置管理", "config", "系统配置管理", 1},
            {"parameter-config-management", "参数配置管理", "config", "参数配置管理", 2},
            {"api-config-management", "接口配置管理", "config", "接口配置管理", 3},
            
            // 个人中心子模块
            {"profile-info-management", "个人信息管理", "profile", "个人信息管理", 1},
            {"password-management", "密码管理", "profile", "密码管理", 2}
        };
        
        for (Object[] data : subModuleData) {
            String moduleCode = (String) data[2];
            Long parentId = moduleCodeToIdMap.get(moduleCode);
            
            if (parentId != null) {
                SysPermission subModule = new SysPermission();
                subModule.setPermissionName((String) data[1]);
                subModule.setPermissionCode((String) data[0]);
                subModule.setPermissionType(2); // 二级权限
                subModule.setLevelDepth(2);
                subModule.setParentId(parentId);
                subModule.setDescription((String) data[3]);
                subModule.setSortOrder((Integer) data[4]);
                subModule.setStatus(1);
                                 subModule.setIsCore(1);
                subModule.setCreateTime(now);
                subModule.setUpdateTime(now);
                subModule.setIsDeleted(false);
                
                subModules.add(subModule);
            }
        }
        
        return subModules;
    }
    
    /**
     * 创建三级权限（操作权限）
     */
    private List<SysPermission> createOperationPermissionsThreeLevel(Map<String, Long> subModuleCodeToIdMap, LocalDateTime now) {
        List<SysPermission> operations = new ArrayList<>();
        
        // 定义基础操作权限模板
        String[][] baseOperations = {
            {"view", "查看", "查看数据权限"},
            {"create", "新增", "新增数据权限"},
            {"edit", "编辑", "编辑数据权限"},
            {"delete", "删除", "删除数据权限"},
            {"export", "导出", "导出数据权限"}
        };
        
        // 定义特殊操作权限（针对特定模块）
        Map<String, String[][]> specialOperations = new HashMap<>();
        
        // 用户管理特殊权限
        specialOperations.put("user-management", new String[][]{
            {"reset-password", "重置密码", "重置用户密码权限"},
            {"assign-role", "分配角色", "为用户分配角色权限"}
        });
        
        // 角色管理特殊权限
        specialOperations.put("role-management", new String[][]{
            {"assign-permission", "分配权限", "为角色分配权限"}
        });
        
        // 权限管理特殊权限
        specialOperations.put("permission-management", new String[][]{
            {"validate", "权限验证", "权限验证操作"}
        });
        
        // 为每个二级权限创建对应的三级操作权限
        for (String subModuleCode : subModuleCodeToIdMap.keySet()) {
            Long parentId = subModuleCodeToIdMap.get(subModuleCode);
            int sortIndex = 1;
            
            // 创建基础操作权限
            for (String[] operation : baseOperations) {
                SysPermission operationPermission = new SysPermission();
                operationPermission.setPermissionName(operation[1]);
                operationPermission.setPermissionCode(subModuleCode + ":" + operation[0]);
                operationPermission.setPermissionType(3); // 三级权限
                operationPermission.setLevelDepth(3);
                operationPermission.setParentId(parentId);
                operationPermission.setDescription(operation[2]);
                operationPermission.setSortOrder(sortIndex * 10);
                operationPermission.setStatus(1);
                operationPermission.setIsCore(1);
                operationPermission.setCreateTime(now);
                operationPermission.setUpdateTime(now);
                operationPermission.setIsDeleted(false);
                    
                operations.add(operationPermission);
                sortIndex++;
            }
            
            // 创建特殊操作权限（如果存在）
            if (specialOperations.containsKey(subModuleCode)) {
                String[][] specialOps = specialOperations.get(subModuleCode);
                for (String[] operation : specialOps) {
                    SysPermission operationPermission = new SysPermission();
                    operationPermission.setPermissionName(operation[1]);
                    operationPermission.setPermissionCode(subModuleCode + ":" + operation[0]);
                    operationPermission.setPermissionType(3); // 三级权限
                    operationPermission.setLevelDepth(3);
                    operationPermission.setParentId(parentId);
                    operationPermission.setDescription(operation[2]);
                    operationPermission.setSortOrder(sortIndex * 10);
                    operationPermission.setStatus(1);
                    operationPermission.setIsCore(1);
                    operationPermission.setCreateTime(now);
                    operationPermission.setUpdateTime(now);
                    operationPermission.setIsDeleted(false);
                        
                    operations.add(operationPermission);
                    sortIndex++;
                }
            }
        }
        
        return operations;
    }
} 