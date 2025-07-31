package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.DepartmentDTO;
import com.example.vliascrm.entity.OrgDepartment;
import com.example.vliascrm.entity.SysOrganization;
import com.example.vliascrm.exception.BusinessException;
import com.example.vliascrm.repository.OrgDepartmentRepository;
import com.example.vliascrm.repository.SysOrganizationRepository;
import com.example.vliascrm.service.OrgDepartmentService;
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
 * 部门Service实现类
 */
@Service
public class OrgDepartmentServiceImpl implements OrgDepartmentService {

    @Autowired
    private OrgDepartmentRepository departmentRepository;
    
    @Autowired
    private SysOrganizationRepository organizationRepository;

    @Override
    @Transactional
    public OrgDepartment save(OrgDepartment department) {
        // 检查组织是否存在
        if (department.getOrgId() != null) {
            Optional<SysOrganization> org = organizationRepository.findById(department.getOrgId());
            if (!org.isPresent()) {
                throw new BusinessException("所属组织不存在");
            }
        }
        
        // 检查部门编码是否存在
        if (checkDeptCodeExists(department.getOrgId(), department.getDeptCode(), null)) {
            throw new BusinessException("部门编码已存在");
        }
        
        // 如果没有设置排序，默认为0
        if (department.getSort() == null) {
            department.setSort(0);
        }
        
        // 如果没有设置状态，默认为正常
        if (department.getStatus() == null) {
            department.setStatus(1);
        }
        
        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public OrgDepartment update(OrgDepartment department) {
        // 检查部门是否存在
        OrgDepartment existingDept = findById(department.getId());
        if (existingDept == null) {
            throw new BusinessException("部门不存在");
        }
        
        // 检查组织是否存在
        if (department.getOrgId() != null) {
            Optional<SysOrganization> org = organizationRepository.findById(department.getOrgId());
            if (!org.isPresent()) {
                throw new BusinessException("所属组织不存在");
            }
        }
        
        // 检查部门编码是否存在
        if (checkDeptCodeExists(department.getOrgId(), department.getDeptCode(), department.getId())) {
            throw new BusinessException("部门编码已存在");
        }
        
        // 更新部门信息
        BeanUtils.copyProperties(department, existingDept, "createTime", "createBy");
        return departmentRepository.save(existingDept);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // 检查部门是否存在
        OrgDepartment department = findById(id);
        if (department == null) {
            throw new BusinessException("部门不存在");
        }
        
        // 检查是否有子部门
        if (departmentRepository.existsByParentId(id)) {
            throw new BusinessException("该部门下存在子部门，无法删除");
        }
        
        departmentRepository.deleteById(id);
    }

    @Override
    public OrgDepartment findById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public List<OrgDepartment> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public List<OrgDepartment> findByOrgId(Long orgId) {
        return departmentRepository.findByOrgIdOrderBySortAsc(orgId);
    }

    @Override
    public List<OrgDepartment> findByParentId(Long parentId) {
        return departmentRepository.findByParentIdOrderBySortAsc(parentId);
    }

    @Override
    public List<DepartmentDTO> getDepartmentTree(Long orgId) {
        // 获取部门列表
        List<OrgDepartment> departments;
        if (orgId != null) {
            departments = departmentRepository.findByOrgIdOrderBySortAsc(orgId);
        } else {
            departments = departmentRepository.findAll();
        }
        
        // 获取所有组织
        List<SysOrganization> organizations = organizationRepository.findAll();
        Map<Long, String> orgNameMap = organizations.stream()
                .collect(Collectors.toMap(SysOrganization::getId, SysOrganization::getOrgName));
        
        // 转换为DTO
        List<DepartmentDTO> deptDTOs = departments.stream().map(dept -> {
            DepartmentDTO dto = new DepartmentDTO();
            BeanUtils.copyProperties(dept, dto);
            
            // 设置组织名称
            if (dept.getOrgId() != null) {
                dto.setOrgName(orgNameMap.getOrDefault(dept.getOrgId(), ""));
            }
            
            return dto;
        }).collect(Collectors.toList());
        
        // 构建部门ID与DTO的映射
        Map<Long, DepartmentDTO> deptMap = deptDTOs.stream()
                .collect(Collectors.toMap(DepartmentDTO::getId, dept -> dept));
        
        // 构建树形结构
        List<DepartmentDTO> rootDepts = new ArrayList<>();
        for (DepartmentDTO dept : deptDTOs) {
            if (dept.getParentId() == null || dept.getParentId() == 0) {
                // 根部门
                rootDepts.add(dept);
            } else {
                // 子部门，添加到父部门的children中
                DepartmentDTO parentDept = deptMap.get(dept.getParentId());
                if (parentDept != null) {
                    if (parentDept.getChildren() == null) {
                        parentDept.setChildren(new ArrayList<>());
                    }
                    parentDept.getChildren().add(dept);
                    
                    // 设置父部门名称
                    dept.setParentName(parentDept.getDeptName());
                }
            }
        }
        
        // 设置是否有子节点标志
        for (DepartmentDTO dept : deptDTOs) {
            dept.setHasChildren(dept.getChildren() != null && !dept.getChildren().isEmpty());
        }
        
        return rootDepts;
    }

    @Override
    public boolean checkDeptCodeExists(Long orgId, String deptCode, Long id) {
        Optional<OrgDepartment> existingDept = departmentRepository.findByOrgIdAndDeptCode(orgId, deptCode);
        return existingDept.isPresent() && !existingDept.get().getId().equals(id);
    }
} 