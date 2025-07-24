package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.system.SysUser;
import com.example.vliascrm.repository.UserRepository;
import com.example.vliascrm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Optional<SysUser> findById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public Optional<SysUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    @Override
    public List<SysUser> findAll() {
        return userRepository.findAll();
    }
    
    @Override
    @Transactional
    public SysUser save(SysUser user) {
        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public SysUser update(SysUser user) {
        SysUser existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        // 如果密码不为空，则进行加密
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            // 否则使用原密码
            user.setPassword(existingUser.getPassword());
        }
        
        return userRepository.save(user);
    }
    
    @Override
    @Transactional
    public void deleteById(Long id) {
        // 逻辑删除
        SysUser user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setIsDeleted(true);
        userRepository.save(user);
    }
} 