package com.example.vliascrm.service;

import com.example.vliascrm.entity.ProdSku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * SKU服务接口
 */
public interface ProdSkuService {

    /**
     * 根据ID查询SKU
     * @param id SKU ID
     * @return SKU对象
     */
    Optional<ProdSku> findById(Long id);

    /**
     * 根据SKU编码查询SKU
     * @param skuCode SKU编码
     * @return SKU对象
     */
    Optional<ProdSku> findBySkuCode(String skuCode);

    /**
     * 查询所有SKU（分页）
     * @param pageable 分页参数
     * @return SKU分页列表
     */
    Page<ProdSku> findAll(Pageable pageable);

    /**
     * 根据商品ID查询SKU列表
     * @param goodsId 商品ID
     * @return SKU列表
     */
    List<ProdSku> findByGoodsId(Long goodsId);

    /**
     * 根据商品ID查询启用的SKU列表
     * @param goodsId 商品ID
     * @return 启用的SKU列表
     */
    List<ProdSku> findActiveByGoodsId(Long goodsId);

    /**
     * 根据SKU名称模糊查询
     * @param skuName SKU名称
     * @return SKU列表
     */
    List<ProdSku> findBySkuNameContaining(String skuName);

    /**
     * 根据条形码查询SKU
     * @param barcode 条形码
     * @return SKU对象
     */
    Optional<ProdSku> findByBarcode(String barcode);

    /**
     * 查询库存预警SKU
     * @return 库存预警SKU列表
     */
    List<ProdSku> findLowStockSkus();

    /**
     * 查询零库存SKU
     * @return 零库存SKU列表
     */
    List<ProdSku> findZeroStockSkus();

    /**
     * 查询热销SKU
     * @return 热销SKU列表
     */
    List<ProdSku> findTopSellingSkus();

    /**
     * 保存SKU
     * @param sku SKU对象
     * @return 保存后的SKU对象
     */
    ProdSku save(ProdSku sku);

    /**
     * 更新SKU
     * @param sku SKU对象
     * @return 更新后的SKU对象
     */
    ProdSku update(ProdSku sku);

    /**
     * 删除SKU（软删除）
     * @param id SKU ID
     */
    void deleteById(Long id);

    /**
     * 批量删除SKU（软删除）
     * @param ids SKU ID列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 启用SKU
     * @param id SKU ID
     */
    void enableSku(Long id);

    /**
     * 禁用SKU
     * @param id SKU ID
     */
    void disableSku(Long id);

    /**
     * 更新库存
     * @param id SKU ID
     * @param stockQty 库存数量
     */
    void updateStock(Long id, Integer stockQty);

    /**
     * 增加库存
     * @param id SKU ID
     * @param quantity 增加数量
     */
    void addStock(Long id, Integer quantity);

    /**
     * 减少库存
     * @param id SKU ID
     * @param quantity 减少数量
     */
    void reduceStock(Long id, Integer quantity);

    /**
     * 更新销量
     * @param id SKU ID
     * @param saleQty 销量
     */
    void updateSaleQty(Long id, Integer saleQty);

    /**
     * 检查SKU编码是否存在
     * @param skuCode SKU编码
     * @return 是否存在
     */
    boolean existsBySkuCode(String skuCode);

    /**
     * 统计商品下的SKU数量
     * @param goodsId 商品ID
     * @return SKU数量
     */
    long countByGoodsId(Long goodsId);

    /**
     * 统计商品下启用的SKU数量
     * @param goodsId 商品ID
     * @return 启用SKU数量
     */
    long countActiveByGoodsId(Long goodsId);

    /**
     * 批量创建SKU
     * @param skus SKU列表
     * @return 创建后的SKU列表
     */
    List<ProdSku> batchSave(List<ProdSku> skus);
} 