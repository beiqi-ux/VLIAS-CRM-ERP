-- 修复采购订单管理相关表中的数量字段类型
-- 从 INT 改为 DECIMAL(10,3) 以支持小数数量

-- 1. 修改 pur_order_item 表的数量相关字段
ALTER TABLE pur_order_item 
MODIFY COLUMN quantity DECIMAL(10,3) NOT NULL COMMENT '采购数量',
MODIFY COLUMN received_qty DECIMAL(10,3) DEFAULT 0 COMMENT '已入库数量',
MODIFY COLUMN remain_qty DECIMAL(10,3) DEFAULT 0 COMMENT '未入库数量';

-- 2. 修改 pur_receipt_item 表的数量字段
ALTER TABLE pur_receipt_item 
MODIFY COLUMN quantity DECIMAL(10,3) NOT NULL COMMENT '入库数量';

-- 3. 修改 pur_return_item 表的数量字段
ALTER TABLE pur_return_item 
MODIFY COLUMN quantity DECIMAL(10,3) NOT NULL COMMENT '退货数量';

-- 4. 更新现有数据，确保数量字段的一致性
-- 重新计算采购订单明细的未入库数量
UPDATE pur_order_item 
SET remain_qty = quantity - IFNULL(received_qty, 0) 
WHERE remain_qty != (quantity - IFNULL(received_qty, 0)); 