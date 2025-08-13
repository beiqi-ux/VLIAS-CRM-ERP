-- 3级规格管理表结构
-- 1. 规格分类表 - 存储规格大类（镜框尺寸、镜片参数、颜色等）
DROP TABLE IF EXISTS prod_specification_category;
CREATE TABLE prod_specification_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规格分类ID',
    category_code VARCHAR(50) NOT NULL COMMENT '分类代码 (frame_size, lens_param, color等)',
    category_name VARCHAR(100) NOT NULL COMMENT '分类名称 (镜框尺寸, 镜片参数, 颜色等)',
    description VARCHAR(500) COMMENT '分类描述',
    sort_order INT DEFAULT 0 COMMENT '排序值',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY uk_category_code (category_code),
    KEY idx_sort_order (sort_order),
    KEY idx_status (status)
) COMMENT='商品规格分类表';

-- 2. 规格项表 - 存储具体的规格项（镜宽、镜高、鼻梁宽等）
DROP TABLE IF EXISTS prod_specification_item;
CREATE TABLE prod_specification_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规格项ID',
    category_id BIGINT NOT NULL COMMENT '所属规格分类ID',
    item_code VARCHAR(50) NOT NULL COMMENT '规格项代码 (frame_width, frame_height, bridge_width等)',
    item_name VARCHAR(100) NOT NULL COMMENT '规格项名称 (镜宽, 镜高, 鼻梁宽等)',
    item_desc VARCHAR(500) COMMENT '规格项描述',
    unit VARCHAR(20) COMMENT '单位 (mm, g, 度等)',
    data_type TINYINT DEFAULT 1 COMMENT '数据类型 1-数值 2-文本 3-枚举',
    is_required TINYINT DEFAULT 0 COMMENT '是否必填 0-否 1-是',
    is_sku TINYINT DEFAULT 0 COMMENT '是否影响SKU 0-否 1-是',
    is_searchable TINYINT DEFAULT 0 COMMENT '是否可搜索 0-否 1-是',
    sort_order INT DEFAULT 0 COMMENT '排序值',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY uk_category_item (category_id, item_code),
    KEY idx_category_id (category_id),
    KEY idx_sort_order (sort_order),
    KEY idx_status (status),
    FOREIGN KEY (category_id) REFERENCES prod_specification_category(id) ON DELETE CASCADE
) COMMENT='商品规格项表';

-- 3. 规格值表 - 存储具体的规格值（140mm、145mm、黑色等）
DROP TABLE IF EXISTS prod_specification_value;
CREATE TABLE prod_specification_value (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规格值ID',
    item_id BIGINT NOT NULL COMMENT '所属规格项ID',
    value_code VARCHAR(50) NOT NULL COMMENT '规格值代码',
    value_name VARCHAR(100) NOT NULL COMMENT '规格值名称',
    value_desc VARCHAR(500) COMMENT '规格值描述',
    numeric_value DECIMAL(10,2) COMMENT '数值型值（用于排序和计算）',
    text_value VARCHAR(200) COMMENT '文本型值',
    sort_order INT DEFAULT 0 COMMENT '排序值',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY uk_item_value (item_id, value_code),
    KEY idx_item_id (item_id),
    KEY idx_sort_order (sort_order),
    KEY idx_status (status),
    KEY idx_numeric_value (numeric_value),
    FOREIGN KEY (item_id) REFERENCES prod_specification_item(id) ON DELETE CASCADE
) COMMENT='商品规格值表';

-- 4. 商品规格关联表 - 商品与规格值的关联关系
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

-- 初始化基础数据
-- 规格分类
INSERT INTO prod_specification_category (category_code, category_name, description, sort_order) VALUES
('frame_size', '镜框尺寸', '眼镜镜框的尺寸参数', 1),
('lens_param', '镜片参数', '镜片的技术参数', 2),
('color', '颜色', '产品颜色选项', 3),
('material', '材质', '产品材质信息', 4);

-- 镜框尺寸规格项
INSERT INTO prod_specification_item (category_id, item_code, item_name, item_desc, unit, data_type, is_required, is_sku, sort_order) VALUES
((SELECT id FROM prod_specification_category WHERE category_code = 'frame_size'), 'frame_width', '镜宽', '镜框宽度（镜片最宽处）', 'mm', 1, 1, 1, 1),
((SELECT id FROM prod_specification_category WHERE category_code = 'frame_size'), 'frame_height', '镜高', '镜框高度（镜片最高处）', 'mm', 1, 1, 1, 2),
((SELECT id FROM prod_specification_category WHERE category_code = 'frame_size'), 'bridge_width', '鼻梁宽', '鼻梁处宽度', 'mm', 1, 1, 1, 3),
((SELECT id FROM prod_specification_category WHERE category_code = 'frame_size'), 'temple_length', '镜腿长', '镜腿长度', 'mm', 1, 0, 0, 4);

-- 镜片参数规格项
INSERT INTO prod_specification_item (category_id, item_code, item_name, item_desc, unit, data_type, is_required, is_sku, sort_order) VALUES
((SELECT id FROM prod_specification_category WHERE category_code = 'lens_param'), 'sphere', '球镜度数', '近视/远视度数', '度', 1, 0, 1, 1),
((SELECT id FROM prod_specification_category WHERE category_code = 'lens_param'), 'cylinder', '柱镜度数', '散光度数', '度', 1, 0, 1, 2),
((SELECT id FROM prod_specification_category WHERE category_code = 'lens_param'), 'axis', '轴位', '散光轴位', '度', 1, 0, 1, 3),
((SELECT id FROM prod_specification_category WHERE category_code = 'lens_param'), 'add', '下加光', '渐进镜片下加光度数', '度', 1, 0, 1, 4);

-- 颜色规格项
INSERT INTO prod_specification_item (category_id, item_code, item_name, item_desc, data_type, is_required, is_sku, sort_order) VALUES
((SELECT id FROM prod_specification_category WHERE category_code = 'color'), 'frame_color', '镜框颜色', '镜框颜色选项', 3, 1, 1, 1),
((SELECT id FROM prod_specification_category WHERE category_code = 'color'), 'lens_color', '镜片颜色', '镜片颜色选项', 3, 0, 1, 2);

-- 材质规格项
INSERT INTO prod_specification_item (category_id, item_code, item_name, item_desc, data_type, is_required, is_sku, sort_order) VALUES
((SELECT id FROM prod_specification_category WHERE category_code = 'material'), 'frame_material', '镜框材质', '镜框材质类型', 3, 1, 1, 1),
((SELECT id FROM prod_specification_category WHERE category_code = 'material'), 'lens_material', '镜片材质', '镜片材质类型', 3, 0, 1, 2);

-- 规格值示例数据
-- 镜宽值
INSERT INTO prod_specification_value (item_id, value_code, value_name, numeric_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_width'), 'width_140', '140mm', 140, 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_width'), 'width_145', '145mm', 145, 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_width'), 'width_150', '150mm', 150, 3),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_width'), 'width_155', '155mm', 155, 4);

-- 镜高值
INSERT INTO prod_specification_value (item_id, value_code, value_name, numeric_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_height'), 'height_45', '45mm', 45, 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_height'), 'height_50', '50mm', 50, 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_height'), 'height_55', '55mm', 55, 3);

-- 鼻梁宽值
INSERT INTO prod_specification_value (item_id, value_code, value_name, numeric_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'bridge_width'), 'bridge_18', '18mm', 18, 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'bridge_width'), 'bridge_20', '20mm', 20, 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'bridge_width'), 'bridge_22', '22mm', 22, 3);

-- 镜框颜色值
INSERT INTO prod_specification_value (item_id, value_code, value_name, text_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_color'), 'black', '黑色', '黑色', 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_color'), 'brown', '棕色', '棕色', 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_color'), 'gold', '金色', '金色', 3),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_color'), 'silver', '银色', '银色', 4);

-- 镜框材质值
INSERT INTO prod_specification_value (item_id, value_code, value_name, text_value, sort_order) VALUES
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_material'), 'titanium', '钛合金', '钛合金', 1),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_material'), 'acetate', '醋酸纤维', '醋酸纤维', 2),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_material'), 'stainless', '不锈钢', '不锈钢', 3),
((SELECT id FROM prod_specification_item WHERE item_code = 'frame_material'), 'plastic', '塑料', '塑料', 4); 