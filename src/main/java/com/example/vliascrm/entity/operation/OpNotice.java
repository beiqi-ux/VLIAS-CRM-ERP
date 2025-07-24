package com.example.vliascrm.entity.operation;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 通知公告实体类
 */
@Data
@Entity
@Table(name = "op_notice")
@EqualsAndHashCode(callSuper = true)
public class OpNotice extends BaseEntity {

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
     * 类型 1-系统通知 2-活动通知 3-公告
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 状态 0-下线 1-上线
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 阅读次数
     */
    @Column(name = "read_count", columnDefinition = "int default 0")
    private Integer readCount = 0;

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