package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.service.FileService;
import com.example.vliascrm.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/files")
@Tag(name = "文件管理", description = "文件上传、删除等操作")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 上传通用文件
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传通用文件", description = "上传文件到指定子目录")
    public ApiResponse<Map<String, String>> uploadFile(
            @Parameter(description = "要上传的文件") @RequestPart("file") MultipartFile file,
            @Parameter(description = "子目录，如avatars、documents等") @RequestParam(value = "subDir", defaultValue = "common") String subDir) {
        try {
            String fileUrl = fileService.uploadFile(file, subDir);
            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("filename", file.getOriginalFilename());
            result.put("size", String.valueOf(file.getSize()));
            return ApiResponse.success(result);
        } catch (IOException e) {
            return ApiResponse.failure("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 上传用户头像
     */
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传用户头像", description = "上传当前登录用户的头像")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Map<String, String>> uploadAvatar(
            @Parameter(description = "要上传的头像文件") @RequestPart("file") MultipartFile file) {
        try {
            // 上传到avatars目录
            String avatarUrl = fileService.uploadFile(file, "avatars");
            
            // 获取当前登录用户
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            
            // 更新用户头像URL
            SysUser updatedUser = sysUserService.updateAvatar(currentUsername, avatarUrl);
            
            Map<String, String> result = new HashMap<>();
            result.put("url", avatarUrl);
            return ApiResponse.success(result);
        } catch (IOException e) {
            return ApiResponse.failure("头像上传失败：" + e.getMessage());
        } catch (RuntimeException e) {
            return ApiResponse.failure("用户头像更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/{fileUrl}")
    @Operation(summary = "删除文件", description = "根据文件URL删除文件")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Boolean> deleteFile(
            @Parameter(description = "文件URL") @PathVariable String fileUrl) {
        boolean deleted = fileService.deleteFile(fileUrl);
        if (deleted) {
            return ApiResponse.success(deleted);
        } else {
            return ApiResponse.failure("文件删除失败");
        }
    }
} 