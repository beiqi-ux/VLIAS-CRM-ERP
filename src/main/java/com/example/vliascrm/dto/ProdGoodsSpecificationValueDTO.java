package com.example.vliascrm.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 商品规格值关联DTO
 */
@Data
public class ProdGoodsSpecificationValueDTO {
    
    private Long id;
    
    /**
     * 商品ID
     */
    private Long goodsId;
    
    /**
     * 商品名称
     */
    private String goodsName;
    
    /**
     * 规格项ID
     */
    private Long specItemId;
    
    /**
     * 规格项名称
     */
    private String specItemName;
    
    /**
     * 规格值ID
     */
    private Long specValueId;
    
    /**
     * 规格值
     */
    private String specValue;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String createBy;
    
    private String updateBy;
    
    private Boolean isDeleted;
} 