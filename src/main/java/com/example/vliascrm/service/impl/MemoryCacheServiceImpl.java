package com.example.vliascrm.service.impl;

import com.example.vliascrm.service.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 内存缓存服务实现类
 * 当Redis不可用时的备选方案
 */
@Slf4j
@Service
@ConditionalOnMissingBean(RedisTemplate.class)
public class MemoryCacheServiceImpl implements CacheService {

    private final ObjectMapper objectMapper;
    private final ConcurrentHashMap<String, CacheItem> cache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * 缓存项
     */
    private static class CacheItem {
        private final Object value;
        private final LocalDateTime expireTime;

        public CacheItem(Object value, LocalDateTime expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        public boolean isExpired() {
            return expireTime != null && LocalDateTime.now().isAfter(expireTime);
        }

        public Object getValue() {
            return value;
        }
    }

    public MemoryCacheServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        
        // 启动定时清理过期缓存的任务
        scheduler.scheduleAtFixedRate(this::cleanExpiredItems, 1, 1, TimeUnit.MINUTES);
        log.info("内存缓存服务已启动，定时清理任务已启动");
    }

    @Override
    public void set(String key, Object value, Duration ttl) {
        try {
            LocalDateTime expireTime = ttl != null ? LocalDateTime.now().plus(ttl) : null;
            cache.put(key, new CacheItem(value, expireTime));
            log.debug("内存缓存已设置: key={}, ttl={}秒", key, ttl != null ? ttl.getSeconds() : "永不过期");
        } catch (Exception e) {
            log.error("设置内存缓存失败: key={}", key, e);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        try {
            CacheItem item = cache.get(key);
            if (item == null || item.isExpired()) {
                if (item != null && item.isExpired()) {
                    cache.remove(key);
                }
                return null;
            }

            Object value = item.getValue();
            if (clazz.isInstance(value)) {
                return clazz.cast(value);
            }

            // 类型转换
            String json = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(json, clazz);

        } catch (Exception e) {
            log.error("获取内存缓存失败: key={}", key, e);
            return null;
        }
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeReference) {
        try {
            CacheItem item = cache.get(key);
            if (item == null || item.isExpired()) {
                if (item != null && item.isExpired()) {
                    cache.remove(key);
                }
                return null;
            }

            Object value = item.getValue();
            String json = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(json, typeReference);

        } catch (Exception e) {
            log.error("获取内存缓存失败: key={}", key, e);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        try {
            CacheItem removed = cache.remove(key);
            log.debug("删除内存缓存: key={}, 结果={}", key, removed != null);
        } catch (Exception e) {
            log.error("删除内存缓存失败: key={}", key, e);
        }
    }

    @Override
    public void deleteByPattern(String pattern) {
        try {
            String regex = pattern.replace("*", ".*");
            int deleted = 0;
            
            for (String key : cache.keySet()) {
                if (key.matches(regex)) {
                    cache.remove(key);
                    deleted++;
                }
            }
            
            log.debug("批量删除内存缓存: pattern={}, 删除数量={}", pattern, deleted);
        } catch (Exception e) {
            log.error("批量删除内存缓存失败: pattern={}", pattern, e);
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            CacheItem item = cache.get(key);
            if (item != null && item.isExpired()) {
                cache.remove(key);
                return false;
            }
            return item != null;
        } catch (Exception e) {
            log.error("检查内存缓存存在性失败: key={}", key, e);
            return false;
        }
    }

    @Override
    public void expire(String key, Duration ttl) {
        try {
            CacheItem item = cache.get(key);
            if (item != null) {
                LocalDateTime expireTime = ttl != null ? LocalDateTime.now().plus(ttl) : null;
                cache.put(key, new CacheItem(item.getValue(), expireTime));
                log.debug("设置内存缓存过期时间: key={}, ttl={}秒", key, ttl != null ? ttl.getSeconds() : "永不过期");
            }
        } catch (Exception e) {
            log.error("设置内存缓存过期时间失败: key={}", key, e);
        }
    }

    /**
     * 清理过期的缓存项
     */
    private void cleanExpiredItems() {
        try {
            int cleaned = 0;
            for (String key : cache.keySet()) {
                CacheItem item = cache.get(key);
                if (item != null && item.isExpired()) {
                    cache.remove(key);
                    cleaned++;
                }
            }
            
            if (cleaned > 0) {
                log.debug("清理过期内存缓存项: 数量={}", cleaned);
            }
        } catch (Exception e) {
            log.error("清理过期内存缓存项失败", e);
        }
    }
} 