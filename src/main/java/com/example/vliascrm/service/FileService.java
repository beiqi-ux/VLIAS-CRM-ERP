package com.example.vliascrm.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件服务接口
 */
public interface FileService {
    
    /**
     * 上传文件
     * @param file 要上传的文件
     * @param subDir 子目录，如"avatars"、"documents"等
     * @return 文件访问URL
     * @throws IOException 如果文件操作失败
     */
    String uploadFile(MultipartFile file, String subDir) throws IOException;
    
    /**
     * 删除文件
     * @param fileUrl 文件URL或路径
     * @return 是否删除成功
     */
    boolean deleteFile(String fileUrl);
} 