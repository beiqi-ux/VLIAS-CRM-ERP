package com.example.vliascrm.repository;

import com.example.vliascrm.entity.InvWarehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 仓库Repository接口
 */
@Repository
public interface InvWarehouseRepository extends JpaRepository<InvWarehouse, Long> {

    /**
     * 根据仓库编码查找仓库
     */
    Optional<InvWarehouse> findByWarehouseCodeAndIsDeleted(String warehouseCode, Integer isDeleted);

    /**
     * 查找所有未删除的仓库
     */
    List<InvWarehouse> findByIsDeletedOrderBySortAsc(Integer isDeleted);

    /**
     * 查找启用状态的仓库
     */
    List<InvWarehouse> findByStatusAndIsDeletedOrderBySortAsc(Integer status, Integer isDeleted);

    /**
     * 查找默认仓库
     */
    Optional<InvWarehouse> findByIsDefaultAndIsDeleted(Integer isDefault, Integer isDeleted);

    /**
     * 分页查询仓库（支持模糊搜索）
     */
    @Query("SELECT w FROM InvWarehouse w WHERE w.isDeleted = 0 " +
           "AND (:warehouseName IS NULL OR w.warehouseName LIKE %:warehouseName%) " +
           "AND (:warehouseCode IS NULL OR w.warehouseCode LIKE %:warehouseCode%) " +
           "AND (:status IS NULL OR w.status = :status)")
    Page<InvWarehouse> findWarehousesWithSearch(
            @Param("warehouseName") String warehouseName,
            @Param("warehouseCode") String warehouseCode,
            @Param("status") Integer status,
            Pageable pageable);

    /**
     * 检查仓库编码是否存在（排除指定ID）
     */
    @Query("SELECT COUNT(w) > 0 FROM InvWarehouse w WHERE w.warehouseCode = :warehouseCode " +
           "AND w.isDeleted = 0 AND (:excludeId IS NULL OR w.id != :excludeId)")
    boolean existsByWarehouseCodeAndNotId(@Param("warehouseCode") String warehouseCode, 
                                         @Param("excludeId") Long excludeId);
} 