-- 验证采购入库单管理权限数据完整性
-- 数据库: vliascrm

SELECT '=== 菜单数据完整性检查 ===' as info;
SELECT 
    id,
    parent_id,
    menu_name,
    menu_code,
    menu_type,
    path,
    component,
    icon,
    sort,
    visible,
    status,
    permission_code,
    is_frame,
    create_time,
    update_time,
    create_by,
    update_by,
    is_deleted
FROM sys_menu 
WHERE id = 50;

SELECT '=== 权限数据完整性检查 ===' as info;
SELECT 
    id,
    permission_name,
    permission_code,
    permission_type,
    parent_id,
    menu_id,
    description,
    status,
    create_time,
    update_time,
    create_by,
    update_by,
    is_deleted,
    is_core,
    level_depth,
    permission_path,
    sort_order,
    resource_id
FROM sys_permission 
WHERE id BETWEEN 235 AND 247 
ORDER BY id;

SELECT '=== 权限层级结构检查 ===' as info;
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
        WHEN parent_id = 235 THEN '采购入库单管理'
        ELSE CONCAT('父级ID: ', parent_id)
    END as parent_info
FROM sys_permission 
WHERE id BETWEEN 235 AND 247 
ORDER BY permission_type, sort_order;

SELECT '=== 字段完整性检查 ===' as info;
SELECT 
    '权限表字段完整性' as check_type,
    COUNT(*) as total_records,
    SUM(CASE WHEN permission_name IS NOT NULL AND permission_name != '' THEN 1 ELSE 0 END) as name_filled,
    SUM(CASE WHEN permission_code IS NOT NULL AND permission_code != '' THEN 1 ELSE 0 END) as code_filled,
    SUM(CASE WHEN description IS NOT NULL AND description != '' THEN 1 ELSE 0 END) as desc_filled,
    SUM(CASE WHEN create_time IS NOT NULL THEN 1 ELSE 0 END) as create_time_filled,
    SUM(CASE WHEN create_by IS NOT NULL AND create_by != '' THEN 1 ELSE 0 END) as create_by_filled
FROM sys_permission 
WHERE id BETWEEN 235 AND 247

UNION ALL

SELECT 
    '菜单表字段完整性' as check_type,
    COUNT(*) as total_records,
    SUM(CASE WHEN menu_name IS NOT NULL AND menu_name != '' THEN 1 ELSE 0 END) as name_filled,
    SUM(CASE WHEN menu_code IS NOT NULL AND menu_code != '' THEN 1 ELSE 0 END) as code_filled,
    SUM(CASE WHEN path IS NOT NULL AND path != '' THEN 1 ELSE 0 END) as path_filled,
    SUM(CASE WHEN create_time IS NOT NULL THEN 1 ELSE 0 END) as create_time_filled,
    SUM(CASE WHEN create_by IS NOT NULL AND create_by != '' THEN 1 ELSE 0 END) as create_by_filled
FROM sys_menu 
WHERE id = 50; 