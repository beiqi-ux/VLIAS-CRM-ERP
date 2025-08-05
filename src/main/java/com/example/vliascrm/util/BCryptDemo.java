package com.example.vliascrm.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * BCrypt密码加密演示工具
 * 证明BCrypt可以加密任何密码，而不是写死的
 */
public class BCryptDemo {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        System.out.println("=== BCrypt密码加密演示 ===");
        System.out.println("BCrypt可以加密任何密码，不是写死的！\n");
        
        // 测试各种不同的密码
        String[] testPasswords = {
            "123456",           // 简单数字
            "password123",      // 字母数字组合
            "MySecretPass!",    // 包含特殊字符
            "中文密码123",       // 中文密码
            "VeryLongPasswordWithManyCharacters2024!@#", // 长密码
            "a",                // 单字符
            "qichengxu123",     // 用户自定义密码
            "Admin@2024"        // 复杂密码
        };
        
        for (String password : testPasswords) {
            System.out.println("原始密码: " + password);
            
            // 加密密码（每次结果都不同，因为有盐值）
            String hash1 = encoder.encode(password);
            String hash2 = encoder.encode(password);
            
            System.out.println("加密结果1: " + hash1);
            System.out.println("加密结果2: " + hash2);
            
            // 验证两个不同的哈希值都能匹配原密码
            boolean match1 = encoder.matches(password, hash1);
            boolean match2 = encoder.matches(password, hash2);
            
            System.out.println("验证结果1: " + (match1 ? "✅" : "❌"));
            System.out.println("验证结果2: " + (match2 ? "✅" : "❌"));
            System.out.println("哈希值相同吗? " + (hash1.equals(hash2) ? "是" : "否（正常，因为有随机盐值）"));
            System.out.println("-".repeat(80));
        }
        
        System.out.println("\n=== 重要说明 ===");
        System.out.println("1. BCrypt可以加密任何字符串，不限制密码内容");
        System.out.println("2. 每次加密同一个密码，结果都不同（因为内置随机盐值）");
        System.out.println("3. 但是验证时，任何一个哈希值都能正确匹配原密码");
        System.out.println("4. 这就是BCrypt的安全性所在！");
    }
} 