package com.example.vliascrm.entity.product;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品规格实体类
 */
@Data
@Entity
@Table(name = "prod_specification")
@EqualsAndHashCode(callSuper = true)
public class ProdSpecification extends BaseEntity {

    /**
     * 商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * 规格名称
     */
    @Column(name = "spec_name", nullable = false, length = 50)
    private String specName;

    /**
     * 规格值JSON
     */
    @Column(name = "spec_values", columnDefinition = "text")
    private String specValues;
} 