package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdImage;
import com.example.vliascrm.repository.ProdImageRepository;
import com.example.vliascrm.service.ProdImageService;
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

    @Autowired
    private ProdImageRepository prodImageRepository;

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
            
            // 先取消该商品的所有主图状态
            clearMainImageByGoodsId(image.getGoodsId());
            
            // 设置当前图片为主图
            prodImageRepository.setAsMainImage(id);
        }
    }

    @Override
    @Transactional
    public void clearMainImageByGoodsId(Long goodsId) {
        prodImageRepository.clearMainImageByGoodsId(goodsId);
    }

    @Override
    public long countByGoodsId(Long goodsId) {
        return prodImageRepository.countByGoodsIdAndIsDeleted(goodsId, false);
    }

    @Override
    @Transactional
    public List<ProdImage> saveAll(List<ProdImage> images) {
        for (ProdImage image : images) {
            if (image.getId() == null) {
                image.setCreateTime(LocalDateTime.now());
                image.setIsDeleted(false);
            }
        }
        return prodImageRepository.saveAll(images);
    }
} 