package com.example.vliascrm.entity.order;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单退换货表实体类
 */
@Data
@Entity
@Table(name = "ord_return")
@EqualsAndHashCode(callSuper = true)
public class OrdReturn extends BaseEntity {

    /**
     * 退货单号
     */
    @Column(name = "return_no", nullable = false, length = 50)
    private String returnNo;

    /**
     * 订单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 订单编号
     */
    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    /**
     * 订单项ID
     */
    @Column(name = "order_item_id")
    private Long orderItemId;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 退货类型 1-退款 2-退货退款 3-换货
     */
    @Column(name = "return_type", nullable = false)
    private Integer returnType;

    /**
     * 退货状态 1-申请中 2-待审核 3-已审核 4-已拒绝 5-退款中 6-已退款 7-已完成
     */
    @Column(name = "return_status", columnDefinition = "tinyint default 1")
    private Integer returnStatus = 1;

    /**
     * 原因类型
     */
    @Column(name = "reason_type")
    private Integer reasonType;

    /**
     * 退货原因
     */
    @Column(name = "reason", length = 200)
    private String reason;

    /**
     * 详细描述
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * 凭证图片，多个以逗号分隔
     */
    @Column(name = "evidence", length = 500)
    private String evidence;

    /**
     * 退款金额
     */
    @Column(name = "return_amount", precision = 10, scale = 2)
    private BigDecimal returnAmount;

    /**
     * 退货数量
     */
    @Column(name = "return_qty")
    private Integer returnQty;

    /**
     * 退运费金额
     */
    @Column(name = "return_freight_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal returnFreightAmount = BigDecimal.ZERO;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审核备注
     */
    @Column(name = "audit_remark", length = 200)
    private String auditRemark;

    /**
     * 拒绝原因
     */
    @Column(name = "reject_reason", length = 200)
    private String rejectReason;

    /**
     * 退货物流公司
     */
    @Column(name = "delivery_company", length = 50)
    private String deliveryCompany;

    /**
     * 退货物流单号
     */
    @Column(name = "delivery_no", length = 50)
    private String deliveryNo;

    /**
     * 退货发货时间
     */
    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    /**
     * 退货签收时间
     */
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

    /**
     * 退款时间
     */
    @Column(name = "refund_time")
    private LocalDateTime refundTime;

    /**
     * 退款交易号
     */
    @Column(name = "refund_transaction_id", length = 100)
    private String refundTransactionId;
} 