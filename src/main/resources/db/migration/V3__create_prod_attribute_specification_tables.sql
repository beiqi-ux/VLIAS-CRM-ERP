-- VLIAS CRM 商品属性和规格表创建脚本
-- V3__create_prod_attribute_specification_tables.sql

-- 创建商品属性表
CREATE TABLE prod_attribute (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '属性ID',
    attribute_name VARCHAR(50) NOT NULL COMMENT '属性名称',
    attribute_code VARCHAR(50) NOT NULL COMMENT '属性编码',
    description TEXT COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_attribute_name (attribute_name),
    KEY idx_attribute_code (attribute_code),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) COMMENT='商品属性表';

-- 创建商品规格表
CREATE TABLE prod_specification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规格ID',
    specification_name VARCHAR(50) NOT NULL COMMENT '规格名称',
    specification_code VARCHAR(50) NOT NULL COMMENT '规格编码',
    description TEXT COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by VARCHAR(50) COMMENT '创建人',
    update_by VARCHAR(50) COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_specification_name (specification_name),
    KEY idx_specification_code (specification_code),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) COMMENT='商品规格表'; 