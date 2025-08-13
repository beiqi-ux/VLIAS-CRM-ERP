package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdSpecificationValue;
import com.example.vliascrm.repository.ProdSpecificationValueRepository;
import com.example.vliascrm.service.ProdSpecificationValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商品规格值服务实现类
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProdSpecificationValueServiceImpl implements ProdSpecificationValueService {

    private final ProdSpecificationValueRepository valueRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<ProdSpecificationValue> findById(Long id) {
        return valueRepository.findByIdAndIsDeleted(id, false);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProdSpecificationValue> findAll(Pageable pageable) {
        return valueRepository.findByIsDeleted(false, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProdSpecificationValue> findByConditions(Pageable pageable, Long itemId, String valueName, Integer status) {
        Specification<ProdSpecificationValue> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 添加 JOIN FETCH 来预加载规格项信息
            if (query.getResultType() == ProdSpecificationValue.class) {
                root.fetch("item", JoinType.LEFT);
            }
            
            // 未删除
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            // 规格项ID
            if (itemId != null) {
                predicates.add(criteriaBuilder.equal(root.get("itemId"), itemId));
            }
            
            // 规格值名称模糊查询
            if (StringUtils.hasText(valueName)) {
                predicates.add(criteriaBuilder.like(root.get("valueName"), "%" + valueName + "%"));
            }
            
            // 状态
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return valueRepository.findAll(spec, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationValue> findByItemId(Long itemId) {
        return valueRepository.findByItemIdAndIsDeleted(itemId, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationValue> findActiveByItemId(Long itemId) {
        return valueRepository.findByItemIdAndStatusAndIsDeleted(itemId, ProdSpecificationValue.STATUS_ENABLED, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationValue> findByValueNameContaining(String valueName) {
        return valueRepository.findByValueNameContainingAndStatusAndIsDeleted(valueName, ProdSpecificationValue.STATUS_ENABLED, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationValue> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return valueRepository.findByIdInAndIsDeleted(ids, false);
    }

    @Override
    public ProdSpecificationValue save(ProdSpecificationValue specValue) {
        // 验证必填字段
        if (specValue.getItemId() == null) {
            throw new IllegalArgumentException("规格项ID不能为空");
        }
        if (!StringUtils.hasText(specValue.getValueCode())) {
            throw new IllegalArgumentException("规格值代码不能为空");
        }
        if (!StringUtils.hasText(specValue.getValueName())) {
            throw new IllegalArgumentException("规格值名称不能为空");
        }
        
        return valueRepository.save(specValue);
    }

    @Override
    public ProdSpecificationValue update(ProdSpecificationValue specValue) {
        // 验证规格值是否存在
        Optional<ProdSpecificationValue> valueOpt = findById(specValue.getId());
        if (valueOpt.isEmpty()) {
            throw new IllegalArgumentException("规格值不存在");
        }
        
        return valueRepository.save(specValue);
    }

    @Override
    public void deleteById(Long id) {
        Optional<ProdSpecificationValue> valueOpt = findById(id);
        if (valueOpt.isEmpty()) {
            throw new IllegalArgumentException("规格值不存在");
        }
        
        ProdSpecificationValue value = valueOpt.get();
        
        // 逻辑删除
        value.setIsDeleted(true);
        valueRepository.save(value);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        
        List<ProdSpecificationValue> values = findByIds(ids);
        for (ProdSpecificationValue value : values) {
            // 逻辑删除
            value.setIsDeleted(true);
        }
        
        valueRepository.saveAll(values);
    }

    @Override
    public void enableById(Long id) {
        Optional<ProdSpecificationValue> valueOpt = findById(id);
        if (valueOpt.isEmpty()) {
            throw new IllegalArgumentException("规格值不存在");
        }
        
        ProdSpecificationValue value = valueOpt.get();
        value.setStatus(ProdSpecificationValue.STATUS_ENABLED);
        valueRepository.save(value);
    }

    @Override
    public void disableById(Long id) {
        Optional<ProdSpecificationValue> valueOpt = findById(id);
        if (valueOpt.isEmpty()) {
            throw new IllegalArgumentException("规格值不存在");
        }
        
        ProdSpecificationValue value = valueOpt.get();
        value.setStatus(ProdSpecificationValue.STATUS_DISABLED);
        valueRepository.save(value);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByValueCode(Long itemId, String valueCode, Long excludeId) {
        if (excludeId != null) {
            return valueRepository.existsByItemIdAndValueCodeAndIdNotAndIsDeleted(itemId, valueCode, excludeId, false);
        } else {
            return valueRepository.existsByItemIdAndValueCodeAndIsDeleted(itemId, valueCode, false);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByValueName(Long itemId, String valueName, Long excludeId) {
        if (excludeId != null) {
            return valueRepository.existsByItemIdAndValueNameAndIdNotAndIsDeleted(itemId, valueName, excludeId, false);
        } else {
            return valueRepository.existsByItemIdAndValueNameAndIsDeleted(itemId, valueName, false);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProdSpecificationValue> findAllActive() {
        return valueRepository.findByStatusAndIsDeleted(ProdSpecificationValue.STATUS_ENABLED, false);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByItemId(Long itemId) {
        return valueRepository.countByItemIdAndIsDeleted(itemId, false);
    }
} 