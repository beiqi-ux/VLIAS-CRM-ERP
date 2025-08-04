-- 更新商品SKU表结构，添加缺失的字段
-- 请在MySQL中执行此脚本

USE vliascrm;

-- 检查并添加sku_name字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'vliascrm' 
     AND TABLE_NAME = 'prod_sku' 
     AND COLUMN_NAME = 'sku_name') = 0,
    'ALTER TABLE prod_sku ADD COLUMN sku_name VARCHAR(100) NOT NULL DEFAULT '''' COMMENT ''SKU名称''',
    'SELECT ''sku_name already exists'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加sale_qty字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'vliascrm' 
     AND TABLE_NAME = 'prod_sku' 
     AND COLUMN_NAME = 'sale_qty') = 0,
    'ALTER TABLE prod_sku ADD COLUMN sale_qty INT DEFAULT 0 COMMENT ''已售数量''',
    'SELECT ''sale_qty already exists'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加weight字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'vliascrm' 
     AND TABLE_NAME = 'prod_sku' 
     AND COLUMN_NAME = 'weight') = 0,
    'ALTER TABLE prod_sku ADD COLUMN weight DECIMAL(10,2) COMMENT ''重量(kg)''',
    'SELECT ''weight already exists'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加volume字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'vliascrm' 
     AND TABLE_NAME = 'prod_sku' 
     AND COLUMN_NAME = 'volume') = 0,
    'ALTER TABLE prod_sku ADD COLUMN volume DECIMAL(10,2) COMMENT ''体积(cm³)''',
    'SELECT ''volume already exists'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加barcode字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'vliascrm' 
     AND TABLE_NAME = 'prod_sku' 
     AND COLUMN_NAME = 'barcode') = 0,
    'ALTER TABLE prod_sku ADD COLUMN barcode VARCHAR(50) COMMENT ''条形码''',
    'SELECT ''barcode already exists'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加sort字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'vliascrm' 
     AND TABLE_NAME = 'prod_sku' 
     AND COLUMN_NAME = 'sort') = 0,
    'ALTER TABLE prod_sku ADD COLUMN sort INT DEFAULT 0 COMMENT ''排序''',
    'SELECT ''sort already exists'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加remark字段
SET @sql = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE TABLE_SCHEMA = 'vliascrm' 
     AND TABLE_NAME = 'prod_sku' 
     AND COLUMN_NAME = 'remark') = 0,
    'ALTER TABLE prod_sku ADD COLUMN remark VARCHAR(500) COMMENT ''备注''',
    'SELECT ''remark already exists'' as message'
));
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 更新现有记录的sku_name字段（如果为空）
UPDATE prod_sku 
SET sku_name = CONCAT('SKU-', IFNULL(sku_code, id)) 
WHERE sku_name = '' OR sku_name IS NULL;

-- 显示表结构确认
DESCRIBE prod_sku; 