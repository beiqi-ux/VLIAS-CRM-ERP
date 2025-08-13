package com.example.vliascrm.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品规格值DTO
 */
@Data
public class ProdSpecificationValueDTO {
    
    private Long id;
    
    /**
     * 规格项ID
     */
    private Long specItemId;
    
    /**
     * 规格项名称
     */
    private String specItemName;
    
    /**
     * 规格项单位
     */
    private String specItemUnit;
    
    /**
     * 规格值
     */
    private String specValue;
    
    /**
     * 规格值描述
     */
    private String description;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String createBy;
    
    private String updateBy;
    
    private Boolean isDeleted;
} 