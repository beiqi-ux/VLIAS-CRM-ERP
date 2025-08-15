package com.example.vliascrm.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购退货明细实体
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "pur_return_item")
public class PurReturnItem {

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 退货单ID
     */
    @Column(name = "return_id", nullable = false)
    private Long returnId;

    /**
     * 退货单号
     */
    @Column(name = "return_no", nullable = false, length = 50)
    private String returnNo;

    /**
     * 关联入库单ID
     */
    @Column(name = "receipt_id")
    private Long receiptId;

    /**
     * 关联入库单项ID
     */
    @Column(name = "receipt_item_id")
    private Long receiptItemId;

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
     * 商品名称
     */
    @Column(name = "goods_name", nullable = false, length = 100)
    private String goodsName;

    /**
     * SKU名称
     */
    @Column(name = "sku_name", length = 100)
    private String skuName;

    /**
     * 商品规格
     */
    @Column(name = "goods_spec", length = 200)
    private String goodsSpec;

    /**
     * 单位
     */
    @Column(name = "goods_unit", length = 20)
    private String goodsUnit;

    /**
     * 批次号
     */
    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    /**
     * 采购价
     */
    @Column(name = "purchase_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal purchasePrice;

    /**
     * 退货数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 总金额
     */
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
        if (totalAmount == null && purchasePrice != null && quantity != null) {
            totalAmount = purchasePrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
        if (totalAmount == null && purchasePrice != null && quantity != null) {
            totalAmount = purchasePrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
} 