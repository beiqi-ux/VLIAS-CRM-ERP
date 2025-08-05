package com.example.vliascrm.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码生成工具类
 */
public class PasswordGenerator {
    
    public static void main(String[] args) {
        // 创建BCryptPasswordEncoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // 数据库中更新后的哈希
        String updatedHash = "$2a$10$5.N/tBZWqZfeZVhuN/r0Nuq2l.iFbWCzLP.4YCPAuOZkGL91cgW6e";
        
        System.out.println("验证更新后的密码哈希:");
        String[] testPasswords = {"123456", "12345678", "123456789", "admin", "password"};
        for (String testPwd : testPasswords) {
            boolean match = encoder.matches(testPwd, updatedHash);
            System.out.println("密码 '" + testPwd + "' 匹配更新后哈希: " + match);
        }
        
        System.out.println("\n=== 最终结果 ===");
        boolean finalCheck = encoder.matches("123456", updatedHash);
        System.out.println("用户: 18845913092");
        System.out.println("密码: 123456");
        System.out.println("验证结果: " + (finalCheck ? "✅ 成功" : "❌ 失败"));
    }
} 