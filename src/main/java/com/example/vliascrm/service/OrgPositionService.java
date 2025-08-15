package com.example.vliascrm.service;

import com.example.vliascrm.dto.PositionDTO;
import com.example.vliascrm.entity.OrgPosition;

import java.util.List;

/**
 * 岗位Service接口
 */
public interface OrgPositionService {

    /**
     * 保存岗位
     * @param position 岗位信息
     * @return 保存后的岗位
     */
    OrgPosition save(OrgPosition position);

    /**
     * 更新岗位
     * @param position 岗位信息
     * @return 更新后的岗位
     */
    OrgPosition update(OrgPosition position);

    /**
     * 根据ID删除岗位
     * @param id 岗位ID
     */
    void delete(Long id);

    /**
     * 根据ID查询岗位
     * @param id 岗位ID
     * @return 岗位信息
     */
    OrgPosition findById(Long id);

    /**
     * 查询所有岗位
     * @return 岗位列表
     */
    List<OrgPosition> findAll();

    /**
     * 根据组织ID查询岗位列表
     * @param orgId 组织ID
     * @return 岗位列表
     */
    List<OrgPosition> findByOrgId(Long orgId);

    /**
     * 根据部门ID查询岗位列表
     * @param deptId 部门ID
     * @return 岗位列表
     */
    List<OrgPosition> findByDeptId(Long deptId);

    /**
     * 获取岗位列表（包含组织和部门信息）
     * @param orgId 组织ID
     * @param deptId 部门ID
     * @return 岗位DTO列表
     */
    List<PositionDTO> getPositionList(Long orgId, Long deptId);

    /**
     * 检查岗位编码是否存在
     * @param orgId 组织ID
     * @param positionCode 岗位编码
     * @param id 岗位ID（更新时使用）
     * @return 存在返回true，否则返回false
     */
    boolean checkPositionCodeExists(Long orgId, String positionCode, Long id);

    /**
     * 获取岗位列表（管理列表用，包含组织和部门信息，显示所有岗位包括禁用的）
     * @param orgId 组织ID
     * @param deptId 部门ID
     * @return 岗位DTO列表
     */
    List<PositionDTO> getPositionListForManagement(Long orgId, Long deptId);

    /**
     * 获取岗位列表（管理列表用，包含组织和部门信息，支持状态筛选）
     * @param orgId 组织ID
     * @param deptId 部门ID
     * @param status 状态（可选）
     * @param positionName 岗位名称（可选）
     * @return 岗位DTO列表
     */
    List<PositionDTO> getPositionListForManagement(Long orgId, Long deptId, Integer status, String positionName);
} 