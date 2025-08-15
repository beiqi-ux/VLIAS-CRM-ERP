# VLIASCRM 眼镜店进销存管理系统

> 基于 Spring Boot + Vue.js 的现代化眼镜店CRM、电商和进销存一体化管理系统

## 🏪 项目简介

VLIASCRM 是专为眼镜店设计的综合性管理系统，集成了客户关系管理(CRM)、电商平台和进销存管理功能，帮助眼镜店实现数字化经营。

## ⭐ 核心功能

### 🛒 进销存管理
- **商品管理**: 眼镜商品分类、品牌、规格、SKU管理
- **采购管理**: 供应商管理、采购订单、入库管理
- **库存管理**: 实时库存、出入库记录、库存预警
- **销售管理**: 销售订单、退换货、销售统计

### 👥 客户关系管理
- **客户档案**: 完整的客户信息管理
- **验光记录**: 专业的验光数据管理
- **跟进记录**: 客户服务跟踪
- **会员体系**: 积分、等级、优惠管理

### 💼 系统管理
- **权限管理**: 细粒度的角色权限控制
- **员工管理**: 多角色用户管理
- **数据统计**: 销售、库存、客户数据分析
- **系统配置**: 灵活的参数配置

## 🏗️ 技术架构

### 后端技术栈
- **框架**: Spring Boot 2.7+
- **数据库**: MySQL 8.0+
- **安全**: Spring Security + JWT
- **ORM**: JPA + Hibernate
- **构建工具**: Maven

### 前端技术栈
- **框架**: Vue.js 3
- **UI库**: Element Plus
- **路由**: Vue Router
- **状态管理**: Vuex/Pinia
- **构建工具**: Vite

## 📁 项目结构

```
VLIASCRM/
├── src/                          # 后端源码
│   ├── main/java/               # Java源码
│   └── main/resources/          # 配置文件
├── frontend/                     # 前端源码
│   ├── src/                     # Vue.js源码
│   └── dist/                    # 构建输出
├── database/                     # 数据库脚本
├── docs/                        # 项目文档
│   ├── database/               # 数据库设计文档
│   ├── guides/                 # 使用指南
│   ├── planning/               # 开发计划
│   ├── analysis/               # 需求分析
│   ├── reports/                # 分析报告
│   └── technical/              # 技术文档
├── uploads/                     # 上传文件目录
├── pom.xml                      # Maven配置
└── README.md                    # 项目说明
```

## 🚀 快速开始

### 环境要求
- JDK 11+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 后端启动
```bash
# 克隆项目
git clone [repository-url]
cd VLIASCRM

# 配置数据库
# 1. 创建数据库: vliascrm
# 2. 导入数据库脚本: database/

# 配置application.yml中的数据库连接

# 启动后端服务
mvn spring-boot:run
```

### 前端启动
```bash
# 进入前端目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

## 📚 文档导航

### 📊 数据库设计
- [VLIAS数据库设计方案](docs/database/VLIAS数据库设计方案.md) - 完整的数据库表结构设计
- [权限3级结构设计文档](docs/database/权限3级结构设计文档.md) - 权限系统设计

### 📋 项目规划
- [眼镜店进销存开发计划](docs/planning/眼镜店进销存开发计划.md) - 开发路线图
- [眼镜行业进销存需求分析](docs/analysis/眼镜行业进销存需求分析.md) - 业务需求分析

### 📖 使用指南
- [眼镜商品管理数据录入指南](docs/guides/眼镜商品管理数据录入指南.md) - 商品数据管理
- [眼镜商品规格分类体系](docs/guides/眼镜商品规格分类体系.md) - 商品分类规范

### 🔒 系统管理
- [VLIASCRM系统权限列表](VLIASCRM系统权限列表.md) - 权限配置说明
- [权限编码一致性对比报告](docs/reports/权限编码一致性对比报告.md) - 权限审计报告

### 🛠️ 技术文档
- [技术栈说明](docs/technical/技术栈说明.md) - 技术选型说明
- [安全审计报告](docs/technical/SECURITY_AUDIT_REPORT.md) - 安全评估

## 🔧 开发指南

### 数据库管理
项目使用MySQL数据库，相关脚本位于 `database/` 目录：
- 初始化脚本
- 权限数据脚本
- 升级脚本

### API接口
后端提供RESTful API接口，主要模块包括：
- 用户管理: `/api/users`
- 商品管理: `/api/goods`
- 订单管理: `/api/orders`
- 库存管理: `/api/inventory`
- 客户管理: `/api/customers`

## 📄 版本信息

当前版本: v1.0.29 

查看详细更新日志: [RELEASE-v1.0.29.md](RELEASE-v1.0.29.md)

## 📞 联系方式

如有问题或建议，请通过以下方式联系：
- 项目Issues
- 邮箱: [your-email]
- QQ群: [group-number]

## 📜 许可证

本项目采用 [MIT License](LICENSE) 开源协议

---

> 💡 **提示**: 首次使用请先阅读 [眼镜商品管理数据录入指南](docs/guides/眼镜商品管理数据录入指南.md) 了解系统使用方法 