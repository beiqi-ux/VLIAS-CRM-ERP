package com.example.vliascrm.entity.i18n;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 币种实体类
 */
@Data
@Entity
@Table(name = "sys_currency")
@EqualsAndHashCode(callSuper = true)
public class SysCurrency extends BaseEntity {

    /**
     * 币种代码
     */
    @Column(name = "currency_code", nullable = false, length = 10)
    private String currencyCode;

    /**
     * 币种名称
     */
    @Column(name = "currency_name", nullable = false, length = 50)
    private String currencyName;

    /**
     * 币种符号
     */
    @Column(name = "currency_symbol", length = 10)
    private String currencySymbol;

    /**
     * 小数位数
     */
    @Column(name = "decimal_places", columnDefinition = "int default 2")
    private Integer decimalPlaces = 2;

    /**
     * 排序
     */
    @Column(name = "sort", columnDefinition = "int default 0")
    private Integer sort = 0;

    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

    /**
     * 是否默认 0-否 1-是
     */
    @Column(name = "is_default", columnDefinition = "tinyint default 0")
    private Integer isDefault = 0;

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