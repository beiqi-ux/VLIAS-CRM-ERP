package com.example.vliascrm.entity.operation;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 用户消息表实体类
 */
@Data
@Entity
@Table(name = "sys_user_message")
@DynamicInsert
@DynamicUpdate
public class SysUserMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    @Column(nullable = false)
    private Long messageId; // 消息ID
    
    @Column(nullable = false)
    private Long userId; // 用户ID
    
    private Integer isRead; // 是否已读 0-未读 1-已读
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date readTime; // 阅读时间
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
} 