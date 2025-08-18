package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.InvLocationDTO;
import com.example.vliascrm.entity.InvLocation;
import com.example.vliascrm.repository.InvLocationRepository;
import com.example.vliascrm.service.InvLocationService;
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
 * 库位Service实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class InvLocationServiceImpl implements InvLocationService {

    private final InvLocationRepository locationRepository;

    @Override
    public InvLocationDTO create(InvLocationDTO locationDTO) {
        log.info("创建库位：{}", locationDTO);
        
        // 数据校验
        validateLocationData(locationDTO, null);
        
        // 检查库位编码是否已存在
        if (StringUtils.hasText(locationDTO.getLocationCode()) && 
            checkLocationCodeExists(locationDTO.getWarehouseId(), locationDTO.getLocationCode(), null)) {
            throw new RuntimeException("库位编码已存在");
        }
        
        // 转换为实体
        InvLocation location = convertToEntity(locationDTO);
        location.setCreateTime(LocalDateTime.now());
        location.setUpdateTime(LocalDateTime.now());
        location.setIsDeleted((byte) 0);
        
        // 设置默认值
        if (location.getStatus() == null) {
            location.setStatus((byte) 1);
        }
        if (location.getSort() == null) {
            location.setSort(0);
        }
        
        // 保存
        InvLocation savedLocation = locationRepository.save(location);
        
        log.info("库位创建成功，ID：{}", savedLocation.getId());
        return convertToDTO(savedLocation);
    }

    @Override
    public InvLocationDTO update(Long id, InvLocationDTO locationDTO) {
        log.info("更新库位，ID：{}，数据：{}", id, locationDTO);
        
        // 查找现有记录
        InvLocation existingLocation = findEntityById(id);
        
        // 数据校验
        validateLocationData(locationDTO, id);
        
        // 检查库位编码是否已存在（排除当前记录）
        if (StringUtils.hasText(locationDTO.getLocationCode()) && 
            checkLocationCodeExists(locationDTO.getWarehouseId(), locationDTO.getLocationCode(), id)) {
            throw new RuntimeException("库位编码已存在");
        }
        
        // 更新字段
        BeanUtils.copyProperties(locationDTO, existingLocation, "id", "createTime", "createBy", "isDeleted");
        existingLocation.setUpdateTime(LocalDateTime.now());
        
        // 保存
        InvLocation updatedLocation = locationRepository.save(existingLocation);
        
        log.info("库位更新成功，ID：{}", id);
        return convertToDTO(updatedLocation);
    }

    @Override
    public void delete(Long id) {
        log.info("删除库位，ID：{}", id);
        
        InvLocation location = findEntityById(id);
        location.setIsDeleted((byte) 1);
        location.setUpdateTime(LocalDateTime.now());
        
        locationRepository.save(location);
        log.info("库位删除成功，ID：{}", id);
    }

    @Override
    public void batchDelete(List<Long> ids) {
        log.info("批量删除库位，IDs：{}", ids);
        
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("删除的ID列表不能为空");
        }
        
        for (Long id : ids) {
            delete(id);
        }
        
        log.info("批量删除库位成功，数量：{}", ids.size());
    }

    @Override
    @Transactional(readOnly = true)
    public InvLocationDTO findById(Long id) {
        InvLocation location = findEntityById(id);
        return convertToDTO(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvLocationDTO> findByWarehouseId(Long warehouseId) {
        List<InvLocation> locations = locationRepository.findByWarehouseIdAndIsDeletedOrderBySort(warehouseId, (byte) 0);
        return locations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvLocationDTO> findByAreaId(Long areaId) {
        List<InvLocation> locations = locationRepository.findByAreaIdAndIsDeletedOrderBySort(areaId, (byte) 0);
        return locations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<InvLocationDTO> findPage(Long warehouseId, Long areaId, String locationName, 
                                         Byte status, Pageable pageable) {
        Page<InvLocation> locationPage = locationRepository.findLocationsByConditions(
                warehouseId, areaId, locationName, status, (byte) 0, pageable);
        
        return locationPage.map(this::convertToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvLocationDTO> findAllActive() {
        List<InvLocation> locations = locationRepository.findByStatusAndIsDeletedOrderBySort((byte) 1, (byte) 0);
        return locations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void enable(Long id) {
        log.info("启用库位，ID：{}", id);
        updateStatus(id, (byte) 1);
    }

    @Override
    public void disable(Long id) {
        log.info("禁用库位，ID：{}", id);
        updateStatus(id, (byte) 0);
    }

    @Override
    public void batchEnable(List<Long> ids) {
        log.info("批量启用库位，IDs：{}", ids);
        batchUpdateStatus(ids, (byte) 1);
    }

    @Override
    public void batchDisable(List<Long> ids) {
        log.info("批量禁用库位，IDs：{}", ids);
        batchUpdateStatus(ids, (byte) 0);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkLocationCodeExists(Long warehouseId, String locationCode, Long excludeId) {
        if (!StringUtils.hasText(locationCode)) {
            return false;
        }
        
        if (excludeId != null) {
            return locationRepository.existsByWarehouseIdAndLocationCodeAndIsDeletedAndIdNot(
                    warehouseId, locationCode, (byte) 0, excludeId);
        } else {
            return locationRepository.existsByWarehouseIdAndLocationCodeAndIsDeleted(
                    warehouseId, locationCode, (byte) 0);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countByWarehouseId(Long warehouseId) {
        return locationRepository.countByWarehouseIdAndIsDeleted(warehouseId, (byte) 0);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByAreaId(Long areaId) {
        return locationRepository.countByAreaIdAndIsDeleted(areaId, (byte) 0);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvLocationDTO> exportLocations(Long warehouseId, Long areaId, String locationName, Byte status) {
        log.info("导出库位数据，条件：仓库ID={}，库区ID={}，库位名称={}，状态={}", warehouseId, areaId, locationName, status);
        
        Page<InvLocation> locationPage = locationRepository.findLocationsByConditions(
                warehouseId, areaId, locationName, status, (byte) 0, Pageable.unpaged());
        
        List<InvLocationDTO> result = locationPage.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        log.info("导出库位数据完成，共{}条记录", result.size());
        return result;
    }

    /**
     * 根据ID查找实体（内部方法）
     */
    private InvLocation findEntityById(Long id) {
        return locationRepository.findByIdAndIsDeleted(id, (byte) 0)
                .orElseThrow(() -> new RuntimeException("库位不存在或已删除，ID：" + id));
    }

    /**
     * 更新状态（内部方法）
     */
    private void updateStatus(Long id, Byte status) {
        InvLocation location = findEntityById(id);
        location.setStatus(status);
        location.setUpdateTime(LocalDateTime.now());
        locationRepository.save(location);
    }

    /**
     * 批量更新状态（内部方法）
     */
    private void batchUpdateStatus(List<Long> ids, Byte status) {
        if (ids == null || ids.isEmpty()) {
            throw new RuntimeException("ID列表不能为空");
        }
        
        for (Long id : ids) {
            updateStatus(id, status);
        }
    }

    /**
     * 数据校验（内部方法）
     */
    private void validateLocationData(InvLocationDTO locationDTO, Long excludeId) {
        if (locationDTO.getWarehouseId() == null) {
            throw new RuntimeException("仓库ID不能为空");
        }
        
        if (!StringUtils.hasText(locationDTO.getLocationName())) {
            throw new RuntimeException("库位名称不能为空");
        }
        
        if (locationDTO.getLocationName().length() > 50) {
            throw new RuntimeException("库位名称长度不能超过50个字符");
        }
        
        if (StringUtils.hasText(locationDTO.getLocationCode()) && 
            locationDTO.getLocationCode().length() > 50) {
            throw new RuntimeException("库位编码长度不能超过50个字符");
        }
    }

    /**
     * 实体转DTO（内部方法）
     */
    private InvLocationDTO convertToDTO(InvLocation location) {
        InvLocationDTO dto = new InvLocationDTO();
        BeanUtils.copyProperties(location, dto);
        return dto;
    }

    /**
     * DTO转实体（内部方法）
     */
    private InvLocation convertToEntity(InvLocationDTO dto) {
        InvLocation location = new InvLocation();
        BeanUtils.copyProperties(dto, location, "id", "createTime", "updateTime", "isDeleted");
        return location;
    }
} 

