package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.InvWarehouseDTO;
import com.example.vliascrm.entity.InvWarehouse;
import com.example.vliascrm.repository.InvWarehouseRepository;
import com.example.vliascrm.service.InvWarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 仓库Service实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InvWarehouseServiceImpl implements InvWarehouseService {

    private final InvWarehouseRepository warehouseRepository;

    @Override
    public InvWarehouseDTO create(InvWarehouseDTO warehouseDTO) {
        log.info("创建仓库：{}", warehouseDTO.getWarehouseName());
        
        // 检查仓库编码是否已存在
        if (StringUtils.hasText(warehouseDTO.getWarehouseCode()) && 
            checkWarehouseCodeExists(warehouseDTO.getWarehouseCode(), null)) {
            throw new RuntimeException("仓库编码已存在：" + warehouseDTO.getWarehouseCode());
        }
        
        InvWarehouse warehouse = new InvWarehouse();
        BeanUtils.copyProperties(warehouseDTO, warehouse);
        warehouse.setId(null); // 确保是新增
        warehouse.setCreateTime(LocalDateTime.now());
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouse.setIsDeleted(0);
        
        // 如果是第一个仓库，自动设为默认仓库
        if (warehouseRepository.count() == 0) {
            warehouse.setIsDefault(1);
        }
        
        InvWarehouse saved = warehouseRepository.save(warehouse);
        return convertToDTO(saved);
    }

    @Override
    public InvWarehouseDTO update(Long id, InvWarehouseDTO warehouseDTO) {
        log.info("更新仓库，ID：{}，名称：{}", id, warehouseDTO.getWarehouseName());
        
        InvWarehouse warehouse = findEntityById(id);
        
        // 检查仓库编码是否已存在（排除当前仓库）
        if (StringUtils.hasText(warehouseDTO.getWarehouseCode()) && 
            checkWarehouseCodeExists(warehouseDTO.getWarehouseCode(), id)) {
            throw new RuntimeException("仓库编码已存在：" + warehouseDTO.getWarehouseCode());
        }
        
        BeanUtils.copyProperties(warehouseDTO, warehouse, "id", "createTime", "createBy", "isDeleted");
        warehouse.setUpdateTime(LocalDateTime.now());
        
        InvWarehouse saved = warehouseRepository.save(warehouse);
        return convertToDTO(saved);
    }

    @Override
    public void delete(Long id) {
        log.info("删除仓库，ID：{}", id);
        InvWarehouse warehouse = findEntityById(id);
        
        // 检查是否为默认仓库
        if (warehouse.getIsDefault() != null && warehouse.getIsDefault() == 1) {
            throw new RuntimeException("默认仓库不能删除");
        }
        
        warehouse.setIsDeleted(1);
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouseRepository.save(warehouse);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        log.info("批量删除仓库，IDs：{}", ids);
        for (Long id : ids) {
            delete(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public InvWarehouseDTO findById(Long id) {
        InvWarehouse warehouse = findEntityById(id);
        return convertToDTO(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvWarehouseDTO> findPage(String warehouseName, String warehouseCode, Integer status, Pageable pageable) {
        Page<InvWarehouse> warehousePage = warehouseRepository.findWarehousesWithSearch(
                warehouseName, warehouseCode, status, pageable);
        return warehousePage.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvWarehouseDTO> findAll() {
        List<InvWarehouse> warehouses = warehouseRepository.findByIsDeletedOrderBySortAsc(0);
        return warehouses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvWarehouseDTO> findAllActive() {
        List<InvWarehouse> warehouses = warehouseRepository.findByStatusAndIsDeletedOrderBySortAsc(1, 0);
        return warehouses.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void enable(Long id) {
        log.info("启用仓库，ID：{}", id);
        updateStatus(id, 1);
    }

    @Override
    public void disable(Long id) {
        log.info("禁用仓库，ID：{}", id);
        InvWarehouse warehouse = findEntityById(id);
        
        // 检查是否为默认仓库
        if (warehouse.getIsDefault() != null && warehouse.getIsDefault() == 1) {
            throw new RuntimeException("默认仓库不能禁用");
        }
        
        updateStatus(id, 0);
    }

    @Override
    public void batchEnable(List<Long> ids) {
        log.info("批量启用仓库，IDs：{}", ids);
        batchUpdateStatus(ids, 1);
    }

    @Override
    public void batchDisable(List<Long> ids) {
        log.info("批量禁用仓库，IDs：{}", ids);
        for (Long id : ids) {
            disable(id); // 使用单个禁用方法，确保默认仓库检查
        }
    }

    @Override
    public void setDefault(Long id) {
        log.info("设置默认仓库，ID：{}", id);
        
        // 先取消所有仓库的默认状态
        List<InvWarehouse> defaultWarehouses = warehouseRepository.findByIsDefaultAndIsDeleted(1, 0)
                .stream().collect(Collectors.toList());
        for (InvWarehouse warehouse : defaultWarehouses) {
            warehouse.setIsDefault(0);
            warehouse.setUpdateTime(LocalDateTime.now());
        }
        warehouseRepository.saveAll(defaultWarehouses);
        
        // 设置新的默认仓库
        InvWarehouse warehouse = findEntityById(id);
        warehouse.setIsDefault(1);
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouseRepository.save(warehouse);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkWarehouseCodeExists(String warehouseCode, Long excludeId) {
        if (!StringUtils.hasText(warehouseCode)) {
            return false;
        }
        
        return warehouseRepository.existsByWarehouseCodeAndNotId(warehouseCode, excludeId);
    }

    @Override
    @Transactional(readOnly = true)
    public InvWarehouseDTO findDefaultWarehouse() {
        return warehouseRepository.findByIsDefaultAndIsDeleted(1, 0)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvWarehouseDTO> exportWarehouses(String warehouseName, String warehouseCode, Integer status) {
        Page<InvWarehouse> warehousePage = warehouseRepository.findWarehousesWithSearch(
                warehouseName, warehouseCode, status, Pageable.unpaged());
        return warehousePage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查找实体（内部方法）
     */
    private InvWarehouse findEntityById(Long id) {
        return warehouseRepository.findById(id)
                .filter(warehouse -> warehouse.getIsDeleted() == 0)
                .orElseThrow(() -> new RuntimeException("仓库不存在或已删除，ID：" + id));
    }

    /**
     * 更新状态（内部方法）
     */
    private void updateStatus(Long id, Integer status) {
        InvWarehouse warehouse = findEntityById(id);
        warehouse.setStatus(status);
        warehouse.setUpdateTime(LocalDateTime.now());
        warehouseRepository.save(warehouse);
    }

    /**
     * 批量更新状态（内部方法）
     */
    private void batchUpdateStatus(List<Long> ids, Integer status) {
        List<InvWarehouse> warehouses = ids.stream()
                .map(this::findEntityById)
                .collect(Collectors.toList());
        
        for (InvWarehouse warehouse : warehouses) {
            warehouse.setStatus(status);
            warehouse.setUpdateTime(LocalDateTime.now());
        }
        
        warehouseRepository.saveAll(warehouses);
    }

    /**
     * 实体转DTO（内部方法）
     */
    private InvWarehouseDTO convertToDTO(InvWarehouse warehouse) {
        InvWarehouseDTO dto = new InvWarehouseDTO();
        BeanUtils.copyProperties(warehouse, dto);
        return dto;
    }
} 