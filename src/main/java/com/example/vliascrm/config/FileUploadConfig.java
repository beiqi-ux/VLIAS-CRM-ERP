package com.example.vliascrm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

/**
 * 文件上传配置
 */
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadConfig.class);

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    /**
     * 添加资源映射，使上传的文件可以通过URL访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
            logger.info("创建上传目录: {}", uploadDirFile.getAbsolutePath());
        }
        
        String uploadPath = "file:" + uploadDirFile.getAbsolutePath() + File.separator;
        logger.info("配置静态资源映射: /uploads/** -> {}", uploadPath);
        
        // 将/uploads/**的请求映射到文件系统中的上传目录
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }

    /**
     * 配置Multipart解析器
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
} 