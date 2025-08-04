-- 简化版本：直接更新商品SKU表结构
-- 如果字段已存在会报错，但不影响后续操作

USE vliascrm;

-- 添加缺失的字段（如果字段已存在会报错，请忽略）
ALTER TABLE prod_sku ADD COLUMN sku_name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'SKU名称';
ALTER TABLE prod_sku ADD COLUMN sale_qty INT DEFAULT 0 COMMENT '已售数量';
ALTER TABLE prod_sku ADD COLUMN weight DECIMAL(10,2) COMMENT '重量(kg)';
ALTER TABLE prod_sku ADD COLUMN volume DECIMAL(10,2) COMMENT '体积(cm³)';
ALTER TABLE prod_sku ADD COLUMN barcode VARCHAR(50) COMMENT '条形码';
ALTER TABLE prod_sku ADD COLUMN sort INT DEFAULT 0 COMMENT '排序';
ALTER TABLE prod_sku ADD COLUMN remark VARCHAR(500) COMMENT '备注';

-- 更新现有记录的sku_name字段
UPDATE prod_sku 
SET sku_name = CONCAT('SKU-', IFNULL(sku_code, id)) 
WHERE sku_name = '' OR sku_name IS NULL;

-- 查看表结构
DESCRIBE prod_sku; 