package com.example.vliascrm.controller;

import com.example.vliascrm.dto.InvWarehouseDTO;
import com.example.vliascrm.service.InvWarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 仓库管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/inv/warehouse")
@RequiredArgsConstructor
@Tag(name = "仓库管理", description = "仓库管理相关接口")
public class InvWarehouseController {

    private final InvWarehouseService warehouseService;

    /**
     * 创建仓库
     */
    @PostMapping
    @Operation(summary = "创建仓库", description = "创建新的仓库")
    @PreAuthorize("hasAuthority('warehouse-management:create')")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody InvWarehouseDTO warehouseDTO) {
        try {
            InvWarehouseDTO result = warehouseService.create(warehouseDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "仓库创建成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("创建仓库失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 更新仓库
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新仓库", description = "根据ID更新仓库信息")
    @PreAuthorize("hasAuthority('warehouse-management:edit')")
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(description = "仓库ID") @PathVariable Long id,
            @Valid @RequestBody InvWarehouseDTO warehouseDTO) {
        try {
            InvWarehouseDTO result = warehouseService.update(id, warehouseDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "仓库更新成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新仓库失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 删除仓库
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除仓库", description = "根据ID删除仓库（逻辑删除）")
    @PreAuthorize("hasAuthority('warehouse-management:delete')")
    public ResponseEntity<Map<String, Object>> delete(
            @Parameter(description = "仓库ID") @PathVariable Long id) {
        try {
            warehouseService.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "仓库删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除仓库失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 批量删除仓库
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除仓库", description = "批量删除仓库（逻辑删除）")
    @PreAuthorize("hasAuthority('warehouse-management:delete')")
    public ResponseEntity<Map<String, Object>> batchDelete(@RequestBody List<Long> ids) {
        try {
            warehouseService.batchDelete(ids);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "批量删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("批量删除仓库失败，IDs：{}", ids, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 根据ID查询仓库详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询仓库详情", description = "根据ID查询仓库详情")
    @PreAuthorize("hasAuthority('warehouse-management:view')")
    public ResponseEntity<Map<String, Object>> findById(
            @Parameter(description = "仓库ID") @PathVariable Long id) {
        try {
            InvWarehouseDTO result = warehouseService.findById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询仓库详情失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 分页查询仓库列表
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询仓库", description = "分页查询仓库列表，支持条件筛选")
    @PreAuthorize("hasAuthority('warehouse-management:view')")
    public ResponseEntity<Map<String, Object>> findPage(
            @Parameter(description = "仓库名称") @RequestParam(required = false) String warehouseName,
            @Parameter(description = "仓库编码") @RequestParam(required = false) String warehouseCode,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序字段") @RequestParam(defaultValue = "sort") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);
            
            Page<InvWarehouseDTO> result = warehouseService.findPage(warehouseName, warehouseCode, status, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", Map.of(
                "content", result.getContent(),
                "totalElements", result.getTotalElements(),
                "totalPages", result.getTotalPages(),
                "size", result.getSize(),
                "number", result.getNumber()
            ));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("分页查询仓库失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 查询所有仓库
     */
    @GetMapping("/all")
    @Operation(summary = "查询所有仓库", description = "查询所有未删除的仓库")
    @PreAuthorize("hasAuthority('warehouse-management:view')")
    public ResponseEntity<Map<String, Object>> findAll() {
        try {
            List<InvWarehouseDTO> result = warehouseService.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询所有仓库失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 查询所有启用的仓库
     */
    @GetMapping("/active")
    @Operation(summary = "查询启用仓库", description = "查询所有启用状态的仓库")
    @PreAuthorize("hasAuthority('warehouse-management:view')")
    public ResponseEntity<Map<String, Object>> findAllActive() {
        try {
            List<InvWarehouseDTO> result = warehouseService.findAllActive();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询启用仓库失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 启用仓库
     */
    @PutMapping("/{id}/enable")
    @Operation(summary = "启用仓库", description = "启用指定仓库")
    @PreAuthorize("hasAuthority('warehouse-management:change-status')")
    public ResponseEntity<Map<String, Object>> enable(
            @Parameter(description = "仓库ID") @PathVariable Long id) {
        try {
            warehouseService.enable(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "仓库启用成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("启用仓库失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 禁用仓库
     */
    @PutMapping("/{id}/disable")
    @Operation(summary = "禁用仓库", description = "禁用指定仓库")
    @PreAuthorize("hasAuthority('warehouse-management:change-status')")
    public ResponseEntity<Map<String, Object>> disable(
            @Parameter(description = "仓库ID") @PathVariable Long id) {
        try {
            warehouseService.disable(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "仓库禁用成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("禁用仓库失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 批量启用仓库
     */
    @PutMapping("/batch/enable")
    @Operation(summary = "批量启用仓库", description = "批量启用仓库")
    @PreAuthorize("hasAuthority('warehouse-management:change-status')")
    public ResponseEntity<Map<String, Object>> batchEnable(@RequestBody List<Long> ids) {
        try {
            warehouseService.batchEnable(ids);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "批量启用成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("批量启用仓库失败，IDs：{}", ids, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 批量禁用仓库
     */
    @PutMapping("/batch/disable")
    @Operation(summary = "批量禁用仓库", description = "批量禁用仓库")
    @PreAuthorize("hasAuthority('warehouse-management:change-status')")
    public ResponseEntity<Map<String, Object>> batchDisable(@RequestBody List<Long> ids) {
        try {
            warehouseService.batchDisable(ids);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "批量禁用成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("批量禁用仓库失败，IDs：{}", ids, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 设置默认仓库
     */
    @PutMapping("/{id}/default")
    @Operation(summary = "设置默认仓库", description = "设置指定仓库为默认仓库")
    @PreAuthorize("hasAuthority('warehouse-management:edit')")
    public ResponseEntity<Map<String, Object>> setDefault(
            @Parameter(description = "仓库ID") @PathVariable Long id) {
        try {
            warehouseService.setDefault(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "设置默认仓库成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("设置默认仓库失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 查询默认仓库
     */
    @GetMapping("/default")
    @Operation(summary = "查询默认仓库", description = "查询默认仓库")
    @PreAuthorize("hasAuthority('warehouse-management:view')")
    public ResponseEntity<Map<String, Object>> findDefaultWarehouse() {
        try {
            InvWarehouseDTO result = warehouseService.findDefaultWarehouse();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询默认仓库失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 检查仓库编码是否存在
     */
    @GetMapping("/check-code")
    @Operation(summary = "检查仓库编码", description = "检查仓库编码是否已存在")
    @PreAuthorize("hasAuthority('warehouse-management:view')")
    public ResponseEntity<Map<String, Object>> checkWarehouseCode(
            @Parameter(description = "仓库编码") @RequestParam String warehouseCode,
            @Parameter(description = "排除的仓库ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = warehouseService.checkWarehouseCodeExists(warehouseCode, excludeId);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "检查完成");
            response.put("data", Map.of("exists", exists));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("检查仓库编码失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 导出仓库数据
     */
    @GetMapping("/export")
    @Operation(summary = "导出仓库数据", description = "导出仓库数据")
    @PreAuthorize("hasAuthority('warehouse-management:export')")
    public ResponseEntity<Map<String, Object>> exportWarehouses(
            @Parameter(description = "仓库名称") @RequestParam(required = false) String warehouseName,
            @Parameter(description = "仓库编码") @RequestParam(required = false) String warehouseCode,
            @Parameter(description = "状态") @RequestParam(required = false) Integer status) {
        try {
            List<InvWarehouseDTO> result = warehouseService.exportWarehouses(warehouseName, warehouseCode, status);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "导出成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("导出仓库数据失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
} 

