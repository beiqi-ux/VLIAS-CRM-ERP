package com.example.vliascrm.repository;

import com.example.vliascrm.entity.PurSupplier;
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
 * 供应商数据访问接口
 */
@Repository
public interface PurSupplierRepository extends JpaRepository<PurSupplier, Long>, JpaSpecificationExecutor<PurSupplier> {

    /**
     * 根据供应商编码查找供应商
     * @param supplierCode 供应商编码
     * @param isDeleted 是否删除
     * @return 供应商信息
     */
    Optional<PurSupplier> findBySupplierCodeAndIsDeleted(String supplierCode, Integer isDeleted);

    /**
     * 根据供应商名称查找供应商
     * @param supplierName 供应商名称
     * @param isDeleted 是否删除
     * @return 供应商信息
     */
    Optional<PurSupplier> findBySupplierNameAndIsDeleted(String supplierName, Integer isDeleted);

    /**
     * 检查供应商编码是否存在
     * @param supplierCode 供应商编码
     * @param isDeleted 是否删除
     * @return 是否存在
     */
    boolean existsBySupplierCodeAndIsDeleted(String supplierCode, Integer isDeleted);

    /**
     * 检查供应商名称是否存在
     * @param supplierName 供应商名称
     * @param isDeleted 是否删除
     * @return 是否存在
     */
    boolean existsBySupplierNameAndIsDeleted(String supplierName, Integer isDeleted);

    /**
     * 分页查询供应商列表
     * @param supplierName 供应商名称（模糊查询）
     * @param contact 联系人（模糊查询）
     * @param status 状态
     * @param isDeleted 是否删除
     * @param pageable 分页参数
     * @return 供应商分页列表
     */
    @Query("SELECT s FROM PurSupplier s WHERE " +
            "(:supplierName IS NULL OR s.supplierName LIKE %:supplierName%) AND " +
            "(:contact IS NULL OR s.contact LIKE %:contact%) AND " +
            "(:status IS NULL OR s.status = :status) AND " +
            "(:supplierType IS NULL OR s.supplierType = :supplierType) AND " +
            "s.isDeleted = :isDeleted")
    Page<PurSupplier> findSuppliersWithConditions(
            @Param("supplierName") String supplierName,
            @Param("contact") String contact,
            @Param("status") Integer status,
            @Param("supplierType") Integer supplierType,
            @Param("isDeleted") Integer isDeleted,
            Pageable pageable);

    /**
     * 查询所有正常状态的供应商
     * @return 供应商列表
     */
    @Query("SELECT s FROM PurSupplier s WHERE s.status = 1 AND s.isDeleted = 0 ORDER BY s.supplierName")
    List<PurSupplier> findAllActiveSuppliers();

    /**
     * 根据供应商等级查询供应商
     * @param level 供应商等级
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 供应商列表
     */
    List<PurSupplier> findByLevelAndStatusAndIsDeleted(Integer level, Integer status, Integer isDeleted);

    /**
     * 根据信用等级查询供应商
     * @param creditLevel 信用等级
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 供应商列表
     */
    List<PurSupplier> findByCreditLevelAndStatusAndIsDeleted(Integer creditLevel, Integer status, Integer isDeleted);

    /**
     * 根据结算方式查询供应商
     * @param settlementType 结算方式
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 供应商列表
     */
    List<PurSupplier> findBySettlementTypeAndStatusAndIsDeleted(Integer settlementType, Integer status, Integer isDeleted);

    /**
     * 统计供应商数量
     * @param status 状态
     * @param isDeleted 是否删除
     * @return 供应商数量
     */
    @Query("SELECT COUNT(s) FROM PurSupplier s WHERE " +
            "(:status IS NULL OR s.status = :status) AND " +
            "s.isDeleted = :isDeleted")
    Long countSuppliers(@Param("status") Integer status, @Param("isDeleted") Integer isDeleted);
} 