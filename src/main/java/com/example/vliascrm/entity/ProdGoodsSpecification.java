package com.example.vliascrm.entity;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 商品规格关联实体类
 * 商品与规格值的关联关系
 */
@Data
@Entity
@Table(name = "prod_goods_specification_value")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class ProdGoodsSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * 规格项ID
     */
    @Column(name = "spec_item_id", nullable = false)
    private Long specItemId;

    /**
     * 规格值ID
     */
    @Column(name = "spec_value_id", nullable = false)
    private Long specValueId;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private LocalDateTime updatedTime;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 更新人
     */
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * 是否删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted = 0;

    /**
     * 商品对象 (关联查询用)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", insertable = false, updatable = false)
    private ProdGoods goods;

    /**
     * 规格值对象 (关联查询用)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spec_value_id", insertable = false, updatable = false)
    private ProdSpecificationValue specificationValue;
} 