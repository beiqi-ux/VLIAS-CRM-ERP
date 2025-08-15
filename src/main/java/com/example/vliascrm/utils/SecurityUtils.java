package com.example.vliascrm.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 安全工具类
 */
public class SecurityUtils {

    /**
     * 获取当前登录用户ID
     * @return 用户ID，如果未登录则返回null
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        // 如果是匿名用户
        if ("anonymousUser".equals(principal)) {
            return null;
        }
        
        // 如果是UserDetails实现
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String username = userDetails.getUsername();
            
            // 这里假设username就是用户ID，实际项目中可能需要调整
            try {
                return Long.parseLong(username);
            } catch (NumberFormatException e) {
                // 如果username不是数字，可能需要根据username查询用户ID
                // 这里暂时返回一个默认值，实际使用时需要调整
                return 1L;
            }
        }
        
        // 如果principal是String类型（通常是username）
        if (principal instanceof String) {
            try {
                return Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                // 同样的处理方式
                return 1L;
            }
        }
        
        // 默认返回值，实际使用时可能需要调整
        return 1L;
    }

    /**
     * 获取当前登录用户名
     * @return 用户名，如果未登录则返回null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if ("anonymousUser".equals(principal)) {
            return null;
        }
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        if (principal instanceof String) {
            return (String) principal;
        }
        
        return null;
    }

    /**
     * 获取当前Authentication对象
     * @return Authentication对象
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 检查是否已认证
     * @return 是否已认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
} 