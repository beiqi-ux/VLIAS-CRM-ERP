package com.example.vliascrm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.vliascrm.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 商品规格实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "prod_specification")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class ProdSpecification extends BaseEntity {
    
    /**
     * 规格ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 商品ID
     */
    @Column(name = "goods_id")
    private Long goodsId;
    
    /**
     * 分类ID
     */
    @Column(name = "category_id")
    private Long categoryId;
    
    /**
     * 规格名称
     */
    @Column(name = "specification_name", length = 50, nullable = false)
    private String specificationName;
    
    /**
     * 规格编码
     */
    @Column(name = "specification_code", length = 50, nullable = false)
    private String specificationCode;
    
    /**
     * 规格值JSON
     */
    @Column(name = "spec_values", columnDefinition = "TEXT")
    private String specValues;
    
    /**
     * 描述
     */
    @Column(name = "description", length = 200)
    private String description;
    
    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "status")
    private Integer status = 1;
    
    /**
     * 规格类型
     */
    @Column(name = "spec_type", nullable = false)
    private Integer specType = 1;
    

    
    /**
     * 默认值
     */
    @Column(name = "default_value", length = 100)
    private String defaultValue;
    
    /**
     * 是否必填
     */
    @Column(name = "is_required")
    private Integer isRequired = 0;
    
    /**
     * 是否可搜索
     */
    @Column(name = "is_searchable")
    private Integer isSearchable = 0;
    
    /**
     * 是否SKU相关
     */
    @Column(name = "is_sku")
    private Integer isSku = 0;
    
    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort = 0;
    
    /**
     * 规格单位
     */
    @Column(name = "spec_unit", length = 20)
    private String specUnit;
    
    /**
     * 验证正则
     */
    @Column(name = "validation_regex", length = 200)
    private String validationRegex;
    
    /**
     * 验证错误信息
     */
    @Column(name = "validation_message", length = 100)
    private String validationMessage;
} 