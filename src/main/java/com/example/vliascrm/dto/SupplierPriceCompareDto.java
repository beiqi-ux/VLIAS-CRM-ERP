package com.example.vliascrm.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 供应商价格比较数据传输对象
 */
@Data
public class SupplierPriceCompareDto {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品编码
     */
    private String goodsCode;

    /**
     * 供应商价格信息列表
     */
    private List<SupplierPriceInfo> suppliers;

    /**
     * 供应商价格信息
     */
    @Data
    public static class SupplierPriceInfo {
        
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
         * 采购价格
         */
        private BigDecimal purchasePrice;

        /**
         * 最小采购量
         */
        private Integer minPurchaseQty;

        /**
         * 交货天数
         */
        private Integer deliveryDay;

        /**
         * 是否推荐
         */
        private Boolean isRecommended;

        /**
         * 推荐理由
         */
        private String recommendReason;

        /**
         * 价格排名
         */
        private Integer priceRank;
    }
} 