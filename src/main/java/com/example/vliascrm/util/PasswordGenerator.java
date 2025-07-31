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
        String password = "12345678";
        String encodedPassword = encoder.encode(password);
        
        System.out.println("原始密码: " + password);
        System.out.println("加密后密码: " + encodedPassword);
    }
} 