package com.example.vliascrm.entity.mobile;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 版本实体类
 */
@Data
@Entity
@Table(name = "app_version")
@EqualsAndHashCode(callSuper = true)
public class AppVersion extends BaseEntity {

    /**
     * 版本名称
     */
    @Column(name = "version_name", nullable = false, length = 20)
    private String versionName;

    /**
     * 版本号
     */
    @Column(name = "version_code", nullable = false)
    private Integer versionCode;

    /**
     * 平台 1-Android 2-iOS 3-小程序 4-H5
     */
    @Column(name = "platform", nullable = false)
    private Integer platform;

    /**
     * 下载地址
     */
    @Column(name = "download_url", length = 255)
    private String downloadUrl;

    /**
     * 更新内容
     */
    @Column(name = "update_content", nullable = false, columnDefinition = "text")
    private String updateContent;

    /**
     * 文件大小(KB)
     */
    @Column(name = "file_size")
    private Integer fileSize;

    /**
     * 是否强制更新 0-否 1-是
     */
    @Column(name = "is_force", columnDefinition = "tinyint default 0")
    private Integer isForce = 0;

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