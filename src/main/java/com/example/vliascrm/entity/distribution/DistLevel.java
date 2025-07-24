package com.example.vliascrm.entity.distribution;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 分销等级实体类
 */
@Data
@Entity
@Table(name = "dist_level")
@EqualsAndHashCode(callSuper = true)
public class DistLevel extends BaseEntity {

    /**
     * 等级名称
     */
    @Column(name = "level_name", nullable = false, length = 50)
    private String levelName;

    /**
     * 等级值
     */
    @Column(name = "level_value", nullable = false)
    private Integer levelValue;

    /**
     * 升级条件类型 1-累计佣金 2-累计订单 3-团队人数 4-直推人数
     */
    @Column(name = "upgrade_type")
    private Integer upgradeType;

    /**
     * 升级条件值
     */
    @Column(name = "upgrade_value")
    private Integer upgradeValue;

    /**
     * 一级佣金比例
     */
    @Column(name = "first_rate", precision = 5, scale = 4, nullable = false)
    private BigDecimal firstRate;

    /**
     * 二级佣金比例
     */
    @Column(name = "second_rate", precision = 5, scale = 4)
    private BigDecimal secondRate;

    /**
     * 三级佣金比例
     */
    @Column(name = "third_rate", precision = 5, scale = 4)
    private BigDecimal thirdRate;

    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

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