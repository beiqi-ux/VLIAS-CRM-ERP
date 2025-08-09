package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.PositionDTO;
import com.example.vliascrm.entity.OrgDepartment;
import com.example.vliascrm.entity.OrgPosition;
import com.example.vliascrm.entity.SysOrganization;
import com.example.vliascrm.exception.BusinessException;
import com.example.vliascrm.repository.OrgDepartmentRepository;
import com.example.vliascrm.repository.OrgPositionRepository;
import com.example.vliascrm.repository.SysOrganizationRepository;
import com.example.vliascrm.service.OrgPositionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 岗位Service实现类
 */
@Service
public  class OrgPositionServiceImpl implements OrgPositionService {

    @Autowired
    private OrgPositionRepository positionRepository;
    
    @Autowired
    private SysOrganizationRepository organizationRepository;
    
    @Autowired
    private OrgDepartmentRepository departmentRepository;

    @Override
    @Transactional
    public OrgPosition save(OrgPosition position) {
        // 检查组织是否存在
        if (position.getOrgId() != null) {
            Optional<SysOrganization> org = organizationRepository.findById(position.getOrgId());
            if (!org.isPresent()) {
                throw new BusinessException("所属组织不存在");
            }
        }
        
        // 检查部门是否存在
        if (position.getDeptId() != null) {
            Optional<OrgDepartment> dept = departmentRepository.findById(position.getDeptId());
            if (!dept.isPresent()) {
                throw new BusinessException("所属部门不存在");
            }
        }
        
        // 检查岗位编码是否存在
        if (checkPositionCodeExists(position.getOrgId(), position.getPositionCode(), null)) {
            throw new BusinessException("岗位编码已存在");
        }
        
        // 如果没有设置排序，默认为0
        if (position.getSort() == null) {
            position.setSort(0);
        }
        
        // 如果没有设置状态，默认为正常
        if (position.getStatus() == null) {
            position.setStatus(1);
        }
        
        return positionRepository.save(position);
    }

    @Override
    @Transactional
    public OrgPosition update(OrgPosition position) {
        // 检查岗位是否存在
        OrgPosition existingPos = findById(position.getId());
        if (existingPos == null) {
            throw new BusinessException("岗位不存在");
        }
        
        // 检查组织是否存在
        if (position.getOrgId() != null) {
            Optional<SysOrganization> org = organizationRepository.findById(position.getOrgId());
            if (!org.isPresent()) {
                throw new BusinessException("所属组织不存在");
            }
        }
        
        // 检查部门是否存在
        if (position.getDeptId() != null) {
            Optional<OrgDepartment> dept = departmentRepository.findById(position.getDeptId());
            if (!dept.isPresent()) {
                throw new BusinessException("所属部门不存在");
            }
        }
        
        // 检查岗位编码是否存在
        if (checkPositionCodeExists(position.getOrgId(), position.getPositionCode(), position.getId())) {
            throw new BusinessException("岗位编码已存在");
        }
        
        // 更新岗位信息
        BeanUtils.copyProperties(position, existingPos, "createTime", "createBy");
        return positionRepository.save(existingPos);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查岗位是否存在
        OrgPosition position = findById(id);
        if (position == null) {
            throw new BusinessException("岗位不存在");
        }
        
        positionRepository.deleteById(id);
    }

    @Override
    public OrgPosition findById(Long id) {
        return positionRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrgPosition> findAll() {
        return positionRepository.findAllWithActiveOrganizationAndDepartment();
    }

    @Override
    public List<OrgPosition> findByOrgId(Long orgId) {
        return positionRepository.findByOrgIdWithActiveOrganizationAndDepartment(orgId);
    }

    @Override
    public List<OrgPosition> findByDeptId(Long deptId) {
        return positionRepository.findByDeptIdWithActiveOrganizationAndDepartment(deptId);
    }

    @Override
    public List<PositionDTO> getPositionList(Long orgId, Long deptId) {
        // 获取岗位列表（只返回所属组织和部门状态都为启用的岗位）
        List<OrgPosition> positions;
        if (orgId != null && deptId != null) {
            positions = positionRepository.findByDeptIdWithActiveOrganizationAndDepartment(deptId);
        } else if (orgId != null) {
            positions = positionRepository.findByOrgIdWithActiveOrganizationAndDepartment(orgId);
        } else if (deptId != null) {
            positions = positionRepository.findByDeptIdWithActiveOrganizationAndDepartment(deptId);
        } else {
            positions = positionRepository.findAllWithActiveOrganizationAndDepartment();
        }
        
        // 获取所有组织
        List<SysOrganization> organizations = organizationRepository.findAll();
        Map<Long, String> orgNameMap = organizations.stream()
                .filter(org -> org.getOrgName() != null) // 过滤null值
                .collect(Collectors.toMap(SysOrganization::getId, SysOrganization::getOrgName));
        
        // 获取所有部门
        List<OrgDepartment> departments = departmentRepository.findAll();
        Map<Long, String> deptNameMap = departments.stream()
                .filter(dept -> dept.getDeptName() != null) // 过滤null值
                .collect(Collectors.toMap(OrgDepartment::getId, OrgDepartment::getDeptName));
        
        // 转换为DTO
        return positions.stream().map(pos -> {
            PositionDTO dto = new PositionDTO();
            BeanUtils.copyProperties(pos, dto);
            
            // 设置组织名称
            if (pos.getOrgId() != null) {
                dto.setOrgName(orgNameMap.getOrDefault(pos.getOrgId(), ""));
            }
            
            // 设置部门名称
            if (pos.getDeptId() != null) {
                dto.setDeptName(deptNameMap.getOrDefault(pos.getDeptId(), ""));
            }
            
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean checkPositionCodeExists(Long orgId, String positionCode, Long id) {
        Optional<OrgPosition> existingPos = positionRepository.findByOrgIdAndPositionCode(orgId, positionCode);
        return existingPos.isPresent() && !existingPos.get().getId().equals(id);
    }

    @Override
    public List<PositionDTO> getPositionListForManagement(Long orgId, Long deptId) {
        // 获取岗位列表（显示所有岗位包括禁用的，但只显示启用组织和部门下的岗位）
        List<OrgPosition> positions;
        if (orgId != null && deptId != null) {
            positions = positionRepository.findByDeptIdForManagement(deptId);
        } else if (orgId != null) {
            positions = positionRepository.findByOrgIdForManagement(orgId);
        } else if (deptId != null) {
            positions = positionRepository.findByDeptIdForManagement(deptId);
        } else {
            positions = positionRepository.findAllForManagement();
        }
        
        // 获取所有组织
        List<SysOrganization> organizations = organizationRepository.findAll();
        Map<Long, String> orgNameMap = organizations.stream()
                .filter(org -> org.getOrgName() != null) // 过滤null值
                .collect(Collectors.toMap(SysOrganization::getId, SysOrganization::getOrgName));
        
        // 获取所有部门
        List<OrgDepartment> departments = departmentRepository.findAll();
        Map<Long, String> deptNameMap = departments.stream()
                .filter(dept -> dept.getDeptName() != null) // 过滤null值
                .collect(Collectors.toMap(OrgDepartment::getId, OrgDepartment::getDeptName));
        
        // 转换为DTO
        return positions.stream().map(pos -> {
            PositionDTO dto = new PositionDTO();
            BeanUtils.copyProperties(pos, dto);
            
            // 设置组织名称
            if (pos.getOrgId() != null) {
                dto.setOrgName(orgNameMap.getOrDefault(pos.getOrgId(), ""));
            }
            
            // 设置部门名称
            if (pos.getDeptId() != null) {
                dto.setDeptName(deptNameMap.getOrDefault(pos.getDeptId(), ""));
            }
            
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<PositionDTO> getPositionListForManagement(Long orgId, Long deptId, Integer status, String positionName) {
        // 使用统一的条件查询方法
        List<OrgPosition> positions = positionRepository.findByConditionsForManagement(orgId, deptId, status, positionName);
        
        // 获取所有组织
        List<SysOrganization> organizations = organizationRepository.findAll();
        Map<Long, String> orgNameMap = organizations.stream()
                .filter(org -> org.getOrgName() != null) // 过滤null值
                .collect(Collectors.toMap(SysOrganization::getId, SysOrganization::getOrgName));
        
        // 获取所有部门
        List<OrgDepartment> departments = departmentRepository.findAll();
        Map<Long, String> deptNameMap = departments.stream()
                .filter(dept -> dept.getDeptName() != null) // 过滤null值
                .collect(Collectors.toMap(OrgDepartment::getId, OrgDepartment::getDeptName));
        
        // 转换为DTO
        return positions.stream().map(pos -> {
            PositionDTO dto = new PositionDTO();
            BeanUtils.copyProperties(pos, dto);
            
            // 设置组织名称
            if (pos.getOrgId() != null) {
                dto.setOrgName(orgNameMap.getOrDefault(pos.getOrgId(), ""));
            }
            
            // 设置部门名称
            if (pos.getDeptId() != null) {
                dto.setDeptName(deptNameMap.getOrDefault(pos.getDeptId(), ""));
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
} 