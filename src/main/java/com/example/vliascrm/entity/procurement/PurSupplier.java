package com.example.vliascrm.entity.procurement;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 供应商实体类
 */
@Data
@Entity
@Table(name = "pur_supplier")
@EqualsAndHashCode(callSuper = true)
public class PurSupplier extends BaseEntity {

    /**
     * 供应商名称
     */
    @Column(name = "supplier_name", nullable = false, length = 100)
    private String supplierName;

    /**
     * 供应商编码
     */
    @Column(name = "supplier_code", length = 50)
    private String supplierCode;

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
     * 邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

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
    @Column(name = "address", length = 200)
    private String address;

    /**
     * 开户行
     */
    @Column(name = "bank_name", length = 100)
    private String bankName;

    /**
     * 银行账号
     */
    @Column(name = "bank_account", length = 50)
    private String bankAccount;

    /**
     * 税号
     */
    @Column(name = "tax_no", length = 50)
    private String taxNo;

    /**
     * 状态 0-禁用 1-正常
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 供应商等级
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 信用等级
     */
    @Column(name = "credit_level")
    private Integer creditLevel;

    /**
     * 结算方式 1-现结 2-月结 3-季结
     */
    @Column(name = "settlement_type")
    private Integer settlementType;

    /**
     * 结算天数
     */
    @Column(name = "settlement_day")
    private Integer settlementDay;

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