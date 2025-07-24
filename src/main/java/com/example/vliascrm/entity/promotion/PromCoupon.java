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
 * 优惠券实体类
 */
@Data
@Entity
@Table(name = "prom_coupon")
@EqualsAndHashCode(callSuper = true)
public class PromCoupon extends BaseEntity {

    /**
     * 优惠券名称
     */
    @Column(name = "coupon_name", nullable = false, length = 100)
    private String couponName;

    /**
     * 优惠券类型 1-满减券 2-折扣券 3-无门槛券
     */
    @Column(name = "coupon_type", columnDefinition = "tinyint default 1")
    private Integer couponType = 1;

    /**
     * 优惠方式 1-固定金额 2-折扣率
     */
    @Column(name = "discount_type", columnDefinition = "tinyint default 1")
    private Integer discountType = 1;

    /**
     * 优惠值（金额/折扣）
     */
    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    /**
     * 使用门槛金额
     */
    @Column(name = "min_amount", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal minAmount = BigDecimal.ZERO;

    /**
     * 最大优惠金额
     */
    @Column(name = "max_discount", precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 有效天数（领取后）
     */
    @Column(name = "validity_days")
    private Integer validityDays;

    /**
     * 发行总量
     */
    @Column(name = "total_qty")
    private Integer totalQty;

    /**
     * 已使用数量
     */
    @Column(name = "used_qty", columnDefinition = "int default 0")
    private Integer usedQty = 0;

    /**
     * 已领取数量
     */
    @Column(name = "receive_qty", columnDefinition = "int default 0")
    private Integer receiveQty = 0;

    /**
     * 每人限领
     */
    @Column(name = "per_limit", columnDefinition = "int default 1")
    private Integer perLimit = 1;

    /**
     * 使用范围 1-全场通用 2-指定商品 3-指定分类
     */
    @Column(name = "use_range", columnDefinition = "tinyint default 1")
    private Integer useRange = 1;

    /**
     * 范围值JSON
     */
    @Column(name = "range_values", columnDefinition = "text")
    private String rangeValues;

    /**
     * 状态 0-未启用 1-已启用 2-已结束
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 使用说明
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

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