-- 创建商品规格关联表
USE vliascrm;

-- 商品规格关联表（存储商品支持哪些规格项）
CREATE TABLE IF NOT EXISTS prod_goods_specification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    spec_item_id BIGINT NOT NULL COMMENT '规格项ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by VARCHAR(50) COMMENT '创建人',
    updated_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    
    UNIQUE KEY uk_goods_spec (goods_id, spec_item_id),
    INDEX idx_goods_id (goods_id),
    INDEX idx_spec_item_id (spec_item_id),
    
    CONSTRAINT fk_goods_spec_goods FOREIGN KEY (goods_id) REFERENCES prod_goods(id),
    CONSTRAINT fk_goods_spec_item FOREIGN KEY (spec_item_id) REFERENCES prod_specification_item(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品规格关联表';

-- 商品规格值关联表（存储商品的具体规格值组合，用于生成SKU）
CREATE TABLE IF NOT EXISTS prod_goods_specification_value (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    spec_item_id BIGINT NOT NULL COMMENT '规格项ID',
    spec_value_id BIGINT NOT NULL COMMENT '规格值ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by VARCHAR(50) COMMENT '创建人',
    updated_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    
    UNIQUE KEY uk_goods_spec_value (goods_id, spec_item_id, spec_value_id),
    INDEX idx_goods_id (goods_id),
    INDEX idx_spec_item_id (spec_item_id),
    INDEX idx_spec_value_id (spec_value_id),
    
    CONSTRAINT fk_goods_spec_value_goods FOREIGN KEY (goods_id) REFERENCES prod_goods(id),
    CONSTRAINT fk_goods_spec_value_item FOREIGN KEY (spec_item_id) REFERENCES prod_specification_item(id),
    CONSTRAINT fk_goods_spec_value_value FOREIGN KEY (spec_value_id) REFERENCES prod_specification_value(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品规格值关联表';

-- 验证表创建
SHOW TABLES LIKE 'prod_goods_specification%'; 