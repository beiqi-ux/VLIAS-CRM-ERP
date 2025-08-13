package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdSpecificationCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品规格分类数据访问接口
 */
@Repository
public interface ProdSpecificationCategoryRepository extends JpaRepository<ProdSpecificationCategory, Long>, JpaSpecificationExecutor<ProdSpecificationCategory> {

    /**
     * 根据ID查询未删除的规格分类
     * @param id 分类ID
     * @return 规格分类
     */
    Optional<ProdSpecificationCategory> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    /**
     * 分页查询未删除的规格分类
     * @param isDeleted 是否删除
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<ProdSpecificationCategory> findByIsDeleted(Boolean isDeleted, Pageable pageable);



    /**
     * 根据分类名称查询
     * @param categoryName 分类名称
     * @param isDeleted 是否删除
     * @return 规格分类
     */
    Optional<ProdSpecificationCategory> findByCategoryNameAndIsDeleted(String categoryName, Boolean isDeleted);

    /**
     * 检查分类名称是否存在
     * @param categoryName 分类名称
     * @param isDeleted 是否删除
     * @return 是否存在
     */
    boolean existsByCategoryNameAndIsDeleted(String categoryName, Boolean isDeleted);

    /**
     * 查询启用状态的规格分类，按排序排列
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 规格分类列表
     */
    List<ProdSpecificationCategory> findByStatusAndIsDeletedOrderBySortAsc(Integer status, Boolean isDeleted);

    /**
     * 根据分类名称模糊查询
     * @param categoryName 分类名称
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 规格分类列表
     */
    List<ProdSpecificationCategory> findByCategoryNameContainingAndStatusAndIsDeleted(String categoryName, Integer status, Boolean isDeleted);
} 