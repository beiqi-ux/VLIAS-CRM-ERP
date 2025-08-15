package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurSupplierGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 供应商商品关联数据访问接口
 */
@Repository
public interface PurSupplierGoodsRepository extends JpaRepository<PurSupplierGoods, Long>, JpaSpecificationExecutor<PurSupplierGoods> {

    /**
     * 根据供应商ID查询商品列表
     * @param supplierId 供应商ID
     * @return 供应商商品列表
     */
    List<PurSupplierGoods> findBySupplierId(Long supplierId);

    /**
     * 根据商品ID查询供应商列表
     * @param goodsId 商品ID
     * @return 供应商商品列表
     */
    List<PurSupplierGoods> findByGoodsId(Long goodsId);

    /**
     * 根据商品ID和删除状态查询供应商列表
     * @param goodsId 商品ID
     * @param isDeleted 是否删除
     * @return 供应商商品列表
     */
    List<PurSupplierGoods> findByGoodsIdAndIsDeleted(Long goodsId, Integer isDeleted);

    /**
     * 查询供应商商品是否存在
     * @param supplierId 供应商ID
     * @param goodsId 商品ID
     * @param skuId SKU ID
     * @return 是否存在
     */
    boolean existsBySupplierIdAndGoodsIdAndSkuId(Long supplierId, Long goodsId, Long skuId);

    /**
     * 根据供应商ID和商品ID查询
     * @param supplierId 供应商ID
     * @param goodsId 商品ID
     * @return 供应商商品信息
     */
    Optional<PurSupplierGoods> findBySupplierIdAndGoodsId(Long supplierId, Long goodsId);

    /**
     * 分页查询供应商商品（带联表查询）
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT new com.example.vliascrm.dto.PurSupplierGoodsDto(" +
           "sg.id, sg.supplierId, s.supplierName, s.supplierCode, " +
           "sg.goodsId, g.goodsName, g.goodsCode, " +
           "sg.skuId, sku.skuName, sg.supplierGoodsCode, sg.supplierGoodsName, " +
           "sg.purchasePrice, sg.minPurchaseQty, sg.deliveryDay, " +
           "sg.createTime, sg.updateTime) " +
           "FROM PurSupplierGoods sg " +
           "LEFT JOIN PurSupplier s ON sg.supplierId = s.id " +
           "LEFT JOIN ProdGoods g ON sg.goodsId = g.id " +
           "LEFT JOIN ProdSku sku ON sg.skuId = sku.id " +
           "WHERE sg.isDeleted = 0 " +
           "ORDER BY sg.createTime DESC")
    Page<com.example.vliascrm.dto.PurSupplierGoodsDto> findPageWithDetails(Pageable pageable);

    /**
     * 条件查询供应商商品（带联表查询）
     * @param supplierId 供应商ID
     * @param goodsId 商品ID
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT new com.example.vliascrm.dto.PurSupplierGoodsDto(" +
           "sg.id, sg.supplierId, s.supplierName, s.supplierCode, " +
           "sg.goodsId, g.goodsName, g.goodsCode, " +
           "sg.skuId, sku.skuName, sg.supplierGoodsCode, sg.supplierGoodsName, " +
           "sg.purchasePrice, sg.minPurchaseQty, sg.deliveryDay, " +
           "sg.createTime, sg.updateTime) " +
           "FROM PurSupplierGoods sg " +
           "LEFT JOIN PurSupplier s ON sg.supplierId = s.id " +
           "LEFT JOIN ProdGoods g ON sg.goodsId = g.id " +
           "LEFT JOIN ProdSku sku ON sg.skuId = sku.id " +
           "WHERE (:supplierId IS NULL OR sg.supplierId = :supplierId) " +
           "AND (:goodsId IS NULL OR sg.goodsId = :goodsId) " +
           "AND sg.isDeleted = 0 " +
           "ORDER BY sg.createTime DESC")
    Page<com.example.vliascrm.dto.PurSupplierGoodsDto> findPageWithDetailsByConditions(
            @Param("supplierId") Long supplierId,
            @Param("goodsId") Long goodsId,
            Pageable pageable);



    /**
     * 查询商品的所有供应商（用于价格比较）
     * @param goodsId 商品ID
     * @return 供应商商品列表
     */
    @Query("SELECT new com.example.vliascrm.dto.PurSupplierGoodsDto(" +
           "sg.id, sg.supplierId, s.supplierName, s.supplierCode, " +
           "sg.goodsId, g.goodsName, g.goodsCode, " +
           "sg.skuId, sku.skuName, sg.supplierGoodsCode, sg.supplierGoodsName, " +
           "sg.purchasePrice, sg.minPurchaseQty, sg.deliveryDay, " +
           "sg.createTime, sg.updateTime) " +
           "FROM PurSupplierGoods sg " +
           "LEFT JOIN PurSupplier s ON sg.supplierId = s.id " +
           "LEFT JOIN ProdGoods g ON sg.goodsId = g.id " +
           "LEFT JOIN ProdSku sku ON sg.skuId = sku.id " +
           "WHERE sg.goodsId = :goodsId AND sg.isDeleted = 0 " +
           "ORDER BY sg.purchasePrice ASC")
    List<com.example.vliascrm.dto.PurSupplierGoodsDto> findSuppliersByGoodsIdForCompare(@Param("goodsId") Long goodsId);

    /**
     * 条件查询供应商商品（带联表查询和商品名称模糊搜索）
     * @param supplierId 供应商ID
     * @param goodsId 商品ID
     * @param goodsName 商品名称（模糊查询）
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT new com.example.vliascrm.dto.PurSupplierGoodsDto(" +
           "sg.id, sg.supplierId, s.supplierName, s.supplierCode, " +
           "sg.goodsId, g.goodsName, g.goodsCode, " +
           "sg.skuId, sku.skuName, sg.supplierGoodsCode, sg.supplierGoodsName, " +
           "sg.purchasePrice, sg.minPurchaseQty, sg.deliveryDay, " +
           "sg.createTime, sg.updateTime) " +
           "FROM PurSupplierGoods sg " +
           "LEFT JOIN PurSupplier s ON sg.supplierId = s.id " +
           "LEFT JOIN ProdGoods g ON sg.goodsId = g.id " +
           "LEFT JOIN ProdSku sku ON sg.skuId = sku.id " +
           "WHERE (:supplierId IS NULL OR sg.supplierId = :supplierId) " +
           "AND (:goodsId IS NULL OR sg.goodsId = :goodsId) " +
           "AND (:goodsName IS NULL OR g.goodsName LIKE %:goodsName% OR sg.supplierGoodsName LIKE %:goodsName%) " +
           "AND sg.isDeleted = 0 " +
           "ORDER BY sg.createTime DESC")
    Page<com.example.vliascrm.dto.PurSupplierGoodsDto> findPageWithDetailsByConditionsAndName(
            @Param("supplierId") Long supplierId,
            @Param("goodsId") Long goodsId,
            @Param("goodsName") String goodsName,
            Pageable pageable);

    /**
     * 查询供应商的商品数量
     * @param supplierId 供应商ID
     * @return 商品数量
     */
    long countBySupplierId(Long supplierId);

    /**
     * 查询商品的供应商数量
     * @param goodsId 商品ID
     * @return 供应商数量
     */
    long countByGoodsId(Long goodsId);

    /**
     * 根据商品名称搜索供应商商品
     * @param goodsName 商品名称
     * @param supplierId 供应商ID（可选）
     * @return 供应商商品列表
     */
    @Query("SELECT new com.example.vliascrm.dto.PurSupplierGoodsDto(" +
           "sg.id, sg.supplierId, s.supplierName, s.supplierCode, " +
           "sg.goodsId, g.goodsName, g.goodsCode, " +
           "sg.skuId, sku.skuName, sg.supplierGoodsCode, sg.supplierGoodsName, " +
           "sg.purchasePrice, sg.minPurchaseQty, sg.deliveryDay, " +
           "sg.createTime, sg.updateTime) " +
           "FROM PurSupplierGoods sg " +
           "LEFT JOIN PurSupplier s ON sg.supplierId = s.id " +
           "LEFT JOIN ProdGoods g ON sg.goodsId = g.id " +
           "LEFT JOIN ProdSku sku ON sg.skuId = sku.id " +
           "WHERE (g.goodsName LIKE %:goodsName% OR sg.supplierGoodsName LIKE %:goodsName%) " +
           "AND (:supplierId IS NULL OR sg.supplierId = :supplierId) " +
           "AND sg.isDeleted = 0 " +
           "ORDER BY sg.createTime DESC")
    List<com.example.vliascrm.dto.PurSupplierGoodsDto> findSupplierGoodsByGoodsName(
            @Param("goodsName") String goodsName,
            @Param("supplierId") Long supplierId);

    /**
     * 查询供应商商品关联信息（带详细信息）
     * @param supplierId 供应商ID（可选）
     * @param goodsId 商品ID（可选）
     * @param goodsName 商品名称（可选）
     * @param pageable 分页参数
     * @return 分页结果
     */
    @Query("SELECT new com.example.vliascrm.dto.PurSupplierGoodsDto(" +
            "sg.id, sg.supplierId, s.supplierName, s.supplierCode, " +
            "sg.goodsId, g.goodsName, g.goodsCode, " +
            "sg.skuId, sku.skuName, " +
            "sg.supplierGoodsCode, sg.supplierGoodsName, " +
            "sg.purchasePrice, sg.minPurchaseQty, sg.deliveryDay, " +
            "sg.createTime, sg.updateTime) " +
            "FROM PurSupplierGoods sg " +
            "LEFT JOIN PurSupplier s ON sg.supplierId = s.id " +
            "LEFT JOIN ProdGoods g ON sg.goodsId = g.id " +
            "LEFT JOIN ProdSku sku ON sg.skuId = sku.id " +
            "WHERE sg.isDeleted = 0 " +
            "AND (:supplierId IS NULL OR sg.supplierId = :supplierId) " +
            "AND (:goodsId IS NULL OR sg.goodsId = :goodsId) " +
            "AND (:goodsName IS NULL OR g.goodsName LIKE %:goodsName%) " +
            "ORDER BY sg.createTime DESC")
    Page<com.example.vliascrm.dto.PurSupplierGoodsDto> findAllWithDetailsByConditions(
            @Param("supplierId") Long supplierId,
            @Param("goodsId") Long goodsId,
            @Param("goodsName") String goodsName,
            Pageable pageable);

    /**
     * 导出查询所有供应商商品（不分页）
     * @param supplierId 供应商ID（可选）
     * @param goodsId 商品ID（可选）
     * @param goodsName 商品名称（可选）
     * @return 供应商商品列表
     */
    @Query("SELECT new com.example.vliascrm.dto.PurSupplierGoodsDto(" +
            "sg.id, sg.supplierId, s.supplierName, s.supplierCode, " +
            "sg.goodsId, g.goodsName, g.goodsCode, " +
            "sg.skuId, sku.skuName, " +
            "sg.supplierGoodsCode, sg.supplierGoodsName, " +
            "sg.purchasePrice, sg.minPurchaseQty, sg.deliveryDay, " +
            "sg.createTime, sg.updateTime) " +
            "FROM PurSupplierGoods sg " +
            "LEFT JOIN PurSupplier s ON sg.supplierId = s.id " +
            "LEFT JOIN ProdGoods g ON sg.goodsId = g.id " +
            "LEFT JOIN ProdSku sku ON sg.skuId = sku.id " +
            "WHERE sg.isDeleted = 0 " +
            "AND (:supplierId IS NULL OR sg.supplierId = :supplierId) " +
            "AND (:goodsId IS NULL OR sg.goodsId = :goodsId) " +
            "AND (:goodsName IS NULL OR g.goodsName LIKE %:goodsName%) " +
            "ORDER BY sg.createTime DESC")
    List<com.example.vliascrm.dto.PurSupplierGoodsDto> findAllWithDetailsByConditionsForExport(
            @Param("supplierId") Long supplierId,
            @Param("goodsId") Long goodsId,
            @Param("goodsName") String goodsName);
} 