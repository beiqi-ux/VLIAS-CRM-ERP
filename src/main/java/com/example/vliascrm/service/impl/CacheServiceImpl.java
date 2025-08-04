package com.example.vliascrm.service.impl;

import com.example.vliascrm.service.CacheService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * 默认过期时间：30分钟
     */
    private static final Duration DEFAULT_TTL = Duration.ofMinutes(30);

    @Override
    public void set(String key, Object value, Duration ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl.toMillis(), TimeUnit.MILLISECONDS);
            log.debug("缓存设置成功: key={}, ttl={}秒", key, ttl.getSeconds());
        } catch (Exception e) {
            log.error("缓存设置失败: key={}, error={}", key, e.getMessage());
            // 缓存设置失败不应该影响主业务流程
        }
    }

    @Override
    public void set(String key, Object value) {
        set(key, value, DEFAULT_TTL);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                log.debug("缓存未命中: key={}", key);
                return null;
            }
            
            log.debug("缓存命中: key={}", key);
            
            // 如果值的类型匹配，直接返回
            if (clazz.isInstance(value)) {
                return clazz.cast(value);
            }
            
            // 否则通过ObjectMapper转换
            return objectMapper.convertValue(value, clazz);
        } catch (Exception e) {
            log.error("缓存获取失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    @Override
    public <T> T get(String key, TypeReference<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                log.debug("缓存未命中: key={}", key);
                return null;
            }
            
            log.debug("缓存命中: key={}", key);
            return objectMapper.convertValue(value, type);
        } catch (Exception e) {
            log.error("缓存获取失败: key={}, error={}", key, e.getMessage());
            return null;
        }
    }

    @Override
    public void delete(String key) {
        try {
            Boolean result = redisTemplate.delete(key);
            log.debug("缓存删除: key={}, result={}", key, result);
        } catch (Exception e) {
            log.error("缓存删除失败: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public void delete(Collection<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return;
        }
        
        try {
            Long result = redisTemplate.delete(keys);
            log.debug("批量缓存删除: keys={}, result={}", keys.size(), result);
        } catch (Exception e) {
            log.error("批量缓存删除失败: keys={}, error={}", keys.size(), e.getMessage());
        }
    }

    @Override
    public void deletePattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                Long result = redisTemplate.delete(keys);
                log.debug("模式缓存删除: pattern={}, count={}, result={}", pattern, keys.size(), result);
            }
        } catch (Exception e) {
            log.error("模式缓存删除失败: pattern={}, error={}", pattern, e.getMessage());
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            Boolean result = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            log.error("缓存存在性检查失败: key={}, error={}", key, e.getMessage());
            return false;
        }
    }

    @Override
    public void expire(String key, Duration ttl) {
        try {
            Boolean result = redisTemplate.expire(key, ttl.toMillis(), TimeUnit.MILLISECONDS);
            log.debug("缓存过期时间设置: key={}, ttl={}秒, result={}", key, ttl.getSeconds(), result);
        } catch (Exception e) {
            log.error("缓存过期时间设置失败: key={}, error={}", key, e.getMessage());
        }
    }

    @Override
    public Set<String> keys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception e) {
            log.error("缓存键查询失败: pattern={}, error={}", pattern, e.getMessage());
            return Set.of();
        }
    }

    @Override
    public long getExpire(String key) {
        try {
            return redisTemplate.getExpire(key, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("缓存过期时间查询失败: key={}, error={}", key, e.getMessage());
            return -2; // 表示查询失败
        }
    }
} 