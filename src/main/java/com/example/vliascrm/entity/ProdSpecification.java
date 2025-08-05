package com.example.vliascrm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.example.vliascrm.common.BaseEntity;

/**
 * 商品规格实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "prod_specification")
public class ProdSpecification extends BaseEntity {
    
    /**
     * 规格ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
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