package com.example.vliascrm.entity.crm;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 客户表实体类
 */
@Data
@Entity
@Table(name = "crm_customer")
@DynamicInsert
@DynamicUpdate
public class CrmCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 客户ID
    
    @Column(nullable = false, length = 100)
    private String customerName; // 客户名称
    
    @Column(length = 50)
    private String customerCode; // 客户编码
    
    private Integer customerType; // 客户类型 1-个人 2-企业
    
    private Long industryId; // 所属行业ID
    
    private Long sourceId; // 客户来源ID
    
    private Long levelId; // 客户等级ID
    
    private Integer status; // 状态 1-潜在 2-意向 3-成交 4-流失
    
    private Integer score; // 客户评分
    
    private Long ownerUserId; // 负责人ID
    
    private Integer isPublic; // 是否公海 0-否 1-是
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextFollowTime; // 下次跟进时间
    
    @Column(length = 20)
    private String mobile; // 联系电话
    
    @Column(length = 100)
    private String email; // 邮箱
    
    @Column(length = 200)
    private String address; // 地址
    
    @Column(length = 50)
    private String province; // 省份
    
    @Column(length = 50)
    private String city; // 城市
    
    @Column(length = 50)
    private String district; // 区县
    
    @Lob
    private String remark; // 备注
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime; // 更新时间
    
    private Long createBy; // 创建人
    
    private Long updateBy; // 更新人
    
    private Integer isDeleted; // 是否删除 0-否 1-是
} 