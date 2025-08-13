package com.example.vliascrm.entity;

import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

/**
 * 商品规格值关联实体类
 * 对应数据库表 prod_goods_specification_value
 */
@Data
@Entity
@Table(name = "prod_goods_specification_value")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class ProdGoodsSpecificationValue extends BaseEntity {

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
     * 商品名称（非持久化字段）
     */
    @Transient
    private String goodsName;

    /**
     * 规格项名称（非持久化字段）
     */
    @Transient
    private String specItemName;

    /**
     * 规格值（非持久化字段）
     */
    @Transient
    private String specValue;
} 