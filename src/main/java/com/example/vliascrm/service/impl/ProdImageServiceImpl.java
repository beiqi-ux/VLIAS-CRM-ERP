package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdImage;
import com.example.vliascrm.repository.ProdImageRepository;
import com.example.vliascrm.service.ProdImageService;
import com.example.vliascrm.service.ProdGoodsService;
import com.example.vliascrm.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 商品图片服务实现类
 */
@Service
public class ProdImageServiceImpl implements ProdImageService {

    // 商品图片最大数量限制
    private static final int MAX_IMAGES_PER_GOODS = 5;

    @Autowired
    private ProdImageRepository prodImageRepository;

    @Autowired
    private ProdGoodsService prodGoodsService;

    @Override
    public Optional<ProdImage> findById(Long id) {
        return prodImageRepository.findById(id);
    }

    @Override
    public List<ProdImage> findByGoodsId(Long goodsId) {
        return prodImageRepository.findByGoodsIdAndIsDeletedOrderBySortAsc(goodsId, false);
    }

    @Override
    public Optional<ProdImage> findMainImageByGoodsId(Long goodsId) {
        return prodImageRepository.findByGoodsIdAndIsMainAndIsDeleted(goodsId, 1, false);
    }

    @Override
    @Transactional
    public ProdImage save(ProdImage image) {
        // 设置创建时间
        if (image.getId() == null) {
            // 检查图片数量限制
            long currentCount = countByGoodsId(image.getGoodsId());
            if (currentCount >= MAX_IMAGES_PER_GOODS) {
                throw new BusinessException("每个商品最多只能上传" + MAX_IMAGES_PER_GOODS + "张图片");
            }
            
            image.setCreateTime(LocalDateTime.now());
            image.setIsDeleted(false);
        }
        return prodImageRepository.save(image);
    }

    @Override
    @Transactional
    public ProdImage update(ProdImage image) {
        return prodImageRepository.save(image);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ProdImage> imageOpt = prodImageRepository.findById(id);
        if (imageOpt.isPresent()) {
            ProdImage image = imageOpt.get();
            image.setIsDeleted(true);
            prodImageRepository.save(image);
        }
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
    }

    @Override
    @Transactional
    public void deleteByGoodsId(Long goodsId) {
        prodImageRepository.deleteByGoodsId(goodsId);
    }

    @Override
    @Transactional
    public void setAsMainImage(Long id) {
        Optional<ProdImage> imageOpt = prodImageRepository.findById(id);
        if (imageOpt.isPresent()) {
            ProdImage image = imageOpt.get();
            
            // 先取消该商品的所有主图状态（但不清除商品表的main_image字段）
            prodImageRepository.clearMainImageByGoodsId(image.getGoodsId());
            
            // 设置当前图片为主图
            prodImageRepository.setAsMainImage(id);
            
            // 同时更新商品表的main_image字段
            prodGoodsService.updateMainImage(image.getGoodsId(), image.getImageUrl());
        }
    }

    @Override
    @Transactional
    public void clearMainImageByGoodsId(Long goodsId) {
        prodImageRepository.clearMainImageByGoodsId(goodsId);
        
        // 同时清除商品表的main_image字段
        prodGoodsService.updateMainImage(goodsId, null);
    }

    @Override
    public long countByGoodsId(Long goodsId) {
        return prodImageRepository.countByGoodsIdAndIsDeleted(goodsId, false);
    }

    @Override
    @Transactional
    public List<ProdImage> saveAll(List<ProdImage> images) {
        if (images.isEmpty()) {
            return images;
        }
        
        // 检查所有新增图片的商品ID是否一致
        Long goodsId = images.get(0).getGoodsId();
        for (ProdImage image : images) {
            if (!goodsId.equals(image.getGoodsId())) {
                throw new BusinessException("批量保存的图片必须属于同一个商品");
            }
        }
        
        // 统计新增图片数量
        long newImageCount = images.stream()
            .filter(image -> image.getId() == null)
            .count();
            
        if (newImageCount > 0) {
            // 检查图片数量限制
            long currentCount = countByGoodsId(goodsId);
            if (currentCount + newImageCount > MAX_IMAGES_PER_GOODS) {
                throw new BusinessException("每个商品最多只能上传" + MAX_IMAGES_PER_GOODS + "张图片，当前已有" + currentCount + "张，尝试新增" + newImageCount + "张");
            }
        }
        
        for (ProdImage image : images) {
            if (image.getId() == null) {
                image.setCreateTime(LocalDateTime.now());
                image.setIsDeleted(false);
            }
        }
        return prodImageRepository.saveAll(images);
    }
} 