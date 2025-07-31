-- 角色初始化数据
INSERT INTO sys_role (role_name, role_code, description, status, create_time, update_time, is_deleted)
VALUES 
  ('超级管理员', 'ADMIN', '系统超级管理员，拥有所有权限', 1, NOW(), NOW(), 0),
  ('经理', 'MANAGER', '部门经理，拥有大部分管理权限', 1, NOW(), NOW(), 0),
  ('员工', 'EMPLOYEE', '普通员工，拥有基础权限', 1, NOW(), NOW(), 0),
  ('仓库管理员', 'WAREHOUSE', '仓库管理员，拥有库存相关权限', 1, NOW(), NOW(), 0)
ON DUPLICATE KEY UPDATE
  role_name = VALUES(role_name),
  description = VALUES(description),
  update_time = NOW();

-- 如果需要删除admin用户，先删除用户角色关联，再删除用户
-- DELETE FROM sys_user_role WHERE user_id = (SELECT id FROM sys_user WHERE username = 'admin');
-- DELETE FROM sys_user WHERE username = 'admin'; 