package com.example.vliascrm.service;

import com.example.vliascrm.dto.InvLocationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 库位Service接口
 */
public interface InvLocationService {

    /**
     * 创建库位
     */
    InvLocationDTO create(InvLocationDTO locationDTO);

    /**
     * 根据ID更新库位
     */
    InvLocationDTO update(Long id, InvLocationDTO locationDTO);

    /**
     * 根据ID删除库位（逻辑删除）
     */
    void delete(Long id);

    /**
     * 批量删除库位（逻辑删除）
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据ID查询库位详情
     */
    InvLocationDTO findById(Long id);

    /**
     * 根据仓库ID查询库位列表
     */
    List<InvLocationDTO> findByWarehouseId(Long warehouseId);

    /**
     * 根据库区ID查询库位列表
     */
    List<InvLocationDTO> findByAreaId(Long areaId);

    /**
     * 分页查询库位
     */
    Page<InvLocationDTO> findPage(Long warehouseId, Long areaId, String locationName, 
                                  Byte status, Pageable pageable);

    /**
     * 查询所有正常状态的库位
     */
    List<InvLocationDTO> findAllActive();

    /**
     * 启用库位
     */
    void enable(Long id);

    /**
     * 禁用库位
     */
    void disable(Long id);

    /**
     * 批量启用库位
     */
    void batchEnable(List<Long> ids);

    /**
     * 批量禁用库位
     */
    void batchDisable(List<Long> ids);

    /**
     * 检查库位编码是否已存在
     */
    boolean checkLocationCodeExists(Long warehouseId, String locationCode, Long excludeId);

    /**
     * 统计仓库下的库位数量
     */
    long countByWarehouseId(Long warehouseId);

    /**
     * 统计库区下的库位数量
     */
    long countByAreaId(Long areaId);

    /**
     * 导出库位数据
     */
    List<InvLocationDTO> exportLocations(Long warehouseId, Long areaId, String locationName, Byte status);
} 

