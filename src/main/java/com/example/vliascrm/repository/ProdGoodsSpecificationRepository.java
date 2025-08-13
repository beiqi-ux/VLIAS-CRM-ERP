package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdGoodsSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品规格关联数据访问接口
 */
@Repository
public interface ProdGoodsSpecificationRepository extends JpaRepository<ProdGoodsSpecification, Long>, JpaSpecificationExecutor<ProdGoodsSpecification> {

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
     * 根据商品ID和规格值ID查询关联关系
     * @param goodsId 商品ID
     * @param specValueId 规格值ID
     * @return 商品规格关联
     */
    ProdGoodsSpecification findByGoodsIdAndSpecValueId(Long goodsId, Long specValueId);

    /**
     * 检查商品和规格值的关联关系是否存在
     * @param goodsId 商品ID
     * @param specValueId 规格值ID
     * @return 是否存在
     */
    boolean existsByGoodsIdAndSpecValueId(Long goodsId, Long specValueId);

    /**
     * 检查商品、规格项和规格值的关联关系是否存在
     * @param goodsId 商品ID
     * @param specItemId 规格项ID
     * @param specValueId 规格值ID
     * @return 是否存在
     */
    boolean existsByGoodsIdAndSpecItemIdAndSpecValueId(Long goodsId, Long specItemId, Long specValueId);

    /**
     * 根据商品ID删除所有规格关联
     * @param goodsId 商品ID
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ProdGoodsSpecification gs WHERE gs.goodsId = ?1")
    void deleteByGoodsId(Long goodsId);

    /**
     * 根据规格值ID删除所有关联
     * @param specValueId 规格值ID
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ProdGoodsSpecification gs WHERE gs.specValueId = ?1")
    void deleteBySpecValueId(Long specValueId);

    /**
     * 根据商品ID和规格值ID删除关联
     * @param goodsId 商品ID
     * @param specValueId 规格值ID
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM ProdGoodsSpecification gs WHERE gs.goodsId = ?1 AND gs.specValueId = ?2")
    void deleteByGoodsIdAndSpecValueId(Long goodsId, Long specValueId);

    /**
     * 查询商品的规格值详情（包含分类信息）
     * @param goodsId 商品ID
     * @return 规格值详情列表
     */
    @Query("SELECT gs FROM ProdGoodsSpecification gs " +
           "LEFT JOIN FETCH gs.specificationValue sv " +
           "LEFT JOIN FETCH sv.item si " +
           "LEFT JOIN FETCH si.category sc " +
           "WHERE gs.goodsId = ?1 " +
           "ORDER BY sc.sort, sv.sortOrder")
    List<ProdGoodsSpecification> findGoodsSpecificationDetails(Long goodsId);

    /**
     * 根据分类ID查询使用该分类规格的商品数量
     * @param categoryId 分类ID
     * @return 商品数量
     */
    @Query("SELECT COUNT(DISTINCT gs.goodsId) FROM ProdGoodsSpecification gs " +
           "JOIN ProdSpecificationValue sv ON gs.specValueId = sv.id " +
           "JOIN ProdSpecificationItem si ON sv.itemId = si.id " +
           "WHERE si.categoryId = ?1")
    long countGoodsByCategoryId(Long categoryId);

    /**
     * 批量插入商品规格关联
     * @param goodsId 商品ID
     * @param specValueIds 规格值ID列表
     */


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO prod_goods_specification (goods_id, spec_value_id, create_time) " +
                   "VALUES (?1, ?2, NOW())", nativeQuery = true)
    void batchInsert(Long goodsId, List<Long> specValueIds);
} 