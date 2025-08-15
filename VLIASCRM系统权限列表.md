# 🔐 VLIASCRM系统权限列表

> 本文档包含VLIASCRM系统中所有权限的详细信息
> 数据来源：sys_permission表
> 生成时间：{{ 当前时间 }}

## 📋 权限总览

本系统共包含 **128个权限点**，分为以下几个层级：
- **一级权限（模块级）**：系统顶级功能模块
- **二级权限（功能级）**：模块下的具体功能
- **三级权限（操作级）**：具体的CRUD操作

---

## 🏢 一级权限（功能模块）

| 权限名称 | 权限代码 | 描述 |
|---------|---------|-----|
| 组织架构 | `org` | 组织架构管理模块 |
| 系统管理 | `system` | 系统基础管理模块 |
| 商品管理 | `product` | 商品相关管理模块 |
| 订单管理 | `order` | 订单处理管理模块 |
| 客户管理 | `customer` | 客户关系管理模块 |
| 库存管理 | `inventory` | 库存管理模块 |
| 财务管理 | `finance` | 财务管理模块 |
| 会员管理 | `member` | 会员管理模块 |
| 营销管理 | `promotion` | 营销活动管理模块 |
| 采购管理 | `purchase` | 采购管理模块 |
| 供应链 | `supply` | 供应链管理模块 |
| 分销管理 | `distribution` | 分销管理模块 |
| 服务管理 | `service` | 售后服务管理模块 |
| 报表分析 | `report` | 数据报表分析模块 |
| API管理 | `api` | API接口管理模块 |
| 平台管理 | `platform-management` | 平台管理模块 |
| 个人中心 | `profile` | 个人信息管理模块 |

---

## 🏗️ 二级权限（功能管理）

### 🏢 组织架构模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 组织机构管理 | `org-management` | 组织架构 |
| 部门管理 | `dept-management` | 组织架构 |
| 岗位管理 | `position-management` | 组织架构 |
| 区域管理 | `region-management` | 组织架构 |

### ⚙️ 系统管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 用户管理 | `user-management` | 系统管理 |
| 角色管理 | `role-management` | 系统管理 |
| 权限管理 | `permission-management` | 系统管理 |
| 菜单管理 | `menu-management` | 系统管理 |
| 字典管理 | `dict-management` | 系统管理 |
| 系统配置 | `system-config` | 系统管理 |

### 📦 商品管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 商品信息管理 | `product-info-management` | 商品管理 |
| 商品分类管理 | `product-category-management` | 商品管理 |
| 商品品牌管理 | `product-brand-management` | 商品管理 |
| 商品规格管理 | `product-specification-management` | 商品管理 |
| 商品属性管理 | `product-attribute-management` | 商品管理 |
| SKU管理 | `product-sku-management` | 商品管理 |

### 📋 订单管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 订单处理 | `order-process` | 订单管理 |
| 支付管理 | `payment-management` | 订单管理 |
| 物流管理 | `logistics-management` | 订单管理 |
| 退换货管理 | `return-management` | 订单管理 |

### 👥 客户管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 客户信息管理 | `customer-info` | 客户管理 |
| 客户分组管理 | `customer-group` | 客户管理 |
| 跟进记录管理 | `follow-record` | 客户管理 |
| 公海管理 | `public-pool` | 客户管理 |

### 📦 库存管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 仓库管理 | `warehouse-management` | 库存管理 |
| 库存查询 | `stock-query` | 库存管理 |
| 出入库管理 | `stock-flow` | 库存管理 |
| 盘点管理 | `stock-check` | 库存管理 |

### 💰 财务管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 账户管理 | `account-management` | 财务管理 |
| 收支管理 | `transaction-management` | 财务管理 |
| 发票管理 | `invoice-management` | 财务管理 |
| 结算管理 | `settlement-management` | 财务管理 |

### 👤 会员管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 会员信息管理 | `member-info` | 会员管理 |
| 会员等级管理 | `member-level` | 会员管理 |
| 积分管理 | `points-management` | 会员管理 |
| 地址管理 | `address-management` | 会员管理 |

### 🎯 营销管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 优惠券管理 | `coupon-management` | 营销管理 |
| 促销活动管理 | `promotion-activity` | 营销管理 |
| 团购管理 | `group-buy` | 营销管理 |
| 秒杀管理 | `seckill-management` | 营销管理 |

### 🛒 采购管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 供应商管理 | `supplier-management` | 采购管理 |
| 收货管理 | `receipt-management` | 采购管理 |
| 采购退货管理 | `purchase-return` | 采购管理 |

### 🔗 供应链模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 供应链节点管理 | `supply-node` | 供应链 |
| 供应链关系管理 | `supply-relation` | 供应链 |
| 供应链监控 | `supply-monitor` | 供应链 |

### 🌐 分销管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 分销商管理 | `distributor-management` | 分销管理 |
| 分销等级管理 | `distribution-level` | 分销管理 |
| 佣金管理 | `commission-management` | 分销管理 |
| 提现管理 | `withdraw-management` | 分销管理 |

### 🛠️ 服务管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 工单管理 | `ticket-management` | 服务管理 |
| 维修管理 | `repair-management` | 服务管理 |
| 退换货服务 | `return-service` | 服务管理 |
| 服务评价管理 | `evaluation-management` | 服务管理 |

### 📊 报表分析模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 销售报表 | `sales-report` | 报表分析 |
| 客户报表 | `customer-report` | 报表分析 |
| 商品报表 | `product-report` | 报表分析 |
| 自定义报表 | `custom-report` | 报表分析 |

### 🔌 API管理模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| API配置管理 | `api-config` | API管理 |
| API权限管理 | `api-permission` | API管理 |
| API日志管理 | `api-log` | API管理 |

### 👤 个人中心模块
| 权限名称 | 权限代码 | 所属模块 |
|---------|---------|---------|
| 个人信息管理 | `profile-info-management` | 个人中心 |

---

## ⚡ 三级权限（具体操作）

### 🏢 组织架构操作权限

#### 组织机构管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看组织 | `org-management:view` | 查看 |
| 新增组织 | `org-management:create` | 创建 |
| 编辑组织 | `org-management:edit` | 编辑 |
| 删除组织 | `org-management:delete` | 删除 |

#### 部门管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看部门 | `dept-management:view` | 查看 |
| 新增部门 | `dept-management:create` | 创建 |
| 编辑部门 | `dept-management:edit` | 编辑 |
| 删除部门 | `dept-management:delete` | 删除 |

#### 岗位管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看岗位 | `position-management:view` | 查看 |
| 新增岗位 | `position-management:create` | 创建 |
| 编辑岗位 | `position-management:edit` | 编辑 |
| 删除岗位 | `position-management:delete` | 删除 |

### ⚙️ 系统管理操作权限

#### 用户管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看用户 | `user-management:view` | 查看 |
| 新增用户 | `user-management:create` | 创建 |
| 编辑用户 | `user-management:edit` | 编辑 |
| 删除用户 | `user-management:delete` | 删除 |
| 重置密码 | `user-management:reset-password` | 特殊操作 |

#### 角色管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看角色 | `role-management:view` | 查看 |
| 新增角色 | `role-management:create` | 创建 |
| 编辑角色 | `role-management:edit` | 编辑 |
| 删除角色 | `role-management:delete` | 删除 |
| 分配权限 | `role-management:assign-permission` | 特殊操作 |

#### 权限管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看权限 | `permission-management:view` | 查看 |
| 新增权限 | `permission-management:create` | 创建 |
| 编辑权限 | `permission-management:edit` | 编辑 |
| 删除权限 | `permission-management:delete` | 删除 |
| 同步权限 | `permission-management:sync` | 特殊操作 |
| 重置权限 | `permission-management:reset` | 特殊操作 |
| 验证权限 | `permission-management:validate` | 特殊操作 |

#### 菜单管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看菜单 | `menu-management:view` | 查看 |
| 新增菜单 | `menu-management:create` | 创建 |
| 编辑菜单 | `menu-management:edit` | 编辑 |
| 删除菜单 | `menu-management:delete` | 删除 |

#### 字典管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看字典 | `dict-management:view` | 查看 |
| 新增字典 | `dict-management:create` | 创建 |
| 编辑字典 | `dict-management:edit` | 编辑 |
| 删除字典 | `dict-management:delete` | 删除 |

### 📦 商品管理操作权限

#### 商品信息管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看商品 | `product-info-management:view` | 查看 |
| 新增商品 | `product-info-management:create` | 创建 |
| 编辑商品 | `product-info-management:edit` | 编辑 |
| 删除商品 | `product-info-management:delete` | 删除 |

#### 商品分类管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看分类 | `product-category-management:view` | 查看 |
| 新增分类 | `product-category-management:create` | 创建 |
| 编辑分类 | `product-category-management:edit` | 编辑 |
| 删除分类 | `product-category-management:delete` | 删除 |

#### 商品品牌管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看品牌 | `product-brand-management:view` | 查看 |
| 新增品牌 | `product-brand-management:create` | 创建 |
| 编辑品牌 | `product-brand-management:edit` | 编辑 |
| 删除品牌 | `product-brand-management:delete` | 删除 |

#### 商品规格管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看规格 | `product-specification-management:view` | 查看 |
| 新增规格 | `product-specification-management:create` | 创建 |
| 编辑规格 | `product-specification-management:edit` | 编辑 |
| 删除规格 | `product-specification-management:delete` | 删除 |

#### 商品属性管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看属性 | `product-attribute-management:view` | 查看 |
| 新增属性 | `product-attribute-management:create` | 创建 |
| 编辑属性 | `product-attribute-management:edit` | 编辑 |
| 删除属性 | `product-attribute-management:delete` | 删除 |

#### SKU管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看SKU | `product-sku-management:view` | 查看 |
| 新增SKU | `product-sku-management:create` | 创建 |
| 编辑SKU | `product-sku-management:edit` | 编辑 |
| 删除SKU | `product-sku-management:delete` | 删除 |

### 👤 个人中心操作权限

#### 个人信息管理操作
| 权限名称 | 权限代码 | 操作类型 |
|---------|---------|---------|
| 查看信息 | `profile-info-management:view` | 查看 |
| 编辑信息 | `profile-info-management:edit` | 编辑 |
| 修改密码 | `profile-info-management:password` | 特殊操作 |

---

## 🧪 测试权限

| 权限名称 | 权限代码 | 描述 |
|---------|---------|-----|
| 测试管理-权限 | `test:manage` | 测试环境权限管理 |

---

## 📝 权限命名规范

### 权限代码命名规则
1. **模块级权限**：使用简单英文单词，如 `system`、`product`
2. **功能级权限**：使用 `功能-management` 格式，如 `user-management`
3. **操作级权限**：使用 `功能:操作` 格式，如 `user-management:view`

### 常用操作类型
- `view`：查看/查询权限
- `create`：新增/创建权限
- `edit`：编辑/修改权限
- `delete`：删除权限
- 特殊操作：如 `reset-password`、`assign-permission` 等

---

## 🔍 权限使用说明

1. **权限继承**：拥有上级权限自动拥有下级权限
2. **权限组合**：实际业务中需要组合多个权限使用
3. **权限验证**：系统通过 `@PreAuthorize` 注解进行权限验证
4. **动态权限**：支持运行时动态权限验证和菜单渲染

---

*本文档由系统自动生成，如有疑问请联系系统管理员。* 