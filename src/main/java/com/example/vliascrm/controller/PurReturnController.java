package com.example.vliascrm.controller;

import com.example.vliascrm.dto.PurReturnDto;
import com.example.vliascrm.dto.PurReturnItemDto;
import com.example.vliascrm.service.PurReturnService;
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

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 采购退货管理控制器
 */
@Tag(name = "采购退货管理", description = "采购退货单相关接口")
@Slf4j
@RestController
@RequestMapping("/api/pur-returns")
@RequiredArgsConstructor
public class PurReturnController {

    private final PurReturnService purReturnService;

    @Operation(summary = "分页查询采购退货单")
    @GetMapping
    @PreAuthorize("hasAuthority('pur-return-management:list')")
    public ResponseEntity<Page<PurReturnDto>> getPurReturnPage(
            @Parameter(description = "退货单号") @RequestParam(required = false) String returnNo,
            @Parameter(description = "入库单号") @RequestParam(required = false) String receiptNo,
            @Parameter(description = "供应商ID") @RequestParam(required = false) Long supplierId,
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "退货状态") @RequestParam(required = false) Integer returnStatus,
            @Parameter(description = "退货原因类型") @RequestParam(required = false) Integer reasonType,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @Parameter(description = "创建开始时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createStartTime,
            @Parameter(description = "创建结束时间") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createEndTime,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页数量") @RequestParam(defaultValue = "20") int size) {
        
        Page<PurReturnDto> result = purReturnService.getPurReturnPage(
                returnNo, receiptNo, supplierId, warehouseId, returnStatus, reasonType,
                startDate, endDate, createStartTime, createEndTime, page, size);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "根据ID获取退货单详情")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pur-return-management:view')")
    public ResponseEntity<PurReturnDto> getPurReturnById(
            @Parameter(description = "退货单ID") @PathVariable Long id) {
        PurReturnDto result = purReturnService.getPurReturnById(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "根据退货单号获取详情")
    @GetMapping("/by-no/{returnNo}")
    @PreAuthorize("hasAuthority('pur-return-management:view')")
    public ResponseEntity<PurReturnDto> getPurReturnByNo(
            @Parameter(description = "退货单号") @PathVariable String returnNo) {
        PurReturnDto result = purReturnService.getPurReturnByNo(returnNo);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "创建采购退货单")
    @PostMapping
    @PreAuthorize("hasAuthority('pur-return-management:create')")
    public ResponseEntity<PurReturnDto> createPurReturn(
            @Parameter(description = "退货单信息") @Valid @RequestBody PurReturnDto purReturnDto) {
        PurReturnDto result = purReturnService.createPurReturn(purReturnDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "更新采购退货单")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('pur-return-management:edit')")
    public ResponseEntity<PurReturnDto> updatePurReturn(
            @Parameter(description = "退货单ID") @PathVariable Long id,
            @Parameter(description = "退货单信息") @Valid @RequestBody PurReturnDto purReturnDto) {
        PurReturnDto result = purReturnService.updatePurReturn(id, purReturnDto);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "删除采购退货单")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pur-return-management:delete')")
    public ResponseEntity<Void> deletePurReturn(
            @Parameter(description = "退货单ID") @PathVariable Long id) {
        purReturnService.deletePurReturn(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "批量删除采购退货单")
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('pur-return-management:delete')")
    public ResponseEntity<Void> batchDeletePurReturns(
            @Parameter(description = "退货单ID列表") @RequestBody List<Long> ids) {
        purReturnService.batchDeletePurReturns(ids);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "提交采购退货单")
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('pur-return-management:submit')")
    public ResponseEntity<Void> submitPurReturn(
            @Parameter(description = "退货单ID") @PathVariable Long id) {
        purReturnService.submitPurReturn(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "审核采购退货单")
    @PostMapping("/{id}/audit")
    @PreAuthorize("hasAuthority('pur-return-management:audit')")
    public ResponseEntity<Void> auditPurReturn(
            @Parameter(description = "退货单ID") @PathVariable Long id,
            @Parameter(description = "是否通过") @RequestParam boolean approved,
            @Parameter(description = "审核备注") @RequestParam(required = false) String auditRemark) {
        purReturnService.auditPurReturn(id, approved, auditRemark);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "确认退货")
    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAuthority('pur-return-management:confirm')")
    public ResponseEntity<Void> confirmReturn(
            @Parameter(description = "退货单ID") @PathVariable Long id) {
        purReturnService.confirmReturn(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "取消采购退货单")
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('pur-return-management:cancel')")
    public ResponseEntity<Void> cancelPurReturn(
            @Parameter(description = "退货单ID") @PathVariable Long id,
            @Parameter(description = "取消原因") @RequestParam(required = false) String reason) {
        purReturnService.cancelPurReturn(id, reason);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "复制采购退货单")
    @PostMapping("/{id}/copy")
    @PreAuthorize("hasAuthority('pur-return-management:copy')")
    public ResponseEntity<PurReturnDto> copyPurReturn(
            @Parameter(description = "退货单ID") @PathVariable Long id) {
        PurReturnDto result = purReturnService.copyPurReturn(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "根据入库单ID获取可退货明细")
    @GetMapping("/returnable-items/{receiptId}")
    @PreAuthorize("hasAuthority('pur-return-management:view')")
    public ResponseEntity<List<PurReturnItemDto>> getReturnableItems(
            @Parameter(description = "入库单ID") @PathVariable Long receiptId) {
        List<PurReturnItemDto> result = purReturnService.getReturnableItemsByReceiptId(receiptId);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取退货单明细列表")
    @GetMapping("/{id}/items")
    @PreAuthorize("hasAuthority('pur-return-management:view')")
    public ResponseEntity<List<PurReturnItemDto>> getPurReturnItems(
            @Parameter(description = "退货单ID") @PathVariable Long id) {
        List<PurReturnItemDto> result = purReturnService.getPurReturnItems(id);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取退货统计")
    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('pur-return-management:statistics')")
    public ResponseEntity<Map<String, Object>> getReturnStatistics(
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Map<String, Object> result = purReturnService.getReturnStatistics(startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "获取待审核退货单")
    @GetMapping("/pending-audit")
    @PreAuthorize("hasAuthority('pur-return-management:audit')")
    public ResponseEntity<List<PurReturnDto>> getPendingAuditReturns() {
        List<PurReturnDto> result = purReturnService.getPendingAuditReturns();
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "生成退货单号")
    @PostMapping("/generate-no")
    @PreAuthorize("hasAuthority('pur-return-management:create')")
    public ResponseEntity<String> generateReturnNo() {
        String returnNo = purReturnService.generateReturnNo();
        return ResponseEntity.ok(returnNo);
    }

    @Operation(summary = "导出退货单")
    @GetMapping("/export")
    @PreAuthorize("hasAuthority('pur-return-management:export')")
    public ResponseEntity<byte[]> exportPurReturns(
            @Parameter(description = "退货单号") @RequestParam(required = false) String returnNo,
            @Parameter(description = "入库单号") @RequestParam(required = false) String receiptNo,
            @Parameter(description = "供应商ID") @RequestParam(required = false) Long supplierId,
            @Parameter(description = "仓库ID") @RequestParam(required = false) Long warehouseId,
            @Parameter(description = "退货状态") @RequestParam(required = false) Integer returnStatus,
            @Parameter(description = "退货原因类型") @RequestParam(required = false) Integer reasonType,
            @Parameter(description = "开始日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        
        byte[] result = purReturnService.exportPurReturns(returnNo, receiptNo, supplierId, 
                warehouseId, returnStatus, reasonType, startDate, endDate);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pur_returns.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(result);
    }

    @Operation(summary = "打印退货单")
    @GetMapping("/{id}/print")
    @PreAuthorize("hasAuthority('pur-return-management:print')")
    public ResponseEntity<byte[]> printPurReturn(
            @Parameter(description = "退货单ID") @PathVariable Long id) {
        byte[] result = purReturnService.printPurReturn(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pur_return_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(result);
    }
} 
 