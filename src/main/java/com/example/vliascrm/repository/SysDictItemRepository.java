package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysDictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 数据字典项数据访问接口
 */
@Repository
public interface SysDictItemRepository extends JpaRepository<SysDictItem, Long>, JpaSpecificationExecutor<SysDictItem> {
    
    /**
     * 根据字典ID查找字典项列表
     * @param dictId 字典ID
     * @return 字典项列表
     */
    List<SysDictItem> findByDictIdOrderBySortAsc(Long dictId);
    
    /**
     * 根据字典ID和状态查找字典项列表
     * @param dictId 字典ID
     * @param status 状态
     * @return 字典项列表
     */
    List<SysDictItem> findByDictIdAndStatusOrderBySortAsc(Long dictId, Integer status);
    
    /**
     * 根据字典ID和字典项值查找字典项
     * @param dictId 字典ID
     * @param itemValue 字典项值
     * @return 字典项
     */
    Optional<SysDictItem> findByDictIdAndItemValue(Long dictId, String itemValue);
    
    /**
     * 检查字典项值是否存在于指定字典中
     * @param dictId 字典ID
     * @param itemValue 字典项值
     * @return 是否存在
     */
    boolean existsByDictIdAndItemValue(Long dictId, String itemValue);
    
    /**
     * 根据字典ID删除所有字典项
     * @param dictId 字典ID
     * @return 影响行数
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM SysDictItem d WHERE d.dictId = ?1")
    int deleteByDictId(Long dictId);
    
    /**
     * 统计指定字典的字典项数量
     * @param dictId 字典ID
     * @return 字典项数量
     */
    long countByDictId(Long dictId);
} 