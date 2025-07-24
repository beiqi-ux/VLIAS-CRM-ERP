package com.example.vliascrm.entity.support;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 短信记录表实体类
 */
@Data
@Entity
@Table(name = "sys_sms_record")
@DynamicInsert
@DynamicUpdate
public class SysSmsRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    @Column(nullable = false, length = 20)
    private String mobile; // 手机号
    
    @Column(nullable = false)
    private Integer smsType; // 短信类型 1-验证码 2-通知 3-营销
    
    @Column(nullable = false, length = 50)
    private String templateCode; // 模板编码
    
    @Column(length = 500)
    private String templateParam; // 模板参数
    
    @Column(length = 500)
    private String content; // 发送内容
    
    private Integer sendStatus; // 发送状态 0-未发送 1-发送中 2-发送成功 3-发送失败
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendTime; // 发送时间
    
    @Column(length = 50)
    private String provider; // 服务商
    
    @Column(length = 500)
    private String errorMsg; // 错误消息
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
} 