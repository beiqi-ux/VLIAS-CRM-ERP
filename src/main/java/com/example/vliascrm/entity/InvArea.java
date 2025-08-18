package com.example.vliascrm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;


/**
 * 库区实体类
 * 对应数据库表：inv_area
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "inv_area")
public class InvArea {

    /**
     * 库区ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Column(name = "status")
    private Integer status = 1;

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