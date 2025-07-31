-- 基础数据字典初始化脚本
-- 只包含通用状态和性别两个基础字典

-- 1. 通用状态字典（启用/禁用）
INSERT INTO sys_dict (dict_name, dict_code, description, status, create_time, create_by) VALUES 
('通用状态', 'common_status', '系统通用状态：启用/禁用', 1, NOW(), '1')
ON DUPLICATE KEY UPDATE dict_name = VALUES(dict_name);

SET @dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'common_status');

INSERT INTO sys_dict_item (dict_id, item_text, item_value, description, sort, status, create_time, create_by) VALUES 
(@dict_id, '禁用', '0', '禁用状态', 1, 1, NOW(), '1'),
(@dict_id, '正常', '1', '正常状态', 2, 1, NOW(), '1')
ON DUPLICATE KEY UPDATE item_text = VALUES(item_text);

-- 2. 通用性别字典
INSERT INTO sys_dict (dict_name, dict_code, description, status, create_time, create_by) VALUES 
('性别', 'gender', '通用性别字典，适用于用户、客户、联系人等所有需要性别信息的场景', 1, NOW(), '1')
ON DUPLICATE KEY UPDATE dict_name = VALUES(dict_name);

SET @dict_id = (SELECT id FROM sys_dict WHERE dict_code = 'gender');

INSERT INTO sys_dict_item (dict_id, item_text, item_value, description, sort, status, create_time, create_by) VALUES 
(@dict_id, '未知', '0', '性别未知或不愿透露', 1, 1, NOW(), '1'),
(@dict_id, '男', '1', '男性', 2, 1, NOW(), '1'),
(@dict_id, '女', '2', '女性', 3, 1, NOW(), '1')
ON DUPLICATE KEY UPDATE item_text = VALUES(item_text); 