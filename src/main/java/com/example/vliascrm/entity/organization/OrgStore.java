package com.example.vliascrm.entity.organization;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 门店表实体类
 */
@Data
@Entity
@Table(name = "org_store")
@DynamicInsert
@DynamicUpdate
public class OrgStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 门店ID
    
    @Column(nullable = false)
    private Long orgId; // 所属组织ID
    
    @Column(nullable = false, length = 100)
    private String storeName; // 门店名称
    
    @Column(nullable = false, length = 50)
    private String storeCode; // 门店编码
    
    @Column(length = 50)
    private String province; // 省份
    
    @Column(length = 50)
    private String city; // 城市
    
    @Column(length = 50)
    private String district; // 区县
    
    @Column(length = 200)
    private String address; // 详细地址
    
    private BigDecimal latitude; // 纬度
    
    private BigDecimal longitude; // 经度
    
    @Column(length = 50)
    private String contact; // 联系人
    
    @Column(length = 20)
    private String mobile; // 联系电话
    
    @Column(length = 100)
    private String businessHours; // 营业时间
    
    private BigDecimal storeArea; // 门店面积
    
    @Column(length = 255)
    private String image; // 门店图片
    
    private Integer status; // 状态 0-停业 1-营业
    
    private Integer sort; // 排序
    
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