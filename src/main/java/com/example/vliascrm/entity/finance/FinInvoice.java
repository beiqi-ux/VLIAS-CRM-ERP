package com.example.vliascrm.entity.finance;

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
 * 发票实体类
 */
@Data
@Entity
@Table(name = "fin_invoice")
@EqualsAndHashCode(callSuper = true)
public class FinInvoice extends BaseEntity {

    /**
     * 发票号码
     */
    @Column(name = "invoice_no", nullable = false, length = 50)
    private String invoiceNo;

    /**
     * 发票代码
     */
    @Column(name = "invoice_code", length = 50)
    private String invoiceCode;

    /**
     * 发票类型 1-普通发票 2-增值税专用发票 3-电子发票
     */
    @Column(name = "invoice_type", nullable = false)
    private Integer invoiceType;

    /**
     * 发票方向 1-销项发票 2-进项发票
     */
    @Column(name = "direction", nullable = false)
    private Integer direction;

    /**
     * 发票日期
     */
    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    /**
     * 发票金额
     */
    @Column(name = "invoice_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal invoiceAmount;

    /**
     * 税额
     */
    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;

    /**
     * 价税合计
     */
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 开票方名称
     */
    @Column(name = "drawer_name", nullable = false, length = 100)
    private String drawerName;

    /**
     * 开票方税号
     */
    @Column(name = "drawer_tax_no", length = 50)
    private String drawerTaxNo;

    /**
     * 开票方地址电话
     */
    @Column(name = "drawer_address_phone", length = 200)
    private String drawerAddressPhone;

    /**
     * 开票方开户行及账号
     */
    @Column(name = "drawer_bank_account", length = 200)
    private String drawerBankAccount;

    /**
     * 收票方名称
     */
    @Column(name = "payee_name", nullable = false, length = 100)
    private String payeeName;

    /**
     * 收票方税号
     */
    @Column(name = "payee_tax_no", length = 50)
    private String payeeTaxNo;

    /**
     * 收票方地址电话
     */
    @Column(name = "payee_address_phone", length = 200)
    private String payeeAddressPhone;

    /**
     * 收票方开户行及账号
     */
    @Column(name = "payee_bank_account", length = 200)
    private String payeeBankAccount;

    /**
     * 关联业务单号
     */
    @Column(name = "business_no", length = 50)
    private String businessNo;

    /**
     * 关联业务类型 1-订单 2-采购单 3-其他
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 关联业务ID
     */
    @Column(name = "business_id")
    private Long businessId;

    /**
     * 状态 1-正常 2-作废 3-红冲
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

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