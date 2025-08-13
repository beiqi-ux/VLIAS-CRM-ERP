package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 商品规格值实体类
 * 对应数据库表 prod_specification_value
 */
@Data
@Entity
@Table(name = "prod_specification_value")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class ProdSpecificationValue extends BaseEntity {

    /**
     * 状态常量
     */
    public static final Integer STATUS_DISABLED = 0; // 禁用
    public static final Integer STATUS_ENABLED = 1;  // 启用

    /**
     * 规格项ID
     */
    @Column(name = "item_id", nullable = false)
    private Long itemId;

    /**
     * 规格值代码
     */
    @Column(name = "value_code", nullable = false, length = 50)
    private String valueCode;

    /**
     * 规格值名称
     */
    @Column(name = "value_name", nullable = false, length = 100)
    private String valueName;

    /**
     * 规格值描述
     */
    @Column(name = "value_desc", columnDefinition = "TEXT")
    private String valueDesc;

    /**
     * 数值型值（用于排序和计算）
     */
    @Column(name = "numeric_value")
    private java.math.BigDecimal numericValue;

    /**
     * 文本型值
     */
    @Column(name = "text_value", length = 200)
    private String textValue;

    /**
     * 排序值
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status")
    private Integer status = 1;

    /**
     * 规格项名称（非持久化字段）
     */
    @Transient
    private String specItemName;

    /**
     * 规格项单位（非持久化字段）
     */
    @Transient
    private String specItemUnit;

    /**
     * 规格项对象 (关联查询用)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private ProdSpecificationItem item;
} 