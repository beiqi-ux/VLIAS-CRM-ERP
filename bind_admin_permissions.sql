-- 绑定所有权限到超级管理员角色并分配给用户18845913092
-- 执行时间: 2024-12-19

-- 1. 清理超级管理员角色的现有权限
DELETE FROM sys_role_permission WHERE role_id = 1;

-- 2. 绑定所有权限到超级管理员角色(role_id=1)
INSERT INTO sys_role_permission (role_id, permission_id, create_time, create_by) 
SELECT 
    1 as role_id,
    id as permission_id,
    NOW() as create_time,
    1 as create_by
FROM sys_permission;

-- 3. 确保用户18845913092绑定到超级管理员角色
-- 先删除该用户的现有角色绑定
DELETE FROM sys_user_role WHERE user_id = 1;

-- 重新绑定超级管理员角色
INSERT INTO sys_user_role (user_id, role_id, create_time, create_by) 
VALUES (1, 1, NOW(), 1);

-- 4. 验证结果
SELECT '权限绑定统计' as info;
SELECT 
    r.role_name,
    COUNT(rp.permission_id) as permission_count
FROM sys_role r
LEFT JOIN sys_role_permission rp ON r.id = rp.role_id
WHERE r.id = 1
GROUP BY r.id, r.role_name;

SELECT '用户角色绑定统计' as info;
SELECT 
    u.username,
    u.mobile,
    r.role_name,
    r.role_code
FROM sys_user u
JOIN sys_user_role ur ON u.id = ur.user_id
JOIN sys_role r ON ur.role_id = r.id
WHERE u.id = 1;

SELECT '权限总数统计' as info;
SELECT COUNT(*) as total_permissions FROM sys_permission; 