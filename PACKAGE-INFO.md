# VLIASCRM v1.0.29 发布包信息

## 📦 包信息
- **文件名**: VLIASCRM-v1.0.29-full.tar.gz
- **文件大小**: 3.4MB
- **创建时间**: 2024年8月14日 22:07
- **MD5校验**: 4d696326180a306fd91a3c530bd99475

## 📁 包内容统计
- **总文件数**: 318个源代码文件
- **包含文件类型**:
  - Java源文件 (.java)
  - JavaScript文件 (.js)
  - Vue组件文件 (.vue)  
  - SQL脚本文件 (.sql)
  - Markdown文档 (.md)
  - 配置文件
  - 静态资源

## 🔍 验证方法

### 校验文件完整性
```bash
# 验证MD5校验和
md5 VLIASCRM-v1.0.29-full.tar.gz
# 应该输出: MD5 (VLIASCRM-v1.0.29-full.tar.gz) = 4d696326180a306fd91a3c530bd99475
```

### 查看包内容
```bash
# 列出所有文件
tar -tzf VLIASCRM-v1.0.29-full.tar.gz

# 解压到指定目录
tar -xzf VLIASCRM-v1.0.29-full.tar.gz -C /your/target/directory
```

## 📋 排除内容
为了减小包大小和避免冲突，已排除以下内容:
- `.git/` - Git版本控制文件
- `target/` - Maven构建输出
- `node_modules/` - Node.js依赖包
- `.DS_Store` - macOS系统文件
- `*.class` - Java编译文件
- `logs/` - 日志文件
- `.idea/` - IntelliJ IDEA配置
- `.vscode/` - VS Code配置

## 🚀 使用说明

### 解压项目
```bash
tar -xzf VLIASCRM-v1.0.29-full.tar.gz
cd VLIASCRM
```

### 项目结构
```
VLIASCRM/
├── src/main/java/             # Spring Boot后端源码
├── frontend/src/              # Vue.js前端源码
├── database/                  # 数据库脚本
├── docs/                      # 项目文档
├── RELEASE-v1.0.29.md        # 发布说明
├── pom.xml                   # Maven配置
└── README.md                 # 项目说明
```

## 🔧 依赖安装

### 后端依赖
```bash
mvn clean install
```

### 前端依赖
```bash
cd frontend
npm install
```

## ⚠️ 注意事项
1. 请确保Java 11+和Node.js 16+环境
2. 需要MySQL 8.0+数据库
3. 首次运行前请配置数据库连接
4. 建议在开发环境中先测试

## 📞 支持信息
- **版本**: v1.0.29
- **创建日期**: 2024-08-14
- **Git标签**: v1.0.29
- **兼容性**: 跨平台 (Windows/macOS/Linux)

---
**此包为VLIASCRM眼镜店管理系统的完整源代码包，包含所有必要的文件和配置。** 