package com.example.vliascrm.entity.finance;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 发票明细实体类
 */
@Data
@Entity
@Table(name = "fin_invoice_item")
@EntityListeners(AuditingEntityListener.class)
public class FinInvoiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发票ID
     */
    @Column(name = "invoice_id", nullable = false)
    private Long invoiceId;

    /**
     * 发票号码
     */
    @Column(name = "invoice_no", nullable = false, length = 50)
    private String invoiceNo;

    /**
     * 商品名称
     */
    @Column(name = "goods_name", nullable = false, length = 100)
    private String goodsName;

    /**
     * 规格型号
     */
    @Column(name = "specification", length = 100)
    private String specification;

    /**
     * 单位
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * 数量
     */
    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    /**
     * 单价
     */
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 金额
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * 税率
     */
    @Column(name = "tax_rate", precision = 10, scale = 2)
    private BigDecimal taxRate;

    /**
     * 税额
     */
    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 