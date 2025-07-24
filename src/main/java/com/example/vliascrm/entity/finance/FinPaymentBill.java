package com.example.vliascrm.entity.finance;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 付款单实体类
 */
@Data
@Entity
@Table(name = "fin_payment_bill")
@EqualsAndHashCode(callSuper = true)
public class FinPaymentBill extends BaseEntity {

    /**
     * 付款单号
     */
    @Column(name = "payment_no", nullable = false, length = 50)
    private String paymentNo;

    /**
     * 付款类型 1-采购付款 2-预付款 3-其他付款
     */
    @Column(name = "payment_type", nullable = false)
    private Integer paymentType;

    /**
     * 付款方式 1-现金 2-微信 3-支付宝 4-银行卡 5-其他
     */
    @Column(name = "payment_method", nullable = false)
    private Integer paymentMethod;

    /**
     * 付款金额
     */
    @Column(name = "payment_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal paymentAmount;

    /**
     * 付款账户
     */
    @Column(name = "payment_account", length = 100)
    private String paymentAccount;

    /**
     * 交易流水号
     */
    @Column(name = "transaction_no", length = 100)
    private String transactionNo;

    /**
     * 收款人
     */
    @Column(name = "payee", length = 50)
    private String payee;

    /**
     * 收款账户
     */
    @Column(name = "payee_account", length = 100)
    private String payeeAccount;

    /**
     * 付款时间
     */
    @Column(name = "payment_time", nullable = false)
    private LocalDateTime paymentTime;

    /**
     * 关联业务单号
     */
    @Column(name = "business_no", length = 50)
    private String businessNo;

    /**
     * 关联业务类型 1-采购单 2-预付款 3-其他
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 关联业务ID
     */
    @Column(name = "business_id")
    private Long businessId;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id")
    private Long supplierId;

    /**
     * 付款人
     */
    @Column(name = "payment_user", length = 50)
    private String paymentUser;

    /**
     * 状态 1-待审核 2-已审核 3-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
    private String remark;

    /**
     * 审核人
     */
    @Column(name = "audit_user")
    private Long auditUser;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private LocalDateTime auditTime;

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