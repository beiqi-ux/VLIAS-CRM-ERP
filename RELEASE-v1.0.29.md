# VLIASCRM v1.0.29 发布说明

## 📦 发布信息
- **版本号**: v1.0.29
- **发布日期**: 2024年8月14日
- **发布类型**: 完整代码包
- **压缩包**: VLIASCRM-v1.0.29-full.tar.gz (3.4MB)

## 🚀 主要功能特性

### 🆕 新增功能
1. **进销存管理系统**
   - 供应商管理模块
   - 采购订单管理
   - 仓库管理功能
   - 商品库存管理

2. **前端组件优化**
   - 新增商品选择器组件 (GoodsSelector)
   - 优化响应处理机制
   - 统一API响应处理

3. **权限管理增强**
   - 权限编码一致性检查
   - 菜单权限优化
   - 用户权限管理改进

### 🔧 技术改进
- **后端优化**
  - 新增采购相关实体类和服务
  - 优化数据库连接和事务管理
  - 代码清理和结构优化

- **前端优化**
  - 统一状态管理
  - 响应处理机制优化
  - 页面性能提升

## 📁 项目结构

```
VLIASCRM/
├── backend/                    # 后端Spring Boot应用
│   ├── src/main/java/         # Java源码
│   ├── src/main/resources/    # 配置文件
│   └── pom.xml               # Maven配置
├── frontend/                  # 前端Vue.js应用
│   ├── src/                  # 源码目录
│   ├── public/               # 静态资源
│   └── package.json          # npm配置
├── database/                  # 数据库脚本
└── docs/                     # 项目文档
```

## 🛠 技术栈

### 后端
- **框架**: Spring Boot 2.7.x
- **数据库**: MySQL 8.0+
- **ORM**: Spring Data JPA
- **安全**: Spring Security + JWT
- **构建工具**: Maven 3.6+

### 前端
- **框架**: Vue.js 3.x
- **UI库**: Element Plus
- **状态管理**: Vuex
- **路由**: Vue Router
- **构建工具**: Vite

## 📋 系统要求

### 开发环境
- JDK 11 或更高版本
- Node.js 16+ 和 npm 8+
- MySQL 8.0+
- Git

### 生产环境
- Java 11 运行时
- MySQL 8.0 数据库
- Nginx (推荐)

## 🚀 快速开始

### 1. 解压项目
```bash
tar -xzf VLIASCRM-v1.0.29-full.tar.gz
cd VLIASCRM
```

### 2. 数据库配置
1. 创建MySQL数据库
2. 执行 `database/` 目录下的SQL脚本
3. 配置 `src/main/resources/application.yml`

### 3. 后端启动
```bash
mvn clean install
mvn spring-boot:run
```

### 4. 前端启动
```bash
cd frontend
npm install
npm run dev
```

## 🔧 配置说明

### 数据库配置
修改 `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vliascrm
    username: your_username
    password: your_password
```

### 前端配置
修改 `frontend/src/config/`:
- API基础URL配置
- 路由配置
- 权限配置

## 📊 项目统计

- **总文件数**: 200+ 文件
- **代码行数**: 15,000+ 行
- **功能模块**: 12个主要模块
- **数据表**: 25+ 张表

## 🔐 默认账户

### 超级管理员
- **用户名**: admin
- **密码**: admin123
- **权限**: 系统全部权限

### 测试账户
- **用户名**: test
- **密码**: test123
- **权限**: 基础查看权限

## 📞 技术支持

如有问题，请联系开发团队或查看项目文档。

## 📝 更新日志

### v1.0.29 (2024-08-14)
- 新增进销存管理功能
- 优化前端响应处理
- 权限管理增强
- 代码结构优化
- 性能提升

---
**注意**: 本版本为完整代码包，包含项目的所有源码文件和配置。请根据实际环境调整配置参数。 