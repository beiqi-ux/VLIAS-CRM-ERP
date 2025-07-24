package com.example.vliascrm.entity.finance;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 财务科目表实体类
 */
@Data
@Entity
@Table(name = "fin_subject")
@DynamicInsert
@DynamicUpdate
public class FinSubject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 科目ID
    
    @Column(nullable = false, length = 50)
    private String subjectName; // 科目名称
    
    @Column(nullable = false, length = 50)
    private String subjectCode; // 科目编码
    
    @Column(nullable = false)
    private Integer subjectType; // 科目类型 1-收入 2-支出
    
    private Long parentId; // 父科目ID
    
    private Integer level; // 层级
    
    private Integer sort; // 排序
    
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