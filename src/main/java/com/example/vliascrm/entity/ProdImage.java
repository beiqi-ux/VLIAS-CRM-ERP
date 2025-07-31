package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 商品图片实体类
 * 严格按照数据库设计方案定义
 */
@Data
@Entity
@Table(name = "prod_image")
@EqualsAndHashCode(callSuper = true)
public class ProdImage extends BaseEntity {

    /**
     * 商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * 图片URL
     */
    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort = 0;

    /**
     * 是否主图 0-否 1-是
     */
    @Column(name = "is_main")
    private Integer isMain = 0;
} 