package com.example.vliascrm.entity.aftersales;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 工单跟进记录实体类
 */
@Data
@Entity
@Table(name = "serv_ticket_record")
@EntityListeners(AuditingEntityListener.class)
public class ServTicketRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 工单ID
     */
    @Column(name = "ticket_id", nullable = false)
    private Long ticketId;

    /**
     * 工单编号
     */
    @Column(name = "ticket_no", nullable = false, length = 50)
    private String ticketNo;

    /**
     * 处理类型 1-受理 2-回复 3-转派 4-关闭 5-解决
     */
    @Column(name = "handle_type", nullable = false)
    private Integer handleType;

    /**
     * 处理内容
     */
    @Column(name = "content", columnDefinition = "text")
    private String content;

    /**
     * 处理人ID
     */
    @Column(name = "handler_id")
    private Long handlerId;

    /**
     * 处理人姓名
     */
    @Column(name = "handler_name", length = 50)
    private String handlerName;

    /**
     * 是否客户可见 0-否 1-是
     */
    @Column(name = "is_visible", columnDefinition = "tinyint default 1")
    private Integer isVisible = 1;

    /**
     * 附件
     */
    @Column(name = "attachments", length = 500)
    private String attachments;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 