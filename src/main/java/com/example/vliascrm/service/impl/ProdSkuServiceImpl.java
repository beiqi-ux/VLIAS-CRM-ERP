package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.ProdSku;
import com.example.vliascrm.repository.ProdSkuRepository;
import com.example.vliascrm.service.ProdSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * SKU服务实现类
 */
@Service
public class ProdSkuServiceImpl implements ProdSkuService {

    @Autowired
    private ProdSkuRepository prodSkuRepository;

    @Override
    public Optional<ProdSku> findById(Long id) {
        return prodSkuRepository.findById(id);
    }

    @Override
    public Optional<ProdSku> findBySkuCode(String skuCode) {
        return prodSkuRepository.findBySkuCode(skuCode);
    }

    @Override
    public Page<ProdSku> findAll(Pageable pageable) {
        return prodSkuRepository.findByIsDeleted(false, pageable);
    }

    @Override
    public List<ProdSku> findByGoodsId(Long goodsId) {
        return prodSkuRepository.findByGoodsIdAndIsDeleted(goodsId, false);
    }

    @Override
    public List<ProdSku> findActiveByGoodsId(Long goodsId) {
        return prodSkuRepository.findByGoodsIdAndStatusAndIsDeleted(goodsId, 1, false);
    }

    @Override
    public List<ProdSku> findBySkuNameContaining(String skuName) {
        return prodSkuRepository.findBySkuNameContainingAndStatusAndIsDeleted(skuName, 1, false);
    }

    @Override
    public Optional<ProdSku> findByBarcode(String barcode) {
        return prodSkuRepository.findByBarcodeAndIsDeleted(barcode, false);
    }

    @Override
    public List<ProdSku> findLowStockSkus() {
        return prodSkuRepository.findLowStockSkus();
    }

    @Override
    public List<ProdSku> findZeroStockSkus() {
        return prodSkuRepository.findByStockQtyAndStatusAndIsDeleted(0, 1, false);
    }

    @Override
    public List<ProdSku> findTopSellingSkus() {
        return prodSkuRepository.findTopSellingSkus();
    }

    @Override
    @Transactional
    public ProdSku save(ProdSku sku) {
        return prodSkuRepository.save(sku);
    }

    @Override
    @Transactional
    public ProdSku update(ProdSku sku) {
        // 先查询现有的SKU
        Optional<ProdSku> existingSkuOpt = prodSkuRepository.findById(sku.getId());
        if (existingSkuOpt.isEmpty()) {
            throw new RuntimeException("SKU不存在");
        }
        
        ProdSku existingSku = existingSkuOpt.get();
        
        // 只更新允许更新的业务字段
        if (sku.getSkuName() != null) {
            existingSku.setSkuName(sku.getSkuName());
        }
        if (sku.getSkuCode() != null) {
            existingSku.setSkuCode(sku.getSkuCode());
        }
        if (sku.getSellingPrice() != null) {
            existingSku.setSellingPrice(sku.getSellingPrice());
        }
        if (sku.getStockQty() != null) {
            existingSku.setStockQty(sku.getStockQty());
        }
        if (sku.getStatus() != null) {
            existingSku.setStatus(sku.getStatus());
        }
        if (sku.getOriginalPrice() != null) {
            existingSku.setOriginalPrice(sku.getOriginalPrice());
        }
        if (sku.getCostPrice() != null) {
            existingSku.setCostPrice(sku.getCostPrice());
        }
        if (sku.getMinPrice() != null) {
            existingSku.setMinPrice(sku.getMinPrice());
        }
        if (sku.getWeight() != null) {
            existingSku.setWeight(sku.getWeight());
        }
        if (sku.getVolume() != null) {
            existingSku.setVolume(sku.getVolume());
        }
        if (sku.getWarnStock() != null) {
            existingSku.setWarnStock(sku.getWarnStock());
        }
        if (sku.getSkuImage() != null) {
            existingSku.setSkuImage(sku.getSkuImage());
        }
        if (sku.getBarcode() != null) {
            existingSku.setBarcode(sku.getBarcode());
        }
        if (sku.getSort() != null) {
            existingSku.setSort(sku.getSort());
        }
        if (sku.getRemark() != null) {
            existingSku.setRemark(sku.getRemark());
        }
        if (sku.getSpecValues() != null) {
            existingSku.setSpecValues(sku.getSpecValues());
        }
        
        // 保存更新后的SKU
        return prodSkuRepository.save(existingSku);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ProdSku> skuOpt = prodSkuRepository.findById(id);
        if (skuOpt.isPresent()) {
            ProdSku sku = skuOpt.get();
            sku.setIsDeleted(true);
            prodSkuRepository.save(sku);
        }
    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        List<ProdSku> skus = prodSkuRepository.findAllById(ids);
        for (ProdSku sku : skus) {
            sku.setIsDeleted(true);
        }
        prodSkuRepository.saveAll(skus);
    }

    @Override
    @Transactional
    public void enableSku(Long id) {
        Optional<ProdSku> skuOpt = prodSkuRepository.findById(id);
        if (skuOpt.isPresent()) {
            ProdSku sku = skuOpt.get();
            sku.setStatus(1);
            prodSkuRepository.save(sku);
        }
    }

    @Override
    @Transactional
    public void disableSku(Long id) {
        Optional<ProdSku> skuOpt = prodSkuRepository.findById(id);
        if (skuOpt.isPresent()) {
            ProdSku sku = skuOpt.get();
            sku.setStatus(0);
            prodSkuRepository.save(sku);
        }
    }

    @Override
    @Transactional
    public void updateStock(Long id, Integer stockQty) {
        Optional<ProdSku> skuOpt = prodSkuRepository.findById(id);
        if (skuOpt.isPresent()) {
            ProdSku sku = skuOpt.get();
            sku.setStockQty(stockQty);
            prodSkuRepository.save(sku);
        }
    }

    @Override
    @Transactional
    public void addStock(Long id, Integer quantity) {
        Optional<ProdSku> skuOpt = prodSkuRepository.findById(id);
        if (skuOpt.isPresent()) {
            ProdSku sku = skuOpt.get();
            sku.setStockQty(sku.getStockQty() + quantity);
            prodSkuRepository.save(sku);
        }
    }

    @Override
    @Transactional
    public void reduceStock(Long id, Integer quantity) {
        Optional<ProdSku> skuOpt = prodSkuRepository.findById(id);
        if (skuOpt.isPresent()) {
            ProdSku sku = skuOpt.get();
            int newStock = Math.max(0, sku.getStockQty() - quantity);
            sku.setStockQty(newStock);
            prodSkuRepository.save(sku);
        }
    }

    @Override
    @Transactional
    public void updateSaleQty(Long id, Integer saleQty) {
        Optional<ProdSku> skuOpt = prodSkuRepository.findById(id);
        if (skuOpt.isPresent()) {
            ProdSku sku = skuOpt.get();
            sku.setSaleQty(saleQty);
            prodSkuRepository.save(sku);
        }
    }

    @Override
    public boolean existsBySkuCode(String skuCode) {
        return prodSkuRepository.existsBySkuCode(skuCode);
    }

    @Override
    public long countByGoodsId(Long goodsId) {
        return prodSkuRepository.countByGoodsIdAndIsDeleted(goodsId, false);
    }

    @Override
    public long countActiveByGoodsId(Long goodsId) {
        return prodSkuRepository.countByGoodsIdAndStatusAndIsDeleted(goodsId, 1, false);
    }

    @Override
    @Transactional
    public List<ProdSku> batchSave(List<ProdSku> skus) {
        return prodSkuRepository.saveAll(skus);
    }
} 