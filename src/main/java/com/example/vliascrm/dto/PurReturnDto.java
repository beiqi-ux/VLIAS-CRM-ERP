package com.example.vliascrm.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 采购退货DTO
 */
@Data
public class PurReturnDto {

    /**
     * 退货单ID
     */
    private Long id;

    /**
     * 退货单号
     */
    private String returnNo;

    /**
     * 供应商ID
     */
    private Long supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 仓库ID
     */
    private Long warehouseId;

    /**
     * 仓库名称
     */
    private String warehouseName;

    /**
     * 关联入库单ID
     */
    private Long receiptId;

    /**
     * 关联入库单号
     */
    private String receiptNo;

    /**
     * 退货状态 1-草稿 2-待审核 3-已审核 4-已退货 5-已取消
     */
    private Integer returnStatus;

    /**
     * 退货状态名称
     */
    private String returnStatusName;

    /**
     * 退货日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnTime;

    /**
     * 退货总金额
     */
    private BigDecimal totalAmount;

    /**
     * 退货原因类型
     */
    private Integer reasonType;

    /**
     * 退货原因类型名称
     */
    private String reasonTypeName;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核人ID
     */
    private Long auditId;

    /**
     * 审核人名称
     */
    private String auditName;

    /**
     * 审核时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auditTime;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 创建人名称
     */
    private String createByName;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 更新人名称
     */
    private String updateByName;

    /**
     * 退货明细列表
     */
    private List<PurReturnItemDto> items;

    /**
     * 明细数量
     */
    private Integer itemCount;

    /**
     * 退货总数量
     */
    private Integer totalQuantity;
} 
 