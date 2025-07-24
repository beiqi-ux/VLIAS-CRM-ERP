package com.example.vliascrm.entity.operation;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 登录日志实体类
 */
@Data
@Entity
@Table(name = "op_login_log")
@EntityListeners(AuditingEntityListener.class)
public class OpLoginLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 用户名
     */
    @Column(name = "user_name", length = 50)
    private String userName;

    /**
     * 登录IP
     */
    @Column(name = "ip", length = 50)
    private String ip;

    /**
     * 登录地点
     */
    @Column(name = "location", length = 100)
    private String location;

    /**
     * 浏览器类型
     */
    @Column(name = "browser", length = 50)
    private String browser;

    /**
     * 操作系统
     */
    @Column(name = "os", length = 50)
    private String os;

    /**
     * 登录状态 0-失败 1-成功
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 登录类型 1-账号密码 2-手机验证码 3-微信 4-支付宝
     */
    @Column(name = "login_type")
    private Integer loginType;

    /**
     * 设备类型 1-PC 2-安卓 3-iOS 4-小程序
     */
    @Column(name = "device_type")
    private Integer deviceType;

    /**
     * 设备标识
     */
    @Column(name = "device_id", length = 100)
    private String deviceId;

    /**
     * 消息提示
     */
    @Column(name = "msg", length = 255)
    private String msg;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 