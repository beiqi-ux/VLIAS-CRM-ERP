package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.repository.SysUserRepository;
import com.example.vliascrm.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统用户服务实现类
 */
@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<SysUser> findById(Long id) {
        return sysUserRepository.findById(id);
    }

    @Override
    public Optional<SysUser> findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
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
        return sysUserRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        sysUserRepository.deleteById(id);
    }

    @Override
    @Transactional
    public SysUser updateAvatar(String username, String avatarUrl) {
        Optional<SysUser> userOpt = sysUserRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            SysUser user = userOpt.get();
            user.setAvatar(avatarUrl);
            return sysUserRepository.save(user);
        }
        throw new RuntimeException("用户不存在: " + username);
    }
} 