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
 * 采购订单实体类
 */
@Data
@Entity
@Table(name = "pur_order")
@EqualsAndHashCode(callSuper = true)
public class PurOrder extends BaseEntity {

    /**
     * 采购单号
     */
    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    /**
     * 供应商ID
     */
    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    /**
     * 订单状态 1-草稿 2-待审核 3-已审核 4-已下单 5-部分入库 6-已完成 7-已取消
     */
    @Column(name = "order_status", columnDefinition = "tinyint default 1")
    private Integer orderStatus = 1;

    /**
     * 入库仓库ID
     */
    @Column(name = "warehouse_id")
    private Long warehouseId;

    /**
     * 订单总金额
     */
    @Column(name = "total_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * 支付状态 0-未支付 1-部分支付 2-已支付
     */
    @Column(name = "pay_status", columnDefinition = "tinyint default 0")
    private Integer payStatus = 0;

    /**
     * 已付金额
     */
    @Column(name = "paid_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /**
     * 预计到货日期
     */
    @Column(name = "expected_time")
    private LocalDate expectedTime;

    /**
     * 发货状态 0-未发货 1-部分发货 2-已发货
     */
    @Column(name = "delivery_status", columnDefinition = "tinyint default 0")
    private Integer deliveryStatus = 0;

    /**
     * 入库状态 0-未入库 1-部分入库 2-已入库
     */
    @Column(name = "receipt_status", columnDefinition = "tinyint default 0")
    private Integer receiptStatus = 0;

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