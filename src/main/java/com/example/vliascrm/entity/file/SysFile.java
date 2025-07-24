package com.example.vliascrm.entity.file;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文件实体类
 */
@Data
@Entity
@Table(name = "sys_file")
@EqualsAndHashCode(callSuper = true)
public class SysFile extends BaseEntity {

    /**
     * 文件名称
     */
    @Column(name = "file_name", nullable = false, length = 200)
    private String fileName;

    /**
     * 原始文件名
     */
    @Column(name = "original_name", nullable = false, length = 200)
    private String originalName;

    /**
     * 文件URL
     */
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    /**
     * 文件路径
     */
    @Column(name = "file_path", length = 500)
    private String filePath;

    /**
     * 存储类型 1-本地 2-阿里云OSS 3-腾讯云COS 4-七牛云
     */
    @Column(name = "storage_type", nullable = false)
    private Integer storageType;

    /**
     * 文件大小(B)
     */
    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    /**
     * 文件类型
     */
    @Column(name = "file_type", length = 50)
    private String fileType;

    /**
     * 文件扩展名
     */
    @Column(name = "file_extension", length = 20)
    private String fileExtension;

    /**
     * MD5值
     */
    @Column(name = "md5", length = 32)
    private String md5;

    /**
     * 分类ID
     */
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;
} 