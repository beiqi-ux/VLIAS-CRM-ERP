package com.example.vliascrm.service;

import com.example.vliascrm.entity.ProdGoodsSpecification;
import com.example.vliascrm.entity.ProdSpecificationValue;

import java.util.List;
import java.util.Map;

/**
 * 商品规格关联服务接口
 */
public interface ProdGoodsSpecificationService {

    /**
     * 根据商品ID查询规格关联列表
     * @param goodsId 商品ID
     * @return 规格关联列表
     */
    List<ProdGoodsSpecification> findByGoodsId(Long goodsId);

    /**
     * 根据规格值ID查询关联的商品列表
     * @param specValueId 规格值ID
     * @return 商品规格关联列表
     */
    List<ProdGoodsSpecification> findBySpecValueId(Long specValueId);

    /**
     * 查询商品的规格值详情（包含分类信息）
     * @param goodsId 商品ID
     * @return 规格值详情列表
     */
    List<ProdGoodsSpecification> findGoodsSpecificationDetails(Long goodsId);

    /**
     * 获取商品的规格值映射（按分类分组）
     * @param goodsId 商品ID
     * @return 分类ID -> 规格值列表的映射
     */
    Map<Long, List<ProdSpecificationValue>> getGoodsSpecificationMap(Long goodsId);

    /**
     * 为商品设置规格值
     * @param goodsId 商品ID
     * @param specValueIds 规格值ID列表
     */
    void setGoodsSpecifications(Long goodsId, List<Long> specValueIds);

    /**
     * 为商品添加规格值
     * @param goodsId 商品ID
     * @param specValueId 规格值ID
     */
    void addGoodsSpecification(Long goodsId, Long specValueId);

    /**
     * 移除商品的规格值
     * @param goodsId 商品ID
     * @param specValueId 规格值ID
     */
    void removeGoodsSpecification(Long goodsId, Long specValueId);

    /**
     * 移除商品的所有规格值
     * @param goodsId 商品ID
     */
    void removeAllGoodsSpecifications(Long goodsId);

    /**
     * 检查商品和规格值的关联关系是否存在
     * @param goodsId 商品ID
     * @param specValueId 规格值ID
     * @return 是否存在
     */
    boolean existsByGoodsIdAndSpecValueId(Long goodsId, Long specValueId);

    /**
     * 根据分类ID查询使用该分类规格的商品数量
     * @param categoryId 分类ID
     * @return 商品数量
     */
    long countGoodsByCategoryId(Long categoryId);

    /**
     * 根据规格值ID查询使用该规格值的商品数量
     * @param specValueId 规格值ID
     * @return 商品数量
     */
    long countGoodsBySpecValueId(Long specValueId);

    /**
     * 批量为商品设置规格值
     * @param goodsSpecifications 商品规格关联列表
     */
    void batchSave(List<ProdGoodsSpecification> goodsSpecifications);

    /**
     * 复制商品规格到新商品
     * @param sourceGoodsId 源商品ID
     * @param targetGoodsId 目标商品ID
     */
    void copyGoodsSpecifications(Long sourceGoodsId, Long targetGoodsId);
} 