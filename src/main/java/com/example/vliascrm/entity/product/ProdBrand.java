package com.example.vliascrm.entity.product;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品品牌实体类
 */
@Data
@Entity
@Table(name = "prod_brand")
@EqualsAndHashCode(callSuper = true)
public class ProdBrand extends BaseEntity {

    /**
     * 品牌名称
     */
    @Column(name = "brand_name", nullable = false, length = 50, unique = true)
    private String brandName;

    /**
     * 品牌LOGO
     */
    @Column(name = "brand_logo", length = 255)
    private String brandLogo;

    /**
     * 品牌描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 品牌网址
     */
    @Column(name = "website", length = 100)
    private String website;

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
} 