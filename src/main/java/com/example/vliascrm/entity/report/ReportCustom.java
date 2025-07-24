package com.example.vliascrm.entity.report;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 自定义报表表实体类
 */
@Data
@Entity
@Table(name = "report_custom")
@DynamicInsert
@DynamicUpdate
public class ReportCustom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    @Column(nullable = false, length = 100)
    private String reportName; // 报表名称
    
    @Column(nullable = false, length = 50)
    private String reportCode; // 报表编码
    
    @Column(nullable = false)
    private Integer reportType; // 报表类型 1-表格 2-图表 3-混合
    
    @Column(nullable = false, length = 100)
    private String dataSource; // 数据源
    
    @Lob
    private String sqlContent; // SQL语句
    
    @Lob
    private String params; // 参数配置JSON
    
    @Lob
    private String fields; // 字段配置JSON
    
    @Lob
    private String chartConfig; // 图表配置JSON
    
    private Integer status; // 状态 0-禁用 1-正常
    
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