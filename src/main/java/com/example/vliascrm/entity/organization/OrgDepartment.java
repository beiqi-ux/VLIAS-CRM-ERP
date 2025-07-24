package com.example.vliascrm.entity.organization;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.util.Date;

/**
 * 部门表实体类
 */
@Data
@Entity
@Table(name = "org_department")
@DynamicInsert
@DynamicUpdate
public class OrgDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 部门ID
    
    @Column(nullable = false)
    private Long orgId; // 所属组织ID
    
    @Column(nullable = false, length = 50)
    private String deptName; // 部门名称
    
    @Column(nullable = false, length = 50)
    private String deptCode; // 部门编码
    
    private Long parentId; // 父部门ID
    
    @Column(length = 50)
    private String leader; // 部门负责人
    
    @Column(length = 20)
    private String phone; // 联系电话
    
    @Column(length = 100)
    private String email; // 邮箱
    
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