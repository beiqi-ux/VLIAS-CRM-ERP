package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.SpecificationDTO;
import com.example.vliascrm.entity.ProdSpecification;
import com.example.vliascrm.repository.ProdSpecificationRepository;
import com.example.vliascrm.service.ProdSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品规格服务实现类
 */
@Service
@RequiredArgsConstructor
public class ProdSpecificationServiceImpl implements ProdSpecificationService {
    
    private final ProdSpecificationRepository specificationRepository;
    
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
        BeanUtils.copyProperties(specificationDTO, specification);
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
        
        BeanUtils.copyProperties(specificationDTO, existingSpecification, "id", "createTime", "createBy");
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
    
    /**
     * 实体转DTO
     */
    private SpecificationDTO convertToDTO(ProdSpecification specification) {
        SpecificationDTO dto = new SpecificationDTO();
        BeanUtils.copyProperties(specification, dto);
        return dto;
    }
} 