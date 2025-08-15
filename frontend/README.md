# VLIAS CRM 前端项目

这是 VLIAS CRM 系统的前端项目，基于 Vue 3 + Element Plus 构建。

## 技术栈

- **Vue 3** - 渐进式 JavaScript 框架
- **Vite** - 下一代前端构建工具
- **Element Plus** - Vue 3 的组件库
- **Vue Router** - Vue.js 官方路由管理器
- **Pinia** - Vue 的状态管理库
- **Axios** - HTTP 客户端

## 功能特性

- 🔐 用户认证与授权
- 👥 用户管理（增删改查）
- 👤 个人资料管理
- 🔒 密码修改
- 📱 响应式设计
- 🎨 现代化 UI 界面

## 项目结构

```
frontend/
├── public/                 # 静态资源
├── src/
│   ├── api/               # API 接口
│   │   ├── auth.js        # 认证相关接口
│   │   └── user.js        # 用户管理接口
│   ├── assets/            # 资源文件
│   ├── components/        # 公共组件
│   ├── router/            # 路由配置
│   ├── stores/            # 状态管理
│   ├── utils/             # 工具函数
│   ├── views/             # 页面组件
│   │   ├── auth/          # 认证页面
│   │   ├── system/        # 系统管理页面
│   │   └── LayoutView.vue # 主布局
│   ├── App.vue            # 根组件
│   └── main.js            # 入口文件
├── index.html             # HTML 模板
├── package.json           # 项目配置
├── vite.config.js         # Vite 配置
└── README.md              # 项目说明
```

## 快速开始

### 环境要求

- Node.js >= 16.0.0
- npm >= 8.0.0

### 安装依赖

```bash
npm install
```

### 开发环境运行

```bash
npm run dev
```

或者使用启动脚本：

```bash
./start.sh
```

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 开发指南

### 环境变量

项目使用以下环境变量：

- `VITE_API_BASE_URL` - API 基础地址（默认：http://localhost:8080）

### API 配置

前端通过 Vite 的代理功能与后端 API 通信：

```javascript
// vite.config.js
export default {
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
}
```

### 路由配置

项目使用 Vue Router 进行路由管理，主要路由包括：

- `/` - 首页
- `/login` - 登录页
- `/system/users` - 用户管理
- `/profile` - 个人资料
- `/*` - 404 页面

### 状态管理

使用 Pinia 进行状态管理，主要 store：

- `user.js` - 用户认证状态管理

### 组件使用

项目使用 Element Plus 组件库，主要组件：

- `el-card` - 卡片容器
- `el-table` - 数据表格
- `el-form` - 表单
- `el-dialog` - 对话框
- `el-button` - 按钮
- `el-input` - 输入框

## 部署说明

### 构建

```bash
npm run build
```

构建完成后，`dist` 目录包含所有静态文件。

### 部署到服务器

将 `dist` 目录的内容部署到 Web 服务器（如 Nginx、Apache）即可。

### Nginx 配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;
    root /path/to/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 常见问题

### 1. 启动失败

检查：
- Node.js 版本是否符合要求
- 依赖是否安装完整
- 端口是否被占用

### 2. API 请求失败

检查：
- 后端服务是否启动
- 代理配置是否正确
- 网络连接是否正常

### 3. 路由跳转问题

检查：
- 路由配置是否正确
- 权限验证是否通过
- 组件是否正确导入

## 开发团队

- 前端开发：VLIAS 团队
- 技术支持：VLIAS CRM 项目组

## 许可证

本项目采用 MIT 许可证。 