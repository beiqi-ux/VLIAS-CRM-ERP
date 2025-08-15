package com.example.vliascrm.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 供应商传输对象
 */
@Data
public class PurSupplierDto {

    /**
     * 供应商ID
     */
    private Long id;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 税号
     */
    private String taxNo;

    /**
     * 状态 0-禁用 1-正常
     */
    private Integer status;

    /**
     * 状态显示文本
     */
    private String statusText;

    /**
     * 供应商等级
     */
    private Integer level;

    /**
     * 供应商等级显示文本
     */
    private String levelText;

    /**
     * 信用等级
     */
    private Integer creditLevel;

    /**
     * 信用等级显示文本
     */
    private String creditLevelText;

    /**
     * 结算方式 1-现结 2-月结 3-季结
     */
    private Integer settlementType;

    /**
     * 结算方式显示文本
     */
    private String settlementTypeText;

    /**
     * 结算天数
     */
    private Integer settlementDay;

    /**
     * 供应商业务类型 1-镜框供应商 2-镜片供应商 3-配件供应商 4-设备供应商 5-其他
     */
    private Integer supplierType;

    /**
     * 供应商业务类型显示文本
     */
    private String supplierTypeText;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建人姓名
     */
    private String createByName;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新人姓名
     */
    private String updateByName;
} 