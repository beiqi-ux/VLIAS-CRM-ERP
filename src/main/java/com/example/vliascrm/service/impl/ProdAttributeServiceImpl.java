package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.AttributeDTO;
import com.example.vliascrm.entity.ProdAttribute;
import com.example.vliascrm.repository.ProdAttributeRepository;
import com.example.vliascrm.service.ProdAttributeService;
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
 * 商品属性服务实现类
 */
@Service
@RequiredArgsConstructor
public class ProdAttributeServiceImpl implements ProdAttributeService {
    
    private final ProdAttributeRepository attributeRepository;
    
    @Override
    public Page<AttributeDTO> findAttributesWithConditions(String attributeName, Integer status, Pageable pageable) {
        Page<ProdAttribute> attributePage = attributeRepository.findAttributesWithConditions(attributeName, status, pageable);
        return attributePage.map(this::convertToDTO);
    }
    
    @Override
    public AttributeDTO getAttributeById(Long id) {
        ProdAttribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("属性不存在"));
        if (Boolean.TRUE.equals(attribute.getIsDeleted())) {
            throw new RuntimeException("属性已被删除");
        }
        return convertToDTO(attribute);
    }
    
    @Override
    @Transactional
    public AttributeDTO createAttribute(AttributeDTO attributeDTO) {
        // 检查属性编码是否已存在
        if (existsByAttributeCode(attributeDTO.getAttributeCode(), null)) {
            throw new RuntimeException("属性编码已存在");
        }
        
        ProdAttribute attribute = new ProdAttribute();
        BeanUtils.copyProperties(attributeDTO, attribute);
        attribute.setCreateTime(LocalDateTime.now());
        attribute.setUpdateTime(LocalDateTime.now());
        attribute.setIsDeleted(false);
        
        ProdAttribute savedAttribute = attributeRepository.save(attribute);
        return convertToDTO(savedAttribute);
    }
    
    @Override
    @Transactional
    public AttributeDTO updateAttribute(Long id, AttributeDTO attributeDTO) {
        ProdAttribute existingAttribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("属性不存在"));
        
        if (Boolean.TRUE.equals(existingAttribute.getIsDeleted())) {
            throw new RuntimeException("属性已被删除");
        }
        
        // 检查属性编码是否已存在（排除当前ID）
        if (existsByAttributeCode(attributeDTO.getAttributeCode(), id)) {
            throw new RuntimeException("属性编码已存在");
        }
        
        BeanUtils.copyProperties(attributeDTO, existingAttribute, "id", "createTime", "createBy");
        existingAttribute.setUpdateTime(LocalDateTime.now());
        
        ProdAttribute savedAttribute = attributeRepository.save(existingAttribute);
        return convertToDTO(savedAttribute);
    }
    
    @Override
    @Transactional
    public void deleteAttribute(Long id) {
        ProdAttribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("属性不存在"));
        
        attribute.setIsDeleted(true);
        attribute.setUpdateTime(LocalDateTime.now());
        attributeRepository.save(attribute);
    }
    
    @Override
    @Transactional
    public void batchDeleteAttributes(List<Long> ids) {
        List<ProdAttribute> attributes = attributeRepository.findAllById(ids);
        LocalDateTime now = LocalDateTime.now();
        
        attributes.forEach(attribute -> {
            attribute.setIsDeleted(true);
            attribute.setUpdateTime(now);
        });
        
        attributeRepository.saveAll(attributes);
    }
    
    @Override
    public List<AttributeDTO> getEnabledAttributes() {
        List<ProdAttribute> attributes = attributeRepository.findByStatusAndIsDeletedOrderByCreateTimeDesc(1, false);
        return attributes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByAttributeCode(String attributeCode, Long excludeId) {
        return attributeRepository.existsByAttributeCodeExcludingId(attributeCode, excludeId);
    }
    
    /**
     * 实体转DTO
     */
    private AttributeDTO convertToDTO(ProdAttribute attribute) {
        AttributeDTO dto = new AttributeDTO();
        BeanUtils.copyProperties(attribute, dto);
        return dto;
    }
} 