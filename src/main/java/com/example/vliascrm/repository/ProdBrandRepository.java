package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品品牌数据访问接口
 */
@Repository
public interface ProdBrandRepository extends JpaRepository<ProdBrand, Long>, JpaSpecificationExecutor<ProdBrand> {

    /**
     * 根据品牌名称查询
     * @param brandName 品牌名称
     * @return 品牌
     */
    Optional<ProdBrand> findByBrandName(String brandName);

    /**
     * 检查品牌名称是否存在
     * @param brandName 品牌名称
     * @return 是否存在
     */
    boolean existsByBrandName(String brandName);

    /**
     * 根据品牌名称模糊查询
     * @param brandName 品牌名称
     * @return 品牌列表
     */
    List<ProdBrand> findByBrandNameContainingAndStatusAndIsDeleted(String brandName, Integer status, Boolean isDeleted);

    /**
     * 查询正常状态的品牌，按排序排列
     * @return 品牌列表
     */
    List<ProdBrand> findByStatusAndIsDeletedOrderBySortAsc(Integer status, Boolean isDeleted);

    /**
     * 统计品牌数量
     * @param status 状态
     * @return 品牌数量
     */
    long countByStatusAndIsDeleted(Integer status, Boolean isDeleted);
} 