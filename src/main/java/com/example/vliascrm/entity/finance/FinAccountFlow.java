package com.example.vliascrm.entity.finance;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账户流水实体类
 */
@Data
@Entity
@Table(name = "fin_account_flow")
@EntityListeners(AuditingEntityListener.class)
public class FinAccountFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 账户ID
     */
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    /**
     * 流水类型 1-收入 2-支出 3-内部转账
     */
    @Column(name = "flow_type", nullable = false)
    private Integer flowType;

    /**
     * 交易金额
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * 交易前余额
     */
    @Column(name = "before_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal beforeBalance;

    /**
     * 交易后余额
     */
    @Column(name = "after_balance", nullable = false, precision = 10, scale = 2)
    private BigDecimal afterBalance;

    /**
     * 交易时间
     */
    @Column(name = "transaction_time", nullable = false)
    private LocalDateTime transactionTime;

    /**
     * 交易流水号
     */
    @Column(name = "transaction_no", length = 100)
    private String transactionNo;

    /**
     * 关联业务单号
     */
    @Column(name = "business_no", length = 50)
    private String businessNo;

    /**
     * 关联业务类型 1-收款单 2-付款单 3-转账 4-其他
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 关联业务ID
     */
    @Column(name = "business_id")
    private Long businessId;

    /**
     * 摘要
     */
    @Column(name = "summary", length = 200)
    private String summary;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
    private String remark;

    /**
     * 操作人
     */
    @Column(name = "operator")
    private Long operator;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 