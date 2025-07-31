package com.example.vliascrm.repository;

import com.example.vliascrm.entity.SysDict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 数据字典数据访问接口
 */
@Repository
public interface SysDictRepository extends JpaRepository<SysDict, Long>, JpaSpecificationExecutor<SysDict> {
    
    /**
     * 根据字典编码查找字典
     * @param dictCode 字典编码
     * @return 字典对象
     */
    Optional<SysDict> findByDictCode(String dictCode);
    
    /**
     * 根据字典编码和状态查找字典
     * @param dictCode 字典编码
     * @param status 状态
     * @return 字典对象
     */
    Optional<SysDict> findByDictCodeAndStatus(String dictCode, Integer status);
    
    /**
     * 检查字典编码是否存在
     * @param dictCode 字典编码
     * @return 是否存在
     */
    boolean existsByDictCode(String dictCode);
    
    /**
     * 根据状态查找字典列表
     * @param status 状态
     * @return 字典列表
     */
    List<SysDict> findByStatusOrderByCreateTimeDesc(Integer status);
    
    /**
     * 查找所有启用的字典
     * @return 字典列表
     */
    List<SysDict> findByStatusOrderByDictName(Integer status);
} 