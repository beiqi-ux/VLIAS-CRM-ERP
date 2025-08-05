-- VLIAS CRM 权限表升级脚本：2级权限升级为3级权限
-- 执行前请备份数据库

-- 1. 修改权限类型字段注释和约束
ALTER TABLE `sys_permission` 
MODIFY COLUMN `permission_type` int NOT NULL COMMENT '权限类型 1-一级权限(模块) 2-二级权限(子模块) 3-三级权限(操作)';

-- 2. 增加权限编码长度支持更复杂的编码规则
ALTER TABLE `sys_permission` 
MODIFY COLUMN `permission_code` varchar(200) NOT NULL COMMENT '权限编码';

-- 3. 增加权限名称长度
ALTER TABLE `sys_permission` 
MODIFY COLUMN `permission_name` varchar(100) NOT NULL COMMENT '权限名称';

-- 4. 修改排序字段名称，与设计文档保持一致
ALTER TABLE `sys_permission` 
CHANGE COLUMN `sort` `sort_order` int DEFAULT '0' COMMENT '排序字段';

-- 5. 增加权限层级深度字段，便于查询优化
ALTER TABLE `sys_permission` 
ADD COLUMN `level_depth` int DEFAULT '1' COMMENT '权限层级深度 1-一级 2-二级 3-三级' AFTER `permission_type`;

-- 6. 增加权限路径字段，便于快速查找父级权限链
ALTER TABLE `sys_permission` 
ADD COLUMN `permission_path` varchar(500) DEFAULT NULL COMMENT '权限路径，格式：/parent1/parent2/current' AFTER `permission_code`;

-- 7. 创建索引优化查询性能
CREATE INDEX `idx_permission_type_level` ON `sys_permission` (`permission_type`, `level_depth`);
CREATE INDEX `idx_permission_path` ON `sys_permission` (`permission_path`);
CREATE INDEX `idx_parent_id_sort` ON `sys_permission` (`parent_id`, `sort_order`);

-- 8. 更新现有数据的层级深度
UPDATE `sys_permission` SET `level_depth` = `permission_type`;

-- 9. 更新现有数据的权限路径
-- 一级权限路径
UPDATE `sys_permission` 
SET `permission_path` = CONCAT('/', `permission_code`) 
WHERE `permission_type` = 1 AND (`parent_id` = 0 OR `parent_id` IS NULL);

-- 二级权限路径（需要找到父级权限）
UPDATE p1 
JOIN p2 ON p1.parent_id = p2.id 
SET p1.permission_path = CONCAT(p2.permission_path, '/', p1.permission_code)
WHERE p1.permission_type = 2 AND p2.permission_type = 1;

-- 三级权限路径（需要找到父级和祖父级权限）
UPDATE p1 
JOIN p2 ON p1.parent_id = p2.id 
SET p1.permission_path = CONCAT(p2.permission_path, '/', p1.permission_code)
WHERE p1.permission_type = 3 AND p2.permission_type = 2;

-- 10. 验证数据完整性
SELECT 
    permission_type,
    level_depth,
    COUNT(*) as count,
    GROUP_CONCAT(permission_name SEPARATOR ', ') as examples
FROM sys_permission 
WHERE is_deleted = 0
GROUP BY permission_type, level_depth
ORDER BY permission_type;

-- 显示升级后的权限结构示例
SELECT 
    id,
    permission_name,
    permission_code,
    permission_type,
    level_depth,
    permission_path,
    parent_id
FROM sys_permission 
WHERE is_deleted = 0 
ORDER BY permission_path, sort_order
LIMIT 20; 