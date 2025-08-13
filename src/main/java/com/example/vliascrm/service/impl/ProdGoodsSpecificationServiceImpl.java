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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        
        // 先删除现有关联，确保事务提交
        try {
            goodsSpecRepository.deleteByGoodsId(goodsId);
            goodsSpecRepository.flush(); // 强制刷新到数据库
        } catch (Exception e) {
            log.error("删除商品现有规格关联失败, goodsId: {}", goodsId, e);
            throw new RuntimeException("删除商品现有规格关联失败");
        }
        
        // 创建新关联
        if (specValueIds != null && !specValueIds.isEmpty()) {
            // 查询规格值以获取itemId
            List<ProdSpecificationValue> specValues = specValueRepository.findByIdInAndIsDeleted(specValueIds, false);
            Map<Long, Long> specValueToItemMap = specValues.stream()
                    .collect(Collectors.toMap(ProdSpecificationValue::getId, ProdSpecificationValue::getItemId));
            
            // 去重处理 - 确保同一个商品的同一个规格项只有一个规格值
            Map<Long, Long> itemToValueMap = new HashMap<>();
            for (Long specValueId : specValueIds) {
                Long itemId = specValueToItemMap.get(specValueId);
                if (itemId != null) {
                    if (itemToValueMap.containsKey(itemId)) {
                        log.warn("商品{}的规格项{}存在多个规格值，只保留最后一个: {}", goodsId, itemId, specValueId);
                    }
                    itemToValueMap.put(itemId, specValueId);
                }
            }
            
            // 创建规格关联记录
            List<ProdGoodsSpecification> goodsSpecs = itemToValueMap.entrySet().stream()
                    .map(entry -> {
                        Long itemId = entry.getKey();
                        Long specValueId = entry.getValue();
                        
                        // 双重检查是否已存在
                        if (goodsSpecRepository.existsByGoodsIdAndSpecItemIdAndSpecValueId(goodsId, itemId, specValueId)) {
                            log.warn("规格关联已存在，跳过: goodsId={}, specItemId={}, specValueId={}", goodsId, itemId, specValueId);
                            return null;
                        }
                        
                        ProdGoodsSpecification goodsSpec = new ProdGoodsSpecification();
                        goodsSpec.setGoodsId(goodsId);
                        goodsSpec.setSpecValueId(specValueId);
                        goodsSpec.setSpecItemId(itemId);
                        goodsSpec.setCreatedTime(LocalDateTime.now());
                        goodsSpec.setUpdatedTime(LocalDateTime.now());
                        return goodsSpec;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            if (!goodsSpecs.isEmpty()) {
                try {
                    goodsSpecRepository.saveAll(goodsSpecs);
                } catch (Exception e) {
                    log.error("保存商品规格关联失败, goodsId: {}, specs: {}", goodsId, goodsSpecs, e);
                    throw new RuntimeException("保存商品规格关联失败: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void addGoodsSpecification(Long goodsId, Long specValueId) {
        log.debug("添加商品规格, goodsId: {}, specValueId: {}", goodsId, specValueId);
        
        // 查询规格值以获取itemId
        ProdSpecificationValue specValue = specValueRepository.findByIdAndIsDeleted(specValueId, false)
                .orElseThrow(() -> new RuntimeException("规格值不存在"));
        
        Long specItemId = specValue.getItemId();
        
        // 检查是否已存在（使用三字段检查）
        if (!goodsSpecRepository.existsByGoodsIdAndSpecItemIdAndSpecValueId(goodsId, specItemId, specValueId)) {
            ProdGoodsSpecification goodsSpec = new ProdGoodsSpecification();
            goodsSpec.setGoodsId(goodsId);
            goodsSpec.setSpecValueId(specValueId);
            goodsSpec.setSpecItemId(specItemId);
            goodsSpec.setCreatedTime(LocalDateTime.now());
            goodsSpec.setUpdatedTime(LocalDateTime.now());
            
            try {
                goodsSpecRepository.save(goodsSpec);
            } catch (Exception e) {
                log.error("添加商品规格关联失败, goodsId: {}, specValueId: {}, specItemId: {}", goodsId, specValueId, specItemId, e);
                throw new RuntimeException("添加商品规格关联失败: " + e.getMessage());
            }
        } else {
            log.warn("商品规格关联已存在，跳过添加: goodsId={}, specItemId={}, specValueId={}", goodsId, specItemId, specValueId);
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
                        targetSpec.setSpecItemId(sourceSpec.getSpecItemId()); // 复制specItemId
                        return targetSpec;
                    })
                    .collect(Collectors.toList());
            
            goodsSpecRepository.saveAll(targetSpecs);
        }
    }
} 