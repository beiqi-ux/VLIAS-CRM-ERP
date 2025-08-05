package com.example.vliascrm.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 权限同步配置类
 * 用于定义系统中需要自动同步的权限
 */
@Data
@Component
@ConfigurationProperties(prefix = "vlias.permission-sync")
public class PermissionSyncConfig {

    /**
     * 是否启用权限自动同步
     */
    private boolean enabled = false;

    /**
     * 系统模块权限配置
     */
    private List<ModuleConfig> modules;

    @Data
    public static class ModuleConfig {
        /**
         * 模块名称
         */
        private String name;
        
        /**
         * 模块编码
         */
        private String code;
        
        /**
         * 模块描述
         */
        private String description;
        
        /**
         * 排序
         */
        private Integer sort;
        
        /**
         * 子模块列表（二级权限）
         */
        private List<SubmoduleConfig> submodules;
    }

    @Data
    public static class SubmoduleConfig {
        /**
         * 子模块名称
         */
        private String name;
        
        /**
         * 子模块编码
         */
        private String code;
        
        /**
         * 子模块描述
         */
        private String description;
        
        /**
         * 排序
         */
        private Integer sort;
        
        /**
         * 操作权限列表（三级权限）
         */
        private List<OperationConfig> operations;
    }

    @Data
    public static class OperationConfig {
        /**
         * 操作名称
         */
        private String name;
        
        /**
         * 操作编码
         */
        private String code;
        
        /**
         * 操作描述
         */
        private String description;
        
        /**
         * 排序
         */
        private Integer sort;
    }
} 