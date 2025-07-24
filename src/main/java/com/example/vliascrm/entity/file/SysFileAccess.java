package com.example.vliascrm.entity.file;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 文件访问记录实体类
 */
@Data
@Entity
@Table(name = "sys_file_access")
@EntityListeners(AuditingEntityListener.class)
public class SysFileAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文件ID
     */
    @Column(name = "file_id", nullable = false)
    private Long fileId;

    /**
     * 访问者ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 访问者类型 1-系统用户 2-会员
     */
    @Column(name = "user_type", columnDefinition = "tinyint default 1")
    private Integer userType = 1;

    /**
     * 访问者IP
     */
    @Column(name = "ip", length = 50)
    private String ip;

    /**
     * 访问者设备
     */
    @Column(name = "device", length = 100)
    private String device;

    /**
     * 访问者浏览器
     */
    @Column(name = "browser", length = 100)
    private String browser;

    /**
     * 访问者操作系统
     */
    @Column(name = "os", length = 50)
    private String os;

    /**
     * 访问类型 1-查看 2-下载 3-预览
     */
    @Column(name = "access_type", nullable = false)
    private Integer accessType;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 