package com.example.vliascrm.controller;
import com.example.vliascrm.entity.InvArea;
import com.example.vliascrm.repository.InvAreaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 库区管理Controller（简化版）
 */
@Slf4j
@RestController
@RequestMapping("/api/inv/area")
@RequiredArgsConstructor
@Tag(name = "库区管理", description = "库区管理相关接口")
public class InvAreaController {

    private final InvAreaRepository areaRepository;

    /**
     * 查询所有启用状态的库区
     */
    @GetMapping("/active")
    @Operation(summary = "查询所有启用状态库区", description = "查询所有启用状态的库区列表")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> findAllActive() {
        try {
            List<InvArea> result = areaRepository.findByStatusAndIsDeletedOrderBySort(1, 0);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("查询所有启用状态库区失败", e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    /**
     * 根据仓库ID查询库区列表
     */
    @GetMapping("/warehouse/{warehouseId}")
    @Operation(summary = "根据仓库查询库区", description = "根据仓库ID查询库区列表")
    @PreAuthorize("hasAuthority('warehouse-location-management:view')")
    public ResponseEntity<Map<String, Object>> findByWarehouseId(@PathVariable Long warehouseId) {
        try {
            List<InvArea> result = areaRepository.findByWarehouseIdAndStatusAndIsDeletedOrderBySort(warehouseId, 1, 0);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "查询成功");
            response.put("data", result);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("根据仓库ID查询库区失败，warehouseId：{}", warehouseId, e);
            Map<String, Object> response = new HashMap<>();
            response.put("code", 500);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
} 

