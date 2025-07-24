package com.example.vliascrm.entity.product;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 商品评价实体类
 */
@Data
@Entity
@Table(name = "prod_review")
@EqualsAndHashCode(callSuper = true)
public class ProdReview extends BaseEntity {

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 订单明细ID
     */
    @Column(name = "order_item_id")
    private Long orderItemId;

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
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 评价内容
     */
    @Column(name = "content", columnDefinition = "text")
    private String content;

    /**
     * 评价图片，多个以逗号分隔
     */
    @Column(name = "images", length = 500)
    private String images;

    /**
     * 评分 1-5
     */
    @Column(name = "star", columnDefinition = "tinyint default 5")
    private Integer star = 5;

    /**
     * 是否匿名 0-否 1-是
     */
    @Column(name = "is_anonymous", columnDefinition = "tinyint default 0")
    private Integer isAnonymous = 0;

    /**
     * 状态 0-未审核 1-已审核 2-拒绝
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 商家回复
     */
    @Column(name = "reply_content", columnDefinition = "text")
    private String replyContent;

    /**
     * 回复时间
     */
    @Column(name = "reply_time")
    private LocalDateTime replyTime;

    /**
     * 回复人ID
     */
    @Column(name = "reply_user_id")
    private Long replyUserId;
} 