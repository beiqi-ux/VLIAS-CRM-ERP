package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.DictDTO;
import com.example.vliascrm.entity.SysDict;
import com.example.vliascrm.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.Map;

/**
 * 数据字典控制器
 */
@RestController
@RequestMapping("/api/sys/dicts")
@RequiredArgsConstructor
@Tag(name = "数据字典管理", description = "数据字典相关操作接口")
public class SysDictController {

    private final SysDictService dictService;

    /**
     * 创建字典
     * @param dictDTO 字典信息
     * @return 创建后的字典
     */
    @PostMapping
    @PreAuthorize("hasAuthority('dict-management:create')")
    @Operation(summary = "创建字典", description = "创建新的数据字典")
    public ApiResponse<SysDict> createDict(@RequestBody DictDTO dictDTO) {
        // 检查字典编码是否已存在
        if (dictService.existsByDictCode(dictDTO.getDictCode())) {
            return ApiResponse.failure("字典编码已存在");
        }

        SysDict dict = new SysDict();
        BeanUtils.copyProperties(dictDTO, dict);
        
        SysDict savedDict = dictService.save(dict);
        return ApiResponse.success(savedDict);
    }

    /**
     * 更新字典
     * @param id 字典ID
     * @param dictDTO 字典信息
     * @return 更新后的字典
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('dict-management:edit')")
    @Operation(summary = "更新字典", description = "根据ID更新字典信息")
    public ApiResponse<SysDict> updateDict(@PathVariable Long id, @RequestBody DictDTO dictDTO) {
        Optional<SysDict> dictOpt = dictService.findById(id);
        if (!dictOpt.isPresent()) {
            return ApiResponse.failure("字典不存在");
        }

        // 检查字典编码是否已存在（排除当前记录）
        if (dictService.existsByDictCodeAndIdNot(dictDTO.getDictCode(), id)) {
            return ApiResponse.failure("字典编码已存在");
        }

        SysDict dict = dictOpt.get();
        BeanUtils.copyProperties(dictDTO, dict, "id", "createTime", "createBy");
        
        SysDict savedDict = dictService.save(dict);
        return ApiResponse.success(savedDict);
    }

    /**
     * 删除字典
     * @param id 字典ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('dict-management:delete')")
    @Operation(summary = "删除字典", description = "根据ID删除字典（同时删除字典项）")
    public ApiResponse<String> deleteDict(@PathVariable Long id) {
        if (!dictService.findById(id).isPresent()) {
            return ApiResponse.failure("字典不存在");
        }
        
        dictService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }

    /**
     * 根据ID获取字典
     * @param id 字典ID
     * @return 字典信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('dict-management:view')")
    @Operation(summary = "获取字典详情", description = "根据ID获取字典详细信息")
    public ApiResponse<SysDict> getDict(@PathVariable Long id) {
        Optional<SysDict> dict = dictService.findById(id);
        if (!dict.isPresent()) {
            return ApiResponse.failure("字典不存在");
        }
        return ApiResponse.success(dict.get());
    }

    /**
     * 分页查询字典列表
     * @param page 页码
     * @param size 每页大小
     * @param dictName 字典名称（模糊查询）
     * @param dictCode 字典编码（模糊查询）
     * @param status 状态
     * @return 分页结果
     */
    @GetMapping
    @PreAuthorize("hasAuthority('dict-management:view')")
    @Operation(summary = "分页查询字典", description = "分页查询数据字典列表")
    public ApiResponse<Page<SysDict>> getDictList(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "字典名称") @RequestParam(required = false) String dictName,
            @Parameter(description = "字典编码") @RequestParam(required = false) String dictCode,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {

        Specification<SysDict> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(dictName)) {
                predicates.add(criteriaBuilder.like(root.get("dictName"), "%" + dictName + "%"));
            }

            if (StringUtils.hasText(dictCode)) {
                predicates.add(criteriaBuilder.like(root.get("dictCode"), "%" + dictCode + "%"));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 默认过滤软删除的记录
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));
        Page<SysDict> result = dictService.findAll(spec, pageable);
        return ApiResponse.success(result);
    }

    /**
     * 获取所有启用的字典列表
     * @return 字典列表
     */
    @GetMapping("/enabled")
    @Operation(summary = "获取启用字典列表", description = "获取所有状态为启用的字典列表")
    public ApiResponse<List<SysDict>> getEnabledDictList() {
        List<SysDict> dictList = dictService.findByStatus(1);
        return ApiResponse.success(dictList);
    }

    /**
     * 更新字典状态
     * @param id 字典ID
     * @param status 状态
     * @return 更新后的字典
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "更新字典状态", description = "更新字典的启用/禁用状态")
    public ApiResponse<SysDict> updateDictStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            SysDict dict = dictService.updateStatus(id, status);
            return ApiResponse.success(dict);
        } catch (RuntimeException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }
} 