package com.example.vliascrm.entity.inventory;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 库区实体类
 */
@Data
@Entity
@Table(name = "inv_area")
@EqualsAndHashCode(callSuper = true)
public class InvArea extends BaseEntity {

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    /**
     * 库区名称
     */
    @Column(name = "area_name", nullable = false, length = 50)
    private String areaName;

    /**
     * 库区编码
     */
    @Column(name = "area_code", length = 50)
    private String areaCode;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
    private String remark;
} 