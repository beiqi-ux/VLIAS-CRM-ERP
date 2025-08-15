-- 添加采购入库单管理权限
-- 数据库: vliascrm
-- 表: sys_menu, sys_permission

-- 1. 首先添加采购入库单管理菜单
INSERT INTO sys_menu (
    id, parent_id, menu_name, menu_code, menu_type, path, component,
    icon, sort, visible, status, permission_code, is_frame,
    create_time, update_time, create_by, update_by, is_deleted
) VALUES (
    50, 10, '采购入库单管理', 'pur-receipt-management', 2, '/purchase/receipt', 'purchase/PurReceiptList',
    'el-icon-goods', 3, 1, 1, 'pur-receipt-management', 0,
    NOW(), NOW(), 'system', 'system', 0
);

-- 2. 添加采购入库单管理主权限 (permission_type = 2, 表示菜单权限)
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    235, '采购入库单管理', 'pur-receipt-management', 2, 10, 50,
    '采购入库单管理模块', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 2, NULL, 2, NULL
);

-- 3. 添加采购入库单管理的功能权限 (permission_type = 3, 表示功能权限)

-- 3.1 查看权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    236, '查看入库单', 'pur-receipt-management:view', 3, 235, NULL,
    '查看采购入库单详情', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 1, NULL
);

-- 3.2 列表权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    237, '入库单列表', 'pur-receipt-management:list', 3, 235, NULL,
    '查看采购入库单列表', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 2, NULL
);

-- 3.3 新增权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    238, '新增入库单', 'pur-receipt-management:create', 3, 235, NULL,
    '新增采购入库单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 3, NULL
);

-- 3.4 编辑权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    239, '编辑入库单', 'pur-receipt-management:edit', 3, 235, NULL,
    '编辑采购入库单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 4, NULL
);

-- 3.5 删除权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    240, '删除入库单', 'pur-receipt-management:delete', 3, 235, NULL,
    '删除采购入库单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 5, NULL
);

-- 3.6 提交权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    241, '提交入库单', 'pur-receipt-management:submit', 3, 235, NULL,
    '提交采购入库单审核', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 6, NULL
);

-- 3.7 审核权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    242, '审核入库单', 'pur-receipt-management:audit', 3, 235, NULL,
    '审核采购入库单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 7, NULL
);

-- 3.8 确认入库权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    243, '确认入库', 'pur-receipt-management:confirm', 3, 235, NULL,
    '确认采购入库单入库', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 8, NULL
);

-- 3.9 取消权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    244, '取消入库单', 'pur-receipt-management:cancel', 3, 235, NULL,
    '取消采购入库单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 9, NULL
);

-- 3.10 复制权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    245, '复制入库单', 'pur-receipt-management:copy', 3, 235, NULL,
    '复制采购入库单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 10, NULL
);

-- 3.11 打印权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    246, '打印入库单', 'pur-receipt-management:print', 3, 235, NULL,
    '打印采购入库单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 11, NULL
);

-- 3.12 导出权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    247, '导出入库单', 'pur-receipt-management:export', 3, 235, NULL,
    '导出采购入库单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 12, NULL
);

-- 验证插入的数据
SELECT '=== 菜单验证 ===' as info;
SELECT id, menu_name, menu_code, parent_id FROM sys_menu WHERE id = 50;

SELECT '=== 权限验证 ===' as info;
SELECT id, permission_name, permission_code, permission_type, parent_id, menu_id 
FROM sys_permission 
WHERE id BETWEEN 235 AND 247 
ORDER BY id; 