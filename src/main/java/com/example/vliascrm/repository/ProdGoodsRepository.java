package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品数据访问接口
 */
@Repository
public interface ProdGoodsRepository extends JpaRepository<ProdGoods, Long>, JpaSpecificationExecutor<ProdGoods> {

    /**
     * 根据商品编码查询
     * @param goodsCode 商品编码
     * @return 商品
     */
    Optional<ProdGoods> findByGoodsCode(String goodsCode);

    /**
     * 检查商品编码是否存在
     * @param goodsCode 商品编码
     * @return 是否存在
     */
    boolean existsByGoodsCode(String goodsCode);

    /**
     * 检查除了指定ID外是否存在相同的商品编码
     * @param goodsCode 商品编码
     * @param id 要排除的商品ID
     * @return 是否存在
     */
    boolean existsByGoodsCodeAndIdNot(String goodsCode, Long id);

    /**
     * 根据分类ID查询商品列表
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<ProdGoods> findByCategoryIdAndStatusAndIsDeleted(Long categoryId, Integer status, Boolean isDeleted);

    /**
     * 根据品牌ID查询商品列表
     * @param brandId 品牌ID
     * @return 商品列表
     */
    List<ProdGoods> findByBrandIdAndStatusAndIsDeleted(Long brandId, Integer status, Boolean isDeleted);

    /**
     * 根据商品名称模糊查询
     * @param goodsName 商品名称
     * @return 商品列表
     */
    List<ProdGoods> findByGoodsNameContainingAndStatusAndIsDeleted(String goodsName, Integer status, Boolean isDeleted);

    /**
     * 根据审核状态查询
     * @param auditStatus 审核状态
     * @return 商品列表
     */
    List<ProdGoods> findByAuditStatusAndIsDeleted(Integer auditStatus, Boolean isDeleted);

    /**
     * 查询推荐商品
     * @return 推荐商品列表
     */
    List<ProdGoods> findByIsRecommendedAndStatusAndIsDeletedOrderBySortAsc(Integer isRecommended, Integer status, Boolean isDeleted);

    /**
     * 查询热销商品
     * @return 热销商品列表
     */
    List<ProdGoods> findByIsHotAndStatusAndIsDeletedOrderBySaleQtyDesc(Integer isHot, Integer status, Boolean isDeleted);

    /**
     * 查询新品
     * @return 新品列表
     */
    List<ProdGoods> findByIsNewAndStatusAndIsDeletedOrderByCreateTimeDesc(Integer isNew, Integer status, Boolean isDeleted);

    /**
     * 查询库存预警商品
     * @return 库存预警商品列表
     */
    @Query("SELECT g FROM ProdGoods g WHERE g.stockQty <= g.warnStock AND g.status = 1 AND g.isDeleted = false")
    List<ProdGoods> findLowStockGoods();

    /**
     * 统计分类下的商品数量
     * @param categoryId 分类ID
     * @return 商品数量
     */
    long countByCategoryIdAndIsDeleted(Long categoryId, Boolean isDeleted);

    /**
     * 统计品牌下的商品数量
     * @param brandId 品牌ID
     * @return 商品数量
     */
    long countByBrandIdAndIsDeleted(Long brandId, Boolean isDeleted);
} 