package com.example.vliascrm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应商对账明细表
 */
@Entity
@Table(name = "pur_reconciliation_item")
public class PurReconciliationItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "reconciliation_id", nullable = false)
    private Long reconciliationId;
    
    @Column(name = "reconciliation_no", nullable = false, length = 50)
    private String reconciliationNo;
    
    @Column(name = "bill_type", nullable = false)
    private Integer billType; // 1-采购单 2-入库单 3-退货单
    
    @Column(name = "bill_id", nullable = false)
    private Long billId;
    
    @Column(name = "bill_no", nullable = false, length = 50)
    private String billNo;
    
    @Column(name = "bill_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billDate;
    
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "remark", length = 200)
    private String remark;
    
    @Column(name = "create_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    // 构造函数
    public PurReconciliationItem() {}
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getReconciliationId() {
        return reconciliationId;
    }
    
    public void setReconciliationId(Long reconciliationId) {
        this.reconciliationId = reconciliationId;
    }
    
    public String getReconciliationNo() {
        return reconciliationNo;
    }
    
    public void setReconciliationNo(String reconciliationNo) {
        this.reconciliationNo = reconciliationNo;
    }
    
    public Integer getBillType() {
        return billType;
    }
    
    public void setBillType(Integer billType) {
        this.billType = billType;
    }
    
    public Long getBillId() {
        return billId;
    }
    
    public void setBillId(Long billId) {
        this.billId = billId;
    }
    
    public String getBillNo() {
        return billNo;
    }
    
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    
    public LocalDate getBillDate() {
        return billDate;
    }
    
    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
} 