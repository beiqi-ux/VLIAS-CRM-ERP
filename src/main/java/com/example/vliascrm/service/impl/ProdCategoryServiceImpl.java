package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdCategory;
import com.example.vliascrm.repository.ProdCategoryRepository;
import com.example.vliascrm.service.ProdCategoryService;
import com.example.vliascrm.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商品分类服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProdCategoryServiceImpl implements ProdCategoryService {

    private final ProdCategoryRepository prodCategoryRepository;
    private final CacheService cacheService;

    // 缓存键前缀
    private static final String CATEGORY_CACHE_PREFIX = "category:";
    private static final String CATEGORY_ALL_CACHE_KEY = "category:all";
    private static final String CATEGORY_TREE_CACHE_KEY = "category:tree";
    
    // 缓存过期时间：10分钟
    private static final Duration CATEGORY_CACHE_TTL = Duration.ofMinutes(10);

    @Override
    public Optional<ProdCategory> findById(Long id) {
        String cacheKey = CATEGORY_CACHE_PREFIX + id;
        
        // 先从缓存中查询
        ProdCategory cachedCategory = cacheService.get(cacheKey, ProdCategory.class);
        if (cachedCategory != null) {
            log.debug("分类缓存命中: id={}", id);
            return Optional.of(cachedCategory);
        }
        
        // 缓存未命中，从数据库查询
        Optional<ProdCategory> categoryOpt = prodCategoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            cacheService.set(cacheKey, categoryOpt.get(), CATEGORY_CACHE_TTL);
            log.debug("分类信息已缓存: id={}", id);
        }
        
        return categoryOpt;
    }

    @Override
    public List<ProdCategory> findAll() {
        // 先从缓存中查询
        List<ProdCategory> cachedCategories = cacheService.get(CATEGORY_ALL_CACHE_KEY, 
            new com.fasterxml.jackson.core.type.TypeReference<List<ProdCategory>>() {});
        
        if (cachedCategories != null && !cachedCategories.isEmpty()) {
            log.debug("分类列表缓存命中，数量: {}", cachedCategories.size());
            return cachedCategories;
        }
        
        // 缓存未命中，从数据库查询（只查询启用且未删除的分类）
        List<ProdCategory> categories = prodCategoryRepository.findAll().stream()
            .filter(category -> !category.getIsDeleted() && category.getStatus() == 1)
            .sorted((a, b) -> Integer.compare(a.getSort() != null ? a.getSort() : 0, 
                                             b.getSort() != null ? b.getSort() : 0))
            .toList();
        
        // 缓存结果
        if (!categories.isEmpty()) {
            cacheService.set(CATEGORY_ALL_CACHE_KEY, categories, CATEGORY_CACHE_TTL);
            log.debug("分类列表已缓存，数量: {}", categories.size());
        }
        
        return categories;
    }

    @Override
    public List<ProdCategory> findByParentId(Long parentId) {
        // 根据父级ID查找子分类
        return prodCategoryRepository.findByParentIdAndStatusAndIsDeletedOrderBySortAsc(parentId, 1, false);
    }

    @Override
    public List<ProdCategory> findRootCategories() {
        return prodCategoryRepository.findRootCategories();
    }

    @Override
    public List<ProdCategory> findByLevel(Integer level) {
        return prodCategoryRepository.findByLevelAndStatusAndIsDeletedOrderBySortAsc(level, 1, false);
    }

    @Override
    public List<ProdCategory> findByCategoryNameContaining(String categoryName) {
        return prodCategoryRepository.findByCategoryNameContainingAndStatusAndIsDeleted(categoryName, 1, false);
    }

    @Override
    public List<ProdCategory> findVisibleCategories() {
        return prodCategoryRepository.findByIsShowAndStatusAndIsDeletedOrderBySortAsc(1, 1, false);
    }

    @Override
    @Transactional
    public ProdCategory save(ProdCategory category) {
        // 设置创建时间和状态
        if (category.getId() == null) {
            category.setCreateTime(LocalDateTime.now());
            category.setIsDeleted(false);
            category.setStatus(1); // 默认启用状态
            category.setIsShow(1); // 默认显示状态
            
            // 设置层级
            if (category.getParentId() == null || category.getParentId() == 0) {
                category.setParentId(0L); // 统一使用0表示根分类
                category.setLevel(1);
            } else {
                // 根据父分类设置层级
                Optional<ProdCategory> parentOpt = findById(category.getParentId());
                if (parentOpt.isPresent()) {
                    category.setLevel(parentOpt.get().getLevel() + 1);
                } else {
                    category.setLevel(1);
                }
            }
        }
        return prodCategoryRepository.save(category);
    }

    @Override
    @Transactional
    public ProdCategory update(ProdCategory category) {
        // 设置更新时间
        category.setUpdateTime(LocalDateTime.now());
        
        // 重新计算层级（当父分类发生变化时）
        if (category.getParentId() == null || category.getParentId() == 0) {
            category.setParentId(0L); // 统一使用0表示根分类
            category.setLevel(1);
        } else {
            // 根据父分类设置层级
            Optional<ProdCategory> parentOpt = findById(category.getParentId());
            if (parentOpt.isPresent()) {
                category.setLevel(parentOpt.get().getLevel() + 1);
            } else {
                category.setLevel(1);
            }
        }
        
        return prodCategoryRepository.save(category);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ProdCategory> categoryOpt = prodCategoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            ProdCategory category = categoryOpt.get();
            category.setIsDeleted(true);
            category.setUpdateTime(LocalDateTime.now());
            prodCategoryRepository.save(category);
            
            // 级联删除子分类
            List<Long> childIds = findChildCategoryIds(id);
            for (Long childId : childIds) {
                deleteById(childId);
            }
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
    public void enable(Long id) {
        Optional<ProdCategory> categoryOpt = prodCategoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            ProdCategory category = categoryOpt.get();
            category.setStatus(1); // 启用状态
            category.setUpdateTime(LocalDateTime.now());
            prodCategoryRepository.save(category);
        }
    }

    @Override
    @Transactional
    public void disable(Long id) {
        Optional<ProdCategory> categoryOpt = prodCategoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            ProdCategory category = categoryOpt.get();
            category.setStatus(0); // 禁用状态
            category.setUpdateTime(LocalDateTime.now());
            prodCategoryRepository.save(category);
        }
    }

    @Override
    @Transactional
    public void show(Long id) {
        Optional<ProdCategory> categoryOpt = prodCategoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            ProdCategory category = categoryOpt.get();
            category.setIsShow(1); // 显示状态
            category.setUpdateTime(LocalDateTime.now());
            prodCategoryRepository.save(category);
        }
    }

    @Override
    @Transactional
    public void hide(Long id) {
        Optional<ProdCategory> categoryOpt = prodCategoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            ProdCategory category = categoryOpt.get();
            category.setIsShow(0); // 隐藏状态
            category.setUpdateTime(LocalDateTime.now());
            prodCategoryRepository.save(category);
        }
    }

    @Override
    public boolean existsByCategoryNameAndParentId(String categoryName, Long parentId) {
        return prodCategoryRepository.existsByCategoryNameAndParentIdAndIsDeleted(categoryName, parentId, false);
    }

    @Override
    public long countByParentId(Long parentId) {
        return prodCategoryRepository.countByParentIdAndIsDeleted(parentId, false);
    }

    @Override
    public List<Long> findChildCategoryIds(Long parentId) {
        return prodCategoryRepository.findChildCategoryIds(parentId);
    }

    @Override
    public List<Long> findAllChildCategoryIds(Long parentId) {
        return prodCategoryRepository.findAllChildCategoryIds(parentId);
    }

    @Override
    public List<ProdCategory> findAllByParentId(Long parentId) {
        return prodCategoryRepository.findByParentIdAndIsDeletedOrderBySortAsc(parentId, false);
    }

    @Override
    public List<ProdCategory> buildCategoryTree() {
        // 获取所有根分类
        List<ProdCategory> rootCategories = findRootCategories();
        
        // 为每个根分类构建子树
        for (ProdCategory root : rootCategories) {
            buildCategoryChildren(root);
        }
        
        return rootCategories;
    }

    @Override
    public List<ProdCategory> buildAdminCategoryTree() {
        // 获取所有根分类（包括禁用状态）
        List<ProdCategory> rootCategories = prodCategoryRepository.findAllRootCategories();
        
        // 为每个根分类构建子树
        for (ProdCategory root : rootCategories) {
            buildAdminCategoryChildren(root);
        }
        
        return rootCategories;
    }

    /**
     * 递归构建分类子树
     * @param category 分类节点
     */
    private void buildCategoryChildren(ProdCategory category) {
        List<ProdCategory> children = prodCategoryRepository.findByParentIdAndStatusAndIsDeletedOrderBySortAsc(category.getId(), 1, false);
        category.setChildren(children);
        
        for (ProdCategory child : children) {
            buildCategoryChildren(child);
        }
    }

    /**
     * 递归构建管理后台分类子树（包括禁用状态）
     * @param category 分类节点
     */
    private void buildAdminCategoryChildren(ProdCategory category) {
        // 管理后台查询所有子分类，包括禁用状态，但排除已删除的
        List<ProdCategory> children = prodCategoryRepository.findByParentIdAndIsDeletedOrderBySortAsc(category.getId(), false);
        log.info("为分类 {} (ID: {}) 找到子分类数量: {}", category.getCategoryName(), category.getId(), children.size());
        
        if (!children.isEmpty()) {
            for (ProdCategory child : children) {
                log.info("子分类: {} (ID: {}, Parent: {})", child.getCategoryName(), child.getId(), child.getParentId());
            }
        }
        
        category.setChildren(children);
        log.info("设置子分类后，分类 {} 的children大小: {}", category.getCategoryName(), category.getChildren().size());
        
        for (ProdCategory child : children) {
            buildAdminCategoryChildren(child);
        }
    }
} 