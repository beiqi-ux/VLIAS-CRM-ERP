-- VLIAS CRM 3级权限初始化数据脚本
-- 清空现有权限数据（谨慎操作）
DELETE FROM sys_role_permission;
DELETE FROM sys_permission WHERE is_deleted = 0;

-- 重置自增ID
ALTER TABLE sys_permission AUTO_INCREMENT = 1;

-- ========================================
-- 1. 组织架构模块权限
-- ========================================

-- 一级权限：组织架构
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('组织架构', 'org', '/org', 1, 1, 0, '组织架构管理模块', 10, 1, 1, NOW(), NOW());

-- 二级权限：组织机构管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('组织机构管理', 'org-management', '/org/org-management', 2, 2, 1, '组织机构管理功能', 10, 1, 1, NOW(), NOW());

-- 三级权限：组织机构操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看组织机构', 'org-management:view', '/org/org-management/view', 3, 3, 2, '查看组织机构列表', 10, 1, 1, NOW(), NOW()),
('新增组织机构', 'org-management:create', '/org/org-management/create', 3, 3, 2, '新增组织机构', 20, 1, 1, NOW(), NOW()),
('编辑组织机构', 'org-management:edit', '/org/org-management/edit', 3, 3, 2, '编辑组织机构信息', 30, 1, 1, NOW(), NOW()),
('删除组织机构', 'org-management:delete', '/org/org-management/delete', 3, 3, 2, '删除组织机构', 40, 1, 1, NOW(), NOW());

-- 二级权限：部门管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('部门管理', 'dept-management', '/org/dept-management', 2, 2, 1, '部门管理功能', 20, 1, 1, NOW(), NOW());

-- 三级权限：部门操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看部门', 'dept-management:view', '/org/dept-management/view', 3, 3, 7, '查看部门列表', 10, 1, 1, NOW(), NOW()),
('新增部门', 'dept-management:create', '/org/dept-management/create', 3, 3, 7, '新增部门', 20, 1, 1, NOW(), NOW()),
('编辑部门', 'dept-management:edit', '/org/dept-management/edit', 3, 3, 7, '编辑部门信息', 30, 1, 1, NOW(), NOW()),
('删除部门', 'dept-management:delete', '/org/dept-management/delete', 3, 3, 7, '删除部门', 40, 1, 1, NOW(), NOW());

-- 二级权限：岗位管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('岗位管理', 'position-management', '/org/position-management', 2, 2, 1, '岗位管理功能', 30, 1, 1, NOW(), NOW());

-- 三级权限：岗位操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看岗位', 'position-management:view', '/org/position-management/view', 3, 3, 12, '查看岗位列表', 10, 1, 1, NOW(), NOW()),
('新增岗位', 'position-management:create', '/org/position-management/create', 3, 3, 12, '新增岗位', 20, 1, 1, NOW(), NOW()),
('编辑岗位', 'position-management:edit', '/org/position-management/edit', 3, 3, 12, '编辑岗位信息', 30, 1, 1, NOW(), NOW()),
('删除岗位', 'position-management:delete', '/org/position-management/delete', 3, 3, 12, '删除岗位', 40, 1, 1, NOW(), NOW());

-- ========================================
-- 2. 系统管理模块权限
-- ========================================

-- 一级权限：系统管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('系统管理', 'system', '/system', 1, 1, 0, '系统管理模块', 20, 1, 1, NOW(), NOW());

-- 二级权限：用户管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('用户管理', 'user-management', '/system/user-management', 2, 2, 17, '用户管理功能', 10, 1, 1, NOW(), NOW());

-- 三级权限：用户操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看用户', 'user-management:view', '/system/user-management/view', 3, 3, 18, '查看用户列表', 10, 1, 1, NOW(), NOW()),
('新增用户', 'user-management:create', '/system/user-management/create', 3, 3, 18, '新增用户', 20, 1, 1, NOW(), NOW()),
('编辑用户', 'user-management:edit', '/system/user-management/edit', 3, 3, 18, '编辑用户信息', 30, 1, 1, NOW(), NOW()),
('删除用户', 'user-management:delete', '/system/user-management/delete', 3, 3, 18, '删除用户', 40, 1, 1, NOW(), NOW()),
('重置密码', 'user-management:reset-password', '/system/user-management/reset-password', 3, 3, 18, '重置用户密码', 50, 1, 1, NOW(), NOW());

-- 二级权限：角色管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('角色管理', 'role-management', '/system/role-management', 2, 2, 17, '角色管理功能', 20, 1, 1, NOW(), NOW());

-- 三级权限：角色操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看角色', 'role-management:view', '/system/role-management/view', 3, 3, 24, '查看角色列表', 10, 1, 1, NOW(), NOW()),
('新增角色', 'role-management:create', '/system/role-management/create', 3, 3, 24, '新增角色', 20, 1, 1, NOW(), NOW()),
('编辑角色', 'role-management:edit', '/system/role-management/edit', 3, 3, 24, '编辑角色信息', 30, 1, 1, NOW(), NOW()),
('删除角色', 'role-management:delete', '/system/role-management/delete', 3, 3, 24, '删除角色', 40, 1, 1, NOW(), NOW()),
('分配权限', 'role-management:assign-permission', '/system/role-management/assign-permission', 3, 3, 24, '为角色分配权限', 50, 1, 1, NOW(), NOW());

-- 二级权限：权限管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('权限管理', 'permission-management', '/system/permission-management', 2, 2, 17, '权限管理功能', 30, 1, 1, NOW(), NOW());

-- 三级权限：权限操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看权限', 'permission-management:view', '/system/permission-management/view', 3, 3, 30, '查看权限列表', 10, 1, 1, NOW(), NOW()),
('新增权限', 'permission-management:create', '/system/permission-management/create', 3, 3, 30, '新增权限', 20, 1, 1, NOW(), NOW()),
('编辑权限', 'permission-management:edit', '/system/permission-management/edit', 3, 3, 30, '编辑权限信息', 30, 1, 1, NOW(), NOW()),
('删除权限', 'permission-management:delete', '/system/permission-management/delete', 3, 3, 30, '删除权限', 40, 1, 1, NOW(), NOW());

-- 二级权限：菜单管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('菜单管理', 'menu-management', '/system/menu-management', 2, 2, 17, '菜单管理功能', 40, 1, 1, NOW(), NOW());

-- 三级权限：菜单操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看菜单', 'menu-management:view', '/system/menu-management/view', 3, 3, 35, '查看菜单列表', 10, 1, 1, NOW(), NOW()),
('新增菜单', 'menu-management:create', '/system/menu-management/create', 3, 3, 35, '新增菜单', 20, 1, 1, NOW(), NOW()),
('编辑菜单', 'menu-management:edit', '/system/menu-management/edit', 3, 3, 35, '编辑菜单信息', 30, 1, 1, NOW(), NOW()),
('删除菜单', 'menu-management:delete', '/system/menu-management/delete', 3, 3, 35, '删除菜单', 40, 1, 1, NOW(), NOW());

-- ========================================
-- 3. 商品管理模块权限
-- ========================================

-- 一级权限：商品管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('商品管理', 'product', '/product', 1, 1, 0, '商品管理模块', 30, 1, 0, NOW(), NOW());

-- 二级权限：商品信息管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('商品信息管理', 'product-info-management', '/product/product-info-management', 2, 2, 40, '商品信息管理功能', 10, 1, 0, NOW(), NOW());

-- 三级权限：商品信息操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看商品', 'product-info-management:view', '/product/product-info-management/view', 3, 3, 41, '查看商品列表', 10, 1, 0, NOW(), NOW()),
('新增商品', 'product-info-management:create', '/product/product-info-management/create', 3, 3, 41, '新增商品', 20, 1, 0, NOW(), NOW()),
('编辑商品', 'product-info-management:edit', '/product/product-info-management/edit', 3, 3, 41, '编辑商品信息', 30, 1, 0, NOW(), NOW()),
('删除商品', 'product-info-management:delete', '/product/product-info-management/delete', 3, 3, 41, '删除商品', 40, 1, 0, NOW(), NOW());

-- 二级权限：商品分类管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('商品分类管理', 'product-category-management', '/product/product-category-management', 2, 2, 40, '商品分类管理功能', 20, 1, 0, NOW(), NOW());

-- 三级权限：商品分类操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看分类', 'product-category-management:view', '/product/product-category-management/view', 3, 3, 46, '查看商品分类', 10, 1, 0, NOW(), NOW()),
('新增分类', 'product-category-management:create', '/product/product-category-management/create', 3, 3, 46, '新增商品分类', 20, 1, 0, NOW(), NOW()),
('编辑分类', 'product-category-management:edit', '/product/product-category-management/edit', 3, 3, 46, '编辑商品分类', 30, 1, 0, NOW(), NOW()),
('删除分类', 'product-category-management:delete', '/product/product-category-management/delete', 3, 3, 46, '删除商品分类', 40, 1, 0, NOW(), NOW());

-- 二级权限：商品品牌管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('商品品牌管理', 'product-brand-management', '/product/product-brand-management', 2, 2, 40, '商品品牌管理功能', 30, 1, 0, NOW(), NOW());

-- 三级权限：商品品牌操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看品牌', 'product-brand-management:view', '/product/product-brand-management/view', 3, 3, 51, '查看商品品牌', 10, 1, 0, NOW(), NOW()),
('新增品牌', 'product-brand-management:create', '/product/product-brand-management/create', 3, 3, 51, '新增商品品牌', 20, 1, 0, NOW(), NOW()),
('编辑品牌', 'product-brand-management:edit', '/product/product-brand-management/edit', 3, 3, 51, '编辑商品品牌', 30, 1, 0, NOW(), NOW()),
('删除品牌', 'product-brand-management:delete', '/product/product-brand-management/delete', 3, 3, 51, '删除商品品牌', 40, 1, 0, NOW(), NOW());

-- ========================================
-- 4. 个人中心模块权限
-- ========================================

-- 一级权限：个人中心
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('个人中心', 'profile', '/profile', 1, 1, 0, '个人中心模块', 90, 1, 1, NOW(), NOW());

-- 二级权限：个人信息管理
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES ('个人信息管理', 'profile-info-management', '/profile/profile-info-management', 2, 2, 56, '个人信息管理功能', 10, 1, 1, NOW(), NOW());

-- 三级权限：个人信息操作
INSERT INTO sys_permission (permission_name, permission_code, permission_path, permission_type, level_depth, parent_id, description, sort_order, status, is_core, create_time, update_time) 
VALUES 
('查看个人信息', 'profile-info-management:view', '/profile/profile-info-management/view', 3, 3, 57, '查看个人信息', 10, 1, 1, NOW(), NOW()),
('编辑个人信息', 'profile-info-management:edit', '/profile/profile-info-management/edit', 3, 3, 57, '编辑个人信息', 20, 1, 1, NOW(), NOW()),
('修改密码', 'profile-info-management:change-password', '/profile/profile-info-management/change-password', 3, 3, 57, '修改登录密码', 30, 1, 1, NOW(), NOW());

-- 查看权限统计
SELECT 
    '权限统计' as type,
    permission_type,
    CASE permission_type 
        WHEN 1 THEN '一级权限(模块)'
        WHEN 2 THEN '二级权限(子模块)'
        WHEN 3 THEN '三级权限(操作)'
    END as type_name,
    COUNT(*) as count
FROM sys_permission 
WHERE is_deleted = 0
GROUP BY permission_type
ORDER BY permission_type;

-- 查看权限树结构示例
SELECT 
    CONCAT(REPEAT('  ', level_depth - 1), permission_name) as permission_tree,
    permission_code,
    permission_path,
    permission_type,
    level_depth
FROM sys_permission 
WHERE is_deleted = 0 
ORDER BY permission_path, sort_order
LIMIT 30; 