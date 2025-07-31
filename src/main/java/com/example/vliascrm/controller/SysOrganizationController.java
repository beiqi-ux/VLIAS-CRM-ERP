package com.example.vliascrm.controller;

import com.example.vliascrm.common.Result;
import com.example.vliascrm.dto.OrganizationDTO;
import com.example.vliascrm.entity.SysOrganization;
import com.example.vliascrm.service.SysOrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 组织机构Controller
 */
@RestController
@RequestMapping("/api/organization")
public class SysOrganizationController {

    @Autowired
    private SysOrganizationService organizationService;

    /**
     * 获取组织机构列表
     * @return 组织机构列表
     */
    @GetMapping("/list")
    public Result<List<SysOrganization>> list() {
        List<SysOrganization> list = organizationService.findAll();
        return Result.success(list);
    }

    /**
     * 获取组织机构树
     * @return 组织机构树
     */
    @GetMapping("/tree")
    public Result<List<OrganizationDTO>> tree() {
        List<OrganizationDTO> tree = organizationService.getOrganizationTree();
        return Result.success(tree);
    }

    /**
     * 根据ID获取组织机构
     * @param id 组织机构ID
     * @return 组织机构信息
     */
    @GetMapping("/{id}")
    public Result<SysOrganization> getById(@PathVariable Long id) {
        SysOrganization organization = organizationService.findById(id);
        return Result.success(organization);
    }

    /**
     * 新增组织机构
     * @param organization 组织机构信息
     * @return 新增结果
     */
    @PostMapping
    public Result<SysOrganization> add(@RequestBody SysOrganization organization) {
        SysOrganization savedOrganization = organizationService.save(organization);
        return Result.success(savedOrganization);
    }

    /**
     * 更新组织机构
     * @param organization 组织机构信息
     * @return 更新结果
     */
    @PutMapping
    public Result<SysOrganization> update(@RequestBody SysOrganization organization) {
        SysOrganization updatedOrganization = organizationService.update(organization);
        return Result.success(updatedOrganization);
    }

    /**
     * 删除组织机构
     * @param id 组织机构ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        organizationService.delete(id);
        return Result.success();
    }

    /**
     * 根据父ID获取子组织机构
     * @param parentId 父ID
     * @return 子组织机构列表
     */
    @GetMapping("/children/{parentId}")
    public Result<List<SysOrganization>> getChildren(@PathVariable Long parentId) {
        List<SysOrganization> children = organizationService.findByParentId(parentId);
        return Result.success(children);
    }

    /**
     * 检查组织编码是否存在
     * @param orgCode 组织编码
     * @param id 组织ID（更新时使用）
     * @return 检查结果
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkOrgCodeExists(@RequestParam String orgCode, @RequestParam(required = false) Long id) {
        boolean exists = organizationService.checkOrgCodeExists(orgCode, id);
        return Result.success(exists);
    }

    /**
     * 检查组织名称是否存在
     * @param orgName 组织名称
     * @param id 组织ID（更新时使用）
     * @return 检查结果
     */
    @GetMapping("/check-name")
    public Result<Boolean> checkOrgNameExists(@RequestParam String orgName, @RequestParam(required = false) Long id) {
        boolean exists = organizationService.checkOrgNameExists(orgName, id);
        return Result.success(exists);
    }
} 