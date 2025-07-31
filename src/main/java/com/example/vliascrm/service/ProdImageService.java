package com.example.vliascrm.service;

import com.example.vliascrm.entity.ProdImage;

import java.util.List;
import java.util.Optional;

/**
 * 商品图片服务接口
 */
public interface ProdImageService {

    /**
     * 根据ID查询图片
     * @param id 图片ID
     * @return 图片对象
     */
    Optional<ProdImage> findById(Long id);

    /**
     * 根据商品ID查询图片列表
     * @param goodsId 商品ID
     * @return 图片列表
     */
    List<ProdImage> findByGoodsId(Long goodsId);

    /**
     * 根据商品ID查询主图
     * @param goodsId 商品ID
     * @return 主图
     */
    Optional<ProdImage> findMainImageByGoodsId(Long goodsId);

    /**
     * 保存图片
     * @param image 图片对象
     * @return 保存后的图片对象
     */
    ProdImage save(ProdImage image);

    /**
     * 更新图片
     * @param image 图片对象
     * @return 更新后的图片对象
     */
    ProdImage update(ProdImage image);

    /**
     * 删除图片（软删除）
     * @param id 图片ID
     */
    void deleteById(Long id);

    /**
     * 批量删除图片（软删除）
     * @param ids 图片ID列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 删除商品的所有图片
     * @param goodsId 商品ID
     */
    void deleteByGoodsId(Long goodsId);

    /**
     * 设置主图
     * @param id 图片ID
     */
    void setAsMainImage(Long id);

    /**
     * 取消商品的所有主图状态
     * @param goodsId 商品ID
     */
    void clearMainImageByGoodsId(Long goodsId);

    /**
     * 统计商品图片数量
     * @param goodsId 商品ID
     * @return 图片数量
     */
    long countByGoodsId(Long goodsId);

    /**
     * 批量保存图片
     * @param images 图片列表
     * @return 保存后的图片列表
     */
    List<ProdImage> saveAll(List<ProdImage> images);
} 