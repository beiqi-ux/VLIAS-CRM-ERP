package com.example.vliascrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 库位DTO
 * 对应数据库表：inv_location
 */
@Data
public class InvLocationDTO {

    /**
     * 库位ID
     */
    private Long id;

    /**
     * 仓库ID
     */
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    /**
     * 库区ID
     */
    private Long areaId;

    /**
     * 库位名称
     */
    @NotBlank(message = "库位名称不能为空")
    private String locationName;

    /**
     * 库位编码
     */
    private String locationCode;

    /**
     * 状态 0-禁用 1-正常
     */
    private Byte status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 是否删除 0-否 1-是
     */
    private Byte isDeleted;
} 

