package com.example.vliascrm.entity.order;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 订单物流表实体类
 */
@Data
@Entity
@Table(name = "ord_logistics")
@EntityListeners(AuditingEntityListener.class)
public class OrdLogistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
     * 物流单号
     */
    @Column(name = "delivery_no", nullable = false, length = 50)
    private String deliveryNo;

    /**
     * 物流公司
     */
    @Column(name = "delivery_company", length = 50)
    private String deliveryCompany;

    /**
     * 物流公司编码
     */
    @Column(name = "delivery_company_code", length = 50)
    private String deliveryCompanyCode;

    /**
     * 物流状态 0-待发货 1-已发货 2-已签收
     */
    @Column(name = "delivery_status", columnDefinition = "tinyint default 0")
    private Integer deliveryStatus = 0;

    /**
     * 收货人
     */
    @Column(name = "consignee", nullable = false, length = 50)
    private String consignee;

    /**
     * 联系电话
     */
    @Column(name = "mobile", nullable = false, length = 20)
    private String mobile;

    /**
     * 省份
     */
    @Column(name = "province", length = 50)
    private String province;

    /**
     * 城市
     */
    @Column(name = "city", length = 50)
    private String city;

    /**
     * 区县
     */
    @Column(name = "district", length = 50)
    private String district;

    /**
     * 详细地址
     */
    @Column(name = "address", nullable = false, length = 200)
    private String address;

    /**
     * 邮编
     */
    @Column(name = "zip_code", length = 20)
    private String zipCode;

    /**
     * 发货时间
     */
    @Column(name = "delivery_time")
    private LocalDateTime deliveryTime;

    /**
     * 收货时间
     */
    @Column(name = "receive_time")
    private LocalDateTime receiveTime;

    /**
     * 物流跟踪信息
     */
    @Column(name = "tracking_info", columnDefinition = "text")
    private String trackingInfo;

    /**
     * 物流更新时间
     */
    @Column(name = "tracking_update_time")
    private LocalDateTime trackingUpdateTime;

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