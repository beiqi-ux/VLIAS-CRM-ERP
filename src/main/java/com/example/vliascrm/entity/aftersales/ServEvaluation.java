package com.example.vliascrm.entity.aftersales;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 客服评价实体类
 */
@Data
@Entity
@Table(name = "serv_evaluation")
@EntityListeners(AuditingEntityListener.class)
public class ServEvaluation {

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
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 客服ID
     */
    @Column(name = "service_user_id", nullable = false)
    private Long serviceUserId;

    /**
     * 客服名称
     */
    @Column(name = "service_user_name", length = 50)
    private String serviceUserName;

    /**
     * 满意度评分 1-非常不满意 2-不满意 3-一般 4-满意 5-非常满意
     */
    @Column(name = "score", nullable = false)
    private Integer score;

    /**
     * 服务态度评分
     */
    @Column(name = "attitude_score")
    private Integer attitudeScore;

    /**
     * 专业程度评分
     */
    @Column(name = "professional_score")
    private Integer professionalScore;

    /**
     * 处理速度评分
     */
    @Column(name = "speed_score")
    private Integer speedScore;

    /**
     * 评价内容
     */
    @Column(name = "content", columnDefinition = "text")
    private String content;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 