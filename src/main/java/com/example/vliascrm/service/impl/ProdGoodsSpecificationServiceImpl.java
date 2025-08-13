package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdGoodsSpecification;
import com.example.vliascrm.entity.ProdSpecificationValue;
import com.example.vliascrm.repository.ProdGoodsSpecificationRepository;
import com.example.vliascrm.repository.ProdSpecificationValueRepository;
import com.example.vliascrm.service.ProdGoodsSpecificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品规格关联服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProdGoodsSpecificationServiceImpl implements ProdGoodsSpecificationService {

    private final ProdGoodsSpecificationRepository goodsSpecRepository;
    private final ProdSpecificationValueRepository specValueRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProdGoodsSpecification> findByGoodsId(Long goodsId) {
        log.debug("查询商品规格关联, goodsId: {}", goodsId);
        return goodsSpecRepository.findByGoodsId(goodsId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdGoodsSpecification> findBySpecValueId(Long specValueId) {
        log.debug("查询规格值关联的商品, specValueId: {}", specValueId);
        return goodsSpecRepository.findBySpecValueId(specValueId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdGoodsSpecification> findGoodsSpecificationDetails(Long goodsId) {
        log.debug("查询商品规格详情, goodsId: {}", goodsId);
        return goodsSpecRepository.findGoodsSpecificationDetails(goodsId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, List<ProdSpecificationValue>> getGoodsSpecificationMap(Long goodsId) {
        log.debug("获取商品规格映射, goodsId: {}", goodsId);
        
        List<ProdGoodsSpecification> goodsSpecs = goodsSpecRepository.findGoodsSpecificationDetails(goodsId);
        
        return goodsSpecs.stream()
                .collect(Collectors.groupingBy(
                        gs -> gs.getSpecificationValue().getItemId(),
                        Collectors.mapping(ProdGoodsSpecification::getSpecificationValue, Collectors.toList())
                ));
    }

    @Override
    public void setGoodsSpecifications(Long goodsId, List<Long> specValueIds) {
        log.debug("设置商品规格, goodsId: {}, specValueIds: {}", goodsId, specValueIds);
        
        // 先删除现有关联
        goodsSpecRepository.deleteByGoodsId(goodsId);
        
        // 创建新关联
        if (specValueIds != null && !specValueIds.isEmpty()) {
            List<ProdGoodsSpecification> goodsSpecs = specValueIds.stream()
                    .map(specValueId -> {
                        ProdGoodsSpecification goodsSpec = new ProdGoodsSpecification();
                        goodsSpec.setGoodsId(goodsId);
                        goodsSpec.setSpecValueId(specValueId);
                        return goodsSpec;
                    })
                    .collect(Collectors.toList());
            
            goodsSpecRepository.saveAll(goodsSpecs);
        }
    }

    @Override
    public void addGoodsSpecification(Long goodsId, Long specValueId) {
        log.debug("添加商品规格, goodsId: {}, specValueId: {}", goodsId, specValueId);
        
        // 检查是否已存在
        if (!goodsSpecRepository.existsByGoodsIdAndSpecValueId(goodsId, specValueId)) {
            ProdGoodsSpecification goodsSpec = new ProdGoodsSpecification();
            goodsSpec.setGoodsId(goodsId);
            goodsSpec.setSpecValueId(specValueId);
            goodsSpecRepository.save(goodsSpec);
        }
    }

    @Override
    public void removeGoodsSpecification(Long goodsId, Long specValueId) {
        log.debug("移除商品规格, goodsId: {}, specValueId: {}", goodsId, specValueId);
        goodsSpecRepository.deleteByGoodsIdAndSpecValueId(goodsId, specValueId);
    }

    @Override
    public void removeAllGoodsSpecifications(Long goodsId) {
        log.debug("移除商品所有规格, goodsId: {}", goodsId);
        goodsSpecRepository.deleteByGoodsId(goodsId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByGoodsIdAndSpecValueId(Long goodsId, Long specValueId) {
        return goodsSpecRepository.existsByGoodsIdAndSpecValueId(goodsId, specValueId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countGoodsByCategoryId(Long categoryId) {
        log.debug("统计使用分类规格的商品数量, categoryId: {}", categoryId);
        return goodsSpecRepository.countGoodsByCategoryId(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countGoodsBySpecValueId(Long specValueId) {
        log.debug("统计使用规格值的商品数量, specValueId: {}", specValueId);
        return goodsSpecRepository.findBySpecValueId(specValueId).size();
    }

    @Override
    public void batchSave(List<ProdGoodsSpecification> goodsSpecifications) {
        log.debug("批量保存商品规格关联, size: {}", goodsSpecifications.size());
        goodsSpecRepository.saveAll(goodsSpecifications);
    }

    @Override
    public void copyGoodsSpecifications(Long sourceGoodsId, Long targetGoodsId) {
        log.debug("复制商品规格, sourceGoodsId: {}, targetGoodsId: {}", sourceGoodsId, targetGoodsId);
        
        List<ProdGoodsSpecification> sourceSpecs = goodsSpecRepository.findByGoodsId(sourceGoodsId);
        
        if (!sourceSpecs.isEmpty()) {
            List<ProdGoodsSpecification> targetSpecs = sourceSpecs.stream()
                    .map(sourceSpec -> {
                        ProdGoodsSpecification targetSpec = new ProdGoodsSpecification();
                        targetSpec.setGoodsId(targetGoodsId);
                        targetSpec.setSpecValueId(sourceSpec.getSpecValueId());
                        return targetSpec;
                    })
                    .collect(Collectors.toList());
            
            goodsSpecRepository.saveAll(targetSpecs);
        }
    }
} 