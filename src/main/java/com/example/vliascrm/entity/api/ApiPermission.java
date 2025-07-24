package com.example.vliascrm.entity.api;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 接口权限实体类
 */
@Data
@Entity
@Table(name = "api_permission")
@EqualsAndHashCode(callSuper = true)
public class ApiPermission extends BaseEntity {

    /**
     * API配置ID
     */
    @Column(name = "api_id", nullable = false)
    private Long apiId;

    /**
     * 权限类型 1-角色 2-用户 3-应用
     */
    @Column(name = "permission_type", nullable = false)
    private Integer permissionType;

    /**
     * 权限对象ID
     */
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    /**
     * 权限范围 1-读取 2-写入 3-读写
     */
    @Column(name = "scope", columnDefinition = "tinyint default 1")
    private Integer scope = 1;

    /**
     * 权限有效期类型 1-永久 2-固定天数 3-自定义
     */
    @Column(name = "expire_type", columnDefinition = "tinyint default 1")
    private Integer expireType = 1;

    /**
     * 有效天数
     */
    @Column(name = "expire_days")
    private Integer expireDays;

    /**
     * 有效期开始时间
     */
    @Column(name = "start_time")
    private java.time.LocalDateTime startTime;

    /**
     * 有效期结束时间
     */
    @Column(name = "end_time")
    private java.time.LocalDateTime endTime;

    /**
     * 访问频率限制（次/分钟） 0-不限制
     */
    @Column(name = "rate_limit", columnDefinition = "int default 0")
    private Integer rateLimit = 0;

    /**
     * 日访问限制（次/天） 0-不限制
     */
    @Column(name = "daily_limit", columnDefinition = "int default 0")
    private Integer dailyLimit = 0;

    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

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