-- 批量插入镜框尺寸规格值到数据库
-- 执行前请确保已经创建了规格管理相关的表

USE vliascrm;

-- 镜片宽规格值 (50mm - 70mm，每5mm一个规格)
INSERT INTO prod_specification_value (item_id, value_code, value_name, numeric_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_width'), 'lens_width_50', '50mm', 50.00, 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_width'), 'lens_width_55', '55mm', 55.00, 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_width'), 'lens_width_60', '60mm', 60.00, 3),
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_width'), 'lens_width_65', '65mm', 65.00, 4),
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_width'), 'lens_width_70', '70mm', 70.00, 5);

-- 镜总宽规格值 (140mm - 165mm，每5mm一个规格)
INSERT INTO prod_specification_value (item_id, value_code, value_name, numeric_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_total_width'), 'total_width_140', '140mm', 140.00, 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_total_width'), 'total_width_145', '145mm', 145.00, 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_total_width'), 'total_width_150', '150mm', 150.00, 3),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_total_width'), 'total_width_155', '155mm', 155.00, 4),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_total_width'), 'total_width_160', '160mm', 160.00, 5),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_total_width'), 'total_width_165', '165mm', 165.00, 6);

-- 镜片高规格值 (40mm - 60mm，每5mm一个规格)
INSERT INTO prod_specification_value (item_id, value_code, value_name, numeric_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_height'), 'lens_height_40', '40mm', 40.00, 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_height'), 'lens_height_45', '45mm', 45.00, 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_height'), 'lens_height_50', '50mm', 50.00, 3),
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_height'), 'lens_height_55', '55mm', 55.00, 4),
((SELECT id FROM prod_specification_item WHERE item_code = 'lens_height'), 'lens_height_60', '60mm', 60.00, 5);

-- 鼻间距规格值 (16mm - 24mm，每2mm一个规格)
INSERT INTO prod_specification_value (item_id, value_code, value_name, numeric_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'bridge_width'), 'bridge_16', '16mm', 16.00, 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'bridge_width'), 'bridge_18', '18mm', 18.00, 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'bridge_width'), 'bridge_20', '20mm', 20.00, 3),
((SELECT id FROM prod_specification_item WHERE item_code = 'bridge_width'), 'bridge_22', '22mm', 22.00, 4),
((SELECT id FROM prod_specification_item WHERE item_code = 'bridge_width'), 'bridge_24', '24mm', 24.00, 5);

-- 镜腿长规格值 (135mm - 150mm，每5mm一个规格)
INSERT INTO prod_specification_value (item_id, value_code, value_name, numeric_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'temple_length'), 'temple_135', '135mm', 135.00, 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'temple_length'), 'temple_140', '140mm', 140.00, 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'temple_length'), 'temple_145', '145mm', 145.00, 3),
((SELECT id FROM prod_specification_item WHERE item_code = 'temple_length'), 'temple_150', '150mm', 150.00, 4);

-- 验证插入结果
SELECT 
    c.category_name,
    i.item_name,
    COUNT(v.id) as value_count,
    GROUP_CONCAT(v.value_name ORDER BY v.sort_order) as spec_values
FROM prod_specification_category c
JOIN prod_specification_item i ON c.id = i.category_id
LEFT JOIN prod_specification_value v ON i.id = v.item_id
WHERE c.category_code = 'size'
GROUP BY c.id, i.id
ORDER BY i.sort_order; 