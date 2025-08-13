-- 规格管理表结构
-- 1. 规格分类表 - 存储规格类型（颜色、尺寸、材质等）
DROP TABLE IF EXISTS prod_specification_category;
CREATE TABLE prod_specification_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规格分类ID',
    category_code VARCHAR(50) NOT NULL COMMENT '分类代码 (color, size, material等)',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称 (颜色, 尺寸, 材质等)',
    description VARCHAR(500) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序值',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY uk_category_code (category_code),
    KEY idx_sort_order (sort_order)
) COMMENT='商品规格分类表';

-- 2. 规格值表 - 存储具体的规格值（黑色、大号、钛合金等）
DROP TABLE IF EXISTS prod_specification_value;
CREATE TABLE prod_specification_value (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规格值ID',
    category_id BIGINT NOT NULL COMMENT '所属规格分类ID',
    value_code VARCHAR(50) NOT NULL COMMENT '规格值代码',
    value_name VARCHAR(100) NOT NULL COMMENT '规格值名称',
    value_desc VARCHAR(500) COMMENT '规格值描述',
    sort_order INT DEFAULT 0 COMMENT '排序值',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY uk_category_value (category_id, value_code),
    KEY idx_category_id (category_id),
    KEY idx_sort_order (sort_order),
    FOREIGN KEY (category_id) REFERENCES prod_specification_category(id) ON DELETE CASCADE
) COMMENT='商品规格值表';

-- 3. 商品规格关联表 - 商品与规格值的关联关系
DROP TABLE IF EXISTS prod_goods_specification;
CREATE TABLE prod_goods_specification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    spec_value_id BIGINT NOT NULL COMMENT '规格值ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    UNIQUE KEY uk_goods_spec (goods_id, spec_value_id),
    KEY idx_goods_id (goods_id),
    KEY idx_spec_value_id (spec_value_id),
    FOREIGN KEY (spec_value_id) REFERENCES prod_specification_value(id) ON DELETE CASCADE
) COMMENT='商品规格关联表'; 