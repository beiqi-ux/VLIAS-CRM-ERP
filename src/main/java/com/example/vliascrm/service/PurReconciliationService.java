package com.example.vliascrm.service;

import com.example.vliascrm.dto.PurReconciliationDto;
import com.example.vliascrm.entity.PurReconciliation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 供应商对账Service接口
 */
public interface PurReconciliationService {
    
    /**
     * 分页查询对账单列表（新方法，与Controller匹配）
     */
    Page<PurReconciliationDto> findAll(Long supplierId, Integer status, 
                                      String reconciliationNo, String startDate, 
                                      String endDate, Pageable pageable);
    
    /**
     * 分页查询对账单列表（原方法保留兼容性）
     */
    Page<PurReconciliationDto> findReconciliationPage(Long supplierId, Integer status, 
                                                     String reconciliationNo, LocalDate startDate, 
                                                     LocalDate endDate, int page, int size);
    
    /**
     * 根据条件查询采购订单明细
     */
    Page<Map<String, Object>> findPurchaseItems(Long supplierId, String startDate, 
                                               String endDate, Pageable pageable);
    
    /**
     * 根据ID查找对账单详情
     */
    PurReconciliationDto findById(Long id);
    
    /**
     * 根据对账单号查找对账单详情
     */
    PurReconciliationDto findByReconciliationNo(String reconciliationNo);
    
    /**
     * 创建对账单
     */
    PurReconciliationDto create(PurReconciliationDto reconciliationDto);
    
    /**
     * 创建对账单（原方法保留兼容性）
     */
    PurReconciliationDto createReconciliation(PurReconciliationDto reconciliationDto);
    
    /**
     * 更新对账单
     */
    PurReconciliationDto update(PurReconciliationDto reconciliationDto);
    
    /**
     * 更新对账单（原方法保留兼容性）
     */
    PurReconciliationDto updateReconciliation(Long id, PurReconciliationDto reconciliationDto);
    
    /**
     * 删除对账单
     */
    void delete(Long id);
    
    /**
     * 删除对账单（原方法保留兼容性）
     */
    void deleteReconciliation(Long id);
    
    /**
     * 确认对账单
     */
    void confirm(Long id);
    
    /**
     * 确认对账单（原方法保留兼容性）
     */
    void confirmReconciliation(Long id);
    
    /**
     * 结算对账单
     */
    void settle(Long id);
    
    /**
     * 结算对账单（原方法保留兼容性）
     */
    void settleReconciliation(Long id);
    
    /**
     * 生成对账单号
     */
    String generateReconciliationNo();
    
    /**
     * 自动生成对账单
     */
    PurReconciliationDto autoGenerateReconciliation(Long supplierId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 计算对账单金额
     */
    void calculateReconciliationAmount(Long reconciliationId);
    
    /**
     * 获取供应商未结算对账单列表
     */
    List<PurReconciliationDto> findUnsettledBySupplierId(Long supplierId);
    
    /**
     * 批量删除对账单
     */
    void batchDelete(List<Long> ids);
    
    /**
     * 批量确认对账单
     */
    void batchConfirm(List<Long> ids);
} 