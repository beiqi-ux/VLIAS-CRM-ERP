package com.example.vliascrm.repository;

    import com.example.vliascrm.entity.ProdSku;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SKU数据访问接口
 */
@Repository
public interface ProdSkuRepository extends JpaRepository<ProdSku, Long>, JpaSpecificationExecutor<ProdSku> {

    /**
     * 分页查询未删除的SKU
     * @param pageable 分页参数
     * @return SKU分页列表
     */
    Page<ProdSku> findByIsDeleted(Boolean isDeleted, Pageable pageable);

    /**
     * 根据SKU编码查询
     * @param skuCode SKU编码
     * @return SKU
     */
    Optional<ProdSku> findBySkuCode(String skuCode);

    /**
     * 检查SKU编码是否存在
     * @param skuCode SKU编码
     * @return 是否存在
     */
    boolean existsBySkuCode(String skuCode);

    /**
     * 根据商品ID查询SKU列表
     * @param goodsId 商品ID
     * @return SKU列表
     */
    List<ProdSku> findByGoodsIdAndStatusAndIsDeleted(Long goodsId, Integer status, Boolean isDeleted);

    /**
     * 根据商品ID查询所有SKU（包括禁用的）
     * @param goodsId 商品ID
     * @return SKU列表
     */
    List<ProdSku> findByGoodsIdAndIsDeleted(Long goodsId, Boolean isDeleted);

    /**
     * 根据SKU名称模糊查询
     * @param skuName SKU名称
     * @return SKU列表
     */
    List<ProdSku> findBySkuNameContainingAndStatusAndIsDeleted(String skuName, Integer status, Boolean isDeleted);

    /**
     * 根据条形码查询
     * @param barcode 条形码
     * @return SKU
     */
    Optional<ProdSku> findByBarcodeAndIsDeleted(String barcode, Boolean isDeleted);

    /**
     * 查询库存预警SKU
     * @return 库存预警SKU列表
     */
    @Query("SELECT s FROM ProdSku s WHERE s.stockQty <= s.warnStock AND s.status = 1 AND s.isDeleted = false")
    List<ProdSku> findLowStockSkus();

    /**
     * 查询零库存SKU
     * @return 零库存SKU列表
     */
    List<ProdSku> findByStockQtyAndStatusAndIsDeleted(Integer stockQty, Integer status, Boolean isDeleted);

    /**
     * 统计商品下的SKU数量
     * @param goodsId 商品ID
     * @return SKU数量
     */
    long countByGoodsIdAndIsDeleted(Long goodsId, Boolean isDeleted);

    /**
     * 统计启用状态的SKU数量
     * @param goodsId 商品ID
     * @return 启用SKU数量
     */
    long countByGoodsIdAndStatusAndIsDeleted(Long goodsId, Integer status, Boolean isDeleted);

    /**
     * 根据商品ID列表查询SKU
     * @param goodsIds 商品ID列表
     * @return SKU列表
     */
    List<ProdSku> findByGoodsIdInAndStatusAndIsDeleted(List<Long> goodsIds, Integer status, Boolean isDeleted);

    /**
     * 查询热销SKU（按销量排序）
     * @param limit 限制数量
     * @return 热销SKU列表
     */
    @Query("SELECT s FROM ProdSku s WHERE s.status = 1 AND s.isDeleted = false ORDER BY s.saleQty DESC")
    List<ProdSku> findTopSellingSkus();
} 