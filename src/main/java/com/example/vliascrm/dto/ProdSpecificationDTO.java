package com.example.vliascrm.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 商品规格DTO
 */
@Data
public class ProdSpecificationDTO {

    /**
     * 规格ID
     */
    private Long id;

    /**
     * 关联的商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    /**
     * 规格名称
     */
    @NotBlank(message = "规格名称不能为空")
    private String specName;

    /**
     * 规格值JSON
     */
    private String specValues;

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