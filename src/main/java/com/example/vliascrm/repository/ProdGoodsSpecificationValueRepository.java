package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdGoodsSpecificationValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品规格值关联数据访问接口
 */
@Repository
public interface ProdGoodsSpecificationValueRepository extends JpaRepository<ProdGoodsSpecificationValue, Long>, JpaSpecificationExecutor<ProdGoodsSpecificationValue> {

    /**
     * 根据ID查询未删除的关联记录
     */
    Optional<ProdGoodsSpecificationValue> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    /**
     * 分页查询未删除的关联记录
     */
    Page<ProdGoodsSpecificationValue> findByIsDeleted(Boolean isDeleted, Pageable pageable);

    /**
     * 根据商品ID查询规格关联列表
     */
    List<ProdGoodsSpecificationValue> findByGoodsIdAndIsDeleted(Long goodsId, Boolean isDeleted);

    /**
     * 根据商品ID和规格项ID查询规格关联
     */
    Optional<ProdGoodsSpecificationValue> findByGoodsIdAndSpecItemIdAndIsDeleted(Long goodsId, Long specItemId, Boolean isDeleted);

    /**
     * 根据商品ID、规格项ID和规格值ID查询关联记录
     */
    Optional<ProdGoodsSpecificationValue> findByGoodsIdAndSpecItemIdAndSpecValueIdAndIsDeleted(Long goodsId, Long specItemId, Long specValueId, Boolean isDeleted);

    /**
     * 根据规格项ID查询关联的商品数量
     */
    @Query("SELECT COUNT(g) FROM ProdGoodsSpecificationValue g WHERE g.specItemId = :specItemId AND g.isDeleted = false")
    long countBySpecItemId(@Param("specItemId") Long specItemId);

    /**
     * 根据规格值ID查询关联的商品数量
     */
    @Query("SELECT COUNT(g) FROM ProdGoodsSpecificationValue g WHERE g.specValueId = :specValueId AND g.isDeleted = false")
    long countBySpecValueId(@Param("specValueId") Long specValueId);

    /**
     * 根据商品ID删除所有规格关联（逻辑删除）
     */
    @Modifying
    @Query("UPDATE ProdGoodsSpecificationValue g SET g.isDeleted = true WHERE g.goodsId = :goodsId")
    void deleteByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 批量查询商品的规格关联信息
     */
    @Query("SELECT g FROM ProdGoodsSpecificationValue g WHERE g.goodsId IN :goodsIds AND g.isDeleted = :isDeleted ORDER BY g.goodsId, g.specItemId")
    List<ProdGoodsSpecificationValue> findByGoodsIdInAndIsDeleted(@Param("goodsIds") List<Long> goodsIds, @Param("isDeleted") Boolean isDeleted);
} 