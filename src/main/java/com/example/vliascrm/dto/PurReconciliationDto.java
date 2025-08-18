package com.example.vliascrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 供应商对账DTO
 */
public class PurReconciliationDto {
    
    private Long id;
    private String reconciliationNo;
    private Long supplierId;
    private String supplierName; // 供应商名称，用于显示
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private BigDecimal unpaidAmount = BigDecimal.ZERO;
    private Integer status = 1; // 1-草稿 2-待确认 3-已确认 4-已结算
    private String statusName; // 状态名称，用于显示
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime settlementTime;
    
    private String remark;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    
    private Long createBy;
    private Long updateBy;
    private String createByName; // 创建人名称，用于显示
    private String updateByName; // 更新人名称，用于显示
    private Integer isDeleted = 0;
    
    // 对账明细列表
    private List<PurReconciliationItemDto> items;
    
    // 构造函数
    public PurReconciliationDto() {}
    
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
    
    public String getSupplierName() {
        return supplierName;
    }
    
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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
    
    public String getStatusName() {
        return statusName;
    }
    
    public void setStatusName(String statusName) {
        this.statusName = statusName;
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
    
    public String getCreateByName() {
        return createByName;
    }
    
    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }
    
    public String getUpdateByName() {
        return updateByName;
    }
    
    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }
    
    public Integer getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public List<PurReconciliationItemDto> getItems() {
        return items;
    }
    
    public void setItems(List<PurReconciliationItemDto> items) {
        this.items = items;
    }
} 