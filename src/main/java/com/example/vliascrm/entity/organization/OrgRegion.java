package com.example.vliascrm.entity.organization;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 区域表实体类
 */
@Data
@Entity
@Table(name = "org_region")
@DynamicInsert
@DynamicUpdate
public class OrgRegion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 区域ID
    
    @Column(nullable = false, length = 50)
    private String regionName; // 区域名称
    
    @Column(nullable = false, length = 50)
    private String regionCode; // 区域编码
    
    private Long parentId; // 父区域ID
    
    private Integer level; // 层级
    
    @Column(length = 200)
    private String fullName; // 完整名称
    
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