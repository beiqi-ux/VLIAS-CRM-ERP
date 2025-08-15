package com.example.vliascrm.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购入库单DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PurReceiptDto {

    /**
     * 入库单ID
     */
    private Long id;

    /**
     * 入库单号
     */
    private String receiptNo;

    /**
     * 采购单ID
     */
    private Long orderId;

    /**
     * 采购单号
     */
    private String orderNo;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商编码
     */
    private String supplierCode;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 入库状态 1-草稿 2-待审核 3-已审核 4-已入库 5-已取消
     */
    private Integer receiptStatus;

    /**
     * 入库状态名称
     */
    private String receiptStatusName;

    /**
     * 入库类型 1-采购入库 2-退货入库 3-调拨入库 4-其他入库
     */
    private Integer receiptType;

    /**
     * 入库类型名称
     */
    private String receiptTypeName;

    /**
     * 入库日期
     */
    private LocalDate receiptTime;

    /**
     * 入库总金额
     */
    private BigDecimal totalAmount;

    /**
     * 入库人
     */
    private Long receiptUserId;

    /**
     * 入库人姓名
     */
    private String receiptUserName;

    /**
     * 联系人
     */
    private String contact;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核人ID
     */
    private Long auditId;

    /**
     * 审核人姓名
     */
    private String auditName;

    /**
     * 审核时间
     */
    private LocalDateTime auditTime;

    /**
     * 审核备注
     */
    private String auditRemark;

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

    /**
     * 入库明细列表
     */
    private List<PurReceiptItemDto> items;

    /**
     * 明细数量
     */
    private Integer itemCount;

    /**
     * 总数量
     */
    private BigDecimal totalQuantity;

    // 状态映射方法
    public String getReceiptStatusName() {
        if (receiptStatus == null) return "";
        switch (receiptStatus) {
            case 1: return "草稿";
            case 2: return "待审核";
            case 3: return "已审核";
            case 4: return "已入库";
            case 5: return "已取消";
            default: return "未知";
        }
    }

    public String getReceiptTypeName() {
        if (receiptType == null) return "";
        switch (receiptType) {
            case 1: return "采购入库";
            case 2: return "退货入库";
            case 3: return "调拨入库";
            case 4: return "其他入库";
            default: return "未知";
        }
    }
} 