package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdBrand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品品牌数据访问接口
 */
@Repository
public interface ProdBrandRepository extends JpaRepository<ProdBrand, Long>, JpaSpecificationExecutor<ProdBrand> {

    /**
     * 根据ID查询未删除的品牌
     * @param id 品牌ID
     * @return 品牌
     */
    Optional<ProdBrand> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    /**
     * 分页查询未删除的品牌
     * @param isDeleted 是否删除
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<ProdBrand> findByIsDeleted(Boolean isDeleted, Pageable pageable);

    /**
     * 根据品牌名称查询未删除的品牌
     * @param brandName 品牌名称
     * @param isDeleted 是否删除
     * @return 品牌
     */
    Optional<ProdBrand> findByBrandNameAndIsDeleted(String brandName, Boolean isDeleted);

    /**
     * 检查品牌名称是否存在（排除已删除的）
     * @param brandName 品牌名称
     * @param isDeleted 是否删除
     * @return 是否存在
     */
    boolean existsByBrandNameAndIsDeleted(String brandName, Boolean isDeleted);

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

    /**
     * 查询所有未删除的品牌
     * @param isDeleted 是否删除
     * @return 品牌列表
     */
    List<ProdBrand> findByIsDeleted(Boolean isDeleted);
} 