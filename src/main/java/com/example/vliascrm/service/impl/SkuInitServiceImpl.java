package com.example.vliascrm.service.impl;

import com.example.vliascrm.repository.ProdSkuRepository;
import com.example.vliascrm.service.SkuInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SKU初始化服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SkuInitServiceImpl implements SkuInitService, CommandLineRunner {

    private final ProdSkuRepository skuRepository;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 检查是否需要初始化SKU数据
        if (skuRepository.count() == 0) {
            log.info("检测到SKU表为空，开始初始化SKU数据...");
            initSkus();
            log.info("SKU数据初始化完成");
        } else {
            log.info("SKU数据已存在，跳过初始化");
        }
    }

    @Override
    public void initSkus() {
        try {
            // 读取SQL文件
            ClassPathResource resource = new ClassPathResource("data/init-sku.sql");
            List<String> sqlLines = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)
            ).lines().collect(Collectors.toList());

            // 执行SQL语句
            for (String sql : sqlLines) {
                if (!sql.trim().isEmpty() && !sql.trim().startsWith("--")) {
                    jdbcTemplate.execute(sql);
                }
            }
            
            log.info("SKU初始化SQL执行完成");
        } catch (Exception e) {
            log.error("SKU初始化失败", e);
        }
    }
} 