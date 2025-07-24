package com.example.vliascrm.entity.crm;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 客户跟进记录实体类
 */
@Data
@Entity
@Table(name = "crm_follow_record")
@EqualsAndHashCode(callSuper = true)
public class CrmFollowRecord extends BaseEntity {

    /**
     * 客户ID
     */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /**
     * 跟进方式 1-电话 2-邮件 3-拜访 4-会议 5-其他
     */
    @Column(name = "follow_type")
    private Integer followType;

    /**
     * 跟进内容
     */
    @Column(name = "follow_content", nullable = false, columnDefinition = "text")
    private String followContent;

    /**
     * 下次跟进时间
     */
    @Column(name = "next_follow_time")
    private LocalDateTime nextFollowTime;

    /**
     * 附件
     */
    @Column(name = "files", length = 500)
    private String files;

    /**
     * 是否删除 0-否 1-是
     */
    @Column(name = "is_deleted", columnDefinition = "tinyint default 0")
    private Boolean isDeleted = false;
} 