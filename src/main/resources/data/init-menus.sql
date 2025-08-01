-- 菜单初始化数据
-- 清空菜单表
DELETE FROM sys_menu WHERE id > 0;

-- 重置自增ID
ALTER TABLE sys_menu AUTO_INCREMENT = 1;

-- 系统管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '系统管理', 'system', 1, '/system', null, 'Setting', 1, 1, 1, 'system', 0, NOW(), NOW(), 1, 1, 0),
(1, '用户管理', 'system:user', 2, '/users', 'system/UserList', 'User', 1, 1, 1, 'system:user:list', 0, NOW(), NOW(), 1, 1, 0),
(1, '角色管理', 'system:role', 2, '/roles', 'system/RoleList', 'UserFilled', 2, 1, 1, 'system:role:list', 0, NOW(), NOW(), 1, 1, 0),
(1, '权限管理', 'system:permission', 2, '/permissions', 'system/PermissionList', 'Lock', 3, 1, 1, 'system:permission:list', 0, NOW(), NOW(), 1, 1, 0),
(1, '菜单管理', 'system:menu', 2, '/menus', 'system/MenuList', 'Menu', 4, 1, 1, 'system:menu:list', 0, NOW(), NOW(), 1, 1, 0),
(1, '数据字典', 'system:dict', 2, '/dicts', 'system/DictList', 'Collection', 5, 1, 1, 'system:dict:list', 0, NOW(), NOW(), 1, 1, 0);

-- 组织架构模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '组织架构', 'org', 1, '/org', null, 'OfficeBuilding', 2, 1, 1, 'org', 0, NOW(), NOW(), 1, 1, 0),
(7, '组织机构', 'org:organization', 2, '/organizations', 'system/OrganizationList', 'SetUp', 1, 1, 1, 'org:organization:list', 0, NOW(), NOW(), 1, 1, 0),
(7, '部门管理', 'org:department', 2, '/departments', 'system/DepartmentList', 'Files', 2, 1, 1, 'org:department:list', 0, NOW(), NOW(), 1, 1, 0),
(7, '岗位管理', 'org:position', 2, '/positions', 'system/PositionList', 'List', 3, 1, 1, 'org:position:list', 0, NOW(), NOW(), 1, 1, 0);

-- 商品管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '商品管理', 'product', 1, '/product', null, 'Goods', 3, 1, 1, 'product', 0, NOW(), NOW(), 1, 1, 0),
(11, '商品管理', 'product:goods', 2, '/goods', 'product/GoodsList', 'Box', 1, 1, 1, 'product:goods:list', 0, NOW(), NOW(), 1, 1, 0),
(11, '分类管理', 'product:category', 2, '/categories', 'product/CategoryList', 'Grid', 2, 1, 1, 'product:category:list', 0, NOW(), NOW(), 1, 1, 0),
(11, '品牌管理', 'product:brand', 2, '/brands', 'product/BrandList', 'Star', 3, 1, 1, 'product:brand:list', 0, NOW(), NOW(), 1, 1, 0);

-- 客户管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '客户管理', 'crm', 1, '/crm', null, 'Document', 4, 1, 1, 'crm', 0, NOW(), NOW(), 1, 1, 0),
(15, '客户管理', 'crm:customer', 2, '/customers', 'crm/CustomerList', 'Document', 1, 1, 1, 'crm:customer:list', 0, NOW(), NOW(), 1, 1, 0),
(15, '联系人管理', 'crm:contact', 2, '/contacts', 'crm/ContactList', 'User', 2, 1, 1, 'crm:contact:list', 0, NOW(), NOW(), 1, 1, 0),
(15, '线索管理', 'crm:lead', 2, '/leads', 'crm/LeadList', 'Calendar', 3, 1, 1, 'crm:lead:list', 0, NOW(), NOW(), 1, 1, 0);

-- 订单管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '订单管理', 'order', 1, '/order', null, 'Calendar', 5, 1, 1, 'order', 0, NOW(), NOW(), 1, 1, 0),
(19, '订单列表', 'order:list', 2, '/orders', 'order/OrderList', 'Document', 1, 1, 1, 'order:list:list', 0, NOW(), NOW(), 1, 1, 0),
(19, '支付管理', 'order:payment', 2, '/payments', 'order/PaymentList', 'Calendar', 2, 1, 1, 'order:payment:list', 0, NOW(), NOW(), 1, 1, 0);

-- 库存管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '库存管理', 'inventory', 1, '/inventory', null, 'Box', 6, 1, 1, 'inventory', 0, NOW(), NOW(), 1, 1, 0),
(22, '库存管理', 'inventory:stock', 2, '/stocks', 'inventory/StockList', 'Box', 1, 1, 1, 'inventory:stock:list', 0, NOW(), NOW(), 1, 1, 0),
(22, '库存盘点', 'inventory:check', 2, '/checks', 'inventory/CheckList', 'Document', 2, 1, 1, 'inventory:check:list', 0, NOW(), NOW(), 1, 1, 0);

-- 采购管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '采购管理', 'purchase', 1, '/purchase', null, 'Document', 7, 1, 1, 'purchase', 0, NOW(), NOW(), 1, 1, 0),
(25, '供应商管理', 'purchase:supplier', 2, '/suppliers', 'purchase/SupplierList', 'Document', 1, 1, 1, 'purchase:supplier:list', 0, NOW(), NOW(), 1, 1, 0),
(25, '采购订单', 'purchase:order', 2, '/purchase-orders', 'purchase/PurchaseOrderList', 'Calendar', 2, 1, 1, 'purchase:order:list', 0, NOW(), NOW(), 1, 1, 0);

-- 促销管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '促销管理', 'promotion', 1, '/promotion', null, 'Star', 8, 1, 1, 'promotion', 0, NOW(), NOW(), 1, 1, 0),
(28, '优惠券管理', 'promotion:coupon', 2, '/coupons', 'promotion/CouponList', 'Star', 1, 1, 1, 'promotion:coupon:list', 0, NOW(), NOW(), 1, 1, 0),
(28, '活动管理', 'promotion:activity', 2, '/activities', 'promotion/ActivityList', 'Calendar', 2, 1, 1, 'promotion:activity:list', 0, NOW(), NOW(), 1, 1, 0);

-- 财务管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '财务管理', 'finance', 1, '/finance', null, 'Money', 9, 1, 1, 'finance', 0, NOW(), NOW(), 1, 1, 0),
(31, '账单管理', 'finance:bill', 2, '/bills', 'finance/BillList', 'Money', 1, 1, 1, 'finance:bill:list', 0, NOW(), NOW(), 1, 1, 0),
(31, '结算管理', 'finance:settlement', 2, '/settlements', 'finance/SettlementList', 'Document', 2, 1, 1, 'finance:settlement:list', 0, NOW(), NOW(), 1, 1, 0);

-- 会员管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '会员管理', 'member', 1, '/member', null, 'User', 10, 1, 1, 'member', 0, NOW(), NOW(), 1, 1, 0),
(34, '会员列表', 'member:list', 2, '/members', 'member/MemberList', 'User', 1, 1, 1, 'member:list:list', 0, NOW(), NOW(), 1, 1, 0),
(34, '会员等级', 'member:level', 2, '/member-levels', 'member/LevelList', 'Star', 2, 1, 1, 'member:level:list', 0, NOW(), NOW(), 1, 1, 0);

-- 消息管理模块
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '消息管理', 'message', 1, '/message', null, 'Bell', 11, 1, 1, 'message', 0, NOW(), NOW(), 1, 1, 0),
(37, '消息通知', 'message:notification', 2, '/notifications', 'message/NotificationList', 'Bell', 1, 1, 1, 'message:notification:list', 0, NOW(), NOW(), 1, 1, 0),
(37, '系统日志', 'message:log', 2, '/logs', 'message/LogList', 'Document', 2, 1, 1, 'message:log:list', 0, NOW(), NOW(), 1, 1, 0);

-- 个人中心
INSERT INTO sys_menu (parent_id, menu_name, menu_code, menu_type, path, component, icon, sort, visible, status, permission_code, is_frame, create_time, update_time, create_by, update_by, is_deleted) VALUES
(0, '个人中心', 'profile', 2, '/profile', 'system/UserProfile', 'User', 12, 1, 1, 'profile', 0, NOW(), NOW(), 1, 1, 0); 