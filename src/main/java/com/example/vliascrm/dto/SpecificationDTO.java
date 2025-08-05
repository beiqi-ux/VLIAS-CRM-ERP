package com.example.vliascrm.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品规格DTO
 */
@Data
public class SpecificationDTO {
    
    private Long id;
    
    private String specificationName;
    
    private String specificationCode;
    
    private String description;
    
    private Integer status;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String createBy;
    
    private String updateBy;
} 