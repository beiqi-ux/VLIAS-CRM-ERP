package com.example.vliascrm.entity.product;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 商品实体类
 */
@Data
@Entity
@Table(name = "prod_goods")
@EqualsAndHashCode(callSuper = true)
public class ProdGoods extends BaseEntity {

    /**
     * 商品名称
     */
    @Column(name = "goods_name", nullable = false, length = 100)
    private String goodsName;

    /**
     * 商品编码
     */
    @Column(name = "goods_code", length = 50)
    private String goodsCode;

    /**
     * 分类ID
     */
    @Column(name = "category_id", nullable = false)
    private Long categoryId;

    /**
     * 品牌ID
     */
    @Column(name = "brand_id")
    private Long brandId;

    /**
     * 商品类型 1-实物商品 2-虚拟商品
     */
    @Column(name = "goods_type", columnDefinition = "tinyint default 1")
    private Integer goodsType = 1;

    /**
     * 单位
     */
    @Column(name = "unit", length = 20)
    private String unit;

    /**
     * 重量(kg)
     */
    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    /**
     * 体积(m³)
     */
    @Column(name = "volume", precision = 10, scale = 2)
    private BigDecimal volume;

    /**
     * 原价
     */
    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    /**
     * 售价
     */
    @Column(name = "selling_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    /**
     * 成本价
     */
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    /**
     * 最低售价
     */
    @Column(name = "min_price", precision = 10, scale = 2)
    private BigDecimal minPrice;

    /**
     * 库存数量
     */
    @Column(name = "stock_qty", columnDefinition = "int default 0")
    private Integer stockQty = 0;

    /**
     * 库存预警值
     */
    @Column(name = "warn_stock", columnDefinition = "int default 0")
    private Integer warnStock = 0;

    /**
     * 销量
     */
    @Column(name = "sale_qty", columnDefinition = "int default 0")
    private Integer saleQty = 0;

    /**
     * 状态 0-下架 1-上架
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 是否推荐 0-否 1-是
     */
    @Column(name = "is_recommended", columnDefinition = "tinyint default 0")
    private Integer isRecommended = 0;

    /**
     * 是否热销 0-否 1-是
     */
    @Column(name = "is_hot", columnDefinition = "tinyint default 0")
    private Integer isHot = 0;

    /**
     * 是否新品 0-否 1-是
     */
    @Column(name = "is_new", columnDefinition = "tinyint default 0")
    private Integer isNew = 0;

    /**
     * 关键词
     */
    @Column(name = "keywords", length = 200)
    private String keywords;

    /**
     * 标签
     */
    @Column(name = "tags", length = 200)
    private String tags;

    /**
     * 主图
     */
    @Column(name = "main_image", length = 255)
    private String mainImage;

    /**
     * 视频URL
     */
    @Column(name = "video_url", length = 255)
    private String videoUrl;

    /**
     * 简介
     */
    @Column(name = "brief", length = 255)
    private String brief;

    /**
     * 详情描述
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
    private String remark;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 审核状态 0-未审核 1-审核通过 2-审核拒绝
     */
    @Column(name = "audit_status", columnDefinition = "tinyint default 0")
    private Integer auditStatus = 0;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private java.time.LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 审核备注
     */
    @Column(name = "audit_remark", length = 200)
    private String auditRemark;
} 