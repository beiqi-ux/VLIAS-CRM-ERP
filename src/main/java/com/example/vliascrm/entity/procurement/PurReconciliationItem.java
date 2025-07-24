package com.example.vliascrm.entity.procurement;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应商对账明细实体类
 */
@Data
@Entity
@Table(name = "pur_reconciliation_item")
@EntityListeners(AuditingEntityListener.class)
public class PurReconciliationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 对账单ID
     */
    @Column(name = "reconciliation_id", nullable = false)
    private Long reconciliationId;

    /**
     * 对账单号
     */
    @Column(name = "reconciliation_no", nullable = false, length = 50)
    private String reconciliationNo;

    /**
     * 单据类型 1-采购单 2-入库单 3-退货单
     */
    @Column(name = "bill_type", nullable = false)
    private Integer billType;

    /**
     * 单据ID
     */
    @Column(name = "bill_id", nullable = false)
    private Long billId;

    /**
     * 单据编号
     */
    @Column(name = "bill_no", nullable = false, length = 50)
    private String billNo;

    /**
     * 单据日期
     */
    @Column(name = "bill_date")
    private LocalDate billDate;

    /**
     * 金额
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 