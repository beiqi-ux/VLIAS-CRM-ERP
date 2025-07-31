package com.example.vliascrm.service;

import com.example.vliascrm.dto.DepartmentDTO;
import com.example.vliascrm.entity.OrgDepartment;

import java.util.List;

/**
 * 部门Service接口
 */
public interface OrgDepartmentService {

    /**
     * 保存部门
     * @param department 部门信息
     * @return 保存后的部门
     */
    OrgDepartment save(OrgDepartment department);

    /**
     * 更新部门
     * @param department 部门信息
     * @return 更新后的部门
     */
    OrgDepartment update(OrgDepartment department);

    /**
     * 根据ID删除部门
     * @param id 部门ID
     */
    void delete(Long id);

    /**
     * 根据ID查询部门
     * @param id 部门ID
     * @return 部门信息
     */
    OrgDepartment findById(Long id);

    /**
     * 查询所有部门
     * @return 部门列表
     */
    List<OrgDepartment> findAll();

    /**
     * 根据组织ID查询部门列表
     * @param orgId 组织ID
     * @return 部门列表
     */
    List<OrgDepartment> findByOrgId(Long orgId);

    /**
     * 根据父ID查询子部门列表
     * @param parentId 父ID
     * @return 子部门列表
     */
    List<OrgDepartment> findByParentId(Long parentId);

    /**
     * 获取部门树形结构
     * @param orgId 组织ID，如果为null则获取所有部门
     * @return 部门树
     */
    List<DepartmentDTO> getDepartmentTree(Long orgId);

    /**
     * 检查部门编码是否存在
     * @param orgId 组织ID
     * @param deptCode 部门编码
     * @param id 部门ID（更新时使用）
     * @return 存在返回true，否则返回false
     */
    boolean checkDeptCodeExists(Long orgId, String deptCode, Long id);
} 