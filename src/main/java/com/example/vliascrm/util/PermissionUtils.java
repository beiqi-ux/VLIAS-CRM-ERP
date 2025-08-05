package com.example.vliascrm.util;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 权限工具类
 * 提供权限相关的工具方法
 */
public class PermissionUtils {

    /**
     * 权限编码的正则表达式
     */
    private static final Pattern MODULE_PATTERN = Pattern.compile("^[a-z]+$");
    private static final Pattern SUBMODULE_PATTERN = Pattern.compile("^[a-z]+-[a-z]+(-[a-z]+)*$");
    private static final Pattern ACTION_PATTERN = Pattern.compile("^[a-z]+-[a-z]+(-[a-z]+)*:[a-z]+(-[a-z]+)*$");

    /**
     * 基础操作权限列表
     */
    public static final List<String> BASIC_ACTIONS = Arrays.asList(
        "view", "create", "edit", "delete"
    );

    /**
     * 扩展操作权限列表
     */
    public static final List<String> EXTENDED_ACTIONS = Arrays.asList(
        "view", "create", "edit", "delete", 
        "export", "import", "audit", "approve", "reject",
        "reset-password", "assign-permission", "change-status"
    );

    /**
     * 判断权限编码的类型
     * @param permissionCode 权限编码
     * @return 权限类型 1-一级权限(模块) 2-二级权限(子模块) 3-三级权限(操作)
     */
    public static Integer getPermissionType(String permissionCode) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return null;
        }

        String code = permissionCode.trim();
        
        if (ACTION_PATTERN.matcher(code).matches()) {
            return 3; // 三级权限(操作)
        } else if (SUBMODULE_PATTERN.matcher(code).matches()) {
            return 2; // 二级权限(子模块)
        } else if (MODULE_PATTERN.matcher(code).matches()) {
            return 1; // 一级权限(模块)
        }
        
        return null;
    }

    /**
     * 生成权限路径
     * @param permissionCode 权限编码
     * @param parentPath 父权限路径
     * @return 权限路径
     */
    public static String generatePermissionPath(String permissionCode, String parentPath) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return null;
        }

        String code = permissionCode.trim();
        
        if (parentPath == null || parentPath.trim().isEmpty()) {
            return "/" + code;
        }
        
        String cleanParentPath = parentPath.trim();
        if (!cleanParentPath.startsWith("/")) {
            cleanParentPath = "/" + cleanParentPath;
        }
        
        return cleanParentPath + "/" + code;
    }

    /**
     * 解析权限编码，获取模块编码
     * @param permissionCode 权限编码
     * @return 模块编码
     */
    public static String extractModuleCode(String permissionCode) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return null;
        }

        String code = permissionCode.trim();
        
        // 如果是三级权限（包含:），先去掉操作部分
        if (code.contains(":")) {
            code = code.split(":")[0];
        }
        
        // 如果是二级权限（包含-），取第一个部分作为模块编码
        if (code.contains("-")) {
            return code.split("-")[0];
        }
        
        // 如果是一级权限，直接返回
        return code;
    }

    /**
     * 解析权限编码，获取子模块编码
     * @param permissionCode 权限编码
     * @return 子模块编码，如果不是三级权限则返回null
     */
    public static String extractSubmoduleCode(String permissionCode) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return null;
        }

        String code = permissionCode.trim();
        
        // 只有三级权限才有子模块编码
        if (code.contains(":")) {
            return code.split(":")[0];
        }
        
        return null;
    }

    /**
     * 解析权限编码，获取操作编码
     * @param permissionCode 权限编码
     * @return 操作编码，如果不是三级权限则返回null
     */
    public static String extractActionCode(String permissionCode) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return null;
        }

        String code = permissionCode.trim();
        
        // 只有三级权限才有操作编码
        if (code.contains(":")) {
            String[] parts = code.split(":");
            if (parts.length == 2) {
                return parts[1];
            }
        }
        
        return null;
    }

    /**
     * 检查权限继承关系
     * @param userPermissions 用户权限编码列表
     * @param requiredPermission 需要检查的权限编码
     * @return 是否有权限
     */
    public static boolean hasPermissionWithInheritance(List<String> userPermissions, String requiredPermission) {
        if (userPermissions == null || userPermissions.isEmpty() || 
            requiredPermission == null || requiredPermission.trim().isEmpty()) {
            return false;
        }

        String required = requiredPermission.trim();
        
        // 1. 直接权限检查
        if (userPermissions.contains(required)) {
            return true;
        }
        
        // 2. 继承权限检查
        Integer permissionType = getPermissionType(required);
        
        if (permissionType == null) {
            return false;
        }
        
        switch (permissionType) {
            case 3: // 三级权限，检查是否有二级或一级权限
                String submoduleCode = extractSubmoduleCode(required);
                if (submoduleCode != null && userPermissions.contains(submoduleCode)) {
                    return true;
                }
                // 继续检查一级权限
                String moduleCode = extractModuleCode(required);
                if (moduleCode != null && userPermissions.contains(moduleCode)) {
                    return true;
                }
                break;
                
            case 2: // 二级权限，检查是否有一级权限
                String parentModuleCode = extractModuleCode(required);
                if (parentModuleCode != null && userPermissions.contains(parentModuleCode)) {
                    return true;
                }
                break;
                
            case 1: // 一级权限，无继承关系
            default:
                break;
        }
        
        return false;
    }

    /**
     * 生成子模块权限编码
     * @param moduleCode 模块编码
     * @param submoduleName 子模块名称
     * @return 子模块权限编码
     */
    public static String generateSubmoduleCode(String moduleCode, String submoduleName) {
        if (moduleCode == null || moduleCode.trim().isEmpty() ||
            submoduleName == null || submoduleName.trim().isEmpty()) {
            return null;
        }
        
        String cleanModuleCode = moduleCode.trim().toLowerCase();
        String cleanSubmoduleName = submoduleName.trim().toLowerCase().replace(" ", "-");
        
        return cleanModuleCode + "-" + cleanSubmoduleName;
    }

    /**
     * 生成操作权限编码
     * @param submoduleCode 子模块编码
     * @param actionName 操作名称
     * @return 操作权限编码
     */
    public static String generateActionCode(String submoduleCode, String actionName) {
        if (submoduleCode == null || submoduleCode.trim().isEmpty() ||
            actionName == null || actionName.trim().isEmpty()) {
            return null;
        }
        
        String cleanSubmoduleCode = submoduleCode.trim().toLowerCase();
        String cleanActionName = actionName.trim().toLowerCase().replace(" ", "-");
        
        return cleanSubmoduleCode + ":" + cleanActionName;
    }

    /**
     * 批量生成基础操作权限编码
     * @param submoduleCode 子模块编码
     * @return 基础操作权限编码列表
     */
    public static List<String> generateBasicActionCodes(String submoduleCode) {
        return BASIC_ACTIONS.stream()
                .map(action -> generateActionCode(submoduleCode, action))
                .filter(code -> code != null)
                .toList();
    }

    /**
     * 验证权限编码格式
     * @param permissionCode 权限编码
     * @return 是否为有效格式
     */
    public static boolean isValidPermissionCode(String permissionCode) {
        if (permissionCode == null || permissionCode.trim().isEmpty()) {
            return false;
        }

        String code = permissionCode.trim();
        
        return MODULE_PATTERN.matcher(code).matches() ||
               SUBMODULE_PATTERN.matcher(code).matches() ||
               ACTION_PATTERN.matcher(code).matches();
    }

    /**
     * 获取权限层级深度
     * @param permissionCode 权限编码
     * @return 层级深度
     */
    public static Integer getPermissionDepth(String permissionCode) {
        return getPermissionType(permissionCode);
    }

    /**
     * 判断是否为基础操作权限
     * @param actionCode 操作编码
     * @return 是否为基础操作
     */
    public static boolean isBasicAction(String actionCode) {
        return BASIC_ACTIONS.contains(actionCode);
    }

    /**
     * 判断是否为扩展操作权限
     * @param actionCode 操作编码
     * @return 是否为扩展操作
     */
    public static boolean isExtendedAction(String actionCode) {
        return EXTENDED_ACTIONS.contains(actionCode);
    }
} 