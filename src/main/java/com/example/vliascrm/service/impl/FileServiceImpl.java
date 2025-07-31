package com.example.vliascrm.service.impl;

import com.example.vliascrm.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件服务实现类
 */
@Service
public class FileServiceImpl implements FileService {
    
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    @Value("${server.port}")
    private String serverPort;

    /**
     * 上传文件
     *
     * @param file 要上传的文件
     * @param subDir 子目录，如"avatars"、"documents"等
     * @return 文件访问URL
     * @throws IOException 如果文件操作失败
     */
    @Override
    public String uploadFile(MultipartFile file, String subDir) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        try {
            // 获取文件名和扩展名
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            logger.info("开始上传文件: {}, 子目录: {}", originalFilename, subDir);
            
            String fileExtension = getFileExtension(originalFilename);
            
            // 生成唯一的文件名
            String newFilename = UUID.randomUUID().toString() + fileExtension;
            
            // 按日期组织子目录
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            
            // 创建目标目录
            String targetDir = uploadDir + File.separator + subDir + File.separator + datePath;
            Path targetPath = Paths.get(targetDir);
            
            // 确保目录存在
            Files.createDirectories(targetPath);
            logger.debug("创建目录: {}", targetPath);
            
            // 保存文件
            Path filePath = targetPath.resolve(newFilename);
            logger.debug("文件将保存到: {}", filePath);
            
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // 返回文件的访问URL（相对路径）
            String fileUrl = "/uploads/" + subDir + "/" + datePath + "/" + newFilename;
            logger.info("文件上传成功, URL: {}", fileUrl);
            
            return fileUrl;
        } catch (IOException e) {
            logger.error("文件上传失败: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL或路径
     * @return 是否删除成功
     */
    @Override
    public boolean deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return false;
        }

        try {
            // 将URL转换为本地文件路径
            String localPath = fileUrl;
            if (fileUrl.startsWith("/uploads/")) {
                localPath = uploadDir + fileUrl.substring("/uploads".length());
            }

            Path path = Paths.get(localPath);
            logger.info("尝试删除文件: {}", path);
            boolean result = Files.deleteIfExists(path);
            
            if (result) {
                logger.info("文件删除成功");
            } else {
                logger.warn("文件不存在或无法删除");
            }
            
            return result;
        } catch (IOException e) {
            logger.error("删除文件时出错: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return 扩展名（包含点号）
     */
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex);
    }
} 