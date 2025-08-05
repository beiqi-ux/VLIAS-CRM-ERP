-- 恢复权限编码和状态脚本
USE vliascrm;

-- 恢复组织架构相关权限
UPDATE sys_permission SET permission_code = 'org_structure', is_deleted = 0, update_time = NOW() WHERE id = 1;
UPDATE sys_permission SET permission_code = 'org', is_deleted = 0, update_time = NOW() WHERE id = 2;
UPDATE sys_permission SET permission_code = 'org:view', is_deleted = 0, update_time = NOW() WHERE id = 3;
UPDATE sys_permission SET permission_code = 'org:add', is_deleted = 0, update_time = NOW() WHERE id = 4;
UPDATE sys_permission SET permission_code = 'org:edit', is_deleted = 0, update_time = NOW() WHERE id = 5;
UPDATE sys_permission SET permission_code = 'org:delete', is_deleted = 0, update_time = NOW() WHERE id = 6;

-- 恢复部门管理相关权限
UPDATE sys_permission SET permission_code = 'dept', is_deleted = 0, update_time = NOW() WHERE id = 7;
UPDATE sys_permission SET permission_code = 'dept:view', is_deleted = 0, update_time = NOW() WHERE id = 8;
UPDATE sys_permission SET permission_code = 'dept:add', is_deleted = 0, update_time = NOW() WHERE id = 9;
UPDATE sys_permission SET permission_code = 'dept:edit', is_deleted = 0, update_time = NOW() WHERE id = 10;
UPDATE sys_permission SET permission_code = 'dept:delete', is_deleted = 0, update_time = NOW() WHERE id = 11;

-- 恢复岗位管理相关权限
UPDATE sys_permission SET permission_code = 'position', is_deleted = 0, update_time = NOW() WHERE id = 12;
UPDATE sys_permission SET permission_code = 'position:view', is_deleted = 0, update_time = NOW() WHERE id = 13;
UPDATE sys_permission SET permission_code = 'position:add', is_deleted = 0, update_time = NOW() WHERE id = 14;
UPDATE sys_permission SET permission_code = 'position:edit', is_deleted = 0, update_time = NOW() WHERE id = 15;
UPDATE sys_permission SET permission_code = 'position:delete', is_deleted = 0, update_time = NOW() WHERE id = 16;

-- 恢复系统管理相关权限
UPDATE sys_permission SET permission_code = 'system', is_deleted = 0, update_time = NOW() WHERE id = 17;
UPDATE sys_permission SET permission_code = 'user', is_deleted = 0, update_time = NOW() WHERE id = 18;
UPDATE sys_permission SET permission_code = 'user:view', is_deleted = 0, update_time = NOW() WHERE id = 19;
UPDATE sys_permission SET permission_code = 'user:add', is_deleted = 0, update_time = NOW() WHERE id = 20;

-- 批量恢复所有被逻辑删除的权限（通用方法）
UPDATE sys_permission 
SET is_deleted = 0, 
    update_time = NOW(),
    permission_code = CASE 
        -- 根据权限名称推断原始编码
        WHEN permission_name = '组织架构' THEN 'org_structure'
        WHEN permission_name = '组织机构管理' THEN 'org'
        WHEN permission_name = '查看组织' THEN 'org:view'
        WHEN permission_name = '新增组织' THEN 'org:add'
        WHEN permission_name = '编辑组织' THEN 'org:edit'
        WHEN permission_name = '删除组织' THEN 'org:delete'
        
        WHEN permission_name = '部门管理' THEN 'dept'
        WHEN permission_name = '查看部门' THEN 'dept:view'
        WHEN permission_name = '新增部门' THEN 'dept:add'
        WHEN permission_name = '编辑部门' THEN 'dept:edit'
        WHEN permission_name = '删除部门' THEN 'dept:delete'
        
        WHEN permission_name = '岗位管理' THEN 'position'
        WHEN permission_name = '查看岗位' THEN 'position:view'
        WHEN permission_name = '新增岗位' THEN 'position:add'
        WHEN permission_name = '编辑岗位' THEN 'position:edit'
        WHEN permission_name = '删除岗位' THEN 'position:delete'
        
        WHEN permission_name = '系统管理' THEN 'system'
        WHEN permission_name = '用户管理' THEN 'user'
        WHEN permission_name = '查看用户' THEN 'user:view'
        WHEN permission_name = '新增用户' THEN 'user:add'
        WHEN permission_name = '编辑用户' THEN 'user:edit'
        WHEN permission_name = '删除用户' THEN 'user:delete'
        WHEN permission_name = '重置密码' THEN 'user:reset-password'
        WHEN permission_name = '分配角色' THEN 'user:assign-role'
        
        WHEN permission_name = '角色管理' THEN 'role'
        WHEN permission_name = '查看角色' THEN 'role:view'
        WHEN permission_name = '新增角色' THEN 'role:add'
        WHEN permission_name = '编辑角色' THEN 'role:edit'
        WHEN permission_name = '删除角色' THEN 'role:delete'
        WHEN permission_name = '分配权限' THEN 'role:assign'
        
        WHEN permission_name = '权限管理' THEN 'permission'
        WHEN permission_name = '查看权限' THEN 'permission:view'
        WHEN permission_name = '新增权限' THEN 'permission:add'
        WHEN permission_name = '编辑权限' THEN 'permission:edit'
        WHEN permission_name = '删除权限' THEN 'permission:delete'
        WHEN permission_name = '同步权限' THEN 'permission:sync'
        WHEN permission_name = '重置权限' THEN 'permission:reset'
        
        WHEN permission_name = '菜单管理' THEN 'menu'
        WHEN permission_name = '查看菜单' THEN 'menu:view'
        WHEN permission_name = '新增菜单' THEN 'menu:add'
        WHEN permission_name = '编辑菜单' THEN 'menu:edit'
        WHEN permission_name = '删除菜单' THEN 'menu:delete'
        
        WHEN permission_name = '字典管理' THEN 'dict'
        WHEN permission_name = '查看字典' THEN 'dict:view'
        WHEN permission_name = '新增字典' THEN 'dict:add'
        WHEN permission_name = '编辑字典' THEN 'dict:edit'
        WHEN permission_name = '删除字典' THEN 'dict:delete'
        
        WHEN permission_name = '商品管理' THEN 'product'
        WHEN permission_name = '查看商品' THEN 'product:view'
        WHEN permission_name = '新增商品' THEN 'product:add'
        WHEN permission_name = '编辑商品' THEN 'product:edit'
        WHEN permission_name = '删除商品' THEN 'product:delete'
        
        WHEN permission_name = '品牌管理' THEN 'brand'
        WHEN permission_name = '查看品牌' THEN 'brand:view'
        WHEN permission_name = '新增品牌' THEN 'brand:add'
        WHEN permission_name = '编辑品牌' THEN 'brand:edit'
        WHEN permission_name = '删除品牌' THEN 'brand:delete'
        
        WHEN permission_name = '分类管理' THEN 'category'
        WHEN permission_name = '查看分类' THEN 'category:view'
        WHEN permission_name = '新增分类' THEN 'category:add'
        WHEN permission_name = '编辑分类' THEN 'category:edit'
        WHEN permission_name = '删除分类' THEN 'category:delete'
        
        WHEN permission_name = '个人中心' THEN 'profile'
        WHEN permission_name = '查看个人信息' THEN 'profile:view'
        WHEN permission_name = '编辑个人信息' THEN 'profile:edit'
        WHEN permission_name = '修改密码' THEN 'profile:password'
        
        -- 如果没有匹配到，保持原始编码去掉temp前缀
        ELSE REPLACE(permission_code, CONCAT('temp_1754371156835_', id), CONCAT('unknown_', id))
    END
WHERE is_deleted = 1 AND permission_code LIKE 'temp_%'; 