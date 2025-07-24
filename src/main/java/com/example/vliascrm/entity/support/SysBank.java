package com.example.vliascrm.entity.support;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 银行表实体类
 */
@Data
@Entity
@Table(name = "sys_bank")
@DynamicInsert
@DynamicUpdate
public class SysBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 银行ID
    
    @Column(nullable = false, length = 20)
    private String bankCode; // 银行编码
    
    @Column(nullable = false, length = 50)
    private String bankName; // 银行名称
    
    @Column(length = 255)
    private String logo; // 银行logo
    
    private Integer status; // 状态 0-禁用 1-正常
    
    private Integer sort; // 排序
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime; // 更新时间
    
    private Long createBy; // 创建人
    
    private Long updateBy; // 更新人
    
    private Integer isDeleted; // 是否删除 0-否 1-是
} 