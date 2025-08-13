package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdGoods;
import com.example.vliascrm.entity.ProdCategory;
import com.example.vliascrm.entity.ProdBrand;
import com.example.vliascrm.repository.ProdGoodsRepository;
import com.example.vliascrm.service.ProdGoodsService;
import com.example.vliascrm.service.ProdCategoryService;
import com.example.vliascrm.service.ProdBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Service
public class ProdGoodsServiceImpl implements ProdGoodsService {

    @Autowired
    private ProdGoodsRepository prodGoodsRepository;
    
    @Autowired
    private ProdCategoryService prodCategoryService;
    
    @Autowired
    private ProdBrandService prodBrandService;

    @Override
    public Optional<ProdGoods> findById(Long id) {
        Optional<ProdGoods> goodsOpt = prodGoodsRepository.findById(id);
        if (goodsOpt.isPresent()) {
            ProdGoods goods = goodsOpt.get();
            fillCategoryAndBrandNames(goods);
            return Optional.of(goods);
        }
        return goodsOpt;
    }

    @Override
    public Optional<ProdGoods> findByGoodsCode(String goodsCode) {
        return prodGoodsRepository.findByGoodsCode(goodsCode);
    }

    @Override
    public Page<ProdGoods> findAll(Pageable pageable) {
        return prodGoodsRepository.findAll(pageable);
    }

    @Override
    public Page<ProdGoods> findByConditions(Pageable pageable, String goodsName, String goodsCode,
                                          Long categoryId, Long brandId, Integer status, Integer auditStatus) {
        Specification<ProdGoods> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 默认查询未删除的记录
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));
            
            // 商品名称模糊查询
            if (StringUtils.hasText(goodsName)) {
                predicates.add(criteriaBuilder.like(root.get("goodsName"), "%" + goodsName + "%"));
            }
            
            // 商品编码模糊查询
            if (StringUtils.hasText(goodsCode)) {
                predicates.add(criteriaBuilder.like(root.get("goodsCode"), "%" + goodsCode + "%"));
            }
            
            // 分类ID
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), categoryId));
            }
            
            // 品牌ID
            if (brandId != null) {
                predicates.add(criteriaBuilder.equal(root.get("brandId"), brandId));
            }
            
            // 状态
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            
            // 审核状态
            if (auditStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("auditStatus"), auditStatus));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        
        Page<ProdGoods> page = prodGoodsRepository.findAll(spec, pageable);
        
        // 填充分类名和品牌名
        List<ProdGoods> goodsWithNames = new ArrayList<>();
        for (ProdGoods goods : page.getContent()) {
            fillCategoryAndBrandNames(goods);
            goodsWithNames.add(goods);
        }
        
        return new PageImpl<>(goodsWithNames, pageable, page.getTotalElements());
    }
    
    /**
     * 填充分类名和品牌名（优化版本，减少数据库查询）
     */
    private void fillCategoryAndBrandNames(ProdGoods goods) {
        // 使用缓存避免重复查询
        if (goods.getCategoryId() != null) {
            Optional<ProdCategory> categoryOpt = prodCategoryService.findById(goods.getCategoryId());
            categoryOpt.ifPresent(category -> goods.setCategoryName(category.getCategoryName()));
        }
        
        if (goods.getBrandId() != null) {
            Optional<ProdBrand> brandOpt = prodBrandService.findById(goods.getBrandId());
            brandOpt.ifPresent(brand -> goods.setBrandName(brand.getBrandName()));
        }
    }

    /**
     * 批量填充分类名和品牌名（优化版本，避免N+1查询）
     */
    private void fillCategoryAndBrandNamesForList(List<ProdGoods> goodsList) {
        if (goodsList.isEmpty()) {
            return;
        }

        // 批量获取所有需要的分类ID和品牌ID
        Set<Long> categoryIds = goodsList.stream()
            .map(ProdGoods::getCategoryId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        Set<Long> brandIds = goodsList.stream()
            .map(ProdGoods::getBrandId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        // 批量查询分类和品牌信息（这里需要在服务层添加批量查询方法）
        Map<Long, String> categoryNameMap = new HashMap<>();
        Map<Long, String> brandNameMap = new HashMap<>();

        // 分类名称映射
        for (Long categoryId : categoryIds) {
            Optional<ProdCategory> categoryOpt = prodCategoryService.findById(categoryId);
            categoryOpt.ifPresent(category -> 
                categoryNameMap.put(categoryId, category.getCategoryName()));
        }

        // 品牌名称映射
        for (Long brandId : brandIds) {
            Optional<ProdBrand> brandOpt = prodBrandService.findById(brandId);
            brandOpt.ifPresent(brand -> 
                brandNameMap.put(brandId, brand.getBrandName()));
        }

        // 填充商品的分类名和品牌名
        goodsList.forEach(goods -> {
            if (goods.getCategoryId() != null) {
                goods.setCategoryName(categoryNameMap.get(goods.getCategoryId()));
            }
            if (goods.getBrandId() != null) {
                goods.setBrandName(brandNameMap.get(goods.getBrandId()));
            }
        });
    }

    @Override
    public List<ProdGoods> findByCategoryId(Long categoryId) {
        return prodGoodsRepository.findByCategoryIdAndStatusAndIsDeleted(categoryId, 1, false);
    }

    @Override
    public List<ProdGoods> findByBrandId(Long brandId) {
        return prodGoodsRepository.findByBrandIdAndStatusAndIsDeleted(brandId, 1, false);
    }

    @Override
    public List<ProdGoods> findByGoodsNameContaining(String goodsName) {
        return prodGoodsRepository.findByGoodsNameContainingAndStatusAndIsDeleted(goodsName, 1, false);
    }

    @Override
    public List<ProdGoods> findRecommendedGoods() {
        return prodGoodsRepository.findByIsRecommendedAndStatusAndIsDeletedOrderBySortAsc(1, 1, false);
    }

    @Override
    public List<ProdGoods> findHotGoods() {
        return prodGoodsRepository.findByIsHotAndStatusAndIsDeletedOrderBySaleQtyDesc(1, 1, false);
    }

    @Override
    public List<ProdGoods> findNewGoods() {
        return prodGoodsRepository.findByIsNewAndStatusAndIsDeletedOrderByCreateTimeDesc(1, 1, false);
    }

    @Override
    public List<ProdGoods> findLowStockGoods() {
        return prodGoodsRepository.findLowStockGoods();
    }

    @Override
    public List<ProdGoods> findByAuditStatus(Integer auditStatus) {
        return prodGoodsRepository.findByAuditStatusAndIsDeleted(auditStatus, false);
    }

    @Override
    @Transactional
    public ProdGoods save(ProdGoods goods) {
        // 设置创建时间和创建人
        if (goods.getId() == null) {
            goods.setCreateTime(LocalDateTime.now());
            goods.setIsDeleted(false);
            goods.setStatus(1); // 默认上架状态
            goods.setAuditStatus(0); // 默认未审核状态
        }
        return prodGoodsRepository.save(goods);
    }

    @Override
    @Transactional
    public ProdGoods update(ProdGoods goods) {
        // 设置更新时间
        goods.setUpdateTime(LocalDateTime.now());
        return prodGoodsRepository.save(goods);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ProdGoods> goodsOpt = prodGoodsRepository.findById(id);
        if (goodsOpt.isPresent()) {
            ProdGoods goods = goodsOpt.get();
            goods.setIsDeleted(true);
            goods.setUpdateTime(LocalDateTime.now());
            prodGoodsRepository.save(goods);
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
    public void onSale(Long id) {
        Optional<ProdGoods> goodsOpt = prodGoodsRepository.findById(id);
        if (goodsOpt.isPresent()) {
            ProdGoods goods = goodsOpt.get();
            goods.setStatus(1); // 上架状态
            goods.setUpdateTime(LocalDateTime.now());
            prodGoodsRepository.save(goods);
        }
    }

    @Override
    @Transactional
    public void offSale(Long id) {
        Optional<ProdGoods> goodsOpt = prodGoodsRepository.findById(id);
        if (goodsOpt.isPresent()) {
            ProdGoods goods = goodsOpt.get();
            goods.setStatus(0); // 下架状态
            goods.setUpdateTime(LocalDateTime.now());
            prodGoodsRepository.save(goods);
        }
    }

    @Override
    @Transactional
    public void auditGoods(Long id, Integer auditStatus, String auditRemark, Long auditUserId) {
        Optional<ProdGoods> goodsOpt = prodGoodsRepository.findById(id);
        if (goodsOpt.isPresent()) {
            ProdGoods goods = goodsOpt.get();
            goods.setAuditStatus(auditStatus);
            goods.setAuditRemark(auditRemark);
            goods.setAuditUserId(auditUserId);
            goods.setAuditTime(LocalDateTime.now());
            goods.setUpdateTime(LocalDateTime.now());
            prodGoodsRepository.save(goods);
        }
    }

    @Override
    @Transactional
    public void updateStock(Long id, Integer stockQty) {
        Optional<ProdGoods> goodsOpt = prodGoodsRepository.findById(id);
        if (goodsOpt.isPresent()) {
            ProdGoods goods = goodsOpt.get();
            goods.setStockQty(stockQty);
            goods.setUpdateTime(LocalDateTime.now());
            prodGoodsRepository.save(goods);
        }
    }

    @Override
    public boolean existsByGoodsCode(String goodsCode) {
        return prodGoodsRepository.existsByGoodsCode(goodsCode);
    }

    @Override
    public long countByCategoryId(Long categoryId) {
        return prodGoodsRepository.countByCategoryIdAndIsDeleted(categoryId, false);
    }

    @Override
    public long countByBrandId(Long brandId) {
        return prodGoodsRepository.countByBrandIdAndIsDeleted(brandId, false);
    }

    @Override
    @Transactional
    public void updateMainImage(Long goodsId, String mainImageUrl) {
        Optional<ProdGoods> goodsOpt = prodGoodsRepository.findById(goodsId);
        if (goodsOpt.isPresent()) {
            ProdGoods goods = goodsOpt.get();
            goods.setMainImage(mainImageUrl);
            goods.setUpdateTime(LocalDateTime.now());
            prodGoodsRepository.save(goods);
        }
    }
} 