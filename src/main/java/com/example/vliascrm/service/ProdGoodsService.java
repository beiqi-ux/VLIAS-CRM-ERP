package com.example.vliascrm.service;

import com.example.vliascrm.entity.ProdGoods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 商品服务接口
 */
public interface ProdGoodsService {

    /**
     * 根据ID查询商品
     * @param id 商品ID
     * @return 商品对象
     */
    Optional<ProdGoods> findById(Long id);

    /**
     * 根据商品编码查询商品
     * @param goodsCode 商品编码
     * @return 商品对象
     */
    Optional<ProdGoods> findByGoodsCode(String goodsCode);

    /**
     * 查询所有商品（分页）
     * @param pageable 分页参数
     * @return 商品分页列表
     */
    Page<ProdGoods> findAll(Pageable pageable);

    /**
     * 根据分类ID查询商品
     * @param categoryId 分类ID
     * @return 商品列表
     */
    List<ProdGoods> findByCategoryId(Long categoryId);

    /**
     * 根据品牌ID查询商品
     * @param brandId 品牌ID
     * @return 商品列表
     */
    List<ProdGoods> findByBrandId(Long brandId);

    /**
     * 根据商品名称模糊查询
     * @param goodsName 商品名称
     * @return 商品列表
     */
    List<ProdGoods> findByGoodsNameContaining(String goodsName);

    /**
     * 查询推荐商品
     * @return 推荐商品列表
     */
    List<ProdGoods> findRecommendedGoods();

    /**
     * 查询热销商品
     * @return 热销商品列表
     */
    List<ProdGoods> findHotGoods();

    /**
     * 查询新品
     * @return 新品列表
     */
    List<ProdGoods> findNewGoods();

    /**
     * 查询库存预警商品
     * @return 库存预警商品列表
     */
    List<ProdGoods> findLowStockGoods();

    /**
     * 根据审核状态查询商品
     * @param auditStatus 审核状态
     * @return 商品列表
     */
    List<ProdGoods> findByAuditStatus(Integer auditStatus);

    /**
     * 保存商品
     * @param goods 商品对象
     * @return 保存后的商品对象
     */
    ProdGoods save(ProdGoods goods);

    /**
     * 更新商品
     * @param goods 商品对象
     * @return 更新后的商品对象
     */
    ProdGoods update(ProdGoods goods);

    /**
     * 删除商品（软删除）
     * @param id 商品ID
     */
    void deleteById(Long id);

    /**
     * 批量删除商品（软删除）
     * @param ids 商品ID列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 上架商品
     * @param id 商品ID
     */
    void onSale(Long id);

    /**
     * 下架商品
     * @param id 商品ID
     */
    void offSale(Long id);

    /**
     * 审核商品
     * @param id 商品ID
     * @param auditStatus 审核状态
     * @param auditRemark 审核备注
     * @param auditUserId 审核人ID
     */
    void auditGoods(Long id, Integer auditStatus, String auditRemark, Long auditUserId);

    /**
     * 更新库存
     * @param id 商品ID
     * @param stockQty 库存数量
     */
    void updateStock(Long id, Integer stockQty);

    /**
     * 检查商品编码是否存在
     * @param goodsCode 商品编码
     * @return 是否存在
     */
    boolean existsByGoodsCode(String goodsCode);

    /**
     * 统计分类下的商品数量
     * @param categoryId 分类ID
     * @return 商品数量
     */
    long countByCategoryId(Long categoryId);

    /**
     * 统计品牌下的商品数量
     * @param brandId 品牌ID
     * @return 商品数量
     */
    long countByBrandId(Long brandId);
} 