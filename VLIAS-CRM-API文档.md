# VLIAS CRM系统接口文档

## 已实现接口清单

### 认证接口

#### 1. 用户登录
- **URL**: `/api/auth/login`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "username": "管理员账号",
    "password": "密码",
    "captcha": "验证码（可选）",
    "captchaKey": "验证码KEY（可选）",
    "rememberMe": true
  }
  ```
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": {
      "userId": 1,
      "username": "admin",
      "realName": "系统管理员",
      "avatar": "头像URL",
      "token": "JWT令牌",
      "tokenType": "Bearer",
      "expiresIn": 604800000,
      "roles": ["admin"],
      "permissions": ["sys:user:view"],
      "orgId": 1,
      "deptId": 1,
      "lastLoginTime": "2023-01-01 12:00:00"
    }
  }
  ```

#### 2. 退出登录
- **URL**: `/api/auth/logout`
- **方法**: POST
- **请求参数**: 无
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": null
  }
  ```

#### 3. 刷新令牌
- **URL**: `/api/auth/refresh`
- **方法**: POST
- **请求参数**: `token=旧令牌`
- **响应结果**: 与登录接口相同

#### 4. 获取当前用户信息
- **URL**: `/api/auth/info`
- **方法**: GET
- **请求参数**: 无（需在请求头中携带令牌）
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": {
      "userId": 1,
      "username": "admin",
      "realName": "系统管理员",
      "avatar": "头像URL",
      "roles": ["admin"],
      "permissions": ["sys:user:view"],
      "orgId": 1,
      "deptId": 1,
      "lastLoginTime": "2023-01-01 12:00:00"
    }
  }
  ```

### 用户管理接口

#### 1. 获取所有用户
- **URL**: `/api/sys/users`
- **方法**: GET
- **请求参数**: 无（需在请求头中携带令牌）
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": [
      {
        "id": 1,
        "username": "admin",
        "realName": "系统管理员",
        "email": "admin@example.com",
        "mobile": "13800138000",
        "avatar": "头像URL",
        "gender": 1,
        "orgId": 1,
        "deptId": 1,
        "positionId": 1,
        "status": 1,
        "lastLoginTime": "2023-01-01 12:00:00",
        "createTime": "2023-01-01 00:00:00",
        "updateTime": "2023-01-01 12:00:00"
      }
    ]
  }
  ```

#### 2. 获取单个用户
- **URL**: `/api/sys/users/{id}`
- **方法**: GET
- **请求参数**: 路径参数id为用户ID
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": {
      "id": 1,
      "username": "admin",
      "realName": "系统管理员",
      "email": "admin@example.com",
      "mobile": "13800138000",
      "avatar": "头像URL",
      "gender": 1,
      "orgId": 1,
      "deptId": 1,
      "positionId": 1,
      "status": 1,
      "lastLoginTime": "2023-01-01 12:00:00",
      "createTime": "2023-01-01 00:00:00",
      "updateTime": "2023-01-01 12:00:00"
    }
  }
  ```

#### 3. 创建用户
- **URL**: `/api/sys/users`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "username": "newuser",
    "password": "123456",
    "realName": "新用户",
    "email": "user@example.com",
    "mobile": "13900139000",
    "avatar": "头像URL",
    "gender": 1,
    "orgId": 1,
    "deptId": 1,
    "positionId": 1,
    "status": 1
  }
  ```
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": {
      "id": 2,
      "username": "newuser",
      "realName": "新用户",
      "email": "user@example.com",
      "mobile": "13900139000",
      "avatar": "头像URL",
      "gender": 1,
      "orgId": 1,
      "deptId": 1,
      "positionId": 1,
      "status": 1,
      "createTime": "2023-01-01 12:00:00",
      "updateTime": null
    }
  }
  ```

#### 4. 更新用户
- **URL**: `/api/sys/users/{id}`
- **方法**: PUT
- **请求参数**:
  ```json
  {
    "realName": "更新名称",
    "email": "updated@example.com",
    "mobile": "13800138001",
    "avatar": "新头像URL"
  }
  ```
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": {
      "id": 1,
      "username": "admin",
      "realName": "更新名称",
      "email": "updated@example.com",
      "mobile": "13800138001",
      "avatar": "新头像URL",
      "gender": 1,
      "orgId": 1,
      "deptId": 1,
      "positionId": 1,
      "status": 1,
      "createTime": "2023-01-01 00:00:00",
      "updateTime": "2023-01-02 12:00:00"
    }
  }
  ```

#### 5. 删除用户
- **URL**: `/api/sys/users/{id}`
- **方法**: DELETE
- **请求参数**: 路径参数id为用户ID
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": null
  }
  ```

#### 6. 修改用户密码
- **URL**: `/api/sys/users/{id}/change-password`
- **方法**: POST
- **请求参数**:
  ```json
  {
    "oldPassword": "原密码",
    "newPassword": "新密码"
  }
  ```
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": null
  }
  ```

#### 7. 重置用户密码
- **URL**: `/api/sys/users/{id}/reset-password`
- **方法**: POST
- **请求参数**: 无
- **响应结果**:
  ```json
  {
    "success": true,
    "message": "操作成功",
    "data": {
      "password": "重置后的密码"
    }
  }
  ```

## 前端调用示例

### 登录接口调用
```javascript
// 使用axios发送请求
axios.post('/api/auth/login', {
  username: 'admin',
  password: '123456'
})
.then(response => {
  if (response.data.success) {
    // 保存token到localStorage
    localStorage.setItem('token', response.data.data.token);
    // 设置默认请求头
    axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.data.token}`;
    // 跳转到首页
    router.push('/dashboard');
  }
})
.catch(error => {
  console.error('登录失败:', error);
});
```

### 获取用户信息调用
```javascript
// 在请求头中添加Authorization
axios.get('/api/auth/info')
.then(response => {
  if (response.data.success) {
    // 保存用户信息
    store.commit('setUserInfo', response.data.data);
  }
})
.catch(error => {
  console.error('获取用户信息失败:', error);
});
``` 