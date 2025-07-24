package com.example.vliascrm.entity.finance;

import com.example.vliascrm.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 账户实体类
 */
@Data
@Entity
@Table(name = "fin_account")
@EqualsAndHashCode(callSuper = true)
public class FinAccount extends BaseEntity {

    /**
     * 账户名称
     */
    @Column(name = "account_name", nullable = false, length = 50)
    private String accountName;

    /**
     * 账户类型 1-现金 2-银行账户 3-微信 4-支付宝 5-其他
     */
    @Column(name = "account_type", nullable = false)
    private Integer accountType;

    /**
     * 账号
     */
    @Column(name = "account_no", length = 50)
    private String accountNo;

    /**
     * 开户行
     */
    @Column(name = "bank_name", length = 100)
    private String bankName;

    /**
     * 开户人
     */
    @Column(name = "account_holder", length = 50)
    private String accountHolder;

    /**
     * 期初余额
     */
    @Column(name = "initial_balance", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal initialBalance = BigDecimal.ZERO;

    /**
     * 当前余额
     */
    @Column(name = "current_balance", precision = 10, scale = 2, columnDefinition = "decimal(10,2) default 0")
    private BigDecimal currentBalance = BigDecimal.ZERO;

    /**
     * 状态 0-禁用 1-启用
     */
    @Column(name = "status", columnDefinition = "tinyint default 1")
    private Integer status = 1;

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