package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdImage;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.service.FileService;
import com.example.vliascrm.service.ProdImageService;
import com.example.vliascrm.service.ProdGoodsService;
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

    @Autowired
    private ProdImageService prodImageService;

    @Autowired
    private ProdGoodsService prodGoodsService;

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
     * 上传商品图片并保存到数据库
     */
    @PostMapping(value = "/product-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传商品图片", description = "上传商品图片并自动保存到数据库")
    public ApiResponse<ProdImage> uploadProductImage(
            @Parameter(description = "要上传的图片文件") @RequestPart("file") MultipartFile file,
            @Parameter(description = "商品ID") @RequestParam("goodsId") Long goodsId,
            @Parameter(description = "是否为主图 0-否 1-是", example = "0") @RequestParam(value = "isMain", defaultValue = "0") Integer isMain,
            @Parameter(description = "排序", example = "0") @RequestParam(value = "sort", defaultValue = "0") Integer sort) {
        try {
            // 上传文件到products目录
            String imageUrl = fileService.uploadFile(file, "products");
            
            // 创建商品图片记录
            ProdImage prodImage = new ProdImage();
            prodImage.setGoodsId(goodsId);
            prodImage.setImageUrl(imageUrl);
            prodImage.setIsMain(isMain);
            prodImage.setSort(sort);
            
            // 如果是主图，先清除该商品的其他主图状态
            if (isMain == 1) {
                prodImageService.clearMainImageByGoodsId(goodsId);
            }
            
            // 保存到数据库
            ProdImage savedImage = prodImageService.save(prodImage);
            
            // 如果是主图，同时更新商品表的main_image字段
            if (savedImage.getIsMain() == 1) {
                prodGoodsService.updateMainImage(goodsId, imageUrl);
            }
            
            return ApiResponse.success(savedImage);
        } catch (IOException e) {
            return ApiResponse.failure("商品图片上传失败：" + e.getMessage());
        } catch (Exception e) {
            return ApiResponse.failure("商品图片保存失败：" + e.getMessage());
        }
    }

    /**
     * 批量上传商品图片
     */
    @PostMapping(value = "/product-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "批量上传商品图片", description = "一次上传多张商品图片")
    public ApiResponse<Map<String, Object>> uploadProductImages(
            @Parameter(description = "要上传的图片文件数组") @RequestPart("files") MultipartFile[] files,
            @Parameter(description = "商品ID") @RequestParam("goodsId") Long goodsId,
            @Parameter(description = "第一张图片是否为主图", example = "1") @RequestParam(value = "firstAsMain", defaultValue = "1") Integer firstAsMain) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("success", 0);
            result.put("failed", 0);
            result.put("images", new java.util.ArrayList<>());
            
            // 如果第一张设为主图，先清除该商品的其他主图状态
            if (firstAsMain == 1) {
                prodImageService.clearMainImageByGoodsId(goodsId);
            }
            
            for (int i = 0; i < files.length; i++) {
                MultipartFile file = files[i];
                try {
                    // 上传文件
                    String imageUrl = fileService.uploadFile(file, "products");
                    
                    // 创建商品图片记录
                    ProdImage prodImage = new ProdImage();
                    prodImage.setGoodsId(goodsId);
                    prodImage.setImageUrl(imageUrl);
                    prodImage.setIsMain(i == 0 && firstAsMain == 1 ? 1 : 0); // 第一张图片设为主图
                    prodImage.setSort(i); // 按上传顺序排序
                    
                    // 保存到数据库
                    ProdImage savedImage = prodImageService.save(prodImage);
                    ((java.util.List) result.get("images")).add(savedImage);
                    
                    // 如果是主图，同时更新商品表的main_image字段
                    if (savedImage.getIsMain() == 1) {
                        prodGoodsService.updateMainImage(goodsId, imageUrl);
                    }
                    
                    result.put("success", (Integer) result.get("success") + 1);
                } catch (Exception e) {
                    result.put("failed", (Integer) result.get("failed") + 1);
                }
            }
            
            return ApiResponse.success(result);
        } catch (Exception e) {
            return ApiResponse.failure("批量上传商品图片失败：" + e.getMessage());
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

    /**
     * 删除商品图片（同时删除文件和数据库记录）
     */
    @DeleteMapping("/product-image/{imageId}")
    @Operation(summary = "删除商品图片", description = "删除商品图片文件和数据库记录")
    public ApiResponse<Boolean> deleteProductImage(
            @Parameter(description = "图片ID") @PathVariable Long imageId) {
        try {
            // 获取图片信息
            var imageOpt = prodImageService.findById(imageId);
            if (!imageOpt.isPresent()) {
                return ApiResponse.failure("图片不存在");
            }
            
            ProdImage image = imageOpt.get();
            
            // 删除文件
            fileService.deleteFile(image.getImageUrl());
            
            // 删除数据库记录（软删除）
            prodImageService.deleteById(imageId);
            
            return ApiResponse.success(true);
        } catch (Exception e) {
            return ApiResponse.failure("删除商品图片失败：" + e.getMessage());
        }
    }
} 