package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.List;

/**
 * 商品规格项实体类
 * 对应数据库表 prod_specification_item
 */
@Data
@Entity
@Table(name = "prod_specification_item")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class ProdSpecificationItem extends BaseEntity {

    /**
     * 状态常量
     */
    public static final Integer STATUS_DISABLED = 0; // 禁用
    public static final Integer STATUS_ENABLED = 1;  // 启用

    /**
     * 规格项代码
     */
    @Column(name = "item_code", nullable = false, length = 50, unique = true)
    private String itemCode;

    /**
     * 规格项名称
     */
    @Column(name = "item_name", nullable = false, length = 50)
    private String itemName;

    /**
     * 规格分类ID
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    /**
     * 单位
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status")
    private Integer status = 1;

    /**
     * 规格分类名称（非持久化字段）
     */
    @Transient
    private String categoryName;

    /**
     * 规格值列表（非持久化字段）
     */
    @Transient
    private List<ProdSpecificationValue> specificationValues;

    /**
     * 规格分类对象 (关联查询用)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    private ProdSpecificationCategory category;
} 