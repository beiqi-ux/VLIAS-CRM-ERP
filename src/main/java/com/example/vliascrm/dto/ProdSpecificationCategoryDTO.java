package com.example.vliascrm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品规格分类DTO
 */
@Data
public class ProdSpecificationCategoryDTO {
    
    private Long id;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 分类描述
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
    
    /**
     * 规格项列表
     */
    @JsonProperty("specificationItems")
    private List<ProdSpecificationItemDTO> specificationItems;
    
    /**
     * 规格项数量（统计字段）
     */
    private Long itemCount;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String createBy;
    
    private String updateBy;
    
    private Boolean isDeleted;
} 