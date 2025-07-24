package com.example.vliascrm.entity.promotion;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 满减活动实体类
 */
@Data
@Entity
@Table(name = "prom_full_reduction")
@EqualsAndHashCode(callSuper = true)
public class PromFullReduction extends BaseEntity {

    /**
     * 活动名称
     */
    @Column(name = "activity_name", nullable = false, length = 100)
    private String activityName;

    /**
     * 开始时间
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    /**
     * 状态 0-未开始 1-进行中 2-已结束
     */
    @Column(name = "status", columnDefinition = "tinyint default 0")
    private Integer status = 0;

    /**
     * 使用范围 1-全场通用 2-指定商品 3-指定分类
     */
    @Column(name = "use_range", columnDefinition = "tinyint default 1")
    private Integer useRange = 1;

    /**
     * 范围值JSON
     */
    @Column(name = "range_values", columnDefinition = "text")
    private String rangeValues;

    /**
     * 满减规则JSON
     */
    @Column(name = "rules", nullable = false, columnDefinition = "text")
    private String rules;

    /**
     * 活动说明
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

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