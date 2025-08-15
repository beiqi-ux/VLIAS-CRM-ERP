package com.example.vliascrm.service;

import com.example.vliascrm.entity.SysDictItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * 数据字典项服务接口
 */
public interface SysDictItemService {

    /**
     * 根据ID查询字典项
     *
     * @param id 字典项ID
     * @return 字典项对象
     */
    Optional<SysDictItem> findById(Long id);

    /**
     * 根据字典ID查询字典项列表
     *
     * @param dictId 字典ID
     * @return 字典项列表
     */
    List<SysDictItem> findByDictId(Long dictId);

    /**
     * 根据字典ID和状态查询字典项列表
     *
     * @param dictId 字典ID
     * @param status 状态
     * @return 字典项列表
     */
    List<SysDictItem> findByDictIdAndStatus(Long dictId, Integer status);

    /**
     * 根据字典编码查询字典项列表
     *
     * @param dictCode 字典编码
     * @return 字典项列表
     */
    List<SysDictItem> findByDictCode(String dictCode);

    /**
     * 根据字典编码和状态查询字典项列表
     *
     * @param dictCode 字典编码
     * @param status 状态
     * @return 字典项列表
     */
    List<SysDictItem> findByDictCodeAndStatus(String dictCode, Integer status);

    /**
     * 分页查询字典项
     *
     * @param specification 查询条件
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<SysDictItem> findAll(Specification<SysDictItem> specification, Pageable pageable);

    /**
     * 保存字典项
     *
     * @param dictItem 字典项对象
     * @return 保存后的字典项对象
     */
    SysDictItem save(SysDictItem dictItem);

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     */
    void deleteById(Long id);

    /**
     * 根据字典ID删除所有字典项
     *
     * @param dictId 字典ID
     */
    void deleteByDictId(Long dictId);

    /**
     * 检查字典项值是否存在于指定字典中
     *
     * @param dictId 字典ID
     * @param itemValue 字典项值
     * @return 是否存在
     */
    boolean existsByDictIdAndItemValue(Long dictId, String itemValue);

    /**
     * 检查字典项值是否存在于指定字典中（排除指定ID）
     *
     * @param dictId 字典ID
     * @param itemValue 字典项值
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByDictIdAndItemValueAndIdNot(Long dictId, String itemValue, Long excludeId);

    /**
     * 更新字典项状态
     *
     * @param id 字典项ID
     * @param status 状态
     * @return 更新后的字典项对象
     */
    SysDictItem updateStatus(Long id, Integer status);
} 