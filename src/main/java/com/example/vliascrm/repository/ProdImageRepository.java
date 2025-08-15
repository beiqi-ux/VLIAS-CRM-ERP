package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 商品图片数据访问接口
 */
@Repository
public interface ProdImageRepository extends JpaRepository<ProdImage, Long>, JpaSpecificationExecutor<ProdImage> {

    /**
     * 根据商品ID查询图片列表
     * @param goodsId 商品ID
     * @return 图片列表
     */
    List<ProdImage> findByGoodsIdAndIsDeletedOrderBySortAsc(Long goodsId, Boolean isDeleted);

    /**
     * 根据商品ID查询主图
     * @param goodsId 商品ID
     * @return 主图
     */
    Optional<ProdImage> findByGoodsIdAndIsMainAndIsDeleted(Long goodsId, Integer isMain, Boolean isDeleted);

    /**
     * 统计商品图片数量
     * @param goodsId 商品ID
     * @return 图片数量
     */
    long countByGoodsIdAndIsDeleted(Long goodsId, Boolean isDeleted);

    /**
     * 删除商品的所有图片
     * @param goodsId 商品ID
     */
    @Modifying
    @Transactional
    @Query("UPDATE ProdImage SET isDeleted = true WHERE goodsId = ?1")
    void deleteByGoodsId(Long goodsId);

    /**
     * 取消商品的所有主图状态
     * @param goodsId 商品ID
     */
    @Modifying
    @Transactional
    @Query("UPDATE ProdImage SET isMain = 0 WHERE goodsId = ?1 AND isDeleted = false")
    void clearMainImageByGoodsId(Long goodsId);

    /**
     * 设置主图
     * @param id 图片ID
     */
    @Modifying
    @Transactional
    @Query("UPDATE ProdImage SET isMain = 1 WHERE id = ?1")
    void setAsMainImage(Long id);
} 