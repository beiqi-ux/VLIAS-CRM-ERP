# 商品属性和规格管理API测试示例

## 商品属性管理API

### 1. 分页查询商品属性
```bash
GET /api/product/attributes?attrName=品牌&page=1&size=10
Authorization: Bearer <token>
```

### 2. 根据商品ID查询属性列表
```bash
GET /api/product/attributes/goods/1
Authorization: Bearer <token>
```

### 3. 查询属性详情
```bash
GET /api/product/attributes/1
Authorization: Bearer <token>
```

### 4. 创建商品属性
```bash
POST /api/product/attributes
Authorization: Bearer <token>
Content-Type: application/json

{
    "goodsId": 1,
    "attrName": "处理器",
    "attrValue": "A16仿生芯片",
    "sort": 5
}
```

### 5. 更新商品属性
```bash
PUT /api/product/attributes/1
Authorization: Bearer <token>
Content-Type: application/json

{
    "goodsId": 1,
    "attrName": "处理器",
    "attrValue": "A16 Bionic芯片",
    "sort": 5
}
```

### 6. 删除商品属性
```bash
DELETE /api/product/attributes/1
Authorization: Bearer <token>
```

### 7. 批量删除商品属性
```bash
DELETE /api/product/attributes/batch
Authorization: Bearer <token>
Content-Type: application/json

[1, 2, 3]
```

## 商品规格管理API

### 1. 分页查询商品规格
```bash
GET /api/product/specifications?specName=颜色&page=1&size=10
Authorization: Bearer <token>
```

### 2. 根据商品ID查询规格列表
```bash
GET /api/product/specifications/goods/1
Authorization: Bearer <token>
```

### 3. 查询规格详情
```bash
GET /api/product/specifications/1
Authorization: Bearer <token>
```

### 4. 创建商品规格
```bash
POST /api/product/specifications
Authorization: Bearer <token>
Content-Type: application/json

{
    "goodsId": 1,
    "specName": "网络制式",
    "specValues": "[\"5G\", \"4G\"]"
}
```

### 5. 更新商品规格
```bash
PUT /api/product/specifications/1
Authorization: Bearer <token>
Content-Type: application/json

{
    "goodsId": 1,
    "specName": "网络制式",
    "specValues": "[\"5G\", \"4G\", \"3G\"]"
}
```

### 6. 删除商品规格
```bash
DELETE /api/product/specifications/1
Authorization: Bearer <token>
```

### 7. 批量删除商品规格
```bash
DELETE /api/product/specifications/batch
Authorization: Bearer <token>
Content-Type: application/json

[1, 2, 3]
```

## 权限说明

以下API需要对应的权限：

- **查看权限**: `product-attribute-management:view` 或 `product-specification-management:view`
- **新增权限**: `product-attribute-management:create` 或 `product-specification-management:create`
- **编辑权限**: `product-attribute-management:edit` 或 `product-specification-management:edit`
- **删除权限**: `product-attribute-management:delete` 或 `product-specification-management:delete`

## 响应格式

所有API都返回统一的响应格式：

```json
{
    "success": true,
    "message": "操作成功",
    "data": {
        // 具体数据
    }
}
```

## 分页响应示例

```json
{
    "success": true,
    "message": "查询成功",
    "data": {
        "content": [
            {
                "id": 1,
                "goodsId": 1,
                "attrName": "品牌",
                "attrValue": "Apple",
                "sort": 1,
                "createTime": "2024-01-01T10:00:00",
                "updateTime": "2024-01-01T10:00:00"
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 10
        },
        "totalElements": 1,
        "totalPages": 1,
        "first": true,
        "last": true
    }
}
``` 