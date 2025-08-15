package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.SysDict;
import com.example.vliascrm.entity.SysDictItem;
import com.example.vliascrm.repository.SysDictItemRepository;
import com.example.vliascrm.repository.SysDictRepository;
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
 * 数据字典项服务实现类
 */
@Service
public class SysDictItemServiceImpl implements SysDictItemService {

    @Autowired
    private SysDictItemRepository sysDictItemRepository;

    @Autowired
    private SysDictRepository sysDictRepository;

    @Override
    public Optional<SysDictItem> findById(Long id) {
        return sysDictItemRepository.findById(id);
    }

    @Override
    public List<SysDictItem> findByDictId(Long dictId) {
        return sysDictItemRepository.findByDictIdOrderBySortAsc(dictId);
    }

    @Override
    public List<SysDictItem> findByDictIdAndStatus(Long dictId, Integer status) {
        return sysDictItemRepository.findByDictIdAndStatusOrderBySortAsc(dictId, status);
    }

    @Override
    public List<SysDictItem> findByDictCode(String dictCode) {
        Optional<SysDict> dictOpt = sysDictRepository.findByDictCode(dictCode);
        if (dictOpt.isPresent()) {
            return sysDictItemRepository.findByDictIdOrderBySortAsc(dictOpt.get().getId());
        }
        return List.of();
    }

    @Override
    public List<SysDictItem> findByDictCodeAndStatus(String dictCode, Integer status) {
        Optional<SysDict> dictOpt = sysDictRepository.findByDictCodeAndStatus(dictCode, status);
        if (dictOpt.isPresent()) {
            return sysDictItemRepository.findByDictIdAndStatusOrderBySortAsc(dictOpt.get().getId(), status);
        }
        return List.of();
    }

    @Override
    public Page<SysDictItem> findAll(Specification<SysDictItem> specification, Pageable pageable) {
        return sysDictItemRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public SysDictItem save(SysDictItem dictItem) {
        return sysDictItemRepository.save(dictItem);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        sysDictItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByDictId(Long dictId) {
        sysDictItemRepository.deleteByDictId(dictId);
    }

    @Override
    public boolean existsByDictIdAndItemValue(Long dictId, String itemValue) {
        return sysDictItemRepository.existsByDictIdAndItemValue(dictId, itemValue);
    }

    @Override
    public boolean existsByDictIdAndItemValueAndIdNot(Long dictId, String itemValue, Long excludeId) {
        Optional<SysDictItem> dictItem = sysDictItemRepository.findByDictIdAndItemValue(dictId, itemValue);
        return dictItem.isPresent() && !dictItem.get().getId().equals(excludeId);
    }

    @Override
    @Transactional
    public SysDictItem updateStatus(Long id, Integer status) {
        Optional<SysDictItem> dictItemOpt = sysDictItemRepository.findById(id);
        if (dictItemOpt.isPresent()) {
            SysDictItem dictItem = dictItemOpt.get();
            dictItem.setStatus(status);
            return sysDictItemRepository.save(dictItem);
        }
        throw new RuntimeException("字典项不存在: " + id);
    }
} 