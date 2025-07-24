package com.example.vliascrm.entity.finance;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算明细表实体类
 */
@Data
@Entity
@Table(name = "fin_settlement_item")
@DynamicInsert
@DynamicUpdate
public class FinSettlementItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID
    
    @Column(nullable = false)
    private Long settlementId; // 结算ID
    
    @Column(nullable = false, length = 50)
    private String settlementNo; // 结算单号
    
    @Column(nullable = false)
    private Long billId; // 账单ID
    
    @Column(nullable = false, length = 50)
    private String billNo; // 账单编号
    
    @Column(nullable = false)
    private BigDecimal amount; // 结算金额
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
} 