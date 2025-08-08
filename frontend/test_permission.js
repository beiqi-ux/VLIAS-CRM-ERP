// 模拟权限检查逻辑
function extractSubmoduleCode(permissionCode) {
  if (!permissionCode || permissionCode.trim() === '') {
    return null
  }
  const code = permissionCode.trim()
  if (code.includes(':')) {
    return code.split(':')[0]
  }
  return null
}

function getPermissionType(permissionCode) {
  if (!permissionCode || permissionCode.trim() === '') {
    return null
  }
  const code = permissionCode.trim()
  const parts = code.split(':')
  return parts.length
}

function hasPermissionWithInheritance(userPermissions, requiredPermission) {
  if (!userPermissions || userPermissions.length === 0 || 
      !requiredPermission || requiredPermission.trim() === '') {
    return false
  }

  const required = requiredPermission.trim()
  
  // 1. 直接权限检查
  if (userPermissions.includes(required)) {
    return true
  }
  
  // 2. 继承权限检查
  const permissionType = getPermissionType(required)
  
  if (!permissionType) {
    return false
  }
  
  switch (permissionType) {
    case 3: // 三级权限，检查是否有二级或一级权限
      const submoduleCode = extractSubmoduleCode(required)
      if (submoduleCode && userPermissions.includes(submoduleCode)) {
        return true
      }
      break
  }
  
  return false
}

// 测试用例
const userPermissions = ['org-management:view'];
const tests = [
  'org-management:view',
  'org-management:create', 
  'org-management:edit',
];

console.log('用户权限:', userPermissions);
console.log('');

tests.forEach(permission => {
  const result = hasPermissionWithInheritance(userPermissions, permission);
  const submodule = extractSubmoduleCode(permission);
  console.log('检查', permission, ':', result);
  console.log('  提取的子模块:', submodule);
  console.log('  用户是否有子模块权限:', userPermissions.includes(submodule));
  console.log('');
});
