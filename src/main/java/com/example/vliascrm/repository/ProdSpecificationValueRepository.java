package com.example.vliascrm.repository;

import com.example.vliascrm.entity.ProdSpecificationValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商品规格值数据访问接口
 */
@Repository
public interface ProdSpecificationValueRepository extends JpaRepository<ProdSpecificationValue, Long>, JpaSpecificationExecutor<ProdSpecificationValue> {

    /**
     * 根据ID查询未删除的规格值
     * @param id 规格值ID
     * @return 规格值
     */
    Optional<ProdSpecificationValue> findByIdAndIsDeleted(Long id, Boolean isDeleted);

    /**
     * 分页查询未删除的规格值
     * @param isDeleted 是否删除
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<ProdSpecificationValue> findByIsDeleted(Boolean isDeleted, Pageable pageable);

    /**
     * 根据规格项ID查询规格值列表
     * @param itemId 规格项ID
     * @param isDeleted 是否删除
     * @return 规格值列表
     */
    List<ProdSpecificationValue> findByItemIdAndIsDeletedOrderBySortOrderAsc(Long itemId, Boolean isDeleted);

    /**
     * 根据规格项ID和状态查询规格值列表
     * @param itemId 规格项ID
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 规格值列表
     */
    List<ProdSpecificationValue> findByItemIdAndStatusAndIsDeletedOrderBySortOrderAsc(Long itemId, Integer status, Boolean isDeleted);

    /**
     * 根据规格项ID和规格值代码查询
     * @param itemId 规格项ID
     * @param valueCode 规格值代码
     * @param isDeleted 是否删除
     * @return 规格值
     */
    Optional<ProdSpecificationValue> findByItemIdAndValueCodeAndIsDeleted(Long itemId, String valueCode, Boolean isDeleted);

    /**
     * 检查规格值代码在规格项下是否存在
     * @param itemId 规格项ID
     * @param valueCode 规格值代码
     * @param isDeleted 是否删除
     * @return 是否存在
     */
    boolean existsByItemIdAndValueCodeAndIsDeleted(Long itemId, String valueCode, Boolean isDeleted);

    /**
     * 根据规格项ID和规格值名称查询
     * @param itemId 规格项ID
     * @param valueName 规格值名称
     * @param isDeleted 是否删除
     * @return 规格值
     */
    Optional<ProdSpecificationValue> findByItemIdAndValueNameAndIsDeleted(Long itemId, String valueName, Boolean isDeleted);

    /**
     * 检查规格值名称在规格项下是否存在
     * @param itemId 规格项ID
     * @param valueName 规格值名称
     * @param isDeleted 是否删除
     * @return 是否存在
     */
    boolean existsByItemIdAndValueNameAndIsDeleted(Long itemId, String valueName, Boolean isDeleted);

    /**
     * 根据规格值名称模糊查询
     * @param valueName 规格值名称
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 规格值列表
     */
    List<ProdSpecificationValue> findByValueNameContainingAndStatusAndIsDeleted(String valueName, Integer status, Boolean isDeleted);

    /**
     * 统计规格项下的规格值数量
     * @param itemId 规格项ID
     * @param isDeleted 是否删除
     * @return 规格值数量
     */
    long countByItemIdAndIsDeleted(Long itemId, Boolean isDeleted);

    /**
     * 根据多个ID查询规格值列表（用于商品规格关联）
     * @param ids 规格值ID列表
     * @param isDeleted 是否删除
     * @return 规格值列表
     */
    @Query("SELECT v FROM ProdSpecificationValue v WHERE v.id IN ?1 AND v.isDeleted = ?2 ORDER BY v.itemId, v.sortOrder")
    List<ProdSpecificationValue> findByIdInAndIsDeleted(List<Long> ids, Boolean isDeleted);

    /**
     * 根据规格项ID查询规格值列表（不分页）
     * @param itemId 规格项ID
     * @param isDeleted 是否删除
     * @return 规格值列表
     */
    List<ProdSpecificationValue> findByItemIdAndIsDeleted(Long itemId, Boolean isDeleted);

    /**
     * 根据规格项ID和状态查询规格值列表（不分页）
     * @param itemId 规格项ID
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 规格值列表
     */
    List<ProdSpecificationValue> findByItemIdAndStatusAndIsDeleted(Long itemId, Integer status, Boolean isDeleted);

    /**
     * 检查规格值代码在规格项下是否存在（排除指定ID）
     * @param itemId 规格项ID
     * @param valueCode 规格值代码
     * @param excludeId 排除的ID
     * @param isDeleted 是否删除
     * @return 是否存在
     */
    boolean existsByItemIdAndValueCodeAndIdNotAndIsDeleted(Long itemId, String valueCode, Long excludeId, Boolean isDeleted);

    /**
     * 检查规格值名称在规格项下是否存在（排除指定ID）
     * @param itemId 规格项ID
     * @param valueName 规格值名称
     * @param excludeId 排除的ID
     * @param isDeleted 是否删除
     * @return 是否存在
     */
    boolean existsByItemIdAndValueNameAndIdNotAndIsDeleted(Long itemId, String valueName, Long excludeId, Boolean isDeleted);

    /**
     * 根据状态查询规格值列表
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 规格值列表
     */
    List<ProdSpecificationValue> findByStatusAndIsDeleted(Integer status, Boolean isDeleted);
} 