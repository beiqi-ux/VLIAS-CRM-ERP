package com.example.vliascrm.entity.promotion;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 拼团记录实体类
 */
@Data
@Entity
@Table(name = "prom_group_buy_record")
@EntityListeners(AuditingEntityListener.class)
public class PromGroupBuyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 拼团活动ID
     */
    @Column(name = "group_buy_id", nullable = false)
    private Long groupBuyId;

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
     * 团编号
     */
    @Column(name = "group_no", nullable = false, length = 50)
    private String groupNo;

    /**
     * 团长ID
     */
    @Column(name = "leader_id", nullable = false)
    private Long leaderId;

    /**
     * 需要人数
     */
    @Column(name = "required_num", nullable = false)
    private Integer requiredNum;

    /**
     * 当前人数
     */
    @Column(name = "current_num", columnDefinition = "int default 1")
    private Integer currentNum = 1;

    /**
     * 状态 1-进行中 2-已成团 3-拼团失败
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 开始时间
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * 到期时间
     */
    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    /**
     * 成团时间
     */
    @Column(name = "success_time")
    private LocalDateTime successTime;

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