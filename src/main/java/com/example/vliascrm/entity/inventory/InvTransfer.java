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
 * 库存调拨实体类
 */
@Data
@Entity
@Table(name = "inv_transfer")
@EqualsAndHashCode(callSuper = true)
public class InvTransfer extends BaseEntity {

    /**
     * 调拨单号
     */
    @Column(name = "transfer_no", nullable = false, length = 50)
    private String transferNo;

    /**
     * 源仓库ID
     */
    @Column(name = "from_warehouse_id", nullable = false)
    private Long fromWarehouseId;

    /**
     * 目标仓库ID
     */
    @Column(name = "to_warehouse_id", nullable = false)
    private Long toWarehouseId;

    /**
     * 状态 1-草稿 2-待出库 3-待入库 4-已完成 5-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 调拨日期
     */
    @Column(name = "transfer_time")
    private LocalDate transferTime;

    /**
     * 出库时间
     */
    @Column(name = "out_time")
    private LocalDateTime outTime;

    /**
     * 入库时间
     */
    @Column(name = "in_time")
    private LocalDateTime inTime;

    /**
     * 调拨人
     */
    @Column(name = "transfer_user_id")
    private Long transferUserId;

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