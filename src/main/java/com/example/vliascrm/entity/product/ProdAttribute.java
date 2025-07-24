package com.example.vliascrm.entity.product;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品属性实体类
 */
@Data
@Entity
@Table(name = "prod_attribute")
@EqualsAndHashCode(callSuper = true)
public class ProdAttribute extends BaseEntity {

    /**
     * 商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * 属性名
     */
    @Column(name = "attr_name", nullable = false, length = 50)
    private String attrName;

    /**
     * 属性值
     */
    @Column(name = "attr_value", length = 200)
    private String attrValue;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;
} 