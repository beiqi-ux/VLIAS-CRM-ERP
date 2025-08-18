package com.example.vliascrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PurReconciliationItemDto {
    
    private Long id;
    
    /**
     * 对账单ID
     */
    private Long reconciliationId;
    
    /**
     * 单据类型：1-采购单 2-入库单 3-退货单
     */
    private Integer billType;
    
    /**
     * 单据类型名称
     */
    private String billTypeName;
    
    /**
     * 单据ID
     */
    private Long billId;
    
    /**
     * 单据号（采购单号）
     */
    private String billNo;
    
    /**
     * 单据号（前端使用orderNo）
     */
    public String getOrderNo() {
        return this.billNo;
    }
    
    public void setOrderNo(String orderNo) {
        this.billNo = orderNo;
    }
    
    /**
     * 单据日期（采购日期）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate billDate;
    
    /**
     * 单据日期（前端使用orderDate）
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getOrderDate() {
        return this.billDate;
    }
    
    public void setOrderDate(LocalDate orderDate) {
        this.billDate = orderDate;
    }
    
    /**
     * 商品编码
     */
    private String goodsCode;
    
    /**
     * 商品名称
     */
    private String goodsName;
    
    /**
     * 规格型号
     */
    private String specification;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 数量
     */
    private BigDecimal quantity;
    
    /**
     * 单价
     */
    private BigDecimal unitPrice;
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 总金额（前端使用totalAmount）
     */
    public BigDecimal getTotalAmount() {
        return this.amount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.amount = totalAmount;
    }
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate createTime;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDate updateTime;
} 