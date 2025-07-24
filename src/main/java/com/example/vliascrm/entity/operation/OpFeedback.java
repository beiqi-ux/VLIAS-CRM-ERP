package com.example.vliascrm.entity.operation;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 意见反馈实体类
 */
@Data
@Entity
@Table(name = "op_feedback")
@EqualsAndHashCode(callSuper = true)
public class OpFeedback extends BaseEntity {

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 反馈类型 1-功能建议 2-购物问题 3-性能问题 4-其他
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 反馈内容
     */
    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    /**
     * 联系方式
     */
    @Column(name = "contact", length = 50)
    private String contact;

    /**
     * 图片URL，多个以逗号分隔
     */
    @Column(name = "images", columnDefinition = "text")
    private String images;

    /**
     * 状态 0-未处理 1-处理中 2-已处理
     */
    @Column(name = "status", columnDefinition = "tinyint default 0")
    private Integer status = 0;

    /**
     * 处理时间
     */
    @Column(name = "handle_time")
    private LocalDateTime handleTime;

    /**
     * 处理人
     */
    @Column(name = "handler_id")
    private Long handlerId;

    /**
     * 处理结果
     */
    @Column(name = "handle_result", columnDefinition = "text")
    private String handleResult;

    /**
     * 处理备注
     */
    @Column(name = "handle_remark", columnDefinition = "text")
    private String handleRemark;
} 