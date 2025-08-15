# 数据库备份内容概览

## 📁 备份文件位置
- **绝对路径**: `/Users/qichengxu/IdeaProjects/VLIASCRM/backup/database/`
- **主备份文件**: `vliascrm_backup_20250805_221518.sql` (218KB)

## 📊 备份内容统计
- **总行数**: 4,752 行
- **表结构数**: 116 个表
- **数据表数**: 12 个表有数据

## 🗄️ 包含数据的表

| 表名 | 数据量 | 说明 |
|------|--------|------|
| org_department | 1条 | 部门信息（技术部） |
| prod_brand | 3条 | 品牌信息（苹果、华为、小米） |
| prod_category | 3条 | 商品分类（电子产品、服装鞋帽、家居用品） |
| sys_dict | 5条 | 数据字典（用户状态、商品状态等） |
| sys_dict_item | 14条 | 字典项详细信息 |
| sys_menu | 25条 | 系统菜单结构 |
| sys_organization | 1条 | 组织机构（VLIAS总公司） |
| sys_permission | 166条 | 权限配置 |
| sys_role | 5条 | 角色信息 |
| sys_role_permission | 621条 | 角色权限关联 |
| sys_user | 详细数据 | 用户信息 |
| sys_user_role | 详细数据 | 用户角色关联 |

## 🔑 核心数据内容

### 系统角色
- 超级管理员 (admin)
- 品牌管理员 (BRAND_ADMIN)  
- 经理 (MANAGER)
- 员工 (EMPLOYEE)
- 仓库管理员 (WAREHOUSE)

### 权限模块
- 组织架构管理 (166条权限)
- 系统管理
- 商品管理  
- 订单管理
- 客户管理
- 库存管理
- 财务管理
- 会员管理
- 营销管理
- 采购管理
- 供应链管理
- 分销管理
- 服务管理
- 报表分析
- API管理

### 菜单结构
- 数据面板 (dashboard)
- 系统管理 (system)
- 组织架构 (org)
- 商品管理 (product)
- 个人中心 (profile)

## 💾 恢复说明
数据库备份包含完整的表结构和核心业务数据，可以完全恢复系统的权限体系、用户管理、商品分类等基础功能。 