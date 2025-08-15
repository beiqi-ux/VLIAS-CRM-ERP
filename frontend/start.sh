#!/bin/bash

# VLIAS CRM 前端项目启动脚本

echo "🚀 启动 VLIAS CRM 前端项目..."

# 检查 Node.js 是否安装
if ! command -v node &> /dev/null; then
    echo "❌ 错误：未找到 Node.js，请先安装 Node.js"
    exit 1
fi

# 检查 npm 是否安装
if ! command -v npm &> /dev/null; then
    echo "❌ 错误：未找到 npm，请先安装 npm"
    exit 1
fi

# 检查 package.json 是否存在
if [ ! -f "package.json" ]; then
    echo "❌ 错误：未找到 package.json 文件，请确保在正确的项目目录中"
    exit 1
fi

# 检查 node_modules 是否存在，如果不存在则安装依赖
if [ ! -d "node_modules" ]; then
    echo "📦 安装项目依赖..."
    npm install
    if [ $? -ne 0 ]; then
        echo "❌ 依赖安装失败"
        exit 1
    fi
    echo "✅ 依赖安装完成"
fi

# 检查后端服务是否运行
echo "🔍 检查后端服务状态..."
if curl -s http://localhost:8080/api/auth/info > /dev/null 2>&1; then
    echo "✅ 后端服务运行正常"
else
    echo "⚠️  警告：后端服务可能未启动，请确保后端服务在 http://localhost:8080 运行"
    echo "   前端仍可启动，但 API 请求可能会失败"
fi

# 启动开发服务器
echo "🌐 启动开发服务器..."
echo "   前端地址：http://localhost:5173"
echo "   后端代理：http://localhost:8080"
echo ""
echo "按 Ctrl+C 停止服务器"
echo ""

npm run dev 