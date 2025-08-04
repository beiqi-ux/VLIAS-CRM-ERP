-- SKU初始化数据
-- 清空SKU表
DELETE FROM prod_sku WHERE id > 0;

-- 重置自增ID
ALTER TABLE prod_sku AUTO_INCREMENT = 1;

-- 插入测试SKU数据
INSERT INTO prod_sku (goods_id, sku_code, sku_name, spec_values, selling_price, cost_price, original_price, min_price, stock_qty, warn_stock, sale_qty, weight, volume, sku_image, barcode, status, sort, remark, create_time, update_time, create_by, update_by, is_deleted) VALUES
-- iPhone 14 SKU
(1, 'IP14-128-BLK', 'iPhone 14 128GB 黑色', '{"颜色":"黑色","存储":"128GB"}', 5999.00, 4500.00, 6299.00, 5500.00, 50, 10, 0, 0.17, 100.50, '/images/sku/ip14-black.jpg', '1234567890123', 1, 1, '热销款', NOW(), NOW(), 1, 1, 0),
(1, 'IP14-128-WHT', 'iPhone 14 128GB 白色', '{"颜色":"白色","存储":"128GB"}', 5999.00, 4500.00, 6299.00, 5500.00, 30, 10, 0, 0.17, 100.50, '/images/sku/ip14-white.jpg', '1234567890124', 1, 2, '经典白色', NOW(), NOW(), 1, 1, 0),
(1, 'IP14-256-BLK', 'iPhone 14 256GB 黑色', '{"颜色":"黑色","存储":"256GB"}', 6999.00, 5200.00, 7299.00, 6500.00, 25, 5, 0, 0.17, 100.50, '/images/sku/ip14-256-black.jpg', '1234567890125', 1, 3, '大容量版本', NOW(), NOW(), 1, 1, 0),

-- MacBook Pro SKU  
(2, 'MBP-M2-SLV', 'MacBook Pro M2 银色', '{"颜色":"银色","芯片":"M2"}', 12999.00, 9500.00, 13999.00, 12000.00, 15, 3, 0, 1.40, 2000.00, '/images/sku/mbp-m2-silver.jpg', '2234567890123', 1, 1, '专业版', NOW(), NOW(), 1, 1, 0),
(2, 'MBP-M2-GRY', 'MacBook Pro M2 深空灰', '{"颜色":"深空灰","芯片":"M2"}', 12999.00, 9500.00, 13999.00, 12000.00, 20, 3, 0, 1.40, 2000.00, '/images/sku/mbp-m2-gray.jpg', '2234567890124', 1, 2, '商务版', NOW(), NOW(), 1, 1, 0),

-- AirPods SKU
(3, 'AP3-WHT', 'AirPods 3代 白色', '{"颜色":"白色","代数":"3代"}', 1399.00, 800.00, 1599.00, 1200.00, 100, 20, 0, 0.04, 50.00, '/images/sku/ap3-white.jpg', '3234567890123', 1, 1, '音质升级版', NOW(), NOW(), 1, 1, 0),
(3, 'APPRO-WHT', 'AirPods Pro 白色', '{"颜色":"白色","型号":"Pro"}', 1999.00, 1200.00, 2299.00, 1800.00, 80, 15, 0, 0.06, 60.00, '/images/sku/appro-white.jpg', '3234567890124', 1, 2, '降噪版', NOW(), NOW(), 1, 1, 0),

-- iPad SKU
(4, 'IPAD-64-SLV', 'iPad 64GB 银色', '{"颜色":"银色","存储":"64GB"}', 2499.00, 1800.00, 2799.00, 2200.00, 40, 8, 0, 0.49, 300.00, '/images/sku/ipad-64-silver.jpg', '4234567890123', 1, 1, '入门版', NOW(), NOW(), 1, 1, 0),
(4, 'IPAD-256-SLV', 'iPad 256GB 银色', '{"颜色":"银色","存储":"256GB"}', 3299.00, 2400.00, 3599.00, 3000.00, 25, 5, 0, 0.49, 300.00, '/images/sku/ipad-256-silver.jpg', '4234567890124', 1, 2, '大容量版', NOW(), NOW(), 1, 1, 0),

-- Apple Watch SKU
(5, 'AW-41-BLK', 'Apple Watch 41mm 黑色', '{"尺寸":"41mm","颜色":"黑色"}', 2799.00, 2000.00, 3099.00, 2500.00, 60, 12, 0, 0.03, 20.00, '/images/sku/aw-41-black.jpg', '5234567890123', 1, 1, '标准版', NOW(), NOW(), 1, 1, 0),
(5, 'AW-45-BLK', 'Apple Watch 45mm 黑色', '{"尺寸":"45mm","颜色":"黑色"}', 3299.00, 2400.00, 3599.00, 3000.00, 35, 8, 0, 0.04, 25.00, '/images/sku/aw-45-black.jpg', '5234567890124', 1, 2, '大屏版', NOW(), NOW(), 1, 1, 0); 