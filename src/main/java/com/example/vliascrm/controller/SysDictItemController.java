package com.example.vliascrm.controller;

import com.example.vliascrm.common.ApiResponse;
import com.example.vliascrm.dto.DictItemDTO;
import com.example.vliascrm.entity.SysDictItem;
import com.example.vliascrm.service.SysDictItemService;
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

/**
 * 数据字典项控制器
 */
@RestController
@RequestMapping("/api/sys/dict-items")
@RequiredArgsConstructor
@Tag(name = "数据字典项管理", description = "数据字典项相关操作接口")
public class SysDictItemController {

    private final SysDictItemService dictItemService;

    /**
     * 创建字典项
     * @param dictItemDTO 字典项信息
     * @return 创建后的字典项
     */
    @PostMapping
    @Operation(summary = "创建字典项", description = "创建新的数据字典项")
    public ApiResponse<SysDictItem> createDictItem(@RequestBody DictItemDTO dictItemDTO) {
        // 检查字典项值是否已存在于指定字典中
        if (dictItemService.existsByDictIdAndItemValue(dictItemDTO.getDictId(), dictItemDTO.getItemValue())) {
            return ApiResponse.failure("字典项值已存在");
        }

        SysDictItem dictItem = new SysDictItem();
        BeanUtils.copyProperties(dictItemDTO, dictItem);
        
        SysDictItem savedDictItem = dictItemService.save(dictItem);
        return ApiResponse.success(savedDictItem);
    }

    /**
     * 更新字典项
     * @param id 字典项ID
     * @param dictItemDTO 字典项信息
     * @return 更新后的字典项
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新字典项", description = "根据ID更新字典项信息")
    public ApiResponse<SysDictItem> updateDictItem(@PathVariable Long id, @RequestBody DictItemDTO dictItemDTO) {
        Optional<SysDictItem> dictItemOpt = dictItemService.findById(id);
        if (!dictItemOpt.isPresent()) {
            return ApiResponse.failure("字典项不存在");
        }

        // 检查字典项值是否已存在于指定字典中（排除当前记录）
        if (dictItemService.existsByDictIdAndItemValueAndIdNot(dictItemDTO.getDictId(), dictItemDTO.getItemValue(), id)) {
            return ApiResponse.failure("字典项值已存在");
        }

        SysDictItem dictItem = dictItemOpt.get();
        BeanUtils.copyProperties(dictItemDTO, dictItem, "id", "createTime", "createBy");
        
        SysDictItem savedDictItem = dictItemService.save(dictItem);
        return ApiResponse.success(savedDictItem);
    }

    /**
     * 删除字典项
     * @param id 字典项ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典项", description = "根据ID删除字典项")
    public ApiResponse<String> deleteDictItem(@PathVariable Long id) {
        if (!dictItemService.findById(id).isPresent()) {
            return ApiResponse.failure("字典项不存在");
        }
        
        dictItemService.deleteById(id);
        return ApiResponse.success("删除成功", null);
    }

    /**
     * 根据ID获取字典项
     * @param id 字典项ID
     * @return 字典项信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取字典项详情", description = "根据ID获取字典项详细信息")
    public ApiResponse<SysDictItem> getDictItem(@PathVariable Long id) {
        Optional<SysDictItem> dictItem = dictItemService.findById(id);
        if (!dictItem.isPresent()) {
            return ApiResponse.failure("字典项不存在");
        }
        return ApiResponse.success(dictItem.get());
    }

    /**
     * 分页查询字典项列表
     * @param page 页码
     * @param size 每页大小
     * @param dictId 字典ID
     * @param itemText 字典项文本（模糊查询）
     * @param itemValue 字典项值（模糊查询）
     * @param status 状态
     * @return 分页结果
     */
    @GetMapping
    @Operation(summary = "分页查询字典项", description = "分页查询数据字典项列表")
    public ApiResponse<Page<SysDictItem>> getDictItemList(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "字典ID") @RequestParam(required = false) Long dictId,
            @Parameter(description = "字典项文本") @RequestParam(required = false) String itemText,
            @Parameter(description = "字典项值") @RequestParam(required = false) String itemValue,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {

        Specification<SysDictItem> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dictId != null) {
                predicates.add(criteriaBuilder.equal(root.get("dictId"), dictId));
            }

            if (StringUtils.hasText(itemText)) {
                predicates.add(criteriaBuilder.like(root.get("itemText"), "%" + itemText + "%"));
            }

            if (StringUtils.hasText(itemValue)) {
                predicates.add(criteriaBuilder.like(root.get("itemValue"), "%" + itemValue + "%"));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            // 默认过滤软删除的记录
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), false));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "sort").and(Sort.by(Sort.Direction.DESC, "createTime")));
        Page<SysDictItem> result = dictItemService.findAll(spec, pageable);
        return ApiResponse.success(result);
    }

    /**
     * 根据字典ID获取字典项列表
     * @param dictId 字典ID
     * @param status 状态（可选）
     * @return 字典项列表
     */
    @GetMapping("/dict/{dictId}")
    @Operation(summary = "根据字典ID获取字典项", description = "根据字典ID获取字典项列表")
    public ApiResponse<List<SysDictItem>> getDictItemsByDictId(
            @PathVariable Long dictId,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        List<SysDictItem> dictItems;
        if (status != null) {
            dictItems = dictItemService.findByDictIdAndStatus(dictId, status);
        } else {
            dictItems = dictItemService.findByDictId(dictId);
        }
        return ApiResponse.success(dictItems);
    }

    /**
     * 根据字典编码获取字典项列表
     * @param dictCode 字典编码
     * @param status 状态（可选）
     * @return 字典项列表
     */
    @GetMapping("/dict-code/{dictCode}")
    @Operation(summary = "根据字典编码获取字典项", description = "根据字典编码获取字典项列表")
    public ApiResponse<List<SysDictItem>> getDictItemsByDictCode(
            @PathVariable String dictCode,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        
        List<SysDictItem> dictItems;
        if (status != null) {
            dictItems = dictItemService.findByDictCodeAndStatus(dictCode, status);
        } else {
            dictItems = dictItemService.findByDictCode(dictCode);
        }
        return ApiResponse.success(dictItems);
    }

    /**
     * 更新字典项状态
     * @param id 字典项ID
     * @param status 状态
     * @return 更新后的字典项
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "更新字典项状态", description = "更新字典项的启用/禁用状态")
    public ApiResponse<SysDictItem> updateDictItemStatus(@PathVariable Long id, @RequestParam Integer status) {
        try {
            SysDictItem dictItem = dictItemService.updateStatus(id, status);
            return ApiResponse.success(dictItem);
        } catch (RuntimeException e) {
            return ApiResponse.failure(e.getMessage());
        }
    }
} 