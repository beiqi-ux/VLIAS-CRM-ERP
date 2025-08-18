package com.example.vliascrm.repository;

import com.example.vliascrm.entity.InvArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库区Repository接口
 */
@Repository
public interface InvAreaRepository extends JpaRepository<InvArea, Long> {

    /**
     * 根据状态和删除标记查询库区列表
     */
    List<InvArea> findByStatusAndIsDeletedOrderBySort(Integer status, Integer isDeleted);

    /**
     * 根据仓库ID、状态和删除标记查询库区列表
     */
    List<InvArea> findByWarehouseIdAndStatusAndIsDeletedOrderBySort(Long warehouseId, Integer status, Integer isDeleted);
} 

