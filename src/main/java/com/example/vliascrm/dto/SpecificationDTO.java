package com.example.vliascrm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品规格DTO
 */
@Data
public class SpecificationDTO {
    
    private Long id;
    
    /**
     * 商品ID
     */
    private Long goodsId;
    
    private String specificationName;
    
    private String specificationCode;
    
    /**
     * 规格值JSON
     */
    private String specValues;
    
    /**
     * 规格值列表（前端字段）
     */
    @JsonProperty("specificationValues")
    private List<SpecificationValueDTO> specificationValues;
    
    private String description;
    
    private Integer status;
    
    /**
     * 规格类型
     */
    private Integer specType;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 默认值
     */
    private String defaultValue;
    
    /**
     * 是否必填
     */
    private Integer isRequired;
    
    /**
     * 是否可搜索
     */
    private Integer isSearchable;
    
    /**
     * 是否SKU相关
     */
    private Integer isSku;
    
    /**
     * 排序
     */
    private Integer sort;
    
    /**
     * 规格单位
     */
    private String specUnit;
    
    /**
     * 验证正则
     */
    private String validationRegex;
    
    /**
     * 验证错误信息
     */
    private String validationMessage;
    
    /**
     * 规格分类（前端使用）
     * frame-镜框，lens-镜片，sunglasses-太阳镜，contact-隐形眼镜
     */
    private String specificationCategory;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
    
    private String createBy;
    
    private String updateBy;
} 