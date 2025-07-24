package com.example.vliascrm.entity.promotion;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 秒杀活动实体类
 */
@Data
@Entity
@Table(name = "prom_seckill")
@EqualsAndHashCode(callSuper = true)
public class PromSeckill extends BaseEntity {

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
     * 状态 0-未开始 1-进行中 2-已结束 3-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 0")
    private Integer status = 0;

    /**
     * 每人限购
     */
    @Column(name = "limit_qty", columnDefinition = "int default 1")
    private Integer limitQty = 1;

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