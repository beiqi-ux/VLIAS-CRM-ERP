#!/bin/bash

# 登录获取token
echo "正在登录..."
TOKEN=$(curl -s -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "18845913092",
    "password": "qichengxu1234"
  }' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

echo "Token: $TOKEN"

# 检查用户权限
echo -e "\n正在检查用户权限..."
curl -s -X GET "http://localhost:8080/api/auth/user-info" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" | python3 -m json.tool

# 测试产品分类接口 - 使用正确的路径
echo -e "\n正在测试产品分类API..."
echo "原始响应:"
RESPONSE=$(curl -s -X GET "http://localhost:8080/api/prod/categories/admin-tree" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json")

echo "$RESPONSE"

echo -e "\n响应长度: ${#RESPONSE}"

if [ ${#RESPONSE} -gt 0 ]; then
    echo -e "\n格式化的JSON:"
    echo "$RESPONSE" | python3 -m json.tool
fi 