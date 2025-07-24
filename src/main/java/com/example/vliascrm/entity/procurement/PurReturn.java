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
 * 采购退货实体类
 */
@Data
@Entity
@Table(name = "pur_return")
@EqualsAndHashCode(callSuper = true)
public class PurReturn extends BaseEntity {

    /**
     * 退货单号
     */
    @Column(name = "return_no", nullable = false, length = 50)
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
    @Column(name = "return_status", columnDefinition = "tinyint default 1")
    private Integer returnStatus = 1;

    /**
     * 退货日期
     */
    @Column(name = "return_time")
    private LocalDate returnTime;

    /**
     * 退货总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal totalAmount = BigDecimal.ZERO;

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