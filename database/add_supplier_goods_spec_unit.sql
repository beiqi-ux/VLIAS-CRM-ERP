-- 为供应商商品表添加规格和单位字段
-- 创建时间: 2024-01-XX
-- 说明: 补充缺失的规格和单位字段，确保与前端代码的兼容性

-- 添加规格字段
ALTER TABLE pur_supplier_goods 
ADD COLUMN specification VARCHAR(200) COMMENT '商品规格' AFTER supplier_goods_name;

-- 添加单位字段  
ALTER TABLE pur_supplier_goods 
ADD COLUMN unit VARCHAR(20) COMMENT '计量单位' AFTER specification;

-- 创建索引以提高查询性能
CREATE INDEX idx_specification ON pur_supplier_goods(specification);
CREATE INDEX idx_unit ON pur_supplier_goods(unit);

-- 更新现有记录，从关联的商品表获取单位信息（如果存在）
UPDATE pur_supplier_goods sg 
LEFT JOIN prod_goods g ON sg.goods_id = g.id 
SET sg.unit = g.unit 
WHERE sg.unit IS NULL AND g.unit IS NOT NULL; 
-- 创建时间: 2024-01-XX
-- 说明: 补充缺失的规格和单位字段，确保与前端代码的兼容性

-- 添加规格字段
ALTER TABLE pur_supplier_goods 
ADD COLUMN specification VARCHAR(200) COMMENT '商品规格' AFTER supplier_goods_name;

-- 添加单位字段  
ALTER TABLE pur_supplier_goods 
ADD COLUMN unit VARCHAR(20) COMMENT '计量单位' AFTER specification;

-- 创建索引以提高查询性能
CREATE INDEX idx_specification ON pur_supplier_goods(specification);
CREATE INDEX idx_unit ON pur_supplier_goods(unit);

-- 更新现有记录，从关联的商品表获取单位信息（如果存在）
UPDATE pur_supplier_goods sg 
LEFT JOIN prod_goods g ON sg.goods_id = g.id 
SET sg.unit = g.unit 
WHERE sg.unit IS NULL AND g.unit IS NOT NULL; 