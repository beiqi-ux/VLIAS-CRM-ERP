package com.example.vliascrm.entity.procurement;

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
 * 采购入库实体类
 */
@Data
@Entity
@Table(name = "pur_receipt")
@EqualsAndHashCode(callSuper = true)
public class PurReceipt extends BaseEntity {

    /**
     * 入库单号
     */
    @Column(name = "receipt_no", nullable = false, length = 50)
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
    @Column(name = "receipt_status", columnDefinition = "tinyint default 1")
    private Integer receiptStatus = 1;

    /**
     * 入库类型 1-采购入库 2-退货入库 3-调拨入库 4-其他入库
     */
    @Column(name = "receipt_type", columnDefinition = "tinyint default 1")
    private Integer receiptType = 1;

    /**
     * 入库日期
     */
    @Column(name = "receipt_time")
    private LocalDate receiptTime;

    /**
     * 入库总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal totalAmount = BigDecimal.ZERO;

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
    @Column(name = "remark", columnDefinition = "text")
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