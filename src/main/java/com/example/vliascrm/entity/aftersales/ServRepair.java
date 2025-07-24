package com.example.vliascrm.entity.aftersales;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 维修记录实体类
 */
@Data
@Entity
@Table(name = "serv_repair")
@EqualsAndHashCode(callSuper = true)
public class ServRepair extends BaseEntity {

    /**
     * 维修单号
     */
    @Column(name = "repair_no", nullable = false, length = 50)
    private String repairNo;

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
     * 商品名称
     */
    @Column(name = "goods_name", nullable = false, length = 100)
    private String goodsName;

    /**
     * 商品图片
     */
    @Column(name = "goods_image", length = 255)
    private String goodsImage;

    /**
     * 维修类型 1-保修 2-有偿维修
     */
    @Column(name = "repair_type", nullable = false)
    private Integer repairType;

    /**
     * 问题描述
     */
    @Column(name = "problem_desc", columnDefinition = "text")
    private String problemDesc;

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
     * 寄修地址
     */
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 物流单号
     */
    @Column(name = "logistics_no", length = 50)
    private String logisticsNo;

    /**
     * 物流公司
     */
    @Column(name = "logistics_company", length = 50)
    private String logisticsCompany;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private Long orderId;

    /**
     * 订单编号
     */
    @Column(name = "order_no", length = 50)
    private String orderNo;

    /**
     * 状态 1-待确认 2-维修中 3-维修完成 4-已寄回 5-已取消
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 维修结果
     */
    @Column(name = "repair_result", columnDefinition = "text")
    private String repairResult;

    /**
     * 维修费用
     */
    @Column(name = "repair_fee", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal repairFee = BigDecimal.ZERO;

    /**
     * 维修完成时间
     */
    @Column(name = "complete_time")
    private LocalDateTime completeTime;

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
     * 寄回时间
     */
    @Column(name = "return_time")
    private LocalDateTime returnTime;

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