package com.example.vliascrm.service.impl;

import com.example.vliascrm.entity.SysDict;
import com.example.vliascrm.entity.SysDictItem;
import com.example.vliascrm.service.SysDictService;
import com.example.vliascrm.service.SysDictItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * 基础数据字典初始化服务
 * 只初始化通用状态和性别两个基础字典
 */
@Slf4j
@Service
public class BasicDictInitService implements ApplicationRunner {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysDictItemService sysDictItemService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("开始初始化基础数据字典...");
        
        try {
            initBasicDicts();
            log.info("基础数据字典初始化完成");
        } catch (Exception e) {
            log.error("基础数据字典初始化失败", e);
        }
    }

    /**
     * 初始化基础数据字典
     */
    private void initBasicDicts() {
        // 1. 通用状态字典
        initCommonStatusDict();
        
        // 2. 性别字典
        initGenderDict();
    }

    /**
     * 初始化通用状态字典
     */
    private void initCommonStatusDict() {
        String dictCode = "common_status";
        if (sysDictService.findByDictCode(dictCode).isPresent()) {
            return;
        }

        SysDict dict = new SysDict();
        dict.setDictName("通用状态");
        dict.setDictCode(dictCode);
        dict.setDescription("系统通用状态：启用/禁用");
        dict.setStatus(1);
        dict.setCreateTime(LocalDateTime.now());
        dict.setCreateBy("1");
        
        SysDict savedDict = sysDictService.save(dict);

        List<SysDictItem> items = Arrays.asList(
            createDictItem(savedDict.getId(), "禁用", "0", "禁用状态", 1),
            createDictItem(savedDict.getId(), "正常", "1", "正常状态", 2)
        );

        items.forEach(sysDictItemService::save);
        log.info("通用状态字典初始化完成");
    }

    /**
     * 初始化性别字典
     */
    private void initGenderDict() {
        String dictCode = "gender";
        if (sysDictService.findByDictCode(dictCode).isPresent()) {
            return;
        }

        SysDict dict = new SysDict();
        dict.setDictName("性别");
        dict.setDictCode(dictCode);
        dict.setDescription("通用性别字典，适用于用户、客户、联系人等所有需要性别信息的场景");
        dict.setStatus(1);
        dict.setCreateTime(LocalDateTime.now());
        dict.setCreateBy("1");
        
        SysDict savedDict = sysDictService.save(dict);

        List<SysDictItem> items = Arrays.asList(
            createDictItem(savedDict.getId(), "未知", "0", "性别未知或不愿透露", 1),
            createDictItem(savedDict.getId(), "男", "1", "男性", 2),
            createDictItem(savedDict.getId(), "女", "2", "女性", 3)
        );

        items.forEach(sysDictItemService::save);
        log.info("通用性别字典初始化完成");
    }

    /**
     * 创建字典项
     */
    private SysDictItem createDictItem(Long dictId, String itemText, String itemValue, String description, Integer sort) {
        SysDictItem item = new SysDictItem();
        item.setDictId(dictId);
        item.setItemText(itemText);
        item.setItemValue(itemValue);
        item.setDescription(description);
        item.setSort(sort);
        item.setStatus(1);
        item.setCreateTime(LocalDateTime.now());
        item.setCreateBy("1");
        return item;
    }
} 