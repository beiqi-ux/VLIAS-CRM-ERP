package com.example.vliascrm.service;

import com.example.vliascrm.dto.OrganizationDTO;
import com.example.vliascrm.entity.SysOrganization;

import java.util.List;

/**
 * 组织机构Service接口
 */
public interface SysOrganizationService {

    /**
     * 保存组织机构
     * @param organization 组织机构信息
     * @return 保存后的组织机构
     */
    SysOrganization save(SysOrganization organization);

    /**
     * 更新组织机构
     * @param organization 组织机构信息
     * @return 更新后的组织机构
     */
    SysOrganization update(SysOrganization organization);

    /**
     * 根据ID删除组织机构
     * @param id 组织机构ID
     */
    void delete(Long id);

    /**
     * 根据ID查询组织机构
     * @param id 组织机构ID
     * @return 组织机构信息
     */
    SysOrganization findById(Long id);

    /**
     * 查询所有组织机构
     * @return 组织机构列表
     */
    List<SysOrganization> findAll();

    /**
     * 根据父ID查询子组织机构
     * @param parentId 父ID
     * @return 子组织机构列表
     */
    List<SysOrganization> findByParentId(Long parentId);

    /**
     * 获取组织机构树形结构
     * @return 组织机构树
     */
    List<OrganizationDTO> getOrganizationTree();

    /**
     * 检查组织编码是否存在
     * @param orgCode 组织编码
     * @param id 组织ID（更新时使用）
     * @return 存在返回true，否则返回false
     */
    boolean checkOrgCodeExists(String orgCode, Long id);

    /**
     * 检查组织名称是否存在
     * @param orgName 组织名称
     * @param id 组织ID（更新时使用）
     * @return 存在返回true，否则返回false
     */
    boolean checkOrgNameExists(String orgName, Long id);
} 