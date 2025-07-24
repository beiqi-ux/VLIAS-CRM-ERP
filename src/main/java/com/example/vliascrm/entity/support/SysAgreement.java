package com.example.vliascrm.entity.support;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 协议表实体类
 */
@Data
@Entity
@Table(name = "sys_agreement")
@DynamicInsert
@DynamicUpdate
public class SysAgreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 协议ID
    
    @Column(nullable = false, length = 100)
    private String agreementName; // 协议名称
    
    @Column(nullable = false, length = 50)
    private String agreementCode; // 协议编码
    
    @Lob
    @Column(nullable = false)
    private String content; // 协议内容
    
    @Column(nullable = false, length = 20)
    private String version; // 协议版本
    
    private Integer status; // 状态 0-禁用 1-正常
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime; // 更新时间
    
    private Long createBy; // 创建人
    
    private Long updateBy; // 更新人
    
    private Integer isDeleted; // 是否删除 0-否 1-是
} 