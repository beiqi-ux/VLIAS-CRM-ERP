package com.example.vliascrm.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用API响应对象
 * @param <T> 数据类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 成功响应
     * @param data 数据
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, Constants.SUCCESS_MESSAGE, data);
    }
    
    /**
     * 成功响应
     * @param message 消息
     * @param data 数据
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
    
    /**
     * 成功响应（仅消息）
     * @param message 消息
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null);
    }
    
    /**
     * 失败响应
     * @param message 消息
     * @param <T> 数据类型
     * @return 响应对象
     */
    public static <T> ApiResponse<T> failure(String message) {
        return new ApiResponse<>(false, message, null);
    }
} 