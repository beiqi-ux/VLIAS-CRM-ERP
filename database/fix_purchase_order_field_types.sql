-- 修复采购相关表的字段类型问题
-- 执行前请先备份数据库

-- 1. 修复采购入库单明细表 (已完成)
-- ALTER TABLE pur_receipt_item MODIFY COLUMN quantity DECIMAL(10,3) NOT NULL COMMENT '入库数量';

-- 2. 修复采购订单明细表
-- 备份采购订单明细表数据
-- CREATE TABLE pur_order_item_backup AS SELECT * FROM pur_order_item;

-- 修复采购订单明细表的数量字段类型
ALTER TABLE pur_order_item MODIFY COLUMN quantity DECIMAL(10,3) NOT NULL COMMENT '采购数量';
ALTER TABLE pur_order_item MODIFY COLUMN received_qty DECIMAL(10,3) DEFAULT 0 COMMENT '已入库数量';
ALTER TABLE pur_order_item MODIFY COLUMN remain_qty DECIMAL(10,3) DEFAULT 0 COMMENT '待入库数量';

-- 3. 修复采购退货明细表
-- 备份采购退货明细表数据
-- CREATE TABLE pur_return_item_backup AS SELECT * FROM pur_return_item;

-- 修复采购退货明细表的数量字段类型
ALTER TABLE pur_return_item MODIFY COLUMN quantity DECIMAL(10,3) NOT NULL COMMENT '退货数量';

-- 验证修改结果
SHOW COLUMNS FROM pur_order_item WHERE Field IN ('quantity', 'received_qty', 'remain_qty');
SHOW COLUMNS FROM pur_return_item WHERE Field = 'quantity'; 