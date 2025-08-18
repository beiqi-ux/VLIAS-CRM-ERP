package com.example.vliascrm.service;

import com.example.vliascrm.dto.InvWarehouseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 仓库Service接口
 */
public interface InvWarehouseService {

    /**
     * 创建仓库
     */
    InvWarehouseDTO create(InvWarehouseDTO warehouseDTO);

    /**
     * 根据ID更新仓库
     */
    InvWarehouseDTO update(Long id, InvWarehouseDTO warehouseDTO);

    /**
     * 根据ID删除仓库（逻辑删除）
     */
    void delete(Long id);

    /**
     * 批量删除仓库（逻辑删除）
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据ID查询仓库详情
     */
    InvWarehouseDTO findById(Long id);

    /**
     * 分页查询仓库
     */
    Page<InvWarehouseDTO> findPage(String warehouseName, String warehouseCode, Integer status, Pageable pageable);

    /**
     * 查询所有未删除的仓库
     */
    List<InvWarehouseDTO> findAll();

    /**
     * 查询所有启用状态的仓库
     */
    List<InvWarehouseDTO> findAllActive();

    /**
     * 启用仓库
     */
    void enable(Long id);

    /**
     * 禁用仓库
     */
    void disable(Long id);

    /**
     * 批量启用仓库
     */
    void batchEnable(List<Long> ids);

    /**
     * 批量禁用仓库
     */
    void batchDisable(List<Long> ids);

    /**
     * 设置默认仓库
     */
    void setDefault(Long id);

    /**
     * 检查仓库编码是否已存在
     */
    boolean checkWarehouseCodeExists(String warehouseCode, Long excludeId);

    /**
     * 查询默认仓库
     */
    InvWarehouseDTO findDefaultWarehouse();

    /**
     * 导出仓库数据
     */
    List<InvWarehouseDTO> exportWarehouses(String warehouseName, String warehouseCode, Integer status);
} 

