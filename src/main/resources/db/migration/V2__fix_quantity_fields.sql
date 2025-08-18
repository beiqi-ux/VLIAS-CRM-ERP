-- 修复采购退货明细表quantity字段类型
-- 从 INT 改为 DECIMAL(10,3) 以支持小数数量

-- 1. 修改 pur_return_item 表的 quantity 字段
ALTER TABLE pur_return_item 
MODIFY COLUMN quantity DECIMAL(10,3) NOT NULL COMMENT '退货数量';

-- 2. 如果存在其他相关的数量字段也需要修改，可以在这里添加
-- 例如：如果有库存相关表也需要修改数量字段类型 