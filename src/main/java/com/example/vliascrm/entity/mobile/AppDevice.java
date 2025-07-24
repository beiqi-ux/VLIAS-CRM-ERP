package com.example.vliascrm.entity.mobile;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备实体类
 */
@Data
@Entity
@Table(name = "app_device")
@EqualsAndHashCode(callSuper = true)
public class AppDevice extends BaseEntity {

    /**
     * 会员ID
     */
    @Column(name = "member_id")
    private Long memberId;

    /**
     * 设备Token
     */
    @Column(name = "device_token", nullable = false, length = 100)
    private String deviceToken;

    /**
     * 设备类型
     */
    @Column(name = "device_type", nullable = false, length = 50)
    private String deviceType;

    /**
     * 设备品牌
     */
    @Column(name = "device_brand", length = 50)
    private String deviceBrand;

    /**
     * 设备型号
     */
    @Column(name = "device_model", length = 50)
    private String deviceModel;

    /**
     * 操作系统类型
     */
    @Column(name = "os_type", length = 20)
    private String osType;

    /**
     * 操作系统版本
     */
    @Column(name = "os_version", length = 20)
    private String osVersion;

    /**
     * APP版本
     */
    @Column(name = "app_version", length = 20)
    private String appVersion;

    /**
     * 推送ID
     */
    @Column(name = "push_id", length = 100)
    private String pushId;

    /**
     * 最后活跃时间
     */
    @Column(name = "last_active_time")
    private LocalDateTime lastActiveTime;
} 