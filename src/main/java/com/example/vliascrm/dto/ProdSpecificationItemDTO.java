package com.example.vliascrm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品规格项DTO
 */
@Data
public class ProdSpecificationItemDTO {
    
    private Long id;
    
    /**
     * 规格项名称
     */
    private String itemName;
    
    /**
     * 规格分类ID
     */
    private Long categoryId;
    
    /**
     * 规格分类名称
     */
    private String categoryName;
    
    /**
     * 单位
     */
    private String unit;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;
    
    /**
     * 规格值列表
     */
    @JsonProperty("specificationValues")
    private List<ProdSpecificationValueDTO> specificationValues;
    
    /**
     * 规格值数量（统计字段）
     */
    private Long valueCount;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String createBy;
    
    private String updateBy;
    
    private Boolean isDeleted;
} 