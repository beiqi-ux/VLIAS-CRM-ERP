package com.example.vliascrm.entity.product;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分类实体类
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
     * 父分类ID
     */
    @Column(name = "parent_id", columnDefinition = "bigint default 0")
    private Long parentId = 0L;

    /**
     * 层级 1-一级 2-二级 3-三级
     */
    @Column(name = "level", columnDefinition = "int default 1")
    private Integer level = 1;

    /**
     * 图标
     */
    @Column(name = "icon", length = 255)
    private String icon;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 是否显示 0-否 1-是
     */
    @Column(name = "is_show", columnDefinition = "tinyint default 1")
    private Integer isShow = 1;
} 