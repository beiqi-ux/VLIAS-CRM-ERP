-- 为pur_supplier表添加supplier_type字段
-- 供应商业务类型：1-镜框供应商 2-镜片供应商 3-配件供应商 4-设备供应商 5-其他

ALTER TABLE pur_supplier 
ADD COLUMN supplier_type INT COMMENT '供应商业务类型 1-镜框供应商 2-镜片供应商 3-配件供应商 4-设备供应商 5-其他'; 