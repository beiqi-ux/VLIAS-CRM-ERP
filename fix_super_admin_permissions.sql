-- 查看超级管理员角色
SELECT id, role_name, role_code, status FROM sys_role WHERE role_code = 'super_admin';

-- 查看所有权限
SELECT id, permission_name, permission_code, permission_type, status FROM sys_permission WHERE status = 1;

-- 获取超级管理员角色ID
SET @super_admin_role_id = (SELECT id FROM sys_role WHERE role_code = 'super_admin');

-- 删除现有的超级管理员权限关联（避免重复）
DELETE FROM sys_role_permission WHERE role_id = @super_admin_role_id;

-- 为超级管理员分配所有启用的权限
INSERT INTO sys_role_permission (role_id, permission_id, create_time, create_by)
SELECT 
    @super_admin_role_id,
    p.id,
    NOW(),
    1
FROM sys_permission p 
WHERE p.status = 1 AND p.is_deleted = 0;

-- 确保超级管理员角色状态为启用
UPDATE sys_role SET status = 1 WHERE role_code = 'super_admin';

-- 查看分配结果
SELECT 
    r.role_name,
    p.permission_name,
    p.permission_code,
    p.permission_type
FROM sys_role_permission rp
JOIN sys_role r ON rp.role_id = r.id
JOIN sys_permission p ON rp.permission_id = p.id
WHERE r.role_code = 'super_admin'
ORDER BY p.permission_type, p.permission_name; 