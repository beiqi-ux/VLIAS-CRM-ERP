package com.example.vliascrm.entity.api;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 接口调用日志实体类
 */
@Data
@Entity
@Table(name = "api_log")
@EntityListeners(AuditingEntityListener.class)
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * API配置ID
     */
    @Column(name = "api_id")
    private Long apiId;

    /**
     * API名称
     */
    @Column(name = "api_name", length = 50)
    private String apiName;

    /**
     * API编码
     */
    @Column(name = "api_code", length = 50)
    private String apiCode;

    /**
     * 请求方式
     */
    @Column(name = "request_method", length = 10)
    private String requestMethod;

    /**
     * 请求URL
     */
    @Column(name = "request_url", length = 500)
    private String requestUrl;

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
     * 请求体
     */
    @Column(name = "request_body", columnDefinition = "text")
    private String requestBody;

    /**
     * 响应状态码
     */
    @Column(name = "response_code")
    private Integer responseCode;

    /**
     * 响应头
     */
    @Column(name = "response_headers", columnDefinition = "text")
    private String responseHeaders;

    /**
     * 响应内容
     */
    @Column(name = "response_body", columnDefinition = "text")
    private String responseBody;

    /**
     * 执行时间(ms)
     */
    @Column(name = "execution_time")
    private Long executionTime;

    /**
     * 调用结果 1-成功 2-失败
     */
    @Column(name = "result", nullable = false)
    private Integer result;

    /**
     * 错误信息
     */
    @Column(name = "error_msg", columnDefinition = "text")
    private String errorMsg;

    /**
     * 调用者IP
     */
    @Column(name = "ip", length = 50)
    private String ip;

    /**
     * 调用者ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 调用者类型 1-系统用户 2-会员 3-第三方系统
     */
    @Column(name = "user_type")
    private Integer userType;

    /**
     * 来源系统
     */
    @Column(name = "source_system", length = 50)
    private String sourceSystem;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 