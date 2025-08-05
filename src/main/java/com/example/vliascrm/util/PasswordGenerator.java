package com.example.vliascrm.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具类
 */
public class PasswordGenerator {
    
    public static void main(String[] args) {
        // 创建BCryptPasswordEncoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 使用用户提供的密码
        String password = "123456789";
        String encodedPassword = encoder.encode(password);
        
        System.out.println("原始密码: " + password);
        System.out.println("加密后密码: " + encodedPassword);
        
        // 验证密码是否匹配
        String existingHash = "$2a$10$N.zmdr9k7uOCQb96VnnqiOasF.QI7NnkqMBpuCzbeJm1XAJhvRmnO";
        boolean matches = encoder.matches(password, existingHash);
        System.out.println("密码是否匹配现有哈希: " + matches);
        
        // 测试现有哈希对应的原始密码
        System.out.println("测试不同密码:");
        String[] testPasswords = {"123456789", "12345678", "123456", "admin", "password"};
        for (String testPwd : testPasswords) {
            boolean match = encoder.matches(testPwd, existingHash);
            System.out.println("密码 '" + testPwd + "' 匹配: " + match);
        }
    }
} 