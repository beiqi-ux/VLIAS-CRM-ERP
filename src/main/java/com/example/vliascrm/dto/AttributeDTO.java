package com.example.vliascrm.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品属性DTO
 */
@Data
public class AttributeDTO {
    
    private Long id;
    
    private String attributeName;
    
    private String attributeCode;
    
    private String description;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String createBy;
    
    private String updateBy;
} 