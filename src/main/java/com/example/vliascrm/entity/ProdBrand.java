package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 商品品牌实体类
 * 管理眼镜品牌信息
 */
@Data
@Entity
@Table(name = "prod_brand")
@EqualsAndHashCode(callSuper = true)
public class ProdBrand extends BaseEntity {

    /**
     * 品牌名称
     */
    @Column(name = "brand_name", nullable = false, length = 50)
    private String brandName;

    /**
     * 品牌LOGO
     */
    @Column(name = "brand_logo", length = 255)
    private String brandLogo;

    /**
     * 品牌网址
     */
    @Column(name = "website", length = 100)
    private String website;

    /**
     * 品牌描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", nullable = false, columnDefinition = "int default 1")
    private Integer status = 1;
} 