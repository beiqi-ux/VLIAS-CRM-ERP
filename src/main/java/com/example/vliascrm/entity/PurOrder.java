package com.example.vliascrm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购订单实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pur_order")
public class PurOrder {

    /**
     * 采购单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 采购单号
     */
    @Column(name = "order_no", unique = true, nullable = false, length = 50)
    private String orderNo;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    /**
     * 订单状态 1-草稿 2-待审核 3-已审核 4-已下单 5-部分入库 6-已完成 7-已取消
     */
    @Column(name = "order_status")
    private Integer orderStatus;

    /**
     * 入库仓库ID
     */
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 订单总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 支付状态 0-未支付 1-部分支付 2-已支付
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    /**
     * 已付金额
     */
    @Column(name = "paid_amount", precision = 10, scale = 2)
    private BigDecimal paidAmount;

    /**
     * 预计到货日期
     */
    @Column(name = "expected_time")
    private LocalDate expectedTime;

    /**
     * 发货状态 0-未发货 1-部分发货 2-已发货
     */
    @Column(name = "delivery_status")
    private Integer deliveryStatus;

    /**
     * 入库状态 0-未入库 1-部分入库 2-已入库
     */
    @Column(name = "receipt_status")
    private Integer receiptStatus;

    /**
     * 联系人
     */
    @Column(name = "contact", length = 50)
    private String contact;

    /**
     * 联系电话
     */
    @Column(name = "mobile", length = 20)
    private String mobile;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /**
     * 制单人ID
     */
    @Column(name = "creator_id")
    private Long creatorId;

    /**
     * 审核人ID
     */
    @Column(name = "audit_id")
    private Long auditId;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private LocalDateTime auditTime;

    /**
     * 审核备注
     */
    @Column(name = "audit_remark", length = 200)
    private String auditRemark;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @Column(name = "create_by", updatable = false)
    private Long createBy;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;

    /**
     * 是否删除 0-否 1-是
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

    // 订单状态常量
    public static final int STATUS_DRAFT = 1;          // 草稿
    public static final int STATUS_PENDING_AUDIT = 2;  // 待审核
    public static final int STATUS_AUDITED = 3;        // 已审核
    public static final int STATUS_ORDERED = 4;        // 已下单
    public static final int STATUS_PARTIAL_RECEIPT = 5; // 部分入库
    public static final int STATUS_COMPLETED = 6;      // 已完成
    public static final int STATUS_CANCELLED = 7;      // 已取消

    // 支付状态常量
    public static final int PAY_STATUS_UNPAID = 0;     // 未支付
    public static final int PAY_STATUS_PARTIAL = 1;    // 部分支付
    public static final int PAY_STATUS_PAID = 2;       // 已支付

    // 发货状态常量
    public static final int DELIVERY_STATUS_NONE = 0;    // 未发货
    public static final int DELIVERY_STATUS_PARTIAL = 1; // 部分发货
    public static final int DELIVERY_STATUS_SENT = 2;    // 已发货

    // 入库状态常量
    public static final int RECEIPT_STATUS_NONE = 0;     // 未入库
    public static final int RECEIPT_STATUS_PARTIAL = 1;  // 部分入库
    public static final int RECEIPT_STATUS_RECEIVED = 2; // 已入库

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (isDeleted == null) {
            isDeleted = 0;
        }
        if (orderStatus == null) {
            orderStatus = STATUS_DRAFT;
        }
        if (payStatus == null) {
            payStatus = PAY_STATUS_UNPAID;
        }
        if (deliveryStatus == null) {
            deliveryStatus = DELIVERY_STATUS_NONE;
        }
        if (receiptStatus == null) {
            receiptStatus = RECEIPT_STATUS_NONE;
        }
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
        if (paidAmount == null) {
            paidAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
} 