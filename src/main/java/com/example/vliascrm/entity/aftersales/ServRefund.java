package com.example.vliascrm.entity.aftersales;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款申请实体类
 */
@Data
@Entity
@Table(name = "serv_refund")
@EqualsAndHashCode(callSuper = true)
public class ServRefund extends BaseEntity {

    /**
     * 退款单号
     */
    @Column(name = "refund_no", nullable = false, length = 50)
    private String refundNo;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

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
     * 订单支付金额
     */
    @Column(name = "order_pay_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderPayAmount;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal refundAmount;

    /**
     * 退款类型 1-仅退款 2-退货退款
     */
    @Column(name = "refund_type", nullable = false)
    private Integer refundType;

    /**
     * 退款原因
     */
    @Column(name = "reason", length = 200)
    private String reason;

    /**
     * 退款说明
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * 退款凭证，多个以逗号分隔
     */
    @Column(name = "evidence", length = 500)
    private String evidence;

    /**
     * 状态 1-待审核 2-已同意 3-已拒绝 4-退款中 5-已退款 6-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 审核备注
     */
    @Column(name = "audit_remark", length = 200)
    private String auditRemark;

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
     * 退款时间
     */
    @Column(name = "refund_time")
    private LocalDateTime refundTime;

    /**
     * 退款流水号
     */
    @Column(name = "refund_trade_no", length = 100)
    private String refundTradeNo;

    /**
     * 原支付流水号
     */
    @Column(name = "origin_trade_no", length = 100)
    private String originTradeNo;

    /**
     * 退款方式 1-原路退回 2-退至余额 3-人工退款
     */
    @Column(name = "refund_way")
    private Integer refundWay;

    /**
     * 退款操作人ID
     */
    @Column(name = "refund_user_id")
    private Long refundUserId;

    /**
     * 退款失败原因
     */
    @Column(name = "fail_reason", length = 200)
    private String failReason;

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