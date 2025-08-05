package com.example.vliascrm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.vliascrm.common.BaseEntity;

/**
 * 商品属性实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "prod_attribute")
public class ProdAttribute extends BaseEntity {
    
    /**
     * 属性ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 属性名称
     */
    @Column(name = "attribute_name", length = 50, nullable = false)
    private String attributeName;
    
    /**
     * 属性编码
     */
    @Column(name = "attribute_code", length = 50, nullable = false)
    private String attributeCode;
    
    /**
     * 描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;
} 