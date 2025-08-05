import bcrypt
import requests
import json

print("=== 密码哈希问题调试 ===\n")

# 1. 测试数据库中当前的哈希
current_hash = "$2b$12$55luDH4HHocFgkSKmYbGn.//MgTE4coOqhGqwYTiFmQUNluUCUZNm"
password = "123456789"

print("1. 当前数据库密码验证:")
print(f"密码: {password}")
print(f"哈希: {current_hash}")

# BCrypt验证
hash_bytes = current_hash.encode('utf-8')
password_bytes = password.encode('utf-8')
matches = bcrypt.checkpw(password_bytes, hash_bytes)
print(f"验证结果: {matches} ✅" if matches else f"验证结果: {matches} ❌")

print("\n" + "="*50)

# 2. 测试前端登录请求
print("2. 测试前端登录请求:")
login_data = {
    "username": "123456789",
    "password": "123456789",
    "rememberMe": True
}

print(f"发送数据: {json.dumps(login_data, indent=2)}")
print("前端是否对密码进行加密: NO (直接发送明文)")

print("\n" + "="*50)

# 3. 分析Spring Security流程
print("3. Spring Security验证流程分析:")
print("步骤1: 前端发送明文密码 '123456789'")
print("步骤2: SysLoginServiceImpl.login() 接收 LoginDTO")
print("步骤3: AuthenticationManager.authenticate() 调用")
print("步骤4: SysUserDetailsService.loadUserByUsername() 查询用户")
print("步骤5: 返回的SysUser对象包含数据库中的哈希密码")
print("步骤6: Spring Security的DaoAuthenticationProvider 比较:")
print(f"       - 输入密码: '{password}' (明文)")
print(f"       - 数据库哈希: '{current_hash}'")
print("步骤7: 使用BCryptPasswordEncoder.matches() 验证")

print("\n" + "="*50)

# 4. 验证BCryptPasswordEncoder的行为
print("4. BCryptPasswordEncoder 验证测试:")
print("这模拟了Spring Security内部的验证过程:")

# 模拟Spring的BCryptPasswordEncoder
try:
    # 直接使用bcrypt验证（Spring内部也是这样做的）
    result = bcrypt.checkpw(password_bytes, hash_bytes)
    print(f"bcrypt.checkpw('{password}', '{current_hash}') = {result}")
    
    if result:
        print("✅ 密码验证应该成功")
    else:
        print("❌ 密码验证失败")
        
except Exception as e:
    print(f"❌ 验证过程出错: {e}")

print("\n" + "="*50)

# 5. 检查可能的问题
print("5. 可能的问题检查:")

# 检查哈希格式
if current_hash.startswith('$2b$'):
    print("✅ 哈希格式正确 ($2b$ BCrypt)")
else:
    print("❌ 哈希格式异常")

# 检查哈希长度
if len(current_hash) == 60:
    print("✅ 哈希长度正确 (60字符)")
else:
    print(f"❌ 哈希长度异常: {len(current_hash)} 字符")

# 检查密码长度
if len(password) == 9:
    print("✅ 密码长度正确 (9字符)")
else:
    print(f"❌ 密码长度异常: {len(password)} 字符")

print("\n" + "="*50)
print("6. 结论:")
if matches:
    print("✅ 密码哈希完全正确，登录应该成功")
    print("如果登录仍然失败，问题可能在于:")
    print("   - Spring Security配置问题")
    print("   - 用户状态问题 (status != 1)")
    print("   - 权限加载问题")
    print("   - 其他业务逻辑问题")
else:
    print("❌ 密码哈希不匹配，这是登录失败的根本原因")
    print("需要重新生成正确的密码哈希") 