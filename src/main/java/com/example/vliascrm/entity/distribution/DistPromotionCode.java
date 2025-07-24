package com.example.vliascrm.entity.distribution;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 推广码实体类
 */
@Data
@Entity
@Table(name = "dist_promotion_code")
@EqualsAndHashCode(callSuper = true)
public class DistPromotionCode extends BaseEntity {

    /**
     * 分销员ID
     */
    @Column(name = "distributor_id", nullable = false)
    private Long distributorId;

    /**
     * 推广码
     */
    @Column(name = "code", nullable = false, length = 20)
    private String code;

    /**
     * 推广类型 1-普通推广 2-商品推广 3-活动推广
     */
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 关联ID
     */
    @Column(name = "target_id")
    private Long targetId;

    /**
     * 二维码图片地址
     */
    @Column(name = "qrcode_url", length = 255)
    private String qrcodeUrl;

    /**
     * 推广链接
     */
    @Column(name = "link_url", length = 255)
    private String linkUrl;

    /**
     * 有效期
     */
    @Column(name = "expire_time")
    private LocalDateTime expireTime;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 扫码次数
     */
    @Column(name = "scan_count", columnDefinition = "int default 0")
    private Integer scanCount = 0;

    /**
     * 转化次数
     */
    @Column(name = "convert_count", columnDefinition = "int default 0")
    private Integer convertCount = 0;

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