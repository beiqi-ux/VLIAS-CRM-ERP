package com.example.vliascrm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 仓库DTO
 * 对应数据库表：inv_warehouse
 */
@Data
public class InvWarehouseDTO {

    /**
     * 仓库ID
     */
    private Long id;

    /**
     * 仓库名称
     */
    @NotBlank(message = "仓库名称不能为空")
    private String warehouseName;

    /**
     * 仓库编码
     */
    private String warehouseCode;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;

    /**
     * 是否默认仓库 0-否 1-是
     */
    private Integer isDefault;

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
    private Integer isDeleted;
} 

