package com.example.vliascrm.entity.inventory;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 库存盘点实体类
 */
@Data
@Entity
@Table(name = "inv_check")
@EqualsAndHashCode(callSuper = true)
public class InvCheck extends BaseEntity {

    /**
     * 盘点单号
     */
    @Column(name = "check_no", nullable = false, length = 50)
    private String checkNo;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    /**
     * 状态 1-草稿 2-待审核 3-已审核 4-已完成 5-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 盘点日期
     */
    @Column(name = "check_time")
    private LocalDate checkTime;

    /**
     * 盘点开始时间
     */
    @Column(name = "begin_time")
    private LocalDateTime beginTime;

    /**
     * 盘点结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 盘点人
     */
    @Column(name = "check_user_id")
    private Long checkUserId;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
    private String remark;

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
} 