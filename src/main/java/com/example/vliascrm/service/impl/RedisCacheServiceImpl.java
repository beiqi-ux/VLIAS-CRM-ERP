package com.example.vliascrm.service.impl;

import com.example.vliascrm.service.CacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

/**
 * Redis缓存服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void set(String key, Object value, Duration ttl) {
        try {
            redisTemplate.opsForValue().set(key, value, ttl);
            log.debug("缓存已设置: key={}, ttl={}秒", key, ttl.getSeconds());
        } catch (Exception e) {
            log.error("设置缓存失败: key={}", key, e);
        }
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            
            if (clazz.isInstance(value)) {
                return clazz.cast(value);
            }
            
            // 如果类型不匹配，尝试通过JSON转换
            String json = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(json, clazz);
            
        } catch (Exception e) {
            log.error("获取缓存失败: key={}", key, e);
            return null;
        }
    }

    @Override
    public <T> T get(String key, TypeReference<T> typeReference) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            if (value == null) {
                return null;
            }
            
            String json = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(json, typeReference);
            
        } catch (Exception e) {
            log.error("获取缓存失败: key={}", key, e);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        try {
            Boolean deleted = redisTemplate.delete(key);
            log.debug("删除缓存: key={}, 结果={}", key, deleted);
        } catch (Exception e) {
            log.error("删除缓存失败: key={}", key, e);
        }
    }

    @Override
    public void deleteByPattern(String pattern) {
        try {
            Set<String> keys = redisTemplate.keys(pattern);
            if (keys != null && !keys.isEmpty()) {
                Long deleted = redisTemplate.delete(keys);
                log.debug("批量删除缓存: pattern={}, 删除数量={}", pattern, deleted);
            }
        } catch (Exception e) {
            log.error("批量删除缓存失败: pattern={}", pattern, e);
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            Boolean exists = redisTemplate.hasKey(key);
            return exists != null && exists;
        } catch (Exception e) {
            log.error("检查缓存存在性失败: key={}", key, e);
            return false;
        }
    }

    @Override
    public void expire(String key, Duration ttl) {
        try {
            Boolean result = redisTemplate.expire(key, ttl);
            log.debug("设置缓存过期时间: key={}, ttl={}秒, 结果={}", key, ttl.getSeconds(), result);
        } catch (Exception e) {
            log.error("设置缓存过期时间失败: key={}", key, e);
        }
    }
} 