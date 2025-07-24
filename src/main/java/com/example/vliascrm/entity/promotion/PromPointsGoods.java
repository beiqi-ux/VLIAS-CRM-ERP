package com.example.vliascrm.entity.promotion;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 积分商品实体类
 */
@Data
@Entity
@Table(name = "prom_points_goods")
@EqualsAndHashCode(callSuper = true)
public class PromPointsGoods extends BaseEntity {

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
     * 积分价格
     */
    @Column(name = "points_price", nullable = false)
    private Integer pointsPrice;

    /**
     * 现金价格
     */
    @Column(name = "cash_price", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal cashPrice = BigDecimal.ZERO;

    /**
     * 兑换库存
     */
    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty;

    /**
     * 每人限兑
     */
    @Column(name = "limit_qty", columnDefinition = "int default 1")
    private Integer limitQty = 1;

    /**
     * 已兑换数量
     */
    @Column(name = "exchange_qty", columnDefinition = "int default 0")
    private Integer exchangeQty = 0;

    /**
     * 开始时间
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    /**
     * 状态 0-未开始 1-进行中 2-已结束 3-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 0")
    private Integer status = 0;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

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