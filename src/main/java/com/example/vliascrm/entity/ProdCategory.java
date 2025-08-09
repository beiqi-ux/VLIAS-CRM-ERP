package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.List;

/**
 * 商品分类实体类
 * 严格按照数据库设计方案 prod_category 表结构
 */
@Data
@Entity
@Table(name = "prod_category")
@EqualsAndHashCode(callSuper = true)
public class ProdCategory extends BaseEntity {

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
     * 父分类ID，null表示顶级分类
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 层级 1-一级 2-二级 3-三级
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 图标
     */
    @Column(name = "icon", length = 255)
    private String icon;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status")
    private Integer status = 1;

    /**
     * 是否显示 0-否 1-是
     */
    @Column(name = "is_show")
    private Integer isShow = 1;

    /**
     * 子分类列表（非持久化字段）
     */
    @Transient
    private List<ProdCategory> children;

    /**
     * 父分类名称（非持久化字段）
     */
    @Transient
    private String parentName;
} 