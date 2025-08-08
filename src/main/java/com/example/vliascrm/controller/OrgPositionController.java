package com.example.vliascrm.controller;

import com.example.vliascrm.common.Result;
import com.example.vliascrm.dto.PositionDTO;
import com.example.vliascrm.entity.OrgPosition;
import com.example.vliascrm.service.OrgPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位Controller
 */
@RestController
@RequestMapping("/api/position")
public class OrgPositionController {

    @Autowired
    private OrgPositionService positionService;

    /**
     * 获取岗位列表
     * @return 岗位列表
     */
    @GetMapping("/list")
    public Result<List<OrgPosition>> list() {
        List<OrgPosition> list = positionService.findAll();
        return Result.success(list);
    }

    /**
     * 获取岗位列表（包含组织和部门信息）
     * @param orgId 组织ID
     * @param deptId 部门ID
     * @return 岗位列表
     */
    @GetMapping("/list-with-info")
    public Result<List<PositionDTO>> listWithInfo(
            @RequestParam(required = false) Long orgId,
            @RequestParam(required = false) Long deptId) {
        List<PositionDTO> list = positionService.getPositionList(orgId, deptId);
        return Result.success(list);
    }

    /**
     * 获取岗位管理列表（包含组织和部门信息，显示所有岗位包括禁用的）
     * @param orgId 组织ID
     * @param deptId 部门ID
     * @param status 状态
     * @param positionName 岗位名称
     * @return 岗位列表
     */
    @GetMapping("/management-list")
    public Result<List<PositionDTO>> managementList(
            @RequestParam(required = false) Long orgId,
            @RequestParam(required = false) Long deptId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String positionName) {
        List<PositionDTO> list = positionService.getPositionListForManagement(orgId, deptId, status, positionName);
        return Result.success(list);
    }

    /**
     * 根据ID获取岗位
     * @param id 岗位ID
     * @return 岗位信息
     */
    @GetMapping("/{id}")
    public Result<OrgPosition> getById(@PathVariable Long id) {
        OrgPosition position = positionService.findById(id);
        return Result.success(position);
    }

    /**
     * 新增岗位
     * @param position 岗位信息
     * @return 新增结果
     */
    @PostMapping
    public Result<OrgPosition> add(@RequestBody OrgPosition position) {
        OrgPosition savedPosition = positionService.save(position);
        return Result.success(savedPosition);
    }

    /**
     * 更新岗位
     * @param position 岗位信息
     * @return 更新结果
     */
    @PutMapping
    public Result<OrgPosition> update(@RequestBody OrgPosition position) {
        OrgPosition updatedPosition = positionService.update(position);
        return Result.success(updatedPosition);
    }

    /**
     * 删除岗位
     * @param id 岗位ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        positionService.delete(id);
        return Result.success();
    }

    /**
     * 根据组织ID获取岗位列表（只返回启用状态组织和部门下的岗位）
     * @param orgId 组织ID
     * @return 岗位列表
     */
    @GetMapping("/org/{orgId}")
    public Result<List<OrgPosition>> getByOrgId(@PathVariable Long orgId) {
        List<OrgPosition> positions = positionService.findByOrgId(orgId);
        return Result.success(positions);
    }

    /**
     * 根据部门ID获取岗位列表（只返回启用状态组织和部门下的岗位）
     * @param deptId 部门ID
     * @return 岗位列表
     */
    @GetMapping("/dept/{deptId}")
    public Result<List<OrgPosition>> getByDeptId(@PathVariable Long deptId) {
        List<OrgPosition> positions = positionService.findByDeptId(deptId);
        return Result.success(positions);
    }

    /**
     * 检查岗位编码是否存在
     * @param orgId 组织ID
     * @param positionCode 岗位编码
     * @param id 岗位ID（更新时使用）
     * @return 检查结果
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkPositionCodeExists(
            @RequestParam Long orgId, 
            @RequestParam String positionCode, 
            @RequestParam(required = false) Long id) {
        boolean exists = positionService.checkPositionCodeExists(orgId, positionCode, id);
        return Result.success(exists);
    }
} 