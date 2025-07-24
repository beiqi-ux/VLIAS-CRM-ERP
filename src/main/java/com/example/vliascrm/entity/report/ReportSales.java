package com.example.vliascrm.entity.report;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 销售报表表实体类
 */
@Data
@Entity
@Table(name = "report_sales")
@DynamicInsert
@DynamicUpdate
public class ReportSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date reportDate; // 报表日期
    
    private Integer orderCount; // 订单数量
    
    private BigDecimal orderAmount; // 订单金额
    
    private Integer paidOrderCount; // 已支付订单数
    
    private BigDecimal paidOrderAmount; // 已支付金额
    
    private Integer refundOrderCount; // 退款订单数
    
    private BigDecimal refundAmount; // 退款金额
    
    private Integer goodsCount; // 商品销量
    
    private Integer newMemberCount; // 新增会员数
    
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