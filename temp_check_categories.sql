
SELECT 
    id,
    category_name,
    parent_id,
    level,
    sort,
    status,
    is_show,
    is_deleted
FROM prod_category 
ORDER BY level, parent_id, sort;

