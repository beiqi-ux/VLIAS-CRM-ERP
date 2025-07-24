package com.example.vliascrm.entity.api;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * API配置实体类
 */
@Data
@Entity
@Table(name = "api_config")
@EqualsAndHashCode(callSuper = true)
public class ApiConfig extends BaseEntity {

    /**
     * API名称
     */
    @Column(name = "api_name", nullable = false, length = 50)
    private String apiName;

    /**
     * API编码
     */
    @Column(name = "api_code", nullable = false, length = 50)
    private String apiCode;

    /**
     * API类型 1-内部接口 2-外部接口
     */
    @Column(name = "api_type", nullable = false)
    private Integer apiType;

    /**
     * 接口地址
     */
    @Column(name = "api_url", nullable = false, length = 255)
    private String apiUrl;

    /**
     * 请求方式 GET/POST/PUT/DELETE
     */
    @Column(name = "request_method", nullable = false, length = 10)
    private String requestMethod;

    /**
     * 请求头
     */
    @Column(name = "request_headers", columnDefinition = "text")
    private String requestHeaders;

    /**
     * 请求参数
     */
    @Column(name = "request_params", columnDefinition = "text")
    private String requestParams;

    /**
     * 响应格式 1-JSON 2-XML 3-其他
     */
    @Column(name = "response_format", columnDefinition = "tinyint default 1")
    private Integer responseFormat = 1;

    /**
     * 是否需要认证 0-否 1-是
     */
    @Column(name = "need_auth", columnDefinition = "tinyint default 0")
    private Integer needAuth = 0;

    /**
     * 认证方式 1-Basic 2-Token 3-OAuth 4-自定义
     */
    @Column(name = "auth_type")
    private Integer authType;

    /**
     * 认证信息
     */
    @Column(name = "auth_config", columnDefinition = "text")
    private String authConfig;

    /**
     * 超时时间(秒)
     */
    @Column(name = "timeout", columnDefinition = "int default 30")
    private Integer timeout = 30;

    /**
     * 重试次数
     */
    @Column(name = "retry_times", columnDefinition = "int default 0")
    private Integer retryTimes = 0;

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