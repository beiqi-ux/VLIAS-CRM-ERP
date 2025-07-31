package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.util.List;

/**
 * 商品分类实体类
 * 支持多级分类，适用于眼镜镜片行业
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
     * 父分类ID，0表示顶级分类
     */
    @Column(name = "parent_id")
    private Long parentId = 0L;

    /**
     * 分类级别 1-一级分类 2-二级分类 3-三级分类
     */
    @Column(name = "level")
    private Integer level = 1;

    /**
     * 分类图标
     */
    @Column(name = "icon", length = 100)
    private String icon;

    /**
     * 分类图片
     */
    @Column(name = "image", length = 255)
    private String image;

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
     * 是否显示 0-隐藏 1-显示
     */
    @Column(name = "is_show")
    private Integer isShow = 1;

    /**
     * 分类描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

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