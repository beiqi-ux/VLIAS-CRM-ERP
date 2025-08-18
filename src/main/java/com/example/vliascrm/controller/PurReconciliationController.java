package com.example.vliascrm.controller;

import com.example.vliascrm.dto.PurReconciliationDto;
import com.example.vliascrm.service.PurReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reconciliation")
public class PurReconciliationController {

    @Autowired
    private PurReconciliationService reconciliationService;

    /**
     * 分页查询对账单列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('reconciliation-management:view')")
    public ResponseEntity<Map<String, Object>> getReconciliationList(
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String reconciliationNo,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        
        // 转换为0开始的页码给Spring Data
        Pageable pageable = PageRequest.of(current - 1, size, 
            Sort.by(Sort.Direction.DESC, "createTime"));
        
        Page<PurReconciliationDto> page = reconciliationService.findAll(
            supplierId, status, reconciliationNo, startDate, endDate, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", page.getContent());
        response.put("total", page.getTotalElements());
        response.put("current", current);
        response.put("size", size);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 根据ID查询对账单详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('reconciliation-management:view')")
    public ResponseEntity<PurReconciliationDto> getReconciliationById(@PathVariable Long id) {
        PurReconciliationDto reconciliation = reconciliationService.findById(id);
        return ResponseEntity.ok(reconciliation);
    }

    /**
     * 新增对账单
     */
    @PostMapping
    @PreAuthorize("hasAuthority('reconciliation-management:add')")
    public ResponseEntity<PurReconciliationDto> createReconciliation(@RequestBody PurReconciliationDto reconciliationDto) {
        PurReconciliationDto created = reconciliationService.create(reconciliationDto);
        return ResponseEntity.ok(created);
    }

    /**
     * 更新对账单
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('reconciliation-management:edit')")
    public ResponseEntity<PurReconciliationDto> updateReconciliation(
            @PathVariable Long id, @RequestBody PurReconciliationDto reconciliationDto) {
        reconciliationDto.setId(id);
        PurReconciliationDto updated = reconciliationService.update(reconciliationDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除对账单
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('reconciliation-management:delete')")
    public ResponseEntity<Map<String, String>> deleteReconciliation(@PathVariable Long id) {
        reconciliationService.delete(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 生成对账单号
     */
    @GetMapping("/generate-no")
    @PreAuthorize("hasAuthority('reconciliation-management:add')")
    public ResponseEntity<Map<String, String>> generateReconciliationNo() {
        String reconciliationNo = reconciliationService.generateReconciliationNo();
        Map<String, String> response = new HashMap<>();
        response.put("reconciliationNo", reconciliationNo);
        return ResponseEntity.ok(response);
    }

    /**
     * 根据条件查询采购订单明细
     */
    @GetMapping("/purchase-items")
    @PreAuthorize("hasAuthority('reconciliation-management:view')")
    public ResponseEntity<Map<String, Object>> getPurchaseItems(
            @RequestParam Long supplierId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "1") int current,
            @RequestParam(defaultValue = "20") int size) {
        
        // 转换为0开始的页码给Spring Data
        Pageable pageable = PageRequest.of(current - 1, size);
        
        Page<Map<String, Object>> page = reconciliationService.findPurchaseItems(
            supplierId, startDate, endDate, pageable);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", page.getContent());
        response.put("total", page.getTotalElements());
        response.put("current", current);
        response.put("size", size);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 确认对账单
     */
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAuthority('reconciliation-management:confirm')")
    public ResponseEntity<Map<String, String>> confirmReconciliation(@PathVariable Long id) {
        reconciliationService.confirm(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "确认成功");
        return ResponseEntity.ok(response);
    }

    /**
     * 结算对账单
     */
    @PostMapping("/{id}/settle")
    @PreAuthorize("hasAuthority('reconciliation-management:settle')")
    public ResponseEntity<Map<String, String>> settleReconciliation(@PathVariable Long id) {
        reconciliationService.settle(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "结算成功");
        return ResponseEntity.ok(response);
    }
} 