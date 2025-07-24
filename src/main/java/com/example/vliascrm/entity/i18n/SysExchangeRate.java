package com.example.vliascrm.entity.i18n;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 汇率实体类
 */
@Data
@Entity
@Table(name = "sys_exchange_rate")
@EqualsAndHashCode(callSuper = true)
public class SysExchangeRate extends BaseEntity {

    /**
     * 源币种代码
     */
    @Column(name = "source_currency", nullable = false, length = 10)
    private String sourceCurrency;

    /**
     * 目标币种代码
     */
    @Column(name = "target_currency", nullable = false, length = 10)
    private String targetCurrency;

    /**
     * 汇率
     */
    @Column(name = "exchange_rate", nullable = false, precision = 15, scale = 6)
    private BigDecimal exchangeRate;

    /**
     * 生效时间
     */
    @Column(name = "effective_time", nullable = false)
    private LocalDateTime effectiveTime;

    /**
     * 失效时间
     */
    @Column(name = "expiry_time")
    private LocalDateTime expiryTime;

    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 是否自动更新 0-否 1-是
     */
    @Column(name = "auto_update", columnDefinition = "tinyint default 0")
    private Integer autoUpdate = 0;

    /**
     * 上次更新时间
     */
    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime;

    /**
     * 数据来源 1-手动录入 2-API获取
     */
    @Column(name = "data_source", columnDefinition = "tinyint default 1")
    private Integer dataSource = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
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