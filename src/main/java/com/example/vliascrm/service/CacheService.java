package com.example.vliascrm.service;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;

/**
 * 通用缓存服务接口
 * 提供Redis缓存的基本操作，支持泛型
 */
public interface CacheService {

    /**
     * 设置缓存
     *
     * @param key   缓存键
     * @param value 缓存值
     * @param ttl   过期时间
     */
    void set(String key, Object value, Duration ttl);

    /**
     * 设置缓存（默认过期时间30分钟）
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    void set(String key, Object value);

    /**
     * 获取缓存
     *
     * @param key   缓存键
     * @param clazz 目标类型
     * @param <T>   泛型类型
     * @return 缓存值
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取缓存（支持复杂类型）
     *
     * @param key      缓存键
     * @param type     类型引用
     * @param <T>      泛型类型
     * @return 缓存值
     */
    <T> T get(String key, com.fasterxml.jackson.core.type.TypeReference<T> type);

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    void delete(String key);

    /**
     * 批量删除缓存
     *
     * @param keys 缓存键集合
     */
    void delete(Collection<String> keys);

    /**
     * 删除匹配模式的缓存
     *
     * @param pattern 匹配模式
     */
    void deletePattern(String pattern);

    /**
     * 检查缓存是否存在
     *
     * @param key 缓存键
     * @return 是否存在
     */
    boolean exists(String key);

    /**
     * 设置过期时间
     *
     * @param key 缓存键
     * @param ttl 过期时间
     */
    void expire(String key, Duration ttl);

    /**
     * 获取匹配模式的所有键
     *
     * @param pattern 匹配模式
     * @return 键集合
     */
    Set<String> keys(String pattern);

    /**
     * 获取缓存剩余过期时间
     *
     * @param key 缓存键
     * @return 剩余时间（秒），-1表示永不过期，-2表示不存在
     */
    long getExpire(String key);
} 