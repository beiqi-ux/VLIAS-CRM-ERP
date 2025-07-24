package com.example.vliascrm.entity.finance;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算表实体类
 */
@Data
@Entity
@Table(name = "fin_settlement")
@DynamicInsert
@DynamicUpdate
public class FinSettlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 结算ID
    
    @Column(nullable = false, length = 50)
    private String settlementNo; // 结算单号
    
    @Column(nullable = false)
    private Integer settlementType; // 结算类型 1-收款 2-付款
    
    @Column(nullable = false)
    private BigDecimal amount; // 金额
    
    @Column(nullable = false)
    private Integer settlementMethod; // 结算方式 1-现金 2-银行转账 3-微信 4-支付宝 5-其他
    
    private Long accountId; // 账户ID
    
    @Column(length = 100)
    private String transactionNo; // 交易流水号
    
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date settlementDate; // 结算日期
    
    private Integer targetType; // 对象类型 1-客户 2-供应商 3-员工 4-其他
    
    private Long targetId; // 对象ID
    
    @Column(length = 100)
    private String targetName; // 对象名称
    
    private Integer status; // 状态 1-待确认 2-已确认 3-已取消
    
    @Lob
    private String remark; // 备注
    
    private Long confirmUserId; // 确认人
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmTime; // 确认时间
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createTime; // 创建时间
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime; // 更新时间
    
    private Long createBy; // 创建人
    
    private Long updateBy; // 更新人
    
    private Integer isDeleted; // 是否删除 0-否 1-是
} 