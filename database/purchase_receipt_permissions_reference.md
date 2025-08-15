# 采购入库单管理权限编码参考

## 数据库记录概述

### 菜单记录 (sys_menu)
- **菜单ID**: 50
- **菜单名称**: 采购入库单管理
- **菜单编码**: pur-receipt-management
- **父级菜单**: 10 (采购管理)
- **菜单路径**: /purchase/receipt
- **Vue组件**: purchase/PurReceiptList

### 权限层级结构

#### 主权限 (菜单权限)
- **ID**: 235
- **权限名称**: 采购入库单管理
- **权限编码**: `pur-receipt-management`
- **权限类型**: 2 (菜单权限)
- **父级权限**: 10 (采购管理模块)

#### 功能权限 (所有子权限的父级为 235)

| ID  | 权限名称      | 权限编码                          | 功能描述           | 排序 |
|-----|-------------|----------------------------------|------------------|------|
| 236 | 查看入库单    | `pur-receipt-management:view`    | 查看采购入库单详情    | 1    |
| 237 | 入库单列表    | `pur-receipt-management:list`    | 查看采购入库单列表    | 2    |
| 238 | 新增入库单    | `pur-receipt-management:create`  | 新增采购入库单       | 3    |
| 239 | 编辑入库单    | `pur-receipt-management:edit`    | 编辑采购入库单       | 4    |
| 240 | 删除入库单    | `pur-receipt-management:delete`  | 删除采购入库单       | 5    |
| 241 | 提交入库单    | `pur-receipt-management:submit`  | 提交采购入库单审核    | 6    |
| 242 | 审核入库单    | `pur-receipt-management:audit`   | 审核采购入库单       | 7    |
| 243 | 确认入库      | `pur-receipt-management:confirm` | 确认采购入库单入库    | 8    |
| 244 | 取消入库单    | `pur-receipt-management:cancel`  | 取消采购入库单       | 9    |
| 245 | 复制入库单    | `pur-receipt-management:copy`    | 复制采购入库单       | 10   |
| 246 | 打印入库单    | `pur-receipt-management:print`   | 打印采购入库单       | 11   |
| 247 | 导出入库单    | `pur-receipt-management:export`  | 导出采购入库单       | 12   |

## 前端权限控制映射

以下是前端 Vue 组件中使用的权限编码映射：

### 工具栏按钮权限
- **新增入库单**: `pur-receipt-management:create`
- **批量删除**: `pur-receipt-management:delete`
- **批量确认入库**: `pur-receipt-management:confirm`
- **批量审核**: `pur-receipt-management:audit`
- **导出**: `pur-receipt-management:export`

### 表格操作列权限
- **查看**: `pur-receipt-management:view`
- **编辑**: `pur-receipt-management:edit`
- **提交**: `pur-receipt-management:submit`
- **审核通过/拒绝**: `pur-receipt-management:audit`
- **确认入库**: `pur-receipt-management:confirm`
- **复制**: `pur-receipt-management:copy`
- **打印**: `pur-receipt-management:print`

### 下拉菜单权限
- **取消**: `pur-receipt-management:cancel`
- **删除**: `pur-receipt-management:delete`

## 数据完整性验证结果

✅ **菜单表 (sys_menu)**: 所有必填字段已完整填写
✅ **权限表 (sys_permission)**: 共13条记录，所有必填字段已完整填写
✅ **权限层级**: 1个菜单权限 + 12个功能权限，层级结构正确
✅ **权限编码**: 全部遵循 `pur-receipt-management:action` 格式
✅ **时间戳**: 所有记录均包含创建时间和创建人
✅ **状态字段**: 所有权限均为启用状态 (status=1)

## 使用说明

1. 这些权限数据已成功写入 `vliascrm` 数据库
2. 前端 Vue 组件 `PurReceiptList.vue` 已配置相应的权限控制
3. 用户需要被分配相应权限才能看到和使用对应功能
4. 权限控制采用细粒度设计，支持精确的功能授权 