package com.example.vliascrm.entity.inventory;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 仓库实体类
 */
@Data
@Entity
@Table(name = "inv_warehouse")
@EqualsAndHashCode(callSuper = true)
public class InvWarehouse extends BaseEntity {

    /**
     * 仓库名称
     */
    @Column(name = "warehouse_name", nullable = false, length = 50)
    private String warehouseName;

    /**
     * 仓库编码
     */
    @Column(name = "warehouse_code", length = 50)
    private String warehouseCode;

    /**
     * 联系人
     */
    @Column(name = "contact", length = 50)
    private String contact;

    /**
     * 联系电话
     */
    @Column(name = "mobile", length = 20)
    private String mobile;

    /**
     * 省份
     */
    @Column(name = "province", length = 50)
    private String province;

    /**
     * 城市
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * 区县
     */
    @Column(name = "district", length = 50)
    private String district;

    /**
     * 详细地址
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 是否默认仓库 0-否 1-是
     */
    @Column(name = "is_default", columnDefinition = "tinyint default 0")
    private Integer isDefault = 0;

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