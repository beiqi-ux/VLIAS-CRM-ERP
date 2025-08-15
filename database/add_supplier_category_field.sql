-- 为pur_supplier表添加supplier_category字段
-- 供应商分类：1-核心供应商 2-重要供应商 3-一般供应商 4-新增供应商 5-试用供应商

ALTER TABLE pur_supplier 
ADD COLUMN supplier_category INT COMMENT '供应商分类 1-核心供应商 2-重要供应商 3-一般供应商 4-新增供应商 5-试用供应商';

-- 可选：为现有数据设置默认值
-- UPDATE pur_supplier SET supplier_category = 3 WHERE supplier_category IS NULL; 