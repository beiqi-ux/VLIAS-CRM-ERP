package com.example.vliascrm.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 商品属性DTO
 */
@Data
public class ProdAttributeDTO {

    /**
     * 属性ID
     */
    private Long id;

    /**
     * 关联的商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    /**
     * 属性名
     */
    @NotBlank(message = "属性名不能为空")
    private String attrName;

    /**
     * 属性值
     */
    private String attrValue;

    /**
     * 排序
     */
    private Integer sort = 0;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;
} 