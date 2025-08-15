-- 添加采购退货管理权限和菜单
-- 数据库: vliascrm
-- 表: sys_menu, sys_permission

-- ============================================
-- 1. 添加采购退货管理菜单
-- ============================================
INSERT INTO sys_menu (
    id, parent_id, menu_name, menu_code, menu_type, path, component,
    icon, sort, visible, status, permission_code, is_frame,
    create_time, update_time, create_by, update_by, is_deleted
) VALUES (
    51, 10, '采购退货管理', 'pur-return-management', 2, '/purchase/return', 'purchase/return/index',
    'el-icon-remove', 4, 1, 1, 'pur-return-management', 0,
    NOW(), NOW(), 'system', 'system', 0
);

-- ============================================
-- 2. 添加采购退货管理主权限 (二级权限 - 菜单权限)
-- ============================================
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    248, '采购退货管理', 'pur-return-management', 2, 10, 51,
    '采购退货管理模块', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 2, NULL, 4, NULL
);

-- ============================================
-- 3. 添加采购退货管理的功能权限 (三级权限 - 功能权限)
-- ============================================

-- 3.1 退货单列表权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    249, '退货单列表', 'pur-return-management:list', 3, 248, NULL,
    '查看采购退货单列表', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 1, NULL
);

-- 3.2 查看退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    250, '查看退货单', 'pur-return-management:view', 3, 248, NULL,
    '查看采购退货单详情', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 2, NULL
);

-- 3.3 新增退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    251, '新增退货单', 'pur-return-management:create', 3, 248, NULL,
    '新增采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 3, NULL
);

-- 3.4 编辑退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    252, '编辑退货单', 'pur-return-management:edit', 3, 248, NULL,
    '编辑采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 4, NULL
);

-- 3.5 删除退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    253, '删除退货单', 'pur-return-management:delete', 3, 248, NULL,
    '删除采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 5, NULL
);

-- 3.6 提交退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    254, '提交退货单', 'pur-return-management:submit', 3, 248, NULL,
    '提交采购退货单审核', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 6, NULL
);

-- 3.7 审核退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    255, '审核退货单', 'pur-return-management:audit', 3, 248, NULL,
    '审核采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 7, NULL
);

-- 3.8 确认退货权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    256, '确认退货', 'pur-return-management:confirm', 3, 248, NULL,
    '确认采购退货单退货', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 8, NULL
);

-- 3.9 取消退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    257, '取消退货单', 'pur-return-management:cancel', 3, 248, NULL,
    '取消采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 9, NULL
);

-- 3.10 复制退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    258, '复制退货单', 'pur-return-management:copy', 3, 248, NULL,
    '复制采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 10, NULL
);

-- 3.11 打印退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    259, '打印退货单', 'pur-return-management:print', 3, 248, NULL,
    '打印采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 11, NULL
);

-- 3.12 导出退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    260, '导出退货单', 'pur-return-management:export', 3, 248, NULL,
    '导出采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 12, NULL
);

-- 3.13 导入退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    261, '导入退货单', 'pur-return-management:import', 3, 248, NULL,
    '导入采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 13, NULL
);

-- 3.14 退货统计权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    262, '退货统计', 'pur-return-management:statistics', 3, 248, NULL,
    '采购退货统计分析', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 14, NULL
);

-- ============================================
-- 4. 为管理员角色分配采购退货管理权限
-- ============================================

-- 查找管理员角色ID (假设角色名称为 admin 或 管理员)
SET @admin_role_id = (SELECT id FROM sys_role WHERE role_code = 'admin' OR role_name = '管理员' LIMIT 1);

-- 为管理员角色分配采购退货管理主权限
INSERT INTO sys_role_permission (role_id, permission_id, create_time, create_by)
SELECT @admin_role_id, 248, NOW(), 1
WHERE @admin_role_id IS NOT NULL 
AND NOT EXISTS (SELECT 1 FROM sys_role_permission WHERE role_id = @admin_role_id AND permission_id = 248);

-- 为管理员角色分配所有采购退货管理功能权限
INSERT INTO sys_role_permission (role_id, permission_id, create_time, create_by)
SELECT @admin_role_id, id, NOW(), 1
FROM sys_permission 
WHERE id BETWEEN 249 AND 262 
AND @admin_role_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM sys_role_permission 
    WHERE role_id = @admin_role_id AND permission_id = sys_permission.id
);

-- ============================================
-- 5. 验证数据完整性
-- ============================================

-- 验证菜单是否正确插入
SELECT 'Menu verification:' as info;
SELECT id, parent_id, menu_name, menu_code, path, component, permission_code
FROM sys_menu 
WHERE id = 51;

-- 验证权限是否正确插入
SELECT 'Permission verification:' as info;
SELECT id, permission_name, permission_code, permission_type, parent_id, level_depth, sort_order
FROM sys_permission 
WHERE id BETWEEN 248 AND 262
ORDER BY permission_type, sort_order;

-- 验证权限层级结构
SELECT 'Permission hierarchy verification:' as info;
SELECT 
    CASE 
        WHEN permission_type = 2 THEN '菜单权限'
        WHEN permission_type = 3 THEN '功能权限'
        ELSE '其他'
    END as permission_type_name,
    permission_name,
    permission_code,
    CASE 
        WHEN parent_id = 10 THEN '采购管理模块'
        WHEN parent_id = 248 THEN '采购退货管理'
        ELSE CONCAT('父级ID: ', parent_id)
    END as parent_info
FROM sys_permission 
WHERE id BETWEEN 248 AND 262
ORDER BY permission_type, sort_order;

-- 验证角色权限分配
SELECT 'Role permission assignment verification:' as info;
SELECT r.role_name, p.permission_name, p.permission_code
FROM sys_role r
JOIN sys_role_permission rp ON r.id = rp.role_id
JOIN sys_permission p ON rp.permission_id = p.id
WHERE p.id BETWEEN 248 AND 262
ORDER BY r.role_name, p.sort_order; 
-- 数据库: vliascrm
-- 表: sys_menu, sys_permission

-- ============================================
-- 1. 添加采购退货管理菜单
-- ============================================
INSERT INTO sys_menu (
    id, parent_id, menu_name, menu_code, menu_type, path, component,
    icon, sort, visible, status, permission_code, is_frame,
    create_time, update_time, create_by, update_by, is_deleted
) VALUES (
    51, 10, '采购退货管理', 'pur-return-management', 2, '/purchase/return', 'purchase/return/index',
    'el-icon-remove', 4, 1, 1, 'pur-return-management', 0,
    NOW(), NOW(), 'system', 'system', 0
);

-- ============================================
-- 2. 添加采购退货管理主权限 (二级权限 - 菜单权限)
-- ============================================
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    248, '采购退货管理', 'pur-return-management', 2, 10, 51,
    '采购退货管理模块', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 2, NULL, 4, NULL
);

-- ============================================
-- 3. 添加采购退货管理的功能权限 (三级权限 - 功能权限)
-- ============================================

-- 3.1 退货单列表权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    249, '退货单列表', 'pur-return-management:list', 3, 248, NULL,
    '查看采购退货单列表', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 1, NULL
);

-- 3.2 查看退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    250, '查看退货单', 'pur-return-management:view', 3, 248, NULL,
    '查看采购退货单详情', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 2, NULL
);

-- 3.3 新增退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    251, '新增退货单', 'pur-return-management:create', 3, 248, NULL,
    '新增采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 3, NULL
);

-- 3.4 编辑退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    252, '编辑退货单', 'pur-return-management:edit', 3, 248, NULL,
    '编辑采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 4, NULL
);

-- 3.5 删除退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    253, '删除退货单', 'pur-return-management:delete', 3, 248, NULL,
    '删除采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 5, NULL
);

-- 3.6 提交退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    254, '提交退货单', 'pur-return-management:submit', 3, 248, NULL,
    '提交采购退货单审核', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 6, NULL
);

-- 3.7 审核退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    255, '审核退货单', 'pur-return-management:audit', 3, 248, NULL,
    '审核采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 7, NULL
);

-- 3.8 确认退货权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    256, '确认退货', 'pur-return-management:confirm', 3, 248, NULL,
    '确认采购退货单退货', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 8, NULL
);

-- 3.9 取消退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    257, '取消退货单', 'pur-return-management:cancel', 3, 248, NULL,
    '取消采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 9, NULL
);

-- 3.10 复制退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    258, '复制退货单', 'pur-return-management:copy', 3, 248, NULL,
    '复制采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 10, NULL
);

-- 3.11 打印退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    259, '打印退货单', 'pur-return-management:print', 3, 248, NULL,
    '打印采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 11, NULL
);

-- 3.12 导出退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    260, '导出退货单', 'pur-return-management:export', 3, 248, NULL,
    '导出采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 12, NULL
);

-- 3.13 导入退货单权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    261, '导入退货单', 'pur-return-management:import', 3, 248, NULL,
    '导入采购退货单', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 13, NULL
);

-- 3.14 退货统计权限
INSERT INTO sys_permission (
    id, permission_name, permission_code, permission_type, parent_id, menu_id,
    description, status, create_time, update_time, create_by, update_by,
    is_deleted, is_core, level_depth, permission_path, sort_order, resource_id
) VALUES (
    262, '退货统计', 'pur-return-management:statistics', 3, 248, NULL,
    '采购退货统计分析', 1, NOW(), NOW(), 'system', 'system',
    0, 1, 3, NULL, 14, NULL
);

-- ============================================
-- 4. 为管理员角色分配采购退货管理权限
-- ============================================

-- 查找管理员角色ID (假设角色名称为 admin 或 管理员)
SET @admin_role_id = (SELECT id FROM sys_role WHERE role_code = 'admin' OR role_name = '管理员' LIMIT 1);

-- 为管理员角色分配采购退货管理主权限
INSERT INTO sys_role_permission (role_id, permission_id, create_time, create_by)
SELECT @admin_role_id, 248, NOW(), 1
WHERE @admin_role_id IS NOT NULL 
AND NOT EXISTS (SELECT 1 FROM sys_role_permission WHERE role_id = @admin_role_id AND permission_id = 248);

-- 为管理员角色分配所有采购退货管理功能权限
INSERT INTO sys_role_permission (role_id, permission_id, create_time, create_by)
SELECT @admin_role_id, id, NOW(), 1
FROM sys_permission 
WHERE id BETWEEN 249 AND 262 
AND @admin_role_id IS NOT NULL
AND NOT EXISTS (
    SELECT 1 FROM sys_role_permission 
    WHERE role_id = @admin_role_id AND permission_id = sys_permission.id
);

-- ============================================
-- 5. 验证数据完整性
-- ============================================

-- 验证菜单是否正确插入
SELECT 'Menu verification:' as info;
SELECT id, parent_id, menu_name, menu_code, path, component, permission_code
FROM sys_menu 
WHERE id = 51;

-- 验证权限是否正确插入
SELECT 'Permission verification:' as info;
SELECT id, permission_name, permission_code, permission_type, parent_id, level_depth, sort_order
FROM sys_permission 
WHERE id BETWEEN 248 AND 262
ORDER BY permission_type, sort_order;

-- 验证权限层级结构
SELECT 'Permission hierarchy verification:' as info;
SELECT 
    CASE 
        WHEN permission_type = 2 THEN '菜单权限'
        WHEN permission_type = 3 THEN '功能权限'
        ELSE '其他'
    END as permission_type_name,
    permission_name,
    permission_code,
    CASE 
        WHEN parent_id = 10 THEN '采购管理模块'
        WHEN parent_id = 248 THEN '采购退货管理'
        ELSE CONCAT('父级ID: ', parent_id)
    END as parent_info
FROM sys_permission 
WHERE id BETWEEN 248 AND 262
ORDER BY permission_type, sort_order;

-- 验证角色权限分配
SELECT 'Role permission assignment verification:' as info;
SELECT r.role_name, p.permission_name, p.permission_code
FROM sys_role r
JOIN sys_role_permission rp ON r.id = rp.role_id
JOIN sys_permission p ON rp.permission_id = p.id
WHERE p.id BETWEEN 248 AND 262
ORDER BY r.role_name, p.sort_order; 