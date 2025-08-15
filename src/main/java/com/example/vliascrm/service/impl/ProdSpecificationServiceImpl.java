package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.SpecificationDTO;
import com.example.vliascrm.dto.SpecificationValueDTO;
import com.example.vliascrm.entity.ProdSpecification;
import com.example.vliascrm.repository.ProdSpecificationRepository;
import com.example.vliascrm.service.ProdSpecificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品规格服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProdSpecificationServiceImpl implements ProdSpecificationService {
    
    private final ProdSpecificationRepository specificationRepository;
    private final ObjectMapper objectMapper;
    
    @Override
    public Page<SpecificationDTO> findSpecificationsWithConditions(String specificationName, Integer status, Pageable pageable) {
        Page<ProdSpecification> specificationPage = specificationRepository.findSpecificationsWithConditions(specificationName, status, pageable);
        return specificationPage.map(this::convertToDTO);
    }
    
    @Override
    public SpecificationDTO getSpecificationById(Long id) {
        ProdSpecification specification = specificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("规格不存在"));
        if (Boolean.TRUE.equals(specification.getIsDeleted())) {
            throw new RuntimeException("规格已被删除");
        }
        return convertToDTO(specification);
    }
    
    @Override
    @Transactional
    public SpecificationDTO createSpecification(SpecificationDTO specificationDTO) {
        // 检查规格编码是否已存在
        if (existsBySpecificationCode(specificationDTO.getSpecificationCode(), null)) {
            throw new RuntimeException("规格编码已存在");
        }
        
        ProdSpecification specification = new ProdSpecification();
        BeanUtils.copyProperties(specificationDTO, specification, "specificationCategory", "specificationValues");
        
        // 确保specType有默认值
        if (specification.getSpecType() == null) {
            specification.setSpecType(1); // 默认规格类型为1
        }
        
        // 将规格值列表转换为JSON存储（过滤null值）
        if (specificationDTO.getSpecificationValues() != null && !specificationDTO.getSpecificationValues().isEmpty()) {
            try {
                // 过滤并清理规格值数据
                List<SpecificationValueDTO> cleanedValues = filterAndCleanSpecificationValues(specificationDTO.getSpecificationValues());
                String specValuesJson = objectMapper.writeValueAsString(cleanedValues);
                specification.setSpecValues(specValuesJson);
            } catch (JsonProcessingException e) {
                log.error("转换规格值列表为JSON失败", e);
                throw new RuntimeException("保存规格值失败");
            }
        }
        
        specification.setCreateTime(LocalDateTime.now());
        specification.setUpdateTime(LocalDateTime.now());
        specification.setIsDeleted(false);
        
        ProdSpecification savedSpecification = specificationRepository.save(specification);
        return convertToDTO(savedSpecification);
    }
    
    @Override
    @Transactional
    public SpecificationDTO updateSpecification(Long id, SpecificationDTO specificationDTO) {
        ProdSpecification existingSpecification = specificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("规格不存在"));
        
        if (Boolean.TRUE.equals(existingSpecification.getIsDeleted())) {
            throw new RuntimeException("规格已被删除");
        }
        
        // 检查规格编码是否已存在（排除当前ID）
        if (existsBySpecificationCode(specificationDTO.getSpecificationCode(), id)) {
            throw new RuntimeException("规格编码已存在");
        }
        
        BeanUtils.copyProperties(specificationDTO, existingSpecification, "id", "createTime", "createBy", "specificationCategory", "specificationValues");
        
        // 确保specType有默认值
        if (existingSpecification.getSpecType() == null) {
            existingSpecification.setSpecType(1); // 默认规格类型为1
        }
        
        // 将规格值列表转换为JSON存储（过滤null值）
        if (specificationDTO.getSpecificationValues() != null && !specificationDTO.getSpecificationValues().isEmpty()) {
            try {
                log.info("=== 更新规格时的数据检查 ===");
                log.info("categoryId: {}", specificationDTO.getCategoryId());
                log.info("前端发送的specificationValues: {}", objectMapper.writeValueAsString(specificationDTO.getSpecificationValues()));
                
                // 过滤并清理规格值数据
                List<SpecificationValueDTO> cleanedValues = filterAndCleanSpecificationValues(specificationDTO.getSpecificationValues());
                log.info("过滤后的specificationValues: {}", objectMapper.writeValueAsString(cleanedValues));
                String specValuesJson = objectMapper.writeValueAsString(cleanedValues);
                existingSpecification.setSpecValues(specValuesJson);
            } catch (JsonProcessingException e) {
                log.error("转换规格值列表为JSON失败", e);
                throw new RuntimeException("保存规格值失败");
            }
        } else {
            // 如果规格值列表为空，清空JSON字段
            existingSpecification.setSpecValues(null);
        }
        
        existingSpecification.setUpdateTime(LocalDateTime.now());
        
        ProdSpecification savedSpecification = specificationRepository.save(existingSpecification);
        return convertToDTO(savedSpecification);
    }
    
    @Override
    @Transactional
    public void deleteSpecification(Long id) {
        ProdSpecification specification = specificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("规格不存在"));
        
        specification.setIsDeleted(true);
        specification.setUpdateTime(LocalDateTime.now());
        specificationRepository.save(specification);
    }
    
    @Override
    @Transactional
    public void batchDeleteSpecifications(List<Long> ids) {
        List<ProdSpecification> specifications = specificationRepository.findAllById(ids);
        LocalDateTime now = LocalDateTime.now();
        
        specifications.forEach(specification -> {
            specification.setIsDeleted(true);
            specification.setUpdateTime(now);
        });
        
        specificationRepository.saveAll(specifications);
    }
    
    @Override
    public List<SpecificationDTO> getEnabledSpecifications() {
        List<ProdSpecification> specifications = specificationRepository.findByStatusAndIsDeletedOrderByCreateTimeDesc(1, false);
        return specifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsBySpecificationCode(String specificationCode, Long excludeId) {
        return specificationRepository.existsBySpecificationCodeExcludingId(specificationCode, excludeId);
    }
    
    @Override
    public List<SpecificationDTO> findByCategoryId(Long categoryId, String subCategory) {
        List<ProdSpecification> specifications = specificationRepository.findByCategoryIdAndIsDeletedFalseOrderBySortAsc(categoryId);
        return specifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SpecificationDTO> findByGoodsId(Long goodsId) {
        List<ProdSpecification> specifications = specificationRepository.findByGoodsIdAndIsDeletedFalseOrderBySortAsc(goodsId);
        return specifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<SpecificationDTO> findOptimalSpecifications(Long goodsId, Long categoryId) {
        List<ProdSpecification> specifications = specificationRepository.findByGoodsIdOrCategoryIdPrioritized(goodsId, categoryId);
        return specifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 实体转DTO
     */
    private SpecificationDTO convertToDTO(ProdSpecification specification) {
        SpecificationDTO dto = new SpecificationDTO();
        BeanUtils.copyProperties(specification, dto);
        
        // 将JSON格式的规格值转换为规格值列表（数据库中已存储过滤后的数据）
        if (StringUtils.hasText(specification.getSpecValues())) {
            try {
                List<SpecificationValueDTO> specificationValues = objectMapper.readValue(
                    specification.getSpecValues(), 
                    new TypeReference<List<SpecificationValueDTO>>() {}
                );
                dto.setSpecificationValues(specificationValues);
            } catch (JsonProcessingException e) {
                log.error("解析规格值JSON失败: {}", specification.getSpecValues(), e);
                dto.setSpecificationValues(new ArrayList<>());
            }
        } else {
            dto.setSpecificationValues(new ArrayList<>());
        }
        
        // 根据规格编码推断规格分类
        String specCode = specification.getSpecificationCode();
        if (specCode != null) {
            if (specCode.contains("frame") || specCode.contains("size")) {
                dto.setSpecificationCategory("frame");
            } else if (specCode.contains("lens") || specCode.contains("LENS")) {
                dto.setSpecificationCategory("lens");
            } else if (specCode.contains("sun") || specCode.contains("SUN")) {
                dto.setSpecificationCategory("sunglasses");
            } else if (specCode.contains("contact") || specCode.contains("CONTACT")) {
                dto.setSpecificationCategory("contact");
            } else {
                dto.setSpecificationCategory("frame"); // 默认为镜框
            }
        } else {
            dto.setSpecificationCategory("frame"); // 默认为镜框
        }
        
        return dto;
    }
    
    /**
     * 过滤并清理规格值数据，移除null值字段
     */
    private List<SpecificationValueDTO> filterAndCleanSpecificationValues(List<SpecificationValueDTO> specificationValues) {
        if (specificationValues == null || specificationValues.isEmpty()) {
            return new ArrayList<>();
        }
        
        return specificationValues.stream()
                .map(this::cleanSpecificationValue)
                .collect(Collectors.toList());
    }
    
    /**
     * 清理单个规格值，移除null值字段
     */
    private SpecificationValueDTO cleanSpecificationValue(SpecificationValueDTO value) {
        SpecificationValueDTO cleanValue = new SpecificationValueDTO();
        
        // 必须字段
        cleanValue.setValueCode(value.getValueCode());
        cleanValue.setSortOrder(value.getSortOrder() != null ? value.getSortOrder() : 0);
        cleanValue.setStatus(value.getStatus() != null ? value.getStatus() : 1);
        
        // 可选字段，只有非null且非空时才设置
        if (value.getFrameWidth() != null && !value.getFrameWidth().toString().trim().isEmpty() && !"null".equals(value.getFrameWidth().toString())) {
            try {
                Double frameWidthValue = Double.parseDouble(value.getFrameWidth().toString());
                cleanValue.setFrameWidth(frameWidthValue.toString());
            } catch (NumberFormatException e) {
                // 如果不是有效数字，不设置该字段
            }
        }
        if (value.getLensWidth() != null && !value.getLensWidth().toString().trim().isEmpty() && !"null".equals(value.getLensWidth().toString())) {
            try {
                Double lensWidthValue = Double.parseDouble(value.getLensWidth().toString());
                cleanValue.setLensWidth(lensWidthValue.toString());
            } catch (NumberFormatException e) {
                // 如果不是有效数字，不设置该字段
            }
        }
        if (value.getLensHeight() != null && !value.getLensHeight().toString().trim().isEmpty() && !"null".equals(value.getLensHeight().toString())) {
            try {
                Double lensHeightValue = Double.parseDouble(value.getLensHeight().toString());
                cleanValue.setLensHeight(lensHeightValue.toString());
            } catch (NumberFormatException e) {
                // 如果不是有效数字，不设置该字段
            }
        }
        if (value.getBridgeWidth() != null && !value.getBridgeWidth().toString().trim().isEmpty() && !"null".equals(value.getBridgeWidth().toString())) {
            try {
                Double bridgeWidthValue = Double.parseDouble(value.getBridgeWidth().toString());
                cleanValue.setBridgeWidth(bridgeWidthValue.toString());
            } catch (NumberFormatException e) {
                // 如果不是有效数字，不设置该字段
            }
        }
        if (value.getTempleLength() != null && !value.getTempleLength().toString().trim().isEmpty() && !"null".equals(value.getTempleLength().toString())) {
            try {
                Double templeLengthValue = Double.parseDouble(value.getTempleLength().toString());
                cleanValue.setTempleLength(templeLengthValue.toString());
            } catch (NumberFormatException e) {
                // 如果不是有效数字，不设置该字段
            }
        }
        if (value.getColorName() != null && !value.getColorName().toString().trim().isEmpty() && !"null".equals(value.getColorName().toString())) {
            cleanValue.setColorName(value.getColorName().toString().trim());
        }
        if (value.getLensColorName() != null && !value.getLensColorName().toString().trim().isEmpty() && !"null".equals(value.getLensColorName().toString())) {
            cleanValue.setLensColorName(value.getLensColorName().toString().trim());
        }
        if (value.getShapeName() != null && !value.getShapeName().toString().trim().isEmpty() && !"null".equals(value.getShapeName().toString())) {
            cleanValue.setShapeName(value.getShapeName().toString().trim());
        }
        if (value.getMaterialName() != null && !value.getMaterialName().toString().trim().isEmpty() && !"null".equals(value.getMaterialName().toString())) {
            cleanValue.setMaterialName(value.getMaterialName().toString().trim());
        }
        
        return cleanValue;
    }

} 