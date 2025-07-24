package com.example.vliascrm.entity.api;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 第三方平台实体类
 */
@Data
@Entity
@Table(name = "api_platform")
@EqualsAndHashCode(callSuper = true)
public class ApiPlatform extends BaseEntity {

    /**
     * 平台名称
     */
    @Column(name = "platform_name", nullable = false, length = 50)
    private String platformName;

    /**
     * 平台编码
     */
    @Column(name = "platform_code", nullable = false, length = 50)
    private String platformCode;

    /**
     * 平台类型 1-支付平台 2-物流平台 3-短信平台 4-推送平台 5-社交平台 6-其他
     */
    @Column(name = "platform_type", nullable = false)
    private Integer platformType;

    /**
     * 平台图标
     */
    @Column(name = "platform_icon", length = 255)
    private String platformIcon;

    /**
     * 平台主页
     */
    @Column(name = "platform_url", length = 255)
    private String platformUrl;

    /**
     * 应用ID
     */
    @Column(name = "app_id", length = 100)
    private String appId;

    /**
     * 应用密钥
     */
    @Column(name = "app_secret", length = 200)
    private String appSecret;

    /**
     * 应用公钥
     */
    @Column(name = "app_public_key", columnDefinition = "text")
    private String appPublicKey;

    /**
     * 应用私钥
     */
    @Column(name = "app_private_key", columnDefinition = "text")
    private String appPrivateKey;

    /**
     * 平台公钥
     */
    @Column(name = "platform_public_key", columnDefinition = "text")
    private String platformPublicKey;

    /**
     * 其他配置
     */
    @Column(name = "other_config", columnDefinition = "text")
    private String otherConfig;

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