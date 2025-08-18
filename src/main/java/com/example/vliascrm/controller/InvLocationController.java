package com.example.vliascrm.controller;

import com.example.vliascrm.dto.InvLocationDTO;
import com.example.vliascrm.service.InvLocationService;
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
 * 库位管理Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/inv/location")
@RequiredArgsConstructor
@Tag(name = "库位管理", description = "库位管理相关接口")
public class InvLocationController {

    private final InvLocationService locationService;

    /**
     * 创建库位
     */
    @PostMapping
    @Operation(summary = "创建库位", description = "创建新的库位")
    @PreAuthorize("hasAuthority('warehouse-location-management:create')")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody InvLocationDTO locationDTO) {
        try {
            InvLocationDTO result = locationService.create(locationDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "库位创建成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("创建库位失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 更新库位
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新库位", description = "根据ID更新库位信息")
    @PreAuthorize("hasAuthority('warehouse-location-management:edit')")
    public ResponseEntity<Map<String, Object>> update(
            @Parameter(description = "库位ID") @PathVariable Long id,
            @Valid @RequestBody InvLocationDTO locationDTO) {
        try {
            InvLocationDTO result = locationService.update(id, locationDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "库位更新成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("更新库位失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 删除库位
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除库位", description = "根据ID删除库位（逻辑删除）")
    @PreAuthorize("hasAuthority('warehouse-location-management:delete')")
    public ResponseEntity<Map<String, Object>> delete(
            @Parameter(description = "库位ID") @PathVariable Long id) {
        try {
            locationService.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "库位删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("删除库位失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 批量删除库位
     */
    @DeleteMapping("/batch")
    @Operation(summary = "批量删除库位", description = "批量删除库位（逻辑删除）")
    @PreAuthorize("hasAuthority('warehouse-location-management:delete')")
    public ResponseEntity<Map<String, Object>> batchDelete(@RequestBody List<Long> ids) {
        try {
            locationService.batchDelete(ids);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "批量删除成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("批量删除库位失败，IDs：{}", ids, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 根据ID查询库位详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询库位详情", description = "根据ID查询库位详情")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> findById(
            @Parameter(description = "库位ID") @PathVariable Long id) {
        try {
            InvLocationDTO result = locationService.findById(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询库位详情失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 分页查询库位
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询库位", description = "分页查询库位列表")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> findPage(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "库区ID") @RequestParam(required = false) Long areaId,
            @Parameter(description = "库位名称") @RequestParam(required = false) String locationName,
            @Parameter(description = "状态") @RequestParam(required = false) Byte status) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("sort", "id"));
            Page<InvLocationDTO> result = locationService.findPage(warehouseId, areaId, locationName, status, pageable);
            
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result.getContent());
            response.put("total", result.getTotalElements());
            response.put("page", page);
            response.put("size", size);
            response.put("totalPages", result.getTotalPages());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("分页查询库位失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 根据仓库ID查询库位列表
     */
    @GetMapping("/warehouse/{warehouseId}")
    @Operation(summary = "根据仓库查询库位", description = "根据仓库ID查询库位列表")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> findByWarehouseId(
            @Parameter(description = "仓库ID") @PathVariable Long warehouseId) {
        try {
            List<InvLocationDTO> result = locationService.findByWarehouseId(warehouseId);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("根据仓库ID查询库位失败，warehouseId：{}", warehouseId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 根据库区ID查询库位列表
     */
    @GetMapping("/area/{areaId}")
    @Operation(summary = "根据库区查询库位", description = "根据库区ID查询库位列表")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> findByAreaId(
            @Parameter(description = "库区ID") @PathVariable Long areaId) {
        try {
            List<InvLocationDTO> result = locationService.findByAreaId(areaId);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("根据库区ID查询库位失败，areaId：{}", areaId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 查询所有正常状态的库位
     */
    @GetMapping("/active")
    @Operation(summary = "查询所有正常状态库位", description = "查询所有正常状态的库位列表")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> findAllActive() {
        try {
            List<InvLocationDTO> result = locationService.findAllActive();
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询所有正常状态库位失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 启用库位
     */
    @PutMapping("/{id}/enable")
    @Operation(summary = "启用库位", description = "启用指定的库位")
    @PreAuthorize("hasAuthority('warehouse-location-management:change-status')")
    public ResponseEntity<Map<String, Object>> enable(
            @Parameter(description = "库位ID") @PathVariable Long id) {
        try {
            locationService.enable(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "库位启用成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("启用库位失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 禁用库位
     */
    @PutMapping("/{id}/disable")
    @Operation(summary = "禁用库位", description = "禁用指定的库位")
    @PreAuthorize("hasAuthority('warehouse-location-management:change-status')")
    public ResponseEntity<Map<String, Object>> disable(
            @Parameter(description = "库位ID") @PathVariable Long id) {
        try {
            locationService.disable(id);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "库位禁用成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("禁用库位失败，ID：{}", id, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 批量启用库位
     */
    @PutMapping("/batch/enable")
    @Operation(summary = "批量启用库位", description = "批量启用库位")
    @PreAuthorize("hasAuthority('warehouse-location-management:change-status')")
    public ResponseEntity<Map<String, Object>> batchEnable(@RequestBody List<Long> ids) {
        try {
            locationService.batchEnable(ids);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "批量启用成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("批量启用库位失败，IDs：{}", ids, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 批量禁用库位
     */
    @PutMapping("/batch/disable")
    @Operation(summary = "批量禁用库位", description = "批量禁用库位")
    @PreAuthorize("hasAuthority('warehouse-location-management:change-status')")
    public ResponseEntity<Map<String, Object>> batchDisable(@RequestBody List<Long> ids) {
        try {
            locationService.batchDisable(ids);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "批量禁用成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("批量禁用库位失败，IDs：{}", ids, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 检查库位编码是否存在
     */
    @GetMapping("/check-code")
    @Operation(summary = "检查库位编码", description = "检查库位编码是否已存在")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> checkLocationCode(
            @Parameter(description = "仓库ID") @RequestParam Long warehouseId,
            @Parameter(description = "库位编码") @RequestParam String locationCode,
            @Parameter(description = "排除的库位ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = locationService.checkLocationCodeExists(warehouseId, locationCode, excludeId);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "检查完成");
            response.put("data", Map.of("exists", exists));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("检查库位编码失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 统计仓库下的库位数量
     */
    @GetMapping("/count/warehouse/{warehouseId}")
    @Operation(summary = "统计仓库库位数量", description = "统计指定仓库下的库位数量")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> countByWarehouseId(
            @Parameter(description = "仓库ID") @PathVariable Long warehouseId) {
        try {
            long count = locationService.countByWarehouseId(warehouseId);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "统计完成");
            response.put("data", Map.of("count", count));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("统计仓库库位数量失败，warehouseId：{}", warehouseId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 统计库区下的库位数量
     */
    @GetMapping("/count/area/{areaId}")
    @Operation(summary = "统计库区库位数量", description = "统计指定库区下的库位数量")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> countByAreaId(
            @Parameter(description = "库区ID") @PathVariable Long areaId) {
        try {
            long count = locationService.countByAreaId(areaId);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "统计完成");
            response.put("data", Map.of("count", count));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("统计库区库位数量失败，areaId：{}", areaId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 导出库位数据
     */
    @GetMapping("/export")
    @Operation(summary = "导出库位数据", description = "导出库位数据")
    @PreAuthorize("hasAuthority('warehouse-location-management:export')")
    public ResponseEntity<Map<String, Object>> exportLocations(
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "库区ID") @RequestParam(required = false) Long areaId,
            @Parameter(description = "库位名称") @RequestParam(required = false) String locationName,
            @Parameter(description = "状态") @RequestParam(required = false) Byte status) {
        try {
            List<InvLocationDTO> result = locationService.exportLocations(warehouseId, areaId, locationName, status);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "导出成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("导出库位数据失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
} 

