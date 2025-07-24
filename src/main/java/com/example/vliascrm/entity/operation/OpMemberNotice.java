package com.example.vliascrm.entity.operation;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 会员通知实体类
 */
@Data
@Entity
@Table(name = "op_member_notice")
@EntityListeners(AuditingEntityListener.class)
public class OpMemberNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 通知ID
     */
    @Column(name = "notice_id", nullable = false)
    private Long noticeId;

    /**
     * 是否已读 0-未读 1-已读
     */
    @Column(name = "is_read", columnDefinition = "tinyint default 0")
    private Integer isRead = 0;

    /**
     * 阅读时间
     */
    @Column(name = "read_time")
    private LocalDateTime readTime;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 