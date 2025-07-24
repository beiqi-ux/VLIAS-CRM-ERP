package com.example.vliascrm.entity.operation;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 操作日志实体类
 */
@Data
@Entity
@Table(name = "op_log")
@EntityListeners(AuditingEntityListener.class)
public class OpLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 操作用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 操作用户名
     */
    @Column(name = "user_name", length = 50)
    private String userName;

    /**
     * 操作模块
     */
    @Column(name = "module", length = 50)
    private String module;

    /**
     * 操作类型 1-新增 2-修改 3-删除 4-查询 5-登录 6-退出
     */
    @Column(name = "operation_type")
    private Integer operationType;

    /**
     * 操作描述
     */
    @Column(name = "operation_desc", columnDefinition = "text")
    private String operationDesc;

    /**
     * 请求URL
     */
    @Column(name = "request_url", length = 255)
    private String requestUrl;

    /**
     * 请求方法
     */
    @Column(name = "request_method", length = 20)
    private String requestMethod;

    /**
     * 请求参数
     */
    @Column(name = "request_params", columnDefinition = "text")
    private String requestParams;

    /**
     * 请求IP
     */
    @Column(name = "ip", length = 50)
    private String ip;

    /**
     * 操作状态 0-失败 1-成功
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 错误信息
     */
    @Column(name = "error_msg", columnDefinition = "text")
    private String errorMsg;

    /**
     * 执行时长(毫秒)
     */
    @Column(name = "execution_time")
    private Long executionTime;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 