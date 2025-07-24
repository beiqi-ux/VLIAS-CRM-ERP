package com.example.vliascrm.entity.support;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 附件表实体类
 */
@Data
@Entity
@Table(name = "sys_attachment")
@DynamicInsert
@DynamicUpdate
public class SysAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 附件ID
    
    @Column(nullable = false, length = 50)
    private String moduleType; // 模块类型
    
    @Column(nullable = false)
    private Long moduleId; // 模块ID
    
    @Column(nullable = false)
    private Long fileId; // 文件ID
    
    @Column(nullable = false, length = 500)
    private String fileUrl; // 文件URL
    
    @Column(nullable = false, length = 200)
    private String fileName; // 文件名称
    
    @Column(nullable = false)
    private Long fileSize; // 文件大小
    
    @Column(length = 50)
    private String fileType; // 文件类型
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
    
    private Long createBy; // 创建人
    
    private Integer isDeleted; // 是否删除 0-否 1-是
} 