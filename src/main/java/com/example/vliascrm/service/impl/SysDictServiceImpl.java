package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.SysDict;
import com.example.vliascrm.repository.SysDictRepository;
import com.example.vliascrm.service.CacheService;
import com.example.vliascrm.service.SysDictService;
import com.example.vliascrm.service.SysDictItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * 数据字典服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl implements SysDictService {

    private final SysDictRepository sysDictRepository;
    private final SysDictItemService sysDictItemService;
    private final CacheService cacheService;

    // 缓存键前缀
    private static final String DICT_CACHE_PREFIX = "dict:";
    private static final String DICT_BY_CODE_PREFIX = "dict:code:";
    private static final String DICT_LIST_PREFIX = "dict:list:";
    
    // 缓存过期时间：1小时
    private static final Duration DICT_CACHE_TTL = Duration.ofHours(1);

    @Override
    public Optional<SysDict> findById(Long id) {
        String cacheKey = DICT_CACHE_PREFIX + id;
        
        // 先从缓存中查询
        SysDict cachedDict = cacheService.get(cacheKey, SysDict.class);
        if (cachedDict != null) {
            log.debug("字典缓存命中: id={}", id);
            return Optional.of(cachedDict);
        }
        
        // 缓存未命中，从数据库查询
        Optional<SysDict> dictOpt = sysDictRepository.findById(id);
        if (dictOpt.isPresent()) {
            cacheService.set(cacheKey, dictOpt.get(), DICT_CACHE_TTL);
            log.debug("字典信息已缓存: id={}", id);
        }
        
        return dictOpt;
    }

    @Override
    public Optional<SysDict> findByDictCode(String dictCode) {
        String cacheKey = DICT_BY_CODE_PREFIX + dictCode;
        
        // 先从缓存中查询
        SysDict cachedDict = cacheService.get(cacheKey, SysDict.class);
        if (cachedDict != null) {
            log.debug("字典缓存命中: dictCode={}", dictCode);
            return Optional.of(cachedDict);
        }
        
        // 缓存未命中，从数据库查询
        Optional<SysDict> dictOpt = sysDictRepository.findByDictCode(dictCode);
        if (dictOpt.isPresent()) {
            SysDict dict = dictOpt.get();
            cacheService.set(cacheKey, dict, DICT_CACHE_TTL);
            // 同时缓存按ID查询的版本
            cacheService.set(DICT_CACHE_PREFIX + dict.getId(), dict, DICT_CACHE_TTL);
            log.debug("字典信息已缓存: dictCode={}", dictCode);
        }
        
        return dictOpt;
    }

    @Override
    public List<SysDict> findAll() {
        String cacheKey = DICT_LIST_PREFIX + "all";
        
        // 先从缓存中查询
        @SuppressWarnings("unchecked")
        List<SysDict> cachedList = cacheService.get(cacheKey, List.class);
        if (cachedList != null) {
            log.debug("字典列表缓存命中: all");
            return cachedList;
        }
        
        // 缓存未命中，从数据库查询
        List<SysDict> dictList = sysDictRepository.findAll();
        cacheService.set(cacheKey, dictList, DICT_CACHE_TTL);
        log.debug("字典列表已缓存: all, size={}", dictList.size());
        
        return dictList;
    }

    @Override
    public List<SysDict> findByStatus(Integer status) {
        String cacheKey = DICT_LIST_PREFIX + "status:" + status;
        
        // 先从缓存中查询
        @SuppressWarnings("unchecked")
        List<SysDict> cachedList = cacheService.get(cacheKey, List.class);
        if (cachedList != null) {
            log.debug("字典列表缓存命中: status={}", status);
            return cachedList;
        }
        
        // 缓存未命中，从数据库查询
        List<SysDict> dictList = sysDictRepository.findByStatusOrderByCreateTimeDesc(status);
        cacheService.set(cacheKey, dictList, DICT_CACHE_TTL);
        log.debug("字典列表已缓存: status={}, size={}", status, dictList.size());
        
        return dictList;
    }

    @Override
    public Page<SysDict> findAll(Specification<SysDict> specification, Pageable pageable) {
        return sysDictRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public SysDict save(SysDict dict) {
        SysDict savedDict = sysDictRepository.save(dict);
        
        // 清理相关缓存
        clearDictCache(savedDict.getId(), savedDict.getDictCode());
        
        return savedDict;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        // 先查询字典编码用于清理缓存
        Optional<SysDict> dictOpt = sysDictRepository.findById(id);
        String dictCode = dictOpt.map(SysDict::getDictCode).orElse(null);
        
        // 删除字典时同时删除字典项
        sysDictItemService.deleteByDictId(id);
        sysDictRepository.deleteById(id);
        
        // 清理相关缓存
        clearDictCache(id, dictCode);
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
            SysDict savedDict = sysDictRepository.save(dict);
            
            // 清理相关缓存
            clearDictCache(savedDict.getId(), savedDict.getDictCode());
            
            return savedDict;
        }
        throw new RuntimeException("字典不存在: " + id);
    }

    /**
     * 清理字典相关缓存
     */
    private void clearDictCache(Long dictId, String dictCode) {
        // 清理单个字典缓存
        if (dictId != null) {
            cacheService.delete(DICT_CACHE_PREFIX + dictId);
        }
        if (dictCode != null) {
            cacheService.delete(DICT_BY_CODE_PREFIX + dictCode);
        }
        
        // 清理列表缓存
        cacheService.deletePattern(DICT_LIST_PREFIX + "*");
        
        log.debug("字典缓存已清理: dictId={}, dictCode={}", dictId, dictCode);
    }
} 