package com.example.vliascrm.repository;

import com.example.vliascrm.entity.InvLocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 库位Repository接口
 */
@Repository
public interface InvLocationRepository extends JpaRepository<InvLocation, Long> {

    /**
     * 根据ID查询未删除的库位
     */
    Optional<InvLocation> findByIdAndIsDeleted(Long id, Byte isDeleted);

    /**
     * 根据仓库ID查询库位列表
     */
    List<InvLocation> findByWarehouseIdAndIsDeletedOrderBySort(Long warehouseId, Byte isDeleted);

    /**
     * 根据库区ID查询库位列表
     */
    List<InvLocation> findByAreaIdAndIsDeletedOrderBySort(Long areaId, Byte isDeleted);

    /**
     * 根据仓库ID和库位编码查询
     */
    Optional<InvLocation> findByWarehouseIdAndLocationCodeAndIsDeleted(Long warehouseId, String locationCode, Byte isDeleted);

    /**
     * 根据库位名称模糊查询
     */
    List<InvLocation> findByLocationNameContainingAndIsDeletedOrderBySort(String locationName, Byte isDeleted);

    /**
     * 根据状态查询库位列表
     */
    List<InvLocation> findByStatusAndIsDeletedOrderBySort(Byte status, Byte isDeleted);

    /**
     * 分页查询库位
     */
    @Query("SELECT l FROM InvLocation l WHERE l.isDeleted = :isDeleted " +
           "AND (:warehouseId IS NULL OR l.warehouseId = :warehouseId) " +
           "AND (:areaId IS NULL OR l.areaId = :areaId) " +
           "AND (:locationName IS NULL OR l.locationName LIKE %:locationName%) " +
           "AND (:status IS NULL OR l.status = :status) " +
           "ORDER BY l.sort, l.id")
    Page<InvLocation> findLocationsByConditions(
            @Param("warehouseId") Long warehouseId,
            @Param("areaId") Long areaId,
            @Param("locationName") String locationName,
            @Param("status") Byte status,
            @Param("isDeleted") Byte isDeleted,
            Pageable pageable
    );

    /**
     * 统计仓库下的库位数量
     */
    long countByWarehouseIdAndIsDeleted(Long warehouseId, Byte isDeleted);

    /**
     * 统计库区下的库位数量
     */
    long countByAreaIdAndIsDeleted(Long areaId, Byte isDeleted);

    /**
     * 检查库位编码在仓库内是否已存在
     */
    boolean existsByWarehouseIdAndLocationCodeAndIsDeletedAndIdNot(Long warehouseId, String locationCode, Byte isDeleted, Long id);

    /**
     * 检查库位编码在仓库内是否已存在（新增时）
     */
    boolean existsByWarehouseIdAndLocationCodeAndIsDeleted(Long warehouseId, String locationCode, Byte isDeleted);
} 
