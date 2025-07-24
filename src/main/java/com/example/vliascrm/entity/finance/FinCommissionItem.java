package com.example.vliascrm.entity.finance;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 佣金结算明细表实体类
 */
@Data
@Entity
@Table(name = "fin_commission_item")
@DynamicInsert
@DynamicUpdate
public class FinCommissionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    @Column(nullable = false)
    private Long commissionId; // 佣金结算单ID
    
    @Column(nullable = false, length = 50)
    private String commissionNo; // 佣金结算单号
    
    @Column(nullable = false)
    private Long orderId; // 订单ID
    
    @Column(nullable = false, length = 50)
    private String orderNo; // 订单编号
    
    @Column(nullable = false)
    private BigDecimal orderAmount; // 订单金额
    
    @Column(nullable = false)
    private BigDecimal commissionRate; // 佣金比例
    
    @Column(nullable = false)
    private BigDecimal commissionAmount; // 佣金金额
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime; // 订单时间
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
} 