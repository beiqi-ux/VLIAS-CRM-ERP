-- 修正商品分类层级关系
-- 这个脚本基于父子关系重新计算所有分类的正确层级

-- 1. 先将所有parent_id为0的记录改为NULL（表示根分类）
UPDATE prod_category SET parent_id = NULL WHERE parent_id = 0;

-- 2. 重新计算所有分类的层级
-- 首先设置根分类（parent_id为NULL）的层级为1
UPDATE prod_category SET level = 1 WHERE parent_id IS NULL;

-- 然后设置二级分类的层级（parent_id指向根分类）
UPDATE prod_category pc1 
SET level = 2 
WHERE parent_id IN (
    SELECT id FROM (
        SELECT id FROM prod_category WHERE parent_id IS NULL
    ) AS temp
);

-- 最后设置三级分类的层级（parent_id指向二级分类）
UPDATE prod_category pc1 
SET level = 3 
WHERE parent_id IN (
    SELECT id FROM (
        SELECT id FROM prod_category WHERE level = 2
    ) AS temp
);

-- 验证修正后的数据
SELECT 
    id, 
    category_name, 
    COALESCE(parent_id, 0) as parent_id, 
    level,
    CASE 
        WHEN parent_id IS NULL THEN '根分类'
        ELSE (SELECT category_name FROM prod_category pc2 WHERE pc2.id = prod_category.parent_id)
    END as parent_name
FROM prod_category 
WHERE is_deleted = false
ORDER BY 
    COALESCE(parent_id, 0), 
    level, 
    id; 