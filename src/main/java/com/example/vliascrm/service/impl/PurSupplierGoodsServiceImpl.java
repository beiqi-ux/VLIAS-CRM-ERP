package com.example.vliascrm.service.impl;

import com.example.vliascrm.dto.PurSupplierGoodsDto;
import com.example.vliascrm.dto.SupplierPriceCompareDto;
import com.example.vliascrm.entity.PurSupplier;
import com.example.vliascrm.entity.PurSupplierGoods;
import com.example.vliascrm.repository.PurSupplierGoodsRepository;
import com.example.vliascrm.repository.PurSupplierRepository;
import com.example.vliascrm.service.PurSupplierGoodsService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.persistence.criteria.Predicate;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.net.URLEncoder;

/**
 * 供应商商品服务实现类
 */
@Service
@Transactional
public class PurSupplierGoodsServiceImpl implements PurSupplierGoodsService {

    @Autowired
    private PurSupplierGoodsRepository supplierGoodsRepository;
    
    @Autowired
    private PurSupplierRepository supplierRepository;

    @Override
    public Page<PurSupplierGoodsDto> getSupplierGoodsPage(Long supplierId, Long goodsId, String supplierGoodsName, Pageable pageable) {
        // 使用 Specification 进行查询，返回完整实体
        Page<PurSupplierGoods> entityPage = supplierGoodsRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // 基本条件：未删除
            predicates.add(criteriaBuilder.equal(root.get("isDeleted"), 0));
            
            // 供应商ID过滤
            if (supplierId != null) {
                predicates.add(criteriaBuilder.equal(root.get("supplierId"), supplierId));
            }
            
            // 商品ID过滤
            if (goodsId != null) {
                predicates.add(criteriaBuilder.equal(root.get("goodsId"), goodsId));
            }
            
            // 供应商商品名称模糊查询
            if (supplierGoodsName != null && !supplierGoodsName.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("supplierGoodsName"), "%" + supplierGoodsName + "%"));
            }
            
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable);
        
        // 获取所有供应商ID
        Set<Long> supplierIds = entityPage.getContent().stream()
                .map(PurSupplierGoods::getSupplierId)
                .collect(Collectors.toSet());
        
        // 批量查询供应商信息
        Map<Long, PurSupplier> supplierMap = new HashMap<>();
        if (!supplierIds.isEmpty()) {
            List<PurSupplier> suppliers = supplierRepository.findAllById(supplierIds)
                    .stream()
                    .filter(supplier -> supplier.getIsDeleted() == 0)
                    .collect(Collectors.toList());
            supplierMap = suppliers.stream().collect(Collectors.toMap(PurSupplier::getId, supplier -> supplier));
        }
        
        // 转换为 DTO 并填充供应商信息
        final Map<Long, PurSupplier> finalSupplierMap = supplierMap;
        return entityPage.map(entity -> {
            PurSupplierGoodsDto dto = new PurSupplierGoodsDto();
            BeanUtils.copyProperties(entity, dto);
            
            // 填充供应商信息
            PurSupplier supplier = finalSupplierMap.get(entity.getSupplierId());
            if (supplier != null) {
                dto.setSupplierName(supplier.getSupplierName());
                dto.setSupplierCode(supplier.getSupplierCode());
            }
            
            return dto;
        });
    }

    @Override
    public PurSupplierGoodsDto getSupplierGoodsById(Long id) {
        PurSupplierGoods entity = supplierGoodsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("供应商商品关联不存在"));
        
        PurSupplierGoodsDto dto = new PurSupplierGoodsDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public PurSupplierGoodsDto createSupplierGoods(PurSupplierGoodsDto dto) {
        // 检查是否已存在相同的关联
        if (checkSupplierGoodsExists(dto.getSupplierId(), dto.getGoodsId(), dto.getSkuId())) {
            throw new RuntimeException("供应商商品关联已存在");
        }

        PurSupplierGoods entity = new PurSupplierGoods();
        BeanUtils.copyProperties(dto, entity);
        entity.setId(null);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsDeleted(0);
        
        PurSupplierGoods saved = supplierGoodsRepository.save(entity);
        
        PurSupplierGoodsDto result = new PurSupplierGoodsDto();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

    @Override
    public PurSupplierGoodsDto updateSupplierGoods(Long id, PurSupplierGoodsDto dto) {
        PurSupplierGoods entity = supplierGoodsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("供应商商品关联不存在"));

        // 复制属性（排除ID和时间字段）
        BeanUtils.copyProperties(dto, entity, "id", "createTime");
        entity.setUpdateTime(LocalDateTime.now());
        
        PurSupplierGoods saved = supplierGoodsRepository.save(entity);
        
        PurSupplierGoodsDto result = new PurSupplierGoodsDto();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

    @Override
    public void deleteSupplierGoods(Long id) {
        PurSupplierGoods entity = supplierGoodsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("供应商商品关联不存在"));
        
        entity.setIsDeleted(1);
        entity.setUpdateTime(LocalDateTime.now());
        supplierGoodsRepository.save(entity);
    }

    @Override
    public List<PurSupplierGoodsDto> getGoodsBySupplierId(Long supplierId) {
        List<PurSupplierGoods> entities = supplierGoodsRepository.findBySupplierId(supplierId);
        return entities.stream()
                .filter(entity -> entity.getIsDeleted() == 0)
                .map(entity -> {
                    PurSupplierGoodsDto dto = new PurSupplierGoodsDto();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PurSupplierGoodsDto> getSuppliersByGoodsId(Long goodsId) {
        return supplierGoodsRepository.findSuppliersByGoodsIdForCompare(goodsId);
    }

    @Override
    public SupplierPriceCompareDto comparePrice(Long goodsId) {
        List<PurSupplierGoodsDto> suppliers = getSuppliersByGoodsId(goodsId);
        
        SupplierPriceCompareDto compareDto = new SupplierPriceCompareDto();
        compareDto.setGoodsId(goodsId);
        
        // 转换为 SupplierPriceInfo 列表
        List<SupplierPriceCompareDto.SupplierPriceInfo> priceInfoList = suppliers.stream()
                .map(supplier -> {
                    SupplierPriceCompareDto.SupplierPriceInfo priceInfo = new SupplierPriceCompareDto.SupplierPriceInfo();
                    priceInfo.setSupplierId(supplier.getSupplierId());
                    priceInfo.setSupplierName(supplier.getSupplierName());
                    priceInfo.setSupplierCode(supplier.getSupplierCode());
                    priceInfo.setPurchasePrice(supplier.getPurchasePrice());
                    priceInfo.setMinPurchaseQty(supplier.getMinPurchaseQty());
                    priceInfo.setDeliveryDay(supplier.getDeliveryDay());
                    priceInfo.setIsRecommended(false);
                    return priceInfo;
                })
                .collect(Collectors.toList());
        
        compareDto.setSuppliers(priceInfoList);
        
        return compareDto;
    }

    @Override
    public List<PurSupplierGoodsDto> batchCreateSupplierGoods(List<PurSupplierGoodsDto> dtoList) {
        List<PurSupplierGoodsDto> results = new ArrayList<>();
        
        for (PurSupplierGoodsDto dto : dtoList) {
            try {
                PurSupplierGoodsDto created = createSupplierGoods(dto);
                results.add(created);
            } catch (Exception e) {
                // 记录错误但继续处理其他数据
                System.err.println("批量创建失败: " + e.getMessage());
            }
        }
        
        return results;
    }

    @Override
    public boolean checkSupplierGoodsExists(Long supplierId, Long goodsId, Long skuId) {
        return supplierGoodsRepository.existsBySupplierIdAndGoodsIdAndSkuId(supplierId, goodsId, skuId);
    }

    @Override
    public long getGoodsCountBySupplierId(Long supplierId) {
        return supplierGoodsRepository.countBySupplierId(supplierId);
    }

    @Override
    public long getSupplierCountByGoodsId(Long goodsId) {
        return supplierGoodsRepository.countByGoodsId(goodsId);
    }

    @Override
    public List<PurSupplierGoodsDto> getRecommendedSuppliers(Long goodsId, Integer quantity) {
        List<PurSupplierGoodsDto> allSuppliers = getSuppliersByGoodsId(goodsId);
        
        // 根据价格和最小采购量推荐
        return allSuppliers.stream()
                .filter(supplier -> supplier.getMinPurchaseQty() == null || 
                                  supplier.getMinPurchaseQty() <= quantity)
                .sorted(Comparator.comparing(PurSupplierGoodsDto::getPurchasePrice))
                .limit(3)
                .collect(Collectors.toList());
    }

    @Override
    public List<PurSupplierGoodsDto> searchSupplierGoods(String supplierGoodsName, Long supplierId) {
        return supplierGoodsRepository.findSupplierGoodsByGoodsName(supplierGoodsName, supplierId);
    }

    @Override
    public void exportSupplierGoods(Long supplierId, Long goodsId, String supplierGoodsName, HttpServletResponse response) throws IOException {
        // 获取数据
        List<PurSupplierGoodsDto> dataList = supplierGoodsRepository.findAllWithDetailsByConditionsForExport(
                supplierId, goodsId, supplierGoodsName);
        
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        
        // 防止中文乱码
        String fileName = URLEncoder.encode("供应商商品关联信息", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        
        // 使用Apache POI生成Excel文件
        try (Workbook workbook = new XSSFWorkbook(); OutputStream out = response.getOutputStream()) {
            Sheet sheet = workbook.createSheet("供应商商品关联");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] headers = {"供应商名称", "供应商商品编码", "供应商商品名称", 
                               "采购价格", "最小采购量", "供货周期(天)", "创建时间"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }
            
            // 填充数据
            int rowIndex = 1;
            for (PurSupplierGoodsDto item : dataList) {
                Row row = sheet.createRow(rowIndex++);
                
                row.createCell(0).setCellValue(item.getSupplierName() != null ? item.getSupplierName() : "");
                row.createCell(1).setCellValue(item.getSupplierGoodsCode() != null ? item.getSupplierGoodsCode() : "");
                row.createCell(2).setCellValue(item.getSupplierGoodsName() != null ? item.getSupplierGoodsName() : "");
                row.createCell(3).setCellValue(item.getPurchasePrice() != null ? item.getPurchasePrice().doubleValue() : 0.0);
                row.createCell(4).setCellValue(item.getMinPurchaseQty() != null ? item.getMinPurchaseQty() : 0);
                row.createCell(5).setCellValue(item.getDeliveryDay() != null ? item.getDeliveryDay() : 0);
                row.createCell(6).setCellValue(item.getCreateTime() != null ? item.getCreateTime().toString() : "");
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            workbook.write(out);
        }
    }

    @Override
    public Map<String, Object> importSupplierGoods(MultipartFile file) throws IOException {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            
            // 跳过标题行
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    PurSupplierGoodsDto dto = new PurSupplierGoodsDto();
                    
                    // 解析Excel行数据
                    dto.setSupplierId(getCellLongValue(row.getCell(0)));
                    dto.setGoodsId(getCellLongValue(row.getCell(1)));
                    dto.setSkuId(getCellLongValue(row.getCell(2)));
                    dto.setSupplierGoodsCode(getCellStringValue(row.getCell(3)));
                    dto.setSupplierGoodsName(getCellStringValue(row.getCell(4)));
                    dto.setPurchasePrice(getCellBigDecimalValue(row.getCell(5)));
                    dto.setMinPurchaseQty(getCellIntegerValue(row.getCell(6)));
                    dto.setDeliveryDay(getCellIntegerValue(row.getCell(7)));


                    createSupplierGoods(dto);
                    successCount++;
                    
                } catch (Exception e) {
                    failCount++;
                    errors.add("第" + (i + 1) + "行: " + e.getMessage());
                }
            }
        }

        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        
        return result;
    }

    @Override
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("供应商商品导入模板");

        // 创建标题行
        String[] headers = {"供应商ID", "供应商商品编码", 
                           "供应商商品名称", "采购价格", "最小采购量", "交货天数", "状态(1启用0禁用)"};
        
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"supplier_goods_template.xlsx\"");

        // 写入响应流
        try (OutputStream out = response.getOutputStream()) {
            workbook.write(out);
        } finally {
            workbook.close();
        }
    }

    // 辅助方法：获取单元格字符串值
    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return null;
        }
    }

    // 辅助方法：获取单元格Long值
    private Long getCellLongValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case NUMERIC:
                return (long) cell.getNumericCellValue();
            case STRING:
                try {
                    return Long.parseLong(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    // 辅助方法：获取单元格Integer值
    private Integer getCellIntegerValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    // 辅助方法：获取单元格BigDecimal值
    private BigDecimal getCellBigDecimalValue(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case STRING:
                try {
                    return new BigDecimal(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }
} 
