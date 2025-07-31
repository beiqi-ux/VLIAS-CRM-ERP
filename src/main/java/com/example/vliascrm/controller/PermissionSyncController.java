package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.service.PermissionSyncService;
import com.example.vliascrm.service.impl.PermissionResetServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 权限同步控制器
 * 提供权限自动同步功能的API接口
 */
@Slf4j
@RestController
@RequestMapping("/api/sys/permission-sync")
@RequiredArgsConstructor
public class PermissionSyncController {

    private final PermissionSyncService permissionSyncService;
    private final PermissionResetServiceImpl permissionResetService;

    /**
     * 同步所有权限
     * 根据配置文件自动创建或更新权限
     * @return 同步结果
     */
    @PostMapping("/sync-all")
    public ApiResponse<String> syncAllPermissions() {
        try {
            log.info("开始执行权限同步操作");
            String result = permissionSyncService.syncAllPermissions();
            log.info("权限同步操作完成");
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("权限同步失败", e);
            return ApiResponse.failure("权限同步失败: " + e.getMessage());
        }
    }

    /**
     * 同步指定模块权限
     * @param moduleCode 模块编码
     * @return 同步结果
     */
    @PostMapping("/sync-module/{moduleCode}")
    public ApiResponse<String> syncModulePermissions(@PathVariable String moduleCode) {
        try {
            log.info("开始同步模块权限: {}", moduleCode);
            String result = permissionSyncService.syncModulePermissions(moduleCode);
            log.info("模块权限同步完成: {}", moduleCode);
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("模块权限同步失败: {}", moduleCode, e);
            return ApiResponse.failure("模块权限同步失败: " + e.getMessage());
        }
    }

    /**
     * 验证权限配置
     * 检查配置文件中的权限配置是否正确
     * @return 验证结果
     */
    @GetMapping("/validate-config")
    public ApiResponse<String> validatePermissionConfig() {
        try {
            String result = permissionSyncService.validatePermissionConfig();
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("权限配置验证失败", e);
            return ApiResponse.failure("权限配置验证失败: " + e.getMessage());
        }
    }

    /**
     * 重置所有权限
     * 删除现有权限并重新创建基础权限数据
     * @return 重置结果
     */
    @PostMapping("/reset-all")
    public ApiResponse<String> resetAllPermissions() {
        try {
            log.info("开始执行权限重置操作");
            String result = permissionResetService.resetAllPermissions();
            log.info("权限重置操作完成");
            return ApiResponse.success(result);
        } catch (Exception e) {
            log.error("权限重置失败", e);
            return ApiResponse.failure("权限重置失败: " + e.getMessage());
        }
    }

    /**
     * 获取权限同步功能状态
     * @return 功能状态信息
     */
    @GetMapping("/status")
    public ApiResponse<String> getSyncStatus() {
        return ApiResponse.success("权限同步功能已就绪，可以使用以下接口：\n" +
                "1. POST /api/sys/permission-sync/sync-all - 同步所有权限\n" +
                "2. POST /api/sys/permission-sync/sync-module/{moduleCode} - 同步指定模块权限\n" +
                "3. POST /api/sys/permission-sync/reset-all - 重置所有权限\n" +
                "4. GET /api/sys/permission-sync/validate-config - 验证权限配置\n" +
                "5. GET /api/sys/permission-sync/status - 获取功能状态");
    }
} 