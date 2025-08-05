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
public class SkuInitServiceImpl implements SkuInitService {  // 移除 CommandLineRunner

    private final ProdSkuRepository skuRepository;
    private final JdbcTemplate jdbcTemplate;

    // 注释掉自动初始化，等待手动导入准确数据
    // @Override
    // public void run(String... args) throws Exception {
    //     // 检查是否需要初始化SKU数据
    //     if (skuRepository.count() == 0) {
    //         log.info("检测到SKU表为空，开始初始化SKU数据...");
    //         initSkus();
    //         log.info("SKU数据初始化完成");
    //     } else {
    //         log.info("SKU数据已存在，跳过初始化");
    //     }
    // }

    @Override
    public void initSkus() {
        try {
            // 读取SQL文件
            ClassPathResource resource = new ClassPathResource("data/init-sku.sql");
            StringBuilder sqlContent = new StringBuilder();
            
            try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // 跳过注释行和空行
                    if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                        sqlContent.append(line).append(" ");
                    }
                }
            }
            
            // 按分号分割SQL语句
            String[] sqlStatements = sqlContent.toString().split(";");
            
            // 执行SQL语句
            for (String sql : sqlStatements) {
                String trimmedSql = sql.trim();
                if (!trimmedSql.isEmpty()) {
                    jdbcTemplate.execute(trimmedSql);
                }
            }
            
            log.info("SKU初始化SQL执行完成");
        } catch (Exception e) {
            log.error("SKU初始化失败", e);
        }
    }
} 