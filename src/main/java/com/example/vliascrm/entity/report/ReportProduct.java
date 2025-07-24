package com.example.vliascrm.entity.report;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品分析表实体类
 */
@Data
@Entity
@Table(name = "report_product")
@DynamicInsert
@DynamicUpdate
public class ReportProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date reportDate; // 报表日期
    
    @Column(nullable = false)
    private Long goodsId; // 商品ID
    
    private Long skuId; // SKU ID
    
    private Long categoryId; // 分类ID
    
    private Integer salesCount; // 销量
    
    private BigDecimal salesAmount; // 销售额
    
    private Integer refundCount; // 退款数量
    
    private BigDecimal refundAmount; // 退款金额
    
    private Integer viewCount; // 浏览次数
    
    private Integer favoriteCount; // 收藏次数
    
    private Integer cartCount; // 加购次数
    
    private BigDecimal conversionRate; // 转化率
    
    @Column(nullable = false)
    private Integer statsType; // 统计类型 1-日 2-周 3-月 4-年
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime; // 更新时间
} 