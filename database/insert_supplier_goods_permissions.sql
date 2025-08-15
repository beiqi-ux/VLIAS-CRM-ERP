-- 供应商商品管理权限插入脚本
-- 所有权限已成功插入到 sys_permission 表

-- 权限层级结构：
-- 1. 供应商商品管理 (二级菜单, ID: 263, parent_id: 10)
--    ├── 查看供应商商品 (ID: 265)
--    ├── 供应商商品列表 (ID: 266)
--    ├── 新增供应商商品 (ID: 267)
--    ├── 修改供应商商品 (ID: 268)
--    ├── 删除供应商商品 (ID: 269)
--    ├── 供应商商品价格比较 (ID: 270)
--    ├── 查看供应商商品历史 (ID: 271)
--    ├── 导入供应商商品 (ID: 272)
--    ├── 导出供应商商品 (ID: 273)
--    └── 审核供应商商品 (ID: 274)

-- 插入主菜单权限（已存在）
-- INSERT INTO sys_permission (
--     permission_name, permission_code, permission_type, parent_id, menu_id, 
--     description, status, create_time, update_time, create_by, update_by, 
--     is_deleted, is_core, level_depth, permission_path, sort_order, resource_id, sort
-- ) VALUES (
--     '供应商商品管理', 'supplier-goods-management', 2, 10, NULL,
--     '管理供应商提供的商品信息、价格、供货能力等', 1, NOW(), NOW(), 'system', 'system',
--     0, 0, 2, '/purchase/supplier-goods', 3, NULL, 3
-- );

-- 插入所有操作权限
INSERT IGNORE INTO sys_permission (
    permission_name, permission_code, permission_type, parent_id, menu_id, 
    description, status, create_time, update_time, create_by, update_by, 
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id, sort
) VALUES 
-- 查看权限
('查看供应商商品', 'supplier-goods-management:view', 3, 263, NULL,
 '查看供应商商品列表和详细信息', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 1, NULL, 1),

-- 列表权限
('供应商商品列表', 'supplier-goods-management:list', 3, 263, NULL,
 '查看供应商商品列表', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 2, NULL, 2),

-- 新增权限
('新增供应商商品', 'supplier-goods-management:create', 3, 263, NULL,
 '新增供应商商品信息', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 3, NULL, 3),

-- 修改权限
('修改供应商商品', 'supplier-goods-management:update', 3, 263, NULL,
 '修改供应商商品信息', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 4, NULL, 4),

-- 删除权限
('删除供应商商品', 'supplier-goods-management:delete', 3, 263, NULL,
 '删除供应商商品信息', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 5, NULL, 5),

-- 价格比较权限
('供应商商品价格比较', 'supplier-goods-management:compare', 3, 263, NULL,
 '比较不同供应商的商品价格', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 6, NULL, 6),

-- 查看历史权限
('查看供应商商品历史', 'supplier-goods-management:view-history', 3, 263, NULL,
 '查看供应商商品的历史记录和变更日志', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 7, NULL, 7),

-- 导入权限
('导入供应商商品', 'supplier-goods-management:import', 3, 263, NULL,
 '批量导入供应商商品信息', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 8, NULL, 8),

-- 导出权限
('导出供应商商品', 'supplier-goods-management:export', 3, 263, NULL,
 '导出供应商商品信息', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 9, NULL, 9),

-- 审核权限
('审核供应商商品', 'supplier-goods-management:audit', 3, 263, NULL,
 '审核供应商商品信息的变更', 1, NOW(), NOW(), 'system', 'system',
 0, 0, 3, NULL, 10, NULL, 10);

-- 验证插入结果
SELECT 
    id, 
    permission_name as '权限名称', 
    permission_code as '权限编码', 
    CASE permission_type 
        WHEN 1 THEN '一级菜单'
        WHEN 2 THEN '二级菜单' 
        WHEN 3 THEN '操作权限'
        ELSE '未知'
    END as '权限类型',
    parent_id as '父级ID',
    status as '状态'
FROM sys_permission 
WHERE permission_code LIKE '%supplier-goods%' 
ORDER BY parent_id, sort; 