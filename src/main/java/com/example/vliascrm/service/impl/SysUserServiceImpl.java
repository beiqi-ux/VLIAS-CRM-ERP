package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.repository.SysUserRepository;
import com.example.vliascrm.service.CacheService;
import com.example.vliascrm.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * 系统用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl implements SysUserService {

    private final SysUserRepository sysUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final CacheService cacheService;

    // 缓存键前缀
    private static final String USER_CACHE_PREFIX = "user:";
    private static final String USER_BY_USERNAME_PREFIX = "user:username:";
    
    // 缓存过期时间：15分钟
    private static final Duration USER_CACHE_TTL = Duration.ofMinutes(15);

    @Override
    public Optional<SysUser> findById(Long id) {
        String cacheKey = USER_CACHE_PREFIX + id;
        
        // 先从缓存中查询
        SysUser cachedUser = cacheService.get(cacheKey, SysUser.class);
        if (cachedUser != null) {
            log.debug("用户缓存命中: id={}", id);
            return Optional.of(cachedUser);
        }
        
        // 缓存未命中，从数据库查询
        Optional<SysUser> userOpt = sysUserRepository.findById(id);
        if (userOpt.isPresent()) {
            SysUser user = userOpt.get();
            // 清除密码字段再缓存
            SysUser userForCache = cloneUserWithoutPassword(user);
            cacheService.set(cacheKey, userForCache, USER_CACHE_TTL);
            log.debug("用户信息已缓存: id={}", id);
        }
        
        return userOpt;
    }

    @Override
    public Optional<SysUser> findByUsername(String username) {
        String cacheKey = USER_BY_USERNAME_PREFIX + username;
        
        // 先从缓存中查询
        SysUser cachedUser = cacheService.get(cacheKey, SysUser.class);
        if (cachedUser != null) {
            log.debug("用户缓存命中: username={}", username);
            return Optional.of(cachedUser);
        }
        
        // 缓存未命中，从数据库查询
        Optional<SysUser> userOpt = sysUserRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            SysUser user = userOpt.get();
            // 清除密码字段再缓存
            SysUser userForCache = cloneUserWithoutPassword(user);
            cacheService.set(cacheKey, userForCache, USER_CACHE_TTL);
            // 同时缓存按ID查询的版本
            cacheService.set(USER_CACHE_PREFIX + user.getId(), userForCache, USER_CACHE_TTL);
            log.debug("用户信息已缓存: username={}", username);
        }
        
        return userOpt;
    }

    @Override
    public List<SysUser> findAll() {
        return sysUserRepository.findAll();
    }

    @Override
    @Transactional
    public SysUser save(SysUser user) {
        // 如果是新用户，对密码进行加密
        if (user.getId() == null && user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        SysUser savedUser = sysUserRepository.save(user);
        
        // 清理相关缓存
        clearUserCache(savedUser.getId(), savedUser.getUsername());
        
        return savedUser;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // 先查询用户名用于清理缓存
        Optional<SysUser> userOpt = sysUserRepository.findById(id);
        String username = userOpt.map(SysUser::getUsername).orElse(null);
        
        sysUserRepository.deleteById(id);
        
        // 清理相关缓存
        clearUserCache(id, username);
    }

    @Override
    @Transactional
    public SysUser updateAvatar(String username, String avatarUrl) {
        Optional<SysUser> userOpt = sysUserRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            SysUser user = userOpt.get();
            user.setAvatar(avatarUrl);
            SysUser savedUser = sysUserRepository.save(user);
            
            // 清理相关缓存
            clearUserCache(savedUser.getId(), savedUser.getUsername());
            
            return savedUser;
        }
        throw new RuntimeException("用户不存在: " + username);
    }

    /**
     * 克隆用户对象但不包含密码字段（用于缓存）
     */
    private SysUser cloneUserWithoutPassword(SysUser source) {
        if (source == null) {
            return null;
        }
        
        SysUser target = new SysUser();
        target.setId(source.getId());
        target.setUsername(source.getUsername());
        target.setRealName(source.getRealName());
        target.setEmail(source.getEmail());
        target.setMobile(source.getMobile());
        target.setAvatar(source.getAvatar());
        target.setGender(source.getGender());
        target.setStatus(source.getStatus());
        target.setOrgId(source.getOrgId());
        target.setDeptId(source.getDeptId());
        target.setPositionId(source.getPositionId());
        target.setLastLoginTime(source.getLastLoginTime());
        target.setEmpNo(source.getEmpNo());
        target.setUserType(source.getUserType());
        target.setCreateTime(source.getCreateTime());
        target.setUpdateTime(source.getUpdateTime());
        // 不复制password字段
        
        return target;
    }

    /**
     * 清理用户相关缓存
     */
    private void clearUserCache(Long userId, String username) {
        if (userId != null) {
            cacheService.delete(USER_CACHE_PREFIX + userId);
        }
        if (username != null) {
            cacheService.delete(USER_BY_USERNAME_PREFIX + username);
        }
        log.debug("用户缓存已清理: userId={}, username={}", userId, username);
    }
} 