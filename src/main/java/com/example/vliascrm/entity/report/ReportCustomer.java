package com.example.vliascrm.entity.report;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 客户分析表实体类
 */
@Data
@Entity
@Table(name = "report_customer")
@DynamicInsert
@DynamicUpdate
public class ReportCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date reportDate; // 报表日期
    
    private Integer newCustomerCount; // 新增客户数
    
    private Integer totalCustomerCount; // 累计客户数
    
    private Integer activeCustomerCount; // 活跃客户数
    
    private Integer transactionCustomerCount; // 成交客户数
    
    private Integer lostCustomerCount; // 流失客户数
    
    private BigDecimal transactionRate; // 成交率
    
    private BigDecimal retentionRate; // 留存率
    
    private BigDecimal customerUnitPrice; // 客单价
    
    private BigDecimal repurchaseRate; // 复购率
    
    @Column(nullable = false)
    private Integer statsType; // 统计类型 1-日 2-周 3-月 4-年
    
    @Column(length = 50)
    private String dimension; // 统计维度
    
    private Long dimensionId; // 维度ID
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime; // 更新时间
} 