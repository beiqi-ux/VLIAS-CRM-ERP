package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.PurSupplierDto;
import com.example.vliascrm.entity.PurSupplier;
import com.example.vliascrm.entity.SysUser;
import com.example.vliascrm.repository.PurSupplierRepository;
import com.example.vliascrm.repository.SysUserRepository;
import com.example.vliascrm.service.PurSupplierService;
import com.example.vliascrm.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 供应商服务实现类
 */
@Service
@Transactional
public class PurSupplierServiceImpl implements PurSupplierService {

    @Autowired
    private PurSupplierRepository supplierRepository;

    @Autowired
    private SysUserRepository userRepository;

    @Override
    public Page<PurSupplierDto> getSupplierPage(String supplierName, String contact, Integer status, Integer supplierType, Pageable pageable) {
        Page<PurSupplier> supplierPage = supplierRepository.findSuppliersWithConditions(
                supplierName, contact, status, supplierType, 0, pageable);
        return supplierPage.map(this::convertToDto);
    }

    @Override
    public PurSupplierDto getSupplierById(Long id) {
        Optional<PurSupplier> supplierOpt = supplierRepository.findById(id);
        if (supplierOpt.isPresent() && supplierOpt.get().getIsDeleted() == 0) {
            return convertToDto(supplierOpt.get());
        }
        throw new RuntimeException("供应商不存在");
    }

    @Override
    public PurSupplierDto createSupplier(PurSupplierDto supplierDto) {
        // 验证供应商编码是否存在
        if (StringUtils.hasText(supplierDto.getSupplierCode()) && 
            isSupplierCodeExists(supplierDto.getSupplierCode(), null)) {
            throw new RuntimeException("供应商编码已存在");
        }

        // 验证供应商名称是否存在
        if (isSupplierNameExists(supplierDto.getSupplierName(), null)) {
            throw new RuntimeException("供应商名称已存在");
        }

        PurSupplier supplier = new PurSupplier();
        BeanUtils.copyProperties(supplierDto, supplier);
        
        // 设置创建人信息
        Long currentUserId = SecurityUtils.getCurrentUserId();
        supplier.setCreateBy(currentUserId);
        supplier.setUpdateBy(currentUserId);

        // 如果没有提供编码，自动生成
        if (!StringUtils.hasText(supplier.getSupplierCode())) {
            supplier.setSupplierCode(generateSupplierCode());
        }

        PurSupplier savedSupplier = supplierRepository.save(supplier);
        return convertToDto(savedSupplier);
    }

    @Override
    public PurSupplierDto updateSupplier(Long id, PurSupplierDto supplierDto) {
        PurSupplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("供应商不存在"));

        if (existingSupplier.getIsDeleted() == 1) {
            throw new RuntimeException("供应商已删除");
        }

        // 验证供应商编码是否存在（排除当前记录）
        if (StringUtils.hasText(supplierDto.getSupplierCode()) && 
            isSupplierCodeExists(supplierDto.getSupplierCode(), id)) {
            throw new RuntimeException("供应商编码已存在");
        }

        // 验证供应商名称是否存在（排除当前记录）
        if (isSupplierNameExists(supplierDto.getSupplierName(), id)) {
            throw new RuntimeException("供应商名称已存在");
        }

        // 复制属性，排除ID和创建信息
        BeanUtils.copyProperties(supplierDto, existingSupplier, 
                "id", "createTime", "createBy");
        
        // 设置更新人信息
        Long currentUserId = SecurityUtils.getCurrentUserId();
        existingSupplier.setUpdateBy(currentUserId);

        PurSupplier savedSupplier = supplierRepository.save(existingSupplier);
        return convertToDto(savedSupplier);
    }

    @Override
    public void deleteSupplier(Long id) {
        PurSupplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("供应商不存在"));

        // 软删除
        supplier.setIsDeleted(1);
        supplier.setUpdateBy(SecurityUtils.getCurrentUserId());
        supplierRepository.save(supplier);
    }

    @Override
    public void deleteSuppliers(List<Long> ids) {
        List<PurSupplier> suppliers = supplierRepository.findAllById(ids);
        Long currentUserId = SecurityUtils.getCurrentUserId();
        
        suppliers.forEach(supplier -> {
            supplier.setIsDeleted(1);
            supplier.setUpdateBy(currentUserId);
        });
        
        supplierRepository.saveAll(suppliers);
    }

    @Override
    public void updateSupplierStatus(Long id, Integer status) {
        PurSupplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("供应商不存在"));

        if (supplier.getIsDeleted() == 1) {
            throw new RuntimeException("供应商已删除");
        }

        supplier.setStatus(status);
        supplier.setUpdateBy(SecurityUtils.getCurrentUserId());
        supplierRepository.save(supplier);
    }

    @Override
    public List<PurSupplierDto> getAllActiveSuppliers() {
        List<PurSupplier> suppliers = supplierRepository.findAllActiveSuppliers();
        return suppliers.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public PurSupplierDto getSupplierByCode(String supplierCode) {
        Optional<PurSupplier> supplierOpt = supplierRepository.findBySupplierCodeAndIsDeleted(supplierCode, 0);
        if (supplierOpt.isPresent()) {
            return convertToDto(supplierOpt.get());
        }
        throw new RuntimeException("供应商不存在");
    }

    @Override
    public boolean isSupplierCodeExists(String supplierCode, Long excludeId) {
        Optional<PurSupplier> supplierOpt = supplierRepository.findBySupplierCodeAndIsDeleted(supplierCode, 0);
        if (supplierOpt.isPresent()) {
            return excludeId == null || !supplierOpt.get().getId().equals(excludeId);
        }
        return false;
    }

    @Override
    public boolean isSupplierNameExists(String supplierName, Long excludeId) {
        Optional<PurSupplier> supplierOpt = supplierRepository.findBySupplierNameAndIsDeleted(supplierName, 0);
        if (supplierOpt.isPresent()) {
            return excludeId == null || !supplierOpt.get().getId().equals(excludeId);
        }
        return false;
    }

    @Override
    public PurSupplierService.SupplierStatistics getSupplierStatistics() {
        PurSupplierService.SupplierStatistics statistics = new PurSupplierService.SupplierStatistics();
        statistics.setTotalCount(supplierRepository.countSuppliers(null, 0));
        statistics.setActiveCount(supplierRepository.countSuppliers(1, 0));
        statistics.setInactiveCount(supplierRepository.countSuppliers(0, 0));
        return statistics;
    }

    /**
     * 将实体转换为DTO
     */
    private PurSupplierDto convertToDto(PurSupplier supplier) {
        PurSupplierDto dto = new PurSupplierDto();
        BeanUtils.copyProperties(supplier, dto);

        // 设置显示文本
        dto.setStatusText(supplier.getStatus() == 1 ? "正常" : "禁用");
        
        if (supplier.getLevel() != null) {
            dto.setLevelText(getLevelText(supplier.getLevel()));
        }
        
        if (supplier.getCreditLevel() != null) {
            dto.setCreditLevelText(getCreditLevelText(supplier.getCreditLevel()));
        }
        
        if (supplier.getSettlementType() != null) {
            dto.setSettlementTypeText(getSettlementTypeText(supplier.getSettlementType()));
        }
        
        if (supplier.getSupplierType() != null) {
            dto.setSupplierTypeText(getSupplierTypeText(supplier.getSupplierType()));
        }

        // 设置创建人和更新人姓名
        if (supplier.getCreateBy() != null) {
            Optional<SysUser> createUser = userRepository.findById(supplier.getCreateBy());
            createUser.ifPresent(user -> dto.setCreateByName(user.getRealName()));
        }
        
        if (supplier.getUpdateBy() != null) {
            Optional<SysUser> updateUser = userRepository.findById(supplier.getUpdateBy());
            updateUser.ifPresent(user -> dto.setUpdateByName(user.getRealName()));
        }

        return dto;
    }

    /**
     * 获取供应商等级文本
     */
    private String getLevelText(Integer level) {
        switch (level) {
            case 1: return "一级供应商";
            case 2: return "二级供应商";
            case 3: return "三级供应商";
            default: return "未分级";
        }
    }

    /**
     * 获取信用等级文本
     */
    private String getCreditLevelText(Integer creditLevel) {
        switch (creditLevel) {
            case 1: return "优秀";
            case 2: return "良好";
            case 3: return "一般";
            case 4: return "较差";
            default: return "未评级";
        }
    }

    /**
     * 获取结算方式文本
     */
    private String getSettlementTypeText(Integer settlementType) {
        switch (settlementType) {
            case 1: return "现结";
            case 2: return "月结";
            case 3: return "季结";
            default: return "未设置";
        }
    }

    /**
     * 获取供应商类型文本
     */
    private String getSupplierTypeText(Integer supplierType) {
        switch (supplierType) {
            case 1: return "镜框供应商";
            case 2: return "镜片供应商";
            case 3: return "配件供应商";
            case 4: return "设备供应商";
            case 5: return "其他";
            default: return "未分类";
        }
    }

    /**
     * 生成供应商编码
     */
    private String generateSupplierCode() {
        // 简单的编码生成规则：SUP + 时间戳后6位
        long timestamp = System.currentTimeMillis();
        return "SUP" + String.valueOf(timestamp).substring(7);
    }
} 