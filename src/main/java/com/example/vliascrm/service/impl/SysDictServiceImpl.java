package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.SysDict;
import com.example.vliascrm.repository.SysDictRepository;
import com.example.vliascrm.service.SysDictService;
import com.example.vliascrm.service.SysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 数据字典服务实现类
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictRepository sysDictRepository;

    @Autowired
    private SysDictItemService sysDictItemService;

    @Override
    public Optional<SysDict> findById(Long id) {
        return sysDictRepository.findById(id);
    }

    @Override
    public Optional<SysDict> findByDictCode(String dictCode) {
        return sysDictRepository.findByDictCode(dictCode);
    }

    @Override
    public List<SysDict> findAll() {
        return sysDictRepository.findAll();
    }

    @Override
    public List<SysDict> findByStatus(Integer status) {
        return sysDictRepository.findByStatusOrderByCreateTimeDesc(status);
    }

    @Override
    public Page<SysDict> findAll(Specification<SysDict> specification, Pageable pageable) {
        return sysDictRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public SysDict save(SysDict dict) {
        return sysDictRepository.save(dict);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // 删除字典时同时删除字典项
        sysDictItemService.deleteByDictId(id);
        sysDictRepository.deleteById(id);
    }

    @Override
    public boolean existsByDictCode(String dictCode) {
        return sysDictRepository.existsByDictCode(dictCode);
    }

    @Override
    public boolean existsByDictCodeAndIdNot(String dictCode, Long excludeId) {
        Optional<SysDict> dict = sysDictRepository.findByDictCode(dictCode);
        return dict.isPresent() && !dict.get().getId().equals(excludeId);
    }

    @Override
    @Transactional
    public SysDict updateStatus(Long id, Integer status) {
        Optional<SysDict> dictOpt = sysDictRepository.findById(id);
        if (dictOpt.isPresent()) {
            SysDict dict = dictOpt.get();
            dict.setStatus(status);
            return sysDictRepository.save(dict);
        }
        throw new RuntimeException("字典不存在: " + id);
    }
} 