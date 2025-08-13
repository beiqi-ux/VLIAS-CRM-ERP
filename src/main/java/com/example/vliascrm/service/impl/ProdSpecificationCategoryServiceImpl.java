package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdSpecificationCategory;
import com.example.vliascrm.repository.ProdSpecificationCategoryRepository;
import com.example.vliascrm.repository.ProdSpecificationItemRepository;
import com.example.vliascrm.repository.ProdSpecificationValueRepository;
import com.example.vliascrm.service.ProdSpecificationCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 商品规格分类服务实现类
 */
@Service
public class ProdSpecificationCategoryServiceImpl implements ProdSpecificationCategoryService {

    @Autowired
    private ProdSpecificationCategoryRepository categoryRepository;

    @Autowired
    private ProdSpecificationItemRepository itemRepository;

    @Autowired
    private ProdSpecificationValueRepository valueRepository;

    @Override
    public Optional<ProdSpecificationCategory> findById(Long id) {
        return categoryRepository.findByIdAndIsDeleted(id, false);
    }

    @Override
    public Page<ProdSpecificationCategory> findAll(Pageable pageable) {
        return categoryRepository.findByIsDeleted(false, pageable);
    }

    @Override
    public Page<ProdSpecificationCategory> findByConditions(Pageable pageable, String categoryName, Integer status) {
        Specification<ProdSpecificationCategory> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 未删除
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            // 分类名称模糊查询
            if (StringUtils.hasText(categoryName)) {
                predicates.add(criteriaBuilder.like(root.get("categoryName"), "%" + categoryName + "%"));
            }
            
            // 状态
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        return categoryRepository.findAll(spec, pageable);
    }

    @Override
    public List<ProdSpecificationCategory> findActiveCategories() {
        return categoryRepository.findByStatusAndIsDeletedOrderBySortAsc(1, false);
    }

    @Override
    public List<ProdSpecificationCategory> findByCategoryNameContaining(String categoryName) {
        return categoryRepository.findByCategoryNameContainingAndStatusAndIsDeleted(categoryName, 1, false);
    }

    @Override
    @Transactional
    public ProdSpecificationCategory save(ProdSpecificationCategory category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public ProdSpecificationCategory update(ProdSpecificationCategory category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ProdSpecificationCategory> categoryOpt = findById(id);
        if (categoryOpt.isPresent()) {
            ProdSpecificationCategory category = categoryOpt.get();
            category.setIsDeleted(true);
            categoryRepository.save(category);
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
    public void enableCategory(Long id) {
        Optional<ProdSpecificationCategory> categoryOpt = findById(id);
        if (categoryOpt.isPresent()) {
            ProdSpecificationCategory category = categoryOpt.get();
            category.setStatus(1);
            categoryRepository.save(category);
        }
    }

    @Override
    @Transactional
    public void disableCategory(Long id) {
        Optional<ProdSpecificationCategory> categoryOpt = findById(id);
        if (categoryOpt.isPresent()) {
            ProdSpecificationCategory category = categoryOpt.get();
            category.setStatus(0);
            categoryRepository.save(category);
        }
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return categoryRepository.existsByCategoryNameAndIsDeleted(categoryName, false);
    }

    @Override
    public boolean existsByCategoryNameAndIdNot(String categoryName, Long excludeId) {
        Optional<ProdSpecificationCategory> existing = categoryRepository.findByCategoryNameAndIsDeleted(categoryName, false);
        return existing.isPresent() && !existing.get().getId().equals(excludeId);
    }

    @Override
    public boolean isUsedBySpecificationValues(Long categoryId) {
        // 检查是否有规格项在使用这个分类
        return itemRepository.countByCategoryIdAndIsDeleted(categoryId, false) > 0;
    }
} 