package com.example.vliascrm.service;

/**
 * 权限同步服务接口
 */
public interface PermissionSyncService {

    /**
     * 同步所有权限
     * @return 同步结果信息
     */
    String syncAllPermissions();

    /**
     * 同步指定模块权限
     * @param moduleCode 模块编码
     * @return 同步结果信息
     */
    String syncModulePermissions(String moduleCode);

    /**
     * 检查权限配置是否有效
     * @return 检查结果信息
     */
    String validatePermissionConfig();
} 