package com.example.vliascrm.entity.member;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 会员等级实体类
 */
@Data
@Entity
@Table(name = "mem_level")
@EqualsAndHashCode(callSuper = true)
public class MemLevel extends BaseEntity {

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
     * 所需最低成长值
     */
    @Column(name = "growth_min", nullable = false)
    private Integer growthMin;

    /**
     * 所需最高成长值
     */
    @Column(name = "growth_max", nullable = false)
    private Integer growthMax;

    /**
     * 折扣率
     */
    @Column(name = "discount", precision = 3, scale = 2, columnDefinition = "decimal(3,2) default 1")
    private BigDecimal discount = BigDecimal.ONE;

    /**
     * 等级描述
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * 等级图标
     */
    @Column(name = "icon", length = 255)
    private String icon;

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