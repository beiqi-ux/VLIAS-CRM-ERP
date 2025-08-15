-- 供应商商品管理菜单插入脚本
-- 已成功插入到 sys_menu 表

-- 插入供应商商品管理菜单
INSERT IGNORE INTO sys_menu (
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
) VALUES (
    10,                              -- 父级ID：采购管理
    '供应商商品管理',                  -- 菜单名称
    'supplier-goods-management',     -- 菜单编码
    2,                               -- 菜单类型：2=二级菜单
    '/purchase/supplier-goods',      -- 路径
    'purchase/SupplierGoodsList',    -- 组件路径
    'el-icon-goods',                 -- 图标
    5,                               -- 排序：在采购退货管理后面
    1,                               -- 可见：1=可见
    1,                               -- 状态：1=启用
    'supplier-goods-management',     -- 权限编码
    0,                               -- 是否外链：0=否
    NOW(),                           -- 创建时间
    NOW(),                           -- 更新时间
    'system',                        -- 创建人
    'system',                        -- 更新人
    0                                -- 是否删除：0=否
);

-- 获取刚插入的菜单ID
SELECT id, menu_name, menu_code, path, component 
FROM sys_menu 
WHERE menu_code = 'supplier-goods-management';

-- 更新权限表中的menu_id字段
UPDATE sys_permission 
SET menu_id = (SELECT id FROM sys_menu WHERE menu_code = 'supplier-goods-management')
WHERE permission_code = 'supplier-goods-management';

-- 验证菜单和权限的关联
SELECT 
    p.id as permission_id,
    p.permission_name,
    p.permission_code,
    p.menu_id,
    m.menu_name,
    m.path,
    m.component,
    m.icon,
    m.sort
FROM sys_permission p
LEFT JOIN sys_menu m ON p.menu_id = m.id
WHERE p.permission_code = 'supplier-goods-management';

-- 查看采购管理下的所有子菜单
SELECT 
    id,
    menu_name,
    menu_code,
    path,
    component,
    sort,
    status
FROM sys_menu 
WHERE parent_id = 10
ORDER BY sort; 