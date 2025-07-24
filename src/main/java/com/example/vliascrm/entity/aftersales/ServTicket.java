package com.example.vliascrm.entity.aftersales;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 售后工单实体类
 */
@Data
@Entity
@Table(name = "serv_ticket")
@EqualsAndHashCode(callSuper = true)
public class ServTicket extends BaseEntity {

    /**
     * 工单编号
     */
    @Column(name = "ticket_no", nullable = false, length = 50)
    private String ticketNo;

    /**
     * 工单标题
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 会员ID
     */
    @Column(name = "member_id")
    private Long memberId;

    /**
     * 工单类型 1-咨询 2-售后 3-投诉 4-建议
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 工单来源 1-网页 2-App 3-小程序 4-电话
     */
    @Column(name = "source")
    private Integer source;

    /**
     * 紧急程度 1-普通 2-紧急 3-非常紧急
     */
    @Column(name = "priority", columnDefinition = "tinyint default 1")
    private Integer priority = 1;

    /**
     * 工单内容
     */
    @Column(name = "content", columnDefinition = "text")
    private String content;

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
     * 附件
     */
    @Column(name = "attachments", length = 500)
    private String attachments;

    /**
     * 关联订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 关联订单编号
     */
    @Column(name = "order_no", length = 50)
    private String orderNo;

    /**
     * 处理状态 1-待受理 2-处理中 3-已解决 4-已关闭
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 分配客服ID
     */
    @Column(name = "service_user_id")
    private Long serviceUserId;

    /**
     * 分配时间
     */
    @Column(name = "assign_time")
    private LocalDateTime assignTime;

    /**
     * 处理时间
     */
    @Column(name = "handle_time")
    private LocalDateTime handleTime;

    /**
     * 完成时间
     */
    @Column(name = "finish_time")
    private LocalDateTime finishTime;

    /**
     * 满意度评价 1-非常不满意 2-不满意 3-一般 4-满意 5-非常满意
     */
    @Column(name = "satisfaction")
    private Integer satisfaction;

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