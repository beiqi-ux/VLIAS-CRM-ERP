package com.example.vliascrm.service;

import com.example.vliascrm.entity.ProdBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 商品品牌服务接口
 */
public interface ProdBrandService {

    /**
     * 根据ID查询品牌
     * @param id 品牌ID
     * @return 品牌对象
     */
    Optional<ProdBrand> findById(Long id);

    /**
     * 根据品牌名称查询品牌
     * @param brandName 品牌名称
     * @return 品牌对象
     */
    Optional<ProdBrand> findByBrandName(String brandName);

    /**
     * 查询所有品牌（分页）
     * @param pageable 分页参数
     * @return 品牌分页列表
     */
    Page<ProdBrand> findAll(Pageable pageable);

    /**
     * 查询所有品牌
     * @return 品牌列表
     */
    List<ProdBrand> findAll();

    /**
     * 根据品牌名称模糊查询
     * @param brandName 品牌名称
     * @return 品牌列表
     */
    List<ProdBrand> findByBrandNameContaining(String brandName);

    /**
     * 查询正常状态的品牌，按排序排列
     * @return 品牌列表
     */
    List<ProdBrand> findActivesBrands();

    /**
     * 保存品牌
     * @param brand 品牌对象
     * @return 保存后的品牌对象
     */
    ProdBrand save(ProdBrand brand);

    /**
     * 更新品牌
     * @param brand 品牌对象
     * @return 更新后的品牌对象
     */
    ProdBrand update(ProdBrand brand);

    /**
     * 删除品牌（软删除）
     * @param id 品牌ID
     */
    void deleteById(Long id);

    /**
     * 批量删除品牌（软删除）
     * @param ids 品牌ID列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 启用品牌
     * @param id 品牌ID
     */
    void enable(Long id);

    /**
     * 禁用品牌
     * @param id 品牌ID
     */
    void disable(Long id);

    /**
     * 检查品牌名称是否存在
     * @param brandName 品牌名称
     * @return 是否存在
     */
    boolean existsByBrandName(String brandName);

    /**
     * 统计品牌数量
     * @param status 状态
     * @return 品牌数量
     */
    long countByStatus(Integer status);
} 