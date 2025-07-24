package com.example.vliascrm.entity.inventory;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 批次/序列号实体类
 */
@Data
@Entity
@Table(name = "inv_batch")
@EntityListeners(AuditingEntityListener.class)
public class InvBatch {

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
     * 批次号/序列号
     */
    @Column(name = "batch_number", nullable = false, length = 50)
    private String batchNumber;

    /**
     * 库存数量
     */
    @Column(name = "stock_qty", columnDefinition = "int default 0")
    private Integer stockQty = 0;

    /**
     * 生产日期
     */
    @Column(name = "production_date")
    private LocalDate productionDate;

    /**
     * 到期日期
     */
    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    /**
     * 入库时间
     */
    @Column(name = "entry_time")
    private LocalDateTime entryTime;

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