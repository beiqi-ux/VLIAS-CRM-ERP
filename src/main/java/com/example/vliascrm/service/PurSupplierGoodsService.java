package com.example.vliascrm.service;

import com.example.vliascrm.dto.PurSupplierGoodsDto;
import com.example.vliascrm.dto.SupplierPriceCompareDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 供应商商品服务接口
 */
public interface PurSupplierGoodsService {

    /**
     * 分页查询供应商商品列表
     * @param supplierId 供应商ID
     * @param goodsId 商品ID
     * @param goodsName 商品名称
     * @param pageable 分页参数
     * @return 供应商商品分页数据
     */
    Page<PurSupplierGoodsDto> getSupplierGoodsPage(Long supplierId, Long goodsId, String goodsName, Pageable pageable);

    /**
     * 根据ID查询供应商商品详情
     * @param id 供应商商品ID
     * @return 供应商商品详情
     */
    PurSupplierGoodsDto getSupplierGoodsById(Long id);

    /**
     * 创建供应商商品关联
     * @param dto 供应商商品信息
     * @return 创建的供应商商品
     */
    PurSupplierGoodsDto createSupplierGoods(PurSupplierGoodsDto dto);

    /**
     * 更新供应商商品关联
     * @param id 供应商商品ID
     * @param dto 供应商商品信息
     * @return 更新的供应商商品
     */
    PurSupplierGoodsDto updateSupplierGoods(Long id, PurSupplierGoodsDto dto);

    /**
     * 删除供应商商品关联
     * @param id 供应商商品ID
     */
    void deleteSupplierGoods(Long id);

    /**
     * 根据供应商ID查询商品列表
     * @param supplierId 供应商ID
     * @return 商品列表
     */
    List<PurSupplierGoodsDto> getGoodsBySupplierId(Long supplierId);

    /**
     * 根据商品ID查询供应商列表
     * @param goodsId 商品ID
     * @return 供应商列表
     */
    List<PurSupplierGoodsDto> getSuppliersByGoodsId(Long goodsId);

    /**
     * 价格比较
     * @param goodsId 商品ID
     * @return 价格比较结果
     */
    SupplierPriceCompareDto comparePrice(Long goodsId);

    /**
     * 批量创建供应商商品关联
     * @param dtoList 供应商商品信息列表
     * @return 创建结果
     */
    List<PurSupplierGoodsDto> batchCreateSupplierGoods(List<PurSupplierGoodsDto> dtoList);

    /**
     * 验证供应商商品关联是否已存在
     * @param supplierId 供应商ID
     * @param goodsId 商品ID
     * @param skuId SKU ID
     * @return 是否存在
     */
    boolean checkSupplierGoodsExists(Long supplierId, Long goodsId, Long skuId);

    /**
     * 获取供应商的商品数量
     * @param supplierId 供应商ID
     * @return 商品数量
     */
    long getGoodsCountBySupplierId(Long supplierId);

    /**
     * 获取商品的供应商数量
     * @param goodsId 商品ID
     * @return 供应商数量
     */
    long getSupplierCountByGoodsId(Long goodsId);

    /**
     * 获取推荐的供应商（基于价格和条件）
     * @param goodsId 商品ID
     * @param quantity 采购数量
     * @return 推荐的供应商列表
     */
    List<PurSupplierGoodsDto> getRecommendedSuppliers(Long goodsId, Integer quantity);

    /**
     * 搜索供应商商品
     * @param goodsName 商品名称
     * @param supplierId 供应商ID（可选）
     * @return 供应商商品列表
     */
    List<PurSupplierGoodsDto> searchSupplierGoods(String goodsName, Long supplierId);

    /**
     * 导出供应商商品信息到Excel
     * @param supplierId 供应商ID
     * @param goodsId 商品ID
     * @param goodsName 商品名称
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    void exportSupplierGoods(Long supplierId, Long goodsId, String goodsName, HttpServletResponse response) throws IOException;

    /**
     * 导入供应商商品信息
     * @param file Excel文件
     * @return 导入结果
     * @throws IOException IO异常
     */
    Map<String, Object> importSupplierGoods(MultipartFile file) throws IOException;

    /**
     * 获取导入模板
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    void downloadImportTemplate(HttpServletResponse response) throws IOException;
} 