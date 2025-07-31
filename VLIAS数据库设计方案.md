# VLIASCRM系统数据库表设计（完整版）

## 目录与功能模块说明

本文档详细设计了VLIASCRM系统的数据库表结构，包含以下19个功能模块：

1. **系统基础表** - 提供系统用户、角色、权限等基础功能支持
2. **客户关系管理(CRM)表** - 管理客户信息、跟进记录和销售线索
3. **商品管理表** - 维护商品信息、分类、品牌和SKU管理
4. **库存管理表** - 支持库存记录、出入库、盘点和调拨功能
5. **订单管理表** - 处理订单创建、支付、物流和售后流程
6. **采购管理表** - 管理供应商、采购订单和入库业务
7. **促销活动表** - 支持优惠券、满减、秒杀和拼团等营销活动
8. **财务管理表** - 处理账单、结算、发票和收支记录
9. **会员管理表** - 管理会员信息、积分、成长值和会员等级
10. **系统运营表** - 提供消息通知、日志记录和系统参数配置
11. **分销/推广系统表** - 支持多级分销、佣金计算和推广码管理
12. **售后服务表** - 管理工单处理、维修记录和客服评价
13. **报表与数据分析表** - 提供销售、客户和商品数据统计分析
14. **多语言/国际化表** - 支持多语言界面和多币种功能
15. **多组织/多门店表** - 实现多级组织架构和多门店管理
16. **API与集成表** - 管理API接口配置和第三方平台集成
17. **移动端/小程序表** - 支持移动设备管理、版本控制和推送功能
18. **文件管理表** - 处理文件上传、存储、分类和分享
19. **其他配套表** - 提供银行、协议、短信和邮件等辅助功能

## 目录与功能模块数据库文件命名

本文档详细介绍了功能模块的整体模块文件命名(Navicat命名文件)

1. **系统基础表** - System Base Tables
2. **客户关系管理(CRM)表** - CRM Database Table Structure
3. **商品管理表** - Product Management Table Schema
4. **库存管理表** - InventoryManagement
5. **订单管理表** - OrderManagement
6. **采购管理表** - ProcurementManagement
7. **促销活动表** - PromotionActivities
8. **财务管理表** - FinancialManagement
9. **会员管理表** - MemberManagement
10. **系统运营表** - SystemOperation
11. **分销/推广系统表** - DistributionPromotion
12. **售后服务表** - AfterSalesService
13. **报表与数据分析表** - ReportDataAnalysis
14. **多语言/国际化表** - MultilingualI18n
15. **多组织/多门店表** - MultiOrgStore
16. **API与集成表** - ApiIntegration
17. **移动端/小程序表** - MobileMiniprogram
18. **文件管理表** - FileManagement
19. **其他配套表** - OtherSupportingTables

根据技术栈说明和数据库设计文档，以下是系统所有模块的完整表设计：

## 一、系统基础表

### 1. 用户表(sys_user)
```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    avatar VARCHAR(255) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
    mobile VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    org_id BIGINT COMMENT '所属组织ID',
    dept_id BIGINT COMMENT '所属部门ID',
    position_id BIGINT COMMENT '岗位ID',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (username),
    KEY idx_org_id (org_id),
    KEY idx_dept_id (dept_id)
) COMMENT='系统用户表';
```

### 2. 角色表(sys_role)
```sql
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '角色ID',
    role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    org_id BIGINT COMMENT '所属组织ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (role_code),
    KEY idx_org_id (org_id)
) COMMENT='角色表';
```

### 3. 权限表(sys_permission)
```sql
CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    permission_name VARCHAR(50) NOT NULL COMMENT '权限名称',
    permission_code VARCHAR(50) NOT NULL COMMENT '权限编码',
    permission_type TINYINT NOT NULL COMMENT '权限类型 1-一级权限(模块) 2-二级权限(操作)',
    parent_id BIGINT DEFAULT 0 COMMENT '父权限ID',
    menu_id BIGINT COMMENT '关联菜单ID',
    description VARCHAR(200) COMMENT '权限描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (permission_code),
    KEY idx_parent_id (parent_id),
    KEY idx_menu_id (menu_id)
) COMMENT='权限表';
```

### 4. 用户角色关联表(sys_user_role)
```sql
CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    UNIQUE KEY (user_id, role_id),
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) COMMENT='用户角色关联表';
```

### 5. 角色权限关联表(sys_role_permission)
```sql
CREATE TABLE sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    UNIQUE KEY (role_id, permission_id),
    KEY idx_role_id (role_id),
    KEY idx_permission_id (permission_id)
) COMMENT='角色权限关联表';
```

### 6. 菜单表(sys_menu)
```sql
CREATE TABLE sys_menu (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '菜单ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父菜单ID',
    menu_name VARCHAR(50) NOT NULL COMMENT '菜单名称',
    menu_code VARCHAR(50) NOT NULL COMMENT '菜单编码',
    menu_type TINYINT NOT NULL COMMENT '菜单类型 1-目录 2-菜单 3-按钮',
    path VARCHAR(200) COMMENT '路由地址',
    component VARCHAR(200) COMMENT '组件路径',
    icon VARCHAR(100) COMMENT '图标',
    sort INT DEFAULT 0 COMMENT '排序',
    visible TINYINT DEFAULT 1 COMMENT '是否显示 0-隐藏 1-显示',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    permission_code VARCHAR(50) COMMENT '权限标识',
    is_frame TINYINT DEFAULT 0 COMMENT '是否外链 0-否 1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (menu_code),
    KEY idx_parent_id (parent_id)
) COMMENT='菜单表';
```

### 7. 组织机构表(sys_organization)
```sql
CREATE TABLE sys_organization (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '组织ID',
    parent_id BIGINT DEFAULT 0 COMMENT '父组织ID',
    org_name VARCHAR(50) NOT NULL COMMENT '组织名称',
    org_code VARCHAR(50) NOT NULL COMMENT '组织编码',
    org_type TINYINT COMMENT '组织类型 1-集团 2-公司 3-分公司 4-部门',
    leader VARCHAR(50) COMMENT '负责人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(200) COMMENT '地址',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (org_code),
    KEY idx_parent_id (parent_id)
) COMMENT='组织机构表';
```

### 8. 数据字典表(sys_dict)
```sql
CREATE TABLE sys_dict (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典ID',
    dict_name VARCHAR(50) NOT NULL COMMENT '字典名称',
    dict_code VARCHAR(50) NOT NULL COMMENT '字典编码',
    description VARCHAR(200) COMMENT '字典描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (dict_code)
) COMMENT='数据字典表';
```

### 9. 数据字典项表(sys_dict_item)
```sql
CREATE TABLE sys_dict_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典项ID',
    dict_id BIGINT NOT NULL COMMENT '字典ID',
    item_text VARCHAR(50) NOT NULL COMMENT '字典项文本',
    item_value VARCHAR(50) NOT NULL COMMENT '字典项值',
    description VARCHAR(200) COMMENT '字典项描述',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_dict_id (dict_id)
) COMMENT='数据字典项表';
```

## 二、客户关系管理(CRM)表

### 1. 客户表(crm_customer)
```sql
CREATE TABLE crm_customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '客户ID',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    customer_code VARCHAR(50) COMMENT '客户编码',
    customer_type TINYINT COMMENT '客户类型 1-个人 2-企业',
    industry_id BIGINT COMMENT '所属行业ID',
    source_id BIGINT COMMENT '客户来源ID',
    level_id BIGINT COMMENT '客户等级ID',
    status TINYINT DEFAULT 1 COMMENT '状态 1-潜在 2-意向 3-成交 4-流失',
    score INT DEFAULT 0 COMMENT '客户评分',
    owner_user_id BIGINT COMMENT '负责人ID',
    is_public TINYINT DEFAULT 0 COMMENT '是否公海 0-否 1-是',
    next_follow_time DATETIME COMMENT '下次跟进时间',
    mobile VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    address VARCHAR(200) COMMENT '地址',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_owner_user_id (owner_user_id),
    KEY idx_customer_name (customer_name),
    KEY idx_level_id (level_id),
    KEY idx_is_public (is_public)
) COMMENT='客户表';
```

### 2. 客户联系人表(crm_contact)
```sql
CREATE TABLE crm_contact (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '联系人ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    name VARCHAR(50) NOT NULL COMMENT '联系人姓名',
    position VARCHAR(50) COMMENT '职位',
    mobile VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    is_primary TINYINT DEFAULT 0 COMMENT '是否主联系人 0-否 1-是',
    gender TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
    birthday DATE COMMENT '生日',
    wechat VARCHAR(50) COMMENT '微信号',
    qq VARCHAR(20) COMMENT 'QQ号',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_customer_id (customer_id)
) COMMENT='客户联系人表';
```

### 3. 客户标签表(crm_tag)
```sql
CREATE TABLE crm_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '标签ID',
    tag_name VARCHAR(50) NOT NULL COMMENT '标签名称',
    tag_color VARCHAR(20) COMMENT '标签颜色',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (tag_name)
) COMMENT='客户标签表';
```

### 4. 客户标签关联表(crm_customer_tag)
```sql
CREATE TABLE crm_customer_tag (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    tag_id BIGINT NOT NULL COMMENT '标签ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    UNIQUE KEY (customer_id, tag_id),
    KEY idx_customer_id (customer_id),
    KEY idx_tag_id (tag_id)
) COMMENT='客户标签关联表';
```

### 5. 客户分组表(crm_group)
```sql
CREATE TABLE crm_group (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分组ID',
    group_name VARCHAR(50) NOT NULL COMMENT '分组名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分组ID',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_parent_id (parent_id)
) COMMENT='客户分组表';
```

### 6. 客户分组关联表(crm_customer_group)
```sql
CREATE TABLE crm_customer_group (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    group_id BIGINT NOT NULL COMMENT '分组ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    UNIQUE KEY (customer_id, group_id),
    KEY idx_customer_id (customer_id),
    KEY idx_group_id (group_id)
) COMMENT='客户分组关联表';
```

### 7. 客户跟进记录表(crm_follow_record)
```sql
CREATE TABLE crm_follow_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '跟进记录ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    follow_type TINYINT COMMENT '跟进方式 1-电话 2-邮件 3-拜访 4-会议 5-其他',
    follow_content TEXT NOT NULL COMMENT '跟进内容',
    next_follow_time DATETIME COMMENT '下次跟进时间',
    files VARCHAR(500) COMMENT '附件',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_customer_id (customer_id),
    KEY idx_create_by (create_by)
) COMMENT='客户跟进记录表';
```

### 8. 客户线索表(crm_lead)
```sql
CREATE TABLE crm_lead (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '线索ID',
    lead_name VARCHAR(100) NOT NULL COMMENT '线索名称',
    contact_name VARCHAR(50) COMMENT '联系人姓名',
    mobile VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    source_id BIGINT COMMENT '线索来源ID',
    status TINYINT DEFAULT 1 COMMENT '状态 1-未跟进 2-跟进中 3-已转化 4-已关闭',
    owner_user_id BIGINT COMMENT '负责人ID',
    content TEXT COMMENT '线索内容',
    address VARCHAR(200) COMMENT '地址',
    is_transformed TINYINT DEFAULT 0 COMMENT '是否已转化 0-否 1-是',
    customer_id BIGINT COMMENT '转化后客户ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_owner_user_id (owner_user_id),
    KEY idx_status (status),
    KEY idx_is_transformed (is_transformed)
) COMMENT='客户线索表';
```

### 9. 客户公海池表(crm_public_pool)
```sql
CREATE TABLE crm_public_pool (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    reason_type TINYINT COMMENT '进入公海原因 1-超时未跟进 2-手动释放 3-离职释放',
    reason VARCHAR(200) COMMENT '进入原因',
    previous_owner_id BIGINT COMMENT '前负责人ID',
    enter_time DATETIME NOT NULL COMMENT '进入公海时间',
    status TINYINT DEFAULT 1 COMMENT '状态 1-待领取 2-已领取',
    receive_user_id BIGINT COMMENT '领取人ID',
    receive_time DATETIME COMMENT '领取时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    KEY idx_customer_id (customer_id),
    KEY idx_status (status),
    KEY idx_previous_owner_id (previous_owner_id),
    KEY idx_receive_user_id (receive_user_id)
) COMMENT='客户公海池表';
```

## 三、商品管理表

### 1. 商品表(prod_goods)
```sql
CREATE TABLE prod_goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    goods_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    goods_code VARCHAR(50) COMMENT '商品编码',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    brand_id BIGINT COMMENT '品牌ID',
    goods_type TINYINT DEFAULT 1 COMMENT '商品类型 1-实物商品 2-虚拟商品',
    unit VARCHAR(20) COMMENT '单位',
    weight DECIMAL(10,2) COMMENT '重量(kg)',
    volume DECIMAL(10,2) COMMENT '体积(m³)',
    original_price DECIMAL(10,2) COMMENT '原价',
    selling_price DECIMAL(10,2) NOT NULL COMMENT '售价',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    min_price DECIMAL(10,2) COMMENT '最低售价',
    stock_qty INT DEFAULT 0 COMMENT '库存数量',
    warn_stock INT DEFAULT 0 COMMENT '库存预警值',
    sale_qty INT DEFAULT 0 COMMENT '销量',
    status TINYINT DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    is_recommended TINYINT DEFAULT 0 COMMENT '是否推荐 0-否 1-是',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热销 0-否 1-是',
    is_new TINYINT DEFAULT 0 COMMENT '是否新品 0-否 1-是',
    keywords VARCHAR(200) COMMENT '关键词',
    tags VARCHAR(200) COMMENT '标签',
    main_image VARCHAR(255) COMMENT '主图',
    video_url VARCHAR(255) COMMENT '视频URL',
    brief VARCHAR(255) COMMENT '简介',
    description TEXT COMMENT '详情描述',
    remark TEXT COMMENT '备注',
    sort INT DEFAULT 0 COMMENT '排序',
    audit_status TINYINT DEFAULT 0 COMMENT '审核状态 0-未审核 1-审核通过 2-审核拒绝',
    audit_time DATETIME COMMENT '审核时间',
    audit_user_id BIGINT COMMENT '审核人ID',
    audit_remark VARCHAR(200) COMMENT '审核备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_category_id (category_id),
    KEY idx_brand_id (brand_id),
    KEY idx_goods_name (goods_name),
    KEY idx_status (status),
    KEY idx_audit_status (audit_status)
) COMMENT='商品表';
```

### 2. 商品分类表(prod_category)
```sql
CREATE TABLE prod_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    level INT DEFAULT 1 COMMENT '层级 1-一级 2-二级 3-三级',
    icon VARCHAR(255) COMMENT '图标',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    is_show TINYINT DEFAULT 1 COMMENT '是否显示 0-否 1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_parent_id (parent_id)
) COMMENT='商品分类表';
```

### 3. 商品品牌表(prod_brand)
```sql
CREATE TABLE prod_brand (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '品牌ID',
    brand_name VARCHAR(50) NOT NULL COMMENT '品牌名称',
    brand_logo VARCHAR(255) COMMENT '品牌LOGO',
    description VARCHAR(200) COMMENT '品牌描述',
    website VARCHAR(100) COMMENT '品牌网址',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (brand_name)
) COMMENT='商品品牌表';
```

### 4. 商品规格表(prod_specification)
```sql
CREATE TABLE prod_specification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '规格ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    spec_name VARCHAR(50) NOT NULL COMMENT '规格名称',
    spec_values TEXT COMMENT '规格值JSON',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_goods_id (goods_id)
) COMMENT='商品规格表';
```

### 5. 商品SKU表(prod_sku)
```sql
CREATE TABLE prod_sku (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'SKU ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_code VARCHAR(50) COMMENT 'SKU编码',
    spec_values TEXT COMMENT '规格值JSON',
    original_price DECIMAL(10,2) COMMENT '原价',
    selling_price DECIMAL(10,2) NOT NULL COMMENT '售价',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    stock_qty INT DEFAULT 0 COMMENT '库存数量',
    warn_stock INT DEFAULT 0 COMMENT '库存预警值',
    sku_image VARCHAR(255) COMMENT 'SKU图片',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_goods_id (goods_id),
    KEY idx_sku_code (sku_code)
) COMMENT='商品SKU表';
```

### 6. 商品属性表(prod_attribute)
```sql
CREATE TABLE prod_attribute (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '属性ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    attr_name VARCHAR(50) NOT NULL COMMENT '属性名',
    attr_value VARCHAR(200) COMMENT '属性值',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_goods_id (goods_id)
) COMMENT='商品属性表';
```

### 7. 商品图片表(prod_image)
```sql
CREATE TABLE prod_image (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '图片ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    image_url VARCHAR(255) NOT NULL COMMENT '图片URL',
    sort INT DEFAULT 0 COMMENT '排序',
    is_main TINYINT DEFAULT 0 COMMENT '是否主图 0-否 1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_goods_id (goods_id)
) COMMENT='商品图片表';
```

### 8. 商品评价表(prod_review)
```sql
CREATE TABLE prod_review (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评价ID',
    order_id BIGINT COMMENT '订单ID',
    order_item_id BIGINT COMMENT '订单明细ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    content TEXT COMMENT '评价内容',
    images VARCHAR(500) COMMENT '评价图片，多个以逗号分隔',
    star TINYINT DEFAULT 5 COMMENT '评分 1-5',
    is_anonymous TINYINT DEFAULT 0 COMMENT '是否匿名 0-否 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态 0-未审核 1-已审核 2-拒绝',
    reply_content TEXT COMMENT '商家回复',
    reply_time DATETIME COMMENT '回复时间',
    reply_user_id BIGINT COMMENT '回复人ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_goods_id (goods_id),
    KEY idx_member_id (member_id),
    KEY idx_order_id (order_id)
) COMMENT='商品评价表';
```

## 四、库存管理表

### 1. 仓库表(inv_warehouse)
```sql
CREATE TABLE inv_warehouse (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '仓库ID',
    warehouse_name VARCHAR(50) NOT NULL COMMENT '仓库名称',
    warehouse_code VARCHAR(50) COMMENT '仓库编码',
    contact VARCHAR(50) COMMENT '联系人',
    mobile VARCHAR(20) COMMENT '联系电话',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    address VARCHAR(200) COMMENT '详细地址',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认仓库 0-否 1-是',
    sort INT DEFAULT 0 COMMENT '排序',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (warehouse_code)
) COMMENT='仓库表';
```

### 2. 库区表(inv_area)
```sql
CREATE TABLE inv_area (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库区ID',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    area_name VARCHAR(50) NOT NULL COMMENT '库区名称',
    area_code VARCHAR(50) COMMENT '库区编码',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    sort INT DEFAULT 0 COMMENT '排序',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_warehouse_id (warehouse_id),
    UNIQUE KEY (warehouse_id, area_code)
) COMMENT='库区表';
```

### 3. 库位表(inv_location)
```sql
CREATE TABLE inv_location (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '库位ID',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    area_id BIGINT COMMENT '库区ID',
    location_name VARCHAR(50) NOT NULL COMMENT '库位名称',
    location_code VARCHAR(50) COMMENT '库位编码',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    sort INT DEFAULT 0 COMMENT '排序',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_area_id (area_id),
    UNIQUE KEY (warehouse_id, location_code)
) COMMENT='库位表';
```

### 4. 库存表(inv_stock)
```sql
CREATE TABLE inv_stock (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    stock_qty INT DEFAULT 0 COMMENT '库存数量',
    lock_qty INT DEFAULT 0 COMMENT '锁定数量',
    available_qty INT DEFAULT 0 COMMENT '可用数量',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (warehouse_id, goods_id, sku_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='库存表';
```

### 5. 库存流水表(inv_stock_flow)
```sql
CREATE TABLE inv_stock_flow (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    batch_number VARCHAR(50) COMMENT '批次号',
    flow_type TINYINT NOT NULL COMMENT '流水类型 1-入库 2-出库 3-调拨 4-盘点 5-锁定 6-解锁',
    change_qty INT NOT NULL COMMENT '变动数量',
    before_qty INT COMMENT '变动前数量',
    after_qty INT COMMENT '变动后数量',
    order_id BIGINT COMMENT '关联订单ID',
    order_item_id BIGINT COMMENT '关联订单项ID',
    ref_id BIGINT COMMENT '关联单据ID',
    ref_type VARCHAR(50) COMMENT '关联单据类型',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id),
    KEY idx_flow_type (flow_type),
    KEY idx_ref_id (ref_id)
) COMMENT='库存流水表';
```

### 6. 库存盘点表(inv_check)
```sql
CREATE TABLE inv_check (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '盘点单ID',
    check_no VARCHAR(50) NOT NULL COMMENT '盘点单号',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    status TINYINT DEFAULT 1 COMMENT '状态 1-草稿 2-待审核 3-已审核 4-已完成 5-已取消',
    check_time DATE COMMENT '盘点日期',
    begin_time DATETIME COMMENT '盘点开始时间',
    end_time DATETIME COMMENT '盘点结束时间',
    check_user_id BIGINT COMMENT '盘点人',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (check_no),
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_status (status)
) COMMENT='库存盘点表';
```

### 7. 库存盘点明细表(inv_check_item)
```sql
CREATE TABLE inv_check_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    check_id BIGINT NOT NULL COMMENT '盘点单ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    batch_number VARCHAR(50) COMMENT '批次号',
    system_qty INT DEFAULT 0 COMMENT '系统数量',
    actual_qty INT DEFAULT 0 COMMENT '实际数量',
    diff_qty INT DEFAULT 0 COMMENT '差异数量',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    KEY idx_check_id (check_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='库存盘点明细表';
```

### 8. 库存调拨表(inv_transfer)
```sql
CREATE TABLE inv_transfer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '调拨单ID',
    transfer_no VARCHAR(50) NOT NULL COMMENT '调拨单号',
    from_warehouse_id BIGINT NOT NULL COMMENT '源仓库ID',
    to_warehouse_id BIGINT NOT NULL COMMENT '目标仓库ID',
    status TINYINT DEFAULT 1 COMMENT '状态 1-草稿 2-待出库 3-待入库 4-已完成 5-已取消',
    transfer_time DATE COMMENT '调拨日期',
    out_time DATETIME COMMENT '出库时间',
    in_time DATETIME COMMENT '入库时间',
    transfer_user_id BIGINT COMMENT '调拨人',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (transfer_no),
    KEY idx_from_warehouse_id (from_warehouse_id),
    KEY idx_to_warehouse_id (to_warehouse_id),
    KEY idx_status (status)
) COMMENT='库存调拨表';
```

### 9. 库存调拨明细表(inv_transfer_item)
```sql
CREATE TABLE inv_transfer_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    transfer_id BIGINT NOT NULL COMMENT '调拨单ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    batch_number VARCHAR(50) COMMENT '批次号',
    transfer_qty INT NOT NULL COMMENT '调拨数量',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    KEY idx_transfer_id (transfer_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='库存调拨明细表';
```

### 10. 批次/序列号表(inv_batch)
```SQL
CREATE TABLE inv_batch (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    batch_number VARCHAR(50) NOT NULL COMMENT '批次号/序列号',
    stock_qty INT DEFAULT 0 COMMENT '库存数量',
    production_date DATE COMMENT '生产日期',
    expiry_date DATE COMMENT '到期日期',
    entry_time DATETIME COMMENT '入库时间',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (warehouse_id, goods_id, sku_id, batch_number),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id),
    KEY idx_batch_number (batch_number)
) COMMENT='批次/序列号表';
```

## 五、订单管理表

### 1. 订单主表(ord_order)
```SQL
CREATE TABLE ord_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    order_type TINYINT DEFAULT 1 COMMENT '订单类型 1-普通 2-秒杀 3-拼团 4-积分兑换',
    order_source TINYINT DEFAULT 1 COMMENT '订单来源 1-PC 2-APP 3-小程序 4-H5',
    order_status TINYINT DEFAULT 1 COMMENT '订单状态 1-待付款 2-待发货 3-待收货 4-已完成 5-已取消 6-已关闭',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态 0-未支付 1-已支付 2-已退款 3-部分退款',
    delivery_status TINYINT DEFAULT 0 COMMENT '发货状态 0-未发货 1-已发货 2-已收货',
    total_amount DECIMAL(10,2) DEFAULT 0 COMMENT '订单总金额',
    goods_amount DECIMAL(10,2) DEFAULT 0 COMMENT '商品总金额',
    freight_amount DECIMAL(10,2) DEFAULT 0 COMMENT '运费金额',
    discount_amount DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额',
    coupon_amount DECIMAL(10,2) DEFAULT 0 COMMENT '优惠券金额',
    points_amount DECIMAL(10,2) DEFAULT 0 COMMENT '积分抵扣金额',
    pay_amount DECIMAL(10,2) DEFAULT 0 COMMENT '实付金额',
    pay_time DATETIME COMMENT '支付时间',
    pay_type TINYINT COMMENT '支付方式 1-微信 2-支付宝 3-银联 4-余额',
    transaction_id VARCHAR(100) COMMENT '支付交易号',
    consignee VARCHAR(50) COMMENT '收货人',
    mobile VARCHAR(20) COMMENT '联系电话',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    address VARCHAR(200) COMMENT '详细地址',
    zip_code VARCHAR(20) COMMENT '邮编',
    delivery_company VARCHAR(50) COMMENT '物流公司',
    delivery_no VARCHAR(50) COMMENT '物流单号',
    delivery_time DATETIME COMMENT '发货时间',
    receive_time DATETIME COMMENT '收货时间',
    complete_time DATETIME COMMENT '完成时间',
    cancel_time DATETIME COMMENT '取消时间',
    cancel_reason VARCHAR(200) COMMENT '取消原因',
    buyer_remark VARCHAR(200) COMMENT '买家备注',
    seller_remark VARCHAR(200) COMMENT '卖家备注',
    invoice_type TINYINT DEFAULT 0 COMMENT '发票类型 0-不开发票 1-个人 2-企业',
    invoice_title VARCHAR(100) COMMENT '发票抬头',
    invoice_tax_no VARCHAR(50) COMMENT '税号',
    invoice_content VARCHAR(100) COMMENT '发票内容',
    invoice_email VARCHAR(100) COMMENT '发票邮箱',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (order_no),
    KEY idx_member_id (member_id),
    KEY idx_order_status (order_status),
    KEY idx_pay_status (pay_status),
    KEY idx_delivery_status (delivery_status),
    KEY idx_create_time (create_time)
) COMMENT='订单主表';
```

### 2. 订单明细表(ord_order_item)
```SQL
CREATE TABLE ord_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单项ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    goods_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    sku_name VARCHAR(100) COMMENT 'SKU名称',
    goods_image VARCHAR(255) COMMENT '商品图片',
    goods_spec VARCHAR(200) COMMENT '商品规格',
    goods_price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    quantity INT NOT NULL COMMENT '购买数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    discount_amount DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额',
    real_amount DECIMAL(10,2) NOT NULL COMMENT '实际金额',
    refund_status TINYINT DEFAULT 0 COMMENT '退款状态 0-未退款 1-退款中 2-已退款',
    refund_amount DECIMAL(10,2) DEFAULT 0 COMMENT '退款金额',
    comment_status TINYINT DEFAULT 0 COMMENT '评价状态 0-未评价 1-已评价',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='订单明细表';
```

### 3. 订单支付表(ord_payment)
```SQL
CREATE TABLE ord_payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '支付ID',
    pay_no VARCHAR(50) NOT NULL COMMENT '支付流水号',
    order_id BIGINT COMMENT '订单ID',
    order_no VARCHAR(50) COMMENT '订单编号',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    pay_type TINYINT COMMENT '支付方式 1-微信 2-支付宝 3-银联 4-余额',
    pay_amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态 0-未支付 1-已支付 2-已退款 3-部分退款',
    transaction_id VARCHAR(100) COMMENT '支付交易号',
    pay_time DATETIME COMMENT '支付时间',
    expire_time DATETIME COMMENT '过期时间',
    refund_amount DECIMAL(10,2) DEFAULT 0 COMMENT '退款金额',
    client_ip VARCHAR(50) COMMENT '客户端IP',
    device_info VARCHAR(100) COMMENT '设备信息',
    pay_channel_extra TEXT COMMENT '支付渠道额外信息',
    callback_content TEXT COMMENT '回调内容',
    callback_time DATETIME COMMENT '回调时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (pay_no),
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no),
    KEY idx_member_id (member_id),
    KEY idx_transaction_id (transaction_id)
) COMMENT='订单支付表';
```

### 4. 订单物流表(ord_logistics)
```SQL
CREATE TABLE ord_logistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '物流ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    delivery_no VARCHAR(50) NOT NULL COMMENT '物流单号',
    delivery_company VARCHAR(50) COMMENT '物流公司',
    delivery_company_code VARCHAR(50) COMMENT '物流公司编码',
    delivery_status TINYINT DEFAULT 0 COMMENT '物流状态 0-待发货 1-已发货 2-已签收',
    consignee VARCHAR(50) NOT NULL COMMENT '收货人',
    mobile VARCHAR(20) NOT NULL COMMENT '联系电话',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    address VARCHAR(200) NOT NULL COMMENT '详细地址',
    zip_code VARCHAR(20) COMMENT '邮编',
    delivery_time DATETIME COMMENT '发货时间',
    receive_time DATETIME COMMENT '收货时间',
    tracking_info TEXT COMMENT '物流跟踪信息',
    tracking_update_time DATETIME COMMENT '物流更新时间',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no),
    KEY idx_delivery_no (delivery_no)
) COMMENT='订单物流表';
```

### 5. 购物车表(ord_cart)
```SQL
CREATE TABLE ord_cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    quantity INT DEFAULT 1 COMMENT '数量',
    checked TINYINT DEFAULT 1 COMMENT '是否选中 0-未选中 1-已选中',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (member_id, goods_id, sku_id),
    KEY idx_member_id (member_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='购物车表';
```

### 6. 订单退换货表(ord_return)
```SQL
CREATE TABLE ord_return (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '退货ID',
    return_no VARCHAR(50) NOT NULL COMMENT '退货单号',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    order_item_id BIGINT COMMENT '订单项ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    return_type TINYINT NOT NULL COMMENT '退货类型 1-退款 2-退货退款 3-换货',
    return_status TINYINT DEFAULT 1 COMMENT '退货状态 1-申请中 2-待审核 3-已审核 4-已拒绝 5-退款中 6-已退款 7-已完成',
    reason_type TINYINT COMMENT '原因类型',
    reason VARCHAR(200) COMMENT '退货原因',
    description TEXT COMMENT '详细描述',
    evidence VARCHAR(500) COMMENT '凭证图片，多个以逗号分隔',
    return_amount DECIMAL(10,2) COMMENT '退款金额',
    return_qty INT COMMENT '退货数量',
    return_freight_amount DECIMAL(10,2) DEFAULT 0 COMMENT '退运费金额',
    audit_time DATETIME COMMENT '审核时间',
    audit_user_id BIGINT COMMENT '审核人ID',
    audit_remark VARCHAR(200) COMMENT '审核备注',
    reject_reason VARCHAR(200) COMMENT '拒绝原因',
    delivery_company VARCHAR(50) COMMENT '退货物流公司',
    delivery_no VARCHAR(50) COMMENT '退货物流单号',
    delivery_time DATETIME COMMENT '退货发货时间',
    receive_time DATETIME COMMENT '退货签收时间',
    refund_time DATETIME COMMENT '退款时间',
    refund_transaction_id VARCHAR(100) COMMENT '退款交易号',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (return_no),
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no),
    KEY idx_member_id (member_id),
    KEY idx_return_status (return_status)
) COMMENT='订单退换货表';
```

### 7. 订单操作日志表(ord_operation_log)
```SQL
CREATE TABLE ord_operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    operation_type TINYINT NOT NULL COMMENT '操作类型',
    operation_content VARCHAR(500) NOT NULL COMMENT '操作内容',
    operator_id BIGINT COMMENT '操作人ID',
    operator_type TINYINT DEFAULT 1 COMMENT '操作人类型 1-系统 2-用户 3-管理员',
    operator_name VARCHAR(50) COMMENT '操作人名称',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no)
) COMMENT='订单操作日志表';
```

## 六、采购管理表

### 1. 供应商表(pur_supplier)
```SQL
CREATE TABLE pur_supplier (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '供应商ID',
    supplier_name VARCHAR(100) NOT NULL COMMENT '供应商名称',
    supplier_code VARCHAR(50) COMMENT '供应商编码',
    contact VARCHAR(50) COMMENT '联系人',
    mobile VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    address VARCHAR(200) COMMENT '详细地址',
    bank_name VARCHAR(100) COMMENT '开户行',
    bank_account VARCHAR(50) COMMENT '银行账号',
    tax_no VARCHAR(50) COMMENT '税号',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    level TINYINT COMMENT '供应商等级',
    credit_level TINYINT COMMENT '信用等级',
    settlement_type TINYINT COMMENT '结算方式 1-现结 2-月结 3-季结',
    settlement_day INT COMMENT '结算天数',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (supplier_code),
    KEY idx_supplier_name (supplier_name)
) COMMENT='供应商表';
```

### 2. 供应商商品表(pur_supplier_goods)
```SQL
CREATE TABLE pur_supplier_goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    supplier_goods_code VARCHAR(50) COMMENT '供应商商品编码',
    supplier_goods_name VARCHAR(100) COMMENT '供应商商品名称',
    purchase_price DECIMAL(10,2) NOT NULL COMMENT '采购价',
    min_purchase_qty INT DEFAULT 1 COMMENT '最小采购量',
    delivery_day INT COMMENT '交货天数',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (supplier_id, goods_id, sku_id),
    KEY idx_supplier_id (supplier_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='供应商商品表';
```

### 3. 采购订单表(pur_order)
```SQL
CREATE TABLE pur_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '采购单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '采购单号',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    order_status TINYINT DEFAULT 1 COMMENT '订单状态 1-草稿 2-待审核 3-已审核 4-已下单 5-部分入库 6-已完成 7-已取消',
    warehouse_id BIGINT COMMENT '入库仓库ID',
    total_amount DECIMAL(10,2) DEFAULT 0 COMMENT '订单总金额',
    pay_status TINYINT DEFAULT 0 COMMENT '支付状态 0-未支付 1-部分支付 2-已支付',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已付金额',
    expected_time DATE COMMENT '预计到货日期',
    delivery_status TINYINT DEFAULT 0 COMMENT '发货状态 0-未发货 1-部分发货 2-已发货',
    receipt_status TINYINT DEFAULT 0 COMMENT '入库状态 0-未入库 1-部分入库 2-已入库',
    contact VARCHAR(50) COMMENT '联系人',
    mobile VARCHAR(20) COMMENT '联系电话',
    remark TEXT COMMENT '备注',
    creator_id BIGINT COMMENT '制单人ID',
    audit_id BIGINT COMMENT '审核人ID',
    audit_time DATETIME COMMENT '审核时间',
    audit_remark VARCHAR(200) COMMENT '审核备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (order_no),
    KEY idx_supplier_id (supplier_id),
    KEY idx_order_status (order_status),
    KEY idx_warehouse_id (warehouse_id)
) COMMENT='采购订单表';
```

### 4. 采购订单明细表(pur_order_item)
```SQL
CREATE TABLE pur_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    order_id BIGINT NOT NULL COMMENT '采购单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '采购单号',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    goods_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    sku_name VARCHAR(100) COMMENT 'SKU名称',
    goods_spec VARCHAR(200) COMMENT '商品规格',
    goods_unit VARCHAR(20) COMMENT '单位',
    purchase_price DECIMAL(10,2) NOT NULL COMMENT '采购价',
    quantity INT NOT NULL COMMENT '采购数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    received_qty INT DEFAULT 0 COMMENT '已入库数量',
    remain_qty INT DEFAULT 0 COMMENT '待入库数量',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='采购订单明细表';
```

### 5. 采购入库表(pur_receipt)
```SQL
CREATE TABLE pur_receipt (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '入库单ID',
    receipt_no VARCHAR(50) NOT NULL COMMENT '入库单号',
    order_id BIGINT COMMENT '采购单ID',
    order_no VARCHAR(50) COMMENT '采购单号',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    receipt_status TINYINT DEFAULT 1 COMMENT '入库状态 1-草稿 2-待审核 3-已审核 4-已入库 5-已取消',
    receipt_type TINYINT DEFAULT 1 COMMENT '入库类型 1-采购入库 2-退货入库 3-调拨入库 4-其他入库',
    receipt_time DATE COMMENT '入库日期',
    total_amount DECIMAL(10,2) DEFAULT 0 COMMENT '入库总金额',
    receipt_user_id BIGINT COMMENT '入库人',
    contact VARCHAR(50) COMMENT '联系人',
    mobile VARCHAR(20) COMMENT '联系电话',
    remark TEXT COMMENT '备注',
    audit_id BIGINT COMMENT '审核人ID',
    audit_time DATETIME COMMENT '审核时间',
    audit_remark VARCHAR(200) COMMENT '审核备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (receipt_no),
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no),
    KEY idx_supplier_id (supplier_id),
    KEY idx_warehouse_id (warehouse_id)
) COMMENT='采购入库表';
```

### 6. 采购入库明细表(pur_receipt_item)
```SQL
CREATE TABLE pur_receipt_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    receipt_id BIGINT NOT NULL COMMENT '入库单ID',
    receipt_no VARCHAR(50) NOT NULL COMMENT '入库单号',
    order_id BIGINT COMMENT '采购单ID',
    order_item_id BIGINT COMMENT '采购单项ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    goods_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    sku_name VARCHAR(100) COMMENT 'SKU名称',
    goods_spec VARCHAR(200) COMMENT '商品规格',
    goods_unit VARCHAR(20) COMMENT '单位',
    batch_number VARCHAR(50) COMMENT '批次号',
    production_date DATE COMMENT '生产日期',
    expiry_date DATE COMMENT '到期日期',
    purchase_price DECIMAL(10,2) NOT NULL COMMENT '采购价',
    quantity INT NOT NULL COMMENT '入库数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    location_id BIGINT COMMENT '库位ID',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    KEY idx_receipt_id (receipt_id),
    KEY idx_receipt_no (receipt_no),
    KEY idx_order_id (order_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='采购入库明细表';
```

### 7. 采购退货表(pur_return)
```SQL
CREATE TABLE pur_return (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '退货单ID',
    return_no VARCHAR(50) NOT NULL COMMENT '退货单号',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    warehouse_id BIGINT NOT NULL COMMENT '仓库ID',
    receipt_id BIGINT COMMENT '关联入库单ID',
    receipt_no VARCHAR(50) COMMENT '关联入库单号',
    return_status TINYINT DEFAULT 1 COMMENT '退货状态 1-草稿 2-待审核 3-已审核 4-已退货 5-已取消',
    return_time DATE COMMENT '退货日期',
    total_amount DECIMAL(10,2) DEFAULT 0 COMMENT '退货总金额',
    reason_type TINYINT COMMENT '退货原因类型',
    reason VARCHAR(200) COMMENT '退货原因',
    remark TEXT COMMENT '备注',
    audit_id BIGINT COMMENT '审核人ID',
    audit_time DATETIME COMMENT '审核时间',
    audit_remark VARCHAR(200) COMMENT '审核备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (return_no),
    KEY idx_supplier_id (supplier_id),
    KEY idx_warehouse_id (warehouse_id),
    KEY idx_receipt_id (receipt_id)
) COMMENT='采购退货表';
```

### 8. 采购退货明细表(pur_return_item)
```SQL
CREATE TABLE pur_return_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    return_id BIGINT NOT NULL COMMENT '退货单ID',
    return_no VARCHAR(50) NOT NULL COMMENT '退货单号',
    receipt_id BIGINT COMMENT '关联入库单ID',
    receipt_item_id BIGINT COMMENT '关联入库单项ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    goods_name VARCHAR(100) NOT NULL COMMENT '商品名称',
    sku_name VARCHAR(100) COMMENT 'SKU名称',
    goods_spec VARCHAR(200) COMMENT '商品规格',
    goods_unit VARCHAR(20) COMMENT '单位',
    batch_number VARCHAR(50) COMMENT '批次号',
    purchase_price DECIMAL(10,2) NOT NULL COMMENT '采购价',
    quantity INT NOT NULL COMMENT '退货数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    KEY idx_return_id (return_id),
    KEY idx_return_no (return_no),
    KEY idx_receipt_id (receipt_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='采购退货明细表';
```

### 9. 供应商对账表(pur_reconciliation)
```SQL
CREATE TABLE pur_reconciliation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    reconciliation_no VARCHAR(50) NOT NULL COMMENT '对账单号',
    supplier_id BIGINT NOT NULL COMMENT '供应商ID',
    start_date DATE COMMENT '对账开始日期',
    end_date DATE COMMENT '对账结束日期',
    total_amount DECIMAL(10,2) DEFAULT 0 COMMENT '对账总金额',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已付金额',
    unpaid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '未付金额',
    status TINYINT DEFAULT 1 COMMENT '状态 1-草稿 2-待确认 3-已确认 4-已结算',
    settlement_time DATETIME COMMENT '结算时间',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (reconciliation_no),
    KEY idx_supplier_id (supplier_id)
) COMMENT='供应商对账表';
```

### 10. 供应商对账明细表(pur_reconciliation_item)
```SQL
CREATE TABLE pur_reconciliation_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    reconciliation_id BIGINT NOT NULL COMMENT '对账单ID',
    reconciliation_no VARCHAR(50) NOT NULL COMMENT '对账单号',
    bill_type TINYINT NOT NULL COMMENT '单据类型 1-采购单 2-入库单 3-退货单',
    bill_id BIGINT NOT NULL COMMENT '单据ID',
    bill_no VARCHAR(50) NOT NULL COMMENT '单据编号',
    bill_date DATE COMMENT '单据日期',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_reconciliation_id (reconciliation_id),
    KEY idx_reconciliation_no (reconciliation_no),
    KEY idx_bill_id (bill_id),
    KEY idx_bill_no (bill_no)
) COMMENT='供应商对账明细表';
```

## 七、促销活动表

### 1. 优惠券表(prom_coupon)
```SQL
CREATE TABLE prom_coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '优惠券ID',
    coupon_name VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    coupon_type TINYINT DEFAULT 1 COMMENT '优惠券类型 1-满减券 2-折扣券 3-无门槛券',
    discount_type TINYINT DEFAULT 1 COMMENT '优惠方式 1-固定金额 2-折扣率',
    discount_value DECIMAL(10,2) NOT NULL COMMENT '优惠值（金额/折扣）',
    min_amount DECIMAL(10,2) DEFAULT 0 COMMENT '使用门槛金额',
    max_discount DECIMAL(10,2) COMMENT '最大优惠金额',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    validity_days INT COMMENT '有效天数（领取后）',
    total_qty INT COMMENT '发行总量',
    used_qty INT DEFAULT 0 COMMENT '已使用数量',
    receive_qty INT DEFAULT 0 COMMENT '已领取数量',
    per_limit INT DEFAULT 1 COMMENT '每人限领',
    use_range TINYINT DEFAULT 1 COMMENT '使用范围 1-全场通用 2-指定商品 3-指定分类',
    range_values TEXT COMMENT '范围值JSON',
    status TINYINT DEFAULT 1 COMMENT '状态 0-未启用 1-已启用 2-已结束',
    description TEXT COMMENT '使用说明',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_coupon_type (coupon_type),
    KEY idx_status (status)
) COMMENT='优惠券表';
```

### 2. 会员优惠券表(prom_member_coupon)
```SQL
CREATE TABLE prom_member_coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    coupon_id BIGINT NOT NULL COMMENT '优惠券ID',
    coupon_code VARCHAR(50) NOT NULL COMMENT '优惠券码',
    status TINYINT DEFAULT 1 COMMENT '状态 1-未使用 2-已使用 3-已过期',
    get_type TINYINT DEFAULT 1 COMMENT '获取方式 1-主动领取 2-系统发放 3-活动获得',
    get_time DATETIME NOT NULL COMMENT '获取时间',
    use_time DATETIME COMMENT '使用时间',
    expire_time DATETIME COMMENT '过期时间',
    order_id BIGINT COMMENT '使用订单ID',
    order_no VARCHAR(50) COMMENT '使用订单号',
    use_amount DECIMAL(10,2) COMMENT '实际优惠金额',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_member_id (member_id),
    KEY idx_coupon_id (coupon_id),
    KEY idx_status (status),
    KEY idx_coupon_code (coupon_code)
) COMMENT='会员优惠券表';
```

### 3. 满减活动表(prom_full_reduction)
```SQL
CREATE TABLE prom_full_reduction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    activity_name VARCHAR(100) NOT NULL COMMENT '活动名称',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    status TINYINT DEFAULT 0 COMMENT '状态 0-未开始 1-进行中 2-已结束',
    use_range TINYINT DEFAULT 1 COMMENT '使用范围 1-全场通用 2-指定商品 3-指定分类',
    range_values TEXT COMMENT '范围值JSON',
    rules TEXT NOT NULL COMMENT '满减规则JSON',
    description TEXT COMMENT '活动说明',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_status (status),
    KEY idx_time (start_time, end_time)
) COMMENT='满减活动表';
```

### 4. 秒杀活动表(prom_seckill)
```SQL
CREATE TABLE prom_seckill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    activity_name VARCHAR(100) NOT NULL COMMENT '活动名称',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    status TINYINT DEFAULT 0 COMMENT '状态 0-未开始 1-进行中 2-已结束 3-已取消',
    limit_qty INT DEFAULT 1 COMMENT '每人限购',
    description TEXT COMMENT '活动说明',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_status (status),
    KEY idx_time (start_time, end_time)
) COMMENT='秒杀活动表';
```

### 5. 秒杀商品表(prom_seckill_goods)
```SQL
CREATE TABLE prom_seckill_goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    seckill_id BIGINT NOT NULL COMMENT '秒杀活动ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    seckill_price DECIMAL(10,2) NOT NULL COMMENT '秒杀价格',
    stock_qty INT NOT NULL COMMENT '秒杀库存',
    limit_qty INT DEFAULT 1 COMMENT '每人限购',
    sold_qty INT DEFAULT 0 COMMENT '已售数量',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (seckill_id, goods_id, sku_id),
    KEY idx_seckill_id (seckill_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='秒杀商品表';
```

### 6. 拼团活动表(prom_group_buy)
```SQL
CREATE TABLE prom_group_buy (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    activity_name VARCHAR(100) NOT NULL COMMENT '活动名称',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    status TINYINT DEFAULT 0 COMMENT '状态 0-未开始 1-进行中 2-已结束 3-已取消',
    group_num INT NOT NULL COMMENT '成团人数',
    limit_qty INT DEFAULT 1 COMMENT '每人限购',
    limit_group_qty INT COMMENT '每人限参团',
    group_time INT NOT NULL COMMENT '成团时限(小时)',
    description TEXT COMMENT '活动说明',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_status (status),
    KEY idx_time (start_time, end_time)
) COMMENT='拼团活动表';
```

### 7. 拼团商品表(prom_group_buy_goods)
```SQL
CREATE TABLE prom_group_buy_goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    group_buy_id BIGINT NOT NULL COMMENT '拼团活动ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    group_price DECIMAL(10,2) NOT NULL COMMENT '拼团价格',
    stock_qty INT NOT NULL COMMENT '活动库存',
    limit_qty INT DEFAULT 1 COMMENT '每人限购',
    sold_qty INT DEFAULT 0 COMMENT '已售数量',
    virtual_group_num INT DEFAULT 0 COMMENT '虚拟成团数',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (group_buy_id, goods_id, sku_id),
    KEY idx_group_buy_id (group_buy_id),
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id)
) COMMENT='拼团商品表';
```

### 8. 拼团记录表(prom_group_buy_record)
```SQL
CREATE TABLE prom_group_buy_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    group_buy_id BIGINT NOT NULL COMMENT '拼团活动ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    group_no VARCHAR(50) NOT NULL COMMENT '团编号',
    leader_id BIGINT NOT NULL COMMENT '团长ID',
    required_num INT NOT NULL COMMENT '需要人数',
    current_num INT DEFAULT 1 COMMENT '当前人数',
    status TINYINT DEFAULT 1 COMMENT '状态 1-进行中 2-已成团 3-拼团失败',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    expire_time DATETIME NOT NULL COMMENT '到期时间',
    success_time DATETIME COMMENT '成团时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (group_no),
    KEY idx_group_buy_id (group_buy_id),
    KEY idx_leader_id (leader_id),
    KEY idx_status (status)
) COMMENT='拼团记录表';
```

### 9. 拼团成员表(prom_group_buy_member)
```SQL
CREATE TABLE prom_group_buy_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    group_record_id BIGINT NOT NULL COMMENT '拼团记录ID',
    group_no VARCHAR(50) NOT NULL COMMENT '团编号',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    order_id BIGINT COMMENT '订单ID',
    order_no VARCHAR(50) COMMENT '订单编号',
    is_leader TINYINT DEFAULT 0 COMMENT '是否团长 0-否 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态 1-待支付 2-拼团中 3-已成团 4-未成团',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    KEY idx_group_record_id (group_record_id),
    KEY idx_group_no (group_no),
    KEY idx_member_id (member_id),
    KEY idx_order_id (order_id)
) COMMENT='拼团成员表';
```

### 10. 积分商品表(prom_points_goods)
```SQL
CREATE TABLE prom_points_goods (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    points_price INT NOT NULL COMMENT '积分价格',
    cash_price DECIMAL(10,2) DEFAULT 0 COMMENT '现金价格',
    stock_qty INT NOT NULL COMMENT '兑换库存',
    limit_qty INT DEFAULT 1 COMMENT '每人限兑',
    exchange_qty INT DEFAULT 0 COMMENT '已兑换数量',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME NOT NULL COMMENT '结束时间',
    status TINYINT DEFAULT 0 COMMENT '状态 0-未开始 1-进行中 2-已结束 3-已取消',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_goods_id (goods_id),
    KEY idx_sku_id (sku_id),
    KEY idx_status (status)
) COMMENT='积分商品表';
```

## 八、财务管理表

### 1. 账单表(fin_bill)
```SQL
CREATE TABLE fin_bill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账单ID',
    bill_no VARCHAR(50) NOT NULL COMMENT '账单编号',
    bill_type TINYINT NOT NULL COMMENT '账单类型 1-收入 2-支出',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    status TINYINT DEFAULT 1 COMMENT '状态 1-待结算 2-已结算 3-已作废',
    subject_id BIGINT COMMENT '科目ID',
    account_id BIGINT COMMENT '账户ID',
    ref_type VARCHAR(50) COMMENT '关联类型',
    ref_id BIGINT COMMENT '关联ID',
    bill_date DATE NOT NULL COMMENT '账单日期',
    settlement_time DATETIME COMMENT '结算时间',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (bill_no),
    KEY idx_bill_type (bill_type),
    KEY idx_status (status),
    KEY idx_subject_id (subject_id),
    KEY idx_ref_id (ref_id)
) COMMENT='账单表';
```

### 2. 结算表(fin_settlement)
```SQL
CREATE TABLE fin_settlement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '结算ID',
    settlement_no VARCHAR(50) NOT NULL COMMENT '结算单号',
    settlement_type TINYINT NOT NULL COMMENT '结算类型 1-收款 2-付款',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    settlement_method TINYINT NOT NULL COMMENT '结算方式 1-现金 2-银行转账 3-微信 4-支付宝 5-其他',
    account_id BIGINT COMMENT '账户ID',
    transaction_no VARCHAR(100) COMMENT '交易流水号',
    settlement_date DATE NOT NULL COMMENT '结算日期',
    target_type TINYINT COMMENT '对象类型 1-客户 2-供应商 3-员工 4-其他',
    target_id BIGINT COMMENT '对象ID',
    target_name VARCHAR(100) COMMENT '对象名称',
    status TINYINT DEFAULT 1 COMMENT '状态 1-待确认 2-已确认 3-已取消',
    remark TEXT COMMENT '备注',
    confirm_user_id BIGINT COMMENT '确认人',
    confirm_time DATETIME COMMENT '确认时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (settlement_no),
    KEY idx_settlement_type (settlement_type),
    KEY idx_status (status),
    KEY idx_target_id (target_id)
) COMMENT='结算表';
```

### 3. 结算明细表(fin_settlement_item)
```SQL
CREATE TABLE fin_settlement_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    settlement_id BIGINT NOT NULL COMMENT '结算ID',
    settlement_no VARCHAR(50) NOT NULL COMMENT '结算单号',
    bill_id BIGINT NOT NULL COMMENT '账单ID',
    bill_no VARCHAR(50) NOT NULL COMMENT '账单编号',
    amount DECIMAL(10,2) NOT NULL COMMENT '结算金额',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_settlement_id (settlement_id),
    KEY idx_settlement_no (settlement_no),
    KEY idx_bill_id (bill_id),
    KEY idx_bill_no (bill_no)
) COMMENT='结算明细表';
```

### 4. 发票表(fin_invoice)
```SQL
CREATE TABLE fin_invoice (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '发票ID',
    invoice_no VARCHAR(50) NOT NULL COMMENT '发票编号',
    invoice_type TINYINT NOT NULL COMMENT '发票类型 1-增值税普通发票 2-增值税专用发票 3-电子发票',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    tax_amount DECIMAL(10,2) DEFAULT 0 COMMENT '税额',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '价税合计',
    invoice_title VARCHAR(100) NOT NULL COMMENT '发票抬头',
    tax_no VARCHAR(50) COMMENT '税号',
    invoice_content VARCHAR(200) COMMENT '发票内容',
    status TINYINT DEFAULT 1 COMMENT '状态 1-待开票 2-已开票 3-已取消',
    target_type TINYINT COMMENT '对象类型 1-客户 2-供应商 3-其他',
    target_id BIGINT COMMENT '对象ID',
    target_name VARCHAR(100) COMMENT '对象名称',
    email VARCHAR(100) COMMENT '接收邮箱',
    ref_type VARCHAR(50) COMMENT '关联类型',
    ref_id BIGINT COMMENT '关联ID',
    issue_time DATETIME COMMENT '开票时间',
    issue_user_id BIGINT COMMENT '开票人',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (invoice_no),
    KEY idx_invoice_type (invoice_type),
    KEY idx_status (status),
    KEY idx_target_id (target_id)
) COMMENT='发票表';
```

### 5. 收支记录表(fin_transaction)
```SQL
CREATE TABLE fin_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    transaction_no VARCHAR(50) NOT NULL COMMENT '交易流水号',
    transaction_type TINYINT NOT NULL COMMENT '交易类型 1-收入 2-支出 3-内部转账',
    amount DECIMAL(10,2) NOT NULL COMMENT '金额',
    account_id BIGINT NOT NULL COMMENT '账户ID',
    target_account_id BIGINT COMMENT '目标账户ID（转账时使用）',
    transaction_method TINYINT COMMENT '交易方式 1-现金 2-银行转账 3-微信 4-支付宝 5-其他',
    transaction_time DATETIME NOT NULL COMMENT '交易时间',
    category_id BIGINT COMMENT '分类ID',
    ref_type VARCHAR(50) COMMENT '关联类型',
    ref_id BIGINT COMMENT '关联ID',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (transaction_no),
    KEY idx_transaction_type (transaction_type),
    KEY idx_account_id (account_id),
    KEY idx_target_account_id (target_account_id),
    KEY idx_category_id (category_id)
) COMMENT='收支记录表';
```

### 6. 账户表(fin_account)
```SQL
CREATE TABLE fin_account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '账户ID',
    account_name VARCHAR(50) NOT NULL COMMENT '账户名称',
    account_no VARCHAR(50) COMMENT '账号',
    account_type TINYINT DEFAULT 1 COMMENT '账户类型 1-现金 2-银行卡 3-微信 4-支付宝 5-其他',
    balance DECIMAL(10,2) DEFAULT 0 COMMENT '余额',
    initial_balance DECIMAL(10,2) DEFAULT 0 COMMENT '初始余额',
    bank_name VARCHAR(100) COMMENT '开户行',
    bank_branch VARCHAR(100) COMMENT '支行',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    sort INT DEFAULT 0 COMMENT '排序',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_account_type (account_type)
) COMMENT='账户表';
```

### 7. 财务科目表(fin_subject)
```SQL
CREATE TABLE fin_subject (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '科目ID',
    subject_name VARCHAR(50) NOT NULL COMMENT '科目名称',
    subject_code VARCHAR(50) NOT NULL COMMENT '科目编码',
    subject_type TINYINT NOT NULL COMMENT '科目类型 1-收入 2-支出',
    parent_id BIGINT DEFAULT 0 COMMENT '父科目ID',
    level INT DEFAULT 1 COMMENT '层级',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (subject_code),
    KEY idx_parent_id (parent_id),
    KEY idx_subject_type (subject_type)
) COMMENT='财务科目表';
```

### 8. 佣金结算表(fin_commission)
```sql
CREATE TABLE fin_commission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    commission_no VARCHAR(50) NOT NULL COMMENT '佣金结算单号',
    distributor_id BIGINT NOT NULL COMMENT '分销员ID',
    period_start DATE NOT NULL COMMENT '结算周期开始日期',
    period_end DATE NOT NULL COMMENT '结算周期结束日期',
    total_order_amount DECIMAL(10,2) DEFAULT 0 COMMENT '订单总金额',
    commission_amount DECIMAL(10,2) DEFAULT 0 COMMENT '佣金金额',
    status TINYINT DEFAULT 1 COMMENT '状态 1-待结算 2-已结算 3-已取消',
    settlement_time DATETIME COMMENT '结算时间',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (commission_no),
    KEY idx_distributor_id (distributor_id),
    KEY idx_status (status)
) COMMENT='佣金结算表';
```

### 9. 佣金结算明细表(fin_commission_item)
```sql
CREATE TABLE fin_commission_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    commission_id BIGINT NOT NULL COMMENT '佣金结算单ID',
    commission_no VARCHAR(50) NOT NULL COMMENT '佣金结算单号',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    order_amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    commission_rate DECIMAL(5,4) NOT NULL COMMENT '佣金比例',
    commission_amount DECIMAL(10,2) NOT NULL COMMENT '佣金金额',
    order_time DATETIME COMMENT '订单时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_commission_id (commission_id),
    KEY idx_commission_no (commission_no),
    KEY idx_order_id (order_id),
    KEY idx_order_no (order_no)
) COMMENT='佣金结算明细表';
```

## 九、会员管理表

### 1. 会员表(mem_member)
```sql
CREATE TABLE mem_member (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会员ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    real_name VARCHAR(50) COMMENT '真实姓名',
    avatar VARCHAR(255) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
    birthday DATE COMMENT '生日',
    mobile VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    level_id BIGINT DEFAULT 1 COMMENT '会员等级ID',
    points INT DEFAULT 0 COMMENT '积分',
    growth INT DEFAULT 0 COMMENT '成长值',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    source TINYINT COMMENT '来源 1-PC 2-APP 3-小程序 4-H5',
    invite_code VARCHAR(20) COMMENT '邀请码',
    inviter_id BIGINT COMMENT '邀请人ID',
    total_order_amount DECIMAL(10,2) DEFAULT 0 COMMENT '累计消费金额',
    order_count INT DEFAULT 0 COMMENT '订单数量',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    register_time DATETIME NOT NULL COMMENT '注册时间',
    register_ip VARCHAR(50) COMMENT '注册IP',
    update_time DATETIME COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (username),
    UNIQUE KEY (mobile),
    KEY idx_level_id (level_id),
    KEY idx_inviter_id (inviter_id)
) COMMENT='会员表';
```

### 2. 会员等级表(mem_level)
```sql
CREATE TABLE mem_level (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '等级ID',
    level_name VARCHAR(50) NOT NULL COMMENT '等级名称',
    level_value INT NOT NULL COMMENT '等级值',
    growth_min INT NOT NULL COMMENT '所需最低成长值',
    growth_max INT NOT NULL COMMENT '所需最高成长值',
    discount DECIMAL(3,2) DEFAULT 1 COMMENT '折扣率',
    description TEXT COMMENT '等级描述',
    icon VARCHAR(255) COMMENT '等级图标',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (level_value)
) COMMENT='会员等级表';
```

### 3. 积分记录表(mem_points)
```sql
CREATE TABLE mem_points (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    points INT NOT NULL COMMENT '积分变动值',
    type TINYINT NOT NULL COMMENT '类型 1-获得 2-消费',
    operation_type TINYINT NOT NULL COMMENT '操作类型 1-注册 2-签到 3-购物 4-评价 5-兑换 6-过期 7-管理员操作',
    order_id BIGINT COMMENT '订单ID',
    order_no VARCHAR(50) COMMENT '订单编号',
    description VARCHAR(200) COMMENT '描述',
    before_points INT COMMENT '变动前积分',
    after_points INT COMMENT '变动后积分',
    expire_time DATETIME COMMENT '过期时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    KEY idx_member_id (member_id),
    KEY idx_order_id (order_id)
) COMMENT='积分记录表';
```

### 4. 会员成长值表(mem_growth)
```sql
CREATE TABLE mem_growth (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    growth INT NOT NULL COMMENT '成长值变动',
    type TINYINT NOT NULL COMMENT '类型 1-获得 2-扣减',
    operation_type TINYINT NOT NULL COMMENT '操作类型 1-注册 2-购物 3-评价 4-签到 5-管理员操作',
    order_id BIGINT COMMENT '订单ID',
    order_no VARCHAR(50) COMMENT '订单编号',
    description VARCHAR(200) COMMENT '描述',
    before_growth INT COMMENT '变动前成长值',
    after_growth INT COMMENT '变动后成长值',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    KEY idx_member_id (member_id),
    KEY idx_order_id (order_id)
) COMMENT='会员成长值表';
```

### 5. 会员收货地址表(mem_address)
```sql
CREATE TABLE mem_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    consignee VARCHAR(50) NOT NULL COMMENT '收货人',
    mobile VARCHAR(20) NOT NULL COMMENT '手机号',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    detail_address VARCHAR(200) NOT NULL COMMENT '详细地址',
    zip_code VARCHAR(20) COMMENT '邮编',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认 0-否 1-是',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_member_id (member_id)
) COMMENT='会员收货地址表';
```

### 6. 会员足迹表(mem_footprint)
```sql
CREATE TABLE mem_footprint (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_member_id (member_id),
    KEY idx_goods_id (goods_id)
) COMMENT='会员足迹表';
```

### 7. 会员收藏表(mem_collection)
```sql
CREATE TABLE mem_collection (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    UNIQUE KEY (member_id, goods_id),
    KEY idx_member_id (member_id),
    KEY idx_goods_id (goods_id)
) COMMENT='会员收藏表';
```

## 十、系统运营表

### 1. 消息通知表(sys_message)
```sql
CREATE TABLE sys_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    title VARCHAR(100) NOT NULL COMMENT '消息标题',
    content TEXT NOT NULL COMMENT '消息内容',
    message_type TINYINT NOT NULL COMMENT '消息类型 1-系统消息 2-活动消息 3-订单消息 4-物流消息 5-其他',
    target_type TINYINT NOT NULL COMMENT '接收对象类型 1-全部用户 2-指定用户 3-指定角色 4-指定部门',
    target_ids VARCHAR(500) COMMENT '接收对象ID集合',
    send_time DATETIME COMMENT '发送时间',
    status TINYINT DEFAULT 1 COMMENT '状态 0-待发送 1-已发送 2-已取消',
    send_type TINYINT DEFAULT 1 COMMENT '发送方式 1-站内信 2-短信 3-邮件 4-微信 5-APP推送',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_message_type (message_type),
    KEY idx_status (status)
) COMMENT='消息通知表';
```

### 2. 用户消息表(sys_user_message)
```sql
CREATE TABLE sys_user_message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    message_id BIGINT NOT NULL COMMENT '消息ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读 0-未读 1-已读',
    read_time DATETIME COMMENT '阅读时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_message_id (message_id),
    KEY idx_user_id (user_id),
    KEY idx_is_read (is_read)
) COMMENT='用户消息表';
```

### 3. 操作日志表(sys_log)
```sql
CREATE TABLE sys_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    operation VARCHAR(50) COMMENT '操作',
    method VARCHAR(200) COMMENT '方法名',
    params TEXT COMMENT '请求参数',
    ip VARCHAR(50) COMMENT 'IP地址',
    status TINYINT DEFAULT 1 COMMENT '状态 0-失败 1-成功',
    error_msg TEXT COMMENT '错误消息',
    operation_time BIGINT COMMENT '操作耗时',
    user_agent VARCHAR(500) COMMENT '用户代理',
    request_uri VARCHAR(200) COMMENT '请求URI',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) COMMENT='操作日志表';
```

### 4. 登录日志表(sys_login_log)
```sql
CREATE TABLE sys_login_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '登录日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    login_type TINYINT DEFAULT 1 COMMENT '登录类型 1-账号密码 2-手机验证码 3-第三方登录 4-扫码登录',
    ip VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    device_type VARCHAR(50) COMMENT '设备类型',
    os_type VARCHAR(50) COMMENT '操作系统',
    browser_type VARCHAR(50) COMMENT '浏览器类型',
    status TINYINT DEFAULT 1 COMMENT '状态 0-失败 1-成功',
    fail_reason VARCHAR(200) COMMENT '失败原因',
    login_time DATETIME NOT NULL COMMENT '登录时间',
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_login_time (login_time)
) COMMENT='登录日志表';
```

### 5. 定时任务表(sys_task)
```sql
CREATE TABLE sys_task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '任务ID',
    task_name VARCHAR(100) NOT NULL COMMENT '任务名称',
    task_group VARCHAR(50) NOT NULL COMMENT '任务组名',
    task_class VARCHAR(255) NOT NULL COMMENT '任务类',
    task_method VARCHAR(100) NOT NULL COMMENT '任务方法',
    task_params VARCHAR(500) COMMENT '任务参数',
    cron_expression VARCHAR(50) NOT NULL COMMENT 'cron表达式',
    status TINYINT DEFAULT 1 COMMENT '状态 0-停用 1-正常',
    remark TEXT COMMENT '备注',
    concurrent TINYINT DEFAULT 0 COMMENT '是否并发 0-否 1-是',
    execute_times INT DEFAULT 0 COMMENT '已执行次数',
    last_execute_time DATETIME COMMENT '上次执行时间',
    next_execute_time DATETIME COMMENT '下次执行时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (task_name, task_group),
    KEY idx_status (status)
) COMMENT='定时任务表';
```

### 6. 任务日志表(sys_task_log)
```sql
CREATE TABLE sys_task_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    task_id BIGINT NOT NULL COMMENT '任务ID',
    task_name VARCHAR(100) NOT NULL COMMENT '任务名称',
    task_group VARCHAR(50) NOT NULL COMMENT '任务组名',
    task_class VARCHAR(255) NOT NULL COMMENT '任务类',
    task_method VARCHAR(100) NOT NULL COMMENT '任务方法',
    task_params VARCHAR(500) COMMENT '任务参数',
    execution_time BIGINT COMMENT '执行耗时(毫秒)',
    status TINYINT DEFAULT 1 COMMENT '状态 0-失败 1-成功',
    error_msg TEXT COMMENT '错误消息',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_task_id (task_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) COMMENT='任务日志表';
```

### 7. 系统参数表(sys_param)
```sql
CREATE TABLE sys_param (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '参数ID',
    param_key VARCHAR(100) NOT NULL COMMENT '参数键',
    param_value VARCHAR(500) NOT NULL COMMENT '参数值',
    param_type TINYINT DEFAULT 1 COMMENT '参数类型 1-系统参数 2-业务参数',
    description VARCHAR(200) COMMENT '参数描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (param_key),
    KEY idx_param_type (param_type)
) COMMENT='系统参数表';
```

## 十一、分销/推广系统表

### 1. 分销员表(dist_distributor)
```sql
CREATE TABLE dist_distributor (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分销员ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    name VARCHAR(50) NOT NULL COMMENT '分销员姓名',
    mobile VARCHAR(20) COMMENT '手机号',
    level_id BIGINT DEFAULT 1 COMMENT '分销等级ID',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    invite_code VARCHAR(20) NOT NULL COMMENT '邀请码',
    parent_id BIGINT COMMENT '上级分销员ID',
    parent_path VARCHAR(500) COMMENT '上级路径ID，用逗号分隔',
    total_commission DECIMAL(10,2) DEFAULT 0 COMMENT '累计佣金',
    available_commission DECIMAL(10,2) DEFAULT 0 COMMENT '可用佣金',
    withdrawn_commission DECIMAL(10,2) DEFAULT 0 COMMENT '已提现佣金',
    freezing_commission DECIMAL(10,2) DEFAULT 0 COMMENT '冻结佣金',
    team_count INT DEFAULT 0 COMMENT '团队人数',
    first_level_count INT DEFAULT 0 COMMENT '一级人数',
    apply_time DATETIME COMMENT '申请时间',
    audit_time DATETIME COMMENT '审核时间',
    audit_user_id BIGINT COMMENT '审核人',
    audit_remark VARCHAR(200) COMMENT '审核备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (member_id),
    UNIQUE KEY (invite_code),
    KEY idx_parent_id (parent_id),
    KEY idx_level_id (level_id)
) COMMENT='分销员表';
```

### 2. 分销等级表(dist_level)
```sql
CREATE TABLE dist_level (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '等级ID',
    level_name VARCHAR(50) NOT NULL COMMENT '等级名称',
    level_value INT NOT NULL COMMENT '等级值',
    first_rate DECIMAL(5,4) NOT NULL COMMENT '一级佣金比例',
    second_rate DECIMAL(5,4) COMMENT '二级佣金比例',
    third_rate DECIMAL(5,4) COMMENT '三级佣金比例',
    upgrade_type TINYINT DEFAULT 1 COMMENT '升级类型 1-累计佣金 2-累计订单 3-直推人数',
    upgrade_value DECIMAL(10,2) COMMENT '升级条件值',
    description TEXT COMMENT '等级描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (level_value)
) COMMENT='分销等级表';
```

### 3. 分销订单表(dist_order)
```sql
CREATE TABLE dist_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单编号',
    order_amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    distributor_id BIGINT NOT NULL COMMENT '分销员ID',
    member_id BIGINT NOT NULL COMMENT '下单会员ID',
    level INT DEFAULT 1 COMMENT '分销层级 1-一级 2-二级 3-三级',
    commission_rate DECIMAL(5,4) NOT NULL COMMENT '佣金比例',
    commission_amount DECIMAL(10,2) NOT NULL COMMENT '佣金金额',
    status TINYINT DEFAULT 1 COMMENT '状态 1-未结算 2-已结算 3-已取消',
    settlement_id BIGINT COMMENT '结算ID',
    settlement_time DATETIME COMMENT '结算时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    KEY idx_order_id (order_id),
    KEY idx_distributor_id (distributor_id),
    KEY idx_member_id (member_id),
    KEY idx_status (status)
) COMMENT='分销订单表';
```

### 4. 佣金记录表(dist_commission)
```sql
CREATE TABLE dist_commission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    distributor_id BIGINT NOT NULL COMMENT '分销员ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '佣金金额',
    type TINYINT NOT NULL COMMENT '类型 1-获得 2-提现 3-退回 4-冻结 5-解冻',
    order_id BIGINT COMMENT '关联订单ID',
    order_no VARCHAR(50) COMMENT '关联订单编号',
    withdraw_id BIGINT COMMENT '提现记录ID',
    description VARCHAR(200) COMMENT '描述',
    before_amount DECIMAL(10,2) COMMENT '变动前金额',
    after_amount DECIMAL(10,2) COMMENT '变动后金额',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_distributor_id (distributor_id),
    KEY idx_order_id (order_id),
    KEY idx_withdraw_id (withdraw_id)
) COMMENT='佣金记录表';
```

### 5. 佣金提现表(dist_withdraw)
```sql
CREATE TABLE dist_withdraw (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    withdraw_no VARCHAR(50) NOT NULL COMMENT '提现单号',
    distributor_id BIGINT NOT NULL COMMENT '分销员ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '提现金额',
    fee DECIMAL(10,2) DEFAULT 0 COMMENT '手续费',
    actual_amount DECIMAL(10,2) NOT NULL COMMENT '实际到账金额',
    withdraw_type TINYINT NOT NULL COMMENT '提现方式 1-微信 2-支付宝 3-银行卡',
    account VARCHAR(100) NOT NULL COMMENT '收款账号',
    account_name VARCHAR(50) NOT NULL COMMENT '收款人姓名',
    bank_name VARCHAR(100) COMMENT '银行名称',
    bank_branch VARCHAR(100) COMMENT '支行名称',
    status TINYINT DEFAULT 1 COMMENT '状态 1-申请中 2-已审核 3-已拒绝 4-提现中 5-已到账 6-已取消',
    reject_reason VARCHAR(200) COMMENT '拒绝原因',
    audit_time DATETIME COMMENT '审核时间',
    audit_user_id BIGINT COMMENT '审核人',
    payment_time DATETIME COMMENT '打款时间',
    payment_user_id BIGINT COMMENT '打款人',
    transaction_id VARCHAR(100) COMMENT '交易流水号',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (withdraw_no),
    KEY idx_distributor_id (distributor_id),
    KEY idx_member_id (member_id),
    KEY idx_status (status)
) COMMENT='佣金提现表';
```

### 6. 推广码表(dist_promotion_code)
```sql
CREATE TABLE dist_promotion_code (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    distributor_id BIGINT NOT NULL COMMENT '分销员ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    code_type TINYINT NOT NULL COMMENT '码类型 1-商品 2-分享链接 3-店铺',
    code_value VARCHAR(100) NOT NULL COMMENT '码值',
    target_id BIGINT COMMENT '目标ID（商品ID、店铺ID等）',
    qr_code VARCHAR(255) COMMENT '二维码图片',
    poster_url VARCHAR(255) COMMENT '海报图片',
    visit_count INT DEFAULT 0 COMMENT '访问次数',
    order_count INT DEFAULT 0 COMMENT '订单次数',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    expire_time DATETIME COMMENT '过期时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    KEY idx_distributor_id (distributor_id),
    KEY idx_member_id (member_id),
    KEY idx_code_type (code_type),
    KEY idx_target_id (target_id)
) COMMENT='推广码表';
```

## 十二、售后服务表

### 1. 工单表(serv_ticket)
```sql
CREATE TABLE serv_ticket (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '工单ID',
    ticket_no VARCHAR(50) NOT NULL COMMENT '工单编号',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    ticket_type TINYINT NOT NULL COMMENT '工单类型 1-咨询 2-建议 3-投诉 4-售后 5-其他',
    title VARCHAR(100) NOT NULL COMMENT '工单标题',
    content TEXT NOT NULL COMMENT '工单内容',
    images VARCHAR(500) COMMENT '图片，多个以逗号分隔',
    status TINYINT DEFAULT 1 COMMENT '状态 1-待处理 2-处理中 3-已解决 4-已关闭 5-已评价',
    priority TINYINT DEFAULT 2 COMMENT '优先级 1-低 2-中 3-高',
    order_id BIGINT COMMENT '关联订单ID',
    order_no VARCHAR(50) COMMENT '关联订单编号',
    goods_id BIGINT COMMENT '关联商品ID',
    handler_id BIGINT COMMENT '处理人ID',
    handler_name VARCHAR(50) COMMENT '处理人姓名',
    handle_time DATETIME COMMENT '受理时间',
    finish_time DATETIME COMMENT '完成时间',
    evaluation_score TINYINT COMMENT '评价分数 1-5',
    evaluation_content VARCHAR(500) COMMENT '评价内容',
    evaluation_time DATETIME COMMENT '评价时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (ticket_no),
    KEY idx_member_id (member_id),
    KEY idx_status (status),
    KEY idx_order_id (order_id)
) COMMENT='工单表';
```

### 2. 工单跟进表(serv_ticket_record)
```sql
CREATE TABLE serv_ticket_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    ticket_id BIGINT NOT NULL COMMENT '工单ID',
    ticket_no VARCHAR(50) NOT NULL COMMENT '工单编号',
    content TEXT NOT NULL COMMENT '跟进内容',
    images VARCHAR(500) COMMENT '图片，多个以逗号分隔',
    operate_type TINYINT NOT NULL COMMENT '操作类型 1-回复 2-转交 3-变更状态 4-其他',
    operator_id BIGINT NOT NULL COMMENT '操作人ID',
    operator_name VARCHAR(50) NOT NULL COMMENT '操作人姓名',
    operator_type TINYINT DEFAULT 1 COMMENT '操作人类型 1-客服 2-会员',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_ticket_id (ticket_id),
    KEY idx_ticket_no (ticket_no),
    KEY idx_operator_id (operator_id)
) COMMENT='工单跟进表';
```

### 3. 维修记录表(serv_repair)
```sql
CREATE TABLE serv_repair (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    repair_no VARCHAR(50) NOT NULL COMMENT '维修单号',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    order_id BIGINT COMMENT '关联订单ID',
    order_no VARCHAR(50) COMMENT '关联订单编号',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    problem_desc TEXT NOT NULL COMMENT '问题描述',
    images VARCHAR(500) COMMENT '图片，多个以逗号分隔',
    status TINYINT DEFAULT 1 COMMENT '状态 1-待受理 2-维修中 3-已完成 4-已取消',
    repair_type TINYINT COMMENT '维修类型 1-保修 2-有偿维修',
    repair_amount DECIMAL(10,2) COMMENT '维修金额',
    repair_result TEXT COMMENT '维修结果',
    handler_id BIGINT COMMENT '处理人ID',
    handler_name VARCHAR(50) COMMENT '处理人姓名',
    start_time DATETIME COMMENT '维修开始时间',
    finish_time DATETIME COMMENT '维修完成时间',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (repair_no),
    KEY idx_member_id (member_id),
    KEY idx_order_id (order_id),
    KEY idx_goods_id (goods_id),
    KEY idx_status (status)
) COMMENT='维修记录表';
```

### 4. 客服评价表(serv_evaluation)
```sql
CREATE TABLE serv_evaluation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    ticket_id BIGINT COMMENT '工单ID',
    ticket_no VARCHAR(50) COMMENT '工单编号',
    service_type TINYINT NOT NULL COMMENT '服务类型 1-在线客服 2-电话客服 3-工单处理',
    staff_id BIGINT COMMENT '客服ID',
    staff_name VARCHAR(50) COMMENT '客服姓名',
    score TINYINT NOT NULL COMMENT '评分 1-5',
    content VARCHAR(500) COMMENT '评价内容',
    service_time DATETIME COMMENT '服务时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_member_id (member_id),
    KEY idx_ticket_id (ticket_id),
    KEY idx_staff_id (staff_id)
) COMMENT='客服评价表';
```

## 十三、报表与数据分析表

### 1. 销售报表表(report_sales)
```sql
CREATE TABLE report_sales (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    report_date DATE NOT NULL COMMENT '报表日期',
    order_count INT DEFAULT 0 COMMENT '订单数量',
    order_amount DECIMAL(10,2) DEFAULT 0 COMMENT '订单金额',
    paid_order_count INT DEFAULT 0 COMMENT '已支付订单数',
    paid_order_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已支付金额',
    refund_order_count INT DEFAULT 0 COMMENT '退款订单数',
    refund_amount DECIMAL(10,2) DEFAULT 0 COMMENT '退款金额',
    goods_count INT DEFAULT 0 COMMENT '商品销量',
    new_member_count INT DEFAULT 0 COMMENT '新增会员数',
    stats_type TINYINT NOT NULL COMMENT '统计类型 1-日 2-周 3-月 4-年',
    dimension VARCHAR(50) COMMENT '统计维度',
    dimension_id BIGINT COMMENT '维度ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (report_date, stats_type, dimension, dimension_id),
    KEY idx_report_date (report_date),
    KEY idx_stats_type (stats_type)
) COMMENT='销售报表表';
```

### 2. 客户分析表(report_customer)
```sql
CREATE TABLE report_customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    report_date DATE NOT NULL COMMENT '报表日期',
    new_customer_count INT DEFAULT 0 COMMENT '新增客户数',
    total_customer_count INT DEFAULT 0 COMMENT '累计客户数',
    active_customer_count INT DEFAULT 0 COMMENT '活跃客户数',
    transaction_customer_count INT DEFAULT 0 COMMENT '成交客户数',
    lost_customer_count INT DEFAULT 0 COMMENT '流失客户数',
    transaction_rate DECIMAL(5,4) DEFAULT 0 COMMENT '成交率',
    retention_rate DECIMAL(5,4) DEFAULT 0 COMMENT '留存率',
    customer_unit_price DECIMAL(10,2) DEFAULT 0 COMMENT '客单价',
    repurchase_rate DECIMAL(5,4) DEFAULT 0 COMMENT '复购率',
    stats_type TINYINT NOT NULL COMMENT '统计类型 1-日 2-周 3-月 4-年',
    dimension VARCHAR(50) COMMENT '统计维度',
    dimension_id BIGINT COMMENT '维度ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (report_date, stats_type, dimension, dimension_id),
    KEY idx_report_date (report_date),
    KEY idx_stats_type (stats_type)
) COMMENT='客户分析表';
```

### 3. 商品分析表(report_product)
```sql
CREATE TABLE report_product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    report_date DATE NOT NULL COMMENT '报表日期',
    goods_id BIGINT NOT NULL COMMENT '商品ID',
    sku_id BIGINT COMMENT 'SKU ID',
    category_id BIGINT COMMENT '分类ID',
    sales_count INT DEFAULT 0 COMMENT '销量',
    sales_amount DECIMAL(10,2) DEFAULT 0 COMMENT '销售额',
    refund_count INT DEFAULT 0 COMMENT '退款数量',
    refund_amount DECIMAL(10,2) DEFAULT 0 COMMENT '退款金额',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    favorite_count INT DEFAULT 0 COMMENT '收藏次数',
    cart_count INT DEFAULT 0 COMMENT '加购次数',
    conversion_rate DECIMAL(5,4) DEFAULT 0 COMMENT '转化率',
    stats_type TINYINT NOT NULL COMMENT '统计类型 1-日 2-周 3-月 4-年',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (report_date, stats_type, goods_id, sku_id),
    KEY idx_report_date (report_date),
    KEY idx_goods_id (goods_id),
    KEY idx_category_id (category_id)
) COMMENT='商品分析表';
```

### 4. 自定义报表表(report_custom)
```sql
CREATE TABLE report_custom (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    report_name VARCHAR(100) NOT NULL COMMENT '报表名称',
    report_code VARCHAR(50) NOT NULL COMMENT '报表编码',
    report_type TINYINT NOT NULL COMMENT '报表类型 1-表格 2-图表 3-混合',
    data_source VARCHAR(100) NOT NULL COMMENT '数据源',
    sql_content TEXT COMMENT 'SQL语句',
    params TEXT COMMENT '参数配置JSON',
    fields TEXT COMMENT '字段配置JSON',
    chart_config TEXT COMMENT '图表配置JSON',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (report_code),
    KEY idx_status (status)
) COMMENT='自定义报表表';
```

## 十四、多语言/国际化表

### 1. 语言表(sys_language)
```sql
CREATE TABLE sys_language (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '语言ID',
    language_code VARCHAR(10) NOT NULL COMMENT '语言代码',
    language_name VARCHAR(50) NOT NULL COMMENT '语言名称',
    icon VARCHAR(255) COMMENT '语言图标',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认 0-否 1-是',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (language_code)
) COMMENT='语言表';
```

### 2. 翻译表(sys_translation)
```sql
CREATE TABLE sys_translation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '翻译ID',
    language_code VARCHAR(10) NOT NULL COMMENT '语言代码',
    translation_key VARCHAR(100) NOT NULL COMMENT '翻译键',
    translation_value TEXT NOT NULL COMMENT '翻译值',
    module VARCHAR(50) COMMENT '所属模块',
    remark VARCHAR(200) COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    UNIQUE KEY (language_code, translation_key, module)
) COMMENT='翻译表';
```

### 3. 币种表(sys_currency)
```sql
CREATE TABLE sys_currency (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '币种ID',
    currency_code VARCHAR(10) NOT NULL COMMENT '币种代码',
    currency_name VARCHAR(50) NOT NULL COMMENT '币种名称',
    currency_symbol VARCHAR(10) NOT NULL COMMENT '币种符号',
    precision_digit INT DEFAULT 2 COMMENT '精度位数',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    is_default TINYINT DEFAULT 0 COMMENT '是否默认 0-否 1-是',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (currency_code)
) COMMENT='币种表';
```

### 4. 汇率表(sys_exchange_rate)
```sql
CREATE TABLE sys_exchange_rate (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    source_currency VARCHAR(10) NOT NULL COMMENT '源币种',
    target_currency VARCHAR(10) NOT NULL COMMENT '目标币种',
    exchange_rate DECIMAL(16,8) NOT NULL COMMENT '汇率',
    effective_date DATE NOT NULL COMMENT '生效日期',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    UNIQUE KEY (source_currency, target_currency, effective_date),
    KEY idx_effective_date (effective_date)
) COMMENT='汇率表';
```

## 十五、多组织/多门店表

### 1. 门店表(org_store)
```sql
CREATE TABLE org_store (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '门店ID',
    org_id BIGINT NOT NULL COMMENT '所属组织ID',
    store_name VARCHAR(100) NOT NULL COMMENT '门店名称',
    store_code VARCHAR(50) NOT NULL COMMENT '门店编码',
    province VARCHAR(50) COMMENT '省份',
    city VARCHAR(50) COMMENT '城市',
    district VARCHAR(50) COMMENT '区县',
    address VARCHAR(200) COMMENT '详细地址',
    latitude DECIMAL(10,6) COMMENT '纬度',
    longitude DECIMAL(10,6) COMMENT '经度',
    contact VARCHAR(50) COMMENT '联系人',
    mobile VARCHAR(20) COMMENT '联系电话',
    business_hours VARCHAR(100) COMMENT '营业时间',
    store_area DECIMAL(10,2) COMMENT '门店面积',
    image VARCHAR(255) COMMENT '门店图片',
    status TINYINT DEFAULT 1 COMMENT '状态 0-停业 1-营业',
    sort INT DEFAULT 0 COMMENT '排序',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (store_code),
    KEY idx_org_id (org_id)
) COMMENT='门店表';
```

### 2. 区域表(org_region)
```sql
CREATE TABLE org_region (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '区域ID',
    region_name VARCHAR(50) NOT NULL COMMENT '区域名称',
    region_code VARCHAR(50) NOT NULL COMMENT '区域编码',
    parent_id BIGINT DEFAULT 0 COMMENT '父区域ID',
    level INT DEFAULT 1 COMMENT '层级',
    full_name VARCHAR(200) COMMENT '完整名称',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (region_code),
    KEY idx_parent_id (parent_id)
) COMMENT='区域表';
```

### 3. 部门表(org_department)
```sql
CREATE TABLE org_department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '部门ID',
    org_id BIGINT NOT NULL COMMENT '所属组织ID',
    dept_name VARCHAR(50) NOT NULL COMMENT '部门名称',
    dept_code VARCHAR(50) NOT NULL COMMENT '部门编码',
    parent_id BIGINT DEFAULT 0 COMMENT '父部门ID',
    leader VARCHAR(50) COMMENT '部门负责人',
    phone VARCHAR(20) COMMENT '联系电话',
    email VARCHAR(100) COMMENT '邮箱',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    sort INT DEFAULT 0 COMMENT '排序',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (org_id, dept_code),
    KEY idx_org_id (org_id),
    KEY idx_parent_id (parent_id)
) COMMENT='部门表';
```

### 4. 岗位表(org_position)
```sql
CREATE TABLE org_position (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '岗位ID',
    org_id BIGINT NOT NULL COMMENT '所属组织ID',
    position_name VARCHAR(50) NOT NULL COMMENT '岗位名称',
    position_code VARCHAR(50) NOT NULL COMMENT '岗位编码',
    dept_id BIGINT COMMENT '所属部门ID',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    sort INT DEFAULT 0 COMMENT '排序',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (org_id, position_code),
    KEY idx_org_id (org_id),
    KEY idx_dept_id (dept_id)
) COMMENT='岗位表';
```

## 十六、API与集成表

### 1. API配置表(api_config)
```sql
CREATE TABLE api_config (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'API ID',
    api_name VARCHAR(100) NOT NULL COMMENT 'API名称',
    api_code VARCHAR(50) NOT NULL COMMENT 'API编码',
    api_url VARCHAR(255) NOT NULL COMMENT 'API地址',
    request_method VARCHAR(10) NOT NULL COMMENT '请求方法',
    content_type VARCHAR(100) COMMENT '内容类型',
    timeout INT DEFAULT 5000 COMMENT '超时时间(毫秒)',
    retry_times INT DEFAULT 0 COMMENT '重试次数',
    request_params TEXT COMMENT '请求参数',
    response_params TEXT COMMENT '响应参数',
    headers TEXT COMMENT '请求头',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (api_code)
) COMMENT='API配置表';
```

### 2. 第三方平台表(api_platform)
```sql
CREATE TABLE api_platform (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '平台ID',
    platform_name VARCHAR(100) NOT NULL COMMENT '平台名称',
    platform_code VARCHAR(50) NOT NULL COMMENT '平台编码',
    platform_type TINYINT NOT NULL COMMENT '平台类型 1-支付 2-物流 3-短信 4-邮件 5-存储 6-推送 7-其他',
    app_id VARCHAR(100) COMMENT 'AppID',
    app_secret VARCHAR(200) COMMENT 'AppSecret',
    access_token VARCHAR(200) COMMENT 'AccessToken',
    token_expire_time DATETIME COMMENT 'Token过期时间',
    other_params TEXT COMMENT '其他参数',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (platform_code)
) COMMENT='第三方平台表';
```

### 3. 接口调用日志表(api_log)
```sql
CREATE TABLE api_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    api_id BIGINT COMMENT 'API ID',
    api_code VARCHAR(50) COMMENT 'API编码',
    platform_id BIGINT COMMENT '平台ID',
    platform_code VARCHAR(50) COMMENT '平台编码',
    request_url VARCHAR(500) NOT NULL COMMENT '请求地址',
    request_method VARCHAR(10) NOT NULL COMMENT '请求方法',
    request_headers TEXT COMMENT '请求头',
    request_body TEXT COMMENT '请求体',
    response_headers TEXT COMMENT '响应头',
    response_body TEXT COMMENT '响应体',
    http_status INT COMMENT 'HTTP状态码',
    status TINYINT DEFAULT 1 COMMENT '状态 0-失败 1-成功',
    error_msg VARCHAR(500) COMMENT '错误消息',
    request_time BIGINT COMMENT '请求耗时(毫秒)',
    ip VARCHAR(50) COMMENT 'IP地址',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_api_id (api_id),
    KEY idx_platform_id (platform_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) COMMENT='接口调用日志表';
```

### 4. 接口权限表(api_permission)
```sql
CREATE TABLE api_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    api_id BIGINT NOT NULL COMMENT 'API ID',
    api_code VARCHAR(50) NOT NULL COMMENT 'API编码',
    app_key VARCHAR(100) NOT NULL COMMENT '应用Key',
    app_secret VARCHAR(200) COMMENT '应用Secret',
    ip_whitelist TEXT COMMENT 'IP白名单',
    rate_limit INT DEFAULT 0 COMMENT '限流次数/分钟',
    expired_time DATETIME COMMENT '过期时间',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (api_id, app_key),
    KEY idx_app_key (app_key)
) COMMENT='接口权限表';
```

## 十七、移动端/小程序表

### 1. 设备表(app_device)
```sql
CREATE TABLE app_device (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '设备ID',
    member_id BIGINT COMMENT '会员ID',
    device_token VARCHAR(100) NOT NULL COMMENT '设备Token',
    device_type VARCHAR(50) NOT NULL COMMENT '设备类型',
    device_brand VARCHAR(50) COMMENT '设备品牌',
    device_model VARCHAR(50) COMMENT '设备型号',
    os_type VARCHAR(20) COMMENT '操作系统类型',
    os_version VARCHAR(20) COMMENT '操作系统版本',
    app_version VARCHAR(20) COMMENT 'APP版本',
    push_id VARCHAR(100) COMMENT '推送ID',
    last_active_time DATETIME COMMENT '最后活跃时间',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (device_token),
    KEY idx_member_id (member_id)
) COMMENT='设备表';
```

### 2. 版本表(app_version)
```sql
CREATE TABLE app_version (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '版本ID',
    version_name VARCHAR(20) NOT NULL COMMENT '版本名称',
    version_code INT NOT NULL COMMENT '版本号',
    platform TINYINT NOT NULL COMMENT '平台 1-Android 2-iOS 3-小程序 4-H5',
    download_url VARCHAR(255) COMMENT '下载地址',
    update_content TEXT NOT NULL COMMENT '更新内容',
    file_size INT COMMENT '文件大小(KB)',
    is_force TINYINT DEFAULT 0 COMMENT '是否强制更新 0-否 1-是',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (platform, version_code)
) COMMENT='版本表';
```

### 3. 推送记录表(app_push)
```sql
CREATE TABLE app_push (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '推送ID',
    push_title VARCHAR(100) NOT NULL COMMENT '推送标题',
    push_content TEXT NOT NULL COMMENT '推送内容',
    push_type TINYINT NOT NULL COMMENT '推送类型 1-通知 2-消息',
    target_type TINYINT NOT NULL COMMENT '目标类型 1-全部 2-指定用户 3-指定标签',
    target_ids VARCHAR(500) COMMENT '目标ID集合',
    platform TINYINT COMMENT '平台 0-全部 1-Android 2-iOS 3-小程序',
    link_type TINYINT COMMENT '链接类型 1-URL 2-页面 3-商品 4-订单',
    link_value VARCHAR(255) COMMENT '链接值',
    push_time DATETIME COMMENT '推送时间',
    status TINYINT DEFAULT 0 COMMENT '状态 0-待推送 1-推送中 2-推送成功 3-推送失败',
    success_count INT DEFAULT 0 COMMENT '成功数量',
    fail_count INT DEFAULT 0 COMMENT '失败数量',
    error_msg VARCHAR(500) COMMENT '错误消息',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_push_type (push_type),
    KEY idx_status (status),
    KEY idx_push_time (push_time)
) COMMENT='推送记录表';
```

### 4. 用户设置表(app_setting)
```sql
CREATE TABLE app_setting (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    member_id BIGINT NOT NULL COMMENT '会员ID',
    push_switch TINYINT DEFAULT 1 COMMENT '推送开关 0-关闭 1-开启',
    sound_switch TINYINT DEFAULT 1 COMMENT '声音开关 0-关闭 1-开启',
    vibrate_switch TINYINT DEFAULT 1 COMMENT '震动开关 0-关闭 1-开启',
    theme VARCHAR(20) DEFAULT 'default' COMMENT '主题',
    language VARCHAR(10) DEFAULT 'zh_CN' COMMENT '语言',
    currency VARCHAR(10) DEFAULT 'CNY' COMMENT '货币',
    font_size VARCHAR(10) DEFAULT 'medium' COMMENT '字体大小',
    privacy_settings TEXT COMMENT '隐私设置JSON',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (member_id)
) COMMENT='用户设置表';
```

## 十八、文件管理表

### 1. 文件表(sys_file)
```sql
CREATE TABLE sys_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文件ID',
    file_name VARCHAR(200) NOT NULL COMMENT '文件名称',
    original_name VARCHAR(200) NOT NULL COMMENT '原始文件名',
    file_url VARCHAR(500) NOT NULL COMMENT '文件URL',
    file_path VARCHAR(500) COMMENT '文件路径',
    storage_type TINYINT NOT NULL COMMENT '存储类型 1-本地 2-阿里云OSS 3-腾讯云COS 4-七牛云',
    file_size BIGINT NOT NULL COMMENT '文件大小(B)',
    file_type VARCHAR(50) COMMENT '文件类型',
    file_extension VARCHAR(20) COMMENT '文件扩展名',
    md5 VARCHAR(32) COMMENT 'MD5值',
    category_id BIGINT COMMENT '分类ID',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_md5 (md5),
    KEY idx_category_id (category_id)
) COMMENT='文件表';
```

### 2. 文件分类表(sys_file_category)
```sql
CREATE TABLE sys_file_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    category_name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_parent_id (parent_id)
) COMMENT='文件分类表';
```

### 3. 文件访问记录表(sys_file_access)
```sql
CREATE TABLE sys_file_access (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    file_id BIGINT NOT NULL COMMENT '文件ID',
    user_id BIGINT COMMENT '用户ID',
    user_type TINYINT COMMENT '用户类型 1-管理员 2-会员',
    ip VARCHAR(50) COMMENT 'IP地址',
    access_type TINYINT NOT NULL COMMENT '访问类型 1-浏览 2-下载',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_file_id (file_id),
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time)
) COMMENT='文件访问记录表';
```

### 4. 文件分享表(sys_file_share)
```sql
CREATE TABLE sys_file_share (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分享ID',
    file_id BIGINT NOT NULL COMMENT '文件ID',
    share_code VARCHAR(32) NOT NULL COMMENT '分享码',
    share_user_id BIGINT NOT NULL COMMENT '分享人ID',
    access_count INT DEFAULT 0 COMMENT '访问次数',
    expire_time DATETIME COMMENT '过期时间',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    UNIQUE KEY (share_code),
    KEY idx_file_id (file_id),
    KEY idx_share_user_id (share_user_id)
) COMMENT='文件分享表';
```

## 十九、其他配套表

### 1. 银行表(sys_bank)
```sql
CREATE TABLE sys_bank (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '银行ID',
    bank_code VARCHAR(20) NOT NULL COMMENT '银行编码',
    bank_name VARCHAR(50) NOT NULL COMMENT '银行名称',
    logo VARCHAR(255) COMMENT '银行logo',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    sort INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (bank_code)
) COMMENT='银行表';
```

### 2. 协议表(sys_agreement)
```sql
CREATE TABLE sys_agreement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '协议ID',
    agreement_name VARCHAR(100) NOT NULL COMMENT '协议名称',
    agreement_code VARCHAR(50) NOT NULL COMMENT '协议编码',
    content TEXT NOT NULL COMMENT '协议内容',
    version VARCHAR(20) NOT NULL COMMENT '协议版本',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME COMMENT '更新时间',
    create_by BIGINT COMMENT '创建人',
    update_by BIGINT COMMENT '更新人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    UNIQUE KEY (agreement_code, version)
) COMMENT='协议表';
```

### 3. 附件表(sys_attachment)
```sql
CREATE TABLE sys_attachment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '附件ID',
    module_type VARCHAR(50) NOT NULL COMMENT '模块类型',
    module_id BIGINT NOT NULL COMMENT '模块ID',
    file_id BIGINT NOT NULL COMMENT '文件ID',
    file_url VARCHAR(500) NOT NULL COMMENT '文件URL',
    file_name VARCHAR(200) NOT NULL COMMENT '文件名称',
    file_size BIGINT NOT NULL COMMENT '文件大小',
    file_type VARCHAR(50) COMMENT '文件类型',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    create_by BIGINT COMMENT '创建人',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    KEY idx_module (module_type, module_id),
    KEY idx_file_id (file_id)
) COMMENT='附件表';
```

### 4. 短信记录表(sys_sms_record)
```sql
CREATE TABLE sys_sms_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    mobile VARCHAR(20) NOT NULL COMMENT '手机号',
    sms_type TINYINT NOT NULL COMMENT '短信类型 1-验证码 2-通知 3-营销',
    template_code VARCHAR(50) NOT NULL COMMENT '模板编码',
    template_param VARCHAR(500) COMMENT '模板参数',
    content VARCHAR(500) COMMENT '发送内容',
    send_status TINYINT DEFAULT 0 COMMENT '发送状态 0-未发送 1-发送中 2-发送成功 3-发送失败',
    send_time DATETIME COMMENT '发送时间',
    provider VARCHAR(50) COMMENT '服务商',
    error_msg VARCHAR(500) COMMENT '错误消息',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_mobile (mobile),
    KEY idx_send_status (send_status),
    KEY idx_create_time (create_time)
) COMMENT='短信记录表';
```

### 5. 邮件记录表(sys_email_record)
```sql
CREATE TABLE sys_email_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    email VARCHAR(100) NOT NULL COMMENT '邮箱地址',
    email_type TINYINT NOT NULL COMMENT '邮件类型 1-验证码 2-通知 3-营销',
    subject VARCHAR(200) NOT NULL COMMENT '邮件主题',
    content TEXT COMMENT '邮件内容',
    template_code VARCHAR(50) COMMENT '模板编码',
    template_param VARCHAR(500) COMMENT '模板参数',
    attachments VARCHAR(500) COMMENT '附件',
    send_status TINYINT DEFAULT 0 COMMENT '发送状态 0-未发送 1-发送中 2-发送成功 3-发送失败',
    send_time DATETIME COMMENT '发送时间',
    error_msg VARCHAR(500) COMMENT '错误消息',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    KEY idx_email (email),
    KEY idx_send_status (send_status),
    KEY idx_create_time (create_time)
) COMMENT='邮件记录表';
```

INSERT INTO sys_role (role_name, role_code, description, status, create_time, is_deleted)
VALUES ('超级管理员', 'admin', '系统最高权限', 1, NOW(), 0);


