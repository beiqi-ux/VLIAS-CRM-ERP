package com.example.vliascrm.entity.inventory;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存实体类
 */
@Data
@Entity
@Table(name = "inv_stock")
public class InvStock {

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
     * 库存数量
     */
    @Column(name = "stock_qty", columnDefinition = "int default 0")
    private Integer stockQty = 0;

    /**
     * 锁定数量
     */
    @Column(name = "lock_qty", columnDefinition = "int default 0")
    private Integer lockQty = 0;

    /**
     * 可用数量
     */
    @Column(name = "available_qty", columnDefinition = "int default 0")
    private Integer availableQty = 0;

    /**
     * 成本价
     */
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;
} 