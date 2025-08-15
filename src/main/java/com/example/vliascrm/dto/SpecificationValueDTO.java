package com.example.vliascrm.dto;

import lombok.Data;

/**
 * 规格值DTO
 */
@Data
public class SpecificationValueDTO {
    
    /**
     * 规格值编码
     */
    private String valueCode;
    
    /**
     * 镜框宽度
     */
    private String frameWidth;
    
    /**
     * 镜片宽度
     */
    private String lensWidth;
    
    /**
     * 镜片高度
     */
    private String lensHeight;
    
    /**
     * 鼻梁宽度
     */
    private String bridgeWidth;
    
    /**
     * 镜腿长度
     */
    private String templeLength;
    
    /**
     * 颜色名称
     */
    private String colorName;
    
    /**
     * 镜片颜色名称
     */
    private String lensColorName;
    
    /**
     * 形状名称
     */
    private String shapeName;
    
    /**
     * 材质名称
     */
    private String materialName;
    
    /**
     * 排序
     */
    private Integer sortOrder;
    
    /**
     * 状态 1-启用 0-禁用
     */
    private Integer status;
} 