package com.example.vliascrm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购退货实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pur_return")
public class PurReturn {

    /**
     * 退货单ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 退货单号
     */
    @Column(name = "return_no", unique = true, nullable = false, length = 50)
    private String returnNo;

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
     * 关联入库单ID
     */
    @Column(name = "receipt_id")
    private Long receiptId;

    /**
     * 关联入库单号
     */
    @Column(name = "receipt_no", length = 50)
    private String receiptNo;

    /**
     * 退货状态 1-草稿 2-待审核 3-已审核 4-已退货 5-已取消
     */
    @Column(name = "return_status")
    private Integer returnStatus;

    /**
     * 退货日期
     */
    @Column(name = "return_time")
    private LocalDate returnTime;

    /**
     * 退货总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 退货原因类型
     */
    @Column(name = "reason_type")
    private Integer reasonType;

    /**
     * 退货原因
     */
    @Column(name = "reason", length = 200)
    private String reason;

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

    // 退货状态常量
    public static final int STATUS_DRAFT = 1;         // 草稿
    public static final int STATUS_PENDING_AUDIT = 2; // 待审核
    public static final int STATUS_AUDITED = 3;       // 已审核
    public static final int STATUS_RETURNED = 4;      // 已退货
    public static final int STATUS_CANCELLED = 5;     // 已取消

    // 退货原因类型常量
    public static final int REASON_TYPE_QUALITY = 1;  // 质量问题
    public static final int REASON_TYPE_DAMAGE = 2;   // 运输损坏
    public static final int REASON_TYPE_WRONG = 3;    // 发货错误
    public static final int REASON_TYPE_OVERDUE = 4;  // 过期商品
    public static final int REASON_TYPE_OTHER = 9;    // 其他

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (isDeleted == null) {
            isDeleted = 0;
        }
        if (returnStatus == null) {
            returnStatus = STATUS_DRAFT;
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