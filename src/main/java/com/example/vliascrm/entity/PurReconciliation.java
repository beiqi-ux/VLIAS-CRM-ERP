package com.example.vliascrm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 供应商对账表
 */
@Entity
@Table(name = "pur_reconciliation")
public class PurReconciliation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "reconciliation_no", nullable = false, length = 50, unique = true)
    private String reconciliationNo;
    
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;
    
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    
    @Column(name = "paid_amount", precision = 10, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;
    
    @Column(name = "unpaid_amount", precision = 10, scale = 2)
    private BigDecimal unpaidAmount = BigDecimal.ZERO;
    
    @Column(name = "status")
    private Integer status = 1; // 1-草稿 2-待确认 3-已确认 4-已结算
    
    @Column(name = "settlement_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementTime;
    
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
    
    @Column(name = "create_time", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @Column(name = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    @Column(name = "create_by")
    private Long createBy;
    
    @Column(name = "update_by")
    private Long updateBy;
    
    @Column(name = "is_deleted")
    private Integer isDeleted = 0;
    
    // 构造函数
    public PurReconciliation() {}
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getReconciliationNo() {
        return reconciliationNo;
    }
    
    public void setReconciliationNo(String reconciliationNo) {
        this.reconciliationNo = reconciliationNo;
    }
    
    public Long getSupplierId() {
        return supplierId;
    }
    
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public BigDecimal getPaidAmount() {
        return paidAmount;
    }
    
    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }
    
    public BigDecimal getUnpaidAmount() {
        return unpaidAmount;
    }
    
    public void setUnpaidAmount(BigDecimal unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getSettlementTime() {
        return settlementTime;
    }
    
    public void setSettlementTime(LocalDateTime settlementTime) {
        this.settlementTime = settlementTime;
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
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public Long getCreateBy() {
        return createBy;
    }
    
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    
    public Long getUpdateBy() {
        return updateBy;
    }
    
    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }
    
    public Integer getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
} 