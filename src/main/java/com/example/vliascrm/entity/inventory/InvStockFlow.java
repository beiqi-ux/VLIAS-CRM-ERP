package com.example.vliascrm.entity.inventory;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 库存流水实体类
 */
@Data
@Entity
@Table(name = "inv_stock_flow")
@EntityListeners(AuditingEntityListener.class)
public class InvStockFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 仓库ID
     */
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    /**
     * 商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * SKU ID
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * 批次号
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    /**
     * 流水类型 1-入库 2-出库 3-调拨 4-盘点 5-锁定 6-解锁
     */
    @Column(name = "flow_type", nullable = false)
    private Integer flowType;

    /**
     * 变动数量
     */
    @Column(name = "change_qty", nullable = false)
    private Integer changeQty;

    /**
     * 变动前数量
     */
    @Column(name = "before_qty")
    private Integer beforeQty;

    /**
     * 变动后数量
     */
    @Column(name = "after_qty")
    private Integer afterQty;

    /**
     * 关联订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 关联订单项ID
     */
    @Column(name = "order_item_id")
    private Long orderItemId;

    /**
     * 关联单据ID
     */
    @Column(name = "ref_id")
    private Long refId;

    /**
     * 关联单据类型
     */
    @Column(name = "ref_type", length = 50)
    private String refType;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    /**
     * 创建人ID
     */
    @Column(name = "create_by")
    private Long createBy;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;
} 