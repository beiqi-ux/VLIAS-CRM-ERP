package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdGoods;
import com.example.vliascrm.service.ProdGoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 商品管理控制器
 */
@RestController
@RequestMapping("/api/prod/goods")
@RequiredArgsConstructor
public class ProdGoodsController {

    private final ProdGoodsService prodGoodsService;

    /**
     * 获取商品列表（支持分页）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param goodsName 商品名称（可选）
     * @param categoryId 分类ID（可选）
     * @param brandId 品牌ID（可选）
     * @param status 状态（可选）
     * @param auditStatus 审核状态（可选）
     * @return 商品分页列表
     */
    @GetMapping
    @PreAuthorize("hasAuthority('product-goods-management:view')")
    public ApiResponse<Page<ProdGoods>> getGoodsList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String goodsName,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer auditStatus) {
        
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, Sort.by("createTime").descending());
        Page<ProdGoods> page = prodGoodsService.findAll(pageable);
        
        return ApiResponse.success(page);
    }

    /**
     * 根据ID获取商品详情
     * @param id 商品ID
     * @return 商品详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('product-goods-management:view')")
    public ApiResponse<ProdGoods> getGoodsById(@PathVariable Long id) {
        Optional<ProdGoods> goods = prodGoodsService.findById(id);
        return goods.map(ApiResponse::success)
                  .orElse(ApiResponse.failure("商品不存在"));
    }

    /**
     * 根据商品编码获取商品
     * @param goodsCode 商品编码
     * @return 商品信息
     */
    @GetMapping("/code/{goodsCode}")
    public ApiResponse<ProdGoods> getGoodsByCode(@PathVariable String goodsCode) {
        Optional<ProdGoods> goods = prodGoodsService.findByGoodsCode(goodsCode);
        if (goods.isPresent()) {
            return ApiResponse.success(goods.get());
        }
        return ApiResponse.failure("商品不存在");
    }

    /**
     * 根据分类ID获取商品列表
     * @param categoryId 分类ID
     * @return 商品列表
     */
    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<ProdGoods>> getGoodsByCategory(@PathVariable Long categoryId) {
        List<ProdGoods> goodsList = prodGoodsService.findByCategoryId(categoryId);
        return ApiResponse.success(goodsList);
    }

    /**
     * 根据品牌ID获取商品列表
     * @param brandId 品牌ID
     * @return 商品列表
     */
    @GetMapping("/brand/{brandId}")
    public ApiResponse<List<ProdGoods>> getGoodsByBrand(@PathVariable Long brandId) {
        List<ProdGoods> goodsList = prodGoodsService.findByBrandId(brandId);
        return ApiResponse.success(goodsList);
    }

    /**
     * 获取推荐商品
     * @return 推荐商品列表
     */
    @GetMapping("/recommended")
    public ApiResponse<List<ProdGoods>> getRecommendedGoods() {
        List<ProdGoods> goodsList = prodGoodsService.findRecommendedGoods();
        return ApiResponse.success(goodsList);
    }

    /**
     * 获取热销商品
     * @return 热销商品列表
     */
    @GetMapping("/hot")
    public ApiResponse<List<ProdGoods>> getHotGoods() {
        List<ProdGoods> goodsList = prodGoodsService.findHotGoods();
        return ApiResponse.success(goodsList);
    }

    /**
     * 获取新品
     * @return 新品列表
     */
    @GetMapping("/new")
    public ApiResponse<List<ProdGoods>> getNewGoods() {
        List<ProdGoods> goodsList = prodGoodsService.findNewGoods();
        return ApiResponse.success(goodsList);
    }

    /**
     * 获取库存预警商品
     * @return 库存预警商品列表
     */
    @GetMapping("/low-stock")
    public ApiResponse<List<ProdGoods>> getLowStockGoods() {
        List<ProdGoods> goodsList = prodGoodsService.findLowStockGoods();
        return ApiResponse.success(goodsList);
    }

    /**
     * 根据审核状态获取商品
     * @param auditStatus 审核状态
     * @return 商品列表
     */
    @GetMapping("/audit/{auditStatus}")
    public ApiResponse<List<ProdGoods>> getGoodsByAuditStatus(@PathVariable Integer auditStatus) {
        List<ProdGoods> goodsList = prodGoodsService.findByAuditStatus(auditStatus);
        return ApiResponse.success(goodsList);
    }

    /**
     * 搜索商品
     * @param goodsName 商品名称
     * @return 商品列表
     */
    @GetMapping("/search")
    public ApiResponse<List<ProdGoods>> searchGoods(@RequestParam String goodsName) {
        List<ProdGoods> goodsList = prodGoodsService.findByGoodsNameContaining(goodsName);
        return ApiResponse.success(goodsList);
    }

    /**
     * 创建商品
     * @param goods 商品信息
     * @return 创建后的商品
     */
    @PostMapping
    @PreAuthorize("hasAuthority('product-goods-management:create')")
    public ApiResponse<ProdGoods> createGoods(@RequestBody ProdGoods goods) {
        // 检查商品编码是否存在
        if (goods.getGoodsCode() != null && prodGoodsService.existsByGoodsCode(goods.getGoodsCode())) {
            return ApiResponse.failure("商品编码已存在");
        }
        
        ProdGoods savedGoods = prodGoodsService.save(goods);
        return ApiResponse.success(savedGoods);
    }

    /**
     * 更新商品
     * @param id 商品ID
     * @param goods 商品信息
     * @return 更新后的商品
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('product-goods-management:edit')")
    public ApiResponse<ProdGoods> updateGoods(@PathVariable Long id, @RequestBody ProdGoods goods) {
        Optional<ProdGoods> existingGoods = prodGoodsService.findById(id);
        if (!existingGoods.isPresent()) {
            return ApiResponse.failure("商品不存在");
        }
        
        goods.setId(id);
        ProdGoods updatedGoods = prodGoodsService.update(goods);
        return ApiResponse.success(updatedGoods);
    }

    /**
     * 删除商品（软删除）
     * @param id 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('product-goods-management:delete')")
    public ApiResponse<String> deleteGoods(@PathVariable Long id) {
        Optional<ProdGoods> goods = prodGoodsService.findById(id);
        if (!goods.isPresent()) {
            return ApiResponse.failure("商品不存在");
        }
        
        prodGoodsService.deleteById(id);
        return ApiResponse.success("删除成功");
    }

    /**
     * 批量删除商品（软删除）
     * @param ids 商品ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public ApiResponse<String> batchDeleteGoods(@RequestBody List<Long> ids) {
        prodGoodsService.deleteByIds(ids);
        return ApiResponse.success("批量删除成功");
    }

    /**
     * 上架商品
     * @param id 商品ID
     * @return 操作结果
     */
    @PutMapping("/{id}/on-sale")
    public ApiResponse<String> onSaleGoods(@PathVariable Long id) {
        Optional<ProdGoods> goods = prodGoodsService.findById(id);
        if (!goods.isPresent()) {
            return ApiResponse.failure("商品不存在");
        }
        
        prodGoodsService.onSale(id);
        return ApiResponse.success("商品上架成功");
    }

    /**
     * 下架商品
     * @param id 商品ID
     * @return 操作结果
     */
    @PutMapping("/{id}/off-sale")
    public ApiResponse<String> offSaleGoods(@PathVariable Long id) {
        Optional<ProdGoods> goods = prodGoodsService.findById(id);
        if (!goods.isPresent()) {
            return ApiResponse.failure("商品不存在");
        }
        
        prodGoodsService.offSale(id);
        return ApiResponse.success("商品下架成功");
    }

    /**
     * 审核商品
     * @param id 商品ID
     * @param auditData 审核数据
     * @return 操作结果
     */
    @PutMapping("/{id}/audit")
    @PreAuthorize("hasAuthority('product-goods-management:audit')")
    public ApiResponse<String> auditGoods(@PathVariable Long id, @RequestBody Map<String, Object> auditData) {
        Optional<ProdGoods> goods = prodGoodsService.findById(id);
        if (!goods.isPresent()) {
            return ApiResponse.failure("商品不存在");
        }
        
        Integer auditStatus = (Integer) auditData.get("auditStatus");
        String auditRemark = (String) auditData.get("auditRemark");
        Long auditUserId = Long.valueOf(auditData.get("auditUserId").toString());
        
        prodGoodsService.auditGoods(id, auditStatus, auditRemark, auditUserId);
        return ApiResponse.success("审核完成");
    }

    /**
     * 更新库存
     * @param id 商品ID
     * @param stockData 库存数据
     * @return 操作结果
     */
    @PutMapping("/{id}/stock")
    public ApiResponse<String> updateStock(@PathVariable Long id, @RequestBody Map<String, Object> stockData) {
        Optional<ProdGoods> goods = prodGoodsService.findById(id);
        if (!goods.isPresent()) {
            return ApiResponse.failure("商品不存在");
        }
        
        Integer stockQty = (Integer) stockData.get("stockQty");
        prodGoodsService.updateStock(id, stockQty);
        return ApiResponse.success("库存更新成功");
    }

    /**
     * 检查商品编码是否存在
     * @param goodsCode 商品编码
     * @return 检查结果
     */
    @GetMapping("/check-code")
    public ApiResponse<Boolean> checkGoodsCode(@RequestParam String goodsCode) {
        boolean exists = prodGoodsService.existsByGoodsCode(goodsCode);
        return ApiResponse.success(exists);
    }

    /**
     * 统计分类下的商品数量
     * @param categoryId 分类ID
     * @return 商品数量
     */
    @GetMapping("/count/category/{categoryId}")
    public ApiResponse<Long> countByCategory(@PathVariable Long categoryId) {
        long count = prodGoodsService.countByCategoryId(categoryId);
        return ApiResponse.success(count);
    }

    /**
     * 统计品牌下的商品数量
     * @param brandId 品牌ID
     * @return 商品数量
     */
    @GetMapping("/count/brand/{brandId}")
    public ApiResponse<Long> countByBrand(@PathVariable Long brandId) {
        long count = prodGoodsService.countByBrandId(brandId);
        return ApiResponse.success(count);
    }
} 