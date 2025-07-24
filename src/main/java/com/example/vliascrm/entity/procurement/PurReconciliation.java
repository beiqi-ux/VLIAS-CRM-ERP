package com.example.vliascrm.entity.procurement;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应商对账实体类
 */
@Data
@Entity
@Table(name = "pur_reconciliation")
@EqualsAndHashCode(callSuper = true)
public class PurReconciliation extends BaseEntity {

    /**
     * 对账单号
     */
    @Column(name = "reconciliation_no", nullable = false, length = 50)
    private String reconciliationNo;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    /**
     * 对账开始日期
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * 对账结束日期
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * 对账总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * 已付金额
     */
    @Column(name = "paid_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /**
     * 未付金额
     */
    @Column(name = "unpaid_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal unpaidAmount = BigDecimal.ZERO;

    /**
     * 状态 1-草稿 2-待确认 3-已确认 4-已结算
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 结算时间
     */
    @Column(name = "settlement_time")
    private LocalDateTime settlementTime;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;
} 