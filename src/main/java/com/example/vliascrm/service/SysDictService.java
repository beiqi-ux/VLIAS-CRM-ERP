package com.example.vliascrm.service;

import com.example.vliascrm.entity.SysDict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

/**
 * 数据字典服务接口
 */
public interface SysDictService {

    /**
     * 根据ID查询字典
     *
     * @param id 字典ID
     * @return 字典对象
     */
    Optional<SysDict> findById(Long id);

    /**
     * 根据字典编码查询字典
     *
     * @param dictCode 字典编码
     * @return 字典对象
     */
    Optional<SysDict> findByDictCode(String dictCode);

    /**
     * 查询所有字典
     *
     * @return 字典列表
     */
    List<SysDict> findAll();

    /**
     * 根据状态查询字典列表
     *
     * @param status 状态
     * @return 字典列表
     */
    List<SysDict> findByStatus(Integer status);

    /**
     * 分页查询字典
     *
     * @param specification 查询条件
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<SysDict> findAll(Specification<SysDict> specification, Pageable pageable);

    /**
     * 保存字典
     *
     * @param dict 字典对象
     * @return 保存后的字典对象
     */
    SysDict save(SysDict dict);

    /**
     * 删除字典
     *
     * @param id 字典ID
     */
    void deleteById(Long id);

    /**
     * 检查字典编码是否存在
     *
     * @param dictCode 字典编码
     * @return 是否存在
     */
    boolean existsByDictCode(String dictCode);

    /**
     * 检查字典编码是否存在（排除指定ID）
     *
     * @param dictCode 字典编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean existsByDictCodeAndIdNot(String dictCode, Long excludeId);

    /**
     * 更新字典状态
     *
     * @param id 字典ID
     * @param status 状态
     * @return 更新后的字典对象
     */
    SysDict updateStatus(Long id, Integer status);
} 