package com.example.vliascrm.entity.operation;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 系统参数表实体类
 */
@Data
@Entity
@Table(name = "sys_param")
@DynamicInsert
@DynamicUpdate
public class SysParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 参数ID
    
    @Column(nullable = false, length = 100)
    private String paramKey; // 参数键
    
    @Column(nullable = false, length = 500)
    private String paramValue; // 参数值
    
    private Integer paramType; // 参数类型 1-系统参数 2-业务参数
    
    @Column(length = 200)
    private String description; // 参数描述
    
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