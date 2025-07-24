package com.example.vliascrm.entity.inventory;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存盘点明细实体类
 */
@Data
@Entity
@Table(name = "inv_check_item")
@EntityListeners(AuditingEntityListener.class)
public class InvCheckItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 盘点单ID
     */
    @Column(name = "check_id", nullable = false)
    private Long checkId;

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
     * 系统数量
     */
    @Column(name = "system_qty", columnDefinition = "int default 0")
    private Integer systemQty = 0;

    /**
     * 实际数量
     */
    @Column(name = "actual_qty", columnDefinition = "int default 0")
    private Integer actualQty = 0;

    /**
     * 差异数量
     */
    @Column(name = "diff_qty", columnDefinition = "int default 0")
    private Integer diffQty = 0;

    /**
     * 成本价
     */
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

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