-- 更新商品SKU表结构，添加缺失的字段
-- V2__update_prod_sku_table.sql

-- 添加SKU名称字段
ALTER TABLE prod_sku ADD COLUMN sku_name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'SKU名称';

-- 添加已售数量字段
ALTER TABLE prod_sku ADD COLUMN sale_qty INT DEFAULT 0 COMMENT '已售数量';

-- 添加重量字段
ALTER TABLE prod_sku ADD COLUMN weight DECIMAL(10,2) COMMENT '重量(kg)';

-- 添加体积字段
ALTER TABLE prod_sku ADD COLUMN volume DECIMAL(10,2) COMMENT '体积(cm³)';

-- 添加条形码字段
ALTER TABLE prod_sku ADD COLUMN barcode VARCHAR(50) COMMENT '条形码';

-- 添加排序字段
ALTER TABLE prod_sku ADD COLUMN sort INT DEFAULT 0 COMMENT '排序';

-- 添加备注字段
ALTER TABLE prod_sku ADD COLUMN remark VARCHAR(500) COMMENT '备注';

-- 更新现有记录的sku_name字段（使用商品名称作为默认值）
UPDATE prod_sku p 
SET sku_name = CONCAT('SKU-', p.sku_code) 
WHERE p.sku_name = '' OR p.sku_name IS NULL; 