-- 重新整理规格值排序，让每个规格项下的值从1开始连续排序

-- 镜片宽 (item_id = 1)
UPDATE prod_specification_value SET sort_order = 1 WHERE id = 1; -- 45mm
UPDATE prod_specification_value SET sort_order = 2 WHERE id = 2; -- 50mm  
UPDATE prod_specification_value SET sort_order = 3 WHERE id = 3; -- 55mm
UPDATE prod_specification_value SET sort_order = 4 WHERE id = 4; -- 60mm
UPDATE prod_specification_value SET sort_order = 5 WHERE id = 5; -- 65mm
UPDATE prod_specification_value SET sort_order = 6 WHERE id = 6; -- 70mm

-- 镜总宽 (item_id = 2) - 已经正确，无需修改
-- UPDATE prod_specification_value SET sort_order = 1 WHERE id = 7; -- 140mm
-- UPDATE prod_specification_value SET sort_order = 2 WHERE id = 8; -- 145mm
-- UPDATE prod_specification_value SET sort_order = 3 WHERE id = 9; -- 150mm
-- UPDATE prod_specification_value SET sort_order = 4 WHERE id = 10; -- 155mm
-- UPDATE prod_specification_value SET sort_order = 5 WHERE id = 11; -- 160mm
-- UPDATE prod_specification_value SET sort_order = 6 WHERE id = 12; -- 165mm

-- 镜片高 (item_id = 3) - 已经正确，无需修改
-- UPDATE prod_specification_value SET sort_order = 1 WHERE id = 13; -- 40mm
-- UPDATE prod_specification_value SET sort_order = 2 WHERE id = 14; -- 45mm
-- UPDATE prod_specification_value SET sort_order = 3 WHERE id = 15; -- 50mm
-- UPDATE prod_specification_value SET sort_order = 4 WHERE id = 16; -- 55mm
-- UPDATE prod_specification_value SET sort_order = 5 WHERE id = 17; -- 60mm

-- 鼻间距 (item_id = 4) - 已经正确，无需修改
-- UPDATE prod_specification_value SET sort_order = 1 WHERE id = 18; -- 16mm
-- UPDATE prod_specification_value SET sort_order = 2 WHERE id = 19; -- 18mm
-- UPDATE prod_specification_value SET sort_order = 3 WHERE id = 20; -- 20mm
-- UPDATE prod_specification_value SET sort_order = 4 WHERE id = 21; -- 22mm
-- UPDATE prod_specification_value SET sort_order = 5 WHERE id = 22; -- 24mm

-- 镜腿长 (item_id = 5) - 已经正确，无需修改
-- UPDATE prod_specification_value SET sort_order = 1 WHERE id = 23; -- 135mm
-- UPDATE prod_specification_value SET sort_order = 2 WHERE id = 24; -- 140mm
-- UPDATE prod_specification_value SET sort_order = 3 WHERE id = 25; -- 145mm
-- UPDATE prod_specification_value SET sort_order = 4 WHERE id = 26; -- 150mm

-- 验证修改结果
SELECT 
    si.item_name,
    sv.value_name,
    sv.sort_order
FROM prod_specification_value sv 
JOIN prod_specification_item si ON sv.item_id = si.id 
ORDER BY si.item_name, sv.sort_order; 