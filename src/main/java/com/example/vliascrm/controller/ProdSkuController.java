package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdSku;
import com.example.vliascrm.service.ProdSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SKU管理控制器
 */
@RestController
@RequestMapping("/api/prod/skus")
@RequiredArgsConstructor
public class ProdSkuController {

    private final ProdSkuService prodSkuService;

    /**
     * 获取SKU列表（支持分页）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param skuName SKU名称（可选）
     * @param goodsId 商品ID（可选）
     * @param status 状态（可选）
     * @param skuCode SKU编码（可选）
     * @return SKU分页列表
     */
    @GetMapping
    public ApiResponse<Page<ProdSku>> getSkuList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String skuName,
            @RequestParam(required = false) Long goodsId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String skuCode) {
        
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("createTime").descending());
        Page<ProdSku> page = prodSkuService.findAll(pageable);
        
        return ApiResponse.success(page);
    }

    /**
     * 根据ID获取SKU详情
     * @param id SKU ID
     * @return SKU详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ProdSku> getSkuById(@PathVariable Long id) {
        Optional<ProdSku> sku = prodSkuService.findById(id);
        return sku.map(ApiResponse::success)
                .orElse(ApiResponse.failure("SKU不存在"));
    }

    /**
     * 根据SKU编码获取SKU详情
     * @param skuCode SKU编码
     * @return SKU详情
     */
    @GetMapping("/code/{skuCode}")
    public ApiResponse<ProdSku> getSkuByCode(@PathVariable String skuCode) {
        Optional<ProdSku> sku = prodSkuService.findBySkuCode(skuCode);
        return sku.map(ApiResponse::success)
                .orElse(ApiResponse.failure("SKU不存在"));
    }

    /**
     * 根据商品ID获取SKU列表
     * @param goodsId 商品ID
     * @param activeOnly 是否只查询启用的SKU
     * @return SKU列表
     */
    @GetMapping("/goods/{goodsId}")
    public ApiResponse<List<ProdSku>> getSkusByGoodsId(
            @PathVariable Long goodsId,
            @RequestParam(defaultValue = "false") Boolean activeOnly) {
        
        List<ProdSku> skus = activeOnly ? 
            prodSkuService.findActiveByGoodsId(goodsId) : 
            prodSkuService.findByGoodsId(goodsId);
            
        return ApiResponse.success(skus);
    }

    /**
     * 根据条形码获取SKU
     * @param barcode 条形码
     * @return SKU详情
     */
    @GetMapping("/barcode/{barcode}")
    public ApiResponse<ProdSku> getSkuByBarcode(@PathVariable String barcode) {
        Optional<ProdSku> sku = prodSkuService.findByBarcode(barcode);
        return sku.map(ApiResponse::success)
                .orElse(ApiResponse.failure("SKU不存在"));
    }

    /**
     * 获取库存预警SKU列表
     * @return 库存预警SKU列表
     */
    @GetMapping("/low-stock")
    public ApiResponse<List<ProdSku>> getLowStockSkus() {
        List<ProdSku> skus = prodSkuService.findLowStockSkus();
        return ApiResponse.success(skus);
    }

    /**
     * 获取零库存SKU列表
     * @return 零库存SKU列表
     */
    @GetMapping("/zero-stock")
    public ApiResponse<List<ProdSku>> getZeroStockSkus() {
        List<ProdSku> skus = prodSkuService.findZeroStockSkus();
        return ApiResponse.success(skus);
    }

    /**
     * 获取热销SKU列表
     * @return 热销SKU列表
     */
    @GetMapping("/top-selling")
    public ApiResponse<List<ProdSku>> getTopSellingSkus() {
        List<ProdSku> skus = prodSkuService.findTopSellingSkus();
        return ApiResponse.success(skus);
    }

    /**
     * 创建SKU
     * @param sku SKU对象
     * @return 创建后的SKU
     */
    @PostMapping
    public ApiResponse<ProdSku> createSku(@RequestBody ProdSku sku) {
        // 检查SKU编码是否已存在
        if (prodSkuService.existsBySkuCode(sku.getSkuCode())) {
            return ApiResponse.failure("SKU编码已存在");
        }
        
        ProdSku savedSku = prodSkuService.save(sku);
        return ApiResponse.success(savedSku);
    }

    /**
     * 批量创建SKU
     * @param skus SKU列表
     * @return 创建后的SKU列表
     */
    @PostMapping("/batch")
    public ApiResponse<List<ProdSku>> batchCreateSkus(@RequestBody List<ProdSku> skus) {
        // 检查SKU编码是否重复
        for (ProdSku sku : skus) {
            if (prodSkuService.existsBySkuCode(sku.getSkuCode())) {
                return ApiResponse.failure("SKU编码 " + sku.getSkuCode() + " 已存在");
            }
        }
        
        List<ProdSku> savedSkus = prodSkuService.batchSave(skus);
        return ApiResponse.success(savedSkus);
    }

    /**
     * 更新SKU
     * @param id SKU ID
     * @param sku SKU对象
     * @return 更新后的SKU
     */
    @PutMapping("/{id}")
    public ApiResponse<ProdSku> updateSku(@PathVariable Long id, @RequestBody ProdSku sku) {
        Optional<ProdSku> existingSku = prodSkuService.findById(id);
        if (existingSku.isEmpty()) {
            return ApiResponse.failure("SKU不存在");
        }
        
        sku.setId(id);
        ProdSku updatedSku = prodSkuService.update(sku);
        return ApiResponse.success(updatedSku);
    }

    /**
     * 删除SKU（软删除）
     * @param id SKU ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteSku(@PathVariable Long id) {
        prodSkuService.deleteById(id);
        return ApiResponse.success(null);
    }

    /**
     * 批量删除SKU（软删除）
     * @param ids SKU ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public ApiResponse<Void> batchDeleteSkus(@RequestBody List<Long> ids) {
        prodSkuService.deleteByIds(ids);
        return ApiResponse.success(null);
    }

    /**
     * 启用SKU
     * @param id SKU ID
     * @return 操作结果
     */
    @PutMapping("/{id}/enable")
    public ApiResponse<Void> enableSku(@PathVariable Long id) {
        prodSkuService.enableSku(id);
        return ApiResponse.success(null);
    }

    /**
     * 禁用SKU
     * @param id SKU ID
     * @return 操作结果
     */
    @PutMapping("/{id}/disable")
    public ApiResponse<Void> disableSku(@PathVariable Long id) {
        prodSkuService.disableSku(id);
        return ApiResponse.success(null);
    }

    /**
     * 更新库存
     * @param id SKU ID
     * @param stockData 库存数据
     * @return 操作结果
     */
    @PutMapping("/{id}/stock")
    public ApiResponse<Void> updateStock(@PathVariable Long id, @RequestBody Map<String, Integer> stockData) {
        Integer stockQty = stockData.get("stockQty");
        if (stockQty == null || stockQty < 0) {
            return ApiResponse.failure("库存数量不能为空且不能小于0");
        }
        
        prodSkuService.updateStock(id, stockQty);
        return ApiResponse.success(null);
    }

    /**
     * 增加库存
     * @param id SKU ID
     * @param stockData 库存数据
     * @return 操作结果
     */
    @PutMapping("/{id}/stock/add")
    public ApiResponse<Void> addStock(@PathVariable Long id, @RequestBody Map<String, Integer> stockData) {
        Integer quantity = stockData.get("quantity");
        if (quantity == null || quantity <= 0) {
            return ApiResponse.failure("增加数量不能为空且必须大于0");
        }
        
        prodSkuService.addStock(id, quantity);
        return ApiResponse.success(null);
    }

    /**
     * 减少库存
     * @param id SKU ID
     * @param stockData 库存数据
     * @return 操作结果
     */
    @PutMapping("/{id}/stock/reduce")
    public ApiResponse<Void> reduceStock(@PathVariable Long id, @RequestBody Map<String, Integer> stockData) {
        Integer quantity = stockData.get("quantity");
        if (quantity == null || quantity <= 0) {
            return ApiResponse.failure("减少数量不能为空且必须大于0");
        }
        
        prodSkuService.reduceStock(id, quantity);
        return ApiResponse.success(null);
    }

    /**
     * 检查SKU编码是否存在
     * @param skuCode SKU编码
     * @return 检查结果
     */
    @GetMapping("/check-code/{skuCode}")
    public ApiResponse<Boolean> checkSkuCode(@PathVariable String skuCode) {
        boolean exists = prodSkuService.existsBySkuCode(skuCode);
        return ApiResponse.success(exists);
    }

    /**
     * 统计商品下的SKU数量
     * @param goodsId 商品ID
     * @param activeOnly 是否只统计启用的SKU
     * @return SKU数量
     */
    @GetMapping("/count/goods/{goodsId}")
    public ApiResponse<Long> countByGoodsId(
            @PathVariable Long goodsId,
            @RequestParam(defaultValue = "false") Boolean activeOnly) {
        
        long count = activeOnly ? 
            prodSkuService.countActiveByGoodsId(goodsId) : 
            prodSkuService.countByGoodsId(goodsId);
            
        return ApiResponse.success(count);
    }
} 