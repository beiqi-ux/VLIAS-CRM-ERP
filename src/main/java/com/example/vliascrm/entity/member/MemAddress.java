package com.example.vliascrm.entity.member;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员收货地址实体类
 */
@Data
@Entity
@Table(name = "mem_address")
@EqualsAndHashCode(callSuper = true)
public class MemAddress extends BaseEntity {

    /**
     * 会员ID
     */
    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /**
     * 收货人
     */
    @Column(name = "consignee", nullable = false, length = 50)
    private String consignee;

    /**
     * 手机号
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
    @Column(name = "detail_address", nullable = false, length = 200)
    private String detailAddress;

    /**
     * 邮编
     */
    @Column(name = "zip_code", length = 20)
    private String zipCode;

    /**
     * 是否默认 0-否 1-是
     */
    @Column(name = "is_default", columnDefinition = "tinyint default 0")
    private Integer isDefault = 0;
} 