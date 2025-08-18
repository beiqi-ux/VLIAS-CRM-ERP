package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.PurReconciliationDto;
import com.example.vliascrm.dto.PurReconciliationItemDto;
import com.example.vliascrm.entity.PurReconciliation;
import com.example.vliascrm.entity.PurReconciliationItem;
import com.example.vliascrm.entity.PurSupplier;
import com.example.vliascrm.repository.PurReconciliationRepository;
import com.example.vliascrm.repository.PurReconciliationItemRepository;
import com.example.vliascrm.repository.PurSupplierRepository;
import com.example.vliascrm.service.PurReconciliationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 供应商对账Service实现类
 */
@Service
@Transactional
public class PurReconciliationServiceImpl implements PurReconciliationService {
    
    @Autowired
    private PurReconciliationRepository reconciliationRepository;
    
    @Autowired
    private PurReconciliationItemRepository reconciliationItemRepository;
    
    @Autowired
    private PurSupplierRepository supplierRepository;
    
    @Override
    public Page<PurReconciliationDto> findReconciliationPage(Long supplierId, Integer status, 
                                                            String reconciliationNo, LocalDate startDate, 
                                                            LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PurReconciliation> reconciliationPage = reconciliationRepository.findReconciliationPage(
                supplierId, status, reconciliationNo, startDate, endDate, pageable);
        
        List<PurReconciliationDto> dtoList = reconciliationPage.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new PageImpl<>(dtoList, pageable, reconciliationPage.getTotalElements());
    }
    
    @Override
    public PurReconciliationDto findById(Long id) {
        Optional<PurReconciliation> reconciliationOpt = reconciliationRepository.findById(id);
        if (reconciliationOpt.isPresent()) {
            PurReconciliation reconciliation = reconciliationOpt.get();
            if (reconciliation.getIsDeleted() == 0) {
                PurReconciliationDto dto = convertToDto(reconciliation);
                // 加载明细数据
                List<PurReconciliationItem> items = reconciliationItemRepository.findByReconciliationIdOrderByCreateTimeDesc(id);
                dto.setItems(items.stream().map(this::convertItemToDto).collect(Collectors.toList()));
                return dto;
            }
        }
        return null;
    }
    
    @Override
    public PurReconciliationDto findByReconciliationNo(String reconciliationNo) {
        Optional<PurReconciliation> reconciliationOpt = reconciliationRepository.findByReconciliationNoAndIsDeleted(reconciliationNo, 0);
        if (reconciliationOpt.isPresent()) {
            PurReconciliation reconciliation = reconciliationOpt.get();
            PurReconciliationDto dto = convertToDto(reconciliation);
            // 加载明细数据
            List<PurReconciliationItem> items = reconciliationItemRepository.findByReconciliationNoOrderByCreateTimeDesc(reconciliationNo);
            dto.setItems(items.stream().map(this::convertItemToDto).collect(Collectors.toList()));
            return dto;
        }
        return null;
    }
    
    @Override
    public PurReconciliationDto createReconciliation(PurReconciliationDto reconciliationDto) {
        PurReconciliation reconciliation = convertToEntity(reconciliationDto);
        
        // 生成对账单号
        if (reconciliation.getReconciliationNo() == null || reconciliation.getReconciliationNo().isEmpty()) {
            reconciliation.setReconciliationNo(generateReconciliationNo());
        }
        
        // 设置创建时间
        reconciliation.setCreateTime(LocalDateTime.now());
        reconciliation.setIsDeleted(0);
        
        // 保存对账单
        reconciliation = reconciliationRepository.save(reconciliation);
        
        // 保存明细
        if (reconciliationDto.getItems() != null && !reconciliationDto.getItems().isEmpty()) {
            for (PurReconciliationItemDto itemDto : reconciliationDto.getItems()) {
                PurReconciliationItem item = convertItemToEntity(itemDto);
                item.setReconciliationId(reconciliation.getId());
                item.setReconciliationNo(reconciliation.getReconciliationNo());
                item.setCreateTime(LocalDateTime.now());
                reconciliationItemRepository.save(item);
            }
        }
        
        // 计算对账单金额
        calculateReconciliationAmount(reconciliation.getId());
        
        return convertToDto(reconciliation);
    }
    
    @Override
    public PurReconciliationDto updateReconciliation(Long id, PurReconciliationDto reconciliationDto) {
        Optional<PurReconciliation> reconciliationOpt = reconciliationRepository.findById(id);
        if (reconciliationOpt.isPresent()) {
            PurReconciliation reconciliation = reconciliationOpt.get();
            if (reconciliation.getIsDeleted() == 0) {
                // 只有草稿状态才能修改
                if (reconciliation.getStatus() != 1) {
                    throw new RuntimeException("只有草稿状态的对账单才能修改");
                }
                
                // 更新基本信息
                reconciliation.setSupplierId(reconciliationDto.getSupplierId());
                reconciliation.setStartDate(reconciliationDto.getStartDate());
                reconciliation.setEndDate(reconciliationDto.getEndDate());
                reconciliation.setRemark(reconciliationDto.getRemark());
                reconciliation.setUpdateTime(LocalDateTime.now());
                reconciliation.setUpdateBy(reconciliationDto.getUpdateBy());
                
                // 删除原有明细
                reconciliationItemRepository.deleteByReconciliationId(id);
                
                // 保存新明细
                if (reconciliationDto.getItems() != null && !reconciliationDto.getItems().isEmpty()) {
                    for (PurReconciliationItemDto itemDto : reconciliationDto.getItems()) {
                        PurReconciliationItem item = convertItemToEntity(itemDto);
                        item.setReconciliationId(reconciliation.getId());
                        item.setReconciliationNo(reconciliation.getReconciliationNo());
                        item.setCreateTime(LocalDateTime.now());
                        reconciliationItemRepository.save(item);
                    }
                }
                
                // 重新计算对账单金额
                calculateReconciliationAmount(id);
                
                return convertToDto(reconciliationRepository.save(reconciliation));
            }
        }
        return null;
    }
    
    @Override
    public void deleteReconciliation(Long id) {
        Optional<PurReconciliation> reconciliationOpt = reconciliationRepository.findById(id);
        if (reconciliationOpt.isPresent()) {
            PurReconciliation reconciliation = reconciliationOpt.get();
            // 只有草稿状态才能删除
            if (reconciliation.getStatus() != 1) {
                throw new RuntimeException("只有草稿状态的对账单才能删除");
            }
            
            // 逻辑删除
            reconciliation.setIsDeleted(1);
            reconciliation.setUpdateTime(LocalDateTime.now());
            reconciliationRepository.save(reconciliation);
        }
    }
    
    @Override
    public void confirmReconciliation(Long id) {
        Optional<PurReconciliation> reconciliationOpt = reconciliationRepository.findById(id);
        if (reconciliationOpt.isPresent()) {
            PurReconciliation reconciliation = reconciliationOpt.get();
            if (reconciliation.getStatus() == 1 || reconciliation.getStatus() == 2) {
                reconciliation.setStatus(3); // 已确认
                reconciliation.setUpdateTime(LocalDateTime.now());
                reconciliationRepository.save(reconciliation);
            } else {
                throw new RuntimeException("对账单状态不允许确认");
            }
        }
    }
    
    @Override
    public void settleReconciliation(Long id) {
        Optional<PurReconciliation> reconciliationOpt = reconciliationRepository.findById(id);
        if (reconciliationOpt.isPresent()) {
            PurReconciliation reconciliation = reconciliationOpt.get();
            if (reconciliation.getStatus() == 3) {
                reconciliation.setStatus(4); // 已结算
                reconciliation.setSettlementTime(LocalDateTime.now());
                reconciliation.setUpdateTime(LocalDateTime.now());
                reconciliationRepository.save(reconciliation);
            } else {
                throw new RuntimeException("对账单状态不允许结算");
            }
        }
    }
    
    @Override
    public String generateReconciliationNo() {
        String prefix = "REC";
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // 查询当天最大序号
        String pattern = prefix + dateStr + "%";
        // 这里简化处理，实际应该查询数据库获取最大序号
        String sequence = String.format("%04d", (int)(Math.random() * 9999) + 1);
        
        return prefix + dateStr + sequence;
    }
    
    @Override
    public PurReconciliationDto autoGenerateReconciliation(Long supplierId, LocalDate startDate, LocalDate endDate) {
        // 创建对账单
        PurReconciliationDto reconciliationDto = new PurReconciliationDto();
        reconciliationDto.setSupplierId(supplierId);
        reconciliationDto.setStartDate(startDate);
        reconciliationDto.setEndDate(endDate);
        reconciliationDto.setStatus(1); // 草稿状态
        
        // 这里应该查询指定时间范围内的采购单、入库单、退货单等数据
        // 由于涉及到其他表的查询，这里简化处理
        List<PurReconciliationItemDto> items = new ArrayList<>();
        reconciliationDto.setItems(items);
        
        return createReconciliation(reconciliationDto);
    }
    
    @Override
    public void calculateReconciliationAmount(Long reconciliationId) {
        Optional<PurReconciliation> reconciliationOpt = reconciliationRepository.findById(reconciliationId);
        if (reconciliationOpt.isPresent()) {
            PurReconciliation reconciliation = reconciliationOpt.get();
            
            // 计算总金额
            BigDecimal totalAmount = reconciliationItemRepository.sumAmountByReconciliationId(reconciliationId);
            if (totalAmount == null) {
                totalAmount = BigDecimal.ZERO;
            }
            
            reconciliation.setTotalAmount(totalAmount);
            // 未付金额 = 总金额 - 已付金额
            reconciliation.setUnpaidAmount(totalAmount.subtract(reconciliation.getPaidAmount() == null ? BigDecimal.ZERO : reconciliation.getPaidAmount()));
            reconciliation.setUpdateTime(LocalDateTime.now());
            
            reconciliationRepository.save(reconciliation);
        }
    }
    
    @Override
    public List<PurReconciliationDto> findUnsettledBySupplierId(Long supplierId) {
        List<PurReconciliation> reconciliations = reconciliationRepository.findBySupplierId(supplierId)
                .stream()
                .filter(r -> r.getStatus() != 4 && r.getIsDeleted() == 0) // 未结算且未删除
                .collect(Collectors.toList());
        
        return reconciliations.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    @Override
    public void batchDelete(List<Long> ids) {
        for (Long id : ids) {
            deleteReconciliation(id);
        }
    }
    
    @Override
    public void batchConfirm(List<Long> ids) {
        for (Long id : ids) {
            confirmReconciliation(id);
        }
    }
    
    /**
     * 实体转DTO
     */
    private PurReconciliationDto convertToDto(PurReconciliation reconciliation) {
        PurReconciliationDto dto = new PurReconciliationDto();
        BeanUtils.copyProperties(reconciliation, dto);
        
        // 设置供应商名称
        if (reconciliation.getSupplierId() != null) {
            Optional<PurSupplier> supplierOpt = supplierRepository.findById(reconciliation.getSupplierId());
            if (supplierOpt.isPresent()) {
                dto.setSupplierName(supplierOpt.get().getSupplierName());
            }
        }
        
        // 设置状态名称
        dto.setStatusName(getStatusName(reconciliation.getStatus()));
        
        return dto;
    }
    
    /**
     * DTO转实体
     */
    private PurReconciliation convertToEntity(PurReconciliationDto dto) {
        PurReconciliation reconciliation = new PurReconciliation();
        BeanUtils.copyProperties(dto, reconciliation);
        return reconciliation;
    }
    
    /**
     * 明细实体转DTO
     */
    private PurReconciliationItemDto convertItemToDto(PurReconciliationItem item) {
        PurReconciliationItemDto dto = new PurReconciliationItemDto();
        BeanUtils.copyProperties(item, dto);
        
        // 设置单据类型名称
        dto.setBillTypeName(getBillTypeName(item.getBillType()));
        
        return dto;
    }
    
    /**
     * 明细DTO转实体
     */
    private PurReconciliationItem convertItemToEntity(PurReconciliationItemDto dto) {
        PurReconciliationItem item = new PurReconciliationItem();
        BeanUtils.copyProperties(dto, item);
        return item;
    }
    
    /**
     * 获取状态名称
     */
    private String getStatusName(Integer status) {
        if (status == null) return "";
        switch (status) {
            case 1: return "草稿";
            case 2: return "待确认";
            case 3: return "已确认";
            case 4: return "已结算";
            default: return "";
        }
    }
    
    /**
     * 获取单据类型名称
     */
    private String getBillTypeName(Integer billType) {
        if (billType == null) return "";
        switch (billType) {
            case 1: return "采购单";
            case 2: return "入库单";
            case 3: return "退货单";
            default: return "";
        }
    }

    /**
     * 分页查询对账单列表（新方法，与Controller匹配）
     */
    @Override
    public Page<PurReconciliationDto> findAll(Long supplierId, Integer status, 
                                            String reconciliationNo, String startDate, 
                                            String endDate, Pageable pageable) {
        // 转换字符串日期为LocalDate
        LocalDate start = null;
        LocalDate end = null;
        if (startDate != null && !startDate.isEmpty()) {
            start = LocalDate.parse(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            end = LocalDate.parse(endDate);
        }
        
        // 调用原有方法
        return findReconciliationPage(supplierId, status, reconciliationNo, start, end, 
                                    pageable.getPageNumber(), pageable.getPageSize());
    }
    
    /**
     * 根据条件查询采购订单明细
     */
    @Override
    public Page<Map<String, Object>> findPurchaseItems(Long supplierId, String startDate, 
                                                      String endDate, Pageable pageable) {
        // 这里应该查询采购订单明细，包含商品信息
        // 暂时返回空结果，需要根据实际的采购订单表结构来实现
        List<Map<String, Object>> items = new ArrayList<>();
        
        // TODO: 实现查询采购订单明细的逻辑
        // 应该关联采购订单表、采购明细表、商品表等
        // SELECT po.order_no, poi.goods_code, g.goods_name, g.specification, g.unit,
        //        poi.quantity, poi.unit_price, poi.total_amount, po.order_date, poi.remark
        // FROM pur_order po
        // JOIN pur_order_item poi ON po.id = poi.order_id  
        // LEFT JOIN goods g ON poi.goods_id = g.id
        // WHERE po.supplier_id = ? AND po.order_date BETWEEN ? AND ?
        
        return new PageImpl<>(items, pageable, 0);
    }
    
    /**
     * 创建对账单（新方法）
     */
    @Override
    public PurReconciliationDto create(PurReconciliationDto reconciliationDto) {
        return createReconciliation(reconciliationDto);
    }
    
    /**
     * 更新对账单（新方法）
     */
    @Override
    public PurReconciliationDto update(PurReconciliationDto reconciliationDto) {
        return updateReconciliation(reconciliationDto.getId(), reconciliationDto);
    }
    
    /**
     * 删除对账单（新方法）
     */
    @Override
    public void delete(Long id) {
        deleteReconciliation(id);
    }
    
    /**
     * 确认对账单（新方法）
     */
    @Override
    public void confirm(Long id) {
        confirmReconciliation(id);
    }
    
    /**
     * 结算对账单（新方法）
     */
    @Override
    public void settle(Long id) {
        settleReconciliation(id);
    }
} 