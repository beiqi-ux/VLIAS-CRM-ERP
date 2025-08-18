package com.example.vliascrm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

/**
 * 仓库实体类
 * 对应数据库表：inv_warehouse
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "inv_warehouse")
public class InvWarehouse {

    /**
     * 仓库ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 仓库名称
     */
    @Column(name = "warehouse_name", nullable = false, length = 50)
    private String warehouseName;

    /**
     * 仓库编码
     */
    @Column(name = "warehouse_code", length = 50, unique = true)
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
    @Column(name = "status")
    private Integer status = 1;

    /**
     * 是否默认仓库 0-否 1-是
     */
    @Column(name = "is_default")
    private Integer isDefault = 0;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort = 0;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 更新人
     */
    @Column(name = "update_by")
    private Long updateBy;

    /**
     * 是否删除 0-否 1-是
     */
    @Column(name = "is_deleted")
    private Integer isDeleted = 0;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
} 