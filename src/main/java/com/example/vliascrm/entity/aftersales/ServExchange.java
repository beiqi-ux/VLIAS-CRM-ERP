package com.example.vliascrm.entity.aftersales;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 换货申请实体类
 */
@Data
@Entity
@Table(name = "serv_exchange")
@EqualsAndHashCode(callSuper = true)
public class ServExchange extends BaseEntity {

    /**
     * 换货单号
     */
    @Column(name = "exchange_no", nullable = false, length = 50)
    private String exchangeNo;

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 订单ID
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 订单编号
     */
    @Column(name = "order_no", nullable = false, length = 50)
    private String orderNo;

    /**
     * 商品ID
     */
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    /**
     * 商品名称
     */
    @Column(name = "goods_name", nullable = false, length = 100)
    private String goodsName;

    /**
     * SKU ID
     */
    @Column(name = "sku_id")
    private Long skuId;

    /**
     * SKU名称
     */
    @Column(name = "sku_name", length = 100)
    private String skuName;

    /**
     * 换货原因
     */
    @Column(name = "reason", length = 200)
    private String reason;

    /**
     * 换货说明
     */
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * 换货图片，多个以逗号分隔
     */
    @Column(name = "images", length = 500)
    private String images;

    /**
     * 状态 1-待审核 2-已同意 3-已拒绝 4-已寄回 5-已收货 6-已发出 7-已完成 8-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 审核备注
     */
    @Column(name = "audit_remark", length = 200)
    private String auditRemark;

    /**
     * 审核时间
     */
    @Column(name = "audit_time")
    private LocalDateTime auditTime;

    /**
     * 审核人ID
     */
    @Column(name = "audit_user_id")
    private Long auditUserId;

    /**
     * 寄回时间
     */
    @Column(name = "return_time")
    private LocalDateTime returnTime;

    /**
     * 寄回物流单号
     */
    @Column(name = "return_logistics_no", length = 50)
    private String returnLogisticsNo;

    /**
     * 寄回物流公司
     */
    @Column(name = "return_logistics_company", length = 50)
    private String returnLogisticsCompany;

    /**
     * 收货时间
     */
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

    /**
     * 收货备注
     */
    @Column(name = "receive_remark", length = 200)
    private String receiveRemark;

    /**
     * 收货人ID
     */
    @Column(name = "receive_user_id")
    private Long receiveUserId;

    /**
     * 发出时间
     */
    @Column(name = "send_time")
    private LocalDateTime sendTime;

    /**
     * 发出物流单号
     */
    @Column(name = "send_logistics_no", length = 50)
    private String sendLogisticsNo;

    /**
     * 发出物流公司
     */
    @Column(name = "send_logistics_company", length = 50)
    private String sendLogisticsCompany;

    /**
     * 完成时间
     */
    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    /**
     * 联系人
     */
    @Column(name = "contact", length = 50)
    private String contact;

    /**
     * 联系电话
     */
    @Column(name = "mobile", length = 20)
    private String mobile;

    /**
     * 收货地址
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 备注
     */
    @Column(name = "remark", columnDefinition = "text")
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