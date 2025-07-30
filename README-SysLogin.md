# 系统登录功能实现说明

## 功能概述

本模块实现了基于JWT令牌的用户登录认证功能，包括：
- 用户登录接口
- 登出接口
- 令牌刷新接口
- 获取当前用户信息接口

## 实现结构

### 实体类 (Entity)
- `SysUser`: 系统用户实体，实现Spring Security的UserDetails接口

### 数据传输对象 (DTO)
- `LoginDTO`: 登录请求DTO，包含用户名、密码等信息
- `LoginResponseDTO`: 登录响应DTO，包含用户信息和JWT令牌

### 数据访问层 (Repository)
- `SysUserRepository`: 系统用户数据访问接口，提供用户查询和更新操作

### 服务层 (Service)
- `SysLoginService`: 登录服务接口
- `SysLoginServiceImpl`: 登录服务实现类，处理登录验证、令牌生成等业务逻辑

### 控制层 (Controller)
- `SysLoginController`: 登录相关接口控制器，处理HTTP请求

## API接口说明

### 登录
- 路径: `/api/auth/login`
- 方法: `POST`
- 请求体:
```json
{
  "username": "用户名",
  "password": "密码",
  "captcha": "验证码(可选)",
  "captchaKey": "验证码KEY(可选)",
  "rememberMe": true
}
```
- 响应:
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

### 登出
- 路径: `/api/auth/logout`
- 方法: `POST`
- 响应:
```json
{
  "success": true,
  "message": "操作成功",
  "data": null
}
```

### 刷新令牌
- 路径: `/api/auth/refresh`
- 方法: `POST`
- 参数: `token=旧令牌`
- 响应: 与登录接口相同

### 获取当前用户信息
- 路径: `/api/auth/info`
- 方法: `GET`
- 请求头: `Authorization: Bearer {token}`
- 响应: 与登录接口相同，但不包含token字段

## 前端集成

### 前端集成步骤
1. 发送登录请求到`/api/auth/login`获取令牌
2. 将令牌存储在前端（如localStorage或Cookie）
3. 在后续请求中添加认证头: `Authorization: Bearer {token}`
4. 根据令牌过期时间，在适当的时候调用`/api/auth/refresh`刷新令牌

### Vue示例代码
```javascript
// 登录请求
async login(username, password) {
  try {
    const response = await axios.post('/api/auth/login', {
      username,
      password
    });
    
    if (response.data.success) {
      const token = response.data.data.token;
      // 存储令牌
      localStorage.setItem('token', token);
      // 设置默认请求头
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      
      return response.data.data;
    } else {
      throw new Error(response.data.message);
    }
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
}

// 获取用户信息
async getUserInfo() {
  try {
    const response = await axios.get('/api/auth/info');
    return response.data.data;
  } catch (error) {
    console.error('获取用户信息失败:', error);
    throw error;
  }
}

// 登出
async logout() {
  try {
    await axios.post('/api/auth/logout');
    // 清除令牌
    localStorage.removeItem('token');
    delete axios.defaults.headers.common['Authorization'];
  } catch (error) {
    console.error('登出失败:', error);
  }
}
```

## 安全配置

要使上述功能正常工作，需要在Spring Security配置中设置：
1. JWT过滤器，用于验证请求中的令牌
2. 认证管理器Bean，用于验证用户名密码
3. 开放登录相关接口的访问权限

参考 `SecurityConfig` 和 `JwtAuthenticationFilter` 类的实现。 