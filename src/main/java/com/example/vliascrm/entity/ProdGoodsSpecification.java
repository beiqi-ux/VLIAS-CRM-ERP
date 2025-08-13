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
@Table(name = "prod_goods_specification")
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
     * 规格值ID
     */
    @Column(name = "spec_value_id", nullable = false)
    private Long specValueId;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime = LocalDateTime.now();

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

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