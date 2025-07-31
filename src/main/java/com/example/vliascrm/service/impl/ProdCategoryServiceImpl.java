package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdCategory;
import com.example.vliascrm.repository.ProdCategoryRepository;
import com.example.vliascrm.service.ProdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 商品分类服务实现类
 */
@Service
public class ProdCategoryServiceImpl implements ProdCategoryService {

    @Autowired
    private ProdCategoryRepository prodCategoryRepository;

    @Override
    public Optional<ProdCategory> findById(Long id) {
        return prodCategoryRepository.findById(id);
    }

    @Override
    public List<ProdCategory> findAll() {
        return prodCategoryRepository.findAll();
    }

    @Override
    public List<ProdCategory> findByParentId(Long parentId) {
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
                category.setParentId(0L);
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
    public List<ProdCategory> buildCategoryTree() {
        // 获取所有根分类
        List<ProdCategory> rootCategories = findRootCategories();
        
        // 为每个根分类构建子树
        for (ProdCategory root : rootCategories) {
            buildCategoryChildren(root);
        }
        
        return rootCategories;
    }

    /**
     * 递归构建分类子树
     * @param category 分类节点
     */
    private void buildCategoryChildren(ProdCategory category) {
        List<ProdCategory> children = findByParentId(category.getId());
        category.setChildren(children);
        
        for (ProdCategory child : children) {
            buildCategoryChildren(child);
        }
    }
} 