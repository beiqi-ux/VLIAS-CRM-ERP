package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.LoginDTO;
import com.example.vliascrm.dto.LoginResponseDTO;
import com.example.vliascrm.service.SysLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统登录控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SysLoginController {
    
    private final SysLoginService sysLoginService;
    
    /**
     * 登录
     *
     * @param loginDTO 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
        LoginResponseDTO loginResponseDTO = sysLoginService.login(loginDTO);
        return ApiResponse.success(loginResponseDTO);
    }
    
    /**
     * 登出
     *
     * @return 结果
     */
    @PostMapping("/logout")
    public ApiResponse<Object> logout() {
        sysLoginService.logout();
        return ApiResponse.success(null);
    }
    
    /**
     * 刷新令牌
     *
     * @param token 旧令牌
     * @return 新令牌
     */
    @PostMapping("/refresh")
    public ApiResponse<LoginResponseDTO> refreshToken(@RequestParam String token) {
        LoginResponseDTO loginResponseDTO = sysLoginService.refreshToken(token);
        return ApiResponse.success(loginResponseDTO);
    }
    
    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/info")
    public ApiResponse<LoginResponseDTO> getUserInfo() {
        LoginResponseDTO loginResponseDTO = sysLoginService.getCurrentUserInfo();
        return ApiResponse.success(loginResponseDTO);
    }
    
    /**
     * 获取当前用户权限调试信息
     *
     * @return 调试信息
     */
    @GetMapping("/debug/permissions")
    public ApiResponse<Map<String, Object>> getDebugPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> debugInfo = new HashMap<>();
        
        if (authentication != null && authentication.isAuthenticated()) {
            debugInfo.put("username", authentication.getName());
            debugInfo.put("authorities", authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()));
            debugInfo.put("principal", authentication.getPrincipal().toString());
            debugInfo.put("isAuthenticated", authentication.isAuthenticated());
            debugInfo.put("authType", authentication.getClass().getSimpleName());
        } else {
            debugInfo.put("message", "No authentication found");
        }
        
        return ApiResponse.success(debugInfo);
    }
} 