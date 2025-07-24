package com.example.vliascrm.entity.finance;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 佣金结算表实体类
 */
@Data
@Entity
@Table(name = "fin_commission")
@DynamicInsert
@DynamicUpdate
public class FinCommission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    @Column(nullable = false, length = 50)
    private String commissionNo; // 佣金结算单号
    
    @Column(nullable = false)
    private Long distributorId; // 分销员ID
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date periodStart; // 结算周期开始日期
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date periodEnd; // 结算周期结束日期
    
    private BigDecimal totalOrderAmount; // 订单总金额
    
    private BigDecimal commissionAmount; // 佣金金额
    
    private Integer status; // 状态 1-待结算 2-已结算 3-已取消
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date settlementTime; // 结算时间
    
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