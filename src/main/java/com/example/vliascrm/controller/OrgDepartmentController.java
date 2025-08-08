package com.example.vliascrm.controller;

import com.example.vliascrm.common.Result;
import com.example.vliascrm.dto.DepartmentDTO;
import com.example.vliascrm.entity.OrgDepartment;
import com.example.vliascrm.service.OrgDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.example.vliascrm.entity.SysOrganization;
import com.example.vliascrm.repository.SysOrganizationRepository;
import org.springframework.beans.BeanUtils;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * 部门Controller
 */
@RestController
@RequestMapping("/api/department")
public class OrgDepartmentController {

    @Autowired
    private OrgDepartmentService departmentService;

    @Autowired
    private SysOrganizationRepository organizationRepository;

    /**
     * 获取部门列表
     * @return 部门列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('dept-management:view')")
    public Result<List<DepartmentDTO>> list() {
        List<OrgDepartment> departments = departmentService.findAll();
        
        // 获取所有组织机构
        List<SysOrganization> organizations = organizationRepository.findAll();
        Map<Long, String> orgNameMap = organizations.stream()
                .collect(Collectors.toMap(SysOrganization::getId, SysOrganization::getOrgName));
        
        // 转换为DTO并设置组织名称
        List<DepartmentDTO> deptDTOs = departments.stream().map(dept -> {
            DepartmentDTO dto = new DepartmentDTO();
            BeanUtils.copyProperties(dept, dto);
            
            // 设置组织名称
            if (dept.getOrgId() != null) {
                dto.setOrgName(orgNameMap.getOrDefault(dept.getOrgId(), ""));
            }
            
            return dto;
        }).collect(Collectors.toList());
        
        return Result.success(deptDTOs);
    }

    /**
     * 获取部门树
     * @param orgId 组织ID
     * @return 部门树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('dept-management:view')")
    public Result<List<DepartmentDTO>> tree(@RequestParam(required = false) Long orgId) {
        List<DepartmentDTO> tree = departmentService.getDepartmentTree(orgId);
        return Result.success(tree);
    }

    /**
     * 根据ID获取部门
     * @param id 部门ID
     * @return 部门信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('dept-management:view')")
    public Result<DepartmentDTO> getById(@PathVariable Long id) {
        OrgDepartment department = departmentService.findById(id);
        if (department == null) {
            return Result.error("部门不存在");
        }
        
        // 获取组织机构名称
        String orgName = "";
        if (department.getOrgId() != null) {
            Optional<SysOrganization> organization = organizationRepository.findById(department.getOrgId());
            if (organization.isPresent()) {
                orgName = organization.get().getOrgName();
            }
        }
        
        // 转换为DTO
        DepartmentDTO dto = new DepartmentDTO();
        BeanUtils.copyProperties(department, dto);
        dto.setOrgName(orgName);
        
        return Result.success(dto);
    }

    /**
     * 新增部门
     * @param department 部门信息
     * @return 新增结果
     */
    @PostMapping
    @PreAuthorize("hasAuthority('dept-management:create')")
    public Result<OrgDepartment> add(@RequestBody OrgDepartment department) {
        OrgDepartment savedDepartment = departmentService.save(department);
        return Result.success(savedDepartment);
    }

    /**
     * 更新部门
     * @param department 部门信息
     * @return 更新结果
     */
    @PutMapping
    @PreAuthorize("hasAuthority('dept-management:edit')")
    public Result<OrgDepartment> update(@RequestBody OrgDepartment department) {
        OrgDepartment updatedDepartment = departmentService.update(department);
        return Result.success(updatedDepartment);
    }

    /**
     * 删除部门
     * @param id 部门ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('dept-management:delete')")
    public Result<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return Result.success();
    }

    /**
     * 根据组织ID获取部门列表
     * 只返回启用状态组织下的部门
     * @param orgId 组织ID
     * @return 部门列表
     */
    @GetMapping("/org/{orgId}")
    @PreAuthorize("hasAuthority('dept-management:view')")
    public Result<List<DepartmentDTO>> getByOrgId(@PathVariable Long orgId) {
        // 首先检查组织是否启用
        Optional<SysOrganization> organization = organizationRepository.findById(orgId);
        if (!organization.isPresent() || organization.get().getStatus() != 1) {
            // 如果组织不存在或未启用，返回空列表
            return Result.success(List.of());
        }
        
        List<OrgDepartment> departments = departmentService.findByOrgId(orgId);
        String orgName = organization.get().getOrgName();
        
        // 转换为DTO并设置组织名称
        List<DepartmentDTO> deptDTOs = departments.stream().map(dept -> {
            DepartmentDTO dto = new DepartmentDTO();
            BeanUtils.copyProperties(dept, dto);
            dto.setOrgName(orgName);
            return dto;
        }).collect(Collectors.toList());
        
        return Result.success(deptDTOs);
    }

    /**
     * 根据组织ID获取部门选项列表（用于下拉框，不需要权限）
     * 只返回启用状态组织下的启用状态部门
     * @param orgId 组织ID
     * @return 部门列表
     */
    @GetMapping("/org/{orgId}/options")
    public Result<List<DepartmentDTO>> getDepartmentOptions(@PathVariable Long orgId) {
        // 首先检查组织是否启用
        Optional<SysOrganization> organization = organizationRepository.findById(orgId);
        if (!organization.isPresent() || organization.get().getStatus() != 1) {
            // 如果组织不存在或未启用，返回空列表
            return Result.success(List.of());
        }
        
        List<OrgDepartment> departments = departmentService.findByOrgId(orgId);
        String orgName = organization.get().getOrgName();
        
        // 转换为DTO并设置组织名称，同时过滤掉禁用状态的部门
        List<DepartmentDTO> deptDTOs = departments.stream()
            .filter(dept -> dept.getStatus() != null && dept.getStatus() == 1) // 只包含启用状态的部门
            .map(dept -> {
            DepartmentDTO dto = new DepartmentDTO();
            BeanUtils.copyProperties(dept, dto);
            dto.setOrgName(orgName);
            return dto;
        }).collect(Collectors.toList());
        
        return Result.success(deptDTOs);
    }

    /**
     * 根据父ID获取子部门
     * @param parentId 父ID
     * @return 子部门列表
     */
    @GetMapping("/children/{parentId}")
    @PreAuthorize("hasAuthority('dept-management:view')")
    public Result<List<DepartmentDTO>> getChildren(@PathVariable Long parentId) {
        List<OrgDepartment> children = departmentService.findByParentId(parentId);
        
        // 获取所有组织机构
        List<SysOrganization> organizations = organizationRepository.findAll();
        Map<Long, String> orgNameMap = organizations.stream()
                .collect(Collectors.toMap(SysOrganization::getId, SysOrganization::getOrgName));
        
        // 转换为DTO并设置组织名称
        List<DepartmentDTO> childrenDTOs = children.stream().map(dept -> {
            DepartmentDTO dto = new DepartmentDTO();
            BeanUtils.copyProperties(dept, dto);
            
            // 设置组织名称
            if (dept.getOrgId() != null) {
                dto.setOrgName(orgNameMap.getOrDefault(dept.getOrgId(), ""));
            }
            
            return dto;
        }).collect(Collectors.toList());
        
        return Result.success(childrenDTOs);
    }

    /**
     * 检查部门编码是否存在
     * @param orgId 组织ID
     * @param deptCode 部门编码
     * @param id 部门ID（更新时使用）
     * @return 检查结果
     */
    @GetMapping("/check-code")
    @PreAuthorize("hasAuthority('dept-management:view')")
    public Result<Boolean> checkDeptCodeExists(
            @RequestParam Long orgId, 
            @RequestParam String deptCode, 
            @RequestParam(required = false) Long id) {
        boolean exists = departmentService.checkDeptCodeExists(orgId, deptCode, id);
        return Result.success(exists);
    }

    /**
     * 获取组织列表（用于下拉框）
     * @return 组织列表
     */
    @GetMapping("/organizations")
    @PreAuthorize("hasAuthority('dept-management:view')")
    public Result<List<Map<String, Object>>> getOrganizations() {
        List<SysOrganization> organizations = organizationRepository.findByStatusOrderBySortAsc(1); // 只获取启用状态的组织
        List<Map<String, Object>> orgList = organizations.stream().map(org -> {
            Map<String, Object> orgMap = Map.of(
                "id", org.getId(),
                "orgName", org.getOrgName(),
                "orgCode", org.getOrgCode()
            );
            return orgMap;
        }).collect(Collectors.toList());
        
        return Result.success(orgList);
    }
} 