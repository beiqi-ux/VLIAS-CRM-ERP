package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdBrand;
import com.example.vliascrm.repository.ProdBrandRepository;
import com.example.vliascrm.service.ProdBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 商品品牌服务实现类
 */
@Service
public class ProdBrandServiceImpl implements ProdBrandService {

    @Autowired
    private ProdBrandRepository prodBrandRepository;

    @Override
    public Optional<ProdBrand> findById(Long id) {
        return prodBrandRepository.findByIdAndIsDeleted(id, false);
    }

    @Override
    public Optional<ProdBrand> findByBrandName(String brandName) {
        return prodBrandRepository.findByBrandNameAndIsDeleted(brandName, false);
    }

    @Override
    public Page<ProdBrand> findAll(Pageable pageable) {
        return prodBrandRepository.findByIsDeleted(false, pageable);
    }

    @Override
    public List<ProdBrand> findAll() {
        return prodBrandRepository.findByIsDeleted(false);
    }

    @Override
    public List<ProdBrand> findByBrandNameContaining(String brandName) {
        return prodBrandRepository.findByBrandNameContainingAndStatusAndIsDeleted(brandName, 1, false);
    }

    @Override
    public List<ProdBrand> findActivesBrands() {
        return prodBrandRepository.findByStatusAndIsDeletedOrderBySortAsc(1, false);
    }

    @Override
    @Transactional
    public ProdBrand save(ProdBrand brand) {
        // 设置创建时间和状态
        if (brand.getId() == null) {
            brand.setCreateTime(LocalDateTime.now());
            brand.setIsDeleted(false);
            
            // 确保status字段有值
            if (brand.getStatus() == null) {
            brand.setStatus(1); // 默认启用状态
            }
            
            // 确保sort字段有值
            if (brand.getSort() == null) {
                brand.setSort(0); // 默认排序
            }
        }
        return prodBrandRepository.save(brand);
    }

    @Override
    @Transactional
    public ProdBrand update(ProdBrand brand) {
        // 获取原有数据
        Optional<ProdBrand> existingBrandOpt = prodBrandRepository.findByIdAndIsDeleted(brand.getId(), false);
        if (!existingBrandOpt.isPresent()) {
            throw new RuntimeException("品牌不存在或已被删除");
        }
        
        ProdBrand existingBrand = existingBrandOpt.get();
        
        // 保留原有的关键字段
        brand.setCreateTime(existingBrand.getCreateTime());
        brand.setIsDeleted(existingBrand.getIsDeleted());
        
        // 确保status字段有值
        if (brand.getStatus() == null) {
            brand.setStatus(existingBrand.getStatus() != null ? existingBrand.getStatus() : 1);
        }
        
        // 确保sort字段有值
        if (brand.getSort() == null) {
            brand.setSort(existingBrand.getSort() != null ? existingBrand.getSort() : 0);
        }
        
        // 设置更新时间
        brand.setUpdateTime(LocalDateTime.now());
        return prodBrandRepository.save(brand);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ProdBrand> brandOpt = prodBrandRepository.findById(id);
        if (brandOpt.isPresent()) {
            ProdBrand brand = brandOpt.get();
            brand.setIsDeleted(true);
            brand.setUpdateTime(LocalDateTime.now());
            prodBrandRepository.save(brand);
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
        Optional<ProdBrand> brandOpt = prodBrandRepository.findByIdAndIsDeleted(id, false);
        if (brandOpt.isPresent()) {
            ProdBrand brand = brandOpt.get();
            brand.setStatus(1); // 启用状态
            brand.setUpdateTime(LocalDateTime.now());
            prodBrandRepository.save(brand);
        }
    }

    @Override
    @Transactional
    public void disable(Long id) {
        Optional<ProdBrand> brandOpt = prodBrandRepository.findByIdAndIsDeleted(id, false);
        if (brandOpt.isPresent()) {
            ProdBrand brand = brandOpt.get();
            brand.setStatus(0); // 禁用状态
            brand.setUpdateTime(LocalDateTime.now());
            prodBrandRepository.save(brand);
        }
    }

    @Override
    public boolean existsByBrandName(String brandName) {
        return prodBrandRepository.existsByBrandNameAndIsDeleted(brandName, false);
    }

    @Override
    public long countByStatus(Integer status) {
        return prodBrandRepository.countByStatusAndIsDeleted(status, false);
    }
} 