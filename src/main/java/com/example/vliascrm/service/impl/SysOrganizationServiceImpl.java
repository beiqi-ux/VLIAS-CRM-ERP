package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.OrganizationDTO;
import com.example.vliascrm.entity.SysOrganization;
import com.example.vliascrm.exception.BusinessException;
import com.example.vliascrm.repository.SysOrganizationRepository;
import com.example.vliascrm.service.SysOrganizationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 组织机构Service实现类
 */
@Service
public class SysOrganizationServiceImpl implements SysOrganizationService {

    @Autowired
    private SysOrganizationRepository organizationRepository;

    @Override
    @Transactional
    public SysOrganization save(SysOrganization organization) {
        // 检查组织编码是否存在
        if (checkOrgCodeExists(organization.getOrgCode(), null)) {
            throw new BusinessException("组织编码已存在");
        }
        
        // 检查组织名称是否存在
        if (checkOrgNameExists(organization.getOrgName(), null)) {
            throw new BusinessException("组织名称已存在");
        }
        
        // 如果没有设置排序，默认为0
        if (organization.getSort() == null) {
            organization.setSort(0);
        }
        
        // 如果没有设置状态，默认为正常
        if (organization.getStatus() == null) {
            organization.setStatus(1);
        }
        
        return organizationRepository.save(organization);
    }

    @Override
    @Transactional
    public SysOrganization update(SysOrganization organization) {
        // 检查组织是否存在
        SysOrganization existingOrg = findById(organization.getId());
        if (existingOrg == null) {
            throw new BusinessException("组织不存在");
        }
        
        // 检查组织编码是否存在
        if (checkOrgCodeExists(organization.getOrgCode(), organization.getId())) {
            throw new BusinessException("组织编码已存在");
        }
        
        // 检查组织名称是否存在
        if (checkOrgNameExists(organization.getOrgName(), organization.getId())) {
            throw new BusinessException("组织名称已存在");
        }
        
        // 更新组织信息
        BeanUtils.copyProperties(organization, existingOrg, "createTime", "createBy");
        return organizationRepository.save(existingOrg);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查组织是否存在
        SysOrganization organization = findById(id);
        if (organization == null) {
            throw new BusinessException("组织不存在");
        }
        
        // 检查是否有子组织
        if (organizationRepository.existsByParentId(id)) {
            throw new BusinessException("该组织下存在子组织，无法删除");
        }
        
        organizationRepository.deleteById(id);
    }

    @Override
    public SysOrganization findById(Long id) {
        return organizationRepository.findById(id).orElse(null);
    }

    @Override
    public List<SysOrganization> findAll() {
        return organizationRepository.findAll();
    }

    @Override
    public List<SysOrganization> findByParentId(Long parentId) {
        return organizationRepository.findByParentIdOrderBySortAsc(parentId);
    }

    @Override
    public List<OrganizationDTO> getOrganizationTree() {
        // 获取所有组织
        List<SysOrganization> allOrgs = organizationRepository.findAll();
        
        // 转换为DTO
        List<OrganizationDTO> orgDTOs = allOrgs.stream().map(org -> {
            OrganizationDTO dto = new OrganizationDTO();
            BeanUtils.copyProperties(org, dto);
            return dto;
        }).collect(Collectors.toList());
        
        // 构建组织ID与DTO的映射
        Map<Long, OrganizationDTO> orgMap = orgDTOs.stream()
                .collect(Collectors.toMap(OrganizationDTO::getId, org -> org));
        
        // 构建树形结构
        List<OrganizationDTO> rootOrgs = new ArrayList<>();
        for (OrganizationDTO org : orgDTOs) {
            if (org.getParentId() == null || org.getParentId() == 0) {
                // 根组织
                rootOrgs.add(org);
            } else {
                // 子组织，添加到父组织的children中
                OrganizationDTO parentOrg = orgMap.get(org.getParentId());
                if (parentOrg != null) {
                    if (parentOrg.getChildren() == null) {
                        parentOrg.setChildren(new ArrayList<>());
                    }
                    parentOrg.getChildren().add(org);
                    
                    // 设置父组织名称
                    org.setParentName(parentOrg.getOrgName());
                }
            }
        }
        
        // 设置是否有子节点标志
        for (OrganizationDTO org : orgDTOs) {
            org.setHasChildren(org.getChildren() != null && !org.getChildren().isEmpty());
        }
        
        return rootOrgs;
    }

    @Override
    public boolean checkOrgCodeExists(String orgCode, Long id) {
        Optional<SysOrganization> existingOrg = organizationRepository.findByOrgCode(orgCode);
        if (id == null) {
            return existingOrg.isPresent();
        }
        return existingOrg.isPresent() && !id.equals(existingOrg.get().getId());
    }

    @Override
    public boolean checkOrgNameExists(String orgName, Long id) {
        Optional<SysOrganization> existingOrg = organizationRepository.findByOrgName(orgName);
        if (id == null) {
            return existingOrg.isPresent();
        }
        return existingOrg.isPresent() && !id.equals(existingOrg.get().getId());
    }
} 