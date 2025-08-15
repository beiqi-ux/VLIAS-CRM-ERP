package com.example.vliascrm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购入库单实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pur_receipt")
public class PurReceipt {

    /**
     * 入库单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 入库单号
     */
    @Column(name = "receipt_no", unique = true, nullable = false, length = 50)
    private String receiptNo;

    /**
     * 采购单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 采购单号
     */
    @Column(name = "order_no", length = 50)
    private String orderNo;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    /**
     * 入库状态 1-草稿 2-待审核 3-已审核 4-已入库 5-已取消
     */
    @Column(name = "receipt_status")
    private Integer receiptStatus;

    /**
     * 入库类型 1-采购入库 2-退货入库 3-调拨入库 4-其他入库
     */
    @Column(name = "receipt_type")
    private Integer receiptType;

    /**
     * 入库日期
     */
    @Column(name = "receipt_time")
    private LocalDate receiptTime;

    /**
     * 入库总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 入库人
     */
    @Column(name = "receipt_user_id")
    private Long receiptUserId;

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

    // 入库状态常量
    public static final int STATUS_DRAFT = 1;         // 草稿
    public static final int STATUS_PENDING_AUDIT = 2; // 待审核
    public static final int STATUS_AUDITED = 3;       // 已审核
    public static final int STATUS_RECEIVED = 4;      // 已入库
    public static final int STATUS_CANCELLED = 5;     // 已取消

    // 入库类型常量
    public static final int TYPE_PURCHASE = 1;        // 采购入库
    public static final int TYPE_RETURN = 2;          // 退货入库
    public static final int TYPE_TRANSFER = 3;        // 调拨入库
    public static final int TYPE_OTHER = 4;           // 其他入库

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (isDeleted == null) {
            isDeleted = 0;
        }
        if (receiptStatus == null) {
            receiptStatus = STATUS_DRAFT;
        }
        if (receiptType == null) {
            receiptType = TYPE_PURCHASE;
        }
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
} 