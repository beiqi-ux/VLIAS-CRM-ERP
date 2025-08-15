package com.example.vliascrm.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品信息DTO - 包含分类名称和品牌名称
 */
@Data
public class ProdGoodsDto {
    
    private Long id;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;
    private Integer isDeleted;
    
    // 商品基本信息
    private String goodsName;
    private String goodsCode;
    private Long categoryId;
    private String categoryName;  // 分类名称
    private Long brandId;
    private String brandName;     // 品牌名称
    private Integer goodsType;
    private String unit;
    private BigDecimal weight;
    private BigDecimal volume;
    
    // 价格信息
    private BigDecimal originalPrice;
    private BigDecimal sellingPrice;
    private BigDecimal costPrice;
    private BigDecimal minPrice;
    
    // 库存信息
    private Integer stockQty;
    private Integer warnStock;
    private Integer saleQty;
    
    // 状态信息
    private Integer status;
    private Integer isRecommended;
    private Integer isHot;
    private Integer isNew;
    
    // 其他信息
    private String keywords;
    private String tags;
    private String mainImage;
    private String videoUrl;
    private String brief;
    private String description;
    private String remark;
    private Integer sort;
    
    // 审核信息
    private Integer auditStatus;
    private LocalDateTime auditTime;
    private Long auditUserId;
    private String auditRemark;
    
    // 兼容性字段
    public String getContent() {
        return this.description;
    }
    
    public Integer getSaleStatus() {
        return this.status;
    }
} 