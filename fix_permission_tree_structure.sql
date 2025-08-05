-- VLIAS CRM 权限树结构修复脚本
-- 为每个二级权限创建对应的三级操作权限，建立正确的树形结构

-- 1. 首先备份现有的通用三级权限（设置为已删除状态）
UPDATE sys_permission 
SET is_deleted = true, 
    permission_code = CONCAT('backup_', permission_code, '_', UNIX_TIMESTAMP()),
    update_time = NOW()
WHERE permission_type = 3 AND parent_id = 0 AND is_deleted = false;

-- 2. 为系统管理模块的二级权限创建三级操作权限
-- 用户管理的操作权限
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time, is_deleted) 
SELECT 
    CONCAT(p.permission_name, '-', op.op_name) as permission_name,
    CONCAT(p.permission_code, ':', op.op_code) as permission_code,
    CONCAT(p.permission_path, '/', op.op_code) as permission_path,
    3 as permission_type,
    3 as level_depth,
    p.id as parent_id,
    CONCAT(op.op_desc, p.permission_name) as description,
    op.sort_order,
    1 as status,
    1 as is_core,
    NOW() as create_time,
    NOW() as update_time,
    false as is_deleted
FROM sys_permission p
CROSS JOIN (
    SELECT 'view' as op_code, '查看' as op_name, '查看' as op_desc, 10 as sort_order
    UNION ALL SELECT 'create' as op_code, '新增' as op_name, '新增' as op_desc, 20 as sort_order
    UNION ALL SELECT 'edit' as op_code, '编辑' as op_name, '编辑' as op_desc, 30 as sort_order
    UNION ALL SELECT 'delete' as op_code, '删除' as op_name, '删除' as op_desc, 40 as sort_order
    UNION ALL SELECT 'export' as op_code, '导出' as op_name, '导出' as op_desc, 50 as sort_order
) op
WHERE p.permission_type = 2 
  AND p.is_deleted = false
  AND p.permission_code IN (
    'user-management', 'role-management', 'permission-management', 
    'menu-management', 'dict-management'
  );

-- 为组织架构模块的二级权限创建三级操作权限
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time, is_deleted) 
SELECT 
    CONCAT(p.permission_name, '-', op.op_name) as permission_name,
    CONCAT(p.permission_code, ':', op.op_code) as permission_code,
    CONCAT(p.permission_path, '/', op.op_code) as permission_path,
    3 as permission_type,
    3 as level_depth,
    p.id as parent_id,
    CONCAT(op.op_desc, p.permission_name) as description,
    op.sort_order,
    1 as status,
    1 as is_core,
    NOW() as create_time,
    NOW() as update_time,
    false as is_deleted
FROM sys_permission p
CROSS JOIN (
    SELECT 'view' as op_code, '查看' as op_name, '查看' as op_desc, 10 as sort_order
    UNION ALL SELECT 'create' as op_code, '新增' as op_name, '新增' as op_desc, 20 as sort_order
    UNION ALL SELECT 'edit' as op_code, '编辑' as op_name, '编辑' as op_desc, 30 as sort_order
    UNION ALL SELECT 'delete' as op_code, '删除' as op_name, '删除' as op_desc, 40 as sort_order
) op
WHERE p.permission_type = 2 
  AND p.is_deleted = false
  AND p.permission_code IN (
    'org-management', 'dept-management', 'position-management'
  );

-- 为其他重要模块的二级权限创建基本操作权限
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time, is_deleted) 
SELECT 
    CONCAT(p.permission_name, '-', op.op_name) as permission_name,
    CONCAT(p.permission_code, ':', op.op_code) as permission_code,
    CONCAT(p.permission_path, '/', op.op_code) as permission_path,
    3 as permission_type,
    3 as level_depth,
    p.id as parent_id,
    CONCAT(op.op_desc, p.permission_name) as description,
    op.sort_order,
    1 as status,
    1 as is_core,
    NOW() as create_time,
    NOW() as update_time,
    false as is_deleted
FROM sys_permission p
CROSS JOIN (
    SELECT 'view' as op_code, '查看' as op_name, '查看' as op_desc, 10 as sort_order
    UNION ALL SELECT 'create' as op_code, '新增' as op_name, '新增' as op_desc, 20 as sort_order
    UNION ALL SELECT 'edit' as op_code, '编辑' as op_name, '编辑' as op_desc, 30 as sort_order
    UNION ALL SELECT 'delete' as op_code, '删除' as op_name, '删除' as op_desc, 40 as sort_order
) op
WHERE p.permission_type = 2 
  AND p.is_deleted = false
  AND p.permission_code IN (
    'product-info-management', 'customer-info-management', 
    'sales-order-management', 'purchase-order-management',
    'inventory-query-management'
  );

-- 3. 验证修复结果 - 查看树形结构
SELECT 
    CASE 
        WHEN p.permission_type = 1 THEN p.permission_name
        WHEN p.permission_type = 2 THEN CONCAT('  ', p.permission_name)
        WHEN p.permission_type = 3 THEN CONCAT('    ', p.permission_name)
    END as tree_structure,
    p.id,
    p.permission_code,
    p.permission_type,
    p.parent_id
FROM sys_permission p
WHERE p.is_deleted = false
ORDER BY 
    CASE WHEN p.permission_type = 1 THEN p.sort_order ELSE 999 END,
    p.parent_id,
    p.sort_order,
    p.id
LIMIT 50;

-- 4. 统计权限数量
SELECT 
    permission_type,
    COUNT(*) as count,
    CASE permission_type 
        WHEN 1 THEN '一级权限(模块)'
        WHEN 2 THEN '二级权限(子模块)'
        WHEN 3 THEN '三级权限(操作)'
    END as type_name
FROM sys_permission 
WHERE is_deleted = false 
GROUP BY permission_type
ORDER BY permission_type; 