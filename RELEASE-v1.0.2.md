# VLIAS CRM/ERP 系统 - 版本 1.0.2 发布说明

## 发布日期
2025年1月9日

## 版本亮点
这个版本主要专注于完善采购管理模块，修复了多个关键Bug，并新增了重要的业务功能模块。

## 🚀 新功能

### 1. 采购对账模块
- 新增采购对账功能，支持供应商对账管理
- 提供对账单创建、编辑、审核流程
- 支持对账明细管理和状态跟踪

### 2. 仓库管理增强
- 新增仓库管理功能模块
- 支持库位管理和地区管理
- 完善库存定位和管理体系

### 3. 采购退货完善
- 优化采购退货功能流程
- 完善退货单表单和列表管理
- 增强退货审核和状态管理

## 🔧 Bug修复

### 1. 数据类型一致性修复
- 修复 `PurReceiptServiceImpl` 中Integer与BigDecimal类型不匹配问题
- 修复 `PurOrderService` 中数量字段类型转换问题  
- 统一采购相关模块的数据类型定义

### 2. 前端组件优化
- 修复商品选择器组件的数据绑定问题
- 优化供应商商品选择器功能
- 完善路由配置和权限管理

### 3. 数据库结构优化
- 修复采购订单字段类型一致性问题
- 添加新的数据库迁移脚本
- 清理过时的数据库文件

## 📈 改进

### 1. 代码质量提升
- 优化服务层实现，提高代码可维护性
- 完善错误处理和日志记录
- 统一编码规范和注释

### 2. 用户体验优化
- 改进前端显示映射逻辑
- 优化权限检查机制
- 提升页面交互体验

### 3. 系统稳定性
- 修复多个潜在的空指针异常
- 增强数据验证和错误处理
- 提高系统整体稳定性

## 🗂️ 新增文件

### 后端模块
- `InvAreaController.java` - 地区管理控制器
- `InvLocationController.java` - 库位管理控制器  
- `InvWarehouseController.java` - 仓库管理控制器
- `PurReconciliationController.java` - 采购对账控制器
- 相关的DTO、Entity、Service和Repository类

### 前端模块  
- `warehouse/` - 仓库管理页面目录
- `reconciliation/` - 对账管理页面目录
- 相关的API接口和工具类

### 数据库
- `fix_purchase_order_field_types.sql` - 字段类型修复脚本
- `add_supplier_goods_spec_unit.sql` - 供应商商品规格单位

## 📋 技术说明

### 依赖版本
- Spring Boot: 保持当前版本
- Vue.js: 保持当前版本  
- MySQL: 保持当前版本

### 兼容性
- 向后兼容之前的1.0.x版本
- 需要执行数据库迁移脚本
- 建议在升级前备份数据库

## 🔄 升级指南

1. **备份数据库**
   ```bash
   mysqldump -u username -p vliascrm > backup_before_v1.0.2.sql
   ```

2. **更新代码**
   ```bash
   git pull origin main
   git checkout v1.0.2
   ```

3. **执行数据库迁移**
   - 运行 `fix_purchase_order_field_types.sql`
   - 运行 `add_supplier_goods_spec_unit.sql`

4. **重启应用**
   ```bash
   ./mvnw spring-boot:run
   ```

## 🐛 已知问题
暂无已知问题

## 📞 技术支持
如有问题，请通过以下方式联系：
- GitHub Issues: [创建新Issue](https://github.com/beiqi-ux/VLIAS-CRM-ERP/issues)
- 邮箱: qichengxu@example.com

## 🙏 致谢
感谢所有参与此版本开发和测试的开发者和用户！

---
**VLIAS CRM/ERP 开发团队**  
*让企业管理更简单、更高效* 