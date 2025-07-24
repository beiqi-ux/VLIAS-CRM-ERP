package com.example.vliascrm.entity.order;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 购物车表实体类
 */
@Data
@Entity
@Table(name = "ord_cart")
@EntityListeners(AuditingEntityListener.class)
public class OrdCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

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
     * 数量
     */
    @Column(name = "quantity", columnDefinition = "int default 1")
    private Integer quantity = 1;

    /**
     * 是否选中 0-未选中 1-已选中
     */
    @Column(name = "checked", columnDefinition = "tinyint default 1")
    private Integer checked = 1;

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