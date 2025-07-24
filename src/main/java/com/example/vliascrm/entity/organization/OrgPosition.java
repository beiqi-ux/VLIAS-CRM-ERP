package com.example.vliascrm.entity.organization;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 岗位表实体类
 */
@Data
@Entity
@Table(name = "org_position")
@DynamicInsert
@DynamicUpdate
public class OrgPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 岗位ID
    
    @Column(nullable = false)
    private Long orgId; // 所属组织ID
    
    @Column(nullable = false, length = 50)
    private String positionName; // 岗位名称
    
    @Column(nullable = false, length = 50)
    private String positionCode; // 岗位编码
    
    private Long deptId; // 所属部门ID
    
    private Integer status; // 状态 0-禁用 1-正常
    
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