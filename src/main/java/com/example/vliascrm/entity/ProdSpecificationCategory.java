package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.List;

/**
 * 商品规格分类实体类
 * 对应数据库表 prod_specification_category
 */
@Data
@Entity
@Table(name = "prod_specification_category")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class ProdSpecificationCategory extends BaseEntity {

    /**
     * 分类名称
     */
    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    /**
     * 分类描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

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
     * 规格项列表（非持久化字段）
     */
    @Transient
    private List<ProdSpecificationItem> specificationItems;
} 