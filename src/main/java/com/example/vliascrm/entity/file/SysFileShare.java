package com.example.vliascrm.entity.file;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 文件分享实体类
 */
@Data
@Entity
@Table(name = "sys_file_share")
@EqualsAndHashCode(callSuper = true)
public class SysFileShare extends BaseEntity {

    /**
     * 文件ID
     */
    @Column(name = "file_id", nullable = false)
    private Long fileId;

    /**
     * 分享标题
     */
    @Column(name = "share_title", length = 100)
    private String shareTitle;

    /**
     * 分享链接
     */
    @Column(name = "share_url", nullable = false, length = 255)
    private String shareUrl;

    /**
     * 分享码
     */
    @Column(name = "share_code", length = 20)
    private String shareCode;

    /**
     * 分享类型 1-公开 2-私密 3-指定用户
     */
    @Column(name = "share_type", nullable = false)
    private Integer shareType;

    /**
     * 有效期类型 1-永久 2-固定天数 3-自定义
     */
    @Column(name = "expire_type", nullable = false)
    private Integer expireType;

    /**
     * 有效天数
     */
    @Column(name = "expire_days")
    private Integer expireDays;

    /**
     * 过期时间
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    /**
     * 分享次数限制 0-不限制
     */
    @Column(name = "limit_count", columnDefinition = "int default 0")
    private Integer limitCount = 0;

    /**
     * 已分享次数
     */
    @Column(name = "share_count", columnDefinition = "int default 0")
    private Integer shareCount = 0;

    /**
     * 状态 0-禁用 1-正常 2-已过期
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 是否需要提取码 0-否 1-是
     */
    @Column(name = "need_code", columnDefinition = "tinyint default 0")
    private Integer needCode = 0;

    /**
     * 提取码
     */
    @Column(name = "extract_code", length = 10)
    private String extractCode;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

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