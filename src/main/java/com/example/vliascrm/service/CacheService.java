package com.example.vliascrm.service;

import com.fasterxml.jackson.core.type.TypeReference;

import java.time.Duration;

/**
 * 缓存服务接口
 * 提供统一的缓存操作接口
 */
public interface CacheService {

    /**
     * 存储缓存
     * @param key 缓存键
     * @param value 缓存值
     * @param ttl 过期时间
     */
    void set(String key, Object value, Duration ttl);

    /**
     * 获取缓存
     * @param key 缓存键
     * @param clazz 返回类型
     * @return 缓存值
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取缓存（支持泛型集合）
     * @param key 缓存键
     * @param typeReference 类型引用
     * @return 缓存值
     */
    <T> T get(String key, TypeReference<T> typeReference);

    /**
     * 删除缓存
     * @param key 缓存键
     */
    void delete(String key);

    /**
     * 删除匹配模式的缓存
     * @param pattern 模式（支持通配符）
     */
    void deleteByPattern(String pattern);

    /**
     * 检查缓存是否存在
     * @param key 缓存键
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 设置缓存过期时间
     * @param key 缓存键
     * @param ttl 过期时间
     */
    void expire(String key, Duration ttl);
} 