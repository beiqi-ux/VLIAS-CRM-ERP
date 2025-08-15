package com.example.vliascrm.controller;

import com.example.vliascrm.dto.PurReceiptDto;
import com.example.vliascrm.dto.PurReceiptItemDto;
import com.example.vliascrm.service.PurReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 采购入库管理控制器
 */
@Tag(name = "采购入库管理", description = "采购入库单相关接口")
@Slf4j
@RestController
@RequestMapping("/api/pur-receipts")
@RequiredArgsConstructor
public class PurReceiptController {

    private final PurReceiptService purReceiptService;

    @Operation(summary = "分页查询采购入库单")
    @GetMapping
    @PreAuthorize("hasAuthority('pur-receipt-management:list')")
    public ResponseEntity<Page<PurReceiptDto>> getPurReceiptPage(
            @Parameter(description = "入库单号") @RequestParam(required = false) String receiptNo,
            @Parameter(description = "采购单号") @RequestParam(required = false) String orderNo,
            @Parameter(description = "供应商ID") @RequestParam(required = false) Long supplierId,
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "入库状态") @RequestParam(required = false) Integer receiptStatus,
            @Parameter(description = "入库类型") @RequestParam(required = false) Integer receiptType,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime,
            @Parameter(description = "创建开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createStartTime,
            @Parameter(description = "创建结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createEndTime,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") int size) {
        
        Page<PurReceiptDto> result = purReceiptService.getPurReceiptPage(
                receiptNo, orderNo, supplierId, warehouseId, receiptStatus, receiptType,
                startTime, endTime, createStartTime, createEndTime, page, size);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "根据ID获取采购入库单详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<PurReceiptDto> getPurReceiptById(@PathVariable Long id) {
        PurReceiptDto result = purReceiptService.getPurReceiptById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "根据入库单号获取详情")
    @GetMapping("/no/{receiptNo}")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<PurReceiptDto> getPurReceiptByNo(@PathVariable String receiptNo) {
        PurReceiptDto result = purReceiptService.getPurReceiptByNo(receiptNo);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "创建采购入库单")
    @PostMapping
    @PreAuthorize("hasAuthority('pur-receipt-management:create')")
    public ResponseEntity<PurReceiptDto> createPurReceipt(@Valid @RequestBody PurReceiptDto purReceiptDto) {
        PurReceiptDto result = purReceiptService.createPurReceipt(purReceiptDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "从采购订单创建入库单")
    @PostMapping("/from-order/{orderId}")
    @PreAuthorize("hasAuthority('pur-receipt-management:create')")
    public ResponseEntity<PurReceiptDto> createReceiptFromOrder(
            @PathVariable Long orderId,
            @Valid @RequestBody PurReceiptDto receiptDto) {
        PurReceiptDto result = purReceiptService.createReceiptFromOrder(orderId, receiptDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "更新采购入库单")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('pur-receipt-management:edit')")
    public ResponseEntity<PurReceiptDto> updatePurReceipt(
            @PathVariable Long id,
            @Valid @RequestBody PurReceiptDto purReceiptDto) {
        PurReceiptDto result = purReceiptService.updatePurReceipt(id, purReceiptDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "删除采购入库单")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pur-receipt-management:delete')")
    public ResponseEntity<Void> deletePurReceipt(@PathVariable Long id) {
        purReceiptService.deletePurReceipt(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "提交采购入库单")
    @PutMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('pur-receipt-management:submit')")
    public ResponseEntity<Void> submitPurReceipt(@PathVariable Long id) {
        purReceiptService.submitPurReceipt(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "审核采购入库单")
    @PutMapping("/{id}/audit")
    @PreAuthorize("hasAuthority('pur-receipt-management:audit')")
    public ResponseEntity<Void> auditPurReceipt(
            @PathVariable Long id,
            @RequestBody Map<String, Object> auditData) {
        boolean approved = (Boolean) auditData.get("approved");
        String auditRemark = (String) auditData.get("auditRemark");
        purReceiptService.auditPurReceipt(id, approved, auditRemark);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "确认入库")
    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAuthority('pur-receipt-management:confirm')")
    public ResponseEntity<Void> confirmReceipt(@PathVariable Long id) {
        purReceiptService.confirmReceipt(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "取消入库单")
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('pur-receipt-management:cancel')")
    public ResponseEntity<Void> cancelPurReceipt(
            @PathVariable Long id,
            @RequestBody Map<String, String> cancelData) {
        String reason = cancelData.get("reason");
        purReceiptService.cancelPurReceipt(id, reason);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "批量确认入库")
    @PutMapping("/batch-confirm")
    @PreAuthorize("hasAuthority('pur-receipt-management:confirm')")
    public ResponseEntity<Void> batchConfirmReceipt(@RequestBody List<Long> ids) {
        purReceiptService.batchConfirmReceipt(ids);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "批量审核入库单")
    @PutMapping("/batch-audit")
    @PreAuthorize("hasAuthority('pur-receipt-management:audit')")
    public ResponseEntity<Void> batchAuditPurReceipt(@RequestBody Map<String, Object> auditData) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) auditData.get("ids");
        boolean approved = (Boolean) auditData.get("approved");
        String auditRemark = (String) auditData.get("auditRemark");
        purReceiptService.batchAuditPurReceipt(ids, approved, auditRemark);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "获取采购订单的入库记录")
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasAuthority('pur-receipt-management:list')")
    public ResponseEntity<List<PurReceiptDto>> getReceiptsByOrderId(@PathVariable Long orderId) {
        List<PurReceiptDto> result = purReceiptService.getReceiptsByOrderId(orderId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取供应商的入库记录")
    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAuthority('pur-receipt-management:list')")
    public ResponseEntity<List<PurReceiptDto>> getReceiptsBySupplierId(@PathVariable Long supplierId) {
        List<PurReceiptDto> result = purReceiptService.getReceiptsBySupplierId(supplierId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取仓库的入库记录")
    @GetMapping("/warehouse/{warehouseId}")
    @PreAuthorize("hasAuthority('pur-receipt-management:list')")
    public ResponseEntity<List<PurReceiptDto>> getReceiptsByWarehouseId(@PathVariable Long warehouseId) {
        List<PurReceiptDto> result = purReceiptService.getReceiptsByWarehouseId(warehouseId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取待审核的入库单")
    @GetMapping("/pending-audit")
    @PreAuthorize("hasAuthority('pur-receipt-management:list')")
    public ResponseEntity<List<PurReceiptDto>> getPendingAuditReceipts() {
        List<PurReceiptDto> result = purReceiptService.getPendingAuditReceipts();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取入库单统计信息")
    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('pur-receipt-management:statistics')")
    public ResponseEntity<Map<String, Object>> getPurReceiptStatistics() {
        Map<String, Object> result = purReceiptService.getPurReceiptStatistics();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取入库趋势数据")
    @GetMapping("/trend")
    @PreAuthorize("hasAuthority('pur-receipt-management:statistics')")
    public ResponseEntity<List<Map<String, Object>>> getReceiptTrendData(
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Map<String, Object>> result = purReceiptService.getReceiptTrendData(startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取商品入库汇总")
    @GetMapping("/goods-summary")
    @PreAuthorize("hasAuthority('pur-receipt-management:statistics')")
    public ResponseEntity<List<Map<String, Object>>> getGoodsReceiptSummary(
            @Parameter(description = "商品ID") @RequestParam Long goodsId,
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Map<String, Object>> result = purReceiptService.getGoodsReceiptSummary(goodsId, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取供应商入库汇总")
    @GetMapping("/supplier-summary")
    @PreAuthorize("hasAuthority('pur-receipt-management:statistics')")
    public ResponseEntity<List<Map<String, Object>>> getSupplierReceiptSummary(
            @Parameter(description = "供应商ID") @RequestParam Long supplierId,
            @Parameter(description = "开始日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<Map<String, Object>> result = purReceiptService.getSupplierReceiptSummary(supplierId, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "检查入库单号是否存在")
    @GetMapping("/exists/{receiptNo}")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<Boolean> existsByReceiptNo(@PathVariable String receiptNo) {
        boolean result = purReceiptService.existsByReceiptNo(receiptNo);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "检查采购订单是否已完全入库")
    @GetMapping("/order/{orderId}/fully-received")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<Boolean> isOrderFullyReceived(@PathVariable Long orderId) {
        boolean result = purReceiptService.isOrderFullyReceived(orderId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取采购订单的入库进度")
    @GetMapping("/order/{orderId}/progress")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<Map<String, Object>> getOrderReceiptProgress(@PathVariable Long orderId) {
        Map<String, Object> result = purReceiptService.getOrderReceiptProgress(orderId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取商品的最近入库价格")
    @GetMapping("/latest-price")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<java.math.BigDecimal> getLatestReceiptPrice(
            @Parameter(description = "商品ID") @RequestParam Long goodsId,
            @Parameter(description = "SKU ID") @RequestParam Long skuId) {
        java.math.BigDecimal result = purReceiptService.getLatestReceiptPrice(goodsId, skuId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取库存预警信息")
    @GetMapping("/stock-warning")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<List<Map<String, Object>>> getStockWarningInfo() {
        List<Map<String, Object>> result = purReceiptService.getStockWarningInfo();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "生成入库单号")
    @GetMapping("/generate-no")
    @PreAuthorize("hasAuthority('pur-receipt-management:create')")
    public ResponseEntity<String> generateReceiptNo() {
        String result = purReceiptService.generateReceiptNo();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "根据批次号查询入库记录")
    @GetMapping("/batch/{batchNumber}")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<List<PurReceiptItemDto>> getReceiptItemsByBatch(@PathVariable String batchNumber) {
        List<PurReceiptItemDto> result = purReceiptService.getReceiptItemsByBatch(batchNumber);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取即将到期的商品")
    @GetMapping("/expiring-items")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<List<PurReceiptItemDto>> getExpiringItems(
            @Parameter(description = "天数") @RequestParam int days) {
        List<PurReceiptItemDto> result = purReceiptService.getExpiringItems(days);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "导出入库单数据")
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('pur-receipt-management:export')")
    public ResponseEntity<byte[]> exportReceiptData(
            @Parameter(description = "入库单号") @RequestParam(required = false) String receiptNo,
            @Parameter(description = "采购单号") @RequestParam(required = false) String orderNo,
            @Parameter(description = "供应商ID") @RequestParam(required = false) Long supplierId,
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "入库状态") @RequestParam(required = false) Integer receiptStatus,
            @Parameter(description = "入库类型") @RequestParam(required = false) Integer receiptType,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startTime,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endTime) {
        
        byte[] result = purReceiptService.exportReceiptData(
                receiptNo, orderNo, supplierId, warehouseId, receiptStatus, receiptType, startTime, endTime);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pur_receipts.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(result);
    }

    @Operation(summary = "导入入库单数据")
    @PostMapping("/import")
    @PreAuthorize("hasAuthority('pur-receipt-management:import')")
    public ResponseEntity<List<String>> importReceiptData(@RequestParam("file") MultipartFile file) {
        try {
            byte[] fileData = file.getBytes();
            List<String> result = purReceiptService.importReceiptData(fileData);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("导入文件失败", e);
            throw new RuntimeException("导入文件失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取入库单明细")
    @GetMapping("/{receiptId}/items")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<List<PurReceiptItemDto>> getReceiptItems(@PathVariable Long receiptId) {
        List<PurReceiptItemDto> result = purReceiptService.getReceiptItems(receiptId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "保存入库单明细")
    @PutMapping("/{receiptId}/items")
    @PreAuthorize("hasAuthority('pur-receipt-management:edit')")
    public ResponseEntity<Void> saveReceiptItems(
            @PathVariable Long receiptId,
            @RequestBody List<PurReceiptItemDto> items) {
        purReceiptService.saveReceiptItems(receiptId, items);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "删除入库单明细")
    @DeleteMapping("/{receiptId}/items")
    @PreAuthorize("hasAuthority('pur-receipt-management:edit')")
    public ResponseEntity<Void> deleteReceiptItems(@PathVariable Long receiptId) {
        purReceiptService.deleteReceiptItems(receiptId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "复制入库单")
    @PostMapping("/{id}/copy")
    @PreAuthorize("hasAuthority('pur-receipt-management:copy')")
    public ResponseEntity<PurReceiptDto> copyPurReceipt(@PathVariable Long id) {
        PurReceiptDto result = purReceiptService.copyPurReceipt(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "生成入库单打印数据")
    @GetMapping("/{id}/print-data")
    @PreAuthorize("hasAuthority('pur-receipt-management:print')")
    public ResponseEntity<Map<String, Object>> generateReceiptPrintData(@PathVariable Long id) {
        Map<String, Object> result = purReceiptService.generateReceiptPrintData(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取入库单审核历史")
    @GetMapping("/{id}/audit-history")
    @PreAuthorize("hasAuthority('pur-receipt-management:view')")
    public ResponseEntity<List<Map<String, Object>>> getReceiptAuditHistory(@PathVariable Long id) {
        List<Map<String, Object>> result = purReceiptService.getReceiptAuditHistory(id);
        return ResponseEntity.ok(result);
    }
} 