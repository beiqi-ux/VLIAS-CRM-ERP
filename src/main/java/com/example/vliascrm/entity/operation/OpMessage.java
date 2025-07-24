package com.example.vliascrm.entity.operation;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 站内信实体类
 */
@Data
@Entity
@Table(name = "op_message")
@EqualsAndHashCode(callSuper = true)
public class OpMessage extends BaseEntity {

    /**
     * 发送者ID
     */
    @Column(name = "sender_id")
    private Long senderId;

    /**
     * 发送者名称
     */
    @Column(name = "sender_name", length = 50)
    private String senderName;

    /**
     * 接收者ID
     */
    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    /**
     * 接收者名称
     */
    @Column(name = "receiver_name", length = 50)
    private String receiverName;

    /**
     * 标题
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * 内容
     */
    @Column(name = "content", nullable = false, columnDefinition = "text")
    private String content;

    /**
     * 消息类型 1-系统消息 2-订单消息 3-活动消息 4-其他
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 是否已读 0-未读 1-已读
     */
    @Column(name = "is_read", columnDefinition = "tinyint default 0")
    private Integer isRead = 0;

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