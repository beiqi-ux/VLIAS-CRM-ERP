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
 * 退货申请实体类
 */
@Data
@Entity
@Table(name = "serv_return")
@EqualsAndHashCode(callSuper = true)
public class ServReturn extends BaseEntity {

    /**
     * 退货单号
     */
    @Column(name = "return_no", nullable = false, length = 50)
    private String returnNo;

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
     * 订单金额
     */
    @Column(name = "order_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal orderAmount;

    /**
     * 退款金额
     */
    @Column(name = "refund_amount", precision = 10, scale = 2)
    private BigDecimal refundAmount;

    /**
     * 退货原因
     */
    @Column(name = "reason", length = 200)
    private String reason;

    /**
     * 退货说明
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * 退货图片，多个以逗号分隔
     */
    @Column(name = "images", length = 500)
    private String images;

    /**
     * 状态 1-待审核 2-已同意 3-已拒绝 4-已退货 5-已收货 6-已完成 7-已取消
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
     * 退货时间
     */
    @Column(name = "return_time")
    private LocalDateTime returnTime;

    /**
     * 物流单号
     */
    @Column(name = "logistics_no", length = 50)
    private String logisticsNo;

    /**
     * 物流公司
     */
    @Column(name = "logistics_company", length = 50)
    private String logisticsCompany;

    /**
     * 收货时间
     */
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

    /**
     * 收货备注
     */
    @Column(name = "receive_remark", length = 200)
    private String receiveRemark;

    /**
     * 收货人ID
     */
    @Column(name = "receive_user_id")
    private Long receiveUserId;

    /**
     * 完成时间
     */
    @Column(name = "complete_time")
    private LocalDateTime completeTime;

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