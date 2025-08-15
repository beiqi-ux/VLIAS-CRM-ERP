package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdSpecificationItem;
import com.example.vliascrm.repository.ProdSpecificationItemRepository;
import com.example.vliascrm.repository.ProdSpecificationValueRepository;
import com.example.vliascrm.service.ProdSpecificationItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商品规格项服务实现类
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProdSpecificationItemServiceImpl implements ProdSpecificationItemService {

    private final ProdSpecificationItemRepository itemRepository;
    private final ProdSpecificationValueRepository valueRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<ProdSpecificationItem> findById(Long id) {
        return itemRepository.findByIdAndIsDeleted(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProdSpecificationItem> findAll(Pageable pageable) {
        return itemRepository.findByIsDeleted(false, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProdSpecificationItem> findByConditions(Pageable pageable, Long categoryId, String itemName, Integer status) {
        Specification<ProdSpecificationItem> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 未删除
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            // 分类ID
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), categoryId));
            }
            
            // 规格项名称模糊查询
            if (StringUtils.hasText(itemName)) {
                predicates.add(criteriaBuilder.like(root.get("itemName"), "%" + itemName + "%"));
            }
            
            // 状态
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return itemRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationItem> findByCategoryId(Long categoryId) {
        return itemRepository.findByCategoryIdAndIsDeletedOrderBySortAsc(categoryId, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationItem> findActiveByCategoryId(Long categoryId) {
        return itemRepository.findByCategoryIdAndStatusAndIsDeletedOrderBySortAsc(categoryId, ProdSpecificationItem.STATUS_ENABLED, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationItem> findByItemNameContaining(String itemName) {
        return itemRepository.findByItemNameContainingAndStatusAndIsDeleted(itemName, ProdSpecificationItem.STATUS_ENABLED, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationItem> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.findByIdInAndIsDeleted(ids, false);
    }

    @Override
    public ProdSpecificationItem save(ProdSpecificationItem specItem) {
        // 验证必填字段
        if (specItem.getCategoryId() == null) {
            throw new IllegalArgumentException("分类ID不能为空");
        }
        if (!StringUtils.hasText(specItem.getItemCode())) {
            throw new IllegalArgumentException("规格项代码不能为空");
        }
        if (!StringUtils.hasText(specItem.getItemName())) {
            throw new IllegalArgumentException("规格项名称不能为空");
        }
        
        return itemRepository.save(specItem);
    }

    @Override
    public ProdSpecificationItem update(ProdSpecificationItem specItem) {
        // 验证规格项是否存在
        Optional<ProdSpecificationItem> itemOpt = findById(specItem.getId());
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("规格项不存在");
        }
        
        return itemRepository.save(specItem);
    }

    @Override
    public void deleteById(Long id) {
        Optional<ProdSpecificationItem> itemOpt = findById(id);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("规格项不存在");
        }
        
        ProdSpecificationItem item = itemOpt.get();
        
        // 检查是否有关联的规格值
        long valueCount = valueRepository.countByItemIdAndIsDeleted(id, false);
        if (valueCount > 0) {
            throw new IllegalStateException("该规格项下还有规格值，无法删除");
        }
        
        // 逻辑删除
        item.setIsDeleted(true);
        itemRepository.save(item);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        
        List<ProdSpecificationItem> items = findByIds(ids);
        for (ProdSpecificationItem item : items) {
            // 检查是否有关联的规格值
            long valueCount = valueRepository.countByItemIdAndIsDeleted(item.getId(), false);
            if (valueCount > 0) {
                throw new IllegalStateException("规格项[" + item.getItemName() + "]下还有规格值，无法删除");
            }
            
            // 逻辑删除
            item.setIsDeleted(true);
        }
        
        itemRepository.saveAll(items);
    }

    @Override
    public void enableById(Long id) {
        Optional<ProdSpecificationItem> itemOpt = findById(id);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("规格项不存在");
        }
        
        ProdSpecificationItem item = itemOpt.get();
        item.setStatus(ProdSpecificationItem.STATUS_ENABLED);
        itemRepository.save(item);
    }

    @Override
    public void disableById(Long id) {
        Optional<ProdSpecificationItem> itemOpt = findById(id);
        if (itemOpt.isEmpty()) {
            throw new IllegalArgumentException("规格项不存在");
        }
        
        ProdSpecificationItem item = itemOpt.get();
        item.setStatus(ProdSpecificationItem.STATUS_DISABLED);
        itemRepository.save(item);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByItemCode(Long categoryId, String itemCode, Long excludeId) {
        return itemRepository.existsByCategoryIdAndItemCodeExcludingId(categoryId, itemCode, excludeId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByItemName(Long categoryId, String itemName, Long excludeId) {
        return itemRepository.existsByCategoryIdAndItemNameExcludingId(categoryId, itemName, excludeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationItem> findAllActive() {
        return itemRepository.findByStatusAndIsDeletedOrderBySortAsc(ProdSpecificationItem.STATUS_ENABLED, false);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCategoryId(Long categoryId) {
        return itemRepository.countByCategoryId(categoryId);
    }
} 