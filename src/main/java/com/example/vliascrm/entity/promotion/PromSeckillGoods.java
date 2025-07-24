package com.example.vliascrm.entity.promotion;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 秒杀商品实体类
 */
@Data
@Entity
@Table(name = "prom_seckill_goods")
@EntityListeners(AuditingEntityListener.class)
public class PromSeckillGoods {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 秒杀活动ID
     */
    @Column(name = "seckill_id", nullable = false)
    private Long seckillId;

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
     * 秒杀价格
     */
    @Column(name = "seckill_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal seckillPrice;

    /**
     * 秒杀库存
     */
    @Column(name = "stock_qty", nullable = false)
    private Integer stockQty;

    /**
     * 每人限购
     */
    @Column(name = "limit_qty", columnDefinition = "int default 1")
    private Integer limitQty = 1;

    /**
     * 已售数量
     */
    @Column(name = "sold_qty", columnDefinition = "int default 0")
    private Integer soldQty = 0;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private LocalDateTime updateTime;
} 