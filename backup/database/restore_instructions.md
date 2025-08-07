# 数据库备份与恢复说明

## 备份信息
- **备份时间**: 2025-01-05 22:15:18
- **数据库名**: vliascrm
- **备份文件**: vliascrm_backup_20250805_221518.sql
- **备份类型**: 完整备份（包括数据、结构、存储过程、触发器）

## 恢复方法

### 方法1: 完全恢复（推荐）
```bash
# 1. 删除现有数据库（谨慎操作！）
mysql -u root -pqichengxu123 -e "DROP DATABASE IF EXISTS vliascrm;"

# 2. 创建新数据库
mysql -u root -pqichengxu123 -e "CREATE DATABASE vliascrm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 3. 恢复数据
mysql -u root -pqichengxu123 vliascrm < backup/database/vliascrm_backup_20250805_221518.sql
```

### 方法2: 快速恢复脚本
```bash
# 运行恢复脚本
./backup/database/quick_restore.sh
```

## 注意事项
1. 恢复前请确保应用程序已停止
2. 恢复过程会覆盖所有现有数据
3. 建议在测试环境先验证恢复效果
4. 如果只需要恢复特定表，请联系管理员

## 验证恢复结果
```bash
# 检查数据库是否正常
mysql -u root -pqichengxu123 vliascrm -e "SHOW TABLES;"

# 检查关键表的数据量
mysql -u root -pqichengxu123 vliascrm -e "
SELECT 
    TABLE_NAME,
    TABLE_ROWS
FROM information_schema.TABLES 
WHERE TABLE_SCHEMA = 'vliascrm' 
ORDER BY TABLE_ROWS DESC;
"
``` 