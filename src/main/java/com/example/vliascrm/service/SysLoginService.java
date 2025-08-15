package com.example.vliascrm.service;

import com.example.vliascrm.dto.LoginDTO;
import com.example.vliascrm.dto.LoginResponseDTO;

/**
 * 登录服务接口
 */
public interface SysLoginService {
    
    /**
     * 用户登录
     *
     * @param loginDTO 登录请求DTO
     * @return 登录结果
     */
    LoginResponseDTO login(LoginDTO loginDTO);
    
    /**
     * 退出登录
     */
    void logout();
    
    /**
     * 刷新令牌
     *
     * @param oldToken 旧令牌
     * @return 新令牌信息
     */
    LoginResponseDTO refreshToken(String oldToken);
    
    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息
     */
    LoginResponseDTO getCurrentUserInfo();
} 