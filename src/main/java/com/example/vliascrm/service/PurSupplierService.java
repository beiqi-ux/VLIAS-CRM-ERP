package com.example.vliascrm.service;

import com.example.vliascrm.dto.PurSupplierDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 供应商服务接口
 */
public interface PurSupplierService {

    /**
     * 分页查询供应商列表
     * @param supplierName 供应商名称
     * @param contact 联系人
     * @param status 状态
     * @param supplierType 供应商类型
     * @param pageable 分页参数
     * @return 供应商分页数据
     */
    Page<PurSupplierDto> getSupplierPage(String supplierName, String contact, Integer status, Integer supplierType, Pageable pageable);

    /**
     * 根据ID查询供应商详情
     * @param id 供应商ID
     * @return 供应商详情
     */
    PurSupplierDto getSupplierById(Long id);

    /**
     * 创建供应商
     * @param supplierDto 供应商信息
     * @return 创建的供应商
     */
    PurSupplierDto createSupplier(PurSupplierDto supplierDto);

    /**
     * 更新供应商
     * @param id 供应商ID
     * @param supplierDto 供应商信息
     * @return 更新的供应商
     */
    PurSupplierDto updateSupplier(Long id, PurSupplierDto supplierDto);

    /**
     * 删除供应商
     * @param id 供应商ID
     */
    void deleteSupplier(Long id);

    /**
     * 批量删除供应商
     * @param ids 供应商ID列表
     */
    void deleteSuppliers(List<Long> ids);

    /**
     * 更新供应商状态
     * @param id 供应商ID
     * @param status 状态
     */
    void updateSupplierStatus(Long id, Integer status);

    /**
     * 查询所有正常状态的供应商
     * @return 供应商列表
     */
    List<PurSupplierDto> getAllActiveSuppliers();

    /**
     * 根据供应商编码查询供应商
     * @param supplierCode 供应商编码
     * @return 供应商信息
     */
    PurSupplierDto getSupplierByCode(String supplierCode);

    /**
     * 检查供应商编码是否存在
     * @param supplierCode 供应商编码
     * @param excludeId 排除的供应商ID
     * @return 是否存在
     */
    boolean isSupplierCodeExists(String supplierCode, Long excludeId);

    /**
     * 检查供应商名称是否存在
     * @param supplierName 供应商名称
     * @param excludeId 排除的供应商ID
     * @return 是否存在
     */
    boolean isSupplierNameExists(String supplierName, Long excludeId);

    /**
     * 获取供应商统计信息
     * @return 统计信息
     */
    SupplierStatistics getSupplierStatistics();

    /**
     * 供应商统计信息
     */
    class SupplierStatistics {
        private Long totalCount;    // 总数
        private Long activeCount;   // 正常状态数量
        private Long inactiveCount; // 禁用状态数量

        // Getters and Setters
        public Long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(Long totalCount) {
            this.totalCount = totalCount;
        }

        public Long getActiveCount() {
            return activeCount;
        }

        public void setActiveCount(Long activeCount) {
            this.activeCount = activeCount;
        }

        public Long getInactiveCount() {
            return inactiveCount;
        }

        public void setInactiveCount(Long inactiveCount) {
            this.inactiveCount = inactiveCount;
        }
    }
} 