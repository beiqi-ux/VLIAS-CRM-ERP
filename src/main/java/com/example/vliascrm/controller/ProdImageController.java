package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.entity.ProdImage;
import com.example.vliascrm.service.ProdImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * 商品图片管理控制器
 */
@RestController
@RequestMapping("/api/prod/images")
@RequiredArgsConstructor
public class ProdImageController {

    private final ProdImageService prodImageService;

    /**
     * 根据ID获取图片详情
     * @param id 图片ID
     * @return 图片详情
     */
    @GetMapping("/{id}")
    public ApiResponse<ProdImage> getImageById(@PathVariable Long id) {
        Optional<ProdImage> image = prodImageService.findById(id);
        if (image.isPresent()) {
            return ApiResponse.success(image.get());
        }
        return ApiResponse.failure("图片不存在");
    }

    /**
     * 根据商品ID获取图片列表
     * @param goodsId 商品ID
     * @return 图片列表
     */
    @GetMapping("/goods/{goodsId}")
    public ApiResponse<List<ProdImage>> getImagesByGoodsId(@PathVariable Long goodsId) {
        List<ProdImage> images = prodImageService.findByGoodsId(goodsId);
        return ApiResponse.success(images);
    }

    /**
     * 根据商品ID获取主图
     * @param goodsId 商品ID
     * @return 主图
     */
    @GetMapping("/goods/{goodsId}/main")
    public ApiResponse<ProdImage> getMainImageByGoodsId(@PathVariable Long goodsId) {
        Optional<ProdImage> mainImage = prodImageService.findMainImageByGoodsId(goodsId);
        if (mainImage.isPresent()) {
            return ApiResponse.success(mainImage.get());
        }
        return ApiResponse.failure("主图不存在");
    }

    /**
     * 创建图片
     * @param image 图片信息
     * @return 创建后的图片
     */
    @PostMapping
    public ApiResponse<ProdImage> createImage(@RequestBody ProdImage image) {
        ProdImage savedImage = prodImageService.save(image);
        return ApiResponse.success(savedImage);
    }

    /**
     * 批量创建图片
     * @param images 图片列表
     * @return 创建后的图片列表
     */
    @PostMapping("/batch")
    public ApiResponse<List<ProdImage>> createImages(@RequestBody List<ProdImage> images) {
        List<ProdImage> savedImages = prodImageService.saveAll(images);
        return ApiResponse.success(savedImages);
    }

    /**
     * 更新图片
     * @param id 图片ID
     * @param image 图片信息
     * @return 更新后的图片
     */
    @PutMapping("/{id}")
    public ApiResponse<ProdImage> updateImage(@PathVariable Long id, @RequestBody ProdImage image) {
        Optional<ProdImage> existingImage = prodImageService.findById(id);
        if (!existingImage.isPresent()) {
            return ApiResponse.failure("图片不存在");
        }
        
        image.setId(id);
        ProdImage updatedImage = prodImageService.update(image);
        return ApiResponse.success(updatedImage);
    }

    /**
     * 删除图片（软删除）
     * @param id 图片ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteImage(@PathVariable Long id) {
        Optional<ProdImage> image = prodImageService.findById(id);
        if (!image.isPresent()) {
            return ApiResponse.failure("图片不存在");
        }
        
        prodImageService.deleteById(id);
        return ApiResponse.success("删除成功");
    }

    /**
     * 批量删除图片（软删除）
     * @param ids 图片ID列表
     * @return 操作结果
     */
    @DeleteMapping("/batch")
    public ApiResponse<String> batchDeleteImages(@RequestBody List<Long> ids) {
        prodImageService.deleteByIds(ids);
        return ApiResponse.success("批量删除成功");
    }

    /**
     * 删除商品的所有图片
     * @param goodsId 商品ID
     * @return 操作结果
     */
    @DeleteMapping("/goods/{goodsId}")
    public ApiResponse<String> deleteImagesByGoodsId(@PathVariable Long goodsId) {
        prodImageService.deleteByGoodsId(goodsId);
        return ApiResponse.success("删除成功");
    }

    /**
     * 设置主图
     * @param id 图片ID
     * @return 操作结果
     */
    @PutMapping("/{id}/set-main")
    public ApiResponse<String> setAsMainImage(@PathVariable Long id) {
        Optional<ProdImage> image = prodImageService.findById(id);
        if (!image.isPresent()) {
            return ApiResponse.failure("图片不存在");
        }
        
        prodImageService.setAsMainImage(id);
        return ApiResponse.success("设置主图成功");
    }

    /**
     * 取消商品的所有主图状态
     * @param goodsId 商品ID
     * @return 操作结果
     */
    @PutMapping("/goods/{goodsId}/clear-main")
    public ApiResponse<String> clearMainImageByGoodsId(@PathVariable Long goodsId) {
        prodImageService.clearMainImageByGoodsId(goodsId);
        return ApiResponse.success("清除主图状态成功");
    }

    /**
     * 统计商品图片数量
     * @param goodsId 商品ID
     * @return 图片数量
     */
    @GetMapping("/count/goods/{goodsId}")
    public ApiResponse<Long> countByGoodsId(@PathVariable Long goodsId) {
        long count = prodImageService.countByGoodsId(goodsId);
        return ApiResponse.success(count);
    }
} 